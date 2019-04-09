package cn.dpider.monitor.po;


public class Page {

    int page;

    int limit;

    @Override
    public String toString() {
        return "PageInfo{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
