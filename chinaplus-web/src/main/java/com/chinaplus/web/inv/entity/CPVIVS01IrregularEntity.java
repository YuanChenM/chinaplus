/**
 * CPVIVS01IrregularEntity.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.util.Date;

import com.chinaplus.common.bean.SupplyChainEntity;

/**
 * Invoice Screen Irregular Entity.
 */
public class CPVIVS01IrregularEntity extends SupplyChainEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Upload ID */
    private String uploadId;

    /** Vessel Name */
    private String vesselName;

    /** Vessel ETD */
    private Date vesselEtd;

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
     * Get the vesselEtd.
     *
     * @return vesselEtd
     */
    public Date getVesselEtd() {
        return this.vesselEtd;
    }

    /**
     * Set the vesselEtd.
     *
     * @param vesselEtd vesselEtd
     */
    public void setVesselEtd(Date vesselEtd) {
        this.vesselEtd = vesselEtd;
    }

}
