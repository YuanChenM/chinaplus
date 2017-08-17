/**
 * CPOOCF01Entity.java
 * 
 * @screen CPOOCF01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TnfBalanceByDay;
import com.chinaplus.core.base.BaseEntity;

/**
 * Order Calculation Supporting Data Report Download
 */
public class CPOOCF01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** no */
    private String no;
    
    /** partsId */
    private Integer partsId;

    /** ttcPartNo */
    private String ttcPartNo;

    /** customerPartNo */
    private String customerPartNo;

    /** oldTtcPartNo */
    private String oldTtcPartNo;

    /** customerCode */
    private String customerCode;

    /** supplierCode */
    private String supplierCode;
    
    /** supplierId */
    private Integer supplierId;

    /** partType */
    private String partType;

    /** orderlot */
    private BigDecimal orderlot;

    /** bals */
    private List<TnfBalanceByDay> bals;

    /** currentMonths */
    private List<CPOOCF01CurrentMonthEntity> currentMonths;

    /** ttcImpStock */
    private BigDecimal ttcImpStock;

    /** nearestDate */
    private Date nearestDate;

    /** stockQty */
    private BigDecimal stockQty;
    
    /** checkMaxDate */
    private String checkMaxDate;
    
    /** checkMinDate */
    private String checkMinDate;
    
    /** endingStockDate */
    private Date endingStockDate;
    
    /** impStockQty */
    private BigDecimal impStockQty;

    /** uomCode */
    private String uomCode;
    /**
     * Get the no.
     *
     * @return no
     */
    public String getNo() {
        return this.no;
    }

    /**
     * Set the no.
     *
     * @param no no
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * Get the ttcPartNo.
     *
     * @return ttcPartNo
     */
    public String getTtcPartNo() {
        return this.ttcPartNo;
    }

    /**
     * Set the ttcPartNo.
     *
     * @param ttcPartNo ttcPartNo
     */
    public void setTtcPartNo(String ttcPartNo) {
        this.ttcPartNo = ttcPartNo;
    }

    /**
     * Get the customerPartNo.
     *
     * @return customerPartNo
     */
    public String getCustomerPartNo() {
        return this.customerPartNo;
    }

    /**
     * Set the customerPartNo.
     *
     * @param customerPartNo customerPartNo
     */
    public void setCustomerPartNo(String customerPartNo) {
        this.customerPartNo = customerPartNo;
    }

    /**
     * Get the oldTtcPartNo.
     *
     * @return oldTtcPartNo
     */
    public String getOldTtcPartNo() {
        return this.oldTtcPartNo;
    }

    /**
     * Set the oldTtcPartNo.
     *
     * @param oldTtcPartNo oldTtcPartNo
     */
    public void setOldTtcPartNo(String oldTtcPartNo) {
        this.oldTtcPartNo = oldTtcPartNo;
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
     * Get the partType.
     *
     * @return partType
     */
    public String getPartType() {
        return this.partType;
    }

    /**
     * Set the partType.
     *
     * @param partType partType
     */
    public void setPartType(String partType) {
        this.partType = partType;
    }


    /**
     * Get the bals.
     *
     * @return bals
     */
    public List<TnfBalanceByDay> getBals() {
        return this.bals;
    }

    /**
     * Set the bals.
     *
     * @param bals bals
     */
    public void setBals(List<TnfBalanceByDay> bals) {
        this.bals = bals;
    }

    /**
     * Get the currentMonths.
     *
     * @return currentMonths
     */
    public List<CPOOCF01CurrentMonthEntity> getCurrentMonths() {
        return this.currentMonths;
    }

    /**
     * Set the currentMonths.
     *
     * @param currentMonths currentMonths
     */
    public void setCurrentMonths(List<CPOOCF01CurrentMonthEntity> currentMonths) {
        this.currentMonths = currentMonths;
    }

    /**
     * Get the ttcImpStock.
     *
     * @return ttcImpStock
     */
    public BigDecimal getTtcImpStock() {
        return this.ttcImpStock;
    }

    /**
     * Set the ttcImpStock.
     *
     * @param ttcImpStock ttcImpStock
     */
    public void setTtcImpStock(BigDecimal ttcImpStock) {
        this.ttcImpStock = ttcImpStock;
    }

    /**
     * Get the nearestDate.
     *
     * @return nearestDate
     */
    public Date getNearestDate() {
        return this.nearestDate;
    }

    /**
     * Set the nearestDate.
     *
     * @param nearestDate nearestDate
     */
    public void setNearestDate(Date nearestDate) {
        this.nearestDate = nearestDate;
    }

    /**
     * Get the stockQty.
     *
     * @return stockQty
     */
    public BigDecimal getStockQty() {
        return this.stockQty;
    }

    /**
     * Set the stockQty.
     *
     * @param stockQty stockQty
     */
    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
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
     * Get the checkMaxDate.
     *
     * @return checkMaxDate
     */
    public String getCheckMaxDate() {
        return this.checkMaxDate;
    }

    /**
     * Set the checkMaxDate.
     *
     * @param checkMaxDate checkMaxDate
     */
    public void setCheckMaxDate(String checkMaxDate) {
        this.checkMaxDate = checkMaxDate;
    }

    /**
     * Get the checkMinDate.
     *
     * @return checkMinDate
     */
    public String getCheckMinDate() {
        return this.checkMinDate;
    }

    /**
     * Set the checkMinDate.
     *
     * @param checkMinDate checkMinDate
     */
    public void setCheckMinDate(String checkMinDate) {
        this.checkMinDate = checkMinDate;
    }

    /**
     * Get the endingStockDate.
     *
     * @return endingStockDate
     */
    public Date getEndingStockDate() {
        return this.endingStockDate;
    }

    /**
     * Set the endingStockDate.
     *
     * @param endingStockDate endingStockDate
     */
    public void setEndingStockDate(Date endingStockDate) {
        this.endingStockDate = endingStockDate;
    }

    /**
     * Get the impStockQty.
     *
     * @return impStockQty
     */
    public BigDecimal getImpStockQty() {
        return this.impStockQty;
    }

    /**
     * Set the impStockQty.
     *
     * @param impStockQty impStockQty
     */
    public void setImpStockQty(BigDecimal impStockQty) {
        this.impStockQty = impStockQty;
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

    /**
     * Get the orderlot.
     *
     * @return orderlot
     */
    public BigDecimal getOrderlot() {
        return this.orderlot;
    }

    /**
     * Set the orderlot.
     *
     * @param orderlot orderlot
     */
    public void setOrderlot(BigDecimal orderlot) {
        this.orderlot = orderlot;
    }


}
