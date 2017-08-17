package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The persistent class for the TNT_KANBAN_PARTS database table.
 * 
 */
@Entity(name = "TNT_KANBAN_PARTS")
public class TntKanbanPart extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNT_KANBAN_PARTS_KANBANPARTSID_GENERATOR",
        sequenceName = "SEQ_TNT_KANBAN_PARTS",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNT_KANBAN_PARTS_KANBANPARTSID_GENERATOR")
    @Column(name = "KANBAN_PARTS_ID")
    private Integer kanbanPartsId;

    @Column(name = "BOX_NO")
    private String boxNo;

    @Column(name = "BOX_TYPE")
    private String boxType;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    private String dock;

    @Column(name = "FC_QTY1")
    private BigDecimal fcQty1;

    @Column(name = "FC_QTY2")
    private BigDecimal fcQty2;

    @Column(name = "FC_QTY3")
    private BigDecimal fcQty3;

    @Column(name = "FC_QTY4")
    private BigDecimal fcQty4;

    @Column(name = "FC_QTY5")
    private BigDecimal fcQty5;

    @Column(name = "FC_QTY6")
    private BigDecimal fcQty6;

    @Column(name = "FORCE_COMPLETED_BY")
    private Integer forceCompletedBy;

    @Column(name = "FORCE_COMPLETED_DATE")
    private Timestamp forceCompletedDate;

    @Column(name = "KANBAN_QTY")
    private BigDecimal kanbanQty;

    @Column(name = "ORDER_MONTH")
    private String orderMonth;

    @Column(name = "PARTS_ID")
    private Integer partsId;

    private String plant;

    private BigDecimal qty;

    private String remark;

    @Column(name = "SEQ_NO")
    private String seqNo;

    private BigDecimal spq;

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

    @Column(name = "KANBAN_ID")
    private Integer kanbanId;

    public TntKanbanPart() {}

    public Integer getKanbanPartsId() {
        return this.kanbanPartsId;
    }

    public void setKanbanPartsId(Integer kanbanPartsId) {
        this.kanbanPartsId = kanbanPartsId;
    }

    public String getBoxNo() {
        return this.boxNo;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public String getBoxType() {
        return this.boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
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

    public String getDock() {
        return this.dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    public BigDecimal getFcQty1() {
        return this.fcQty1;
    }

    public void setFcQty1(BigDecimal fcQty1) {
        this.fcQty1 = fcQty1;
    }

    public BigDecimal getFcQty2() {
        return this.fcQty2;
    }

    public void setFcQty2(BigDecimal fcQty2) {
        this.fcQty2 = fcQty2;
    }

    public BigDecimal getFcQty3() {
        return this.fcQty3;
    }

    public void setFcQty3(BigDecimal fcQty3) {
        this.fcQty3 = fcQty3;
    }

    public BigDecimal getFcQty4() {
        return this.fcQty4;
    }

    public void setFcQty4(BigDecimal fcQty4) {
        this.fcQty4 = fcQty4;
    }

    public BigDecimal getFcQty5() {
        return this.fcQty5;
    }

    public void setFcQty5(BigDecimal fcQty5) {
        this.fcQty5 = fcQty5;
    }

    public BigDecimal getFcQty6() {
        return this.fcQty6;
    }

    public void setFcQty6(BigDecimal fcQty6) {
        this.fcQty6 = fcQty6;
    }

    public Integer getForceCompletedBy() {
        return this.forceCompletedBy;
    }

    public void setForceCompletedBy(Integer forceCompletedBy) {
        this.forceCompletedBy = forceCompletedBy;
    }

    public Timestamp getForceCompletedDate() {
        return this.forceCompletedDate;
    }

    public void setForceCompletedDate(Timestamp forceCompletedDate) {
        this.forceCompletedDate = forceCompletedDate;
    }

    public BigDecimal getKanbanQty() {
        return this.kanbanQty;
    }

    public void setKanbanQty(BigDecimal kanbanQty) {
        this.kanbanQty = kanbanQty;
    }

    public String getOrderMonth() {
        return this.orderMonth;
    }

    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    public Integer getPartsId() {
        return this.partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public String getPlant() {
        return this.plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public BigDecimal getQty() {
        return this.qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public BigDecimal getSpq() {
        return this.spq;
    }

    public void setSpq(BigDecimal spq) {
        this.spq = spq;
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

    public Integer getKanbanId() {
        return this.kanbanId;
    }

    public void setKanbanId(Integer kanbanId) {
        this.kanbanId = kanbanId;
    }

}