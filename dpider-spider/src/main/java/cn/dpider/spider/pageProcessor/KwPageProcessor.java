package cn.dpider.spider.pageProcessor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.TimeUtils;
import cn.dpider.spider.scheduler.IndexSlaveScheduler;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by 万洪基 on 2017/8/12.
 */
public class KwPageProcessor implements PageProcessor {

    private static Logger logger = Logger.getLogger(KwPageProcessor.class);
    
    IndexSlaveScheduler indexSlaveScheduler ;
    
    private int pageNum = 30;
	private int totalPage = 1;
	private int curPage = 2;
	
    public KwPageProcessor( IndexSlaveScheduler indexSlScheduler) {
        try {
            indexSlaveScheduler = indexSlScheduler;
        } catch (Exception e) {
            logger.error(e);
        }
    }

//     Domain
    private final String DOMAIN = "http://www.kuwo.cn";
	private final String API_DOMAIN = "http://www.kuwo.cn/api/";
	
	private final String SINGER_LIST_VIEW_JOSN = API_DOMAIN + "www/artist/artistMusic\\?artistid.+";
	
//    歌手列表
    private final String SINGER_LIST_VIEW = DOMAIN + "/artist/indexAjax.*";
//    歌曲列表
    private final String SONG_LIST_VIEW = DOMAIN + "/artist/contentMusicsAjax\\?artistId=\\d+&pn=\\d*&rn=\\d*";
//    歌曲详情
    private final String SONG_VIEW = DOMAIN + "/yinyue/\\d+";
    
	private final String SINGER_LIST_JOSN_ADDRESS =  API_DOMAIN + "www/artist/artistMusic?";
	
//    歌手首页
    private final String SINGER_INDEX_VIEW = DOMAIN + "/artist/content\\?name=.+";


    private Site site =
            Site.me().addHeader("Referer",DOMAIN)
                    .setDomain(DOMAIN)
                    .setRetryTimes(3)
                    .setSleepTime(3000)
    				.setTimeOut(10000);



    public void process(Page page) {
        String currentInfo = "";

        /***
         * 如果是歌手列表页面
         * http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
         * info：
         * SINGER_INDEX_VIEW（歌手首页）
         */
        if (page.getUrl().regex(SINGER_LIST_VIEW).match()) {
        	addAllPages(page.getHtml().links().regex(SINGER_INDEX_VIEW).all());
            currentInfo = "kwSpider/歌手列表页面/";
        }
        
        currentInfo = TimeUtils.dateToString(new Date(System.currentTimeMillis())) + ":\t" + currentInfo;
        logger.info("currentInfo:\t" + currentInfo);
        site.setUserAgent(Constant.randomUserAgent());
//        向本爬虫的监控中心汇报处理URL数量
        AtomicInteger processUrlSize = SpiderMonitorCenter.processUrlSize;
        processUrlSize.incrementAndGet();
    }
    
    private  void addAllPages(List<String> urls){
    	for (int i=0;i< urls.size();i++) {
			Request request = new Request();
			request.setUrl(urls.get(i));
			Map<String, Object> extras = new HashMap<String,Object>();
			extras.put(Request.CYCLE_TRIED_TIMES, "0");
			request.setExtras(extras);
			Task task=null;
			indexSlaveScheduler.push(request,task);
		}
    }

    public Site getSite() {
        return site;
    }
  
}
