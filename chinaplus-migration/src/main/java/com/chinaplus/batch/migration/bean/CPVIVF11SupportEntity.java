/**
 * CPVIVF11SupportEntity.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.batch.migration.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Upload Support Entity.
 */
public class CPVIVF11SupportEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Office ID */
    private Integer officeId;

    /** Parts ID */
    private Integer partsId;

    /** TTC Parts No. */
    private String ttcPartsNo;

    /** UOM Code */
    private String uomCode;

    /** Customer ID */
    private Integer customerId;

    /** TTC Customer Code */
    private String ttcCustCode;

    /** Export Customer Code */
    private String expCustCode;

    /** Invoice Customer Code */
    private String invCustCode;

    /** Supplier ID */
    private Integer supplierId;

    /** Exp Parts ID */
    private Integer expPartsId;

    /** TTC Supplier Code */
    private String ttcSuppCode;

    /** Supplier Part No. */
    private String suppPartsNo;

    /** Order Month */
    private String orderMonth;

    /** From Date */
    private Date fromDate;

    /** To Date */
    private Date toDate;

    /** Min Issued Date */
    private Date minIssuedDate;

    /** Max Issued Date */
    private Date maxIssuedDate;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Exp Region */
    private String expRegion;

    /** Imp Region */
    private String impRegion;

    /** Vessel Name */
    private String vesselName;

    /** ETD */
    private Date etd;

    /** ETA */
    private Date eta;

    /** Inbound Plan Date */
    private Date inbPlanDate;

    /** Invoice No. */
    private String invoiceNo;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Upload ID */
    private String uploadId;

    /** Invoice Qty */
    private BigDecimal invoiceQty;

    /** Transport Mode */
    private Integer transMode;

    /** Export Customer Code List */
    private List<String> expCustomerCodeList;

    /** Invoice Customer Code List */
    private List<String> invCustomerCodeList;

    /** Invoice Summary ID List */
    private List<Integer> summaryIdList;

    /** Current Customers */
    private List<Integer> currentCustomers;

    /** Customer ID List */
    private List<Integer> customerIdList;

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
     * Get the ttcPartsNo.
     *
     * @return ttcPartsNo
     */
    public String getTtcPartsNo() {
        return this.ttcPartsNo;
    }

    /**
     * Set the ttcPartsNo.
     *
     * @param ttcPartsNo ttcPartsNo
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
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
     * Get the customerId.
     *
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the ttcCustCode.
     *
     * @return ttcCustCode
     */
    public String getTtcCustCode() {
        return this.ttcCustCode;
    }

    /**
     * Set the ttcCustCode.
     *
     * @param ttcCustCode ttcCustCode
     */
    public void setTtcCustCode(String ttcCustCode) {
        this.ttcCustCode = ttcCustCode;
    }

    /**
     * Get the expCustCode.
     *
     * @return expCustCode
     */
    public String getExpCustCode() {
        return this.expCustCode;
    }

    /**
     * Set the expCustCode.
     *
     * @param expCustCode expCustCode
     */
    public void setExpCustCode(String expCustCode) {
        this.expCustCode = expCustCode;
    }

    /**
     * Get the invCustCode.
     *
     * @return invCustCode
     */
    public String getInvCustCode() {
        return this.invCustCode;
    }

    /**
     * Set the invCustCode.
     *
     * @param invCustCode invCustCode
     */
    public void setInvCustCode(String invCustCode) {
        this.invCustCode = invCustCode;
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
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * Get the ttcSuppCode.
     *
     * @return ttcSuppCode
     */
    public String getTtcSuppCode() {
        return this.ttcSuppCode;
    }

    /**
     * Set the ttcSuppCode.
     *
     * @param ttcSuppCode ttcSuppCode
     */
    public void setTtcSuppCode(String ttcSuppCode) {
        this.ttcSuppCode = ttcSuppCode;
    }

    /**
     * Get the suppPartsNo.
     *
     * @return suppPartsNo
     */
    public String getSuppPartsNo() {
        return this.suppPartsNo;
    }

    /**
     * Set the suppPartsNo.
     *
     * @param suppPartsNo suppPartsNo
     */
    public void setSuppPartsNo(String suppPartsNo) {
        this.suppPartsNo = suppPartsNo;
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
     * Get the fromDate.
     *
     * @return fromDate
     */
    public Date getFromDate() {
        return this.fromDate;
    }

    /**
     * Set the fromDate.
     *
     * @param fromDate fromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Get the toDate.
     *
     * @return toDate
     */
    public Date getToDate() {
        return this.toDate;
    }

    /**
     * Set the toDate.
     *
     * @param toDate toDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Get the minIssuedDate.
     *
     * @return minIssuedDate
     */
    public Date getMinIssuedDate() {
        return this.minIssuedDate;
    }

    /**
     * Set the minIssuedDate.
     *
     * @param minIssuedDate minIssuedDate
     */
    public void setMinIssuedDate(Date minIssuedDate) {
        this.minIssuedDate = minIssuedDate;
    }

    /**
     * Get the maxIssuedDate.
     *
     * @return maxIssuedDate
     */
    public Date getMaxIssuedDate() {
        return this.maxIssuedDate;
    }

    /**
     * Set the maxIssuedDate.
     *
     * @param maxIssuedDate maxIssuedDate
     */
    public void setMaxIssuedDate(Date maxIssuedDate) {
        this.maxIssuedDate = maxIssuedDate;
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
     * Get the expRegion.
     *
     * @return expRegion
     */
    public String getExpRegion() {
        return this.expRegion;
    }

    /**
     * Set the expRegion.
     *
     * @param expRegion expRegion
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    /**
     * Get the impRegion.
     *
     * @return impRegion
     */
    public String getImpRegion() {
        return this.impRegion;
    }

    /**
     * Set the impRegion.
     *
     * @param impRegion impRegion
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
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
     * Get the eta.
     *
     * @return eta
     */
    public Date getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * Get the inbPlanDate.
     *
     * @return inbPlanDate
     */
    public Date getInbPlanDate() {
        return this.inbPlanDate;
    }

    /**
     * Set the inbPlanDate.
     *
     * @param inbPlanDate inbPlanDate
     */
    public void setInbPlanDate(Date inbPlanDate) {
        this.inbPlanDate = inbPlanDate;
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
     * Get the uploadId.
     *
     * @return uploadId
     */
    public String getUploadId() {
        return this.uploadId;
    }

    /**
     * Set the uploadId.
     *
     * @param uploadId uploadId
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
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
     * Get the transMode.
     *
     * @return transMode
     */
    public Integer getTransMode() {
        return this.transMode;
    }

    /**
     * Set the transMode.
     *
     * @param transMode transMode
     */
    public void setTransMode(Integer transMode) {
        this.transMode = transMode;
    }

    /**
     * Get the expCustomerCodeList.
     *
     * @return expCustomerCodeList
     */
    public List<String> getExpCustomerCodeList() {
        return this.expCustomerCodeList;
    }

    /**
     * Set the expCustomerCodeList.
     *
     * @param expCustomerCodeList expCustomerCodeList
     */
    public void setExpCustomerCodeList(List<String> expCustomerCodeList) {
        this.expCustomerCodeList = expCustomerCodeList;
    }

    /**
     * Get the invCustomerCodeList.
     *
     * @return invCustomerCodeList
     */
    public List<String> getInvCustomerCodeList() {
        return this.invCustomerCodeList;
    }

    /**
     * Set the invCustomerCodeList.
     *
     * @param invCustomerCodeList invCustomerCodeList
     */
    public void setInvCustomerCodeList(List<String> invCustomerCodeList) {
        this.invCustomerCodeList = invCustomerCodeList;
    }

    /**
     * Get the summaryIdList.
     *
     * @return summaryIdList
     */
    public List<Integer> getSummaryIdList() {
        return this.summaryIdList;
    }

    /**
     * Set the summaryIdList.
     *
     * @param summaryIdList summaryIdList
     */
    public void setSummaryIdList(List<Integer> summaryIdList) {
        this.summaryIdList = summaryIdList;
    }

    /**
     * Get the currentCustomers.
     *
     * @return currentCustomers
     */
    public List<Integer> getCurrentCustomers() {
        return this.currentCustomers;
    }

    /**
     * Set the currentCustomers.
     *
     * @param currentCustomers currentCustomers
     */
    public void setCurrentCustomers(List<Integer> currentCustomers) {
        this.currentCustomers = currentCustomers;
    }

    /**
     * Get the customerIdList.
     *
     * @return customerIdList
     */
    public List<Integer> getCustomerIdList() {
        return this.customerIdList;
    }

    /**
     * Set the customerIdList.
     *
     * @param customerIdList customerIdList
     */
    public void setCustomerIdList(List<Integer> customerIdList) {
        this.customerIdList = customerIdList;
    }

}
