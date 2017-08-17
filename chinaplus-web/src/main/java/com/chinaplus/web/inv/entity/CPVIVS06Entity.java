/**
 * CPVIVS06Entity.java
 * 
 * @screen CPVIVS06
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * Post GR/GI Entity.
 */
public class CPVIVS06Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Version */
    private Integer version;

    /** GR Date */
    private String grDate;

    /** GI Date */
    private String giDate;

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
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the grDate.
     *
     * @return grDate
     */
    public String getGrDate() {
        return this.grDate;
    }

    /**
     * Set the grDate.
     *
     * @param grDate grDate
     */
    public void setGrDate(String grDate) {
        this.grDate = grDate;
    }

    /**
     * Get the giDate.
     *
     * @return giDate
     */
    public String getGiDate() {
        return this.giDate;
    }

    /**
     * Set the giDate.
     *
     * @param giDate giDate
     */
    public void setGiDate(String giDate) {
        this.giDate = giDate;
    }

}
