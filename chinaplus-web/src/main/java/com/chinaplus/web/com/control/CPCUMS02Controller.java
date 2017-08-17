/**
 * Controller of Role  Detail Screen.
 * 
 * @screen CPCUMS02
 * @author zhang_chi
 */
package com.chinaplus.web.com.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCUMS02ComboEntity;
import com.chinaplus.web.com.entity.CPCUMS02DetailEntity;
import com.chinaplus.web.com.entity.CPCUMS02Entity;
import com.chinaplus.web.com.service.CPCUMS02Service;

/**
 * Controller of Role  Detail Screen
 */
@Controller
public class CPCUMS02Controller extends BaseController {

    /**
     * YMCRDS01Service.
     */
    @Autowired
    private CPCUMS02Service cpcums02Service;

    /**
     * Get role infomation from database.
     * 
     * @param param param
     * @param request request
     * @return TnmRole
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS02/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<TnmUser> getUserInfo(@RequestBody BaseParam param, HttpServletRequest request) throws Exception {

        // get user Id
        String userId = StringUtil.toSafeString(param.getSwapData().get("userId"));

        // if is empty, create new user
        if (StringUtil.isEmpty(userId)) {
            return new BaseResult<TnmUser>();
        }

        // check user is owner or not 
        if (userId.equalsIgnoreCase("owner")) {
            userId = this.getLoginUser(request).getUserId().toString();
            param.setSwapData("userId", userId);
        }

        // if user id is not OK
        if (!StringUtil.isNumeric(userId)) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // get user information by roleId
        TnmUser userInfo = cpcums02Service.getOneByParam("getUserDetail", param);

        // user is not exist
        if (userInfo == null) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // set user info
        BaseResult<TnmUser> result = new BaseResult<TnmUser>();
        result.setData(userInfo);

        return result;
    }

    /**
     * get detail for screen YMCUDS01 by filter.
     * 
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS02/getUserRoleDetail", method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCUMS02DetailEntity> getUserOfficeRoleDetail(@RequestBody BaseParam param)
        throws Exception {
      
        // get data
        PageResult<CPCUMS02DetailEntity> result = cpcums02Service.getAllList(param);

        return result;
    }

    /**
     * update detail for screen CPCUMS02.
     * 
     * @param request request
     * @param param param
     * @return RoleEntity
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS02/updateDetail", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> updateDetail(HttpServletRequest request, @RequestBody ObjectParam<CPCUMS02Entity> param)
        throws Exception {

        
//        maxLengthValidator(param.getData().getLoginId(), NumberConst.IntDef.INT_TWENTY, "CPCUMS02_Label_LoginId", true);
//        maxLengthValidator(param.getData().getUserName(), NumberConst.IntDef.INT_FIFTY, "CPCUMS02_Label_UserName", true);
//        maxLengthValidator(param.getData().getMailAddr(), NumberConst.IntDef.INT_TWO_HUNDRED, "CPCUMS02_Label_MailAddress", false);
//        requiredValidator(param.getData().getDefaultLang(), "CPCUMS02_Label_DefaultLang");
//        requiredValidator(param.getData().getDefaultOfficeId(), "CPCUMS02_Label_DefaultOffice");
//        requiredValidator(param.getData().getStatus(), "CPCUMS02_Label_Status");
//
//        // if no role
//        if (param.getData().getUserOfficeRole() == null || param.getData().getUserOfficeRole().isEmpty()) {
//            this.addErrorMessage(null, MessageManager.getMessage("validator.message.empty")); //TODO
//        }
        
        
        // set detail
        setCommonParam(param, request);

        // do update
        TnmUser user = cpcums02Service.doUpdateUserDetail(param);

        // result
        BaseResult<Integer> result = new BaseResult<Integer>();
        result.setData(user.getUserId());

        return result;
    }
    
    /**
     * reset password.
     * 
     * 
     * @param param param
     * @param request request
     * @return version
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS02/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> resetPassword(@RequestBody ObjectParam<CPCUMS02Entity> param, HttpServletRequest request) throws Exception {

        // user is not exist
        if (param.getData() == null || param.getData().getUserId() == null) {
            throw new BusinessException(MessageCodeConst.W1022);
        }
        
        // set common
        this.setCommonParam(param, request);
        
        // do reset
        Integer version = cpcums02Service.doResetPassword(param);
        
        // return
        BaseResult<Integer> result = new BaseResult<Integer>();
        result.setData(version);
        
        return result;
    }
    
    /**
     * load  uesr  Status.
     * 
     * @param request request
     * @param param  param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS02/loadStatus",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCUMS02ComboEntity> loadStatus(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
      
        String userId =  (String) param.getSwapData().get("userId"); 
        UserInfo userInfo = getLoginUser(request);  
        List<CPCUMS02ComboEntity>  statusList  = cpcums02Service.loadStatus(userId,userInfo.getDefaultLang());                     
        PageResult<CPCUMS02ComboEntity> result = new PageResult<CPCUMS02ComboEntity>();
        result.setDatas(statusList);               
        return result;
    }
    
    /**
     * Load All office customers.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all office customers
     */
    @RequestMapping(value = "com/CPCUMS02/loadAllCustomersForId",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCUMS02ComboEntity> loadCustomersForId(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = new BaseParam();
        setCommonParam(param, request);
        // id : customer id, text : customer code
        PageResult<CPCUMS02ComboEntity> result = new PageResult<CPCUMS02ComboEntity>();
        List<CPCUMS02ComboEntity> comboList = cpcums02Service.loadAllCustomers();        
        result.setDatas(comboList);

        return result;
    }
    
    /**
     * Load All office .
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all office 
     */
    @RequestMapping(value = "com/CPCUMS02/loadAllOfficesForId",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCUMS02ComboEntity> loadOfficesForId(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = new BaseParam();
        setCommonParam(param, request);
        // id : customer id, text : customer code
        PageResult<CPCUMS02ComboEntity> result = new PageResult<CPCUMS02ComboEntity>();
        List<CPCUMS02ComboEntity> comboList = cpcums02Service.loadAllOffice();        
        result.setDatas(comboList);

        return result;
    }
    
}
