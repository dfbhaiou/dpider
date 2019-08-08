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
import cn.dpider.spider.scheduler.IndexSlaveScheduler;
import cn.dpider.spider.scheduler.ListSlaveScheduler;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;

/**
 * Created by Ww on 2017/11/10.
 */
public class SpiderMain {
    private static Logger logger = Logger.getLogger(SpiderMain.class);



    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = ContextUtil.loadSpringContext(
                "spring/spring-dpider-spider.xml");

        ListSlaveScheduler listSlaveScheduler = (ListSlaveScheduler) context.getBean("listSlaveScheduler");
        IndexSlaveScheduler indexSlaveScheduler = (IndexSlaveScheduler) context.getBean("indexSlaveScheduler");
        
        Spider spider = Spider.create(new KwPageProcessor(indexSlaveScheduler))
        		.setScheduler(listSlaveScheduler)
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


        logger.info("spider\t" + spider.getUUID() + "startPage = " + Constant.getInt("startPage") + " endPage = " + Constant.getInt("endPage"));
        
        AddPage(listSlaveScheduler);
        spider.run();
        logger.info("spider\t" + spider.getUUID() +" flushBufferList completed");
        exit(context);
    }
    
    
    public static void AddPage(ListSlaveScheduler listSlaveScheduler){
    	int startPage = Constant.getInt("startPage");
		int endPage = Constant.getInt("endPage");
		for (int i = startPage; i < endPage; i++) {
			String url = "http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=" + i;
			Request request = new Request();
			request.setUrl(url);
			Map<String, Object> extras = new HashMap<String,Object>();
			extras.put(Request.CYCLE_TRIED_TIMES, "0");
			request.setExtras(extras);
			Task task=null;
			listSlaveScheduler.push(request,task);
		}
    }

    private static void exit(ClassPathXmlApplicationContext context) {
        context.close();
        System.exit(0);
    }

}
