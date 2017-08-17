package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.IfSsPartsEntity;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.CompletedFlag;
import com.chinaplus.common.consts.CodeConst.NirdFlag;
import com.chinaplus.common.consts.CodeConst.SimulationEndDayPattern;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.InvoiceAdjEntity;
import com.chinaplus.common.entity.InvoiceAdjTempEntity;
import com.chinaplus.common.entity.TntSsPart;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.InvoiceAdjParam;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;

/**
 * Service for invoice adjust
 * 
 * @author cheng_xingfei
 *
 */
@Service
public class InvoiceAdjService extends BaseService {

    /** logger */
    // private static Logger logger = LoggerFactory.getLogger(InvoiceAdjService.class);

    /** System adjust reason */
    private static final String SYSTEM_ADJUST_REASON = "系统自动调整";

    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * doInvoiceAdjLogic
     * 
     * @param processDateTime Timestamp
     * @param invoiceIdList List<Integer>
     * @throws BusinessException BusinessException
     */
    public void doInvoiceAdjLogic(Timestamp processDateTime, List<Integer> invoiceIdList) throws BusinessException {

        /**
         * Get invoice order infomation
         */
        InvoiceAdjParam param = new InvoiceAdjParam();
        param.setProcessDateTime(processDateTime);
        param.setInvoiceIdList(invoiceIdList);
        List<InvoiceAdjEntity> invoiceAllList = this.baseMapper.select(this.getSqlId("queryInvoiceList"), param);

        invoiceAdjLogic(invoiceAllList);
    }

    /**
     * doInvoiceAdjForDailyBatchLogic
     * 
     * @param officeId int
     * @param processDate Date
     * @throws BusinessException BusinessException
     */
    public void doInvoiceAdjForDailyBatchLogic(int officeId, Date processDate) throws BusinessException {

        /**
         * Get invoice order infomation
         */
        InvoiceAdjParam param = new InvoiceAdjParam();
        param.setCurrSysDate(processDate);
        param.setOfficeId(officeId);
        List<InvoiceAdjEntity> allOrderList = this.baseMapper.select(this.getSqlId("queryAllOrderList"), param);

        if (allOrderList != null && allOrderList.size() > 0) {
            for (InvoiceAdjEntity orderInfo : allOrderList) {
                /**
                 * planMap
                 * Key : ssPlanId
                 * Value : planMap : plan info, old parts list / new parts list.
                 */
                Map<Integer, InvoiceAdjTempEntity> planMap = new HashMap<Integer, InvoiceAdjTempEntity>();

                InvoiceAdjEntity queryPlanParam = new InvoiceAdjEntity();
                queryPlanParam.setImpPoNo(orderInfo.getImpPoNo());
                queryPlanParam.setCustomerOrderNo(orderInfo.getCustomerOrderNo());
                queryPlanParam.setCustomerId(orderInfo.getCustomerId());
                List<InvoiceAdjEntity> allOrderPartsList = this.baseMapper.select(
                    this.getSqlId("getAllOrderPartsForDailyBatch"), queryPlanParam);

                if (allOrderPartsList != null && allOrderPartsList.size() > 0) {

                    /**
                     * adjust all TransportMode = SEA 's plan
                     */
                    for (InvoiceAdjEntity eachPartsInfo : allOrderPartsList) {
                        eachPartsInfo.setEtd(processDate);
                        // eachPartsInfo.setChainStartDate(processDate);
                        eachPartsInfo.setTansportMode(TransportMode.SEA);
                    }
                    // supplyChainService.prepareSupplyChain(allOrderPartsList, ChainStep.INVOICE, BusinessPattern.V_V,
                    // false);

                    invoiceAdjCommonLogic(allOrderPartsList, orderInfo.getOrderId(), orderInfo.getOfficeId(), null,
                        true, planMap);

                    /**
                     * adjust all TransportMode = AIR 's plan
                     */
                    for (InvoiceAdjEntity eachPartsInfo : allOrderPartsList) {
                        eachPartsInfo.setEtd(processDate);
                        // eachPartsInfo.setChainStartDate(processDate);
                        eachPartsInfo.setTansportMode(TransportMode.AIR);
                    }
                    // supplyChainService.prepareSupplyChain(allOrderPartsList, ChainStep.INVOICE, BusinessPattern.V_V,
                    // false);

                    invoiceAdjCommonLogic(allOrderPartsList, orderInfo.getOrderId(), orderInfo.getOfficeId(), null,
                        true, planMap);

                    /**
                     * update ssPlan and ssParts
                     */
                    updateDB(planMap);
                }
            }
        }
    }

    /**
     * invoice adjustment logic
     * 
     * @param invoiceAllList List<InvoiceAdjEntity>
     */
    private void invoiceAdjLogic(List<InvoiceAdjEntity> invoiceAllList) {

        if (invoiceAllList != null && invoiceAllList.size() > 0) {

            /**
             * According to the order and invoice as the unit, re grouped data
             */
            Map<String, List<InvoiceAdjEntity>> invoiceMap = new LinkedHashMap<String, List<InvoiceAdjEntity>>();
            for (int i = 0; i < invoiceAllList.size(); i++) {
                InvoiceAdjEntity orderInvoice = invoiceAllList.get(i);
                String orderKey = new StringBuilder().append(orderInvoice.getOrderId()).append("|#&")
                    .append(orderInvoice.getInvoiceNo()).toString();
                if (i == 0) {
                    List<InvoiceAdjEntity> orderList = new ArrayList<InvoiceAdjEntity>();
                    orderList.add(orderInvoice);
                    invoiceMap.put(orderKey, orderList);
                } else {
                    InvoiceAdjEntity preOrderInvoice = invoiceAllList.get(i - 1);
                    String preOrderKey = new StringBuilder().append(preOrderInvoice.getOrderId()).append("|#&")
                        .append(preOrderInvoice.getInvoiceNo()).toString();

                    if (preOrderKey.equals(orderKey)) {
                        invoiceMap.get(orderKey).add(orderInvoice);
                    } else {
                        List<InvoiceAdjEntity> orderList = new ArrayList<InvoiceAdjEntity>();
                        orderList.add(orderInvoice);
                        invoiceMap.put(orderKey, orderList);
                    }
                }
            }

            if (!invoiceMap.isEmpty()) {

                /**
                 * Loop all the order.
                 */
                for (Map.Entry<String, List<InvoiceAdjEntity>> invoiceEntry : invoiceMap.entrySet()) {
                    List<InvoiceAdjEntity> orderList = invoiceEntry.getValue();

                    if (orderList == null || orderList.size() == 0) {
                        continue;
                    }

                    /**
                     * planMap
                     * Key : ssPlanId
                     * Value : planMap : plan info, old parts list / new parts list.
                     */
                    Map<Integer, InvoiceAdjTempEntity> planMap = new HashMap<Integer, InvoiceAdjTempEntity>();

                    InvoiceAdjEntity queryPlanParam = new InvoiceAdjEntity();
                    InvoiceAdjEntity orderBaseInfo = orderList.get(0);
                    queryPlanParam.setImpPoNo(orderBaseInfo.getImpPoNo());
                    queryPlanParam.setCustomerOrderNo(orderBaseInfo.getCustomerOrderNo());
                    queryPlanParam.setCustomerId(orderBaseInfo.getCustomerId());

                    List<InvoiceAdjEntity> allOrderPartsList = this.baseMapper.select(
                        this.getSqlId("getAllOrderParts"), queryPlanParam);

                    invoiceAdjCommonLogic(allOrderPartsList, orderBaseInfo.getOrderId(), orderBaseInfo.getOfficeId(),
                        orderList, false, planMap);

                    /**
                     * update ssPlan and ssParts
                     */
                    updateDB(planMap);
                }
            }
        }
    }

    /**
     * invoice adjustment common logic
     * 
     * @param allOrderPartsList List<InvoiceAdjEntity>
     * @param orderId Integer
     * @param officeId Integer
     * @param orderList List<InvoiceAdjEntity>
     * @param isDailyBatch boolean
     * @param planMap Map<Integer, InvoiceAdjTempEntity>
     */
    private void invoiceAdjCommonLogic(List<InvoiceAdjEntity> allOrderPartsList, Integer orderId, Integer officeId,
        List<InvoiceAdjEntity> orderList, boolean isDailyBatch, Map<Integer, InvoiceAdjTempEntity> planMap) {

        /**
         * Query all plan information in the current Order
         */
        InvoiceAdjEntity queryPlanParam = new InvoiceAdjEntity();
        queryPlanParam.setOrderId(orderId);
        queryPlanParam.setOfficeId(officeId);
        queryPlanParam.setCompletedFlag(CompletedFlag.NORMAL);

        queryPlanParam.setTansportMode(TransportMode.SEA);
        List<InvoiceAdjEntity> seaPlanPartsList = this.baseMapper.select(this.getSqlId("getPlanPartsList"),
            queryPlanParam);
        queryPlanParam.setTansportMode(TransportMode.AIR);
        List<InvoiceAdjEntity> airPlanPartsList = this.baseMapper.select(this.getSqlId("getPlanPartsList"),
            queryPlanParam);

        /**
         * Loop this order's all parts.
         */
        for (InvoiceAdjEntity orderParts : allOrderPartsList) {
            /**
             * if this parts is force completed, skipped.
             */
            if (orderParts.getForceCompletedQty() != null
                    && DecimalUtil.isGreater(orderParts.getForceCompletedQty(), BigDecimal.ZERO)) {
                continue;
            }

            InvoiceAdjEntity invoiceInfo = null;
            if (isDailyBatch) {
                invoiceInfo = orderParts;
            } else {
                /**
                 * if invoice parts is not exists in orderList, then add a zero amount of the part
                 */
                invoiceInfo = getInvoiceInfo(orderList, orderParts);
            }

            Map<String, List<InvoiceAdjEntity>> sameModePartsMap = new HashMap<String, List<InvoiceAdjEntity>>();
            Map<String, List<InvoiceAdjEntity>> otherModePartsMap = new HashMap<String, List<InvoiceAdjEntity>>();

            /**
             * set same mode plan and other mode plan.
             */
            if (invoiceInfo.getTansportMode().intValue() == TransportMode.SEA) {
                if (seaPlanPartsList != null && seaPlanPartsList.size() > 0) {
                    for (InvoiceAdjEntity invoiceAdjEntity : seaPlanPartsList) {
                        String planPartsKey = new StringBuilder().append(invoiceAdjEntity.getPartsId()).append("|#&")
                            .append(invoiceAdjEntity.getOrderStatusId()).toString();
                        if (sameModePartsMap.containsKey(planPartsKey)) {
                            List<InvoiceAdjEntity> invoiceAdjList = sameModePartsMap.get(planPartsKey);
                            invoiceAdjList.add(invoiceAdjEntity);
                            sameModePartsMap.put(planPartsKey, invoiceAdjList);
                        } else {
                            List<InvoiceAdjEntity> invoiceAdjList = new ArrayList<InvoiceAdjEntity>();
                            invoiceAdjList.add(invoiceAdjEntity);
                            sameModePartsMap.put(planPartsKey, invoiceAdjList);
                        }
                    }
                }

                if (airPlanPartsList != null && airPlanPartsList.size() > 0) {
                    for (InvoiceAdjEntity invoiceAdjEntity : airPlanPartsList) {
                        String planPartsKey = new StringBuilder().append(invoiceAdjEntity.getPartsId()).append("|#&")
                            .append(invoiceAdjEntity.getOrderStatusId()).toString();
                        if (otherModePartsMap.containsKey(planPartsKey)) {
                            List<InvoiceAdjEntity> invoiceAdjList = otherModePartsMap.get(planPartsKey);
                            invoiceAdjList.add(invoiceAdjEntity);
                            otherModePartsMap.put(planPartsKey, invoiceAdjList);
                        } else {
                            List<InvoiceAdjEntity> invoiceAdjList = new ArrayList<InvoiceAdjEntity>();
                            invoiceAdjList.add(invoiceAdjEntity);
                            otherModePartsMap.put(planPartsKey, invoiceAdjList);
                        }
                    }
                }
            } else {
                if (airPlanPartsList != null && airPlanPartsList.size() > 0) {
                    for (InvoiceAdjEntity invoiceAdjEntity : airPlanPartsList) {
                        String planPartsKey = new StringBuilder().append(invoiceAdjEntity.getPartsId()).append("|#&")
                            .append(invoiceAdjEntity.getOrderStatusId()).toString();
                        if (sameModePartsMap.containsKey(planPartsKey)) {
                            List<InvoiceAdjEntity> invoiceAdjList = sameModePartsMap.get(planPartsKey);
                            invoiceAdjList.add(invoiceAdjEntity);
                            sameModePartsMap.put(planPartsKey, invoiceAdjList);
                        } else {
                            List<InvoiceAdjEntity> invoiceAdjList = new ArrayList<InvoiceAdjEntity>();
                            invoiceAdjList.add(invoiceAdjEntity);
                            sameModePartsMap.put(planPartsKey, invoiceAdjList);
                        }
                    }
                }

                if (seaPlanPartsList != null && seaPlanPartsList.size() > 0) {
                    for (InvoiceAdjEntity invoiceAdjEntity : seaPlanPartsList) {
                        String planPartsKey = new StringBuilder().append(invoiceAdjEntity.getPartsId()).append("|#&")
                            .append(invoiceAdjEntity.getOrderStatusId()).toString();
                        if (otherModePartsMap.containsKey(planPartsKey)) {
                            List<InvoiceAdjEntity> invoiceAdjList = otherModePartsMap.get(planPartsKey);
                            invoiceAdjList.add(invoiceAdjEntity);
                            otherModePartsMap.put(planPartsKey, invoiceAdjList);
                        } else {
                            List<InvoiceAdjEntity> invoiceAdjList = new ArrayList<InvoiceAdjEntity>();
                            invoiceAdjList.add(invoiceAdjEntity);
                            otherModePartsMap.put(planPartsKey, invoiceAdjList);
                        }
                    }
                }
            }

            BigDecimal invoiceAdjQty = invoiceInfo.getQty();

            InvoiceAdjTempEntity invoiceAdj = null;
            InvoiceAdjEntity newPlan = null;
            boolean hasNextEtd = false;
            Integer existNextEtdPlanId = null;

            /**
             * Loop this order's same mode plan.
             */
            if (!sameModePartsMap.isEmpty()) {
                String sameModeKey = new StringBuilder().append(invoiceInfo.getPartsId()).append("|#&")
                    .append(invoiceInfo.getOrderStatusId()).toString();
                List<InvoiceAdjEntity> samePlanPartsList = sameModePartsMap.get(sameModeKey);

                /**
                 * if has next etd
                 */
                if (invoiceInfo.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A) {
                    if (invoiceInfo.getTansportMode().intValue() == TransportMode.AIR) {
                        hasNextEtd = false;
                    } else {
                        List<InvoiceAdjEntity> newPlanList = new ArrayList<InvoiceAdjEntity>();
                        InvoiceAdjEntity originalPlan = new InvoiceAdjEntity();
    
                        originalPlan.setChainStartDate(invoiceInfo.getEtd());
                        originalPlan.setImpPlanInboundDate(null);
                        originalPlan.setTansportMode(TransportMode.SEA);
                        originalPlan.setExpPartsId(invoiceInfo.getExpPartsId());
                        newPlanList.add(originalPlan);
                        supplyChainService.prepareSupplyChain(newPlanList, ChainStep.NEXT_ETD, BusinessPattern.V_V, false);
                        newPlan = newPlanList.get(0);
    
                        if (newPlan.getImpPlanInboundDate() != null) {
                            hasNextEtd = true;
                        }

                        if (invoiceInfo.getTansportMode().intValue() == TransportMode.SEA) {
                            for (InvoiceAdjEntity seaPlan : seaPlanPartsList) {
                                if (DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, newPlan.getEtd())
                                    .equals(
                                        DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, seaPlan.getEtd()))
                                        && DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, newPlan.getEta())
                                            .equals(
                                                DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                                    seaPlan.getEta()))
                                        && DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                            newPlan.getImpPlanCustomDate()).equals(
                                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                                seaPlan.getImpPlanCustomDate()))
                                        && DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                            newPlan.getImpPlanInboundDate()).equals(
                                            DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                                                seaPlan.getImpPlanInboundDate()))) {
                                    existNextEtdPlanId = seaPlan.getSsPlanId();
                                }
                            }
                        }
                    }
                }

                if (samePlanPartsList != null && samePlanPartsList.size() > 0) {
                    for (InvoiceAdjEntity planData : samePlanPartsList) {

                        // invoice adjustment qty is zero, and invoice not delay, skip.
                        if (DecimalUtil.isEquals(invoiceAdjQty, BigDecimal.ZERO)) {
                            if (invoiceInfo.getEtd().compareTo(planData.getEtd()) < 0) {
                                continue;
                            } else if (planData.getNirdFlag().intValue() == NirdFlag.NOT_IN_RUNDOWN) {
                                continue;
                            }
                        }

                        TntSsPart nirdSsPart = null;
                        IfSsPartsEntity noNextEtdSsPart = null;

                        Integer ssPlanIdKey = planData.getSsPlanId();
                        
                        if (!planMap.containsKey(ssPlanIdKey)) {
                            invoiceAdj = new InvoiceAdjTempEntity();

                            invoiceAdj.setSsPlanId(ssPlanIdKey);
                            invoiceAdj.setSsId(planData.getSsId());
                            invoiceAdj.setTransportMode(planData.getTansportMode());
                            invoiceAdj.setInvoiceEtd(invoiceInfo.getEtd());
                            invoiceAdj.setEtd(planData.getEtd());
                            invoiceAdj.setEta(planData.getEta());
                            invoiceAdj.setCcDate(planData.getImpPlanCustomDate());
                            invoiceAdj.setImpInbPlanDate(planData.getImpPlanInboundDate());
                            invoiceAdj.setOriginalVersion(planData.getOriginalVersion());
                            invoiceAdj.setRevisionVersion(planData.getRevisionVersion());
                            invoiceAdj.setNirdFlag(planData.getNirdFlag());

                            TntSsPart conditionForAllParts = new TntSsPart();
                            conditionForAllParts.setSsPlanId(ssPlanIdKey);
                            List<TntSsPart> newPartsList = this.baseMapper.select(this.getSqlId("getAllPartsOfPlan"),
                                conditionForAllParts);

                            invoiceAdj.setNewSsPartsList(newPartsList);

                            planMap.put(ssPlanIdKey, invoiceAdj);
                        } else {
                            invoiceAdj = planMap.get(ssPlanIdKey);
                        }

                        nirdSsPart = new TntSsPart();
                        noNextEtdSsPart = new IfSsPartsEntity();

                        if (planData.getNirdFlag().intValue() == NirdFlag.NOT_IN_RUNDOWN) {
                            // this plan's nirdFlag = 1(not in rundown)
                            if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)
                                    && DecimalUtil.isLessEquals(invoiceAdjQty, planData.getQty())) {

                                nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                nirdSsPart.setPartsId(planData.getPartsId());
                                nirdSsPart.setQty(DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));
                                invoiceAdj.getNirdSsPartsList().add(nirdSsPart);

                                resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                    planData.getOrderStatusId(), nirdSsPart.getQty());

                                invoiceAdjQty = BigDecimal.ZERO;

                            } else if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)
                                    && DecimalUtil.isGreater(invoiceAdjQty, planData.getQty())) {

                                nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                nirdSsPart.setPartsId(planData.getPartsId());
                                nirdSsPart.setQty(BigDecimal.ZERO);
                                invoiceAdj.getNirdSsPartsList().add(nirdSsPart);

                                resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                    planData.getOrderStatusId(), nirdSsPart.getQty());

                                invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());

                            }
                        } else {
                            // this plan's nirdFlag = 0(normal)
                            int ret = 0;
                            // if (isDailyBatch) {
                            // ret = 1;
                            // }
                            
                            if (invoiceInfo.getEtd().compareTo(planData.getEtd()) >= ret) {
                                // invoice's ETD >= plan's ETD, Invoice Delay
                                if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)) {

                                    if (DecimalUtil.isLess(invoiceAdjQty, planData.getQty())) {

                                        if (invoiceInfo.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A) {
                                            resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                                planData.getOrderStatusId(), BigDecimal.ZERO);

                                            /**
                                             * save data, if pattern A and no next etd, using this data
                                             */
                                            noNextEtdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                            noNextEtdSsPart.setPartsId(planData.getPartsId());
                                            noNextEtdSsPart.setQty(DecimalUtil.subtract(planData.getQty(),
                                                invoiceAdjQty));
                                            noNextEtdSsPart.setDelayAdjustmentPattern(invoiceInfo
                                                .getDelayAdjustmentPattern());
                                            noNextEtdSsPart.setHasNextEtd(hasNextEtd);
                                            invoiceAdj.getNoNextEtdSsPartsList().add(noNextEtdSsPart);

                                            invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());

                                        } else {
                                            nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                            nirdSsPart.setPartsId(planData.getPartsId());
                                            nirdSsPart.setQty(DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));
                                            invoiceAdj.getNirdSsPartsList().add(nirdSsPart);

                                            invoiceAdjQty = BigDecimal.ZERO;
                                            resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                                planData.getOrderStatusId(), nirdSsPart.getQty());
                                        }
                                    } else {
                                        nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                        nirdSsPart.setPartsId(planData.getPartsId());
                                        nirdSsPart.setQty(BigDecimal.ZERO);
                                        invoiceAdj.getNirdSsPartsList().add(nirdSsPart);

                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(), BigDecimal.ZERO);
                                        invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());
                                    }
                                } else {
                                    if (invoiceInfo.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A) {
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(), BigDecimal.ZERO);

                                        invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());

                                        noNextEtdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                        noNextEtdSsPart.setPartsId(planData.getPartsId());
                                        noNextEtdSsPart.setQty(planData.getQty());
                                        noNextEtdSsPart.setDelayAdjustmentPattern(invoiceInfo
                                            .getDelayAdjustmentPattern());
                                        noNextEtdSsPart.setHasNextEtd(hasNextEtd);
                                        invoiceAdj.getNoNextEtdSsPartsList().add(noNextEtdSsPart);
                                    } else {
                                        nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                        nirdSsPart.setPartsId(planData.getPartsId());
                                        nirdSsPart.setQty(planData.getQty());
                                        invoiceAdj.getNirdSsPartsList().add(nirdSsPart);

                                        invoiceAdjQty = BigDecimal.ZERO;
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(), nirdSsPart.getQty());
                                    }
                                }

                            } else {
                                // invoice's ETD < plan's ETD, Invoice Advance
                                if (DecimalUtil.isEquals(invoiceAdjQty, BigDecimal.ZERO)) {
                                    break;
                                } else if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)) {
                                    if (DecimalUtil.isGreaterEquals(invoiceAdjQty, planData.getQty())) {

                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(), BigDecimal.ZERO);

                                        invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());

                                        if (DecimalUtil.isEquals(invoiceAdjQty, BigDecimal.ZERO)) {
                                            break;
                                        }

                                    } else {
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(),
                                            DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));

                                        invoiceAdjQty = BigDecimal.ZERO;
                                        break;
                                    }

                                } else {
                                    if (invoiceInfo.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A) {

                                        // if Pattern_A, then add the qty to next plan.
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(),
                                            DecimalUtil.add(planData.getQty(), getDecimalAbsValue(invoiceAdjQty)));

                                        invoiceAdjQty = BigDecimal.ZERO;
                                        break;
                                    } else {
                                        invoiceAdjQty = BigDecimal.ZERO;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!DecimalUtil.isEquals(invoiceAdjQty, BigDecimal.ZERO)) {
                if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)) {

                    if (!otherModePartsMap.isEmpty()) {
                        String otherModeKey = new StringBuilder().append(invoiceInfo.getPartsId()).append("|#&")
                            .append(invoiceInfo.getOrderStatusId()).toString();
                        List<InvoiceAdjEntity> otherPlanPartsList = otherModePartsMap.get(otherModeKey);

                        if (otherPlanPartsList != null && otherPlanPartsList.size() > 0) {
                            for (InvoiceAdjEntity planData : otherPlanPartsList) {

                                Integer ssPlanIdKey = planData.getSsPlanId();
                                if (!planMap.containsKey(ssPlanIdKey)) {
                                    invoiceAdj = new InvoiceAdjTempEntity();

                                    invoiceAdj.setSsPlanId(planData.getSsPlanId());
                                    invoiceAdj.setSsId(planData.getSsId());
                                    invoiceAdj.setTransportMode(planData.getTansportMode());
                                    invoiceAdj.setInvoiceEtd(invoiceInfo.getEtd());
                                    invoiceAdj.setEtd(planData.getEtd());
                                    invoiceAdj.setEta(planData.getEta());
                                    invoiceAdj.setCcDate(planData.getImpPlanCustomDate());
                                    invoiceAdj.setImpInbPlanDate(planData.getImpPlanInboundDate());
                                    invoiceAdj.setOriginalVersion(planData.getOriginalVersion());
                                    invoiceAdj.setRevisionVersion(planData.getRevisionVersion());
                                    invoiceAdj.setNirdFlag(planData.getNirdFlag());
                                    invoiceAdj.setCompletedFlag(planData.getCompletedFlag());

                                    TntSsPart conditionForAllParts = new TntSsPart();
                                    conditionForAllParts.setSsPlanId(ssPlanIdKey);
                                    List<TntSsPart> newPartsList = this.baseMapper.select(
                                        this.getSqlId("getAllPartsOfPlan"), conditionForAllParts);

                                    invoiceAdj.setNewSsPartsList(newPartsList);

                                    planMap.put(ssPlanIdKey, invoiceAdj);
                                } else {
                                    invoiceAdj = planMap.get(ssPlanIdKey);
                                }

                                if (DecimalUtil.isGreater(invoiceAdjQty, BigDecimal.ZERO)) {
                                    if (DecimalUtil.isGreater(invoiceAdjQty, planData.getQty())) {

                                        if (planData.getNirdFlag().intValue() == NirdFlag.NOT_IN_RUNDOWN) {
                                            TntSsPart nirdSsPart = new TntSsPart();
                                            nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                            nirdSsPart.setPartsId(planData.getPartsId());
                                            nirdSsPart.setQty(DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));
                                            invoiceAdj.getNirdSsPartsList().add(nirdSsPart);
                                        }
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(), BigDecimal.ZERO);

                                        invoiceAdjQty = DecimalUtil.subtract(invoiceAdjQty, planData.getQty());

                                        if (DecimalUtil.isEquals(invoiceAdjQty, BigDecimal.ZERO)) {
                                            break;
                                        }

                                    } else {

                                        if (planData.getNirdFlag().intValue() == NirdFlag.NOT_IN_RUNDOWN) {
                                            TntSsPart nirdSsPart = new TntSsPart();
                                            nirdSsPart.setOrderStatusId(planData.getOrderStatusId());
                                            nirdSsPart.setPartsId(planData.getPartsId());
                                            nirdSsPart.setQty(DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));
                                            invoiceAdj.getNirdSsPartsList().add(nirdSsPart);
                                        }
                                        resetNewPartsList(invoiceAdj.getNewSsPartsList(), planData.getPartsId(),
                                            planData.getOrderStatusId(),
                                            DecimalUtil.subtract(planData.getQty(), invoiceAdjQty));

                                        invoiceAdjQty = BigDecimal.ZERO;
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                } else {

                    // then add a new plan or move the qty to not in rundown.
                    if (invoiceInfo.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A) {

                        if (hasNextEtd) {
                            Integer ssPlanIdKey = null;
                            if (existNextEtdPlanId != null) {
                                ssPlanIdKey = existNextEtdPlanId;
                            } else {
                                // ssPlanId = -1, its means need to insert a new plan.
                                ssPlanIdKey = -1;
                            }
                            if (!planMap.containsKey(ssPlanIdKey)) {
                                invoiceAdj = new InvoiceAdjTempEntity();
                                planMap.put(ssPlanIdKey, invoiceAdj);
                                
                                // found next etd, add a new plan
                                invoiceAdj.setSsPlanId(ssPlanIdKey);
                                invoiceAdj.setSsId(invoiceInfo.getSsId());
                                invoiceAdj.setTransportMode(newPlan.getTansportMode());
                                invoiceAdj.setInvoiceEtd(invoiceInfo.getEtd());
                                invoiceAdj.setEtd(newPlan.getEtd());
                                invoiceAdj.setEta(newPlan.getEta());
                                invoiceAdj.setCcDate(newPlan.getImpPlanCustomDate());
                                invoiceAdj.setImpInbPlanDate(newPlan.getImpPlanInboundDate());
                                invoiceAdj.setOriginalVersion(1);
                                invoiceAdj.setRevisionVersion(0);
                                invoiceAdj.setNirdFlag(NirdFlag.NORMAL);
                                invoiceAdj.setCompletedFlag(CompletedFlag.NORMAL);
                            } else {
                                invoiceAdj = planMap.get(ssPlanIdKey);
                            }

                            resetNewPartsList(invoiceAdj.getNewSsPartsList(), invoiceInfo.getPartsId(),
                                invoiceInfo.getOrderStatusId(), getDecimalAbsValue(invoiceAdjQty));
                        }
                    }
                }
            }
        }
    }

    /**
     * update DB
     *
     * @param planMap Map<Integer, InvoiceAdjTempEntity>
     */
    private void updateDB(Map<Integer, InvoiceAdjTempEntity> planMap) {

        Timestamp updateDateTime = this.getDBDateTimeByDefaultTimezone();

        if (!planMap.isEmpty()) {

            InvoiceAdjTempEntity newPlanEntity = planMap.get(-1);
            if (newPlanEntity != null) {
                /**
                 * same mode plan not found, add a new plan
                 */
                boolean addNewPlanFlag = checkHasParts(newPlanEntity.getNewSsPartsList(), true);
                if (addNewPlanFlag) {
                    
                    TntSsPlan checkExistsParam = new TntSsPlan();
                    checkExistsParam.setSsId(newPlanEntity.getSsId());
                    checkExistsParam.setEtd(newPlanEntity.getEtd());
                    checkExistsParam.setEta(newPlanEntity.getEta());
                    checkExistsParam.setCcDate(newPlanEntity.getCcDate());
                    checkExistsParam.setImpInbPlanDate(newPlanEntity.getImpInbPlanDate());
                    checkExistsParam.setTransportMode(newPlanEntity.getTransportMode());
                    
                    List<TntSsPlan> samePlanList = this.baseMapper.select(this.getSqlId("checkExistsSamePlan"), checkExistsParam);
                    if (samePlanList != null && samePlanList.size() > 0) {
                        // if has same plan, complete original plan and insert into a new plan.
                        /**
                         * completed original plan
                         */
                        Integer existSsPlanId = samePlanList.get(0).getSsPlanId();
                        TntSsPlan updatePlan = new TntSsPlan();
                        updatePlan.setSsPlanId(existSsPlanId);
                        updatePlan.setCompletedFlag(CompletedFlag.COMPLETED);
                        updatePlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        updatePlan.setUpdatedDate(updateDateTime);

                        this.baseMapper.update(this.getSqlId("updateTntSsPlan"), updatePlan);
                        
                        /**
                         * insert a new plan
                         */
                        Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");

                        TntSsPlan newPlan = new TntSsPlan();
                        newPlan.setSsPlanId(newSsPlanId);
                        newPlan.setSsId(newPlanEntity.getSsId());
                        newPlan.setTransportMode(newPlanEntity.getTransportMode());
                        newPlan.setEtd(newPlanEntity.getEtd());
                        newPlan.setEta(newPlanEntity.getEta());
                        newPlan.setCcDate(newPlanEntity.getCcDate());
                        newPlan.setImpInbPlanDate(newPlanEntity.getImpInbPlanDate());
                        newPlan.setOriginalVersion(samePlanList.get(0).getOriginalVersion());
                        newPlan.setRevisionVersion(samePlanList.get(0).getRevisionVersion().intValue() + 1);
                        newPlan.setRevisionReason(SYSTEM_ADJUST_REASON);
                        newPlan.setNirdFlag(NirdFlag.NORMAL);
                        newPlan.setCompletedFlag(CompletedFlag.NORMAL);
                        newPlan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                        newPlan.setCreatedDate(updateDateTime);
                        newPlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        newPlan.setUpdatedDate(updateDateTime);

                        this.baseMapper.insert(this.getSqlId("insertTntSsPlan"), newPlan);
                        
                        /**
                         *  insert the original plan's all parts into tnt_ss_parts
                         */
                        TntSsPart conditionForAllParts = new TntSsPart();
                        conditionForAllParts.setSsPlanId(existSsPlanId);
                        List<TntSsPart> existPlanAllParts = this.baseMapper.select(this.getSqlId("getAllPartsOfPlan"),
                            conditionForAllParts);
                        
                        if (existPlanAllParts != null && existPlanAllParts.size() > 0) {
                            for (TntSsPart ssPart : existPlanAllParts) {
                                if (ssPart.getQty() != null && !DecimalUtil.isEquals(ssPart.getQty(), BigDecimal.ZERO)) {
                                    ssPart.setSsPlanId(newSsPlanId);
                                    ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setCreatedDate(updateDateTime);
                                    ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setUpdatedDate(updateDateTime);
    
                                    this.baseMapper.insert(this.getSqlId("insertSsParts"), ssPart);
                                }
                            }
                        }
                        
                        /**
                         * insert or merge the new parts into tnt_ss_parts
                         */
                        for (TntSsPart ssPart : newPlanEntity.getNewSsPartsList()) {
                            if (ssPart.getQty() != null && !DecimalUtil.isEquals(ssPart.getQty(), BigDecimal.ZERO)) {
                                insertOrUpdateSsParts(ssPart, newSsPlanId, updateDateTime);
                            }
                        }
                        
                    } else {
                        Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");

                        TntSsPlan newPlan = new TntSsPlan();
                        newPlan.setSsPlanId(newSsPlanId);
                        newPlan.setSsId(newPlanEntity.getSsId());
                        newPlan.setTransportMode(newPlanEntity.getTransportMode());
                        newPlan.setEtd(newPlanEntity.getEtd());
                        newPlan.setEta(newPlanEntity.getEta());
                        newPlan.setCcDate(newPlanEntity.getCcDate());
                        newPlan.setImpInbPlanDate(newPlanEntity.getImpInbPlanDate());

                        TntSsPlan planParam = new TntSsPlan();
                        planParam.setSsId(newPlanEntity.getSsId());
                        List<TntSsPlan> versionList = this.baseMapper.select(this.getSqlId("getMaxPlanVersion"), planParam);
                        if (versionList == null || versionList.size() == 0) {
                            newPlan.setOriginalVersion(1);
                        } else {
                            newPlan.setOriginalVersion(versionList.get(0).getOriginalVersion() + 1);
                        }

                        newPlan.setRevisionVersion(0);
                        newPlan.setRevisionReason(SYSTEM_ADJUST_REASON);
                        newPlan.setNirdFlag(newPlanEntity.getNirdFlag());
                        newPlan.setCompletedFlag(CompletedFlag.NORMAL);
                        newPlan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                        newPlan.setCreatedDate(updateDateTime);
                        newPlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        newPlan.setUpdatedDate(updateDateTime);

                        this.baseMapper.insert(this.getSqlId("insertTntSsPlan"), newPlan);

                        for (TntSsPart ssPart : newPlanEntity.getNewSsPartsList()) {
                            if (ssPart.getQty() != null && !DecimalUtil.isEquals(ssPart.getQty(), BigDecimal.ZERO)) {
                                ssPart.setSsPlanId(newSsPlanId);
                                ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                ssPart.setCreatedDate(updateDateTime);
                                ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                ssPart.setUpdatedDate(updateDateTime);

                                this.baseMapper.insert(this.getSqlId("insertSsParts"), ssPart);
                            }
                        }
                    }
                }
            }

            for (Map.Entry<Integer, InvoiceAdjTempEntity> planEntry : planMap.entrySet()) {
                Integer ssPlanId = planEntry.getKey();
                InvoiceAdjTempEntity planData = planEntry.getValue();

                if (ssPlanId.intValue() == -1) {
                    continue;
                }

                /** merge no next etd qty */
                boolean hasNird = mergeSsParts(planData.getNewSsPartsList(), planData.getNoNextEtdSsPartsList());

                /**
                 * completed all changed plan
                 */
                TntSsPlan updatePlan = new TntSsPlan();
                updatePlan.setSsPlanId(ssPlanId);
                updatePlan.setCompletedFlag(CompletedFlag.COMPLETED);
                updatePlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                updatePlan.setUpdatedDate(updateDateTime);

                this.baseMapper.update(this.getSqlId("updateTntSsPlan"), updatePlan);

                /**
                 * To determine if you need to insert a new plan
                 */
                List<TntSsPart> newSsPartsList = planData.getNewSsPartsList();
                List<TntSsPart> nirdSsPartsList = planData.getNirdSsPartsList();
                boolean addNewPlanFlag = checkHasParts(newSsPartsList, true);
                boolean isNirdFlag = checkHasParts(nirdSsPartsList, false);

                if (addNewPlanFlag) {
                    Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");

                    TntSsPlan newPlan = new TntSsPlan();
                    newPlan.setSsPlanId(newSsPlanId);
                    newPlan.setSsId(planData.getSsId());
                    newPlan.setTransportMode(planData.getTransportMode());
                    newPlan.setEtd(planData.getEtd());
                    newPlan.setEta(planData.getEta());
                    newPlan.setCcDate(planData.getCcDate());
                    newPlan.setImpInbPlanDate(planData.getImpInbPlanDate());
                    newPlan.setOriginalVersion(planData.getOriginalVersion());
                    newPlan.setRevisionVersion(planData.getRevisionVersion().intValue() + 1);
                    newPlan.setRevisionReason(SYSTEM_ADJUST_REASON);

                    if (isNirdFlag || hasNird) {
                        newPlan.setNirdFlag(NirdFlag.NOT_IN_RUNDOWN);
                    } else {
                        newPlan.setNirdFlag(NirdFlag.NORMAL);
                    }
                    newPlan.setCompletedFlag(CompletedFlag.NORMAL);
                    newPlan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                    newPlan.setCreatedDate(updateDateTime);
                    newPlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    newPlan.setUpdatedDate(updateDateTime);

                    this.baseMapper.insert(this.getSqlId("insertTntSsPlan"), newPlan);

                    for (TntSsPart ssPart : planData.getNewSsPartsList()) {
                        if (ssPart.getQty() != null && !DecimalUtil.isEquals(ssPart.getQty(), BigDecimal.ZERO)) {
                            ssPart.setSsPlanId(newSsPlanId);
                            ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                            ssPart.setCreatedDate(updateDateTime);
                            ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            ssPart.setUpdatedDate(updateDateTime);

                            this.baseMapper.insert(this.getSqlId("insertSsParts"), ssPart);
                        }
                    }
                }
            }
        }
    }

    /**
     * insert or update tnt_ss_parts
     * 
     * @param ssPart TntSsPart
     * @param newSsPlanId Integer
     * @param updateDateTime Timestamp
     */
    private void insertOrUpdateSsParts(TntSsPart ssPart, Integer newSsPlanId, Timestamp updateDateTime) {
        TntSsPart param = new TntSsPart();
        param.setSsPlanId(newSsPlanId);
        param.setOrderStatusId(ssPart.getOrderStatusId());
        param.setPartsId(ssPart.getPartsId());
        
        List<TntSsPart> existPartsList = this.baseMapper.select(this.getSqlId("getExistParts"), param);
        if (existPartsList != null && existPartsList.size() > 0) {
            ssPart.setSsPartsId(existPartsList.get(0).getSsPartsId());
            ssPart.setQty(DecimalUtil.add(ssPart.getQty(), existPartsList.get(0).getQty()));
            ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            ssPart.setCreatedDate(updateDateTime);
            ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            ssPart.setUpdatedDate(updateDateTime);

            this.baseMapper.update(this.getSqlId("updateSsParts"), ssPart);
        } else {
            ssPart.setSsPlanId(newSsPlanId);
            ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
            ssPart.setCreatedDate(updateDateTime);
            ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
            ssPart.setUpdatedDate(updateDateTime);

            this.baseMapper.insert(this.getSqlId("insertSsParts"), ssPart);
        }
    }

    /**
     * if invoice parts is not comprehensive, then add a zero amount of the part
     * 
     * @param orderList List<InvoiceAdjEntity>
     * @param orderParts InvoiceAdjEntity
     * @return InvoiceAdjEntity
     */
    private InvoiceAdjEntity getInvoiceInfo(List<InvoiceAdjEntity> orderList, InvoiceAdjEntity orderParts) {

        if (orderList != null && orderList.size() > 0) {
            for (InvoiceAdjEntity order : orderList) {
                if (order.getPartsId().intValue() == orderParts.getPartsId().intValue()
                        && order.getOrderStatusId().intValue() == orderParts.getOrderStatusId().intValue()) {
                    return order;
                }
            }
        }

        InvoiceAdjEntity ret = new InvoiceAdjEntity();
        ret.setPartsId(orderParts.getPartsId());
        ret.setOrderStatusId(orderParts.getOrderStatusId());
        ret.setQty(BigDecimal.ZERO);
        if (orderList == null || orderList.size() == 0) {
            ret.setSsId(orderParts.getSsId());
            ret.setEtd(orderParts.getEtd());
            ret.setEta(orderParts.getEta());
            ret.setImpPlanCustomDate(orderParts.getImpPlanCustomDate());
            ret.setImpPlanInboundDate(orderParts.getImpPlanInboundDate());
            ret.setTansportMode(orderParts.getTansportMode());
        } else {
            ret.setSsId(orderList.get(0).getSsId());
            ret.setEtd(orderList.get(0).getEtd());
            ret.setEta(orderList.get(0).getEta());
            ret.setImpPlanCustomDate(orderList.get(0).getImpPlanCustomDate());
            ret.setImpPlanInboundDate(orderList.get(0).getImpPlanInboundDate());
            ret.setTansportMode(orderList.get(0).getTansportMode());
        }
        ret.setDelayAdjustmentPattern(orderParts.getDelayAdjustmentPattern());
        ret.setExpPartsId(orderParts.getExpPartsId());
        return ret;
    }

    /**
     * merge noNextEtdSsPartsList into newSsPartsList
     * 
     * @param newSsPartsList List<TntSsPart>
     * @param noNextEtdSsPartsList List<IfSsPartsEntity>
     * @return boolean
     */
    private boolean mergeSsParts(List<TntSsPart> newSsPartsList, List<IfSsPartsEntity> noNextEtdSsPartsList) {
        if (noNextEtdSsPartsList == null || noNextEtdSsPartsList.size() == 0) {
            return false;
        }
        boolean isNird = false;
        for (IfSsPartsEntity ssPart : noNextEtdSsPartsList) {
            if (ssPart.getDelayAdjustmentPattern().intValue() == SimulationEndDayPattern.PATTERN_A
                    && !ssPart.isHasNextEtd()) {
                resetNewPartsList(newSsPartsList, ssPart.getPartsId(), ssPart.getOrderStatusId(), ssPart.getQty());
                isNird = true;
            }
        }

        return isNird;
    }

    /**
     * reset New Parts List
     * 
     * @param newSsPartsList List<TntSsPart>
     * @param partsId Integer
     * @param orderStatusId Integer
     * @param qty BigDecimal
     */
    private void resetNewPartsList(List<TntSsPart> newSsPartsList, Integer partsId, Integer orderStatusId,
        BigDecimal qty) {
        boolean hasThisPart = false;
        for (TntSsPart newSsPart : newSsPartsList) {
            if (newSsPart.getPartsId().intValue() == partsId.intValue()
                    && newSsPart.getOrderStatusId().intValue() == orderStatusId.intValue()) {
                newSsPart.setQty(qty);
                hasThisPart = true;
                break;
            }
        }
        if (!hasThisPart) {
            TntSsPart newPart = new TntSsPart();
            newPart.setPartsId(partsId);
            newPart.setOrderStatusId(orderStatusId);
            newPart.setQty(qty);

            newSsPartsList.add(newPart);
        }
    }

    /**
     * get decimal's absolute value
     * 
     * @param value BigDecimal
     * @return BigDecimal
     */
    private BigDecimal getDecimalAbsValue(BigDecimal value) {
        if (DecimalUtil.isGreaterEquals(value, BigDecimal.ZERO)) {
            return value;
        } else {
            return DecimalUtil.subtract(BigDecimal.ZERO, value);
        }
    }

    /**
     * To determine if you need to insert a new plan
     * 
     * @param ssPartsList List<TntSsPart>
     * @param needCheckQty boolean
     * @return boolean
     */
    private boolean checkHasParts(List<TntSsPart> ssPartsList, boolean needCheckQty) {
        if (ssPartsList == null || ssPartsList.size() == 0) {
            return false;
        } else {
            if (!needCheckQty) {
                return true;
            }
        }

        for (TntSsPart ssPart : ssPartsList) {
            if (needCheckQty) {
                if (ssPart.getQty() != null && DecimalUtil.isGreater(ssPart.getQty(), BigDecimal.ZERO)) {
                    return true;
                }
            }
        }
        return false;
    }
}
