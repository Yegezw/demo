package zk.framework;

import cn.hutool.core.util.StrUtil;
import framework.CrudExamples;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class CrudExamplesTest {

    private static CuratorFramework client;
    private static final byte[] b1 = "apple".getBytes(StandardCharsets.UTF_8);
    private static final byte[] b2 = "banana".getBytes(StandardCharsets.UTF_8);
    private static final byte[] b3 = "orange1".getBytes(StandardCharsets.UTF_8);
    private static final byte[] b4 = "orange2".getBytes(StandardCharsets.UTF_8);
    private static final byte[] b5 = "orange3".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    /*
     * PERSISTENT                      持久节点, 一旦创建成功不会被删除, 除非客户端主动发起删除请求
     * PERSISTENT_SEQUENTIAL           持久顺序节点, 会在用户路径后面拼接一个不会重复的自增数字后缀, 其它同上
     *
     * EPHEMERAL                       临时节点, 当创建该节点的客户端链接断开后自动被删除
     * EPHEMERAL_SEQUENTIAL            临时顺序节点, 基本同上, 也是增加一个数字后缀
     *
     * CONTAINER                       容器节点, 一旦子节点被删除完就会被服务端删除
     * PERSISTENT_WITH_TTL             带过期时间的持久节点, 带有超时时间的节点, 如果超时时间内没有子节点被创建, 就会被删除
     * PERSISTENT_SEQUENTIAL_WITH_TTL  带过期时间的持久顺序节点, 基本同上, 多了一个数字后缀
     *
     * 同一个客户端, 同一个路径下只能有一种类型 (共三种: 一次性、持久、持久递归) 的订阅, 后注册的类别会覆盖之前注册的类别
     */

    /**
     * 持久节点
     */
    @Test
    public void create() throws Exception {
        CrudExamples.create(client, "/user", b1);
    }

    /**
     * 临时节点
     */
    @Test
    public void createEphemeral() throws Exception {
        CrudExamples.createEphemeral(client, "/user", b1);
        LockSupport.park();
    }

    /**
     * 临时顺序节点
     */
    @Test
    public void createEphemeralSequential() throws Exception {
        // withProtection 打开保护模式后
        // Curator 会在 "实际创建的节点名前" 加上一个唯一标识 GUID 前缀
        // 这样做的目的是: 当第一次尝试创建节点时, 客户端没有收到服务器返回 (例如服务器在返回之前崩溃或网络断开)
        // 重试时, Curator 会在父目录里查找带有这个 GUID 的节点, 并认为它就是之前 "丢失" 的节点, 进而避免重复创建或无法找到已创建节点的问题
        CrudExamples.createEphemeralSequential(client, "/user/m", null);
        CrudExamples.createEphemeralSequential(client, "/user/m", null);
        CrudExamples.createEphemeralSequential(client, "/user/m", null);
        LockSupport.park();
    }

    /**
     * 幂等的创建
     */
    @Test
    public void createIdempotent() throws Exception {
        // 如果第一次创建请求在服务器端实际上已经成功 (节点已被创建), 但客户端因为超时 OR 断网没收到返回, 后续重试时 Curator 会检查目标路径是否已经存在
        // 如果发现该路径已存在, 且该节点的数据与本次要写入的 payload 相同, Curator 会把这次操作当作成功并返回 (对调用者而言就像第一次创建成功一样)
        // 如果该路径已存在但数据不同, Curator 不会盲目覆盖 (这取决于具体 Curator 版本的实现策略, 通常会把冲突作为错误处理), 从而避免把别人已存在的节点不经意间覆盖掉
        CrudExamples.createIdempotent(client, "/user", b1);
        CrudExamples.createIdempotent(client, "/user", b1);
    }

    /**
     * 更新节点
     */
    @Test
    public void setData() throws Exception {
        CrudExamples.setData(client, "/user", b2);
    }

    /**
     * 更新节点 - 异步
     */
    @Deprecated // 应该用 AsyncCuratorFramework
    @Test
    public void setDataAsync() throws Exception {
        // 全局生效: 回调由 Zookeeper EventThread 线程执行
        CuratorListener listener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) {
                Object data = StrUtil.str(event.getContext(), StandardCharsets.UTF_8);
                log.info("callback 全局 {} -> {}:{} | {}", event.getType(), event.getPath(), data, event.getResultCode()); // KeeperException.Code
            }
        };
        client.getCuratorListenable().addListener(listener);
        // client.getCuratorListenable().removeListener(listener); 需要手动移除监听器
        client.setData().inBackground(b3).forPath("/user", b3); // 全局

        // 一次性: 仅用于当前这次异步操作, 回调由 Zookeeper EventThread 线程执行
        BackgroundCallback callback = new BackgroundCallback() {
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                Object data = StrUtil.str(event.getContext(), StandardCharsets.UTF_8);
                log.info("callback 局部 {} -> {}:{} | {}", event.getType(), event.getPath(), data, event.getResultCode()); // KeeperException.Code
            }
        };
        client.setData().inBackground(callback, b4).forPath("/user", b4); // 局部
        client.setData().inBackground(b5).forPath("/user", b5);           // 全局

        Thread.sleep(1000);
    }

    /**
     * 更新节点 - 幂等
     */
    @Test
    public void setDataIdempotent() throws Exception {
        Stat stat = client.checkExists().forPath("/user");
        int currentVersion = stat.getVersion();

        // 如果 currentVersion == X 且 payload == P
        // 那么这个操作被认为成功的条件是最终节点状态满足: version == X + 1 且 data == P
        client.setData().idempotent().withVersion(currentVersion).forPath("/user", b1);

        // 只要最终节点数据等于 P 就认为成功, 不要求版本是某个固定值或被增加了多少
        // 这意味着: 即使有别人也写了相同的数据 P, 只要最后的 data == P, Curator 会认为本次操作成功
        client.setData().idempotent().forPath("/user", b2);
    }

    /**
     * 删除节点
     */
    @Test
    public void delete() throws Exception {
        CrudExamples.delete(client, "/user");
    }

    /**
     * 删除节点 - 保证
     */
    @Test
    public void guaranteedDelete() throws Exception {
        // 失败时: 会把删除任务记录到内部队列, 并在后台持续重试, 直到删除成功或实例关闭
        // 如果节点在第一次删除失败后被其它客户端重新创建 (同一路径)
        // guaranteed 的重试会再次尝试删除该路径, 可能会删除新创建的节点
        // guaranteed 只按路径重试删除, 不区分是不是原来的那个节点实例 (没有捕获节点创建时的独有标识如 zxid 等来辨别)
        CrudExamples.guaranteedDelete(client, "/user");
    }

    /**
     * 删除节点 - 幂等
     */
    @Test
    public void deleteIdempotent() throws Exception {
        Stat stat = client.checkExists().forPath("/user");
        int currentVersion = stat.getVersion();

        // 不会把失败任务长期保存到后台队列中去无限期重试

        // 若第一次请求在服务器已成功删除但客户端未确认, 重试时 Curator 会检查当前状态
        // 如果发现节点不存在则成功
        // 如果节点仍存在且版本仍是 currentVersion, Curator 会继续尝试并最终删除或确定失败
        // 如果版本已变 (别人在此期间修改过), 则视为并发冲突, 最终可能抛出 BadVersion / 其它异常
        client.delete().idempotent().withVersion(currentVersion).forPath("/user");

        // 只要最终节点被删除就视为成功
        // 如果第一次请求在服务器已成功但客户端未收到响应, 重试时 Curator 会检查并 "在发现 node 已删除时" 返回成功
        client.delete().idempotent().forPath("/user");

        // idempotent() 的行为在功能上等价于旧的 quietly()
        // client.delete().quietly().withVersion(currentVersion).forPath("/user");
        // client.delete().quietly().forPath("/user");
    }

    /**
     * 一次性通知 - 子节点
     */
    @Deprecated // 应该用 AsyncCuratorFramework
    @Test
    public void watchedGetChildren1() throws Exception {
        CrudExamples.createEphemeralSequential(client, "/user/m", null);
        CrudExamples.createEphemeralSequential(client, "/user/m", null);

        CuratorListener listener = new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) {
                log.info("全局 {} -> {}", event.getType(), event.getPath());
            }
        };
        client.getCuratorListenable().addListener(listener);

        List<String> list = CrudExamples.watchedGetChildren(client, "/user"); // 下一次子节点变更时收到一次通知
        log.info("watchedGetChildren1 {}", list);

        CrudExamples.createEphemeralSequential(client, "/user/m", null); // 有通知
        CrudExamples.createEphemeralSequential(client, "/user/m", null); // 无通知

        LockSupport.park();
    }

    /**
     * 一次性通知 - 子节点
     */
    @Deprecated // 应该用 AsyncCuratorFramework
    @Test
    public void watchedGetChildren2() throws Exception {
        CrudExamples.createEphemeralSequential(client, "/user/m", null);
        CrudExamples.createEphemeralSequential(client, "/user/m", null);

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("局部 {} -> {}", event.getType(), event.getPath());
            }
        };
        List<String> list = CrudExamples.watchedGetChildren(client, "/user", watcher); // 下一次子节点变更时收到一次通知
        log.info("watchedGetChildren2 {}", list);

        CrudExamples.createEphemeralSequential(client, "/user/m", null); // 有通知
        CrudExamples.createEphemeralSequential(client, "/user/m", null); // 无通知

        LockSupport.park();
    }
}
