/**
 * CPCAUS01Service.java
 * 
 * @screen CPCAUS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.entity.TnmUserCustomer;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.web.com.entity.CPCAUS01Entity;

/**
 * CPCAUS01Service.
 */
@Service
public class CPCAUS01Service extends BaseService {

    /**
     * Save customer to user.
     * 
     * @param param the parameters
     * @param baseParam the parameters
     * @throws Exception e
     */
    public void doSave(ObjectParam<CPCAUS01Entity> param, BaseParam baseParam) throws Exception {

        // get parameters
        List<CPCAUS01Entity> customers = param.getDatas();
        super.baseMapper.delete(super.getSqlId("delUserCustomerCode"), baseParam);
        // get current date
        Timestamp currentTime = super.getDBDateTimeByDefaultTimezone();
        // Insert new customer data to user
        for (CPCAUS01Entity userCustomer : customers) {
            CPCAUS01Entity insertParams = new CPCAUS01Entity();
            Integer cuntomerId = userCustomer.getCustomerId();
            Integer userId = userCustomer.getUserId();
            String officeCode = userCustomer.getOfficeCode();
            Integer allCustomerFlag = userCustomer.getAllCustomerFlag();
            Integer businessPattern = userCustomer.getBusinessPattern();
            if (allCustomerFlag == 1) {
                    insertParams.setUserId(userId);
                    insertParams.setOfficeCode(officeCode);
                    insertParams.setAllCustomerFlag(allCustomerFlag);
                    insertParams.setUpdatedBy(param.getLoginUserId());
                    insertParams.setUpdatedDate(currentTime);
                    insertParams.setCreatedBy(param.getLoginUserId());
                    insertParams.setCreatedDate(currentTime);
                    insertParams.setVersion(1);
                    super.baseMapper.insert(super.getSqlId("addUserCustomer"), insertParams);
            	break;
            } else if (cuntomerId != 0) {
            	insertParams.setBusinessPattern(businessPattern);
            	insertParams.setCustomerId(cuntomerId);
            	
                insertParams.setUserId(userId);
                insertParams.setOfficeCode(officeCode);
                insertParams.setAllCustomerFlag(allCustomerFlag);
                insertParams.setUpdatedBy(param.getLoginUserId());
                insertParams.setUpdatedDate(currentTime);
                insertParams.setCreatedBy(param.getLoginUserId());
                insertParams.setCreatedDate(currentTime);
                insertParams.setVersion(1);
                super.baseMapper.insert(super.getSqlId("addUserCustomer"), insertParams);
                continue;
            }
        }

    }

    /**
     * getAllCustomerFlag
     * 
     * @param param param
     * @return String
     */
    public Integer getAllCusFlag(BaseParam param) {
        String hql = "FROM TNM_USER_CUSTOMER WHERE USER_ID = ? AND OFFICE_ID = ?";
        Object[] param2 = new Object[] { param.getSwapData().get("userId"), param.getSwapData().get("officeId") };
        List<TnmUserCustomer> reglist = baseDao.select(hql, param2);
        if (reglist != null && reglist.size() != 0) {
            return reglist.get(0).getAllCustomerFlag();
        }
        return null;
    }
    
    /**
     * getAllCustomer
     * 
     * @param param param
     * @return String
     */
    public List<CPCAUS01Entity> getOfficeCus(BaseParam param) {
        return baseMapper.select(getSqlId("getUserCustomerForUser"), param);
    }
    
    /**
     * getOfficesByUser
     * 
     * @param param param
     * @return Map<String, Object>
     */
    public List<ComboData> getOfficesByUser(BaseParam param) {
        return baseMapper.select(getSqlId("getOfficesByUser"), param);
    }
    
}
