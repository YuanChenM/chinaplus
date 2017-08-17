/**
 * CPVIVS04Entity.java
 * 
 * @screen CPVIVS04
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Irregular Shipping Schedule Entity.
 */
public class CPVIVS04Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload ID */
    private String uploadId;

    /** Vessel Name */
    private String vesselName;

    /** ETD */
    private Date etd;

    /** Display ETD */
    private String disEtd;

    /** ETA */
    private String eta;

    /** Inbound Plan Date */
    private String planInbound;

    /** EXP Parts ID */
    private Integer expPartsId;

    /** Shipping Route */
    private String shippingRoute;

    /**
     * Get the uploadId.
     *
     * @return uploadId
     */
    public String getUploadId() {
        return this.uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * Get the vesselName.
     *
     * @return vesselName
     */
    public String getVesselName() {
        return this.vesselName;
    }

    /**
     * Set the vesselName.
     *
     * @param vesselName vesselName
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
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
     * Get the disEtd.
     *
     * @return disEtd
     */
    public String getDisEtd() {
        return this.disEtd;
    }

    /**
     * Set the disEtd.
     *
     * @param disEtd disEtd
     */
    public void setDisEtd(String disEtd) {
        this.disEtd = disEtd;
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
    public String getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * Get the planInbound.
     *
     * @return planInbound
     */
    public String getPlanInbound() {
        return this.planInbound;
    }

    /**
     * Set the planInbound.
     *
     * @param planInbound planInbound
     */
    public void setPlanInbound(String planInbound) {
        this.planInbound = planInbound;
    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * Get the shippingRoute.
     *
     * @return shippingRoute
     */
    public String getShippingRoute() {
        return this.shippingRoute;
    }

    /**
     * Set the shippingRoute.
     *
     * @param shippingRoute shippingRoute
     */
    public void setShippingRoute(String shippingRoute) {
        this.shippingRoute = shippingRoute;
    }

}
