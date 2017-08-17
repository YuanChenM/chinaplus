/**
 * @screen CPSRDF11Entity
 * @author zhang_chi
 */
package com.chinaplus.web.sa.entity;

import com.chinaplus.web.mm.entity.MMCommonEntity;

/**
 * CPSRDF11Entity.
 */
public class CPSRDF11Entity extends MMCommonEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L; 
    
    
    /** ttcPartNo */
    private String ttcPartNo;
    
    /** impOfficeCode */
    private String impOfficeCode;
    
    /** ttcCustomerCode */
    private String ttcCustomerCode;
    
    /** rundownRemark */
    private String rundownRemark;
    
    /** partsId */
    private Integer partsId;  
    
    /** rowNum */
    private Integer rowNum;
    
    /** businessPattern */
    private Integer businessPattern;
    
    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }
    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }
    /**
     * Get the rowNum.
     *
     * @return rowNum
     */
    public Integer getRowNum() {
        return this.rowNum;
    }
    /**
     * Set the rowNum.
     *
     * @param rowNum rowNum
     */
    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
    
    /**
     * Get the ttcPartNo.
     *
     * @return ttcPartNo
     */
    public String getTtcPartNo() {
        return this.ttcPartNo;
    }
    /**
     * Set the ttcPartNo.
     *
     * @param ttcPartNo ttcPartNo
     */
    public void setTtcPartNo(String ttcPartNo) {
        this.ttcPartNo = ttcPartNo;
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
     * Get the rundownRemark.
     *
     * @return rundownRemark
     */
    public String getRundownRemark() {
        return this.rundownRemark;
    }
    /**
     * Set the rundownRemark.
     *
     * @param rundownRemark rundownRemark
     */
    public void setRundownRemark(String rundownRemark) {
        this.rundownRemark = rundownRemark;
    }
    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public Integer getPartsId() {
        return this.partsId;
    }
    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

}
