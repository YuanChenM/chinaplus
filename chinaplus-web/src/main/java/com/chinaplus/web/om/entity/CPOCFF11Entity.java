/**
 * CPOCFCOMEntity.java
 * 
 * @screen CPOCFF11
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntCfcMaster;
import com.chinaplus.common.entity.TntCfcMonth;

/**
 * Upload Data Entity.
 */
public class CPOCFF11Entity extends TntCfcMaster {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** sheetName */
    private String sheetName;
    
    /** uploadType */
    private String uploadType;

    /** businessPatternValue */
    private String businessPatternValue;

    /** startFcDate */
    private Date startFcDate;

    /** endFcDate */
    private Date endFcDate;

    /** partsMap */
    private Map<String, TnmPartsMaster> partsMap;

    /** cfcMonthMap */
    private Map<String, List<TntCfcMonth>> cfcMonthMap;

    /** cfcDayMap */
    private Map<String, List<TntCfcDay>> cfcDayMap;

    /** customerCodeLst */
    private List<String> customerCodeLst;

    /** forecastNoMap */
    private Map<String, String> forecastNoMap;

    /** calendarDate */
    private Date calendarDate;

    /** workingFlag */
    private Integer workingFlag;

    /** calendarId */
    private Integer calendarId;

    /** customerCode */
    private String customerCode;

    /**
     * Get the forecastNoMap.
     *
     * @return forecastNoMap
     */
    public Map<String, String> getForecastNoMap() {
        return this.forecastNoMap;
    }

    /**
     * Set the forecastNoMap.
     *
     * @param forecastNoMap forecastNoMap
     */
    public void setForecastNoMap(Map<String, String> forecastNoMap) {
        this.forecastNoMap = forecastNoMap;
    }

    /**
     * Get the uploadType.
     *
     * @return uploadType
     */
    public String getUploadType() {
        return this.uploadType;
    }

    /**
     * Set the uploadType.
     *
     * @param uploadType uploadType
     */
    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    /**
     * Get the calendarDate.
     *
     * @return calendarDate
     */
    public Date getCalendarDate() {
        return this.calendarDate;
    }

    /**
     * Set the calendarDate.
     *
     * @param calendarDate calendarDate
     */
    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    /**
     * Get the workingFlag.
     *
     * @return workingFlag
     */
    public Integer getWorkingFlag() {
        return this.workingFlag;
    }

    /**
     * Set the workingFlag.
     *
     * @param workingFlag workingFlag
     */
    public void setWorkingFlag(Integer workingFlag) {
        this.workingFlag = workingFlag;
    }

    /**
     * Get the calendarId.
     *
     * @return calendarId
     */
    public Integer getCalendarId() {
        return this.calendarId;
    }

    /**
     * Set the calendarId.
     *
     * @param calendarId calendarId
     */
    public void setCalendarId(Integer calendarId) {
        this.calendarId = calendarId;
    }

    /**
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Get the sheetName.
     *
     * @return sheetName
     */
    public String getSheetName() {
        return this.sheetName;
    }

    /**
     * Set the sheetName.
     *
     * @param sheetName sheetName
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * Get the cfcMonthMap.
     *
     * @return cfcMonthMap
     */
    public Map<String, List<TntCfcMonth>> getCfcMonthMap() {
        return this.cfcMonthMap;
    }

    /**
     * Set the cfcMonthMap.
     *
     * @param cfcMonthMap cfcMonthMap
     */
    public void setCfcMonthMap(Map<String, List<TntCfcMonth>> cfcMonthMap) {
        this.cfcMonthMap = cfcMonthMap;
    }

    /**
     * Get the cfcDayMap.
     *
     * @return cfcDayMap
     */
    public Map<String, List<TntCfcDay>> getCfcDayMap() {
        return this.cfcDayMap;
    }

    /**
     * Set the cfcMonthMap.
     *
     * @param cfcMonthMap cfcMonthMap
     */
    public void setCfcDayMap(Map<String, List<TntCfcDay>> cfcDayMap) {
        this.cfcDayMap = cfcDayMap;
    }

    /**
     * Get the businessPatternValue.
     *
     * @return businessPatternValue
     */
    public String getBusinessPatternValue() {
        return this.businessPatternValue;
    }

    /**
     * Set the businessPatternValue.
     *
     * @param businessPatternValue businessPatternValue
     */
    public void setBusinessPatternValue(String businessPatternValue) {
        this.businessPatternValue = businessPatternValue;
    }

    /**
     * Get the customerCodeLst.
     *
     * @return customerCodeLst
     */
    public List<String> getCustomerCodeLst() {
        return this.customerCodeLst;
    }

    /**
     * Set the customerCodeLst.
     *
     * @param customerCodeLst customerCodeLst
     */
    public void setCustomerCodeLst(List<String> customerCodeLst) {
        this.customerCodeLst = customerCodeLst;
    }

    /**
     * Get the partsMap.
     *
     * @return partsMap
     */
    public Map<String, TnmPartsMaster> getPartsMap() {
        return this.partsMap;
    }

    /**
     * Set the partsMap.
     *
     * @param partsMap partsMap
     */
    public void setPartsMap(Map<String, TnmPartsMaster> partsMap) {
        this.partsMap = partsMap;
    }

    /**
     * Get the startFcDate.
     *
     * @return startFcDate
     */
    public Date getStartFcDate() {
        return this.startFcDate;
    }

    /**
     * Set the startFcDate.
     *
     * @param startFcDate startFcDate
     */
    public void setStartFcDate(Date startFcDate) {
        this.startFcDate = startFcDate;
    }

    /**
     * Get the endFcDate.
     *
     * @return endFcDate
     */
    public Date getEndFcDate() {
        return this.endFcDate;
    }

    /**
     * Set the endFcDate.
     *
     * @param endFcDate endFcDate
     */
    public void setEndFcDate(Date endFcDate) {
        this.endFcDate = endFcDate;
    }

}