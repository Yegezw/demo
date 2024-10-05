package com.zzw.string;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CaseFormat 字符串格式转换
 */
public class CaseFormatTest
{

    @Test
    public void test1()
    {
        CaseFormat lowerHyphen     = CaseFormat.LOWER_HYPHEN;     // lower-hyphen     小写连字符
        CaseFormat lowerCamel      = CaseFormat.LOWER_CAMEL;      // lowerCamel       小写驼峰式
        CaseFormat upperCamel      = CaseFormat.UPPER_CAMEL;      // UpperCamel       大写驼峰式
        CaseFormat lowerUnderscore = CaseFormat.LOWER_UNDERSCORE; // lower_underscore 小写下划线
        CaseFormat upperUnderscore = CaseFormat.UPPER_UNDERSCORE; // UPPER_UNDERSCORE 大写下划线

        // lowerCamel -> UPPER_UNDERSCORE
        Converter<String, String> converter = lowerCamel.converterTo(upperUnderscore);
        assertEquals("HELLO_WORLD", converter.convert("helloWorld"));
    }

    @Test
    public void test2()
    {
        String actual   = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, "HELLO_WORLD");
        String expected = "hello-world";
        assertEquals(expected, actual);
    }
}
