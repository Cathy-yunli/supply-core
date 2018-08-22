package com.lingshou.supply.common.vo.page;

import java.util.List;

/**
 * @author siliang.zheng
 * Date : 2018/5/8
 * Describle 分页基类VO
 */
public class PageBaseVO<T> {
    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页的数量
     */
    private Integer pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 结果集
     */
    private List<T> list;

    public Integer getPageNum() {
        return pageNum;
    }

    public PageBaseVO setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public PageBaseVO setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Long getTotal() {
        return total;
    }

    public PageBaseVO setTotal(Long total) {
        this.total = total;
        return this;
    }

    public Integer getPages() {
        return pages;
    }

    public PageBaseVO setPages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public List<T> getList() {
        return list;
    }

    public PageBaseVO setList(List<T> list) {
        this.list = list;
        return this;
    }

    @Override
    public String toString() {
        return "PageBaseVO{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", pages=" + pages +
                ", list=" + list +
                '}';
    }
}
