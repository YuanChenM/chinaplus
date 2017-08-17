/**
 * CPKKPF12AllActualInvoiceInfoEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

import com.chinaplus.common.util.JsonDateTimeSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12AllActualInfoEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Plan No */
    private String kanbanPlanNo;

    /** Invoice Id */
    private int invoiceId;

    /** Revision Version */
    private int revisionVersion;

    /** Revision Reason */
    private String revisionReason;

    /** Invoice No. */
    private String invoiceNo;

    /** Vanning Date */
    private Timestamp vanningDate;

    /** ETD */
    private Timestamp etd;

    /** ETA */
    private Timestamp eta;

    /** CC Date */
    private Timestamp ccDate;

    /** Imp Inb Plan Date */
    private Timestamp impInbPlanDate;

    /** Imp Inb Actual Date */
    private Timestamp impInbActualDate;

    /** Parts Id */
    private int partsId;

    /** Qty */
    private BigDecimal qty;

    /** Parts Id : Qty */
    private HashMap<Integer, BigDecimal> partsMap;

    /**
     * Get the kanbanPlanNo.
     *
     * @return kanbanPlanNo
     */
    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    /**
     * Set the kanbanPlanNo.
     *
     * @param kanbanPlanNo kanbanPlanNo
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    /**
     * Get the invoiceId.
     *
     * @return invoiceId
     */
    public int getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * Set the invoiceId.
     *
     * @param invoiceId invoiceId
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public int getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(int revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * Get the revisionReason.
     *
     * @return revisionReason
     */
    public String getRevisionReason() {
        return this.revisionReason;
    }

    /**
     * Set the revisionReason.
     *
     * @param revisionReason revisionReason
     */
    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
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
     * Get the vanningDate.
     *
     * @return vanningDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getVanningDate() {
        return this.vanningDate;
    }

    /**
     * Set the vanningDate.
     *
     * @param vanningDate vanningDate
     */
    public void setVanningDate(Timestamp vanningDate) {
        this.vanningDate = vanningDate;
    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Timestamp etd) {
        this.etd = etd;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Timestamp eta) {
        this.eta = eta;
    }

    /**
     * Get the ccDate.
     *
     * @return ccDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getCcDate() {
        return this.ccDate;
    }

    /**
     * Set the ccDate.
     *
     * @param ccDate ccDate
     */
    public void setCcDate(Timestamp ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * Get the impInbPlanDate.
     *
     * @return impInbPlanDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getImpInbPlanDate() {
        return this.impInbPlanDate;
    }

    /**
     * Set the impInbPlanDate.
     *
     * @param impInbPlanDate impInbPlanDate
     */
    public void setImpInbPlanDate(Timestamp impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    /**
     * Get the impInbActualDate.
     *
     * @return impInbActualDate
     */
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    public Timestamp getImpInbActualDate() {
        return this.impInbActualDate;
    }

    /**
     * Set the impInbActualDate.
     *
     * @param impInbActualDate impInbActualDate
     */
    public void setImpInbActualDate(Timestamp impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public int getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(int partsId) {
        this.partsId = partsId;
    }

    /**
     * Get the qty.
     *
     * @return qty
     */
    public BigDecimal getQty() {
        return this.qty;
    }

    /**
     * Set the qty.
     *
     * @param qty qty
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * Get the partsMap.
     *
     * @return partsMap
     */
    public HashMap<Integer, BigDecimal> getPartsMap() {
        return this.partsMap;
    }

    /**
     * Set the partsMap.
     *
     * @param partsMap partsMap
     */
    public void setPartsMap(HashMap<Integer, BigDecimal> partsMap) {
        this.partsMap = partsMap;
    }
}
