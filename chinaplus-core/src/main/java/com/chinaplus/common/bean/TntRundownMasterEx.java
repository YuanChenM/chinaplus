/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TntRdAttachCfc;
import com.chinaplus.common.entity.TntRdMaster;

/**
 * TntRundownMasterEx.
 */
public class TntRundownMasterEx extends TntRdMaster {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** TTC_PARTS_NO */
    private String ttcPartsNo;

    /** PARTS_NAME_EN */
    private String partNameEn;

    /** Office Code */
    private String officeCode;

    /** PARTS_NAME_CN */
    private String partNameCn;

    /** CUST_BACK_NO */
    private String custBackNo;

    /** EXP_REGION */
    private String expRegion;

    /** TTC_SUPP_CODE */
    private String ttcSupplierCode;

    /** SUPP_PARTS_NO */
    private String supplierPartsNo;

    /** IMP_REGION */
    private String impRegion;

    /** CUSTOMER_CODE */
    private String customerCode;

    /** CUST_PARTS_NO */
    private String custPartsNo;

    /** BUSINESS_TYPE */
    private Integer businessType;

    /** TNM_EXP_PARTS.REMARK1 */
    private String partsRemark;

    /** TNM_EXP_PARTS.REMARK1 */
    private String timeZone;

    /** UOM */
    private String uomCode;

    /** carModel */
    private String carModel;

    /** orion data time */
    private Timestamp orionDataTime;

    /** digit */
    private Integer digit;

    /** digit */
    private String expPartsSet;

    /** digit */
    private Integer expPartsId;

    /** officeId */
    private Integer officeId;
    
    /** businessPattern */
    private Integer businessPattern;

    /** Old Parts No. */
    private String oldPartsNo;

    /** spq */
    private BigDecimal spq;

    /** Part Type */
    private Integer partType;

    /** On Shipping Delay Pattern */
    private Integer delayAdjPattern;

    /** Build Out Indicator */
    private Integer buildoutFlag;

    /** Build Out Indicator */
    private String buildoutFlagName;

    /** Build Out Month */
    private String buildoutMonth;

    /** Last PO Month */
    private String lastPoMonth;

    /** stockDate */
    private Date stockDate;

    /** endTargetDate */
    private Date endTargetDate;

    /** tempFilePath */
    private String tempFilePath;

    /** Ending Stock days (Until Last Day of Target Month for Firm PO) */
    private BigDecimal lastStockDays;

    /** Add On Qty to Avoid Shortage */
    private BigDecimal lastShortAddOnQty;

    /** Add On Qty to Achieve Safety Stock Level */
    private BigDecimal lastSaftyAddOnQty;

    /** not in run-down list */
    private List<TntNotInRundownEx> yetEtdList;

    /** not in run-down list */
    private List<TntNotInRundownEx> yetInboundList;

    /** not in run-down list */
    private List<TntNotInRundownEx> impWhOnHoldList;

    /** not in run-down list */
    private List<TntNotInRundownEx> notInRundownList;

    /** run-down Detail list */
    private List<TntRundownDetailEx> rundownDetailList;

    /** run-down detail attach information */
    private List<TntRdDetailAttachEx> rdDetailAttachList;

    /** run-down detail attach information */
    private List<TntRdAttachCfc> shareInfoList;

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
     * Get the partNameEn.
     *
     * @return partNameEn
     */
    public String getPartNameEn() {
        return this.partNameEn;
    }

    /**
     * Set the partNameEn.
     *
     * @param partNameEn partNameEn
     */
    public void setPartNameEn(String partNameEn) {
        this.partNameEn = partNameEn;

    }

    /**
     * Get the partNameCn.
     *
     * @return partNameCn
     */
    public String getPartNameCn() {
        return this.partNameCn;
    }

    /**
     * Set the partNameCn.
     *
     * @param partNameCn partNameCn
     */
    public void setPartNameCn(String partNameCn) {
        this.partNameCn = partNameCn;

    }

    /**
     * Get the custBackNo.
     *
     * @return custBackNo
     */
    public String getCustBackNo() {
        return this.custBackNo;
    }

    /**
     * Set the custBackNo.
     *
     * @param custBackNo custBackNo
     */
    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;

    }

    /**
     * Get the expRegion.
     *
     * @return expRegion
     */
    public String getExpRegion() {
        return this.expRegion;
    }

    /**
     * Set the expRegion.
     *
     * @param expRegion expRegion
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;

    }

    /**
     * Get the ttcSupplierCode.
     *
     * @return ttcSupplierCode
     */
    public String getTtcSupplierCode() {
        return this.ttcSupplierCode;
    }

    /**
     * Set the ttcSupplierCode.
     *
     * @param ttcSupplierCode ttcSupplierCode
     */
    public void setTtcSupplierCode(String ttcSupplierCode) {
        this.ttcSupplierCode = ttcSupplierCode;

    }

    /**
     * Get the supplierPartsNo.
     *
     * @return supplierPartsNo
     */
    public String getSupplierPartsNo() {
        return this.supplierPartsNo;
    }

    /**
     * Set the supplierPartsNo.
     *
     * @param supplierPartsNo supplierPartsNo
     */
    public void setSupplierPartsNo(String supplierPartsNo) {
        this.supplierPartsNo = supplierPartsNo;

    }

    /**
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }

    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;

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
     * Get the businessType.
     *
     * @return businessType
     */
    public Integer getBusinessType() {
        return this.businessType;
    }

    /**
     * Set the businessType.
     *
     * @param businessType businessType
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;

    }

    /**
     * Get the partsRemark.
     *
     * @return partsRemark
     */
    public String getPartsRemark() {
        return this.partsRemark;
    }

    /**
     * Set the partsRemark.
     *
     * @param partsRemark partsRemark
     */
    public void setPartsRemark(String partsRemark) {
        this.partsRemark = partsRemark;

    }

    /**
     * Get the rdDetailAttachList.
     *
     * @return rdDetailAttachList
     */
    public List<TntRdDetailAttachEx> getRdDetailAttachList() {
        return this.rdDetailAttachList;
    }

    /**
     * Set the rdDetailAttachList.
     *
     * @param rdDetailAttachList rdDetailAttachList
     */
    public void setRdDetailAttachList(List<TntRdDetailAttachEx> rdDetailAttachList) {
        this.rdDetailAttachList = rdDetailAttachList;

    }

    /**
     * Get the uomCode.
     *
     * @return uomCode
     */
    public String getUomCode() {
        return this.uomCode;
    }

    /**
     * Set the uomCode.
     *
     * @param uomCode uomCode
     */
    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;

    }

    /**
     * Get the digit.
     *
     * @return digit
     */
    public Integer getDigit() {
        return this.digit;
    }

    /**
     * Set the digit.
     *
     * @param digit digit
     */
    public void setDigit(Integer digit) {
        this.digit = digit;

    }

    /**
     * Get the yetEtdList.
     *
     * @return yetEtdList
     */
    public List<TntNotInRundownEx> getYetEtdList() {
        return this.yetEtdList;
    }

    /**
     * Set the yetEtdList.
     *
     * @param yetEtdList yetEtdList
     */
    public void setYetEtdList(List<TntNotInRundownEx> yetEtdList) {
        this.yetEtdList = yetEtdList;

    }

    /**
     * Get the yetInboundList.
     *
     * @return yetInboundList
     */
    public List<TntNotInRundownEx> getYetInboundList() {
        return this.yetInboundList;
    }

    /**
     * Set the yetInboundList.
     *
     * @param yetInboundList yetInboundList
     */
    public void setYetInboundList(List<TntNotInRundownEx> yetInboundList) {
        this.yetInboundList = yetInboundList;

    }

    /**
     * Get the impWhOnHoldList.
     *
     * @return impWhOnHoldList
     */
    public List<TntNotInRundownEx> getImpWhOnHoldList() {
        return this.impWhOnHoldList;
    }

    /**
     * Set the impWhOnHoldList.
     *
     * @param impWhOnHoldList impWhOnHoldList
     */
    public void setImpWhOnHoldList(List<TntNotInRundownEx> impWhOnHoldList) {
        this.impWhOnHoldList = impWhOnHoldList;

    }

    /**
     * Get the orionDataTime.
     *
     * @return orionDataTime
     */
    public Timestamp getOrionDataTime() {
        return this.orionDataTime;
    }

    /**
     * Set the orionDataTime.
     *
     * @param orionDataTime orionDataTime
     */
    public void setOrionDataTime(Timestamp orionDataTime) {
        this.orionDataTime = orionDataTime;

    }

    /**
     * Get the officeCode.
     *
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     *
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;

    }

    /**
     * Get the rundownDetailList.
     *
     * @return rundownDetailList
     */
    public List<TntRundownDetailEx> getRundownDetailList() {
        return this.rundownDetailList;
    }

    /**
     * Set the rundownDetailList.
     *
     * @param rundownDetailList rundownDetailList
     */
    public void setRundownDetailList(List<TntRundownDetailEx> rundownDetailList) {
        this.rundownDetailList = rundownDetailList;

    }

    /**
     * Get the oldPartsNo.
     *
     * @return oldPartsNo
     */
    public String getOldPartsNo() {
        return this.oldPartsNo;
    }

    /**
     * Set the oldPartsNo.
     *
     * @param oldPartsNo oldPartsNo
     */
    public void setOldPartsNo(String oldPartsNo) {
        this.oldPartsNo = oldPartsNo;

    }

    /**
     * Get the carModel.
     *
     * @return carModel
     */
    public String getCarModel() {
        return this.carModel;
    }

    /**
     * Set the carModel.
     *
     * @param carModel carModel
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;

    }

    /**
     * Get the partType.
     *
     * @return partType
     */
    public Integer getPartType() {
        return this.partType;
    }

    /**
     * Set the partType.
     *
     * @param partType partType
     */
    public void setPartType(Integer partType) {
        this.partType = partType;

    }

    /**
     * Get the delayAdjPattern.
     *
     * @return delayAdjPattern
     */
    public Integer getDelayAdjPattern() {
        return this.delayAdjPattern;
    }

    /**
     * Set the delayAdjPattern.
     *
     * @param delayAdjPattern delayAdjPattern
     */
    public void setDelayAdjPattern(Integer delayAdjPattern) {
        this.delayAdjPattern = delayAdjPattern;

    }

    /**
     * Get the buildoutFlag.
     *
     * @return buildoutFlag
     */
    public Integer getBuildoutFlag() {
        return this.buildoutFlag;
    }

    /**
     * Set the buildoutFlag.
     *
     * @param buildoutFlag buildoutFlag
     */
    public void setBuildoutFlag(Integer buildoutFlag) {
        this.buildoutFlag = buildoutFlag;

    }

    /**
     * Get the buildoutMonth.
     *
     * @return buildoutMonth
     */
    public String getBuildoutMonth() {
        return this.buildoutMonth;
    }

    /**
     * Set the buildoutMonth.
     *
     * @param buildoutMonth buildoutMonth
     */
    public void setBuildoutMonth(String buildoutMonth) {
        this.buildoutMonth = buildoutMonth;

    }

    /**
     * Get the lastPoMonth.
     *
     * @return lastPoMonth
     */
    public String getLastPoMonth() {
        return this.lastPoMonth;
    }

    /**
     * Set the lastPoMonth.
     *
     * @param lastPoMonth lastPoMonth
     */
    public void setLastPoMonth(String lastPoMonth) {
        this.lastPoMonth = lastPoMonth;

    }

    /**
     * Get the spq.
     *
     * @return spq
     */
    public BigDecimal getSpq() {
        return this.spq;
    }

    /**
     * Set the spq.
     *
     * @param spq spq
     */
    public void setSpq(BigDecimal spq) {
        this.spq = spq;

    }

    /**
     * Get the lastStockDays.
     *
     * @return lastStockDays
     */
    public BigDecimal getLastStockDays() {
        return this.lastStockDays;
    }

    /**
     * Set the lastStockDays.
     *
     * @param lastStockDays lastStockDays
     */
    public void setLastStockDays(BigDecimal lastStockDays) {
        this.lastStockDays = lastStockDays;

    }

    /**
     * Get the lastShortAddOnQty.
     *
     * @return lastShortAddOnQty
     */
    public BigDecimal getLastShortAddOnQty() {
        return this.lastShortAddOnQty;
    }

    /**
     * Set the lastShortAddOnQty.
     *
     * @param lastShortAddOnQty lastShortAddOnQty
     */
    public void setLastShortAddOnQty(BigDecimal lastShortAddOnQty) {
        this.lastShortAddOnQty = lastShortAddOnQty;

    }

    /**
     * Get the lastSaftyAddOnQty.
     *
     * @return lastSaftyAddOnQty
     */
    public BigDecimal getLastSaftyAddOnQty() {
        return this.lastSaftyAddOnQty;
    }

    /**
     * Set the lastSaftyAddOnQty.
     *
     * @param lastSaftyAddOnQty lastSaftyAddOnQty
     */
    public void setLastSaftyAddOnQty(BigDecimal lastSaftyAddOnQty) {
        this.lastSaftyAddOnQty = lastSaftyAddOnQty;

    }

    /**
     * Get the shareInfoList.
     *
     * @return shareInfoList
     */
    public List<TntRdAttachCfc> getShareInfoList() {
        return this.shareInfoList;
    }

    /**
     * Set the shareInfoList.
     *
     * @param shareInfoList shareInfoList
     */
    public void setShareInfoList(List<TntRdAttachCfc> shareInfoList) {
        this.shareInfoList = shareInfoList;

    }

    /**
     * Get the notInRundownList.
     *
     * @return notInRundownList
     */
    public List<TntNotInRundownEx> getNotInRundownList() {
        return this.notInRundownList;
    }

    /**
     * Set the notInRundownList.
     *
     * @param notInRundownList notInRundownList
     */
    public void setNotInRundownList(List<TntNotInRundownEx> notInRundownList) {
        this.notInRundownList = notInRundownList;

    }

    /**
     * Get the endTargetDate.
     *
     * @return endTargetDate
     *
     * @author liu_yinchuan
     */
    public Date getEndTargetDate() {
        return this.endTargetDate;
    }

    /**
     * Set the endTargetDate.
     *
     * @param endTargetDate endTargetDate
     *
     * @author liu_yinchuan
     */
    public void setEndTargetDate(Date endTargetDate) {
        this.endTargetDate = endTargetDate;

    }

    /**
     * Get the expPartsSet.
     *
     * @return expPartsSet
     *
     * @author liu_yinchuan
     */
    public String getExpPartsSet() {
        return this.expPartsSet;
    }

    /**
     * Set the expPartsSet.
     *
     * @param expPartsSet expPartsSet
     *
     * @author liu_yinchuan
     */
    public void setExpPartsSet(String expPartsSet) {
        this.expPartsSet = expPartsSet;

    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     *
     * @author liu_yinchuan
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     *
     * @author liu_yinchuan
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
        
    }

    /**
     * Get the buildoutFlagName.
     *
     * @return buildoutFlagName
     *
     * @author liu_yinchuan
     */
    public String getBuildoutFlagName() {
        return this.buildoutFlagName;
    }

    /**
     * Set the buildoutFlagName.
     *
     * @param buildoutFlagName buildoutFlagName
     *
     * @author liu_yinchuan
     */
    public void setBuildoutFlagName(String buildoutFlagName) {
        this.buildoutFlagName = buildoutFlagName;
        
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     *
     * @author liu_yinchuan
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     *
     * @author liu_yinchuan
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
        
    }

    /**
     * Get the timeZone.
     *
     * @return timeZone
     *
     * @author liu_yinchuan
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone timeZone
     *
     * @author liu_yinchuan
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        
    }

    /**
     * Get the officeId.
     *
     * @return officeId
     *
     * @author liu_yinchuan
     */
    public Integer getOfficeId() {
        return this.officeId;
    }

    /**
     * Set the officeId.
     *
     * @param officeId officeId
     *
     * @author liu_yinchuan
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
        
    }

    /**
     * Get the stockDate.
     *
     * @return stockDate
     *
     * @author liu_yinchuan
     */
    public Date getStockDate() {
        return this.stockDate;
    }

    /**
     * Set the stockDate.
     *
     * @param stockDate stockDate
     *
     * @author liu_yinchuan
     */
    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
        
    }

    /**
     * Get the tempFilePath.
     *
     * @return tempFilePath
     *
     * @author liu_yinchuan
     */
    public String getTempFilePath() {
        return this.tempFilePath;
    }

    /**
     * Set the tempFilePath.
     *
     * @param tempFilePath tempFilePath
     *
     * @author liu_yinchuan
     */
    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        
    }

}
