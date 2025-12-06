package com.zzw.collection.util;

import com.google.common.base.Function;
import com.google.common.collect.*;
import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Multimaps
 */
public class MultimapsTest
{

    /*
     * Multimaps
     * ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction)
     * <M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest)
     * SetMultimap<K, V> forMap(Map<K, V> map)
     */

    @Test
    public void test1()
    {
        // 根据字符串的长度对字符串进行分组
        ImmutableSet<String> digits = ImmutableSet.of(
                "zero", "one", "two", "three", "four",
                "five", "six", "seven", "eight", "nine"
        );
        Function<String, Integer> lengthFunction = String::length;

        /*
         * 3 => { "one", "two", "six" }
         * 4 => { "zero", "four", "five", "nine" }
         * 5 => { "three", "seven", "eight" }
         */
        ImmutableListMultimap<Integer, String> digitsByLength = Multimaps.index(digits, lengthFunction);
        System.out.println(digitsByLength);
    }

    @Test
    public void test2()
    {
        ArrayListMultimap<String, Integer> multimap = ArrayListMultimap.create();
        multimap.putAll("b", Ints.asList(2, 4, 6));
        multimap.putAll("a", Ints.asList(4, 2, 1));
        multimap.putAll("c", Ints.asList(2, 5, 3));

        /*
         * 1 => { "a" }
         * 2 => { "a", "b", "c" }
         * 3 => { "c" }
         * 4 => { "a", "b" }
         * 5 => { "c" }
         * 6 => { "b" }
         */
        TreeMultimap<Integer, String> inverse = Multimaps.invertFrom(multimap, TreeMultimap.<Integer, String>create());
        System.out.println(inverse);

        // 如果使用的是 ImmutableMultimap，改用 ImmutableMultimap.inverse()
    }

    @Test
    public void test3()
    {
        Map<String, Integer> map = ImmutableMap.of(
                "a", 1,
                "b", 1,
                "c", 2
        );

        // 将 Map 转换为 SetMultimap [ "a" => {1}, "b" => {1}, "c" => {2} ]
        SetMultimap<String, Integer> multimap = Multimaps.forMap(map);
        System.out.println(multimap);

        // 将 multimap 翻转 [ 1 => {"a", "b"}, 2 => {"c"} ]
        Multimap<Integer, String> inverse = Multimaps.invertFrom(multimap, HashMultimap.<Integer, String>create());
        System.out.println(inverse);
    }

    @Test
    public void test4()
    {
        /*
         * Wrappers
         *
         * Multimap type        Unmodifiable                     Synchronized                     Custom
         * Multimap             unmodifiableMultimap             synchronizedMultimap             newMultimap
         * ListMultimap         unmodifiableListMultimap         synchronizedListMultimap         newListMultimap
         * SetMultimap          unmodifiableSetMultimap          synchronizedSetMultimap          newSetMultimap
         * SortedSetMultimap    unmodifiableSortedSetMultimap    synchronizedSortedSetMultimap    newSortedSetMultimap
         */
    }
}
