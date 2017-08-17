/**
 * CPIIFB01Param.java
 * 
 * @screen CPIIFB01
 * @author yang_jia1
 */
package com.chinaplus.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Class for batch parameters.
 * 
 * @author yang_jia1
 */
public class SsmsCommonParam implements Serializable  {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** ifDateTime */
    private Timestamp ifDateTime;

    /** expPartsId */
    private List<Integer> expPartsId;

    /** Office ID */
    private Integer officeId;

    /** Office Code */
    private String officeCode;

    /**
     * @return the ifDateTime
     */
    public Timestamp getIfDateTime() {
        return ifDateTime;
    }

    /**
     * @param ifDateTime the ifDateTime to set
     */
    public void setIfDateTime(Timestamp ifDateTime) {
        this.ifDateTime = ifDateTime;
    }

    /**
     * @return the expPartsId
     */
    public List<Integer> getExpPartsId() {
        return expPartsId;
    }

    /**
     * @param expPartsId the expPartsId to set
     */
    public void setExpPartsId(List<Integer> expPartsId) {
        this.expPartsId = expPartsId;
    }

    /**
     * @return the officeId
     */
    public Integer getOfficeId() {
        return officeId;
    }

    /**
     * @param officeId the officeId to set
     */
    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    /**
     * @return the officeCode
     */
    public String getOfficeCode() {
        return officeCode;
    }

    /**
     * @param officeCode the officeCode to set
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }
}
