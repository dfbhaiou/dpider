package cn.dpider.urlScheduler.duplicate;

import cn.dpider.urlScheduler.po.SimpleRequest;

public interface Duplicater {

    boolean isDuplicate(SimpleRequest simpleRequest);
    
}
