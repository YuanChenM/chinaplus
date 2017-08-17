package com.chinaplus.batch.migration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.chinaplus.batch.common.bean.BaseCsvFileEntity;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class TntMgInvoiceEx extends BaseCsvFileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer mgInvoiceId;

    private String indicator;
    
    private Integer seqNo;
    
    private String invoiceNo;
    
    private String suppPartsNo;
    
    private String invCustCode;

    private String supplierCode;
    
    private Date issuedDate;
    
    private Date deliveredDate;
    
    private Date etd;
    
    private Date eta;
    
    private Date planIbDate;
    
    private BigDecimal qty;
    
    private Integer transportMode;
    
    private String transportModeCode;
    
    private String vesselName;
    
    private String containerNo;
    
    private String moduleNo;
    
    private String buyingPrice;
    
    private String buyingCurrency;
    
    private String expRegion;

    private String impRegion;
    
    private String kanbanPlanNo;
    
    private Integer officeId;
    
    private Integer partsId;
    
    private Integer supplierId;
    
    private Integer orderStatusId;
    
    private BigDecimal expOnShippingQty;
    
    private String orderMonth;

    /**
     * Get the mgInvoiceId.
     *
     * @return mgInvoiceId
     */
    public Integer getMgInvoiceId() {
        return this.mgInvoiceId;
    }

    /**
     * Set the mgInvoiceId.
     *
     * @param mgInvoiceId mgInvoiceId
     */
    public void setMgInvoiceId(Integer mgInvoiceId) {
        this.mgInvoiceId = mgInvoiceId;
        
    }

    /**
     * Get the seqNo.
     *
     * @return seqNo
     */
    public Integer getSeqNo() {
        return this.seqNo;
    }

    /**
     * Set the seqNo.
     *
     * @param seqNo seqNo
     */
    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
        
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
     * Get the issuedDate.
     *
     * @return issuedDate
     */
    public Date getIssuedDate() {
        return this.issuedDate;
    }

    /**
     * Set the issuedDate.
     *
     * @param issuedDate issuedDate
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
        
    }

    /**
     * Get the deliveredDate.
     *
     * @return deliveredDate
     */
    public Date getDeliveredDate() {
        return this.deliveredDate;
    }

    /**
     * Set the deliveredDate.
     *
     * @param deliveredDate deliveredDate
     */
    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
        
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
     * Get the planIbDate.
     *
     * @return planIbDate
     */
    public Date getPlanIbDate() {
        return this.planIbDate;
    }

    /**
     * Set the planIbDate.
     *
     * @param planIbDate planIbDate
     */
    public void setPlanIbDate(Date planIbDate) {
        this.planIbDate = planIbDate;
        
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
     * Get the transportMode.
     *
     * @return transportMode
     */
    public Integer getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
        
    }

    /**
     * Get the transportModeCode.
     *
     * @return transportModeCode
     */
    public String getTransportModeCode() {
        return this.transportModeCode;
    }

    /**
     * Set the transportModeCode.
     *
     * @param transportModeCode transportModeCode
     */
    public void setTransportModeCode(String transportModeCode) {
        this.transportModeCode = transportModeCode;
        
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
     * Get the containerNo.
     *
     * @return containerNo
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        
    }

    /**
     * Get the moduleNo.
     *
     * @return moduleNo
     */
    public String getModuleNo() {
        return this.moduleNo;
    }

    /**
     * Set the moduleNo.
     *
     * @param moduleNo moduleNo
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
        
    }

    /**
     * Get the buyingPrice.
     *
     * @return buyingPrice
     */
    public String getBuyingPrice() {
        return this.buyingPrice;
    }

    /**
     * Set the buyingPrice.
     *
     * @param buyingPrice buyingPrice
     */
    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
        
    }

    /**
     * Get the buyingCurrency.
     *
     * @return buyingCurrency
     */
    public String getBuyingCurrency() {
        return this.buyingCurrency;
    }

    /**
     * Set the buyingCurrency.
     *
     * @param buyingCurrency buyingCurrency
     */
    public void setBuyingCurrency(String buyingCurrency) {
        this.buyingCurrency = buyingCurrency;
        
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
     * Get the expOnShippingQty.
     *
     * @return expOnShippingQty
     */
    public BigDecimal getExpOnShippingQty() {
        return this.expOnShippingQty;
    }

    /**
     * Set the expOnShippingQty.
     *
     * @param expOnShippingQty expOnShippingQty
     */
    public void setExpOnShippingQty(BigDecimal expOnShippingQty) {
        this.expOnShippingQty = expOnShippingQty;
        
    }

    /**
     * Get the orderStatusId.
     *
     * @return orderStatusId
     */
    public Integer getOrderStatusId() {
        return this.orderStatusId;
    }

    /**
     * Set the orderStatusId.
     *
     * @param orderStatusId orderStatusId
     */
    public void setOrderStatusId(Integer orderStatusId) {
        this.orderStatusId = orderStatusId;
        
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
     * Get the indicator.
     *
     * @return indicator
     */
    public String getIndicator() {
        return this.indicator;
    }

    /**
     * Set the indicator.
     *
     * @param indicator indicator
     */
    public void setIndicator(String indicator) {
        this.indicator = indicator;
        
    }

    
}