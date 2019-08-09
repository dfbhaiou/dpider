package cn.dpider.spider.pageProcessor;

import cn.dpider.common.utils.Constant;
import cn.dpider.common.utils.PageUtil;
import cn.dpider.common.utils.TimeUtils;
import cn.dpider.spider.po.Song;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 万洪基 on 2017/8/12.
 */
public class KwPageProcessor_bak implements PageProcessor {

    private static Logger logger = Logger.getLogger(KwPageProcessor_bak.class);

//     Domain
    private final String DOMAIN = "http://www\\.kuwo\\.cn";
//    歌手列表
    private final String SINGER_LIST_VIEW = DOMAIN + "/artist/indexAjax.*";
//    歌曲列表
    private final String SONG_LIST_VIEW = DOMAIN + "/artist/contentMusicsAjax\\?artistId=\\d+&pn=\\d*&rn=\\d*";
//    歌曲详情
    private final String SONG_VIEW = DOMAIN + "/yinyue/\\d+";
//    歌手首页
    private final String SINGER_INDEX_VIEW = DOMAIN + "/artist/content\\?name=.+";


    private Site site =
            Site.me().addHeader("Referer",DOMAIN)
                    .setDomain(DOMAIN)
                    .setRetryTimes(1)
                    .setSleepTime(1)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");



    public void process(Page page) {
        long currentSingerId = 0;
        String currentInfo = "";

        /***
         * 如果是歌手列表页面
         * http://www.kuwo.cn/artist/indexAjax?category=0&prefix=&pn=0
         * info：
         * SINGER_INDEX_VIEW（歌手首页）
         */
        if (page.getUrl().regex(SINGER_LIST_VIEW).match()) {
            page.addTargetRequests(page.getHtml().links().regex(SINGER_INDEX_VIEW).all());
            currentInfo = "kwSpider/歌手列表页面/";
        }
        /***
         * 如果是歌手首页
         * http://www.kuwo.cn/artist/content?name=%E8%B5%B5%E9%9B%B7
         * info：
         * <div class="page" data-page="12"></div>
         * 歌曲列表总数
         * 歌手artistId
         */
        if (page.getUrl().regex(SINGER_INDEX_VIEW).match()) {
//            System.out.println("如果是歌手首页");
            String listCount = page.getHtml().xpath("//div[@class='page']/outerHtml()").get();
            listCount = PageUtil.getValueByKeyInHtml(listCount,"data-page");
            currentSingerId = Long.parseLong(PageUtil.getValueByKeyInHtml(
                    page.getHtml().xpath("//div[@class='artistTop']/outerHtml()").get(),
                    "data-artistid"
            ));
//            System.out.println("currentSingerId:\t"+currentSingerId);
            List<String> urlList = new LinkedList<>();
            for (int i = 0; i < Integer.parseInt(listCount); i++) {
                urlList.add("http://www.kuwo.cn/artist/contentMusicsAjax?artistId=" + currentSingerId + "&pn=" + i + "&rn=15");
            }
            page.addTargetRequests(urlList);
            currentInfo = "kwSpider/歌手首页页面/currentSingerId = " + currentSingerId + "/listCount = " + listCount;
        }

        /***
         * 如果是歌曲列表
         * http://www.kuwo.cn/artist/contentMusicsAjax?artistId=125658&pn=2&rn=15
         * info:
         * SONG_VIEW（歌曲详情）
         */
        if (page.getUrl().regex(SONG_LIST_VIEW).match()) {
//            System.out.println("如果是歌曲列表");
            page.addTargetRequests(page.getHtml().links().regex(SONG_VIEW).all());
            currentInfo = "kwSpider/歌曲列表页面/";
        }

        /***
         * 如果是歌曲详情
         * http://www.kuwo.cn/yinyue/6468891
         * info：
         * 歌曲
         *      |-name
         *      |-singer
         *      |-album
         *      |-lyric
         *      |-kwid
         *      |-comments
         */
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
        }

        site.setUserAgent(Constant.randomUserAgent());
        currentInfo = TimeUtils.dateToString(new Date(System.currentTimeMillis())) + ":\t" + currentInfo;
        logger.info("currentInfo:\t" + currentInfo);

//        向本爬虫的监控中心汇报处理URL数量
        AtomicInteger processUrlSize = SpiderMonitorCenter.processUrlSize;
        processUrlSize.incrementAndGet();
    }

    public Site getSite() {
        return site;
    }

  

}
