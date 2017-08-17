/**
 * CPVIVF11KanbEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.util.JsonMonthSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Invoice Upload KANB Entity.
 */
public class CPVIVF11KanbEntity extends CPVIVF11PartEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** WEST Invoice No. */
    private String invoiceNo;

    /** Pallet No. */
    private Integer palletNo;

    /** Qty */
    private BigDecimal qty;

    /** Input Qty */
    private BigDecimal inputQty;

    /** Issued Date */
    private Date issuedDate;

    /** Vanning Date */
    private Date vanningDate;

    /** File Name */
    private String fileName;

    /** Line Number */
    private Integer lineNum;

    /** Original Part No. */
    private String originalPartNo;

    /** Order Month */
    private String orderMonth;

    /** DisPlay Order Month */
    private String disOrderMonth;

    /** Show Flag */
    private Integer showFlag;

    /** Vessel Name */
    private String vesselName;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Mail Invoice */
    private CPVIVF11MailInvoiceEntity mailInvoice;

    /** Suppliers */
    private List<CPVIVF11SupportEntity> suppliers;

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
     * Get the palletNo.
     *
     * @return palletNo
     */
    public Integer getPalletNo() {
        return this.palletNo;
    }

    /**
     * Set the palletNo.
     *
     * @param palletNo palletNo
     */
    public void setPalletNo(Integer palletNo) {
        this.palletNo = palletNo;
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
     * Get the inputQty.
     *
     * @return inputQty
     */
    public BigDecimal getInputQty() {
        return this.inputQty;
    }

    /**
     * Set the inputQty.
     *
     * @param inputQty inputQty
     */
    public void setInputQty(BigDecimal inputQty) {
        this.inputQty = inputQty;
    }

    /**
     * Get the issuedDate.
     *
     * @return issuedDate
     */
    public Date getIssuedDate() {
        return this.issuedDate;
    }

    /**
     * Set the issuedDate.
     *
     * @param issuedDate issuedDate
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
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

    /**
     * Get the originalPartNo.
     *
     * @return originalPartNo
     */
    public String getOriginalPartNo() {
        return this.originalPartNo;
    }

    /**
     * Set the originalPartNo.
     *
     * @param originalPartNo originalPartNo
     */
    public void setOriginalPartNo(String originalPartNo) {
        this.originalPartNo = originalPartNo;
    }

    /**
     * Get the orderMonth.
     *
     * @return orderMonth
     */
    public String getOrderMonth() {
        return this.orderMonth;
    }

    /**
     * Set the orderMonth.
     *
     * @param orderMonth orderMonth
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    /**
     * Get the disOrderMonth.
     *
     * @return disOrderMonth
     */
    @JsonSerialize(using = JsonMonthSerializer.class)
    public String getDisOrderMonth() {
        return this.disOrderMonth;
    }

    /**
     * Set the disOrderMonth.
     *
     * @param disOrderMonth disOrderMonth
     */
    public void setDisOrderMonth(String disOrderMonth) {
        this.disOrderMonth = disOrderMonth;
    }

    /**
     * Get the showFlag.
     *
     * @return showFlag
     */
    public Integer getShowFlag() {
        return this.showFlag;
    }

    /**
     * Set the showFlag.
     *
     * @param showFlag showFlag
     */
    public void setShowFlag(Integer showFlag) {
        this.showFlag = showFlag;
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
     * Get the mailInvoice.
     *
     * @return mailInvoice
     */
    public CPVIVF11MailInvoiceEntity getMailInvoice() {
        return this.mailInvoice;
    }

    /**
     * Set the mailInvoice.
     *
     * @param mailInvoice mailInvoice
     */
    public void setMailInvoice(CPVIVF11MailInvoiceEntity mailInvoice) {
        this.mailInvoice = mailInvoice;
    }

    /**
     * Get the suppliers.
     *
     * @return suppliers
     */
    public List<CPVIVF11SupportEntity> getSuppliers() {
        return this.suppliers;
    }

    /**
     * Set the suppliers.
     *
     * @param suppliers suppliers
     */
    public void setSuppliers(List<CPVIVF11SupportEntity> suppliers) {
        this.suppliers = suppliers;
    }

}
