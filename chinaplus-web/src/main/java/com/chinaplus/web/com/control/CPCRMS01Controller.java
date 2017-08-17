/**
 * CPCRMS01Controller.java
 * 
 * @screen CPCRMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.com.control;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.entity.TnmRole;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.com.entity.CPCRMS01Entity;
import com.chinaplus.web.com.service.CPCRMS01Service;

/**
 * Controller of Role List Screen.
 * 
 */
@Controller
public class CPCRMS01Controller extends BaseController {

    /**
     * CPCRMS01Service.
     */
    @Autowired
    private CPCRMS01Service cpcrms01Service;

    /**
     * get mpo list for screen CPCRMS01 by filter.
     * 
     * @param param param
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "manage/CPCRMS01/getRoleList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPCRMS01Entity> getRoleList(@RequestBody PageParam param) throws Exception {

        // condition prepare
        StringUtil.buildLikeCondition(param, "roleName");
        StringUtil.buildLikeCondition(param, "roleNotes");

        // find data by paging
        PageResult<CPCRMS01Entity> result = cpcrms01Service.getPageList(param);

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
    @RequestMapping(value = "manage/CPCRMS01/deleteRole",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> deleteRole(HttpServletRequest request, @RequestBody ObjectParam<TnmRole> param)
        throws Exception {

        // do delete
        cpcrms01Service.doDeleteRole(param);

        // return result
        BaseResult<String> result = new BaseResult<String>();
        // result.setMessages(new BaseMessage[] { new BaseMessage(MessageCodeConst.I1004,
        // new String[] { "CPCRMS01_Button_Delete" }) });

        return result;
    }

}
