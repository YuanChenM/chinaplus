/**
 * DailyAdjustKanbanService.java
 * 
 * @screen common
 * @author gu_chengchen
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.KanbanPartsStatus;
import com.chinaplus.common.consts.CodeConst.KbsCompletedFlag;
import com.chinaplus.common.consts.CodeConst.KbsNirdFlag;
import com.chinaplus.common.consts.CodeConst.PlanType;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.DailyAdjustKanbanEntity;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;

/**
 * Daily Adjust KANBAN Service.
 */
@Service
public class DailyAdjustKanbanService extends BaseService {

    /** System adjust reason */
    private static final String SYSTEM_ADJUST_REASON = "系统自动调整";

    /** SQL ID: find need adjust KANBAN */
    private static final String SQLID_FIND_NEED_ADJUST_KANBAN = "findNeedAdjustKanban";

    /** SQL ID: find non completed plan */
    private static final String SQLID_FIND_NON_COMPLETED_PLAN = "findNonCompletedPlan";

    /** SQL ID: find all parts */
    private static final String SQLID_FIND_ALL_PARTS = "findAllParts";

    /** SQL ID: find max original version */
    private static final String SQLID_FIND_MAX_ORIGINAL_VERSION = "findMaxOriginalVersion";

    /** Qty Type: Plan */
    private static final int QTY_TYPE_PLAN = 0;

    /** Qty Type: Not in rundown */
    private static final int QTY_TYPE_NIRD = 1;

    /** Qty Type: Difference */
    private static final int QTY_TYPE_DIFF = 2;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * Daily Adjust Logic.
     */
    public void doAdjustment() {

        doAdjustment(null);
    }

    /**
     * Daily Adjust Logic.
     * 
     * @param officeId office ID
     */
    public void doAdjustment(Integer officeId) {

        // Find need process office
        Timestamp systemTime = super.getDBDateTimeByDefaultTimezone();
        List<TnmOffice> offices = null;
        if (officeId != null) {
            offices = new ArrayList<TnmOffice>();
            TnmOffice curOffice = super.getOneById(TnmOffice.class, officeId);
            if (curOffice != null) {
                offices.add(curOffice);
            }
        } else {
            TnmOffice officeCondition = new TnmOffice();
            officeCondition.setInactiveFlag(InactiveFlag.ACTIVE);
            offices = super.baseDao.select(officeCondition);
        }
        for (TnmOffice office : offices) {
            // Find need adjust KANBAN
            Timestamp officeTime = super.getDBDateTime(office.getTimeZone());
            Date officeToday = DateTimeUtil.parseDate(DateTimeUtil.formatDate(officeTime),
                DateTimeUtil.FORMAT_DATE_YYYYMMDD);
            List<DailyAdjustKanbanEntity> needAdjustKanbans = getNeedAdjustKanbans(office.getOfficeId(), officeToday);
            for (DailyAdjustKanbanEntity needAdjustKanban : needAdjustKanbans) {
                // Find non completed plans
                Integer kanbanId = needAdjustKanban.getKanbanId();
                Map<Integer, DailyAdjustKanbanEntity> seaPlanPartsMap = new LinkedHashMap<Integer, DailyAdjustKanbanEntity>();
                Map<Integer, DailyAdjustKanbanEntity> airPlanPartsMap = new LinkedHashMap<Integer, DailyAdjustKanbanEntity>();
                List<DailyAdjustKanbanEntity> nonCompletedPlans = getNonCompletedPlans(kanbanId);
                for (DailyAdjustKanbanEntity nonCompletedPlan : nonCompletedPlans) {
                    Integer kanbanShippingId = nonCompletedPlan.getKanbanShippingId();
                    Integer planTransMode = nonCompletedPlan.getTransportMode();
                    DailyAdjustKanbanEntity planParts = null;
                    if (TransportMode.SEA == planTransMode) {
                        planParts = seaPlanPartsMap.get(kanbanShippingId);
                    } else {
                        planParts = airPlanPartsMap.get(kanbanShippingId);
                    }
                    Map<Integer, BigDecimal[]> planQtyMap = null;
                    if (planParts == null) {
                        planParts = new DailyAdjustKanbanEntity();
                        BeanUtils.copyProperties(nonCompletedPlan, planParts);
                        planQtyMap = new HashMap<Integer, BigDecimal[]>();
                        planParts.setPlanQtyMap(planQtyMap);
                        if (TransportMode.SEA == planTransMode) {
                            seaPlanPartsMap.put(kanbanShippingId, planParts);
                        } else {
                            airPlanPartsMap.put(kanbanShippingId, planParts);
                        }
                    } else {
                        planQtyMap = planParts.getPlanQtyMap();
                    }
                    putQty(planQtyMap, QTY_TYPE_PLAN, nonCompletedPlan.getPartsId(), nonCompletedPlan.getPlanQty());
                }

                // Find all KANBAN parts
                Map<Integer, BigDecimal> spqMap = new HashMap<Integer, BigDecimal>();
                List<DailyAdjustKanbanEntity> allKanbanParts = getAllKanbanParts(kanbanId);
                for (DailyAdjustKanbanEntity kanbanPart : allKanbanParts) {
                    // If the part's STATUS = 9(Force Completed), then no adjust and forward to next part
                    if (KanbanPartsStatus.FORCE_COMPLETED == kanbanPart.getPartStatus()) {
                        continue;
                    }

                    // Adjust plan qty
                    Integer partsId = kanbanPart.getPartsId();
                    Integer expPartsId = kanbanPart.getExpPartsId();
                    spqMap.put(partsId, kanbanPart.getSpq());
                    adjustPlanQty(partsId, expPartsId, seaPlanPartsMap, TransportMode.SEA, officeToday,
                        needAdjustKanban, systemTime);
                    adjustPlanQty(partsId, expPartsId, airPlanPartsMap, TransportMode.AIR, officeToday,
                        needAdjustKanban, systemTime);
                }

                // Do KANBAN Plan Adjustment
                adjustKanbanPlan(seaPlanPartsMap, IntDef.INT_ZERO, systemTime, spqMap);
                adjustKanbanPlan(airPlanPartsMap, IntDef.INT_ZERO, systemTime, spqMap);
            }
        }
    }

    /**
     * Adjust plan qty.
     * 
     * @param partsId parts ID
     * @param expPartsId EXP parts ID
     * @param planPartsMap plan parts map
     * @param transportMode transport mode
     * @param officeToday office today
     * @param needAdjustKanban KANBAN data
     * @param systemTime system time
     */
    private void adjustPlanQty(Integer partsId, Integer expPartsId, Map<Integer, DailyAdjustKanbanEntity> planPartsMap,
        Integer transportMode, Date officeToday, DailyAdjustKanbanEntity needAdjustKanban, Timestamp systemTime) {

        // KANBAN information
        Integer loginUserId = IntDef.INT_ZERO;
        Integer kanbanId = needAdjustKanban.getKanbanId();
        String kanbanPlanNo = needAdjustKanban.getKanbanPlanNo();
        String orderMonth = needAdjustKanban.getOrderMonth();

        // Adjust plan qty
        BigDecimal todayQty = BigDecimal.ZERO;
        for (Map.Entry<Integer, DailyAdjustKanbanEntity> planEntry : planPartsMap.entrySet()) {
            DailyAdjustKanbanEntity planData = planEntry.getValue();
            Date planEtd = planData.getEtd();
            Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
            BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
            if (officeToday.compareTo(planEtd) > 0) {
                // today > plan's ETD, Delay
                planData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                putQty(planQtyMap, QTY_TYPE_NIRD, partsId, planQty);
                todayQty = DecimalUtil.subtract(todayQty, planQty);
            } else {
                // today <= plan's ETD, Advance
                if (DecimalUtil.isEquals(todayQty, BigDecimal.ZERO)) {
                    // Today Qty = 0
                    break;
                } else {
                    // Today Qty < 0, adjust the plan to add delay qty
                    putQty(planQtyMap, QTY_TYPE_DIFF, partsId, DecimalUtil.subtract(BigDecimal.ZERO, todayQty));
                    todayQty = BigDecimal.ZERO;
                    planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                    break;
                }
            }
        }

        // Today Qty < 0, then add a new plan or move the qty to not in rundown
        if (!DecimalUtil.isEquals(todayQty, BigDecimal.ZERO)) {
            if (TransportMode.SEA == transportMode) {
                // Transport mode = 1(Sea), add a new plan and move the qty to this plan
                List<SupplyChainEntity> chainList = new ArrayList<SupplyChainEntity>();
                SupplyChainEntity chainEntity = new SupplyChainEntity();
                chainEntity.setTansportMode(TransportMode.SEA);
                chainEntity.setExpPartsId(expPartsId);
                chainEntity.setChainStartDate(officeToday);
                chainList.add(chainEntity);
                supplyChainService.prepareSupplyChain(chainList, ChainStep.NEXT_ETD, BusinessPattern.AISIN, false);
                SupplyChainEntity resultChain = chainList.get(0);
                Date foundEtd = resultChain.getEtd();
                Date foundEta = resultChain.getEta();
                Date foundInbDate = resultChain.getImpPlanInboundDate();
                if (foundEtd != null && foundEta != null && foundInbDate != null) {
                    // Insert into TNT_KANBAN_SHIPPING
                    TntKanbanShipping shipping = new TntKanbanShipping();
                    shipping.setKanbanId(kanbanId);
                    shipping.setShippingUuid(UUID.randomUUID().toString());
                    shipping.setTransportMode(TransportMode.SEA);
                    shipping.setEtd(foundEtd);
                    shipping.setEta(foundEta);
                    shipping.setImpInbPlanDate(foundInbDate);
                    shipping.setOriginalVersion(getNextOriginalVersion(kanbanId));
                    shipping.setRevisionVersion(null);
                    shipping.setRevisionReason(SYSTEM_ADJUST_REASON);
                    shipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                    shipping.setNirdFlag(KbsNirdFlag.NORMAL);
                    shipping.setCreatedBy(loginUserId);
                    shipping.setCreatedDate(systemTime);
                    shipping.setUpdatedBy(loginUserId);
                    shipping.setUpdatedDate(systemTime);
                    shipping.setVersion(1);
                    super.baseDao.insert(shipping);

                    // Insert into TNT_KANBAN_PLAN
                    TntKanbanPlan kanbanPlan = new TntKanbanPlan();
                    kanbanPlan.setKanbanId(kanbanId);
                    kanbanPlan.setShippingUuid(shipping.getShippingUuid());
                    kanbanPlan.setOrderMonth(orderMonth);
                    kanbanPlan.setPlanType(PlanType.SHIPPING_PLAN);
                    kanbanPlan.setIssuedDate(null);
                    kanbanPlan.setIssueRemark(null);
                    kanbanPlan.setDeliveredDate(null);
                    kanbanPlan.setDelivereRemark(null);
                    kanbanPlan.setVanningDate(null);
                    kanbanPlan.setVanningRemark(null);
                    kanbanPlan.setCreatedBy(loginUserId);
                    kanbanPlan.setCreatedDate(systemTime);
                    kanbanPlan.setUpdatedBy(loginUserId);
                    kanbanPlan.setUpdatedDate(systemTime);
                    kanbanPlan.setVersion(1);
                    super.baseDao.insert(kanbanPlan);

                    // Insert into TNT_KANBAN_PLAN_PARTS
                    TntKanbanPlanPart kanbanPlanPart = new TntKanbanPlanPart();
                    kanbanPlanPart.setKanbanPlanId(kanbanPlan.getKanbanPlanId());
                    kanbanPlanPart.setPartsId(partsId);
                    kanbanPlanPart.setSpq(null);
                    kanbanPlanPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, todayQty));
                    kanbanPlanPart.setKanbanQty(null);
                    kanbanPlanPart.setCreatedBy(loginUserId);
                    kanbanPlanPart.setCreatedDate(systemTime);
                    kanbanPlanPart.setUpdatedBy(loginUserId);
                    kanbanPlanPart.setUpdatedDate(systemTime);
                    kanbanPlanPart.setVersion(1);
                    super.baseDao.insert(kanbanPlanPart);

                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                    TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                    shippingPart.setKanbanShippingId(shipping.getKanbanShippingId());
                    shippingPart.setPartsId(partsId);
                    shippingPart.setSpq(null);
                    shippingPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, todayQty));
                    shippingPart.setKanbanQty(null);
                    shippingPart.setCreatedBy(loginUserId);
                    shippingPart.setCreatedDate(systemTime);
                    shippingPart.setUpdatedBy(loginUserId);
                    shippingPart.setUpdatedDate(systemTime);
                    shippingPart.setVersion(1);
                    super.baseDao.insert(shippingPart);

                    // add the new plan to plan map
                    DailyAdjustKanbanEntity newMapPlan = new DailyAdjustKanbanEntity();
                    newMapPlan.setKanbanId(kanbanId);
                    newMapPlan.setKanbanPlanNo(kanbanPlanNo);
                    newMapPlan.setOrderMonth(orderMonth);
                    newMapPlan.setKanbanShippingId(shipping.getKanbanShippingId());
                    newMapPlan.setShippingUUID(shipping.getShippingUuid());
                    newMapPlan.setOriginalVersion(shipping.getOriginalVersion());
                    newMapPlan.setRevisionVersion(shipping.getRevisionVersion());
                    newMapPlan.setTransportMode(shipping.getTransportMode());
                    newMapPlan.setCompletedFlag(shipping.getCompletedFlag());
                    newMapPlan.setNirdFlag(shipping.getNirdFlag());
                    newMapPlan.setEtd(shipping.getEtd());
                    newMapPlan.setEta(shipping.getEta());
                    newMapPlan.setInbPlanDate(shipping.getImpInbPlanDate());
                    newMapPlan.setDifferBoxId(null);
                    newMapPlan.setDummyBoxId(kanbanPlan.getKanbanPlanId());
                    Map<Integer, BigDecimal[]> newQtyMap = new HashMap<Integer, BigDecimal[]>();
                    putQty(newQtyMap, QTY_TYPE_PLAN, shippingPart.getPartsId(), shippingPart.getQty());
                    newMapPlan.setPlanQtyMap(newQtyMap);
                    planPartsMap.put(shipping.getKanbanShippingId(), newMapPlan);
                } else {
                    // non shipping route found, then move the qty to not in rundown
                    moveToNotInRundown(planPartsMap, partsId, kanbanId, loginUserId, systemTime);
                }
            } else {
                // Transport mode = 2(Air), move the qty to not in rundown
                moveToNotInRundown(planPartsMap, partsId, kanbanId, loginUserId, systemTime);
            }
        }
    }

    /**
     * Move qty to not in rundown.
     * 
     * @param sameModePartsMap parts map
     * @param partsId parts ID
     * @param kanbanId KANBAN ID
     * @param loginUserId login user ID
     * @param systemTime system time
     */
    private void moveToNotInRundown(Map<Integer, DailyAdjustKanbanEntity> sameModePartsMap, Integer partsId,
        Integer kanbanId, Integer loginUserId, Timestamp systemTime) {

        for (Map.Entry<Integer, DailyAdjustKanbanEntity> planEntry : sameModePartsMap.entrySet()) {
            DailyAdjustKanbanEntity planData = planEntry.getValue();
            Integer nirdFlag = planData.getNirdFlag();
            Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
            BigDecimal nirdQty = getQty(planQtyMap, QTY_TYPE_NIRD, partsId);
            if (KbsNirdFlag.NORMAL == nirdFlag && DecimalUtil.isGreater(nirdQty, BigDecimal.ZERO)) {
                // Find exist not in rundown KANBAN shipping
                TntKanbanShipping nirdData = null;
                TntKanbanShipping nirdCondition = new TntKanbanShipping();
                nirdCondition.setKanbanId(kanbanId);
                nirdCondition.setNirdFlag(KbsNirdFlag.NOT_IN_RUNDOWN);
                nirdCondition.setTransportMode(planData.getTransportMode());
                nirdCondition.setEtd(planData.getEtd());
                nirdCondition.setEta(planData.getEta());
                nirdCondition.setImpInbPlanDate(planData.getInbPlanDate());
                List<TntKanbanShipping> nirdDatas = super.baseDao.select(nirdCondition);
                if (nirdDatas != null && nirdDatas.size() > 0) {
                    nirdData = nirdDatas.get(0);
                } else {
                    // Insert into TNT_KANBAN_SHIPPING
                    nirdData = new TntKanbanShipping();
                    nirdData.setKanbanId(kanbanId);
                    nirdData.setShippingUuid(planData.getShippingUUID());
                    nirdData.setTransportMode(planData.getTransportMode());
                    nirdData.setEtd(planData.getEtd());
                    nirdData.setEta(planData.getEta());
                    nirdData.setImpInbPlanDate(planData.getInbPlanDate());
                    nirdData.setOriginalVersion(planData.getOriginalVersion());
                    nirdData.setRevisionVersion(getNextRevisionVersion(planData.getRevisionVersion()));
                    nirdData.setRevisionReason(SYSTEM_ADJUST_REASON);
                    nirdData.setCompletedFlag(KbsCompletedFlag.NORMAL);
                    nirdData.setNirdFlag(KbsNirdFlag.NOT_IN_RUNDOWN);
                    nirdData.setCreatedBy(loginUserId);
                    nirdData.setCreatedDate(systemTime);
                    nirdData.setUpdatedBy(loginUserId);
                    nirdData.setUpdatedDate(systemTime);
                    nirdData.setVersion(1);
                    super.baseDao.insert(nirdData);
                }

                // Insert into TNT_KANBAN_SHIPPING_PARTS
                TntKanbanShippingPart nirdPart = new TntKanbanShippingPart();
                nirdPart.setKanbanShippingId(nirdData.getKanbanShippingId());
                nirdPart.setPartsId(partsId);
                nirdPart.setSpq(null);
                nirdPart.setQty(nirdQty);
                nirdPart.setKanbanQty(null);
                nirdPart.setCreatedBy(loginUserId);
                nirdPart.setCreatedDate(systemTime);
                nirdPart.setUpdatedBy(loginUserId);
                nirdPart.setUpdatedDate(systemTime);
                nirdPart.setVersion(1);
                super.baseDao.insert(nirdPart);
            }
        }
    }

    /**
     * Adjust KANBAN plan.
     * 
     * @param planPartsMap parts map
     * @param loginUserId login user ID
     * @param systemTime system time
     * @param spqMap SPQ map
     */
    private void adjustKanbanPlan(Map<Integer, DailyAdjustKanbanEntity> planPartsMap, Integer loginUserId,
        Timestamp systemTime, Map<Integer, BigDecimal> spqMap) {

        for (Map.Entry<Integer, DailyAdjustKanbanEntity> planEntry : planPartsMap.entrySet()) {
            DailyAdjustKanbanEntity planData = planEntry.getValue();
            Integer kanbanShippingId = planData.getKanbanShippingId();
            Integer completedFlag = planData.getCompletedFlag();
            Integer nirdFlag = planData.getNirdFlag();
            Integer differBoxId = planData.getDifferBoxId();
            Integer dummyBoxId = planData.getDummyBoxId();

            if (KbsCompletedFlag.COMPLETED == completedFlag) {
                // the plan's COMPLETED_FLAG = 1(Completed), update TNT_KANBAN_SHIPPING
                TntKanbanShipping shippingData = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                shippingData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                shippingData.setUpdatedBy(loginUserId);
                shippingData.setUpdatedDate(systemTime);
                shippingData.setVersion(shippingData.getVersion() + 1);
                super.baseDao.update(shippingData);
            } else if (KbsCompletedFlag.HISTORY == completedFlag) {
                // the plan's COMPLETED_FLAG = 2(History)
                boolean needCompleted = true;
                Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                for (Map.Entry<Integer, BigDecimal[]> qtyEntry : planQtyMap.entrySet()) {
                    BigDecimal[] qtyArray = qtyEntry.getValue();
                    BigDecimal planQty = qtyArray[QTY_TYPE_PLAN];
                    BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                    BigDecimal totalQty = DecimalUtil.add(planQty, diffQty);
                    if (!DecimalUtil.isEquals(totalQty, BigDecimal.ZERO)) {
                        needCompleted = false;
                        break;
                    }
                }
                if (needCompleted) {
                    // Update old TNT_KANBAN_SHIPPING
                    TntKanbanShipping shippingData = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                    shippingData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                    shippingData.setNirdFlag(KbsNirdFlag.NORMAL);
                    shippingData.setUpdatedBy(loginUserId);
                    shippingData.setUpdatedDate(systemTime);
                    shippingData.setVersion(shippingData.getVersion() + 1);
                    super.baseDao.update(shippingData);
                } else {
                    // Update old TNT_KANBAN_SHIPPING
                    TntKanbanShipping oldShipping = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                    oldShipping.setCompletedFlag(KbsCompletedFlag.HISTORY);
                    oldShipping.setNirdFlag(KbsNirdFlag.NORMAL);
                    oldShipping.setUpdatedBy(loginUserId);
                    oldShipping.setUpdatedDate(systemTime);
                    oldShipping.setVersion(oldShipping.getVersion() + 1);
                    super.baseDao.update(oldShipping);

                    // Insert new TNT_KANBAN_SHIPPING
                    TntKanbanShipping newShipping = new TntKanbanShipping();
                    newShipping.setKanbanId(oldShipping.getKanbanId());
                    newShipping.setShippingUuid(oldShipping.getShippingUuid());
                    newShipping.setTransportMode(oldShipping.getTransportMode());
                    newShipping.setEtd(oldShipping.getEtd());
                    newShipping.setEta(oldShipping.getEta());
                    newShipping.setImpInbPlanDate(oldShipping.getImpInbPlanDate());
                    newShipping.setOriginalVersion(oldShipping.getOriginalVersion());
                    newShipping.setRevisionVersion(getNextRevisionVersion(oldShipping.getRevisionVersion()));
                    newShipping.setRevisionReason(SYSTEM_ADJUST_REASON);
                    newShipping.setCompletedFlag(KbsCompletedFlag.NORMAL);
                    newShipping.setNirdFlag(nirdFlag);
                    newShipping.setCreatedBy(loginUserId);
                    newShipping.setCreatedDate(systemTime);
                    newShipping.setUpdatedBy(loginUserId);
                    newShipping.setUpdatedDate(systemTime);
                    newShipping.setVersion(1);
                    super.baseDao.insert(newShipping);

                    // Insert difference plan in TNT_KANBAN_PLAN
                    if (KbsNirdFlag.NORMAL == nirdFlag && dummyBoxId == null && differBoxId == null) {
                        TntKanbanPlan differPlan = new TntKanbanPlan();
                        differPlan.setKanbanId(oldShipping.getKanbanId());
                        differPlan.setShippingUuid(oldShipping.getShippingUuid());
                        differPlan.setOrderMonth(planData.getOrderMonth());
                        differPlan.setPlanType(PlanType.DIFFERENCE);
                        differPlan.setIssuedDate(null);
                        differPlan.setIssueRemark(null);
                        differPlan.setDeliveredDate(null);
                        differPlan.setDelivereRemark(null);
                        differPlan.setVanningDate(null);
                        differPlan.setVanningRemark(null);
                        differPlan.setCreatedBy(loginUserId);
                        differPlan.setCreatedDate(systemTime);
                        differPlan.setUpdatedBy(loginUserId);
                        differPlan.setUpdatedDate(systemTime);
                        differPlan.setVersion(1);
                        super.baseDao.insert(differPlan);
                        differBoxId = differPlan.getKanbanPlanId();
                    }

                    // Insert part information
                    for (Map.Entry<Integer, BigDecimal[]> qtyEntry : planQtyMap.entrySet()) {
                        Integer partsId = qtyEntry.getKey();
                        BigDecimal[] qtyArray = qtyEntry.getValue();
                        BigDecimal planQty = qtyArray[QTY_TYPE_PLAN];
                        BigDecimal diffQty = qtyArray[QTY_TYPE_DIFF];
                        BigDecimal totalQty = DecimalUtil.add(planQty, diffQty);

                        // Insert into TNT_KANBAN_SHIPPING_PARTS
                        TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                        shippingPart.setKanbanShippingId(newShipping.getKanbanShippingId());
                        shippingPart.setPartsId(partsId);
                        shippingPart.setSpq(null);
                        shippingPart.setQty(totalQty);
                        shippingPart.setKanbanQty(null);
                        shippingPart.setCreatedBy(loginUserId);
                        shippingPart.setCreatedDate(systemTime);
                        shippingPart.setUpdatedBy(loginUserId);
                        shippingPart.setUpdatedDate(systemTime);
                        shippingPart.setVersion(1);
                        super.baseDao.insert(shippingPart);

                        // Insert into TNT_KANBAN_PLAN_PARTS
                        if (KbsNirdFlag.NORMAL == nirdFlag && diffQty != null
                                && !DecimalUtil.isEquals(diffQty, BigDecimal.ZERO)) {
                            BigDecimal kanbanQty = DecimalUtil.divide(diffQty, spqMap.get(partsId), IntDef.INT_TWO);
                            // Find exist part
                            TntKanbanPlanPart kanbanPlanPart = null;
                            Integer kanbanPlanId = dummyBoxId != null ? dummyBoxId : differBoxId;
                            TntKanbanPlanPart partCondition = new TntKanbanPlanPart();
                            partCondition.setPartsId(partsId);
                            partCondition.setKanbanPlanId(kanbanPlanId);
                            List<TntKanbanPlanPart> existParts = super.baseDao.select(partCondition);
                            if (existParts != null && existParts.size() > 0) {
                                kanbanPlanPart = existParts.get(0);
                                kanbanPlanPart.setQty(dummyBoxId != null ? totalQty : null);
                                kanbanPlanPart.setKanbanQty(dummyBoxId != null ? null : DecimalUtil.add(
                                    kanbanPlanPart.getKanbanQty(), kanbanQty));
                                kanbanPlanPart.setUpdatedBy(loginUserId);
                                kanbanPlanPart.setUpdatedDate(systemTime);
                                kanbanPlanPart.setVersion(kanbanPlanPart.getVersion() + IntDef.INT_ONE);
                                super.baseDao.update(kanbanPlanPart);
                            } else {
                                kanbanPlanPart = new TntKanbanPlanPart();
                                kanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                                kanbanPlanPart.setPartsId(partsId);
                                kanbanPlanPart.setSpq(null);
                                kanbanPlanPart.setQty(dummyBoxId != null ? totalQty : null);
                                kanbanPlanPart.setKanbanQty(dummyBoxId != null ? null : kanbanQty);
                                kanbanPlanPart.setCreatedBy(loginUserId);
                                kanbanPlanPart.setCreatedDate(systemTime);
                                kanbanPlanPart.setUpdatedBy(loginUserId);
                                kanbanPlanPart.setUpdatedDate(systemTime);
                                kanbanPlanPart.setVersion(1);
                                super.baseDao.insert(kanbanPlanPart);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get need adjust KANBAN.
     * 
     * @param officeId office ID
     * @param officeToday office today
     * @return need adjust KANBAN
     */
    private List<DailyAdjustKanbanEntity> getNeedAdjustKanbans(Integer officeId, Date officeToday) {

        DailyAdjustKanbanEntity condition = new DailyAdjustKanbanEntity();
        condition.setOfficeId(officeId);
        condition.setOfficeToday(officeToday);
        return super.baseMapper.select(getSqlId(SQLID_FIND_NEED_ADJUST_KANBAN), condition);
    }

    /**
     * Get non completed plans.
     * 
     * @param kanbanId the KANBAN ID
     * @return non completed plans
     */
    private List<DailyAdjustKanbanEntity> getNonCompletedPlans(Integer kanbanId) {

        DailyAdjustKanbanEntity condition = new DailyAdjustKanbanEntity();
        condition.setKanbanId(kanbanId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_NON_COMPLETED_PLAN), condition);
    }

    /**
     * Get all KANBAN parts.
     * 
     * @param kanbanId the KANBAN ID
     * @return all KANBAN parts
     */
    private List<DailyAdjustKanbanEntity> getAllKanbanParts(Integer kanbanId) {

        DailyAdjustKanbanEntity condition = new DailyAdjustKanbanEntity();
        condition.setKanbanId(kanbanId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_PARTS), condition);
    }

    /**
     * Get next original version.
     * 
     * @param kanbanId the KANBAN ID
     * @return next original version
     */
    private Integer getNextOriginalVersion(Integer kanbanId) {

        super.baseDao.flush();
        DailyAdjustKanbanEntity condition = new DailyAdjustKanbanEntity();
        condition.setKanbanId(kanbanId);
        List<DailyAdjustKanbanEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_MAX_ORIGINAL_VERSION),
            condition);
        if (result.size() > 0) {
            return result.get(0).getOriginalVersion() + IntDef.INT_ONE;
        }

        return IntDef.INT_ONE;
    }

    /**
     * Put qty to map.
     * 
     * @param planQtyMap qty map
     * @param qtyType qty type
     * @param mapKey map key
     * @param qty the qty
     */
    private void putQty(Map<Integer, BigDecimal[]> planQtyMap, int qtyType, Integer mapKey, BigDecimal qty) {

        BigDecimal[] qtyArray = planQtyMap.get(mapKey);
        if (qtyArray == null) {
            qtyArray = new BigDecimal[IntDef.INT_THREE];
            planQtyMap.put(mapKey, qtyArray);
        }

        qtyArray[qtyType] = qty;
    }

    /**
     * Get qty from map.
     * 
     * @param planQtyMap qty map
     * @param qtyType qty type
     * @param mapKey map key
     * @return qty
     */
    private BigDecimal getQty(Map<Integer, BigDecimal[]> planQtyMap, int qtyType, Integer mapKey) {

        BigDecimal ret = null;
        BigDecimal[] qtyArray = planQtyMap.get(mapKey);
        if (qtyArray != null) {
            ret = qtyArray[qtyType];
        }
        if (ret == null && QTY_TYPE_PLAN == qtyType) {
            ret = BigDecimal.ZERO;
        }

        return ret;
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
