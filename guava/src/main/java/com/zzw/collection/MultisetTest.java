package com.zzw.collection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Multiset 可重复集合
 */
public class MultisetTest
{

    /*
     * 可重复集合 Multiset 的核心在 count，不能为负数
     * 1、HashMultiset          没有顺序，元素存放于 HashMap
     * 2、TreeMultiset          自然顺序，元素存放于 TreeMap
     * 3、LinkedHashMultiset    插入顺序，元素存放于 LinkedHashMap
     * 4、EnumMultiset          元素必须是 enum 类型
     * 5、ImmutableMultiset     不可修改的 Multiset
     *
     * Map                  Corresponding Multiset    Supports null elements
     * HashMap              HashMultiset              是
     * TreeMap              TreeMultiset              是
     * LinkedHashMap        LinkedHashMultiset        是
     * ConcurrentHashMap    ConcurrentHashMultiset    否
     * ImmutableMap         ImmutableMultiset         否
     */

    /*
     * Multiset
     *
     * add(E)
     * add(E, int)
     *
     * contains(Object)
     * containsAll(Collection<?>)
     *
     * count(Object)
     * setCount(E)
     * setCount(E, int)
     *
     * remove(Object)
     * remove(Object, int)
     * removeAll(Collection<?>)
     * retainAll(Collection<?>)
     *
     * Set<E>        elementSet()
     * Set<Entry<E>> entrySet()
     *
     * equals()
     * forEach(Consumer<? super E>)
     * forEachEntry(ObjIntConsumer<? super E> action)
     * Iterator<E> iterator()
     * size()
     *
     * **********************************************
     *
     * SortedMultiset
     *
     * Comparator<? super E> comparator()
     *
     * Entry<E> firstEntry()
     * Entry<E> lastEntry()
     *
     * Entry<E> pollFirstEntry()
     * Entry<E> pollLastEntry()
     *
     * NavigableSet<E> elementSet()
     * Set<Entry<E>> entrySet()
     *
     * SortedMultiset<E> descendingMultiset()
     * SortedMultiset<E> headMultiset(E upperBound, BoundType boundType)
     * SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType)
     * SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType)
     */

    @Test
    public void test()
    {
        List<String> words = Lists.newArrayList("hello", "world", "hello");

        // 词频统计 HashMap
        Map<String, Integer> counts = new HashMap<>();
        for (String word : words)
        {
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }
        assertEquals(2, counts.get("hello"));

        // ------------------------------------------------

        // 词频统计 HashMultiset
        Multiset<String> countMultiset = HashMultiset.create();
        countMultiset.addAll(words);

        // 设置个数
        countMultiset.setCount(null, 2);
        // 添加个数
        countMultiset.add(null);
        countMultiset.add(null, 2);

        // 包含 ? 不考虑个数
        assertTrue(countMultiset.contains(null));
        assertTrue(countMultiset.containsAll(Lists.<String>newArrayList(null, null, null, null, null, null)));

        // 直接打印
        System.out.println(countMultiset);
        // 元素 x 个数
        System.out.println(countMultiset.entrySet());
        // 唯一元素
        System.out.println(countMultiset.elementSet());

        // 删除元素
        countMultiset.remove(null, 5);

        // 查询个数
        assertEquals(2, countMultiset.count("hello"));
        // 元素总个数
        assertEquals(3, countMultiset.size());
        // 唯一元素个数
        assertEquals(2, countMultiset.entrySet().size());
        assertEquals(2, countMultiset.elementSet().size());
    }
}
