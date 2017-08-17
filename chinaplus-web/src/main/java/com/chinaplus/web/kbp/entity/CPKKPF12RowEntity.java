/**
 * CPKKPF12PlanRowEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12RowEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Row No. */
    private int rowNo;

    /** Parts ID */
    private int partsId;

    /** Force Completed Flag */
    private int forceCompletedFlag;

    /** TTC Parts No */
    private String ttcPartsNo;

    /** Old Parts No */
    private String oldPartsNo;

    /** TTC Customer Code */
    private String ttcCustomerCode;

    /** Kanban Customer Code */
    private String kanbanCustomerCode;

    /** Supp Parts No. */
    private String suppPartsNo;

    /** Supplier Code */
    private String supplierCode;

    /** Remark */
    private String remark;

    /** Qty/Box */
    private String qtyBox;

    /** Order Qty */
    private String orderQty;

    /** Kanban Qty */
    private String kanbanQty;

    /** Digits */
    private int digits;

    /**
     * Get the rowNo.
     *
     * @return rowNo
     */
    public int getRowNo() {
        return this.rowNo;
    }

    /**
     * Set the rowNo.
     *
     * @param rowNo rowNo
     */
    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    /**
     * Get the partsId.
     *
     * @return partsId
     */
    public int getPartsId() {
        return this.partsId;
    }

    /**
     * Set the partsId.
     *
     * @param partsId partsId
     */
    public void setPartsId(int partsId) {
        this.partsId = partsId;
    }

    /**
     * Get the forceCompletedFlag.
     *
     * @return forceCompletedFlag
     */
    public int getForceCompletedFlag() {
        return this.forceCompletedFlag;
    }

    /**
     * Set the forceCompletedFlag.
     *
     * @param forceCompletedFlag forceCompletedFlag
     */
    public void setForceCompletedFlag(int forceCompletedFlag) {
        this.forceCompletedFlag = forceCompletedFlag;
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
     * Get the oldPartsNo.
     *
     * @return oldPartsNo
     */
    public String getOldPartsNo() {
        return this.oldPartsNo;
    }

    /**
     * Set the oldPartsNo.
     *
     * @param oldPartsNo oldPartsNo
     */
    public void setOldPartsNo(String oldPartsNo) {
        this.oldPartsNo = oldPartsNo;
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
     * Get the kanbanCustomerCode.
     *
     * @return kanbanCustomerCode
     */
    public String getKanbanCustomerCode() {
        return this.kanbanCustomerCode;
    }

    /**
     * Set the kanbanCustomerCode.
     *
     * @param kanbanCustomerCode kanbanCustomerCode
     */
    public void setKanbanCustomerCode(String kanbanCustomerCode) {
        this.kanbanCustomerCode = kanbanCustomerCode;
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
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Get the remark.
     *
     * @return remark
     */
    public String getRemark() {
        return this.remark;
    }

    /**
     * Set the remark.
     *
     * @param remark remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Get the qtyBox.
     *
     * @return qtyBox
     */
    public String getQtyBox() {
        return this.qtyBox;
    }

    /**
     * Set the qtyBox.
     *
     * @param qtyBox qtyBox
     */
    public void setQtyBox(String qtyBox) {
        this.qtyBox = qtyBox;
    }

    /**
     * Get the orderQty.
     *
     * @return orderQty
     */
    public String getOrderQty() {
        return this.orderQty;
    }

    /**
     * Set the orderQty.
     *
     * @param orderQty orderQty
     */
    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * Get the kanbanQty.
     *
     * @return kanbanQty
     */
    public String getKanbanQty() {
        return this.kanbanQty;
    }

    /**
     * Set the kanbanQty.
     *
     * @param kanbanQty kanbanQty
     */
    public void setKanbanQty(String kanbanQty) {
        this.kanbanQty = kanbanQty;
    }

    /**
     * Get the digits.
     *
     * @return digits
     */
    public int getDigits() {
        return this.digits;
    }

    /**
     * Set the digits.
     *
     * @param digits digits
     */
    public void setDigits(int digits) {
        this.digits = digits;
    }
}
