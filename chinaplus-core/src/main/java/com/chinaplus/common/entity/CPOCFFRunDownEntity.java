/**
 * CPOCFFRunDownEntity.java
 * 
 * @screen CPOCFF05
 * @author li_feng
 */
package com.chinaplus.common.entity;

import com.chinaplus.common.entity.TntRundownCfc;

/**
 * Customer Stock DownLoad Screen Entity.
 */
public class CPOCFFRunDownEntity extends TntRundownCfc {

    /** serialVersionUID */
    private static final long serialVersionUID = 1955966849349258439L;

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