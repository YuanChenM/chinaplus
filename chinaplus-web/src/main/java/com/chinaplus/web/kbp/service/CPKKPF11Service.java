/**
 * CPKKPF11Service.java
 * 
 * @screen CPKKPF11
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.entity.TntKanbanPart;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.kbp.entity.CPKKPF11Entity;

/**
 * Upload Monthly Kanban Plan Service.
 */
@Service
public class CPKKPF11Service extends BaseService {

    /**
     * Exist Customer Code
     * 
     * @param entity TnmPartsMaster
     * @return Exist Customer Code
     */
    public TnmPartsMaster existCustomerCode(TnmPartsMaster entity) {
        List<TnmPartsMaster> result = super.baseMapper.select(getSqlId("existCustomerCode"), entity);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Get Parts Master Info.
     * 
     * @param entity CPKKPF11Entity
     * @return Parts Master Info
     */
    public List<CPKKPF11Entity> getPartsMasterInfo(CPKKPF11Entity entity) {
        return super.baseMapper.select(getSqlId("getPartsMasterInfo"), entity);
    }

    /**
     * Get Order Month.
     * 
     * @param entity CPKKPF11Entity
     * @return Order Month
     */
    public String getOrderMonth(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> list = super.baseMapper.select(getSqlId("getOrderMonth"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0).getOrderMonth();
        }
        return null;
    }

    /**
     * Check Invoice is exists or not.
     * 
     * @param entity CPKKPF11Entity
     * @return true:exist/false:not exist
     */
    public boolean existInvoiceInfo(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> list = super.baseMapper.select(getSqlId("existInvoiceInfo"), entity);
        if (list != null && list.size() > 0) {
            int count = list.get(0).getCount();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get Kanban Plan Info.
     * 
     * @param entity CPKKPF11Entity
     * @return Kanban Plan Info
     */
    public CPKKPF11Entity getKanbanPlanInfo(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> kanbanPlanInfoList = super.baseMapper.select(getSqlId("getKanbanPlanInfo"), entity);
        if (kanbanPlanInfoList != null && kanbanPlanInfoList.size() > 0) {
            return kanbanPlanInfoList.get(0);
        }
        return null;
    }

    /**
     * Get QTY Info.
     * 
     * @param entity CPKKPF11Entity
     * @return QTY Info
     */
    public CPKKPF11Entity getQtyInfo(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> qtyInfoList = super.baseMapper.select(getSqlId("getQtyInfo"), entity);
        if (qtyInfoList != null && qtyInfoList.size() > 0) {
            return qtyInfoList.get(0);
        }
        return null;
    }

    /**
     * Get Kanban Parts Info.
     * 
     * @param entity TntKanbanParts
     * @return Get Kanban Parts Info
     */
    public HashMap<Integer, TntKanbanPart> getKanbanPartsInfo(TntKanbanPart entity) {
        List<TntKanbanPart> kanbanPartsInfo = super.baseMapper.select(getSqlId("getKanbanPartsInfo"), entity);
        HashMap<Integer, TntKanbanPart> kanbanPartsInfoMap = new HashMap<Integer, TntKanbanPart>();
        for (TntKanbanPart tntKanbanPart : kanbanPartsInfo) {
            kanbanPartsInfoMap.put(tntKanbanPart.getPartsId(), tntKanbanPart);
        }
        return kanbanPartsInfoMap;
    }

    /**
     * Get Kanban Shipping AND Kanban Shipping Parts Info.
     * 
     * @param param BaseParam
     * @return Kanban Shipping AND Kanban Shipping Parts Info
     */
    public List<CPKKPF11Entity> getKanbanShippingAndShippingPartsInfo(BaseParam param) {
        return super.baseMapper.select(getSqlId("getKanbanShippingAndShippingPartsInfo"), param);
    }

    /**
     * Get Kanban Plan AND Kanban Plan Parts Info.
     * 
     * @param param BaseParam
     * @return Kanban Plan AND Kanban Plan Parts Info
     */
    public List<CPKKPF11Entity> getKanbanPlanAndPlanPartsInfo(BaseParam param) {
        return super.baseMapper.select(getSqlId("getKanbanPlanAndPlanPartsInfo"), param);
    }

    /**
     * Get Order Status Info.
     * 
     * @param entity CPKKPF11Entity
     * @return Order Status Info
     */
    public HashMap<Integer, TnfOrderStatus> getOrderStatusInfo(TnfOrderStatus entity) {
        List<TnfOrderStatus> orderStatusInfo = super.baseMapper.select(getSqlId("getOrderStatusInfo"), entity);
        HashMap<Integer, TnfOrderStatus> orderStatusInfoMap = new HashMap<Integer, TnfOrderStatus>();
        for (TnfOrderStatus tnfOrderStatus : orderStatusInfo) {
            orderStatusInfoMap.put(tnfOrderStatus.getPartsId(), tnfOrderStatus);
        }
        return orderStatusInfoMap;
    }

    /**
     * Insert Order Status.
     * 
     * @param param BaseParam
     * @param entity TnfOrderStatus
     * @return insert count
     */
    public int insertOrderStatus(BaseParam param, TnfOrderStatus entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertOrderStatus"), entity);
    }

    /**
     * Update Order Status.
     * 
     * @param param BaseParam
     * @param entity TnfOrderStatus
     * @return update count
     */
    public int updateOrderStatus(BaseParam param, TnfOrderStatus entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.update(getSqlId("updateOrderStatus"), entity);
    }

    /**
     * Delete Order Status.
     * 
     * @param param BaseParam
     * @param entity CPKKPF11Entity
     * @return delete count
     */
    public int deleteOrderStatus(BaseParam param, CPKKPF11Entity entity) {
        return super.baseMapper.delete(getSqlId("deleteOrderStatus"), param);
    }

    /**
     * Delete TNT_PFC_DETAIL.
     * 
     * @param param BaseParam
     * @param entity CPKKPF11Entity
     * @return delete count
     */
    public int deletePfcDetail(BaseParam param, CPKKPF11Entity entity) {
        return super.baseMapper.delete(getSqlId("deletePfcDetail"), param);
    }

    /**
     * Delete TNT_PFC_SHIPPING.
     * 
     * @param param BaseParam
     * @param entity CPKKPF11Entity
     * @return delete count
     */
    public int deletePfcShipping(BaseParam param, CPKKPF11Entity entity) {
        return super.baseMapper.delete(getSqlId("deletePfcShipping"), param);
    }

    /**
     * Update Kanban.
     * 
     * @param param BaseParam
     * @param entity CPKKPF11Entity
     * @return update count
     */
    public int updateKanban(BaseParam param, CPKKPF11Entity entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.update(getSqlId("updateKanban"), entity);
    }

    /**
     * Insert Kanban.
     *
     * @param param BaseParam
     * @param entity TntKanban
     * @return insert count
     */
    public int insertKanban(BaseParam param, TntKanban entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanban"), entity);
    }

    /**
     * Insert Kanban Parts.
     *
     * @param param BaseParam
     * @param entity TntKanbanPart
     * @return insert count
     */
    public int insertKanbanParts(BaseParam param, TntKanbanPart entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanbanParts"), entity);
    }

    /**
     * Insert Kanban Shipping.
     *
     * @param param BaseParam
     * @param entity TntKanbanShipping
     * @return insert count
     */
    public int insertKanbanShipping(BaseParam param, TntKanbanShipping entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanbanShipping"), entity);
    }

    /**
     * Insert Kanban Shipping Parts.
     *
     * @param param BaseParam
     * @param entity TntKanbanShippingPart
     * @return insert count
     */
    public int insertKanbanShippingParts(BaseParam param, TntKanbanShippingPart entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanbanShippingParts"), entity);
    }

    /**
     * Insert Kanban Plan.
     *
     * @param param BaseParam
     * @param entity TntKanbanPlan
     * @return insert count
     */
    public int insertKanbanPlan(BaseParam param, TntKanbanPlan entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanbanPlan"), entity);
    }

    /**
     * Insert Kanban Plan Parts.
     *
     * @param param BaseParam
     * @param entity TntKanbanPlanPart
     * @return insert count
     */
    public int insertKanbanPlanParts(BaseParam param, TntKanbanPlanPart entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        entity.setUpdatedDate((Timestamp) param.getSwapData().get("DATE_TIME"));
        return super.baseMapper.insert(getSqlId("insertKanbanPlanParts"), entity);
    }

    /**
     * Get the exist shipping plan.
     * 
     * @param entity CPKKPF11Entity
     * @return the exist shipping plan
     */
    public CPKKPF11Entity getExistShippingPlan(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> list = super.baseMapper.select(getSqlId("getExistShippingPlan"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Get the exist kanban plan.
     * 
     * @param entity CPKKPF11Entity
     * @return the exist kanban plan
     */
    public CPKKPF11Entity getExistKanbanPlan(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> list = super.baseMapper.select(getSqlId("getExistKanbanPlan"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Get max original version.
     * 
     * @param entity CPKKPF11Entity
     * @return originalVersion + 1
     */
    public int getMaxOriginalVersion(CPKKPF11Entity entity) {
        List<CPKKPF11Entity> list = super.baseMapper.select(getSqlId("getMaxOriginalVersion"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0).getOriginalVersion() + 1;
        }
        return 1;
    }
}
