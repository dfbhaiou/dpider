package cn.dpider.common.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import cn.dpider.common.utils.Constant;

public class ZookeeperFactory {

    private static CuratorFramework client;

    public static CuratorFramework create() {
        if (client == null) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,3);
            client = CuratorFrameworkFactory.newClient(Constant.getConfig("dubbo.RegistryAddress"),retryPolicy);
            client.start();
        }
        return client;
    }
}
