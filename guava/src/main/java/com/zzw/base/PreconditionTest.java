package com.zzw.base;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;

/**
 * Precondition 断言
 */
public class PreconditionTest
{

    /**
     * NullPointerException
     */
    @Test
    public void test1()
    {
        Preconditions.checkNotNull(null);
        Preconditions.checkNotNull(null, "对象不能为空");
        Preconditions.checkNotNull(null, "对象不能为空，并且长度必须为 %s", 6);
    }

    /**
     * IllegalArgumentException
     */
    @Test
    public void test2()
    {
        Preconditions.checkArgument(false);
        Preconditions.checkArgument(false, "未通过验证");
    }

    /**
     * IllegalStateException
     */
    @Test
    public void test3()
    {
        Preconditions.checkState(false);
        Preconditions.checkState(false, "未通过验证");
    }

    /**
     * IndexOutOfBoundsException
     */
    @Test
    public void test4()
    {
        // [0 ... size - 1] 才不会报错
        // 用于数组或列表 "查询操作" 前检查

        // index (3) must be less than size (3)
        Preconditions.checkElementIndex(3, 3);

        // list.index (3) must be less than size (3)
        Preconditions.checkElementIndex(3, 3, "list.index");

        // ------------------------------------------------

        // [0 ... size] 才不会报错
        // 用于数组或列表 "插入或删除" 操作前检查

        // index (4) must not be greater than size (3)
        Preconditions.checkPositionIndex(4, 3);

        // list.index (4) must not be greater than size (3)
        Preconditions.checkPositionIndex(4, 3, "list.index");

        // start <= end && in [0 ... size] 才不会报错
        Preconditions.checkPositionIndexes(1, 3, 3);
    }
}
