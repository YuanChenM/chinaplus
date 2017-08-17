/**
 * CPMCLS02Entity
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.entity;

import java.sql.Timestamp;
import java.util.List;

import com.chinaplus.common.util.JsonDateSerializer;
import com.chinaplus.core.base.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * CPMCLS02Entity.
 */
public class CPMCLS02Entity extends BaseEntity {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    // CUSTOMER_ID SEQ
    private Integer customerId; // OFFICE_ID
    private Integer officeId; // OFFICE_ID
    private String customerCode;// CUSTOMER_CODE
    private String customerName;// CUSTOMER_NAME
    private String whsCode; // WHS_CUST_CODE
    private String region; // REGION_CODE
    private String address1; // ADDRESS1
    private String address2; // ADDRESS2
    private String address3; // ADDRESS3
    private String address4; // ADDRESS4
    private String contact1; // CONTACT1
    private String telephone1; // TELEPHONE1
    private String fax1; // FAX1
    private String email1; // EMAIL1
    private String postalCode; // POSTAL_CODE
    private String contact2; // CONTACT2
    private String telephone2; // TELEPHONE2
    private String fax2; // FAX2
    private String email2; // EMAIL2
    private Integer businessPattern; // businessPattern
    private Integer activeFlag; // ACTIVE_FLAG
    private Integer createdBy; // CREATED_BY
    private String createdUser; //
    private Timestamp createdDate;// CREATED_DATE
    private Integer updatedBy; // UPDATED_BY
    private String updatedUser; //
    private Timestamp updatedDate;// UPDATED_DATE
    private Integer version; // VERSION
    private boolean modifyAuth; // VERSION

    private List<TTCLogixCustomer> ttcCus;

    /**
     * Get the customerId.
     * 
     * @return customerId
     */
    public Integer getCustomerId() {
        return this.customerId;
    }

    /**
     * Set the customerId.
     * 
     * @param customerId customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * Get the customerName.
     * 
     * @return customerName
     */
    public String getCustomerName() {
        return this.customerName;
    }

    /**
     * Set the customerName.
     * 
     * @param customerName customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Get the whsCode.
     * 
     * @return whsCode
     */
    public String getWhsCode() {
        return this.whsCode;
    }

    /**
     * Set the whsCode.
     * 
     * @param whsCode whsCode
     */
    public void setWhsCode(String whsCode) {
        this.whsCode = whsCode;
    }

    /**
     * Get the region.
     * 
     * @return region
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Set the region.
     * 
     * @param region region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Get the address1.
     * 
     * @return address1
     */
    public String getAddress1() {
        return this.address1;
    }

    /**
     * Set the address1.
     * 
     * @param address1 address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * Get the address2.
     * 
     * @return address2
     */
    public String getAddress2() {
        return this.address2;
    }

    /**
     * Set the address2.
     * 
     * @param address2 address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * Get the address3.
     * 
     * @return address3
     */
    public String getAddress3() {
        return this.address3;
    }

    /**
     * Set the address3.
     * 
     * @param address3 address3
     */
    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    /**
     * Get the address4.
     * 
     * @return address4
     */
    public String getAddress4() {
        return this.address4;
    }

    /**
     * Set the address4.
     * 
     * @param address4 address4
     */
    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    /**
     * Get the contact1.
     * 
     * @return contact1
     */
    public String getContact1() {
        return this.contact1;
    }

    /**
     * Set the contact1.
     * 
     * @param contact1 contact1
     */
    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    /**
     * Get the telephone1.
     * 
     * @return telephone1
     */
    public String getTelephone1() {
        return this.telephone1;
    }

    /**
     * Set the telephone1.
     * 
     * @param telephone1 telephone1
     */
    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    /**
     * Get the fax1.
     * 
     * @return fax1
     */
    public String getFax1() {
        return this.fax1;
    }

    /**
     * Set the fax1.
     * 
     * @param fax1 fax1
     */
    public void setFax1(String fax1) {
        this.fax1 = fax1;
    }

    /**
     * Get the email1.
     * 
     * @return email1
     */
    public String getEmail1() {
        return this.email1;
    }

    /**
     * Set the email1.
     * 
     * @param email1 email1
     */
    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
     * Get the postalCode.
     * 
     * @return postalCode
     */
    public String getPostalCode() {
        return this.postalCode;
    }

    /**
     * Set the postalCode.
     * 
     * @param postalCode postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the contact2.
     * 
     * @return contact2
     */
    public String getContact2() {
        return this.contact2;
    }

    /**
     * Set the contact2.
     * 
     * @param contact2 contact2
     */
    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    /**
     * Get the teltphone2.
     * 
     * @return teltphone2
     */
    public String getTelephone2() {
        return this.telephone2;
    }

    /**
     * Set the teltphone2.
     * 
     * @param telephone2 telephone2
     */
    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    /**
     * Get the fax2.
     * 
     * @return fax2
     */
    public String getFax2() {
        return this.fax2;
    }

    /**
     * Set the fax2.
     * 
     * @param fax2 fax2
     */
    public void setFax2(String fax2) {
        this.fax2 = fax2;
    }

    /**
     * Get the email2.
     * 
     * @return email2
     */
    public String getEmail2() {
        return this.email2;
    }

    /**
     * Set the email2.
     * 
     * @param email2 email2
     */
    public void setEmail2(String email2) {
        this.email2 = email2;
    }


    /**
     * Get the activeFlag.
     * 
     * @return activeFlag
     */
    public Integer getActiveFlag() {
        return this.activeFlag;
    }

    /**
     * Set the activeFlag.
     * 
     * @param activeFlag activeFlag
     */
    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    /**
     * Get the createdBy.
     * 
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Set the createdBy.
     * 
     * @param createdBy createdBy
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Get the createdDate.
     * 
     * @return createdDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
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
     * Get the updatedBy.
     * 
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Set the updatedBy.
     * 
     * @param updatedBy updatedBy
     */
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Get the updatedDate.
     * 
     * @return updatedDate
     */
    @JsonSerialize(using = JsonDateSerializer.class)
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
     * Get the version.
     * 
     * @return version
     */
    public Integer getVersion() {
        return this.version;
    }

    /**
     * Set the version.
     * 
     * @param version version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * Get the createdUser.
     * 
     * @return createdUser
     */
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**
     * Set the createdUser.
     * 
     * @param createdUser createdUser
     */
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**
     * Get the updatedUser.
     * 
     * @return updatedUser
     */
    public String getUpdatedUser() {
        return this.updatedUser;
    }

    /**
     * Set the updatedUser.
     * 
     * @param updatedUser updatedUser
     */
    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    /**
     * Get the businessPattern.
     *
     * @return businessPattern
     */
    public Integer getBusinessPattern() {
        return this.businessPattern;
    }

    /**
     * Set the businessPattern.
     *
     * @param businessPattern businessPattern
     */
    public void setBusinessPattern(Integer businessPattern) {
        this.businessPattern = businessPattern;
    }

    /**
     * Get the ttcCus.
     *
     * @return ttcCus
     */
    public List<TTCLogixCustomer> getTtcCus() {
        return this.ttcCus;
    }

    /**
     * Set the ttcCus.
     *
     * @param ttcCus ttcCus
     */
    public void setTtcCus(List<TTCLogixCustomer> ttcCus) {
        this.ttcCus = ttcCus;
    }

	/**
	 * @return the modifyAuth
	 */
	public boolean isModifyAuth() {
		return modifyAuth;
	}

	/**
	 * @param modifyAuth the modifyAuth to set
	 */
	public void setModifyAuth(boolean modifyAuth) {
		this.modifyAuth = modifyAuth;
	}
}
