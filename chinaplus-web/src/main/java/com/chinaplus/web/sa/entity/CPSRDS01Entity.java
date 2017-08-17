/**
 * @screen CPSRDS01Entity
 * @author zhang_chi
 */
package com.chinaplus.web.sa.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPSRDS01Entity.
 */
public class CPSRDS01Entity extends BaseEntity{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L; 
    
    /** start  the  start */
    private String start;
    
    /** end  the  end */
    private String end;  
    
    /** The dateTime. */
    private Date dateTime;
    
    /**
     * Get the dateTime.
     *
     * @return dateTime
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDateTime() {
        return this.dateTime;
    }

    /**
     * Set the dateTime.
     *
     * @param dateTime dateTime
     */
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Get the start.
     *
     * @return start
     */
    public String getStart() {
        return this.start;
    }

    /**
     * Set the start.
     *
     * @param start start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * Get the end.
     *
     * @return end
     */
    public String getEnd() {
        return this.end;
    }

    /**
     * Set the end.
     *
     * @param end end
     */
    public void setEnd(String end) {
        this.end = end;
    }

}
