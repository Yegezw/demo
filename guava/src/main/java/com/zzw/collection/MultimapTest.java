package com.zzw.collection;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Multimap Key : Multi Value
 */
public class MultimapTest
{

    /*
     * ListMultimap 或 SetMultimap 分别将 Key 映射到 List 或 Set
     *
     * Implementation	        Keys behave like    Values behave like
     * ArrayListMultimap        HashMap             ArrayList
     * HashMultimap             HashMap             HashSet
     * LinkedListMultimap       LinkedHashMap       LinkedList
     * LinkedHashMultimap       LinkedHashMap       LinkedHashSet
     * TreeMultimap             TreeMap             TreeSet
     * ImmutableListMultimap    ImmutableMap        ImmutableList
     * ImmutableSetMultimap     ImmutableMap        ImmutableSet
     * 
     * 除了不可变的实现之外，这些实现中的每一个都支持 null 键和值
     */

    /*
     * Multimap
     * SortedSetMultimap
     */

    @Test
    public void test()
    {
        ListMultimap<String, String> multimap = MultimapBuilder.hashKeys().arrayListValues().build();

        // 即使没有 value，get() 也不会返回 null
        List<String> list = multimap.get("张三");
        assertNotNull(list);

        // 每次获取到的 List 并非是同一个对象
        assertNotSame(multimap.get("张三"), multimap.get("张三"));

        // 对 list 的修改将直接写入 Multimap
        list.addAll(Arrays.asList("1", "2", "3"));
        list.clear();
        list.addAll(Arrays.asList("1", "2", "3"));
        assertEquals("[1, 2, 3]", multimap.get("张三").toString());

        // ------------------------------------------------

        // 添加
        multimap.put("李四", "1");
        multimap.put("李四", "2");
        multimap.putAll("李四", Arrays.asList("3", "4"));

        // 删除
        multimap.remove("李四", "4");
        assertEquals("[1, 2, 3]", multimap.removeAll("李四").toString());

        // 清空并替换
        multimap.replaceValues("李四", Arrays.asList("4", "5", "6"));
        assertEquals("[4, 5, 6]", multimap.get("李四").toString());

        // ------------------------------------------------

        /*
         * asMap() 将 Multimap<K, V> 视为 Map<K, Collection<V>>
         * 多次 asMap() 返回的是同一个对象，对 asMap() 的操作将作用到 Multimap 中
         *
         * 返回的映射支持 remove() 和对 Collection<V> 的更改写入，但该映射不支持 put 或 putAll
         *
         * 当你希望 get(缺失的 Key) 为 null 而不是一个新的、可写的空集合时，你可以使用 asMap().get(key)
         *
         * 你应该将 asMap.get(key) 转换为适当的集合类型，SetMultimap 的 Set，ListMultimap 的 List
         * 但类型系统不允许 ListMultimap 在 asMap() 处返回 Map<K, List<V>>
         */

        Map<String, Collection<String>> map = multimap.asMap();
        assertSame(multimap.asMap(), multimap.asMap());
        assertThrows(UnsupportedOperationException.class, () -> map.put("1", Arrays.asList("1", "2", "3")));

        map.get("李四").add("9");
        assertEquals(map.get("李四"), multimap.get("李四"));
        assertEquals("[4, 5, 6, 9]", map.remove("李四").toString());
        assertFalse(multimap.containsKey("李四"));

        // ------------------------------------------------

        /*
         * entries()  keySet()  keys()  values()
         * 返回的结果都包装有 multimap 的真实对象，更新将直接作用于 multimap
         */

        multimap.putAll("李四", Arrays.asList("2", "3", "4"));

        // 查看 Multimap 中所有条目的集合 Map.Entry<K, V>
        Collection<Map.Entry<String, String>> entries1 = multimap.entries();
        entries1.forEach(System.out::println);

        // 查看 Multimap 中所有键集合条目 Map.Entry<String, Collection<String>>
        Set<Map.Entry<String, Collection<String>>> entries2 = multimap.asMap().entrySet();
        entries2.forEach(System.out::println);

        // 将 Multimap 中的非重复键视为 Set
        Set<String> keySet = multimap.keySet();
        assertEquals("[李四, 张三]", keySet.toString());

        // 将 Multimap 的键视为 Multiset
        Multiset<String> keys = multimap.keys();
        assertEquals("[李四 x 3, 张三 x 3]", keys.toString());

        Collection<String> values = multimap.values();
        System.out.println(values);

        // ------------------------------------------------

        // 6
        System.out.println(multimap.size());
        // 2
        System.out.println(multimap.keySet().size());
    }
}
