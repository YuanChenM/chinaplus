/**
 * CPOOCS01Controller.java
 * 
 * @screen CPOOCS01
 * @author shi_yuxi
 */
package com.chinaplus.web.om.control;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.CodeConst.CustForcastStatus;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.TntCfcMasterShow;
import com.chinaplus.web.om.service.CPOOCS01Service;

/**
 * Order Calculation Supporting Data Report Download Screen
 */
@Controller
public class CPOOCS01Controller extends BaseController {

    /** CURRENT */
    private static final String CURRENT = "1";

    /** LAST */
    private static final String LAST = "2";
    @Autowired
    private CPOOCS01Service service;

    /**
     * get mpo list for screen CPOOCS01 by filter.
     * 
     * @param request request
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "/om/CPOOCS01/getList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<TntCfcMasterShow> getRoleList(HttpServletRequest request, @RequestBody PageParam param)
        throws Exception {
        // set common param
        this.setCommonParam(param, request);
        String customerIds = (String) param.getSwapData().get("customerId");
        if (StringUtil.isEmpty(customerIds)) {
            PageResult<TntCfcMasterShow> resultEmpty = new PageResult<TntCfcMasterShow>();
            return resultEmpty;
        }
        StringUtil.buildLikeCondition(param, "cfcNo");
        param.getSwapData().put("status", CustForcastStatus.Y);
        Date officeDate = service.getDBDateTime(param.getOfficeTimezone());
        Calendar c = Calendar.getInstance();
        c.setTime(officeDate);
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);

        param.getSwapData().put("fcDate",
            DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, c.getTime())));
        String fcDateLan = (String) param.getFilters().get("fcDateLan");
        Calendar cal = Calendar.getInstance();
        cal.setTime(officeDate);
        cal.add(Calendar.MONTH, -1);
        Date lastMonth = cal.getTime();
        if (CURRENT.equals(fcDateLan)) {
            param.getFilters().put(
                "fcDateLanFrom",
                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.firstDay(officeDate))));
            param.getFilters().put(
                "fcDateLanTo",
                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.lastDay(officeDate))));
        } else if (LAST.equals(fcDateLan)) {
            param.getFilters().put(
                "fcDateLanFrom",
                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.firstDay(lastMonth))));
            param.getFilters().put(
                "fcDateLanTo",
                DateTimeUtil.parseDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                    DateTimeUtil.lastDay(lastMonth))));
        }

        // find data by paging
        PageResult<TntCfcMasterShow> result = service.getPageList(param);
        List<TntCfcMasterShow> list = result.getDatas();
        result.setDatas(list);
        return result;
    }
}
