package com.zzw.collection.util;

import org.junit.jupiter.api.Test;

/**
 * Lists
 */
public class ListsTest
{

    @Test
    public void test1()
    {
        /*
         * 除了静态构造函数方法和函数式编程方法外，Lists 还为 List 对象提供了大量有价值的实用方法
         *
         * 分区
         * Lists.partition(List, int)
         *
         * Lists.reverse(List)
         * 如果列表是不可变的，请考虑改用 ImmutableList.reverse()
         */
    }

    @Test
    public void test2()
    {
        /*
         * 静态工厂
         *
         * Lists.newArrayList()
         * Lists.newArrayList(E... elements)
         * Lists.newArrayList(Iterable<? extends E> elements)
         * Lists.newArrayList(Iterator<? extends E> elements)
         *
         * Lists.newArrayListWithCapacity(int initialArraySize)
         * Lists.newArrayListWithExpectedSize(int estimatedSize)
         *
         * Lists.newLinkedList()
         * Lists.newLinkedList(Iterable<? extends E> elements)
         *
         * Lists.newCopyOnWriteArrayList()
         * Lists.newCopyOnWriteArrayList(Iterable<? extends E> elements)
         *
         * 返回不可修改的列表，rest 改变则 result 也变
         * Lists.asList(@Nullable E first, E[] rest)
         * Lists.asList(@Nullable E first, @Nullable E second, E[] rest)
         *
         * 变换
         * Lists.transform(List, Function)
         *
         * 分割字符串为 ImmutableList<Character>
         * Lists.charactersOf(String)
         * Lists.charactersOf(CharSequence)
         */
    }

    @Test
    public void test3()
    {
        /*
         * Comparators
         *
         * What you're comparing         Exactly 2 instances           More than 2 instances
         * unboxed numeric primitives    Math.max(a, b)                Ints.max(a, b, c) OR Longs.max(a, b, c)
         * Comparable instances          Comparators.max(a, b)         Collections.max(asList(a, b, c))
         * using a custom Comparator     Comparators.max(a, b, cmp)    Collections.max(asList(a, b, c), cmp)
         */
    }
}
