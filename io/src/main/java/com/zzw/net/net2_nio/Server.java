package com.zzw.net.net2_nio;

import com.zzw.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Server
{

    /**
     * 非阻塞 IO
     */
    public static void main(String[] args) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 服务器
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));
        server.configureBlocking(false); // 非阻塞

        List<SocketChannel> clients    = new ArrayList<>();
        List<SocketChannel> needRemove = new ArrayList<>();
        while (true)
        {
            // accept() 非阻塞
            SocketChannel client = server.accept();
            if (client != null)
            {
                client.configureBlocking(false);
                clients.add(client);
                log.warn("accept after {} ...", client);
            }

            for (SocketChannel socket : clients)
            {
                // read() 非阻塞
                int read = socket.read(buffer);
                if (read == -1)
                {
                    needRemove.add(socket);
                    log.info("read close {} ...\n", socket);
                }
                else if (read > 0)
                {
                    buffer.flip();
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear();
                    log.info("read after {} ...\n", socket);
                }
            }

            // 注意: 在这里进行删除
            clients.removeAll(needRemove);
        }
    }
}
