package zk.framework;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zk.Util;

import java.util.Collection;

@Slf4j
public class TransactionExamplesTest {

    private static CuratorFramework client;

    @BeforeEach
    public void setClient() {
        client = Util.getClient();
    }

    @AfterEach
    public void closeClient() {
        if (client != null) client.close();
    }

    /**
     * 事务操作
     */
    @Test
    public void transaction() throws Exception {
        CuratorOp createOp = client.transactionOp().create().forPath("/a", "hello1".getBytes());
        CuratorOp setDataOp = client.transactionOp().setData().forPath("/a", "hello2".getBytes());
        CuratorOp deleteOp = client.transactionOp().delete().forPath("/a");

        // 按顺序执行各个操作, 返回的结果也是按顺序对应的, 任意一个操作失败, 整个事务都会失败
        Collection<CuratorTransactionResult> results = client.transaction().forOperations(createOp, setDataOp, deleteOp);

        for (CuratorTransactionResult result : results) {
            log.info("{} - {}", result.getType(), result.getForPath());
        }
    }
}
