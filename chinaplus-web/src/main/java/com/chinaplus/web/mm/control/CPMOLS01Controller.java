/**
 * CPMOLS01Control.
 * 
 * @author shi_yuxi
 * @screen CPMOLS01
 */
package com.chinaplus.web.mm.control;

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
import com.chinaplus.web.mm.entity.CPMOLS01Entity;
import com.chinaplus.web.mm.service.CPMOLS01Service;

/**
 * Office List Screen                                           
 */
@Controller
public class CPMOLS01Controller extends BaseController {

    /** Office List service */
    @Autowired
    private CPMOLS01Service service;

    /**
     * list
     * get list result
     * 
     * @param param PageParam
     * @return List<CPMOLS01Entity>
     */
    @RequestMapping(value = "/mm/CPMOLS01/init",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMOLS01Entity> list(@RequestBody PageParam param) {
        StringUtil.buildLikeCondition(param, "officeCode");
        StringUtil.buildLikeCondition(param, "officeName");
        StringUtil.buildLikeCondition(param, "calendarCode");
        StringUtil.buildLikeCondition(param, "createBy");
        PageResult<CPMOLS01Entity> result = service.getPageList("getOfficeListCount", "getOfficeList", param);
        return result;
    }

}
