package cn.dpider.urlScheduler.support;

import cn.dpider.urlScheduler.api.UrlSchedulerService;
import cn.dpider.urlScheduler.duplicate.Duplicater;
import cn.dpider.urlScheduler.po.SimpleRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public abstract class AbstractUrlSchedulerService implements UrlSchedulerService {

    private static final String LIST_URL = "";

    private static final String DETAIL_URL = "";

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private Duplicater duplicater;

    @Override
    public void push(SimpleRequest simpleRequest) {
        if (simpleRequest == null) {
            return;
        }
        String url = simpleRequest.getUrl();
        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (!duplicater.isDuplicate(simpleRequest) || this.shouldReserved(simpleRequest)) {
            this.logger.debug("push to queue {}", url);
            this.pushWhenNoDuplicate(url);
        }
    }

    protected void pushWhenNoDuplicate(String url) {
    }

    protected boolean isHighPriorityUrl(String url) {
        return Pattern.matches(LIST_URL,url);
    }

    protected boolean isLowPriorityUrl(String url) {
        return Pattern.matches(DETAIL_URL,url);
    }

    protected boolean shouldReserved(SimpleRequest request) {
        return request.getExtra("_cycle_tried_times") != null;
    }

    @Override
    public SimpleRequest poll() {
        SimpleRequest simpleRequest = new SimpleRequest();
        String url = doPoll();
        if (StringUtils.isEmpty(url)) {
            url = "";
        }
        simpleRequest.setUrl(url);
        return simpleRequest;
    }

    protected String doPoll() {
        return null;
    }

    public void setDuplicater(Duplicater duplicater) {
        this.duplicater = duplicater;
    }
}
