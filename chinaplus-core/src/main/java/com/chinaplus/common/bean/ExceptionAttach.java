package com.chinaplus.common.bean;

import com.chinaplus.core.base.BaseEntity;

/**
 * 
 * BatchException.
 * @author yang_jia1
 */
public class ExceptionAttach extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = -2052512387196609026L;
    
    /**
     * id
     */
    private Integer id;
    
    /**
     * flag
     */
    private Integer flag;
    
    /**
     * reason
     */
    private String reason;

    /**
     * Get the id.
     *
     * @return id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Set the id.
     *
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
        
    }

    /**
     * Get the flag.
     *
     * @return flag
     */
    public Integer getFlag() {
        return this.flag;
    }

    /**
     * Set the flag.
     *
     * @param flag flag
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
        
    }

    /**
     * Get the reason.
     *
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Set the reason.
     *
     * @param reason reason
     */
    public void setReason(String reason) {
        this.reason = reason;
        
    }
 
}
