package com.zzw.base;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.zzw.entity.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Objects + MoreObjects + ComparisonChain
 */
public class ObjectsTest
{

    @Test
    @SuppressWarnings("all")
    public void test1()
    {
        assertTrue(Objects.equal("a", "a"));
        assertTrue(Objects.equal(null, null));
        assertFalse(Objects.equal(null, "a"));
    }

    @Test
    @SuppressWarnings("all")
    public void test2()
    {
        int h1 = Objects.hashCode(1, "two", 3.0);
        int h2 = Objects.hashCode(new Integer(1), new String("two"), new Double(3.0));
        assertEquals(h1, h2);

        int h3 = Objects.hashCode(1, null, 10);
        int h4 = Objects.hashCode(1, 10, null);
        assertNotEquals(h3, h4);
    }

    @Test
    public void test3()
    {
        assertEquals("a", MoreObjects.firstNonNull("a", null));
        assertEquals("b", MoreObjects.firstNonNull("b", null));
        assertThrows(NullPointerException.class, () -> MoreObjects.firstNonNull(null, null));

        String string = MoreObjects.toStringHelper(Student.class)
                .add("name", "张三")
                .add("gender", "男")
                .add("girlfriend", null)
                .addValue(10)
                .addValue(null)
                .omitNullValues()
                .toString();
        assertEquals("Student{name=张三, gender=男, 10}", string);
    }

    @Test
    public void test4()
    {
        String strA = "a";
        String strB = "b";
        int    intA = 1;
        int    intB = 2;

        /*
         * 用于编写 Comparator.compare() 或 Comparable.compareTo()
         * ComparisonChain 执行 "惰性" 比较，它只执行比较，直到找到非零结果，之后它会忽略进一步的输入
         */

        ComparisonChain.start()
                .compare(strA, strB)
                .compare(intA, intB, Ordering.natural().nullsLast())
                .result();
    }
}
