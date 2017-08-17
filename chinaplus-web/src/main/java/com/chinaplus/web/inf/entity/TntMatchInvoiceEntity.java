/**
 * @screen CPIIFS03
 * @author zhang_chi
 */
package com.chinaplus.web.inf.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.web.mm.entity.MMCommonEntity;

/**
 * TntMatchInvoiceEntity.
 */
public class TntMatchInvoiceEntity extends MMCommonEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 7048576297489016165L;

    /** officeId */
    private Integer officeId;

    /** whsId */
    private Integer whsId;

    /** invoiceId */
    private Integer invoiceId;

    /** status */
    private Integer status;
    
    /** invoiceNo */
    private String invoiceNo;
    
    /** whsInvoiceNo */
    private String whsInvoiceNo;

    /** dataDate */
    private Date dataDate;

    /** dataDate */
    private Timestamp matchedDate;

    /** matchedBy */
    private Integer matchedBy;

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
     * Get the whsId.
     *
     * @return whsId
     */
    public Integer getWhsId() {
        return this.whsId;
    }

    /**
     * Set the whsId.
     *
     * @param whsId whsId
     */
    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
    }

    /**
     * Get the invoiceId.
     *
     * @return invoiceId
     */
    public Integer getInvoiceId() {
        return this.invoiceId;
    }

    /**
     * Set the invoiceId.
     *
     * @param invoiceId invoiceId
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
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
     * Get the whsInvoiceNo.
     *
     * @return whsInvoiceNo
     */
    public String getWhsInvoiceNo() {
        return this.whsInvoiceNo;
    }

    /**
     * Set the whsInvoiceNo.
     *
     * @param whsInvoiceNo whsInvoiceNo
     */
    public void setWhsInvoiceNo(String whsInvoiceNo) {
        this.whsInvoiceNo = whsInvoiceNo;
    }

    /**
     * Get the dataDate.
     *
     * @return dataDate
     */
    public Date getDataDate() {
        return this.dataDate;
    }

    /**
     * Set the dataDate.
     *
     * @param dataDate dataDate
     */
    public void setDataDate(Date dataDate) {
        this.dataDate = dataDate;
    }

    /**
     * Get the matchedBy.
     *
     * @return matchedBy
     */
    public Integer getMatchedBy() {
        return this.matchedBy;
    }

    /**
     * Set the matchedBy.
     *
     * @param matchedBy matchedBy
     */
    public void setMatchedBy(Integer matchedBy) {
        this.matchedBy = matchedBy;
    }

    /**
     * Get the matchedDate.
     *
     * @return matchedDate
     */
    public Timestamp getMatchedDate() {
        return this.matchedDate;
    }

    /**
     * Set the matchedDate.
     *
     * @param matchedDate matchedDate
     */
    public void setMatchedDate(Timestamp matchedDate) {
        this.matchedDate = matchedDate;
        
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
        
    }
    
}
