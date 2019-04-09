package cn.dpider.monitor.service.impl;

import cn.dpider.monitor.Monitor;
import cn.dpider.monitor.po.Page;
import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.po.UrlSchedulerNode;
import cn.dpider.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private Monitor monitor;

    @Override
    public List<SpiderNode> getAllSpider(Page page) {
        return monitor.getAllSpider();
    }

    @Override
    public List<UrlSchedulerNode> getAllUrlScheduler(Page page) {
        return monitor.getAllUrlScheduler();
    }
}
