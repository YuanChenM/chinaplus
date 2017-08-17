/**
 * The utility class for date and time.
 * 
 * @screen core
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.List;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.OfficeResource;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst;

/**
 * 
 * The user's utility class..
 */
public final class UserManager {

    /** User Information. */
    private UserInfo userInfo;

    /**
     * <p>
     * The Constructors Method.
     * </p>
     * 
     * @param pUserInfo the user information in session
     */
    private UserManager(UserInfo pUserInfo) {
        this.userInfo = pUserInfo;
    }

    /**
     * 
     * The Constructors Method.
     *
     */
    public UserManager() {}

    /**
     * <p>
     * Get a instance of Context.
     * </p>
     * 
     * @param session the session
     * @return a instance of Context
     */
    public static UserManager getLocalInstance(SessionInfoManager session) {

        if (session != null) {
            return new UserManager(session.getLoginInfo());
        } else {
            return new UserManager(null);
        }
    }

    /**
     * <p>
     * Get a instance of Context.
     * </p>
     * 
     * @param userInfo the userInfo
     * @return a instance of Context
     */
    public static UserManager getLocalInstance(UserInfo userInfo) {

        return new UserManager(userInfo);
    }

    /**
     * <p>
     * Get a instance of Context.
     * </p>
     * 
     * @param officeId the screenId
     */
    public void changeCurrentOffice(Integer officeId) {
        this.userInfo.setOfficeId(officeId);
        this.userInfo.setCurrentOffice(this.getOfficeInfoByOfficeId(officeId));
        this.userInfo.setVvAllFlag(getAllVVFlag());
        this.userInfo.setAisinAllFlag(getAllAisinFlag());
    }

    /**
     * <p>
     * Get user Information.
     * <p>
     * 
     * @return userInfo
     */
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    /**
     * Get Resource list by office id.
     * 
     * @return the resource list for current office
     */
    public List<OfficeResource> getResourceByOffice() {

        // set login
        UserOffice office = this.getCurrentOfficeInfo();
        if (office != null) {

            // if office is not null
            return office.getResourceList();
        }
        return null;
    }

    /**
     * Get Resource list by office id.
     * 
     * @return current office information
     */
    public UserOffice getCurrentOfficeInfo() {

        // current office is exist or not
        if (userInfo.getCurrentOffice() == null) {

            // get office
            UserOffice currentOffice = getOfficeInfoByOfficeId(userInfo.getOfficeId());

            // set into current office
            userInfo.setCurrentOffice(currentOffice);

            // return
            return currentOffice;
        } else {

            return userInfo.getCurrentOffice();
        }

    }

    /**
     * Get the office information by office id.
     * 
     * @param officeId officeId
     * @return office
     */
    public UserOffice getOfficeInfoByOfficeId(Integer officeId) {
        // set login
        if (this.userInfo != null && this.userInfo.getUserOffice() != null && !this.userInfo.getUserOffice().isEmpty()) {

            // get office Resource
            for (UserOffice office : this.userInfo.getUserOffice()) {

                // get return value
                if (office.getOfficeId().equals(officeId)) {
                    return office;
                }
            }
        }

        return null;
    }

    /**
     * Get the office information by office code.
     * 
     * @param officeCode officeCode
     * @return office
     */
    public UserOffice getOfficeInfoByOfficeCode(String officeCode) {
        // set login
        if (this.userInfo != null && this.userInfo.getUserOffice() != null && !this.userInfo.getUserOffice().isEmpty()) {

            // get office Resource
            for (UserOffice office : this.userInfo.getUserOffice()) {

                // get return value
                if (office.getOfficeCode().equals(officeCode)) {
                    return office;
                }
            }
        }

        return null;
    }

    /**
     * Check office exist for current user.
     * 
     * @param chkOffice office
     * 
     * @return check exist
     */
    public boolean checkOfficeForUser(Integer chkOffice) {

        // set login
        if (this.userInfo != null && this.userInfo.getUserOffice() != null && !this.userInfo.getUserOffice().isEmpty()) {

            // get office Resource
            for (UserOffice office : this.userInfo.getUserOffice()) {

                // get return value
                if (office.getOfficeId().equals(chkOffice)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get Resource list by office id.
     * 
     * @param mainResourceId the access level control screen
     * @return current office information
     */
    public Integer getMaxAccessLevel(String mainResourceId) {

        Integer maxAccessLevel = CodeConst.AccessLevel.NONE;

        // get all resources for current office
        if (this.userInfo.getUserOffice() == null) {
            return maxAccessLevel;
        }

        // lopp all office
        for (UserOffice office : this.userInfo.getUserOffice()) {

            // office
            Integer officeAccess = CodeConst.AccessLevel.NONE;

            // get office resource
            List<OfficeResource> resources = office.getResourceList();

            // loop resource
            if (resources != null && !resources.isEmpty()) {
                // get office Resource
                for (OfficeResource resource : resources) {
                    if (resource != null) {
                        // get return value
                        if (resource.getResourceId().equals(mainResourceId)) {
                            // get office access level
                            officeAccess = resource.getAccessLevel();
                            break;
                        }
                    }
                }
            }

            // if inactive office, only low
            if (office.getInactiveFlag() == null || office.getInactiveFlag().equals(CodeConst.InactiveFlag.INACTIVE)) {
                // if inactive office, only low
                if (officeAccess.compareTo(CodeConst.AccessLevel.LOW) > 0) {
                    officeAccess = CodeConst.AccessLevel.LOW;
                }
            }

            // if max
            if (maxAccessLevel.compareTo(officeAccess) < 0) {
                maxAccessLevel = officeAccess;
            }
        }

        return maxAccessLevel;
    }

    /**
     * Get Resource list by office id.
     * 
     * @param mainResourceId the access level control screen
     * @return current office information
     */
    public Integer getAccessLevel(String mainResourceId) {

        Integer accessLevel = CodeConst.AccessLevel.NONE;

        // get all resources for current office
        List<OfficeResource> resources = this.getResourceByOffice();

        // loop resource
        if (resources != null && !resources.isEmpty()) {
            // get office Resource
            for (OfficeResource resource : resources) {
                if (resource != null) {
                    // get return value
                    if (resource.getResourceId().equals(mainResourceId)) {
                        accessLevel = resource.getAccessLevel();
                        break;
                    }
                }
            }
        }

        // if inactive office, only low
        UserOffice office = this.getCurrentOfficeInfo();
        if (office.getInactiveFlag() == null || office.getInactiveFlag().equals(CodeConst.InactiveFlag.INACTIVE)) {
            // if inactive office, only low
            if (accessLevel.compareTo(CodeConst.AccessLevel.LOW) > 0) {
                accessLevel = CodeConst.AccessLevel.LOW;
            }
        }

        return accessLevel;
    }

    /**
     * Get Resource list by office id.
     * 
     * @param mainResourceId the access level control screen
     * @param officeId officeId
     * @return current office information
     */
    public Integer getAccessLevel(String mainResourceId, Integer officeId) {

        Integer accessLevel = CodeConst.AccessLevel.NONE;

        // get all resources for current office
        UserOffice office = this.getOfficeInfoByOfficeId(officeId);
        if (office == null) {
            return accessLevel;
        }

        // get office resource
        List<OfficeResource> resources = office.getResourceList();

        // loop resource
        if (resources != null && !resources.isEmpty()) {
            // get office Resource
            for (OfficeResource resource : resources) {
                if (resource != null) {
                    // get return value
                    if (resource.getResourceId().equals(mainResourceId)) {
                        accessLevel = resource.getAccessLevel();
                        break;
                    }
                }
            }
        }

        // if inactive office, only low
        if (office.getInactiveFlag() == null || office.getInactiveFlag().equals(CodeConst.InactiveFlag.INACTIVE)) {
            // if inactive office, only low
            if (accessLevel.compareTo(CodeConst.AccessLevel.LOW) > 0) {
                accessLevel = CodeConst.AccessLevel.LOW;
            }
        }

        return accessLevel;
    }

    /**
     * Check auth.
     * 
     * @param mainResourceId the access level control screen
     * @param screenId screenId
     * @param authCode authCode
     * @return current office information
     */
    public boolean isHasAuth(String mainResourceId, String screenId, String authCode) {

        return chkAuthByAuthCode(mainResourceId, screenId, authCode, this.getAccessLevel(mainResourceId));
    }

    /**
     * Check auth for all office.
     * 
     * @param mainResourceId the access level control screen
     * @param screenId screenId
     * @param authCode authCode
     * @return current office information
     */
    public boolean isHasAuthByOffice(String mainResourceId, String screenId, String authCode) {

        return chkAuthByAuthCode(mainResourceId, screenId, authCode, this.getMaxAccessLevel(mainResourceId));
    }

    /**
     * Check auth by office.
     * 
     * @param mainResourceId the access level control screen
     * @param screenId screenId
     * @param officeId officeId
     * @param authCode authCode
     * @return current office information
     */
    public boolean isHasAuthByOffice(String mainResourceId, String screenId, Integer officeId, String authCode) {

        return chkAuthByAuthCode(mainResourceId, screenId, authCode, this.getAccessLevel(mainResourceId, officeId));
    }

    /**
     * Check auth by office.
     * 
     * @param mainResourceId the access level control screen
     * @param screenId screenId
     * @param officeCode officeCode
     * @param authCode authCode
     * @return current office information
     */
    public boolean isHasAuthByOffice(String mainResourceId, String screenId, String officeCode, String authCode) {

        // get user office
        UserOffice office = this.getOfficeInfoByOfficeCode(officeCode);
        if (office == null) {
            return false;
        }

        // do check
        return chkAuthByAuthCode(mainResourceId, screenId, authCode,
            this.getAccessLevel(mainResourceId, office.getOfficeId()));
    }

    /**
     * Check auth by auth code.
     * 
     * @param mainResourceId the access level control screen
     * @param screenId screenId
     * @param authCode authCode
     * @param accessLevel accessLevel
     * @return current office information
     */
    public boolean chkAuthByAuthCode(String mainResourceId, String screenId, String authCode, Integer accessLevel) {

        // get current authcode accesslevel
        Integer minAccessLevel = ScreenInfoManager.getMinAccessLevel(mainResourceId, screenId, authCode);

        // if no min accesslevel
        if (minAccessLevel == null) {
            return false;
        }

        // if low
        if (accessLevel.compareTo(minAccessLevel) < 0) {
            return false;
        }

        return true;
    }

    /**
     * Get V-V Flag from user information.
     * 
     * @return current office information
     */
    public boolean getVVFlag() {
        boolean flag = false;
        if (userInfo != null) {
            List<UserOffice> userOfficeList = userInfo.getUserOffice();
            for (UserOffice uo : userOfficeList) {
                if (userInfo.getOfficeId().equals(uo.getOfficeId())) {
                    flag = uo.getVvFlag();
                }
            }
        }
        return flag;
    }

    /**
     * Get AISIN Flag from user information.
     * 
     * @return current office information
     */
    public boolean getAisinFlag() {
        boolean flag = false;
        if (userInfo != null) {
            List<UserOffice> userOfficeList = userInfo.getUserOffice();
            for (UserOffice uo : userOfficeList) {
                if (userInfo.getOfficeId().equals(uo.getOfficeId())) {
                    flag = uo.getAisinFlag();
                }
            }
        }
        return flag;
    }

    /**
     * Get all V-V Flag from user information.
     * 
     * @return current office information
     */
    public boolean getAllVVFlag() {
        boolean flag = false;
        if (userInfo != null) {
            List<UserOffice> userOfficeList = userInfo.getUserOffice();
            for (UserOffice uo : userOfficeList) {
                if (uo.getVvFlag()) {
                    flag = uo.getVvFlag();
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * Get all AISIN Flag from user information.
     * 
     * @return current office information
     */
    public boolean getAllAisinFlag() {
        boolean flag = false;
        if (userInfo != null) {
            List<UserOffice> userOfficeList = userInfo.getUserOffice();
            for (UserOffice uo : userOfficeList) {
                if (uo.getAisinFlag()) {
                    flag = uo.getAisinFlag();
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * get Current BusPattern list
     * 
     * @return current office information
     */
    public List<BusinessPattern> getCurrentBusPattern() {
        List<BusinessPattern> bplist = new ArrayList<BusinessPattern>();
        List<UserOffice> uoList = userInfo.getUserOffice();
        for (UserOffice uo : uoList) {
            if (userInfo.getOfficeId().equals(uo.getOfficeId())) {
                List<BusinessPattern> uobplist = uo.getBusinessPatternList();
                if (uobplist != null && uobplist.size() > 0) {
                    bplist.addAll(uobplist);
                }
                break;
            }
        }
        return bplist;
    }

    /**
     * get Current list
     * 
     * @return current information
     */
    public List<BusinessPattern> getCurrentForAll() {
        List<BusinessPattern> bplist = new ArrayList<BusinessPattern>();
        List<UserOffice> uoList = userInfo.getUserOffice();
        for (UserOffice uo : uoList) {
            List<BusinessPattern> uobplist = uo.getBusinessPatternList();
            if (uobplist != null && uobplist.size() > 0) {
                bplist.addAll(uobplist);
            }
        }
        return bplist;
    }

}
