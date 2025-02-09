package com.zzw.base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(32);

        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));

        // -----------------------------------------------------------

        // 客户端发送数据
        buffer.put("hello".getBytes());
        buffer.flip(); // 必须切换为读模式才能发送
        socket.write(buffer);
        buffer.clear();

        // -----------------------------------------------------------

        // 客户端不再发送数据
        // 准备关闭连接, 发送客户端 FIN 报文
        socket.shutdownOutput();

        // -----------------------------------------------------------

        // 客户端还可以接收数据
        while (true)
        {
            int read = socket.read(buffer);
            if (read != -1)
            {
                buffer.flip();
                String str = new String(buffer.array(), buffer.position(), buffer.limit());
                System.out.println(str);
                buffer.clear();
            }
            else
            {
                // 服务器需要关闭了
                break;
            }
        }
        socket.close();
    }
}
