/**
 * ReceiveData.java
 * 
 * @screen CPIIFB02
 * @author shi_xf
 */
package com.chinaplus.common.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.BaseInterfaceEntity;

/**
 * 
 * ReceiveData.
 * 
 * @author shi_xf
 */
public class ReceiveData extends BaseInterfaceEntity {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** Office Code */
    private String officeCode;

    /** Business Pattern */
    private Integer businessPattern;

    /** ifIpId */
    private Integer ifIpId;

    /** whsId */
    private Integer whsId;

    /** supplierId */
    private Integer supplierId;

    /** invoiceNo */
    private String invoiceNo;

    /** moduleNo */
    private String moduleNo;

    /** kanbanPlanNo */
    private String kanbanPlanNo;

    /** orderMonth */
    private String orderMonth;

    /** invoiceQty */
    private BigDecimal invoiceQty;

    /** splitQty */
    private BigDecimal splitQty;

    /** inbDate */
    private String inbDate;

    /** dataType */
    private Integer dataType;

    /** inbInvoiceNo */
    private String inbInvoiceNo;

    /** sumQty */
    private BigDecimal sumQty;

    /** invoiceNoList */
    private List<String> invoiceNoList;

    /** processDate */
    private Date processDate;

    /** actualCcDate */
    private Date actualCcDate;

    /** actualInbDate */
    private Date actualInbDate;

    /** devannedDate */
    private Date devannedDate;

    /** devannedDate */
    private Timestamp batchProcessTime;

    /** devannedDate */
    private Timestamp ssmsProcessTime;

    /** devannedDate */
    private Integer ssmsProcessStatus;

    /** match Invoice Id List */
    private List<Integer> matchInvIdList;

    /** matchInvId */
    private Integer matchInvId;

    /** lastBatchStatus */
    private Integer lastBatchStatus;

    /** CONTAINER_NO */
    private String containerNo;

    /**
     * @return the officeCode
     */
    public String getOfficeCode() {
        return officeCode;
    }

    /**
     * @param officeCode the officeCode to set
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
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
     * @return the ifIpId
     */
    public Integer getIfIpId() {
        return ifIpId;
    }

    /**
     * @param ifIpId the ifIpId to set
     */
    public void setIfIpId(Integer ifIpId) {
        this.ifIpId = ifIpId;
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
     * @return the inbDate
     */
    public String getInbDate() {
        return inbDate;
    }

    /**
     * @param inbDate the inbDate to set
     */
    public void setInbDate(String inbDate) {
        this.inbDate = inbDate;
    }

    /**
     * @return the inbInvoiceNo
     */
    public String getInbInvoiceNo() {
        return inbInvoiceNo;
    }

    /**
     * @param inbInvoiceNo the inbInvoiceNo to set
     */
    public void setInbInvoiceNo(String inbInvoiceNo) {
        this.inbInvoiceNo = inbInvoiceNo;
    }

    /**
     * @return the sumQty
     */
    public BigDecimal getSumQty() {
        return sumQty;
    }

    /**
     * @param sumQty the sumQty to set
     */
    public void setSumQty(BigDecimal sumQty) {
        this.sumQty = sumQty;
    }

    /**
     * @return the invoiceNoList
     */
    public List<String> getInvoiceNoList() {
        return invoiceNoList;
    }

    /**
     * @param invoiceNoList the invoiceNoList to set
     */
    public void setInvoiceNoList(List<String> invoiceNoList) {
        this.invoiceNoList = invoiceNoList;
    }

    /**
     * @return the processDate
     */
    public Date getProcessDate() {
        return processDate;
    }

    /**
     * @param processDate the processDate to set
     */
    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    @Override
    public int[] getFieldsPosition() {
        return null;
    }

    @Override
    public String[] getFieldsName() {
        return null;
    }

    @Override
    public int getTotalLength() {
        return 0;
    }

    /**
     * Get the actualCcDate.
     *
     * @return actualCcDate
     */
    public Date getActualCcDate() {
        return this.actualCcDate;
    }

    /**
     * Set the actualCcDate.
     *
     * @param actualCcDate actualCcDate
     */
    public void setActualCcDate(Date actualCcDate) {
        this.actualCcDate = actualCcDate;
        
    }

    /**
     * Get the actualInbDate.
     *
     * @return actualInbDate
     */
    public Date getActualInbDate() {
        return this.actualInbDate;
    }

    /**
     * Set the actualInbDate.
     *
     * @param actualInbDate actualInbDate
     */
    public void setActualInbDate(Date actualInbDate) {
        this.actualInbDate = actualInbDate;
        
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
     * Get the batchProcessTime.
     *
     * @return batchProcessTime
     */
    public Timestamp getBatchProcessTime() {
        return this.batchProcessTime;
    }

    /**
     * Set the batchProcessTime.
     *
     * @param batchProcessTime batchProcessTime
     */
    public void setBatchProcessTime(Timestamp batchProcessTime) {
        this.batchProcessTime = batchProcessTime;
        
    }

    /**
     * Get the ssmsProcessTime.
     *
     * @return ssmsProcessTime
     */
    public Timestamp getSsmsProcessTime() {
        return this.ssmsProcessTime;
    }

    /**
     * Set the ssmsProcessTime.
     *
     * @param ssmsProcessTime ssmsProcessTime
     */
    public void setSsmsProcessTime(Timestamp ssmsProcessTime) {
        this.ssmsProcessTime = ssmsProcessTime;
        
    }

    /**
     * Get the whsId.
     *
     * @return whsId
     */
    public Integer getWhsId() {
        return this.whsId;
    }

    /**
     * Set the whsId.
     *
     * @param whsId whsId
     */
    public void setWhsId(Integer whsId) {
        this.whsId = whsId;
        
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
     * Get the invoiceQty.
     *
     * @return invoiceQty
     */
    public BigDecimal getInvoiceQty() {
        return this.invoiceQty;
    }

    /**
     * Set the invoiceQty.
     *
     * @param invoiceQty invoiceQty
     */
    public void setInvoiceQty(BigDecimal invoiceQty) {
        this.invoiceQty = invoiceQty;
        
    }

    /**
     * Get the splitQty.
     *
     * @return splitQty
     */
    public BigDecimal getSplitQty() {
        return this.splitQty;
    }

    /**
     * Set the splitQty.
     *
     * @param splitQty splitQty
     */
    public void setSplitQty(BigDecimal splitQty) {
        this.splitQty = splitQty;
        
    }

    /**
     * Get the dataType.
     *
     * @return dataType
     */
    public Integer getDataType() {
        return this.dataType;
    }

    /**
     * Set the dataType.
     *
     * @param dataType dataType
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
        
    }

    /**
     * Get the matchInvIdList.
     *
     * @return matchInvIdList
     *
     * @author liu_yinchuan
     */
    public List<Integer> getMatchInvIdList() {
        return this.matchInvIdList;
    }

    /**
     * Set the matchInvIdList.
     *
     * @param matchInvIdList matchInvIdList
     *
     * @author liu_yinchuan
     */
    public void setMatchInvIdList(List<Integer> matchInvIdList) {
        this.matchInvIdList = matchInvIdList;
        
    }

    /**
     * Get the matchInvId.
     *
     * @return matchInvId
     *
     * @author liu_yinchuan
     */
    public Integer getMatchInvId() {
        return this.matchInvId;
    }

    /**
     * Set the matchInvId.
     *
     * @param matchInvId matchInvId
     *
     * @author liu_yinchuan
     */
    public void setMatchInvId(Integer matchInvId) {
        this.matchInvId = matchInvId;
        
    }

    /**
     * Get the lastBatchStatus.
     *
     * @return lastBatchStatus
     *
     * @author liu_yinchuan
     */
    public Integer getLastBatchStatus() {
        return this.lastBatchStatus;
    }

    /**
     * Set the lastBatchStatus.
     *
     * @param lastBatchStatus lastBatchStatus
     *
     * @author liu_yinchuan
     */
    public void setLastBatchStatus(Integer lastBatchStatus) {
        this.lastBatchStatus = lastBatchStatus;
        
    }

    /**
     * Get the ssmsProcessStatus.
     *
     * @return ssmsProcessStatus
     *
     * @author liu_yinchuan
     */
    public Integer getSsmsProcessStatus() {
        return this.ssmsProcessStatus;
    }

    /**
     * Set the ssmsProcessStatus.
     *
     * @param ssmsProcessStatus ssmsProcessStatus
     *
     * @author liu_yinchuan
     */
    public void setSsmsProcessStatus(Integer ssmsProcessStatus) {
        this.ssmsProcessStatus = ssmsProcessStatus;
        
    }

    /**
     * Get the containerNo.
     *
     * @return containerNo
     *
     * @author liu_yinchuan
     */
    public String getContainerNo() {
        return this.containerNo;
    }

    /**
     * Set the containerNo.
     *
     * @param containerNo containerNo
     *
     * @author liu_yinchuan
     */
    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
        
    }
}
