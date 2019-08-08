package cn.dpider.spider.pipeline;

import cn.dpider.common.utils.TimeUtils;
import cn.dpider.spider.po.Song;
import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 万洪基 on 2017/8/13.
 */
public class KwPipeLine implements Pipeline {

    private static Logger logger = Logger.getLogger(KwPipeLine.class);

    private int objectCount;

    private Lock lock = new ReentrantLock();

    private List<Song> bufferList = new ArrayList<>();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public KwPipeLine() {
        objectCount = 0;
    }

    public void process(ResultItems resultItems, Task task) {
        if (resultItems.get("song") != null) {
            List<Song> song = resultItems.get("song");

            if (bufferList.size() < 500) {
                lock.lock();
                try {
                    bufferList.addAll(song);
                } finally {
                    lock.unlock();
                }
            } else {
                lock.lock();
                try {
//                    批量方案（完成）
                    if (bufferList.size() < 500) {
                        bufferList.addAll(song);
                    } else {
                        try {
//                            为了性能，用SpringJDBC
                            Date date = new Date();
                            batchInsertSelective(bufferList);
                            Date date1 = new Date();
                            addObjectCount(500);
                            System.out.println("[INFO] 线程\t" + Thread.currentThread().getId() + "\t本次插入所花时间：\t" + (date1.getTime() - date.getTime()) + "\tcurrentObjectCount = " + objectCount);
                            bufferList.clear();
                            logger.info("[INFO] 线程\t" + Thread.currentThread().getId() + "\t本次插入所花时间：\t" + (date1.getTime() - date.getTime()) + "\tcurrentObjectCount = " + objectCount);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error(e.getMessage());
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }

////            单条插入
//            try {
//                System.out.println("pipeLine song \t:" + song);
//                songMapper.insertSelective(song);
//                System.out.println("insert successful");
//                objectCount++;
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//            }

            resultItems.put("song", null);

//            向本爬虫的监控中心汇报处理的Item数
            AtomicInteger processItemSize = SpiderMonitorCenter.processItemSize;
            processItemSize.incrementAndGet();
        }
    }

    //    @Transactional("dbTransaction")
//    @Transactional("abc")
    protected void batchInsertSelective(List<Song> songList) {
        if (songList != null && songList.size() >= 1) {
//            batch import 必须insert 所有字段,空字段用 _ 或 0作为缺省值
            StringBuffer sqlbuf = new StringBuffer()
                    .append("insert into song (name, album, singer, lyric, kwid, comments) values ");
            for (Song song : songList) {
                sqlbuf.append(generateSQL(song) + ",");
            }
            String sql = sqlbuf.delete(sqlbuf.length() - 1, sqlbuf.length()).toString();
            jdbcTemplate.update(sql);
        }
        return;

    }

    protected String generateSQL(Song song) {
        StringBuffer buffer = new StringBuffer("(");
        if (!StringUtils.isEmpty(song.getName())) {
            buffer.append("'" + song.getName() + "',");
        } else {
            buffer.append("'_',");
        }
        if (!StringUtils.isEmpty(song.getAlbum())) {
            buffer.append("'" + song.getAlbum() + "',");
        } else {
            buffer.append("'_',");
        }
        if (!StringUtils.isEmpty(song.getSinger())) {
            buffer.append("'" + song.getSinger() + "',");
        } else {
            buffer.append("'_',");
        }
        if (!StringUtils.isEmpty(song.getLyric())) {
//            buffer.append("'" + song.getLyric() + "',");
            buffer.append("'_',");
        } else {
            buffer.append("'_',");
        }
        if (song.getKwid() != null) {
            buffer.append(song.getKwid() + ",");
        } else {
            buffer.append("0,");
        }
        if (song.getComments() != null) {
            buffer.append(song.getComments() + ",");
        } else {
            buffer.append("0,");
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        buffer.append(")");
        return buffer.toString();
    }

    public int getObjectCount() {
        return objectCount;
    }

    public void addObjectCount(int size) {
        objectCount +=size;
    }

    public void flushBufferList() {
        try {
            batchInsertSelective(bufferList);
            addObjectCount(bufferList.size());
            bufferList.clear();
            System.out.println(TimeUtils.dateToString() + "\tflushBufferList()\tcurrentObjectCount = " + objectCount);
            logger.info(TimeUtils.dateToString() + "[INFO] 线程\t" + Thread.currentThread().getId() + " flushBufferList() Completed! \tcurrentObjectCount = " + objectCount);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
