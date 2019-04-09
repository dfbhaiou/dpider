package cn.dpider.urlScheduler.urlScdMoniter;
import java.util.Date;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.common.po.UrlSchedulerNode;
import cn.dpider.common.zk.ZookeeperFactory;
import cn.dpider.urlScheduler.utils.RedisCacheManager;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;

public class UrlSchedulerMonitorCenter {

    private static CuratorFramework client = ZookeeperFactory.create();

    @Autowired
    RedisCacheManager redisCacheManager;

    private String id;

    private String name;

    private Date startTime;

    public void init(String id1,String name1,Date startTime1) {
        this.id = id1;
        this.name = name1;
        this.startTime = startTime1;
    }

    public void submit() throws Exception {
        Date now = new Date();
        UrlSchedulerNode urlSchedulerNode = new UrlSchedulerNode();
        urlSchedulerNode.setId(id);
        urlSchedulerNode.setName(name);
        urlSchedulerNode.setStartTime(startTime);

        urlSchedulerNode.setHpSize(redisCacheManager.lGetListSize(ConstantValue.REDIS_HP_URL));
        urlSchedulerNode.setLpSize(redisCacheManager.lGetListSize(ConstantValue.REDIS_LP_URL));
        urlSchedulerNode.setConsumedSize(redisCacheManager.sGetSetSize(ConstantValue.REDIS_CONSUMED));

        urlSchedulerNode.setUpdateTime(now);
        urlSchedulerNode.setRunTime(0.0D);
        urlSchedulerNode.setRate(0.0D);

        client.setData().forPath(id, JSON.toJSONString(urlSchedulerNode).getBytes());
    }

}
