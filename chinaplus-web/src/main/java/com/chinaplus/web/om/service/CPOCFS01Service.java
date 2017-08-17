/**
 * CPOCFS01Service.java
 * 
 * @screen CPOCFS01
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.om.entity.CPOCFS01Entity;

/**
 * Customer Stock Upload Screen Service.
 */
@Service
public class CPOCFS01Service extends BaseService {

    /**
     * Get business patterns.
     * @param <T> CPOCFS01Entity
     * @param param page parameter
     * @return all roles
     */
    public <T extends CPOCFS01Entity> PageResult<T> getcustomerForecastInfo(BaseParam param) {

        // data result
        PageResult<T> result = new PageResult<T>();
        List<T> datas = this.baseMapper.select(this.getSqlId("getcustomerForecastInfo"), param);

        // set query result
        result.setTotalCount(datas == null ? 0 : datas.size());
        result.setDatas(datas);

        return result;
    }
    
    /**
     * Cancel Customer Forecast.
     * 
     * @param param param
     * @throws Exception e
     */
    public void doCancelCustomerForecast(BaseParam param) throws Exception {

        param.setSwapData("UPDATE_DATE", super.getDBDateTimeByDefaultTimezone());
        // cancel customer forecast
        baseMapper.update("cancelCustomerForecast", param);

    }

}