/**
 * Invoice.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.common.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.chinaplus.core.consts.NumberConst.IntDef;

/**
 * 
 * Invoice.
 * @author yang_jia1
 */
public class IFInvoiceEntity extends BaseInterfaceEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    /** ifInvoiceId */
    private Integer ifInvoiceId;

    /** representativeRoot */
    private String representativeRoot;

    /** invoiceNo */
    private String invoiceNo;

    /** blDate */
    private String blDate;

    /** blNo */
    private String blNo;

    /** etdStr */
    private String etdStr;

    /** etaStr */
    private String etaStr;

    /** BLDateTime */
    private Date BLDateTime;

    /** transportMode */
    private String transportMode;

    /** vesselName */
    private String vesselName;

    /** containerNo */
    private String containerNo;

    /** moduleNo */
    private String moduleNo;

    /** officeId */
    private Integer officeId;

    /** businessPattern */
    private Integer businessPattern;

    /** totalQty */
    private BigDecimal totalQty;

    /** invoiceTotalQty */
    private BigDecimal invoiceTotalQty;

    /** originalQty */
    private BigDecimal originalQty ;

    /** qty */
    private BigDecimal qty;

    /** supplierCode */
    private String supplierCode;

    /** impRegionCode */
    private String impRegionCode;

    /** expRegionCode */
    private String expRegionCode;

    /** invoiceSummaryId */
    private Integer invoiceSummaryId;

    /** invoiceId */
    private Integer invoiceId;

    /** impPoNo */
    private String impPoNo;

    /** expPoNo */
    private String expPoNo;

    /** customerOrderNo */
    private String customerOrderNo;

    /** customerId */
    private Integer customerId;

    /** impInbPlanDate */
    private Date impInbPlanDate;

    /** vanningDate */
    private Date vanningDate;

    /** shippingRouteCode */
    private String shippingRouteCode;

    /** impWhsId */
    private Integer impWhsId;

    /** impStockFlag */
    private Integer impStockFlag;

    /** partsIdList */
    private List<Integer> partsIdList;

    /** invoiceIdList */
    private List<Integer> invoiceIdList;
    
    /** uploadId */
    private String uploadId;
    
    /** uploadedBy */
    private Integer uploadedBy;
    
    /** originalVersion */
    private Integer originalVersion;
    
    /** revisionVersion */
    private Integer revisionVersion;
    
    /** invoiceStatus */
    private Integer invoiceStatus;
    
    /** mainRoute */
    private String mainRoute;
    
    /** impInbActualDate */
    private Date impInbActualDate;

    /**
     * @return the ifInvoiceId
     */
    public Integer getIfInvoiceId() {
        return ifInvoiceId;
    }

    /**
     * @param ifInvoiceId the ifInvoiceId to set
     */
    public void setIfInvoiceId(Integer ifInvoiceId) {
        this.ifInvoiceId = ifInvoiceId;
    }

    /**
     * @return the representativeRoot
     */
    public String getRepresentativeRoot() {
        return representativeRoot;
    }

    /**
     * @param representativeRoot the representativeRoot to set
     */
    public void setRepresentativeRoot(String representativeRoot) {
        this.representativeRoot = representativeRoot;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return the blDate
     */
    public String getBlDate() {
        return blDate;
    }

    /**
     * @param blDate the blDate to set
     */
    public void setBlDate(String blDate) {
        this.blDate = blDate;
    }

    /**
     * @return the blNo
     */
    public String getBlNo() {
        return blNo;
    }

    /**
     * @param blNo the blNo to set
     */
    public void setBlNo(String blNo) {
        this.blNo = blNo;
    }

    /**
     * @return the etdStr
     */
    public String getEtdStr() {
        return etdStr;
    }

    /**
     * @param etdStr the etdStr to set
     */
    public void setEtdStr(String etdStr) {
        this.etdStr = etdStr;
    }

    /**
     * @return the etaStr
     */
    public String getEtaStr() {
        return etaStr;
    }

    /**
     * @param etaStr the etaStr to set
     */
    public void setEtaStr(String etaStr) {
        this.etaStr = etaStr;
    }

    /**
     * @return the bLDateTime
     */
    public Date getBLDateTime() {
        return BLDateTime;
    }

    /**
     * @param bLDateTime the bLDateTime to set
     */
    public void setBLDateTime(Date bLDateTime) {
        BLDateTime = bLDateTime;
    }

    /**
     * @return the transportMode
     */
    public String getTransportMode() {
        return transportMode;
    }

    /**
     * @param transportMode the transportMode to set
     */
    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    /**
     * @return the vesselName
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * @param vesselName the vesselName to set
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    /**
     * @return the containerNo
     */
    public String getContainerNo() {
        return containerNo;
    }

    /**
     * @param containerNo the containerNo to set
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    /**
     * @return the moduleNo
     */
    public String getModuleNo() {
        return moduleNo;
    }

    /**
     * @param moduleNo the moduleNo to set
     */
    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    /**
     * @return the officeId
     */
    public Integer getOfficeId() {
        return officeId;
    }

    /**
     * @param officeId the officeId to set
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * @return the businessPattern
     */
    public Integer getBusinessPattern() {
        return businessPattern;
    }

    /**
     * @param businessPattern the businessPattern to set
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }

    /**
     * @return the totalQty
     */
    public BigDecimal getTotalQty() {
        return totalQty;
    }

    /**
     * @param totalQty the totalQty to set
     */
    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    /**
     * @return the invoiceTotalQty
     */
    public BigDecimal getInvoiceTotalQty() {
        return invoiceTotalQty;
    }

    /**
     * @param invoiceTotalQty the invoiceTotalQty to set
     */
    public void setInvoiceTotalQty(BigDecimal invoiceTotalQty) {
        this.invoiceTotalQty = invoiceTotalQty;
    }

    /**
     * @return the originalQty
     */
    public BigDecimal getOriginalQty() {
        return originalQty;
    }

    /**
     * @param originalQty the originalQty to set
     */
    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
    }

    /**
     * @return the qty
     */
    public BigDecimal getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    /**
     * @return the supplierCode
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     * @param supplierCode the supplierCode to set
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * @return the impRegionCode
     */
    public String getImpRegionCode() {
        return impRegionCode;
    }

    /**
     * @param impRegionCode the impRegionCode to set
     */
    public void setImpRegionCode(String impRegionCode) {
        this.impRegionCode = impRegionCode;
    }

    /**
     * @return the expRegionCode
     */
    public String getExpRegionCode() {
        return expRegionCode;
    }

    /**
     * @param expRegionCode the expRegionCode to set
     */
    public void setExpRegionCode(String expRegionCode) {
        this.expRegionCode = expRegionCode;
    }

    /**
     * @return the invoiceSummaryId
     */
    public Integer getInvoiceSummaryId() {
        return invoiceSummaryId;
    }

    /**
     * @param invoiceSummaryId the invoiceSummaryId to set
     */
    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
    }

    /**
     * @return the invoiceId
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the impPoNo
     */
    public String getImpPoNo() {
        return impPoNo;
    }

    /**
     * @param impPoNo the impPoNo to set
     */
    public void setImpPoNo(String impPoNo) {
        this.impPoNo = impPoNo;
    }

    /**
     * @return the expPoNo
     */
    public String getExpPoNo() {
        return expPoNo;
    }

    /**
     * @param expPoNo the expPoNo to set
     */
    public void setExpPoNo(String expPoNo) {
        this.expPoNo = expPoNo;
    }

    /**
     * @return the customerOrderNo
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * @param customerOrderNo the customerOrderNo to set
     */
    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the impInbPlanDate
     */
    public Date getImpInbPlanDate() {
        return impInbPlanDate;
    }

    /**
     * @param impInbPlanDate the impInbPlanDate to set
     */
    public void setImpInbPlanDate(Date impInbPlanDate) {
        this.impInbPlanDate = impInbPlanDate;
    }

    /**
     * @return the vanningDate
     */
    public Date getVanningDate() {
        return vanningDate;
    }

    /**
     * @param vanningDate the vanningDate to set
     */
    public void setVanningDate(Date vanningDate) {
        this.vanningDate = vanningDate;
    }

    /**
     * @return the shippingRouteCode
     */
    public String getShippingRouteCode() {
        return shippingRouteCode;
    }

    /**
     * @param shippingRouteCode the shippingRouteCode to set
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    /**
     * @return the impWhsId
     */
    public Integer getImpWhsId() {
        return impWhsId;
    }

    /**
     * @param impWhsId the impWhsId to set
     */
    public void setImpWhsId(Integer impWhsId) {
        this.impWhsId = impWhsId;
    }

    /**
     * @return the impStockFlag
     */
    public Integer getImpStockFlag() {
        return impStockFlag;
    }

    /**
     * @param impStockFlag the impStockFlag to set
     */
    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;
    }

    /**
     * @return the partsIdList
     */
    public List<Integer> getPartsIdList() {
        return partsIdList;
    }

    /**
     * @param partsIdList the partsIdList to set
     */
    public void setPartsIdList(List<Integer> partsIdList) {
        this.partsIdList = partsIdList;
    }

    /**
     * @return the invoiceIdList
     */
    public List<Integer> getInvoiceIdList() {
        return invoiceIdList;
    }

    /**
     * @param invoiceIdList the invoiceIdList to set
     */
    public void setInvoiceIdList(List<Integer> invoiceIdList) {
        this.invoiceIdList = invoiceIdList;
    }

    /**
     * @return the originalVersion
     */
    public Integer getOriginalVersion() {
        return originalVersion;
    }

    /**
     * @param originalVersion the originalVersion to set
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * @return the revisionVersion
     */
    public Integer getRevisionVersion() {
        return revisionVersion;
    }

    /**
     * @param revisionVersion the revisionVersion to set
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    /**
     * @return the uploadId
     */
    public String getUploadId() {
        return uploadId;
    }

    /**
     * @param uploadId the uploadId to set
     */
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    /**
     * @return the uploadedBy
     */
    public Integer getUploadedBy() {
        return uploadedBy;
    }

    /**
     * @param uploadedBy the uploadedBy to set
     */
    public void setUploadedBy(Integer uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    /**
     * @return the invoiceStatus
     */
    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    /**
     * @param invoiceStatus the invoiceStatus to set
     */
    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    /**
     * @return the mainRoute
     */
    public String getMainRoute() {
        return mainRoute;
    }

    /**
     * @param mainRoute the mainRoute to set
     */
    public void setMainRoute(String mainRoute) {
        this.mainRoute = mainRoute;
    }

    /**
     * @return the impInbActualDate
     */
    public Date getImpInbActualDate() {
        return impInbActualDate;
    }

    /**
     * @param impInbActualDate the impInbActualDate to set
     */
    public void setImpInbActualDate(Date impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
    }

    @Override
    public int[] getFieldsPosition() {
        int[] filedsPi = { IntDef.INT_ONE, IntDef.INT_TWO, IntDef.INT_TWO, IntDef.INT_TEN, IntDef.INT_EIGHT,
            IntDef.INT_TWENTY, IntDef.INT_EIGHT, IntDef.INT_EIGHT, IntDef.INT_ONE, IntDef.INT_THIRTY,
            IntDef.INT_TWENTY, IntDef.INT_TWENTY };
        return filedsPi;
    }

    @Override
    public String[] getFieldsName() {
        String[] filedsNm = { "dataId", "expCode", "representativeRoot", "invoiceNo", "blDate", "blNo", "etdStr",
            "etaStr", "transportMode", "vesselName", "containerNo", "moduleNo"};
        return filedsNm;
    }

    @Override
    public int getTotalLength() {
        return IntDef.INT_ONE_HUNDRED_AND_THIRTY;
    }
}
