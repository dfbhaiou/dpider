package cn.dpider.monitor.service;

import cn.dpider.monitor.po.Page;
import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.po.UrlSchedulerNode;

import java.util.List;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public interface MonitorService {

    List<SpiderNode> getAllSpider(Page page);

    List<UrlSchedulerNode> getAllUrlScheduler(Page page);
}
