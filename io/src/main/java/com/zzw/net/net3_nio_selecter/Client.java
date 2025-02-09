package com.zzw.net.net3_nio_selecter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));

        socket.write(ByteBuffer.wrap("hello, my name is zhang san, how are you?\n".getBytes()));
        socket.write(ByteBuffer.wrap("如果是这样的输入呢\n".getBytes()));

        System.out.println();
        socket.close();
    }
}
