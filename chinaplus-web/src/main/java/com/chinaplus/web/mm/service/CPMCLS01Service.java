/**
 * CPMCLS01Service.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.entity.TnmCalendarMaster;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.mm.entity.CPMCLS01Entity;

/**
 * Customer List Screen service
 */
@Service
public class CPMCLS01Service extends BaseService {


    /**
     * getCalendarListCombo
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<ComboData> getCalendarListCombo(BaseParam param) {
        List<TnmCalendarMaster> list = baseMapper.select(getSqlId("getCalendarListCombo"), param);
        List<ComboData> result = new ArrayList<ComboData>();
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TnmCalendarMaster calendar = list.get(i);
                if (null == calendar) {
                    continue;
                }
                ComboData code = new ComboData();
                code.setId(calendar.getCalendarId() + "");
                code.setText(calendar.getCalendarCode());
                result.add(code);
            }
        }
        return result;
    }
    /**
     * getcustomers
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<TnmCustomer> getCustomers(BaseParam param)
    {
        return baseMapper.select(getSqlId("getCustomers"), param);
    }
    
    /**
     * getcustomers
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<ComboData> getTtlCustomers(BaseParam param)
    {
        return baseMapper.select(getSqlId("getTtlCustomers"), param);
    }
    
    /**
     * getTtlCustomerCodebyCustomerId
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<CPMCLS01Entity> getTtlCustomerCodebyCustomerId(BaseParam param)
    {
        return baseMapper.select(getSqlId("getTtlCustomerCodebyCustomerId"), param);
    }
}
