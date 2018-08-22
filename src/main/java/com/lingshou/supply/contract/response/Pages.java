package com.lingshou.supply.contract.response;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class Pages<T> implements Serializable{

    /**
     * 当前页
     */
    protected Integer limit = 0;
    /**
     * 行数
     */
    protected Integer offset = 0;
    /**
     * 总记录数
     */
    protected Integer total = 0;
    protected List<T> list = Lists.newArrayList();

    @Override
    public String toString() {
        return "Pages{" +
                "limit=" + limit +
                ", offset=" + offset +
                ", total=" + total +
                ", list=" + list +
                '}';
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Pages(Integer limit, Integer offset, Integer total, List<T> list) {

        this.limit = limit;
        this.offset = offset;
        this.total = total;
        this.list = list;
    }

    public Pages() {

    }
}
