/**
 * CPCRMS02Controller.java
 * 
 * @screen CPCRMS02
 * @author shi_yuxi
 */
package com.chinaplus.web.com.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCRMS02DetailEntity;
import com.chinaplus.web.com.entity.CPCRMS02Entity;
import com.chinaplus.web.com.service.CPCRMS02Service;

/**
 * Controller of Role List Screen.
 */
@Controller
public class CPCRMS02Controller extends BaseController {
    /** ROLE_NAME */
    private static final String ROLE_NAME = "CPCRMS01_Grid_RoleName";
    /** ROLE_NOTES */
    private static final String ROLE_NOTES = "CPCRMS01_Grid_RoleNotes";
    /**
     * CPCRMS02Service.
     */
    @Autowired
    private CPCRMS02Service cpcrms02Service;

    /**
     * Get role infomation from database.
     * 
     * @param param param
     * @return TnmRole
     * @throws Exception e
     */
    @RequestMapping(value = "manage/CPCRMS02/getRoleInfo",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<TnmRole> getRoleInfo(@RequestBody BaseParam param) throws Exception {

        // get role Id
        String roleId = StringUtil.toSafeString(param.getSwapData().get("roleId"));

        // if is empty
        if (StringUtil.isEmpty(roleId)) {
            return new BaseResult<TnmRole>();
        }

        // if role id is not ok
        if (!StringUtil.isNumeric(roleId)) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // get role information by roleId
        TnmRole roleInfo = cpcrms02Service.getOneById(TnmRole.class, Integer.valueOf(roleId));

        if (roleInfo == null) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // set role info
        BaseResult<TnmRole> result = new BaseResult<TnmRole>();
        result.setData(roleInfo);

        return result;
    }

    /**
     * get detail for screen CPCRMS02 by filter.
     * 
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "manage/CPCRMS02/getRoleDetail",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCRMS02DetailEntity> getRoleDetail(@RequestBody BaseParam param) throws Exception {

        // get daat
        PageResult<CPCRMS02DetailEntity> result = cpcrms02Service.getAllList(param);

        return result;
    }

    /**
     * update detail for screen CPCRMS02.
     * 
     * @param request request
     * @param param param
     * @return RoleEntity
     * @throws Exception e
     */
    @RequestMapping(value = "manage/CPCRMS02/updateDetail",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> updateDetail(HttpServletRequest request, @RequestBody ObjectParam<CPCRMS02Entity> param)
        throws Exception {

        // do validator
        List<BaseMessage> messageList = new ArrayList<BaseMessage>();

        maxLengthValidator(param.getData().getRoleName(), NumberConst.IntDef.INT_FIFTY, ROLE_NAME, true, messageList);
        maxLengthValidator(param.getData().getRoleNotes(), NumberConst.IntDef.INT_TWO_HUNDRED, ROLE_NOTES, false,
            messageList);

        if (messageList.size() != 0) {
            throw new BusinessException(messageList);
        }

        // set detail
        setCommonParam(param, request);

        // do update
        TnmRole role = cpcrms02Service.doUpdateRoleDetail(param);

        // result
        BaseResult<Integer> result = new BaseResult<Integer>();
        result.setData(role.getRoleId());

        return result;
    }

    /**
     * Check maximum length.
     * 
     * @param value validate value
     * @param maxLength the maxinum length
     * @param fieldName validate fieldName
     * @param required true is required
     * @param list messagetList
     * @return true is check ok
     */
    public boolean maxLengthValidator(String value, int maxLength, String fieldName, boolean required,
        List<BaseMessage> list) {
        if (value == null) {
            return true;
        } else {
            if (value.getBytes().length > maxLength) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_004);
                message.setMessageArgs(new String[] { fieldName, StringUtil.toSafeString(maxLength) });
                list.add(message);
                return false;
            }
        }
        return true;
    }
}
