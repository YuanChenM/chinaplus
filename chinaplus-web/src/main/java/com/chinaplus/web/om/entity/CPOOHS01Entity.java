/**
 * CPOOHS01Entity.java
 * 
 * @screen CPOOHS01
 * @author Xiang_chao
 */
package com.chinaplus.web.om.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPOOHS01Entity.
 */
public class CPOOHS01Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** orderStatusId. */
    private Integer orderStatusId;

    /** orderId. */
    private Integer orderId;

    /** ttcPartstNo. */
    private String ttcPartsNo;

    /** impPONo. */
    private String impPONo;

    /** expPONo. */
    private String expPONo;

    /** customerOrderNo. */
    private String customerOrderNo;

    /** expSODate. */
    private Timestamp expSODate;

    /** expRegion. */
    private String expRegion;

    /** impRegion. */
    private String impRegion;

    /** ttcSuppCode. */
    private String ttcSuppCode;

    /** ttcCusCode. */
    private String ttcCusCode;

    /** transportMode. */
    private Integer transportMode;

    /** orderQty. */
    private BigDecimal orderQty;

    /** expBalanceOrder. */
    private BigDecimal expBalanceOrder;

    /** expWHStock. */
    private BigDecimal expWHStock;

    /** expOutboundQty. */
    private BigDecimal expOutboundQty;

    /** onShippingQty. */
    private BigDecimal onShippingQty;

    /** inboundQty. */
    private BigDecimal inboundQty;

    /** impOrderBalance. */
    private BigDecimal impOrderBalance;

    /** customerBalance. */
    private BigDecimal customerBalance;

    /** stockTransferOut. */
    private BigDecimal stockTransferOut;
    
    /** stockTransferOut. */
    private BigDecimal cancelledQty;

    /** transferToDetails. */
    private String transferToDetails;

    /** transferToDetails. */
    private String transferFrom;

    /** transferToCode. */
    private String transferToCode;

    /** transferToQty. */
    private BigDecimal transferToQty;

    /** transferFromCode. */
    private String transferFromCode;

    /** disOrderStatus. */
    private Integer disOrderStatus;

    /** partsId. */
    private Integer partsId;

    /** partsNameEN. */
    private String partsNameEN;

    /** partsNameCN. */
    private String partsNameCN;

    /** custBackNo. */
    private String custBackNo;

    /** custPartsNo. */
    private String custPartsNo;

    /** suppPartsNo. */
    private String suppPartsNo;

    /** businessType. */
    private Integer businessType;

    /** impStockQty. */
    private BigDecimal impStockQty;

    /** impECIQty. */
    private BigDecimal impECIQty;

    /** availableImpStock. */
    private BigDecimal availableImpStock;

    /** impPrepareOBQty. */
    private BigDecimal impPrepareOBQty;

    /** impNGQty. */
    private BigDecimal impNGQty;

    /** TotalSmBalanceQty. */
    private BigDecimal TotalSmBalanceQty;

    /** impDeliveredQty. */
    private BigDecimal impDeliveredQty;

    /** impAdjustedQty. */
    private BigDecimal impAdjustedQty;

    /** uom code */
    private String uomCode;

    /**
     * @return the orderStatusId
     */
    public Integer getOrderStatusId() {
        return orderStatusId;
    }

    /**
     * @param orderStatusId
     *        the orderStatusId to set
     */
    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    /**
     * @return the orderId
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     *        the orderId to set
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the ttcPartstNo
     */
    public String getTtcPartsNo() {
        return ttcPartsNo;
    }

    /**
     * @param ttcPartsNo
     *        the ttcPartsNo to set
     */
    public void setTtcPartsNo(String ttcPartsNo) {
        this.ttcPartsNo = ttcPartsNo;
    }

    /**
     * @return the impPONo
     */
    public String getImpPONo() {
        return impPONo;
    }

    /**
     * @param impPONo
     *        the impPONo to set
     */
    public void setImpPONo(String impPONo) {
        this.impPONo = impPONo;
    }

    /**
     * @return the expPONo
     */
    public String getExpPONo() {
        return expPONo;
    }

    /**
     * @param expPONo
     *        the expPONo to set
     */
    public void setExpPONo(String expPONo) {
        this.expPONo = expPONo;
    }

    /**
     * @return the customerOrderNo
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * @param customerOrderNo
     *        the customerOrderNo to set
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * @return the expSODate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Timestamp getExpSODate() {
        return expSODate;
    }

    /**
     * @param expSODate
     *        the expSODate to set
     */
    public void setExpSODate(Timestamp expSODate) {
        this.expSODate = expSODate;
    }

    /**
     * @return the expRegion
     */
    public String getExpRegion() {
        return expRegion;
    }

    /**
     * @param expRegion
     *        the expRegion to set
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
    }

    /**
     * @return the impRegion
     */
    public String getImpRegion() {
        return impRegion;
    }

    /**
     * @param impRegion
     *        the impRegion to set
     */
    public void setImpRegion(String impRegion) {
        this.impRegion = impRegion;
    }

    /**
     * @return the ttcSuppCode
     */
    public String getTtcSuppCode() {
        return ttcSuppCode;
    }

    /**
     * @param ttcSuppCode
     *        the ttcSuppCode to set
     */
    public void setTtcSuppCode(String ttcSuppCode) {
        this.ttcSuppCode = ttcSuppCode;
    }

    /**
     * @return the ttcCusCode
     */
    public String getTtcCusCode() {
        return ttcCusCode;
    }

    /**
     * @param ttcCusCode
     *        the ttcCusCode to set
     */
    public void setTtcCusCode(String ttcCusCode) {
        this.ttcCusCode = ttcCusCode;
    }

    /**
     * @return the transportMode
     */
    public Integer getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode
     *        the transportMode to set
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return the orderQty
     */
    public BigDecimal getOrderQty() {
        return orderQty;
    }

    /**
     * @param orderQty
     *        the orderQty to set
     */
    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * @return the expBalanceOrder
     */
    public BigDecimal getExpBalanceOrder() {
        return expBalanceOrder;
    }

    /**
     * @param expBalanceOrder
     *        the expBalanceOrder to set
     */
    public void setExpBalanceOrder(BigDecimal expBalanceOrder) {
        this.expBalanceOrder = expBalanceOrder;
    }

    /**
     * @return the expWHStock
     */
    public BigDecimal getExpWHStock() {
        return expWHStock;
    }

    /**
     * @param expWHStock
     *        the expWHStock to set
     */
    public void setExpWHStock(BigDecimal expWHStock) {
        this.expWHStock = expWHStock;
    }

    /**
     * Get the expOutboundQty.
     *
     * @return expOutboundQty
     */
    public BigDecimal getExpOutboundQty() {
        return this.expOutboundQty;
    }

    /**
     * Set the expOutboundQty.
     *
     * @param expOutboundQty expOutboundQty
     */
    public void setExpOutboundQty(BigDecimal expOutboundQty) {
        this.expOutboundQty = expOutboundQty;
    }

    /**
     * @return the onShippingQty
     */
    public BigDecimal getOnShippingQty() {
        return onShippingQty;
    }

    /**
     * @param onShippingQty
     *        the onShippingQty to set
     */
    public void setOnShippingQty(BigDecimal onShippingQty) {
        this.onShippingQty = onShippingQty;
    }

    /**
     * @return the inboundQty
     */
    public BigDecimal getInboundQty() {
        return inboundQty;
    }

    /**
     * @param inboundQty
     *        the inboundQty to set
     */
    public void setInboundQty(BigDecimal inboundQty) {
        this.inboundQty = inboundQty;
    }

    /**
     * @return the importOrderBalance
     */
    public BigDecimal getImpOrderBalance() {
        return impOrderBalance;
    }

    /**
     * @param importOrderBalance
     *        the importOrderBalance to set
     */
    public void setImpOrderBalance(BigDecimal importOrderBalance) {
        impOrderBalance = importOrderBalance;
    }

    /**
     * @return the customerBalance
     */
    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    /**
     * @param customerBalance
     *        the customerBalance to set
     */
    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    /**
     * @return the stockTransferOut
     */
    public BigDecimal getStockTransferOut() {
        return stockTransferOut;
    }

    /**
     * @param stockTransferOut
     *        the stockTransferOut to set
     */
    public void setStockTransferOut(BigDecimal stockTransferOut) {
        this.stockTransferOut = stockTransferOut;
    }

    /**
     * @return the transferToDetails
     */
    public String getTransferToDetails() {
        return transferToDetails;
    }

    /**
     * @param transferToDetails
     *        the transferToDetails to set
     */
    public void setTransferToDetails(String transferToDetails) {
        this.transferToDetails = transferToDetails;
    }

    /**
     * @return the transferFrom
     */
    public String getTransferFrom() {
        return transferFrom;
    }

    /**
     * @param transferFrom
     *        the transferFrom to set
     */
    public void setTransferFrom(String transferFrom) {
        this.transferFrom = transferFrom;
    }

    /**
     * @return the transferToCode
     */
    public String getTransferToCode() {
        return transferToCode;
    }

    /**
     * @param transferToCode
     *        the transferToCode to set
     */
    public void setTransferToCode(String transferToCode) {
        this.transferToCode = transferToCode;
    }

    /**
     * @return the transferToQty
     */
    public BigDecimal getTransferToQty() {
        return transferToQty;
    }

    /**
     * @param transferToQty
     *        the transferToQty to set
     */
    public void setTransferToQty(BigDecimal transferToQty) {
        this.transferToQty = transferToQty;
    }

    /**
     * @return the transferFromCode
     */
    public String getTransferFromCode() {
        return transferFromCode;
    }

    /**
     * @param transferFromCode
     *        the transferFromCode to set
     */
    public void setTransferFromCode(String transferFromCode) {
        this.transferFromCode = transferFromCode;
    }

    /**
     * @return the disOrderStatus
     */
    public Integer getDisOrderStatus() {
        return disOrderStatus;
    }

    /**
     * @param disOrderStatus
     *        the disOrderStatus to set
     */
    public void setDisOrderStatus(Integer disOrderStatus) {
        this.disOrderStatus = disOrderStatus;
    }

    /**
     * @return the partsId
     */
    public Integer getPartsId() {
        return partsId;
    }

    /**
     * @param partsId the partsId to set
     */
    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    /**
     * @return the partsNameEN
     */
    public String getPartsNameEN() {
        return partsNameEN;
    }

    /**
     * @param partsNameEN
     *        the partsNameEN to set
     */
    public void setPartsNameEN(String partsNameEN) {
        this.partsNameEN = partsNameEN;
    }

    /**
     * @return the partsNameCN
     */
    public String getPartsNameCN() {
        return partsNameCN;
    }

    /**
     * @param partsNameCN
     *        the partsNameCN to set
     */
    public void setPartsNameCN(String partsNameCN) {
        this.partsNameCN = partsNameCN;
    }

    /**
     * @return the custBackNo
     */
    public String getCustBackNo() {
        return custBackNo;
    }

    /**
     * @param custBackNo
     *        the custBackNo to set
     */
    public void setCustBackNo(String custBackNo) {
        this.custBackNo = custBackNo;
    }

    /**
     * @return the custPartsNo
     */
    public String getCustPartsNo() {
        return custPartsNo;
    }

    /**
     * @param custPartsNo
     *        the custPartsNo to set
     */
    public void setCustPartsNo(String custPartsNo) {
        this.custPartsNo = custPartsNo;
    }

    /**
     * @return the suppPartsNo
     */
    public String getSuppPartsNo() {
        return suppPartsNo;
    }

    /**
     * @param suppPartsNo
     *        the suppPartsNo to set
     */
    public void setSuppPartsNo(String suppPartsNo) {
        this.suppPartsNo = suppPartsNo;
    }

    /**
     * @return the businessType
     */
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * @param businessType
     *        the businessType to set
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * @return the impStockQty
     */
    public BigDecimal getImpStockQty() {
        return impStockQty;
    }

    /**
     * @param impStockQty
     *        the impStockQty to set
     */
    public void setImpStockQty(BigDecimal impStockQty) {
        this.impStockQty = impStockQty;
    }

    /**
     * @return the impECIQty
     */
    public BigDecimal getImpECIQty() {
        return impECIQty;
    }

    /**
     * @param impECIQty
     *        the impECIQty to set
     */
    public void setImpECIQty(BigDecimal impECIQty) {
        this.impECIQty = impECIQty;
    }

    /**
     * @return the availableImpStock
     */
    public BigDecimal getAvailableImpStock() {
        return availableImpStock;
    }

    /**
     * @param availableImpStock
     *        the availableImpStock to set
     */
    public void setAvailableImpStock(BigDecimal availableImpStock) {
        this.availableImpStock = availableImpStock;
    }

    /**
     * @return the impPrepareOBQty
     */
    public BigDecimal getImpPrepareOBQty() {
        return impPrepareOBQty;
    }

    /**
     * @param impPrepareOBQty
     *        the impPrepareOBQty to set
     */
    public void setImpPrepareOBQty(BigDecimal impPrepareOBQty) {
        this.impPrepareOBQty = impPrepareOBQty;
    }

    /**
     * @return the impNGQty
     */
    public BigDecimal getImpNGQty() {
        return impNGQty;
    }

    /**
     * @param impNGQty
     *        the impNGQty to set
     */
    public void setImpNGQty(BigDecimal impNGQty) {
        this.impNGQty = impNGQty;
    }

    /**
     * @return the totalSmBalanceQty
     */
    public BigDecimal getTotalSmBalanceQty() {
        return TotalSmBalanceQty;
    }

    /**
     * @param totalSmBalanceQty
     *        the totalSmBalanceQty to set
     */
    public void setTotalSmBalanceQty(BigDecimal totalSmBalanceQty) {
        TotalSmBalanceQty = totalSmBalanceQty;
    }

    /**
     * @return the impDeliveredQty
     */
    public BigDecimal getImpDeliveredQty() {
        return impDeliveredQty;
    }

    /**
     * @param impDeliveredQty
     *        the impDeliveredQty to set
     */
    public void setImpDeliveredQty(BigDecimal impDeliveredQty) {
        this.impDeliveredQty = impDeliveredQty;
    }

    /**
     * @return the impAdjustedQty
     */
    public BigDecimal getImpAdjustedQty() {
        return impAdjustedQty;
    }

    /**
     * @param impAdjustedQty
     *        the impAdjustedQty to set
     */
    public void setImpAdjustedQty(BigDecimal impAdjustedQty) {
        this.impAdjustedQty = impAdjustedQty;
    }

    /**
     * @return the uomCode
     */
    public String getUomCode() {
        return uomCode;
    }

    /**
     * @param uomCode the uomCode to set
     */
    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    /**
     * Get the cancelledQty.
     *
     * @return cancelledQty
     */
    public BigDecimal getCancelledQty() {
        return this.cancelledQty;
    }

    /**
     * Set the cancelledQty.
     *
     * @param cancelledQty cancelledQty
     */
    public void setCancelledQty(BigDecimal cancelledQty) {
        this.cancelledQty = cancelledQty;
        
    }

}
