package cn.dpider.urlScheduler.job;

import cn.dpider.urlScheduler.urlScdMoniter.UrlSchedulerMonitorCenter;
import org.springframework.beans.factory.annotation.Autowired;

public class UploadUrlScheInfoJob {

    @Autowired
    UrlSchedulerMonitorCenter urlSchedulerMonitorCenter;

    public void run() {
        try {
            urlSchedulerMonitorCenter.submit();
        } catch (Exception e) {
        }
    }
}
