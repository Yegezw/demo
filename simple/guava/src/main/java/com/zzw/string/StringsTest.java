package com.zzw.string;

import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Strings 工具
 */
public class StringsTest
{

    /*
     * Strings.emptyToNull()
     * Strings.nullToEmpty()
     * Strings.isNullOrEmpty()
     *
     * Strings.repeat()
     *
     * Strings.padStart()
     * Strings.padEnd()
     *
     * Strings.commonPrefix()
     * Strings.commonSuffix()
     *
     * Strings.lenientFormat()
     */

    @Test
    @SuppressWarnings("all")
    public void test1()
    {
        // 检查字符串是否为 null 或空字符串
        assertTrue(Strings.isNullOrEmpty(null));
        assertTrue(Strings.isNullOrEmpty(""));
        assertFalse(Strings.isNullOrEmpty(" "));

        // 将空字符串转换为 null
        assertEquals(null, Strings.emptyToNull(""));
        assertEquals(null, Strings.emptyToNull(null));
        assertEquals(" ", Strings.emptyToNull(" "));

        // 将 null 转换为空字符串
        assertEquals("", Strings.nullToEmpty(null));
        assertEquals(" ", Strings.nullToEmpty(" "));
    }

    @Test
    public void test2()
    {
        String s1 = "hello";
        String s2 = "ha";
        String s3 = "abcdefpqw";
        String s4 = "abcxyzpqw";

        // 首部填充字符
        assertEquals("---hello", Strings.padStart(s1, 8, '-'));
        // 尾部填充字符
        assertEquals("hello---", Strings.padEnd(s1, 8, '-'));

        // 重复字符串指定次数
        assertEquals("hahaha", Strings.repeat(s2, 3));

        // 公共前缀
        assertEquals("abc", Strings.commonPrefix(s3, s4));
        // 公共后缀
        assertEquals("pqw", Strings.commonSuffix(s3, s4));
    }

    @Test
    public void test3()
    {
        assertEquals(
                "hello zhang san world",
                Strings.lenientFormat("hello %s world", "zhang san")
        );

        /*
         * Strings.lenientFormat() 方法在格式化时更加宽容
         * 它不会像 String.format() 那样在格式字符串和参数不匹配时抛出 IllegalFormatException
         */

        // 参数缺失
        String template1 = "This is a %s template with %s missing.";
        String result1   = Strings.lenientFormat(template1, "formatting");
        System.out.println(result1); // This is a formatting template with %s missing.

        // 多余参数
        String template2 = "This is a %s template.";
        String result2   = Strings.lenientFormat(template2, "formatting", "extra");
        System.out.println(result2); // This is a formatting template. [extra]

        // null 值
        String template = "This is a %s template with a null value.";
        String result3  = Strings.lenientFormat(template, (Object) null);
        System.out.println(result3); // This is a null template with a null value.
    }
}
