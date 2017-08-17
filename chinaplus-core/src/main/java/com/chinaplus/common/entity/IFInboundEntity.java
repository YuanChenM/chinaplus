/**
 * Inbound.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Inbound.
 * 
 * @author yang_jia1
 */
public class IFInboundEntity extends BaseInterfaceEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ifInboundId */
    private Integer ifInboundId;

    /** representativeRoot */
    private String representativeRoot;

    /** ssmsCustomerCode */
    private String ssmsCustomerCode;

    /** impPoNo */
    private String impPoNo;

    /** expPoNo */
    private String expPoNo;

    /** expPoItemCode */
    private String expPoItemCode;

    /** partsNo */
    private String partsNo;

    /** expInboundPlanDate */
    private String expInboundPlanDate;

    /** expInboundActualDate */
    private String expInboundActualDate;

    /** expInPlanDate */
    private Date expInPlanDate;

    /** expInActDate */
    private Date expInActDate;

    /** qty */
    private BigDecimal qty;

    /** dtNo */
    private String dtNo;

    /** cancelFlag */
    private String cancelFlag;

    /** customerOrderNo */
    private String customerOrderNo;

    /** customerId */
    private Integer customerId;

    /** status */
    private Integer status;

    /**
     * @return the ifInboundId
     */
    public Integer getIfInboundId() {
        return ifInboundId;
    }

    /**
     * @param ifInboundId the ifInboundId to set
     */
    public void setIfInboundId(Integer ifInboundId) {
        this.ifInboundId = ifInboundId;
    }

    /**
     * @return the representativeRoot
     */
    public String getRepresentativeRoot() {
        return representativeRoot;
    }

    /**
     * @param representativeRoot the representativeRoot to set
     */
    public void setRepresentativeRoot(String representativeRoot) {
        this.representativeRoot = representativeRoot;
    }

    /**
     * @return the ssmsCustomerCode
     */
    public String getSsmsCustomerCode() {
        return ssmsCustomerCode;
    }

    /**
     * @param ssmsCustomerCode the ssmsCustomerCode to set
     */
    public void setSsmsCustomerCode(String ssmsCustomerCode) {
        this.ssmsCustomerCode = ssmsCustomerCode;
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
     * @return the expPoItemCode
     */
    public String getExpPoItemCode() {
        return expPoItemCode;
    }

    /**
     * @param expPoItemCode the expPoItemCode to set
     */
    public void setExpPoItemCode(String expPoItemCode) {
        this.expPoItemCode = expPoItemCode;
    }

    /**
     * @return the partsNo
     */
    public String getPartsNo() {
        return partsNo;
    }

    /**
     * @param partsNo the partsNo to set
     */
    public void setPartsNo(String partsNo) {
        this.partsNo = partsNo;
    }

    /**
     * @return the expInboundPlanDate
     */
    public String getExpInboundPlanDate() {
        return expInboundPlanDate;
    }

    /**
     * @param expInboundPlanDate the expInboundPlanDate to set
     */
    public void setExpInboundPlanDate(String expInboundPlanDate) {
        this.expInboundPlanDate = expInboundPlanDate;
    }

    /**
     * @return the expInboundActualDate
     */
    public String getExpInboundActualDate() {
        return expInboundActualDate;
    }

    /**
     * @param expInboundActualDate the expInboundActualDate to set
     */
    public void setExpInboundActualDate(String expInboundActualDate) {
        this.expInboundActualDate = expInboundActualDate;
    }

    /**
     * @return the expInPlanDate
     */
    public Date getExpInPlanDate() {
        return expInPlanDate;
    }

    /**
     * @param expInPlanDate the expInPlanDate to set
     */
    public void setExpInPlanDate(Date expInPlanDate) {
        this.expInPlanDate = expInPlanDate;
    }

    /**
     * @return the expInActDate
     */
    public Date getExpInActDate() {
        return expInActDate;
    }

    /**
     * @param expInActDate the expInActDate to set
     */
    public void setExpInActDate(Date expInActDate) {
        this.expInActDate = expInActDate;
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
     * @return the dtNo
     */
    public String getDtNo() {
        return dtNo;
    }

    /**
     * @param dtNo the dtNo to set
     */
    public void setDtNo(String dtNo) {
        this.dtNo = dtNo;
    }

    /**
     * @return the cancelFlag
     */
    public String getCancelFlag() {
        return cancelFlag;
    }

    /**
     * @param cancelFlag the cancelFlag to set
     */
    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
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
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN, IntDef.INT_TEN,
            IntDef.INT_TEN, IntDef.INT_FIVE, IntDef.INT_EIGHTEEN, IntDef.INT_EIGHT, IntDef.INT_EIGHT, IntDef.INT_TEN,
            IntDef.INT_SIX, IntDef.INT_ONE };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "representativeRoot", "ssmsCustomerCode", "impPoNo", "expPoNo",
            "expPoItemCode", "partsNo", "expInboundPlanDate", "expInboundActualDate", "qty", "dtNo", "cancelFlag" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_NINETYONE;
    }
}
