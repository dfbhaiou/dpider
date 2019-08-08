package cn.dpider.spider;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.dpider.common.register.Register;
import cn.dpider.common.register.RegisterRequest;
import cn.dpider.common.register.impl.DefaultRegistry;
import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.ContextUtil;
import cn.dpider.common.utils.NetUtil;
import cn.dpider.spider.pageProcessor.KwPageProcessor;
import cn.dpider.spider.pageProcessor.KwPageProcessor2;
import cn.dpider.spider.pageProcessor.KwPageProcessor3;
import cn.dpider.spider.pipeline.KwPipeLine;
import cn.dpider.spider.scheduler.IndexSlaveScheduler;
import cn.dpider.spider.scheduler.ListSlaveScheduler;
import cn.dpider.spider.scheduler.SongSlaveScheduler;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;

/**
 * Created by Ww on 2017/11/10.
 */
public class IndexSpiderMain {
    private static Logger logger = Logger.getLogger(IndexSpiderMain.class);



    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = ContextUtil.loadSpringContext(
                "spring/spring-dpider-spider.xml");

        IndexSlaveScheduler indexSlaveScheduler = (IndexSlaveScheduler) context.getBean("indexSlaveScheduler");
        SongSlaveScheduler songSlaveScheduler = (SongSlaveScheduler) context.getBean("songSlaveScheduler");
        

        
        Spider indexSpider = Spider.create(new KwPageProcessor2(songSlaveScheduler))
                .setScheduler(indexSlaveScheduler)
                .thread(Constant.getInt("thread"));

//        注册自己到节点
        Date startTime = new Date();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setSubPath("spider");
        registerRequest.setHost(NetUtil.getHostAddr());
        registerRequest.setPort("0");
        registerRequest.setName("index-spider-" + Constant.getConfig("spiderName"));
        registerRequest.setNodeInfoJson("");

        Register register = new DefaultRegistry();
        String path = register.register(registerRequest);
//        再将自身节点初始数据更新一遍到zk
        SpiderMonitorCenter.init(path,"index-spider-" + Constant.getConfig("spiderName"),startTime);
        SpiderMonitorCenter.submit();
        
        indexSpider.run();
        logger.info("indexSpider\t" + indexSpider.getUUID() +" run finished");
        logger.info("indexSpider\t" + indexSpider.getUUID() +" flushBufferList completed");
        exit(context);
    }
    

    private static void exit(ClassPathXmlApplicationContext context) {
        context.close();
        System.exit(0);
    }

}
