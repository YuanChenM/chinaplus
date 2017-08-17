/**
 * CPOOCF01Entity.java
 * 
 * @screen CPOOCF01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Order Calculation Supporting Data Report Download
 */
public class CPOOCF01CurrentMonthEntity extends BaseEntity{
    
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** sumQty */
    private BigDecimal sumQty;
    
    /** partsId */
    private Integer partsId;
    
    /** supplierId */
    private Integer supplierId;

    /** impPoNo */
    private String impPoNo;
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
}
