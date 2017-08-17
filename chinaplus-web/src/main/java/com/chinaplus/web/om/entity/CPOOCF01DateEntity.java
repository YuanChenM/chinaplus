/**
 * CPOOCF01Entity.java
 * 
 * @screen CPOOCF01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * Order Calculation Supporting Data Report Download
 */
public class CPOOCF01DateEntity extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** partsId */
    private Integer partsId;
    
    /** cfcMonth */
    private String cfcMonth;
    
    /** cfcQty */
    private BigDecimal cfcQty;
    
    /** fcDate*/
    private Date fcDate;

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
     * Get the cfcMonth.
     *
     * @return cfcMonth
     */
    public String getCfcMonth() {
        return this.cfcMonth;
    }

    /**
     * Set the cfcMonth.
     *
     * @param cfcMonth cfcMonth
     */
    public void setCfcMonth(String cfcMonth) {
        this.cfcMonth = cfcMonth;
    }

    /**
     * Get the cfcQty.
     *
     * @return cfcQty
     */
    public BigDecimal getCfcQty() {
        return this.cfcQty;
    }

    /**
     * Set the cfcQty.
     *
     * @param cfcQty cfcQty
     */
    public void setCfcQty(BigDecimal cfcQty) {
        this.cfcQty = cfcQty;
    }

    /**
     * Get the fcDate.
     *
     * @return fcDate
     */
    public Date getFcDate() {
        return this.fcDate;
    }

    /**
     * Set the fcDate.
     *
     * @param fcDate fcDate
     */
    public void setFcDate(Date fcDate) {
        this.fcDate = fcDate;
    }
    
    
}
