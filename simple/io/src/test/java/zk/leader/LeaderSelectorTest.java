package zk.leader;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.util.concurrent.locks.LockSupport;

/*
 * 路径创建: 在选举路径下创建 EPHEMERAL_SEQUENTIAL (临时顺序)节点
 * 序号判定: 所有参与者检查自己创建的节点序号, 序号最小的节点获得领导权
 * Watch 机制: 没有获得领导权的节点, 会 Watch 比自己序号小的前一个节点
 *
 * 容错处理
 * 正常释放: 调用 close() 时删除节点, Watcher 触发, 后一个节点成为新 Leader
 * 异常宕机: 由于是临时节点, ZK 会在 Session 超时后自动删除节点, 下一位 "候选人" 自动上位
 * 脑裂保护: Curator 内部处理了 SUSPENDED (连接挂起)状态, 如果与 ZK 失去连接, Curator 会立即触发 notLeader() 回调, 防止出现多个 Leader 的 "脑裂" 现象
 */

@Slf4j
public class LeaderSelectorTest {

    private static CuratorFramework client;

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    // LeaderSelector 任务轮转型 (资源公平利用)
    @Test
    public void test() {
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                // 一旦进入这个方法, 说明你已经是 Leader 了
                log.info("当前我是 Leader");

                // while (leaderLatch.hasLeadership())
                Thread.sleep(5000); // 模拟工作

                log.info("任务完成, 释放领导权, 重新排队");
                // 方法结束时, Curator 会自动释放领导权
            }
        };

        LeaderSelector selector = new LeaderSelector(client, "/election/selector", listener);
        selector.setId("client-id-1");
        selector.autoRequeue(); // 释放领导权后, 自动重新加入竞选队列, 否则只执行一次
        selector.start();

        LockSupport.park();
    }
}
