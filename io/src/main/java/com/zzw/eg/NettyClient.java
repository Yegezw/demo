package com.zzw.eg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient
{

    public static void main(String[] args) throws InterruptedException
    {
        EventLoopGroup group = new NioEventLoopGroup();

        try
        {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(
                            new ChannelInitializer<SocketChannel>()
                            {
                                @Override
                                protected void initChannel(SocketChannel channel)
                                {
                                    ChannelPipeline pipeline = channel.pipeline();
                                    pipeline.addLast(new StringDecoder());
                                    pipeline.addLast(new StringEncoder());
                                    pipeline.addLast(new ClientHandler());
                                }
                            }
                    );
            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
            future.channel().closeFuture().sync();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }

    static class ClientHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelActive(ChannelHandlerContext ctx)
        {
            ctx.writeAndFlush("Hello, Server!");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
        {
            System.out.println("Received: " + msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        {
            ctx.close();
            log.error("Exception caught: ", cause);
        }
    }
}
