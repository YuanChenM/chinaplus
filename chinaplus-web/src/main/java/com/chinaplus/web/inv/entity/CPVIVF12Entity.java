/**
 * CPVIVF12Entity.java
 * 
 * @screen CPVIVF12
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Supplementary Upload Entity.
 */
public class CPVIVF12Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Row Number */
    private int rowNum;

    /** WEST Invoice No. */
    private String invoiceNo;

    /** ETD */
    private Date etd;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** Mail Invoice Customer Code */
    private String mailCustomerCode;

    /** TTC Part No. */
    private String ttcPartNo;

    /** Supplier Part No. */
    private String supplierPartNo;

    /** Total Qty */
    private BigDecimal totalQty;

    /** Transport Mode */
    private String transportMode;

    /** TTC Supplier Code */
    private String ttcSupplierCode;

    /** Supplier Qty */
    private BigDecimal supplierQty;

    /** Order Month-1 */
    private String orderMonth1;

    /** Qty-1 */
    private BigDecimal qty1;

    /** KANBAN Plan No.-1 */
    private String kanbanPlanNo1;

    /** Order Month-2 */
    private String orderMonth2;

    /** Qty-2 */
    private BigDecimal qty2;

    /** KANBAN Plan No.-2 */
    private String kanbanPlanNo2;

    /** Order Month-3 */
    private String orderMonth3;

    /** Qty-3 */
    private BigDecimal qty3;

    /** KANBAN Plan No.-3 */
    private String kanbanPlanNo3;

    /** ETA */
    private Date eta;

    /** Inbound Date */
    private Date inboundDate;

    /** Upload ID */
    private String uploadId;

    /** Invoice Summary ID */
    private Integer invoiceSummaryId;

    /** Parts ID */
    private Integer partsId;

    /** Qty */
    private BigDecimal qty;

    /** Uom Code */
    private String uomCode;

    /** Customer ID */
    private Integer customerId;

    /** Supplier ID */
    private Integer supplierId;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Office ID */
    private Integer officeId;

    /** Order Month */
    private String orderMonth;

    /** Invoice Group ID */
    private Integer invoiceGroupId;

    /** Invoice ID */
    private Integer invoiceId;

    /** Invoice Detail ID */
    private Integer invoiceDetailId;

    /** Current Customers */
    private List<Integer> currentCustomers;

    /**
     * Get the rowNum.
     *
     * @return rowNum
     */
    public int getRowNum() {
        return this.rowNum;
    }

    /**
     * Set the rowNum.
     *
     * @param rowNum rowNum
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
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
     * Get the ttcCustomerCode.
     *
     * @return ttcCustomerCode
     */
    public String getTtcCustomerCode() {
        return this.ttcCustomerCode;
    }

    /**
     * Set the ttcCustomerCode.
     *
     * @param ttcCustomerCode ttcCustomerCode
     */
    public void setTtcCustomerCode(String ttcCustomerCode) {
        this.ttcCustomerCode = ttcCustomerCode;
    }

    /**
     * Get the mailCustomerCode.
     *
     * @return mailCustomerCode
     */
    public String getMailCustomerCode() {
        return this.mailCustomerCode;
    }

    /**
     * Set the mailCustomerCode.
     *
     * @param mailCustomerCode mailCustomerCode
     */
    public void setMailCustomerCode(String mailCustomerCode) {
        this.mailCustomerCode = mailCustomerCode;
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
     * Get the supplierPartNo.
     *
     * @return supplierPartNo
     */
    public String getSupplierPartNo() {
        return this.supplierPartNo;
    }

    /**
     * Set the supplierPartNo.
     *
     * @param supplierPartNo supplierPartNo
     */
    public void setSupplierPartNo(String supplierPartNo) {
        this.supplierPartNo = supplierPartNo;
    }

    /**
     * Get the totalQty.
     *
     * @return totalQty
     */
    public BigDecimal getTotalQty() {
        return this.totalQty;
    }

    /**
     * Set the totalQty.
     *
     * @param totalQty totalQty
     */
    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     */
    public String getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * Get the ttcSupplierCode.
     *
     * @return ttcSupplierCode
     */
    public String getTtcSupplierCode() {
        return this.ttcSupplierCode;
    }

    /**
     * Set the ttcSupplierCode.
     *
     * @param ttcSupplierCode ttcSupplierCode
     */
    public void setTtcSupplierCode(String ttcSupplierCode) {
        this.ttcSupplierCode = ttcSupplierCode;
    }

    /**
     * Get the supplierQty.
     *
     * @return supplierQty
     */
    public BigDecimal getSupplierQty() {
        return this.supplierQty;
    }

    /**
     * Set the supplierQty.
     *
     * @param supplierQty supplierQty
     */
    public void setSupplierQty(BigDecimal supplierQty) {
        this.supplierQty = supplierQty;
    }

    /**
     * Get the orderMonth1.
     *
     * @return orderMonth1
     */
    public String getOrderMonth1() {
        return this.orderMonth1;
    }

    /**
     * Set the orderMonth1.
     *
     * @param orderMonth1 orderMonth1
     */
    public void setOrderMonth1(String orderMonth1) {
        this.orderMonth1 = orderMonth1;
    }

    /**
     * Get the qty1.
     *
     * @return qty1
     */
    public BigDecimal getQty1() {
        return this.qty1;
    }

    /**
     * Set the qty1.
     *
     * @param qty1 qty1
     */
    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    /**
     * Get the kanbanPlanNo1.
     *
     * @return kanbanPlanNo1
     */
    public String getKanbanPlanNo1() {
        return this.kanbanPlanNo1;
    }

    /**
     * Set the kanbanPlanNo1.
     *
     * @param kanbanPlanNo1 kanbanPlanNo1
     */
    public void setKanbanPlanNo1(String kanbanPlanNo1) {
        this.kanbanPlanNo1 = kanbanPlanNo1;
    }

    /**
     * Get the orderMonth2.
     *
     * @return orderMonth2
     */
    public String getOrderMonth2() {
        return this.orderMonth2;
    }

    /**
     * Set the orderMonth2.
     *
     * @param orderMonth2 orderMonth2
     */
    public void setOrderMonth2(String orderMonth2) {
        this.orderMonth2 = orderMonth2;
    }

    /**
     * Get the qty2.
     *
     * @return qty2
     */
    public BigDecimal getQty2() {
        return this.qty2;
    }

    /**
     * Set the qty2.
     *
     * @param qty2 qty2
     */
    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    /**
     * Get the kanbanPlanNo2.
     *
     * @return kanbanPlanNo2
     */
    public String getKanbanPlanNo2() {
        return this.kanbanPlanNo2;
    }

    /**
     * Set the kanbanPlanNo2.
     *
     * @param kanbanPlanNo2 kanbanPlanNo2
     */
    public void setKanbanPlanNo2(String kanbanPlanNo2) {
        this.kanbanPlanNo2 = kanbanPlanNo2;
    }

    /**
     * Get the orderMonth3.
     *
     * @return orderMonth3
     */
    public String getOrderMonth3() {
        return this.orderMonth3;
    }

    /**
     * Set the orderMonth3.
     *
     * @param orderMonth3 orderMonth3
     */
    public void setOrderMonth3(String orderMonth3) {
        this.orderMonth3 = orderMonth3;
    }

    /**
     * Get the qty3.
     *
     * @return qty3
     */
    public BigDecimal getQty3() {
        return this.qty3;
    }

    /**
     * Set the qty3.
     *
     * @param qty3 qty3
     */
    public void setQty3(BigDecimal qty3) {
        this.qty3 = qty3;
    }

    /**
     * Get the kanbanPlanNo3.
     *
     * @return kanbanPlanNo3
     */
    public String getKanbanPlanNo3() {
        return this.kanbanPlanNo3;
    }

    /**
     * Set the kanbanPlanNo3.
     *
     * @param kanbanPlanNo3 kanbanPlanNo3
     */
    public void setKanbanPlanNo3(String kanbanPlanNo3) {
        this.kanbanPlanNo3 = kanbanPlanNo3;
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
     * Get the inboundDate.
     *
     * @return inboundDate
     */
    public Date getInboundDate() {
        return this.inboundDate;
    }

    /**
     * Set the inboundDate.
     *
     * @param inboundDate inboundDate
     */
    public void setInboundDate(Date inboundDate) {
        this.inboundDate = inboundDate;
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
     * Get the invoiceGroupId.
     *
     * @return invoiceGroupId
     */
    public Integer getInvoiceGroupId() {
        return this.invoiceGroupId;
    }

    /**
     * Set the invoiceGroupId.
     *
     * @param invoiceGroupId invoiceGroupId
     */
    public void setInvoiceGroupId(Integer invoiceGroupId) {
        this.invoiceGroupId = invoiceGroupId;
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
     * Get the invoiceDetailId.
     *
     * @return invoiceDetailId
     */
    public Integer getInvoiceDetailId() {
        return this.invoiceDetailId;
    }

    /**
     * Set the invoiceDetailId.
     *
     * @param invoiceDetailId invoiceDetailId
     */
    public void setInvoiceDetailId(Integer invoiceDetailId) {
        this.invoiceDetailId = invoiceDetailId;
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

}
