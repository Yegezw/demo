package zk.cache;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class CuratorCacheExampleTest {

    private static CuratorFramework client;
    private static final String PATH = "/cache";

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    private String makeRandomPath(ThreadLocalRandom random, int depth) {
        if (depth == 0) return PATH;
        return makeRandomPath(random, depth - 1) + "/" + random.nextInt(3);
    }

    @Test
    public void test() throws Exception {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        try (CuratorCache cache = CuratorCache.build(client, PATH)) {
            CuratorCacheListener listener = CuratorCacheListener.builder()
                    .forCreates(
                            node -> log.info(
                                    "Node created: [{} : {}]",
                                    node.getPath(),
                                    StrUtil.str(node.getData(), StandardCharsets.UTF_8)
                            )
                    )
                    .forChanges(
                            (oldNode, node) ->
                                    log.info(
                                            "Node changed: [{} : {} -> {}]",
                                            oldNode.getPath(),
                                            StrUtil.str(oldNode.getData(), StandardCharsets.UTF_8),
                                            StrUtil.str(node.getData(), StandardCharsets.UTF_8)
                                    )
                    )
                    .forCreatesAndChanges(
                            (oldNode, node) ->
                                    log.info(
                                            "Node created and changed: [{} : {} -> {}]",
                                            node.getPath(),
                                            StrUtil.str(oldNode == null ? null : oldNode.getData(), StandardCharsets.UTF_8),
                                            StrUtil.str(node.getData(), StandardCharsets.UTF_8)
                                    )
                    )
                    .forDeletes(
                            oldNode ->
                                    log.info(
                                            "Node deleted: [{} : {}]",
                                            oldNode.getPath(),
                                            StrUtil.str(oldNode.getData(), StandardCharsets.UTF_8)
                                    )
                    ).forAll(
                            (type, oldData, data) -> log.info("All {}: [{} : {}]", type, oldData, data)
                    )
                    .forInitialized(() -> log.info("Cache initialized")) // 当 Cache 启动后, 会先执行一次全量同步, 同步完成后触发此事件
                    .build();
            cache.listenable().addListener(listener); // listener 的所有回调在同一个线程中顺序执行
            cache.start();

            // LockSupport.park();

            // now randomly create/change/delete nodes
            for (int i = 0; i < 200; i++) {
                int depth = random.nextInt(1, 4);
                String path = makeRandomPath(random, depth);
                if (random.nextBoolean()) {
                    client
                            .create()
                            .orSetData()
                            .creatingParentsIfNeeded()
                            .forPath(path, Long.toString(random.nextLong()).getBytes());
                } else {
                    client.delete().quietly().deletingChildrenIfNeeded().forPath(path);
                }

                Thread.sleep(5);
            }
        }
    }
}
