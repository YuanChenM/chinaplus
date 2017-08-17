/**
 * CPVIVF11Service.java
 * 
 * @screen CPVIVF11
 * @author gu_chengchen
 */
package com.chinaplus.batch.migration.service;

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

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.CPVIVF11AdjustEntity;
import com.chinaplus.batch.migration.bean.CPVIVF11SupportEntity;
import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.InvoiceShippingStatus;
import com.chinaplus.common.consts.CodeConst.KanbanPartsStatus;
import com.chinaplus.common.consts.CodeConst.KbsCompletedFlag;
import com.chinaplus.common.consts.CodeConst.KbsNirdFlag;
import com.chinaplus.common.consts.CodeConst.PlanType;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TntInvoiceShipping;
import com.chinaplus.common.entity.TntKanbanPlan;
import com.chinaplus.common.entity.TntKanbanPlanPart;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.entity.TntKanbanShippingPart;
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DecimalUtil;

/**
 * Invoice Upload Service.
 */
@Service
public class CPVIVF11Service extends BaseService {

    /** System adjust reason */
    private static final String SYSTEM_ADJUST_REASON = "系统自动调整";

    /** SQL ID: find need adjust KANBAN plan */
    private static final String SQLID_FIND_NEED_ADJUEST_KANBAN_PLAN = "findNeedAdjuestKanbanPlan";

    /** SQL ID: find all KANBAN parts */
    private static final String SQLID_FIND_ALL_KANBAN_PARTS = "findAllKanbanParts";

    /** SQL ID: find max original version */
    private static final String SQLID_FIND_MAX_ORIGINAL_VERSION = "findMaxOriginalVersion";

    /** SQL ID: find uploaded invoices */
    private static final String SQLID_FIND_UPLOADED_INVOICES = "findUploadedInvoices";

    /** separator */
    private static final String SEPARATOR = "#;!";

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
     * Automatic adjustment plan and order status.
     * 
     * @param officeId officeId
     * @param systemTime system time
     */
    public void doPlanAdjust(Integer officeId, Timestamp systemTime) {

        // Create KANBAN invoice data
        // Find completed invoices in this upload
        List<CPVIVF11SupportEntity> invoiceList = getCompletedInvoices(officeId);
        
        Map<String, CPVIVF11AdjustEntity> kanbanInvoiceMap = new LinkedHashMap<String, CPVIVF11AdjustEntity>();
        for (CPVIVF11SupportEntity invoice : invoiceList) {
            String kanbanPlanNo = invoice.getKanbanPlanNo();
            String invoiceNo = invoice.getInvoiceNo();
            String kanbanInvoiceKey = kanbanPlanNo + SEPARATOR + invoiceNo;
            CPVIVF11AdjustEntity kanbanInvoice = kanbanInvoiceMap.get(kanbanInvoiceKey);
            Map<Integer, BigDecimal> partQtyMap = null;
            if (kanbanInvoice == null) {
                kanbanInvoice = new CPVIVF11AdjustEntity();
                kanbanInvoice.setKanbanPlanNo(kanbanPlanNo);
                kanbanInvoice.setInvoiceNo(invoiceNo);
                kanbanInvoice.setTransportMode(invoice.getTransMode());
                kanbanInvoice.setEtd(invoice.getEtd());
                kanbanInvoice.setEta(invoice.getEta());
                kanbanInvoice.setInbPlanDate(invoice.getInbPlanDate());
                partQtyMap = new HashMap<Integer, BigDecimal>();
                kanbanInvoice.setPartQtyMap(partQtyMap);
                kanbanInvoiceMap.put(kanbanInvoiceKey, kanbanInvoice);
            } else {
                partQtyMap = kanbanInvoice.getPartQtyMap();
            }
            partQtyMap.put(invoice.getPartsId(), invoice.getInvoiceQty());
        }

        // Loop each KANBAN invoice and then adjustment
        for (Map.Entry<String, CPVIVF11AdjustEntity> entry : kanbanInvoiceMap.entrySet()) {
            CPVIVF11AdjustEntity kanbanInvoice = entry.getValue();
            String kanbanPlanNo = kanbanInvoice.getKanbanPlanNo();
            Integer kanbanId = null;
            String orderMonth = null;
            Integer invoiceTransMode = kanbanInvoice.getTransportMode();
            Date invoiceEtd = kanbanInvoice.getEtd();
            Map<Integer, BigDecimal> invoiceQtyMap = kanbanInvoice.getPartQtyMap();
            Map<Integer, Integer> adjustShippingMap = new HashMap<Integer, Integer>();

            // Update TNF_ORDER_STATUS
            for (Map.Entry<Integer, BigDecimal> invoiceQtyEntry : invoiceQtyMap.entrySet()) {
                Integer partsId = invoiceQtyEntry.getKey();
                BigDecimal qty = invoiceQtyEntry.getValue();
                TnfOrderStatus orderCondition = new TnfOrderStatus();
                orderCondition.setKanbanPlanNo(kanbanPlanNo);
                orderCondition.setPartsId(partsId);
                List<TnfOrderStatus> orderDatas = super.baseDao.select(orderCondition);
                if (orderDatas.size() > 0) {
                    TnfOrderStatus orderData = orderDatas.get(0);
                    orderData.setExpOnshippingQty(DecimalUtil.add(orderData.getExpOnshippingQty(), qty));
                    orderData.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    orderData.setUpdatedDate(systemTime);
                    orderData.setVersion(orderData.getVersion() + IntDef.INT_ONE);
                    super.baseDao.update(orderData);
                }
            }

            // Find need adjust KANBAN plan
            Map<Integer, CPVIVF11AdjustEntity> seaPlanPartsMap = new LinkedHashMap<Integer, CPVIVF11AdjustEntity>();
            Map<Integer, CPVIVF11AdjustEntity> airPlanPartsMap = new LinkedHashMap<Integer, CPVIVF11AdjustEntity>();
            List<CPVIVF11AdjustEntity> needAdjustPlans = getNeedAdjustPlans(kanbanPlanNo);
            for (CPVIVF11AdjustEntity needAdjustPlan : needAdjustPlans) {
                kanbanId = needAdjustPlan.getKanbanId();
                orderMonth = needAdjustPlan.getOrderMonth();
                Integer kanbanShippingId = needAdjustPlan.getKanbanShippingId();
                Integer planTransMode = needAdjustPlan.getTransportMode();
                CPVIVF11AdjustEntity planParts = null;
                if (TransportMode.SEA == planTransMode) {
                    planParts = seaPlanPartsMap.get(kanbanShippingId);
                } else {
                    planParts = airPlanPartsMap.get(kanbanShippingId);
                }
                Map<Integer, BigDecimal[]> planQtyMap = null;
                if (planParts == null) {
                    planParts = new CPVIVF11AdjustEntity();
                    BeanUtils.copyProperties(needAdjustPlan, planParts);
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
                putQty(planQtyMap, QTY_TYPE_PLAN, needAdjustPlan.getPartsId(), needAdjustPlan.getPlanQty());
            }

            // Find all KANBAN parts
            if (kanbanId != null) {
                Map<Integer, BigDecimal> spqMap = new HashMap<Integer, BigDecimal>();
                List<CPVIVF11AdjustEntity> allKanbanParts = getAllKanbanParts(kanbanId);
                for (CPVIVF11AdjustEntity kanbanPart : allKanbanParts) {
                    // If the part's STATUS = 9(Force Completed), then no adjust and forward to next part
                    if (KanbanPartsStatus.FORCE_COMPLETED == kanbanPart.getPartStatus()) {
                        continue;
                    }

                    // Invoice Qty
                    Integer partsId = kanbanPart.getPartsId();
                    spqMap.put(partsId, kanbanPart.getSpq());
                    BigDecimal invoiceQty = invoiceQtyMap.get(partsId);
                    if (invoiceQty == null) {
                        invoiceQty = BigDecimal.ZERO;
                    }

                    // Loop each plan that with the same transport mode as the invoice.
                    Map<Integer, CPVIVF11AdjustEntity> sameModePartsMap = null;
                    if (TransportMode.SEA == invoiceTransMode) {
                        sameModePartsMap = seaPlanPartsMap;
                    } else {
                        sameModePartsMap = airPlanPartsMap;
                    }
                    for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : sameModePartsMap.entrySet()) {
                        CPVIVF11AdjustEntity planData = planEntry.getValue();
                        Integer nirdFlag = planData.getNirdFlag();
                        Integer kanbanShippingId = planData.getKanbanShippingId();
                        Date planEtd = planData.getEtd();
                        Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                        BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                        if (KbsNirdFlag.NOT_IN_RUNDOWN == nirdFlag) {
                            // the plan's NIRD_FLAG = 1(Not in rundown)
                            if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                    && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                // 0 < Invoice Qty <= Plan Qty
                                putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                    DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                invoiceQty = BigDecimal.ZERO;
                                planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                            } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                    && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                // Invoice Qty > Plan Qty > 0
                                putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                    DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                            }
                        } else {
                            // the plan's NIRD_FLAG = 0(Normal)
                            if (invoiceEtd.compareTo(planEtd) >= 0) {
                                // invoice's ETD >= plan's ETD, Invoice Delay
                                planData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                                adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                if (DecimalUtil.isLessEquals(invoiceQty, BigDecimal.ZERO)) {
                                    // Invoice Qty <= 0
                                    putQty(planQtyMap, QTY_TYPE_NIRD, partsId, planQty);
                                    invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                } else {
                                    // Invoice Qty > 0
                                    putQty(planQtyMap, QTY_TYPE_NIRD, partsId,
                                        DecimalUtil.subtract(planQty, invoiceQty));
                                    invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                }
                            } else {
                                // invoice's ETD < plan's ETD, Invoice Advance
                                if (DecimalUtil.isEquals(invoiceQty, BigDecimal.ZERO)) {
                                    // Invoice Qty = 0
                                    break;
                                } else {
                                    // Invoice Qty != 0
                                    if (DecimalUtil.isLess(invoiceQty, BigDecimal.ZERO)) {
                                        // Invoice Qty < 0, adjust the plan to add delay qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                }
                            }
                        }
                    }

                    // If Invoice Qty != 0, then adjust the plan with difference transport mode
                    if (!DecimalUtil.isEquals(invoiceQty, BigDecimal.ZERO)) {
                        if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)) {
                            // Invoice Qty > 0, then adjuset the plan to remove the advance qty.
                            Map<Integer, CPVIVF11AdjustEntity> otherModePartsMap = null;
                            if (TransportMode.SEA == invoiceTransMode) {
                                otherModePartsMap = airPlanPartsMap;
                            } else {
                                otherModePartsMap = seaPlanPartsMap;
                            }
                            for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : otherModePartsMap.entrySet()) {
                                CPVIVF11AdjustEntity planData = planEntry.getValue();
                                Integer nirdFlag = planData.getNirdFlag();
                                Integer kanbanShippingId = planData.getKanbanShippingId();
                                Map<Integer, BigDecimal[]> planQtyMap = planData.getPlanQtyMap();
                                BigDecimal planQty = getQty(planQtyMap, QTY_TYPE_PLAN, partsId);
                                if (KbsNirdFlag.NOT_IN_RUNDOWN == nirdFlag) {
                                    // the plan's NIRD_FLAG = 1(Not in rundown)
                                    if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                } else {
                                    // the plan's NIRD_FLAG = 0(Normal)
                                    if (DecimalUtil.isGreater(invoiceQty, BigDecimal.ZERO)
                                            && DecimalUtil.isLessEquals(invoiceQty, planQty)) {
                                        // 0 < Invoice Qty <= Plan Qty, adjust plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                        invoiceQty = BigDecimal.ZERO;
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                        break;
                                    } else if (DecimalUtil.isGreater(planQty, BigDecimal.ZERO)
                                            && DecimalUtil.isGreater(invoiceQty, planQty)) {
                                        // Invoice Qty > Plan Qty > 0, adjust the plan to remove advance qty
                                        putQty(planQtyMap, QTY_TYPE_DIFF, partsId,
                                            DecimalUtil.subtract(BigDecimal.ZERO, planQty));
                                        invoiceQty = DecimalUtil.subtract(invoiceQty, planQty);
                                        planData.setCompletedFlag(KbsCompletedFlag.HISTORY);
                                        adjustShippingMap.put(kanbanShippingId, nirdFlag);
                                    }
                                }
                            }
                        } else {
                            // Invoice Qty < 0, then add a new plan or move the qty to not in rundown.
                            if (TransportMode.SEA == invoiceTransMode) {
                                // invoice transport mode = 1(Sea), add a new plan and move the qty to this plan
                                List<SupplyChainEntity> chainList = new ArrayList<SupplyChainEntity>();
                                SupplyChainEntity chainEntity = new SupplyChainEntity();
                                chainEntity.setTansportMode(TransportMode.SEA);
                                chainEntity.setExpPartsId(kanbanPart.getExpPartsId());
                                chainEntity.setChainStartDate(invoiceEtd);
                                chainList.add(chainEntity);
                                supplyChainService.prepareSupplyChain(chainList, ChainStep.NEXT_ETD,
                                    BusinessPattern.AISIN, false);
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
                                    shipping.setCreatedBy(BatchConst.BATCH_USER_ID);
                                    shipping.setCreatedDate(systemTime);
                                    shipping.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                                    kanbanPlan.setCreatedBy(BatchConst.BATCH_USER_ID);
                                    kanbanPlan.setCreatedDate(systemTime);
                                    kanbanPlan.setUpdatedBy(BatchConst.BATCH_USER_ID);
                                    kanbanPlan.setUpdatedDate(systemTime);
                                    kanbanPlan.setVersion(1);
                                    super.baseDao.insert(kanbanPlan);

                                    // Insert into TNT_KANBAN_PLAN_PARTS
                                    TntKanbanPlanPart kanbanPlanPart = new TntKanbanPlanPart();
                                    kanbanPlanPart.setKanbanPlanId(kanbanPlan.getKanbanPlanId());
                                    kanbanPlanPart.setPartsId(partsId);
                                    kanbanPlanPart.setSpq(null);
                                    kanbanPlanPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                    kanbanPlanPart.setKanbanQty(null);
                                    kanbanPlanPart.setCreatedBy(BatchConst.BATCH_USER_ID);
                                    kanbanPlanPart.setCreatedDate(systemTime);
                                    kanbanPlanPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
                                    kanbanPlanPart.setUpdatedDate(systemTime);
                                    kanbanPlanPart.setVersion(1);
                                    super.baseDao.insert(kanbanPlanPart);

                                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                                    TntKanbanShippingPart shippingPart = new TntKanbanShippingPart();
                                    shippingPart.setKanbanShippingId(shipping.getKanbanShippingId());
                                    shippingPart.setPartsId(partsId);
                                    shippingPart.setSpq(null);
                                    shippingPart.setQty(DecimalUtil.subtract(BigDecimal.ZERO, invoiceQty));
                                    shippingPart.setKanbanQty(null);
                                    shippingPart.setCreatedBy(BatchConst.BATCH_USER_ID);
                                    shippingPart.setCreatedDate(systemTime);
                                    shippingPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
                                    shippingPart.setUpdatedDate(systemTime);
                                    shippingPart.setVersion(1);
                                    super.baseDao.insert(shippingPart);

                                    // add the new plan to plan map
                                    CPVIVF11AdjustEntity newMapPlan = new CPVIVF11AdjustEntity();
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
                                    seaPlanPartsMap.put(shipping.getKanbanShippingId(), newMapPlan);
                                } else {
                                    // non shipping route found, then move the qty to not in rundown
                                    moveToNotInRundown(sameModePartsMap, partsId, kanbanId, systemTime);
                                }
                            } else {
                                // invoice transport mode = 2(Air), move the qty to not in rundown
                                moveToNotInRundown(sameModePartsMap, partsId, kanbanId, systemTime);
                            }
                        }
                    }
                }

                // Do KANBAN Plan Adjustment
                adjustKanbanPlan(seaPlanPartsMap, systemTime, spqMap);
                adjustKanbanPlan(airPlanPartsMap, systemTime, spqMap);

                // Insert TNT_INVOICE_SHIPPING
                for (Map.Entry<Integer, Integer> adjustEntry : adjustShippingMap.entrySet()) {
                    TntInvoiceShipping invoiceShipping = new TntInvoiceShipping();
                    invoiceShipping.setInvoiceNo(kanbanInvoice.getInvoiceNo());
                    invoiceShipping.setKanbanShippingId(adjustEntry.getKey());
                    invoiceShipping.setStatus(InvoiceShippingStatus.NORMAL);
                    invoiceShipping.setNirdFlag(adjustEntry.getValue());
                    invoiceShipping.setCreatedBy(BatchConst.BATCH_USER_ID);
                    invoiceShipping.setCreatedDate(systemTime);
                    invoiceShipping.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    invoiceShipping.setUpdatedDate(systemTime);
                    invoiceShipping.setVersion(1);
                    super.baseDao.insert(invoiceShipping);
                }
            }
        }
    }

    /**
     * Adjust KANBAN plan.
     * 
     * @param planPartsMap parts map
     * @param systemTime system time
     * @param spqMap SPQ map
     */
    private void adjustKanbanPlan(Map<Integer, CPVIVF11AdjustEntity> planPartsMap,
        Timestamp systemTime, Map<Integer, BigDecimal> spqMap) {

        for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : planPartsMap.entrySet()) {
            CPVIVF11AdjustEntity planData = planEntry.getValue();
            Integer kanbanShippingId = planData.getKanbanShippingId();
            Integer completedFlag = planData.getCompletedFlag();
            Integer nirdFlag = planData.getNirdFlag();
            Integer differBoxId = planData.getDifferBoxId();
            Integer dummyBoxId = planData.getDummyBoxId();

            if (KbsCompletedFlag.COMPLETED == completedFlag) {
                // the plan's COMPLETED_FLAG = 1(Completed), update TNT_KANBAN_SHIPPING
                TntKanbanShipping shippingData = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                shippingData.setCompletedFlag(KbsCompletedFlag.COMPLETED);
                shippingData.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                    shippingData.setUpdatedBy(BatchConst.BATCH_USER_ID);
                    shippingData.setUpdatedDate(systemTime);
                    shippingData.setVersion(shippingData.getVersion() + 1);
                    super.baseDao.update(shippingData);
                } else {
                    // Update old TNT_KANBAN_SHIPPING
                    TntKanbanShipping oldShipping = super.getOneById(TntKanbanShipping.class, kanbanShippingId);
                    oldShipping.setCompletedFlag(KbsCompletedFlag.HISTORY);
                    oldShipping.setNirdFlag(KbsNirdFlag.NORMAL);
                    oldShipping.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                    newShipping.setCreatedBy(BatchConst.BATCH_USER_ID);
                    newShipping.setCreatedDate(systemTime);
                    newShipping.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                        differPlan.setCreatedBy(BatchConst.BATCH_USER_ID);
                        differPlan.setCreatedDate(systemTime);
                        differPlan.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                        shippingPart.setCreatedBy(BatchConst.BATCH_USER_ID);
                        shippingPart.setCreatedDate(systemTime);
                        shippingPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                                kanbanPlanPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                                kanbanPlanPart.setCreatedBy(BatchConst.BATCH_USER_ID);
                                kanbanPlanPart.setCreatedDate(systemTime);
                                kanbanPlanPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
     * Move qty to not in rundown.
     * 
     * @param sameModePartsMap parts map
     * @param partsId parts ID
     * @param kanbanId KANBAN ID
     * @param systemTime system time
     */
    private void moveToNotInRundown(Map<Integer, CPVIVF11AdjustEntity> sameModePartsMap, Integer partsId,
        Integer kanbanId, Timestamp systemTime) {

        for (Map.Entry<Integer, CPVIVF11AdjustEntity> planEntry : sameModePartsMap.entrySet()) {
            CPVIVF11AdjustEntity planData = planEntry.getValue();
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
                    nirdData.setCreatedBy(BatchConst.BATCH_USER_ID);
                    nirdData.setCreatedDate(systemTime);
                    nirdData.setUpdatedBy(BatchConst.BATCH_USER_ID);
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
                nirdPart.setCreatedBy(BatchConst.BATCH_USER_ID);
                nirdPart.setCreatedDate(systemTime);
                nirdPart.setUpdatedBy(BatchConst.BATCH_USER_ID);
                nirdPart.setUpdatedDate(systemTime);
                nirdPart.setVersion(1);
                super.baseDao.insert(nirdPart);
            }
        }
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

    /**
     * Get next original version.
     * 
     * @param kanbanId the KANBAN ID
     * @return next original version
     */
    private Integer getNextOriginalVersion(Integer kanbanId) {

        super.baseDao.flush();
        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanId(kanbanId);
        List<CPVIVF11AdjustEntity> result = super.baseMapper.select(getSqlId(SQLID_FIND_MAX_ORIGINAL_VERSION),
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
     * Get need adjust KANBAN plans.
     * 
     * @param kanbanPlanNo the KANBAN plan No.
     * @return need adjust KANBAN plans
     */
    private List<CPVIVF11AdjustEntity> getNeedAdjustPlans(String kanbanPlanNo) {

        super.baseDao.flush();
        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanPlanNo(kanbanPlanNo);
        return super.baseMapper.select(getSqlId(SQLID_FIND_NEED_ADJUEST_KANBAN_PLAN), condition);
    }

    /**
     * Get all KANBAN parts.
     * 
     * @param kanbanId the KANBAN ID
     * @return all KANBAN parts
     */
    private List<CPVIVF11AdjustEntity> getAllKanbanParts(Integer kanbanId) {

        CPVIVF11AdjustEntity condition = new CPVIVF11AdjustEntity();
        condition.setKanbanId(kanbanId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_ALL_KANBAN_PARTS), condition);
    }

    /**
     * Get completed invoices.
     * 
     * @param officeId officeId
     * @return completed invoices
     */
    private List<CPVIVF11SupportEntity> getCompletedInvoices(Integer officeId) {

        CPVIVF11SupportEntity condition = new CPVIVF11SupportEntity();
        condition.setOfficeId(officeId);
        return super.baseMapper.select(getSqlId(SQLID_FIND_UPLOADED_INVOICES), condition);
    }
}
