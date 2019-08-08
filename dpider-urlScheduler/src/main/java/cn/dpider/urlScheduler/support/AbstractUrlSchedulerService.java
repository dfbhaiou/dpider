package cn.dpider.urlScheduler.support;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dpider.urlScheduler.api.UrlSchedulerService;
import cn.dpider.urlScheduler.duplicate.Duplicater;
import cn.dpider.urlScheduler.po.SimpleRequest;
import us.codecraft.webmagic.Request;

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
        if (!duplicater.isDuplicate(simpleRequest) && this.shouldReserved(simpleRequest)) {
            this.logger.debug("push to queue {}", url);
            this.pushWhenNoDuplicate(url);
        }
    }

    
    
    @Override
	public void push(SimpleRequest simpleRequest, String redisKey) {
		// TODO Auto-generated method stub
    	
    	if (simpleRequest == null) {
            return;
        }
    	System.out.println("push url:" + simpleRequest.getUrl() + ",key:" + redisKey);
        String url = simpleRequest.getUrl();
        if (StringUtils.isEmpty(url)) {
            return;
        }
        if (!duplicater.isDuplicate(simpleRequest) && this.shouldReserved(simpleRequest)) {
            this.logger.debug("push to queue {}", url);
            this.pushWhenNoDuplicate(url,redisKey);
        }
	}



	@Override
	public SimpleRequest poll(String redisKey) {
		// TODO Auto-generated method stub
		
		SimpleRequest simpleRequest = null;
        String url = doPoll(redisKey);
        if (StringUtils.isEmpty(url)) {
        	return simpleRequest;
        }
        
        simpleRequest = new SimpleRequest();
        simpleRequest.setUrl(url);
        System.out.println("poll url:" + simpleRequest.getUrl() + ",key:" + redisKey);
        Map<String, Object> extras = new HashMap<String,Object>();
        extras.put(Request.CYCLE_TRIED_TIMES, "0");
		simpleRequest.setExtras(extras);
        return simpleRequest;
	}



	protected void pushWhenNoDuplicate(String url) {
    }
	
	protected void pushWhenNoDuplicate(String url,String redisKey) {
    }

    protected boolean isHighPriorityUrl(String url) {
        return Pattern.matches(LIST_URL,url);
    }

    protected boolean isLowPriorityUrl(String url) {
        return Pattern.matches(DETAIL_URL,url);
    }

    protected boolean shouldReserved(SimpleRequest request) {
    	boolean flag = false;
    	try{
    		flag = (request.getExtra("_cycle_tried_times") != null);
    	}catch(Exception e){
    		flag = false;
    	}
        return flag;
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
    
    protected String doPoll(String redisKey) {
        return null;
    }

    public void setDuplicater(Duplicater duplicater) {
        this.duplicater = duplicater;
    }
}
