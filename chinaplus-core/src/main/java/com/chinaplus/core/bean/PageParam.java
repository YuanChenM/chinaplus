/**
 * PageParam.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.util.List;

/**
 * The parameter from page request.
 */
public class PageParam extends BaseParam {

    /** the default limit. */
    protected static final int DEFAULT_ROW_LIMIT = 10;

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** the start No. */
    private int start;

    /** the start No. */
    private int end;

    /** the selected datas in list. */
    private List<String> selectedDatas;

    /**
     * The Constructors Method.
     */
    public PageParam() {
        super();
        this.setLimit(DEFAULT_ROW_LIMIT);
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
     * Get the selectedDatas.
     *
     * @return selectedDatas
     */
    public List<String> getSelectedDatas() {
        return this.selectedDatas;
    }

    /**
     * Set the selectedDatas.
     *
     * @param selectedDatas selectedDatas
     */
    public void setSelectedDatas(List<String> selectedDatas) {
        this.selectedDatas = selectedDatas;
    }

    /**
     * Set the jsonData.
     *
     * @param jsonData jsonData
     */
    @SuppressWarnings("unchecked")
    public void setJsonData(String jsonData) {

        super.setJsonData(jsonData);
        List<String> selectedDatas = (List<String>) getSwapData().get("selectedDatas");
        if (selectedDatas != null) {
            setSelectedDatas(selectedDatas);
        }
    }

    /**
     * 
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Get the end.
     *
     * @return end
     *
     * @author liu_yinchuan
     */
    public int getEnd() {
        return this.end;
    }

    /**
     * Set the end.
     *
     * @param end end
     *
     * @author liu_yinchuan
     */
    public void setEnd(int end) {
        this.end = end;
        
    }

}
