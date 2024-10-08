package com.zzw.collection.util;

import org.junit.jupiter.api.Test;

/**
 * Sets
 */
public class SetsTest
{

    @Test
    public void test1()
    {
        /*
         * SetView 视图
         * 可以直接作为 Set 使用，因为它实现了 Set 接口
         *
         * 复制到另一个可变集合中
         * SetView.copyInto(Set)
         *
         * 不可变副本
         * SetView.immutableCopy(Set)
         *
         * ***************************************
         *
         * 并集
         * Sets.union(Set1, Set2)
         *
         * 交集
         * Sets.intersection(Set1, Set2)
         *
         * 差集 Set1 有 Set2 没有
         * Sets.difference(Set1, Set2)
         *
         * 对称差集，交集的反义词
         * Sets.symmetricDifference(Set1, Set2)
         */
    }

    @Test
    public void test2()
    {
        /*
         * 子集
         * NavigableSet<K> subSet(NavigableSet<K> set, Range<K> range)
         *
         * 子集集
         * Sets.powerSet(Set)
         * Sets.combinations(Set, int)
         *
         * 笛卡尔积
         * Sets.cartesianProduct(List<Set>)
         */
    }

    @Test
    public void test3()
    {
        /*
         * 静态工厂
         *
         * Sets.newHashSet()
         * Sets.newHashSet(E... elements)
         * Sets.newHashSet(Iterable<? extends E> elements)
         * Sets.newHashSet(Iterator<? extends E> elements)
         * Sets.newHashSetWithExpectedSize(int expectedSize)
         *
         * Sets.newLinkedHashSet()
         * Sets.newLinkedHashSet(Iterable<? extends E> elements)
         * Sets.newLinkedHashSetWithExpectedSize(int expectedSize)
         *
         * Sets.newTreeSet()
         * Sets.newTreeSet(Iterable<? extends E> elements)
         * Sets.newTreeSet(Comparator<? super E> comparator)
         *
         * 过滤
         * Sets.filter(Set, Predicate)
         */
    }
}
