package cn.dpider.common.po;

import java.util.Date;

public class UrlSchedulerNode {

    private String id;

    private String name;

    private Long hpSize;

    private Long lpSize;

    private Long consumedSize;

    private Double rate;

    private Date startTime;

    private Date updateTime;

    private Double runTime;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getHpSize() {
        return hpSize;
    }

    public void setHpSize(Long hpSize) {
        this.hpSize = hpSize;
    }

    public Long getLpSize() {
        return lpSize;
    }

    public void setLpSize(Long lpSize) {
        this.lpSize = lpSize;
    }

    public Long getConsumedSize() {
        return consumedSize;
    }

    public void setConsumedSize(Long consumedSize) {
        this.consumedSize = consumedSize;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Double getRunTime() {
        return runTime;
    }

    public void setRunTime(Double runTime) {
        this.runTime = runTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
