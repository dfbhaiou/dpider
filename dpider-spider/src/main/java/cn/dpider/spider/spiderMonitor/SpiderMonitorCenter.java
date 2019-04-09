package cn.dpider.spider.spiderMonitor;

import cn.dpider.common.constant.SpiderState;
import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.zk.ZookeeperFactory;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo 歌曲插入数据库的条数，歌曲异常插入的条数及原因
 */
public class SpiderMonitorCenter {

    private static CuratorFramework client = ZookeeperFactory.create();

    private static String id;

    private static String name;

    private static Date startTime;

    private static String state = SpiderState.READY;

    public static AtomicInteger processUrlSize = new AtomicInteger(0);

    public static AtomicInteger processItemSize = new AtomicInteger(0);

    public static void init(String path,String name2,Date startTime2) {
        id = path;
        name = name2;
        startTime = startTime2;
    }

    public static void submit() throws Exception {
        int pus = processUrlSize.get();
        int pis = processItemSize.get();
        Date now = new Date();
        SpiderNode spiderNode = new SpiderNode();
        spiderNode.setId(id);
        spiderNode.setName(name);
        spiderNode.setState(state);
        spiderNode.setProcessUrlSize(pus);
        spiderNode.setStartTime(startTime);
        spiderNode.setProcessItemSize(pis);
//        runtime and itemRate and urlRate was calculated in monitor side;
        spiderNode.setRunTime(0.0D);
        spiderNode.setItemRate(0.0D);
        spiderNode.setUrlRate(0.0D);
        spiderNode.setUpdateTime(now);
        client.setData().forPath(id, JSON.toJSONString(spiderNode).getBytes());
    }
}
