/**
 * BaseEntity.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.base;

import java.io.Serializable;

/**
 * <p>
 * The Base Class of all Database Entity.
 * </p>
 * 
 * @author ma_b
 */
public class BaseEntity implements Cloneable, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -6021304438136244791L;

    /** Sort Group No.(Use for parts sort) */
    private String sortGroupNo;

    /** Sort Group Type(Use for parts sort) */
    private Integer sortGroupType;

    /** Sort Group Index(Use for parts sort) */
    private Integer sortGroupIdex;

    /**
     * The Constructors Method.
     */
    public BaseEntity() {

    }

    /**
     * Get the sortGroupNo.
     *
     * @return sortGroupNo
     */
    public String getSortGroupNo() {
        return this.sortGroupNo;
    }

    /**
     * Set the sortGroupNo.
     *
     * @param sortGroupNo sortGroupNo
     */
    public void setSortGroupNo(String sortGroupNo) {
        this.sortGroupNo = sortGroupNo;
    }

    /**
     * Get the sortGroupType.
     *
     * @return sortGroupType
     */
    public Integer getSortGroupType() {
        return this.sortGroupType;
    }

    /**
     * Set the sortGroupType.
     *
     * @param sortGroupType sortGroupType
     */
    public void setSortGroupType(Integer sortGroupType) {
        this.sortGroupType = sortGroupType;
    }

    /**
     * Get the sortGroupIdex.
     *
     * @return sortGroupIdex
     */
    public Integer getSortGroupIdex() {
        return this.sortGroupIdex;
    }

    /**
     * Set the sortGroupIdex.
     *
     * @param sortGroupIdex sortGroupIdex
     */
    public void setSortGroupIdex(Integer sortGroupIdex) {
        this.sortGroupIdex = sortGroupIdex;
    }

}
