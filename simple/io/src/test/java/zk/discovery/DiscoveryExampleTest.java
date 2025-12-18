package zk.discovery;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.apache.zookeeper.KeeperException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
public class DiscoveryExampleTest {

    private static CuratorFramework client;
    private static final String PATH = "/discovery";

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    public static void main(String[] args) throws Exception {
        DiscoveryExampleTest test = new DiscoveryExampleTest();
        test.setClient();
        test.test();
        test.closeClient();
    }

    @Test
    public void test() throws Exception {
        // 服务发现
        ServiceDiscovery<InstanceDetails> serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class)
                .client(client)
                .basePath(PATH)
                .serializer(InstanceDetails.INSTANCE_SERIALIZER)
                .build();
        serviceDiscovery.start();
        // 服务提供缓存
        Map<String, ServiceProvider<InstanceDetails>> providers = Maps.newHashMap();

        processCommands(serviceDiscovery, providers);

        for (ServiceProvider<InstanceDetails> provider : providers.values()) provider.close();
        serviceDiscovery.close();
    }

    public void processCommands(ServiceDiscovery<InstanceDetails> serviceDiscovery,
                                Map<String, ServiceProvider<InstanceDetails>> providers) throws Exception {
        printHelp();
        List<ExampleServer> servers = Lists.newArrayList(); // 所有实例

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        while (!done) {
            Thread.sleep(100L);
            System.out.print("> ");

            String line = in.readLine();
            if (line == null) break;

            String command = line.trim();
            String[] parts = command.split("\\s"); // 任意空白字符
            if (parts.length == 0) continue;
            String operation = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            switch (operation) {
                case "?":
                case "help":
                    printHelp();
                    break;
                case "q":
                case "quit":
                    done = true;
                    break;
                case "add":
                    addInstance(args, command, servers);                            // add <name> <description>
                    break;
                case "delete":
                    deleteInstance(args, command, servers);                         // delete <name>
                    break;
                case "random":
                    listRandomInstance(args, command, serviceDiscovery, providers); // random <name>
                    break;
                case "list":
                    listInstances(serviceDiscovery);                                // list
                    break;
            }
        }

        for (ExampleServer server : servers) server.close();
    }

    // add <name> <description>
    private void addInstance(String[] args, String command, List<ExampleServer> servers) throws Exception {
        if (args.length < 2) {
            System.err.println("syntax error (expected add <name> <description>): " + command);
            return;
        }

        StringBuilder description = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            description.append(args[i]);
            if (i != args.length - 1) description.append(' ');
        }

        String name = args[0];
        ExampleServer server = new ExampleServer(client, PATH, name, "", -1, description.toString());
        servers.add(server);
        server.start();

        System.out.println(name + " " + description + " added");
    }

    // delete <name>
    private void deleteInstance(String[] args, String command, List<ExampleServer> servers) throws Exception {
        if (args.length != 1) {
            System.err.println("syntax error (expected delete <name>): " + command);
            return;
        }

        String name = args[0];
        ExampleServer server = Iterables.find(servers, e -> e.getInstance().getName().endsWith(name), null);
        if (server == null) {
            System.err.println("No servers found named: " + name);
            return;
        }

        servers.remove(server);
        server.close();
        System.out.println("Removed a random instance of: " + name + " " + server.getInstance().getPayload().getDescription());
    }

    // random <name>
    private void listRandomInstance(
            String[] args, String command,
            ServiceDiscovery<InstanceDetails> serviceDiscovery,
            Map<String, ServiceProvider<InstanceDetails>> providers) throws Exception {
        if (args.length != 1) {
            System.err.println("syntax error (expected random <name>): " + command);
            return;
        }

        String name = args[0];
        ServiceProvider<InstanceDetails> provider = providers.get(name);
        if (provider == null) {
            provider = serviceDiscovery
                    .serviceProviderBuilder()
                    .serviceName(name)                            // 服务名称
                    .providerStrategy(new RoundRobinStrategy<>()) // 轮询策略
                    .build();
            providers.put(name, provider);
            provider.start(); // 启动后会持续缓存服务列表
        }

        // 直接获取一个可用的实例
        ServiceInstance<InstanceDetails> instance = provider.getInstance();
        if (instance == null) System.err.println("No instances named: " + name);
        else outputInstance(instance);
    }

    // list
    private void listInstances(ServiceDiscovery<InstanceDetails> serviceDiscovery) throws Exception {
        try {
            Collection<String> names = serviceDiscovery.queryForNames(); // 获取所有服务名称
            System.out.println(names.size() + " type(s)");
            for (String name : names) {
                Collection<ServiceInstance<InstanceDetails>> instances = serviceDiscovery.queryForInstances(name); // 获取指定服务名称的所有实例
                for (ServiceInstance<InstanceDetails> instance : instances) outputInstance(instance);
            }
        } catch (KeeperException.NoNodeException e) {
            System.err.println("There are no registered instances.");
        }
    }

    private void outputInstance(ServiceInstance<InstanceDetails> instance) {
        System.out.println("\t" + instance.getName() + " -> " + instance.getPayload().getDescription());
    }

    private void printHelp() {
        System.out.println("An example of using the ServiceDiscovery APIs. This example is driven by entering commands at the prompt:\n");
        System.out.println("add <name> <description>: Adds a mock service with the given name and description");
        System.out.println("delete <name>: Deletes one of the mock services with the given name");
        System.out.println("list: Lists all the currently registered services");
        System.out.println("random <name>: Lists a random instance of the service with the given name");
        System.out.println("quit: Quit the example");
        System.out.println();
    }
}
