package com.zzw.string;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Joiner 字符串连接
 */
public class JoinerTest
{

    /*
     * Joiner.on()
     *
     * Joiner.join()
     * Joiner.appendTo()
     *
     * Joiner.skipNulls()
     * Joiner.useForNull()
     *
     * Joiner.withKeyValueSeparator()
     */

    private final List<String> stringList              = Arrays.asList(
            "Google", "Guava", "Java", "Scala", "Kafka"
    );
    private final List<String> stringListWithNullValue = Arrays.asList(
            "Google", "Guava", "Java", "Scala", null
    );

    private final Map<String, String> stringMap = ImmutableMap.of(
            "Hello", "Guava",
            "Java", "Scala"
    );

    private final String targetFileName = "F:\\github\\guava-joiner.txt";

    @Test
    public void test1()
    {
        // 拼接可迭代对象
        String result1 = Joiner.on("#").join(stringList);
        assertEquals("Google#Guava#Java#Scala#Kafka", result1);

        // 集合中存在 null 值，空指针异常
        assertThrows(
                NullPointerException.class,
                () -> Joiner.on("#").join(stringListWithNullValue)
        );

        String result2 = Joiner.on("#")
                .skipNulls() // 跳过 null 值
                .join(stringListWithNullValue);
        assertEquals("Google#Guava#Java#Scala", result2);

        String result3 = Joiner.on("#")
                .useForNull("Default") // 使用自定义字符串，代替集合中的 null 元素
                .join(stringListWithNullValue);
        assertEquals("Google#Guava#Java#Scala#Default", result3);
    }

    @Test
    public void test2() throws Exception
    {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = Joiner.on("#")
                .useForNull("Default")
                .appendTo(sb1, stringListWithNullValue); // 将拼接好的字符串，添加到 StringBuilder 中去
        assertSame(sb1, sb2);
        assertEquals("Google#Guava#Java#Scala#Default", sb2.toString());

        try (FileWriter writer = new FileWriter(targetFileName))
        {
            Joiner.on("#")
                    .useForNull("Default")
                    .appendTo(writer, stringListWithNullValue); // 将拼接好的字符串，写入到文件中去
        }
    }

    @Test
    public void test3() throws Exception
    {
        // 拼接 Map
        String result = Joiner.on('&').withKeyValueSeparator("=").join(stringMap);
        assertEquals("Hello=Guava&Java=Scala", result);

        try (FileWriter writer = new FileWriter(targetFileName))
        {
            Joiner.on("#")
                    .withKeyValueSeparator("=")
                    .appendTo(writer, stringMap); // 将拼接好的字符串，写入到文件中去
        }
    }
}
