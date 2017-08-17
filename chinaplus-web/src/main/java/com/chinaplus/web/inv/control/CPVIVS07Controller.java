/**
 * CPVIVS07Controller.java
 * 
 * @screen CPVIVS07
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVS07Entity;
import com.chinaplus.web.inv.service.CPVIVS07Service;

/**
 * Create New Invoice Screen Controller.
 */
@Controller
public class CPVIVS07Controller extends BaseController {

    /** Condition:impOrderNo */
    private static final String CONDITION_IMP_ORDER_NO = "impOrderNo";

    /** Condition:cusOrderNo */
    private static final String CONDITION_CUS_ORDER_NO = "cusOrderNo";

    /** Create New Invoice Screen Service */
    @Autowired
    private CPVIVS07Service cpvivs07Service;

    /**
     * Load order list.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @return order list
     */
    @RequestMapping(value = "/inv/CPVIVS07/loadOrderList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPVIVS07Entity> getOrderList(@RequestBody PageParam param, HttpServletRequest request) {

        super.setCommonParam(param, request);
        StringUtil.buildLikeCondition(param, CONDITION_IMP_ORDER_NO);
        StringUtil.buildLikeCondition(param, CONDITION_CUS_ORDER_NO);
        PageResult<CPVIVS07Entity> result = cpvivs07Service.getPageList(param);
        return result;
    }

}
