package cn.dpider.urlScheduler.duplicate;

import cn.dpider.urlScheduler.po.SimpleRequest;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractDuplicater implements Duplicater {
    @Override
    public boolean isDuplicate(SimpleRequest simpleRequest) {
        if (simpleRequest == null) {
            return true;
        }
        String url = simpleRequest.getUrl();
        if (StringUtils.isEmpty(url)) {
            return true;
        }
        return judge(url);
    }

    protected abstract boolean judge(String url);
}
