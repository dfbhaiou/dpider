package cn.dpider.urlScheduler;

import cn.dpider.common.register.Register;
import cn.dpider.common.register.RegisterRequest;
import cn.dpider.common.register.impl.DefaultRegistry;
import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.ContextUtil;
import cn.dpider.common.utils.NetUtil;
import cn.dpider.urlScheduler.urlScdMoniter.UrlSchedulerMonitorCenter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Date;

public class UrlSechedulerMain {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = ContextUtil.loadSpringContext(
                "spring/spring-dpider-urlScheduler.xml");
        System.out.println((Constant.getConfig("log4j")));
        ContextUtil.loadLog4jContext(Constant.getConfig("log4j"));

        UrlSchedulerMonitorCenter urlSchedulerMonitorCenter =
                (UrlSchedulerMonitorCenter) context.getBean("urlSchedulerMonitorCenter");

//        注册自己到节点
        Date startTime = new Date();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setSubPath("urlScheduler");
        registerRequest.setHost(NetUtil.getHostAddr());
        registerRequest.setPort("8181");
        registerRequest.setName(Constant.getConfig("urlSchedulerName"));
        registerRequest.setNodeInfoJson("");
        Register register = new DefaultRegistry();
        String path = register.register(registerRequest);

//        向zk提交初始化数据
        urlSchedulerMonitorCenter.init(path,Constant.getConfig("urlSchedulerName"),startTime);
        urlSchedulerMonitorCenter.submit();

        System.in.read();
    }
}
