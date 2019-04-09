package cn.dpider.urlScheduler.support.redis;

import cn.dpider.common.constant.ConstantValue;
import cn.dpider.common.utils.Constant;
import cn.dpider.urlScheduler.support.AbstractUrlSchedulerService;
import cn.dpider.urlScheduler.utils.RedisCacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class RedisUrlSchedulerServiceImpl extends AbstractUrlSchedulerService {

    @Autowired
    private RedisCacheManager redisCacheManager;

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    protected void pushWhenNoDuplicate(String url) {
        if (isHighPriorityUrl(url)) {
            redisCacheManager.lladd(ConstantValue.REDIS_HP_URL, url);
        }
        redisCacheManager.lladd(ConstantValue.REDIS_LP_URL, url);
        return;
    }

    @Override
    protected String doPoll() {
        String url = null;
        if (count.get() >= Constant.getInt("redis-hp-null-times")) {
            url = (String) redisCacheManager.lrpop(ConstantValue.REDIS_LP_URL);
        } else {
            url = (String) redisCacheManager.lrpop(ConstantValue.REDIS_HP_URL);
            if (StringUtils.isNotEmpty(url)) {
                count.set(0);
            } else {
                count.incrementAndGet();
                url = (String) redisCacheManager.lrpop(ConstantValue.REDIS_LP_URL);
            }
        }
        return url;
    }
}
