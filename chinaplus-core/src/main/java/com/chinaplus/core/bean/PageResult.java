/**
 * PageResult.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.util.List;

/**
 * The result of the page request.
 * 
 * @param <T> the class type of the entity that is in result
 */
public final class PageResult<T> extends BaseResult<T> {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** the datas. */
    private List<T> datas;
    /** the total count of datas. */
    private int totalCount;
    /** the start No. */
    private int start;
    /** the limit. */
    private int limit = PageParam.DEFAULT_ROW_LIMIT;

    /**
     * The Constructors Method.
     */
    public PageResult() {
        this.setLimit(PageParam.DEFAULT_ROW_LIMIT);
        this.datas = null;
    }

    /**
     * The Constructors Method.
     * 
     * @param datas the datas
     */
    public PageResult(List<T> datas) {
        this.setLimit(PageParam.DEFAULT_ROW_LIMIT);
        this.datas = datas;
    }

    /**
     * Get the data.
     * 
     * @return data
     */
    public List<T> getDatas() {
        return this.datas;
    }

    /**
     * Set the data.
     * 
     * @param datas data
     */
    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    /**
     * Get the totalCount.
     * 
     * @return totalCount
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * Set the totalCount.
     * 
     * @param totalCount totalCount
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Get the limit.
     * 
     * @return limit
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * Set the limit.
     * 
     * @param limit limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Get the start.
     * 
     * @return start
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Set the start.
     * 
     * @param start start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * To string.
     * 
     * @return string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
