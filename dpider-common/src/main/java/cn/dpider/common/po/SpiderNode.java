package cn.dpider.common.po;

import java.util.Date;

public class SpiderNode {

    private String id;

    private String name;

    private String state;

    private Integer processUrlSize;

    private Date startTime;

    private Date updateTime;

    private Double runTime;

    private Integer processItemSize;

    private Double itemRate;

    private Double urlRate;

    public Double getItemRate() {
        return itemRate;
    }

    public void setItemRate(Double itemRate) {
        this.itemRate = itemRate;
    }

    public Double getUrlRate() {
        return urlRate;
    }

    public void setUrlRate(Double urlRate) {
        this.urlRate = urlRate;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getProcessUrlSize() {
        return processUrlSize;
    }

    public void setProcessUrlSize(Integer processUrlSize) {
        this.processUrlSize = processUrlSize;
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

    public Integer getProcessItemSize() {
        return processItemSize;
    }

    public void setProcessItemSize(Integer processItemSize) {
        this.processItemSize = processItemSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
