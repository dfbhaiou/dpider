package cn.dpider.spider.job;


import cn.dpider.spider.spiderMonitor.SpiderMonitorCenter;

public class UploadSpiderInfoJob {

    public void run() {
        try {
            SpiderMonitorCenter.submit();
        } catch (Exception e) {
        }
    }
}
