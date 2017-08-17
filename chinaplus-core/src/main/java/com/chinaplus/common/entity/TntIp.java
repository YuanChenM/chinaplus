package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the TNT_IP database table.
 * 
 */
@Entity(name = "TNT_IP")
public class TntIp extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_IP_IPID_GENERATOR",
        sequenceName = "SEQ_TNT_IP",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_IP_IPID_GENERATOR")
    @Column(name = "IP_ID")
    private Integer ipId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "CUSTOMER_ORDER_NO")
    private String customerOrderNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "CUSTOMS_CLR_DATE")
    private Date customsClrDate;

    @Column(name = "DELIVERY_NOTE_NO")
    private String deliveryNoteNo;

    @Temporal(TemporalType.DATE)
    @Column(name = "DEVANNED_DATE")
    private Date devannedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXP_INB_PLAN_DATE")
    private Date expInbPlanDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EXP_OB_ACTUAL_DATE")
    private Date expObActualDate;

    @Column(name = "EXP_PARTS_ID")
    private Integer expPartsId;

    @Column(name = "EXP_PO_ITEM_NO")
    private String expPoItemNo;

    @Column(name = "EXP_PO_NO")
    private String expPoNo;

    @Column(name = "IMP_DECANT_DATE")
    private Timestamp impDecantDatetime;

    @Column(name = "IMP_DISPATCHED_DATE")
    private Timestamp impDispatchedDatetime;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_INB_ACTUAL_DATE")
    private Date impInbActualDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_OB_ACTUAL_DATE")
    private Date impObActualDate;

    @Column(name = "IMP_OUTBOUND_DATE")
    private Timestamp impOutboundDatetime;

    @Column(name = "IMP_PO_NO")
    private String impPoNo;

    @Column(name = "IMP_SA_DATE")
    private Timestamp impSaDatetime;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_ST_DATE")
    private Date impStDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "IMP_WT_DATE")
    private Date impWtDate;

    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    @Column(name = "IP_NO")
    private String ipNo;

    @Column(name = "KANBAN_PLAN_NO")
    private String kanbanPlanNo;

    @Column(name = "MATCHED_INV_QTY")
    private BigDecimal matchedInvQty;

    @Column(name = "MODULE_NO")
    private String moduleNo;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Temporal(TemporalType.DATE)
    @Column(name = "ONHOLD_DATE")
    private Date onholdDate;

    @Column(name = "ONHOLD_FLAG")
    private Integer onholdFlag;

    @Column(name = "ORIGINAL_PARTS_ID")
    private Integer originalPartsId;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    @Column(name = "PID_NO")
    private String pidNo;

    private BigDecimal qty;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    @Column(name = "WHS_ID")
    private Integer whsId;

    public TntIp() {}

    public Integer getIpId() {
        return this.ipId;
    }

    public void setIpId(Integer ipId) {
        this.ipId = ipId;
    }

    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }

    public String getContainerNo() {
        return this.containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public Date getCustomsClrDate() {
        return this.customsClrDate;
    }

    public void setCustomsClrDate(Date customsClrDate) {
        this.customsClrDate = customsClrDate;
    }

    public String getDeliveryNoteNo() {
        return this.deliveryNoteNo;
    }

    public void setDeliveryNoteNo(String deliveryNoteNo) {
        this.deliveryNoteNo = deliveryNoteNo;
    }

    public Date getDevannedDate() {
        return this.devannedDate;
    }

    public void setDevannedDate(Date devannedDate) {
        this.devannedDate = devannedDate;
    }

    public Date getExpInbPlanDate() {
        return this.expInbPlanDate;
    }

    public void setExpInbPlanDate(Date expInbPlanDate) {
        this.expInbPlanDate = expInbPlanDate;
    }

    public Date getExpObActualDate() {
        return this.expObActualDate;
    }

    public void setExpObActualDate(Date expObActualDate) {
        this.expObActualDate = expObActualDate;
    }

    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    public String getExpPoItemNo() {
        return this.expPoItemNo;
    }

    public void setExpPoItemNo(String expPoItemNo) {
        this.expPoItemNo = expPoItemNo;
    }

    public String getExpPoNo() {
        return this.expPoNo;
    }

    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    public Timestamp getImpDecantDatetime() {
        return this.impDecantDatetime;
    }

    public void setImpDecantDatetime(Timestamp impDecantDatetime) {
        this.impDecantDatetime = impDecantDatetime;
    }

    public Timestamp getImpDispatchedDatetime() {
        return this.impDispatchedDatetime;
    }

    public void setImpDispatchedDatetime(Timestamp impDispatchedDatetime) {
        this.impDispatchedDatetime = impDispatchedDatetime;
    }

    public Date getImpInbActualDate() {
        return this.impInbActualDate;
    }

    public void setImpInbActualDate(Date impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
    }

    public Date getImpObActualDate() {
        return this.impObActualDate;
    }

    public void setImpObActualDate(Date impObActualDate) {
        this.impObActualDate = impObActualDate;
    }

    public Timestamp getImpOutboundDatetime() {
        return this.impOutboundDatetime;
    }

    public void setImpOutboundDatetime(Timestamp impOutboundDatetime) {
        this.impOutboundDatetime = impOutboundDatetime;
    }

    public String getImpPoNo() {
        return this.impPoNo;
    }

    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    public Timestamp getImpSaDatetime() {
        return this.impSaDatetime;
    }

    public void setImpSaDatetime(Timestamp impSaDatetime) {
        this.impSaDatetime = impSaDatetime;
    }

    public Date getImpStDate() {
        return this.impStDate;
    }

    public void setImpStDate(Date impStDate) {
        this.impStDate = impStDate;
    }

    public Date getImpWtDate() {
        return this.impWtDate;
    }

    public void setImpWtDate(Date impWtDate) {
        this.impWtDate = impWtDate;
    }

    public String getInvoiceNo() {
        return this.invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getIpNo() {
        return this.ipNo;
    }

    public void setIpNo(String ipNo) {
        this.ipNo = ipNo;
    }

    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    public BigDecimal getMatchedInvQty() {
        return this.matchedInvQty;
    }

    public void setMatchedInvQty(BigDecimal matchedInvQty) {
        this.matchedInvQty = matchedInvQty;
    }

    public String getModuleNo() {
        return this.moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Date getOnholdDate() {
        return this.onholdDate;
    }

    public void setOnholdDate(Date onholdDate) {
        this.onholdDate = onholdDate;
    }

    public Integer getOnholdFlag() {
        return this.onholdFlag;
    }

    public void setOnholdFlag(Integer onholdFlag) {
        this.onholdFlag = onholdFlag;
    }

    public Integer getOriginalPartsId() {
        return this.originalPartsId;
    }

    public void setOriginalPartsId(Integer originalPartsId) {
        this.originalPartsId = originalPartsId;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public String getPidNo() {
        return this.pidNo;
    }

    public void setPidNo(String pidNo) {
        this.pidNo = pidNo;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getWhsId() {
        return this.whsId;
    }

    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
    }

}