/**
 * CPCRPS01Service.java
 * 
 * @screen CPCRPS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.util.EncryptManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Controller of Role List Screen.
 */
@Service
public class CPCRPS01Service extends BaseService {

    /**
     * reset password.
     * 
     * @param param role information
     * @return version
     * @throws Exception e
     */
    public Integer doResetPassword(BaseParam param) {

        // get password
        String oldPassword = StringUtil.toSafeString(param.getSwapData().get("oldPassword"));
        String newPassword = StringUtil.toSafeString(param.getSwapData().get("newPassword"));
        Integer version = StringUtil.toInteger(param.getSwapData().get("version"));

        // check version
        TnmUser user = baseDao.findOne(TnmUser.class, param.getLoginUserId());

        // if role already delete or role has been update by other user
        if (user == null || (version != null && !user.getVersion().equals(version))) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // if pass word is not match
        if (!EncryptManager.match(oldPassword, user.getPwd())) {

            // create message
            List<BaseMessage> messageList = new ArrayList<BaseMessage>();
            messageList.add(new BaseMessage(MessageCodeConst.W1003));
            messageList.add(new BaseMessage(MessageCodeConst.W1003_016));

            // output
            throw new BusinessException(messageList);
        }

        // get time
        Timestamp time = this.getDBDateTime(param.getOfficeTimezone());

        // reset password
        user.setPwd(EncryptManager.encrypt(newPassword));
        user.setExpiryDate(DateTimeUtil.add(time, 0, NumberConst.IntDef.INT_THREE, 0, 0, 0, 0));

        // set
        user.setUpdatedBy(param.getLoginUserId());
        user.setUpdatedDate(this.getDBDateTimeByDefaultTimezone());
        user.setVersion(user.getVersion() + 1);

        // lock
        baseDao.lock(user);

        // update save
        baseDao.update(user);

        // return version
        return user.getVersion();
    }

}
