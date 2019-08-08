package cn.dpider.urlScheduler.api;

import cn.dpider.urlScheduler.po.SimpleRequest;

public interface UrlSchedulerService {

    void push(SimpleRequest simpleRequest);

    SimpleRequest poll();
    
    void push(SimpleRequest simpleRequest,String redisKey);
    
    SimpleRequest poll(String redisKey);
}
