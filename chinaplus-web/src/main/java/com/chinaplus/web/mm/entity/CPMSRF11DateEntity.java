/**
 * @screen CPMSRF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * CPMSRF01Entity.
 */
public class CPMSRF11DateEntity extends BaseEntity implements Comparable<CPMSRF11DateEntity>{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L; 
    
    /** start  the  start */
    private Date start;
    
    /** end  the  end */
    private Date end;  

    /**
     * Get the start.
     *
     * @return start
     */
    public Date getStart() {
        return this.start;
    }

    /**
     * Set the start.
     *
     * @param start start
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Get the end.
     *
     * @return end
     */
    public Date getEnd() {
        return this.end;
    }

    /**
     * Set the end.
     *
     * @param end end
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public int compareTo(CPMSRF11DateEntity o) {
        int result = this.start.compareTo(o.getStart());
        return result;
    }

}
