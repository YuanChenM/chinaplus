/*
 * Orion System
 * Copyright(C) TOYOTSU SYSCOM CORPORATION All Rights Reserved.
 */
package com.chinaplus.batch.common.bean;

import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;

/**
 * BaseTbFileEntity.
 * 
 * @author yang_jia1   
 */
public abstract class BaseCsvFileEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -5213689129714942799L;

    /** fileCreateDate */
    private Timestamp fileCreateDate;

    /** fileType */
    private String fileType;

    /** validFlag */
    private Integer validFlag;
    
    /** handleFlag */
    private Integer handleFlag;
    
    /** createdBy */
    private Integer createdBy;

    /** createdDate */
    private Timestamp createdDate;

    /** updatedBy */
    private Integer updatedBy;

    /** updatedDate */
    private Timestamp updatedDate;
    
    /** ifDateTime */
    private Timestamp ifDateTime;

    /** version */
    private Integer version;
    
    /**
     * @return the fileCreateDate
     */
    public Timestamp getFileCreateDate() {
        return fileCreateDate;
    }

    /**
     * @param fileCreateDate the fileCreateDate to set
     */
    public void setFileCreateDate(Timestamp fileCreateDate) {
        this.fileCreateDate = fileCreateDate;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the validFlag
     */
    public Integer getValidFlag() {
        return validFlag;
    }

    /**
     * @param validFlag the validFlag to set
     */
    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    /**
     * @return the handleFlag
     */
    public Integer getHandleFlag() {
        return handleFlag;
    }

    /**
     * @param handleFlag the handleFlag to set
     */
    public void setHandleFlag(Integer handleFlag) {
        this.handleFlag = handleFlag;
    }

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createdDate
     */
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the updatedBy
     */
    public Integer getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the updatedDate
     */
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    /**
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the ifDateTime
     */
    public Timestamp getIfDateTime() {
        return ifDateTime;
    }

    /**
     * @param ifDateTime the ifDateTime to set
     */
    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}
