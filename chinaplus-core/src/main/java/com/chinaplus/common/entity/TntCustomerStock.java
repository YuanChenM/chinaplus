package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_CUSTOMER_STOCK database table.
 * 
 */
@Entity(name="TNT_CUSTOMER_STOCK")
public class TntCustomerStock extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_CUSTOMER_STOCK_CUSTOMERSTOCKID_GENERATOR", sequenceName="SEQ_TNT_CUSTOMER_STOCK", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_CUSTOMER_STOCK_CUSTOMERSTOCKID_GENERATOR")
	@Column(name="CUSTOMER_STOCK_ID")
	private Integer customerStockId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Temporal(TemporalType.DATE)
	@Column(name="ENDING_STOCK_DATE")
	private Date endingStockDate;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="PARTS_ID")
	private Integer partsId;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="STOCK_QTY")
	private BigDecimal stockQty;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntCustomerStock() {
	}

	public Integer getCustomerStockId() {
		return this.customerStockId;
	}

	public void setCustomerStockId(Integer customerStockId) {
		this.customerStockId = customerStockId;
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

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getEndingStockDate() {
		return this.endingStockDate;
	}

	public void setEndingStockDate(Date endingStockDate) {
		this.endingStockDate = endingStockDate;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getStockQty() {
		return this.stockQty;
	}

	public void setStockQty(BigDecimal stockQty) {
		this.stockQty = stockQty;
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

}