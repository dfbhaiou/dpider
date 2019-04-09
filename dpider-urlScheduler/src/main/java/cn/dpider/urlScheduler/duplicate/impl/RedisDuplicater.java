package cn.dpider.urlScheduler.duplicate.impl;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.urlScheduler.duplicate.AbstractDuplicater;
import cn.dpider.urlScheduler.utils.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisDuplicater extends AbstractDuplicater {

    @Autowired
    RedisCacheManager redisCacheManager;

    @Override
    protected boolean judge(String url) {
        boolean isMember = redisCacheManager.sIsMember(ConstantValue.REDIS_CONSUMED, url);
        if (!isMember) {
            redisCacheManager.sAdd(ConstantValue.REDIS_CONSUMED,url);
        }
        return isMember;
    }
}
