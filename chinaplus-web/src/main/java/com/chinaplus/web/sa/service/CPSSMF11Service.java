/**
 * CPSSMF11Service.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.CompletedFlag;
import com.chinaplus.common.consts.CodeConst.NirdFlag;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceHistory;
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.entity.TntOrder;
import com.chinaplus.common.entity.TntSsMaster;
import com.chinaplus.common.entity.TntSsPart;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.control.CPSSMF11Controller;
import com.chinaplus.web.sa.control.CPSSMF11Controller.ColumnType;
import com.chinaplus.web.sa.entity.CPSSMF11PartEntity;
import com.chinaplus.web.sa.entity.CPSSMF11PlanEntity;
import com.chinaplus.web.sa.entity.CPSSMF11UploadEntity;

/**
 * Revised Shipping Status Upload Service.
 */
@Service
public class CPSSMF11Service extends BaseService {

    /** SQL ID: find part info */
    private static final String SQLID_FIND_PART_INFO = "findPartInfo";

    /** SQL ID: find not completed plans */
    private static final String SQLID_FIND_NOT_COMPLETED_PLANS = "findNotCompletedPlans";

    /** SQL ID: find all invoices */
    private static final String SQLID_FIND_ALL_INVOICES = "findAllInvoices";

    /** SQL ID: find max original version */
    private static final String SQLID_FIND_MAX_ORIGINAL_VERSION = "findMaxOriginalVersion";

    /** SQL ID: delete non parts plan */
    private static final String SQLID_DELETE_NON_PARTS_PLAN = "deleteNonPartsPlan";

    /** Uploaded file name */
    private static final String UPLOADED_FILE_NAME = "SS_File1_Upload_{0}.xlsx";

    /**
     * Check order exist.
     * 
     * @param ipo IPO No.
     * @param cpo customer order No.
     * @param custId customer ID
     * @return check result
     */
    public boolean isOrderExist(String ipo, String cpo, Integer custId) {

        if (StringUtil.isEmpty(ipo) || StringUtil.isEmpty(cpo) || custId == null) {
            return false;
        }

        TntOrder orderCondition = new TntOrder();
        orderCondition.setImpPoNo(ipo);
        orderCondition.setCustomerOrderNo(cpo);
        orderCondition.setCustomerId(custId);
        List<TntOrder> orderList = super.baseDao.select(orderCondition);
        return orderList.size() > 0;
    }

    /**
     * Get part information.
     * 
     * @param condition search condition
     * @return part information
     */
    public CPSSMF11PartEntity getPartInfo(CPSSMF11PartEntity condition) {

        List<CPSSMF11PartEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_PART_INFO), condition);
        if (result == null || result.size() != 1) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Get not completed plans.
     * 
     * @param officeId Office ID
     * @param ipo IMP PO No.
     * @param cpo Customer Order No.
     * @param custId customer ID
     * @return not completed plans
     */
    public List<CPSSMF11PlanEntity> getNotCompletedPlans(Integer officeId, String ipo, String cpo, Integer custId) {

        List<CPSSMF11PlanEntity> plans = new ArrayList<CPSSMF11PlanEntity>();
        if (StringUtil.isEmpty(ipo) || StringUtil.isEmpty(cpo) || custId == null) {
            return plans;
        }

        CPSSMF11PlanEntity condition = new CPSSMF11PlanEntity();
        condition.setOfficeId(officeId);
        condition.setImpPoNo(ipo);
        condition.setCustomerOrderNo(cpo);
        condition.setCustId(custId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_NOT_COMPLETED_PLANS), condition);
    }

    /**
     * Get all invoices.
     * 
     * @param officeId Office ID
     * @param ipo IMP PO No.
     * @param cpo Customer Order No.
     * @param custId customer ID
     * @return all invoices
     */
    public List<CPSSMF11PlanEntity> getAllInvoices(Integer officeId, String ipo, String cpo, Integer custId) {

        List<CPSSMF11PlanEntity> invoices = new ArrayList<CPSSMF11PlanEntity>();
        if (StringUtil.isEmpty(ipo) || StringUtil.isEmpty(cpo) || custId == null) {
            return invoices;
        }

        CPSSMF11PlanEntity condition = new CPSSMF11PlanEntity();
        condition.setOfficeId(officeId);
        condition.setImpPoNo(ipo);
        condition.setCustomerOrderNo(cpo);
        condition.setCustId(custId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_INVOICES), condition);
    }

    /**
     * Upload logic.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     */
    public void doUpload(CPSSMF11UploadEntity uploadEntity, BaseParam param) {

        // Insert history data.
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        Integer newSsId = insertHistoryData(uploadEntity, param, systemTime);

        // Plan data column process
        processPlanData(uploadEntity, param, systemTime, newSsId);

        // Plan MOD column process
        processModPlan(uploadEntity, param, systemTime, newSsId);

        // NIRD data column process
        processNirdData(uploadEntity, param, systemTime, newSsId);

        // Plan NEW/NIRD NEW/NIRD MOD column process
        processNewPlan(uploadEntity, param, systemTime, newSsId);

        // Delete non parts plan
        deleteNonPartsPlan(newSsId);

        // Invoice data column process
        updateInvoice(uploadEntity, param, systemTime);
    }

    /**
     * Plan data column process.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     * @param newSsId new master data's ID
     */
    private void processPlanData(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime,
        Integer newSsId) {

        for (CPSSMF11PlanEntity planData : uploadEntity.getExcelPlanList()) {
            if (planData.getColumnType() != ColumnType.PLAN_DATA) {
                continue;
            }

            // Find exist TNT_SS_PARTS data
            List<TntSsPart> existPartList = new ArrayList<TntSsPart>();
            TntSsPart existPartCondition = new TntSsPart();
            existPartCondition.setSsPlanId(planData.getSsPlanId());
            List<TntSsPart> dbPartList = super.baseDao.select(existPartCondition);
            for (TntSsPart dbPart : dbPartList) {
                if (DecimalUtil.isLessEquals(dbPart.getQty(), BigDecimal.ZERO)) {
                    continue;
                }
                String partKey = dbPart.getOrderStatusId() + CPSSMF11Controller.SEPARATOR + dbPart.getPartsId();
                if (uploadEntity.getPartKeyList().contains(partKey)) {
                    continue;
                }
                existPartList.add(dbPart);
            }
            planData.setExistPartList(existPartList);

            // Insert TNT_SS_PLAN data
            TntSsPlan ssPlan = new TntSsPlan();
            ssPlan.setSsId(newSsId);
            ssPlan.setTransportMode(planData.getTransportMode());
            ssPlan.setEtd(planData.getEtd());
            ssPlan.setEta(planData.getEta());
            ssPlan.setCcDate(planData.getCcDate());
            ssPlan.setImpInbPlanDate(planData.getInboundPlanDate());
            ssPlan.setOriginalVersion(planData.getOriginalVersion());
            ssPlan.setRevisionVersion(planData.getRevisionVersion());
            ssPlan.setRevisionReason(planData.getRevisionReason());
            ssPlan.setNirdFlag(NirdFlag.NORMAL);
            if (planData.getModEntity() != null) {
                ssPlan.setCompletedFlag(CompletedFlag.COMPLETED);
            } else {
                ssPlan.setCompletedFlag(CompletedFlag.NORMAL);
            }
            ssPlan.setCreatedBy(param.getLoginUserId());
            ssPlan.setCreatedDate(systemTime);
            ssPlan.setUpdatedBy(param.getLoginUserId());
            ssPlan.setUpdatedDate(systemTime);
            ssPlan.setVersion(1);
            super.baseDao.insert(ssPlan);

            // Insert TNT_SS_PARTS data
            Map<Integer, BigDecimal> partQtyMap = planData.getQtyMap();
            for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                    continue;
                }
                CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
                TntSsPart ssPart = new TntSsPart();
                ssPart.setSsPlanId(ssPlan.getSsPlanId());
                ssPart.setOrderStatusId(partInfo.getOrderStatusId());
                ssPart.setPartsId(partInfo.getPartsId());
                ssPart.setQty(qtyEntry.getValue());
                ssPart.setCreatedBy(param.getLoginUserId());
                ssPart.setCreatedDate(systemTime);
                ssPart.setUpdatedBy(param.getLoginUserId());
                ssPart.setUpdatedDate(systemTime);
                ssPart.setVersion(1);
                super.baseDao.insert(ssPart);
            }
            for (TntSsPart existPart : existPartList) {
                TntSsPart ssPart = new TntSsPart();
                ssPart.setSsPlanId(ssPlan.getSsPlanId());
                ssPart.setOrderStatusId(existPart.getOrderStatusId());
                ssPart.setPartsId(existPart.getPartsId());
                ssPart.setQty(existPart.getQty());
                ssPart.setCreatedBy(param.getLoginUserId());
                ssPart.setCreatedDate(systemTime);
                ssPart.setUpdatedBy(param.getLoginUserId());
                ssPart.setUpdatedDate(systemTime);
                ssPart.setVersion(1);
                super.baseDao.insert(ssPart);
            }
        }
    }

    /**
     * Plan MOD column process.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     * @param newSsId new master data's ID
     */
    private void processModPlan(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime,
        Integer newSsId) {

        for (CPSSMF11PlanEntity planData : uploadEntity.getExcelPlanList()) {
            if (planData.getColumnType() != ColumnType.PLAN_DATA || planData.getModEntity() == null) {
                continue;
            }

            // Get plan basic information
            CPSSMF11PlanEntity modEntity = planData.getModEntity();
            Integer transportMode = null;
            Date etd = null;
            Date eta = null;
            Date ccDate = null;
            Date inboundPlanDate = null;
            if (modEntity.getTransportMode() != null) {
                transportMode = modEntity.getTransportMode();
                etd = modEntity.getEtd();
                eta = modEntity.getEta();
                ccDate = modEntity.getCcDate();
                inboundPlanDate = modEntity.getInboundPlanDate();
            } else {
                transportMode = planData.getTransportMode();
                etd = planData.getEtd();
                eta = planData.getEta();
                ccDate = planData.getCcDate();
                inboundPlanDate = planData.getInboundPlanDate();
            }

            // Find exist plan
            TntSsPlan existPlan = null;
            TntSsPlan existPlanCondition = new TntSsPlan();
            existPlanCondition.setSsId(newSsId);
            existPlanCondition.setTransportMode(transportMode);
            existPlanCondition.setEtd(etd);
            existPlanCondition.setEta(eta);
            existPlanCondition.setCcDate(ccDate);
            existPlanCondition.setImpInbPlanDate(inboundPlanDate);
            existPlanCondition.setCompletedFlag(CompletedFlag.NORMAL);
            existPlanCondition.setNirdFlag(NirdFlag.NORMAL);
            List<TntSsPlan> existPlanList = super.baseDao.select(existPlanCondition);
            if (existPlanList.size() > 0) {
                existPlan = existPlanList.get(0);
            }

            // Save mod plan data
            if (existPlan == null) {
                // Insert TNT_SS_PLAN data
                TntSsPlan ssPlan = new TntSsPlan();
                ssPlan.setSsId(newSsId);
                ssPlan.setTransportMode(transportMode);
                ssPlan.setEtd(etd);
                ssPlan.setEta(eta);
                ssPlan.setCcDate(ccDate);
                ssPlan.setImpInbPlanDate(inboundPlanDate);
                ssPlan.setOriginalVersion(planData.getOriginalVersion());
                ssPlan.setRevisionVersion(getNextRevisionVersion(planData.getRevisionVersion()));
                ssPlan.setRevisionReason(modEntity.getRevisionReason());
                ssPlan.setNirdFlag(NirdFlag.NORMAL);
                ssPlan.setCompletedFlag(CompletedFlag.NORMAL);
                ssPlan.setCreatedBy(param.getLoginUserId());
                ssPlan.setCreatedDate(systemTime);
                ssPlan.setUpdatedBy(param.getLoginUserId());
                ssPlan.setUpdatedDate(systemTime);
                ssPlan.setVersion(1);
                super.baseDao.insert(ssPlan);

                // Insert TNT_SS_PARTS data
                Map<Integer, BigDecimal> partQtyMap = modEntity.getQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        continue;
                    }
                    CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
                    TntSsPart ssPart = new TntSsPart();
                    ssPart.setSsPlanId(ssPlan.getSsPlanId());
                    ssPart.setOrderStatusId(partInfo.getOrderStatusId());
                    ssPart.setPartsId(partInfo.getPartsId());
                    ssPart.setQty(qtyEntry.getValue());
                    ssPart.setCreatedBy(param.getLoginUserId());
                    ssPart.setCreatedDate(systemTime);
                    ssPart.setUpdatedBy(param.getLoginUserId());
                    ssPart.setUpdatedDate(systemTime);
                    ssPart.setVersion(1);
                    super.baseDao.insert(ssPart);
                }
                List<TntSsPart> existPartList = planData.getExistPartList();
                for (TntSsPart existPart : existPartList) {
                    TntSsPart ssPart = new TntSsPart();
                    ssPart.setSsPlanId(ssPlan.getSsPlanId());
                    ssPart.setOrderStatusId(existPart.getOrderStatusId());
                    ssPart.setPartsId(existPart.getPartsId());
                    ssPart.setQty(existPart.getQty());
                    ssPart.setCreatedBy(param.getLoginUserId());
                    ssPart.setCreatedDate(systemTime);
                    ssPart.setUpdatedBy(param.getLoginUserId());
                    ssPart.setUpdatedDate(systemTime);
                    ssPart.setVersion(1);
                    super.baseDao.insert(ssPart);
                }
            } else {
                // Update history TNT_SS_PLAN data
                existPlan.setCompletedFlag(CompletedFlag.COMPLETED);
                existPlan.setUpdatedBy(param.getLoginUserId());
                existPlan.setUpdatedDate(systemTime);
                existPlan.setVersion(existPlan.getVersion() + 1);
                super.baseDao.update(existPlan);

                // Insert new TNT_SS_PLAN data
                TntSsPlan newSsPlan = new TntSsPlan();
                newSsPlan.setSsId(newSsId);
                newSsPlan.setTransportMode(existPlan.getTransportMode());
                newSsPlan.setEtd(existPlan.getEtd());
                newSsPlan.setEta(existPlan.getEta());
                newSsPlan.setCcDate(existPlan.getCcDate());
                newSsPlan.setImpInbPlanDate(existPlan.getImpInbPlanDate());
                newSsPlan.setOriginalVersion(existPlan.getOriginalVersion());
                newSsPlan.setRevisionVersion(getNextRevisionVersion(existPlan.getRevisionVersion()));
                newSsPlan.setRevisionReason(modEntity.getRevisionReason());
                newSsPlan.setNirdFlag(NirdFlag.NORMAL);
                newSsPlan.setCompletedFlag(CompletedFlag.NORMAL);
                newSsPlan.setCreatedBy(param.getLoginUserId());
                newSsPlan.setCreatedDate(systemTime);
                newSsPlan.setUpdatedBy(param.getLoginUserId());
                newSsPlan.setUpdatedDate(systemTime);
                newSsPlan.setVersion(1);
                super.baseDao.insert(newSsPlan);

                // Insert new TNT_SS_PARTS data from history TNT_SS_PLAN data
                TntSsPart partHisCondition = new TntSsPart();
                partHisCondition.setSsPlanId(existPlan.getSsPlanId());
                List<TntSsPart> partHisList = super.baseDao.select(partHisCondition);
                for (TntSsPart partHis : partHisList) {
                    TntSsPart newSsPart = new TntSsPart();
                    newSsPart.setSsPlanId(newSsPlan.getSsPlanId());
                    newSsPart.setOrderStatusId(partHis.getOrderStatusId());
                    newSsPart.setPartsId(partHis.getPartsId());
                    newSsPart.setQty(partHis.getQty());
                    newSsPart.setCreatedBy(param.getLoginUserId());
                    newSsPart.setCreatedDate(systemTime);
                    newSsPart.setUpdatedBy(param.getLoginUserId());
                    newSsPart.setUpdatedDate(systemTime);
                    newSsPart.setVersion(1);
                    super.baseDao.insert(newSsPart);
                }

                // Insert new TNT_SS_PARTS data from upload file
                Map<Integer, BigDecimal> partQtyMap = modEntity.getQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        continue;
                    }
                    CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
                    TntSsPart partExistCondition = new TntSsPart();
                    partExistCondition.setSsPlanId(newSsPlan.getSsPlanId());
                    partExistCondition.setOrderStatusId(partInfo.getOrderStatusId());
                    partExistCondition.setPartsId(partInfo.getPartsId());
                    List<TntSsPart> partExistList = super.baseDao.select(partExistCondition);
                    if (partExistList.size() > 0) {
                        TntSsPart ssPart = partExistList.get(0);
                        ssPart.setQty(DecimalUtil.add(ssPart.getQty(), qtyEntry.getValue()));
                        super.baseDao.update(ssPart);
                    } else {
                        TntSsPart ssPart = new TntSsPart();
                        ssPart.setSsPlanId(newSsPlan.getSsPlanId());
                        ssPart.setOrderStatusId(partInfo.getOrderStatusId());
                        ssPart.setPartsId(partInfo.getPartsId());
                        ssPart.setQty(qtyEntry.getValue());
                        ssPart.setCreatedBy(param.getLoginUserId());
                        ssPart.setCreatedDate(systemTime);
                        ssPart.setUpdatedBy(param.getLoginUserId());
                        ssPart.setUpdatedDate(systemTime);
                        ssPart.setVersion(1);
                        super.baseDao.insert(ssPart);
                    }
                }

                // Insert new TNT_SS_PARTS data from exist part
                List<TntSsPart> existPartList = planData.getExistPartList();
                for (TntSsPart existPart : existPartList) {
                    TntSsPart partExistCondition = new TntSsPart();
                    partExistCondition.setSsPlanId(newSsPlan.getSsPlanId());
                    partExistCondition.setOrderStatusId(existPart.getOrderStatusId());
                    partExistCondition.setPartsId(existPart.getPartsId());
                    List<TntSsPart> partExistList = super.baseDao.select(partExistCondition);
                    if (partExistList.size() > 0) {
                        TntSsPart ssPart = partExistList.get(0);
                        ssPart.setQty(DecimalUtil.add(ssPart.getQty(), existPart.getQty()));
                        super.baseDao.update(ssPart);
                    } else {
                        TntSsPart ssPart = new TntSsPart();
                        ssPart.setSsPlanId(newSsPlan.getSsPlanId());
                        ssPart.setOrderStatusId(existPart.getOrderStatusId());
                        ssPart.setPartsId(existPart.getPartsId());
                        ssPart.setQty(existPart.getQty());
                        ssPart.setCreatedBy(param.getLoginUserId());
                        ssPart.setCreatedDate(systemTime);
                        ssPart.setUpdatedBy(param.getLoginUserId());
                        ssPart.setUpdatedDate(systemTime);
                        ssPart.setVersion(1);
                        super.baseDao.insert(ssPart);
                    }
                }
            }
        }
    }

    /**
     * Plan NEW/NIRD NEW/NIRD MOD column process.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     * @param newSsId new master data's ID
     */
    private void processNewPlan(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime,
        Integer newSsId) {

        for (CPSSMF11PlanEntity newPlanData : uploadEntity.getExcelPlanList()) {
            if (newPlanData.getColumnType() != ColumnType.PLAN_NEW
                    && newPlanData.getColumnType() != ColumnType.NIRD_NEW
                    && newPlanData.getColumnType() != ColumnType.NIRD_MOD) {
                continue;
            }
            if (newPlanData.isRemoved()) {
                continue;
            }

            // Find exist plan
            TntSsPlan existPlan = null;
            TntSsPlan existPlanCondition = new TntSsPlan();
            existPlanCondition.setSsId(newSsId);
            existPlanCondition.setTransportMode(newPlanData.getTransportMode());
            existPlanCondition.setEtd(newPlanData.getEtd());
            existPlanCondition.setEta(newPlanData.getEta());
            existPlanCondition.setCcDate(newPlanData.getCcDate());
            existPlanCondition.setImpInbPlanDate(newPlanData.getInboundPlanDate());
            existPlanCondition.setCompletedFlag(CompletedFlag.NORMAL);
            existPlanCondition.setNirdFlag(NirdFlag.NORMAL);
            List<TntSsPlan> existPlanList = super.baseDao.select(existPlanCondition);
            if (existPlanList.size() > 0) {
                existPlan = existPlanList.get(0);
            }

            // Save new plan data
            if (existPlan == null) {
                // Insert TNT_SS_PLAN data
                TntSsPlan ssPlan = new TntSsPlan();
                ssPlan.setSsId(newSsId);
                ssPlan.setTransportMode(newPlanData.getTransportMode());
                ssPlan.setEtd(newPlanData.getEtd());
                ssPlan.setEta(newPlanData.getEta());
                ssPlan.setCcDate(newPlanData.getCcDate());
                ssPlan.setImpInbPlanDate(newPlanData.getInboundPlanDate());
                ssPlan.setOriginalVersion(getNextOriginalVersion(newSsId));
                ssPlan.setRevisionVersion(0);
                ssPlan.setRevisionReason(newPlanData.getRevisionReason());
                ssPlan.setNirdFlag(NirdFlag.NORMAL);
                ssPlan.setCompletedFlag(CompletedFlag.NORMAL);
                ssPlan.setCreatedBy(param.getLoginUserId());
                ssPlan.setCreatedDate(systemTime);
                ssPlan.setUpdatedBy(param.getLoginUserId());
                ssPlan.setUpdatedDate(systemTime);
                ssPlan.setVersion(1);
                super.baseDao.insert(ssPlan);

                // Insert TNT_SS_PARTS data
                Map<Integer, BigDecimal> partQtyMap = newPlanData.getQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        continue;
                    }
                    CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
                    TntSsPart ssPart = new TntSsPart();
                    ssPart.setSsPlanId(ssPlan.getSsPlanId());
                    ssPart.setOrderStatusId(partInfo.getOrderStatusId());
                    ssPart.setPartsId(partInfo.getPartsId());
                    ssPart.setQty(qtyEntry.getValue());
                    ssPart.setCreatedBy(param.getLoginUserId());
                    ssPart.setCreatedDate(systemTime);
                    ssPart.setUpdatedBy(param.getLoginUserId());
                    ssPart.setUpdatedDate(systemTime);
                    ssPart.setVersion(1);
                    super.baseDao.insert(ssPart);
                }
            } else {
                // Update history TNT_SS_PLAN data
                existPlan.setCompletedFlag(CompletedFlag.COMPLETED);
                existPlan.setUpdatedBy(param.getLoginUserId());
                existPlan.setUpdatedDate(systemTime);
                existPlan.setVersion(existPlan.getVersion() + 1);
                super.baseDao.update(existPlan);

                // Insert new TNT_SS_PLAN data
                TntSsPlan newSsPlan = new TntSsPlan();
                newSsPlan.setSsId(newSsId);
                newSsPlan.setTransportMode(existPlan.getTransportMode());
                newSsPlan.setEtd(existPlan.getEtd());
                newSsPlan.setEta(existPlan.getEta());
                newSsPlan.setCcDate(existPlan.getCcDate());
                newSsPlan.setImpInbPlanDate(existPlan.getImpInbPlanDate());
                newSsPlan.setOriginalVersion(existPlan.getOriginalVersion());
                newSsPlan.setRevisionVersion(getNextRevisionVersion(existPlan.getRevisionVersion()));
                newSsPlan.setRevisionReason(newPlanData.getRevisionReason());
                newSsPlan.setNirdFlag(NirdFlag.NORMAL);
                newSsPlan.setCompletedFlag(CompletedFlag.NORMAL);
                newSsPlan.setCreatedBy(param.getLoginUserId());
                newSsPlan.setCreatedDate(systemTime);
                newSsPlan.setUpdatedBy(param.getLoginUserId());
                newSsPlan.setUpdatedDate(systemTime);
                newSsPlan.setVersion(1);
                super.baseDao.insert(newSsPlan);

                // Insert new TNT_SS_PARTS data from history TNT_SS_PLAN data
                TntSsPart partHisCondition = new TntSsPart();
                partHisCondition.setSsPlanId(existPlan.getSsPlanId());
                List<TntSsPart> partHisList = super.baseDao.select(partHisCondition);
                for (TntSsPart partHis : partHisList) {
                    TntSsPart newSsPart = new TntSsPart();
                    newSsPart.setSsPlanId(newSsPlan.getSsPlanId());
                    newSsPart.setOrderStatusId(partHis.getOrderStatusId());
                    newSsPart.setPartsId(partHis.getPartsId());
                    newSsPart.setQty(partHis.getQty());
                    newSsPart.setCreatedBy(param.getLoginUserId());
                    newSsPart.setCreatedDate(systemTime);
                    newSsPart.setUpdatedBy(param.getLoginUserId());
                    newSsPart.setUpdatedDate(systemTime);
                    newSsPart.setVersion(1);
                    super.baseDao.insert(newSsPart);
                }

                // Insert new TNT_SS_PARTS data from upload file
                Map<Integer, BigDecimal> partQtyMap = newPlanData.getQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        continue;
                    }
                    CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
                    TntSsPart partExistCondition = new TntSsPart();
                    partExistCondition.setSsPlanId(newSsPlan.getSsPlanId());
                    partExistCondition.setOrderStatusId(partInfo.getOrderStatusId());
                    partExistCondition.setPartsId(partInfo.getPartsId());
                    List<TntSsPart> partExistList = super.baseDao.select(partExistCondition);
                    if (partExistList.size() > 0) {
                        TntSsPart ssPart = partExistList.get(0);
                        ssPart.setQty(DecimalUtil.add(ssPart.getQty(), qtyEntry.getValue()));
                        super.baseDao.update(ssPart);
                    } else {
                        TntSsPart ssPart = new TntSsPart();
                        ssPart.setSsPlanId(newSsPlan.getSsPlanId());
                        ssPart.setOrderStatusId(partInfo.getOrderStatusId());
                        ssPart.setPartsId(partInfo.getPartsId());
                        ssPart.setQty(qtyEntry.getValue());
                        ssPart.setCreatedBy(param.getLoginUserId());
                        ssPart.setCreatedDate(systemTime);
                        ssPart.setUpdatedBy(param.getLoginUserId());
                        ssPart.setUpdatedDate(systemTime);
                        ssPart.setVersion(1);
                        super.baseDao.insert(ssPart);
                    }
                }
            }
        }
    }

    /**
     * NIRD data column process.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     * @param newSsId new master data's ID
     */
    private void processNirdData(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime,
        Integer newSsId) {

        for (CPSSMF11PlanEntity nirdData : uploadEntity.getExcelPlanList()) {
            if (nirdData.getColumnType() != ColumnType.NIRD_DATA) {
                continue;
            }

            // Find exist TNT_SS_PARTS data
            List<TntSsPart> existPartList = new ArrayList<TntSsPart>();
            TntSsPart existPartCondition = new TntSsPart();
            existPartCondition.setSsPlanId(nirdData.getSsPlanId());
            List<TntSsPart> dbPartList = super.baseDao.select(existPartCondition);
            for (TntSsPart dbPart : dbPartList) {
                if (DecimalUtil.isLessEquals(dbPart.getQty(), BigDecimal.ZERO)) {
                    continue;
                }
                String partKey = dbPart.getOrderStatusId() + CPSSMF11Controller.SEPARATOR + dbPart.getPartsId();
                if (uploadEntity.getPartKeyList().contains(partKey)) {
                    continue;
                }
                existPartList.add(dbPart);
            }

            // Insert old Not in Rundown plan data
            boolean needCompleted = nirdData.isNirdUpdate() ? true : false;
            insertNirdData(uploadEntity, nirdData, existPartList, param, systemTime, newSsId, needCompleted, false);

            // Check whether need to insert new not in rundown data
            if (needCompleted) {
                boolean needNewNird = false;
                Map<Integer, BigDecimal> leftQtyMap = nirdData.getLeftQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : leftQtyMap.entrySet()) {
                    if (DecimalUtil.isGreater(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        needNewNird = true;
                        break;
                    }
                }
                if (!needNewNird && existPartList.size() > 0) {
                    needNewNird = true;
                }

                // Insert new Not in Rundown data
                if (needNewNird) {
                    insertNirdData(uploadEntity, nirdData, existPartList, param, systemTime, newSsId, false, true);
                }
            }
        }
    }

    /**
     * Insert Not in Rundown data.
     * 
     * @param uploadEntity upload parameter
     * @param nirdData NIRD data
     * @param existPartList exist part list
     * @param param page parameter
     * @param systemTime system time
     * @param newSsId new master data's ID
     * @param needCompleted need completed flag
     * @param isNewNird new NIRD flag
     */
    private void insertNirdData(CPSSMF11UploadEntity uploadEntity, CPSSMF11PlanEntity nirdData,
        List<TntSsPart> existPartList, BaseParam param, Timestamp systemTime, Integer newSsId, boolean needCompleted,
        boolean isNewNird) {

        // Insert TNT_SS_PLAN data
        TntSsPlan ssPlan = new TntSsPlan();
        ssPlan.setSsId(newSsId);
        ssPlan.setTransportMode(nirdData.getTransportMode());
        ssPlan.setEtd(nirdData.getEtd());
        ssPlan.setEta(nirdData.getEta());
        ssPlan.setCcDate(nirdData.getCcDate());
        ssPlan.setImpInbPlanDate(nirdData.getInboundPlanDate());
        ssPlan.setOriginalVersion(nirdData.getOriginalVersion());
        if (isNewNird) {
            ssPlan.setRevisionVersion(getNextRevisionVersion(nirdData.getRevisionVersion()));
        } else {
            ssPlan.setRevisionVersion(nirdData.getRevisionVersion());
        }
        ssPlan.setRevisionReason(nirdData.getRevisionReason());
        if (needCompleted) {
            ssPlan.setNirdFlag(NirdFlag.NORMAL);
            ssPlan.setCompletedFlag(CompletedFlag.COMPLETED);
        } else {
            ssPlan.setNirdFlag(NirdFlag.NOT_IN_RUNDOWN);
            ssPlan.setCompletedFlag(CompletedFlag.NORMAL);
        }
        ssPlan.setCreatedBy(param.getLoginUserId());
        ssPlan.setCreatedDate(systemTime);
        ssPlan.setUpdatedBy(param.getLoginUserId());
        ssPlan.setUpdatedDate(systemTime);
        ssPlan.setVersion(1);
        super.baseDao.insert(ssPlan);

        // Insert TNT_SS_PARTS data
        Map<Integer, BigDecimal> partQtyMap = null;
        if (isNewNird) {
            partQtyMap = nirdData.getLeftQtyMap();
        } else {
            partQtyMap = nirdData.getQtyMap();
        }
        for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
            if (DecimalUtil.isLessEquals(qtyEntry.getValue(), BigDecimal.ZERO)) {
                continue;
            }
            CPSSMF11PartEntity partInfo = uploadEntity.getPartMap().get(qtyEntry.getKey());
            TntSsPart ssPart = new TntSsPart();
            ssPart.setSsPlanId(ssPlan.getSsPlanId());
            ssPart.setOrderStatusId(partInfo.getOrderStatusId());
            ssPart.setPartsId(partInfo.getPartsId());
            ssPart.setQty(qtyEntry.getValue());
            ssPart.setCreatedBy(param.getLoginUserId());
            ssPart.setCreatedDate(systemTime);
            ssPart.setUpdatedBy(param.getLoginUserId());
            ssPart.setUpdatedDate(systemTime);
            ssPart.setVersion(1);
            super.baseDao.insert(ssPart);
        }
        for (TntSsPart existPart : existPartList) {
            TntSsPart ssPart = new TntSsPart();
            ssPart.setSsPlanId(ssPlan.getSsPlanId());
            ssPart.setOrderStatusId(existPart.getOrderStatusId());
            ssPart.setPartsId(existPart.getPartsId());
            ssPart.setQty(existPart.getQty());
            ssPart.setCreatedBy(param.getLoginUserId());
            ssPart.setCreatedDate(systemTime);
            ssPart.setUpdatedBy(param.getLoginUserId());
            ssPart.setUpdatedDate(systemTime);
            ssPart.setVersion(1);
            super.baseDao.insert(ssPart);
        }
    }

    /**
     * Insert history data.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     * @return new master data's ID
     */
    private Integer insertHistoryData(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime) {

        // Insert new TNT_SS_MASTER data
        TntSsMaster ssMaster = new TntSsMaster();
        ssMaster.setOrderId(uploadEntity.getOrderId());
        ssMaster.setOfficeId(param.getCurrentOfficeId());
        ssMaster.setSeaFlag(null);
        ssMaster.setAirFlag(null);
        ssMaster.setRevisionReason(uploadEntity.getReasonSet());
        ssMaster.setReasonCodeSet(uploadEntity.getReasonCodeSet());
        ssMaster.setRevisionVersion(getNextRevisionVersion(uploadEntity.getLastVersion()));
        ssMaster.setUploadedBy(param.getLoginUserId());
        ssMaster.setUploadedDate(super.getDBDateTime(param.getOfficeTimezone()));
        ssMaster.setUploadedFileName(StringUtil.formatMessage(UPLOADED_FILE_NAME, param.getClientTime()));
        ssMaster.setCreatedBy(param.getLoginUserId());
        ssMaster.setCreatedDate(systemTime);
        ssMaster.setUpdatedBy(param.getLoginUserId());
        ssMaster.setUpdatedDate(systemTime);
        ssMaster.setVersion(1);
        super.baseDao.insert(ssMaster);

        // Copy completed plan from old master data
        TntSsPlan planHisCondition = new TntSsPlan();
        planHisCondition.setSsId(uploadEntity.getLastSsId());
        planHisCondition.setCompletedFlag(CompletedFlag.COMPLETED);
        List<TntSsPlan> planHisList = super.baseDao.select(planHisCondition);
        // Insert history TNT_SS_PLAN data
        for (TntSsPlan planHis : planHisList) {
            TntSsPlan newHisPlan = new TntSsPlan();
            newHisPlan.setSsId(ssMaster.getSsId());
            newHisPlan.setTransportMode(planHis.getTransportMode());
            newHisPlan.setEtd(planHis.getEtd());
            newHisPlan.setEta(planHis.getEta());
            newHisPlan.setCcDate(planHis.getCcDate());
            newHisPlan.setImpInbPlanDate(planHis.getImpInbPlanDate());
            newHisPlan.setOriginalVersion(planHis.getOriginalVersion());
            newHisPlan.setRevisionVersion(planHis.getRevisionVersion());
            newHisPlan.setRevisionReason(planHis.getRevisionReason());
            newHisPlan.setNirdFlag(planHis.getNirdFlag());
            newHisPlan.setCompletedFlag(planHis.getCompletedFlag());
            newHisPlan.setCreatedBy(param.getLoginUserId());
            newHisPlan.setCreatedDate(systemTime);
            newHisPlan.setUpdatedBy(param.getLoginUserId());
            newHisPlan.setUpdatedDate(systemTime);
            newHisPlan.setVersion(1);
            super.baseDao.insert(newHisPlan);

            // Insert history TNT_SS_PARTS data
            TntSsPart partHisCondition = new TntSsPart();
            partHisCondition.setSsPlanId(planHis.getSsPlanId());
            List<TntSsPart> partHisList = super.baseDao.select(partHisCondition);
            for (TntSsPart partHis : partHisList) {
                TntSsPart newHisPart = new TntSsPart();
                newHisPart.setSsPlanId(newHisPlan.getSsPlanId());
                newHisPart.setOrderStatusId(partHis.getOrderStatusId());
                newHisPart.setPartsId(partHis.getPartsId());
                newHisPart.setQty(partHis.getQty());
                newHisPart.setCreatedBy(param.getLoginUserId());
                newHisPart.setCreatedDate(systemTime);
                newHisPart.setUpdatedBy(param.getLoginUserId());
                newHisPart.setUpdatedDate(systemTime);
                newHisPart.setVersion(1);
                super.baseDao.insert(newHisPart);
            }
        }

        return ssMaster.getSsId();
    }

    /**
     * Invoice update process.
     * 
     * @param uploadEntity upload parameter
     * @param param page parameter
     * @param systemTime system time
     */
    private void updateInvoice(CPSSMF11UploadEntity uploadEntity, BaseParam param, Timestamp systemTime) {

        for (CPSSMF11PlanEntity invoiceData : uploadEntity.getExcelPlanList()) {
            if (invoiceData.getColumnType() == ColumnType.ACTUAL_DATA && invoiceData.getInboundActualDate() == null
                    && invoiceData.getModEntity() != null) {
                // Insert TNT_INVOICE_HISTORY
                TntInvoiceHistory invoiceHistory = new TntInvoiceHistory();
                invoiceHistory.setInvoiceId(invoiceData.getInvoiceId());
                invoiceHistory.setOriginalVersion(invoiceData.getOriginalVersion());
                invoiceHistory.setRevisionVersion(invoiceData.getRevisionVersion());
                invoiceHistory.setRevisionReason(invoiceData.getRevisionReason());
                invoiceHistory.setVanningDate(invoiceData.getVanningDate());
                invoiceHistory.setEtd(invoiceData.getEtd());
                invoiceHistory.setEta(invoiceData.getEta());
                invoiceHistory.setCcDate(invoiceData.getCcDate());
                invoiceHistory.setImpInbPlanDate(invoiceData.getInboundPlanDate());
                invoiceHistory.setCreatedBy(param.getLoginUserId());
                invoiceHistory.setCreatedDate(systemTime);
                invoiceHistory.setUpdatedBy(param.getLoginUserId());
                invoiceHistory.setUpdatedDate(systemTime);
                invoiceHistory.setVersion(1);
                super.baseDao.insert(invoiceHistory);

                // Update TNT_INVOICE
                TntInvoice invoice = super.getOneById(TntInvoice.class, invoiceData.getInvoiceId());
                if (invoice != null) {
                    invoice.setOriginalVersion(invoiceData.getRevisionVersion());
                    invoice.setRevisionVersion(getNextRevisionVersion(invoiceData.getRevisionVersion()));
                    invoice.setRevisionReason(invoiceData.getModEntity().getRevisionReason());
                    invoice.setEtd(invoiceData.getModEntity().getEtd());
                    invoice.setEta(invoiceData.getModEntity().getEta());
                    invoice.setCcDate(invoiceData.getModEntity().getCcDate());
                    invoice.setImpInbPlanDate(invoiceData.getModEntity().getInboundPlanDate());
                    invoice.setUpdatedBy(param.getLoginUserId());
                    invoice.setUpdatedDate(systemTime);
                    invoice.setVersion(invoice.getVersion() + 1);
                    super.baseDao.update(invoice);
                }

                // Update TNT_INVOICE_SUMMARY
                TntInvoiceSummary invoiceSummary = super.getOneById(TntInvoiceSummary.class,
                    invoiceData.getInvoiceSummaryId());
                if (invoiceSummary != null) {
                    invoiceSummary.setEtd(invoiceData.getModEntity().getEtd());
                    invoiceSummary.setEta(invoiceData.getModEntity().getEta());
                    invoiceSummary.setUpdatedBy(param.getLoginUserId());
                    invoiceSummary.setUpdatedDate(systemTime);
                    invoiceSummary.setVersion(invoiceSummary.getVersion() + 1);
                    super.baseDao.update(invoiceSummary);
                }
            }
        }
    }

    /**
     * Delete non parts plan.
     * 
     * @param ssId master data's ID
     */
    private void deleteNonPartsPlan(Integer ssId) {

        super.baseDao.flush();
        CPSSMF11UploadEntity condition = new CPSSMF11UploadEntity();
        condition.setLastSsId(ssId);
        super.baseMapper.update(getSqlId(SQLID_DELETE_NON_PARTS_PLAN), condition);
    }

    /**
     * Get next original version.
     * 
     * @param ssId master data's ID
     * @return next original version
     */
    private Integer getNextOriginalVersion(Integer ssId) {

        super.baseDao.flush();
        CPSSMF11UploadEntity condition = new CPSSMF11UploadEntity();
        condition.setLastSsId(ssId);
        List<CPSSMF11UploadEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_MAX_ORIGINAL_VERSION),
            condition);
        if (result.size() > 0) {
            return result.get(0).getMaxOriginalVersion() + IntDef.INT_ONE;
        }

        return IntDef.INT_ONE;
    }

    /**
     * Get next revision version.
     * 
     * @param curVersion current revision version
     * @return next revision version
     */
    private Integer getNextRevisionVersion(Integer curVersion) {

        if (curVersion == null) {
            return IntDef.INT_ONE;
        }
        return curVersion + IntDef.INT_ONE;
    }

}
