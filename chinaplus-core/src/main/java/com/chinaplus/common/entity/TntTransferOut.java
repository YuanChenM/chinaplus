package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TNT_TRANSFER_OUT database table.
 * 
 */
@Entity(name="TNT_TRANSFER_OUT")
public class TntTransferOut extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_TRANSFER_OUT_TRANSFEROUTID_GENERATOR", sequenceName="SEQ_TNT_TRANSFER_OUT", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_TRANSFER_OUT_TRANSFEROUTID_GENERATOR")
	@Column(name="TRANSFER_OUT_ID")
	private Integer transferOutId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Column(name="CUSTOMER_ORDER_NO")
	private String customerOrderNo;

	@Column(name="EXP_PO_NO")
	private String expPoNo;

	@Column(name="FROM_CUSTOMER_ID")
	private Integer fromCustomerId;

	@Column(name="FROM_PARTS_ID")
	private Integer fromPartsId;

	@Column(name="IMP_PO_NO")
	private String impPoNo;

	@Column(name="KANBAN_PLAN_NO")
	private String kanbanPlanNo;

	@Column(name="ORDER_MONTH")
	private String orderMonth;

	@Column(name="OUTBOUND_QTY")
	private BigDecimal outboundQty;

	private BigDecimal qty;

	@Column(name="SUPPLIER_ID")
	private Integer supplierId;

	@Column(name="TO_CUSTOMER_ID")
	private Integer toCustomerId;

	@Column(name="TO_PARTS_ID")
	private Integer toPartsId;

	@Temporal(TemporalType.DATE)
	@Column(name="TRANSFERED_DATE")
	private Date transferedDate;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	public TntTransferOut() {
	}

	public Integer getTransferOutId() {
		return this.transferOutId;
	}

	public void setTransferOutId(Integer transferOutId) {
		this.transferOutId = transferOutId;
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

	public String getCustomerOrderNo() {
		return this.customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getExpPoNo() {
		return this.expPoNo;
	}

	public void setExpPoNo(String expPoNo) {
		this.expPoNo = expPoNo;
	}

	public Integer getFromCustomerId() {
		return this.fromCustomerId;
	}

	public void setFromCustomerId(Integer fromCustomerId) {
		this.fromCustomerId = fromCustomerId;
	}

	public Integer getFromPartsId() {
		return this.fromPartsId;
	}

	public void setFromPartsId(Integer fromPartsId) {
		this.fromPartsId = fromPartsId;
	}

	public String getImpPoNo() {
		return this.impPoNo;
	}

	public void setImpPoNo(String impPoNo) {
		this.impPoNo = impPoNo;
	}

	public String getKanbanPlanNo() {
		return this.kanbanPlanNo;
	}

	public void setKanbanPlanNo(String kanbanPlanNo) {
		this.kanbanPlanNo = kanbanPlanNo;
	}

	public String getOrderMonth() {
		return this.orderMonth;
	}

	public void setOrderMonth(String orderMonth) {
		this.orderMonth = orderMonth;
	}

	public BigDecimal getOutboundQty() {
		return this.outboundQty;
	}

	public void setOutboundQty(BigDecimal outboundQty) {
		this.outboundQty = outboundQty;
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

	public Integer getToCustomerId() {
		return this.toCustomerId;
	}

	public void setToCustomerId(Integer toCustomerId) {
		this.toCustomerId = toCustomerId;
	}

	public Integer getToPartsId() {
		return this.toPartsId;
	}

	public void setToPartsId(Integer toPartsId) {
		this.toPartsId = toPartsId;
	}

	public Date getTransferedDate() {
		return this.transferedDate;
	}

	public void setTransferedDate(Date transferedDate) {
		this.transferedDate = transferedDate;
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