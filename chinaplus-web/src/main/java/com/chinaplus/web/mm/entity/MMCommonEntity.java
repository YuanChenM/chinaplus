/**
 * @screen MMCommonEntity
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/** 
 * MMCommonEntity.
 */ 
public class MMCommonEntity extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = -2990545553733950210L;
    
    /** createdDate  the createdDate  */
    private  Date  createdDate;
    
    /** updatedDate  the updatedDate  */
    private  Date  updatedDate;
    
    /** createdBy  the createdBy  */
    private  Integer  createdBy;
    
    /** updatedBy  the updatedBy  */
    private  Integer  updatedBy;
    
    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getCreatedDate() {
        return this.createdDate;
    }
    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getUpdatedDate() {
        return this.updatedDate;
    }
    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }
    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }
    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    
}
