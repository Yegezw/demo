package com.zzw.collection;

import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Table
 */
public class TableTest
{

    /*
     * Table<R, C, V>
     * RowKey + ColumnKye + Value
     * HashBasedTable    HashMap<R, HashMap<C, V>>
     * TreeBasedTable    TreeMap<R, TreeMap<C, V>>
     * ImmutableTable    ImmutableMap<R, ImmutableMap<C, V>>
     * ArrayTable        要求在构造时就指定行和列的大小，本质上由一个二维数组实现，以提升密集 Table 的访问速度和的内存利用率
     */

    @Test
    public void test1()
    {
        // RowKey + ColumnKye + Value
        Table<String, String, Integer> table = HashBasedTable.create();
        table.put("jack", "java", 98);
        table.put("jack", "php", 65);
        table.put("jack", "ui", 80);
        table.put("jack", "mysql", 86);
        assertEquals("{jack={java=98, php=65, ui=80, mysql=86}}", table.toString());

        // 遍历
        Set<Table.Cell<String, String, Integer>> cells = table.cellSet();
        cells.forEach(
                c -> System.out.println(c.getRowKey() + "." + c.getColumnKey() + "=" + c.getValue())
        );
    }

    @Test
    public void test2()
    {
        // RowKey + ColumnKye + Value
        Table<String, Integer, Integer> table = HashBasedTable.create();
        table.put("A", 1, 100);
        table.put("A", 2, 101);
        table.put("B", 1, 200);
        table.put("B", 2, 201);

        // 是否存在映射
        assertTrue(table.containsRow("A"));
        assertTrue(table.containsColumn(2));
        assertTrue(table.containsValue(201));
        assertFalse(table.contains("A", 3));

        // 获取映射，返回 Value
        assertEquals(201, table.get("B", 2));

        // 删除映射，返回 Value
        assertEquals(101, table.remove("A", 2));
    }

    @Test
    public void test3()
    {
        // RowKey + ColumnKye + Value
        HashBasedTable<String, String, String> table = HashBasedTable.create();

        // 使用员工详细信息初始化表
        table.put("IBM", "101", "Mahesh");
        table.put("IBM", "102", "Ramesh");
        table.put("IBM", "103", "Suresh");

        table.put("Microsoft", "111", "Sohan");
        table.put("Microsoft", "112", "Mohan");
        table.put("Microsoft", "113", "Rohan");

        table.put("TCS", "121", "Ram");
        table.put("TCS", "102", "Shyam");
        table.put("TCS", "123", "Sunil");

        // 获取表格的所有唯一键
        Set<String> employers = table.rowKeySet();
        System.out.println("雇主: " + Joiner.on(" ").join(employers));

        // 获取 IBM 对应的 Map
        // 返回指定 RowKey 下的所有 ColumnKye 与 Value 映射
        Map<String, String> ibm = table.row("IBM");
        ibm.forEach((key, value) -> System.out.printf("Emp Id: %s, Name: %s\n", key, value));

        // 得到工号为 102 的雇佣关系
        // 返回指定 ColumnKye 下的所有 RowKey 与 Value 映射
        Map<String, String> relations = table.column("102");
        relations.forEach((key, value) -> System.out.printf("雇主: %s, 雇员: %s\n", key, value));

        // ------------------------------------------------

        // 常用方法

        // 所有数据 RowKey + ColumnKye 为组合键
        Set<Table.Cell<String, String, String>> cells = table.cellSet();
        System.out.println(cells);

        // RowKey 为键
        Map<String, Map<String, String>> map1 = table.rowMap();
        Set<String>                      set1 = table.rowKeySet();
        Map<String, String>              row1 = table.row("IBM");
        System.out.println(map1);
        System.out.println(set1);
        System.out.println(row1);

        // ColumnKye 为键
        Map<String, Map<String, String>> map2 = table.columnMap();
        Set<String>                      set2 = table.columnKeySet();
        Map<String, String>              col2 = table.column("102");
        System.out.println(map2);
        System.out.println(set2);
        System.out.println(col2);
    }
}
