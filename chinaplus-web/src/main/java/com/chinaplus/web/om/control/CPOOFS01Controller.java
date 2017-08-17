/**
 * CPOOFS01Controller.
 * 
 * @author shi_yuxi
 * @screen CPOOFS01
 */
package com.chinaplus.web.om.control;

import java.util.List;

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
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOFS01Entity;
import com.chinaplus.web.om.service.CPOOFS01Service;

/**
 * Order Forecast Summary Screen
 */
@Controller
public class CPOOFS01Controller extends BaseController {

    @Autowired
    private CPOOFS01Service service;

    /**
     * list
     * get list result
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return PageResult<CPMCLS01Entity>
     */
    @RequestMapping(value = "/om/CPOOFS01/list",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPOOFS01Entity> init(@RequestBody PageParam param, HttpServletRequest request) {
        StringUtil.buildLikeCondition(param, "orderForecastNo");
        setCommonParam(param, request);
        PageResult<CPOOFS01Entity> result = service.getPageList("getAllListCount", "getAllList", param);
        List<CPOOFS01Entity> list = result.getDatas();
        for(CPOOFS01Entity entity : list)
        {
            String orderMonth = DateTimeUtil.getDisOrderMonth(entity.getOrderMonth(), "yyyyMM");
            entity.setOrderMonth(orderMonth);
            String firstMonth = DateTimeUtil.getDisOrderMonth(entity.getFirstFcMonth(), "yyyyMM");
            entity.setFirstFcMonth(firstMonth);
            String lastMonth = DateTimeUtil.getDisOrderMonth(entity.getLastFcMonth(), "yyyyMM");
            entity.setLastFcMonth(lastMonth);
        }
        return result;
    }
}
