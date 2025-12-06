package com.zzw.net.net4_aio;

import com.zzw.util.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class AIO
{

    /**
     * 异步 IO、由守护线程做回调
     */
    public static void main(String[] args) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        Path                    path1   = Paths.get("F:\\test-file\\in");
        Path                    path2   = Paths.get("F:\\test-file\\in.txt");
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(path2, StandardOpenOption.READ);

        log.info("read begin");
        channel.read(buffer, 0, buffer,
                new CompletionHandler<>()
                {
                    @Override
                    public void completed(Integer count, ByteBuffer attachment)
                    {
                        log.info("read completed {}", count);
                        attachment.flip();
                        ByteBufferUtil.debugRead(attachment);
                        attachment.clear();
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment)
                    {
                        log.info("read failed");
                    }
                }
        );
        log.info("read end");

        LockSupport.park(); // 主线程阻塞
    }
}
