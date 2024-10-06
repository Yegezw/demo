package com.zzw.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.zzw.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Immutable Collections 不可变集合
 */
public class ImmutableCollectionsTest
{

    /*
     * 不可变集合的优点
     * 1、可供不受信任库安全使用
     * 2、线程安全
     * 3、节省时间和空间，所有不可变集合实现都比其可变兄弟姐妹更节省内存
     * 4、可作为常量使用，并期望其保持不变
     *
     * 每个 Guava 不可变集合实现都拒绝 null 值
     * 如果需要使用 null 值，请考虑在允许 null 的集合实现上使用 Collections.unmodifiableList() 及其 friends
     */

    /*
     * Interface             JDK or Guava    Immutable Version
     *
     * Collection                JDK         ImmutableCollection
     * List                      JDK         ImmutableList
     * Set                       JDK         ImmutableSet
     * SortedSet/NavigableSet    JDK         ImmutableSortedSet
     * Map                       JDK         ImmutableMap
     * SortedMap                 JDK         ImmutableSortedMap
     *
     * Multiset                 Guava        ImmutableMultiset
     * SortedMultiset           Guava        ImmutableSortedMultiset
     * Multimap                 Guava        ImmutableMultimap
     * ListMultimap             Guava        ImmutableListMultimap
     * SetMultimap              Guava        ImmutableSetMultimap
     * BiMap                    Guava        ImmutableBiMap
     * ClassToInstanceMap       Guava        ImmutableClassToInstanceMap
     * Table                    Guava        ImmutableTable
     */

    @Test
    public void test1()
    {
        /*
         * 不可变集合的创建
         * 除了排序的集合外，集合中元素的顺序 == 创建时的顺序
         *
         * ImmutableSet.copyOf(set)
         * ImmutableList<E> ImmutableList.sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements)
         *
         * ImmutableSet.of("a", "b", "c")
         * ImmutableMap.of("a", 1, "b", 2)
         *
         * ImmutableSet.<Color>builder()
         *             .addAll(COLORS)
         *             .add(new Color(0, 191, 255))
         *             .build()
         */

        List<Student> list = Lists.newArrayList(
                new Student(1, 1),
                new Student(2, 2),
                new Student(3, 3)
        );
        ImmutableList<Student> immutableList = ImmutableList.copyOf(list);

        list.get(2).age   = 4;
        list.get(2).score = 4;
        System.out.println(list);
        System.out.println(immutableList);
    }

    @Test
    public void test2()
    {
        /*
         * 所有不可变集合都能通过 asList() 提供 ImmutableList 视图
         * 即使数据存储为 ImmutableSortedSet，也能通过 sortedSet.asList().get(k) 获取第 k 个最小元素
         */

        List<Student> list = Lists.newArrayList(
                new Student(3, 3),
                new Student(1, 1),
                new Student(2, 2)
        );

        ImmutableSortedSet<Student> immutableSortedSet = ImmutableSortedSet.copyOf(
                Ordering.natural().onResultOf(student -> student.age),
                list
        );
        ImmutableList<Student> immutableList = immutableSortedSet.asList();
        System.out.println(immutableList.get(1));
    }
}
