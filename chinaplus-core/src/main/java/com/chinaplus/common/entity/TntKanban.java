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
 * The persistent class for the TNT_KANBAN database table.
 * 
 */
@Entity(name = "TNT_KANBAN")
public class TntKanban extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_KANBAN_KANBANID_GENERATOR",
        sequenceName = "SEQ_TNT_KANBAN",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_KANBAN_KANBANID_GENERATOR")
    @Column(name = "KANBAN_ID")
    private Integer kanbanId;

    @Column(name = "AIR_FLAG")
    private Integer airFlag;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "KANBAN_PLAN_NO")
    private String kanbanPlanNo;

    @Column(name = "OFFICE_ID")
    private Integer officeId;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "REVISION_CODE_SET")
    private String revisionCodeSet;

    @Column(name = "REVISION_REASON")
    private String revisionReason;

    @Column(name = "REVISION_VERSION")
    private Integer revisionVersion;

    @Column(name = "SEA_FLAG")
    private Integer seaFlag;

    @Column(name = "\"STATUS\"")
    private Integer status;

    @Temporal(TemporalType.DATE)
    @Column(name = "CANCELLED_DATE")
    private Date cancelledDate;

    @Column(name = "SUPPLIER_ID")
    private Integer supplierId;

    @Column(name = "TOTAL_BALANCE_QTY")
    private BigDecimal totalBalanceQty;

    @Column(name = "TOTAL_INBOUND_QTY")
    private BigDecimal totalInboundQty;

    @Column(name = "TOTAL_ONSHIPPING_QTY")
    private BigDecimal totalOnshippingQty;

    @Column(name = "TOTAL_ORDER_QTY")
    private BigDecimal totalOrderQty;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "UPLOAD_FILE_TYPE")
    private Integer uploadFileType;

    @Column(name = "UPLOADED_BY")
    private Integer uploadedBy;

    @Column(name = "UPLOADED_DATE")
    private Timestamp uploadedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TntKanban() {}

    public Integer getKanbanId() {
        return this.kanbanId;
    }

    public void setKanbanId(Integer kanbanId) {
        this.kanbanId = kanbanId;
    }

    public Integer getAirFlag() {
        return this.airFlag;
    }

    public void setAirFlag(Integer airFlag) {
        this.airFlag = airFlag;
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

    public String getKanbanPlanNo() {
        return this.kanbanPlanNo;
    }

    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
    }

    public Integer getOfficeId() {
        return this.officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public String getRevisionCodeSet() {
        return this.revisionCodeSet;
    }

    public void setRevisionCodeSet(String revisionCodeSet) {
        this.revisionCodeSet = revisionCodeSet;
    }

    public String getRevisionReason() {
        return this.revisionReason;
    }

    public void setRevisionReason(String revisionReason) {
        this.revisionReason = revisionReason;
    }

    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    public Integer getSeaFlag() {
        return this.seaFlag;
    }

    public void setSeaFlag(Integer seaFlag) {
        this.seaFlag = seaFlag;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCancelledDate() {
        return this.cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public Integer getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getTotalBalanceQty() {
        return this.totalBalanceQty;
    }

    public void setTotalBalanceQty(BigDecimal totalBalanceQty) {
        this.totalBalanceQty = totalBalanceQty;
    }

    public BigDecimal getTotalInboundQty() {
        return this.totalInboundQty;
    }

    public void setTotalInboundQty(BigDecimal totalInboundQty) {
        this.totalInboundQty = totalInboundQty;
    }

    public BigDecimal getTotalOnshippingQty() {
        return this.totalOnshippingQty;
    }

    public void setTotalOnshippingQty(BigDecimal totalOnshippingQty) {
        this.totalOnshippingQty = totalOnshippingQty;
    }

    public BigDecimal getTotalOrderQty() {
        return this.totalOrderQty;
    }

    public void setTotalOrderQty(BigDecimal totalOrderQty) {
        this.totalOrderQty = totalOrderQty;
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

    public Integer getUploadFileType() {
        return this.uploadFileType;
    }

    public void setUploadFileType(Integer uploadFileType) {
        this.uploadFileType = uploadFileType;
    }

    public Integer getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Timestamp getUploadedDate() {
        return this.uploadedDate;
    }

    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}