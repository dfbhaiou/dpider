package cn.dpider.spider.pageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.dpider.spider.po.Song;
import cn.dpider.spider.scheduler.SongSlaveScheduler;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * Created by dfbhaiou on 2019/8/1.
 */
public class KwPageProcessor3 implements PageProcessor {

	private int pageNum = 30;
	
	private static Logger logger = Logger.getLogger(KwPageProcessor3.class);
	
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
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");
	private  SongSlaveScheduler songSlaveScheduler;
	
	public KwPageProcessor3(SongSlaveScheduler songSlaScheduler) {
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
		int totalPage = 1;
		int curPage = 1;
		System.out.println("KwPageProcessor3 --> " + "url:" + page.getUrl());
		if(page.getUrl().regex(SINGER_LIST_VIEW_JOSN).match()){
			
			String total = new JsonPathSelector("$.data.total").select(page.getRawText());
			totalPage = (int)Math.ceil(Double.valueOf(total)/pageNum);
			
			List<String>  jsonList = new JsonPathSelector("$.data.list").selectList(page.getRawText());
			List<Song> list =new ArrayList<Song>();
			for(String str : jsonList){
				logger.debug(str);
				JSONObject  strJson = JSONObject.parseObject(str);
				Song song = new Song();
				song.setAlbum(strJson.getString("album"));
				song.setLyric(strJson.getString("musicrid"));
				song.setName(strJson.getString("name"));
				song.setSinger(strJson.getString("artist"));
				song.setComments(0);
				song.setKwid(0L);
				list.add(song);
			}
			page.putField("song", list);
			
			String url =  page.getUrl().toString();
			String curPageStr = url.substring(url.indexOf("pn=")+3, url.indexOf("&rn="));
			if(curPageStr!=""){
				curPage = Integer.parseInt(curPageStr);
				if(curPage<=totalPage){
					url = url.replace("pn=" + (curPage), "pn=" + (curPage+1));
	
					Request request = new Request();
					request.setUrl(url);
					Map<String, Object> extras = new HashMap<String,Object>();
					extras.put(Request.CYCLE_TRIED_TIMES, "0");
					request.setExtras(extras);
					Task task=null;
					songSlaveScheduler.push(request, task);
				}
			}	
			curPage ++;
		}
	}

	public static void main(String[] args) {
		/*Spider.create(new KwPageProcessor3())
				// 从"https://github.com/code4craft"开始抓
				//.addUrl("http://www.kuwo.cn/artist/content?name=g.e.m.邓紫棋")
				.addUrl("http://www.kuwo.cn/api/www/artist/artistMusic?artistid=5371&pn=1&rn=30&reqId=89a1cfc5X150bX4642Xb385X99a947b8d885")
				// 开启5个线程抓取
				.thread(1)
				// 启动爬虫
				.run();*/
		
		
		String url =  "http://www.kuwo.cn/api/www/artist/artistMusic?artistid=5371&pn=12&rn=30&reqId=89a1cfc5X150bX4642Xb385X99a947b8d885";
		String curPageStr = url.substring(url.indexOf("pn=")+3, url.indexOf("&rn="));
		System.out.println(curPageStr);
	}
	
	
	

}
