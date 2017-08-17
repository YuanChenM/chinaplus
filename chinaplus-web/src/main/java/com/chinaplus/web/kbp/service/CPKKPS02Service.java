/**
 * CPKKPS02Service.java
 * 
 * @screen CPKKPS02
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.kbp.entity.CPKKPS02Entity;

/**
 * Kanban Plan Detailed Information Screen Service.
 */
@Service
public class CPKKPS02Service extends BaseService {

    /**
     * Get kanban info.
     * 
     * @param param ObjectParam<String>
     * @return kanban info
     */
    public CPKKPS02Entity getKanbanInfo(ObjectParam<TntKanban> param) {
        List<CPKKPS02Entity> list = baseMapper.select(getSqlId("getKanbanInfo"), param);
        return list.get(0);
    }

    /**
     * Get part info list.
     * 
     * @param param page parameter
     * @return part info list
     */
    public PageResult<CPKKPS02Entity> getPartInfoList(PageParam param) {

        PageResult<CPKKPS02Entity> result = super.getPageList("findPartInfoPageListCount", "findPartInfoPageList",
            param);

        return result;
    }

    /**
     * Get upload info list.
     * 
     * @param param page parameter
     * @return upload info list
     */
    public PageResult<CPKKPS02Entity> getUploadInfoList(PageParam param) {

        PageResult<CPKKPS02Entity> result = super.getPageList("findUploadInfoPageListCount", "findUploadInfoPageList",
            param);

        return result;
    }

    /**
     * Check the Kanban plan is already cancelled or not.
     * 
     * @param param BaseParam
     * @return data count
     */
    public int getCancelledKanbanCount(BaseParam param) {

        int count = super.getDatasCount(getSqlId("getCancelledKanbanCount"), param);
        return count;
    }

    /**
     * Get Parts On Shipping Qty
     * 
     * @param entity CPKKPS02Entity
     * @return Parts On Shipping Qty
     */
    public BigDecimal getPartsOnShippingQty(CPKKPS02Entity entity) {
        List<CPKKPS02Entity> list = baseMapper.select(getSqlId("getPartsOnShippingQty"), entity);
        return list.get(0).getPartsOnShippingQty();
    }

    /**
     * Update TNT_KANBAN_PARTS's status.
     * 
     * @param param BaseParam
     * @return the count of updated datas
     */
    public int updateKanbanPartsToForceCompleted(BaseParam param) {
        param.setSwapData("UPDATE_DATE", getDBDateTimeByDefaultTimezone());
        return baseMapper.update("updateKanbanPartsToForceCompleted", param);
    }

    /**
     * Delete from TNT_KANBAN_SHIPPING_PARTS.
     * 
     * @param param BaseParam
     * @return the count of deleted datas
     */
    public int deleteKanbanShippingParts(BaseParam param) {
        return baseMapper.delete("deleteKanbanShippingParts", param);
    }

    /**
     * Delete from TNT_KANBAN_SHIPPING.
     * 
     * @param param BaseParam
     * @return the count of deleted datas
     */
    public int deleteKanbanShipping(BaseParam param) {
        return baseMapper.delete("deleteKanbanShipping", param);
    }

    /**
     * Update Kanban shipping to completed.
     * 
     * @param param BaseParam
     * @return the count of updated datas
     */
    public int updateKanbanShippingToCompleted(BaseParam param) {
        param.setSwapData("UPDATE_DATE", getDBDateTimeByDefaultTimezone());
        return baseMapper.update("updateKanbanShippingToCompleted", param);
    }

    /**
     * Update TNF_ORDER_STATUS's FORCE_COMPLETED_QTY.
     * 
     * @param entity CPKKPS02Entity
     * @param param BaseParam
     * @return the count of updated datas
     */
    public int updateKanbanShippingForceCompletedQty(CPKKPS02Entity entity, BaseParam param) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return baseMapper.update("updateKanbanShippingForceCompletedQty", entity);
    }
}
