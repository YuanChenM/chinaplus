/**
 * @screen YMCUDS02
 * @author zhang_chi
 */
package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.entity.TnmUserOfficeRole;
import com.chinaplus.common.util.EncryptManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.web.com.entity.CPCUMS02ComboEntity;
import com.chinaplus.web.com.entity.CPCUMS02DetailEntity;
import com.chinaplus.web.com.entity.CPCUMS02Entity;

/**
 * Controller of Role List Screen.
 */
@Service
public class CPCUMS02Service extends BaseService {

    /**
     * update Role Detail.
     * 
     * @param param role information
     * @return TnmRole role Info
     * @throws Exception e
     */
    public TnmUser doUpdateUserDetail(ObjectParam<CPCUMS02Entity> param) throws Exception {

        // get data
        CPCUMS02Entity scnUser = param.getData();

        // check mail
//        String mailAddr = scnUser.getMailAddr();
//        if (!StringUtil.isNullOrEmpty(mailAddr)) {
//            boolean mailFlag = ValidatorUtils.checkMail(mailAddr);
//            if (!mailFlag) {
//                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_028);
//                message.setMessageArgs(new String[] { "CPCUMS02_Label_MailAddress" });
//                throw new BusinessException(message);
//            }
//        }

        // same name check
        if (scnUser.getUserId() == null) {

            // check login repeat
            Object[] sameParam = new Object[] { scnUser.getLoginId() };
            List<TnmUser> sameUser = baseDao.select("from TNM_USER where loginId = ?", sameParam);

            // if exists
            if (sameUser != null && sameUser.size() > 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_015);
                message.setMessageArgs(new String[] { "CPCUMS02_Label_LoginId" ,
                    "TNM_USER" });
                throw new BusinessException(message);
            }
        }

        // user information
        TnmUser user = null;
        // get database time
        Timestamp time = this.getDBDateTimeByDefaultTimezone();

        // do master data save
        if (scnUser.getUserId() == null) {

            // get new user
            user = new TnmUser();

            TnmOffice office = baseDao.findOne(TnmOffice.class, scnUser.getDefaultOfficeId());
            // set date
            // user.setuserId(scnuser.getRoleId());
            user.setLoginId(scnUser.getLoginId());
            user.setUserName(scnUser.getUserName());
            user.setMailAddr(scnUser.getMailAddr());
            user.setDefaultLang(scnUser.getDefaultLang());
            user.setDefaultOfficeId(scnUser.getDefaultOfficeId());         
            user.setStatus(scnUser.getStatus());                      
            user.setPwd(EncryptManager.encrypt(user.getLoginId()));
            user.setExpiryDate(this.getDBDateTime(office.getTimeZone()));
            user.setCreatedBy(param.getLoginUserId());
            user.setUpdatedBy(param.getLoginUserId());
            user.setVersion(NumberConst.IntDef.INT_ONE);
            user.setCreatedDate(time);
            user.setUpdatedDate(time);

            // if add
            baseDao.insert(user);
        } else {

            // check version
            user = baseDao.findOne(TnmUser.class, scnUser.getUserId());

            // if role already delete or role has been update by other user
            if (user == null || !user.getVersion().equals(scnUser.getVersion())) {
                throw new BusinessException(MessageCodeConst.W1022);
            }

            // set
            user.setUserName(scnUser.getUserName());
            user.setMailAddr(scnUser.getMailAddr());
            user.setDefaultLang(scnUser.getDefaultLang());
            user.setDefaultOfficeId(scnUser.getDefaultOfficeId());
            if(null != scnUser.getStatus() && user.getStatus() != CodeConst.User.Status.ACTIVE){
                user.setStatus(scnUser.getStatus());
                if (scnUser.getStatus() == CodeConst.User.Status.ACTIVE){
                	user.setMissCount(NumberConst.IntDef.INT_ZERO);
                }
            }   
            user.setVersion(user.getVersion() + 1);
            user.setUpdatedBy(param.getLoginUserId());
            user.setUpdatedDate(time);

            // role
            baseDao.lock(user);
            baseDao.update(user);
        }

        // set role detail information
        List<CPCUMS02DetailEntity> scrDetailLst = scnUser.getUserOfficeRole();
        List<TnmUserOfficeRole> userRoleLst = new ArrayList<TnmUserOfficeRole>();
        List<TnmUserOfficeRole> userRoleListHistory = new ArrayList<TnmUserOfficeRole>();
        List<Integer> userRoleOfficeIdList = new ArrayList<Integer>();
        List<Integer> delectedRoleList = new ArrayList<Integer>();
        for (CPCUMS02DetailEntity detail : scrDetailLst) {

            // copy user
            TnmUserOfficeRole officeRole = new TnmUserOfficeRole();

            // role
            if (detail.getUserOfficeRoleId() == null || detail.getUserOfficeRoleId() == NumberConst.IntDef.INT_N_ONE) {
                // add new database
                officeRole.setCreatedDate(time);
                officeRole.setCreatedBy(param.getLoginUserId());
                officeRole.setUpdatedDate(time);
                officeRole.setUpdatedBy(param.getLoginUserId());
            } else {
                // get from database
                TnmUserOfficeRole dbUser = baseDao.findOne(TnmUserOfficeRole.class, detail.getUserOfficeRoleId());
                officeRole.setCreatedDate(dbUser.getCreatedDate());
                officeRole.setCreatedBy(dbUser.getCreatedBy());
                officeRole.setUpdatedDate(time);
                officeRole.setUpdatedBy(param.getLoginUserId());
            }

            // set into detail information
            officeRole.setUserId(user.getUserId());
            officeRole.setRoleId(detail.getRoleId());
            officeRole.setOfficeId(detail.getOfficeId());
            officeRole.setVersion(NumberConst.IntDef.INT_ONE);

            userRoleLst.add(officeRole);
        }
        
        // delete user office role if ok.
        if (scnUser.getUserId() != null) {
        	BaseParam params = new BaseParam();
        	params.setSwapData("userId", scnUser.getUserId());
        	userRoleListHistory = this.baseMapper.select(this.getSqlId("getRoleOfficeByUser"), params);
            this.baseMapper.delete(this.getSqlId("deleteRoleOfficeByUser"), scnUser);
            for (TnmUserOfficeRole role : userRoleLst){
            	userRoleOfficeIdList.add(role.getOfficeId());
            }
            // get the delected office role list for user
            for (int i = 0; i < userRoleListHistory.size(); i++ ){
            	TnmUserOfficeRole role = userRoleListHistory.get(i);
            	if (!userRoleOfficeIdList.contains(role.getOfficeId())){
            		delectedRoleList.add(role.getOfficeId());
            	}
            }
            // delect customer under the role
            if (delectedRoleList != null && delectedRoleList.size() > 0){
            	BaseParam roleListParam = new BaseParam();
            	roleListParam.setSwapData("delectedRoleList", delectedRoleList);
            	roleListParam.setSwapData("userId", scnUser.getUserId());
            	this.baseMapper.delete(this.getSqlId("deleteCustomerByRoleOfficeandUser"), roleListParam);
            }
        }

        // insert
        for (TnmUserOfficeRole role : userRoleLst) {
            baseDao.insert(role);
        }
        return user;
    }

    /**
     * update Role Detail.
     * 
     * @param param role information
     * @return TnmRole role Info
     * @throws Exception e
     */
    public Integer doResetPassword(ObjectParam<CPCUMS02Entity> param) {

        // check version
        TnmUser user = baseDao.findOne(TnmUser.class, param.getData().getUserId());

        // if role already delete or role has been update by other user
        if (user == null || !user.getVersion().equals(param.getData().getVersion())) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // lock
        baseDao.lock(user);

        // get time
        Timestamp time = this.getDBDateTimeByDefaultTimezone();
        TnmOffice office = baseDao.findOne(TnmOffice.class, user.getDefaultOfficeId());
        // reset password
        user.setPwd(EncryptManager.encrypt(user.getLoginId()));
        user.setExpiryDate(this.getDBDateTime(office.getTimeZone()));

        // set
        user.setUpdatedBy(param.getLoginUserId());
        user.setUpdatedDate(time);
        user.setVersion(user.getVersion() + 1);
        user.setMissCount(0);

        // update save
        baseDao.update(user);

        // return version
        return user.getVersion();
    }

    /**
     * Get uesr Status
     * 
     * @param userId userId
     * @param language language
     * @return List<ComboData> comboDataList
     */
    public List<CPCUMS02ComboEntity> loadStatus(String userId, Integer language) {
        BaseParam param = new BaseParam();
        param.setSwapData("language", language);
        List<Integer> idList = new ArrayList<Integer>();
        idList.add(IntDef.INT_ONE);
        idList.add(IntDef.INT_NINE);
        if (!userId.equals("undefined")) {
            idList.add(IntDef.INT_ZERO);
        }
        param.setSwapData("idList", idList);
        List<CPCUMS02ComboEntity> comboDataList = baseMapper.select(this.getSqlId("loadStatus"), param);
        return comboDataList;
    }

    /**
     * Get All customers
     * 
     * @return List<CPCUMS02ComboEntity> customers
     */
	public List<CPCUMS02ComboEntity> loadAllCustomers() {
        BaseParam param = new BaseParam();
		List<CPCUMS02ComboEntity> result =  baseMapper.select(this.getSqlId("loadAllCustomers"), param);
		return result;
	}
	
    /**
     * Get All Office
     * 
     * @return List<CPCUMS02ComboEntity> Offices
     */
	public List<CPCUMS02ComboEntity> loadAllOffice() {
        BaseParam param = new BaseParam();
		List<CPCUMS02ComboEntity> result =  baseMapper.select(this.getSqlId("loadAllOffice"), param);
		return result;
	}

}
