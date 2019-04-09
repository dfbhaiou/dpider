package cn.dpider.common.utils;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Ww on 2017/11/11.
 */
public class ContextUtil {
    public static ClassPathXmlApplicationContext loadSpringContext(String ... files) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext(files);
        context.start();
        return context;
    }

    public static void loadLog4jContext(String filePath) {
        PropertyConfigurator.configure(filePath);
    }
}
