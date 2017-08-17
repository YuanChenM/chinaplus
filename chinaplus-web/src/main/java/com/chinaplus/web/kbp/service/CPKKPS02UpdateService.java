/**
 * CPKKPS02UpdateService.java
 * 
 * @screen CPKKPS02
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.web.kbp.entity.CPKKPS02Entity;

/**
 * Kanban Plan Detailed Information Screen Service.
 */
@Service
public class CPKKPS02UpdateService extends BaseService {

    /** Kanban Plan Detailed Information Screen Service */
    @Autowired
    private CPKKPS02Service cpkkps02Service;

    /**
     * Force Complete.
     * 
     * @param param BaseParam
     */
    public void doForceComplete(ObjectParam<CPKKPS02Entity> param) {
        // Update TNT_KANBAN_PARTS's status.
        cpkkps02Service.updateKanbanPartsToForceCompleted(param);

        // // Delete from TNT_KANBAN_SHIPPING_PARTS.
        // cpkkps02Service.deleteKanbanShippingParts(param);
        //
        // // Delete from TNT_KANBAN_SHIPPING.
        // cpkkps02Service.deleteKanbanShipping(param);

        // Update Kanban shipping to completed.
        cpkkps02Service.updateKanbanShippingToCompleted(param);

        // Loop each part in screen selected data,
        // and update TNF_ORDER_STATUS's FORCE_COMPLETED_QTY.
        List<CPKKPS02Entity> selectedList = param.getDatas();
        for (int i = 0; i < selectedList.size(); i++) {
            CPKKPS02Entity entity = selectedList.get(i);
            cpkkps02Service.updateKanbanShippingForceCompletedQty(entity, param);
        }
    }
}
