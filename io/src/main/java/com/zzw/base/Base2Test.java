package com.zzw.base;

import com.zzw.util.ByteBufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Base2Test
{

    @Test
    public void test1()
    {
        String str1 = "hello";

        // 通过 StandardCharsets 的 encode 方法获得 ByteBuffer
        // 此时获得的 ByteBuffer 为读模式, 无需通过 flip 切换模式
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(str1);
        ByteBufferUtil.debugAll(buffer);

        // 将缓冲区中的数据转化为字符串
        // 通过 StandardCharsets 解码, 获得 CharBuffer, 再通过 toString 获得字符串
        String str2 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str2);
        ByteBufferUtil.debugAll(buffer);
    }

    @Test
    public void test2()
    {
        // 准备两个字符串
        String str1 = "hello";

        // 通过 ByteBuffer 的 wrap 方法获得 ByteBuffer
        // 此时获得的 ByteBuffer 为读模式, 无需通过 flip 切换模式
        ByteBuffer buffer = ByteBuffer.wrap(str1.getBytes());
        ByteBufferUtil.debugAll(buffer);

        // 将缓冲区中的数据转化为字符串
        // 通过 StandardCharsets 解码, 获得 CharBuffer, 再通过 toString 获得字符串
        String str2 = StandardCharsets.UTF_8.decode(buffer).toString();
        System.out.println(str2);
        ByteBufferUtil.debugAll(buffer);
    }

    @Test
    public void test3()
    {
        ByteBuffer buffer = ByteBuffer.allocate(32);

        buffer.put("Hello, world\nI'm Lisi\nHo".getBytes());
        split(buffer);

        buffer.put("w are you?\n".getBytes());
        split(buffer);

        // 注意: hasRemaining 前一定要切换为读模式
        buffer.flip();
        System.out.println("是否还有剩余: " + buffer.hasRemaining());
        ByteBufferUtil.debugAll(buffer);
    }

    private void split(ByteBuffer buffer)
    {
        // 切换为读模式
        buffer.flip();

        for (int i = 0; i < buffer.limit(); i++)
        {
            if (buffer.get(i) == '\n')
            {
                int len = i - buffer.position() + 1;

                byte[] bytes = new byte[len];
                buffer.get(bytes);

                System.out.print(new String(bytes));
            }
        }

        // 切换为写模式
        // 但是缓冲区可能未读完, 这里需要使用 compact
        buffer.compact();
    }
}
