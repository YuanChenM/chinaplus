/**
 * CPSSMS02Service.java
 * 
 * @screen CPOCSF11
 * @author li_feng
 */
package com.chinaplus.web.sa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.util.StringUtil;


/**
 * Shipping Status Service.
 */
@Service
public class CPSSMS02Service extends BaseService {
    
    
    
    /**
     * Get all active regions.
     * @param param BaseParam
     * @return all active suppliers
     */
    public List<TnmSupplier> getActiveSuppliers(PageParam param) {
        String exportCountry = (String) param.getSwapData().get("exportCountry");     
        param.setSwapData("ExportCountry", setStringList(exportCountry));     
        return baseMapper.select(this.getSqlId("getActiveSuppliers"), param);
    }
    
    /**
     * Get current office customers.
     * 
     * @param officeId current office ID
     * @param param PageParam
     * @return current office customers
     */
    public List<TnmCustomer> getCurrentOfficeCustomers(int officeId, PageParam param) {
        String importCountry = (String) param.getSwapData().get("importCountry");     
        param.setSwapData("ImportCountry", setStringList(importCountry)); 
        param.setCurrentOfficeId(officeId);
        return baseMapper.select(this.getSqlId("getCurrentOfficeCustomers"), param);
    }
    
    /**
     * set string list
     * 
     * @param Lst String
     * @return List<String> list
     */
    public List<String> setStringList(String Lst) {
        List<String> stringList = new ArrayList<String>();
        if (!StringUtil.isNullOrEmpty(Lst)) {
            String[] list = Lst.split(",");
            for (String c : list) {
                stringList.add(c);
            }
        }
        return stringList;
    }
}