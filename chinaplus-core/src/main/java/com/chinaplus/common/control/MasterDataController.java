/**
 * @screen common
 * @author ma_b
 */
package com.chinaplus.common.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.common.entity.TnmRegion;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.service.CommonService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;

/**
 * The controller for load master datas.
 */
@Controller
public class MasterDataController extends BaseController {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(MasterDataController.class);

    /** the master key : User offices */
    private static final String MASTER_KEY_USEROFFICE = "USER_OFFICES";

    /** the master key : TNM_UOM */
    private static final String MASTER_KEY_UOM = "TNM_UOM";

    @Autowired
    CommonService commonService;

    /**
     * Load master data for combobox at extjs initialization.
     * 
     * @param request the request
     * @param response the response
     * @return the json for master data
     */
    @RequestMapping(value = "/common/master/load",
        method = RequestMethod.POST)
    @ResponseBody
    public JSONObject loadMasterDatas(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("method MasterDataController.loadMasterDatas is start");

        BaseParam param = new BaseParam();
        this.setCommonParam(param, request);

        // get loginUser's language
        UserInfo userInfo = getLoginUser(request);
        Integer lang = userInfo.getLanguage().getCode();

        JSONObject json = new JSONObject();

        // load code category
        Map<String, Map<Integer, String>> codeCategoryMap = CodeCategoryManager.getCodeCategaryByLang(lang);
        if (codeCategoryMap != null) {
            for (String codeCategory : codeCategoryMap.keySet()) {

                if (!json.containsKey(codeCategory)) {
                    json.put(codeCategory, new JSONArray());
                }

                Map<Integer, String> codeMap = codeCategoryMap.get(codeCategory);

                for (Integer code : codeMap.keySet()) {

                    ComboData entity = new ComboData();
                    entity.setId(String.valueOf(code));
                    entity.setText(codeMap.get(code));
                    ((JSONArray) json.get(codeCategory)).add(entity);
                }

            }
        }

        /*
         * // load region
         * List<ComboData> regions = commonService.getAllRegions();
         * if (regions != null) {
         * if (!json.containsKey(MASTER_KEY_REGION)) {
         * json.put(MASTER_KEY_REGION, new JSONArray());
         * }
         * for (ComboData region : regions) {
         * ((JSONArray) json.get(MASTER_KEY_REGION)).add(region);
         * }
         * }
         * // load calendar
         * List<ComboData> calendars = commonService.getAllCalendar();
         * if (calendars != null) {
         * if (!json.containsKey(MASTER_KEY_CALENDAR)) {
         * json.put(MASTER_KEY_CALENDAR, new JSONArray());
         * }
         * for (ComboData calendar : calendars) {
         * ((JSONArray) json.get(MASTER_KEY_CALENDAR)).add(calendar);
         * }
         * }
         */

        // load Uom
        Map<String, Integer> uomMap = MasterManager.getAllUomDigits();
        if (uomMap != null) {
            if (!json.containsKey(MASTER_KEY_UOM)) {
                json.put(MASTER_KEY_UOM, new JSONArray());
            }
            for (String uom : uomMap.keySet()) {

                ComboData entity = new ComboData();
                entity.setId(uom);
                entity.setText(StringUtil.toSafeString(uomMap.get(uom)));
                ((JSONArray) json.get(MASTER_KEY_UOM)).add(entity);
            }
        }

        // load user offices
        List<UserOffice> userOffices = userInfo.getUserOffice();
        if (userOffices != null) {

            if (!json.containsKey(MASTER_KEY_USEROFFICE)) {
                json.put(MASTER_KEY_USEROFFICE, new JSONArray());
            }

            for (UserOffice userOffice : userOffices) {
                ComboData comboData = new ComboData();
                comboData.setId(StringUtil.toSafeString(userOffice.getOfficeId()));
                comboData.setText(userOffice.getOfficeCode());
                ((JSONArray) json.get(MASTER_KEY_USEROFFICE)).add(comboData);
            }
        }
        /*
         * //load user office there are active
         * List<UserOffice> userOfficesAll = userInfo.getUserOffice();
         * if(null != userOfficesAll) {
         * if (!json.containsKey(MASTER_KEY_USEROFFICE_ACTIVE)) {
         * json.put(MASTER_KEY_USEROFFICE_ACTIVE, new JSONArray());
         * }
         * for (UserOffice userOffice : userOfficesAll) {
         * if(null != userOffice && null != userOffice.getActiveFlag() && ActiveFlag.N == userOffice.getActiveFlag()) {
         * continue;
         * }
         * ComboData comboData = new ComboData();
         * comboData.setId(StringUtil.toSafeString(userOffice.getOfficeId()));
         * comboData.setText(userOffice.getOfficeCode());
         * ((JSONArray) json.get(MASTER_KEY_USEROFFICE_ACTIVE)).add(comboData);
         * }
         * }
         * // load config contents
         * String configMaxRows = ConfigManager.getProperty(SELECTED_MAX_ROWS);
         * if (configMaxRows != null) {
         * if (!json.containsKey(SELECTED_MAX_ROWS)) {
         * json.put(SELECTED_MAX_ROWS, configMaxRows);
         * }
         * }
         * // load xxxx TODO
         * logger.debug("method MasterDataController.loadMasterDatas is end");
         */
        return json;
    }

    /**
     * Load all supplier combo data.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all supplier
     */
    @RequestMapping(value = "/common/loadAllOffices",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadAllOffices(HttpServletRequest request, HttpServletResponse response) {
        PageResult<ComboData> result = new PageResult<ComboData>();

        // query all suppliers
        List<TnmOffice> offices = commonService.getAllOffices();

        // convert to combo data
        // id : supplier id, text : supplier code
        List<ComboData> comboOffices = null;
        if (offices != null) {
            comboOffices = new ArrayList<ComboData>();
            for (TnmOffice office : offices) {
                ComboData combo = new ComboData();
                combo.setId(String.valueOf(office.getOfficeId()));
                combo.setText(office.getOfficeCode());
                comboOffices.add(combo);
            }
        }
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load all supplier combo data.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all supplier
     */
    @RequestMapping(value = "/common/loadAllOfficesWithoutInactive",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadAllOfficesWithoutInactive(HttpServletRequest request, HttpServletResponse response) {

        PageResult<ComboData> result = new PageResult<ComboData>();

        // query all suppliers
        List<TnmOffice> offices = commonService.getAllOffices();

        // convert to combo data
        // id : supplier id, text : supplier code
        List<ComboData> comboOffices = null;
        if (offices != null) {
            comboOffices = new ArrayList<ComboData>();
            for (TnmOffice office : offices) {

                if (office.getInactiveFlag() == null || office.getInactiveFlag().equals(CodeConst.ActiveFlag.Y)) {
                    continue;
                }

                ComboData combo = new ComboData();
                combo.setId(String.valueOf(office.getOfficeId()));
                combo.setText(office.getOfficeCode());
                comboOffices.add(combo);
            }
        }
        result.setDatas(comboOffices);

        return result;
    }

    /**
     * Load all active regions.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active regions
     */
    @RequestMapping(value = "/common/loadActiveRegions",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadActiveRegions(HttpServletRequest request, HttpServletResponse response) {

        // id : region code, text : region code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmRegion> regions = commonService.getActiveRegions();
        if (regions != null && regions.size() > 0) {
            for (TnmRegion item : regions) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(item.getRegionCode()));
                combo.setText(item.getRegionCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load all active regions.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active regions
     */
    @RequestMapping(value = "/common/loadActiveRegionsID",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadActiveRegionsId(HttpServletRequest request, HttpServletResponse response) {

        // id : region code, text : region code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmRegion> regions = commonService.getActiveRegions();
        if (regions != null && regions.size() > 0) {
            for (TnmRegion item : regions) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(item.getRegionId()));
                combo.setText(item.getRegionCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load all active suppliers.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active suppliers
     */
    @RequestMapping(value = "/common/loadActiveSuppliers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadActiveSuppliers(HttpServletRequest request, HttpServletResponse response) {

        // id : supplier id, text : supplier code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmSupplier> suppliers = commonService.getActiveSuppliers();
        if (suppliers != null && suppliers.size() > 0) {
            for (TnmSupplier item : suppliers) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(item.getSupplierId()));
                combo.setText(item.getSupplierCode());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load all active suppliers.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active suppliers
     */
    @RequestMapping(value = "/common/loadActiveSuppliersCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadActiveSuppliersCode(HttpServletRequest request, HttpServletResponse response) {

        // id : supplier code, text : supplier code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmSupplier> suppliers = commonService.getActiveSuppliers();
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
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/common/loadOfficeCustomers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCurrentOfficeCustomers(HttpServletRequest request, HttpServletResponse response) {

        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmCustomer> customers = commonService.getCurrentOfficeCustomers(this.getLoginUser(request).getOfficeId());
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
     * Load current office users.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of current office users
     */
    @RequestMapping(value = "/common/loadOfficeUsers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCurrentOfficeUsers(HttpServletRequest request, HttpServletResponse response) {

        // id : user id, text : login id
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmUser> users = commonService.getCurrentOfficeUsers(this.getLoginUser(request).getOfficeId());
        if (users != null && users.size() > 0) {
            for (TnmUser user : users) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(user.getUserId()));
                combo.setText(user.getLoginId());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load all active revision reasons for Aisin.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active revision reasons
     */
    @RequestMapping(value = "/common/loadRevisionReasonsForAisin",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadRevisionReasonsForAisin(HttpServletRequest request, HttpServletResponse response) {

        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmReason> revisionReasons = commonService.getActiveRevisionReasons(CodeConst.BusinessPattern.AISIN);
        if (revisionReasons != null && revisionReasons.size() > 0) {
            for (TnmReason reason : revisionReasons) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(reason.getReasonCode()));
                combo.setText(reason.getReasonDesc());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load all active revision reasons for VV.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of all active revision reasons
     */
    @RequestMapping(value = "/common/loadRevisionReasonsForVv",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadRevisionReasonsForVv(HttpServletRequest request, HttpServletResponse response) {

        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        List<TnmReason> revisionReasons = commonService.getActiveRevisionReasons(CodeConst.BusinessPattern.V_V);
        if (revisionReasons != null && revisionReasons.size() > 0) {
            for (TnmReason reason : revisionReasons) {
                ComboData combo = new ComboData();
                combo.setId(StringUtil.toSafeString(reason.getReasonCode()));
                combo.setText(reason.getReasonDesc());
                comboList.add(combo);
            }
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Get session key.
     * 
     * @param request the request
     * @param response the response
     * @return the combo data of current office users
     */
    @RequestMapping(value = "/common/getSessionKey",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> getDynamicSessionKey(HttpServletRequest request, HttpServletResponse response) {

        BaseResult<String> baseResult = new BaseResult<String>();
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        String sessionKey = StringUtil.getSessionKey(new Date().getTime());
        if (context.get(sessionKey) != null) {
            sessionKey = StringUtil.getSessionKey(new Date().getTime());
        }
        baseResult.setData(sessionKey);

        return baseResult;
    }

}
