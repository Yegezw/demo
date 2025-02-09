package com.zzw.net.net3_nio_selecter;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class Server
{

    /*
     * 读事件: 0000 0001
     * public static final int OP_READ = 1 << 0;
     *
     * 写事件: 0000 0100
     * public static final int OP_WRITE = 1 << 2;
     *
     * 连接事件: 0000 1000, 用于 Client 端
     * public static final int OP_CONNECT = 1 << 3;
     *
     * 接收事件: 0001 0000, 仅 ServerSocketChannel 支持
     * public static final int OP_ACCEPT = 1 << 4;
     */

    /**
     * 非阻塞 IO + IO 多路复用
     */
    public static void main(String[] args) throws IOException
    {
        Selector selector = Selector.open();

        // 服务器
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));
        server.configureBlocking(false); // 非阻塞
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true)
        {
            // 阻塞, 直到监听的 Channel 可用
            // 水平模式 LT, 每次有事件都会通知
            int ready = selector.select(10000); // 10 s
            log.error("selector ready counts is {}", ready);

            if (ready == 0)
            {
                close(selector);
                break;
            }
            else
            {
                processor(selector);
            }
        }
    }

    private static void close(Selector selector) throws IOException
    {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys)
        {
            key.channel().close();
        }
        selector.close();
        log.error("selector ready counts is 0, server close {} channel", keys.size());
    }

    private static void processor(Selector selector) throws IOException
    {
        Set<SelectionKey>      keys = selector.selectedKeys();
        Iterator<SelectionKey> it   = keys.iterator();
        while (it.hasNext())
        {
            SelectionKey key = it.next();

            // 有客户端连接到来
            if (key.isAcceptable())
            {
                processAccept(key);
            }

            // 客户端有数据到来
            else if (key.isReadable())
            {
                processRead(key);
            }

            it.remove(); // 处理完毕后必须移除
        }
    }

    // ------------------------------------------------------------------------------

    private static void processAccept(SelectionKey key) throws IOException
    {
        Selector            selector = key.selector();
        ServerSocketChannel server   = (ServerSocketChannel) key.channel();
        SocketChannel       client   = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(64));
        log.warn("accept after {}\n", client);
    }

    private static void processRead(SelectionKey key) throws IOException
    {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer    buffer = (ByteBuffer) key.attachment();

        while (true)
        {
            int read;
            try
            {
                read = client.read(buffer);
            }
            catch (IOException e)
            {
                // 远程主机强迫关闭了一个现有的连接
                log.info(e.getMessage());
                key.cancel();   // 解除 Selector 对该 Channel 的监控
                client.close(); // 关闭 Channel
                break;
            }

            if (read == -1)
            {
                key.cancel();   // 解除 Selector 对该 Channel 的监控
                client.close(); // 关闭 Channel
                break;
            }
            else if (read > 0)
            {
                buffer.flip();
                String str = new String(buffer.array(), buffer.position(), buffer.limit());
                System.out.print(str);
                buffer.clear();
            }
            else if (read == 0)
            {
                break;          // 本次读取事件处理完毕
            }
        }

        if (client.isOpen())
        {
            log.info("read after {}\n", client);
        }
        else
        {
            log.info("read close {}\n", client);
        }
    }
}
