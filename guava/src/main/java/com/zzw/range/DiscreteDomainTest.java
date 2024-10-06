package com.zzw.range;

import com.google.common.base.Strings;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

/**
 * 离散域 DiscreteDomain
 */
public class DiscreteDomainTest
{

    /**
     * 打印连续集 ContiguousSet
     */
    public static void printInfo(ContiguousSet<? extends Comparable<?>> set, boolean hasBound)
    {
        if (hasBound)
        {
            String str1 = set.toString();
            String str2 = Lists.newArrayList(set).toString();
            String info = Strings.padEnd(str1, 12, ' ') + str2;
            System.out.println(info);
        }
        else
        {
            System.out.println(set);
        }
    }

    /**
     * 自定义小写字符离散域
     */
    private static final class LowerCaseDomain extends DiscreteDomain<Character>
    {
        private static final LowerCaseDomain domain = new LowerCaseDomain();

        private LowerCaseDomain()
        {
        }

        @Override
        public Character next(@Nonnull Character value)
        {
            return (char) (value + 1);
        }

        @Override
        public Character previous(@Nonnull Character value)
        {
            return (char) (value - 1);
        }

        @Override
        public long distance(@Nonnull Character start, @Nonnull Character end)
        {
            return end - start;
        }

        @Nonnull
        @Override
        public Character minValue()
        {
            return 'a';
        }

        @Nonnull
        @Override
        public Character maxValue()
        {
            return 'z';
        }
    }

    @Test
    public void test1()
    {
        /*
         * 连续集 = 范围 + 离散域
         * ContiguousSet.create(range, domain)
         * 并没有真的构造了整个集合，而是返回了 set 形式的区间视图
         * 但是可以进行遍历，从 toString 和 forEach 的使用上可以显示出区别
         */

        // Integer 范围
        Range<Integer> range;
        // Integer 离散域
        DiscreteDomain<Integer> domain = DiscreteDomain.integers();

        // ------------------------------------------

        range = Range.closed(20, 30);     // [20..30]
        printInfo(ContiguousSet.create(range, domain), true);

        range = Range.open(20, 30);       // [21..29]
        printInfo(ContiguousSet.create(range, domain), true);

        // 该 range 的规范形式
        range = range.canonical(domain);
        System.out.println(range);                    // [21..30)

        range = Range.openClosed(20, 30); // [21..30]
        printInfo(ContiguousSet.create(range, domain), true);

        range = Range.closedOpen(20, 30); // [20..29]
        printInfo(ContiguousSet.create(range, domain), true);

        // ------------------------------------------

        range = Range.greaterThan(20);        // [21..2147483647]
        printInfo(ContiguousSet.create(range, domain), false);

        range = Range.atLeast(20);            // [20..2147483647]
        printInfo(ContiguousSet.create(range, domain), false);

        range = Range.lessThan(30);           // [-2147483648..29]
        printInfo(ContiguousSet.create(range, domain), false);

        range = Range.atMost(30);             // [-2147483648..30]
        printInfo(ContiguousSet.create(range, domain), false);

        range = Range.all();                           // [-2147483648..2147483647]
        printInfo(ContiguousSet.create(range, domain), false);
    }

    @Test
    public void test2()
    {
        /*
         * 连续集 = 范围 + 离散域
         * ContiguousSet.create(range, domain)
         * 并没有真的构造了整个集合，而是返回了 set 形式的区间视图
         * 但是可以进行遍历，从 toString 和 forEach 的使用上可以显示出区别
         */

        Range<Character> range  = Range.closed('a', 'z');
        LowerCaseDomain  domain = LowerCaseDomain.domain;

        ContiguousSet<Character> set = ContiguousSet.create(range, domain);
        printInfo(set, true);
    }
}
