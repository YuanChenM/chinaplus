package com.chinaplus.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNF_IMP_STOCK_BY_DAY database table.
 * 
 */
@Entity(name="TNF_IMP_STOCK_BY_DAY")
public class TnfImpStockByDay extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TNF_IMP_STOCK_BY_DAY_STOCKDAYID_GENERATOR", sequenceName="SEQ_TNF_IMP_STOCK_BY_DAY", allocationSize = 1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNF_IMP_STOCK_BY_DAY_STOCKDAYID_GENERATOR")
	@Column(name="STOCK_DAY_ID")
	private Integer stockDayId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="DAY_INBOUND_QTY")
	private BigDecimal dayInboundQty;

	@Column(name="DAY_OUTBOUND_QTY")
	private BigDecimal dayOutboundQty;

	@Temporal(TemporalType.DATE)
	@Column(name="END_DATE")
	private Date endDate;

	@Column(name="IMP_IN_RACK_QTY")
	private BigDecimal impInRackQty;

	@Column(name="IMP_STOCK_QTY")
	private BigDecimal impStockQty;

	@Column(name="NG_QTY")
	private BigDecimal ngQty;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="ONHOLD_QTY")
	private BigDecimal onholdQty;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WHS_ID")
	private Integer whsId;

    @Column(name = "BUSINESS_PATTERN")
    private Integer businessPattern;

	@Column(name="SYSTEM_STOCK_QTY")
	private BigDecimal whsStockQty;

	public TnfImpStockByDay() {
	}

	public Integer getStockDayId() {
		return this.stockDayId;
	}

	public void setStockDayId(Integer stockDayId) {
		this.stockDayId = stockDayId;
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

	public BigDecimal getDayInboundQty() {
		return this.dayInboundQty;
	}

	public void setDayInboundQty(BigDecimal dayInboundQty) {
		this.dayInboundQty = dayInboundQty;
	}

	public BigDecimal getDayOutboundQty() {
		return this.dayOutboundQty;
	}

	public void setDayOutboundQty(BigDecimal dayOutboundQty) {
		this.dayOutboundQty = dayOutboundQty;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getImpInRackQty() {
		return this.impInRackQty;
	}

	public void setImpInRackQty(BigDecimal impInRackQty) {
		this.impInRackQty = impInRackQty;
	}

	public BigDecimal getImpStockQty() {
		return this.impStockQty;
	}

	public void setImpStockQty(BigDecimal impStockQty) {
		this.impStockQty = impStockQty;
	}

	public BigDecimal getNgQty() {
		return this.ngQty;
	}

	public void setNgQty(BigDecimal ngQty) {
		this.ngQty = ngQty;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public BigDecimal getOnholdQty() {
		return this.onholdQty;
	}

	public void setOnholdQty(BigDecimal onholdQty) {
		this.onholdQty = onholdQty;
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

	public Integer getWhsId() {
		return this.whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
	}

	public BigDecimal getWhsStockQty() {
		return this.whsStockQty;
	}

	public void setWhsStockQty(BigDecimal whsStockQty) {
		this.whsStockQty = whsStockQty;
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