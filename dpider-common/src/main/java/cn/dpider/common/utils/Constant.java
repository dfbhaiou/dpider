package cn.dpider.common.utils;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * Created by wanhongji on 2017/11/20.
 */
public class Constant {
    private static Logger logger = Logger.getLogger(Constant.class);

    private static Properties props;
    
    private static  ArrayList<String> stringArrayList ;

    private static String defaultLoadProperties = "spider.properties";//默认加载配置文件
    private static String defaultUserAgents = "userAgents.txt";
    private static InputStreamReader reader;
    private static BufferedReader bufferedReader;
    
    /**
     * 静态加载配置文件
     */
    static{
        props = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("").getFile()+ defaultLoadProperties));
            props.load(is);
            
            stringArrayList = new ArrayList<>();
            File file = new File(Thread.currentThread().getContextClassLoader().getResource("").getFile()+ defaultUserAgents);
            reader = new InputStreamReader(new FileInputStream(file));
            bufferedReader = new BufferedReader(reader);
            String lineText;
            while ((lineText = bufferedReader.readLine()) != null){
                stringArrayList.add(lineText.replaceAll("User-Agent:","").trim());
            }
        } catch (IOException ex) {
        	ex.printStackTrace();
            logger.warn("Could not load spider.properties" ,ex.getCause());
        }  finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeBufferReader(bufferedReader);
            IOUtils.closeInputStreamReader(reader);
        }
    }

    public static String randomUserAgent(){
        Random random = new Random();
        return stringArrayList.get(random.nextInt(stringArrayList.size()-1));
    }
    
    /**
     * 保存全局属性值(缓存)
     */
    private static Map<String, String> map = Maps.newHashMap();


    /**
     * 获取配置属性
     * @param key
     * @return
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = props.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }


    public static Integer getInt(String key) {
        return getInt(key, null);
    }

    public static  Integer getInt(String key, Integer defaultValue) {
        String value = getConfig(key);
        if (value != null)
            return Integer.parseInt(value.trim());
        return defaultValue;
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static Long getLong(String key, Long defaultValue) {
        String value = getConfig(key);
        if (value != null)
            return Long.parseLong(value.trim());
        return defaultValue;
    }


    public static Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }


    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String value = getConfig(key);
        if (value != null) {
            value = value.toLowerCase().trim();
            if ("true".equals(value))
                return true;
            else if ("false".equals(value))
                return false;
            throw new RuntimeException("The value can not parse to Boolean : "
                    + value);
        }
        return defaultValue;
    }
}
