/**
 * CPKKPS01Service.java
 * 
 * @screen CPKKPS01
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.web.kbp.entity.CPKKPS01Entity;

/**
 * Kanban Issued Plan Screen Service.
 */
@Service
public class CPKKPS01Service extends BaseService {

    /**
     * Get kanban list.
     * 
     * @param param page parameter
     * @return kanban list
     */
    public PageResult<CPKKPS01Entity> getKanbanList(PageParam param) {

        PageResult<CPKKPS01Entity> result = super.getPageList(param);

        return result;
    }

    /**
     * Check the Kanban plan has received the invoice or not.
     * 
     * @param param BaseParam
     * @return data count
     */
    public int getReceivedInvoiceCount(BaseParam param) {

        int count = super.getDatasCount(getSqlId("getReceivedInvoiceCount"), param);
        return count;
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
     * Update TNT_KANBAN's status.<br>
     * Condition : TNT_KANBAN.KANBAN_PLAN_NO IN {selected Kanban plan'KANBAN_PLAN_NO}
     * 
     * @param param BaseParam
     * @return the count of updated datas
     */
    public int updateKanbanToCancel(BaseParam param) {
        param.setSwapData("UPDATE_DATE", getDBDateTimeByDefaultTimezone());
        return baseMapper.update("updateKanbanToCancel", param);
    }

    /**
     * Find total informations for these Kanban Plan.
     * 
     * @param entity TntKanban
     * @return TntKanban
     */
    public TntKanban getTotalInformation(TntKanban entity) {
        List<TntKanban> list = baseMapper.select(getSqlId("getTotalInformation"), entity);
        return list.get(0);
    }

    /**
     * Update TNT_KANBAN's TOTAL_xxx_QTY.<br>
     * Condition : TNT_KANBAN.KANBAN_ID IN {selected Kanban plan'KANBAN_ID}
     * 
     * @param entity TntKanban
     * @param param BaseParam
     * @return the count of updated datas
     */
    public int updateKanbanTotalQtyInfo(TntKanban entity, BaseParam param) {
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedBy(param.getLoginUserId());
        return baseMapper.update("updateKanbanTotalQtyInfo", entity);
    }

    /**
     * Delete order status data.
     * 
     * @param entity TntKanban
     * @return the count of deleted datas
     */
    public int deleteOrderStatusByKanbanPlanNo(TntKanban entity) {
        return baseMapper.delete("deleteOrderStatusByKanbanPlanNo", entity);
    }

    /**
     * Delete pfc shipping data.
     * 
     * @param entity TntKanban
     * @return the count of deleted datas
     */
    public int deleteTfcShipping(TntKanban entity) {
        return baseMapper.delete("deleteTfcShipping", entity);
    }

    /**
     * Delete pfc detail data.
     * 
     * @param entity TntKanban
     * @return the count of deleted datas
     */
    public int deleteTfcDetail(TntKanban entity) {
        return baseMapper.delete("deleteTfcDetail", entity);
    }
}
