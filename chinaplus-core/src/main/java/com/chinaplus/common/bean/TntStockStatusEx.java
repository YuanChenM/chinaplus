/**
 * Base parts information entity.
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TnfImpStockByDay;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntRdAttachShipping;
import com.chinaplus.common.entity.TntRdDetail;
import com.chinaplus.common.entity.TntStockStatus;

/**
 * Base Tnt Stock status entity.
 * 
 * @author liu_yinchuan
 */
public class TntStockStatusEx extends TntStockStatus {

    /** serialVersionUID. */
    private static final long serialVersionUID = 737386502624524774L;

    /** TTC Parts No.. */
    private String ttcPartsNo;

    /**  Parts Name In English. */
    private String partsNameEn;

    /** Parts Name In Chinese. */
    private String partsNameCn;

    /** Exp Country. */
    private String expCounty;

    /** TTC Supplier Code */
    private String supplierCode;

    /** Supplier Parts No. */
    private String supplierPartsNo;

    /** Imp Country */
    private String impCountry;

    /** Imp Office Code */
    private String impOfficeCode;
    
    /** TTC Customer Code */
    private String customerCode;
    
    /** Old Part No. */
    private String oldPartsNo;
    
    /** Customer Part No. */
    private String custPartsNo;
    
    /** Customer Back No. */
    private String custBackNo;
    
    /** Business Type */
    private Integer businessType;

    /** Business Pattern */
    private Integer businessPattern;
    
    /** Parts Type */
    private Integer partsType;

    /** Car Model */
    private String carModel;

    /** parts remark */
    private String partsRemark;

    /** rundown remark */
    private String rundownRemark;

    /** Build-out Flag */
    private String buildOutFlag;

    /** original Build-out Flag */
    private Integer buildOutFlagOrg;

    /** Build-out Month */
    private String buildOutMonth;

    /** Last TTC Imp Order Month */
    private String lastOrderMonth;

    /** Last Supplier Delivery Month */
    private String lastDeliveryMonth;

    /** Discontinue Indicator */
    private String inActiveFlag;

    /** original Discontinue Indicator */
    private Integer inActiveFlagOrg;
    
    /** digit */
    private String uomCode;
    
    /** digit */
    private Date endDateTmComp;
    
    /** digit */
    private Date endDate3Comp;
    
    /** digit */
    private Integer digit;

    /** digit */
    private Integer simulationEndDatePattern;

    /** orderDay. */
    private Integer orderDay;

    /** inactiveFlag. */
    private Integer discontinueIndicator;

    /** expPartsId. */
    private Integer expPartsId;

    /** impStockFlag. */
    private Integer impStockFlag;

    /** endCfcDate. */
    private Date endCfcDate;
    
    /** endPfcDate. */
    private Date endPfcDate;

    /** onShippingQty. */
    private boolean hasMoreSupplier;

    /** lastTargetMonth. */
    private String lastTargetMonth;

    /** totalInRackQty. */
    private BigDecimal totalInRackQty;

    /** totalImpWhsQty. */
    private BigDecimal totalImpWhsQty;

    /** totalPreparedObQty. */
    private BigDecimal totalPreparedObQty;

    /** totalNgOnholdQty. */
    private BigDecimal totalNgOnholdQty;

    /** totalEciOnholdQty. */
    private BigDecimal totalEciOnholdQty;

    /** impBalanceQty. */
    private BigDecimal impBalanceQty;

    /** impBalanceQty. */
    private BigDecimal expBalanceQty;

    /** onShippingQty. */
    private BigDecimal onShippingQty;

    /** onShippingQty. */
    private BigDecimal totalImportStockQty;

    /** totalEciOnholdQty. */
    private BigDecimal totalSupplyChainQty;

    /** totalEciOnholdQty. */
    private BigDecimal totalNotInRdQty;

    /** CustCalendarList. */
    private List<TnmCalendarDetailEx> custCalendarList;
    
    /** daily usage list. */  
    private List<TntCfcDay> dailyUsageList;
    
    /** actOutboundList. */  
    private List<TnfImpStockByDay> actOutboundList;
    
    /** rdDetailList. */  
    private List<TntRdDetail> rdDetailList;
    
    /** on-Shipping Information. */  
    private List<TntRdAttachShipping> onShippingList;

    /**
     * Get the orderDay.
     *
     * @return orderDay
     */
    public Integer getOrderDay() {
        return this.orderDay;
    }

    /**
     * Set the orderDay.
     *
     * @param orderDay orderDay
     */
    public void setOrderDay(Integer orderDay) {
        this.orderDay = orderDay;
        
    }

    /**
     * Get the impStockFlag.
     *
     * @return impStockFlag
     */
    public Integer getImpStockFlag() {
        return this.impStockFlag;
    }

    /**
     * Set the impStockFlag.
     *
     * @param impStockFlag impStockFlag
     */
    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;
        
    }

    /**
     * Get the endCfcDate.
     *
     * @return endCfcDate
     */
    public Date getEndCfcDate() {
        return this.endCfcDate;
    }

    /**
     * Set the endCfcDate.
     *
     * @param endCfcDate endCfcDate
     */
    public void setEndCfcDate(Date endCfcDate) {
        this.endCfcDate = endCfcDate;
        
    }

    /**
     * Get the lastTargetMonth.
     *
     * @return lastTargetMonth
     */
    public String getLastTargetMonth() {
        return this.lastTargetMonth;
    }

    /**
     * Set the lastTargetMonth.
     *
     * @param lastTargetMonth lastTargetMonth
     */
    public void setLastTargetMonth(String lastTargetMonth) {
        this.lastTargetMonth = lastTargetMonth;
        
    }

    /**
     * Get the totalInRackQty.
     *
     * @return totalInRackQty
     */
    public BigDecimal getTotalInRackQty() {
        return this.totalInRackQty;
    }

    /**
     * Set the totalInRackQty.
     *
     * @param totalInRackQty totalInRackQty
     */
    public void setTotalInRackQty(BigDecimal totalInRackQty) {
        this.totalInRackQty = totalInRackQty;
        
    }

    /**
     * Get the totalImpWhsQty.
     *
     * @return totalImpWhsQty
     */
    public BigDecimal getTotalImpWhsQty() {
        return this.totalImpWhsQty;
    }

    /**
     * Set the totalImpWhsQty.
     *
     * @param totalImpWhsQty totalImpWhsQty
     */
    public void setTotalImpWhsQty(BigDecimal totalImpWhsQty) {
        this.totalImpWhsQty = totalImpWhsQty;
        
    }

    /**
     * Get the totalPreparedObQty.
     *
     * @return totalPreparedObQty
     */
    public BigDecimal getTotalPreparedObQty() {
        return this.totalPreparedObQty;
    }

    /**
     * Set the totalPreparedObQty.
     *
     * @param totalPreparedObQty totalPreparedObQty
     */
    public void setTotalPreparedObQty(BigDecimal totalPreparedObQty) {
        this.totalPreparedObQty = totalPreparedObQty;
        
    }

    /**
     * Get the totalNgOnholdQty.
     *
     * @return totalNgOnholdQty
     */
    public BigDecimal getTotalNgOnholdQty() {
        return this.totalNgOnholdQty;
    }

    /**
     * Set the totalNgOnholdQty.
     *
     * @param totalNgOnholdQty totalNgOnholdQty
     */
    public void setTotalNgOnholdQty(BigDecimal totalNgOnholdQty) {
        this.totalNgOnholdQty = totalNgOnholdQty;
        
    }

    /**
     * Get the totalEciOnholdQty.
     *
     * @return totalEciOnholdQty
     */
    public BigDecimal getTotalEciOnholdQty() {
        return this.totalEciOnholdQty;
    }

    /**
     * Set the totalEciOnholdQty.
     *
     * @param totalEciOnholdQty totalEciOnholdQty
     */
    public void setTotalEciOnholdQty(BigDecimal totalEciOnholdQty) {
        this.totalEciOnholdQty = totalEciOnholdQty;
        
    }

    /**
     * Get the impBalanceQty.
     *
     * @return impBalanceQty
     */
    public BigDecimal getImpBalanceQty() {
        return this.impBalanceQty;
    }

    /**
     * Set the impBalanceQty.
     *
     * @param impBalanceQty impBalanceQty
     */
    public void setImpBalanceQty(BigDecimal impBalanceQty) {
        this.impBalanceQty = impBalanceQty;
        
    }

    /**
     * Get the onShippingQty.
     *
     * @return onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return this.onShippingQty;
    }

    /**
     * Set the onShippingQty.
     *
     * @param onShippingQty onShippingQty
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
        
    }

    /**
     * Get the custCalendarList.
     *
     * @return custCalendarList
     */
    public List<TnmCalendarDetailEx> getCustCalendarList() {
        return this.custCalendarList;
    }

    /**
     * Set the custCalendarList.
     *
     * @param custCalendarList custCalendarList
     */
    public void setCustCalendarList(List<TnmCalendarDetailEx> custCalendarList) {
        this.custCalendarList = custCalendarList;
        
    }

    /**
     * Get the dailyUsageList.
     *
     * @return dailyUsageList
     */
    public List<TntCfcDay> getDailyUsageList() {
        return this.dailyUsageList;
    }

    /**
     * Set the dailyUsageList.
     *
     * @param dailyUsageList dailyUsageList
     */
    public void setDailyUsageList(List<TntCfcDay> dailyUsageList) {
        this.dailyUsageList = dailyUsageList;
        
    }

    /**
     * Get the actOutboundList.
     *
     * @return actOutboundList
     */
    public List<TnfImpStockByDay> getActOutboundList() {
        return this.actOutboundList;
    }

    /**
     * Set the actOutboundList.
     *
     * @param actOutboundList actOutboundList
     */
    public void setActOutboundList(List<TnfImpStockByDay> actOutboundList) {
        this.actOutboundList = actOutboundList;
        
    }

    /**
     * Get the rdDetailList.
     *
     * @return rdDetailList
     */
    public List<TntRdDetail> getRdDetailList() {
        return this.rdDetailList;
    }

    /**
     * Set the rdDetailList.
     *
     * @param rdDetailList rdDetailList
     */
    public void setRdDetailList(List<TntRdDetail> rdDetailList) {
        this.rdDetailList = rdDetailList;
        
    }

    /**
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     *
     * @author liu_yinchuan
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     *
     * @author liu_yinchuan
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
        
    }

    /**
     * Get the partsNameEn.
     *
     * @return partsNameEn
     *
     * @author liu_yinchuan
     */
    public String getPartsNameEn() {
        return this.partsNameEn;
    }

    /**
     * Set the partsNameEn.
     *
     * @param partsNameEn partsNameEn
     *
     * @author liu_yinchuan
     */
    public void setPartsNameEn(String partsNameEn) {
        this.partsNameEn = partsNameEn;
        
    }

    /**
     * Get the partsNameCn.
     *
     * @return partsNameCn
     *
     * @author liu_yinchuan
     */
    public String getPartsNameCn() {
        return this.partsNameCn;
    }

    /**
     * Set the partsNameCn.
     *
     * @param partsNameCn partsNameCn
     *
     * @author liu_yinchuan
     */
    public void setPartsNameCn(String partsNameCn) {
        this.partsNameCn = partsNameCn;
        
    }

    /**
     * Get the expCounty.
     *
     * @return expCounty
     *
     * @author liu_yinchuan
     */
    public String getExpCounty() {
        return this.expCounty;
    }

    /**
     * Set the expCounty.
     *
     * @param expCounty expCounty
     *
     * @author liu_yinchuan
     */
    public void setExpCounty(String expCounty) {
        this.expCounty = expCounty;
        
    }

    /**
     * Get the supplierCode.
     *
     * @return supplierCode
     *
     * @author liu_yinchuan
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     *
     * @author liu_yinchuan
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
        
    }

    /**
     * Get the supplierPartsNo.
     *
     * @return supplierPartsNo
     *
     * @author liu_yinchuan
     */
    public String getSupplierPartsNo() {
        return this.supplierPartsNo;
    }

    /**
     * Set the supplierPartsNo.
     *
     * @param supplierPartsNo supplierPartsNo
     *
     * @author liu_yinchuan
     */
    public void setSupplierPartsNo(String supplierPartsNo) {
        this.supplierPartsNo = supplierPartsNo;
        
    }

    /**
     * Get the impCountry.
     *
     * @return impCountry
     *
     * @author liu_yinchuan
     */
    public String getImpCountry() {
        return this.impCountry;
    }

    /**
     * Set the impCountry.
     *
     * @param impCountry impCountry
     *
     * @author liu_yinchuan
     */
    public void setImpCountry(String impCountry) {
        this.impCountry = impCountry;
        
    }

    /**
     * Get the customerCode.
     *
     * @return customerCode
     *
     * @author liu_yinchuan
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     *
     * @author liu_yinchuan
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
        
    }

    /**
     * Get the oldPartsNo.
     *
     * @return oldPartsNo
     *
     * @author liu_yinchuan
     */
    public String getOldPartsNo() {
        return this.oldPartsNo;
    }

    /**
     * Set the oldPartsNo.
     *
     * @param oldPartsNo oldPartsNo
     *
     * @author liu_yinchuan
     */
    public void setOldPartsNo(String oldPartsNo) {
        this.oldPartsNo = oldPartsNo;
        
    }

    /**
     * Get the custPartsNo.
     *
     * @return custPartsNo
     *
     * @author liu_yinchuan
     */
    public String getCustPartsNo() {
        return this.custPartsNo;
    }

    /**
     * Set the custPartsNo.
     *
     * @param custPartsNo custPartsNo
     *
     * @author liu_yinchuan
     */
    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
        
    }

    /**
     * Get the custBackNo.
     *
     * @return custBackNo
     *
     * @author liu_yinchuan
     */
    public String getCustBackNo() {
        return this.custBackNo;
    }

    /**
     * Set the custBackNo.
     *
     * @param custBackNo custBackNo
     *
     * @author liu_yinchuan
     */
    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;
        
    }

    /**
     * Get the businessType.
     *
     * @return businessType
     *
     * @author liu_yinchuan
     */
    public Integer getBusinessType() {
        return this.businessType;
    }

    /**
     * Set the businessType.
     *
     * @param businessType businessType
     *
     * @author liu_yinchuan
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
        
    }

    /**
     * Get the partsType.
     *
     * @return partsType
     *
     * @author liu_yinchuan
     */
    public Integer getPartsType() {
        return this.partsType;
    }

    /**
     * Set the partsType.
     *
     * @param partsType partsType
     *
     * @author liu_yinchuan
     */
    public void setPartsType(Integer partsType) {
        this.partsType = partsType;
        
    }

    /**
     * Get the carModel.
     *
     * @return carModel
     *
     * @author liu_yinchuan
     */
    public String getCarModel() {
        return this.carModel;
    }

    /**
     * Set the carModel.
     *
     * @param carModel carModel
     *
     * @author liu_yinchuan
     */
    public void setCarModel(String carModel) {
        this.carModel = carModel;
        
    }

    /**
     * Get the impOfficeCode.
     *
     * @return impOfficeCode
     *
     * @author liu_yinchuan
     */
    public String getImpOfficeCode() {
        return this.impOfficeCode;
    }

    /**
     * Set the impOfficeCode.
     *
     * @param impOfficeCode impOfficeCode
     *
     * @author liu_yinchuan
     */
    public void setImpOfficeCode(String impOfficeCode) {
        this.impOfficeCode = impOfficeCode;
        
    }

    /**
     * Get the digit.
     *
     * @return digit
     *
     * @author liu_yinchuan
     */
    public Integer getDigit() {
        return this.digit;
    }

    /**
     * Set the digit.
     *
     * @param digit digit
     *
     * @author liu_yinchuan
     */
    public void setDigit(Integer digit) {
        this.digit = digit;
        
    }

    /**
     * Get the onShippingList.
     *
     * @return onShippingList
     *
     * @author liu_yinchuan
     */
    public List<TntRdAttachShipping> getOnShippingList() {
        return this.onShippingList;
    }

    /**
     * Set the onShippingList.
     *
     * @param onShippingList onShippingList
     *
     * @author liu_yinchuan
     */
    public void setOnShippingList(List<TntRdAttachShipping> onShippingList) {
        this.onShippingList = onShippingList;
        
    }

    /**
     * Get the hasMoreSupplier.
     *
     * @return hasMoreSupplier
     *
     * @author liu_yinchuan
     */
    public boolean isHasMoreSupplier() {
        return this.hasMoreSupplier;
    }

    /**
     * Set the hasMoreSupplier.
     *
     * @param hasMoreSupplier hasMoreSupplier
     *
     * @author liu_yinchuan
     */
    public void setHasMoreSupplier(boolean hasMoreSupplier) {
        this.hasMoreSupplier = hasMoreSupplier;
        
    }

    /**
     * Get the partsRemark.
     *
     * @return partsRemark
     *
     * @author liu_yinchuan
     */
    public String getPartsRemark() {
        return this.partsRemark;
    }

    /**
     * Set the partsRemark.
     *
     * @param partsRemark partsRemark
     *
     * @author liu_yinchuan
     */
    public void setPartsRemark(String partsRemark) {
        this.partsRemark = partsRemark;
        
    }

    /**
     * Get the rundownRemark.
     *
     * @return rundownRemark
     *
     * @author liu_yinchuan
     */
    public String getRundownRemark() {
        return this.rundownRemark;
    }

    /**
     * Set the rundownRemark.
     *
     * @param rundownRemark rundownRemark
     *
     * @author liu_yinchuan
     */
    public void setRundownRemark(String rundownRemark) {
        this.rundownRemark = rundownRemark;
        
    }

    /**
     * Get the buildOutFlag.
     *
     * @return buildOutFlag
     *
     * @author liu_yinchuan
     */
    public String getBuildOutFlag() {
        return this.buildOutFlag;
    }

    /**
     * Set the buildOutFlag.
     *
     * @param buildOutFlag buildOutFlag
     *
     * @author liu_yinchuan
     */
    public void setBuildOutFlag(String buildOutFlag) {
        this.buildOutFlag = buildOutFlag;
        
    }

    /**
     * Get the buildOutMonth.
     *
     * @return buildOutMonth
     *
     * @author liu_yinchuan
     */
    public String getBuildOutMonth() {
        return this.buildOutMonth;
    }

    /**
     * Set the buildOutMonth.
     *
     * @param buildOutMonth buildOutMonth
     *
     * @author liu_yinchuan
     */
    public void setBuildOutMonth(String buildOutMonth) {
        this.buildOutMonth = buildOutMonth;
        
    }

    /**
     * Get the lastOrderMonth.
     *
     * @return lastOrderMonth
     *
     * @author liu_yinchuan
     */
    public String getLastOrderMonth() {
        return this.lastOrderMonth;
    }

    /**
     * Set the lastOrderMonth.
     *
     * @param lastOrderMonth lastOrderMonth
     *
     * @author liu_yinchuan
     */
    public void setLastOrderMonth(String lastOrderMonth) {
        this.lastOrderMonth = lastOrderMonth;
        
    }

    /**
     * Get the lastDeliveryMonth.
     *
     * @return lastDeliveryMonth
     *
     * @author liu_yinchuan
     */
    public String getLastDeliveryMonth() {
        return this.lastDeliveryMonth;
    }

    /**
     * Set the lastDeliveryMonth.
     *
     * @param lastDeliveryMonth lastDeliveryMonth
     *
     * @author liu_yinchuan
     */
    public void setLastDeliveryMonth(String lastDeliveryMonth) {
        this.lastDeliveryMonth = lastDeliveryMonth;
        
    }

    /**
     * Get the inActiveFlag.
     *
     * @return inActiveFlag
     *
     * @author liu_yinchuan
     */
    public String getInActiveFlag() {
        return this.inActiveFlag;
    }

    /**
     * Set the inActiveFlag.
     *
     * @param inActiveFlag inActiveFlag
     *
     * @author liu_yinchuan
     */
    public void setInActiveFlag(String inActiveFlag) {
        this.inActiveFlag = inActiveFlag;
        
    }

    /**
     * Get the discontinueIndicator.
     *
     * @return discontinueIndicator
     *
     * @author liu_yinchuan
     */
    public Integer getDiscontinueIndicator() {
        return this.discontinueIndicator;
    }

    /**
     * Set the discontinueIndicator.
     *
     * @param discontinueIndicator discontinueIndicator
     *
     * @author liu_yinchuan
     */
    public void setDiscontinueIndicator(Integer discontinueIndicator) {
        this.discontinueIndicator = discontinueIndicator;
        
    }

    /**
     * Get the expBalanceQty.
     *
     * @return expBalanceQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getExpBalanceQty() {
        return this.expBalanceQty;
    }

    /**
     * Set the expBalanceQty.
     *
     * @param expBalanceQty expBalanceQty
     *
     * @author liu_yinchuan
     */
    public void setExpBalanceQty(BigDecimal expBalanceQty) {
        this.expBalanceQty = expBalanceQty;
        
    }

    /**
     * Get the totalImportStockQty.
     *
     * @return totalImportStockQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getTotalImportStockQty() {
        return this.totalImportStockQty;
    }

    /**
     * Set the totalImportStockQty.
     *
     * @param totalImportStockQty totalImportStockQty
     *
     * @author liu_yinchuan
     */
    public void setTotalImportStockQty(BigDecimal totalImportStockQty) {
        this.totalImportStockQty = totalImportStockQty;
        
    }

    /**
     * Get the totalSupplyChainQty.
     *
     * @return totalSupplyChainQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getTotalSupplyChainQty() {
        return this.totalSupplyChainQty;
    }

    /**
     * Set the totalSupplyChainQty.
     *
     * @param totalSupplyChainQty totalSupplyChainQty
     *
     * @author liu_yinchuan
     */
    public void setTotalSupplyChainQty(BigDecimal totalSupplyChainQty) {
        this.totalSupplyChainQty = totalSupplyChainQty;
        
    }

    /**
     * Get the totalNotInRdQty.
     *
     * @return totalNotInRdQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getTotalNotInRdQty() {
        return this.totalNotInRdQty;
    }

    /**
     * Set the totalNotInRdQty.
     *
     * @param totalNotInRdQty totalNotInRdQty
     *
     * @author liu_yinchuan
     */
    public void setTotalNotInRdQty(BigDecimal totalNotInRdQty) {
        this.totalNotInRdQty = totalNotInRdQty;
        
    }

    /**
     * Get the buildOutFlagOrg.
     *
     * @return buildOutFlagOrg
     *
     * @author liu_yinchuan
     */
    public Integer getBuildOutFlagOrg() {
        return this.buildOutFlagOrg;
    }

    /**
     * Set the buildOutFlagOrg.
     *
     * @param buildOutFlagOrg buildOutFlagOrg
     *
     * @author liu_yinchuan
     */
    public void setBuildOutFlagOrg(Integer buildOutFlagOrg) {
        this.buildOutFlagOrg = buildOutFlagOrg;
        
    }

    /**
     * Get the inActiveFlagOrg.
     *
     * @return inActiveFlagOrg
     *
     * @author liu_yinchuan
     */
    public Integer getInActiveFlagOrg() {
        return this.inActiveFlagOrg;
    }

    /**
     * Set the inActiveFlagOrg.
     *
     * @param inActiveFlagOrg inActiveFlagOrg
     *
     * @author liu_yinchuan
     */
    public void setInActiveFlagOrg(Integer inActiveFlagOrg) {
        this.inActiveFlagOrg = inActiveFlagOrg;
        
    }

    /**
     * Get the uomCode.
     *
     * @return uomCode
     *
     * @author liu_yinchuan
     */
    public String getUomCode() {
        return this.uomCode;
    }

    /**
     * Set the uomCode.
     *
     * @param uomCode uomCode
     *
     * @author liu_yinchuan
     */
    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
        
    }

    /**
     * Get the endPfcDate.
     *
     * @return endPfcDate
     *
     * @author liu_yinchuan
     */
    public Date getEndPfcDate() {
        return this.endPfcDate;
    }

    /**
     * Set the endPfcDate.
     *
     * @param endPfcDate endPfcDate
     *
     * @author liu_yinchuan
     */
    public void setEndPfcDate(Date endPfcDate) {
        this.endPfcDate = endPfcDate;
        
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
     * Get the endDate3Comp.
     *
     * @return endDate3Comp
     *
     * @author liu_yinchuan
     */
    public Date getEndDate3Comp() {
        return this.endDate3Comp;
    }

    /**
     * Set the endDate3Comp.
     *
     * @param endDate3Comp endDate3Comp
     *
     * @author liu_yinchuan
     */
    public void setEndDate3Comp(Date endDate3Comp) {
        this.endDate3Comp = endDate3Comp;
        
    }

    /**
     * Get the endDateTmComp.
     *
     * @return endDateTmComp
     *
     * @author liu_yinchuan
     */
    public Date getEndDateTmComp() {
        return this.endDateTmComp;
    }

    /**
     * Set the endDateTmComp.
     *
     * @param endDateTmComp endDateTmComp
     *
     * @author liu_yinchuan
     */
    public void setEndDateTmComp(Date endDateTmComp) {
        this.endDateTmComp = endDateTmComp;
        
    }

    /**
     * Get the simulationEndDatePattern.
     *
     * @return simulationEndDatePattern
     *
     * @author liu_yinchuan
     */
    public Integer getSimulationEndDatePattern() {
        return this.simulationEndDatePattern;
    }

    /**
     * Set the simulationEndDatePattern.
     *
     * @param simulationEndDatePattern simulationEndDatePattern
     *
     * @author liu_yinchuan
     */
    public void setSimulationEndDatePattern(Integer simulationEndDatePattern) {
        this.simulationEndDatePattern = simulationEndDatePattern;
        
    }

}
