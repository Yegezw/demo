package com.zzw.base;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * StopWatch 计时器
 */
public class StopWatchTest
{

    @Test
    public void test() throws Exception
    {
        // 创建 stopwatch 并开始计时
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(1980);

        // 停止计时
        stopwatch.stop();
        // 打印从计时开始至现在的所用时间，向下取整，单位为秒
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS)); // 1

        // 再次计时
        stopwatch.start();
        Thread.sleep(100);
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS)); // 2

        // ------------------------------------------------

        // 重置并开始
        stopwatch.reset().start();
        Thread.sleep(1030);

        // 检查是否运行
        System.out.println(stopwatch.isRunning());

        // 打印耗时
        System.out.println(stopwatch);  // 1.044 s
        System.out.println(stopwatch.elapsed(TimeUnit.NANOSECONDS));
    }
}
