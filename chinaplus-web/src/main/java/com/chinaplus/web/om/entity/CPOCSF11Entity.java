/**
 * CPOCSF11Entity.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Upload Data Entity.
 */
public class CPOCSF11Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -9110717705053638462L;

    /** Business Pattern */
    private String codeCategory;

    /** Code Name */
    private String codeName;

    /** No. */
    private int no;

    /** TTC P/N (V-V) & Customer P/N (AISIN) */
    private String partNo;

    /** TTC Customer Code */
    private String customerCode;

    /** Customer Stock */
    private BigDecimal customerStock;

    /** Customer Stock Date (Ending Stock) */
    private Date customerStockDate;

    /** Customer Stock Date (Ending Stock) */
    private String customerStockDateStr;

    /** Business Pattern List */
    private List<String> codeCategoryList;

    /** Parts Id */
    private BigDecimal partsId;
    
    /** Decimal Digits */
    private Integer decimalDigits;
    
    /** TTC PARTS NO */
    private String ttcPartsNo;
    
    /** CUST PARTS NO */
    private String custPartsNo;
    
    /** Business Patterns */
    private Integer businessPattern;
    
    /** OS_CUST_STOCK_FLAG */
    private Integer osCustStockFlag;
    
    /** SA_CUST_STOCK_FLAG */
    private Integer saCustStockFlag;
    
    /** ENDING_STOCK_DATE */
    private Date endingStockDate;
    
    /** DATA_EFFECTIVE_FLG */
    private Boolean dataEffectiveFlg;
    
    /** DB_DATA */
    private String dbData;
    
    /** createdBy. */
    private Integer createdBy;
    
    /** createdDate. */
    private Timestamp createdDate;
    
    /** updatedBy. */
    private Integer updatedBy;
    
    /** updatedDate. */
    private Timestamp updatedDate;
    
    /** Customer Stock String */
    private String customerStockStr;
    
    /**
     * Get the codeName.
     *
     * @return codeName
     */
    public String getCodeName() {
        return this.codeName;
    }

    /**
     * Set the codeName.
     *
     * @param codeName codeName
     */
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    /**
     * Get the codeCategory.
     *
     * @return codeCategory
     */
    public String getCodeCategory() {
        return this.codeCategory;
    }

    /**
     * Set the codeCategory.
     *
     * @param codeCategory codeCategory
     */
    public void setCodeCategory(String codeCategory) {
        this.codeCategory = codeCategory;
    }

    /**
     * Get the no.
     *
     * @return no
     */
    public int getNo() {
        return this.no;
    }

    /**
     * Set the no.
     *
     * @param no no
     */
    public void setNo(int no) {
        this.no = no;
    }

    /**
     * Get the partNo.
     *
     * @return partNo
     */
    public String getPartNo() {
        return this.partNo;
    }

    /**
     * Set the partNo.
     *
     * @param partNo partNo
     */
    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    /**
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Get the customerStock.
     *
     * @return customerStock
     */
    public BigDecimal getCustomerStock() {
        return this.customerStock;
    }

    /**
     * Set the customerStock.
     *
     * @param customerStock customerStock
     */
    public void setCustomerStock(BigDecimal customerStock) {
        this.customerStock = customerStock;
    }

    /**
     * Get the customerStockDate.
     *
     * @return customerStockDate
     */
    public Date getCustomerStockDate() {
        return this.customerStockDate;
    }

    /**
     * Set the customerStockDate.
     *
     * @param customerStockDate customerStockDate
     */
    public void setCustomerStockDate(Date customerStockDate) {
        this.customerStockDate = customerStockDate;
    }

    /**
     * Get the customerStockDateStr.
     *
     * @return customerStockDateStr
     */
    public String getCustomerStockDateStr() {
        return this.customerStockDateStr;
    }

    /**
     * Set the customerStockDateStr.
     *
     * @param customerStockDateStr customerStockDateStr
     */
    public void setCustomerStockDateStr(String customerStockDateStr) {
        this.customerStockDateStr = customerStockDateStr;
    }

    /**
     * Get the codeCategoryList.
     *
     * @return codeCategoryList
     */
    public List<String> getCodeCategoryList() {
        return this.codeCategoryList;
    }

    /**
     * Set the codeCategoryList.
     *
     * @param codeCategoryList codeCategoryList
     */
    public void setCodeCategoryList(List<String> codeCategoryList) {
        this.codeCategoryList = codeCategoryList;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public BigDecimal getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(BigDecimal partsId) {
        this.partsId = partsId;
    }


    /**
     * Get the decimalDigits.
     *
     * @return decimalDigits
     */
    public Integer getDecimalDigits() {
        return this.decimalDigits;
    }

    /**
     * Set the decimalDigits.
     *
     * @param decimalDigits decimalDigits
     */
    public void setDecimalDigits(Integer decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    /**
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    /**
     * Get the custPartsNo.
     *
     * @return custPartsNo
     */
    public String getCustPartsNo() {
        return this.custPartsNo;
    }

    /**
     * Set the custPartsNo.
     *
     * @param custPartsNo custPartsNo
     */
    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }

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
     * Get the osCustStockFlag.
     *
     * @return osCustStockFlag
     */
    public Integer getOsCustStockFlag() {
        return this.osCustStockFlag;
    }

    /**
     * Set the osCustStockFlag.
     *
     * @param osCustStockFlag osCustStockFlag
     */
    public void setOsCustStockFlag(Integer osCustStockFlag) {
        this.osCustStockFlag = osCustStockFlag;
    }

    /**
     * Get the saCustStockFlag.
     *
     * @return saCustStockFlag
     */
    public Integer getSaCustStockFlag() {
        return this.saCustStockFlag;
    }

    /**
     * Set the saCustStockFlag.
     *
     * @param saCustStockFlag saCustStockFlag
     */
    public void setSaCustStockFlag(Integer saCustStockFlag) {
        this.saCustStockFlag = saCustStockFlag;
    }

    /**
     * Get the endingStockDate.
     *
     * @return endingStockDate
     */
    public Date getEndingStockDate() {
        return this.endingStockDate;
    }

    /**
     * Set the endingStockDate.
     *
     * @param endingStockDate endingStockDate
     */
    public void setEndingStockDate(Date endingStockDate) {
        this.endingStockDate = endingStockDate;
    }

    /**
     * Get the dataEffectiveFlg.
     *
     * @return dataEffectiveFlg
     */
    public Boolean getDataEffectiveFlg() {
        return this.dataEffectiveFlg;
    }

    /**
     * Set the dataEffectiveFlg.
     *
     * @param dataEffectiveFlg dataEffectiveFlg
     */
    public void setDataEffectiveFlg(Boolean dataEffectiveFlg) {
        this.dataEffectiveFlg = dataEffectiveFlg;
    }

    /**
     * Get the dbData.
     *
     * @return dbData
     */
    public String getDbData() {
        return this.dbData;
    }

    /**
     * Set the dbData.
     *
     * @param dbData dbData
     */
    public void setDbData(String dbData) {
        this.dbData = dbData;
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
     * Get the customerStockStr.
     *
     * @return customerStockStr
     */
    public String getCustomerStockStr() {
        return this.customerStockStr;
    }

    /**
     * Set the customerStockStr.
     *
     * @param customerStockStr customerStockStr
     */
    public void setCustomerStockStr(String customerStockStr) {
        this.customerStockStr = customerStockStr;
    }

    
}