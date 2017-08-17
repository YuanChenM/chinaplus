/**
 * CPOCFF01Service.java
 * 
 * @screen CPOCFF01
 * @author li_feng
 */
package com.chinaplus.web.om.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.om.entity.CPOCFF01Entity;

/**
 * Customer Stock DownLoad Screen Service.
 */
@Service
public class CPOCFF01Service extends BaseService {
    
    private static final String KEY_SEPARATOR = "***separator***";
    
    /**
     * Get last update forecast month.
     * 
     * @param param page parameter
     * @return list
     */
    public List<CPOCFF01Entity> getLastUpdateForecastMonth(PageParam param) {

        List<CPOCFF01Entity> list = this.baseMapper.select(this.getSqlId("getLastUpdateForecastMonth"), param);
        if (list.size() > 0) {
            for (CPOCFF01Entity entity : list) {
                param.setSwapData("CustomerId", entity.getCustomerId());
                List<CPOCFF01Entity> data = this.baseMapper.select(this.getSqlId("getCustomerCode"), param);
                entity.setCustomercode(data.get(0).getCustomercode());
            }
        }
        return list;
    }

    /**
     * Select the customer calendar.
     * 
     * @param param T
     * @param <T> BaseParam
     * @return list
     */
    public <T extends BaseParam> List<CPOCFF01Entity> getCustomerCalendar(T param) {
        String custStartMonth = param.getSwapData().get("custStartMonth").toString();
        String custEndMonth = param.getSwapData().get("custEndMonth").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // set first day and end day
        Calendar c = Calendar.getInstance();
        c.setTime(DateTimeUtil.parseMonth(custStartMonth));
        c.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = format.format(c.getTime());
        c.setTime(DateTimeUtil.parseMonth(custEndMonth));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = format.format(c.getTime());
        param.setSwapData("StartDate", startDate);
        param.setSwapData("EndDate", endDate);

        // get customer calendar
        List<CPOCFF01Entity> list = this.baseMapper.select(this.getSqlId("getCustomerCalendar"), param);
        return list;
    }

    /**
     * Select the customer code.
     * 
     * @param param T
     * @param <T> BaseParam
     * @return list
     */
    public <T extends BaseParam> List<CPOCFF01Entity> getCustomerCode(T param) {

        // get customer code
        List<CPOCFF01Entity> list = this.baseMapper.select(this.getSqlId("getCustomerCalendarMonth"), param);
        return list;
    }
    
    
    /**
     * Select the customer calendar.
     * 
     * @param param T
     * @param <T> BaseParam
     * @return list
     */
    public <T extends BaseParam> Map<String,String> getCustomerCalendarEndDateMap(T param) {
        Map<String,String> map = new HashMap<String,String>();
        String mapKey = null;
        String custEndMonth = param.getSwapData().get("custEndMonth").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // set first day and end day
        Calendar c = Calendar.getInstance();
        c.setTime(DateTimeUtil.parseMonth(custEndMonth));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = format.format(c.getTime());
        param.setSwapData("EndDate", endDate);

        // get customer calendar
        List<CPOCFF01Entity> list = this.baseMapper.select(this.getSqlId("getCustomerCalendarEndDate"), param);
        for(CPOCFF01Entity entity : list){
            mapKey = new StringBuilder().append(entity.getCustomercode()).append(KEY_SEPARATOR)
            .append(entity.getCalendarDate()).toString();
            map.put(mapKey,entity.getCustomercode());
        }
        return map;
    }
    
    /**
     * Select the customer code.
     * 
     * @param custId Integer
     * @return String
     */
    public String getCustCode(Integer custId){
        return baseDao.findOne(TnmCustomer.class, custId).getCustomerCode();
    }
}