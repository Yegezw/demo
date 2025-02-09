package com.zzw.net.net2_nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.LockSupport;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
        socket.write(ByteBuffer.wrap("hello world nio".getBytes()));
        socket.close();

        System.out.println();
        LockSupport.park();
    }
}
