/*
 * Orion System
 * Copyright(C) TOYOTSU SYSCOM CORPORATION All Rights Reserved.
 */
package com.chinaplus.common.entity;

import java.sql.Timestamp;
import java.util.List;

import com.chinaplus.common.bean.SupplyChainEntity;

/**
 * BaseTbFileEntity.
 * 
 * @author yang_jia1   
 */
public abstract class BaseInterfaceEntity extends SupplyChainEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -5213689129714942799L;

    /**
     * fileCreateDate
     */
    private Timestamp fileCreateDate;

    /**
     * fileType
     */
    private String fileType;

    /**
     * fileType
     */
    private Integer dataId;
    
    private String expCode;
    
    private Integer validFlag;
    
    /** handleFlag */
    private Integer handleFlag;
    
    /** handleFlagParam */
    private List<Integer> handleFlagParam;
    
    private Integer createdBy;
    
    private Timestamp createdDate;
    
    private Integer updatedBy;
    
    private Timestamp updatedDate;
    
    /**
     * ifDateTime
     */
    private Timestamp ifDateTime;
    
    private Integer version;
    
    /** expPartsId */
    private List<Integer> expPartsIdList; 

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
     * @return the dataId
     */
    public Integer getDataId() {
        return dataId;
    }

    /**
     * @param dataId the dataId to set
     */
    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    /**
     * @return the expCode
     */
    public String getExpCode() {
        return expCode;
    }

    /**
     * @param expCode the expCode to set
     */
    public void setExpCode(String expCode) {
        this.expCode = expCode;
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
     * @return the handleFlagParam
     */
    public List<Integer> getHandleFlagParam() {
        return handleFlagParam;
    }

    /**
     * @param handleFlagParam the handleFlagParam to set
     */
    public void setHandleFlagParam(List<Integer> handleFlagParam) {
        this.handleFlagParam = handleFlagParam;
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

    /**
     * @return the expPartsIdList
     */
    public List<Integer> getExpPartsIdList() {
        return expPartsIdList;
    }

    /**
     * @param expPartsIdList the expPartsIdList to set
     */
    public void setExpPartsIdList(List<Integer> expPartsIdList) {
        this.expPartsIdList = expPartsIdList;
    }

    /**
     * getFieldsPosition
     * @return int[]
     */
    public abstract int[] getFieldsPosition();

    /**
     * getFieldsName
     * @return String[]
     */
    public abstract String[] getFieldsName();

    /**
     * getFieldsName
     * @return int
     */
    public abstract int getTotalLength();
}
