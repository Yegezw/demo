package com.zzw.simple;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Slf4j
public abstract class SingleThreadEventLoop extends SingleThreadEventExecutor
{

    public SingleThreadEventLoop()
    {
    }

    public void register(SocketChannel socketChannel)
    {
        if (inEventLoop(Thread.currentThread())) doRegister(socketChannel);
        else execute(() -> doRegister(socketChannel));
    }

    @SneakyThrows
    private void doRegister(SocketChannel channel)
    {
        Selector selector = getSelector();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

    protected abstract Selector getSelector();
}
