/**
 * CPSDRF01Entity.java
 * 
 * @screen CPSKSS01
 * @author shi_yuxi
 */
package com.chinaplus.web.sa.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * kpi download
 */
public class CPSDRF01Entity extends BaseEntity {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** partsId */
    private Integer partsId;

    /** ttcPart */
    private String ttcPart;

    /** expRegion */
    private String region;

    private String customerCode;

    /** supplierCode */
    private String supplierCode;

    /** expSoDate */
    private Date expSoDate;

    /** impPoNo */
    private String impPoNo;

    /** etd */
    private Date etd;

    /** sumQty */
    private BigDecimal sumQty;

    private String orderMonth;

    private String planNo;

    private String invoiceNo;

    private String invoiceNoQty;

    private boolean needShowQty;

    private BigDecimal invoiceQty;

    private BigDecimal delay;

    private String uomCode;

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
     * Get the ttcPart.
     *
     * @return ttcPart
     */
    public String getTtcPart() {
        return this.ttcPart;
    }

    /**
     * Set the ttcPart.
     *
     * @param ttcPart ttcPart
     */
    public void setTtcPart(String ttcPart) {
        this.ttcPart = ttcPart;
    }

    /**
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Get the expSoDate.
     *
     * @return expSoDate
     */
    public Date getExpSoDate() {
        return this.expSoDate;
    }

    /**
     * Set the expSoDate.
     *
     * @param expSoDate expSoDate
     */
    public void setExpSoDate(Date expSoDate) {
        this.expSoDate = expSoDate;
    }

    /**
     * Get the impPoNo.
     *
     * @return impPoNo
     */
    public String getImpPoNo() {
        return this.impPoNo;
    }

    /**
     * Set the impPoNo.
     *
     * @param impPoNo impPoNo
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
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
     * Get the sumQty.
     *
     * @return sumQty
     */
    public BigDecimal getSumQty() {
        return this.sumQty;
    }

    /**
     * Set the sumQty.
     *
     * @param sumQty sumQty
     */
    public void setSumQty(BigDecimal sumQty) {
        this.sumQty = sumQty;
    }

    /**
     * Get the region.
     *
     * @return region
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Set the region.
     *
     * @param region region
     */
    public void setRegion(String region) {
        this.region = region;
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
     * Get the planNo.
     *
     * @return planNo
     */
    public String getPlanNo() {
        return this.planNo;
    }

    /**
     * Set the planNo.
     *
     * @param planNo planNo
     */
    public void setPlanNo(String planNo) {
        this.planNo = planNo;
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
     * Get the invoiceNoQty.
     *
     * @return invoiceNoQty
     */
    public String getInvoiceNoQty() {
        return this.invoiceNoQty;
    }

    /**
     * Set the invoiceNoQty.
     *
     * @param invoiceNoQty invoiceNoQty
     */
    public void setInvoiceNoQty(String invoiceNoQty) {
        this.invoiceNoQty = invoiceNoQty;
    }

    /**
     * Get the needShowQty.
     *
     * @return needShowQty
     */
    public boolean isNeedShowQty() {
        return this.needShowQty;
    }

    /**
     * Set the needShowQty.
     *
     * @param needShowQty needShowQty
     */
    public void setNeedShowQty(boolean needShowQty) {
        this.needShowQty = needShowQty;
    }

    /**
     * Get the invoiceQty.
     *
     * @return invoiceQty
     */
    public BigDecimal getInvoiceQty() {
        return this.invoiceQty;
    }

    /**
     * Set the invoiceQty.
     *
     * @param invoiceQty invoiceQty
     */
    public void setInvoiceQty(BigDecimal invoiceQty) {
        this.invoiceQty = invoiceQty;
    }

    /**
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Get the delay.
     *
     * @return delay
     */
    public BigDecimal getDelay() {
        return this.delay;
    }

    /**
     * Set the delay.
     *
     * @param delay delay
     */
    public void setDelay(BigDecimal delay) {
        this.delay = delay;
    }

	/**
	 * @return the uomCode
	 */
	public String getUomCode() {
		return uomCode;
	}

	/**
	 * @param uomCode the uomCode to set
	 */
	public void setUomCode(String uomCode) {
		this.uomCode = uomCode;
	}

}
