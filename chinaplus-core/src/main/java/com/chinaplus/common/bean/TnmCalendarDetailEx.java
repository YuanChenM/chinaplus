/**
 * TnmCalendarDetailEx.java
 *
 * @screen Stock batch
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.util.Date;
import java.util.List;

import com.chinaplus.common.entity.TnmCalendarDetail;

/**
 * Class for batch parameters.
 */
public class TnmCalendarDetailEx extends TnmCalendarDetail {
    
    /** serialVersionUID */
    private static final long serialVersionUID = -1437812241657987412L;
    
    /** calendarId */
    private Integer calendarId;
    
    /** office id List */
    private List<Integer> officeList;
    
    /** fromDate */
    private Date fromDate;
    
    /** toDate */
    private Date toDate;

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
     * Get the officeList.
     *
     * @return officeList
     */
    public List<Integer> getOfficeList() {
        return this.officeList;
    }

    /**
     * Set the officeList.
     *
     * @param officeList officeList
     */
    public void setOfficeList(List<Integer> officeList) {
        this.officeList = officeList;
        
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
