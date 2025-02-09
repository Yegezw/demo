package com.zzw.net.net1_bio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

public class Client
{

    public static void main(String[] args) throws IOException
    {
        SocketChannel socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
        socket.write(ByteBuffer.wrap("hello world bio".getBytes()));
        socket.close();

        System.out.println();
        LockSupport.park();
    }

    @Test
    public void test() throws UnknownHostException
    {
        InetAddress localHost = InetAddress.getLocalHost();

        String hostName          = localHost.getHostName();          // zzw
        byte[] address           = localHost.getAddress();           // [-64, -88, 1, 4] -128 ~ 127
        String hostAddress       = localHost.getHostAddress();       // 192.168.1.4
        String canonicalHostName = localHost.getCanonicalHostName(); // zzw

        System.out.println(hostName);
        System.out.println(Arrays.toString(address));
        System.out.println(hostAddress);
        System.out.println(canonicalHostName);
    }
}
