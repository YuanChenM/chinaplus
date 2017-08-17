package com.chinaplus.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the TNT_PFC_SHIPPING database table.
 * 
 */
@Entity(name = "TNT_PFC_SHIPPING")
public class TntPfcShipping extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_PFC_SHIPPING_PFCSHIPPINGID_GENERATOR",
        sequenceName = "SEQ_TNT_PFC_SHIPPING",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_PFC_SHIPPING_PFCSHIPPINGID_GENERATOR")
    @Column(name = "PFC_SHIPPING_ID")
    private Integer pfcShippingId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Temporal(TemporalType.DATE)
    @Column(name = "CC_DATE")
    private Date ccDate;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Temporal(TemporalType.DATE)
    private Date eta;

    @Temporal(TemporalType.DATE)
    private Date etd;

    @Column(name = "FC_MONTH")
    private String fcMonth;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_INB_PLAN_DATE")
    private Date impInbPlanDate;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private BigDecimal qty;

    @Column(name = "TRANSPORT_MODE")
    private Integer transportMode;

    @Temporal(TemporalType.DATE)
    @Column(name = "VANNING_DATE")
    private Date vanningDate;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "VALID_FLAG")
    private Integer validFlag;

    @Column(name = "\"VERSION\"")
    private Integer version;

    // bi-directional many-to-one association to TntPfcDetail
    /*
     * @ManyToOne(fetch=FetchType.LAZY)
     * @JoinColumn(name="PFC_DETAIL_ID")
     * private TntPfcDetail tntPfcDetail;
     */

    @Column(name = "PFC_DETAIL_ID")
    private Integer pfcDetailId;

    public TntPfcShipping() {}

    public Integer getPfcShippingId() {
        return this.pfcShippingId;
    }

    public void setPfcShippingId(Integer pfcShippingId) {
        this.pfcShippingId = pfcShippingId;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }

    public Date getCcDate() {
        return this.ccDate;
    }

    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEta() {
        return this.eta;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEtd() {
        return this.etd;
    }

    public void setEtd(Date etd) {
        this.etd = etd;
    }

    public String getFcMonth() {
        return this.fcMonth;
    }

    public void setFcMonth(String fcMonth) {
        this.fcMonth = fcMonth;
    }

    public Date getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getTransportMode() {
        return this.transportMode;
    }

    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getValidFlag() {
        return this.validFlag;
    }

    public void setValidFlag(Integer validFlag) {
        this.validFlag = validFlag;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /*
     * public TntPfcDetail getTntPfcDetail() {
     * return this.tntPfcDetail;
     * }
     * public void setTntPfcDetail(TntPfcDetail tntPfcDetail) {
     * this.tntPfcDetail = tntPfcDetail;
     * }
     */

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
     * Get the pfcDetailId.
     *
     * @return pfcDetailId
     */
    public Integer getPfcDetailId() {
        return this.pfcDetailId;
    }

    /**
     * Set the pfcDetailId.
     *
     * @param pfcDetailId pfcDetailId
     */
    public void setPfcDetailId(Integer pfcDetailId) {
        this.pfcDetailId = pfcDetailId;
    }

}