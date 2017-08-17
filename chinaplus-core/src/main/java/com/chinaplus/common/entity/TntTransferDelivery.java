package com.chinaplus.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/**
 * The persistent class for the TNT_TRANSFER_DELIVERY database table.
 * 
 */
@Entity(name="TNT_TRANSFER_DELIVERY")
public class TntTransferDelivery extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_TRANSFER_DELIVERY_TRANSFERDELIVERYID_GENERATOR", sequenceName="SEQ_TNT_TRANSFER_DELIVERY", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_TRANSFER_DELIVERY_TRANSFERDELIVERYID_GENERATOR")
	@Column(name="TRANSFER_DELIVERY_ID")
	private Integer transferDeliveryId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ID")
	private Integer customerId;

	@Column(name="DELIVERED_DATE")
	private Date deliveredDate;

	@Column(name="EXP_PO_NO")
	private String expPoNo;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="ORDER_MONTH")
	private String orderMonth;

	@Column(name="PARTS_ID")
	private Integer partsId;

	private BigDecimal qty;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntTransferDelivery() {
	}

	public Integer getTransferDeliveryId() {
		return this.transferDeliveryId;
	}

	public void setTransferDeliveryId(Integer transferDeliveryId) {
		this.transferDeliveryId = transferDeliveryId;
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

	public Date getDeliveredDate() {
		return this.deliveredDate;
	}

	public void setDeliveredDate(Date deliveredDate) {
		this.deliveredDate = deliveredDate;
	}

	public String getExpPoNo() {
		return this.expPoNo;
	}

	public void setExpPoNo(String expPoNo) {
		this.expPoNo = expPoNo;
	}

	public String getImpPoNo() {
		return this.impPoNo;
	}

	public void setImpPoNo(String impPoNo) {
		this.impPoNo = impPoNo;
	}

	public String getOrderMonth() {
		return this.orderMonth;
	}

	public void setOrderMonth(String orderMonth) {
		this.orderMonth = orderMonth;
	}

	public Integer getPartsId() {
		return this.partsId;
	}

	public void setPartsId(Integer partsId) {
		this.partsId = partsId;
	}

	public BigDecimal getQty() {
		return this.qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

}