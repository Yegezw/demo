package com.zzw.collection.util;

import org.junit.jupiter.api.Test;

/**
 * Maps
 */
public class MapsTest
{

    @Test
    public void test1()
    {
        /*
         * 有许多对象都有一些独特的属性，我们希望能根据这些属性来查找这些对象
         * Maps.uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction)
         * Maps.uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction)
         *
         * ****************************************************************************************
         *
         * 差异
         * Maps.difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right)
         *
         * MapDifference.areEqual()              两个映射没有差异 ?
         * MapDifference.entriesInCommon()       两个映射中的交集，具有匹配的键和值
         * MapDifference.entriesDiffering()      键相同但值不同的条目
         *                                       返回的 Value 类型为 MapDifference.ValueDifference
         *                                       MapDifference.ValueDifference.leftValue()
         *                                       MapDifference.ValueDifference.rightValue()
         * MapDifference.entriesOnlyOnLeft()     键位于左侧但不在右侧映射中的条目
         * MapDifference.entriesOnlyOnRight()    键位于右侧但不在左侧映射中的条目
         */
    }

    @Test
    public void test2()
    {
        /*
         * BiMap utility                    Corresponding Map utility
         * Maps.synchronizedBiMap(BiMap)    Collections.synchronizedMap(Map)
         * Maps.unmodifiableBiMap(BiMap)    Collections.unmodifiableMap(Map)
         */
    }

    @Test
    public void test3()
    {
        /*
         * 静态工厂
         *
         * Maps.newHashMap()
         * Maps.newHashMap(Map<? extends K, ? extends V> map)
         * Maps.newHashMapWithExpectedSize(int expectedSize)
         *
         * Maps.newLinkedHashMap()
         * Maps.newLinkedHashMap(Map<? extends K, ? extends V> map)
         * Maps.newLinkedHashMapWithExpectedSize(int expectedSize)
         *
         * Maps.newTreeMap()
         * Maps.newTreeMap(Comparator<C> comparator)
         * Maps.newTreeMap(SortedMap<K, ? extends V> map)
         *
         * Maps.newEnumMap(Class<K> type)
         * Maps.newEnumMap(Map<K, ? extends V> map)
         *
         * Maps.newConcurrentMap()
         * Maps.newIdentityHashMap()
         */
    }

    @Test
    public void test4()
    {
        /*
         * 子集
         * NavigableMap<K, V> subMap(NavigableMap<K, V> map, Range<K> range)
         *
         * 过滤
         * Maps.filterKeys(Map, Predicate)
         * Maps.filterValues(Map, Predicate)
         * Maps.filterEntries(Map, Predicate)
         *
         * 转换，每次 get(key) 都会调用 transformer
         * Map<K, V2> Maps.transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function)
         * Map<K, V2> Maps.transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer)
         *
         * Set 转换 Map
         * Map<K, V>          Maps.asMap(Set<K> set, Function<? super K, V> function)                     每次 get(key) 都会调用 function
         * ImmutableMap<K, V> Maps.toMap(Iterable<K> keys, Function<? super K, V> valueFunction)          预先计算所有值，返回不可变映射
         * ImmutableMap<K, V> Maps.uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction)    预先计算所有键，返回不可变映射
         */
    }
}
