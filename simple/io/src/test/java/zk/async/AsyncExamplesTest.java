package zk.async;

import async.AsyncExamples;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.AsyncEventException;
import org.apache.curator.x.async.WatchMode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class AsyncExamplesTest {

    private static AsyncCuratorFramework client;
    private static final byte[] data = "apple".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    public void setClient() {
        client = AsyncExamples.wrap(Util.getClient());
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.unwrap().close();
    }

    /**
     * 临时节点
     */
    @Test
    public void create() {
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/user", data).whenComplete(
                (name, exception) -> {
                    if (exception != null) {
                        log.error("create error", exception);
                    } else {
                        log.info("Created node name is: {}", name);
                    }
                }
        );
        LockSupport.park();
    }

    /**
     * 临时节点 + 一次性通知
     */
    @Test
    public void createThenWatchSimple() {
        // 回调由 Zookeeper EventThread 线程执行
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/user", data).whenComplete(
                (name, exception) -> {
                    if (exception != null) {
                        log.error("createThenWatchSimple error", exception);
                    } else {
                        // successOnly              只在节点事件 (创建、删除、数据变更、子节点变更) 时触发, 忽略连接状态变化
                        // stateChangeOnly          只在连接状态变化时触发
                        // stateChangeAndSuccess    两种都会触发 (默认)
                        client.with(WatchMode.successOnly)
                                .watched()
                                .checkExists()
                                .forPath("/user")
                                .event()
                                .thenAccept(event -> log.info("A 一次性事件 {}: {}", event.getType(), event.getPath()));
                    }
                }
        );
        LockSupport.park();
    }

    /**
     * 临时节点 + 一次性通知 + 异常时重置监听
     */
    @Test
    public void createThenWatch() {
        // 回调由 Zookeeper EventThread 线程执行
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/user").whenComplete(
                (name, exception) -> {
                    if (exception != null) {
                        log.error("createThenWatch error", exception);
                    } else {
                        // successOnly              只在节点事件 (创建、删除、数据变更、子节点变更) 时触发, 忽略连接状态变化
                        // stateChangeOnly          只在连接状态变化时触发
                        // stateChangeAndSuccess    两种都会触发 (默认)
                        CompletionStage<WatchedEvent> event = client
                                .watched()
                                .checkExists()
                                .forPath("/user")
                                .event();
                        handleWatchedStage(event);
                    }
                }
        );
        LockSupport.park();
    }

    private void handleWatchedStage(CompletionStage<WatchedEvent> watchedStage) {
        watchedStage.thenAccept(event -> log.info("B 一次性事件 {}: {}", event.getType(), event.getPath()));

        /*
         * 正常流程
         * 1. 设置 Watch on /node
         * 2. /node 数据改变
         * 3. Watch 触发 -> 收到 NodeDataChanged 事件
         *
         * 异常流程 (这就是为什么需要 reset)
         * 1. 设置 Watch on /node
         * 2. 网络断开 -> Watch 触发 -> 收到 Disconnected 事件
         * 3. 网络恢复 -> Watch 仍然有效!
         * 4. /node 数据改变 -> Watch 再次触发 -> 收到 NodeDataChanged 事件
         * 
         * ZooKeeper Watch 的特殊行为
         * ZooKeeper 的 Watcher 在连接状态变化时会被临时触发, 但 Watch 本身并没有失效
         * reset 重置一个新的 CompletionStage, 以便继续等待真正的触发
         */
        watchedStage.exceptionally(
                exception -> {
                    AsyncEventException asyncEx = (AsyncEventException) exception;
                    log.error("handleWatchedStage error", exception);
                    handleWatchedStage(asyncEx.reset());
                    return null;
                }
        );
    }
}
