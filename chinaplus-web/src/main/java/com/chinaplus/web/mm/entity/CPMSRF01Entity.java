/**
 * @screen CPMSRF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMSRF01Entity.
 */
public class CPMSRF01Entity extends MMCommonEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** shippingRouteCode  the  shippingRouteCode */
    private String shippingRouteCode;

    /**   impCcLeadtime the  impCcLeadtime */
    private Integer impCcLeadtime;
   
    /**  impInboundLeadtime the  impInboundLeadtime */
    private Integer impInboundLeadtime; 
    
    /** deliveryStart  the deliveryStart  */
    private Date deliveryStart;
    
    /**  deliveryEnd the deliveryEnd  */
    private Date deliveryEnd;
    
    /** packingEnd  the packingEnd  */
    private Date packingEnd;
    
    /** lastVanning  the  lastVanning */
    private Date lastVanning;
    
    /** shippingInstruction  the  shippingInstruction */
    private Date shippingInstruction;
    
    /** docsPreparation  the  docsPreparation */
    private Date docsPreparation;
    
    /** customClearance  the customClearance  */
    private Date customClearance;
    
    /** cyCut  the  cyCut */
    private Date cyCut;
    
    /** portIn  the portIn  */
    private Date portIn;
    
    /** etd  the  etd */
    private Date  etd;
    
    /** eta  the  eta */
    private Date eta;
    
    /** inactiveFlag  the  inactiveFlag */
    private String inactiveFlag;
   

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
     * Get the impCcLeadtime.
     *
     * @return impCcLeadtime
     */
    public Integer getImpCcLeadtime() {
        return this.impCcLeadtime;
    }
    /**
     * Set the impCcLeadtime.
     *
     * @param impCcLeadtime impCcLeadtime
     */
    public void setImpCcLeadtime(Integer impCcLeadtime) {
        this.impCcLeadtime = impCcLeadtime;
    }
    /**
     * Get the impInboundLeadtime.
     *
     * @return impInboundLeadtime
     */
    public Integer getImpInboundLeadtime() {
        return this.impInboundLeadtime;
    }
    /**
     * Set the impInboundLeadtime.
     *
     * @param impInboundLeadtime impInboundLeadtime
     */
    public void setImpInboundLeadtime(Integer impInboundLeadtime) {
        this.impInboundLeadtime = impInboundLeadtime;
    }
    /**
     * Get the deliveryStart.
     *
     * @return deliveryStart
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDeliveryStart() {
        return this.deliveryStart;
    }
    /**
     * Set the deliveryStart.
     *
     * @param deliveryStart deliveryStart
     */
    public void setDeliveryStart(Date deliveryStart) {
        this.deliveryStart = deliveryStart;
    }
    /**
     * Get the deliveryEnd.
     *
     * @return deliveryEnd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDeliveryEnd() {
        return this.deliveryEnd;
    }
    /**
     * Set the deliveryEnd.
     *
     * @param deliveryEnd deliveryEnd
     */
    public void setDeliveryEnd(Date deliveryEnd) {
        this.deliveryEnd = deliveryEnd;
    }
    /**
     * Get the packingEnd.
     *
     * @return packingEnd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getPackingEnd() {
        return this.packingEnd;
    }
    /**
     * Set the packingEnd.
     *
     * @param packingEnd packingEnd
     */
    public void setPackingEnd(Date packingEnd) {
        this.packingEnd = packingEnd;
    }
    /**
     * Get the lastVanning.
     *
     * @return lastVanning
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getLastVanning() {
        return this.lastVanning;
    }
    /**
     * Set the lastVanning.
     *
     * @param lastVanning lastVanning
     */
    public void setLastVanning(Date lastVanning) {
        this.lastVanning = lastVanning;
    }
    /**
     * Get the shippingInstruction.
     *
     * @return shippingInstruction
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getShippingInstruction() {
        return this.shippingInstruction;
    }
    /**
     * Set the shippingInstruction.
     *
     * @param shippingInstruction shippingInstruction
     */
    public void setShippingInstruction(Date shippingInstruction) {
        this.shippingInstruction = shippingInstruction;
    }
    /**
     * Get the docsPreparation.
     *
     * @return docsPreparation
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDocsPreparation() {
        return this.docsPreparation;
    }
    /**
     * Set the docsPreparation.
     *
     * @param docsPreparation docsPreparation
     */
    public void setDocsPreparation(Date docsPreparation) {
        this.docsPreparation = docsPreparation;
    }
    /**
     * Get the customClearance.
     *
     * @return customClearance
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getCustomClearance() {
        return this.customClearance;
    }
    /**
     * Set the customClearance.
     *
     * @param customClearance customClearance
     */
    public void setCustomClearance(Date customClearance) {
        this.customClearance = customClearance;
    }
    /**
     * Get the cyCut.
     *
     * @return cyCut
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getCyCut() {
        return this.cyCut;
    }
    /**
     * Set the cyCut.
     *
     * @param cyCut cyCut
     */
    public void setCyCut(Date cyCut) {
        this.cyCut = cyCut;
    }
    /**
     * Get the portIn.
     *
     * @return portIn
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getPortIn() {
        return this.portIn;
    }
    /**
     * Set the portIn.
     *
     * @param portIn portIn
     */
    public void setPortIn(Date portIn) {
        this.portIn = portIn;
    }
    /**
     * Get the etd.
     *
     * @return etd
     */
    @JsonSerialize(using= JsonDateSerializer.class)
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
    @JsonSerialize(using= JsonDateSerializer.class)
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
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public String getInactiveFlag() {
        return this.inactiveFlag;
    }
    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(String inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

}
