/**
 * @screen CPCUMS01
 * @author zhang_chi
 */
package com.chinaplus.web.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;

/**
 * YMCULS01Service.
 */
@Service
public class CPCUMS01Service extends BaseService {

    /**
     * Delete role by roleId.
     * 
     * @param param param
     * @throws Exception e
     */
    public void doDeleteUser(ObjectParam<TnmUser> param) throws Exception {

        // get user ids
        List<TnmUser> users = param.getDatas();

        // Required Check. Must be selected at least one item to process.
        if (null == users || users.isEmpty()) {
            throw new BusinessException(MessageCodeConst.W1011);
        }

        // message
        List<BaseMessage> messages = new ArrayList<BaseMessage>();

        // do check
        for (TnmUser scnUser : users) {

            // get role from database
            TnmUser user = baseDao.findOne(TnmUser.class, scnUser.getUserId());

            // if role is not exist or version is not ok
            if (user == null || !user.getVersion().equals(scnUser.getVersion())) {

                // set message
                messages.add(new BaseMessage(MessageCodeConst.W1006,new String[] {"delete"}));
                messages.add(new BaseMessage(MessageCodeConst.W1002_005, new String[] { "CPCUMS01_Grid_LoginId",
                    scnUser.getLoginId() }));

                // next
                continue;
            }

            // if role is not exist or version is not ok
            if (user.getStatus().equals(CodeConst.User.Status.INACTIVE)) {

                // set message
                messages.add(new BaseMessage(MessageCodeConst.W1006,new String[] {"delete"}));
                messages.add(new BaseMessage(MessageCodeConst.W1006_015, new String[] {
                    user.getLoginId(),
                    CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.USER_STATUS,
                        user.getStatus()) }));

                // next
                continue;
            }

            // lock
            baseDao.lock(user);

            // update
            user.setStatus(CodeConst.User.Status.INACTIVE);
            user.setUpdatedBy(param.getLoginUserId());
            user.setUpdatedBy(param.getLoginUserId());
            user.setUpdatedDate(getDBDateTimeByDefaultTimezone());
            user.setVersion(user.getVersion() + 1);

            // save
            baseDao.update(user);

        }

        // if has error throw
        if (!messages.isEmpty()) {
            throw new BusinessException(messages);
        }
    }

}
