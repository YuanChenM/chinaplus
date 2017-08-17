/**
 * Controller of Parts Master Screen.
 * 
 * @screen CPMPMS01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMS01Entity;
import com.chinaplus.web.mm.service.CPMPMS01Service;

/**
 * Controller of Parts Master Screen.
 */
@Controller
public class CPMPMS01Controller extends BaseController {

    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMPMS01Service cpmpms01Service;

    /**
     * get mpo list for screen CPMPMS01 by filter.
     * 
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/getCPMPMS01EntityList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMPMS01Entity> getCPMPMS01EntityList(@RequestBody PageParam param) throws Exception {

        // condition prepare
        StringUtil.buildLikeCondition(param, "ttcPartNo");
        StringUtil.buildLikeCondition(param, "partsDesEnglish");
        StringUtil.buildLikeCondition(param, "partsDesChinese");
        StringUtil.buildLikeCondition(param, "oldTTCPartNo");
        StringUtil.buildLikeCondition(param, "ssmsSuppCd");
        StringUtil.buildLikeCondition(param, "suppPartNo");
        StringUtil.buildLikeCondition(param, "ssmskbCustCd");
        StringUtil.buildLikeCondition(param, "custPartNo");
        StringUtil.buildLikeCondition(param, "westCustCd");
        StringUtil.buildLikeCondition(param, "westPartNo");
        StringUtil.buildLikeCondition(param, "carModel");
        StringUtil.buildLikeCondition(param, "createdBy");
        StringUtil.buildLikeCondition(param, "lastModifiedBy");
        StringUtil.buildDateTimeCondition(param, "createdDate");
        StringUtil.buildDateTimeCondition(param, "lastModifiedDate");

        // find data by paging
        PageResult<CPMPMS01Entity> result = cpmpms01Service.getPageList(param);

        return result;
    }

    /**
     * Load IMP Office Code combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/loadImpOfficeCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadImpOfficeCode(HttpServletRequest request) throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getImpOfficeCode(userInfo.getLanguage().getName());
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load TTC Customer Code combo data.
     * 
     * @param request the request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadTtcCustomerCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadTtcCustomerCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> impOfficeCodeList = (List<String>) param.getSwapData().get("impOfficeCode");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getTtcCustomerCode(userInfo.getLanguage().getName(),
            impOfficeCodeList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Export Country combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/loadExportCountry",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadExportRegion(HttpServletRequest request) throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getExportCountry();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load TCC Supplier Code combo data.
     * 
     * @param request the request
     * @param param the param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadTccSupCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadTccSupCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> exportCountryList = (List<String>) param.getSwapData().get("exportCountry");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getTccSupCode(exportCountryList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load SSMS Main Route combo data.
     * 
     * @param request the request
     * @param param the param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadSsmsMainRoute",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadSsmsMainRoute(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> exportCountryList = (List<String>) param.getSwapData().get("exportCountry");
        List<String> ttcSuppCdList = (List<String>) param.getSwapData().get("ttcSuppCd");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getSsmsMainRoute(userInfo.getLanguage().getName(),
            exportCountryList, ttcSuppCdList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load SSMS Vendor Route combo data.
     * 
     * @param request the request
     * @param param the param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadSSMSVendorRoute",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadSSMSVendorRoute(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> exportCountryList = (List<String>) param.getSwapData().get("exportCountry");
        List<String> ttcSuppCdList = (List<String>) param.getSwapData().get("ttcSuppCd");
        List<String> ssmsMainRouteList = (List<String>) param.getSwapData().get("ssmsMainRoute");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getSSMSVendorRoute(userInfo.getLanguage().getName(),
            exportCountryList, ttcSuppCdList, ssmsMainRouteList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Mail Invoice Customer Code combo data.
     * 
     * @param request the request
     * @param param the param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadMailInvoiceCustomerCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadMailInvoiceCustomerCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> impOfficeCodeList = (List<String>) param.getSwapData().get("impOfficeCode");
        List<String> ttcCustCdList = (List<String>) param.getSwapData().get("ttcCustCd");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getMailInvoiceCustomerCode(userInfo.getLanguage().getName(),
            impOfficeCodeList, ttcCustCdList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load TTC Imp W/H Code combo data.
     * 
     * @param request the request
     * @param param the param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "mm/CPMPMS01/loadTtcImpWHCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadTtcImpWHCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();

        List<String> impOfficeCodeList = (List<String>) param.getSwapData().get("impOfficeCode");

        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getTtcImpWHCode(userInfo.getLanguage().getName(),
            impOfficeCodeList);
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Part Type combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/loadPartType",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadPartType(HttpServletRequest request) throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getPartType(userInfo.getLanguage().getCode(), userInfo
            .getLanguage().getName());
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Part Status combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/loadPartStatus",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadPartStatus(HttpServletRequest request) throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpmpms01Service.getPartStatus(userInfo.getLanguage().getCode(), userInfo
            .getLanguage().getName());
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load discontinue indicator combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS01/loadDiscontinueIndicator",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadDiscontinueIndicator(HttpServletRequest request) throws Exception {

        UserInfo userInfo = getLoginUser(request);
        Map<Integer, String> codes = CodeCategoryManager.getCodeCategaryByLang(userInfo.getLanguage().getCode(),
            CodeMasterCategory.DISCONTINUE_INDICATOR);
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        String blankId = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String blankText = MessageManager.getMessage("CPMPMS01_Grid_Blank", userInfo.getLanguage().getLocale());
        ComboData blankData = new ComboData();
        blankData.setId(blankId);
        blankData.setText(blankText);
        comboDataList.add(blankData);
        for (Map.Entry<Integer, String> entry : codes.entrySet()) {
            ComboData comboata = new ComboData();
            comboata.setId(String.valueOf(entry.getKey()));
            comboata.setText(entry.getValue());
            comboDataList.add(comboata);
        }

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboDataList);
        return result;
    }

}
