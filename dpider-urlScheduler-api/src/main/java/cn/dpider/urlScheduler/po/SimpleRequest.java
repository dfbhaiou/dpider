package cn.dpider.urlScheduler.po;

import java.io.Serializable;
import java.util.Map;

public class SimpleRequest implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6412389746391954816L;

	private String url;

    private Map<String, Object> extras;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public Object getExtra(String key) {
        return this.extras.get(key);
    }
    
    
}
