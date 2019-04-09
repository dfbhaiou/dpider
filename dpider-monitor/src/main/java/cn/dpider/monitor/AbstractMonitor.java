package cn.dpider.monitor;

import cn.dpider.monitor.notice.Notice;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public abstract class AbstractMonitor implements Monitor {

    private Notice noticeHelper;

    protected void notice(String content) {
        noticeHelper.notice(content);
    }

    public void setNoticeHelper(Notice noticeHelper) {
        this.noticeHelper = noticeHelper;
    }
}
