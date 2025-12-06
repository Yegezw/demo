package com.zzw.collection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BiMap k v 都是唯一的
 */
public class BimapTest
{

    /*
     * Implementation    Map Impl
     * HashBiMap         HashMap
     * ImmutableBiMap    ImmutableMap
     * EnumBiMap         EnumMap
     * EnumHashBiMap     EnumMap
     *
     * BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) 位于 Maps 中
     */

    @Test
    public void test()
    {
        BiMap<String, String> map = HashBiMap.create();
        map.put("A", "a");
        map.put("B", "b");
        map.put("C", "c");
        map.put(null, null);

        // ------------------------------------------------

        // 继续添加 D -> c

        // 普通添加，报错 value already present: c
        assertThrows(IllegalArgumentException.class, () -> map.put("D", "c"));

        // 强制添加，不会报错
        map.forcePut("D", "c");
        assertEquals("c", map.get("D"));
        // 强制添加导致 C -> c 被删除了
        assertFalse(map.containsKey("C"));
        assertNull(map.get("C"));

        // ------------------------------------------------

        /*
         * inverse() 返回一个反转后的 BiMap，即 key / value 互相切换的映射
         * 这个反转的 map 并不是一个新的 map，而是一个视图
         * 在这个反转后的 map 中的任何增删改操作都会影响原来的 map
         */

        BiMap<String, String> inverse = map.inverse();
        assertSame(map, inverse.inverse());
        assertEquals("D", inverse.get("c"));

        inverse.put("c", "C");
        assertEquals("c", map.get("C"));

        // ------------------------------------------------

        System.out.println(map.values());
        System.out.println(inverse.values());
    }
}
