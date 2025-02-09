package com.zzw.base;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class Base3Test
{

    @Test
    public void test1()
    {
        Path source1 = Paths.get("1.txt");     // 相对路径: 不带盘符, 使用 user.dir 环境变量来定位 1.txt

        Path source2 = Paths.get("d:\\1.txt"); // 绝对路径: 代表了 d:\1.txt, 反斜杠需要转义

        Path source3 = Paths.get("d:/1.txt");  // 绝对路径: 代表了 d:\1.txt

        Path source4 = Paths.get("d:\\data\\projects\\a\\..\\b");

        Path project = Paths.get("d:\\data", "project"); // 代表了 d:\data\project

        System.out.println(source1.toAbsolutePath());
        System.out.println(source2);
        System.out.println(source3);
        System.out.println(source4);
        System.out.println(project);

        // 正常化路径: 会去除 . 以及 ..
        // d:\data\projects\b
        System.out.println(source4.normalize());
    }

    @Test
    public void test2() throws IOException
    {
        Path path = Paths.get("F:\\test-file\\in");
        Path dir1 = Paths.get("dir1");
        Path dir3 = Paths.get("dir1\\dir2\\dir3");

        // 是否存在
        System.out.println(Files.exists(path));

        // 如果目录已存在: 会抛异常 FileAlreadyExistsException
        // 不能一次创建多级目录: 否则会抛异常 NoSuchFileException
        Files.createDirectory(dir1);

        Files.createDirectories(dir3);
    }

    @Test
    public void test3() throws IOException
    {
        Path source = Paths.get("F:\\test-file\\in");
        Path target = Paths.get("F:\\test-file\\out");

        // 如果文件已存在: 会抛异常 FileAlreadyExistsException
        // Files.copy(source, target);

        // 如果希望用 source 覆盖掉 target, 需要用 StandardCopyOption 来控制
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void test4() throws IOException
    {
        Path source = Paths.get("F:\\test-file\\in");
        Path target = Paths.get("F:\\test-file\\out");

        // 移动文件 StandardCopyOption.ATOMIC_MOVE 保证文件移动的原子性
        Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
    }

    @Test
    public void test5() throws IOException
    {
        Path file = Paths.get("F:\\test-file\\out");
        Path dir  = Paths.get("F:\\test-file\\test");

        // 删除文件
        // 如果文件不存在: 会抛异常 NoSuchFileException
        Files.delete(file);

        // 删除目录
        // 如果目录还有内容: 会抛异常 DirectoryNotEmptyException
        Files.delete(dir);
    }

    @Test
    public void test6() throws IOException
    {
        Path path = Paths.get("D:\\software\\code\\Java\\jdk1.8.0_202");

        AtomicInteger dirCount  = new AtomicInteger(); // 文件目录数目
        AtomicInteger fileCount = new AtomicInteger(); // 文件数目
        AtomicInteger jarCount  = new AtomicInteger(); // jar 包数

        Files.walkFileTree(
                path,
                new SimpleFileVisitor<>()
                {
                    // 访问到目录的操作
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
                    {
                        System.out.println("===>" + dir);
                        dirCount.incrementAndGet();
                        return super.preVisitDirectory(dir, attrs);
                    }

                    // 访问到文件的操作
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
                    {
                        fileCount.incrementAndGet();
                        if (file.toString().endsWith(".jar"))
                        {
                            jarCount.incrementAndGet();
                            System.out.println(file);
                        }
                        return super.visitFile(file, attrs);
                    }
                }
        );

        // 打印数目
        System.out.println("文件目录数: " + dirCount.get());
        System.out.println("文件数: " + fileCount.get());
        System.out.println("Jar 包数: " + jarCount.get());
    }
}
