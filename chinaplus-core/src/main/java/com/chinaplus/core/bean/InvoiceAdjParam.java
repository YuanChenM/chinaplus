/**
 * BaseParam.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The parameter from page request.
 */
public class InvoiceAdjParam extends BaseParam {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** processDateTime */
    private Timestamp processDateTime;
    
    /** invoiceIdList */
    private List<Integer> invoiceIdList;
    
    /** officeId */
    private Integer officeId;
    
    /** customerId */
    private Integer customerId;
    
    /** currSysDate */
    private Date currSysDate;

    /**
     * The Constructors Method.
     */
    public InvoiceAdjParam() {
        invoiceIdList = new ArrayList<Integer>();
    }

    /**
     * @return the processDateTime
     */
    public Timestamp getProcessDateTime() {
        return processDateTime;
    }

    /**
     * @param processDateTime the processDateTime to set
     */
    public void setProcessDateTime(Timestamp processDateTime) {
        this.processDateTime = processDateTime;
    }

    /**
     * @return the invoiceIdList
     */
    public List<Integer> getInvoiceIdList() {
        return invoiceIdList;
    }

    /**
     * @param invoiceIdList the invoiceIdList to set
     */
    public void setInvoiceIdList(List<Integer> invoiceIdList) {
        this.invoiceIdList = invoiceIdList;
    }

    /**
     * @return the officeId
     */
    public Integer getOfficeId() {
        return officeId;
    }

    /**
     * @param officeId the officeId to set
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the currSysDate
     */
    public Date getCurrSysDate() {
        return currSysDate;
    }

    /**
     * @param currSysDate the currSysDate to set
     */
    public void setCurrSysDate(Date currSysDate) {
        this.currSysDate = currSysDate;
    }
}
