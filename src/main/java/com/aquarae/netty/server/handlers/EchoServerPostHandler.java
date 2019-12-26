package com.aquarae.netty.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
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
          .addListener(future -> {
              if (future.isSuccess()) {
                  System.out.println("\nSend success!");
              } else {
                  System.out.println("\nSend failed: " + future.cause());
              }
          });

        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
