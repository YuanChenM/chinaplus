/**
 * CPOOHS01Controller.
 * 
 * @author Xiang_chao
 * @screen CPOOHS01
 */
package com.chinaplus.web.om.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.om.entity.CPOOHS01Entity;
import com.chinaplus.web.om.service.CPOOHS01Service;

/**
 * Order Forecast Summary Screen
 */
@Controller
public class CPOOHS01Controller extends BaseFileController {

    @Autowired
    private CPOOHS01Service service;

    /**
     * list get list result
     * 
     * @param param
     *        PageParam
     * @param request
     *        HttpServletRequest
     * @return PageResult<CPMCLS01Entity>
     */
    @RequestMapping(value = "/om/CPOOHS01/list",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPOOHS01Entity> init(@RequestBody PageParam param, HttpServletRequest request) {
        StringUtil.buildLikeCondition(param, "ttcPartsNo");
        StringUtil.buildLikeCondition(param, "impPONo");
        StringUtil.buildLikeCondition(param, "expPONo");
        StringUtil.buildLikeCondition(param, "customerOrderNo");
        setCommonParam(param, request);
        PageResult<CPOOHS01Entity> result = service.getPageList("getAllListCount", "getAllList", param);
        List<CPOOHS01Entity> orderHistorylist = result.getDatas();
        List<Integer> orderStatusIdList = new ArrayList<Integer>();
        if (orderHistorylist != null && orderHistorylist.size() > 0) {
            for (CPOOHS01Entity entity : orderHistorylist) {
                Integer orderStatusId = entity.getOrderStatusId();
                if (!orderStatusIdList.contains(orderStatusId)) {
                    orderStatusIdList.add(orderStatusId);
                }
                entity.setUomCode(StringUtil.toSafeString(MasterManager.getUomDigits(entity.getUomCode())));
            }
        }
        if (orderStatusIdList.size() == 0) {
            return result;
        }
        Map<String, Object> swapData = new HashMap<String, Object>();
        swapData.put("orderStatusIdList", orderStatusIdList);
        param.setSwapData(swapData);
        PageResult<CPOOHS01Entity> transferInfoResult = service.getAllList("getTransferInfoList", param);
        List<CPOOHS01Entity> transferInfolist = transferInfoResult.getDatas();
        Map<Integer, Map<String, StringBuffer>> transferInfoMap = new HashMap<Integer, Map<String, StringBuffer>>();
        if (transferInfolist != null && transferInfolist.size() > 0) {
            for (CPOOHS01Entity entity : transferInfolist) {
            	int digits = MasterManager.getUomDigits(entity.getUomCode());
                Integer orderStatusId = entity.getOrderStatusId();
                String toCode = entity.getTransferToCode();
                String fromCode = entity.getTransferFromCode();
                BigDecimal toQty = entity.getTransferToQty();
                Map<String, StringBuffer> transferMap = transferInfoMap.get(orderStatusId);
                if (transferMap == null) {
                    transferMap = new HashMap<String, StringBuffer>();
                    StringBuffer transferToDetails = new StringBuffer();
                    StringBuffer transferFrom = new StringBuffer();
                    transferMap.put("transferToDetails", transferToDetails);
                    transferMap.put("transferFrom", transferFrom);
                    transferInfoMap.put(orderStatusId, transferMap);
                }
                StringBuffer transferToDetails = transferMap.get("transferToDetails");
                StringBuffer transferFrom = transferMap.get("transferFrom");
                if (!StringUtil.isNullOrEmpty(toCode) && toQty != null) {
                    if (transferToDetails.length() > 0) {
                        transferToDetails.append(",");
                    }
                    transferToDetails.append(toCode);
                    transferToDetails.append("(");
                    transferToDetails.append(toQty.setScale(digits));
                    transferToDetails.append(")");
                }
                if (!StringUtil.isNullOrEmpty(fromCode)) {
                    if (transferFrom.length() > 0) {
                        if (transferFrom.toString().contains(fromCode)) {
                            continue;
                        }
                        transferFrom.append(",");
                    }
                    transferFrom.append(fromCode);
                }
            }
        }
        if (transferInfoMap.size() > 0) {
            for (CPOOHS01Entity entity : orderHistorylist) {
                Integer orderStatusId = entity.getOrderStatusId();
                if (transferInfoMap.containsKey(orderStatusId)) {
                    entity.setTransferToDetails(transferInfoMap.get(orderStatusId).get("transferToDetails").toString());
                    entity.setTransferFrom(transferInfoMap.get(orderStatusId).get("transferFrom").toString());
                }
            }
        }
        return result;
    }

    /**
     * Change the disStatus to Force Complete.
     * 
     * @param param param
     * @param request request
     * @throws Exception e
     * @return result
     * 
     * @author Common
     */
    @RequestMapping(value = { "/om/CPOOHS01/forceComplete" })
    @ResponseBody
    protected BaseResult<String> changeOffice(@RequestBody PageParam param, HttpServletRequest request)
        throws Exception {

        // get user information
        UserManager um = UserManager.getLocalInstance(this.getLoginUser(request));
        // get loginUserId
        Integer userId = um.getUserInfo().getUserId();
        param.setLoginUserId(userId);
        // get orderStatusId list
        List<String> orderStatusIdList = param.getSelectedDatas();
        // do update ForceCompleted
        service.doForceCompletedUpdate(param, orderStatusIdList);

        return new BaseResult<String>();
    }

    @Override
    protected String getFileId() {
        // TODO Auto-generated method stub
        return null;
    }
}
