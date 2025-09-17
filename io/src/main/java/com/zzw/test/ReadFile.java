package com.zzw.test;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ReadFile {

    public static final String path = "/Users/lidong/Desktop/temp/test";
    public static final String tarFilePath = "/Users/lidong/Desktop/temp/test/file.tar";
    public static final byte[] unit = "hello world\n".getBytes(StandardCharsets.UTF_8);

    public static final Map<String, byte[]> map = new HashMap<>(4096);

    public static void main(String[] args) throws IOException {
        // generateFile(2000, 5 * 1024 * 1024);
        test1(); // 6371 ms 普通读取 2000 个 5MB 文件
        test2(); // 5744 ms 流式读取 tar 包中的 2000 个 5MB 文件
        test3(); // 5015 ms 一次读取 tar 包中的 2000 个 5MB 文件
    }

    // ------------------------------------------------

    public static void test1() throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 1; i <= 2000; i++) {
            String filename = path + "/" + i + ".txt";
            byte[] bytes = FileUtils.readFileToByteArray(new File(filename));
            map.put(filename, bytes);
            // log.info("read1: fileName = [{}], size = [{}]", filename, bytes.length);
        }
        long time = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("test1 time = [{}] ms", time);
        map.clear();
    }

    // cd /Users/lidong/Desktop/temp/test
    // tar -cvf file.tar ./
    public static void test2() throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 流式读取
        try (TarArchiveInputStream tar = new TarArchiveInputStream(FileUtils.openInputStream(new File(tarFilePath)))) {
            TarArchiveEntry entry;
            while ((entry = tar.getNextTarEntry()) != null) {
                if (entry.isDirectory() || entry.getName().contains("._")) continue;
                int size = (int) entry.getSize();
                byte[] bytes = new byte[size];

                IOUtils.readFully(tar, bytes); // 5131 ms 得到精确长度后一次性分配
                // IOUtils.toByteArray(tar);   // 6581 ms 拿不到已知长度, 无法一次性按大小分配目标数组, 只能边读边扩容

                map.put(entry.getName(), bytes);
                // log.info("read2: fileName = [{}], size = [{}]", entry.getName(), size);
            }
        }

        long time = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("test2 time = [{}] ms", time);
        map.clear();
    }

    public static void test3() throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();

        // 一次性读取
        try (TarFile tar = new TarFile(Paths.get(tarFilePath))) {
            for (TarArchiveEntry entry : tar.getEntries()) {
                if (entry.isDirectory() || entry.getName().contains("._")) continue;
                int size = (int) entry.getSize();
                byte[] bytes = new byte[(int) size];
                IOUtils.readFully(tar.getInputStream(entry), bytes);
                map.put(entry.getName(), bytes);
                // log.info("read2: fileName = [{}], size = [{}]", entry.getName(), size);
            }
        }

        long time = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("test3 time = [{}] ms", time);
        map.clear();
    }

    // ------------------------------------------------

    public static void generateFile(int count, int size) throws IOException {
        int num = size / unit.length;
        byte[] bytes = repeat(unit, num);
        for (int i = 1; i <= count; i++) {
            String filename = path + "/" + i + ".txt";
            FileUtils.writeByteArrayToFile(new File(filename), bytes);
            log.info("write: fileName = [{}], size = [{}]", filename, bytes.length);
        }
    }

    public static byte[] repeat(byte[] bytes, int count) {
        List<byte[]> list = Collections.nCopies(count, bytes);
        return ArrayUtil.addAll(list.toArray(new byte[0][]));
    }
}
