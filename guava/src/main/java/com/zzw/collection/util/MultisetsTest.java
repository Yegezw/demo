package com.zzw.collection.util;

import org.junit.jupiter.api.Test;

/**
 * Multisets
 */
public class MultisetsTest
{

    @Test
    public void test1()
    {
        /*
         * 包含: sub.count(o) <= super.count(o)
         * Multisets.containsOccurrences(Multiset super, Multiset sub)
         * Collection.containsAll 忽略计数，只测试是否包含元素
         *
         * 删除: 对于 toRemove 中元素的每个匹配项，删除 removeFrom 中的一个匹配项
         * Multisets.removeOccurrences(Multiset removeFrom, Multiset toRemove)
         * Collection.removeAll 会删除任何元素的所有匹配项，即使元素在 toRemove 仅中出现一次
         *
         * 保留: removeFrom.count(o) <= toRetain.count(o)
         * Multisets.retainOccurrences(Multiset removeFrom, Multiset toRetain)
         * Collection.retainAll 保留所有在 toRetain 中出现的元素，哪怕仅出现一次
         *
         * 并集: 相同元素的计数相加
         * Multisets.sum(Multiset1, Multiset2)
         *
         * 并集: 相同元素取最大计数
         * Multisets.union(Multiset1, Multiset2)
         *
         * 交集
         * Multisets.intersection(Multiset1, Multiset2)
         *
         * 差集 Set1 有 Set2 没有
         * Multisets.difference(Multiset1, Multiset2)
         */
    }

    @Test
    public void test2()
    {
        /*
         * 返回 multiset 的不可变副本，该副本按频率降序迭代元素
         * ImmutableMultiset<E> Multisets.copyHighestCountFirst(Multiset<E>)
         *
         * 返回 multiset 的不可修改视图
         * Multiset<E> Multisets.unmodifiableMultiset(Multiset<E>)
         *
         * 返回已排序的 multiset 的不可修改视图
         * SortedMultiset<E> Multisets.unmodifiableSortedMultiset(SortedMultiset<E>)
         */
    }
}
