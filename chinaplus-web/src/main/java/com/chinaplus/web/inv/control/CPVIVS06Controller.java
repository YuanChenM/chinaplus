/**
 * CPVIVS06Controller.java
 * 
 * @screen CPVIVS06
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVS06Entity;
import com.chinaplus.web.inv.service.CPVIVS06Service;

/**
 * Post GR/GI Controller.
 */
@Controller
public class CPVIVS06Controller extends BaseController {

    /** Post GR/GI Service */
    @Autowired
    private CPVIVS06Service cpvivs06Service;

    /**
     * Post GR/GI.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return process result
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVS06/post",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> post(@RequestBody ObjectParam<CPVIVS06Entity> param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        super.setCommonParam(param, request);
        CPVIVS06Entity data = param.getData();
        Integer invoiceSummaryId = data.getInvoiceSummaryId();
        Integer version = data.getVersion();

        // Basic Validation
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        String grd = data.getGrDate();
        String gri = data.getGiDate();
        Date grDate = null;
        Date giDate = null;
        if (StringUtil.isEmpty(grd)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPVIVS06_Label_GRDate" });
            messageLists.add(message);
        } else {
            grDate = DateTimeUtil.parseDate(grd);
            if (grDate == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_028);
                message.setMessageArgs(new String[] { "CPVIVS06_Label_GRDate" });
                messageLists.add(message);
            }
        }
        if (StringUtil.isEmpty(gri)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPVIVS06_Label_GIDate" });
            messageLists.add(message);
        } else {
            giDate = DateTimeUtil.parseDate(gri);
            if (giDate == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_028);
                message.setMessageArgs(new String[] { "CPVIVS06_Label_GIDate" });
                messageLists.add(message);
            }
        }
        if (grDate != null && giDate != null && giDate.compareTo(grDate) < 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_009);
            message.setMessageArgs(new String[] { "CPVIVS06_Label_GIDate", "CPVIVS06_Label_GRDate" });
            messageLists.add(message);
        }
        if (messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // Exclusive Validation
        if (cpvivs06Service.checkExclusive(invoiceSummaryId, version)) {
            throw new BusinessException(MessageCodeConst.W1022);
        }

        // Post GR/GI
        cpvivs06Service.doPost(param.getCurrentOfficeId(), param.getLoginUserId(), invoiceSummaryId, grDate, giDate);

        return new BaseResult<String>();
    }

}
