package zk.leader;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
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
public class LeaderLatchTest {

    private static CuratorFramework client;

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    // LeaderLatch 抢占式 (主备模式): 只要不挂, 我就是老大
    @Test
    public void test() throws Exception {
        // 1. 创建 LeaderLatch
        // 路径 /election/leader 下的所有参与者竞选 Leader
        LeaderLatch leaderLatch = new LeaderLatch(client, "/election/leader", "client-id-1");

        // 2. 监听领导权变更 (可选)
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                log.info("我当选为 Leader 了! 执行定时任务!");
            }

            @Override
            public void notLeader() {
                log.info("我失去领导权了! 停止执行任务!");
            }
        });

        // 3. 启动
        leaderLatch.start();

        // 4. 阻塞直到成为 Leader (如果需要同步等待)
        leaderLatch.await();

        // 5. 判断当前是否是 Leader (非阻塞检查)
        log.info(String.valueOf(leaderLatch.hasLeadership()));
        LockSupport.park();

        // 6. 关闭 (会自动释放领导权)
        leaderLatch.close(LeaderLatch.CloseMode.NOTIFY_LEADER);
        log.info(String.valueOf(leaderLatch.hasLeadership()));
    }
}
