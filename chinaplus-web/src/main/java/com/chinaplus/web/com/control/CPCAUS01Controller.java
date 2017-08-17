/**
 * CPCAUS01Controller.java
 * 
 * @screen CPCAUS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.control;

import java.util.ArrayList;
import java.util.Hashtable;
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

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.service.CommonService;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCAUS01Entity;
import com.chinaplus.web.com.service.CPCAUS01Service;

/**
 * CPCAUS01Controller. Assign user to customer
 */
@Controller
public class CPCAUS01Controller extends BaseController {

    /** Assign user to customer Service */
    @Autowired
    private CPCAUS01Service cpcaus01service;

    /** common Service */
    @Autowired
    private CommonService commonService;


    /**
     * Load Office Code combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/master/CPCAUS01/loadOffices",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadOffices(@RequestBody BaseParam param, HttpServletRequest request) {

        PageResult<ComboData> result = new PageResult<ComboData>();
        // query all login id
        super.setCommonParam(param, request);
        List<ComboData> offices = cpcaus01service.getOfficesByUser(param);
        result.setDatas(offices);
        return result;
    }

    /**
     * Load login id combo data.
     * 
     * @param param the parameter
     * @param request the request
     * @return the combo data of login id
     */
    @RequestMapping(value = "/master/CPCAUS01/loadLoginIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<TnmUser> loadLoginIds(@RequestBody BaseParam param, HttpServletRequest request) {

        // query all login id
        super.setCommonParam(param, request);

        if (null != param.getSwapData()) {
            String officeId = StringUtil.toSafeString(param.getSwapData().get("officeId"));
            if (StringUtil.isEmpty(officeId)) {
                param.getSwapData().put("officeId", param.getCurrentOfficeId());
            }
        }
        PageResult<TnmUser> result = cpcaus01service.getAllList("getAllUser", param);
        List<TnmUser> userList = new ArrayList<TnmUser>();
        Integer userId = param.getLoginUserId();
        if (null != result && null != result.getDatas() && 0 < result.getDatas().size()) {
            List<TnmUser> tempList = result.getDatas();
            for (int i = 0; i < tempList.size(); i++) {
                TnmUser entity = tempList.get(i);
                if (null == entity) {
                    continue;
                }
                if (userId.equals(entity.getUserId())) {
                    userList.add(entity);
                    tempList.remove(i);
                    break;
                }
            }
            userList.addAll(tempList);
            result.setDatas(userList);
        }
        return result;
    }

    /**
     * 
     * @param param BaseParam
     * @param request request
     * @param response response
     * @return BaseResult
     */
    @RequestMapping(value = "/master/CPCAUS01/checkbtn")
    @ResponseBody
    public BaseResult<String> checkBtn(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        setCommonParam(param, request);

        String officeStr = StringUtil.toSafeString(param.getSwapData().get("officeId"));
        Integer officeId = null;
        try {
            officeId = StringUtil.toSafeInteger(officeStr);
        } catch (Exception e) {
            officeId = param.getCurrentOfficeId();
        }
        BaseResult<String> result = new PageResult<String>();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        //String mainResourceId = sm.getMainMenuResource();
        boolean haveAuth = UserManager.getLocalInstance(sm).isHasAuthByOffice("CPCAUS01", "CPCAUS01", officeId,
            "_btnModify");
        if (haveAuth) {
            result.setData("1");
        } else {
            result.setData("0");
        }
        return result;
    }

    /**
     * Get customer list.
     * 
     * @param param the parameter
     * @param request the request
     * @return the customer list
     */
    @RequestMapping(value = "/master/CPCAUS01/getCusList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCAUS01Entity> getCusList(@RequestBody BaseParam param, HttpServletRequest request) {

        setCommonParam(param, request);

        try {
            if (null != param.getSwapData()) {
                String officeIdStr = StringUtil.toSafeString(param.getSwapData().get("officeId"));
                Integer officeId = StringUtil.toSafeInteger(officeIdStr);
                param.getSwapData().put("officeId", officeId);
            }
        } catch (Exception e) {
            param.getSwapData().remove("officeId");
        }

        PageResult<CPCAUS01Entity> result = cpcaus01service.getAllList("getUserCustomerByCode", param);
        Integer allCustomerFlag = cpcaus01service.getAllCusFlag(param);
        if (result.getDatas() != null && result.getDatas().size() != 0) {
            result.getDatas().get(0).setAllCustomerFlag(allCustomerFlag);
        }

        return result;
    }

    /**
     * Get customer list.
     * 
     * @param param the parameter
     * @param request the request
     * @return the customer list
     */
    @RequestMapping(value = "/master/CPCAUS01/getCusListForUser",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCAUS01Entity> getCusListForUser(@RequestBody BaseParam param, HttpServletRequest request) {

        setCommonParam(param, request);
        // PageResult<CPCAUS01Entity> result = cpcaus01service.getAllList("getUserCustomerByCode", param);
        List<CPCAUS01Entity> resultList = new ArrayList<CPCAUS01Entity>();
        List<Integer> officeIds = param.getUserOfficeIds();
        for (Integer officeId : officeIds) {
            param.getSwapData().put("officeId", officeId);
            Integer allFlag = cpcaus01service.getAllCusFlag(param);
            if (allFlag != null && allFlag == IntDef.INT_ONE) {
                param.getSwapData().put("flag", false);
                resultList.addAll(cpcaus01service.getOfficeCus(param));
            } else if (allFlag != null && allFlag == IntDef.INT_ZERO) {
                param.getSwapData().put("flag", true);
                resultList.addAll(cpcaus01service.getOfficeCus(param));
            }
            
        }
        PageResult<CPCAUS01Entity> result = new PageResult<CPCAUS01Entity>();
        result.setDatas(resultList);
        return result;
    }

    /**
     * Save customer to user.
     * 
     * @param param the parameter
     * @param request the request
     * @return save result
     * @throws Exception e
     */
    @RequestMapping(value = "/master/CPCAUS01/save",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> save(@RequestBody ObjectParam<CPCAUS01Entity> param, HttpServletRequest request)
        throws Exception {
        BaseParam baseParam = new PageParam();
        // Save customer to user
        super.setCommonParam(param, request);
        super.setCommonParam(baseParam, request);
        List<CPCAUS01Entity> customers = param.getDatas();
        Map<String, Object> map = new Hashtable<String, Object>();
        try {
            if (null != customers && customers.size() > 0) {
                String officeIdStr = customers.get(0).getOfficeCode();
                Integer userId = customers.get(0).getUserId();
                Integer officeId = StringUtil.toSafeInteger(officeIdStr);
                map.put("officeId", officeId);
                map.put("userId", userId);
                baseParam.setSwapData(map);
            }
        } catch (Exception e) {
            if (null != customers && customers.size() > 0) {
                Integer userId = customers.get(0).getUserId();
                map.put("userId", userId);
                baseParam.setSwapData(map);
            }
            baseParam.setSwapData(param.getSwapData());
        }
        cpcaus01service.doSave(param, baseParam);
        return new BaseResult<Integer>();
    }

    /**
     * Load office customers.
     * 
     * @param param param
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/CPCAUS01/loadOfficeCustomers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCurrentOfficeCustomers(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {

        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();
        this.setCommonParam(param, request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        int officeId = 0;
        if (param.getSwapData().get("officeId") != null) {
            officeId = StringUtil.toSafeInteger((String) param.getSwapData().get("officeId"));
        } else {
            officeId = param.getCurrentOfficeId();
        }
        List<TnmCustomer> customers = commonService.getCurrentOfficeCustomers(officeId);
        if (customers != null && customers.size() > 0) {
            for (TnmCustomer customer : customers) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(customer.getCustomerId()));
                combo.setText(customer.getCustomerCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * 
     * @param param BaseParam
     * @param request request
     * @param response response
     * @return BaseResult
     */
    @RequestMapping(value = "/master/CPCAUS01/checkAllCust")
    @ResponseBody
    public BaseResult<String> checkAllCust(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        setCommonParam(param, request);

        String officeStr = StringUtil.toSafeString(param.getSwapData().get("officeId"));
        String userStr = StringUtil.toSafeString(param.getSwapData().get("userId"));
        Integer officeId = null;
        Integer userId = null;
        try {
            officeId = StringUtil.toSafeInteger(officeStr);
            userId = StringUtil.toSafeInteger(userStr);
        } catch (Exception e) {
            officeId = param.getCurrentOfficeId();
            userId = param.getLoginUserId();
        }
        BaseParam params = new BaseParam();
        params.setSwapData("officeId", officeId);
        params.setSwapData("userId", userId);
        BaseResult<String> result = new PageResult<String>();
        Integer allCustomerFlag = cpcaus01service.getAllCusFlag(params);
        if (allCustomerFlag != null && allCustomerFlag == 1) {
        	result.setData("1");
        } else {
        	result.setData("0");
        }
        return result;
    }
}
