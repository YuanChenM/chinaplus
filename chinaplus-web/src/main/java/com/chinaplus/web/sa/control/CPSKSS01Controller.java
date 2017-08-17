/**
 * CPSKSS01Controller.java
 * 
 * @screen CPSKSS01
 * @author shi_yuxi
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
import java.util.List;

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
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.service.CPSKSS01Service;

/**
 * kpi screen
 */
@Controller
public class CPSKSS01Controller extends BaseController {

    @Autowired
    private CPSKSS01Service service;

    /**
     * Load current office customers.
     * 
     * @param param param
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/CPSKSS01/loadOfficeCustomers",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCurrentOfficeCustomers(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {
        this.setCommonParam(param, request);
        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();

        PageResult<ComboData> result = new PageResult<ComboData>();
        String businessPattern = (String) param.getSwapData().get("businessPattern");
        if (!StringUtil.isEmpty(businessPattern)) {
            comboList = service.loadCustomers(param);
        }
        result.setDatas(comboList);

        return result;
    }

    /**
     * Load current office customers.
     * 
     * @param param param
     * @param request the request
     * @param response the response
     * @return the combo data of current office customers
     */
    @RequestMapping(value = "/CPSKSS01/loadBusinessPattern",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadBusinessPattern(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) {
        this.setCommonParam(param, request);
        // id : customer id, text : customer code
        List<ComboData> comboList = new ArrayList<ComboData>();
        UserInfo loginUser = getLoginUser(request);
        UserManager userManager = UserManager.getLocalInstance(loginUser);
        List<BusinessPattern> list = userManager.getCurrentBusPattern();
        List<String> idList = new ArrayList<String>();
        for (BusinessPattern bp : list) {
			int businessPatternId = bp.getBusinessPattern();
			if ( !idList.contains(String.valueOf(businessPatternId))) {
            ComboData combo = new ComboData();
            combo.setText(CodeCategoryManager.getCodeName(param.getLanguage(), CodeMasterCategory.BUSINESS_PATTERN,
						businessPatternId));
				combo.setId(String.valueOf(businessPatternId));
            comboList.add(combo);
				idList.add(String.valueOf(businessPatternId));
        }
        }
        PageResult<ComboData> result = new PageResult<ComboData>();
        result.setDatas(comboList);

        return result;
    }
}
