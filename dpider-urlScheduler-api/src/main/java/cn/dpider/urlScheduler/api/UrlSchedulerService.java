package cn.dpider.urlScheduler.api;

import cn.dpider.urlScheduler.po.SimpleRequest;

public interface UrlSchedulerService {

    void push(SimpleRequest simpleRequest);

    SimpleRequest poll();
}
