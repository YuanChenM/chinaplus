/**
 * CPPSPF01Service.java
 * 
 * @screen CPPSPF01
 * @author xing_ming
 */
package com.chinaplus.web.vvp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmRegion;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.web.vvp.entity.VVPSDF11Entity;

/**
 * Download Sample Data File Service.
 */
@Service
public class VVPSDF11Service extends BaseService {

    /** SQL ID: findPrimaryKeyExist */
    private static final String SQLID_FIND_PRIMARY_KEY_EXIST = "findPrimaryKeyExist";

    /** SQL ID: findPrimaryKeyExist */
    private static final String SQLID_INSERT_INTO_VVP_SUPPLIER = "insertIntoVVPSupplier";

    /**
     * Check primary key is exist or not.
     * 
     * @param param search parameter
     * @return download list
     */
    public Integer checkPrimaryKeyExist(BaseParam param) {

        return super.baseMapper.count(this.getSqlId(SQLID_FIND_PRIMARY_KEY_EXIST), param);
    }

    /**
     * Save data into database.
     * 
     * @param supplierInfo supplierInfo
     * @return save count
     */
    public Integer doVVPSupplierInfo(VVPSDF11Entity supplierInfo ) {
        
        // do save
        return super.baseMapper.insert(this.getSqlId(SQLID_INSERT_INTO_VVP_SUPPLIER), supplierInfo);
        
    }

    /**
     * Get office Id by office code.
     * 
     * @param officeCode office Code
     * @return exist or not
     */
    public Integer getOfficeIdByOfficeCode(String officeCode) {

        // parameter
        TnmOffice offInfo = new TnmOffice();
        offInfo.setOfficeCode(officeCode);

        // get list
        List<TnmOffice> officeList = baseDao.select(offInfo);
        if (officeList != null && !officeList.isEmpty()) {
            return officeList.get(IntDef.INT_ZERO).getOfficeId();
        }

        // return result
        return null;
    }

    /**
     * Get Region Id by region code.
     * 
     * @param regionCode region Code
     * @return exist or not
     */
    public Integer getRegionIdByRegionCode(String regionCode) {

        // parameter
        TnmRegion regionInfo = new TnmRegion();
        regionInfo.setRegionCode(regionCode);

        // get list
        List<TnmRegion> regionList = baseDao.select(regionInfo);
        if (regionList != null && !regionList.isEmpty()) {
            return regionList.get(IntDef.INT_ZERO).getRegionId();
        }

        // return result
        return null;
    }
}
