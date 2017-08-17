/**
 * CommonService.java
 * 
 * @screen common
 * @author ma_b
 */
package com.chinaplus.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.common.entity.TnmRegion;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;

/**
 * Common service for china plus system.
 */
@Service
public class CommonService extends BaseService {

    /**
     * Get all suppliers.
     * 
     * @return all suppliers
     */
    public List<TnmOffice> getAllOffices() {
        TnmOffice param = new TnmOffice();
        return baseDao.selectWithSort(param, "officeCode");
    }

    /**
     * Get all active regions.
     * 
     * @return all active regions
     */
    public List<TnmRegion> getActiveRegions() {

        BaseParam param = new BaseParam();
        return baseMapper.select(this.getSqlId("getActiveRegions"), param);
    }

    /**
     * Get all active regions.
     * 
     * @return all active suppliers
     */
    public List<TnmSupplier> getActiveSuppliers() {

        BaseParam param = new BaseParam();
        return baseMapper.select(this.getSqlId("getActiveSuppliers"), param);
    }

    /**
     * Get current office customers.
     * 
     * @param officeId current office ID
     * @return current office customers
     */
    public List<TnmCustomer> getCurrentOfficeCustomers(int officeId) {

        BaseParam param = new BaseParam();
        param.setCurrentOfficeId(officeId);
        return baseMapper.select(this.getSqlId("getCurrentOfficeCustomers"), param);
    }

    /**
     * Get current office users.
     * 
     * @param officeId current office ID
     * @return current office users
     */
    public List<TnmUser> getCurrentOfficeUsers(int officeId) {

        BaseParam param = new BaseParam();
        param.setCurrentOfficeId(officeId);
        return baseMapper.select(this.getSqlId("getCurrentOfficeUsers"), param);
    }

    /**
     * get all active revision reasons
     * 
     * @param businessPattern BUSINESS_PATTERN
     * @return all active revision reasons
     */
    public List<TnmReason> getActiveRevisionReasons(int businessPattern) {

        ObjectParam<String> param = new ObjectParam<String>();
        param.setSwapData("BUSINESS_PATTERN", businessPattern);
        return baseMapper.select(this.getSqlId("getActiveRevisionReasons"), param);
    }
}
