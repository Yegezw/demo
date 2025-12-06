package com.zzw.functional;

/**
 * Functions
 */
public class FunctionsTest
{

    /*
     * Function<E, E>           Functions.identity()
     * Function<Object, E>      Functions.constant(E value)    忽略 Object
     * Function<Object, String> Functions.toStringFunction()
     *
     * Function<K, V>           Functions.forMap(Map<K, V> map)                              map.get(K) 为 null 则 IllegalArgumentException
     * Function<K, V>           Functions.forMap(Map<K, ? extends V> map, V defaultValue)    map.get(K) 为 null 则 defaultValue
     *
     * Function<F, T>           Functions.forSupplier(Supplier<T> supplier)    忽略 F
     * Function<T, Boolean>     Functions.forPredicate(Predicate<T> predicate)
     * Function<A, C>           Functions.compose(Function<B, C> g, Function<A, ? extends B> f)    组合
     */
}
