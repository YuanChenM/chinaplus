/**
 * CPVIVS01AdjustEntity.java
 * 
 * @screen CPVIVS01
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaplus.core.base.BaseEntity;

/**
 * Invoice Screen Adjust Entity.
 */
public class CPVIVS01AdjustEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** Latest KANBAN ID */
    private Integer latestKanbanId;

    /** Invoice No. */
    private String invoiceNo;

    /** KANBAN ID */
    private Integer kanbanId;

    /** KANBAN Plan No. */
    private String kanbanPlanNo;

    /** Order Month */
    private String orderMonth;

    /** KANBAN Shipping ID */
    private Integer kanbanShippingId;

    /** Shipping UUID */
    private String shippingUUID;

    /** Original Version */
    private Integer originalVersion;

    /** Revision Version */
    private Integer revisionVersion;

    /** Transport Mode */
    private Integer transportMode;

    /** Completed Flag */
    private Integer completedFlag;

    /** NIRD Flag */
    private Integer nirdFlag;

    /** ETD */
    private Date etd;

    /** ETA */
    private Date eta;

    /** Inbound Plan Date */
    private Date inbPlanDate;

    /** Difference Box ID */
    private Integer differBoxId;

    /** Dummy Box ID */
    private Integer dummyBoxId;

    /** Parts ID */
    private Integer partsId;

    /** Invoice Qty */
    private BigDecimal invoiceQty;

    /** Plan Qty */
    private BigDecimal planQty;

    /** SPQ */
    private BigDecimal spq;

    /** Latest SPQ */
    private BigDecimal latestSpq;

    /** Part Status */
    private Integer partStatus;

    /** Shipping Route Code */
    private String shippingRouteCode;

    /** Exp Parts ID */
    private Integer expPartsId;

    /** Part Qty Map */
    private Map<Integer, BigDecimal> partQtyMap;

    /** Plan Part Qty Map */
    private Map<Integer, BigDecimal[]> planQtyMap;

    /** Invoice Summary ID */
    private List<String> summaryIds;

    /** System Time */
    private Timestamp systemTime;

    /** Login User Id */
    private Integer loginUserId;

    /** Need Adjust Flag */
    private boolean needAdjust;

    /**
     * Get the latestKanbanId.
     *
     * @return latestKanbanId
     */
    public Integer getLatestKanbanId() {
        return this.latestKanbanId;
    }

    /**
     * Set the latestKanbanId.
     *
     * @param latestKanbanId latestKanbanId
     */
    public void setLatestKanbanId(Integer latestKanbanId) {
        this.latestKanbanId = latestKanbanId;
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
     * Get the kanbanShippingId.
     *
     * @return kanbanShippingId
     */
    public Integer getKanbanShippingId() {
        return this.kanbanShippingId;
    }

    /**
     * Set the kanbanShippingId.
     *
     * @param kanbanShippingId kanbanShippingId
     */
    public void setKanbanShippingId(Integer kanbanShippingId) {
        this.kanbanShippingId = kanbanShippingId;
    }

    /**
     * Get the shippingUUID.
     *
     * @return shippingUUID
     */
    public String getShippingUUID() {
        return this.shippingUUID;
    }

    /**
     * Set the shippingUUID.
     *
     * @param shippingUUID shippingUUID
     */
    public void setShippingUUID(String shippingUUID) {
        this.shippingUUID = shippingUUID;
    }

    /**
     * Get the originalVersion.
     *
     * @return originalVersion
     */
    public Integer getOriginalVersion() {
        return this.originalVersion;
    }

    /**
     * Set the originalVersion.
     *
     * @param originalVersion originalVersion
     */
    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    /**
     * Get the revisionVersion.
     *
     * @return revisionVersion
     */
    public Integer getRevisionVersion() {
        return this.revisionVersion;
    }

    /**
     * Set the revisionVersion.
     *
     * @param revisionVersion revisionVersion
     */
    public void setRevisionVersion(Integer revisionVersion) {
        this.revisionVersion = revisionVersion;
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
     * Get the completedFlag.
     *
     * @return completedFlag
     */
    public Integer getCompletedFlag() {
        return this.completedFlag;
    }

    /**
     * Set the completedFlag.
     *
     * @param completedFlag completedFlag
     */
    public void setCompletedFlag(Integer completedFlag) {
        this.completedFlag = completedFlag;
    }

    /**
     * Get the nirdFlag.
     *
     * @return nirdFlag
     */
    public Integer getNirdFlag() {
        return this.nirdFlag;
    }

    /**
     * Set the nirdFlag.
     *
     * @param nirdFlag nirdFlag
     */
    public void setNirdFlag(Integer nirdFlag) {
        this.nirdFlag = nirdFlag;
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
     * Get the inbPlanDate.
     *
     * @return inbPlanDate
     */
    public Date getInbPlanDate() {
        return this.inbPlanDate;
    }

    /**
     * Set the inbPlanDate.
     *
     * @param inbPlanDate inbPlanDate
     */
    public void setInbPlanDate(Date inbPlanDate) {
        this.inbPlanDate = inbPlanDate;
    }

    /**
     * Get the differBoxId.
     *
     * @return differBoxId
     */
    public Integer getDifferBoxId() {
        return this.differBoxId;
    }

    /**
     * Set the differBoxId.
     *
     * @param differBoxId differBoxId
     */
    public void setDifferBoxId(Integer differBoxId) {
        this.differBoxId = differBoxId;
    }

    /**
     * Get the dummyBoxId.
     *
     * @return dummyBoxId
     */
    public Integer getDummyBoxId() {
        return this.dummyBoxId;
    }

    /**
     * Set the dummyBoxId.
     *
     * @param dummyBoxId dummyBoxId
     */
    public void setDummyBoxId(Integer dummyBoxId) {
        this.dummyBoxId = dummyBoxId;
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
     * Get the planQty.
     *
     * @return planQty
     */
    public BigDecimal getPlanQty() {
        return this.planQty;
    }

    /**
     * Set the planQty.
     *
     * @param planQty planQty
     */
    public void setPlanQty(BigDecimal planQty) {
        this.planQty = planQty;
    }

    /**
     * Get the spq.
     *
     * @return spq
     */
    public BigDecimal getSpq() {
        return this.spq;
    }

    /**
     * Set the spq.
     *
     * @param spq spq
     */
    public void setSpq(BigDecimal spq) {
        this.spq = spq;
    }

    /**
     * Get the latestSpq.
     *
     * @return latestSpq
     */
    public BigDecimal getLatestSpq() {
        return this.latestSpq;
    }

    /**
     * Set the latestSpq.
     *
     * @param latestSpq latestSpq
     */
    public void setLatestSpq(BigDecimal latestSpq) {
        this.latestSpq = latestSpq;
    }

    /**
     * Get the partStatus.
     *
     * @return partStatus
     */
    public Integer getPartStatus() {
        return this.partStatus;
    }

    /**
     * Set the partStatus.
     *
     * @param partStatus partStatus
     */
    public void setPartStatus(Integer partStatus) {
        this.partStatus = partStatus;
    }

    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    /**
     * Get the expPartsId.
     *
     * @return expPartsId
     */
    public Integer getExpPartsId() {
        return this.expPartsId;
    }

    /**
     * Set the expPartsId.
     *
     * @param expPartsId expPartsId
     */
    public void setExpPartsId(Integer expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * Get the partQtyMap.
     *
     * @return partQtyMap
     */
    public Map<Integer, BigDecimal> getPartQtyMap() {
        return this.partQtyMap;
    }

    /**
     * Set the partQtyMap.
     *
     * @param partQtyMap partQtyMap
     */
    public void setPartQtyMap(Map<Integer, BigDecimal> partQtyMap) {
        this.partQtyMap = partQtyMap;
    }

    /**
     * Get the planQtyMap.
     *
     * @return planQtyMap
     */
    public Map<Integer, BigDecimal[]> getPlanQtyMap() {
        return this.planQtyMap;
    }

    /**
     * Set the planQtyMap.
     *
     * @param planQtyMap planQtyMap
     */
    public void setPlanQtyMap(Map<Integer, BigDecimal[]> planQtyMap) {
        this.planQtyMap = planQtyMap;
    }

    /**
     * Get the summaryIds.
     *
     * @return summaryIds
     */
    public List<String> getSummaryIds() {
        return this.summaryIds;
    }

    /**
     * Set the summaryIds.
     *
     * @param summaryIds summaryIds
     */
    public void setSummaryIds(List<String> summaryIds) {
        this.summaryIds = summaryIds;
    }

    /**
     * Get the systemTime.
     *
     * @return systemTime
     */
    public Timestamp getSystemTime() {
        return this.systemTime;
    }

    /**
     * Set the systemTime.
     *
     * @param systemTime systemTime
     */
    public void setSystemTime(Timestamp systemTime) {
        this.systemTime = systemTime;
    }

    /**
     * Get the loginUserId.
     *
     * @return loginUserId
     */
    public Integer getLoginUserId() {
        return this.loginUserId;
    }

    /**
     * Set the loginUserId.
     *
     * @param loginUserId loginUserId
     */
    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    /**
     * Get the needAdjust.
     *
     * @return needAdjust
     */
    public boolean isNeedAdjust() {
        return this.needAdjust;
    }

    /**
     * Set the needAdjust.
     *
     * @param needAdjust needAdjust
     */
    public void setNeedAdjust(boolean needAdjust) {
        this.needAdjust = needAdjust;
    }

}
