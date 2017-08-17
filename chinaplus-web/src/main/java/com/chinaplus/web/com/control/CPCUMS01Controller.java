/**
 * Controller of Role List Screen.
 * 
 * @screen CPCUMS01
 * @author zhang_chi
 */
package com.chinaplus.web.com.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCUMS01Entity;
import com.chinaplus.web.com.service.CPCUMS01Service;

/**
 * Controller of User List Screen.
 */
@Controller
public class CPCUMS01Controller extends BaseController {

    /**
     * cpcums01Service.
     */
    @Autowired
    private CPCUMS01Service cpcums01Service;

    /**
     * get mpo list for screen CPCUMS01 by filter.
     * 
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS01/getUserDetailsList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCUMS01Entity> getUserDetailsList(@RequestBody PageParam param) throws Exception {

        // condition prepare
        StringUtil.buildLikeCondition(param, "loginId");
        StringUtil.buildLikeCondition(param, "userName");
        StringUtil.buildLikeCondition(param, "mailAddr");

        // find data by paging
        PageResult<CPCUMS01Entity> result = cpcums01Service.getPageList(param);
        List<CPCUMS01Entity> datas = result.getDatas();
        for (CPCUMS01Entity data : datas) {
            String cusCodeSet = StringUtil.toSafeString(data.getCustomerCodeSet());
            cusCodeSet = cusCodeSet.replaceAll("<A>", "").replaceAll("</A>", "").replaceAll("<A/>", "");
            if (cusCodeSet.length() > 0) {
                cusCodeSet = cusCodeSet.substring(0, cusCodeSet.length() - 1);
            }
            data.setCustomerCodeSet(cusCodeSet);
        }

        return result;
    }

    /**
     * Delete selected roleIds.
     * 
     * @param request request
     * @param param param
     * @return BaseResult
     * @throws Exception e
     */
    @RequestMapping(value = "com/CPCUMS01/deleteUser",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> deleteUser(HttpServletRequest request, @RequestBody ObjectParam<TnmUser> param)
        throws Exception {

        // set language
        setCommonParam(param, request);

        // do delete
        cpcums01Service.doDeleteUser(param);

        // return result
        BaseResult<String> result = new BaseResult<String>();

        return result;
    }

}
