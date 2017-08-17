/**
 * CPOOFS01Service.
 * 
 * @author shi_yuxi
 * @screen CPOOFS01
 */
package com.chinaplus.web.vvp.service;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.web.vvp.entity.VVPDS01Entity;

/**
 * Order Forecast Summary Screen
 */
@Service
public class VVPDS01Service extends BaseService {

    /** SQL ID: findSupplierInfoBySupplierId */
    private static final String SQLID_FIND_SUPPLIERINFO_BY_SUPPLIERID = "findSupplierInfoBySupplierId";

    /**
     * Save data into database.
     * 
     * @param supplierId supplierId
     * @return save count
     */
    public VVPDS01Entity getVVPSupplierInfo(Integer supplierId) {

        // set entity
        VVPDS01Entity param = new VVPDS01Entity();
        param.setSupplierId(supplierId);

        // do save
        return super.baseMapper.findOne(this.getSqlId(SQLID_FIND_SUPPLIERINFO_BY_SUPPLIERID), param);
    }
    
    /**
     * Save data into database.
     * 
     * @param supplierInfo supplierInfo
     * @param updateFlag is New or update
     * @return save count
     */
    public VVPDS01Entity doUpdateSupplierInfo(VVPDS01Entity supplierInfo, boolean updateFlag) {

        // check
        if (!updateFlag) {
            super.baseMapper.insert(this.getSqlId("insertIntoVVPSupplier"), supplierInfo);
        } else {
            super.baseMapper.update(this.getSqlId("updateIntoVVPSupplier"), supplierInfo);
        }

        // do save
        return supplierInfo;
    }

}
