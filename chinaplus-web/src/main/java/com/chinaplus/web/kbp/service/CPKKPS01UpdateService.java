/**
 * CPKKPS01UpdateService.java
 * 
 * @screen CPKKPS01
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.ObjectParam;

/**
 * Kanban Issued Plan Screen Service.
 */
@Service
public class CPKKPS01UpdateService extends BaseService {

    /** Kanban Issued Plan Screen Service */
    @Autowired
    private CPKKPS01Service cpkkps01Service;

    /**
     * Cancel kanban.
     * 
     * @param param BaseParam
     */
    public void doCancelKanban(ObjectParam<TntKanban> param) {
        // Update TNT_KANBAN's status.
        // Condition : TNT_KANBAN.KANBAN_PLAN_NO IN {selected Kanban plan'KANBAN_PLAN_NO}
        param.setSwapData("CANCELLED_DATE", getDBDateTime(param.getOfficeTimezone()));
        cpkkps01Service.updateKanbanToCancel(param);

        // Loop each selected Kanban plan.
        List<TntKanban> selectedList = param.getDatas();
        for (int i = 0; i < selectedList.size(); i++) {
            TntKanban entity = selectedList.get(i);

            // Find total informations for these Kanban Plan.
            TntKanban entityForUpdate = cpkkps01Service.getTotalInformation(entity);

            // Update TNT_KANBAN's TOTAL_xxx_QTY.
            // Condition : TNT_KANBAN.KANBAN_ID IN {selected Kanban plan'KANBAN_ID}
            cpkkps01Service.updateKanbanTotalQtyInfo(entityForUpdate, param);

            // Delete order status data.
            cpkkps01Service.deleteOrderStatusByKanbanPlanNo(entity);

            // Delete pfc shipping data.
            cpkkps01Service.deleteTfcShipping(entity);

            // Delete pfc detail data.
            cpkkps01Service.deleteTfcDetail(entity);
        }
    }
}
