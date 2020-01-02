package com.aqua.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class EchoServerPostHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf in = ((ByteBuf) msg);
//        in.writeBytes("\nAdd some bytes from post inbound handler!".getBytes());
        System.out.println("\nInside of inbound post handler: ");
        ByteBuf newOut = ctx.alloc().buffer();
        newOut.writeBytes("\nI'm the King!".getBytes());
        ctx.write(newOut);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.write(Unpooled.copiedBuffer("\nI'm the Queue!", CharsetUtil.UTF_8))
          .addListener((ChannelFutureListener) future -> {
              if (future.isSuccess()) {
                  System.out.println("\nSend success!");
              } else {
                  System.out.println("\nSend failed: " + future.cause());
                  future.cause().printStackTrace();
                  future.channel().close();
              }
          });

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        System.out.println("Caught exceptions in POST echo server handler!");
        cause.printStackTrace();
        ctx.close();
    }
}
