/**
 * CPCRPS01Controller.java
 * 
 * @screen CPCRPS01
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

import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.service.CPCRPS01Service;

/**
 * Controller of Reset password Screen.
 */
@Controller
public class CPCRPS01Controller extends BaseController {

    /**newString */
    private static final String NEWPASSWORD = "newPassword";
    
    /**confPassword */
    private static final String CONFPASSWORD = "confPassword";
    
    /**oldPassword */
    private static final String OLDPASSWORD = "oldPassword";
    
    /**
     * CPCRPS01Service.
     */
    @Autowired
    private CPCRPS01Service cpcrps01Service;

    /**
     * reset password.
     * 
     * @param param param
     * @param request request
     * @throws Exception e
     * @return Integer
     */
    @RequestMapping(value = "manage/CPCRPS01/resetPassword",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> resetPassword(@RequestBody BaseParam param, HttpServletRequest request) throws Exception {
        List<BaseMessage> messageList = new ArrayList<BaseMessage>();

        // do check
        String oldPassword = StringUtil.toSafeString(param.getSwapData().get("oldPassword"));
        String newPassword = StringUtil.toSafeString(param.getSwapData().get("newPassword"));
        String confPassword = StringUtil.toSafeString(param.getSwapData().get("confPassword"));
        // requiredValidator
        requiredValidator(oldPassword, "CPCRPS01_Label_OldPw", messageList);
        requiredValidator(newPassword, "CPCRPS01_Label_NewPw", messageList);
        requiredValidator(confPassword, "CPCRPS01_Label_ConfirmPw", messageList);

        // check
        if (oldPassword.length() > IntDef.INT_THIRTY) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_004);
            message.setMessageArgs(new String[]{OLDPASSWORD, IntDef.INT_THIRTY + StringConst.EMPTY });
            messageList.add(message);
        }
        // check
        if (newPassword.length() > IntDef.INT_THIRTY) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_004);
            message.setMessageArgs(new String[]{ NEWPASSWORD, IntDef.INT_THIRTY + StringConst.EMPTY });
            messageList.add(message);
        }
        // check
        if (confPassword.length() > IntDef.INT_THIRTY) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_004);
            message.setMessageArgs(new String[]{ CONFPASSWORD, IntDef.INT_THIRTY + StringConst.EMPTY });
            messageList.add(message);
        }
        // check
        if (!newPassword.equals(confPassword)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_017);
            messageList.add(message);
        }

        // check
        if (oldPassword.equals(newPassword)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_018);
            messageList.add(message);
        }
        if (messageList.size() != 0) {
            throw new BusinessException(messageList);
        }

        // set common
        this.setCommonParam(param, request);

        Integer version = cpcrps01Service.doResetPassword(param);

        // return
        BaseResult<Integer> result = new BaseResult<Integer>();
        result.setData(version);

        return result;
    }

    /**
     * Do required validation.
     * 
     * @param value validate value
     * @param fieldName validate fieldName
     * @param list messageList
     * @return true is check ok
     */
    public boolean requiredValidator(Object value, String fieldName, List<BaseMessage> list) {
        boolean result = true;
        if (value == null) {
            result = false;
        } else if (value instanceof String && StringUtil.isEmpty((String) value)) {
            result = false;
        }

        if (!result) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { fieldName });
            list.add(message);
        }
        return result;
    }
}
