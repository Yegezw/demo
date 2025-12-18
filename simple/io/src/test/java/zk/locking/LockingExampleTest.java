package zk.locking;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LockingExampleTest {

    private static CuratorFramework client;

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    public int count;

    @Test
    public void test() throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, "/test-lock");

        ExecutorService pool = Executors.newFixedThreadPool(10);
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    lock.acquire();
                    try {
                        count++;
                    } finally {
                        lock.release();
                    }
                } catch (Exception ignore) {
                }
            }
        };

        for (int i = 0; i < 10; i++) pool.submit(task);
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);

        log.info("i = {}", count);
    }
}
