/**
 * CPVIVS04Controller.java
 * 
 * @screen CPVIVS04
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.inv.entity.CPVIVS04Entity;
import com.chinaplus.web.inv.service.CPVIVS04Service;

/**
 * Irregular Shipping Schedule Controller.
 */
@Controller
public class CPVIVS04Controller extends BaseController {

    /** Parameter: Upload ID */
    private static final String PARAM_UPLOAD_ID = "uploadId";

    /** Irregular Shipping Schedule Service */
    @Autowired
    private CPVIVS04Service cpvivs04Service;

    /**
     * Load irregular data list.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @return irregular data list
     */
    @RequestMapping(value = "/inv/CPVIVS04/loadIrregularData",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPVIVS04Entity> getIrregularData(@RequestBody BaseParam param, HttpServletRequest request) {

        String uploadId = (String) param.getSwapData().get(PARAM_UPLOAD_ID);
        List<CPVIVS04Entity> irregularList = cpvivs04Service.getIrregularParts(uploadId);
        for (CPVIVS04Entity irregularData : irregularList) {
            irregularData.setDisEtd(DateTimeUtil.getDisDate(irregularData.getEtd()));
        }

        PageResult<CPVIVS04Entity> result = new PageResult<CPVIVS04Entity>();
        result.setDatas(irregularList);
        return result;
    }

    /**
     * Confirm irregular shipping.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return process result
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVS04/confirm",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> confrim(@RequestBody ObjectParam<CPVIVS04Entity> param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        // Do input value check
        super.setCommonParam(param, request);
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Map<String, Date[]> dateMap = new HashMap<String, Date[]>();
        List<CPVIVS04Entity> irregularDatas = param.getDatas();
        if (irregularDatas != null && irregularDatas.size() > 0) {
            for (int i = 0; i < irregularDatas.size(); i++) {
                CPVIVS04Entity irregularData = irregularDatas.get(i);
                Date etd = DateTimeUtil.parseDate(irregularData.getDisEtd());
                String strEta = irregularData.getEta();
                String mapKey = irregularData.getVesselName() + StringConst.UNDERLINE + irregularData.getDisEtd();
                Date[] dateArray = dateMap.get(mapKey);
                if (dateArray == null) {
                    dateArray = new Date[IntDef.INT_TWO];
                    dateMap.put(mapKey, dateArray);
                }

                // ETA
                Date eta = null;
                if (StringUtil.isEmpty(strEta)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_010);
                    message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_ETA" });
                    messageLists.add(message);
                } else {
                    eta = DateTimeUtil.parseDate(strEta);
                    if (eta == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_011);
                        message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_ETA" });
                        messageLists.add(message);
                    } else {
                        dateArray[0] = eta;
                    }
                    if (etd != null && eta != null && eta.compareTo(etd) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_020);
                        message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_ETA",
                            "CPVIVS04_Grid_ETD" });
                        messageLists.add(message);
                    }
                }

                // Inbound Plan Date
                String strPlanInbound = irregularData.getPlanInbound();
                Date planInbound = null;
                if (StringUtil.isEmpty(strPlanInbound)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_010);
                    message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_PlanInbound" });
                    messageLists.add(message);
                } else {
                    planInbound = DateTimeUtil.parseDate(strPlanInbound);
                    if (planInbound == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_011);
                        message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_PlanInbound" });
                        messageLists.add(message);
                    } else {
                        dateArray[1] = planInbound;
                    }
                    if (eta != null && planInbound != null && planInbound.compareTo(eta) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1003_020);
                        message.setMessageArgs(new String[] { String.valueOf(i + 1), "CPVIVS04_Grid_PlanInbound",
                            "CPVIVS04_Grid_ETA" });
                        messageLists.add(message);
                    }
                }
            }
        }

        if (messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // Save ETA and Plan Inbound Date to DB
        String uploadId = (String) param.getSwapData().get(PARAM_UPLOAD_ID);
        cpvivs04Service.doIrregularShippingUpdate(param.getLoginUserId(), uploadId, dateMap);

        return new BaseResult<String>();
    }

}
