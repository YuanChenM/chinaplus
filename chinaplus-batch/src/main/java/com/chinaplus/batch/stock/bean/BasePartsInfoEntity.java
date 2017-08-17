/**
 * Base parts information entity.
 * 
 * @screen Run-Down Batch
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntNotInRundown;
import com.chinaplus.common.entity.TntRdAttachCfc;

/**
 * Base parts information entity.
 * 
 * @author liu_yinchuan
 */
public class BasePartsInfoEntity extends TnmPartsMaster {

    /** serialVersionUID. */
    private static final long serialVersionUID = 737386502624524774L;

    /** officeCode. */
    private String officeCode;

    /** supplier id. */
    private Integer supplierId;

    /** supplier id. */
    private Integer onlineFlag;
    
    /** stockDate. */
    private Date stockDate;
    
    /** officeTime. */
    private Timestamp officeTime;
    
    /** lastEndStockDate. */
    private Date lastEndStockDate;
    
    /** firstDayOfLastMonth. */
    private Date firstDayOfLastMonth;
    
    /** lastDayOfLastMonth. */
    private Date lastDayOfLastMonth;
    
    /** firstShareDate. */
    private Date firstShareDate;
    
    /** lastShareDate. */
    private Date lastShareDate;
    
    /** cfcMonth. */
    private String cfcMonth;
    
    /** effectOrderMonth. */
    private String effectOrderMonth;
    
    /** Exp Balance Order. */
    private BigDecimal expBalanceQty;
    
    /** Exp W/H Stock. */
    private BigDecimal expWhsStock;
    
    /** On-Shipping Stock. */
    private BigDecimal onShippingStock;
    
    /** In Rack Qty. */
    private BigDecimal inRackQty;
    
    /** Imp W/H Available Qty. */
    private BigDecimal impWhsAvailableQty;
    
    /** warehouse stock qty. */
    private BigDecimal whsStockQty;
    
    /** Imp On Hold qty. */
    private BigDecimal ngQty;
    
    /** ECI on Hold qty. */
    private BigDecimal onHoldQty;
    
    /** Already Prepared for Imp Outbound qty. */
    private BigDecimal preparedOutboundQty;
    
    /** In Rack Qty. */
    private BigDecimal etdDelayQty;

    /** In Rack Qty. */
    private BigDecimal inbDelayQty;

    /** Customer Stock Upload End Date. */
    private Date customerStockDate;
    
    /** Customer Stock. */
    private BigDecimal customerStockQty;
    
    /** The latest order month. */
    private String lastOrderMonth;

    /** The last input of Customer Usage Date. */
    private Date endCfcDate;
    
    /** endOFCDate. */
    private Date endOFCDate;
    
    /** The endPlanDate. */
    private Date endPlanDate;
    
    /** The endPlanDate. */
    private Date endOrderPlanDate;
    
    /** The endPlanDate. */
    private Date runEndDate;
    
    /** The last order month of forecast. */
    private String fcOrderMonth;
    
    /** Imp Stock Flag. */
    private Integer impStockFlag;
    
    /** Delivered To Customer for last month. */
    private BigDecimal lastDeliveryQty;
    
    /** TotalOFCQty. */
    private BigDecimal totalOFCQty;
    
    /** officeCalendarList. */
    private List<TnmCalendarDetailEx> officeCalendarList;

    /** CustCalendarList. */
    private List<TnmCalendarDetailEx> custCalendarList;
    
    /** daily usage list. */  
    private List<TntCfcDay> dailyUsageList;
    
    /** shareInfoList. */  
    private List<TntRdAttachCfc> shareInfoList;
    
    /** planDetailList. */  
    private List<TntRdDetailAttachEx> planDetailList;
    
    /** notInRundownList. */  
    private List<TntNotInRundown> notInRundownList;
    
    /** lastTgMonthDate. */  
    private Date lastTgMonthDate;
    
    /** lastFC1Date. */  
    private Date lastFC1Date;
    
    /** lastFC2Date. */  
    private Date lastFC2Date;
    
    /** lastFC3Date. */  
    private Date lastFC3Date;
    
    /** lastFC4Date. */  
    private Date lastFC4Date;
    
    /** lastFC5Date. */  
    private Date lastFC5Date;
    
    /** lastFC6Date. */  
    private Date lastFC6Date;
    
    /** lastFC6Date. */  
    private String timeZone;
    
    /** userId. */  
    private Integer userId;
    
    /** userId. */  
    private Integer lang;
    
    /** userId. */  
    private Integer rdType;
    
    /** mailAddr. */  
    private String mailAddr;
    
    /** Count of mail alert 1. */  
    private Integer alert1Cnt;
    
    /** Count of mail alert 2. */  
    private Integer alert2Cnt;
    
    /** Count of mail alert 3. */  
    private Integer alert3Cnt;
    
    /** Count of mail alert 3. */  
    private String clintTime;
    
    /** Count of mail alert 3. */  
    private String tempFilePath;

    /** rundownRemark. */  
    private String rundownRemark;
    
    /**
     * Get the officeCode.
     *
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     *
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
        
    }

    /**
     * Get the stockDate.
     *
     * @return stockDate
     */
    public Date getStockDate() {
        return this.stockDate;
    }

    /**
     * Set the stockDate.
     *
     * @param stockDate stockDate
     */
    public void setStockDate(Date stockDate) {
        this.stockDate = stockDate;
        
    }

    /**
     * Get the expBalanceQty.
     *
     * @return expBalanceQty
     */
    public BigDecimal getExpBalanceQty() {
        return this.expBalanceQty;
    }

    /**
     * Set the expBalanceQty.
     *
     * @param expBalanceQty expBalanceQty
     */
    public void setExpBalanceQty(BigDecimal expBalanceQty) {
        this.expBalanceQty = expBalanceQty;
        
    }

    /**
     * Get the expWhsStock.
     *
     * @return expWhsStock
     */
    public BigDecimal getExpWhsStock() {
        return this.expWhsStock;
    }

    /**
     * Set the expWhsStock.
     *
     * @param expWhsStock expWhsStock
     */
    public void setExpWhsStock(BigDecimal expWhsStock) {
        this.expWhsStock = expWhsStock;
        
    }

    /**
     * Get the onShippingStock.
     *
     * @return onShippingStock
     */
    public BigDecimal getOnShippingStock() {
        return this.onShippingStock;
    }

    /**
     * Set the onShippingStock.
     *
     * @param onShippingStock onShippingStock
     */
    public void setOnShippingStock(BigDecimal onShippingStock) {
        this.onShippingStock = onShippingStock;
        
    }

    /**
     * Get the inRackQty.
     *
     * @return inRackQty
     */
    public BigDecimal getInRackQty() {
        return this.inRackQty;
    }

    /**
     * Set the inRackQty.
     *
     * @param inRackQty inRackQty
     */
    public void setInRackQty(BigDecimal inRackQty) {
        this.inRackQty = inRackQty;
        
    }

    /**
     * Get the impWhsAvailableQty.
     *
     * @return impWhsAvailableQty
     */
    public BigDecimal getImpWhsAvailableQty() {
        return this.impWhsAvailableQty;
    }

    /**
     * Set the impWhsAvailableQty.
     *
     * @param impWhsAvailableQty impWhsAvailableQty
     */
    public void setImpWhsAvailableQty(BigDecimal impWhsAvailableQty) {
        this.impWhsAvailableQty = impWhsAvailableQty;
        
    }

    /**
     * Get the whsStockQty.
     *
     * @return whsStockQty
     */
    public BigDecimal getWhsStockQty() {
        return this.whsStockQty;
    }

    /**
     * Set the whsStockQty.
     *
     * @param whsStockQty whsStockQty
     */
    public void setWhsStockQty(BigDecimal whsStockQty) {
        this.whsStockQty = whsStockQty;
        
    }

    /**
     * Get the ngQty.
     *
     * @return ngQty
     */
    public BigDecimal getNgQty() {
        return this.ngQty;
    }

    /**
     * Set the ngQty.
     *
     * @param ngQty ngQty
     */
    public void setNgQty(BigDecimal ngQty) {
        this.ngQty = ngQty;
        
    }

    /**
     * Get the onHoldQty.
     *
     * @return onHoldQty
     */
    public BigDecimal getOnHoldQty() {
        return this.onHoldQty;
    }

    /**
     * Set the onHoldQty.
     *
     * @param onHoldQty onHoldQty
     */
    public void setOnHoldQty(BigDecimal onHoldQty) {
        this.onHoldQty = onHoldQty;
        
    }

    /**
     * Get the preparedOutboundQty.
     *
     * @return preparedOutboundQty
     */
    public BigDecimal getPreparedOutboundQty() {
        return this.preparedOutboundQty;
    }

    /**
     * Set the preparedOutboundQty.
     *
     * @param preparedOutboundQty preparedOutboundQty
     */
    public void setPreparedOutboundQty(BigDecimal preparedOutboundQty) {
        this.preparedOutboundQty = preparedOutboundQty;
        
    }

    /**
     * Get the customerStockDate.
     *
     * @return customerStockDate
     */
    public Date getCustomerStockDate() {
        return this.customerStockDate;
    }

    /**
     * Set the customerStockDate.
     *
     * @param customerStockDate customerStockDate
     */
    public void setCustomerStockDate(Date customerStockDate) {
        this.customerStockDate = customerStockDate;
        
    }

    /**
     * Get the customerStockQty.
     *
     * @return customerStockQty
     */
    public BigDecimal getCustomerStockQty() {
        return this.customerStockQty;
    }

    /**
     * Set the customerStockQty.
     *
     * @param customerStockQty customerStockQty
     */
    public void setCustomerStockQty(BigDecimal customerStockQty) {
        this.customerStockQty = customerStockQty;
        
    }

    /**
     * Get the lastOrderMonth.
     *
     * @return lastOrderMonth
     */
    public String getLastOrderMonth() {
        return this.lastOrderMonth;
    }

    /**
     * Set the lastOrderMonth.
     *
     * @param lastOrderMonth lastOrderMonth
     */
    public void setLastOrderMonth(String lastOrderMonth) {
        this.lastOrderMonth = lastOrderMonth;
        
    }

    /**
     * Get the endCfcDate.
     *
     * @return endCfcDate
     */
    public Date getEndCfcDate() {
        return this.endCfcDate;
    }

    /**
     * Set the endCfcDate.
     *
     * @param endCfcDate endCfcDate
     */
    public void setEndCfcDate(Date endCfcDate) {
        this.endCfcDate = endCfcDate;
        
    }

    /**
     * Get the fcOrderMonth.
     *
     * @return fcOrderMonth
     */
    public String getFcOrderMonth() {
        return this.fcOrderMonth;
    }

    /**
     * Set the fcOrderMonth.
     *
     * @param fcOrderMonth fcOrderMonth
     */
    public void setFcOrderMonth(String fcOrderMonth) {
        this.fcOrderMonth = fcOrderMonth;
        
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
     * Get the lastDeliveryQty.
     *
     * @return lastDeliveryQty
     */
    public BigDecimal getLastDeliveryQty() {
        return this.lastDeliveryQty;
    }

    /**
     * Set the lastDeliveryQty.
     *
     * @param lastDeliveryQty lastDeliveryQty
     */
    public void setLastDeliveryQty(BigDecimal lastDeliveryQty) {
        this.lastDeliveryQty = lastDeliveryQty;
        
    }

    /**
     * Get the lastEndStockDate.
     *
     * @return lastEndStockDate
     */
    public Date getLastEndStockDate() {
        return this.lastEndStockDate;
    }

    /**
     * Set the lastEndStockDate.
     *
     * @param lastEndStockDate lastEndStockDate
     */
    public void setLastEndStockDate(Date lastEndStockDate) {
        this.lastEndStockDate = lastEndStockDate;
        
    }

    /**
     * Get the firstDayOfLastMonth.
     *
     * @return firstDayOfLastMonth
     */
    public Date getFirstDayOfLastMonth() {
        return this.firstDayOfLastMonth;
    }

    /**
     * Set the firstDayOfLastMonth.
     *
     * @param firstDayOfLastMonth firstDayOfLastMonth
     */
    public void setFirstDayOfLastMonth(Date firstDayOfLastMonth) {
        this.firstDayOfLastMonth = firstDayOfLastMonth;
        
    }

    /**
     * Get the lastDayOfLastMonth.
     *
     * @return lastDayOfLastMonth
     */
    public Date getLastDayOfLastMonth() {
        return this.lastDayOfLastMonth;
    }

    /**
     * Set the lastDayOfLastMonth.
     *
     * @param lastDayOfLastMonth lastDayOfLastMonth
     */
    public void setLastDayOfLastMonth(Date lastDayOfLastMonth) {
        this.lastDayOfLastMonth = lastDayOfLastMonth;
        
    }

    /**
     * Get the firstShareDate.
     *
     * @return firstShareDate
     */
    public Date getFirstShareDate() {
        return this.firstShareDate;
    }

    /**
     * Set the firstShareDate.
     *
     * @param firstShareDate firstShareDate
     */
    public void setFirstShareDate(Date firstShareDate) {
        this.firstShareDate = firstShareDate;
        
    }

    /**
     * Get the lastShareDate.
     *
     * @return lastShareDate
     */
    public Date getLastShareDate() {
        return this.lastShareDate;
    }

    /**
     * Set the lastShareDate.
     *
     * @param lastShareDate lastShareDate
     */
    public void setLastShareDate(Date lastShareDate) {
        this.lastShareDate = lastShareDate;
        
    }

    /**
     * Get the cfcMonth.
     *
     * @return cfcMonth
     */
    public String getCfcMonth() {
        return this.cfcMonth;
    }

    /**
     * Set the cfcMonth.
     *
     * @param cfcMonth cfcMonth
     */
    public void setCfcMonth(String cfcMonth) {
        this.cfcMonth = cfcMonth;
        
    }

    /**
     * Get the effectOrderMonth.
     *
     * @return effectOrderMonth
     */
    public String getEffectOrderMonth() {
        return this.effectOrderMonth;
    }

    /**
     * Set the effectOrderMonth.
     *
     * @param effectOrderMonth effectOrderMonth
     */
    public void setEffectOrderMonth(String effectOrderMonth) {
        this.effectOrderMonth = effectOrderMonth;
        
    }

    /**
     * Get the officeCalendarList.
     *
     * @return officeCalendarList
     */
    public List<TnmCalendarDetailEx> getOfficeCalendarList() {
        return this.officeCalendarList;
    }

    /**
     * Set the officeCalendarList.
     *
     * @param officeCalendarList officeCalendarList
     */
    public void setOfficeCalendarList(List<TnmCalendarDetailEx> officeCalendarList) {
        this.officeCalendarList = officeCalendarList;
        
    }

    /**
     * Get the endOFCDate.
     *
     * @return endOFCDate
     */
    public Date getEndOFCDate() {
        return this.endOFCDate;
    }

    /**
     * Set the endOFCDate.
     *
     * @param endOFCDate endOFCDate
     */
    public void setEndOFCDate(Date endOFCDate) {
        this.endOFCDate = endOFCDate;
        
    }

    /**
     * Get the totalOFCQty.
     *
     * @return totalOFCQty
     */
    public BigDecimal getTotalOFCQty() {
        return this.totalOFCQty;
    }

    /**
     * Set the totalOFCQty.
     *
     * @param totalOFCQty totalOFCQty
     */
    public void setTotalOFCQty(BigDecimal totalOFCQty) {
        this.totalOFCQty = totalOFCQty;
        
    }

    /**
     * Get the dailyUsageList.
     *
     * @return dailyUsageList
     */
    public List<TntCfcDay> getDailyUsageList() {
        return this.dailyUsageList;
    }

    /**
     * Set the dailyUsageList.
     *
     * @param dailyUsageList dailyUsageList
     */
    public void setDailyUsageList(List<TntCfcDay> dailyUsageList) {
        this.dailyUsageList = dailyUsageList;
        
    }

    /**
     * Get the shareInfoList.
     *
     * @return shareInfoList
     */
    public List<TntRdAttachCfc> getShareInfoList() {
        return this.shareInfoList;
    }

    /**
     * Set the shareInfoList.
     *
     * @param shareInfoList shareInfoList
     */
    public void setShareInfoList(List<TntRdAttachCfc> shareInfoList) {
        this.shareInfoList = shareInfoList;
        
    }

    /**
     * Get the custCalendarList.
     *
     * @return custCalendarList
     */
    public List<TnmCalendarDetailEx> getCustCalendarList() {
        return this.custCalendarList;
    }

    /**
     * Set the custCalendarList.
     *
     * @param custCalendarList custCalendarList
     */
    public void setCustCalendarList(List<TnmCalendarDetailEx> custCalendarList) {
        this.custCalendarList = custCalendarList;
        
    }

    /**
     * Get the notInRundownList.
     *
     * @return notInRundownList
     */
    public List<TntNotInRundown> getNotInRundownList() {
        return this.notInRundownList;
    }

    /**
     * Set the notInRundownList.
     *
     * @param notInRundownList notInRundownList
     */
    public void setNotInRundownList(List<TntNotInRundown> notInRundownList) {
        this.notInRundownList = notInRundownList;
        
    }

    /**
     * Set the notInRundownList.
     *
     * @param notInRundownList notInRundownList
     */
    public void addNotInRundownList(List<TntNotInRundown> notInRundownList) {
        if (this.getNotInRundownList() == null) {
            this.setNotInRundownList(new ArrayList<TntNotInRundown>());
        }
        this.getNotInRundownList().addAll(notInRundownList);

    }

    /**
     * Set the notInRundownList.
     *
     * @param notInRundown notInRundown
     */
    public void addNotInRundown(TntNotInRundown notInRundown) {
        if(this.getNotInRundownList() == null) {
            this.setNotInRundownList(new ArrayList<TntNotInRundown>());
        }
        this.getNotInRundownList().add(notInRundown);
        
    }
    
    /**
     * Get the lastTgMonthDate.
     *
     * @return lastTgMonthDate
     */
    public Date getLastTgMonthDate() {
        return this.lastTgMonthDate;
    }

    /**
     * Set the lastTgMonthDate.
     *
     * @param lastTgMonthDate lastTgMonthDate
     */
    public void setLastTgMonthDate(Date lastTgMonthDate) {
        this.lastTgMonthDate = lastTgMonthDate;
        
    }

    /**
     * Get the lastFC1Date.
     *
     * @return lastFC1Date
     */
    public Date getLastFC1Date() {
        return this.lastFC1Date;
    }

    /**
     * Set the lastFC1Date.
     *
     * @param lastFC1Date lastFC1Date
     */
    public void setLastFC1Date(Date lastFC1Date) {
        this.lastFC1Date = lastFC1Date;
        
    }

    /**
     * Get the lastFC2Date.
     *
     * @return lastFC2Date
     */
    public Date getLastFC2Date() {
        return this.lastFC2Date;
    }

    /**
     * Set the lastFC2Date.
     *
     * @param lastFC2Date lastFC2Date
     */
    public void setLastFC2Date(Date lastFC2Date) {
        this.lastFC2Date = lastFC2Date;
        
    }

    /**
     * Get the lastFC3Date.
     *
     * @return lastFC3Date
     */
    public Date getLastFC3Date() {
        return this.lastFC3Date;
    }

    /**
     * Set the lastFC3Date.
     *
     * @param lastFC3Date lastFC3Date
     */
    public void setLastFC3Date(Date lastFC3Date) {
        this.lastFC3Date = lastFC3Date;
        
    }

    /**
     * Get the lastFC4Date.
     *
     * @return lastFC4Date
     */
    public Date getLastFC4Date() {
        return this.lastFC4Date;
    }

    /**
     * Set the lastFC4Date.
     *
     * @param lastFC4Date lastFC4Date
     */
    public void setLastFC4Date(Date lastFC4Date) {
        this.lastFC4Date = lastFC4Date;
        
    }

    /**
     * Get the lastFC5Date.
     *
     * @return lastFC5Date
     */
    public Date getLastFC5Date() {
        return this.lastFC5Date;
    }

    /**
     * Set the lastFC5Date.
     *
     * @param lastFC5Date lastFC5Date
     */
    public void setLastFC5Date(Date lastFC5Date) {
        this.lastFC5Date = lastFC5Date;
        
    }

    /**
     * Get the lastFC6Date.
     *
     * @return lastFC6Date
     */
    public Date getLastFC6Date() {
        return this.lastFC6Date;
    }

    /**
     * Set the lastFC6Date.
     *
     * @param lastFC6Date lastFC6Date
     */
    public void setLastFC6Date(Date lastFC6Date) {
        this.lastFC6Date = lastFC6Date;
        
    }

    /**
     * Get the planDetailList.
     *
     * @return planDetailList
     */
    public List<TntRdDetailAttachEx> getPlanDetailList() {
        return this.planDetailList;
    }

    /**
     * Set the planDetailList.
     *
     * @param planDetailList planDetailList
     */
    public void setPlanDetailList(List<TntRdDetailAttachEx> planDetailList) {
        this.planDetailList = planDetailList;
        
    }

    /**
     * Set the planDetailList.
     *
     * @param planDetailList planDetailList
     */
    public void addPlanDetailList(List<TntRdDetailAttachEx> planDetailList) {
        if(this.getPlanDetailList() == null) {
            this.setPlanDetailList(new ArrayList<TntRdDetailAttachEx>());
        }
        this.getPlanDetailList().addAll(planDetailList);
    }

    /**
     * Set the notInRundownList.
     *
     * @param planDetail planDetail
     */
    public void addPlanDetail(TntRdDetailAttachEx planDetail) {
        if(this.getPlanDetailList() == null) {
            this.setPlanDetailList(new ArrayList<TntRdDetailAttachEx>());
        }
        this.getPlanDetailList().add(planDetail);
        
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
     * Get the endPlanDate.
     *
     * @return endPlanDate
     */
    public Date getEndPlanDate() {
        return this.endPlanDate;
    }

    /**
     * Set the endPlanDate.
     *
     * @param endPlanDate endPlanDate
     */
    public void setEndPlanDate(Date endPlanDate) {
        this.endPlanDate = endPlanDate;
        
    }

    /**
     * Get the userId.
     *
     * @return userId
     */
    public Integer getUserId() {
        return this.userId;
    }

    /**
     * Set the userId.
     *
     * @param userId userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
        
    }

    /**
     * Get the mailAddr.
     *
     * @return mailAddr
     */
    public String getMailAddr() {
        return this.mailAddr;
    }

    /**
     * Set the mailAddr.
     *
     * @param mailAddr mailAddr
     */
    public void setMailAddr(String mailAddr) {
        this.mailAddr = mailAddr;
        
    }

    /**
     * Get the alert1Cnt.
     *
     * @return alert1Cnt
     */
    public Integer getAlert1Cnt() {
        return this.alert1Cnt;
    }

    /**
     * Set the alert1Cnt.
     *
     * @param alert1Cnt alert1Cnt
     */
    public void setAlert1Cnt(Integer alert1Cnt) {
        this.alert1Cnt = alert1Cnt;
        
    }

    /**
     * Get the alert2Cnt.
     *
     * @return alert2Cnt
     */
    public Integer getAlert2Cnt() {
        return this.alert2Cnt;
    }

    /**
     * Set the alert2Cnt.
     *
     * @param alert2Cnt alert2Cnt
     */
    public void setAlert2Cnt(Integer alert2Cnt) {
        this.alert2Cnt = alert2Cnt;
        
    }

    /**
     * Get the alert3Cnt.
     *
     * @return alert3Cnt
     */
    public Integer getAlert3Cnt() {
        return this.alert3Cnt;
    }

    /**
     * Set the alert3Cnt.
     *
     * @param alert3Cnt alert3Cnt
     */
    public void setAlert3Cnt(Integer alert3Cnt) {
        this.alert3Cnt = alert3Cnt;
        
    }

    /**
     * Get the language.
     *
     * @return language
     */
    public Integer getLang() {
        return this.lang;
    }

    /**
     * Set the language.
     *
     * @param lang language
     */
    public void setLang(Integer lang) {
        this.lang = lang;
        
    }

    /**
     * Get the timeZone.
     *
     * @return timeZone
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * Set the timeZone.
     *
     * @param timeZone timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        
    }

    /**
     * Get the clintTime.
     *
     * @return clintTime
     */
    public String getClintTime() {
        return this.clintTime;
    }

    /**
     * Set the clintTime.
     *
     * @param clintTime clintTime
     */
    public void setClintTime(String clintTime) {
        this.clintTime = clintTime;
        
    }

    /**
     * Get the rdType.
     *
     * @return rdType
     */
    public Integer getRdType() {
        return this.rdType;
    }

    /**
     * Set the rdType.
     *
     * @param rdType rdType
     */
    public void setRdType(Integer rdType) {
        this.rdType = rdType;
        
    }

    /**
     * Get the officeTime.
     *
     * @return officeTime
     */
    public Timestamp getOfficeTime() {
        return this.officeTime;
    }

    /**
     * Set the officeTime.
     *
     * @param officeTime officeTime
     */
    public void setOfficeTime(Timestamp officeTime) {
        this.officeTime = officeTime;
        
    }

    /**
     * Get the tempFilePath.
     *
     * @return tempFilePath
     */
    public String getTempFilePath() {
        return this.tempFilePath;
    }

    /**
     * Set the tempFilePath.
     *
     * @param tempFilePath tempFilePath
     */
    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        
    }

    /**
     * Get the etdDelayQty.
     *
     * @return etdDelayQty
     */
    public BigDecimal getEtdDelayQty() {
        return this.etdDelayQty;
    }

    /**
     * Set the etdDelayQty.
     *
     * @param etdDelayQty etdDelayQty
     */
    public void setEtdDelayQty(BigDecimal etdDelayQty) {
        this.etdDelayQty = etdDelayQty;
        
    }

    /**
     * Get the inbDelayQty.
     *
     * @return inbDelayQty
     */
    public BigDecimal getInbDelayQty() {
        return this.inbDelayQty;
    }

    /**
     * Set the inbDelayQty.
     *
     * @param inbDelayQty inbDelayQty
     */
    public void setInbDelayQty(BigDecimal inbDelayQty) {
        this.inbDelayQty = inbDelayQty;
        
    }

    /**
     * Get the endOrderPlanDate.
     *
     * @return endOrderPlanDate
     */
    public Date getEndOrderPlanDate() {
        return this.endOrderPlanDate;
    }

    /**
     * Set the endOrderPlanDate.
     *
     * @param endOrderPlanDate endOrderPlanDate
     */
    public void setEndOrderPlanDate(Date endOrderPlanDate) {
        this.endOrderPlanDate = endOrderPlanDate;
        
    }

    /**
     * Get the runEndDate.
     *
     * @return runEndDate
     */
    public Date getRunEndDate() {
        return this.runEndDate;
    }

    /**
     * Set the runEndDate.
     *
     * @param runEndDate runEndDate
     */
    public void setRunEndDate(Date runEndDate) {
        this.runEndDate = runEndDate;
        
    }

    /**
     * Get the onlineFlag.
     *
     * @return onlineFlag
     */
    public Integer getOnlineFlag() {
        return this.onlineFlag;
    }

    /**
     * Set the onlineFlag.
     *
     * @param onlineFlag onlineFlag
     */
    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
        
    }

    /**
     * Get the rundownRemark.
     *
     * @return rundownRemark
     */
    public String getRundownRemark() {
        return this.rundownRemark;
    }

    /**
     * Set the rundownRemark.
     *
     * @param rundownRemark rundownRemark
     */
    public void setRundownRemark(String rundownRemark) {
        this.rundownRemark = rundownRemark;
        
    }

}
