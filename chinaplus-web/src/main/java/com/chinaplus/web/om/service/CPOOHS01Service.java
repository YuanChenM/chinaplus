/**
 * CPOOFS01Service.
 * 
 * @author shi_yuxi
 * @screen CPOOFS01
 */
package com.chinaplus.web.om.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.om.entity.CPOOHS01Entity;


/** 
 * Order Forecast Summary Screen                                                    
 */ 
@Service
public class CPOOHS01Service extends BaseService{

    /** SQL ID: update ForceCompleted Qty */
    private static final String SQLID_UPDATE_FORCECOMPLETED_QTY = "updateForceCompletedQty";

    /** SQL ID: select Order Details */
    private static final String SQLID_SELECT_ORDERDETAIL_LIST = "getOrderDetailList";

    /** SQL ID: update ForceCompleted User */
    private static final String SQLID_UPDATE_FORCECOMPLETED_USER = "updateForceCompletedUser";

    /** SQL ID: update ForceCompleted Plan */
    private static final String SQLID_UPDATE_FORCECOMPLETED_PLAN = "updateForceCompletedPlan";
	/**
     * do upload for ForceCompleted
     * 
     * @param param BaseParam
     * @param orderStatusIdList List<String>
     */
    public void doForceCompletedUpdate(BaseParam param, List<String> orderStatusIdList) {
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
    	Integer loginUserId = param.getLoginUserId();
        param.setSwapData("loginUserId", loginUserId);
        param.setSwapData("systemTime", systemTime);
        param.setSwapData("orderStatusIdList", orderStatusIdList);
        super.baseMapper.update(SQLID_UPDATE_FORCECOMPLETED_QTY, param);
        List<CPOOHS01Entity> orderDetailList = super.baseMapper.select(SQLID_SELECT_ORDERDETAIL_LIST, param);
        if (orderDetailList != null && orderDetailList.size() > 0){
        	for (CPOOHS01Entity entity : orderDetailList){
                param.setSwapData("orderId", entity.getOrderId());
                param.setSwapData("expPONO", entity.getExpPONo());
                param.setSwapData("partsId", entity.getPartsId());
        		super.baseMapper.update(SQLID_UPDATE_FORCECOMPLETED_USER, param);
        	}
        }
        for (String orderStatusId : orderStatusIdList) {
            param.setSwapData("orderStatusId", orderStatusId);
            super.baseMapper.update(SQLID_UPDATE_FORCECOMPLETED_PLAN, param);
        }
    }
}
