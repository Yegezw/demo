package com.zzw.eg;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer
{

    private static final AttributeKey<String>  SERVER_GLOBAL_ATTR_KEY     = AttributeKey.valueOf("serverGlobalAttr");
    private static final AttributeKey<Integer> CLIENT_CONNECTION_TYPE_KEY = AttributeKey.valueOf("clientType");

    public static void main(String[] args) throws InterruptedException
    {
        EventLoopGroup bossGroup   = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .attr(SERVER_GLOBAL_ATTR_KEY, "NettyServer-1.0") // 设置全局属性 ssc
                    .handler(new LoggingHandler(LogLevel.INFO))      // ssc 日志

                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childAttr(CLIENT_CONNECTION_TYPE_KEY, 1)  // 设置客户端连接属性 sc
                    .childHandler(
                            new ChannelInitializer<SocketChannel>()
                            {
                                @Override
                                protected void initChannel(SocketChannel channel)
                                {
                                    String  serverVersion = channel.parent().attr(SERVER_GLOBAL_ATTR_KEY).get();
                                    Integer clientType    = channel.attr(CLIENT_CONNECTION_TYPE_KEY).get();

                                    System.out.println("Server Version: " + serverVersion);
                                    System.out.println("Client Type: " + clientType);

                                    ChannelPipeline pipeline = channel.pipeline();
                                    pipeline.addLast(new LoggingHandler(LogLevel.INFO)); // sc 日志
                                    pipeline.addLast(new StringDecoder());
                                    pipeline.addLast(new StringEncoder());
                                    pipeline.addLast(new ServerHandler());
                                }
                            }
                    );

            ChannelFuture future = bootstrap.bind(8080).sync();
            System.out.println("Server started on port 8080");
            future.channel().closeFuture().sync();
        }
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    static class ServerHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
        {
            Integer clientType = ctx.channel().attr(CLIENT_CONNECTION_TYPE_KEY).get();
            System.out.println("[ClientType=" + clientType + "] Received: " + msg);
            ChannelFuture future = ctx.writeAndFlush("Hello, Client!");
            future.addListener(
                    (ChannelFuture f) ->
                    {
                        if (f.isSuccess()) System.out.println("Message sent successfully");
                        else System.err.println("Failed to send message: " + f.cause());
                    }
            );
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        {
            ctx.close();
            log.error("Exception caught: ", cause);
        }
    }
}
