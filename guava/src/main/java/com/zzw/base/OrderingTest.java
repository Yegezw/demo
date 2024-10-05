package com.zzw.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.zzw.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ordering 排序
 */
public class OrderingTest
{

    @Test
    public void test1()
    {
        // 这个排序器不会根据任何逻辑或顺序来比较对象
        // 它的排序结果是完全任意的，但是在整个 Java 虚拟机的生命周期内是一致的
        Ordering<Object> ordering = Ordering.arbitrary();
        assertEquals("Ordering.arbitrary()", ordering.toString());

        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) list.add(i);
        list.sort(ordering);
        System.out.println(list);
        list.sort(ordering);
        System.out.println(list);
    }

    @Test
    public void test2()
    {
        // 返回一个 Ordering，所有值的排序地位都是平等的，表明无排序
        Ordering<Object> ordering = Ordering.allEqual();
        assertEquals("Ordering.allEqual()", ordering.toString());

        // 可以比较 null
        assertEquals(0, ordering.compare(null, 1));
        assertEquals(0, ordering.compare("apples", "oranges"));

        List<String> list = Lists.newArrayList("a", "c", null, "d", "b");
        // 返回排序后的可变集合
        assertEquals(list, ordering.sortedCopy(list));
        // 返回排序后的不可变集合 NullPointerException: at index 2
        assertEquals(list, ordering.immutableSortedCopy(list));
    }

    /**
     * <a href="https://tool.oschina.net/commons?type=4">ASCII 码对照表</a>
     */
    @Test
    public void test3()
    {
        // 自定义比较器排序，from() 将 Comparator -> Ordering
        // String.CASE_INSENSITIVE_ORDER 按照 ASCII 排序，不区分大小写的排序
        Ordering<String> ordering = Ordering.from(String.CASE_INSENSITIVE_ORDER);

        // 不可以比较 null，会报 NullPointerException
        assertThrows(NullPointerException.class, () -> ordering.compare("1", null));

        assertEquals(0, ordering.compare("A", "a"));
        assertTrue(ordering.compare("a", "B") < 0);
        assertTrue(ordering.compare("1", "a") < 0);
        assertTrue(ordering.compare("9", "=") < 0);
    }

    @Test
    public void test4()
    {
        Ordering<Student> o1 = Ordering.from(Comparator.comparingInt(student -> student.age));
        Ordering<Student> o2 = Ordering.from((student1, student2) -> student2.score - student1.score);

        // 组合比较器
        Ordering<Student> o3 = o1.compound(o2);
        List<Student> list = Lists.newArrayList(
                new Student(17, 95),
                new Student(13, 90),
                new Student(20, 85),
                new Student(17, 85),
                new Student(15, 100),
                new Student(13, 80)
        );
        List<Student> sorted = o3.sortedCopy(list);
        sorted.forEach(System.out::println);
    }

    // =================================================================================================================

    /**
     * <a href="https://tool.oschina.net/commons?type=4">ASCII 码对照表</a>
     */
    @Test
    public void test5()
    {
        // 对可排序类型做自然排序
        // 如数字按大小、日期按先后排序、字符串按字典顺序
        Ordering<Integer> o1 = Ordering.natural();
        // 通过 toString() 得到字符串，按字典顺序
        Ordering<Object> o2 = Ordering.usingToString();
        assertEquals("Ordering.natural()", o1.toString());
        assertEquals("Ordering.usingToString()", o2.toString());

        // 2 < 11 自然排序
        assertEquals(-1, o1.compare(2, 11));
        // 2 > 11 字符串字典排序
        assertEquals(1, o2.compare(2, 11));

        // 不可以比较 null，会报 NullPointerException
        assertThrows(NullPointerException.class, () -> o1.compare(1, null));
        assertThrows(NullPointerException.class, () -> o2.compare("apple", null));

        // ------------------------------------------------

        List<Integer> actual;   // 实际
        List<Integer> expected; // 预期
        List<Integer> list1 = Lists.newArrayList(1, 6, 2, 4, 5, 3);
        List<Integer> list2 = Lists.newArrayList(1, 6, null, 2, 4, null, 5, 3);

        // 不可以比较 null
        actual   = o1.sortedCopy(list1);
        expected = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        assertEquals(expected, actual);

        // 可以比较 null
        actual   = o1
                .nullsFirst() // null 值排到最前面
                .nullsLast()  // null 值排到最后面
                .sortedCopy(list2);
        expected = Lists.newArrayList(1, 2, 3, 4, 5, 6, null, null);
        assertEquals(expected, actual);

        // 可以比较 null
        actual   = o1
                .nullsFirst() // null 值排到最前面
                .reverse()    // 获取语义相反的排序器
                .sortedCopy(list2);
        expected = Lists.newArrayList(6, 5, 4, 3, 2, 1, null, null);
        assertEquals(expected, actual);

        // ------------------------------------------------

        // o1 对 Integer 排序
        // o3 对 Iterable<Integer> 进行排序，o3 的排序规则是 o1
        Ordering<Iterable<Integer>> o3 = o1
                .nullsFirst()
                .lexicographical()
                .nullsLast();

        List<Integer> list3 = Lists.newArrayList(5, 7, 6, 8);
        List<Integer> list4 = Lists.newArrayList(null, 10, null, 9);
        List<Integer> list5 = Lists.newArrayList(30, 1, 2, 4);
        List<Integer> list6 = Lists.newArrayList();

        List<List<Integer>> listAll = Lists.newArrayList(list3, list4, list5, null, list6);
        List<List<Integer>> sorted  = o3.sortedCopy(listAll);
        /*
         * []
         * [null, 10, null, 9]
         * [5, 7, 6, 8]
         * [30, 1, 2, 4]
         * null
         */
        sorted.forEach(System.out::println);
    }

    @Test
    public void test6()
    {
        // onResultOf 将传入 function 应用到每个元素上面
        // 再通过 Ordering 进行排序
        Ordering<Integer> o1 = Ordering.natural().reverse();
        Ordering<String>  o2 = o1.onResultOf(String::length);

        List<String> list = Lists.newArrayList("a", "dm", "b", "e", "cw");
        o2.sortedCopy(list).forEach(System.out::println);
    }

    // =================================================================================================================

    @Test
    public void test7()
    {
        // 根据给定列表的顺序比较对象，只能比较参数列表中存在的对象，参数列表中的值不能重复
        Ordering<Integer> o1 = Ordering.explicit(Lists.newArrayList(3, 1, 7, 5, 9));
        Ordering<Integer> o2 = Ordering.explicit(3, 1, 7, 5, 9);
        assertEquals(o1, o2);
        assertEquals("Ordering.explicit([3, 1, 7, 5, 9])", o1.toString());
        assertEquals("Ordering.explicit([3, 1, 7, 5, 9])", o2.toString());

        List<Integer>      actual;   // 实际
        List<Integer>      expected; // 预期
        ArrayList<Integer> list = Lists.newArrayList(1, 3, 5, 7, 9);

        actual   = o1.sortedCopy(list);
        expected = Lists.newArrayList(3, 1, 7, 5, 9);
        assertEquals(expected, actual);
    }

    @Test
    public void test8()
    {
        Ordering<Integer> ordering = Ordering.natural();

        // ------------------------------------------------

        // isOrdered 是否已排序，允许相等

        assertFalse(ordering.isOrdered(Arrays.asList(5, 3, 0, 9)));

        assertTrue(ordering.isOrdered(Collections.emptyList()));
        assertTrue(ordering.isOrdered(Arrays.asList(0, 3, 5, 9)));
        assertTrue(ordering.isOrdered(Arrays.asList(0, 0, 1, 2)));

        // ------------------------------------------------

        // isStrictlyOrdered 是否已严格排序，不允许相等

        assertFalse(ordering.isStrictlyOrdered(Arrays.asList(5, 3, 0, 9)));
        assertFalse(ordering.isStrictlyOrdered(Arrays.asList(0, 0, 1, 2)));

        assertTrue(ordering.isStrictlyOrdered(Collections.emptyList()));
        assertTrue(ordering.isStrictlyOrdered(Arrays.asList(0, 3, 5, 9)));

        // ------------------------------------------------

        List<Integer> actual;   // 实际
        List<Integer> expected; // 预期

        // 返回前 K 位的不可变集合
        actual   = ordering.leastOf(Arrays.asList(3, 1, 2, 4), 2);
        expected = ImmutableList.of(1, 2);
        assertEquals(expected, actual);
        final List<Integer> finalActual1 = actual;
        assertThrows(UnsupportedOperationException.class, () -> finalActual1.set(0, 1));

        // 返回后 K 位的不可变集合
        actual   = ordering.greatestOf(Arrays.asList(3, 1, 2, 4), 2);
        expected = ImmutableList.of(4, 3);
        assertEquals(expected, actual);
        final List<Integer> finalActual2 = actual;
        assertThrows(UnsupportedOperationException.class, () -> finalActual2.set(0, 1));

        // ------------------------------------------------

        List<Integer> list = Lists.newArrayList(5, 3, 0, 9);

        // 最左边的最小值
        assertEquals(0, ordering.min(list));
        // 最左边的最大值
        assertEquals(9, ordering.max(list));
    }
}
