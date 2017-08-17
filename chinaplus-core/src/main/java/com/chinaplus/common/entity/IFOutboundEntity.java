/**
 * Outbound.java
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
 * Outbound.
 * 
 * @author yang_jia1
 */
public class IFOutboundEntity extends BaseInterfaceEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** ifOutboundId */
    private Integer ifOutboundId;

    /** representativeRoot */
    private String representativeRoot;

    /** impPoNo */
    private String impPoNo;

    /** partsNo */
    private String partsNo;

    /** ipNo */
    private String ipNo;

    /** outboundQty */
    private String outboundQty;

    /** containerNo */
    private String containerNo;

    /** moduleNo */
    private String moduleNo;

    /** expInboundPlanDate */
    private String expInboundPlanDate;

    /** expOutboundActualDate */
    private String expOutboundActualDate;

    /** expInPlanDate */
    private Date expInPlanDate;

    /** expOutActDate */
    private Date expOutActDate;

    /** transportationMode */
    private String transportationMode;

    /** cancelFlag */
    private String cancelFlag;

    /** businessPattern */
    private Integer businessPattern;

    /** customerOrderNo */
    private String customerOrderNo;

    /** expPoNo */
    private String expPoNo;

    /** expPoItemNo */
    private String expPoItemNo;

    /** qty */
    private BigDecimal qty;

    /** dtNo */
    private String dtNo;

    /** status */
    private Integer status;

    /** totalQty */
    private BigDecimal totalQty;

    /**
     * @return the ifOutboundId
     */
    public Integer getIfOutboundId() {
        return ifOutboundId;
    }

    /**
     * @param ifOutboundId the ifOutboundId to set
     */
    public void setIfOutboundId(Integer ifOutboundId) {
        this.ifOutboundId = ifOutboundId;
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
     * @return the ipNo
     */
    public String getIpNo() {
        return ipNo;
    }

    /**
     * @param ipNo the ipNo to set
     */
    public void setIpNo(String ipNo) {
        this.ipNo = ipNo;
    }

    /**
     * @return the outboundQty
     */
    public String getOutboundQty() {
        return outboundQty;
    }

    /**
     * @param outboundQty the outboundQty to set
     */
    public void setOutboundQty(String outboundQty) {
        this.outboundQty = outboundQty;
    }

    /**
     * @return the containerNo
     */
    public String getContainerNo() {
        return containerNo;
    }

    /**
     * @param containerNo the containerNo to set
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * @return the moduleNo
     */
    public String getModuleNo() {
        return moduleNo;
    }

    /**
     * @param moduleNo the moduleNo to set
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
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
     * @return the expOutboundActualDate
     */
    public String getExpOutboundActualDate() {
        return expOutboundActualDate;
    }

    /**
     * @param expOutboundActualDate the expOutboundActualDate to set
     */
    public void setExpOutboundActualDate(String expOutboundActualDate) {
        this.expOutboundActualDate = expOutboundActualDate;
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
     * @return the expOutActDate
     */
    public Date getExpOutActDate() {
        return expOutActDate;
    }

    /**
     * @param expOutActDate the expOutActDate to set
     */
    public void setExpOutActDate(Date expOutActDate) {
        this.expOutActDate = expOutActDate;
    }

    /**
     * @return the transportationMode
     */
    public String getTransportationMode() {
        return transportationMode;
    }

    /**
     * @param transportationMode the transportationMode to set
     */
    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
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
     * @return the businessPattern
     */
    public Integer getBusinessPattern() {
        return businessPattern;
    }

    /**
     * @param businessPattern the businessPattern to set
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
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
     * @return the expPoItemNo
     */
    public String getExpPoItemNo() {
        return expPoItemNo;
    }

    /**
     * @param expPoItemNo the expPoItemNo to set
     */
    public void setExpPoItemNo(String expPoItemNo) {
        this.expPoItemNo = expPoItemNo;
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

    /**
     * @return the totalQty
     */
    public BigDecimal getTotalQty() {
        return totalQty;
    }

    /**
     * @param totalQty the totalQty to set
     */
    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN, IntDef.INT_EIGHTEEN,
            IntDef.INT_TEN, IntDef.INT_TEN, IntDef.INT_TWENTY, IntDef.INT_TWENTY, IntDef.INT_EIGHT, IntDef.INT_EIGHT,
            IntDef.INT_ONE, IntDef.INT_ONE };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "representativeRoot", "impPoNo", "partsNo", "ipNo", "outboundQty",
            "containerNo", "moduleNo" , "expInboundPlanDate" , "expOutboundActualDate", "transportationMode", "cancelFlag" };
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_ONE_HUNDRED_AND_ELEVEN;
    }

}
