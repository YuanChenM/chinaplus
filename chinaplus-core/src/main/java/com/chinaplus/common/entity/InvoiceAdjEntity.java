package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.common.bean.SupplyChainEntity;

public class InvoiceAdjEntity extends SupplyChainEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** orderId */
    private Integer orderId;

    /** qty */
    private BigDecimal qty;

    /** impPoNo */
    private String impPoNo;

    /** customerOrderNo */
    private String customerOrderNo;

    /** expPoNo */
    private String expPoNo;

    /** orderStatusId */
    private Integer orderStatusId;
    
    /** expInbPlanDate */
    private Date expInbPlanDate;
    
    /** ssId */
    private Integer ssId;
    
    /** ssPartsId */
    private Integer ssPartsId;
    
    /** ssPlanId */
    private Integer ssPlanId;
    
    /** completedFlag */
    private Integer completedFlag;
    
    /** delayAdjustmentPattern */
    private Integer delayAdjustmentPattern;
    
    /** originalVersion */
    private Integer originalVersion;
    
    /** revisionVersion */
    private Integer revisionVersion;
    
    /** nirdFlag */
    private Integer nirdFlag;
    
    /** forceCompletedQty */
    private BigDecimal forceCompletedQty;

    /** invoiceNo */
    private String invoiceNo;

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the qty
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return the impPoNo
     */
    public String getImpPoNo() {
        return impPoNo;
    }

    /**
     * @param impPoNo the impPoNo to set
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    /**
     * @return the customerOrderNo
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * @param customerOrderNo the customerOrderNo to set
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * @return the expPoNo
     */
    public String getExpPoNo() {
        return expPoNo;
    }

    /**
     * @param expPoNo the expPoNo to set
     */
    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    /**
     * @return the orderStatusId
     */
    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    /**
     * @param orderStatusId the orderStatusId to set
     */
    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    /**
     * @return the expInbPlanDate
     */
    public Date getExpInbPlanDate() {
        return expInbPlanDate;
    }

    /**
     * @param expInbPlanDate the expInbPlanDate to set
     */
    public void setExpInbPlanDate(Date expInbPlanDate) {
        this.expInbPlanDate = expInbPlanDate;
    }

    /**
     * @return the ssId
     */
    public Integer getSsId() {
        return ssId;
    }

    /**
     * @param ssId the ssId to set
     */
    public void setSsId(Integer ssId) {
        this.ssId = ssId;
    }

    /**
     * @return the ssPartsId
     */
    public Integer getSsPartsId() {
        return ssPartsId;
    }

    /**
     * @param ssPartsId the ssPartsId to set
     */
    public void setSsPartsId(Integer ssPartsId) {
        this.ssPartsId = ssPartsId;
    }

    /**
     * @return the ssPlanId
     */
    public Integer getSsPlanId() {
        return ssPlanId;
    }

    /**
     * @param ssPlanId the ssPlanId to set
     */
    public void setSsPlanId(Integer ssPlanId) {
        this.ssPlanId = ssPlanId;
    }

    /**
     * @return the completedFlag
     */
    public Integer getCompletedFlag() {
        return completedFlag;
    }

    /**
     * @param completedFlag the completedFlag to set
     */
    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * @return the delayAdjustmentPattern
     */
    public Integer getDelayAdjustmentPattern() {
        return delayAdjustmentPattern;
    }

    /**
     * @param delayAdjustmentPattern the delayAdjustmentPattern to set
     */
    public void setDelayAdjustmentPattern(Integer delayAdjustmentPattern) {
        this.delayAdjustmentPattern = delayAdjustmentPattern;
    }

    /**
     * @return the originalVersion
     */
    public Integer getOriginalVersion() {
        return originalVersion;
    }

    /**
     * @param originalVersion the originalVersion to set
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * @return the revisionVersion
     */
    public Integer getRevisionVersion() {
        return revisionVersion;
    }

    /**
     * @param revisionVersion the revisionVersion to set
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * @return the nirdFlag
     */
    public Integer getNirdFlag() {
        return nirdFlag;
    }

    /**
     * @param nirdFlag the nirdFlag to set
     */
    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
    }

    /**
     * @return the forceCompletedQty
     */
    public BigDecimal getForceCompletedQty() {
        return forceCompletedQty;
    }

    /**
     * @param forceCompletedQty the forceCompletedQty to set
     */
    public void setForceCompletedQty(BigDecimal forceCompletedQty) {
        this.forceCompletedQty = forceCompletedQty;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
}
