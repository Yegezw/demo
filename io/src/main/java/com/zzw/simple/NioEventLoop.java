package com.zzw.simple;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
@Getter
public class NioEventLoop extends SingleThreadEventLoop
{

    private final Selector selector;

    @SneakyThrows
    public NioEventLoop()
    {
        this.selector = Selector.open();
    }

    // ------------------------------------------------

    @Override
    public void run()
    {
        while (true)
        {
            doRun();
            runAllTasks();
        }
    }

    @SneakyThrows
    private void doRun()
    {
        select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext())
        {
            processSelectedKey(it.next());
            it.remove();
        }
    }

    private void select() throws IOException
    {
        for (; ; )
        {
            int num = selector.select(3000);
            if (num != 0 || hasTasks()) break;
        }
    }

    // ------------------------------------------------

    private void processSelectedKey(SelectionKey key) throws IOException
    {
        if (!key.isReadable()) return;

        SocketChannel channel    = (SocketChannel) key.channel();
        ByteBuffer    byteBuffer = ByteBuffer.allocate(1024);

        int len = channel.read(byteBuffer);
        if (len == -1)
        {
            channel.close();
            return;
        }

        byte[] bytes = new byte[len];
        byteBuffer.flip();
        byteBuffer.get(bytes);
        log.info("新线程收到客户端发送的数据: {}", new String(bytes));
    }
}
