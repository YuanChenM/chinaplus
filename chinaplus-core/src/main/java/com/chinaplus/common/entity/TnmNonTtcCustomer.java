package com.chinaplus.common.entity;

import java.io.Serializable;

import javax.persistence.*;


import java.sql.Timestamp;

/**
 * The persistent class for the TNM_NON_TTC_CUSTOMER database table.
 * 
 */
@Entity(name = "TNM_NON_TTC_CUSTOMER")
public class TnmNonTtcCustomer extends com.chinaplus.core.base.BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TNM_NON_TTC_CUSTOMER_NONTTCCUSTID_GENERATOR",
        sequenceName = "SEQ_TNM_NON_TTC_CUSTOMER",
        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "TNM_NON_TTC_CUSTOMER_NONTTCCUSTID_GENERATOR")
    @Column(name = "NON_TTC_CUST_ID")
    private Integer nonTtcCustId;

    @Column(name = "CREATED_BY")
    private Integer createdBy;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "KANB_CUSTOMER_CODE")
    private String kanbCustomerCode;

    @Column(name = "UPDATED_BY")
    private Integer updatedBy;

    @Column(name = "UPDATED_DATE")
    private Timestamp updatedDate;

    @Column(name = "\"VERSION\"")
    private Integer version;

    public TnmNonTtcCustomer() {}

    public Integer getNonTtcCustId() {
        return this.nonTtcCustId;
    }

    public void setNonTtcCustId(Integer nonTtcCustId) {
        this.nonTtcCustId = nonTtcCustId;
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

    public String getKanbCustomerCode() {
        return this.kanbCustomerCode;
    }

    public void setKanbCustomerCode(String kanbCustomerCode) {
        this.kanbCustomerCode = kanbCustomerCode;
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
     *
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        TnmNonTtcCustomer ttc = (TnmNonTtcCustomer) obj;
        if (ttc.getNonTtcCustId() != null && ttc.getNonTtcCustId().equals(nonTtcCustId)) {
            return true;
        }
        return false;
    }
}