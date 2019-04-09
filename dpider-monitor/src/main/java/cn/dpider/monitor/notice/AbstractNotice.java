package cn.dpider.monitor.notice;

import cn.dpider.common.utils.TimeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
public abstract class AbstractNotice implements Notice {

    @Override
    public void notice(String content) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        content += "\n******************************************\n山海通知团队\n敬上\n" + TimeUtils.dateToString();
        doNotice(content);
    }

    protected abstract void doNotice(String content);
}
