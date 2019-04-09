package cn.dpider.common.register.impl;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.common.register.AbstractRegistry;
import cn.dpider.common.register.RegisterRequest;
import cn.dpider.common.zk.ZookeeperFactory;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;


public class DefaultRegistry extends AbstractRegistry {

    CuratorFramework client = ZookeeperFactory.create();

    @Override
    public String doRegister(RegisterRequest request) {

        String parentPath = ConstantValue.SERVER_PATH;
        try {
            checkServicePathIsExist(parentPath);

            String path = client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(parentPath + "/" + request.getSubPath() + "/" + request.getHost() + "#" + request.getPort() + "#"
                            , request.getNodeInfoJson().getBytes());

            return path;
        } catch (Exception e) {
            return null;
        }
    }

    private void checkServicePathIsExist(String parentPath) throws Exception {
        if (client.checkExists().forPath(parentPath) == null) {
            prepareZkDirectory(parentPath);
        }
    }

    private void prepareZkDirectory(String parentPath) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath(parentPath + "/spider");
        client.create().withMode(CreateMode.PERSISTENT)
                .forPath(parentPath + "/urlScheduler");
        client.create().withMode(CreateMode.PERSISTENT)
                .forPath(parentPath + "/configurator");
    }

    @Override
    protected void doUnRegister(RegisterRequest request) {

    }
}
