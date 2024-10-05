package com.zzw.string;

import com.google.common.base.CharMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CharMatcher 字符匹配: 匹配 + 处理
 */
public class CharMatcherTest
{

    /*
     * CharMatcher.any()
     * CharMatcher.none()
     *
     * CharMatcher.anyOf(CharSequence)
     * CharMatcher.noneOf(CharSequence)
     * CharMatcher.is(char)
     * CharMatcher.isNot(char)
     * CharMatcher.inRange(char, char)
     * CharMatcher.forPredicate(Predicate)
     *
     * CharMatcher.invisible()
     * CharMatcher.whitespace()
     * CharMatcher.breakingWhitespace()
     *
     * CharMatcher.digit()
     * CharMatcher.ascii()
     *
     * CharMatcher.javaLetter()
     * CharMatcher.javaDigit()
     * CharMatcher.javaLetterOrDigit()
     * CharMatcher.javaIsoControl()
     * CharMatcher.javaLowerCase()
     * CharMatcher.javaUpperCase()
     *
     * CharMatcher.or()
     * CharMatcher.and()
     * CharMatcher.negate()
     *
     * ***************************************************
     *
     * CharMatcher.retainFrom(CharSequence)
     * CharMatcher.removeFrom(CharSequence)
     * CharMatcher.replaceFrom(CharSequence, CharSequence)
     *
     * CharMatcher.trimFrom(CharSequence)
     * CharMatcher.trimLeadingFrom(CharSequence)
     * CharMatcher.trimTrailingFrom(CharSequence)
     *
     * CharMatcher.collapseFrom(CharSequence, char)
     * CharMatcher.trimAndCollapseFrom(CharSequence, char)
     *
     * CharMatcher.matches(char)
     * CharMatcher.matchesAllOf(CharSequence)
     * CharMatcher.matchesAnyOf(CharSequence)
     * CharMatcher.matchesNoneOf(CharSequence)
     *
     * CharMatcher.countIn(CharSequence)
     * CharMatcher.indexIn(CharSequence)
     * CharMatcher.indexIn(CharSequence, int)
     * CharMatcher.lastIndexIn(CharSequence)
     */

    /**
     * <a href="https://tool.oschina.net/commons?type=4">ASCII 码对照表</a>
     */
    @Test
    public void test1()
    {
        // 匹配任何 "被视为空白" 的字符
        // 比如空格、制表符、换行符、回车符，包括空白的 Unicode 字符
        CharMatcher matcher1 = CharMatcher.whitespace();

        // 匹配任何 "破坏性空格" 的字符，更常用
        // 比如空格、制表符、换行符、回车符，不包括空白的 Unicode 字符
        CharMatcher matcher2 = CharMatcher.breakingWhitespace();

        // 匹配所有 Java ISO 控制字符
        // ISO 控制字符是指那些在 ASCII 编码中位于 0 到 31 之间的字符
        CharMatcher matcher3 = CharMatcher.javaIsoControl();

        String actual;   // 实际
        String expected; // 预期
        String input1 = "   Hi 123 Ting 456 Feng  ";
        String input2 = "     Ting Feng     ";
        String input3 = "ab\tcd\nef\bg";
        assertEquals("abcdefg", matcher3.removeFrom(input3));

        // 保留匹配到的字符
        actual   = matcher2.retainFrom(input1);
        expected = "         ";
        assertEquals(expected, actual);
        // 删除匹配到的字符
        actual   = matcher2.removeFrom(input1);
        expected = "Hi123Ting456Feng";
        assertEquals(expected, actual);
        // 替换匹配到的字符
        actual   = matcher2.replaceFrom(input1, "*");
        expected = "***Hi*123*Ting*456*Feng**";
        assertEquals(expected, actual);

        // 删除首尾匹配到的字符
        actual   = matcher2.trimFrom(input2);
        expected = "Ting Feng";
        assertEquals(expected, actual);
        // 删除首部匹配到的字符
        actual   = matcher2.trimLeadingFrom(input2);
        expected = "Ting Feng     ";
        assertEquals(expected, actual);
        // 删除尾部匹配到的字符
        actual   = matcher2.trimTrailingFrom(input2);
        expected = "     Ting Feng";
        assertEquals(expected, actual);

        // 折叠自
        actual   = matcher2.collapseFrom(input2, '-');
        expected = "-Ting-Feng-";
        assertEquals(expected, actual);
        // 删除首尾匹配到的字符 + 折叠自
        actual   = matcher2.trimAndCollapseFrom(input2, '-');
        expected = "Ting-Feng";
        assertEquals(expected, actual);
    }

    @Test
    public void test2()
    {
        String input1 = "H*el.lo,}12";
        String input2 = "あH*el.lo,}12";

        // 匹配字母或数字
        CharMatcher matcher1 = CharMatcher.forPredicate(Character::isLetterOrDigit);
        // 匹配小写字母
        CharMatcher matcher2 = CharMatcher.forPredicate(Character::isLowerCase);
        // 匹配 Ascii
        CharMatcher matcher3 = CharMatcher.ascii();
        // 匹配非 Ascii
        CharMatcher matcher4 = matcher3.negate();
        // 匹配任意字符
        CharMatcher matcher5 = CharMatcher.any();
        // 匹配任意字符 "Hel"
        CharMatcher matcher6 = CharMatcher.anyOf("Hel");
        // 不匹配任意字符
        CharMatcher matcher7 = CharMatcher.none();
        // 不匹配任意字符 "Hel"
        CharMatcher matcher8 = CharMatcher.noneOf("Hel");

        // 两个 Matcher 同时匹配 and
        CharMatcher and = matcher1.and(matcher2);
        // 两个 Matcher 同时匹配 or
        CharMatcher or = matcher1.or(matcher2);

        assertEquals("ello", and.retainFrom(input1));
        assertEquals("Hello12", or.retainFrom(input1));
        assertEquals("H*el.lo,}12", matcher3.retainFrom(input2));
        assertEquals("あ", matcher4.retainFrom(input2));
        assertEquals("H*el.lo,}12", matcher5.retainFrom(input1));
        assertEquals("Hell", matcher6.retainFrom(input1));
        assertEquals("", matcher7.retainFrom(input1));
        assertEquals("*.o,}12", matcher8.retainFrom(input1));
    }

    @Test
    public void test3()
    {
        String actual;   // 实际
        String expected; // 预期
        String input = "a, c, z, 1, 2";

        // 匹配参数之内的所有字符
        CharMatcher matcher1 = CharMatcher.is(',');
        // 匹配参数之外的所有字符
        CharMatcher matcher2 = CharMatcher.isNot(',');

        actual   = matcher1.retainFrom(input);
        expected = ",,,,";
        assertEquals(expected, actual);
        actual   = matcher1.removeFrom(input);
        expected = "a c z 1 2";
        assertEquals(expected, actual);

        actual   = matcher2.retainFrom(input);
        expected = "a c z 1 2";
        assertEquals(expected, actual);
        actual   = matcher2.removeFrom(input);
        expected = ",,,,";
        assertEquals(expected, actual);
    }

    @Test
    public void test4()
    {
        String      input    = "**e,l.lo,}12";
        CharMatcher matcher1 = CharMatcher.is(',');
        CharMatcher matcher2 = CharMatcher.is('?');

        // matchesAllOf  所有都匹配 false
        assertFalse(matcher1.matchesAllOf(input));
        // matchesAnyOf  有一个匹配 true
        assertTrue(matcher1.matchesAnyOf(input));
        // matchesNoneOf 全都不匹配 true
        assertTrue(matcher2.matchesNoneOf(input));
    }

    @Test
    public void test5()
    {
        String input = "H*el.lo,}12";

        // 匹配字母或数字
        CharMatcher matcher1 = CharMatcher.forPredicate(Character::isLetterOrDigit);
        // 匹配 [a ... l] 字符
        CharMatcher matcher2 = CharMatcher.inRange('a', 'l');

        // 匹配到的个数
        assertEquals(7, matcher1.countIn(input)); // Hello12
        assertEquals(3, matcher2.countIn(input)); // ell

        // 匹配到的索引
        assertEquals(0, matcher1.indexIn(input));
        assertEquals(5, matcher1.indexIn(input, 4));
        assertEquals(10, matcher1.lastIndexIn(input));
    }
}
