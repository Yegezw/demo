package com.zzw.collection;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.junit.jupiter.api.Test;

/**
 * RangeMap
 */
public class RangeMapTest
{

    /*
     * RangeMap 是一种集合类型，用于描述从不相交的非空范围到值的映射
     * 与 RangeSet 不同，RangeMap 永远不会合并相邻映射，即使相邻范围映射到相同的值也是如此
     */

    @Test
    public void test1()
    {
        RangeMap<Integer, String> rangeMap = TreeRangeMap.create();

        // { [1..10] => "foo" }
        rangeMap.put(Range.closed(1, 10), "foo");
        // { [1..3] => "foo", (3..6) => "bar", [6..10] => "foo" }
        rangeMap.put(Range.open(3, 6), "bar");

        // { [1..3] => "foo", (3..6) => "bar", [6..10] => "foo", (10..20) => "foo" }
        rangeMap.put(Range.open(10, 20), "foo");
        // { [1..3] => "foo", (3..5) => "bar", (11..20) => "foo" }
        rangeMap.remove(Range.closed(5, 11));
    }

    @Test
    public void test2()
    {
        /*
         * RangeMap<K, V>
         *
         * asMapOfRanges()          以 Map<Range<K>, V> 的形式查看 RangeMap，可用于迭代 RangeMap
         * subRangeMap(Range<K>)    将 RangeMap<K, V> 与指定 Range<K> 的交集视为 RangeMap<K, V>
         */
    }
}
