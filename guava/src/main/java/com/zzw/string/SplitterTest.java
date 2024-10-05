package com.zzw.string;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Splitter 字符串分隔
 */
public class SplitterTest
{

    /*
     * Splitter.on()
     * Splitter.onPattern()
     * Splitter.fixedLength()
     *
     * Splitter.limit()
     * Splitter.omitEmptyStrings()
     * Splitter.trimResults()
     * Splitter.trimResults(CharMatcher)
     * Splitter.withKeyValueSeparator()
     *
     * Splitter.split()
     * Splitter.splitToList()
     * Splitter.splitToStream()
     */

    @Test
    public void test1()
    {
        List<String> actual;   // 实际
        List<String> expected; // 预期

        // 分割字符串为集合
        actual   = Splitter.on("|").splitToList("hello|world");
        expected = Arrays.asList("hello", "world");
        assertEquals(expected, actual);

        // 包含空字符串 ""
        actual = Splitter.on("|").splitToList("hello|world|||");
        System.out.println(actual);
        assertTrue(actual.get(2).isEmpty());
        assertTrue(actual.get(3).isEmpty());
        assertTrue(actual.get(4).isEmpty());

        // 忽略空字符串 ""
        actual = Splitter
                .on("|")
                .omitEmptyStrings()
                .splitToList("hello|world|||");
        assertEquals(expected, actual);

        // 去除左右空格
        actual   = Splitter
                .on("|")
                .omitEmptyStrings() // 忽略空字符串
                .trimResults()      // 去除左右空格
                .splitToList(" hello|world |||");
        expected = Arrays.asList("hello", "world");
        assertEquals(expected, actual);
    }

    @Test
    public void test2()
    {
        List<String> actual;   // 实际
        List<String> expected; // 预期

        actual   = Splitter
                .on("#")
                .limit(3) // 分割为 3 个字符串
                .splitToList("hello#world#java#google#scala");
        expected = Arrays.asList("hello", "world", "java#google#scala");
        assertEquals(expected, actual);

        actual   = Splitter
                .fixedLength(4) // 固定长度分割
                .splitToList("aaaabbbbccccdddd");
        expected = Arrays.asList("aaaa", "bbbb", "cccc", "dddd");
        assertEquals(expected, actual);

        expected = Arrays.asList("Hello", "World");
        actual   = Splitter
                .onPattern("\\|")  // 正则
                .trimResults()
                .omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertEquals(expected, actual);
        actual = Splitter
                .on(Pattern.compile("\\|")) // 正则
                .trimResults()
                .omitEmptyStrings()
                .splitToList("Hello | World||||");
        assertEquals(expected, actual);
    }

    @Test
    public void test3()
    {
        Map<String, String> actual;   // 实际
        Map<String, String> expected; // 预期

        actual   = Splitter
                .on(Pattern.compile("\\|"))
                .trimResults()
                .omitEmptyStrings()
                .withKeyValueSeparator("=")
                .split("hello=HELLO | world=WORLD|||");
        expected = ImmutableMap.of(
                "hello", "HELLO",
                "world", "WORLD"
        );
        assertEquals(expected, actual);
    }

    @Test
    public void test4()
    {
        List<String> actual;   // 实际
        List<String> expected; // 预期

        // 去除左右匹配的字符
        actual   = Splitter
                .on(',')
                .omitEmptyStrings()
                .trimResults(CharMatcher.anyOf("~? "))
                .splitToList("~?~this, is~~ , , random , text,");
        expected = Arrays.asList("this", "is", "random", "text");
        assertEquals(expected, actual);
    }
}
