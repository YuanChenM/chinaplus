/**
 * CPMMAF01Entity.java
 * 
 * @screen CPMMAF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;

import com.chinaplus.core.base.BaseEntity;


/** 
 * CPMMAF01Entity.
 */ 
public class CPMMAF01Entity extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** newMod */
    private String newMod;
    
    /** reason */
    private String reason;
    
    /** loginId */
    private String loginId;
    
    /** impOfficeCode */
    private String impOfficeCode;
    
    /** ttcCustomerCode */
    private String ttcCustomerCode;
    
    /** alertLevel */
    private String alertLevel;
    
    /** remark */
    private String remark;
    
    /** inActiveFlag */
    private Integer inActiveFlag;
    
    /** CREATE_BY */
    private String createdBy;
    
    /** CREATE_DATE */
    private Timestamp createdDate;
    
    /** UPDATE_BY */
    private String updatedBy;
    
    /** UPDATE_DATE */
    private Timestamp updatedDate;
    
    /** version */
    private Integer version;
    
    /** officeId */
    private Integer officeId;
    
    /** excelLine */
    private Integer excelLine;

    /**
     * Get the loginId.
     *
     * @return loginId
     */
    public String getLoginId() {
        return this.loginId;
    }

    /**
     * Set the loginId.
     *
     * @param loginId loginId
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get the impOfficeCode.
     *
     * @return impOfficeCode
     */
    public String getImpOfficeCode() {
        return this.impOfficeCode;
    }

    /**
     * Set the impOfficeCode.
     *
     * @param impOfficeCode impOfficeCode
     */
    public void setImpOfficeCode(String impOfficeCode) {
        this.impOfficeCode = impOfficeCode;
    }

    /**
     * Get the ttcCustomerCode.
     *
     * @return ttcCustomerCode
     */
    public String getTtcCustomerCode() {
        return this.ttcCustomerCode;
    }

    /**
     * Set the ttcCustomerCode.
     *
     * @param ttcCustomerCode ttcCustomerCode
     */
    public void setTtcCustomerCode(String ttcCustomerCode) {
        this.ttcCustomerCode = ttcCustomerCode;
    }

    /**
     * Get the alertLevel.
     *
     * @return alertLevel
     */
    public String getAlertLevel() {
        return this.alertLevel;
    }

    /**
     * Set the alertLevel.
     *
     * @param alertLevel alertLevel
     */
    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    /**
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Get the inActiveFlag.
     *
     * @return inActiveFlag
     */
    public Integer getInActiveFlag() {
        return this.inActiveFlag;
    }

    /**
     * Set the inActiveFlag.
     *
     * @param inActiveFlag inActiveFlag
     */
    public void setInActiveFlag(Integer inActiveFlag) {
        this.inActiveFlag = inActiveFlag;
    }

    /**
     * Get the createdBy.
     *
     * @return createdBy
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the officeId.
     *
     * @return officeId
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * Get the newMod.
     *
     * @return newMod
     */
    public String getNewMod() {
        return this.newMod;
    }

    /**
     * Set the newMod.
     *
     * @param newMod newMod
     */
    public void setNewMod(String newMod) {
        this.newMod = newMod;
    }

    /**
     * Get the reason.
     *
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Set the reason.
     *
     * @param reason reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Get the excelLine.
     *
     * @return excelLine
     */
    public Integer getExcelLine() {
        return this.excelLine;
    }

    /**
     * Set the excelLine.
     *
     * @param excelLine excelLine
     */
    public void setExcelLine(Integer excelLine) {
        this.excelLine = excelLine;
    }

}
