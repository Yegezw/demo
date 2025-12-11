package com.zzw.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class Main {

    public static String toStr(byte[] bytes) {
        if (bytes == null) return null;
        return new String(bytes);
    }

    public static void main(String[] args) throws Exception {
        // test1();
        // test2();
        // test3();
        // test4();
        // test5();
    }

    /**
     * 同步
     */
    public static void test1() throws Exception {
        CuratorFramework client = getZkClient();

        // create() 创建 ZNode
        String path = client.create().withMode(CreateMode.PERSISTENT).forPath("/user", "test".getBytes());
        log.info(path);

        // checkExists() 检查一个节点是否存在
        Stat stat = client.checkExists().forPath("/user");
        log.info(String.valueOf(stat != null));

        // getData() 获取一个节点中的数据
        byte[] data = client.getData().forPath("/user");
        log.info(new String(data));

        // setData() 设置一个节点中的数据
        stat = client.setData().forPath("/user", "data".getBytes());
        data = client.getData().forPath("/user");
        log.info(new String(data));

        // 在 /user 节点下创建多个临时顺序节点
        for (int i = 0; i < 3; i++) {
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/user/child-");
        }

        // 获取所有子节点
        List<String> children = client.getChildren().forPath("/user");
        log.info(children.toString());

        // delete() 删除指定节点
        // deletingChildrenIfNeeded() 会级联删除子节点
        client.delete().deletingChildrenIfNeeded().forPath("/user");

        client.close();
    }

    /**
     * 异步
     */
    public static void test2() throws Exception {
        CuratorFramework client = getZkClient();

        // 添加 CuratorListener 监听器, 针对不同的事件进行处理
        client.getCuratorListenable().addListener(
                new CuratorListener() {
                    public void eventReceived(CuratorFramework client, CuratorEvent event) {
                        switch (event.getType()) {
                            case CREATE:
                                log.info("CREATE: {}", event.getPath());
                                break;
                            case DELETE:
                                log.info("DELETE: {}", event.getPath());
                                break;
                            case EXISTS:
                                log.info("EXISTS: {}", event.getPath());
                                break;
                            case SET_DATA:
                                log.info("SET_DATA: {}", event.getPath());
                                break;
                            case GET_DATA:
                                log.info("GET_DATA: {} -> {}", event.getPath(), toStr(event.getData()));
                                break;
                            case CHILDREN:
                                log.info("CHILDREN: {} -> {}", event.getPath(), event.getChildren());
                                break;
                            default:
                        }
                    }
                }
        );

        // 注意: 下面所有的操作都添加了 inBackground() 方法, 转换为后台操作
        client.create().withMode(CreateMode.PERSISTENT).inBackground().forPath("/user", "test".getBytes());
        client.checkExists().inBackground().forPath("/user");
        client.setData().inBackground().forPath("/user", "setData-Test".getBytes());
        client.getData().inBackground().forPath("/user");
        for (int i = 0; i < 3; i++) {
            client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).inBackground().forPath("/user/child-");
        }
        client.getChildren().inBackground().forPath("/user");

        // 添加 BackgroundCallback
        client.getChildren().inBackground(
                new BackgroundCallback() {
                    public void processResult(CuratorFramework client, CuratorEvent event) {
                        log.info("{}: {} -> {} - [in background]", event.getType(), event.getPath(), event.getChildren());
                    }
                }
        ).forPath("/user");

        client.delete().deletingChildrenIfNeeded().inBackground().forPath("/user");

        Thread.sleep(1000L);
        client.close();
    }

    /**
     * 连接
     */
    public static void test3() throws Exception {
        CuratorFramework client = getZkClient();

        // 添加 ConnectionStateListener 监听器
        client.getConnectionStateListenable().addListener(
                new ConnectionStateListener() {
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {
                        // 可以针对不同的连接状态进行特殊的处理
                        switch (newState) {
                            case CONNECTED:
                                // 首次成功连接到 ZooKeeper
                                // 对于每个 CuratorFramework 对象, 此状态仅出现一次
                                log.info("CONNECTED");
                                break;
                            case SUSPENDED:
                                // 与 ZooKeeper 的连接已丢失 (例如网络中断、ZooKeeper 实例宕机), 但 Session 尚未过期, Session 仍被 ZooKeeper 服务端维护
                                log.info("SUSPENDED");
                                break;
                            case RECONNECTED:
                                // 客户端在 sessionTimeout 规定的时间内重新连接到 ZooKeeper, 并且 Session 仍然有效, 由 SUSPENDED 恢复而来
                                // Curator 会自动重新创建临时节点 (Ephemeral Nodes) 并重新注册 Watcher, 应用可以恢复正常操作
                                log.info("RECONNECTED");
                                break;
                            case LOST:
                                // 客户端连接长时间断开, Session 已过期, ZooKeeper 服务端已清理该 Session 关联的资源
                                // Curator 客户端对象 CuratorFramework 已失效, 需要应用程序代码彻底关闭当前客户端, 并创建一个全新的客户端 CuratorFramework 来建立新的 Session
                                // 在新 Session 上重新执行所有必要的初始化工作, 例如重新创建临时节点、重新注册 Watcher (使用 Curator Recipes, 如 PathChildrenCache 或 NodeCache. 通常会自动处理这些重新注册)
                                log.info("LOST");
                                break;
                            case READ_ONLY:   // 连接进入只读模式
                                log.info("READ_ONLY");
                                break;
                        }
                    }
                }
        );

        Thread.sleep(1000L);
        client.close();
        Thread.sleep(1000L);
    }

    /**
     * Watch
     */
    public static void test4() throws Exception {
        CuratorFramework client = getZkClient();

        client.create().withMode(CreateMode.PERSISTENT).forPath("/user", "test".getBytes());
        // 这里通过 usingWatcher() 添加一个 Watcher, 触发完毕后就会销毁
        List<String> children = client.getChildren().usingWatcher(
                new CuratorWatcher() {
                    public void process(WatchedEvent event) {
                        log.info("一次性事件 {}: {}", event.getType(), event.getPath());
                    }
                }
        ).forPath("/user");

        log.info("子节点: {}", children.toString());
        LockSupport.park();
    }

    /**
     * Watch 缓存
     */
    public static void test5() throws Exception {
        CuratorFramework client = getZkClient();
        /*
         * 实践中常用的 Cache 有三大类
         *
         * NodeCache
         * 对一个节点进行监听, 监听事件包括指定节点的增删改操作
         * 不仅可以监听数据节点的内容变更, 也能监听指定节点是否存在, 如果原本节点不存在, 那么 Cache 就会在节点被创建后触发 NodeCacheListener, 删除操作亦然
         *
         * PathChildrenCache
         * 对指定节点的一级子节点进行监听, 监听事件包括子节点的增删改操作, 但是不对该节点的操作监听
         *
         * TreeCache
         * 综合 NodeCache 和 PathChildrenCache 的功能, 是对指定节点以及其子节点进行监听, 同时还可以设置监听的深度
         */

        // 创建 NodeCache, 监听的是 "/user" 这个节点
        NodeCache nodeCache = new NodeCache(client, "/user");
        // start() 方法有个 boolean 类型的参数, 默认是 false
        // 如果设置为 true, 那么 NodeCache 在第一次启动的时候就会立刻从 ZooKeeper 上读取对应节点的数据内容, 并保存在 Cache 中
        nodeCache.start(true);
        if (nodeCache.getCurrentData() != null) {
            log.info("NodeCache 节点初始化数据: {}", new String(nodeCache.getCurrentData().getData()));
        } else {
            log.info("NodeCache 节点数据为空");
        }
        // 添加监听器
        nodeCache.getListenable().addListener(() -> {
            String data = new String(nodeCache.getCurrentData().getData());
            log.info("NodeCache 节点路径: {}, 节点数据: {}", nodeCache.getCurrentData().getPath(), data);
        });

        // 创建 PathChildrenCache 实例, 监听的是 "user" 这个节点
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/user", true);

        // StartMode 指定的初始化的模式
        // NORMAL                 普通异步初始化
        // BUILD_INITIAL_CACHE    同步初始化
        // POST_INITIALIZED_EVENT 异步初始化, 初始化之后会触发事件
        // childrenCache.start(PathChildrenCache.StartMode.NORMAL); 
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        // childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        List<ChildData> children = childrenCache.getCurrentData();
        log.info("获取子节点列表");
        // 如果是 BUILD_INITIAL_CACHE 可以获取这个数据, 如果不是就不行
        children.forEach(childData -> log.info(new String(childData.getData())));

        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
                        log.info("持久性事件 {} {}", LocalDateTime.now(), event.getType());
                        if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                            log.info("PathChildrenCache 子节点初始化成功");
                        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                            log.info("PathChildrenCache 添加子节点: {}", event.getData().getPath());
                            log.info("PathChildrenCache 子节点数据: {}", new String(event.getData().getData()));
                        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                            log.info("PathChildrenCache 删除子节点: {}", event.getData().getPath());
                        } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                            log.info("PathChildrenCache 修改子节点路径: {}", event.getData().getPath());
                            log.info("PathChildrenCache 修改子节点数据: {}", new String(event.getData().getData()));
                        }
                    }
                }
        );

        // 创建 TreeCache 实例监听 "user" 节点
        TreeCache cache = TreeCache.newBuilder(client, "/user").setCacheData(false).build();
        cache.getListenable().addListener(
                new TreeCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, TreeCacheEvent event) {
                        if (event.getData() != null) {
                            log.info("TreeCache,type={} path={}", event.getType(), event.getData().getPath());
                        } else {
                            log.info("TreeCache,type={}", event.getType());
                        }
                    }
                }
        );

        cache.start();
        LockSupport.park();
    }

    public static CuratorFramework getZkClient() {
        // Zookeeper 集群地址, 多个节点地址可以用逗号分隔
        String zkAddress = "127.0.0.1:2181";
        // 重试策略, 如果连接不上 ZooKeeper 集群, 会重试三次, 重试间隔会递增
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // 创建 Curator Client 并启动
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .namespace("demo")
                .build();
        client.start();
        return client;
    }
}
