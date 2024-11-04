package com.zzw.functional;

/**
 * Predicates
 */
public class PredicatesTest
{

    /*
     * Predicate<T> alwaysTrue()
     * Predicate<T> alwaysFalse()
     *
     * Predicate<T> isNull()
     * Predicate<T> notNull()
     *
     * Predicate<T> not(Predicate<T> predicate)                                     非
     *
     * Predicate<T> and(Iterable<? extends Predicate<? super T>> components)        与
     * Predicate<T> and(Predicate<? super T>... components)                         与
     * Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second)    与
     *
     * Predicate<T> or(Iterable<? extends Predicate<? super T>> components)         或
     * Predicate<T> or(Predicate<? super T>... components)                          或
     * Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second)     或
     *
     * Predicate<T>        equalTo(T target)             相等
     * Predicate<T>        instanceOf(Class<?> clazz)    实例
     * Predicate<Class<?>> subtypeOf(Class<?> clazz)     子类
     * Predicate<T> in(Collection<? extends T> target)   包含
     *
     * Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function)    predicate(A -> B)
     *
     * Predicate<CharSequence> containsPattern(String pattern)    正则
     * Predicate<CharSequence> contains(Pattern pattern)          正则
     */
}
