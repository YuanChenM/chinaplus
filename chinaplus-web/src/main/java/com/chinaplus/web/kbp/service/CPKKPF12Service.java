/**
 * CPKKPF12Service.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmCalendarDetail;
import com.chinaplus.common.entity.TnmKbIssuedDate;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceHistory;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.entity.TntKanbanPart;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.common.entity.TntPfcDetail;
import com.chinaplus.common.entity.TntPfcShipping;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.kbp.entity.CPKKPF12AllActualInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12AllPartsInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12CompletedEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12HistoryCompletedEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12QtyInfoEntity;

/**
 * Revised Kanban Plan Upload Service.
 */
@Service
public class CPKKPF12Service extends BaseService {

    /**
     * Delete TntKanbanPlan.
     * 
     * @param entity TntKanbanPlan
     * @return the count of deleted datas
     */
    public int deleteKanbanPlan(TntKanbanPlan entity) {
        return baseMapper.delete("deleteKanbanPlan", entity);
    }

    /**
     * Delete TntKanbanPlanPart.
     * 
     * @param entity TntKanbanPlan
     * @return the count of deleted datas
     */
    public int deleteKanbanPlanPart(TntKanbanPlan entity) {
        return baseMapper.delete("deleteKanbanPlanPart", entity);
    }

    /**
     * Update SeaFlag And AirFlag.<br>
     * If exist SEA transport mode in the Kanban file then 1 else 0<br>
     * If exist AIR transport mode in the Kanban file then 1 else 0<br>
     * 
     * @param param BaseParam
     * @param entity TntKanban
     * @return update count
     */
    public int updateSeaAirFlag(BaseParam param, TntKanban entity) {
        return super.baseMapper.update(getSqlId("updateSeaAirFlag"), entity);
    }

    /**
     * Get kb issued date.
     * 
     * @param TnmKbIssuedDate tnmKbIssuedDate
     * @return tnmKbIssuedDate
     */
    public TnmKbIssuedDate getKbIssuedDate(TnmKbIssuedDate tnmKbIssuedDate) {
        List<TnmKbIssuedDate> list = super.baseMapper.select(getSqlId("getKbIssuedDate"), tnmKbIssuedDate);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Get working day.
     * 
     * @param param BaseParam
     * @return working day
     */
    public List<TnmCalendarDetail> getWorkingDay(BaseParam param) {
        return super.baseMapper.select(getSqlId("getWorkingDay"), param);
    }

    /**
     * Exist Nird Plan
     * 
     * @param entity TntKanbanShipping
     * @return Nird Plan List (key:TransportMode,Etd,Eta,ImpInbPlanDate)
     */
    public HashMap<String, TntKanbanShipping> existNirdPlan(TntKanbanShipping entity) {
        List<TntKanbanShipping> result = super.baseMapper.select(getSqlId("existNirdPlan"), entity);
        HashMap<String, TntKanbanShipping> map = new HashMap<String, TntKanbanShipping>();
        for (TntKanbanShipping tntKanbanShipping : result) {
            String key = tntKanbanShipping.getTransportMode() + StringConst.COMMA
                    + DateTimeUtil.formatDate(tntKanbanShipping.getEtd()) + StringConst.COMMA
                    + DateTimeUtil.formatDate(tntKanbanShipping.getEta()) + StringConst.COMMA
                    + DateTimeUtil.formatDate(tntKanbanShipping.getImpInbPlanDate());
            map.put(key, tntKanbanShipping);
        }
        return map;
    }

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
     * Exist Kanban Plan.
     * 
     * @param entity TntKanban
     * @return Kanban Plan Info
     */
    public TntKanban existKanbanPlan(TntKanban entity) {
        List<TntKanban> list = super.baseMapper.select(getSqlId("existKanbanPlan"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Exist Shipping Plan.
     * 
     * @param entity TntKanbanShipping
     * @return Shipping Plan Info
     */
    public TntKanbanShipping existShippingPlan(TntKanbanShipping entity) {
        List<TntKanbanShipping> list = super.baseMapper.select(getSqlId("existShippingPlan"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Select all parts information in this Kanban Plan.
     * 
     * @param entity CPKKPF12AllPartsInfoEntity
     * @param allPartsInfo all parts information map (key:
     *        TTC_PARTS_NO+CUSTOMER_CODE+KANBAN_CUST_CODE+SUPP_PARTS_NO+TTC_SUPP_CODE)
     * @param allPartsInfoPartsId all parts information map (key: PARTS_ID)
     */
    public void getAllPartsInfo(CPKKPF12AllPartsInfoEntity entity,
        HashMap<String, CPKKPF12AllPartsInfoEntity> allPartsInfo,
        HashMap<Integer, CPKKPF12AllPartsInfoEntity> allPartsInfoPartsId) {
        List<CPKKPF12AllPartsInfoEntity> result = super.baseMapper.select(getSqlId("getAllPartsInfo"), entity);
        for (CPKKPF12AllPartsInfoEntity partsInfo : result) {
            allPartsInfo.put(partsInfo.getTtcPartsNo() + partsInfo.getCustomerCode() + partsInfo.getKanbanCustCode()
                    + partsInfo.getSuppPartsNo() + partsInfo.getTtcSuppCode(), partsInfo);
            allPartsInfoPartsId.put(partsInfo.getPartsId(), partsInfo);
        }
    }

    /**
     * Select all actual invoice data.
     * 
     * @param entity CPKKPF12AllActualInvoiceInfoEntity
     * @return all actual invoice
     */
    public List<CPKKPF12AllActualInfoEntity> getAllActualInvoiceInfo(CPKKPF12AllActualInfoEntity entity) {
        return super.baseMapper.select(getSqlId("getAllActualInvoiceInfo"), entity);
    }

    /**
     * Find total information for the old Kanban Plan.
     * 
     * @param entity CPKKPF12QtyInfoEntity
     * @return total information
     */
    public CPKKPF12QtyInfoEntity getQtyInfo(CPKKPF12QtyInfoEntity entity) {
        List<CPKKPF12QtyInfoEntity> list = super.baseMapper.select(getSqlId("getQtyInfo"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Update Kanban.
     * 
     * @param param BaseParam
     * @param entity TntKanban
     * @return update count
     */
    public int updateKanban(BaseParam param, TntKanban entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updateKanban"), entity);
    }

    /**
     * Find parts in the old Kanban Plan.
     * 
     * @param entity TntKanbanPart
     * @return parts in the old Kanban Plan
     */
    public HashMap<Integer, TntKanbanPart> getPartsInfoOfOldKanbanPlan(TntKanbanPart entity) {
        List<TntKanbanPart> tntKanbanPartList = super.baseMapper
            .select(getSqlId("getPartsInfoOfOldKanbanPlan"), entity);
        HashMap<Integer, TntKanbanPart> tntKanbanPartMap = new HashMap<Integer, TntKanbanPart>();
        for (TntKanbanPart tntKanbanPart : tntKanbanPartList) {
            tntKanbanPartMap.put(tntKanbanPart.getPartsId(), tntKanbanPart);
        }
        return tntKanbanPartMap;
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
     * Update Order Status.
     * 
     * @param param BaseParam
     * @param entity TnfOrderStatus
     * @return update count
     */
    public int updateOrderStatus(BaseParam param, TnfOrderStatus entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updateOrderStatus"), entity);
    }

    /**
     * Get TNT_PFC_DETAIL.
     * 
     * @param entity TntPfcDetail
     * @return TNT_PFC_DETAIL
     */
    public TntPfcDetail getPfcDetail(TntPfcDetail entity) {
        List<TntPfcDetail> tntPfcDetailList = super.baseMapper.select(getSqlId("getPfcDetail"), entity);
        if (tntPfcDetailList != null && tntPfcDetailList.size() > 0) {
            return tntPfcDetailList.get(0);
        }
        return null;
    }

    /**
     * Update TNT_PFC_DETAIL.
     * 
     * @param param BaseParam
     * @param entity TntPfcDetail
     * @return update count
     */
    public int updatePfcDetail(BaseParam param, TntPfcDetail entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updatePfcDetail"), entity);
    }

    /**
     * Insert Pfc Detail.
     * 
     * @param param BaseParam
     * @param entity TntPfcDetail
     * @return insert count
     */
    public int insertPfcDetail(BaseParam param, TntPfcDetail entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertPfcDetail"), entity);
    }

    /**
     * Insert Pfc Shipping.
     * 
     * @param param BaseParam
     * @param entity TntPfcShipping
     * @return insert count
     */
    public int insertPfcShipping(BaseParam param, TntPfcShipping entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertPfcShipping"), entity);
    }

    /**
     * Update TNT_PFC_SHIPPING.
     * 
     * @param param BaseParam
     * @param entity TntPfcShipping
     * @return update count
     */
    public int updatePfcShipping(BaseParam param, TntPfcShipping entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updatePfcShipping"), entity);
    }

    /**
     * Find history and completed shipping plan and parts qty information.
     * 
     * @param entity CPKKPF12HistoryCompletedEntity
     * @return TNT_PFC_DETAIL
     */
    public List<CPKKPF12HistoryCompletedEntity> getHistoryCompletedKanbanPlanAndPartsQty(
        CPKKPF12HistoryCompletedEntity entity) {
        return super.baseMapper.select(getSqlId("getHistoryCompletedKanbanPlanAndPartsQty"), entity);
    }

    /**
     * Find history and completed shipping plan and parts qty information.
     * 
     * @param entity CPKKPF12CompletedEntity
     * @return TNT_PFC_DETAIL
     */
    public List<CPKKPF12CompletedEntity> getCompletedKanbanPlanAndPartsQty(CPKKPF12CompletedEntity entity) {
        return super.baseMapper.select(getSqlId("getCompletedKanbanPlanAndPartsQty"), entity);
    }

    /**
     * Find shipping plan from the old Kanban Plan.
     * 
     * @param entity TntKanbanShipping
     * @return TNT_PFC_DETAIL
     */
    public List<TntKanbanShipping> getShippingPlanFromOldPlan(TntKanbanShipping entity) {
        return super.baseMapper.select(getSqlId("getShippingPlanFromOldPlan"), entity);
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanParts"), entity);
    }

    /**
     * Get max original version.
     * 
     * @param entity TntKanbanShipping
     * @return originalVersion + 1
     */
    public int getMaxOriginalVersion(TntKanbanShipping entity) {
        List<TntKanbanShipping> list = super.baseMapper.select(getSqlId("getMaxOriginalVersion"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0).getOriginalVersion() + 1;
        }
        return 1;
    }

    /**
     * Get max revision version.
     * 
     * @param entity TntKanbanShipping
     * @return revisionVersion + 1
     */
    public Integer getMaxRevisionVersion(TntKanbanShipping entity) {
        List<TntKanbanShipping> list = super.baseMapper.select(getSqlId("getMaxRevisionVersion"), entity);
        if (list != null && list.size() > 0 && list.get(0).getOriginalVersion() != null) {
            int revisionVersion = list.get(0).getRevisionVersion();
            return revisionVersion + 1;
        }
        return null;
    }

    /**
     * Get max revision version.
     * 
     * @param entity TntKanbanShipping
     * @return revisionVersion + 1
     */
    public TntKanbanShipping getVersionForNird(TntKanbanShipping entity) {
        List<TntKanbanShipping> list = super.baseMapper.select(getSqlId("getVersionForNird"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
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
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertKanbanPlanParts"), entity);
    }

    /**
     * Update TNT_INVOICE_SUMMARY.
     * 
     * @param param BaseParam
     * @param entity TntInvoiceSummary
     * @return update count
     */
    public int updateInvoiceSummary(BaseParam param, TntInvoiceSummary entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updateInvoiceSummary"), entity);
    }

    /**
     * Find need update TNT_INVOICE data.
     * 
     * @param entity TntInvoice
     * @return need update TNT_INVOICE data
     */
    public TntInvoice getInvoiceUpdateObject(TntInvoice entity) {
        List<TntInvoice> list = super.baseMapper.select(getSqlId("getInvoiceUpdateObject"), entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Insert TNT_INVOICE_HISTORY.
     *
     * @param param BaseParam
     * @param entity TntInvoiceHistory
     * @return insert count
     */
    public int insertInvoiceHistory(BaseParam param, TntInvoiceHistory entity) {
        entity.setCreatedBy(param.getLoginUserId());
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setCreatedDate(getDBDateTimeByDefaultTimezone());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.insert(getSqlId("insertInvoiceHistory"), entity);
    }

    /**
     * Update TNT_INVOICE.
     * 
     * @param param BaseParam
     * @param entity TntInvoice
     * @return update count
     */
    public int updateInvoice(BaseParam param, TntInvoice entity) {
        entity.setUpdatedBy(param.getLoginUserId());
        entity.setUpdatedDate(getDBDateTimeByDefaultTimezone());
        return super.baseMapper.update(getSqlId("updateInvoice"), entity);
    }
}
