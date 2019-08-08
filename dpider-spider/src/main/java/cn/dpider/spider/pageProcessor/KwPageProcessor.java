package cn.dpider.spider.pageProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.PageUtil;
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
    private ArrayList<String> userAgents;
    
    IndexSlaveScheduler indexSlaveScheduler ;
    
    private int pageNum = 30;
	private int totalPage = 1;
	private int curPage = 2;
	
    public KwPageProcessor( IndexSlaveScheduler indexSlScheduler) {
        try {
            userAgents = PageUtil.getUserAgentList(Constant.getConfig("userAgents"));
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
        long currentSingerId = 0;
        String currentInfo = "";

        /***
         * 如果是歌手列表页面
         * http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
         * info：
         * SINGER_INDEX_VIEW（歌手首页）
         */
        site.setUserAgent(randomUserAgent());
        
        if (page.getUrl().regex(SINGER_LIST_VIEW).match()) {
        	addAllPages(page.getHtml().links().regex(SINGER_INDEX_VIEW).all());
            currentInfo = "kwSpider/歌手列表页面/";
        }
       
        /***
         * 如果是歌手首页
         * http://www.kuwo.cn/artist/content?name=%E8%B5%B5%E9%9B%B7
         * info：
         * 歌手artistId
         *//*
        if (page.getUrl().regex(SINGER_INDEX_VIEW).match()) {
        	String artistid = page.getHtml().xpath("//div[@class='artistTop']/@data-artistid").toString();
			String reqId = page.getHtml().xpath("//input[@name=reqId]/@value").toString().trim();
            String url = SINGER_LIST_JOSN_ADDRESS + "artistid=" + artistid +"&pn=1&rn=30&reqId="+reqId;
            List<String> list = new ArrayList<String>();
            list.add(url);
            addAllPages(list);
            currentInfo = "kwSpider/歌手首页页面/currentSingerId = " + artistid + "/reqId = " + reqId;
        }
        
        *//***
         * 如果是歌曲列表接口
         *http://www.kuwo.cn/api/www/artist/artistMusic?artistid=5371&pn=1&rn=30&reqId=bec131c1-b736-11e9-afcb-7d94eefb324a
         * info：
         * 歌曲List
         *      |-name 歌曲名
         *      |-artist 演唱人
         *      |-album 专辑
         *      |-musicrid
         *      |- wid 
         *//*
        if(page.getUrl().regex(SINGER_LIST_VIEW_JOSN).match()){
			
			if(curPage==2){
				String total = new JsonPathSelector("$.data.total").select(page.getRawText());
				totalPage = (int)Math.ceil(Double.valueOf(total)/pageNum);
			}
			
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
			
			if(curPage <= totalPage){
				String url =  page.getUrl().toString();
				url = url.replace("pn=" + (curPage-1), "pn=" + (curPage));
				List<String> newUrlList = new ArrayList<String>();
				newUrlList.add(url);
	            addAllPages(newUrlList);
			}	
			curPage ++;
			currentInfo = "kwSpider/歌曲详情页面/总页数：" + totalPage +"/当前页：" + curPage;
		}*/

        /***
         * 如果是歌曲列表
         * http://www.kuwo.cn/artist/contentMusicsAjax?artistId=125658&pn=2&rn=15
         * info:
         * SONG_VIEW（歌曲详情）
         *//*
        if (page.getUrl().regex(SONG_LIST_VIEW).match()) {
//            System.out.println("如果是歌曲列表");
            page.addTargetRequests(page.getHtml().links().regex(SONG_VIEW).all());
            currentInfo = "kwSpider/歌曲列表页面/";
        }

        *//***
         * 如果是歌曲详情
         * http://www.kuwo.cn/yinyue/6468891
         * info：
         * 歌曲
         *      |-name 歌曲名
         *      |-singer 演唱人
         *      |-album 专辑
         *      |-lyric
         *      |-kwid  
         *      |-comments 歌词
         *//*
        if (page.getUrl().regex(SONG_VIEW).match()) {
//            System.out.println("如果是歌曲详情页面");
            Song song = new Song();
            song.setName(page.getHtml().xpath("//p[@id='lrcName']/text()").get());
            song.setSinger(page.getHtml().xpath("//p[@class='artist']/span/a/text()").get());
            song.setAlbum(page.getHtml().xpath("//p[@class='album']/span/a/text()").get());
            song.setLyric(page.getHtml().xpath("//div[@id='lrcContent']/tidyText()").get());
            song.setKwid(Long.valueOf(PageUtil.getRequestName(page.getUrl().toString())));
//            设置JsonPath获取comments
            JsonPathSelector selector = new JsonPathSelector("$.total");
            try {
                String comments = selector.select(PageUtil.sendRequest(
                        "http://comment.kuwo.cn/com.s?type=get_comment&uid=0&digest=15&sid=" + song.getKwid() + "&page=1&rows=20&f=web"
                ));
                song.setComments(Integer.parseInt(comments));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (song != null){
                page.putField("song",song);
            }else {
                page.setSkip(true);
            }
//            System.out.println("song:\t"+song);
            currentInfo = "kwSpider/歌曲详情页面/songName = " + song.getName() + "/singer = " + song.getSinger() +"/album = " + song.getAlbum() + "/kwid = " + song.getKwid() + "/comments = " + song.getComments();
        }*/
		
        currentInfo = TimeUtils.dateToString(new Date(System.currentTimeMillis())) + ":\t" + currentInfo;
        logger.info("currentInfo:\t" + currentInfo);

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

    public String randomUserAgent(){
        Random random = new Random();
        return userAgents.get(random.nextInt(userAgents.size()-1));
    }

}
