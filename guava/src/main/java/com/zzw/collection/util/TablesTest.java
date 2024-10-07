package com.zzw.collection.util;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * Tables
 */
public class TablesTest
{

    /*
     * 不可变单元格
     * Table.Cell<R, C, V> immutableCell(R rowKey, C columnKey, V value)
     *
     * 自定义表
     * Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory)
     *
     * 翻转
     * Table<C, R, V> transpose(Table<R, C, V> table)
     * Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function)
     *
     * 包装
     * unmodifiableTable
     * unmodifiableRowSortedTable
     */

    @Test
    public void test()
    {
        // use LinkedHashMaps instead of HashMaps
        Table<String, Character, Integer> table = Tables.newCustomTable(
                Maps.<String, Map<Character, Integer>>newLinkedHashMap(),
                new Supplier<Map<Character, Integer>>()
                {
                    public Map<Character, Integer> get()
                    {
                        return Maps.newLinkedHashMap();
                    }
                }
        );
    }
}
