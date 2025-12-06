package com.zzw.simple;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server
{

    public static void main(String[] args) throws Exception
    {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);

        Selector     selector     = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);

        NioEventLoop[] workerGroup = new NioEventLoop[2];
        workerGroup[0] = new NioEventLoop();
        workerGroup[1] = new NioEventLoop();

        int i = 0;
        while (true)
        {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext())
            {
                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable())
                {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                    workerGroup[(i++) % 2].register(socketChannel);
                    socketChannel.write(ByteBuffer.wrap("服务端发送成功了".getBytes()));
                }
            }
        }
    }
}
