/**
 * @screen CPIIFS03
 * @author zhang_chi
 */
package com.chinaplus.web.inf.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPIIFS03Entity.
 */
public class CPIIFS03Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 7048576297489016165L;

    /** invoiceSummaryId */
    private Integer  invoiceSummaryId;
    
    /** invoiceNo */
    private String invoiceNo;

    /** etd */
    private Date etd;
    
    /** eta */
    private Date eta;

    /** transportMode */
    private String transportMode;
    
    /** vesselName */
    private String vesselName;
    
    /** blno */
    private String blno;

    /** bldate */
    private Date bldate;

    /** expRegion */
    private String expRegion;
    
    /** impRegion */
    private String impRegion;
    
    /** uploadedBy */
    private String uploadedBy;

    /** uploadedDate */
    private Date uploadedDate;
    
    /** invoice match status */
    private Integer status;

    /**
     * Get the invoiceSummaryId.
     *
     * @return invoiceSummaryId
     */
    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    /**
     * Set the invoiceSummaryId.
     *
     * @param invoiceSummaryId invoiceSummaryId
     */
    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

    /**
     * Get the invoiceNo.
     *
     * @return invoiceNo
     */
    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    /**
     * Set the invoiceNo.
     *
     * @param invoiceNo invoiceNo
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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
     * Get the transportMode.
     *
     * @return transportMode
     */
    public String getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
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
     * Get the blno.
     *
     * @return blno
     */
    public String getBlno() {
        return this.blno;
    }

    /**
     * Set the blno.
     *
     * @param blno blno
     */
    public void setBlno(String blno) {
        this.blno = blno;
    }

    /**
     * Get the bldate.
     *
     * @return bldate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getBldate() {
        return this.bldate;
    }

    /**
     * Set the bldate.
     *
     * @param bldate bldate
     */
    public void setBldate(Date bldate) {
        this.bldate = bldate;
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
     * Get the uploadedBy.
     *
     * @return uploadedBy
     */
    public String getUploadedBy() {
        return this.uploadedBy;
    }

    /**
     * Set the uploadedBy.
     *
     * @param uploadedBy uploadedBy
     */
    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * Get the uploadedDate.
     *
     * @return uploadedDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getUploadedDate() {
        return this.uploadedDate;
    }

    /**
     * Set the uploadedDate.
     *
     * @param uploadedDate uploadedDate
     */
    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
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
    
}
