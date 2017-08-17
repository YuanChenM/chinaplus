/**
 * CPCRMS01Service.java
 * 
 * @screen CPCRMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;

/**
 * CPCRMS01Service.
 */
@Service
public class CPCRMS01Service extends BaseService {

    /**
     * Delete role by roleId.
     * 
     * @param param param
     * @throws Exception e
     */
    public void doDeleteRole(ObjectParam<TnmRole> param) throws Exception {

        // get role ids
        List<TnmRole> roles = param.getDatas();

        // Required Check. Must be selected at least one item to process.
        if (null == roles || roles.isEmpty()) {
            throw new BusinessException(MessageCodeConst.W1011);
        }

        // message
        List<BaseMessage> messages = new ArrayList<BaseMessage>();

        // do check
        for (TnmRole scnRole : roles) {

            // get role from database
            TnmRole role = baseDao.findOne(TnmRole.class, scnRole.getRoleId());

            // if role is not exist or version is not matched
            if (role == null || !role.getVersion().equals(scnRole.getVersion())) {
                messages.add(new BaseMessage(MessageCodeConst.W1002_005, new String[] { "CPCRMS01_Grid_RoleName",
                    scnRole.getRoleName() }));
            } else {
                baseDao.lock(role);
            }
        }

        // if has error throw
        if (!messages.isEmpty()) {
            throw new BusinessException(messages);
        }

        // delete role
        baseMapper.delete("deleteRole", param);

        // delete Role Resource
        baseMapper.delete("deleteRoleResource", param);

        // delete Role Resource
        baseMapper.delete("deleteUserOfficeRole", param);

    }

}
