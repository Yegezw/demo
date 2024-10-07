package com.zzw.collection.util;

import org.junit.jupiter.api.Test;

/**
 * Iterables
 */
public class IterablesTest
{

    /*
     * 大多数 Iterables 方法在 Iterators 中都有一个接受原始迭代器的相应版本
     *
     * Iterables 类中的绝大多数操作都是惰性的
     * 它们只在绝对必要时推进后备迭代，返回 Iterables 的方法本身返回的是惰性计算的视图，而不是在内存中显式构造一个集合
     *
     * 从 Guava 12 开始，Iterables 由 FluentIterable 类补充，该类包装了 Iterable 并为其中许多操作提供了 "流畅" 语法
     */

    @Test
    public void test1()
    {
        /*
         * 串联
         * Iterables.concat(Iterable...)
         * Iterables.concat(Iterable<Iterable>)
         *
         * 对象出现的次数
         * Iterables.frequency(Iterable, Object)
         * Collections.frequency(Collection, Object)
         *
         * 分区，不可修改视图
         * Iterables.partition(Iterable, int)
         * Iterables.paddedPartition(Iterable, int)
         * Lists.partition(List, int)
         *
         * 首元素
         * Iterables.getFirst(Iterable, T default)
         * Iterable.iterator().next()
         * FluentIterable.first()
         *
         * 尾元素
         * Iterables.getLast(Iterable)
         * Iterables.getLast(Iterable, T default)
         * FluentIterable.last()
         *
         * 相同
         * Iterables.elementsEqual(Iterable, Iterable)
         * List.equals(Object)
         *
         * 不可修改视图
         * Iterables.unmodifiableIterable(Iterable)
         * Collections.unmodifiableCollection(Collection)
         *
         * 最多 limitSize 个
         * Iterables.limit(Iterable, int)
         * FluentIterable.limit(int)
         *
         * 返回 Iterable 中的唯一元素，空或者多个则快速失败
         * Iterables.getOnlyElement(Iterable)
         * Iterables.getOnlyElement(Iterable, T default)
         */
    }

    @Test
    public void test2()
    {
        /*
         * 在集合上支持这些操作，但在可迭代对象上不支持
         * 当 input 是 Collection 时，这些操作中的每一个都会委托给相应的 Collection 接口方法
         * 如果 Iterables.size() 传递了一个 Collection，它将调用 Collection.size() 方法，而不是遍历迭代器
         *
         * Iterables method                                       Analogous Collection method         FluentIterable equivalent
         * addAll(Collection addTo, Iterable toAdd)               Collection.addAll(Collection)
         * contains(Iterable, Object)                             Collection.contains(Object)         FluentIterable.contains(Object)
         * removeAll(Iterable removeFrom, Collection toRemove)    Collection.removeAll(Collection)
         * retainAll(Iterable removeFrom, Collection toRetain)    Collection.retainAll(Collection)
         * size(Iterable)                                         Collection.size()                   FluentIterable.size()
         * toArray(Iterable, Class)                               Collection.toArray(T[])             FluentIterable.toArray(Class)
         * isEmpty(Iterable)                                      Collection.isEmpty()                FluentIterable.isEmpty()
         * get(Iterable, int)                                     List.get(int)                       FluentIterable.get(int)
         * toString(Iterable)                                     Collection.toString()               FluentIterable.toString()
         */
    }

    @Test
    public void test3()
    {
        /*
         * FluentIterable.toImmutableSet()
         * FluentIterable.toImmutableList()
         * FluentIterable.toImmutableSortedSet(Comparator)
         */
    }

    @Test
    public void test4()
    {
        /*
         * 循环迭代
         * Iterables.cycle(Iterable)
         *
         * 跳过
         * Iterables.skip(Iterable, int)
         *
         * 过滤
         * Iterables.filter(Iterable, Class)
         * Iterables.filter(Iterable, Predicate)
         *
         * 变换
         * Iterables.transform(Iterable, Function)
         *
         * 删除
         * Iterables.removeIf(Iterable, Predicate)
         *
         * 查询
         * Iterables.any(Iterable, Predicate)
         * Iterables.all(Iterable, Predicate)
         * Iterables.find(Iterable, Predicate)
         * Iterables.tryFind(Iterable, Predicate)
         * Iterables.indexOf(Iterable, Predicate)
         */
    }
}
