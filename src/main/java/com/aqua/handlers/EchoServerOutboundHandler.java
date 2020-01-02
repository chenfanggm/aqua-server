package com.aqua.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class EchoServerOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf out = ((ByteBuf) msg);
        out.writeBytes("\nMore content is appended from outbound handler!".getBytes());

        System.out.println("\nHere's the out message: ");
        for (int i=0; i < out.capacity(); i++) {
            System.out.print((char) out.getByte(i));
            System.out.flush();
        }
        super.write(ctx, out, promise);
    }
}
