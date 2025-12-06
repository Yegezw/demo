package com.zzw.simple;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public abstract class SingleThreadEventExecutor implements Executor
{

    protected static final int DEFAULT_MAX_PENDING_TASKS = Integer.MAX_VALUE;

    // ------------------------------------------------

    private volatile boolean start = false;

    private Thread thread;

    private final Queue<Runnable> taskQueue;

    public SingleThreadEventExecutor()
    {
        this.taskQueue = new LinkedBlockingQueue<>(DEFAULT_MAX_PENDING_TASKS);
    }

    // ------------------------------------------------

    protected boolean inEventLoop(Thread thread)
    {
        return this.thread == thread;
    }

    protected boolean hasTasks()
    {
        return !taskQueue.isEmpty();
    }

    protected abstract void run();

    // ------------------------------------------------

    @Override
    public void execute(Runnable task)
    {
        taskQueue.offer(task);

        if (start) return;

        start = true;
        new Thread(
                () ->
                {
                    thread = Thread.currentThread();
                    SingleThreadEventExecutor.this.run();
                }
        ).start();
    }

    protected void runAllTasks()
    {
        Runnable task;
        while ((task = taskQueue.poll()) != null) task.run();
    }
}
