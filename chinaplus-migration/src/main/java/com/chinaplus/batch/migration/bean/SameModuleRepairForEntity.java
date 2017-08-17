package com.chinaplus.batch.migration.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.chinaplus.core.base.BaseEntity;

/**
 * The persistent class for the TNT_IF_IMP_IP database table.
 * 
 */
public class SameModuleRepairForEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mainRoute;

    private Integer ipId;

    private String invoiceNo;

    private List<String> moduleNoList;

    private BigDecimal qty;

    private String transportMode;

    private List<String> invoiceNoList;

    private Integer officeId;

    private Integer invoiceDetailId;

    private Integer createdBy;

    private Timestamp createdDate;

    private String customerOrderNo;

    private String expPoNo;

    private String impPoNo;

    private String invCustCode;

    private BigDecimal originalQty;

    private Integer partsId;

    private Integer supplierId;

    private String supplierPartsNo;

    private Integer updatedBy;

    private Timestamp updatedDate;

    private Integer version;

    private Integer invoiceId;

    private Integer invoiceContainerId;

    private String buyingCurrency;

    private String buyingPrice;

    private String containerNo;

    private String invoicePartsNo;

    private String moduleNo;

    private Date ccDate;

    private Date devannedDate;

    private Integer status;

    private String sealNo;

    private Integer invoiceSummaryId;

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
     * Get the mainRoute.
     *
     * @return mainRoute
     */
    public String getMainRoute() {
        return this.mainRoute;
    }

    /**
     * Set the mainRoute.
     *
     * @param mainRoute mainRoute
     */
    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
    }

    /**
     * Get the ipId.
     *
     * @return ipId
     */
    public Integer getIpId() {
        return this.ipId;
    }

    /**
     * Set the ipId.
     *
     * @param ipId ipId
     */
    public void setIpId(Integer ipId) {
        this.ipId = ipId;
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
     * Get the moduleNoList.
     *
     * @return moduleNoList
     */
    public List<String> getModuleNoList() {
        return this.moduleNoList;
    }

    /**
     * Set the moduleNoList.
     *
     * @param moduleNoList moduleNoList
     */
    public void setModuleNoList(List<String> moduleNoList) {
        this.moduleNoList = moduleNoList;
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
     * Get the invoiceNoList.
     *
     * @return invoiceNoList
     */
    public List<String> getInvoiceNoList() {
        return this.invoiceNoList;
    }

    /**
     * Set the invoiceNoList.
     *
     * @param invoiceNoList invoiceNoList
     */
    public void setInvoiceNoList(List<String> invoiceNoList) {
        this.invoiceNoList = invoiceNoList;
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
     * Get the createdBy.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     *
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the customerOrderNo.
     *
     * @return customerOrderNo
     */
    public String getCustomerOrderNo() {
        return this.customerOrderNo;
    }

    /**
     * Set the customerOrderNo.
     *
     * @param customerOrderNo customerOrderNo
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * Get the expPoNo.
     *
     * @return expPoNo
     */
    public String getExpPoNo() {
        return this.expPoNo;
    }

    /**
     * Set the expPoNo.
     *
     * @param expPoNo expPoNo
     */
    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    /**
     * Get the impPoNo.
     *
     * @return impPoNo
     */
    public String getImpPoNo() {
        return this.impPoNo;
    }

    /**
     * Set the impPoNo.
     *
     * @param impPoNo impPoNo
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
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
     * Get the originalQty.
     *
     * @return originalQty
     */
    public BigDecimal getOriginalQty() {
        return this.originalQty;
    }

    /**
     * Set the originalQty.
     *
     * @param originalQty originalQty
     */
    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
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
     * Get the supplierPartsNo.
     *
     * @return supplierPartsNo
     */
    public String getSupplierPartsNo() {
        return this.supplierPartsNo;
    }

    /**
     * Set the supplierPartsNo.
     *
     * @param supplierPartsNo supplierPartsNo
     */
    public void setSupplierPartsNo(String supplierPartsNo) {
        this.supplierPartsNo = supplierPartsNo;
    }

    /**
     * Get the updatedBy.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     *
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
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
     * Get the invoiceContainerId.
     *
     * @return invoiceContainerId
     */
    public Integer getInvoiceContainerId() {
        return this.invoiceContainerId;
    }

    /**
     * Set the invoiceContainerId.
     *
     * @param invoiceContainerId invoiceContainerId
     */
    public void setInvoiceContainerId(Integer invoiceContainerId) {
        this.invoiceContainerId = invoiceContainerId;
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
     * Get the invoicePartsNo.
     *
     * @return invoicePartsNo
     */
    public String getInvoicePartsNo() {
        return this.invoicePartsNo;
    }

    /**
     * Set the invoicePartsNo.
     *
     * @param invoicePartsNo invoicePartsNo
     */
    public void setInvoicePartsNo(String invoicePartsNo) {
        this.invoicePartsNo = invoicePartsNo;
    }

    /**
     * Get the ccDate.
     *
     * @return ccDate
     */
    public Date getCcDate() {
        return this.ccDate;
    }

    /**
     * Set the ccDate.
     *
     * @param ccDate ccDate
     */
    public void setCcDate(Date ccDate) {
        this.ccDate = ccDate;
    }

    /**
     * Get the devannedDate.
     *
     * @return devannedDate
     */
    public Date getDevannedDate() {
        return this.devannedDate;
    }

    /**
     * Set the devannedDate.
     *
     * @param devannedDate devannedDate
     */
    public void setDevannedDate(Date devannedDate) {
        this.devannedDate = devannedDate;
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Get the sealNo.
     *
     * @return sealNo
     */
    public String getSealNo() {
        return this.sealNo;
    }

    /**
     * Set the sealNo.
     *
     * @param sealNo sealNo
     */
    public void setSealNo(String sealNo) {
        this.sealNo = sealNo;
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
}