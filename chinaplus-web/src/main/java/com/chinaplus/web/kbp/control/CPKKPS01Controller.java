/**
 * CPKKPS01Controller.java
 * 
 * @screen CPKKPS01
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.CPKKPS01Entity;
import com.chinaplus.web.kbp.service.CPKKPS01Service;
import com.chinaplus.web.kbp.service.CPKKPS01UpdateService;

/**
 * Kanban Issued Plan Screen Controller.
 */
@Controller
public class CPKKPS01Controller extends BaseController {

    /** Kanban Issued Plan Screen Service */
    @Autowired
    private CPKKPS01Service cpkkps01Service;

    /** Kanban Issued Plan Screen Service */
    @Autowired
    private CPKKPS01UpdateService cpkkps01UpdateService;

    /**
     * Load kanban list.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return kanban list
     */
    @RequestMapping(value = "/kbp/CPKKPS01/loadKanbanList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPKKPS01Entity> getKanbanList(@RequestBody PageParam param, HttpServletRequest request) {

        super.setCommonParam(param, request);
        StringUtil.buildLikeCondition(param, "kanbanPlanNoDisplay");
        StringUtil.buildDateTimeCondition(param, "lastUploadTime");

        PageResult<CPKKPS01Entity> result = cpkkps01Service.getKanbanList(param);
        List<CPKKPS01Entity> list = result.getDatas();
        List<Integer> deleteSameKanbanNoList = new ArrayList<Integer>();
        HashMap<String, String> differenceKanbanNoMap = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            String kanbanNoDiplay = list.get(i).getKanbanPlanNoDisplay();
            if (differenceKanbanNoMap.containsKey(kanbanNoDiplay)) {
                deleteSameKanbanNoList.add(i);
            } else {
                differenceKanbanNoMap.put(kanbanNoDiplay, kanbanNoDiplay);
            }

            int airFlag = list.get(i).getAirFlag().intValue();
            int seaFlag = list.get(i).getSeaFlag().intValue();
            int language = param.getLanguage();

            if (airFlag == 1 && seaFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.SEA)
                            + StringConst.COMMA
                            + CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                                CodeConst.TransportMode.AIR));
            } else if (seaFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.SEA));
            } else if (airFlag == 1) {
                list.get(i).setTransportMode(
                    CodeCategoryManager.getCodeName(language, CodeMasterCategory.TRANSPORT_MODE,
                        CodeConst.TransportMode.AIR));
            }
        }
        for (int i = deleteSameKanbanNoList.size() - 1; i > -1; i--) {
            list.remove(deleteSameKanbanNoList.get(i).intValue());
        }
        return result;
    }

    /**
     * Cancel kanban.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @return BaseResult
     * @throws Exception exception
     */
    @RequestMapping(value = "kbp/CPKKPS01/cancelKanban",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<String> cancelKanban(@RequestBody ObjectParam<TntKanban> param, HttpServletRequest request)
        throws Exception {

        super.setCommonParam(param, request);

        // Check the Kanban plan has received the invoice or not.
        int countReceivedInvoice = cpkkps01Service.getReceivedInvoiceCount(param);
        if (countReceivedInvoice > 0) {
            throw new BusinessException(MessageCodeConst.W1026);
        }

        // Check the Kanban plan is already cancelled or not.
        int countCancelledKanban = cpkkps01Service.getCancelledKanbanCount(param);
        if (countCancelledKanban > 0) {
            throw new BusinessException(MessageCodeConst.W1013);
        }

        // Update TNT_KANBAN's status.
        // Update TNT_KANBAN's TOTAL_xxx_QTY.
        // Delete order status data.
        cpkkps01UpdateService.doCancelKanban(param);

        BaseResult<String> result = new BaseResult<String>();
        return result;
    }
}
