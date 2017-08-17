/**
 * CPMKBF01Entity.java
 * 
 * @screen CPMKBF01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;
import java.util.Date;

import com.chinaplus.core.base.BaseEntity;

/**
 * CPMKBF01Entity.
 */
public class CPMKBF01Entity extends BaseEntity implements Comparable<CPMKBF01Entity> {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** officeCode */
    private String officeCode;

    /** customerCode */
    private String customerCode;

    /** orderMonth */
    private String orderMonth;

    /** orderMonthDate */
    private Date orderMonthDate;

    /** fromDate */
    private Date fromDate;

    /** fromDateString */
    private String fromDateString;

    /** toDate */
    private Date toDate;

    /** toDateString */
    private String toDateString;

    /** regionCode */
    private String regionCode;

    /** newMod */
    private String newMod;

    /** changeReason */
    private String changeReason;

    /** officeAndCusmtor */
    private String officeAndCusmtor;

    /** customerId */
    private int customerId;

    /** version */
    private int version;

    /** createdDate */
    private Timestamp createdDate;

    /** updatedDate */
    private Timestamp updatedDate;

    /** updatedDate */
    private int kbIssuedId;

    /** excelLine */
    private int excelLine;
    
    private boolean flag;
    

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
     * Get the customerCode.
     *
     * @return customerCode
     */
    public String getCustomerCode() {
        return this.customerCode;
    }

    /**
     * Set the customerCode.
     *
     * @param customerCode customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Get the orderMonth.
     *
     * @return orderMonth
     */
    public String getOrderMonth() {
        return this.orderMonth;
    }

    /**
     * Set the orderMonth.
     *
     * @param orderMonth orderMonth
     */
    public void setOrderMonth(String orderMonth) {
        this.orderMonth = orderMonth;
    }

    /**
     * Get the fromDate.
     *
     * @return fromDate
     */
    public Date getFromDate() {
        return this.fromDate;
    }

    /**
     * Set the fromDate.
     *
     * @param fromDate fromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Get the toDate.
     *
     * @return toDate
     */
    public Date getToDate() {
        return this.toDate;
    }

    /**
     * Set the toDate.
     *
     * @param toDate toDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Get the regionCode.
     *
     * @return regionCode
     */
    public String getRegionCode() {
        return this.regionCode;
    }

    /**
     * Set the regionCode.
     *
     * @param regionCode regionCode
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * Get the newMod.
     *
     * @return newMod
     */
    public String getNewMod() {
        return this.newMod;
    }

    /**
     * Set the newMod.
     *
     * @param newMod newMod
     */
    public void setNewMod(String newMod) {
        this.newMod = newMod;
    }

    /**
     * Get the changeReason.
     *
     * @return changeReason
     */
    public String getChangeReason() {
        return this.changeReason;
    }

    /**
     * Set the changeReason.
     *
     * @param changeReason changeReason
     */
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    /**
     * Get the orderMonthDate.
     *
     * @return orderMonthDate
     */
    public Date getOrderMonthDate() {
        return this.orderMonthDate;
    }

    /**
     * Set the orderMonthDate.
     *
     * @param orderMonthDate orderMonthDate
     */
    public void setOrderMonthDate(Date orderMonthDate) {
        this.orderMonthDate = orderMonthDate;
    }

    @Override
    public int compareTo(CPMKBF01Entity o) {
        int result = this.officeCode.compareTo(o.getOfficeCode());
        if (result == 0) {
            result = this.customerCode.compareTo(o.getCustomerCode());
            if (result == 0) {
                result = this.orderMonth.compareTo(o.getOrderMonth());
                if (result == 0) {
                    result = this.fromDate.compareTo(o.getFromDate());
                }
            }
        }
        return result;
    }

    /**
     * Get the officeAndCusmtor.
     *
     * @return officeAndCusmtor
     */
    public String getOfficeAndCusmtor() {
        return this.officeAndCusmtor;
    }

    /**
     * Set the officeAndCusmtor.
     *
     * @param officeAndCusmtor officeAndCusmtor
     */
    public void setOfficeAndCusmtor(String officeAndCusmtor) {
        this.officeAndCusmtor = officeAndCusmtor;
    }

    /**
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        try {
            CPMKBF01Entity temp = (CPMKBF01Entity) obj;
            if (temp != null && officeCode.equals(temp.getOfficeCode()) && customerCode.equals(temp.getCustomerCode())
                    && temp.getOrderMonthDate() != null && orderMonthDate.equals(temp.getOrderMonthDate())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the customerId.
     *
     * @return customerId
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     *
     * @param customerId customerId
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the version.
     *
     * @return version
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     *
     * @param version version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Get the createdDate.
     *
     * @return createdDate
     */
    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    /**
     * Set the createdDate.
     *
     * @param createdDate createdDate
     */
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the updatedDate.
     *
     * @return updatedDate
     */
    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * Set the updatedDate.
     *
     * @param updatedDate updatedDate
     */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Get the kbIssuedId.
     *
     * @return kbIssuedId
     */
    public int getKbIssuedId() {
        return this.kbIssuedId;
    }

    /**
     * Set the kbIssuedId.
     *
     * @param kbIssuedId kbIssuedId
     */
    public void setKbIssuedId(int kbIssuedId) {
        this.kbIssuedId = kbIssuedId;
    }

    /**
     * Get the excelLine.
     *
     * @return excelLine
     */
    public int getExcelLine() {
        return this.excelLine;
    }

    /**
     * Set the excelLine.
     *
     * @param excelLine excelLine
     */
    public void setExcelLine(int excelLine) {
        this.excelLine = excelLine;
    }

    /**
     * Get the fromDateString.
     *
     * @return fromDateString
     */
    public String getFromDateString() {
        return this.fromDateString;
    }

    /**
     * Set the fromDateString.
     *
     * @param fromDateString fromDateString
     */
    public void setFromDateString(String fromDateString) {
        this.fromDateString = fromDateString;
    }

    /**
     * Get the toDateString.
     *
     * @return toDateString
     */
    public String getToDateString() {
        return this.toDateString;
    }

    /**
     * Set the toDateString.
     *
     * @param toDateString toDateString
     */
    public void setToDateString(String toDateString) {
        this.toDateString = toDateString;
    }

    /**
     *
     * @return
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Get the flag.
     *
     * @return flag
     */
    public boolean isFlag() {
        return this.flag;
    }

    /**
     * Set the flag.
     *
     * @param flag flag
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
