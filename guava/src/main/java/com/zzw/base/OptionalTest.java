package com.zzw.base;

import com.google.common.base.Optional;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Optional
 */
public class OptionalTest
{

    @Test
    public void test()
    {
        // guava.Optional 与 jdk.Optional 大致相同

        List<Optional<Integer>> list = Arrays.asList(
                Optional.of(1),
                Optional.absent(),
                Optional.of(3)
        );
        // 从给定的 Optional 集合中提取所有非空的值
        Iterable<Integer> values = Optional.presentInstances(list);
        values.forEach(System.out::println);
    }
}
