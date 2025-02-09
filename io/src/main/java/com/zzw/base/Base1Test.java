package com.zzw.base;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Base1Test
{

    /*
     * Buffer  的本质是缓存区
     * Channel 的本质是 fd 和操作 fd 的函数
     */

    // ------------------------------------------------------------------------------

    /*
     * 间接缓存: 创建 / 销毁快、操作慢
     * 直接缓存: 创建 / 销毁慢、操作快
     */

    /**
     * 0 <= mark <= position <= limit <= capacity
     */
    @Test
    public void test1()
    {
        String str = "abcde";

        // 分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("---------allocate---------");
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 1024
        System.out.println(byteBuffer.position());   // 0

        // 利用 put() 存入数据到缓冲区中
        byteBuffer.put(str.getBytes());
        System.out.println("------------put-----------");
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 1024
        System.out.println(byteBuffer.position());   // 5

        // 切换到读数据模式
        byteBuffer.flip();
        System.out.println("-----------flip-----------");
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 5, limit 表示可以操作数据的大小, 只有 5 个字节的数据给你读, 所以可操作数据大小是 5
        System.out.println(byteBuffer.position());   // 0, 读数据要从第 0 个位置开始读

        // 利用 get() 读取缓冲区中的数据
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        System.out.println("------------get-----------");
        System.out.println(new String(data));
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 5, 可以读取数据的大小依然是 5 个
        System.out.println(byteBuffer.position());   // 5, 读完之后位置变到了第 5 个

        // rewind() 可重复读, 这个方法调用完后, 又变成了读模式
        byteBuffer.rewind();
        System.out.println("----------rewind----------");
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 5
        System.out.println(byteBuffer.position());   // 0

        // clear() 清空缓冲区并进入写模式, 虽然缓冲区被清空了, 但是缓冲区中的数据依然存在, 只是处于 "被遗忘" 状态
        // 解释: 缓冲区中的界限、位置等信息都被置为最初的状态了, 所以你无法再根据这些信息找到原来的数据了, 原来数据就处于 "被遗忘" 状态
        byteBuffer.clear();
        System.out.println("----------clear-----------");
        System.out.println(byteBuffer.capacity());   // 1024
        System.out.println(byteBuffer.limit());      // 1024
        System.out.println(byteBuffer.position());   // 0
    }

    /**
     * 0 <= mark <= position <= limit <= capacity
     */
    @Test
    public void test2()
    {
        String     str        = "abcde";
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        // 切换到读数据模式
        byteBuffer.flip();
        System.out.println("-----------flip-----------");
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data, 0, 2);
        System.out.println(new String(data, 0, 2)); // ab
        System.out.println(byteBuffer.position());               // 2

        // 标记一下当前 position 的位置 2
        byteBuffer.mark();

        System.out.println("------------get-----------");
        byteBuffer.get(data, 2, 2);
        System.out.println(new String(data, 2, 2)); // cd
        System.out.println(byteBuffer.position());               // 4

        // reset() 恢复到 mark 的位置 2
        byteBuffer.reset();
        System.out.println("-----------reset----------");
        System.out.println(byteBuffer.position());      // 2

        // 判断缓冲区中是否还有剩余数据
        if (byteBuffer.hasRemaining())
        {
            // 获取缓冲区中可以操作的数量
            System.out.println(byteBuffer.remaining()); // 5 - 2 = 3
        }
    }

    /**
     * 直接缓冲区
     */
    @Test
    public void test3()
    {
        // 分配直接缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect()); // 判断是直接缓冲区还是非直接缓冲区: true
        byteBuffer.clear(); // 清空缓冲区, 并进入写模式
    }

    // ------------------------------------------------------------------------------

    /**
     * 利用通道完成文件的复制(非直接缓冲区)
     */
    @Test
    public void test4() throws Exception
    {
        FileInputStream  in  = new FileInputStream("F:\\test-file\\in.txt");
        FileOutputStream out = new FileOutputStream("F:\\test-file\\out.txt");

        // 获取通道
        FileChannel inChannel  = in.getChannel();
        FileChannel outChannel = out.getChannel();

        // 通道没有办法传输数据, 必须依赖缓冲区, 分配指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 将通道中的数据存入缓冲区中, inChannel 中的数据读到 byteBuffer 缓冲区中
        while (inChannel.read(byteBuffer) != -1)
        {
            byteBuffer.flip();            // 切换成读数据模式
            outChannel.write(byteBuffer); // 将缓冲区中的数据写入通道
            byteBuffer.clear();           // 清空缓冲区, 并进入写模式
        }

        outChannel.close();
        inChannel.close();
        out.close();
        in.close();
    }

    /**
     * 使用直接缓冲区完成文件的复制(mmap)
     */
    @Test
    public void test5() throws Exception
    {
        /*
         * 使用 open 方法来获取通道
         * 需要两个参数
         * 参数 1: Path 是 JDK 1.7 以后给我们提供的一个类, 代表文件路径
         * 参数 2: Option 就是针对这个文件想要做什么样的操作
         *        -- StandardOpenOption.READ:   读模式
         *        -- StandardOpenOption.WRITE:  写模式
         *        -- StandardOpenOption.CREATE: 如果文件不存在就创建
         *        -- StandardOpenOption.APPEND: 追加
         */
        FileChannel inChannel = FileChannel.open(
                Paths.get("F:\\test-file\\in.txt"),
                StandardOpenOption.READ
        );
        FileChannel outChannel = FileChannel.open(
                Paths.get("F:\\test-file\\out.txt"),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE
        );

        /*
         * 内存映射文件
         * 这种方式缓冲区是直接建立在物理内存之上的
         * 所以我们就不需要通道了
         */
        MappedByteBuffer inMapped = inChannel.map(
                FileChannel.MapMode.READ_ONLY,
                0,
                inChannel.size()
        );
        MappedByteBuffer outMapped = outChannel.map(
                FileChannel.MapMode.READ_WRITE,
                0,
                inChannel.size()
        );

        // 直接对缓冲区进行数据的读写操作
        outMapped.put(inMapped);

        inChannel.close();
        outChannel.close();
    }

    /**
     * 利用零拷贝完成文件复制
     */
    @Test
    public void test6() throws Exception
    {
        /*
         * 通道之间的数据传输(零拷贝), 一次最大传输 2G
         * transferFrom
         * transferTo
         */
        FileChannel inChannel = FileChannel.open(
                Paths.get("F:\\test-file\\in"),
                StandardOpenOption.READ
        );
        FileChannel outChannel = FileChannel.open(
                Paths.get("F:\\test-file\\out"),
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );

        final int BUFFER_SIZE = 1024 * 1024 * 1024; // 1 GB
        long      size        = inChannel.size();
        long      left        = 0;
        while (left < size)
        {
            long remaining     = size - left;
            long transferCount = Math.min(BUFFER_SIZE, remaining);
            left += inChannel.transferTo(left, transferCount, outChannel);
        }

        inChannel.close();
        outChannel.close();
    }

    // ------------------------------------------------------------------------------

    /**
     * 分散读取与聚集写入
     */
    @Test
    public void test7() throws Exception
    {
        // FileInputStream  只能顺序读取文件
        // RandomAccessFile 可以随机访问文件中的任意位置
        // FileInputStream in      = new FileInputStream("F:\\test-file\\in.txt");
        RandomAccessFile in        = new RandomAccessFile("F:\\test-file\\in.txt", "rw");
        FileChannel      inChannel = in.getChannel(); // 获取通道

        // 分配指定大小缓冲区
        ByteBuffer   buf1 = ByteBuffer.allocate(2);
        ByteBuffer   buf2 = ByteBuffer.allocate(1024);
        ByteBuffer[] bufs = {buf1, buf2};

        // 分散读取
        long read = inChannel.read(bufs); // 参数需要一个数组

        // ---------------------------------------------

        for (ByteBuffer byteBuffer : bufs)
        {
            byteBuffer.flip(); // 切换到读模式
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));  // 打印 he
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));  // 打印 llo

        // ---------------------------------------------

        RandomAccessFile out        = new RandomAccessFile("F:\\test-file\\out.txt", "rw");
        FileChannel      outChannel = out.getChannel(); // 获取通道

        // 聚集写入
        long write = outChannel.write(bufs); // 把 bufs 里面的几个缓冲区聚集到 outChannel 这个通道中, 也就是到了 out.txt 文件中
        outChannel.close();
    }
}
