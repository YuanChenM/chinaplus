package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNF_PARTS_STATUS database table.
 * 
 */
@Entity(name="TNF_EXP_PARTS_STATUS")
public class TnfExpPartsStatus extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNF_EXP_PARTS_STATUS_PARTSSTATUSID_GENERATOR", sequenceName="SEQ_TNF_EXP_PARTS_STATUS", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNF_EXP_PARTS_STATUS_PARTSSTATUSID_GENERATOR")
	@Column(name="PARTS_STATUS_ID")
	private Integer partsStatusId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DAY_ONSHIPPING_QTY")
	private BigDecimal dayOnshippingQty;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="EXP_BALANCE_QTY")
	private BigDecimal expBalanceQty;

	@Column(name="EXP_ONSHIPPING_QTY")
	private BigDecimal expOnshippingQty;

	@Column(name="EXP_PLAN_DELAY_QTY")
	private BigDecimal expPlanDelayQty;

	@Column(name="EXP_STOCK_QTY")
	private BigDecimal expStockQty;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TnfExpPartsStatus() {
	}

	public Integer getPartsStatusId() {
		return this.partsStatusId;
	}

	public void setPartsStatusId(Integer partsStatusId) {
		this.partsStatusId = partsStatusId;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getDayOnshippingQty() {
		return this.dayOnshippingQty;
	}

	public void setDayOnshippingQty(BigDecimal dayOnshippingQty) {
		this.dayOnshippingQty = dayOnshippingQty;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getExpBalanceQty() {
		return this.expBalanceQty;
	}

	public void setExpBalanceQty(BigDecimal expBalanceQty) {
		this.expBalanceQty = expBalanceQty;
	}

	public BigDecimal getExpOnshippingQty() {
		return this.expOnshippingQty;
	}

	public void setExpOnshippingQty(BigDecimal expOnshippingQty) {
		this.expOnshippingQty = expOnshippingQty;
	}

	public BigDecimal getExpPlanDelayQty() {
		return this.expPlanDelayQty;
	}

	public void setExpPlanDelayQty(BigDecimal expPlanDelayQty) {
		this.expPlanDelayQty = expPlanDelayQty;
	}

	public BigDecimal getExpStockQty() {
		return this.expStockQty;
	}

	public void setExpStockQty(BigDecimal expStockQty) {
		this.expStockQty = expStockQty;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public Integer getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Integer partsId) {
		this.partsId = partsId;
	}

	public Integer getSupplierId() {
		return this.supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     *
     * @author liu_yinchuan
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     *
     * @author liu_yinchuan
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
        
    }

}