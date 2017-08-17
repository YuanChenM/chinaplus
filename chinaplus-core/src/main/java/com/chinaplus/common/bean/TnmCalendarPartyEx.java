/**
 * TnmCalendarDetailEx.java
 *
 * @screen Stock batch
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.util.List;

import com.chinaplus.common.entity.TnmCalendarParty;

/**
 * Class for batch parameters.
 */
public class TnmCalendarPartyEx extends TnmCalendarParty {
    
    /** serialVersionUID */
    private static final long serialVersionUID = -1437812241657987412L;
    
    /** calendarId */
    private Integer calendarId;
    
    /** office id List */
    private List<Integer> officeList;

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
    
}
