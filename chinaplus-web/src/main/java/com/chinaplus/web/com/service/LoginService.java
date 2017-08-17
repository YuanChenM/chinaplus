package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.OfficeResource;
import com.chinaplus.common.bean.UserAccessInfo;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst.User;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * LoginService.
 */
@Service
public class LoginService extends BaseService {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    /**
     * check the login info is validate.
     * 
     * @param loginUser loginUser
     * @param messageEntity messageEntity
     * @return login user
     * @throws Exception Exception
     * 
     */
    public boolean doCheckLoginInfo(UserInfo loginUser, BaseMessage messageEntity) throws Exception {

        logger.debug("userCheck begin");

        // login id is blank
        if (StringUtil.isEmpty(loginUser.getLoginId())) {

            messageEntity.setMessageCode(MessageCodeConst.W1017);
            return false;
        }
        // password is blank
        if (StringUtil.isEmpty(loginUser.getPwd())) {

            messageEntity.setMessageCode(MessageCodeConst.W1019);
            return false;
        }

        // login id's length is over limit
        if (StringUtil.countLengthByByte(loginUser.getLoginId()) > IntDef.INT_TWENTY) {

            messageEntity.setMessageCode(MessageCodeConst.W1017);
            return false;
        }

        // find user by login id
        TnmUser userInfo = baseDao
            .findOne("FROM TNM_USER A WHERE A.loginId=?", new Object[] { loginUser.getLoginId() });

        // Login id does not exist.
        if (userInfo == null) {

            messageEntity.setMessageCode(MessageCodeConst.W1017);
            return false;
        } else if (userInfo.getStatus() != null && userInfo.getStatus().intValue() == User.Status.LOCKED) {

            // User is locked
            messageEntity.setMessageCode(MessageCodeConst.W1018);
            return false;
        } else if (userInfo.getStatus() != null && userInfo.getStatus().intValue() == User.Status.INACTIVE) {

            // user is inactive
            messageEntity.setMessageCode(MessageCodeConst.W1020);
            return false;
        }

        // Expiry
        Timestamp now = this.getDBDateTimeByDefaultTimezone();

        // If password is not correct
        if (!loginUser.getPwd().equals(userInfo.getPwd())) {

            if (userInfo.getMissCount() == null) {
                // reset miss count
                userInfo.setMissCount(0);
            }

            // if miss count is more than 4 times, lock user
            if (userInfo.getMissCount() >= IntDef.INT_FOUR) {
                userInfo.setStatus(User.Status.LOCKED);
            }

            // save miss count
            userInfo.setMissCount(userInfo.getMissCount() + 1);
            userInfo.setLastNgTime(now);
            userInfo.setUpdatedBy(userInfo.getUserId());
            userInfo.setUpdatedDate(now);
            baseDao.saveOrUpdate(userInfo);

            messageEntity.setMessageCode(MessageCodeConst.W1019);
            return false;
        } else {

            // login successful
            userInfo.setMissCount(0);
            userInfo.setLastLoginTime(now);
            baseDao.saveOrUpdate(userInfo);
        }

        // set into entity
        loginUser.setUserId(userInfo.getUserId());
        loginUser.setLoginId(userInfo.getLoginId());
        loginUser.setUserName(userInfo.getUserName());
        loginUser.setStatus(userInfo.getStatus());
        loginUser.setExpiryDate(userInfo.getExpiryDate());
        loginUser.setDefaultOfficeId(userInfo.getDefaultOfficeId());
        loginUser.setDefaultLang(userInfo.getDefaultLang());
        loginUser.setOfficeId(userInfo.getDefaultOfficeId());
        loginUser.setLanguage(MessageManager.getLanguage(userInfo.getDefaultLang()));

        logger.debug("userCheck over");

        // return success
        return true;
    }

    /**
     * login with user login id and password.
     * 
     * 
     * @param userInfo userInfo
     * @throws Exception e
     */
    public void setLoginUserInfo(UserInfo userInfo) throws Exception {

        // get user access level
        BaseParam param = new BaseParam();

        // login user id and project flag
        param.setSwapData("userId", userInfo.getUserId());

        // get user access information
        List<UserAccessInfo> accessInfo = baseMapper.select(this.getSqlId("findByCondition"), param);
        // /userInfo.setAccessInfo(accessInfo);

        // create role Type resource
        Integer officeId = null;
        UserOffice office = null;
        OfficeResource officeResource = null;
        List<UserOffice> officeLst = new ArrayList<UserOffice>();
        List<OfficeResource> resourceLst = null;
        boolean defExist = false;

        for (UserAccessInfo ai : accessInfo) {

            // split role resource by office id
            if (ai.getOfficeId().equals(officeId)) {

                // set office Resource
                officeResource = new OfficeResource();
                officeResource.setSysRootId(ai.getSysRootId());
                officeResource.setRootId(ai.getRootId());
                officeResource.setSysResourceId(ai.getSysResourceId());
                officeResource.setResourceId(ai.getResourceId());
                // officeResource.setResourceUrl(ai.getResourceUrl());
                officeResource.setAccessLevel(ai.getAccessLevel());

                // set into resourceLst
                resourceLst.add(officeResource);

            } else {
                // set old
                if (office != null) {
                    office.setResourceList(resourceLst);
                    officeLst.add(office);
                }

                // set office
                office = new UserOffice();
                office.setOfficeId(ai.getOfficeId());
                office.setOfficeCode(ai.getOfficeCode());
                office.setRegionCode(ai.getRegionCode());
                office.setTimezone(ai.getTimezone());
                office.setInactiveFlag(ai.getInactiveFlag());
                // create new office information
                // set office Resource
                resourceLst = new ArrayList<OfficeResource>();
                officeResource = new OfficeResource();
                officeResource.setSysRootId(ai.getSysRootId());
                officeResource.setRootId(ai.getRootId());
                officeResource.setSysResourceId(ai.getSysResourceId());
                officeResource.setResourceId(ai.getResourceId());
                // officeResource.setResourceUrl(ai.getResourceUrl());
                officeResource.setAccessLevel(ai.getAccessLevel());

                // set into resourceLst
                resourceLst.add(officeResource);

                // reset office Id
                officeId = ai.getOfficeId();

                // check default office is exist or not.
                if (userInfo.getOfficeId().equals(officeId)) {
                    defExist = true;
                }
            }
        }

        // add the lastest data
        if (office != null) {
            office.setResourceList(resourceLst);
            officeLst.add(office);
        }

        // get user business Pattern information
        List<BusinessPattern> businessPatternListFlag = baseMapper.select(this.getSqlId("findBusinessPatternListFlag"),
            param);

        boolean vvAllFlag = false;
        boolean aisinAllFlag = false;
        if (officeLst != null && officeLst.size() > 0) {
            for (UserOffice userOffice : officeLst) {

                Integer officeID = userOffice.getOfficeId();
                if (businessPatternListFlag != null && businessPatternListFlag.size() > 0) {
                    for (BusinessPattern bp : businessPatternListFlag) {
                        if (bp.getOfficeId().equals(officeID)) {
                            Integer allCustomerFlag = bp.getAllCustomerFlag();
                            if (allCustomerFlag == null) {
                                userOffice.setBusinessPatternList(null);
                            } else if (allCustomerFlag == IntDef.INT_ZERO) {
                                param.setSwapData("officeId", officeID);
                                List<BusinessPattern> businessPatternList = baseMapper.select(
                                    this.getSqlId("findBusinessPatternList"), param);
                                userOffice.setBusinessPatternList(businessPatternList);
                            } else if (allCustomerFlag == IntDef.INT_ONE) {
                                param.setSwapData("officeId", officeID);
                                List<BusinessPattern> businessPatternListById = baseMapper.select(
                                    this.getSqlId("findBusinessPatternListById"), param);
                                userOffice.setBusinessPatternList(businessPatternListById);
                            }
                        }
                    }
                }

                Boolean vvFlag = false;
                Boolean aisinFlag = false;
                List<BusinessPattern> businessPList = userOffice.getBusinessPatternList();
                if (businessPList != null && businessPList.size() > 0) {
                    for (BusinessPattern bup : businessPList) {
                        if (bup.getOfficeId().equals(officeID)) {
                            if (bup.getBusinessPattern() == IntDef.INT_ONE) {
                                vvFlag = true;
                                vvAllFlag = true;
                            }
                            if (bup.getBusinessPattern() == IntDef.INT_TWO) {
                                aisinFlag = true;
                                aisinAllFlag = true;
                            }
                        }
                    }
                }

                userOffice.setVvFlag(vvFlag);
                userOffice.setAisinFlag(aisinFlag);
                List<OfficeResource> resourceLists = getOfficeResourceLst(userOffice.getResourceList(), vvFlag,
                    aisinFlag);
                userOffice.setResourceList(resourceLists);
            }
        }

        // add into userInfo
        userInfo.setUserOffice(officeLst);

        // reset current office
        if (!defExist && officeLst.size() > 0) {
            userInfo.setOfficeId(officeLst.get(0).getOfficeId());
        }

        // set all vv aisin
        userInfo.setVvAllFlag(vvAllFlag);
        userInfo.setAisinAllFlag(aisinAllFlag);
    }

    /**
     * get Offic eResource List
     * 
     * @param resourceLst resourceLst
     * @param vvFlag vvFlag
     * @param aisinFlag aisinFlag
     * @return officeResourceLst officeResourceLst
     */
    private List<OfficeResource> getOfficeResourceLst(List<OfficeResource> resourceLst, boolean vvFlag,
        boolean aisinFlag) {
        List<OfficeResource> officeResourceLst = new ArrayList<OfficeResource>();
        for (int i = 0; i < resourceLst.size(); i++) {
            String rootId = resourceLst.get(i).getRootId();
            if (vvFlag && rootId.equals(CoreConst.CPV00000)) {
                officeResourceLst.add(resourceLst.get(i));
            } else if (aisinFlag && rootId.equals(CoreConst.CPA00000)) {
                officeResourceLst.add(resourceLst.get(i));
            } else if (rootId.equals(CoreConst.CPM00000) || rootId.equals(CoreConst.CPC00000)
                    || rootId.equals(CoreConst.VVP0000)) {
                officeResourceLst.add(resourceLst.get(i));
            }
        }
        return officeResourceLst;
    }

}
