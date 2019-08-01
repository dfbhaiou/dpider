package cn.dpider.monitor.zk;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.common.zk.ZookeeperFactory;
import cn.dpider.monitor.AbstractMonitor;
import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.po.UrlSchedulerNode;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZkMonitor extends AbstractMonitor {

    private CuratorFramework client = ZookeeperFactory.create();

    private ConcurrentMap<String, SpiderNode> spiderData = new ConcurrentHashMap<>();

    private ConcurrentMap<String, UrlSchedulerNode> urlSchedulerData = new ConcurrentHashMap<>();

    @PostConstruct
    @Override
    public void startMonitor() {
        try {
//            初始化spiders相关
            initSpiderCache();
//            初始化urlScheduler相关
            initUrlSchedulerCache();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 该方法对于
     *  增：新上线的，需要加入本地缓存的
     *  修改：已经存在的，需要更新到本地缓存的
     *  已经删除的：zk中已经不存在了，需要从本地删除的
     * 都适用
     * @param id
     * @throws Exception
     */
    private void updateUrlScheduler(String id) throws Exception {
        try {
            UrlSchedulerNode urlSchedulerNode;

            urlSchedulerNode = getNodeData(id, UrlSchedulerNode.class);
            urlSchedulerNode.setRunTime(getRunTime(urlSchedulerNode.getStartTime(), urlSchedulerNode.getUpdateTime()));
            urlSchedulerNode.setRate(getRate(urlSchedulerNode.getConsumedSize(), urlSchedulerNode.getRunTime()));

            urlSchedulerData.put(id, urlSchedulerNode);
        } catch (KeeperException.NoNodeException e) {
            urlSchedulerData.remove(id);
        }
    }

    /**
     * 同上
     * @param id
     * @throws Exception
     */
    private void updateSpider(String id) throws Exception {
        try {
            SpiderNode spiderNode = getNodeData(id, SpiderNode.class);

            spiderNode.setRunTime(getRunTime(spiderNode.getStartTime(), spiderNode.getUpdateTime()));
            spiderNode.setItemRate(getRate(Long.valueOf(spiderNode.getProcessItemSize()), spiderNode.getRunTime()));
            spiderNode.setUrlRate(getRate(Long.valueOf(spiderNode.getProcessUrlSize()), spiderNode.getRunTime()));

            spiderData.put(id, spiderNode);
        } catch (KeeperException.NoNodeException e) {
            spiderData.remove(id);
        }
    }

    private void initUrlSchedulerCache() throws Exception {
        String urlSchedulerZkPath = ConstantValue.SERVER_PATH + "-urlScheduler";
        List<String> urlSchedulerList = client.getChildren().forPath(urlSchedulerZkPath);
        for (String name :
                urlSchedulerList) {
            String childPath = urlSchedulerZkPath + "/" + name;
            updateUrlScheduler(childPath);
        }
        client.getChildren().usingWatcher(new UrlSchedulerWatcher()).forPath(urlSchedulerZkPath);
    }

    private void initSpiderCache() throws Exception {
        String spiderZkPath = ConstantValue.SERVER_PATH + "-spider";
        List<String> spiderList = client.getChildren().forPath(spiderZkPath);
        for (String name :
                spiderList) {
            String childPath = spiderZkPath + "/" + name;
            updateSpider(childPath);
        }
        client.getChildren().usingWatcher(new SpiderWatcher()).forPath(spiderZkPath);
    }

    private Double getRate(Long size, Double runTime) {
        if (size == null || size == 0 || runTime == null || runTime == 0) {
            return 1D;
        }
        double rate = size / runTime;
        return rate == 0 ? 1D : rate;
    }

    /**
     * 以分钟为单位
     *
     * @param startTime
     * @param updateTime
     * @return
     */
    private Double getRunTime(Date startTime, Date updateTime) {
        if (startTime == null || updateTime == null) {
            return 1D;
        }
        double runtime = updateTime.getTime() - startTime.getTime();
        runtime /= (1000 * 60);
        return runtime == 0 ? 1D : runtime;
    }

    private <T> T getNodeData(String zkPath, Class<T> clazz) throws Exception {
        byte[] dataBytes = client.getData().forPath(zkPath);
        String dataJson = new String(dataBytes);
        return JSON.parseObject(dataJson, clazz);
    }

    @Override
    public void refreshAllSpider() {
        for (String id :
                spiderData.keySet()) {
            refreshSingleSpider(id);
        }
    }

    /**
     * 在monitor侧计算runtime、rate等数据
     *
     * @param id
     */
    @Override
    public void refreshSingleSpider(String id) {
        try {
            updateSpider(id);
        } catch (Exception e) {
        }
    }

    @Override
    public void refreshAllUrlScheduler() {
        for (String id :
                urlSchedulerData.keySet()) {
            refreshSingleUrlScheduler(id);
        }
    }

    /**
     * 在monitor侧计算runtime、rate等数据
     *
     * @param id
     */
    @Override
    public void refreshSingleUrlScheduler(String id) {
        try {
            updateUrlScheduler(id);
        } catch (Exception e) {
        }
    }

    @Override
    public List<SpiderNode> getAllSpider() {
        return new ArrayList<>(spiderData.values());
    }

    @Override
    public List<UrlSchedulerNode> getAllUrlScheduler() {
        return new ArrayList<>(urlSchedulerData.values());
    }

    @Override
    public SpiderNode getSpiderById(String id) {
        return spiderData.get(id);
    }

    @Override
    public UrlSchedulerNode getUrlSchedulerById(String id) {
        return urlSchedulerData.get(id);
    }

    /**
     * 比对最新zk里的节点状态与本地缓存的节点状态的不同，将对应的不同通知给对应的人
     *
     * @param now
     * @param kind
     * @throws Exception
     */
    private void comparisonAndNotice(Set<String> now, String kind) throws Exception {
        Set<String> src = null;
        if (kind.equals("spider")) {
            src = spiderData.keySet();
        } else if (kind.equals("urlScheduler")) {
            src = urlSchedulerData.keySet();
        }
        Set<String> add = new HashSet<>();
        Set<String> rem = new HashSet<>();
        for (String t :
                src) {
            if (now.remove(t)) {
                continue;
            }
            rem.add(t);
        }
        add.addAll(now);

//        根据kind分别更新对应的map
        if (kind.equals("spider")) {
            for (String s :
                    rem) {
                spiderData.remove(s);
            }
            for (String s :
                    add) {
                updateSpider(s);
            }
            kind = "子爬虫";
        } else if (kind.equals("urlScheduler")) {
            for (String s :
                    rem) {
                urlSchedulerData.remove(s);
            }
            for (String s :
                    add) {
                updateUrlScheduler(s);
            }
            kind = "URL调度中心";
        }

//        组装报警消息并进行报警通知
        if (add.size() > 0 || rem.size() > 0) {
            StringBuilder noticeContent = new StringBuilder();
            noticeContent.append("【注意】 ")
                    .append(kind)
                    .append(" 节点发生了变化，增加的节点有")
                    .append(add.size())
                    .append("个，其中包括以下节点：")
                    .append(JSON.toJSONString(add))
                    .append("，删除的节点有")
                    .append(rem.size())
                    .append("个，其中包括以下节点：")
                    .append(JSON.toJSONString(rem))
                    .append("，请引起注意！");
            notice(noticeContent.toString());
        }
    }

    private class SpiderWatcher implements CuratorWatcher {
        @Override
        public void process(WatchedEvent watchedEvent) throws Exception {
            CuratorFramework client = ZkMonitor.this.client;
            String path = watchedEvent.getPath();
            client.getChildren().usingWatcher(this).forPath(path);

            if (watchedEvent.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)) {
                List<String> spiderList = client.getChildren().forPath(path);
                Set<String> spiderSet = new HashSet<>();
                for (String s :
                        spiderList) {
                    spiderSet.add(path + "/" + s);
                }
                comparisonAndNotice(spiderSet, "spider");
            }
        }
    }

    private class UrlSchedulerWatcher implements CuratorWatcher {
        @Override
        public void process(WatchedEvent watchedEvent) throws Exception {
            CuratorFramework client = ZkMonitor.this.client;
            String path = watchedEvent.getPath();
            client.getChildren().usingWatcher(this).forPath(path);

            if (watchedEvent.getType().equals(Watcher.Event.EventType.NodeChildrenChanged)) {
                List<String> urlSchedulerList = client.getChildren().forPath(path);
                Set<String> urlSchedulerSet = new HashSet<>();
                for (String s :
                        urlSchedulerList) {
                    urlSchedulerSet.add(path + "/" + s);
                }
                comparisonAndNotice(urlSchedulerSet, "urlScheduler");
            }
        }
    }
}
