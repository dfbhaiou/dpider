package cn.dpider.spider;
import java.util.Date;

import cn.dpider.common.constant.SpiderState;
import cn.dpider.common.po.SpiderNode;
import cn.dpider.common.register.Register;
import cn.dpider.common.register.RegisterRequest;
import cn.dpider.common.register.impl.DefaultRegistry;
import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.ContextUtil;
import cn.dpider.common.utils.NetUtil;
import cn.dpider.common.zk.ZookeeperFactory;
import cn.dpider.spider.pageProcessor.KwPageProcessor;
import cn.dpider.spider.pipeline.KwPipeLine;
import cn.dpider.spider.scheduler.SlaveScheduler;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatus;

import javax.management.JMException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ww on 2017/11/10.
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);



    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = ContextUtil.loadSpringContext(
                "spring/spring-dpider-spider.xml");
        ContextUtil.loadLog4jContext(Constant.getConfig("log4j"));

        SlaveScheduler slaveScheduler = (SlaveScheduler) context.getBean("slaveScheduler");
        KwPipeLine pipeLine = (KwPipeLine) context.getBean("kwPipeLine");

        Spider spider = Spider.create(new KwPageProcessor())
                .addPipeline(pipeLine)
                .setScheduler(slaveScheduler)
                .thread(Constant.getInt("thread"));

//        注册自己到节点
        Date startTime = new Date();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setSubPath("spider");
        registerRequest.setHost(NetUtil.getHostAddr());
        registerRequest.setPort("0");
        registerRequest.setName(Constant.getConfig("spiderName"));
        registerRequest.setNodeInfoJson("");

        Register register = new DefaultRegistry();
        String path = register.register(registerRequest);
//        再将自身节点初始数据更新一遍到zk
        SpiderMonitorCenter.init(path,Constant.getConfig("spiderName"),startTime);
        SpiderMonitorCenter.submit();


//        addAllPage(Constant.getInt("startPage"),Constant.getInt("endPage"),kwSchedulerService,spider.getUUID());
        logger.info("spider\t" + spider.getUUID() + "startPage = " + Constant.getInt("startPage") + " endPage = " + Constant.getInt("endPage"));

//        SpiderMonitor.instance().register(spider);

        spider.run();
        logger.info("spider\t" + spider.getUUID() +" run finished");
        pipeLine.flushBufferList();
        logger.info("spider\t" + spider.getUUID() +" flushBufferList completed");
        exit(context);
    }

//    public static void addAllPage(Integer startPage, Integer endPage, KwSchedulerService kwSchedulerService, String uuid) {
//        if (!kwSchedulerService.isAddAllPage()) {
//            List<String> urlList = new LinkedList<>();
//            for (int i = startPage; i <= endPage; i++) {
//                urlList.add("http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn="+i);
//            }
//            SerializableTask task1 = new SerializableTask();
//            task1.setUuid(uuid);
//            kwSchedulerService.pushAllPage(urlList,task1);
//        }
//    }

    private static void exit(ClassPathXmlApplicationContext context) {
        context.close();
        System.exit(0);
    }

}
