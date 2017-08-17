/**
 * @screen CPMSMS01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * CPMSMS01Service.
 */
@Service
public class CPMSMS01Service extends BaseService {

    
    /**
     * Get  Vendor   Route
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getVendorRoute() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getVendorRoute"), param);
        return comboDataList;
    }
        
    
}
