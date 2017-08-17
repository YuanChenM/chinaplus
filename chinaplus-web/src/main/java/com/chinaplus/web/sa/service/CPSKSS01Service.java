/**
 * CPSKSS01Service.java
 * 
 * @screen CPSKSS01
 * @author shi_yuxi
 */
package com.chinaplus.web.sa.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * KPI Screen
 */
@Service
public class CPSKSS01Service extends BaseService{
    /**
     * 
     * loadCustomers
     * @param param param
     * @return List<ComboData>
     */
    public List<ComboData> loadCustomers(BaseParam param)
    {
        List<ComboData> list = this.baseMapper.select(getSqlId("getCurrentOfficeCustomers"), param);
        return list;
    }
}
