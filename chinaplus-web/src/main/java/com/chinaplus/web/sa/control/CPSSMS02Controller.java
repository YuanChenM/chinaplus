/**
 * CPSSMS02Controller.java
 * 
 * @screen CPSSMS02
 * @author li_feng
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.service.CPSSMS02Service;

/**
 * Shipping Status Download Screen Controller.
 */
@Controller
public class CPSSMS02Controller extends BaseFileController {

    @Override
    protected String getFileId() {
        // TODO 
        return null;
    }
    
    /** Customer Stock Upload Screen Service */
    @Autowired
    private CPSSMS02Service service;
    
    /**
     * Get To Time
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/sa/CPSSMS02/getToTime",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> getToTime(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        BaseResult<String> result = new BaseResult<String>();
        this.setCommonParam(param, request);
        String startDate = (String) param.getSwapData().get("startDate");
        if(startDate != null && !startDate.equals("1000-01-01T00:00:00")){
            Date startTime = DateTimeUtil.parseDate(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.MONTH, IntDef.INT_SIX);
            calendar.getTime();
            
            result.setData(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, calendar.getTime()));
        }
        
        return result;
    }
    
    /**
     * Get From Time
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/sa/CPSSMS02/getFromTime",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> getFromTime(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        BaseResult<String> result = new BaseResult<String>();
        this.setCommonParam(param, request);
        String endDate = (String) param.getSwapData().get("endDate");
        if(endDate != null && !endDate.equals("9999-12-31T00:00:00")){
            Date endTime = DateTimeUtil.parseDate(endDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.MONTH, -IntDef.INT_SIX);
            calendar.getTime();
            
            result.setData(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, calendar.getTime()));
        }
        return result;
    }
    
    
    /**
     * Load all active suppliers.
     * 
     * @param request the request
     * @param response the response
     * @param param  the param
     * @return the combo data of all active suppliers
     */
    @RequestMapping(value = "/sa/CPSSMS02/loadTtcSupplierCode",
            method = RequestMethod.POST)
        @ResponseBody
    public PageResult<ComboData> loadTtcSupplierCode(HttpServletRequest request, HttpServletResponse response, @RequestBody PageParam param) {

        this.setCommonParam(param, request);
        // id : supplier code, text : supplier code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmSupplier> suppliers = service.getActiveSuppliers(param);
        if (suppliers != null && suppliers.size() > 0) {
            for (TnmSupplier item : suppliers) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(item.getSupplierCode()));
                combo.setText(item.getSupplierCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }
    
    /**
     * Load current office customers.
     * 
     * @param request the request
     * @param response the response
     * @param param the param
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/sa/CPSSMS02/loadCustomerCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCustomerCode(HttpServletRequest request, HttpServletResponse response, @RequestBody PageParam param) {

        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();
        this.setCommonParam(param, request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmCustomer> customers = service.getCurrentOfficeCustomers(this.getLoginUser(request).getOfficeId(), param);
        if (customers != null && customers.size() > 0) {
            for (TnmCustomer customer : customers) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(customer.getCustomerCode()));
                combo.setText(customer.getCustomerCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }
    
}