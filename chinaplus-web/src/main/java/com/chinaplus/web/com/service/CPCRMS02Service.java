/**
 * CPCRMS02Service.java
 * 
 * @screen CPCRMS02
 * @author shi_yuxi
 */
package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.common.entity.TnmRoleResource;
import com.chinaplus.common.entity.TnmRoleResourcePK;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.web.com.entity.CPCRMS02DetailEntity;
import com.chinaplus.web.com.entity.CPCRMS02Entity;

/**
 * Controller of Role List Screen.
 */
@Service
public class CPCRMS02Service extends BaseService {

    /**
     * update Role Detail.
     * 
     * @param param role information
     * @return TnmRole role Info
     * 
     * @author Common
     * @throws Exception e
     */
    public TnmRole doUpdateRoleDetail(ObjectParam<CPCRMS02Entity> param) throws Exception {

        // get data
        CPCRMS02Entity scnRole = param.getData();

        // same name check
        Object[] sameParam = new Object[] { scnRole.getRoleName(),
            scnRole.getRoleId() == null ? -1 : scnRole.getRoleId() };
        List<TnmRole> sameRole = baseDao.select("from TNM_ROLE where roleName = ? and roleId <> ?", sameParam);

        // if exists
        if (sameRole != null && sameRole.size() > 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_015);
            message.setMessageArgs(new String[]{"CPCRMS02_Label_RoleName", "CPCRMS02_Label_RoleList"});
            throw new BusinessException(message);
        }

        // role information
        TnmRole role = null;
        // get database time
        Timestamp time = this.getDBDateTimeByDefaultTimezone();

        // do master data save
        if (scnRole.getRoleId() == null) {

            // get new role
            role = new TnmRole();

            // set date
            // role.setRoleId(scnRole.getRoleId());
            role.setRoleName(scnRole.getRoleName());
            role.setRoleNotes(scnRole.getRoleNotes());
            role.setCreatedBy(param.getLoginUserId());
            role.setUpdatedBy(param.getLoginUserId());
            role.setVersion(NumberConst.IntDef.INT_ONE);
            role.setCreatedDate(time);
            role.setUpdatedDate(time);

            // if add
            baseDao.insert(role);
        } else {

            // check version
            role = baseDao.findOne(TnmRole.class, scnRole.getRoleId());

            // if role already delete or role has been update by other user
            if (role == null || !role.getVersion().equals(scnRole.getVersion())) {
                throw new BusinessException(MessageCodeConst.W1022);
            }

            // set
            role.setRoleName(scnRole.getRoleName());
            role.setRoleNotes(scnRole.getRoleNotes());
            role.setVersion(role.getVersion() + 1);
            role.setUpdatedBy(param.getLoginUserId());
            role.setUpdatedDate(time);

            // role
            baseDao.update(role);
        }

        // set role detail information
        List<CPCRMS02DetailEntity> scrDetailLst = scnRole.getRoleResource();
        for (CPCRMS02DetailEntity detail : scrDetailLst) {
            // copy role
            TnmRoleResource roleDetail = new TnmRoleResource();
            TnmRoleResourcePK rolePK = new TnmRoleResourcePK();

            // copy PK
            rolePK.setRoleId(role.getRoleId());
            rolePK.setSysScreenId(detail.getSysResourceId());

            // set into detail information

            roleDetail.setId(rolePK);
            roleDetail.setAccessLevel(detail.getAccessLevel());
            // set into list
            // role
            if (scnRole.getRoleId() == null) {
                roleDetail.setCreatedBy(param.getLoginUserId());
                roleDetail.setCreatedDate(time);
                roleDetail.setUpdatedBy(param.getLoginUserId());
                roleDetail.setUpdatedDate(time);
                roleDetail.setVersion(IntDef.INT_ONE);
                baseDao.insert(roleDetail);

            } else {
                String hql = "FROM TNM_ROLE_RESOURCE WHERE ROLE_ID = ? AND SYS_SCREEN_ID = ?";
                Object[] objs = new Object[] { role.getRoleId(), detail.getSysResourceId() };
                TnmRoleResource trr = baseDao.findOne(hql, objs);
                trr.setAccessLevel(detail.getAccessLevel());
                trr.setVersion(trr.getVersion() + 1);
                trr.setUpdatedBy(param.getLoginUserId());
                trr.setUpdatedDate(time);
                baseDao.update(trr);
            }
        }

        return role;
    }
}
