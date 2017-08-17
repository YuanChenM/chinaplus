package com.chinaplus.common.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;


/**
 * The persistent class for the TNT_MATCH_INVOICE database table.
 * 
 */
@Entity(name="TNT_MATCH_INVOICE")
public class TntMatchInvoice extends com.chinaplus.core.base.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TNT_MATCH_INVOICE_MATCHINVOICEID_GENERATOR", sequenceName="SEQ_TNT_MATCH_INVOICE", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TNT_MATCH_INVOICE_MATCHINVOICEID_GENERATOR")
	@Column(name="MATCH_INVOICE_ID")
	private Integer matchInvoiceId;

	@Column(name="CREATED_BY")
	private Integer createdBy;

	@Column(name="CREATED_DATE")
	private Timestamp createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name="DATA_DATE")
	private Date dataDate;

	@Column(name="INVOICE_NO")
	private String invoiceNo;

	@Column(name="MATCHED_BY")
	private Integer matchedBy;

	@Column(name="MATCHED_DATE")
	private Timestamp matchedDate;

	@Column(name="OFFICE_ID")
	private Integer officeId;

	@Column(name="\"STATUS\"")
	private Integer status;

	@Column(name="UPDATED_BY")
	private Integer updatedBy;

	@Column(name="UPDATED_DATE")
	private Timestamp updatedDate;

	@Column(name="\"VERSION\"")
	private Integer version;

	@Column(name="WHS_ID")
	private Integer whsId;

	@Column(name="WHS_INVOICE_NO")
	private String whsInvoiceNo;

	public TntMatchInvoice() {
	}

	public Integer getMatchInvoiceId() {
		return this.matchInvoiceId;
	}

	public void setMatchInvoiceId(Integer matchInvoiceId) {
		this.matchInvoiceId = matchInvoiceId;
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

	public Date getDataDate() {
		return this.dataDate;
	}

	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}

	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Integer getMatchedBy() {
		return this.matchedBy;
	}

	public void setMatchedBy(Integer matchedBy) {
		this.matchedBy = matchedBy;
	}

	public Timestamp getMatchedDate() {
		return this.matchedDate;
	}

	public void setMatchedDate(Timestamp matchedDate) {
		this.matchedDate = matchedDate;
	}

	public Integer getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(Integer officeId) {
		this.officeId = officeId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getWhsInvoiceNo() {
		return this.whsInvoiceNo;
	}

	public void setWhsInvoiceNo(String whsInvoiceNo) {
		this.whsInvoiceNo = whsInvoiceNo;
	}

}