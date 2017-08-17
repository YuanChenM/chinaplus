/**
 * CPSSMS01Service.java
 * 
 * @screen CPSSMS01
 * @author li_feng
 */
package com.chinaplus.web.sa.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.sa.entity.CPSSMS01Entity;


/**
 * Download Shipping Status Revison History Service.
 */
@Service
public class CPSSMS01Service extends BaseService {
    
    
    /**
     * Get business patterns.
     * @param <T> CPOCFS01Entity
     * @param param page parameter
     * @return all roles
     */
    public <T extends CPSSMS01Entity> PageResult<T> getHistoryInfo(BaseParam param) {

        // data result
        PageResult<T> result = new PageResult<T>();
        List<T> datas = this.baseMapper.select(this.getSqlId("getShippingStatusRevisonInfo"), param);
        
        for (T t : datas) {
            if(t.getExpCountry() != null){
                String expCountryTemp = t.getExpCountry().replaceAll("<TNM_REGION>", "").replaceAll("</TNM_REGION>", "");
                t.setExpCountry(expCountryTemp.substring(0, expCountryTemp.length()-1));
            }
            if(t.getTtcSupplierCode() != null){
                String ttcSupplierCodeTemp = t.getTtcSupplierCode().replaceAll("<TNM_SUPPLIER>", "").replaceAll("</TNM_SUPPLIER>", "");
                t.setTtcSupplierCode(ttcSupplierCodeTemp.substring(0, ttcSupplierCodeTemp.length()-1));
            }
            if(t.getRevisionReason() != null){
                String revisionReasonTemp = t.getRevisionReason().replaceAll("<TNM_CODE_CATEGORY>", "").replaceAll("</TNM_CODE_CATEGORY>", "");
                t.setRevisionReason(revisionReasonTemp.substring(0, revisionReasonTemp.length()-1));
            }
            
        }
        
        
        // set query result
        result.setTotalCount(datas == null ? 0 : datas.size());
        result.setDatas(datas);

        return result;
    }
    
    
}