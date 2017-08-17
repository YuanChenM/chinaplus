/**
 * PostGrGiEntity.java
 * 
 * @screen CPVIVB01
 * @author gu_chengchen
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Post GR/GI Entity.
 */
public class PostGrGiEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Parts ID */
    private Integer partsId;

    /** Supplier ID */
    private Integer supplierId;

    /** IPO */
    private String ipo;

    /** CPO */
    private String cpo;

    /** EPO */
    private String epo;

    /** Qty */
    private BigDecimal qty;

    /** Condition Date */
    private Date conditionDate;

    /** Stock ID */
    private Integer stockId;

    /** Office ID */
    private Integer officeId;

    /** Business Pattern */
    private Integer businessPattern;

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
     * Get the partsId.
     *
     * @return partsId
     */
    public Integer getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    /**
     * Get the supplierId.
     *
     * @return supplierId
     */
    public Integer getSupplierId() {
        return this.supplierId;
    }

    /**
     * Set the supplierId.
     *
     * @param supplierId supplierId
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * Get the ipo.
     *
     * @return ipo
     */
    public String getIpo() {
        return this.ipo;
    }

    /**
     * Set the ipo.
     *
     * @param ipo ipo
     */
    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    /**
     * Get the cpo.
     *
     * @return cpo
     */
    public String getCpo() {
        return this.cpo;
    }

    /**
     * Set the cpo.
     *
     * @param cpo cpo
     */
    public void setCpo(String cpo) {
        this.cpo = cpo;
    }

    /**
     * Get the epo.
     *
     * @return epo
     */
    public String getEpo() {
        return this.epo;
    }

    /**
     * Set the epo.
     *
     * @param epo epo
     */
    public void setEpo(String epo) {
        this.epo = epo;
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
     * Get the conditionDate.
     *
     * @return conditionDate
     */
    public Date getConditionDate() {
        return this.conditionDate;
    }

    /**
     * Set the conditionDate.
     *
     * @param conditionDate conditionDate
     */
    public void setConditionDate(Date conditionDate) {
        this.conditionDate = conditionDate;
    }

    /**
     * Get the stockId.
     *
     * @return stockId
     */
    public Integer getStockId() {
        return this.stockId;
    }

    /**
     * Set the stockId.
     *
     * @param stockId stockId
     */
    public void setStockId(Integer stockId) {
        this.stockId = stockId;
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

}
