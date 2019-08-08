package cn.dpider.spider.pageProcessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.dpider.spider.scheduler.SongSlaveScheduler;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by dfbhaiou on 2019/8/1.
 */
public class KwPageProcessor2 implements PageProcessor {

	private int pageNum = 30;
	private int totalPage = 1;
	private int curPage = 1;
	
	private static Logger logger = Logger.getLogger(KwPageProcessor2.class);
	
	private final String DOMAIN = "http://www.kuwo.cn";
	private final String API_DOMAIN = "http://www.kuwo.cn/api/";
	// 歌手列表
	private final String SINGER_LIST_VIEW = DOMAIN + "/artist/indexAjax.*";

	private final String SINGER_LIST_VIEW_JOSN = API_DOMAIN + "www/artist/artistMusic\\?artistid.+";
	
	private final String SINGER_LIST_JOSN_ADDRESS =  API_DOMAIN + "www/artist/artistMusic?";
	// 歌手首页
	private final String SINGER_INDEX_VIEW = DOMAIN + "/artist/content\\?name=.+";

	private Site site = Site.me().addHeader("Referer", DOMAIN).setDomain(DOMAIN).setRetryTimes(3).setSleepTime(3000)
			.setTimeOut(10000)
			.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
	private  SongSlaveScheduler songSlaveScheduler;
	
	public KwPageProcessor2(SongSlaveScheduler songSlaScheduler) {
        try {
        	songSlaveScheduler = songSlaScheduler;
        } catch (Exception e) {
            logger.error(e);
        }
    }
	
	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		
		logger.debug("KwPageProcessor2 --> " + "url:" + page.getUrl() + ",p:" +page.getUrl().regex(SINGER_LIST_VIEW_JOSN).match());
		if (page.getUrl().regex(SINGER_INDEX_VIEW).match()) {
			//logger.debug(page.getHtml().toString());
			String artistid = page.getHtml().xpath("//div[@class='artistTop']/@data-artistid").toString();
			String reqId = page.getHtml().xpath("//input[@name=reqId]/@value").toString().trim();
			System.out.println("artistid:" + artistid + ",reqId:"+ reqId);
			
			Request request = new Request();
			request.setUrl(SINGER_LIST_JOSN_ADDRESS + "artistid=" + artistid +"&pn="+curPage+"&rn=" + pageNum+"&reqId="+reqId);
			Map<String, Object> extras = new HashMap<String,Object>();
			extras.put(Request.CYCLE_TRIED_TIMES, "0");
			request.setExtras(extras);
			Task task=null;
			
			songSlaveScheduler.push(request, task);
			
			//page.addTargetRequest(SINGER_LIST_JOSN_ADDRESS + "artistid=" + artistid +"&pn="+curPage+"&rn=" + pageNum+"&reqId="+reqId);
			//http://www.kuwo.cn/api/www/artist/artistMusic?artistid=5371&pn=1&rn=30&reqId=298a4b30-b43b-11e9-92a9-3fa5acae26fd
		}
	}

	public static void main(String[] args) {
		/*Spider.create(new KwPageProcessor2())
				// 从"https://github.com/code4craft"开始抓
				.addUrl("http://www.kuwo.cn/artist/content?name=g.e.m.邓紫棋")
				.setDownloader( new KwDownloader())
				//.addUrl("http://www.kuwo.cn/api/www/artist/artistMusic?artistid=5371&pn=1&rn=30&reqId=89a1cfc5X150bX4642Xb385X99a947b8d885")
				// 开启5个线程抓取
				.thread(1)
				// 启动爬虫
				.run();*/
	}
	
	
	

}
