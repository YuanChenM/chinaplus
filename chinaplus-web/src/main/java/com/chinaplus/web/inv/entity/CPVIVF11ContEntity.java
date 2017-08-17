/**
 * CPVIVF11ContEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Upload CONT Entity.
 */
public class CPVIVF11ContEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Container No. */
    private String containerNo;

    /** ETD */
    private Date etd;

    /** Vessel Name */
    private String vesselName;

    /** Vanning Date */
    private Date vanningDate;

    /** File Name */
    private String fileName;

    /** Line Number */
    private Integer lineNum;

    /**
     * Get the containerNo.
     *
     * @return containerNo
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
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
     * Get the fileName.
     *
     * @return fileName
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Set the fileName.
     *
     * @param fileName fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the lineNum.
     *
     * @return lineNum
     */
    public Integer getLineNum() {
        return this.lineNum;
    }

    /**
     * Set the lineNum.
     *
     * @param lineNum lineNum
     */
    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

}
