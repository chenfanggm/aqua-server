package com.aqua.server;

import com.aqua.handlers.EchoServerPostHandler;
import com.aqua.handlers.EchoServerHandler;
import com.aqua.handlers.EchoServerOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


public class HttpServer {

    private Integer port;

    public HttpServer() {}

    public HttpServer(int port) {
        this.port = port;
    }

    public CompletionStage listen(Integer port) {
        if (null != port) {
            this.port = port;
        }

        if (null == this.port) {
            throw new IllegalArgumentException("Port is not defined for starting a HTTP server.");
        }

        return bootStrapUnderlyingServer(this.port);
    }

    private CompletionStage<Channel> bootStrapUnderlyingServer(int port) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        final ServerBootstrap bootstrap = new ServerBootstrap(); // (2)
        bootstrap.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class) // (3)
          .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
              @Override
              public void initChannel(SocketChannel ch) {
                  ch.pipeline().addLast(new EchoServerOutboundHandler());
                  ch.pipeline().addLast(new EchoServerHandler());
                  ch.pipeline().addLast(new EchoServerPostHandler());
              }
          })
          .option(ChannelOption.SO_BACKLOG, 128)          // (5)
          .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

        // Bind and start to accept incoming connections.
        final CompletableFuture bootStrapFuture = new CompletableFuture();

        final ChannelFuture channelFuture = bootstrap.bind(port); // (7)
        channelFuture.addListener((res) -> {
            if (res.isSuccess()) {
                bootStrapFuture.complete(channelFuture.channel());
            } else {
                bootStrapFuture.completeExceptionally(res.cause());
            }
        });

        return bootStrapFuture;
    }
}
