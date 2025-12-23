package zk.modeled;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.JacksonModelSerializer;
import org.apache.curator.x.async.modeled.ModelSpec;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.ZPath;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

@Deprecated
@Slf4j
public class ModeledCuratorExamplesTest {

    private static final ZPath PATH = ZPath.parseWithIds("/persons/{id}");
    private static AsyncCuratorFramework client;
    private static ModelSpec<Person> spec;

    @BeforeEach
    public void setClient() {
        client = AsyncCuratorFramework.wrap(Util.getClient());
        spec = ModelSpec
                .builder(PATH, JacksonModelSerializer.build(Person.class))
                .withCreateMode(CreateMode.PERSISTENT)
                .build();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.unwrap().close();
    }

    @Test
    public void add() {
        Person person = new Person("张三", 30, "zhangsan@example.com");
        // 使用路径参数 {id}
        ModeledFramework.wrap(client, spec.resolved("person001"))
                .set(person)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("保存失败: {}", exception.getMessage());
                    } else {
                        log.info("保存成功");
                    }
                })
                .toCompletableFuture().join();
    }

    @Test
    public void read() {
        ModeledFramework.wrap(client, spec.resolved("person001"))
                .read()
                .whenComplete((person, exception) -> {
                    if (exception != null) {
                        log.error("读取失败: {}", exception.getMessage());
                    } else {
                        log.info("读取数据 {}", person);
                    }
                })
                .toCompletableFuture().join();
    }

    @Test
    public void update() {
        ModeledFramework.wrap(client, spec.resolved("person001"))
                .read()
                .thenCompose(person -> {
                    person.setAge(18);
                    return ModeledFramework.wrap(client, spec.resolved("person001")).update(person);
                })
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("更新失败: {}", exception.getMessage());
                    } else {
                        log.info("更新成功");
                    }
                })
                .toCompletableFuture().join();
    }

    @Test
    public void delete() {
        ModeledFramework.wrap(client, spec.resolved("person001"))
                .delete()
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("删除失败: {}", exception.getMessage());
                    } else {
                        log.info("删除成功");
                    }
                })
                .toCompletableFuture().join();
    }
}
