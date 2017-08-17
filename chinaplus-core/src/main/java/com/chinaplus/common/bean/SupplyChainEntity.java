package com.chinaplus.common.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the Supply Chain interface.
 * 
 */
public class SupplyChainEntity extends com.chinaplus.core.base.BaseEntity implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1454922036369776761L;

    /** chainStartDate */
    private Date chainStartDate;

    /** Deliver Date to Obu */
    private Date deliverToObuDate;

    /** vanningDate */
    private Date vanningDate;

    /** etd */
    private Date etd;

    /** eta */
    private Date eta;

    /** impPlanCustomDate */
    private Date impPlanCustomDate;

    /** impPlanInboundDate */
    private Date impPlanInboundDate;

    /** Exp Parts Id */
    private Integer expPartsId;

    /** Parts Id */
    private Integer partsId;

    /** Supplier Id */
    private Integer supplierId;

    /** Office Id */
    private Integer officeId;

    /** Customer Id */
    private Integer customerId;

    /** Warehouse Id */
    private Integer impWhsId;

    /** Warehouse Id */
    private String impWhsCode;

    /** Transport Mode */
    private Integer tansportMode;

    /** Imp Stock Flag */
    private Integer impStockFlag;

    /** Shipping Route Code */
    private String shippingRouteCode;

    /** Working Lead Time from Export Inbound date to ETD for Air Shipment */
    private Integer airEtdLt;

    /** Lead Time from ETD to ETA for Air Shipment */
    private Integer airEtaLt;

    /** Working Lead Time from ETA to Import Inbound for Air Shipment */
    private Integer airInboundLt;

    /** Lead Time from ETD to ETA for Irregular Sea Shipment */
    private Integer seaEtaLt;

    /** Working Lead Time from ETA to Import Inbound for Irregular Sea Shipment */
    private Integer seaInboundLt;

    /** Chain Step */
    private Integer chainStep;

    /** Business Pattern */
    private Integer businessPattern;

    /** Is effective */
    private boolean isEffective;

    /**
     * Get the chainStartDate.
     *
     * @return chainStartDate
     */
    public Date getChainStartDate() {
        return this.chainStartDate;
    }

    /**
     * Set the chainStartDate.
     *
     * @param chainStartDate chainStartDate
     */
    public void setChainStartDate(Date chainStartDate) {
        this.chainStartDate = chainStartDate;

    }

    /**
     * Get the vanningDate.
     *
     * @return vanningDate
     */
    public Date getVanningDate() {
        return this.vanningDate;
    }

    /**
     * Set the vanningDate.
     *
     * @param vanningDate vanningDate
     */
    public void setVanningDate(Date vanningDate) {
        this.vanningDate = vanningDate;

    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    public Date getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Date etd) {
        this.etd = etd;

    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    public Date getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Date eta) {
        this.eta = eta;

    }

    /**
     * Get the impPlanCustomDate.
     *
     * @return impPlanCustomDate
     */
    public Date getImpPlanCustomDate() {
        return this.impPlanCustomDate;
    }

    /**
     * Set the impPlanCustomDate.
     *
     * @param impPlanCustomDate impPlanCustomDate
     */
    public void setImpPlanCustomDate(Date impPlanCustomDate) {
        this.impPlanCustomDate = impPlanCustomDate;

    }

    /**
     * Get the impPlanInboundDate.
     *
     * @return impPlanInboundDate
     */
    public Date getImpPlanInboundDate() {
        return this.impPlanInboundDate;
    }

    /**
     * Set the impPlanInboundDate.
     *
     * @param impPlanInboundDate impPlanInboundDate
     */
    public void setImpPlanInboundDate(Date impPlanInboundDate) {
        this.impPlanInboundDate = impPlanInboundDate;

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
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;

    }

    /**
     * Get the impWhsId.
     *
     * @return impWhsId
     */
    public Integer getImpWhsId() {
        return this.impWhsId;
    }

    /**
     * Set the impWhsId.
     *
     * @param impWhsId impWhsId
     */
    public void setImpWhsId(Integer impWhsId) {
        this.impWhsId = impWhsId;

    }

    /**
     * Get the tansportMode.
     *
     * @return tansportMode
     */
    public Integer getTansportMode() {
        return this.tansportMode;
    }

    /**
     * Set the tansportMode.
     *
     * @param tansportMode tansportMode
     */
    public void setTansportMode(Integer tansportMode) {
        this.tansportMode = tansportMode;

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
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;

    }

    /**
     * Get the airEtdLt.
     *
     * @return airEtdLt
     */
    public Integer getAirEtdLt() {
        return this.airEtdLt;
    }

    /**
     * Set the airEtdLt.
     *
     * @param airEtdLt airEtdLt
     */
    public void setAirEtdLt(Integer airEtdLt) {
        this.airEtdLt = airEtdLt;

    }

    /**
     * Get the airEtaLt.
     *
     * @return airEtaLt
     */
    public Integer getAirEtaLt() {
        return this.airEtaLt;
    }

    /**
     * Set the airEtaLt.
     *
     * @param airEtaLt airEtaLt
     */
    public void setAirEtaLt(Integer airEtaLt) {
        this.airEtaLt = airEtaLt;

    }

    /**
     * Get the airInboundLt.
     *
     * @return airInboundLt
     */
    public Integer getAirInboundLt() {
        return this.airInboundLt;
    }

    /**
     * Set the airInboundLt.
     *
     * @param airInboundLt airInboundLt
     */
    public void setAirInboundLt(Integer airInboundLt) {
        this.airInboundLt = airInboundLt;

    }

    /**
     * Get the seaEtaLt.
     *
     * @return seaEtaLt
     */
    public Integer getSeaEtaLt() {
        return this.seaEtaLt;
    }

    /**
     * Set the seaEtaLt.
     *
     * @param seaEtaLt seaEtaLt
     */
    public void setSeaEtaLt(Integer seaEtaLt) {
        this.seaEtaLt = seaEtaLt;

    }

    /**
     * Get the seaInboundLt.
     *
     * @return seaInboundLt
     */
    public Integer getSeaInboundLt() {
        return this.seaInboundLt;
    }

    /**
     * Set the seaInboundLt.
     *
     * @param seaInboundLt seaInboundLt
     */
    public void setSeaInboundLt(Integer seaInboundLt) {
        this.seaInboundLt = seaInboundLt;

    }

    /**
     * Get the chainStep.
     *
     * @return chainStep
     */
    public Integer getChainStep() {
        return this.chainStep;
    }

    /**
     * Set the chainStep.
     *
     * @param chainStep chainStep
     */
    public void setChainStep(Integer chainStep) {
        this.chainStep = chainStep;

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
     * Get the deliverToObuDate.
     *
     * @return deliverToObuDate
     */
    public Date getDeliverToObuDate() {
        return this.deliverToObuDate;
    }

    /**
     * Set the deliverToObuDate.
     *
     * @param deliverToObuDate deliverToObuDate
     */
    public void setDeliverToObuDate(Date deliverToObuDate) {
        this.deliverToObuDate = deliverToObuDate;

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
     * Get the partsId.
     *
     * @return partsId
     *
     * @author liu_yinchuan
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     *
     * @author liu_yinchuan
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;

    }

    /**
     * Get the isEffective.
     *
     * @return isEffective
     *
     * @author liu_yinchuan
     */
    public boolean isEffective() {
        return this.isEffective;
    }

    /**
     * Set the isEffective.
     *
     * @param isEffective isEffective
     *
     * @author liu_yinchuan
     */
    public void setEffective(boolean isEffective) {
        this.isEffective = isEffective;

    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     *
     * @author liu_yinchuan
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     *
     * @author liu_yinchuan
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
        
    }

    /**
     * Get the impWhsCode.
     *
     * @return impWhsCode
     *
     * @author liu_yinchuan
     */
    public String getImpWhsCode() {
        return this.impWhsCode;
    }

    /**
     * Set the impWhsCode.
     *
     * @param impWhsCode impWhsCode
     *
     * @author liu_yinchuan
     */
    public void setImpWhsCode(String impWhsCode) {
        this.impWhsCode = impWhsCode;
        
    }

}