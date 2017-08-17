/**
 * Controller of Match Invoice List Screen.
 * 
 * @screen CPIIFS02
 * @author zhang_chi
 */
package com.chinaplus.web.inf.control;

import java.util.List;
import java.util.Locale;
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
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.inf.entity.CPIIFS02Entity;
import com.chinaplus.web.inf.service.CPIIFS02Service;

/**
 * Controller of Match Invoice List Screen.
 */
@Controller
public class CPIIFS02Controller extends BaseController {

    /**
     * cpiifs02Service.
     */
    @Autowired
    private CPIIFS02Service cpiifs02Service;

    /**
     * get mpo list for screen CPIIFS02 by filter.
     * 
     * @param param param
     * @param request request
     * @return PageResult
     * @throws Exception e
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "inf/CPIIFS02/getCPIIFS02EntityList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPIIFS02Entity> getDetailsList(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {

        UserInfo loginUser = getLoginUser(request);
        Locale langs = MessageManager.getLanguage(loginUser.getLanguage().getCode()).getLocale();
        String value = MessageManager.getMessage("CPIIFS02_Grid_MatchInvoice", langs);
        param.getFilters().put("matchInvoice", value);
        Map<String, Object> filters = param.getFilters();
        if (filters.containsKey("matchedInvocie")) {
            List<String> matchedInvocie = (List<String>) filters.get("matchedInvocie");
            String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
            if (matchedInvocie != null && matchedInvocie.size() > 0 && !matchedInvocie.contains(id)) {
                param.getFilters().put("matchedInvoiceNOBlank", true);
            }
        }

        // find data by paging
        PageResult<CPIIFS02Entity> result = cpiifs02Service.getPageList(param);
        List<CPIIFS02Entity> list = result.getDatas();
        for (int i = 0; i < list.size(); i++) {

            // get entity
            CPIIFS02Entity entity = list.get(i);
            int status = Integer.parseInt(entity.getStatus());
            if (status == CodeConst.InvoiceMatchStatus.MATCHED) {
                entity.setMatchInvoice(null);
            }

            // set display dspMismatchDate
            entity.setDspMismatchDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, entity.getMismatchDate()));
        }
        return result;
    }

    /**
     * Load Office Code combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS02/loadOfficeCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadOfficeCode(HttpServletRequest request) throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpiifs02Service.getOfficeCode();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load InvoiceNo combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS02/loadMatchedInvoice",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadInvoiceNo(HttpServletRequest request) throws Exception {
        UserInfo userInfo = getLoginUser(request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpiifs02Service.getMatchedInvoice(userInfo.getLanguage().getName());
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * Load WhsInvoiceNo combo data.
     * 
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS02/loadWrongInvocie",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadWhsInvoiceNo(HttpServletRequest request) throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        // convert to combo data
        List<ComboData> comboOffices = cpiifs02Service.getWrongInvocie();
        result.setDatas(comboOffices);
        return result;
    }

    /**
     * match invocie with combo data.
     * 
     * @param param param
     * @param request the request
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "inf/CPIIFS02/matchInvoiceList",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> matchInvoiceList(@RequestBody PageParam param, HttpServletRequest request) throws Exception {

        // set common parameter
        super.setCommonParam(param, request);
        
        // do matching invoice
        cpiifs02Service.doMatchInvoice(param);
        
        return new BaseResult<String>();
    }

}
