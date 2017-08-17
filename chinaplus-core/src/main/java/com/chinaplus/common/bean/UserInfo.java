/**
 * @screen common
 * @author liu_yinchuan
 */
package com.chinaplus.common.bean;

import java.io.Serializable;
import java.util.List;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.entity.TnmUser;

/**
 * login user info.
 * 
 */
public class UserInfo extends TnmUser implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 8981914893983923397L;

    /** current language. */
    private Language language;

    /** current office. */
    private Integer officeId;

    /** The RoleTypeResource. */
    private UserOffice currentOffice;

    /** The RoleTypeResource. */
    // private List<UserAccessInfo> accessInfo;

    /** The RoleTypeResource. */
    private List<UserOffice> userOffice;

    /** The dataDateTime. */
    private String dataDateTime;

    /** The all vvAllFlag. */
    private boolean vvAllFlag;

    /** The all aisinAllFlag. */
    private boolean aisinAllFlag;

    /** The vv and aisinFlag */
    private String vvAisinFlag;

    /**
     * Get the vvAllFlag.
     *
     * @return vvAllFlag
     */
    public boolean isVvAllFlag() {
        return this.vvAllFlag;
    }

    /**
     * Set the vvAllFlag.
     *
     * @param vvAllFlag vvAllFlag
     */
    public void setVvAllFlag(boolean vvAllFlag) {
        this.vvAllFlag = vvAllFlag;
    }

    /**
     * Get the aisinAllFlag.
     *
     * @return aisinAllFlag
     */
    public boolean isAisinAllFlag() {
        return this.aisinAllFlag;
    }

    /**
     * Set the aisinAllFlag.
     *
     * @param aisinAllFlag aisinAllFlag
     */
    public void setAisinAllFlag(boolean aisinAllFlag) {
        this.aisinAllFlag = aisinAllFlag;
    }

    /**
     * Get the vvAisinFlag.
     *
     * @return vvAisinFlag
     */
    public String getVvAisinFlag() {
        return this.vvAisinFlag;
    }

    /**
     * Set the vvAisinFlag.
     *
     * @param vvAisinFlag vvAisinFlag
     */
    public void setVvAisinFlag(String vvAisinFlag) {
        this.vvAisinFlag = vvAisinFlag;
    }

    /**
     * Get the dataDateTime.
     *
     * @return dataDateTime
     */
    public String getDataDateTime() {
        return this.dataDateTime;
    }

    /**
     * Set the dataDateTime.
     *
     * @param dataDateTime dataDateTime
     */
    public void setDataDateTime(String dataDateTime) {
        this.dataDateTime = dataDateTime;
    }

    /**
     * Get the language.
     *
     * @return language
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * Set the language.
     *
     * @param language language
     */
    public void setLanguage(Language language) {
        this.language = language;
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

    // /**
    // * Get the accessInfo.
    // *
    // * @return accessInfo
    // */
    // public List<UserAccessInfo> getAccessInfo() {
    // return this.accessInfo;
    // }
    //
    // /**
    // * Set the accessInfo.
    // *
    // * @param accessInfo accessInfo
    // */
    // public void setAccessInfo(List<UserAccessInfo> accessInfo) {
    // this.accessInfo = accessInfo;
    //
    // }

    /**
     * Get the userOffice.
     * 
     * @return userOffice
     */
    public List<UserOffice> getUserOffice() {
        return this.userOffice;
    }

    /**
     * Set the userOffice.
     * 
     * @param userOffice userOffice
     */
    public void setUserOffice(List<UserOffice> userOffice) {
        this.userOffice = userOffice;

    }

    /**
     * Get the currentOffice.
     *
     * @return currentOffice
     */
    public UserOffice getCurrentOffice() {
        return this.currentOffice;
    }

    /**
     * Set the currentOffice.
     *
     * @param currentOffice currentOffice
     */
    public void setCurrentOffice(UserOffice currentOffice) {
        this.currentOffice = currentOffice;

    }

}
