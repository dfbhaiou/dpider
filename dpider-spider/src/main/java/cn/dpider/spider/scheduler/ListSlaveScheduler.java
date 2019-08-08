package cn.dpider.spider.scheduler;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.common.utils.Constant;
import cn.dpider.spider.pageProcessor.KwPageProcessor;
import cn.dpider.urlScheduler.api.UrlSchedulerService;
import cn.dpider.urlScheduler.po.SimpleRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

/**
 * Created by Ww on 2017/11/10.
 * 关于去重问题：
 *  如果在某种情况下，在子爬虫本身先去一遍重，如果发现不重复然后再送往URL调度中心去重，
 *  也就是说子爬虫本身去重有优势的话，会考虑这种实现方式。
 *  现阶段先在URL调度中心进行去重
 *
 * 关于task对象的使用：
 *  task主要用来标识某个子爬虫
 *  先不做具体到某个子爬虫工作量的监控，所以先不用task
 *
 *  关于SimpleRequest：
 *     private String method;
 *     private Map<String, Object> extras;
 *     private long priority;
 *   以上的几个属性都先不用
 *
 */
public class ListSlaveScheduler implements Scheduler {

	 private static Logger logger = Logger.getLogger(ListSlaveScheduler.class);
	 
    @Autowired
    UrlSchedulerService urlSchedulerService;

    @Override
    public void push(Request request, Task task) {
        String url = request.getUrl();
        if (StringUtils.isEmpty(url)) {
            return;
        }
        SimpleRequest simpleRequest = new SimpleRequest();
        simpleRequest.setUrl(url);
        simpleRequest.setExtras(request.getExtras());
//        此处考虑应用dubbo的哪种集群容错策略
        urlSchedulerService.push(simpleRequest,ConstantValue.REDIS_KEY_KW_LIST);
        return;
    }

    @Override
    public Request poll(Task task) {
        SimpleRequest simpleRequest = urlSchedulerService.poll(ConstantValue.REDIS_KEY_KW_LIST);
        Request request = new Request();
        if (simpleRequest == null) {
            return request;
        }
        System.out.println("ListSlaveScheduler poll:" + simpleRequest.getUrl());
        request.setUrl(simpleRequest.getUrl());
        request.setExtras(simpleRequest.getExtras());
        return request;
    }
}
