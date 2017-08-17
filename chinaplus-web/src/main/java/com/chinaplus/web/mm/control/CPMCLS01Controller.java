/**
 * CPMCLS01Control.
 * 
 * @author shi_yuxi
 * @screen CPMCLS01
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMCLS01Entity;
import com.chinaplus.web.mm.service.CPMCLS01Service;

/**
 * Customer List Screen service
 */
@Controller
public class CPMCLS01Controller extends BaseController {

    @Autowired
    CPMCLS01Service service;

    /**
     * getCalendarListCombo
     * get calendar list from customer table
     * 
     * @param param PageParam
     * @return List<ComboData>
     */
    @RequestMapping(value = "/mm/CPMCLS01/calList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> getCalendarListCombo(@RequestBody BaseParam param) {
        PageResult<ComboData> result = new PageResult<ComboData>();
        List<ComboData> list = service.getCalendarListCombo(param);
        result.setDatas(list);
        return result;
    }

    /**
     * list
     * get list result
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return PageResult<CPMCLS01Entity>
     */
    @RequestMapping(value = "/mm/CPMCLS01/list",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMCLS01Entity> init(@RequestBody PageParam param, HttpServletRequest request) {

        StringUtil.buildLikeCondition(param, "whsCustCode");
        StringUtil.buildLikeCondition(param, "customerName");
        StringUtil.buildLikeCondition(param, "createBy");
        StringUtil.buildLikeCondition(param, "updateBy");
        // PageResult<CPMCLS01Entity> result =service.getCustomerList(param);
        setCommonParam(param, request);
        PageResult<CPMCLS01Entity> result = service.getPageList("getCustomerListCount", "getCustomerList", param);
        List<CPMCLS01Entity> resultList = result.getDatas();
        param.setSwapData("custmoerList", resultList);
        List<CPMCLS01Entity> ttlresultList = new ArrayList<CPMCLS01Entity>();
        if (resultList != null && resultList.size() > 0) {
            ttlresultList = service.getTtlCustomerCodebyCustomerId(param);
        }
        Map<String, String> ttlMap = new HashMap<String, String>();
        ttlMap.put("", "");
        for (CPMCLS01Entity entity : ttlresultList) {
            String customerId = entity.getCustomerId();
            String ttlcustomerCode = entity.getTtlCustomerCode();
            if (!ttlMap.containsKey(customerId)) {
                ttlMap.put(customerId, ttlcustomerCode);
            } else {
                String ttlcustomerCodeStr = ttlMap.get(customerId);
                if (!ttlcustomerCodeStr.contains(ttlcustomerCode)) {
                    ttlMap.put(customerId, ttlMap.get(customerId) + "," + ttlcustomerCode);
                }
            }
        }
        for (CPMCLS01Entity entity : resultList) {
            String customerId = entity.getCustomerId();
            entity.setTtlCustomerCode(ttlMap.get(customerId));
        }
        return result;
    }

    /**
     * Load current office customers.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/mm/CPMCLS01/loadCustomers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCustomers(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = new BaseParam();
        setCommonParam(param, request);
        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();
        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmCustomer> customers = service.getCustomers(param);
        if (customers != null && customers.size() > 0) {
            for (TnmCustomer customer : customers) {
                ComboData combo = new ComboData();
                combo.setId(customer.getCustomerCode());
                combo.setText(customer.getCustomerCode());
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
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/mm/CPMCLS01/loadCustomersForId",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCustomersForId(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = new BaseParam();
        setCommonParam(param, request);
        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();
        PageResult<ComboData> result = new PageResult<ComboData>();
        List<BusinessPattern> customers = getCurrentBusPattern(request);
        if (customers != null && customers.size() > 0) {
            for (BusinessPattern customer : customers) {
                ComboData combo = new ComboData();
                combo.setId(String.valueOf(customer.getCustomerId()));
                combo.setText(customer.getCustomerCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * get Current BusPattern list
     * 
     * @param request request
     * @return List<BusinessPattern>
     */
    public List<BusinessPattern> getCurrentBusPattern(HttpServletRequest request) {
        List<BusinessPattern> bplist = new ArrayList<BusinessPattern>();
        UserInfo userInfo = getLoginUser(request);
        List<UserOffice> uoList = userInfo.getUserOffice();
        for (UserOffice uo : uoList) {
            List<BusinessPattern> uobplist = uo.getBusinessPatternList();
            if (uobplist != null && uobplist.size() > 0) {
                bplist.addAll(uobplist);
            }
        }
        return bplist;
    }

    /**
     * Load current office customers.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/mm/CPMCLS01/getTtlCsutomerCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> getTtlCsutomerCode(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = new BaseParam();
        setCommonParam(param, request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // id : customer id, text : customer code
        List<ComboData> customers = service.getTtlCustomers(param);
        result.setDatas(customers);

        return result;
    }
}
