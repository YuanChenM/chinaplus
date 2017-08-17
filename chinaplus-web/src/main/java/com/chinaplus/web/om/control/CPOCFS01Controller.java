/**
 * CPOCFS01Controller.java
 * 
 * @screen CPOCFS01
 * @author li_feng
 */
package com.chinaplus.web.om.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOCFS01Entity;
import com.chinaplus.web.om.service.CPOCFS01Service;

/**
 *Customer Forecast Screen Controller.
 */
@Controller
public class CPOCFS01Controller extends BaseController {

    /** Customer Forecast Screen Service */
    @Autowired
    private CPOCFS01Service service;

    /**
     * get customer forecast for screen CPOCFS01 by filter.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "/om/CPOCFS01/getCustomerForecastList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPOCFS01Entity> getUserDetailsList(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {

        super.setCommonParam(param, request);
        // condition prepare
        StringUtil.buildLikeCondition(param, "loginId");
        StringUtil.buildLikeCondition(param, "userName");
        StringUtil.buildLikeCondition(param, "mailAddr");

        // find data by paging
        PageResult<CPOCFS01Entity> result = service.getPageList(param);
        //PageResult<CPOCFS01Entity> result = service.getcustomerForecastInfo(param);

        return result;
    }

    /**
     * Delete selected roleIds.
     * 
     * @param request request
     * @param param RequestBody
     * @return BaseResult
     * @throws Exception e
     */
    @RequestMapping(value = "/om/CPOCFS01/cancelCustomerForecast",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> cancelCustomerForecast(HttpServletRequest request, @RequestBody ObjectParam<CPOCFS01Entity> param)
        throws Exception {

        super.setCommonParam(param, request);
        // do delete
        service.doCancelCustomerForecast(param);

        // return result
        BaseResult<String> result = new BaseResult<String>();

        return result;
    }
}
