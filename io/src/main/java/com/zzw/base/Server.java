package com.zzw.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server
{

    public static void main(String[] args) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(32);

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8888));

        SocketChannel client = server.accept();

        // -----------------------------------------------------------

        // 读取客户端发送过来的数据
        while (true)
        {
            int read = client.read(buffer);
            if (read != -1)
            {
                buffer.flip();
                String str = new String(buffer.array(), buffer.position(), buffer.limit());
                System.out.println(str);
                buffer.clear();
            }
            else
            {
                // 客户端需要关闭了
                buffer.put("我知道你要关闭了".getBytes());
                buffer.flip(); // 切换为读模式才能发送
                client.write(buffer);

                // 准备关闭连接, 发送服务器 FIN 报文
                client.shutdownOutput();
                break;
            }
        }

        //  -----------------------------------------------------------

        server.close();
    }
}
