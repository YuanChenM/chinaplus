/**
 * CPMSRF13Entity.java
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.entity;

import java.util.Date;

import com.chinaplus.common.util.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMSRF13Entity.
 */
public class AisinCommonEntity extends MMCommonEntity implements Comparable<AisinCommonEntity>{

    /** serialVersionUID */
    private static final long serialVersionUID = 6901683044031317722L;
    
    /** srId the srId */
    private Integer srId;

    /** supplierId the supplierId */
    private Integer supplierId;

    /** lastVanning the lastVanning */
    private Date lastVanning;

    /** kanbanIssueDate the kanbanIssueDate */
    private Date kanbanIssueDate;

    /** expInboundDate the expInboundDate */
    private Date expInboundDate;

    /** etd the etd */
    private Date etd;

    /** eta the eta */
    private Date eta;

    /** inactiveFlag the inactiveFlag */
    private String inactiveFlag;

    /** shippingRouteCode the shippingRouteCode */
    private String shippingRouteCode;
    
    /** shippingRouteType the shippingRouteType */
    private Integer shippingRouteType;
    
    /** firstEtd the firstEtd */
    private Date firstEtd;
    
    /** lastEtd the lastEtd */
    private Date lastEtd;
    
    /** vanningDay the vanningDay */
    private Integer vanningDay;
    
    /** workingDays the workingDays */
    private Integer workingDays;
    
    /** expVanningLeadtime the expVanningLeadtime */
    private Integer expVanningLeadtime;
    
    /** etdWeek the etdWeek */
    private Integer etdWeek;
    
    /** etdDate the etdDate */
    private Integer etdDate;
    
    /** deliveryLeadtime the deliveryLeadtime */
    private Integer deliveryLeadtime;  
    
    /** supplierCode the supplierCode */
    private String supplierCode;
    
    /** deliveryStartDate  the deliveryStartDate */
    private  Date  deliveryStartDate;     

    /** deliveryStartDateMax  the deliveryStartDateMax */
    private  Date  deliveryStartDateMax;  

    /** deliveryToObuDate  the deliveryToObuDate */
    private  Date  deliveryToObuDate; 
    
    /** officeId  the officeId */
    private Integer officeId;
    
    /** expRegion  the expRegion */
    private String  expRegion;
    
    /**
     * Get the expRegion.
     *
     * @return expRegion
     */
    public String getExpRegion() {
        return this.expRegion;
    }

    /**
     * Set the expRegion.
     *
     * @param expRegion expRegion
     */
    public void setExpRegion(String expRegion) {
        this.expRegion = expRegion;
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
     * Get the deliveryStartDateMax.
     *
     * @return deliveryStartDateMax
     */
    public Date getDeliveryStartDateMax() {
        return this.deliveryStartDateMax;
    }

    /**
     * Set the deliveryStartDateMax.
     *
     * @param deliveryStartDateMax deliveryStartDateMax
     */
    public void setDeliveryStartDateMax(Date deliveryStartDateMax) {
        this.deliveryStartDateMax = deliveryStartDateMax;
    }
    
    /**
     * Get the deliveryToObuDate.
     *
     * @return deliveryToObuDate
     */
    public Date getDeliveryToObuDate() {
        return this.deliveryToObuDate;
    }

    /**
     * Set the deliveryToObuDate.
     *
     * @param deliveryToObuDate deliveryToObuDate
     */
    public void setDeliveryToObuDate(Date deliveryToObuDate) {
        this.deliveryToObuDate = deliveryToObuDate;
    }

    /**
     * Get the deliveryStartDate.
     *
     * @return deliveryStartDate
     */
    @JsonSerialize(using= JsonDateSerializer.class)
    public Date getDeliveryStartDate() {
        return this.deliveryStartDate;
    }

    /**
     * Set the deliveryStartDate.
     *
     * @param deliveryStartDate deliveryStartDate
     */
    public void setDeliveryStartDate(Date deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }    
    
    /**
     * Get the supplierCode.
     *
     * @return supplierCode
     */
    public String getSupplierCode() {
        return this.supplierCode;
    }

    /**
     * Set the supplierCode.
     *
     * @param supplierCode supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
    /**
     * copy AisinCommonEntity
     * 
     * @return  AisinCommonEntity  AisinCommonEntity
     */
    public AisinCommonEntity cloneBean() {
        try {
            return (AisinCommonEntity) this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }  
    
    /**
     * Get the shippingRouteCode.
     *
     * @return shippingRouteCode
     */
    public String getShippingRouteCode() {
        return this.shippingRouteCode;
    }

    /**
     * Set the shippingRouteCode.
     *
     * @param shippingRouteCode shippingRouteCode
     */
    public void setShippingRouteCode(String shippingRouteCode) {
        this.shippingRouteCode = shippingRouteCode;
    }

    /**
     * Get the workingDays.
     *
     * @return workingDays
     */
    public Integer getWorkingDays() {
        return this.workingDays;
    }

    /**
     * Set the workingDays.
     *
     * @param workingDays workingDays
     */
    public void setWorkingDays(Integer workingDays) {
        this.workingDays = workingDays;
    }

    /**
     * Get the expVanningLeadtime.
     *
     * @return expVanningLeadtime
     */
    public Integer getExpVanningLeadtime() {
        return this.expVanningLeadtime;
    }

    /**
     * Set the expVanningLeadtime.
     *
     * @param expVanningLeadtime expVanningLeadtime
     */
    public void setExpVanningLeadtime(Integer expVanningLeadtime) {
        this.expVanningLeadtime = expVanningLeadtime;
    }

    /**
     * Get the deliveryLeadtime.
     *
     * @return deliveryLeadtime
     */
    public Integer getDeliveryLeadtime() {
        return this.deliveryLeadtime;
    }

    /**
     * Set the deliveryLeadtime.
     *
     * @param deliveryLeadtime deliveryLeadtime
     */
    public void setDeliveryLeadtime(Integer deliveryLeadtime) {
        this.deliveryLeadtime = deliveryLeadtime;
    }

    /**
     * Get the firstEtd.
     *
     * @return firstEtd
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getFirstEtd() {
        return this.firstEtd;
    }

    /**
     * Set the firstEtd.
     *
     * @param firstEtd firstEtd
     */
    public void setFirstEtd(Date firstEtd) {
        this.firstEtd = firstEtd;
    }

    /**
     * Get the lastEtd.
     *
     * @return lastEtd
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getLastEtd() {
        return this.lastEtd;
    }

    /**
     * Set the lastEtd.
     *
     * @param lastEtd lastEtd
     */
    public void setLastEtd(Date lastEtd) {
        this.lastEtd = lastEtd;
    }

    /**
     * Get the etdDate.
     *
     * @return etdDate
     */
    public Integer getEtdDate() {
        return this.etdDate;
    }

    /**
     * Set the etdDate.
     *
     * @param etdDate etdDate
     */
    public void setEtdDate(Integer etdDate) {
        this.etdDate = etdDate;
    }

    /**
     * Get the etdWeek.
     *
     * @return etdWeek
     */
    public Integer getEtdWeek() {
        return this.etdWeek;
    }

    /**
     * Set the etdWeek.
     *
     * @param etdWeek etdWeek
     */
    public void setEtdWeek(Integer etdWeek) {
        this.etdWeek = etdWeek;
    }

    /**
     * Get the vanningDay.
     *
     * @return vanningDay
     */
    public Integer getVanningDay() {
        return this.vanningDay;
    }

    /**
     * Set the vanningDay.
     *
     * @param vanningDay vanningDay
     */
    public void setVanningDay(Integer vanningDay) {
        this.vanningDay = vanningDay;
    }

    /**
     * Get the srId.
     *
     * @return srId
     */
    public Integer getSrId() {
        return this.srId;
    }

    /**
     * Set the srId.
     *
     * @param srId srId
     */
    public void setSrId(Integer srId) {
        this.srId = srId;
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

    /**
     * Get the lastVanning.
     *
     * @return lastVanning
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getLastVanning() {
        return this.lastVanning;
    }

    /**
     * Set the lastVanning.
     *
     * @param lastVanning lastVanning
     */
    public void setLastVanning(Date lastVanning) {
        this.lastVanning = lastVanning;
    }

    /**
     * Get the kanbanIssueDate.
     *
     * @return kanbanIssueDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getKanbanIssueDate() {
        return this.kanbanIssueDate;
    }

    /**
     * Set the kanbanIssueDate.
     *
     * @param kanbanIssueDate kanbanIssueDate
     */
    public void setKanbanIssueDate(Date kanbanIssueDate) {
        this.kanbanIssueDate = kanbanIssueDate;
    }

    /**
     * Get the expInboundDate.
     *
     * @return expInboundDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getExpInboundDate() {
        return this.expInboundDate;
    }

    /**
     * Set the expInboundDate.
     *
     * @param expInboundDate expInboundDate
     */
    public void setExpInboundDate(Date expInboundDate) {
        this.expInboundDate = expInboundDate;
    }

    /**
     * Get the etd.
     *
     * @return etd
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEtd() {
        return this.etd;
    }

    /**
     * Set the etd.
     *
     * @param etd etd
     */
    public void setEtd(Date etd) {
        this.etd = etd;
    }

    /**
     * Get the eta.
     *
     * @return eta
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getEta() {
        return this.eta;
    }

    /**
     * Set the eta.
     *
     * @param eta eta
     */
    public void setEta(Date eta) {
        this.eta = eta;
    }

    /**
     * Get the inactiveFlag.
     *
     * @return inactiveFlag
     */
    public String getInactiveFlag() {
        return this.inactiveFlag;
    }

    /**
     * Set the inactiveFlag.
     *
     * @param inactiveFlag inactiveFlag
     */
    public void setInactiveFlag(String inactiveFlag) {
        this.inactiveFlag = inactiveFlag;
    }

    @Override
    public int compareTo(AisinCommonEntity o) {
        int result = this.shippingRouteCode.compareTo(o.getShippingRouteCode());
        if(result == 0){
            result = this.supplierId.compareTo(o.getSupplierId());
            if(result == 0){
                result = this.deliveryToObuDate.compareTo(o.getDeliveryToObuDate());
            }
        }
        return result;
    }

    /**
     * Get the shippingRouteType.
     *
     * @return shippingRouteType
     */
    public Integer getShippingRouteType() {
        return this.shippingRouteType;
    }

    /**
     * Set the shippingRouteType.
     *
     * @param shippingRouteType shippingRouteType
     */
    public void setShippingRouteType(Integer shippingRouteType) {
        this.shippingRouteType = shippingRouteType;
    }

}
