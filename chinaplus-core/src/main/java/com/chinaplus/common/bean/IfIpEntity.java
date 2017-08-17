package com.chinaplus.common.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TntIfImpIp;
import com.chinaplus.common.entity.TntIpKanban;

/**
 * The persistent class for the TNT_IP & TNT_IF_IMP_IP database table.
 * 
 * @author shi_xf
 */
public class IfIpEntity extends TntIfImpIp {

    private static final long serialVersionUID = 6062846748069184228L;

    private Integer ipId;

    private Integer businessPattern;

    private Integer decantActionType;

    private Integer decantOriginalPartsId;
    
    private Integer decantOriginalCustId;

    private String decantImpPoNo;

    private String decantCustomerOrderNo;

    private String decantExpPoNo;

    private String decantExpPoItemNo;

    private String decantInvoiceNo;

    private String decantContainerNo;

    private String decantModuleNo;
    
    private Integer partsId;

    private Integer ifPartsId;

    private Integer expPartsId;

    private Integer ifExpPartsId;

    private Integer officeId;

    private Integer ifOfficeId;

    private Integer customerId;

    private Integer ifCustomerId;

    private Integer supplierId;

    private Integer ipOnholdFlag;

    private Integer whsId;

    private Integer ifWhsId;
    
    private Integer transportMode;
    
    private String kanbanPlanNo;

    private String impPoNo;

    private String expPoNo;

    private String expPoItemNo;
    
    private String ipPidNo;

    private int invoiceId;

    private BigDecimal invoiceQty;
    
    private BigDecimal originalQty;

    private Timestamp impStDate;

    private Date expInbPlanDate;
    
    private Date invDevanDate;
    
    private Date ccDate;
    
    private String actualInvoiceNo;
    
    private Date decantCcDate;

    private Date decantDevanDate;

    private Date expObActualDate;

    private Date impInbActualDate;

    private Date impWtDate;

    private Timestamp impSaDate;

    private Date ipOnholdDate;

    private Integer invoiceSummaryId;

    /** impStockFlag */
    private Integer impStockFlag;

    /** ipKanbanList */
    private List<TntIpKanban> ipKanbanList;

    /** expPartsIds */
    private List<Integer> expPartsIds;
    
    /**
     * @return the ipId
     */
    public Integer getIpId() {
        return ipId;
    }

    /**
     * @param ipId the ipId to set
     */
    public void setIpId(Integer ipId) {
        this.ipId = ipId;
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
     * @return the decantActionType
     */
    public Integer getDecantActionType() {
        return decantActionType;
    }

    /**
     * @param decantActionType the decantActionType to set
     */
    public void setDecantActionType(Integer decantActionType) {
        this.decantActionType = decantActionType;
    }

    /**
     * @return the decantOriginalPartsId
     */
    public Integer getDecantOriginalPartsId() {
        return decantOriginalPartsId;
    }

    /**
     * @param decantOriginalPartsId the decantOriginalPartsId to set
     */
    public void setDecantOriginalPartsId(Integer decantOriginalPartsId) {
        this.decantOriginalPartsId = decantOriginalPartsId;
    }

    /**
     * @return the decantImpPoNo
     */
    public String getDecantImpPoNo() {
        return decantImpPoNo;
    }

    /**
     * @param decantImpPoNo the decantImpPoNo to set
     */
    public void setDecantImpPoNo(String decantImpPoNo) {
        this.decantImpPoNo = decantImpPoNo;
    }

    /**
     * @return the decantCustomerOrdrrNo
     */
    public String getDecantCustomerOrderNo() {
        return decantCustomerOrderNo;
    }

    /**
     * @param decantCustomerOrderNo the decantCustomerOrderNo to set
     */
    public void setDecantCustomerOrderNo(String decantCustomerOrderNo) {
        this.decantCustomerOrderNo = decantCustomerOrderNo;
    }

    /**
     * @return the decantExpPoNo
     */
    public String getDecantExpPoNo() {
        return decantExpPoNo;
    }

    /**
     * @param decantExpPoNo the decantExpPoNo to set
     */
    public void setDecantExpPoNo(String decantExpPoNo) {
        this.decantExpPoNo = decantExpPoNo;
    }

    /**
     * @return the decantExpPoItemNo
     */
    public String getDecantExpPoItemNo() {
        return decantExpPoItemNo;
    }

    /**
     * @param decantExpPoItemNo the decantExpPoItemNo to set
     */
    public void setDecantExpPoItemNo(String decantExpPoItemNo) {
        this.decantExpPoItemNo = decantExpPoItemNo;
    }

    /**
     * @return the decantInvoiceNo
     */
    public String getDecantInvoiceNo() {
        return decantInvoiceNo;
    }

    /**
     * @param decantInvoiceNo the decantInvoiceNo to set
     */
    public void setDecantInvoiceNo(String decantInvoiceNo) {
        this.decantInvoiceNo = decantInvoiceNo;
    }

    /**
     * @return the decantContainerNo
     */
    public String getDecantContainerNo() {
        return decantContainerNo;
    }

    /**
     * @param decantContainerNo the decantContainerNo to set
     */
    public void setDecantContainerNo(String decantContainerNo) {
        this.decantContainerNo = decantContainerNo;
    }

    /**
     * @return the decantModuleNo
     */
    public String getDecantModuleNo() {
        return decantModuleNo;
    }

    /**
     * @param decantModuleNo the decantModuleNo to set
     */
    public void setDecantModuleNo(String decantModuleNo) {
        this.decantModuleNo = decantModuleNo;
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
     * @return the ifPartsId
     */
    public Integer getIfPartsId() {
        return ifPartsId;
    }

    /**
     * @param ifPartsId the ifPartsId to set
     */
    public void setIfPartsId(Integer ifPartsId) {
        this.ifPartsId = ifPartsId;
    }

    /**
     * @return the expPartsId
     */
    public Integer getExpPartsId() {
        return expPartsId;
    }

    /**
     * @param expPartsId the expPartsId to set
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
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
     * @return the ifOfficeId
     */
    public Integer getIfOfficeId() {
        return ifOfficeId;
    }

    /**
     * @param ifOfficeId the ifOfficeId to set
     */
    public void setIfOfficeId(Integer ifOfficeId) {
        this.ifOfficeId = ifOfficeId;
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
     * @return the ifCustomerId
     */
    public Integer getIfCustomerId() {
        return ifCustomerId;
    }

    /**
     * @param ifCustomerId the ifCustomerId to set
     */
    public void setIfCustomerId(Integer ifCustomerId) {
        this.ifCustomerId = ifCustomerId;
    }

    /**
     * @return the supplierId
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the ipOnholdFlag
     */
    public Integer getIpOnholdFlag() {
        return ipOnholdFlag;
    }

    /**
     * @param ipOnholdFlag the ipOnholdFlag to set
     */
    public void setIpOnholdFlag(Integer ipOnholdFlag) {
        this.ipOnholdFlag = ipOnholdFlag;
    }

    /**
     * @return the whsId
     */
    public Integer getWhsId() {
        return whsId;
    }

    /**
     * @param whsId the whsId to set
     */
    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
    }

    /**
     * @return the ifWhsId
     */
    public Integer getIfWhsId() {
        return ifWhsId;
    }

    /**
     * @param ifWhsId the ifWhsId to set
     */
    public void setIfWhsId(Integer ifWhsId) {
        this.ifWhsId = ifWhsId;
    }

    /**
     * @return the kanbanPlanNo
     */
    public String getKanbanPlanNo() {
        return kanbanPlanNo;
    }

    /**
     * @param kanbanPlanNo the kanbanPlanNo to set
     */
    public void setKanbanPlanNo(String kanbanPlanNo) {
        this.kanbanPlanNo = kanbanPlanNo;
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
     * @return the expPoItemNo
     */
    public String getExpPoItemNo() {
        return expPoItemNo;
    }

    /**
     * @param expPoItemNo the expPoItemNo to set
     */
    public void setExpPoItemNo(String expPoItemNo) {
        this.expPoItemNo = expPoItemNo;
    }

    /**
     * @return the invoiceId
     */
    public int getInvoiceId() {
        return invoiceId;
    }

    /**
     * @param invoiceId the invoiceId to set
     */
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * @return the invoiceQty
     */
    public BigDecimal getInvoiceQty() {
        return invoiceQty;
    }

    /**
     * @param invoiceQty the invoiceQty to set
     */
    public void setInvoiceQty(BigDecimal invoiceQty) {
        this.invoiceQty = invoiceQty;
    }

    /**
     * @return the impStDate
     */
    public Timestamp getImpStDate() {
        return impStDate;
    }

    /**
     * @param impStDate the impStDate to set
     */
    public void setImpStDate(Timestamp impStDate) {
        this.impStDate = impStDate;
    }

    /**
     * @return the expInbPlanDate
     */
    public Date getExpInbPlanDate() {
        return expInbPlanDate;
    }

    /**
     * @param expInbPlanDate the expInbPlanDate to set
     */
    public void setExpInbPlanDate(Date expInbPlanDate) {
        this.expInbPlanDate = expInbPlanDate;
    }

    /**
     * Get the impStockFlag.
     *
     * @return impStockFlag
     */
    public Integer getImpStockFlag() {
        return this.impStockFlag;
    }

    /**
     * Set the impStockFlag.
     *
     * @param impStockFlag impStockFlag
     */
    public void setImpStockFlag(Integer impStockFlag) {
        this.impStockFlag = impStockFlag;
        
    }

    /**
     * Get the invDevanDate.
     *
     * @return invDevanDate
     */
    public Date getInvDevanDate() {
        return this.invDevanDate;
    }

    /**
     * Set the invDevanDate.
     *
     * @param invDevanDate invDevanDate
     */
    public void setInvDevanDate(Date invDevanDate) {
        this.invDevanDate = invDevanDate;
        
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
     * Get the ifExpPartsId.
     *
     * @return ifExpPartsId
     */
    public Integer getIfExpPartsId() {
        return this.ifExpPartsId;
    }

    /**
     * Set the ifExpPartsId.
     *
     * @param ifExpPartsId ifExpPartsId
     */
    public void setIfExpPartsId(Integer ifExpPartsId) {
        this.ifExpPartsId = ifExpPartsId;
        
    }

    /**
     * Get the decantOriginalCustId.
     *
     * @return decantOriginalCustId
     */
    public Integer getDecantOriginalCustId() {
        return this.decantOriginalCustId;
    }

    /**
     * Set the decantOriginalCustId.
     *
     * @param decantOriginalCustId decantOriginalCustId
     */
    public void setDecantOriginalCustId(Integer decantOriginalCustId) {
        this.decantOriginalCustId = decantOriginalCustId;
        
    }

    /**
     * Get the ipKanbanList.
     *
     * @return ipKanbanList
     */
    public List<TntIpKanban> getIpKanbanList() {
        return this.ipKanbanList;
    }

    /**
     * Set the ipKanbanList.
     *
     * @param ipKanbanList ipKanbanList
     */
    public void setIpKanbanList(List<TntIpKanban> ipKanbanList) {
        this.ipKanbanList = ipKanbanList;
        
    }

    /**
     * Get the expPartsIds.
     *
     * @return expPartsIds
     *
     * @author liu_yinchuan
     */
    public List<Integer> getExpPartsIds() {
        return this.expPartsIds;
    }

    /**
     * Set the expPartsIds.
     *
     * @param expPartsIds expPartsIds
     *
     * @author liu_yinchuan
     */
    public void setExpPartsIds(List<Integer> expPartsIds) {
        this.expPartsIds = expPartsIds;
        
    }

    /**
     * Get the originalQty.
     *
     * @return originalQty
     *
     * @author liu_yinchuan
     */
    public BigDecimal getOriginalQty() {
        return this.originalQty;
    }

    /**
     * Set the originalQty.
     *
     * @param originalQty originalQty
     *
     * @author liu_yinchuan
     */
    public void setOriginalQty(BigDecimal originalQty) {
        this.originalQty = originalQty;
        
    }

    /**
     * Get the ipPidNo.
     *
     * @return ipPidNo
     *
     * @author liu_yinchuan
     */
    public String getIpPidNo() {
        return this.ipPidNo;
    }

    /**
     * Set the ipPidNo.
     *
     * @param ipPidNo ipPidNo
     *
     * @author liu_yinchuan
     */
    public void setIpPidNo(String ipPidNo) {
        this.ipPidNo = ipPidNo;
        
    }

    /**
     * Get the decantCcDate.
     *
     * @return decantCcDate
     *
     * @author liu_yinchuan
     */
    public Date getDecantCcDate() {
        return this.decantCcDate;
    }

    /**
     * Set the decantCcDate.
     *
     * @param decantCcDate decantCcDate
     *
     * @author liu_yinchuan
     */
    public void setDecantCcDate(Date decantCcDate) {
        this.decantCcDate = decantCcDate;
        
    }

    /**
     * Get the decantDevanDate.
     *
     * @return decantDevanDate
     *
     * @author liu_yinchuan
     */
    public Date getDecantDevanDate() {
        return this.decantDevanDate;
    }

    /**
     * Set the decantDevanDate.
     *
     * @param decantDevanDate decantDevanDate
     *
     * @author liu_yinchuan
     */
    public void setDecantDevanDate(Date decantDevanDate) {
        this.decantDevanDate = decantDevanDate;
        
    }

    /**
     * Get the expObActualDate.
     *
     * @return expObActualDate
     *
     * @author liu_yinchuan
     */
    public Date getExpObActualDate() {
        return this.expObActualDate;
    }

    /**
     * Set the expObActualDate.
     *
     * @param expObActualDate expObActualDate
     *
     * @author liu_yinchuan
     */
    public void setExpObActualDate(Date expObActualDate) {
        this.expObActualDate = expObActualDate;
        
    }

    /**
     * Get the impInbActualDate.
     *
     * @return impInbActualDate
     *
     * @author liu_yinchuan
     */
    public Date getImpInbActualDate() {
        return this.impInbActualDate;
    }

    /**
     * Set the impInbActualDate.
     *
     * @param impInbActualDate impInbActualDate
     *
     * @author liu_yinchuan
     */
    public void setImpInbActualDate(Date impInbActualDate) {
        this.impInbActualDate = impInbActualDate;
        
    }

    /**
     * Get the impWtDate.
     *
     * @return impWtDate
     *
     * @author liu_yinchuan
     */
    public Date getImpWtDate() {
        return this.impWtDate;
    }

    /**
     * Set the impWtDate.
     *
     * @param impWtDate impWtDate
     *
     * @author liu_yinchuan
     */
    public void setImpWtDate(Date impWtDate) {
        this.impWtDate = impWtDate;
        
    }

    /**
     * Get the impSaDate.
     *
     * @return impSaDate
     *
     * @author liu_yinchuan
     */
    public Timestamp getImpSaDate() {
        return this.impSaDate;
    }

    /**
     * Set the impSaDate.
     *
     * @param impSaDate impSaDate
     *
     * @author liu_yinchuan
     */
    public void setImpSaDate(Timestamp impSaDate) {
        this.impSaDate = impSaDate;
        
    }

    /**
     * Get the ipOnholdDate.
     *
     * @return ipOnholdDate
     *
     * @author liu_yinchuan
     */
    public Date getIpOnholdDate() {
        return this.ipOnholdDate;
    }

    /**
     * Set the ipOnholdDate.
     *
     * @param ipOnholdDate ipOnholdDate
     *
     * @author liu_yinchuan
     */
    public void setIpOnholdDate(Date ipOnholdDate) {
        this.ipOnholdDate = ipOnholdDate;
        
    }

    /**
     * Get the actualInvoiceNo.
     *
     * @return actualInvoiceNo
     *
     * @author liu_yinchuan
     */
    public String getActualInvoiceNo() {
        return this.actualInvoiceNo;
    }

    /**
     * Set the actualInvoiceNo.
     *
     * @param actualInvoiceNo actualInvoiceNo
     *
     * @author liu_yinchuan
     */
    public void setActualInvoiceNo(String actualInvoiceNo) {
        this.actualInvoiceNo = actualInvoiceNo;
        
    }

    /**
     * Get the transportMode.
     *
     * @return transportMode
     *
     * @author liu_yinchuan
     */
    public Integer getTransportMode() {
        return this.transportMode;
    }

    /**
     * Set the transportMode.
     *
     * @param transportMode transportMode
     *
     * @author liu_yinchuan
     */
    public void setTransportMode(Integer transportMode) {
        this.transportMode = transportMode;
        
    }

    /**
     * Get the invoiceSummaryId.
     *
     * @return invoiceSummaryId
     *
     * @author liu_yinchuan
     */
    public Integer getInvoiceSummaryId() {
        return this.invoiceSummaryId;
    }

    /**
     * Set the invoiceSummaryId.
     *
     * @param invoiceSummaryId invoiceSummaryId
     *
     * @author liu_yinchuan
     */
    public void setInvoiceSummaryId(Integer invoiceSummaryId) {
        this.invoiceSummaryId = invoiceSummaryId;
        
    }

}