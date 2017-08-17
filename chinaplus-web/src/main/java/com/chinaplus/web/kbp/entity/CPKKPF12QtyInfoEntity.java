/**
 * CPKKPF12QtyInfoEntity.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.entity;

import java.math.BigDecimal;

import com.chinaplus.core.base.BaseEntity;

/**
 * Revised Kanban Plan Upload Entity.
 */
public class CPKKPF12QtyInfoEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Kanban Id */
    private Integer kanbanId;

    /** Order Qty */
    private BigDecimal orderQty;

    /** On Shipping Qty */
    private BigDecimal onShippingQty;

    /** Inbound Qty */
    private BigDecimal inboundQty;

    /** Order Balance */
    private BigDecimal orderBalance;

    /**
     * Get the kanbanId.
     *
     * @return kanbanId
     */
    public Integer getKanbanId() {
        return this.kanbanId;
    }

    /**
     * Set the kanbanId.
     *
     * @param kanbanId kanbanId
     */
    public void setKanbanId(Integer kanbanId) {
        this.kanbanId = kanbanId;
    }

    /**
     * Get the orderQty.
     *
     * @return orderQty
     */
    public BigDecimal getOrderQty() {
        return this.orderQty;
    }

    /**
     * Set the orderQty.
     *
     * @param orderQty orderQty
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * Get the onShippingQty.
     *
     * @return onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return this.onShippingQty;
    }

    /**
     * Set the onShippingQty.
     *
     * @param onShippingQty onShippingQty
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
    }

    /**
     * Get the inboundQty.
     *
     * @return inboundQty
     */
    public BigDecimal getInboundQty() {
        return this.inboundQty;
    }

    /**
     * Set the inboundQty.
     *
     * @param inboundQty inboundQty
     */
    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    /**
     * Get the orderBalance.
     *
     * @return orderBalance
     */
    public BigDecimal getOrderBalance() {
        return this.orderBalance;
    }

    /**
     * Set the orderBalance.
     *
     * @param orderBalance orderBalance
     */
    public void setOrderBalance(BigDecimal orderBalance) {
        this.orderBalance = orderBalance;
    }
}
