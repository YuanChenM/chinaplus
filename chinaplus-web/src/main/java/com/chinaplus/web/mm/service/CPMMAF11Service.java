/**
 * CPMMAF11Service.java
 * 
 * @screen CPMMAF11
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMMAF01Entity;

/** 
 * email alert upload.
 */ 
@Service
public class CPMMAF11Service extends BaseService {
    
    /**
     * getCustomer office related
     * 
     * @param param param
     * @return count
     */
    public int getCusOffice(BaseParam param)
    {
        return baseMapper.count(getSqlId("getCusOffice"), param);
    }
    
    /**
     * getCustomer office related
     * 
     * @param param param
     * @return count
     */
    public int getUserOffice(BaseParam param)
    {
        return baseMapper.count(getSqlId("getUserOffice"), param);
    }
    
    /**
     * check ttcCustomerCode exist emailalertmaster or not
     * 
     * @param param param
     * @return count
     */
    public int checkExist(BaseParam param)
    {
        return baseMapper.count(getSqlId("checkExist"), param);
    }
    
    
    /**
     * save data
     * 
     * @param list list data
     * @param param param
     */
    public void saveList(List<CPMMAF01Entity> list, BaseParam param)
    {
        for(CPMMAF01Entity entity : list)
        {
            Object[] sameParam = new Object[] { entity.getLoginId() };
            TnmUser user = baseDao.findOne("from TNM_USER where login_id = ?", sameParam);
            entity.setLoginId(user.getUserId().toString());
            
            Object[] officeParam = new Object[] { entity.getImpOfficeCode() };
            TnmOffice office = baseDao.findOne("from TNM_OFFICE where office_code = ?", officeParam);
            entity.setOfficeId(office.getOfficeId());
            
            
            String ttcCus = entity.getTtcCustomerCode();
            if(!StringUtil.isEmpty(ttcCus))
            {
                String[] ttcs = ttcCus.split(StringConst.COMMA);
                for(String ttc : ttcs)
                {
                    entity.setTtcCustomerCode(ttc);
                    Object[] getParam = new Object[] { ttc, entity.getOfficeId() };
                    TnmCustomer customer = baseDao.findOne("from TNM_CUSTOMER where customer_code = ? AND office_id = ?", getParam);
                    entity.setTtcCustomerCode(customer.getCustomerId().toString());
                    entity.setUpdatedBy(param.getLoginUserId().toString());
                    entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
                    if("NEW".equals(entity.getNewMod()))
                    {
                        entity.setCreatedBy(param.getLoginUserId().toString());
                        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
                        entity.setVersion(IntDef.INT_ONE);
                        baseMapper.insert(getSqlId("addEmailAlert"), entity);
                    }
                    if("MOD".equals(entity.getNewMod()))
                    {
                        baseMapper.update(getSqlId("updateEmailAlert"), entity);
                    }
                }
            }
        }
    }
}
