package com.zzw.eg;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class DirectorMemoryReporter {

    private static final AtomicLong directorMemory;

    static {
        try {
            Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
            field.setAccessible(true);
            directorMemory = (AtomicLong) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        log.warn("Max available memory: {}", PlatformDependent.maxDirectMemory());
        new Thread(() -> {
            while (true) {
                long cur;
                if (directorMemory != null) cur = directorMemory.get();
                else if (PlatformDependent.usedDirectMemory() != -1) cur = PlatformDependent.usedDirectMemory();
                else cur = PooledByteBufAllocator.DEFAULT.metric().usedDirectMemory();

                log.warn("Current used memory: {}", cur);
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
            }
        }).start();
    }
}
