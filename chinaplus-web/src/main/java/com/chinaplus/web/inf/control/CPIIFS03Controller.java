/**
 * Controller of Match Invoice detail Screen.
 * 
 * @screen CPIIFS03
 * @author zhang_chi
 */
package com.chinaplus.web.inf.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inf.entity.CPIIFS03Entity;
import com.chinaplus.web.inf.service.CPIIFS03Service;

/**
 * Controller of Match Invoice detail Screen.
 */
@Controller
public class CPIIFS03Controller extends BaseController {

    /**
     * cpiifs03Service.
     */
    @Autowired
    private CPIIFS03Service cpiifs03Service;

    /**
     * get list for screen CPIIFS03 by filter.
     * 
     * @param param param
     * @param request request
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS03/getCPIIFS03EntityList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPIIFS03Entity> getDetailsList(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {

        // UserInfo loginUser = getLoginUser(request);
        // get invoice form Data

        // condition prepare
        StringUtil.buildLikeCondition(param, "vesselName");
        StringUtil.buildLikeCondition(param, "blno");
        // param.getFilters().put("officeId", loginUser.getOfficeId());

        // find data by paging
        PageResult<CPIIFS03Entity> result = cpiifs03Service.getPageList(param);

        return result;
    }

    /**
     * Load InvoiceNo combo data.
     * 
     * @param param param
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS03/loadInvoiceNo",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadInvoiceNo(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {
        // UserInfo loginUser = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = null;
        if (param.getSwapData().containsKey("officeId")) {
            comboOffices = cpiifs03Service.getInvoiceNo(param);
        } else {
            comboOffices = new ArrayList<ComboData>();
        }
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load RegionCode combo data.
     * 
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS03/loadRegionCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadRegionCode() throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpiifs03Service.getRegionCode();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load UploadedBy combo data.
     * 
     * @param param param
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS03/loadUploadedBy",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadUploadedBy(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {

        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboUserIds = null;
        if (param.getSwapData().containsKey("officeId")) {
            comboUserIds = cpiifs03Service.getUploadedBy(param);
        } else {
            comboUserIds = new ArrayList<ComboData>();
        }
        result.setDatas(comboUserIds);
        return result;
    }

    /**
     * save Data
     * 
     * @param request request
     * @param param param
     * @return BaseResult
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS03/saveData",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> saveData(HttpServletRequest request, @RequestBody BaseParam param) throws Exception {

        //String invoiceSummaryId = (String) param.getSwapData().get("invoiceSummaryId");
        //String impInvoiceId = (String) param.getSwapData().get("str");
        //UserInfo loginUser = getLoginUser(request);
        
        // set common parameter
        super.setCommonParam(param, request);

        // do save
        cpiifs03Service.doSaveData(param);

        // return result
        BaseResult<String> result = new BaseResult<String>();

        return result;
    }

}
