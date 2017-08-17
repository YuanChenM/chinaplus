/**
 * CPMSRF13Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMSRF13Entity.
 */
public class CalendarDateEntity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;

    /** srId the srId */
    private Date CalendarDate;

    /** supplierId the supplierId */
    private Integer supplierId;

    /** officeId */
    private Integer officeId;

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
     * Get the calendarDate.
     *
     * @return calendarDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getCalendarDate() {
        return this.CalendarDate;
    }

    /**
     * Set the calendarDate.
     *
     * @param calendarDate calendarDate
     */
    public void setCalendarDate(Date calendarDate) {
        CalendarDate = calendarDate;
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

}
