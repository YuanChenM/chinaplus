/**
 * BaseParam.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.util.List;

/**
 * The parameter from page request.
 * 
 * @param <T> the class type of the entity that is in result
 */
public class ObjectParam<T> extends BaseParam {

    /** serialVersionUID. */
    private static final long serialVersionUID = -146841497016795082L;

    /** the data of result. */
    private T data;

    /** the data of result. */
    private List<T> datas;

    /**
     * The Constructors Method.
     */
    public ObjectParam() {
        super();
    }

    /**
     * Get the data.
     *
     * @return data
     */
    public T getData() {
        return this.data;
    }

    /**
     * Set the data.
     *
     * @param data data
     */
    public void setData(T data) {
        this.data = data;
        
    }

    /**
     * TO string.
     * 
     * @return tostring
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Get the datas.
     *
     * @return datas
     */
    public List<T> getDatas() {
        return this.datas;
    }

    /**
     * Set the datas.
     *
     * @param datas datas
     */
    public void setDatas(List<T> datas) {
        this.datas = datas;
        
    }
}
