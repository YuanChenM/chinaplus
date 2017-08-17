/**
 * CPIIFB06Service.java
 * 
 * @screen CPIIFB06
 * @author yang_jia1
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.CompletedFlag;
import com.chinaplus.common.consts.CodeConst.NirdFlag;
import com.chinaplus.common.entity.IFOrderEntity;
import com.chinaplus.common.entity.SsmsMailEntity;
import com.chinaplus.common.entity.TntSsMaster;
import com.chinaplus.common.entity.TntSsPart;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;

/**
 * 
 * CPIIFB06Service.
 * order ss plan
 * 
 * @author yang_jia1
 */
@Service
@Component("ORDERPlan")
public class SsPlanService extends BaseService {

    /** System adjust reason */
    private static final String SYSTEM_ADJUST_REASON = "系统自动调整";

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(SsPlanService.class);

    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * doOrderPlanCreate
     * 
     * @param currSysDTForOrder currSysDTForOrder
     * @param sendMailList List<SsmsMailEntity>
     * @return boolean
     */
    public boolean doOrderPlanCreate(Timestamp currSysDTForOrder, List<SsmsMailEntity> sendMailList) {

        logger.debug("--------------- doOrderPlanCreate start-------------");

        IFOrderEntity condition = new IFOrderEntity();
        condition.setCreatedDate(currSysDTForOrder);
        condition.setUpdatedDate(currSysDTForOrder);

        List<IFOrderEntity> orderCreateList = this.baseMapper.select(this.getSqlId("queryOrderCreateList"), condition);
        
        createPlan(orderCreateList, sendMailList, currSysDTForOrder);

        logger.debug("--------------- doOrderPlanCreate end-------------");

        return true;
    }
    
    /**
     * doOrderPlanCreate
     * @param officeCode String
     * @param businessPattern Integer
     * 
     * @return boolean
     */
    public boolean doOrderPlanCreateForReRun(String officeCode, Integer businessPattern) {

        logger.debug("--------------- doOrderPlanCreateForReRun start-------------");
        List<SsmsMailEntity> sendMailList = new ArrayList<SsmsMailEntity>();

        Timestamp currSysDTForAdjPlan = this.getDBDateTimeByDefaultTimezone();
        IFOrderEntity condition = new IFOrderEntity();
        condition.setOfficeCode(officeCode);
        condition.setBusinessPattern(businessPattern);

        List<IFOrderEntity> orderCreateList = this.baseMapper.select(this.getSqlId("queryOrderCreateListForReRun"), condition);
        
        createPlan(orderCreateList, sendMailList, currSysDTForAdjPlan);

        logger.debug("--------------- doOrderPlanCreateForReRun end-------------");

        return true;
    }
    
    /**
     * createPlan
     * 
     * @param orderCreateList List<IFOrderEntity>
     * @param sendMailList List<SsmsMailEntity>
     * @param currSysDTForOrder Timestamp
     */
    private void createPlan(List<IFOrderEntity> orderCreateList, List<SsmsMailEntity> sendMailList, Timestamp currSysDTForOrder) {
        
        if (orderCreateList != null && orderCreateList.size() > 0) {

            // call common get plan
            for (IFOrderEntity orderEntity : orderCreateList) {
                orderEntity.setChainStartDate(orderEntity.getExpInbPlanDate());
            }

            supplyChainService.prepareSupplyChain(orderCreateList, ChainStep.PLAN, BusinessPattern.V_V);

            List<IFOrderEntity> orderCreateListMerge = mergeAndSortList(orderCreateList, sendMailList);

            String ssMasterKey = "";
            String ssPlanKey = "";
            String ssPartKey = "";
            Map<String, Integer> masterKeyMap = new HashMap<String, Integer>();
            Map<String, Integer> planKeyMap = new HashMap<String, Integer>();
            Map<String, Integer> partKeyMap = new HashMap<String, Integer>();

            for (IFOrderEntity order : orderCreateListMerge) {

                Timestamp currOfficeTime = this.getDBDateTime(order.getTimeZone());

                ssMasterKey = new StringBuffer().append(order.getOrderId()).append("|#&").append(order.getOfficeId())
                    .append("|#&").append(order.getImpPoNo()).toString();
                ssPlanKey = new StringBuffer().append(order.getOrderId()).append("|#&").append(order.getImpPoNo())
                    .append("|#&").append(order.getEtd()).append("|#&").append(order.getEta()).append("|#&")
                    .append(order.getImpPlanCustomDate()).append("|#&").append(order.getImpPlanInboundDate())
                    .append(order.getTansportMode()).toString();
                ssPartKey = new StringBuffer().append(ssPlanKey).append("|#&").append(order.getOrderStatusId())
                    .append("|#&").append(order.getPartsId()).toString();

                if (!masterKeyMap.containsKey(ssMasterKey)) {
                    /**
                     * insert into Tnt_SS_Master
                     */
                    insertTntSSMaster(order, currSysDTForOrder, currOfficeTime);

                    masterKeyMap.put(ssMasterKey, order.getSsId());

                    /**
                     * insert into Tnt_SS_Plan
                     */
                    insertTntSSPlan(order, currSysDTForOrder);

                    planKeyMap.put(ssPlanKey, order.getSsPlanId());

                    /**
                     * insert into Tnt_SS_Parts
                     */
                    insertTntSSParts(order, currSysDTForOrder);

                    partKeyMap.put(ssPartKey, order.getSsPartsId());
                } else {
                    if (!planKeyMap.containsKey(ssPlanKey)) {

                        /**
                         * insert into Tnt_SS_Plan
                         */
                        order.setSsId(masterKeyMap.get(ssMasterKey));
                        insertTntSSPlan(order, currSysDTForOrder);

                        planKeyMap.put(ssPlanKey, order.getSsPlanId());

                        /**
                         * insert into Tnt_SS_Parts
                         */
                        insertTntSSParts(order, currSysDTForOrder);

                        partKeyMap.put(ssPartKey, order.getSsPartsId());
                    } else {
                        if (!partKeyMap.containsKey(ssPartKey)) {
                            /**
                             * insert into Tnt_SS_Parts
                             */
                            order.setSsPlanId(planKeyMap.get(ssPlanKey));
                            insertTntSSParts(order, currSysDTForOrder);
                            partKeyMap.put(ssPartKey, order.getSsPartsId());
                        } else {
                            /**
                             * update into Tnt_SS_Parts
                             */
                            updateTntSSParts(order, currSysDTForOrder, partKeyMap.get(ssPartKey));
                        }
                    }
                }
            }
        }
    }

    /**
     * doOrderPlanModify
     * 
     * @param currSysDTForOrder orderList
     * @param sendMailList List<SsmsMailEntity>
     * @return boolean
     */
    public boolean doOrderPlanModify(Timestamp currSysDTForOrder, List<SsmsMailEntity> sendMailList) {

        logger.debug("--------------- doOrderPlanChange start-------------");

        IFOrderEntity condition = new IFOrderEntity();
        condition.setCreatedDate(currSysDTForOrder);
        condition.setUpdatedDate(currSysDTForOrder);

        List<IFOrderEntity> orderChangeList = this.baseMapper.select(this.getSqlId("queryOrderChangeList"), condition);

        if (orderChangeList != null && orderChangeList.size() > 0) {
            for (IFOrderEntity order : orderChangeList) {
                order.setChainStartDate(order.getExpInbPlanDate());
            }
        }

        // call common get plan
        supplyChainService.prepareSupplyChain(orderChangeList, ChainStep.PLAN, BusinessPattern.V_V, false);

        List<IFOrderEntity> orderChangeListMerge = mergeAndSortList(orderChangeList, sendMailList);

        Timestamp currChangeTime = this.getDBDateTimeByDefaultTimezone();

        for (IFOrderEntity changeOrder : orderChangeListMerge) {
            Integer transportMode = changeOrder.getTansportMode();
            Integer originalTransportMode = changeOrder.getOriginalTransportMode();

            /**
             * if transportMode changed, example: Sea 1000 ---> Air 500, then Sea - 1000 and Air + 500.
             * if transportMode not changed, example: Sea 1000 ---> Sea 500, then Sea - 500.
             */
            if (isIntegerEquals(transportMode, originalTransportMode)) {
                // transportMode not change
                changeShippingPlan(changeOrder, transportMode, false, currChangeTime, false);
            } else {
                // transportMode has changed
                changeShippingPlan(changeOrder, originalTransportMode, true, currChangeTime, true);
                changeShippingPlan(changeOrder, transportMode, true, currChangeTime, false);
            }
        }

        logger.debug("--------------- doOrderPlanChange end-------------");
        return true;
    }

    /**
     * doOrderPlanCancel
     * 
     * @param currSysDTForOrderCancel Timestamp
     * @param sendMailList List<SsmsMailEntity>
     * @return boolean
     */
    public boolean doOrderPlanCancel(Timestamp currSysDTForOrderCancel, List<SsmsMailEntity> sendMailList) {
        logger.debug("--------------- doOrderPlanCancel start-------------");

        IFOrderEntity param = new IFOrderEntity();
        param.setUpdatedDate(currSysDTForOrderCancel);
        List<IFOrderEntity> orderCancelList = this.baseMapper.select(this.getSqlId("queryOrderCancelList"), param);

        if (orderCancelList != null && orderCancelList.size() > 0) {
            for (IFOrderEntity order : orderCancelList) {
                order.setChainStartDate(order.getExpInbPlanDate());
            }
        }

        // call common get plan
        supplyChainService.prepareSupplyChain(orderCancelList, ChainStep.PLAN, BusinessPattern.V_V, false);

        List<IFOrderEntity> orderCancelListMerge = mergeAndSortList(orderCancelList, sendMailList);

        Timestamp currCancelTime = this.getDBDateTimeByDefaultTimezone();

        for (IFOrderEntity cancelOrder : orderCancelListMerge) {

            /**
             * order cancel do not consider changes the transportMode
             */
            Integer transportMode = cancelOrder.getTansportMode();

            /**
             * order cancel equivalent to changed the qty to zero.
             */
            cancelOrder.setOriginalQty(cancelOrder.getOrderQty());
            cancelOrder.setOrderQty(BigDecimal.ZERO);
            cancelOrder.setOriginalTransportMode(transportMode);

            changeShippingPlan(cancelOrder, transportMode, false, currCancelTime, false);
        }

        logger.debug("--------------- doOrderPlanCancel end-------------");
        return true;
    }

    /**
     * mergeAndSortList
     * 
     * @param originalList List<IFOrderEntity>
     * @param sendMailList List<SsmsMailEntity>
     * @return List<IFOrderEntity>
     */
    private List<IFOrderEntity> mergeAndSortList(List<IFOrderEntity> originalList, List<SsmsMailEntity> sendMailList) {

        List<IFOrderEntity> mergeList = new ArrayList<IFOrderEntity>();

        /**
         * map Key : ssId + transportMode + etd + eta + ccDate + impInbPlanDate
         * map value: IFOrderEntity
         */
        Map<String, IFOrderEntity> totalQtyMap = new HashMap<String, IFOrderEntity>();
        String totalKey = "";
        List<String> keys = new ArrayList<String>();
        for (IFOrderEntity order : originalList) {

            /**
             * if get etd from prepareSupplyChain failed, add this entity to sendMailList.
             */
            if (order.getImpPlanInboundDate() == null) {
                SsmsMailEntity sendMailEntity = new SsmsMailEntity();
                sendMailEntity.setOfficeId(order.getOfficeId());
                sendMailEntity.setOfficeCode(order.getOfficeCode());
                sendMailEntity.setTimeZone(order.getTimeZone());
                sendMailEntity.setTtcCustomerCode(order.getTtcCustomerCode());
                sendMailEntity.setImpPoNo(order.getImpPoNo());
                sendMailEntity.setExpSoDate(order.getExpSoDate());
                sendMailEntity.setShippingRouteCode(order.getShippingRouteCode());

                logger.info("----------------------------------------------------");
                logger.info("OfficeCode: [" + order.getOfficeCode() + "]");
                logger.info("PartsId: [" + order.getPartsId() + "]");
                logger.info("ImpPoNo: [" + order.getImpPoNo() + "]");
                logger.info("ExpPoNo: [" + order.getExpPoNo() + "]");
                logger.info("ShippingRouteCode: [" + order.getShippingRouteCode() + "]");
                logger.info("ExpInbPlanDateStr: [" + order.getExpInbPlanDate() + "]");

                sendMailList.add(sendMailEntity);
            }

            totalKey = new StringBuffer().append(order.getOrderId()).append("|#&").append(order.getTansportMode())
                .append("|#&").append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, order.getEtd()))
                .append("|#&").append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, order.getEta()))
                .append("|#&")
                .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, order.getImpPlanCustomDate()))
                .append("|#&")
                .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, order.getImpPlanInboundDate()))
                .append("|#&").append(order.getPartsId()).append("|#&").append(order.getSupplierId()).toString();
            if (!totalQtyMap.containsKey(totalKey)) {
                keys.add(totalKey);
                totalQtyMap.put(totalKey, order);
            } else {
                IFOrderEntity totalQty = totalQtyMap.get(totalKey);
                totalQty.setOriginalQty(DecimalUtil.add(totalQty.getOriginalQty(), order.getOriginalQty()));
                totalQty.setOrderQty(DecimalUtil.add(totalQty.getOrderQty(), order.getOrderQty()));
                totalQtyMap.put(totalKey, totalQty);
            }
        }

        for (String key : keys) {
            mergeList.add(totalQtyMap.get(key));
        }

        Collections.sort(mergeList, new Comparator<IFOrderEntity>() {

            @Override
            public int compare(IFOrderEntity o1, IFOrderEntity o2) {
                String key1 = new StringBuffer().append(o1.getOrderId()).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o1.getEtd())).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o1.getEta())).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o1.getImpPlanCustomDate()))
                    .append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o1.getImpPlanInboundDate()))
                    .toString();
                String key2 = new StringBuffer().append(o2.getOrderId()).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o2.getEtd())).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o2.getEta())).append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o2.getImpPlanCustomDate()))
                    .append("|#&")
                    .append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, o2.getImpPlanInboundDate()))
                    .toString();
                return key1.compareTo(key2);
            }

        });
        return mergeList;
    }

    /**
     * change Shipping Plan
     * 
     * @param changeOrder IFOrderEntity
     * @param transportMode Integer
     * @param modeHasChanged boolean
     * @param currSysDTForOrder Timestamp
     * @param reSetTransportModeFlag boolean
     */
    private void changeShippingPlan(IFOrderEntity changeOrder, Integer transportMode, boolean modeHasChanged,
        Timestamp currSysDTForOrder, boolean reSetTransportModeFlag) {

        BigDecimal originalQty = changeOrder.getOriginalQty();
        BigDecimal orderQty = changeOrder.getOrderQty();
        BigDecimal changeQty = null;
        IFOrderEntity condition = new IFOrderEntity();

        if (modeHasChanged) {
            if (isIntegerEquals(transportMode, changeOrder.getOriginalTransportMode())) {
                changeQty = DecimalUtil.subtract(BigDecimal.ZERO, originalQty);
                condition.setOriginalTransportMode(changeOrder.getOriginalTransportMode());
                condition.setTansportMode(changeOrder.getTansportMode());
            } else {
                changeQty = orderQty;
                condition.setOriginalTransportMode(changeOrder.getTansportMode());
                condition.setTansportMode(changeOrder.getOriginalTransportMode());
            }
        } else {
            changeQty = DecimalUtil.subtract(orderQty, originalQty);
            condition.setOriginalTransportMode(changeOrder.getOriginalTransportMode());
            condition.setTansportMode(changeOrder.getTansportMode());
        }

        if (modeHasChanged && reSetTransportModeFlag) {

            List<IFOrderEntity> reSetEtdlist = new ArrayList<IFOrderEntity>();
            IFOrderEntity entity = new IFOrderEntity();
            BeanUtils.copyProperties(changeOrder, entity);
            entity.setTansportMode(entity.getOriginalTransportMode());
            entity.setEtd(null);
            entity.setEta(null);
            entity.setImpPlanCustomDate(null);
            entity.setImpPlanInboundDate(null);
            reSetEtdlist.add(entity);

            // call common get plan
            supplyChainService.prepareSupplyChain(reSetEtdlist, ChainStep.PLAN, BusinessPattern.V_V, false);
            condition.setEtd(entity.getEtd());
            condition.setEta(entity.getEta());
            condition.setImpPlanCustomDate(entity.getImpPlanCustomDate());
            condition.setImpPlanInboundDate(entity.getImpPlanInboundDate());
        } else {
            condition.setEtd(changeOrder.getEtd());
            condition.setEta(changeOrder.getEta());
            condition.setImpPlanCustomDate(changeOrder.getImpPlanCustomDate());
            condition.setImpPlanInboundDate(changeOrder.getImpPlanInboundDate());
        }
        condition.setPartsId(changeOrder.getPartsId());
        condition.setOrderStatusId(changeOrder.getOrderStatusId());
        condition.setOrderId(changeOrder.getOrderId());

        List<IFOrderEntity> ssPlanList = this.baseMapper.select(this.getSqlId("getPlanList"), condition);

        Map<Integer, List<TntSsPart>> adjPlanMap = new HashMap<Integer, List<TntSsPart>>();

        boolean qtyIsIncrease = false;
        boolean existsOriginalPlan = false;
        boolean existsPlanFlag = false;

        if (ssPlanList != null && ssPlanList.size() > 0) {

            for (IFOrderEntity ssParts : ssPlanList) {

                Integer ssPlanId = ssParts.getSsPlanId();
                List<TntSsPart> partsList = null;
                if (adjPlanMap.containsKey(ssPlanId)) {
                    partsList = adjPlanMap.get(ssPlanId);
                } else {
                    TntSsPart conditionForAllParts = new TntSsPart();
                    conditionForAllParts.setSsPlanId(ssParts.getSsPlanId());
                    partsList = this.baseMapper.select(this.getSqlId("getAllPartsOfPlan"), conditionForAllParts);
                    adjPlanMap.put(ssPlanId, partsList);
                }

                if (DecimalUtil.isGreater(changeQty, BigDecimal.ZERO)) {
                    if (ssParts.getSortNum().intValue() == 1) {
                        /**
                         * Original Plan exists, update the qty = qty + changeQty
                         */
                        for (TntSsPart part : partsList) {
                            if (part.getPartsId().equals(changeOrder.getPartsId())
                                    && part.getOrderStatusId().equals(changeOrder.getOrderStatusId())) {
                                part.setQty(DecimalUtil.add(part.getQty(), changeQty));
                                existsPlanFlag = true;
                                break;
                            }
                        }
                        if (!existsPlanFlag) {
                            TntSsPart part = new TntSsPart();
                            part.setSsPlanId(ssPlanId);
                            part.setPartsId(changeOrder.getPartsId());
                            part.setOrderStatusId(changeOrder.getOrderStatusId());
                            part.setQty(changeQty);
                            partsList.add(part);
                        }
                        qtyIsIncrease = true;
                        existsOriginalPlan = true;
                        break;
                    } else {
                        /**
                         * qty increase and original plan not exists, create new plan
                         */
                        qtyIsIncrease = true;
                        existsOriginalPlan = false;
                        break;
                    }
                } else if (DecimalUtil.isEquals(changeQty, BigDecimal.ZERO)) {

                    if (modeHasChanged) {
                        /**
                         * when transportMode has changed, changeQty equals orderQty or OriginalOrderQty, can not be
                         * zero. So do not do any processing.
                         */
                        break;
                    } else {
                        /**
                         * Qty not changed and transportMode not changed, do not do any processing.
                         */
                        break;
                    }

                } else {
                    BigDecimal surplusQty = null;
                    TntSsPart currPart = null;

                    for (TntSsPart part : partsList) {
                        if (part.getPartsId().equals(changeOrder.getPartsId())
                                && part.getOrderStatusId().equals(changeOrder.getOrderStatusId())) {
                            surplusQty = DecimalUtil.add(changeQty, part.getQty());
                            currPart = part;
                            break;
                        }
                    }
                    
                    if (currPart == null) {
                        // surplusQty = BigDecimal.ZERO;
                        surplusQty = changeQty;
                    }
                    
                    if (!DecimalUtil.isGreater(surplusQty, BigDecimal.ZERO)) {
                        /**
                         * update qty = 0.
                         */
                        if (currPart != null) {
                            currPart.setQty(BigDecimal.ZERO);
                        }

                        if (DecimalUtil.isEquals(surplusQty, BigDecimal.ZERO)) {
                            /**
                             * The current plan is enough to adjust, stop processing.
                             */
                            break;
                        } else {
                            changeQty = surplusQty;
                        }
                    } else {
                        /**
                         * update qty = surplusQty.
                         */
                        if (currPart != null) {
                            currPart.setQty(surplusQty);
                        }
                        break;
                    }
                }
            }
        } else {
            qtyIsIncrease = true;
            existsOriginalPlan = false;
        }

        if (qtyIsIncrease) {
            /**
             * parts's qty increase
             */
            if (existsOriginalPlan) {
                /**
                 * this parts's original plan exists
                 */
                for (Map.Entry<Integer, List<TntSsPart>> adjPlanEntry : adjPlanMap.entrySet()) {
                    Integer ssPlanId = adjPlanEntry.getKey();
                    List<TntSsPart> adjPlanList = adjPlanEntry.getValue();

                    TntSsPlan planParam = new TntSsPlan();
                    planParam.setSsPlanId(ssPlanId);
                    TntSsPlan plan = this.baseMapper.findOne(this.getSqlId("findOneTntSsPlan"), planParam);

                    /**
                     * check need to add a new plan
                     */
                    boolean needAddPlanFlag = checkNeedAddPlan(adjPlanList);

                    if (!plan.getUpdatedDate().equals(currSysDTForOrder)) {
                        /**
                         * completed the original plan
                         */
                        plan.setUpdatedDate(currSysDTForOrder);
                        plan.setCompletedFlag(CompletedFlag.COMPLETED);
                        plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        this.baseMapper.update(this.getSqlId("completedTntSsPlan"), plan);

                        if (needAddPlanFlag) {
                            /**
                             * insert a new plan.
                             */
                            Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");
                            plan.setSsPlanId(newSsPlanId);
                            plan.setCompletedFlag(CompletedFlag.NORMAL);
                            plan.setRevisionVersion(plan.getRevisionVersion() + 1);
                            plan.setRevisionReason(SYSTEM_ADJUST_REASON);
                            plan.setUpdatedDate(currSysDTForOrder);
                            plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            plan.setCreatedDate(currSysDTForOrder);
                            plan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);

                            this.doInsertSSPlan(plan);

                            /**
                             * insert each part.
                             */
                            for (TntSsPart part : adjPlanList) {
                                if (part.getQty() != null && DecimalUtil.isGreater(part.getQty(), BigDecimal.ZERO)) {
                                    TntSsPart ssPart = new TntSsPart();
                                    Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
                                    ssPart.setSsPartsId(ssPartsId);
                                    ssPart.setQty(part.getQty());
                                    ssPart.setSsPlanId(newSsPlanId);
                                    ssPart.setOrderStatusId(part.getOrderStatusId());
                                    ssPart.setPartsId(part.getPartsId());
                                    ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setCreatedDate(currSysDTForOrder);
                                    ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setUpdatedDate(currSysDTForOrder);
                                    this.doInsertSSParts(ssPart);
                                }
                            }
                        }
                    } else {
                        /**
                         * The original plan is new created plan just, add or update this parts
                         */
                        TntSsPart partParam = new TntSsPart();
                        partParam.setSsPlanId(ssPlanId);
                        partParam.setPartsId(changeOrder.getPartsId());
                        partParam.setOrderStatusId(changeOrder.getOrderStatusId());
                        TntSsPart currPart = this.baseMapper.findOne(this.getSqlId("getCurrParts"), partParam);

                        if (currPart != null) {
                            /**
                             * update this parts's qty
                             */
                            TntSsPart updatePart = new TntSsPart();
                            updatePart.setSsPartsId(currPart.getSsPartsId());
                            updatePart.setQty(DecimalUtil.add(changeQty, currPart.getQty()));
                            updatePart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            updatePart.setUpdatedDate(currSysDTForOrder);
                            this.doUpdateSSParts(updatePart);

                        } else {
                            /**
                             * insert the new parts
                             */
                            TntSsPart insertPart = new TntSsPart();
                            Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
                            insertPart.setSsPartsId(ssPartsId);
                            insertPart.setQty(changeQty);
                            insertPart.setSsPlanId(ssPlanId);
                            insertPart.setOrderStatusId(changeOrder.getOrderStatusId());
                            insertPart.setPartsId(changeOrder.getPartsId());
                            insertPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                            insertPart.setCreatedDate(currSysDTForOrder);
                            insertPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            insertPart.setUpdatedDate(currSysDTForOrder);
                            this.doInsertSSParts(insertPart);
                        }
                    }
                }
            } else {
                /**
                 * this parts's original plan not exists, create a new plan
                 */
                TntSsPlan plan = new TntSsPlan();
                Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");
                plan.setSsPlanId(newSsPlanId);
                plan.setSsId(changeOrder.getSsId());
                plan.setTransportMode(changeOrder.getTansportMode());
                plan.setEtd(changeOrder.getEtd());
                plan.setEta(changeOrder.getEta());
                plan.setCcDate(changeOrder.getImpPlanCustomDate());
                plan.setImpInbPlanDate(changeOrder.getImpPlanInboundDate());

                List<IFOrderEntity> versionList = this.baseMapper.select(this.getSqlId("queryMaxVersion"), changeOrder);
                if (versionList == null || versionList.size() == 0) {
                    plan.setOriginalVersion(1);
                    plan.setRevisionVersion(0);
                } else {
                    IFOrderEntity version = versionList.get(0);
                    if (version.getNewPlanFlag() == 1) {
                        plan.setOriginalVersion(version.getOriginalVersion() + 1);
                        plan.setRevisionVersion(version.getRevisionVersion());
                    } else {
                        plan.setOriginalVersion(version.getOriginalVersion());
                        plan.setRevisionVersion(version.getRevisionVersion() + 1);
                    }
                }
                plan.setRevisionReason(SYSTEM_ADJUST_REASON);
                plan.setNirdFlag(NirdFlag.NORMAL);
                plan.setCompletedFlag(CompletedFlag.NORMAL);
                plan.setUpdatedDate(currSysDTForOrder);
                plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                plan.setCreatedDate(currSysDTForOrder);
                plan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                this.doInsertSSPlan(plan);

                /**
                 * insert this parts
                 */
                TntSsPart insertPart = new TntSsPart();
                Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
                insertPart.setSsPartsId(ssPartsId);
                insertPart.setQty(changeQty);
                insertPart.setSsPlanId(newSsPlanId);
                insertPart.setOrderStatusId(changeOrder.getOrderStatusId());
                insertPart.setPartsId(changeOrder.getPartsId());
                insertPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                insertPart.setCreatedDate(currSysDTForOrder);
                insertPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                insertPart.setUpdatedDate(currSysDTForOrder);
                this.doInsertSSParts(insertPart);
            }
        } else {
            /**
             * parts's qty reduce
             */
            if (!adjPlanMap.isEmpty()) {
                for (Map.Entry<Integer, List<TntSsPart>> adjPlanEntry : adjPlanMap.entrySet()) {
                    Integer ssPlanId = adjPlanEntry.getKey();
                    List<TntSsPart> adjPlanList = adjPlanEntry.getValue();

                    TntSsPlan planParam = new TntSsPlan();
                    planParam.setSsPlanId(ssPlanId);
                    TntSsPlan plan = this.baseMapper.findOne(this.getSqlId("findOneTntSsPlan"), planParam);

                    /**
                     * check need to add a new plan
                     */
                    boolean needAddPlanFlag = checkNeedAddPlan(adjPlanList);

                    if (!plan.getUpdatedDate().equals(currSysDTForOrder)) {
                        /**
                         * completed the original plan
                         */
                        plan.setUpdatedDate(currSysDTForOrder);
                        plan.setCompletedFlag(CompletedFlag.COMPLETED);
                        plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        this.baseMapper.update(this.getSqlId("completedTntSsPlan"), plan);

                        if (needAddPlanFlag) {
                            /**
                             * insert a new plan.
                             */
                            Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");
                            plan.setSsPlanId(newSsPlanId);
                            plan.setCompletedFlag(CompletedFlag.NORMAL);
                            plan.setRevisionVersion(plan.getRevisionVersion() + 1);
                            plan.setRevisionReason(SYSTEM_ADJUST_REASON);
                            plan.setUpdatedDate(currSysDTForOrder);
                            plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            plan.setCreatedDate(currSysDTForOrder);
                            plan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);

                            this.doInsertSSPlan(plan);

                            /**
                             * insert each part.
                             */
                            for (TntSsPart part : adjPlanList) {
                                if (part.getQty() != null && DecimalUtil.isGreater(part.getQty(), BigDecimal.ZERO)) {
                                    TntSsPart ssPart = new TntSsPart();
                                    Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
                                    ssPart.setSsPartsId(ssPartsId);
                                    ssPart.setQty(part.getQty());
                                    ssPart.setSsPlanId(newSsPlanId);
                                    ssPart.setOrderStatusId(part.getOrderStatusId());
                                    ssPart.setPartsId(part.getPartsId());
                                    ssPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setCreatedDate(currSysDTForOrder);
                                    ssPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    ssPart.setUpdatedDate(currSysDTForOrder);
                                    this.doInsertSSParts(ssPart);
                                }
                            }
                        } else {
                            /**
                             * if this plan's all parts is cancelled, then add a new completed plan.
                             */
                            Integer newSsPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");
                            plan.setSsPlanId(newSsPlanId);
                            plan.setCompletedFlag(CompletedFlag.COMPLETED);
                            plan.setRevisionVersion(plan.getRevisionVersion() + 1);
                            plan.setRevisionReason(SYSTEM_ADJUST_REASON);
                            plan.setUpdatedDate(currSysDTForOrder);
                            plan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                            plan.setCreatedDate(currSysDTForOrder);
                            plan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);

                            this.doInsertSSPlan(plan);
                        }
                    } else {
                        /**
                         * The original plan is new created plan just, add or update this parts
                         */
                        for (TntSsPart part : adjPlanList) {

                            TntSsPart partParam = new TntSsPart();
                            partParam.setSsPlanId(part.getSsPlanId());
                            partParam.setPartsId(part.getPartsId());
                            partParam.setOrderStatusId(part.getOrderStatusId());
                            TntSsPart currPart = this.baseMapper.findOne(this.getSqlId("getCurrParts"), partParam);

                            if (currPart != null) {
                                /**
                                 * qty is zero, then ,delete parts, if the plan has no parts, delete plan.
                                 */
                                boolean deletePlanFlag = true;
                                for (TntSsPart p : adjPlanList) {
                                    if (p.getQty() != null && DecimalUtil.isGreater(p.getQty(), BigDecimal.ZERO)) {
                                        deletePlanFlag = false;
                                    }
                                }

                                TntSsPart delOrUpdatePart = new TntSsPart();
                                delOrUpdatePart.setSsPartsId(currPart.getSsPartsId());

                                if (DecimalUtil.isEquals(part.getQty(), BigDecimal.ZERO)) {
                                    /**
                                     * delete parts
                                     */
                                    this.baseMapper.update(this.getSqlId("delTntSsParts"), delOrUpdatePart);

                                    if (deletePlanFlag) {
                                        // /**
                                        // * delete plan
                                        // */
                                        // TntSsPlan delPlan = new TntSsPlan();
                                        // delPlan.setSsPlanId(part.getSsPlanId());
                                        // this.baseMapper.update(this.getSqlId("delTntSsPlan"), delPlan);
                                        /**
                                         * completed plan
                                         */
                                        TntSsPlan delPlan = new TntSsPlan();
                                        delPlan.setUpdatedDate(currSysDTForOrder);
                                        delPlan.setCompletedFlag(CompletedFlag.COMPLETED);
                                        delPlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                        delPlan.setSsPlanId(part.getSsPlanId());
                                        this.baseMapper.update(this.getSqlId("completedTntSsPlan"), delPlan);
                                    }
                                } else {
                                    /**
                                     * update this parts's qty
                                     */
                                    delOrUpdatePart.setQty(part.getQty());
                                    delOrUpdatePart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    delOrUpdatePart.setUpdatedDate(currSysDTForOrder);
                                    this.doUpdateSSParts(delOrUpdatePart);
                                }

                            } else {
                                /**
                                 * insert the new parts
                                 */
                                if (part.getQty() != null && DecimalUtil.isGreater(part.getQty(), BigDecimal.ZERO)) {
                                    TntSsPart insertPart = new TntSsPart();
                                    Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
                                    insertPart.setSsPartsId(ssPartsId);
                                    insertPart.setQty(part.getQty());
                                    insertPart.setSsPlanId(part.getSsPlanId());
                                    insertPart.setOrderStatusId(part.getOrderStatusId());
                                    insertPart.setPartsId(part.getPartsId());
                                    insertPart.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    insertPart.setCreatedDate(currSysDTForOrder);
                                    insertPart.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    insertPart.setUpdatedDate(currSysDTForOrder);
                                    this.doInsertSSParts(insertPart);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * check need to add a new plan
     * 
     * @param adjPlanList List<TntSsPart>
     * @return boolean
     */
    private boolean checkNeedAddPlan(List<TntSsPart> adjPlanList) {
        if (adjPlanList != null && adjPlanList.size() > 0) {
            for (TntSsPart ssPart : adjPlanList) {
                if (ssPart.getQty() != null && DecimalUtil.isGreater(ssPart.getQty(), BigDecimal.ZERO)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * insert into Tnt_SS_Parts
     * 
     * @param order IFOrderEntity
     * @param currSysDTForOrder Timestamp
     * @return boolean
     */
    private int insertTntSSParts(IFOrderEntity order, Timestamp currSysDTForOrder) {
        Integer ssPartsId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PARTS");
        TntSsPart ssParts = new TntSsPart();
        ssParts.setQty(order.getOrderQty());
        ssParts.setSsPartsId(ssPartsId);
        ssParts.setSsPlanId(order.getSsPlanId());
        ssParts.setOrderStatusId(order.getOrderStatusId());
        ssParts.setPartsId(order.getPartsId());
        ssParts.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
        ssParts.setCreatedDate(currSysDTForOrder);
        ssParts.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        ssParts.setUpdatedDate(currSysDTForOrder);
        order.setSsPartsId(ssPartsId);
        return this.doInsertSSParts(ssParts);
    }

    /**
     * insert into Tnt_SS_Parts
     * 
     * @param order IFOrderEntity
     * @param currSysDTForOrder Timestamp
     * @param ssPartsId Integer
     * @return boolean
     */
    private int updateTntSSParts(IFOrderEntity order, Timestamp currSysDTForOrder, Integer ssPartsId) {
        TntSsPart ssParts = new TntSsPart();
        ssParts.setQty(order.getOrderQty());
        ssParts.setSsPartsId(ssPartsId);
        ssParts.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        ssParts.setUpdatedDate(currSysDTForOrder);
        return this.doUpdateSSParts(ssParts);
    }

    /**
     * insert into Tnt_SS_Plan
     * 
     * @param order IFOrderEntity
     * @param currSysDTForOrder Timestamp
     * @return boolean
     */
    private int insertTntSSPlan(IFOrderEntity order, Timestamp currSysDTForOrder) {
        Integer ssPlanId = this.baseMapper.getNextSequence("SEQ_TNT_SS_PLAN");

        TntSsPlan orderPlan = new TntSsPlan();
        orderPlan.setSsPlanId(ssPlanId);
        orderPlan.setSsId(order.getSsId());
        orderPlan.setTransportMode(order.getTansportMode());
        orderPlan.setEtd(order.getEtd());
        orderPlan.setEta(order.getEta());
        orderPlan.setCcDate(order.getImpPlanCustomDate());
        orderPlan.setImpInbPlanDate(order.getImpPlanInboundDate());

        List<IFOrderEntity> versionList = this.baseMapper.select(this.getSqlId("queryMaxVersion"), order);
        if (versionList == null || versionList.size() == 0) {
            orderPlan.setOriginalVersion(1);
            orderPlan.setRevisionVersion(0);
        } else {
            IFOrderEntity version = versionList.get(0);
            if (version.getNewPlanFlag() == 1) {
                orderPlan.setOriginalVersion(version.getOriginalVersion() + 1);
                orderPlan.setRevisionVersion(version.getRevisionVersion());
            } else {
                orderPlan.setOriginalVersion(version.getOriginalVersion());
                orderPlan.setRevisionVersion(version.getRevisionVersion() + 1);
            }
        }

        orderPlan.setNirdFlag(NirdFlag.NORMAL);
        orderPlan.setCompletedFlag(CompletedFlag.NORMAL);
        orderPlan.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
        orderPlan.setCreatedDate(currSysDTForOrder);
        orderPlan.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        orderPlan.setUpdatedDate(currSysDTForOrder);
        order.setSsPlanId(ssPlanId);
        return this.doInsertSSPlan(orderPlan);
    }

    /**
     * insert into Tnt_SS_Master
     * 
     * @param order IFOrderEntity
     * @param currSysDTForOrder Timestamp
     * @param currOfficeTime Timestamp
     * @return boolean
     */
    private int insertTntSSMaster(IFOrderEntity order, Timestamp currSysDTForOrder, Timestamp currOfficeTime) {
        Integer ssId = this.baseMapper.getNextSequence("SEQ_TNT_SS_MASTER");

        TntSsMaster planMaster = new TntSsMaster();
        planMaster.setSsId(ssId);
        planMaster.setOrderId(order.getOrderId());
        planMaster.setOfficeId(order.getOfficeId());
        planMaster.setRevisionVersion(0);
        planMaster.setUploadedBy(ChinaPlusConst.BATCH_USER_ID);
        planMaster.setUploadedDate(currOfficeTime);
        planMaster.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
        planMaster.setCreatedDate(currSysDTForOrder);
        planMaster.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        planMaster.setUpdatedDate(currSysDTForOrder);
        order.setSsId(ssId);
        return this.doInsertSSMaster(planMaster);
    }

    /**
     * compare two Integer
     * 
     * @param int1 Integer
     * @param int2 Integer
     * @return boolean
     */
    private boolean isIntegerEquals(Integer int1, Integer int2) {
        Integer i1 = Integer.valueOf(0);
        Integer i2 = Integer.valueOf(0);
        if (int1 != null) {
            i1 = int1;
        }
        if (int2 != null) {
            i2 = int2;
        }
        return i1.equals(i2);
    }

    /**
     * doInsertSSMaster
     * 
     * @param ssMaster TntSsMaster
     * @return int
     */
    private int doInsertSSMaster(TntSsMaster ssMaster) {
        return this.baseMapper.insert(this.getSqlId("addSSMaster"), ssMaster);
    }

    /**
     * doInsertSSPlan
     * 
     * @param ssPlan TntSsPlan
     * @return int
     */
    private int doInsertSSPlan(TntSsPlan ssPlan) {
        return this.baseMapper.insert(this.getSqlId("addSSPlan"), ssPlan);
    }

    /**
     * doInsertSSParts
     * 
     * @param ssParts TntSsPart
     * @return int
     */
    private int doInsertSSParts(TntSsPart ssParts) {
        return this.baseMapper.insert(this.getSqlId("addSSParts"), ssParts);
    }

    /**
     * doUpdateSSParts
     * 
     * @param ssParts TntSsPart
     * @return int
     */
    private int doUpdateSSParts(TntSsPart ssParts) {
        return this.baseMapper.update(this.getSqlId("updateSSParts"), ssParts);
    }
}
