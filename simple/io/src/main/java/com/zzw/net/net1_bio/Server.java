package com.zzw.net.net1_bio;

import com.zzw.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class Server
{

    /**
     * 阻塞 IO
     */
    public static void main(String[] args) throws IOException
    {
        // 服务器
        // 没有显式指定 IP 地址, 仅指定了端口号
        // 在这种情况下, 默认监听的 IP 地址是 0.0.0.0, 所有本地 IP 地址
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));

        // 客户端 + 处理客户端的线程
        Set<SocketChannel> clients = new HashSet<>();
        Set<Thread>        threads = new HashSet<>();

        while (true)
        {
            log.warn("accept before ...");

            SocketChannel client = server.accept();
            clients.add(client);
            Thread thread = new Thread(
                    () ->
                    {
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        while (true)
                        {
                            int read;
                            try
                            {
                                read = client.read(buffer);
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException(e);
                            }
                            if (read == -1)
                            {
                                log.info("read close {} ...\n", client);
                                break;
                            }
                            else if (read > 0)
                            {
                                buffer.flip();
                                ByteBufferUtil.debugRead(buffer);
                                buffer.clear();
                                log.info("read after {} ...\n", client);
                            }
                        }
                        clients.remove(client);
                        threads.remove(Thread.currentThread());
                    }
            );
            thread.start();
            threads.add(thread);

            log.warn("accept after {} ...", client);
        }
    }
}
