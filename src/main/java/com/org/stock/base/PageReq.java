package com.org.stock.base;

/**
 * @Author hanjie.l
 */
public class PageReq {

    /**搜素内容*/
    private String search;
    /**页码*/
    private int pageNum = 1;
    /**页长*/
    private int pageSize = 20;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public static PageReq of(int pageNum, int pageSize) {
        PageReq pageReq = new PageReq();
        pageReq.setPageNum(pageNum);
        pageReq.setPageSize(pageSize);
        return pageReq;
    }
    public static PageReq of(String search, int pageNum, int pageSize) {
        PageReq pageReq = new PageReq();
        pageReq.setSearch(search);
        pageReq.setPageNum(pageNum);
        pageReq.setPageSize(pageSize);
        return pageReq;
    }
}
