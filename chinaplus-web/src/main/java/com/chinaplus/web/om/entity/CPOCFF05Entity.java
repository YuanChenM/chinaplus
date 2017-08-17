/**
 * CPOCFF05Entity.java
 * 
 * @screen CPOCFF05
 * @author li_feng
 */
package com.chinaplus.web.om.entity;

import com.chinaplus.common.entity.TntRundownCfc;

/**
 * Customer Stock DownLoad Screen Entity.
 */
public class CPOCFF05Entity extends TntRundownCfc {

    /** serialVersionUID */
    private static final long serialVersionUID = 1955966849349258429L;

    /** workingFlag */
    private Integer workingFlag;

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

    


}