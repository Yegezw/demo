package com.zzw.base;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Throwables 简化异常和错误的传播与检查
 */
@Slf4j
public class ThrowablesTest
{

    /**
     * 低等级异常: 编译时异常
     */
    public static class LowLevelException extends Exception
    {
        public LowLevelException(String msg)
        {
            super(msg);
        }
    }

    /**
     * 中等级异常: 编译时异常
     */
    public static class MidLevelException extends Exception
    {
        public MidLevelException(String msg, Throwable cause)
        {
            super(msg, cause);
        }
    }

    /**
     * 高等级异常: 运行时异常
     */
    public static class HighLevelException extends RuntimeException
    {
        public HighLevelException(String msg, Throwable cause)
        {
            super(msg, cause);
        }
    }

    // =================================================================================================================

    public static void fa() throws LowLevelException
    {
        throw new LowLevelException("LowLevelException-msg");
    }

    public static void fb() throws LowLevelException
    {
        fa();
    }

    public static void fc() throws MidLevelException
    {
        try
        {
            fb();
        }
        catch (LowLevelException e)
        {
            throw new MidLevelException("MidLevelException-msg", e);
        }
    }

    public static void fd()
    {
        try
        {
            fc();
        }
        catch (MidLevelException e)
        {
            throw new HighLevelException("HighLevelException-msg", e);
        }
    }

    public static void fe()
    {
        fd();
    }

    // =================================================================================================================

    /**
     * test -> fe -> fd(高) -> fc(中) -> fb -> fa(低)
     */
    @Test
    public void test()
    {
        try
        {
            fe();
        }
        catch (HighLevelException e)
        {
            // Throwable 类型为 HighLevelException 才抛出
            // Throwables.throwIfInstanceOf(e, HighLevelException.class);

            // Throwable 类型为 RuntimeException OR Error 才抛出
            // Throwables.throwIfUnchecked(e);

            // 完整的异常
            log.error(e.getMessage(), e);

            // 完整的栈追踪字符串
            String string = Throwables.getStackTraceAsString(e);
            // 完整的异常链 [HighLevelException, MidLevelException, LowLevelException]
            List<Throwable> causalChain = Throwables.getCausalChain(e);
            // 异常链中的某个异常 MidLevelException
            MidLevelException mid = Throwables.getCauseAs(e, MidLevelException.class);
            // 异常链的根异常 LowLevelException
            LowLevelException low = (LowLevelException) Throwables.getRootCause(e);
            log.error(low.getMessage(), low);
        }
    }
}
