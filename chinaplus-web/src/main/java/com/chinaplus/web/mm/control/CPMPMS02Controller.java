/**
 * Controller of Master Upload/Download Screen.
 * 
 * @screen CPMPMS02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.AccessLevel;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.service.CommonService;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMS02Entity;
import com.chinaplus.web.mm.service.CPMPMS01Service;
import com.chinaplus.web.mm.service.CPMPMS02Service;

/**
 * Controller of Master Upload/Download Screen.
 */
@Controller
public class CPMPMS02Controller extends BaseFileController {

    /**
     * cpmpms02Service.
     */
    @Autowired
    private CPMPMS02Service cpmpms02Service;

    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMPMS01Service cpmpms01Service;

    /** Common Service */
    @Autowired
    private CommonService commonService;

    @Override
    protected String getFileId() {
        return FileId.CPMPMS02;
    }

    /**
     * Load Office Code By Id combo data.
     * 
     * @param request the request
     * @param response the response
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadOfficeCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadOfficeCodeByIds(HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        UserInfo loginUser = getLoginUser(request);
        List<UserOffice> offices = loginUser.getUserOffice();
        List<ComboData> comboOffices = new ArrayList<ComboData>();
        for (UserOffice office : offices) {
            ComboData data = new ComboData();
            data.setId(String.valueOf(office.getOfficeId()));
            data.setText(office.getOfficeCode());
            comboOffices.add(data);
        }
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Customer Code By userOffIds combo data.
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadCustomerCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCustomerCodeByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        String impOfficeCode = (String) param.getSwapData().get("impOfficeCode");
        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);

        List<ComboData> comboOffices = cpmpms02Service.getCustomerCodeByIds(userOffIds);

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load TCC Supplier Code By userOffIds combo data.
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadTccSupCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadTccSupCodeByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        String impOfficeCode = (String) param.getSwapData().get("impOfficeCode");
        String ttcCustCd = (String) param.getSwapData().get("ttcCustCd");

        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);

        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);

        // convert to combo data
        List<ComboData> comboOffices = cpmpms02Service.getTccSupCodeByIds(userOffIds, ttcCustCd, um.getAllVVFlag(),
            um.getAllAisinFlag());

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get SSMS Main Route Code By userOffIds
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadSsmsMainRouteCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadSsmsMainRouteCodeByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        String impOfficeCode = (String) param.getSwapData().get("impOfficeCode");
        String ttcCustCd = (String) param.getSwapData().get("ttcCustCd");
        String ttcSuppCd = (String) param.getSwapData().get("ttcSuppCd");

        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);

        // convert to combo data
        List<ComboData> comboDataList = cpmpms02Service.getSsmsMainRouteCodeByIds(userOffIds, ttcCustCd, ttcSuppCd);

        // do check
        PageResult<ComboData> result = new PageResult<ComboData>();
        if (comboDataList != null && !comboDataList.isEmpty()) {
            // check last one is null or not
            int lastIdx = comboDataList.size() - IntDef.INT_ONE;
            ComboData lastRecord = comboDataList.get(lastIdx);

            // check is null or not
            if (lastRecord == null) {
                comboDataList.remove(lastIdx);
                result.setData(new ComboData());
            }
        }
        // set data
        result.setDatas(comboDataList);

        return result;
    }

    /**
     * Get SSMS supplier Code By userOffIds
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadSsmsSuppCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadSsmsSuppCodeByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        String impOfficeCode = (String) param.getSwapData().get("impOfficeCode");
        String ttcCustCd = (String) param.getSwapData().get("ttcCustCd");
        String ttcSuppCd = (String) param.getSwapData().get("ttcSuppCd");
        String ssmsMainRoute = (String) param.getSwapData().get("ssmsMainRoute");

        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);

        // convert to combo data
        List<ComboData> comboDataList = cpmpms02Service.getSsmsSuppCodeByIdsByIds(userOffIds, ttcCustCd, ttcSuppCd,
            ssmsMainRoute);

        PageResult<ComboData> result = new PageResult<ComboData>();
        if (comboDataList != null && !comboDataList.isEmpty()) {
            // check last one is null or not
            int lastIdx = comboDataList.size() - IntDef.INT_ONE;
            ComboData lastRecord = comboDataList.get(lastIdx);

            // check is null or not
            if (lastRecord == null) {
                comboDataList.remove(lastIdx);
                result.setData(new ComboData());
            }
        }
        result.setDatas(comboDataList);
        return result;
    }

    /**
     * Get Business Pattern Code By userOffIds
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadBusinessPatternByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadBusinessPatternByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        UserInfo userInfo = getLoginUser(request);

        List<ComboData> comboOffices = cpmpms02Service.getBusinessPatternByIds(userInfo.getLanguage().getCode());

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Shipping Route Code
     * 
     * @param request request
     * @param param parameter
     * 
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadShipRouteCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadShipRouteCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        // shipping route type
        String shipRouteType = (String) param.getSwapData().get("shipRouteType");
        List<Integer> shipRouteTypes = cpmpms02Service.setCodeList(shipRouteType);
        param.setSwapData("shipRouteTypes", shipRouteTypes);
        String impOfficeCode = (String) param.getSwapData().get("srImpOfficeCode");
        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);
        param.setSwapData("userOffIds", userOffIds);

        PageResult<ComboData> result = new PageResult<ComboData>();

        // convert to combo data
        List<ComboData> comboOffices = cpmpms02Service.getShipRouteCode(param);

        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Shipping Route Type
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadShipRouteType",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadShipRouteType(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        UserInfo userInfo = getLoginUser(request);

        // shipping route
        String shipRouteCode = (String) param.getSwapData().get("shipRouteCode");

        // office
        String impOfficeCode = (String) param.getSwapData().get("srImpOfficeCode");
        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);

        // convert to combo data
        // id : supplier id, text : supplier code
        List<ComboData> comboOffices = cpmpms02Service.getShipRouteType(userOffIds, shipRouteCode, userInfo
            .getLanguage().getCode());

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Eff From Etd AND Effective to ETD
     * 
     * @param param param
     * @param request request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadEffFromToEtd",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMPMS02Entity> loadEffFromToEtd(@RequestBody BaseParam param, HttpServletRequest request)
        throws Exception {
        String shipRouteCode = (String) param.getSwapData().get("shipRouteCode");

        PageResult<CPMPMS02Entity> result = new PageResult<CPMPMS02Entity>();

        if (!StringUtil.isNullOrEmpty(shipRouteCode)) {
            UserInfo loginUser = getLoginUser(request);
            CPMPMS02Entity ce = cpmpms02Service.getEffFromToEtd(shipRouteCode, loginUser.getLanguage().getCode());
            List<CPMPMS02Entity> ceList = new ArrayList<CPMPMS02Entity>();
            if (ce != null) {
                ceList.add(ce);
            }
            result.setDatas(ceList);
        }
        return result;
    }

    // /**
    // * Get Eff From Etd AND Effective to ETD
    // *
    // * @param param param
    // * @param request request
    // * @return result
    // * @throws Exception e
    // */
    // @RequestMapping(value = "mm/CPMPMS02/loadOrderMonthFromTo",
    // method = RequestMethod.POST)
    // @ResponseBody
    // public PageResult<CPMPMS02Entity> loadOrderMonthFromTo(@RequestBody BaseParam param, HttpServletRequest request)
    // throws Exception {
    //
    // String kbImpOfficeCode = (String) param.getSwapData().get("kbImpOfficeCode");
    // String kbttcCustomerCode = (String) param.getSwapData().get("kbttcCustomerCode");
    //
    // PageResult<CPMPMS02Entity> result = new PageResult<CPMPMS02Entity>();
    //
    // if (!StringUtil.isNullOrEmpty(kbImpOfficeCode) && !StringUtil.isNullOrEmpty(kbttcCustomerCode)) {
    // UserInfo loginUser = getLoginUser(request);
    // CPMPMS02Entity ce = cpmpms02Service.getOrderMonthFromTo(kbImpOfficeCode, kbttcCustomerCode, loginUser
    // .getLanguage().getCode());
    // List<CPMPMS02Entity> ceList = new ArrayList<CPMPMS02Entity>();
    // if (ce != null) {
    // ceList.add(ce);
    // }
    // result.setDatas(ceList);
    // }
    // return result;
    // }

    /**
     * Get Country
     * 
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadCountry",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCountry() throws Exception {

        // convert to combo data
        List<ComboData> comboOffices = cpmpms02Service.getCountry();

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load Calendar Code By Id combo data.
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadCalendarCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCalendarCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        UserInfo userInfo = getLoginUser(request);
        List<Integer> userOffIds = cpmpms01Service.setUserOffIds(userInfo);

        String country = (String) param.getSwapData().get("country");

        // convert to combo data
        List<ComboData> comboOffices = null;
        if (userOffIds != null && userOffIds.size() > 0) {
            comboOffices = cpmpms02Service.getCalendarCode(userOffIds, country);
        }

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Year By country,calendarCode
     * 
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadYear",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadYear(@RequestBody BaseParam param) throws Exception {

        String country = (String) param.getSwapData().get("country");
        String calendarCode = (String) param.getSwapData().get("calendarCode");

        List<ComboData> comboOffices = null;
        if (country != null && calendarCode != null) {
            // convert to combo data
            comboOffices = cpmpms02Service.getYear(country, calendarCode);
        }

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Party
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadParty",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadParty(HttpServletRequest request, @RequestBody BaseParam param) throws Exception {

        UserInfo userInfo = getLoginUser(request);
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        // convert to combo data
        List<ComboData> comboOffices = cpmpms02Service.getParty(userInfo.getLanguage().getCode(), um.getAllVVFlag(),
            um.getAllAisinFlag());

        String pageFrom = param.getSwapData().get("pageFrom") != null ? (String) param.getSwapData().get("pageFrom")
            : "";
        if ("modify".equals(pageFrom)) {
            Integer accessLevel = um.getMaxAccessLevel(param.getScreenId());
            if (accessLevel < AccessLevel.MAINTAINERS) {
                comboOffices.remove(0);
            }
        }

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Party Code
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadPartyCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadPartyCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        UserInfo userInfo = getLoginUser(request);
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);

        List<Integer> userOffIds = cpmpms01Service.setUserOffIds(userInfo);
        String country = (String) param.getSwapData().get("country");
        String party = (String) param.getSwapData().get("party");
        String cmImpOfficeCode = (String) param.getSwapData().get("cmImpOfficeCode");
        // convert to combo data
        List<ComboData> comboOffices = null;
        if (userOffIds != null && userOffIds.size() > 0 && party != null) {
            comboOffices = cpmpms02Service.getPartyCode(userInfo.getLanguage().getCode(), party, country,
                cmImpOfficeCode, userOffIds, um.getVVFlag(), um.getAisinFlag());
        }
        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Login Id Code By userOffIds
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadLoginId",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadLoginIdByIds(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        String impOfficeCode = (String) param.getSwapData().get("eaImpOfficeCode");
        List<Integer> userOffIds = cpmpms02Service.setCodeList(impOfficeCode);
        param.setSwapData("userOffIds", userOffIds);

        List<ComboData> comboOffices = cpmpms02Service.getLoginIdByIds(param);

        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Get Alert Level code
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "mm/CPMPMS02/loadAlertLevel",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadAlertLevel(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {

        UserInfo userInfo = getLoginUser(request);
        List<ComboData> comboOffices = cpmpms02Service.getAlertLevel(userInfo.getLanguage().getCode());
        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Customer Stock Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/mm/CPMPMS02/dealCommonCheck",
        method = RequestMethod.POST)
    @ResponseBody
    public void dealCommonCheck(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        this.setCommonParam(param, request);
        uploadFileProcess(file, FileType.EXCEL, param, request, response);
    }

    /**
     * Load all supplier combo data.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all supplier
     */
    @RequestMapping(value = "/mm/CPMPMS02/loadSrOffices",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadAllOffices(HttpServletRequest request, HttpServletResponse response) {
        PageResult<ComboData> result = new PageResult<ComboData>();

        // query all suppliers
        List<TnmOffice> offices = commonService.getAllOffices();

        // convert to combo data
        // id : supplier id, text : supplier code
        List<ComboData> comboOffices = new ArrayList<ComboData>();
        if (offices != null) {
            for (TnmOffice office : offices) {
                ComboData combo = new ComboData();
                combo.setId(String.valueOf(office.getOfficeId()));
                combo.setText(office.getOfficeCode());
                comboOffices.add(combo);
            }
        }

        // add no assign office
        ComboData naoCombo = new ComboData();
        naoCombo.setId("999");
        naoCombo.setText(MessageManager.getMessage("CPMPMS02_Label_NoAssignOffice"));
        comboOffices.add(naoCombo);

        result.setDatas(comboOffices);
        return result;
    }

}
