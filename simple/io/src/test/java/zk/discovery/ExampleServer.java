package zk.discovery;

import lombok.Getter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.io.Closeable;
import java.io.IOException;

/**
 * 演示实例
 */
public class ExampleServer implements Closeable {

    /**
     * 当前实例
     */
    @Getter
    private final ServiceInstance<InstanceDetails> instance;
    /**
     * 服务发现
     */
    private final ServiceDiscovery<InstanceDetails> serviceDiscovery;

    // 服务名所在 path 是持久节点, 服务实例所在 path 是临时有序节点
    public ExampleServer(CuratorFramework client, String path, String name, String ip, int port, String description) throws Exception {
        instance = ServiceInstance.<InstanceDetails>builder()
                .name(name)                                // 服务名称
                .address(ip)
                .port(port)
                .payload(new InstanceDetails(description)) // 携带的元数据
                // .uriSpec()
                .build();
        // instance.buildUriSpec();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(path)                                  // 所有服务的根路径
                .serializer(InstanceDetails.INSTANCE_SERIALIZER) // 序列化方式
                .thisInstance(instance)                          // 当前要注册的实例
                .build();
    }

    public void start() throws Exception {
        serviceDiscovery.start(); // 启动后自动完成注册
        System.out.println(instance.getName() + " " + instance.getPayload().getDescription() + " registered");
    }

    @Override
    public void close() throws IOException {
        serviceDiscovery.close(); // 关闭后自动取消注册
        System.out.println(instance.getName() + " " + instance.getPayload().getDescription() + " unregistered");
    }
}
