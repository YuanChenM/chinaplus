/**
 * VVPLS01Controller.
 * 
 * @author ren_yi
 * @screen VVPLS01
 */
package com.chinaplus.web.vvp.control;

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
import com.chinaplus.web.vvp.entity.VVPLS01Entity;
import com.chinaplus.web.vvp.service.VVPLS01Service;

/**
 * Order Forecast Summary Screen
 */
@Controller
public class VVPLS01Controller extends BaseController {

	@Autowired
	private VVPLS01Service vvpls01Service;

	    /**
     * Load single list.
     * 
     * @param param
     *        page parameter
     * @param request
     *        HttpServletRequest
     * @return single list
     */
    @RequestMapping(value = "/vvp/VVPLS01/VVPLS01PageList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<VVPLS01Entity> getOrderList(@RequestBody PageParam param, HttpServletRequest request) {
        StringUtil.buildLikeCondition(param, "fullCompanyName");
        super.setCommonParam(param, request);
        PageResult<VVPLS01Entity> result = vvpls01Service.getPageList("getAllListCount", "getAllList", param);
        List<VVPLS01Entity> list = result.getDatas();
        for (VVPLS01Entity entity : list) {
            entity.setCreatedDateForDisplay(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY,
                entity.getCreatedDate()));
            entity.setLastUpdatedDateForDisplay(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY,
                entity.getLastUpdatedDate()));
        }
        return result;
    }
}
