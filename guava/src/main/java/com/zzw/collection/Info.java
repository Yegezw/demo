package com.zzw.collection;

public class Info
{
    /*
     * Interface    JDK or Guava    Corresponding Guava utility class
     *
     * Collection       JDK         Collections2
     * List             JDK         Lists
     * Set              JDK         Sets
     * SortedSet        JDK         Sets
     * Map              JDK         Maps
     * SortedMap        JDK         Maps
     * Queue            JDK         Queues
     *
     * Multiset        Guava        Multisets
     * Multimap        Guava        Multimaps
     * BiMap           Guava        Maps
     * Table           Guava        Tables
     *
     * 视图 View 是一种动态的数据结构
     * 副本 Copy 是原始数据的一份物理拷贝
     *
     * 过滤    filter(Predicate)     Lists 中不存在
     * 转换    transform(Function)   Sets  中不存在
     */


    /*
     * Collection
     *
     * add
     * addAll
     *
     * clear
     * isEmpty
     *
     * equals
     * contains
     * containsAll
     *
     * remove
     * removeAll
     * removeIf
     * retainAll
     *
     * size
     * stream
     * toArray
     * iterator
     */


    /*
     * SortedSet
     *
     * Comparator<? super E> comparator()
     *
     * SortedSet<E> subSet(E fromElement, E toElement)
     * SortedSet<E> headSet(E toElement)
     * SortedSet<E> tailSet(E fromElement)
     *
     * E first()
     * E last()
     *
     * ****************************************************
     *
     * NavigableSet
     *
     * E lower(E e)      <
     * E floor(E e)      <=
     *
     * E ceiling(E e)    >=
     * E higher(E e)     >
     *
     * E pollFirst()
     * E pollLast()
     *
     * Iterator<E> iterator()
     * NavigableSet<E> descendingSet()    翻转集合
     * Iterator<E> descendingIterator()   翻转迭代器
     *
     * NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
     * NavigableSet<E> headSet(E toElement, boolean inclusive)
     * NavigableSet<E> tailSet(E fromElement, boolean inclusive)
     */


    /*
     * Map
     *
     * of
     *
     * put
     * putAll
     * putIfAbsent
     *
     * get
     * getOrDefault
     *
     * clear
     * isEmpty
     *
     * equals
     * containsKey
     * containsValue
     *
     * remove
     * replace(k, v)                      存在 k 才替换
     * replace(k, oldV, newV)             类似 CAS
     * replaceAll(BiFunction<K, V, V>)    替换所有值
     *
     * merge
     * compute
     * computeIfAbsent
     * computeIfPresent
     *
     * ****************************************************
     *
     * SortedMap
     *
     * Comparator<? super K> comparator()
     *
     * SortedMap<K,V> subMap(K fromKey, K toKey)
     * SortedMap<K,V> headMap(K toKey)
     * SortedMap<K,V> tailMap(K fromKey)
     *
     * K firstKey()
     * K lastKey()
     *
     * ****************************************************
     *
     * NavigableMap
     *
     * Map.Entry<K,V> lowerEntry(K key)      <
     * K lowerKey(K key)                     <
     * Map.Entry<K,V> floorEntry(K key)      <=
     * K floorKey(K key)                     <=
     * Map.Entry<K,V> ceilingEntry(K key)    >=
     * K ceilingKey(K key)                   >=
     * Map.Entry<K,V> higherEntry(K key)     >
     * K higherKey(K key)                    >
     *
     * Map.Entry<K,V> firstEntry()
     * Map.Entry<K,V> lastEntry()
     *
     * Map.Entry<K,V> pollFirstEntry()
     * Map.Entry<K,V> pollLastEntry()
     *
     * NavigableMap<K,V> descendingMap()     翻转 M 集合
     * NavigableSet<K> navigableKeySet()     翻转 K 集合
     * NavigableSet<K> descendingKeySet()    翻转 K 集合
     *
     * NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
     * NavigableMap<K,V> headMap(K toKey, boolean inclusive)
     * NavigableMap<K,V> tailMap(K fromKey, boolean inclusive)
     */
}
