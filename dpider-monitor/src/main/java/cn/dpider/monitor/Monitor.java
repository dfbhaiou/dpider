package cn.dpider.monitor;

import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.po.UrlSchedulerNode;

import java.util.List;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public interface Monitor {

    /**
     * 基于Tomcat容器，将会调用此方法开始监控（建议做一些初始化工作），监控工作包括两部分：
     *  1.自动监控urlScheduler节点和spider节点的状况，出现状况实现自动报警
     *  2.对web端监控的支持，用于获取节点信息等功能
     */
    void startMonitor();

    void refreshAllSpider();

    /**
     * id：该节点zkPath
     * @param id
     */
    void refreshSingleSpider(String id);

    void refreshAllUrlScheduler();

    void refreshSingleUrlScheduler(String id);

    List<SpiderNode> getAllSpider();

    List<UrlSchedulerNode> getAllUrlScheduler();

    SpiderNode getSpiderById(String id);

    UrlSchedulerNode getUrlSchedulerById(String id);

//    todo 修改某个爬虫的权重等功能
}
