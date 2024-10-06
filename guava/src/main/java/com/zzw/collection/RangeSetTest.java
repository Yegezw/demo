package com.zzw.collection;

import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RangeSet
 */
public class RangeSetTest
{

    /*
     * RangeSet 描述了一组互不相连的非空范围
     * 向可变 RangeSet 添加范围时，任何相连的范围都会合并在一起，空范围会被忽略
     */

    @Test
    public void test1()
    {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();

        // ------------------------------------------------

        // 初始    { [1..10] }
        rangeSet.add(Range.closed(1, 10));
        // 断开    { [1..10], [11..15) }
        rangeSet.add(Range.closedOpen(11, 15));
        assertEquals("[[1..10], [11..15)]", rangeSet.toString());

        // 连接    { [1..10], [11..20) }
        rangeSet.add(Range.closedOpen(15, 20));
        // 无效    { [1..10], [11..20) }
        rangeSet.add(Range.openClosed(0, 0));

        // 分割    { [1..5], [10..10], [11..20) }
        rangeSet.remove(Range.open(5, 10));
    }

    @Test
    public void test2()
    {
        RangeSet<Integer> rangeSet = TreeRangeSet.create();

        // ------------------------------------------------

        // 对 Range 规范形式后添加
        DiscreteDomain<Integer> domain = DiscreteDomain.integers();

        // 初始    { [1..11) }
        rangeSet.add(Range.closed(1, 10).canonical(domain));
        // 断开    { [1..15) }
        rangeSet.add(Range.closedOpen(11, 15).canonical(domain));

        // 连接    { [1..20) }
        rangeSet.add(Range.closedOpen(15, 20).canonical(domain));
        // 无效    { [1..20) }
        rangeSet.add(Range.openClosed(0, 0).canonical(domain));

        // 分割    { [1..6), [10..20) }
        rangeSet.remove(Range.open(5, 10).canonical(domain));
        
        rangeSet.asRanges();
    }

    @Test
    public void test3()
    {
        /*
         * RangeSet<C>
         *
         * contains(C)           查询 RangeSet 中是否有 Range 包含指定元素
         * rangeContaining(C)    返回包含指定元素的 Range，如果没有则返回 null
         * encloses(Range<C>)    测试 RangeSet 中的任何 Range 是否包含指定的范围
         * span()                返回包含此 RangeSet 中每个 Range 的最小 Range
         */
    }

    @Test
    public void test4()
    {
        /*
         * RangeSet<C>
         *
         * complement()                查看 RangeSet<C> 的补集
         * subRangeSet(Range<C>)       返回 RangeSet<C> 与指定 Range<C> 的交集
         * asRanges()                  会把 RangeSet<C> 视为 Set<Range<C>>，可以迭代
         * asSet(DiscreteDomain<C>)    会把 RangeSet<C> 视为 ImmutableSortedSet<C>，查看区域中的元素，而不是范围本身
         *                             如果 DiscreteDomain 和 RangeSet 在上面都是无界的，或者在下面都是无界的，则不支持此操作
         */
    }
}
