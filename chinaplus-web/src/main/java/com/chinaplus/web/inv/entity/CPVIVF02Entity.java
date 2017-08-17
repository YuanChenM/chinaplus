/**
 * CPVIVF02Entity.java
 * 
 * @screen CPVIVF02
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Download For WEST Entity.
 */
public class CPVIVF02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Container No. */
    private String containerNo;

    /** Shipment Type */
    private String sealNo;

    /** Pallet No. */
    private String moduleNo;

    /** Part No. */
    private String partNo;

    /** Qty */
    private BigDecimal qty;

    /** Price */
    private String price;

    /** Currency */
    private String currency;

    /** Uom Code */
    private String uomCode;

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
     * Get the sealNo.
     *
     * @return sealNo
     */
    public String getSealNo() {
        return this.sealNo;
    }

    /**
     * Set the sealNo.
     *
     * @param sealNo sealNo
     */
    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
    }

    /**
     * Get the moduleNo.
     *
     * @return moduleNo
     */
    public String getModuleNo() {
        return this.moduleNo;
    }

    /**
     * Set the moduleNo.
     *
     * @param moduleNo moduleNo
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    /**
     * Get the partNo.
     *
     * @return partNo
     */
    public String getPartNo() {
        return this.partNo;
    }

    /**
     * Set the partNo.
     *
     * @param partNo partNo
     */
    public void setPartNo(String partNo) {
        this.partNo = partNo;
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
     * Get the price.
     *
     * @return price
     */
    public String getPrice() {
        return this.price;
    }

    /**
     * Set the price.
     *
     * @param price price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Get the currency.
     *
     * @return currency
     */
    public String getCurrency() {
        return this.currency;
    }

    /**
     * Set the currency.
     *
     * @param currency currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the uomCode.
     *
     * @return uomCode
     */
    public String getUomCode() {
        return this.uomCode;
    }

    /**
     * Set the uomCode.
     *
     * @param uomCode uomCode
     */
    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

}
