package com.zzw.range;

import com.google.common.collect.BoundType;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Range 区间
 * <table>
 * <caption>Range Types</caption>
 * <tr><th>Notation        <th>Definition               <th>Factory method
 * <tr><td>{@code (a..b)}  <td>{@code {x | a < x < b}}  <td>{@link Range#open open}
 * <tr><td>{@code [a..b]}  <td>{@code {x | a <= x <= b}}<td>{@link Range#closed closed}
 * <tr><td>{@code (a..b]}  <td>{@code {x | a < x <= b}} <td>{@link Range#openClosed openClosed}
 * <tr><td>{@code [a..b)}  <td>{@code {x | a <= x < b}} <td>{@link Range#closedOpen closedOpen}
 * <tr><td>{@code (a..+∞)} <td>{@code {x | x > a}}      <td>{@link Range#greaterThan greaterThan}
 * <tr><td>{@code [a..+∞)} <td>{@code {x | x >= a}}     <td>{@link Range#atLeast atLeast}
 * <tr><td>{@code (-∞..b)} <td>{@code {x | x < b}}      <td>{@link Range#lessThan lessThan}
 * <tr><td>{@code (-∞..b]} <td>{@code {x | x <= b}}     <td>{@link Range#atMost atMost}
 * <tr><td>{@code (-∞..+∞)}<td>{@code {x}}              <td>{@link Range#all all}
 * </table>
 */
public class RangeTest
{

    /*
     * Range.open()
     * Range.closed()
     *
     * Range.openClosed()
     * Range.closedOpen()
     *
     * Range.lessThan()
     * Range.greaterThan()
     *
     * Range.atLeast()
     * Range.atMost()
     *
     * Range.all()
     * Range.singleton()
     *
     * Range<C> encloseAll(Iterable<C> values)
     *
     * Range<C> upTo(C endpoint, BoundType boundType)      Bottom
     * Range<C> downTo(C endpoint, BoundType boundType)    Top
     * Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType)
     *
     * Range<C> canonical(DiscreteDomain<C> domain) 规范形式
     *
     * **************************************************************************
     *
     * Range.contains()
     * Range.containsAll()
     * Range.isEmpty()
     *
     * Range.hasLowerBound()
     * Range.hasUpperBound()
     *
     * Range.lowerEndpoint()
     * Range.upperEndpoint()
     *
     * Range.lowerBoundType()
     * Range.upperBoundType()
     *
     * 包含 Range.encloses()
     * 相连 Range.isConnected()
     * 交集 Range.intersection()
     * 组合 Range.span()
     */

    @Test
    public void test1()
    {
        Range<Integer> r1 = Range.closed(1, 3);
        Range<Integer> r2 = Range.atLeast(5);

        assertEquals("[1..3]", r1.toString());
        assertEquals("[5..+∞)", r2.toString());

        assertTrue(r1.contains(2));
        assertFalse(r1.contains(4));
        assertTrue(r2.containsAll(Ints.asList(5, 6, 7)));
    }

    @Test
    public void test2()
    {
        // isEmpty() 判断是否为空区间

        assertFalse(Range.closed(4, 4).isEmpty());
        assertTrue(Range.closedOpen(4, 4).isEmpty());
        assertTrue(Range.openClosed(4, 4).isEmpty());

        // ------------------------------------------------

        // hasLowerBound()、hasUpperBound() 判断区间是否有特定边界

        assertTrue(Range.closedOpen(3, 3).hasLowerBound());
        assertTrue(Range.closedOpen(3, 3).hasUpperBound());

        // ------------------------------------------------

        // lowerEndpoint()、upperEndpoint() 返回区间的端点值，没有则 IllegalStateException

        assertEquals(11, Range.open(11, 20).lowerEndpoint());
        assertEquals(20, Range.open(11, 20).upperEndpoint());

        assertEquals(11, Range.closed(11, 20).lowerEndpoint());
        assertEquals(20, Range.closed(11, 20).upperEndpoint());

        assertAll(
                "range unbounded on this side",
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> Range.atLeast(5).upperEndpoint()
                ),
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> Range.atMost(5).lowerEndpoint()
                )
        );

        // ------------------------------------------------

        // lowerBoundType()、upperBoundType() 返回区间边界类型，没有则 IllegalStateException

        assertEquals(BoundType.CLOSED, Range.closedOpen(3, 10).lowerBoundType());
        assertEquals(BoundType.OPEN, Range.closedOpen(3, 10).upperBoundType());

        assertAll(
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> Range.atLeast(5).upperBoundType()
                ),
                () -> assertThrows(
                        IllegalStateException.class,
                        () -> Range.atMost(5).lowerBoundType()
                )
        );
    }

    @Test
    public void test3()
    {
        Range<Integer> r1 = Range.open(1, 10);
        Range<Integer> r2 = Range.closed(2, 3);
        Range<Integer> r3 = Range.closed(1, 3);

        // encloses() 包含
        assertTrue(r1.encloses(r2));
        assertFalse(r1.encloses(r3));

        // isConnected() 相连
        assertTrue(Range.closed(3, 5).isConnected(Range.open(5, 10)));
        assertTrue(Range.closed(0, 5).isConnected(Range.closed(3, 9)));
        assertFalse(Range.open(3, 5).isConnected(Range.open(5, 10)));
        assertFalse(Range.closed(1, 5).isConnected(Range.closed(6, 10)));

        // intersection() 交集，没有交集则 IllegalArgumentException
        assertEquals("(5..5]", Range.closed(3, 5).intersection(Range.open(5, 10)).toString());
        assertEquals("[3..4]", Range.closed(0, 9).intersection(Range.closed(3, 4)).toString());
        assertEquals("[3..5]", Range.closed(0, 5).intersection(Range.closed(3, 9)).toString());

        assertAll(
                "intersection is undefined for disconnected ranges A and B",
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Range.open(3, 5).intersection(Range.open(5, 10))
                ),
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> Range.closed(1, 5).intersection(Range.closed(6, 10))
                )
        );

        // span() 同时包括两个区间的最小区间
        assertEquals("[0..9]", Range.closed(0, 9).span(Range.closed(3, 4)).toString());
        assertEquals("[0..9]", Range.closed(0, 5).span(Range.closed(3, 9)).toString());
        assertEquals("(3..10)", Range.open(3, 5).span(Range.open(5, 10)).toString());
        assertEquals("[3..10)", Range.closed(3, 5).span(Range.open(5, 10)).toString());
        assertEquals("[1..10]", Range.closed(1, 5).span(Range.closed(6, 10)).toString());
    }

    @Test
    public void test4()
    {
        // TreeMap 有序，默认根据 ASCII 码表顺序
        TreeMap<String, Integer> treeMap = Maps.newTreeMap();
        treeMap.put("Scala", 1);
        treeMap.put("Guava", 2);
        treeMap.put("Java", 3);
        treeMap.put("Kafka", 4);
        assertEquals("{Guava=2, Java=3, Kafka=4, Scala=1}", treeMap.toString());

        NavigableMap<String, Integer> subMap = Maps.subMap(treeMap, Range.open("Guava", "Scala"));
        assertEquals("{Java=3, Kafka=4}", subMap.toString());
    }
}
