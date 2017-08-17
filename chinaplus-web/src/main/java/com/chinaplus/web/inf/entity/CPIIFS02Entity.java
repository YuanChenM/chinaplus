/**
 * @screen CPIIFS02
 * @author zhang_chi
 */
package com.chinaplus.web.inf.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPIIFS02Entity.
 */
public class CPIIFS02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 7048576297489016165L;

    /** office id */
    private Integer officeId;

    /** if imp keyNo */
    private Integer ifIpNo;

    /** officeCode */
    private String officeCode;

    /** wrongInvocie */
    private String wrongInvocie;

    /** businessPattern */
    private String businessPattern;

    /** matchedInvocie */
    private String matchedInvocie;

    /** status */
    private String status;

    /** mismatchDate */
    private Date mismatchDate;
    
    /** mismatchDate */
    private String dspMismatchDate;

    /** matchDate */
    private Date matchDate;

    /** matchInvoice */
    private String matchInvoice;

    /**
     * @return the ifIpNo
     */
    public Integer getIfIpNo() {
        return ifIpNo;
    }

    /**
     * @param ifIpNo the ifIpNo to set
     */
    public void setIfIpNo(Integer ifIpNo) {
        this.ifIpNo = ifIpNo;
    }

    /**
     * Get the officeCode.
     *
     * @return officeCode
     */
    public String getOfficeCode() {
        return this.officeCode;
    }

    /**
     * Set the officeCode.
     *
     * @param officeCode officeCode
     */
    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    /**
     * Get the wrongInvocie.
     *
     * @return wrongInvocie
     */
    public String getWrongInvocie() {
        return this.wrongInvocie;
    }

    /**
     * Set the wrongInvocie.
     *
     * @param wrongInvocie wrongInvocie
     */
    public void setWrongInvocie(String wrongInvocie) {
        this.wrongInvocie = wrongInvocie;
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public String getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(String businessPattern) {
        this.businessPattern = businessPattern;
    }

    /**
     * Get the matchedInvocie.
     *
     * @return matchedInvocie
     */
    public String getMatchedInvocie() {
        return this.matchedInvocie;
    }

    /**
     * Set the matchedInvocie.
     *
     * @param matchedInvocie matchedInvocie
     */
    public void setMatchedInvocie(String matchedInvocie) {
        this.matchedInvocie = matchedInvocie;
    }

    /**
     * Get the status.
     *
     * @return status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Set the status.
     *
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the mismatchDate.
     *
     * @return mismatchDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getMismatchDate() {
        return this.mismatchDate;
    }

    /**
     * Set the mismatchDate.
     *
     * @param mismatchDate mismatchDate
     */
    public void setMismatchDate(Date mismatchDate) {
        this.mismatchDate = mismatchDate;
    }

    /**
     * @return the matchDate
     */
    public Date getMatchDate() {
        return matchDate;
    }

    /**
     * @param matchDate the matchDate to set
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    /**
     * Get the matchInvoice.
     *
     * @return matchInvoice
     */
    public String getMatchInvoice() {
        return this.matchInvoice;
    }

    /**
     * Set the matchInvoice.
     *
     * @param matchInvoice matchInvoice
     */
    public void setMatchInvoice(String matchInvoice) {
        this.matchInvoice = matchInvoice;
    }

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
     * Get the dspMismatchDate.
     *
     * @return dspMismatchDate
     */
    public String getDspMismatchDate() {
        return this.dspMismatchDate;
    }

    /**
     * Set the dspMismatchDate.
     *
     * @param dspMismatchDate dspMismatchDate
     */
    public void setDspMismatchDate(String dspMismatchDate) {
        this.dspMismatchDate = dspMismatchDate;
        
    }

}
