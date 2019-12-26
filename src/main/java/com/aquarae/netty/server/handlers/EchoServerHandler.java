package com.aquarae.netty.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = ((ByteBuf) msg).copy();
        while (in.isReadable()) { // (1)
            System.out.print((char) in.readByte());
            System.out.flush();
        }
        ctx.write(msg);
        ctx.fireChannelRead(msg);
//        throw new Exception("My testing exception!");
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
//        System.out.println("Caught exceptions in echo server handler!");
//        cause.printStackTrace();
//        ctx.close();
//    }
}
