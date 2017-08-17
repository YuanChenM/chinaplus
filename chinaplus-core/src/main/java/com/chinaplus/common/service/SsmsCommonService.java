/**
 * CPIIFB05Service.java
 * 
 * @screen CPIIFB05
 * @author yang_jia1
 */
package com.chinaplus.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.InboundSource;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.OutboundSource;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.HandleFlag;
import com.chinaplus.common.consts.CodeConst.InvoiceStatus;
import com.chinaplus.common.consts.CodeConst.IpStatus;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.IFInboundEntity;
import com.chinaplus.common.entity.IFInvoiceEntity;
import com.chinaplus.common.entity.IFOrderCancelEntity;
import com.chinaplus.common.entity.IFOrderEntity;
import com.chinaplus.common.entity.IFOutboundEntity;
import com.chinaplus.common.entity.SsmsCommonParam;
import com.chinaplus.common.entity.SsmsMailEntity;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceContainer;
import com.chinaplus.common.entity.TntInvoicePart;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.MailUtil;
import com.chinaplus.core.util.MailUtilForOnline;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * SSMSBusiness Service.
 * ssms logic
 * 
 * @author yang_jia1
 */
@Service
@Component("SMSSService")
public class SsmsCommonService extends BaseService {

    /** inbound status */
    private static final int INBOUND_STATUS_CANCELED = 99;

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(SsmsCommonService.class);

    @Autowired
    private SsPlanService ssPlanService;

    @Autowired
    private InvoiceAdjService invoiceAdjService;

    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * SendEmailService.
     */
    @Autowired
    private SendEmailService sendEmailService;

    /**
     * doSSMSLogic
     * 
     * @param expPartsIdList List<Integer>
     * @return Map<String, Integer>
     */
    public Map<String, Integer> doSSMSLogic(List<Integer> expPartsIdList) {

        SsmsCommonParam param = new SsmsCommonParam();
        param.setExpPartsId(expPartsIdList);
        return doSSMSLogic(param);

    }

    /**
     * doSSMSBusinessLogic
     * 
     * @param param param
     * @return excuteResult
     * @throws BatchException e
     */
    public Map<String, Integer> doSSMSLogic(SsmsCommonParam param) throws BatchException {

        boolean isOnLine = false;
        if (param.getExpPartsId() != null) {
            isOnLine = true;
        }

        List<SsmsMailEntity> sendMailList = new ArrayList<SsmsMailEntity>();

        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        logger.debug("--------------- doOrderCancelLogic & doOrderLogic start-------------");

        List<String> ifDateTimeList = new ArrayList<String>();

        /**
         * get cancel order info
         */
        IFOrderCancelEntity cancelParam = new IFOrderCancelEntity();
        cancelParam.setIfDateTime(param.getIfDateTime());
        cancelParam.setExpPartsIdList(param.getExpPartsId());
        Long startUDCancel = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER start: [" + startUDCancel + "]");
        int ocSystemNumber = this.baseMapper.update(this.getSqlId("modOrderCancelIfValidFlag"), cancelParam);
        Long endUDCancel = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER end: [" + endUDCancel + "]");
        logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER times: [" + (endUDCancel - startUDCancel) + "]");

        List<Integer> handleFlagParam = new ArrayList<Integer>();
        handleFlagParam.add(HandleFlag.UNPROCESS);
        cancelParam.setHandleFlagParam(handleFlagParam);
        Long startSelCancel = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER start: [" + startSelCancel + "]");
        List<IFOrderCancelEntity> ocList = this.doQeuryOrderCancelIf(cancelParam);
        Long endSelCancel = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER end: [" + endSelCancel + "]");
        logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER times: [" + (endSelCancel - startSelCancel) + "]");

        Map<String, List<IFOrderCancelEntity>> orderCancelMap = new HashMap<String, List<IFOrderCancelEntity>>();

        logger.info("+++++++++++++++  ordercancel cnt: [" + ocList.size() + "]");
        for (IFOrderCancelEntity orderCancel : ocList) {
            String ifDateTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS,
                orderCancel.getIfDateTime());
            if (!ifDateTimeList.contains(ifDateTime)) {
                ifDateTimeList.add(ifDateTime);
            }

            if (!orderCancelMap.containsKey(ifDateTime)) {
                List<IFOrderCancelEntity> orderCancelList = new ArrayList<IFOrderCancelEntity>();
                orderCancelList.add(orderCancel);
                orderCancelMap.put(ifDateTime, orderCancelList);
            } else {
                List<IFOrderCancelEntity> orderCancelList = orderCancelMap.get(ifDateTime);
                orderCancelList.add(orderCancel);
            }
        }
        // release memory
        ocList = null;

        /**
         * get order info
         */
        IFOrderEntity orderParam = new IFOrderEntity();
        orderParam.setIfDateTime(param.getIfDateTime());
        orderParam.setExpPartsIdList(param.getExpPartsId());
        Long startUDOrder = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER start: [" + startUDOrder + "]");
        int odSystemNumber = this.baseMapper.update(this.getSqlId("modOrderIfValidFlag"), orderParam);
        Long endUDOrder = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER end: [" + endUDOrder + "]");
        logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER times: [" + (endUDOrder - startUDOrder) + "]");

        List<Integer> handleFlagParam1 = new ArrayList<Integer>();
        handleFlagParam1.add(HandleFlag.UNPROCESS);
        orderParam.setHandleFlagParam(handleFlagParam1);
        Long startSelOrder = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER start: [" + startSelOrder + "]");
        List<IFOrderEntity> ifList = this.doQeuryOrderIf(orderParam);
        Long endSelOrder = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER end: [" + endSelOrder + "]");
        logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER times: [" + (endSelOrder - startSelOrder) + "]");

        Long startGetMinExpPlanDate = System.currentTimeMillis();
        logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder start: [" + startGetMinExpPlanDate + "]");
        List<IFOrderEntity> minExpInbPlanDateByOrderList = this.getMinExpInbPlanDateByOrder(orderParam);
        Long endGetMinExpPlanDate = System.currentTimeMillis();
        logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder end: [" + endGetMinExpPlanDate + "]");
        logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder times: ["
                + (endGetMinExpPlanDate - startGetMinExpPlanDate) + "]");

        Map<String, List<IFOrderEntity>> orderMap = new HashMap<String, List<IFOrderEntity>>();

        logger.info("+++++++++++++++  order cnt: [" + ifList.size() + "]");
        for (IFOrderEntity order : ifList) {
            String ifDateTime = DateTimeUtil
                .formatDate(DateTimeUtil.FORMAT_DATE_DDMMMYYYYHHMMSS, order.getIfDateTime());
            if (!ifDateTimeList.contains(ifDateTime)) {
                ifDateTimeList.add(ifDateTime);
            }

            if (!orderMap.containsKey(ifDateTime)) {
                List<IFOrderEntity> orderList = new ArrayList<IFOrderEntity>();
                orderList.add(order);
                orderMap.put(ifDateTime, orderList);
            } else {
                List<IFOrderEntity> orderList = orderMap.get(ifDateTime);
                orderList.add(order);
            }
        }
        // release memory
        ifList = null;

        // sort ifDateTimeList
        Collections.sort(ifDateTimeList);

        for (String ifDateTime : ifDateTimeList) {
            /**
             * do order cancel processing
             */
            Timestamp currSysDTForOrderCancel = this.getDBDateTimeByDefaultTimezone();
            List<IFOrderCancelEntity> orderCancelList = orderCancelMap.get(ifDateTime);

            if (orderCancelList != null && orderCancelList.size() > 0) {
                for (IFOrderCancelEntity orderCancel : orderCancelList) {
                    orderCancel.setCreatedDate(currSysDTForOrderCancel);
                    orderCancel.setUpdatedDate(currSysDTForOrderCancel);
                    orderCancel.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);

                    IFOrderCancelEntity existOrder = this.doOrderCancelDetailExist(orderCancel);

                    if (existOrder != null) {

                        if (existOrder.getForceCompletedBy() != null) {
                            logger.error("EXP_PO_NO: [" + orderCancel.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
                                    + orderCancel.getExpPoItemNo() + "], PARTS_NO :[" + orderCancel.getPartsNo()
                                    + "]. is already force completed!");
                            orderCancel.setHandleFlag(HandleFlag.ALREADY_FORCE_COMPLETED);
                            this.doUpdateOrderCancelHandleFlag(orderCancel);
                            continue;
                        }

                        this.doUpdateOrderCancelDetail(orderCancel);
                    } else {
                        logger.error("EXP_PO_NO: [" + orderCancel.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
                                + orderCancel.getExpPoItemNo() + "], PARTS_NO :[" + orderCancel.getPartsNo()
                                + "]. Not exist in TNT_ORDER_DETAIL table!");
                        orderCancel.setHandleFlag(HandleFlag.ORDER_NOT_EXIST);
                        this.doUpdateOrderCancelHandleFlag(orderCancel);
                        continue;
                    }

                    TnfOrderStatus statusParam = new TnfOrderStatus();
                    statusParam.setImpPoNo(orderCancel.getImpPoNo());
                    statusParam.setExpPoNo(orderCancel.getExpPoNo());
                    statusParam.setCustomerOrderNo(orderCancel.getCustomerOrderNo());
                    statusParam.setPartsId(orderCancel.getPartsId());
                    statusParam.setSupplierId(orderCancel.getSupplierId());
                    statusParam.setUpdatedBy(orderCancel.getUpdatedBy());
                    statusParam.setUpdatedDate(currSysDTForOrderCancel);

                    TnfOrderStatus existorderCancelStatus = this.doOrderCancelStatusExist(statusParam);

                    if (existorderCancelStatus != null) {
                        statusParam.setOrderQty(DecimalUtil.subtract(BigDecimal.ZERO, existOrder.getOrderQty()));
                        statusParam.setOrderStatusId(existorderCancelStatus.getOrderStatusId());
                        this.doUpdateOrderStatus(statusParam);
                    } else {
                        logger.error("IMP_PO_NO: [" + statusParam.getImpPoNo() + "], EXP_PO_NO: ["
                                + statusParam.getExpPoNo() + "], CUSTOMER_ORDER_NO: ["
                                + statusParam.getCustomerOrderNo() + "], PARTS_ID: [" + statusParam.getPartsId()
                                + "], SUPPLIER_ID: [" + statusParam.getSupplierId()
                                + "]. Not exist in TNF_ORDER_STATUS table!");
                        orderCancel.setHandleFlag(HandleFlag.ORDER_NOT_EXIST);
                        this.doUpdateOrderCancelHandleFlag(orderCancel);
                        continue;
                    }

                    // update handle flag
                    orderCancel.setHandleFlag(HandleFlag.PROCESS_SUCCESS);

                    this.doUpdateOrderCancelHandleFlag(orderCancel);
                }
                logger.info(ifDateTime + "+++++++++++++++  ordercancel : [" + orderCancelList.size()
                    + "] records has been successful");

                /**
                 * Update shipping status info
                 */
                ssPlanService.doOrderPlanCancel(currSysDTForOrderCancel, sendMailList);
            }

            /**
             * do order processing
             */
            Timestamp currSysDTForOrder = this.getDBDateTimeByDefaultTimezone();
            List<IFOrderEntity> orderList = orderMap.get(ifDateTime);

            Integer orderId = this.baseMapper.getNextSequence("SEQ_TNT_ORDER");

            if (orderList != null && orderList.size() > 0 && minExpInbPlanDateByOrderList != null
                    && minExpInbPlanDateByOrderList.size() > 0) {
                Map<String, String> orderMonthMap = new HashMap<String, String>();
                for (IFOrderEntity ifOrder : minExpInbPlanDateByOrderList) {

                    String key = new StringBuffer().append(ifOrder.getImpPoNo()).append("|#&")
                        .append(ifOrder.getCustomerOrderNo()).append("|#&").append(ifOrder.getCustomerId()).toString();

                    Date expInbPlanDate = DateTimeUtil.parseDate(ifOrder.getMinExpInbPlanDate(),
                        DateTimeUtil.FORMAT_YYYYMMDD);
                    String minOrderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                        DateTimeUtil.addMonth(expInbPlanDate, -1));

                    if (!orderMonthMap.containsKey(key)) {
                        orderMonthMap.put(key, minOrderMonth);
                    }
                }

                for (int i = 0; i < orderList.size(); i++) {

                    if (i % IntDef.INT_THOUSAND == 0 && i / IntDef.INT_THOUSAND != 0) {
                        logger.info(ifDateTime + "+++++++++++++++  order : [" + i + "] records has been successful");
                    }

                    IFOrderEntity ssmsOrder = orderList.get(i);
                    IFOrderEntity existOrderDetail = this.doQeuryOrderDetailExist(ssmsOrder);
                    if (existOrderDetail != null && existOrderDetail.getForceCompletedBy() != null) {
                        logger.error("EXP_PO_NO: [" + ssmsOrder.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
                                + ssmsOrder.getExpPoItemNo() + "], PARTS_NO :[" + ssmsOrder.getTtcPartsNo()
                                + "]. is already force completed!");
                        ssmsOrder.setHandleFlag(HandleFlag.ALREADY_FORCE_COMPLETED);
                        this.doUpdateOrderHandleFlag(ssmsOrder);
                        continue;
                    }

                    ssmsOrder.setCreatedDate(currSysDTForOrder);
                    ssmsOrder.setUpdatedDate(currSysDTForOrder);
                    if (!StringUtil.isEmpty(ssmsOrder.getExpInbPlanDateStr())) {
                        ssmsOrder.setExpInbPlanDate(DateTimeUtil.parseDate(ssmsOrder.getExpInbPlanDateStr(),
                            DateTimeUtil.FORMAT_YYYYMMDD));
                    } else {
                        ssmsOrder.setExpInbPlanDate(null);
                    }

                    if (ssmsOrder.getTransportModeStr() != null && !"".equals(ssmsOrder.getTransportModeStr())) {
                        if ("S".equals(ssmsOrder.getTransportModeStr())) {
                            ssmsOrder.setTansportMode(TransportMode.SEA);
                        }

                        if ("A".equals(ssmsOrder.getTransportModeStr())) {
                            ssmsOrder.setTansportMode(TransportMode.AIR);
                        }
                    }

                    // tnt_order
                    logger.debug("doBusinessdataSaveLogic : insert table TNT_ORDER");

                    IFOrderEntity existOrder = this.doQeuryOrderExist(ssmsOrder);

                    String orderKey = new StringBuffer().append(ssmsOrder.getImpPoNo()).append("|#&")
                        .append(ssmsOrder.getCustomerOrderNo()).append("|#&").append(ssmsOrder.getCustomerId())
                        .toString();
                    ssmsOrder.setOrderMonth(orderMonthMap.get(orderKey));

                    String soDate = ssmsOrder.getSoDate();
                    if (soDate != null && !"".equals(soDate)) {
                        Date expSoDate = new Date(DateTimeUtil.parseDate(soDate).getTime());
                        ssmsOrder.setExpSoDate(expSoDate);
                    }

                    String poDate = ssmsOrder.getPoDate();
                    if (poDate != null && !"".equals(poDate)) {
                        Date expPoDate = new Date(DateTimeUtil.parseDate(poDate).getTime());
                        ssmsOrder.setExpPoDate(expPoDate);
                    }

                    if (i != 0) {
                        IFOrderEntity temp = orderList.get(i - 1);

                        if (!ssmsOrder.getCustomerOrderNo().equals(temp.getCustomerOrderNo())
                                || !ssmsOrder.getImpPoNo().equals(temp.getImpPoNo())
                                || !ssmsOrder.getCustomerId().equals(temp.getCustomerId())) {
                            if (existOrder != null) {
                                ssmsOrder.setOrderId(existOrder.getOrderId());
                                this.doUpdateOrderSSMS(ssmsOrder);
                            } else {
                                orderId = this.baseMapper.getNextSequence("SEQ_TNT_ORDER");
                                ssmsOrder.setOrderId(orderId);
                                this.doInsertOrderSSMS(ssmsOrder);
                            }
                        } else {
                            if (existOrder != null) {
                                ssmsOrder.setOrderId(existOrder.getOrderId());
                            } else {
                                ssmsOrder.setOrderId(orderId);
                            }
                        }
                    } else {
                        if (existOrder != null) {
                            ssmsOrder.setOrderId(existOrder.getOrderId());
                            this.doUpdateOrderSSMS(ssmsOrder);
                        } else {
                            ssmsOrder.setOrderId(orderId);
                            this.doInsertOrderSSMS(ssmsOrder);
                        }
                    }

                    // tnt_order_detail
                    logger.debug("doBusinessdataSaveLogic : insert table TNT_ORDER_DETAIL");

                    if (existOrderDetail != null) {
                        ssmsOrder.setOriginalQty(existOrderDetail.getOrderQty());
                        ssmsOrder.setOriginalTransportMode(existOrderDetail.getTansportMode());
                        this.doUpdateOrderDetail(ssmsOrder);
                    } else {
                        ssmsOrder.setOriginalQty(BigDecimal.ZERO);
                        ssmsOrder.setOriginalTransportMode(ssmsOrder.getTansportMode());
                        this.doInsertOrderDetail(ssmsOrder);
                    }

                    // tnf_order_status
                    logger.debug("doBusinessdataSaveLogic : insert table TNF_ORDER_STATUS");

                    TnfOrderStatus orderStatus = new TnfOrderStatus();
                    orderStatus.setImpPoNo(ssmsOrder.getImpPoNo());
                    orderStatus.setExpPoNo(ssmsOrder.getExpPoNo());
                    orderStatus.setCustomerOrderNo(ssmsOrder.getCustomerOrderNo());
                    orderStatus.setPartsId(ssmsOrder.getPartsId());
                    orderStatus.setSupplierId(ssmsOrder.getSupplierId());
                    orderStatus.setUpdatedBy(ssmsOrder.getUpdatedBy());
                    orderStatus.setCreatedDate(currSysDTForOrder);
                    orderStatus.setUpdatedDate(currSysDTForOrder);

                    if (ssmsOrder.getShippingRouteCode() != null) {
                        orderStatus.setShippingStatusFlag(IntDef.INT_ONE);
                    } else {
                        orderStatus.setShippingStatusFlag(IntDef.INT_ZERO);
                    }

                    TnfOrderStatus orderStatusOld = this.doQeuryOrderStatusExist(orderStatus);

                    // order status change
                    if (orderStatusOld != null) {
                        BigDecimal orderQty = ssmsOrder.getOrderQty();
                        orderStatus.setOrderStatusId(orderStatusOld.getOrderStatusId());
                        ssmsOrder.setOrderStatusId(orderStatusOld.getOrderStatusId());
                        // order detail change
                        if (existOrderDetail != null) {
                            orderStatus.setOrderQty(DecimalUtil.subtract(orderQty, existOrderDetail.getOrderQty()));

                            // order detail add
                        } else {
                            orderStatus.setOrderQty(orderQty);
                        }
                        this.doUpdateOrderStatus(orderStatus);
                        // order status add
                    } else {
                        Integer orderStatusId = this.baseMapper.getNextSequence("SEQ_TNF_ORDER_STATUS");

                        ssmsOrder.setOrderStatusId(orderStatusId);
                        orderStatus.setOrderStatusId(orderStatusId);
                        orderStatus.setBusinessPattern(ssmsOrder.getBusinessPattern());
                        orderStatus.setOrderQty(ssmsOrder.getOrderQty());
                        orderStatus.setOrderMonth(ssmsOrder.getOrderMonth());
                        orderStatus.setCreatedBy(ssmsOrder.getCreatedBy());
                        orderStatus.setUpdatedBy(ssmsOrder.getUpdatedBy());
                        this.doInsertOrderStatus(orderStatus);
                    }

                    // ifList.set(i, ssmsOrder);

                    // update handle flag
                    ssmsOrder.setHandleFlag(HandleFlag.PROCESS_SUCCESS);

                    this.doUpdateOrderHandleFlag(ssmsOrder);
                }
                logger.info(ifDateTime + "+++++++++++++++  order : [" + orderList.size()
                        + "] records has been successful");
            }

            /**
             * Update shipping status info
             */
            ssPlanService.doOrderPlanModify(currSysDTForOrder, sendMailList);
            ssPlanService.doOrderPlanCreate(currSysDTForOrder, sendMailList);
        }

        // del at 20160909 for can not get order cancel info at parts master upload start.
        // logger.debug("--------------- doOrderCancelLogic start-------------");
        //
        // Timestamp currSysDTForOrderCancel = this.getDBDateTimeByDefaultTimezone();
        // IFOrderCancelEntity cancelParam = new IFOrderCancelEntity();
        // cancelParam.setIfDateTime(param.getIfDateTime());
        // cancelParam.setExpPartsIdList(param.getExpPartsId());
        // Long startUDCancel = System.currentTimeMillis();
        // logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER start: [" + startUDCancel + "]");
        // int ocSystemNumber = this.baseMapper.update(this.getSqlId("modOrderCancelIfValidFlag"), cancelParam);
        // Long endUDCancel = System.currentTimeMillis();
        // logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER end: [" + endUDCancel + "]");
        // logger.info("+++++++++++++++ update TNT_IF_CANCEL_ORDER times: [" + (endUDCancel - startUDCancel) + "]");
        //
        // List<Integer> handleFlagParam = new ArrayList<Integer>();
        // handleFlagParam.add(HandleFlag.UNPROCESS);
        // cancelParam.setHandleFlagParam(handleFlagParam);
        // Long startSelCancel = System.currentTimeMillis();
        // logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER start: [" + startSelCancel + "]");
        // List<IFOrderCancelEntity> ocList = this.doQeuryOrderCancelIf(cancelParam);
        // Long endSelCancel = System.currentTimeMillis();
        // logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER end: [" + endSelCancel + "]");
        // logger.info("+++++++++++++++ select TNT_IF_CANCEL_ORDER times: [" + (endSelCancel - startSelCancel) + "]");
        //
        // if (ocList != null && ocList.size() > 0) {
        // logger.info("+++++++++++++++  ordercancel cnt: [" + ocList.size() + "]");
        // for (IFOrderCancelEntity orderCancel : ocList) {
        // orderCancel.setCreatedDate(currSysDTForOrderCancel);
        // orderCancel.setUpdatedDate(currSysDTForOrderCancel);
        // orderCancel.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
        //
        // IFOrderCancelEntity existOrder = this.doOrderCancelDetailExist(orderCancel);
        //
        // if (existOrder != null) {
        //
        // if (existOrder.getForceCompletedBy() != null) {
        // logger.error("EXP_PO_NO: [" + orderCancel.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
        // + orderCancel.getExpPoItemNo() + "], PARTS_NO :[" + orderCancel.getPartsNo()
        // + "]. is already force completed!");
        // orderCancel.setHandleFlag(HandleFlag.ALREADY_FORCE_COMPLETED);
        // this.doUpdateOrderCancelHandleFlag(orderCancel);
        // continue;
        // }
        //
        // this.doUpdateOrderCancelDetail(orderCancel);
        // } else {
        // logger.error("EXP_PO_NO: [" + orderCancel.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
        // + orderCancel.getExpPoItemNo() + "], PARTS_NO :[" + orderCancel.getPartsNo()
        // + "]. Not exist in TNT_ORDER_DETAIL table!");
        // orderCancel.setHandleFlag(HandleFlag.ORDER_NOT_EXIST);
        // this.doUpdateOrderCancelHandleFlag(orderCancel);
        // continue;
        // }
        //
        // TnfOrderStatus statusParam = new TnfOrderStatus();
        // statusParam.setImpPoNo(orderCancel.getImpPoNo());
        // statusParam.setExpPoNo(orderCancel.getExpPoNo());
        // statusParam.setCustomerOrderNo(orderCancel.getCustomerOrderNo());
        // statusParam.setPartsId(orderCancel.getPartsId());
        // statusParam.setSupplierId(orderCancel.getSupplierId());
        // statusParam.setUpdatedBy(orderCancel.getUpdatedBy());
        // statusParam.setUpdatedDate(currSysDTForOrderCancel);
        //
        // TnfOrderStatus existorderCancelStatus = this.doOrderCancelStatusExist(statusParam);
        //
        // if (existorderCancelStatus != null) {
        // statusParam.setOrderQty(DecimalUtil.subtract(BigDecimal.ZERO, existOrder.getOrderQty()));
        // statusParam.setOrderStatusId(existorderCancelStatus.getOrderStatusId());
        // this.doUpdateOrderStatus(statusParam);
        // } else {
        // logger.error("IMP_PO_NO: [" + statusParam.getImpPoNo() + "], EXP_PO_NO: ["
        // + statusParam.getExpPoNo() + "], CUSTOMER_ORDER_NO: [" + statusParam.getCustomerOrderNo()
        // + "], PARTS_ID: [" + statusParam.getPartsId() + "], SUPPLIER_ID: ["
        // + statusParam.getSupplierId() + "]. Not exist in TNF_ORDER_STATUS table!");
        // orderCancel.setHandleFlag(HandleFlag.ORDER_NOT_EXIST);
        // this.doUpdateOrderCancelHandleFlag(orderCancel);
        // continue;
        // }
        //
        // // update handle flag
        // orderCancel.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
        //
        // this.doUpdateOrderCancelHandleFlag(orderCancel);
        // }
        //
        // /**
        // * Update shipping status info
        // */
        // ssPlanService.doOrderPlanCancel(currSysDTForOrderCancel, sendMailList);
        // }
        //
        // logger.debug("--------------- doOrderCancelLogic end-------------");
        //
        // logger.debug("--------------- doOrderLogic start-------------");
        // Timestamp currSysDTForOrder = this.getDBDateTimeByDefaultTimezone();
        // IFOrderEntity order = new IFOrderEntity();
        // order.setIfDateTime(param.getIfDateTime());
        // order.setExpPartsIdList(param.getExpPartsId());
        // Long startUDOrder = System.currentTimeMillis();
        // logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER start: [" + startUDOrder + "]");
        // int odSystemNumber = this.baseMapper.update(this.getSqlId("modOrderIfValidFlag"), order);
        // Long endUDOrder = System.currentTimeMillis();
        // logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER end: [" + endUDOrder + "]");
        // logger.info("+++++++++++++++ update TNT_IF_EXP_ORDER times: [" + (endUDOrder - startUDOrder) + "]");
        //
        // List<Integer> handleFlagParam1 = new ArrayList<Integer>();
        // handleFlagParam1.add(HandleFlag.UNPROCESS);
        // order.setHandleFlagParam(handleFlagParam1);
        // Long startSelOrder = System.currentTimeMillis();
        // logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER start: [" + startSelOrder + "]");
        // List<IFOrderEntity> ifList = this.doQeuryOrderIf(order);
        // Long endSelOrder = System.currentTimeMillis();
        // logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER end: [" + endSelOrder + "]");
        // logger.info("+++++++++++++++ select TNT_IF_EXP_ORDER times: [" + (endSelOrder - startSelOrder) + "]");
        //
        // Long startGetMinExpPlanDate = System.currentTimeMillis();
        // logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder start: [" + startGetMinExpPlanDate + "]");
        // List<IFOrderEntity> minExpInbPlanDateByOrderList = this.getMinExpInbPlanDateByOrder(order);
        // Long endGetMinExpPlanDate = System.currentTimeMillis();
        // logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder end: [" + endGetMinExpPlanDate + "]");
        // logger.info("+++++++++++++++ getMinExpInbPlanDateByOrder times: ["
        // + (endGetMinExpPlanDate - startGetMinExpPlanDate) + "]");
        //
        // Integer orderId = this.baseMapper.getNextSequence("SEQ_TNT_ORDER");
        //
        // if (ifList != null && ifList.size() > 0 && minExpInbPlanDateByOrderList != null
        // && minExpInbPlanDateByOrderList.size() > 0) {
        // logger.info("+++++++++++++++  order cnt: [" + ifList.size() + "]");
        // Map<String, String> orderMonthMap = new HashMap<String, String>();
        // for (IFOrderEntity ifOrder : minExpInbPlanDateByOrderList) {
        //
        // String key = new StringBuffer().append(ifOrder.getImpPoNo()).append("|#&")
        // .append(ifOrder.getCustomerOrderNo()).append("|#&").append(ifOrder.getCustomerId()).toString();
        //
        // Date expInbPlanDate = DateTimeUtil.parseDate(ifOrder.getMinExpInbPlanDate(),
        // DateTimeUtil.FORMAT_YYYYMMDD);
        // String minOrderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
        // DateTimeUtil.addMonth(expInbPlanDate, -1));
        //
        // if (!orderMonthMap.containsKey(key)) {
        // orderMonthMap.put(key, minOrderMonth);
        // }
        // }
        //
        // for (int i = 0; i < ifList.size(); i++) {
        //
        // if (i % IntDef.INT_THOUSAND == 0) {
        // logger.info("+++++++++++++++  order : [" + i + "] records has been successful");
        // }
        //
        // IFOrderEntity ssmsOrder = ifList.get(i);
        // IFOrderEntity existOrderDetail = this.doQeuryOrderDetailExist(ssmsOrder);
        // if (existOrderDetail != null && existOrderDetail.getForceCompletedBy() != null) {
        // logger.error("EXP_PO_NO: [" + ssmsOrder.getExpPoNo() + "], EXP_PO_ITEM_NO: ["
        // + ssmsOrder.getExpPoItemNo() + "], PARTS_NO :[" + ssmsOrder.getTtcPartsNo()
        // + "]. is already force completed!");
        // ssmsOrder.setHandleFlag(HandleFlag.ALREADY_FORCE_COMPLETED);
        // this.doUpdateOrderHandleFlag(ssmsOrder);
        // continue;
        // }
        //
        // ssmsOrder.setCreatedDate(currSysDTForOrder);
        // ssmsOrder.setUpdatedDate(currSysDTForOrder);
        // if (!StringUtil.isEmpty(ssmsOrder.getExpInbPlanDateStr())) {
        // ssmsOrder.setExpInbPlanDate(DateTimeUtil.parseDate(ssmsOrder.getExpInbPlanDateStr(),
        // DateTimeUtil.FORMAT_YYYYMMDD));
        // } else {
        // ssmsOrder.setExpInbPlanDate(null);
        // }
        //
        // if (ssmsOrder.getTransportModeStr() != null && !"".equals(ssmsOrder.getTransportModeStr())) {
        // if ("S".equals(ssmsOrder.getTransportModeStr())) {
        // ssmsOrder.setTansportMode(TransportMode.SEA);
        // }
        //
        // if ("A".equals(ssmsOrder.getTransportModeStr())) {
        // ssmsOrder.setTansportMode(TransportMode.AIR);
        // }
        // }
        //
        // // tnt_order
        // logger.debug("doBusinessdataSaveLogic : insert table TNT_ORDER");
        //
        // IFOrderEntity existOrder = this.doQeuryOrderExist(ssmsOrder);
        //
        // String orderKey = new StringBuffer().append(ssmsOrder.getImpPoNo()).append("|#&")
        // .append(ssmsOrder.getCustomerOrderNo()).append("|#&").append(ssmsOrder.getCustomerId()).toString();
        // ssmsOrder.setOrderMonth(orderMonthMap.get(orderKey));
        //
        // String soDate = ssmsOrder.getSoDate();
        // if (soDate != null && !"".equals(soDate)) {
        // Date expSoDate = new Date(DateTimeUtil.parseDate(soDate).getTime());
        // ssmsOrder.setExpSoDate(expSoDate);
        //
        // // del at 20160818 for tel from liubin confirm with weetat.
        // // List<TnmCalendarDetailEx> cldDetailList = new ArrayList<TnmCalendarDetailEx>();
        // // Date fromDate = DateTimeUtil.firstDay(expSoDate);
        // // Date toDate = DateTimeUtil.lastDay(DateTimeUtil.addMonth(expSoDate, IntDef.INT_ONE));
        // //
        // // if (ssmsOrder.getCalendarId() != null) {
        // // TnmCalendarDetailEx paramCalendar = new TnmCalendarDetailEx();
        // // paramCalendar.setFromDate(fromDate);
        // // paramCalendar.setToDate(toDate);
        // // paramCalendar.setCalendarId(ssmsOrder.getCalendarId());
        // //
        // // cldDetailList = this.baseMapper.select(this.getSqlId("getCldDetailList"), paramCalendar);
        // // }
        // //
        // // // Set loopDate = Next day of (Last day of Current Month)
        // // Date firstDay = CalendarManager.getNextWorkingDay(
        // // DateTimeUtil.firstDay(DateTimeUtil.addMonth(expSoDate, IntDef.INT_ONE)), cldDetailList,
        // // IntDef.INT_ONE);
        // //
        // // // Get The Last Order Date
        // // Date lastOrderDate = CalendarManager.getDateByWorkingDay(ssmsOrder.getOrderDay(), firstDay,
        // // cldDetailList, IntDef.INT_N_ONE);
        //
        // // if (expSoDate.after(lastOrderDate)) {
        // // // set order month next month
        // // ssmsOrder.setOrderMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, firstDay));
        // // } else {
        // // // set order month current month
        // // ssmsOrder.setOrderMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, expSoDate));
        // // }
        // }
        //
        // String poDate = ssmsOrder.getPoDate();
        // if (poDate != null && !"".equals(poDate)) {
        // Date expPoDate = new Date(DateTimeUtil.parseDate(poDate).getTime());
        // ssmsOrder.setExpPoDate(expPoDate);
        // }
        //
        // if (i != 0) {
        // IFOrderEntity temp = ifList.get(i - 1);
        //
        // if (!ssmsOrder.getCustomerOrderNo().equals(temp.getCustomerOrderNo())
        // || !ssmsOrder.getImpPoNo().equals(temp.getImpPoNo())
        // || !ssmsOrder.getCustomerId().equals(temp.getCustomerId())) {
        // if (existOrder != null) {
        // ssmsOrder.setOrderId(existOrder.getOrderId());
        // this.doUpdateOrderSSMS(ssmsOrder);
        // } else {
        // orderId = this.baseMapper.getNextSequence("SEQ_TNT_ORDER");
        // ssmsOrder.setOrderId(orderId);
        // this.doInsertOrderSSMS(ssmsOrder);
        // }
        // } else {
        // if (existOrder != null) {
        // ssmsOrder.setOrderId(existOrder.getOrderId());
        // } else {
        // ssmsOrder.setOrderId(orderId);
        // }
        // }
        // } else {
        // if (existOrder != null) {
        // ssmsOrder.setOrderId(existOrder.getOrderId());
        // this.doUpdateOrderSSMS(ssmsOrder);
        // } else {
        // ssmsOrder.setOrderId(orderId);
        // this.doInsertOrderSSMS(ssmsOrder);
        // }
        // }
        //
        // // tnt_order_detail
        // logger.debug("doBusinessdataSaveLogic : insert table TNT_ORDER_DETAIL");
        //
        // if (existOrderDetail != null) {
        // ssmsOrder.setOriginalQty(existOrderDetail.getOrderQty());
        // ssmsOrder.setOriginalTransportMode(existOrderDetail.getTansportMode());
        // this.doUpdateOrderDetail(ssmsOrder);
        // } else {
        // ssmsOrder.setOriginalQty(BigDecimal.ZERO);
        // ssmsOrder.setOriginalTransportMode(ssmsOrder.getTansportMode());
        // this.doInsertOrderDetail(ssmsOrder);
        // }
        //
        // // tnf_order_status
        // logger.debug("doBusinessdataSaveLogic : insert table TNF_ORDER_STATUS");
        //
        // TnfOrderStatus orderStatus = new TnfOrderStatus();
        // orderStatus.setImpPoNo(ssmsOrder.getImpPoNo());
        // orderStatus.setExpPoNo(ssmsOrder.getExpPoNo());
        // orderStatus.setCustomerOrderNo(ssmsOrder.getCustomerOrderNo());
        // orderStatus.setPartsId(ssmsOrder.getPartsId());
        // orderStatus.setSupplierId(ssmsOrder.getSupplierId());
        // orderStatus.setUpdatedBy(ssmsOrder.getUpdatedBy());
        // orderStatus.setCreatedDate(currSysDTForOrder);
        // orderStatus.setUpdatedDate(currSysDTForOrder);
        //
        // if (ssmsOrder.getShippingRouteCode() != null) {
        // orderStatus.setShippingStatusFlag(IntDef.INT_ONE);
        // } else {
        // orderStatus.setShippingStatusFlag(IntDef.INT_ZERO);
        // }
        //
        // TnfOrderStatus orderStatusOld = this.doQeuryOrderStatusExist(orderStatus);
        //
        // // order status change
        // if (orderStatusOld != null) {
        // BigDecimal orderQty = ssmsOrder.getOrderQty();
        // orderStatus.setOrderStatusId(orderStatusOld.getOrderStatusId());
        // ssmsOrder.setOrderStatusId(orderStatusOld.getOrderStatusId());
        // // order detail change
        // if (existOrderDetail != null) {
        // orderStatus.setOrderQty(DecimalUtil.subtract(orderQty, existOrderDetail.getOrderQty()));
        //
        // // order detail add
        // } else {
        // orderStatus.setOrderQty(orderQty);
        // }
        // this.doUpdateOrderStatus(orderStatus);
        // // order status add
        // } else {
        // Integer orderStatusId = this.baseMapper.getNextSequence("SEQ_TNF_ORDER_STATUS");
        //
        // ssmsOrder.setOrderStatusId(orderStatusId);
        // orderStatus.setOrderStatusId(orderStatusId);
        // orderStatus.setBusinessPattern(ssmsOrder.getBusinessPattern());
        // orderStatus.setOrderQty(ssmsOrder.getOrderQty());
        // orderStatus.setOrderMonth(ssmsOrder.getOrderMonth());
        // orderStatus.setCreatedBy(ssmsOrder.getCreatedBy());
        // orderStatus.setUpdatedBy(ssmsOrder.getUpdatedBy());
        // this.doInsertOrderStatus(orderStatus);
        // }
        //
        // // ifList.set(i, ssmsOrder);
        //
        // // update handle flag
        // ssmsOrder.setHandleFlag(HandleFlag.PROCESS_SUCCESS);
        //
        // this.doUpdateOrderHandleFlag(ssmsOrder);
        // }
        //
        // /**
        // * Update shipping status info
        // */
        // ssPlanService.doOrderPlanModify(currSysDTForOrder, sendMailList);
        // ssPlanService.doOrderPlanCreate(currSysDTForOrder, sendMailList);
        // }
        //
        // logger.debug("--------------- doOrderLogic end-------------");
        // del at 20160909 for can not get order cancel info at parts master upload end.

        logger.debug("--------------- doInboundLogic start-------------");
        Timestamp currSysDTForInbound = this.getDBDateTimeByDefaultTimezone();
        IFInboundEntity inboundParam = new IFInboundEntity();
        inboundParam.setIfDateTime(param.getIfDateTime());
        inboundParam.setExpPartsIdList(param.getExpPartsId());
        Long startUDInbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_INBOUND start: [" + startUDInbound + "]");
        int inSystemNumber = this.baseMapper.update(this.getSqlId("modInboundIfValidFlag"), inboundParam);
        Long endUDInbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_INBOUND end: [" + endUDInbound + "]");
        logger.info("+++++++++++++++ update TNT_IF_EXP_INBOUND times: [" + (endUDInbound - startUDInbound) + "]");

        List<Integer> handleFlagParam2 = new ArrayList<Integer>();
        handleFlagParam2.add(HandleFlag.UNPROCESS);
        inboundParam.setHandleFlagParam(handleFlagParam2);
        Long startSelInbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_INBOUND start: [" + startSelInbound + "]");
        List<IFInboundEntity> ibList = this.doQeuryInboundIf(inboundParam);
        Long endSelInbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_INBOUND end: [" + endSelInbound + "]");
        logger.info("+++++++++++++++ select TNT_IF_EXP_INBOUND times: [" + (endSelInbound - startSelInbound) + "]");

        logger.info("+++++++++++++++  inbound cnt: [" + ibList.size() + "]");
        for (IFInboundEntity inbound : ibList) {

            if (ibList.indexOf(inbound) % IntDef.INT_THOUSAND == 0 && ibList.indexOf(inbound) / IntDef.INT_THOUSAND != 0) {
                logger.info("+++++++++++++++  inbound : [" + ibList.indexOf(inbound) + "] records has been successful");
            }

            inbound.setCreatedDate(currSysDTForInbound);
            inbound.setUpdatedDate(currSysDTForInbound);

            IFInboundEntity existInbound = this.doQeuryInboundSSMSExist(inbound);

            if (existInbound == null) {
                if (inbound.getCancelFlag() != null && InboundSource.DELETE.equals(inbound.getCancelFlag())) {

                    /**
                     * delete, but the record not exist in table.
                     */
                    logger.warn("Delete data not found in TXT_EXP_DT (DT_NO: " + inbound.getDtNo() + ").");

                    // update handle flag
                    inbound.setHandleFlag(HandleFlag.DELETE_INBOUND_DATA_NOT_FOUND);
                    this.doUpdateInboundFlag(inbound);

                    continue;

                } else if (inbound.getCancelFlag() == null || InboundSource.ADD_MODIFY.equals(inbound.getCancelFlag())) {

                    /**
                     * insert
                     */
                    if (inbound.getExpInboundPlanDate() != null) {
                        Date date = new Date(DateTimeUtil.parseDate(inbound.getExpInboundPlanDate()).getTime());
                        inbound.setExpInPlanDate(date);
                    }

                    if (inbound.getExpInboundActualDate() != null) {
                        Date date = new Date(DateTimeUtil.parseDate(inbound.getExpInboundActualDate()).getTime());
                        inbound.setExpInActDate(date);
                    }

                    this.doInsertInboundSSMS(inbound);

                    TnfOrderStatus inboundStatus = new TnfOrderStatus();

                    inboundStatus.setImpPoNo(inbound.getImpPoNo());
                    inboundStatus.setExpPoNo(inbound.getExpPoNo());
                    inboundStatus.setCustomerOrderNo(inbound.getCustomerOrderNo());
                    inboundStatus.setPartsId(inbound.getPartsId());
                    inboundStatus.setSupplierId(inbound.getSupplierId());
                    inboundStatus.setUpdatedDate(currSysDTForInbound);

                    TnfOrderStatus inboundStatusOld = this.doQeuryOrderStatusExist(inboundStatus);

                    if (inboundStatusOld != null) {
                        inboundStatus.setCreatedBy(inbound.getCreatedBy());
                        inboundStatus.setUpdatedBy(inbound.getUpdatedBy());
                        inboundStatus.setOrderStatusId(inboundStatusOld.getOrderStatusId());
                        if (inbound.getQty() != null) {
                            inboundStatus.setExpInboundQty(inbound.getQty());
                        }

                        this.doUpdateOrderStatus(inboundStatus);
                    }
                } else {

                    /**
                     * Nothing
                     */
                    continue;
                }
            } else {
                if (inbound.getCancelFlag() != null && InboundSource.DELETE.equals(inbound.getCancelFlag())) {

                    /**
                     * delete
                     */
                    this.doDeleteInbound(inbound);

                    TnfOrderStatus inboundStatus = new TnfOrderStatus();

                    inboundStatus.setImpPoNo(inbound.getImpPoNo());
                    inboundStatus.setExpPoNo(inbound.getExpPoNo());
                    inboundStatus.setCustomerOrderNo(inbound.getCustomerOrderNo());
                    inboundStatus.setPartsId(inbound.getPartsId());
                    inboundStatus.setSupplierId(inbound.getSupplierId());

                    TnfOrderStatus oldOrderStatus = this.doQeuryOrderStatusExist(inboundStatus);

                    if (oldOrderStatus != null) {
                        inboundStatus.setExpInboundQty(DecimalUtil.subtract(BigDecimal.ZERO, inbound.getQty()));
                        inboundStatus.setCreatedBy(inbound.getCreatedBy());
                        inboundStatus.setUpdatedBy(inbound.getUpdatedBy());
                        inboundStatus.setUpdatedDate(currSysDTForInbound);
                        inboundStatus.setOrderStatusId(oldOrderStatus.getOrderStatusId());
                        this.doUpdateOrderStatus(inboundStatus);
                    }

                } else if (inbound.getCancelFlag() == null || InboundSource.ADD_MODIFY.equals(inbound.getCancelFlag())) {

                    /**
                     * update
                     */
                    if (inbound.getExpInboundPlanDate() != null) {
                        Date date = new Date(DateTimeUtil.parseDate(inbound.getExpInboundPlanDate()).getTime());
                        inbound.setExpInPlanDate(date);
                    }

                    if (inbound.getExpInboundActualDate() != null) {
                        Date date = new Date(DateTimeUtil.parseDate(inbound.getExpInboundActualDate()).getTime());
                        inbound.setExpInActDate(date);
                    }
                    this.doUpdateInboundSSMS(inbound);

                    TnfOrderStatus inboundStatus = new TnfOrderStatus();

                    inboundStatus.setImpPoNo(inbound.getImpPoNo());
                    inboundStatus.setExpPoNo(inbound.getExpPoNo());
                    inboundStatus.setCustomerOrderNo(inbound.getCustomerOrderNo());
                    inboundStatus.setPartsId(inbound.getPartsId());
                    inboundStatus.setSupplierId(inbound.getSupplierId());

                    TnfOrderStatus inboundStatusOld = this.doQeuryOrderStatusExist(inboundStatus);

                    if (inboundStatusOld != null) {

                        inboundStatus.setOrderStatusId(inboundStatusOld.getOrderStatusId());
                        inboundStatus.setUpdatedBy(inbound.getUpdatedBy());
                        inboundStatus.setUpdatedDate(currSysDTForInbound);
                        if (existInbound.getStatus() == INBOUND_STATUS_CANCELED) {
                            if (inbound.getQty() != null) {
                                inboundStatus.setExpInboundQty(inbound.getQty());
                            }
                        } else {
                            inboundStatus.setExpInboundQty(DecimalUtil.subtract(inbound.getQty(), existInbound.getQty()));
                        }
                        this.doUpdateOrderStatus(inboundStatus);
                    }

                } else {

                    /**
                     * Nothing
                     */
                    continue;
                }
            }

            // update handle flag
            inbound.setHandleFlag(HandleFlag.PROCESS_SUCCESS);

            this.doUpdateInboundFlag(inbound);
        }
        logger.info("+++++++++++++++  inbound : [" + ibList.size() + "] records has been successful");

        logger.debug("--------------- doInboundLogic end-------------");

        logger.debug("--------------- doOutboundLogic start-------------");
        Timestamp currSysDTForOutbound = this.getDBDateTimeByDefaultTimezone();
        IFOutboundEntity outboundParam = new IFOutboundEntity();
        outboundParam.setIfDateTime(param.getIfDateTime());
        outboundParam.setExpPartsIdList(param.getExpPartsId());

        Long startUDOutbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_OUTBOUND start: [" + startUDOutbound + "]");
        int outSystemNumber = this.baseMapper.update(this.getSqlId("modOutboundIfValidFlag"), outboundParam);
        int outSystemNumberForCancel = this.baseMapper.update(this.getSqlId("modOutboundIfValidFlagForCancel"), outboundParam);
        outSystemNumber = outSystemNumber + outSystemNumberForCancel;
        Long endUDOutbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_OUTBOUND end: [" + endUDOutbound + "]");
        logger.info("+++++++++++++++ update TNT_IF_EXP_OUTBOUND times: [" + (endUDOutbound - startUDOutbound) + "]");

        List<Integer> handleFlagParam3 = new ArrayList<Integer>();
        handleFlagParam3.add(HandleFlag.UNPROCESS);
        outboundParam.setHandleFlagParam(handleFlagParam3);

        // when new or modify
        Long startSelOutbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_OUTBOUND start: [" + startSelOutbound + "]");
        List<IFOutboundEntity> obList = this.doQeuryOutboundIf(outboundParam);
        Long endSelOutbound = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_OUTBOUND end: [" + endSelOutbound + "]");
        logger.info("+++++++++++++++ select TNT_IF_EXP_OUTBOUND times: [" + (endSelOutbound - startSelOutbound) + "]");

        logger.info("+++++++++++++++  outbound cnt: [" + obList.size() + "]");
        for (IFOutboundEntity outbound : obList) {
            if (obList.indexOf(outbound) % IntDef.INT_THOUSAND == 0 && obList.indexOf(outbound) / IntDef.INT_THOUSAND != 0) {
                logger.info("+++++++++++++++  outbound : [" + obList.indexOf(outbound)
                        + "] records has been successful");
            }

            outbound.setCreatedDate(currSysDTForOutbound);
            outbound.setUpdatedDate(currSysDTForOutbound);

            if (outbound.getStatus().intValue() == INBOUND_STATUS_CANCELED) {
                /**
                 * Inbound data has been deleted.
                 */
                logger.warn("The inbound data has been deleted (MODULE_NO: " + outbound.getModuleNo() + ").");

                // update handle flag
                outbound.setHandleFlag(HandleFlag.LOGIC_CHECK_NG);
                this.doUpdateOutboundFlag(outbound);

                continue;
            }

            outbound.setCreatedDate(currSysDTForOutbound);
            outbound.setUpdatedDate(currSysDTForOutbound);
            outbound.setExpOutActDate(DateTimeUtil.parseDate(outbound.getExpOutboundActualDate(),
                DateTimeUtil.FORMAT_YYYYMMDD));
            /** modify for outbound cancel by cheng_xingfei at 20161201 start */
            if (!OutboundSource.DELETE.equals(outbound.getCancelFlag())) {
                outbound.setExpInPlanDate(DateTimeUtil.parseDate(outbound.getExpInboundPlanDate(),
                    DateTimeUtil.FORMAT_YYYYMMDD));
                outbound.setQty(new BigDecimal(outbound.getOutboundQty()));
            }
            /** modify for outbound cancel by cheng_xingfei at 20161201 end */

            TnfOrderStatus outboundStatus = new TnfOrderStatus();

            if (OutboundSource.ADD_MODIFY.equals(outbound.getCancelFlag()) || outbound.getCancelFlag() == null) {
                // check exist
                IFOutboundEntity existOutbound = this.doQeuryOutboundSSMSExist(outbound);

                if (existOutbound != null) {

                    if (existOutbound.getStatus() != IpStatus.EXP_OUTBOUND) {
                        /**
                         * exist, but the record status is not 1 (Exp Outbound).
                         */
                        logger.warn("IP_NO: [" + existOutbound.getIpNo()
                                + "] has already in import stock. Can not update");

                        // update handle flag
                        outbound.setHandleFlag(HandleFlag.UPDATE_OB_DATA_IN_IMPORT_STOCK);
                        this.doUpdateOutboundFlag(outbound);

                        continue;
                    } else {
                        /**
                         * update
                         */
                        // update TNT_IP table
                        this.doUpdateOutboundSSMS(outbound);
                        
                        if (existOutbound.getStatus() != null && existOutbound.getStatus().intValue() != IpStatus.CANCELLED) {
                            outboundStatus.setExpInboundQty(DecimalUtil.subtract(existOutbound.getQty(),
                                outbound.getQty()));
                            outboundStatus.setExpOutboundQty(DecimalUtil.subtract(outbound.getQty(),
                                existOutbound.getQty()));
                        } else {
                            outboundStatus.setExpInboundQty(DecimalUtil.subtract(BigDecimal.ZERO, outbound.getQty()));
                            outboundStatus.setExpOutboundQty(outbound.getQty());
                        }
                    }
                } else {
                    /**
                     * insert
                     */
                    this.doInsertOutboundSSMS(outbound);

                    this.doInsertOutboundDT(outbound);
                    outboundStatus.setExpInboundQty(DecimalUtil.subtract(BigDecimal.ZERO, outbound.getQty()));
                    outboundStatus.setExpOutboundQty(outbound.getQty());
                }

                outboundStatus.setImpPoNo(outbound.getImpPoNo());
                outboundStatus.setExpPoNo(outbound.getExpPoNo());
                outboundStatus.setCustomerOrderNo(outbound.getCustomerOrderNo());
                outboundStatus.setPartsId(outbound.getPartsId());
                outboundStatus.setSupplierId(outbound.getSupplierId());

                TnfOrderStatus outboundStatusOld = this.doQeuryOrderStatusExist(outboundStatus);

                if (outboundStatusOld != null) {
                    outboundStatus.setCreatedBy(outbound.getCreatedBy());
                    outboundStatus.setUpdatedBy(outbound.getUpdatedBy());
                    outboundStatus.setUpdatedDate(currSysDTForOutbound);
                    outboundStatus.setOrderStatusId(outboundStatusOld.getOrderStatusId());

                    this.doUpdateOrderStatus(outboundStatus);
                }

            } else if (OutboundSource.DELETE.equals(outbound.getCancelFlag())) {
                /**
                 * delete
                 */
                IFOutboundEntity existOutbound = this.doQeuryOutboundIPExist(outbound);
                if (existOutbound == null) {
                    /**
                     * delete, but the record not exist in table.
                     */
                    logger.warn("Delete data not found in TNT_IP (MODULE_NO: " + outbound.getModuleNo() + ").");

                    // update handle flag
                    outbound.setHandleFlag(HandleFlag.DELETE_OUTBOUND_DATA_NOT_FOUND);
                    this.doUpdateOutboundFlag(outbound);

                    continue;
                } else {

                    if (existOutbound.getStatus() != IpStatus.EXP_OUTBOUND) {
                        /**
                         * delete, but the record status is not 1 (Exp Outbound).
                         */
                        logger.warn("MODULE_NO: [" + outbound.getModuleNo()
                                + "] has already in import stock. Can not delete");

                        // update handle flag
                        outbound.setHandleFlag(HandleFlag.UPDATE_OB_DATA_IN_IMPORT_STOCK);
                        this.doUpdateOutboundFlag(outbound);

                        continue;
                    } else {
                        // sum qty
                        IFOutboundEntity totalQtyOutbound = this.doSumOutboundQty(outbound);
                        BigDecimal totalQty = BigDecimal.ZERO;
                        if (totalQtyOutbound != null) {
                            totalQty = totalQtyOutbound.getTotalQty();
                        }

                        outboundStatus.setImpPoNo(outbound.getImpPoNo());
                        outboundStatus.setExpPoNo(outbound.getExpPoNo());
                        outboundStatus.setCustomerOrderNo(outbound.getCustomerOrderNo());
                        outboundStatus.setPartsId(outbound.getPartsId());
                        outboundStatus.setSupplierId(outbound.getSupplierId());

                        TnfOrderStatus outboundStatusOld = this.doQeuryOrderStatusExist(outboundStatus);

                        if (outboundStatusOld != null) {
                            outboundStatus.setCreatedBy(outbound.getCreatedBy());
                            outboundStatus.setUpdatedBy(outbound.getUpdatedBy());
                            outboundStatus.setUpdatedDate(currSysDTForOutbound);
                            outboundStatus.setOrderStatusId(outboundStatusOld.getOrderStatusId());
                            outboundStatus.setExpInboundQty(totalQty);
                            outboundStatus.setExpOutboundQty(DecimalUtil.subtract(BigDecimal.ZERO, totalQty));

                            this.doUpdateOrderStatus(outboundStatus);
                        }

                        this.doUpdateOutboundCancle(outbound);
                    }
                }
            } else {
                /**
                 * Nothing
                 */
                continue;
            }

            outbound.setHandleFlag(HandleFlag.PROCESS_SUCCESS);

            this.doUpdateOutboundFlag(outbound);
        }
        logger.info("+++++++++++++++  outbound : [" + obList.size() + "] records has been successful");

        logger.debug("--------------- doOutboundLogic end-------------");

        logger.debug("--------------- doInvoiceLogic start-------------");

        Timestamp currSysDTForInvoice = this.getDBDateTimeByDefaultTimezone();
        IFInvoiceEntity invoiceParam = new IFInvoiceEntity();
        invoiceParam.setIfDateTime(param.getIfDateTime());
        invoiceParam.setExpPartsIdList(param.getExpPartsId());
        Long startUDInvoice = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_INVOICE start: [" + startUDInvoice + "]");
        int invSystemNumber = this.baseMapper.update(this.getSqlId("modInvoiceIfValidFlag"), invoiceParam);
        Long endUDInvoice = System.currentTimeMillis();
        logger.info("+++++++++++++++ update TNT_IF_EXP_INVOICE end: [" + endUDInvoice + "]");
        logger.info("+++++++++++++++ update TNT_IF_EXP_INVOICE times: [" + (endUDInvoice - startUDInvoice) + "]");

        List<Integer> handleFlagParam4 = new ArrayList<Integer>();
        handleFlagParam4.add(HandleFlag.UNPROCESS);
        invoiceParam.setHandleFlagParam(handleFlagParam4);

        Long startSelInvoice = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_INVOICE start: [" + startSelInvoice + "]");
        List<IFInvoiceEntity> allList = this.doQeuryInvoiceIf(invoiceParam);
        Long endSelInvoice = System.currentTimeMillis();
        logger.info("+++++++++++++++ select TNT_IF_EXP_INVOICE end: [" + endSelInvoice + "]");
        logger.info("+++++++++++++++ select TNT_IF_EXP_INVOICE times: [" + (endSelInvoice - startSelInvoice) + "]");
        List<IFInvoiceEntity> validList = new ArrayList<IFInvoiceEntity>();

        if (allList != null && allList.size() > 0) {

            logger.info("+++++++++++++++  all invoice cnt: [" + allList.size() + "]");
            Map<String, String> invoiceKeyMap = new HashMap<String, String>();
            Map<String, String> ipKeyMap = new HashMap<String, String>();
            for (IFInvoiceEntity invoice : allList) {
                String invoiceKey = new StringBuffer().append(invoice.getMainRoute()).append("|#&")
                    .append(invoice.getInvoiceNo()).append("|#&").append(invoice.getContainerNo()).append("|#&")
                    .append(invoice.getModuleNo()).append("|#&").append(invoice.getImpPoNo()).append("|#&")
                    .append(invoice.getCustomerOrderNo()).append("|#&").append(invoice.getExpPoNo()).append("|#&")
                    .append(invoice.getPartsId()).append("|#&").append(invoice.getSupplierId()).append("|#&")
                    .append(invoice.getExpPartsId()).toString();

                /** modify at 2017/02/13 by cheng_xingfei start */
                List<IFInvoiceEntity> duplicateList = this.baseMapper.select(this.getSqlId("checkInvoiceDataExist"), invoice);

                // String ipKey = invoice.getModuleNo();
                // if (invoiceKeyMap.containsKey(invoiceKey)) {
                /** modify at 2017/03/28 by cheng_xingfei start */
                // String ipKey = new StringBuffer().append(invoice.getModuleNo()).append("|#&").append(invoice.getExpPartsId()).toString();
                String ipKey = new StringBuffer().append(invoice.getModuleNo()).append("|#&")
                    .append(invoice.getExpPartsId()).append("|#&").append(invoice.getImpPoNo()).append("|#&")
                    .append(invoice.getCustomerOrderNo()).append("|#&").append(invoice.getExpPoNo()).toString();
                /** modify at 2017/03/28 by cheng_xingfei end */
                if (invoiceKeyMap.containsKey(invoiceKey) || (duplicateList != null && duplicateList.size() > 0)) {
                /** modify at 2017/02/13 by cheng_xingfei end */
                    invoice.setHandleFlag(HandleFlag.INVOICE_NO_IS_EXIST);
                    this.doUpdateInvoiceFlag(invoice);
                    continue;
                } else {
                    invoiceKeyMap.put(invoiceKey, invoiceKey);
                }
                if (allList.indexOf(invoice) % IntDef.INT_THOUSAND == 0 && allList.indexOf(invoice) / IntDef.INT_THOUSAND != 0) {
                    logger.info("+++++++++++++++  invoice check: [" + allList.indexOf(invoice)
                            + "] records has been successful");
                }
                IFInvoiceEntity invoiceSummary = this.doQeuryInvoiceSummaryExist(invoice);
                invoice.setUpdatedDate(currSysDTForInvoice);
                invoice.setCreatedDate(currSysDTForInvoice);

                if (invoiceSummary != null && !StringUtil.isEmpty(invoiceSummary.getUploadId())) {
                    logger.warn("InvoiceNo: [" + invoice.getInvoiceNo() + "] is exist in TNT_INVOICE_SUMMARY");

                    // update handle flag
                    invoice.setHandleFlag(HandleFlag.INVOICE_NO_IS_EXIST);
                    this.doUpdateInvoiceFlag(invoice);
                    continue;
                } else {

                    // if (invoiceSummary != null) {
                    // List<IFInvoiceEntity> moduleList = this.baseMapper.select(this.getSqlId("checkModuleExist"),
                    // invoice);
                    // if (moduleList != null && !moduleList.isEmpty()) {
                    // continue;
                    // }
                    // }

                    if (invoiceSummary != null && StringUtil.isEmpty(invoiceSummary.getUploadId())) {
                        invoice.setInvoiceSummaryId(invoiceSummary.getInvoiceSummaryId());
                        invoice.setInvoiceStatus(invoiceSummary.getInvoiceStatus());
                    }

                    invoice.setCreatedDate(currSysDTForInvoice);
                    invoice.setUpdatedDate(currSysDTForInvoice);
                    invoice.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                    invoice.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);

                    // update Tnt_Ip to set the invoiceNo
                    if (!ipKeyMap.containsKey(ipKey)) {
                        this.doUpdateInvoiceIp(invoice);
                        ipKeyMap.put(ipKey, ipKey);
                    }

                    if (invoice.getTransportMode() != null && !"".equals(invoice.getTransportMode())) {
                        if ("S".equals(invoice.getTransportMode())) {
                            invoice.setTansportMode(TransportMode.SEA);
                        }

                        if ("A".equals(invoice.getTransportMode())) {
                            invoice.setTansportMode(TransportMode.AIR);
                        }
                    }

                    if (invoice.getBlDate() != null && DateTimeUtil.parseDate(invoice.getBlDate()) != null) {
                        invoice.setBLDateTime(DateTimeUtil.parseDate(invoice.getBlDate()));
                    }

                    if (!StringUtil.isEmpty(invoice.getEtdStr())) {
                        invoice.setEtd(DateTimeUtil.parseDate(invoice.getEtdStr()));
                        invoice.setChainStartDate(invoice.getEtd());
                    }

                    if (!StringUtil.isEmpty(invoice.getEtaStr())) {
                        invoice.setEta(DateTimeUtil.parseDate(invoice.getEtaStr()));
                    }

                    // get SUPPLIER_CODE_SET
                    getSupplierCodeList(invoice);
                    validList.add(invoice);
                }
            }
            logger.info("+++++++++++++++  invoice check: [" + allList.size() + "] records has been successful");
        }

        if (validList.size() > 0) {

            // Call common function to get the list of VANNING_DATE, IMP_INB_PLAN_DATE.
            logger.info("+++++++++++++++  invoice merge start.");
            supplyChainService.prepareSupplyChain(validList, ChainStep.INVOICE, BusinessPattern.V_V, false);
            List<IFInvoiceEntity> mergeInvoiceList = getMergeInvoiceList(validList);
            logger.info("+++++++++++++++  invoice merge end.");

            Map<String, Integer> invoiceSummaryMap = new HashMap<String, Integer>();
            Map<String, String> invoiceContainerMap = new HashMap<String, String>();
            Map<String, Integer> invoiceMap = new HashMap<String, Integer>();
            String summaryKey = "";
            String containerKey = "";
            String invoiceKey = "";

            logger.info("+++++++++++++++  invoice cnt: [" + mergeInvoiceList.size() + "]");
            
            Map<String, Integer> summaryStatusMap = new HashMap<String, Integer>();
            for (IFInvoiceEntity invoice : mergeInvoiceList) {
                if (mergeInvoiceList.indexOf(invoice) % IntDef.INT_THOUSAND == 0 && mergeInvoiceList.indexOf(invoice) / IntDef.INT_THOUSAND != 0) {
                    logger.info("+++++++++++++++  invoice summary/invoice/invoice parts : ["
                            + mergeInvoiceList.indexOf(invoice) + "] records has been successful");
                }

                summaryKey = invoice.getInvoiceNo();
                invoiceKey = invoice.getInvoiceNo();

                Integer dbInvoiceSummaryId = invoice.getInvoiceSummaryId();

                if (dbInvoiceSummaryId != null) {
                    /**
                     * update TNT_INVOICE_SUMMARY table's INVOICE_QTY
                     */
                    if(summaryStatusMap.containsKey(summaryKey)) {
                        invoice.setInvoiceStatus(summaryStatusMap.get(summaryKey));
                    } else {
                        summaryStatusMap.put(summaryKey, InvoiceStatus.PENDING);
                    }
                    invoice.setInvoiceSummaryId(dbInvoiceSummaryId);
                    invoice.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    this.doUpdateInvoiceSummary(invoice);
                    

                    invoiceSummaryMap.put(summaryKey, dbInvoiceSummaryId);

                    /**
                     * query all invoice of this invoice summary
                     */
                    TntInvoice paramInvoice = new TntInvoice();
                    paramInvoice.setInvoiceSummaryId(dbInvoiceSummaryId);
                    List<TntInvoice> allInvoiceList = this.baseMapper.select(this.getSqlId("getAllInvoiceList"),
                        paramInvoice);

                    if (invoice.getInvoiceStatus() == null
                            || (invoice.getInvoiceStatus() != null
                                    && invoice.getInvoiceStatus().intValue() != InvoiceStatus.INBOUND_COMPLETED && invoice
                                .getInvoiceStatus().intValue() != InvoiceStatus.CANCELLED)) {
                        for (TntInvoice currInvoice : allInvoiceList) {
                            /**
                             * insert into TNT_INVOICE_PARTS
                             */
                            invoice.setInvoiceId(currInvoice.getInvoiceId());
                            if (currInvoice.getImpInbActualDate() != null) {
                                invoice.setOriginalQty(invoice.getTotalQty());
                                invoice.setQty(BigDecimal.ZERO);
                            } else {
                                invoice.setOriginalQty(invoice.getTotalQty());
                                invoice.setQty(invoice.getTotalQty());
                            }
                            this.doInsertOrUpdateTntInvoiceParts(invoice);
                        }
                    } else {
                        if (invoice.getInvoiceStatus().intValue() == InvoiceStatus.INBOUND_COMPLETED) {
                            for (int i = 0; i < allInvoiceList.size(); i++) {
                                TntInvoice currInvoice = allInvoiceList.get(i);
                                /**
                                 * update original parts's Qty
                                 */
                                if (i == allInvoiceList.size() - 1) {
                                    TntInvoicePart parts = new TntInvoicePart();
                                    parts.setInvoiceId(currInvoice.getInvoiceId());
                                    parts.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                                    parts.setUpdatedDate(currSysDTForInvoice);
                                    this.baseMapper.update(this.getSqlId("modOriginalInvoice"), parts);
                                }

                                /**
                                 * insert new part into all old invoice
                                 */
                                invoice.setInvoiceId(currInvoice.getInvoiceId());
                                invoice.setImpInbActualDate(currInvoice.getImpInbActualDate());
                                invoice.setOriginalQty(invoice.getTotalQty());
                                invoice.setQty(BigDecimal.ZERO);
                                this.doInsertOrUpdateTntInvoiceParts(invoice);
                            }
                            
                            Integer invoiceId = this.baseMapper.getNextSequence("SEQ_TNT_INVOICE");
                            invoice.setInvoiceId(invoiceId);
                            invoice.setInvoiceSummaryId(dbInvoiceSummaryId);

                            /**
                             * insert all original parts into new invoice
                             */
                            this.baseMapper.insert(this.getSqlId("addTntInvoicePartsForOriginal"), invoice);

                            /**
                             * insert a new invoice
                             */
                            invoice.setOriginalVersion(allInvoiceList.get(allInvoiceList.size() - 1)
                                .getRevisionVersion());
                            invoice.setRevisionVersion(allInvoiceList.get(allInvoiceList.size() - 1)
                                .getRevisionVersion() + 1);
                            List<TntInvoice> existInvoieList = this.doInsertOrUpdateTntInvoice(invoice, true);

                            /**
                             * insert new parts into new invoice
                             */
                            if (existInvoieList == null || existInvoieList.size() == 0) {
                                invoice.setInvoiceId(invoiceId);
                            } else {
                                invoice.setInvoiceId(existInvoieList.get(0).getInvoiceId());
                            }
                            invoice.setOriginalQty(invoice.getTotalQty());
                            invoice.setQty(invoice.getTotalQty());
                            this.doInsertOrUpdateTntInvoiceParts(invoice);
                        }
                    }
                } else {

                    if (!invoiceSummaryMap.containsKey(summaryKey)) {
                        /**
                         * insert into TNT_INVOICE_SUMMARY
                         */
                        Integer invoiceSummaryId = this.baseMapper.getNextSequence("SEQ_TNT_INVOICE_SUMMARY");
                        invoice.setUploadedBy(ChinaPlusConst.BATCH_USER_ID);
                        invoice.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                        invoice.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        invoice.setInvoiceSummaryId(invoiceSummaryId);
                        this.doInsertInvoiceSummary(invoice);

                        invoiceSummaryMap.put(summaryKey, invoiceSummaryId);
                    } else {
                        /**
                         * update TNT_INVOICE_SUMMARY table's INVOICE_QTY
                         */
                        invoice.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                        this.doUpdateInvoiceSummaryByInvoiceNo(invoice);
                    }

                    if (!invoiceMap.containsKey(invoiceKey)) {
                        /**
                         * insert into TNT_INVOICE
                         */
                        Integer invoiceId = this.baseMapper.getNextSequence("SEQ_TNT_INVOICE");
                        invoice.setInvoiceId(invoiceId);
                        invoice.setInvoiceSummaryId(invoiceSummaryMap.get(invoice.getInvoiceNo()));
                        invoice.setOriginalVersion(0);
                        invoice.setRevisionVersion(1);
                        this.doInsertOrUpdateTntInvoice(invoice, false);

                        invoiceMap.put(invoiceKey, invoiceId);
                    }

                    /**
                     * insert new parts into new invoice
                     */
                    invoice.setInvoiceId(invoiceMap.get(invoiceKey));
                    invoice.setOriginalQty(invoice.getTotalQty());
                    invoice.setQty(invoice.getTotalQty());
                    this.doInsertOrUpdateTntInvoiceParts(invoice);
                }

                TnfOrderStatus orderStatus = new TnfOrderStatus();
                orderStatus.setImpPoNo(invoice.getImpPoNo());
                orderStatus.setCustomerOrderNo(invoice.getCustomerOrderNo());
                orderStatus.setExpPoNo(invoice.getExpPoNo());
                orderStatus.setPartsId(invoice.getPartsId());
                orderStatus.setSupplierId(invoice.getSupplierId());
                orderStatus.setExpOutboundQty(DecimalUtil.subtract(BigDecimal.ZERO, invoice.getTotalQty()));
                orderStatus.setExpOnshippingQty(invoice.getTotalQty());
                orderStatus.setCreatedBy(invoice.getCreatedBy());
                orderStatus.setUpdatedDate(currSysDTForInvoice);
                orderStatus.setUpdatedBy(invoice.getUpdatedBy());

                TnfOrderStatus orderStatusOld = this.doQeuryOrderStatusExist(orderStatus);
                if (orderStatusOld != null) {
                    orderStatus.setOrderStatusId(orderStatusOld.getOrderStatusId());
                    this.doUpdateOrderStatus(orderStatus);
                }
            }
            logger.info("+++++++++++++++  invoice summary/invoice/invoice parts : [" + mergeInvoiceList.size()
                    + "] records has been successful");

            for (IFInvoiceEntity invoice : validList) {
                if (validList.indexOf(invoice) % IntDef.INT_THOUSAND == 0 && validList.indexOf(invoice) / IntDef.INT_THOUSAND != 0) {
                    logger.info("+++++++++++++++  invoice container : [" + validList.indexOf(invoice)
                            + "] records has been successful");
                }

                containerKey = new StringBuffer().append(invoice.getContainerNo()).append("|#&")
                    .append(invoice.getModuleNo()).append("|#&").append(invoice.getPartsId()).toString();

                if (!invoiceContainerMap.containsKey(containerKey)) {
                    /**
                     * insert into TNT_INVOICE_CONTAINER
                     */
                    TntInvoiceContainer invoiceContainer = new TntInvoiceContainer();

                    invoiceContainer.setInvoiceSummaryId(invoiceSummaryMap.get(invoice.getInvoiceNo()));
                    invoiceContainer.setContainerNo(invoice.getContainerNo());

                    if (invoice.getTransportMode() != null && !"".equals(invoice.getTransportMode())) {
                        if ("S".equals(invoice.getTransportMode())) {
                            invoiceContainer.setSealNo("a");
                        }

                        if ("A".equals(invoice.getTransportMode())) {
                            invoiceContainer.setSealNo("o");
                        }
                    }

                    invoiceContainer.setModuleNo(invoice.getModuleNo());
                    invoiceContainer.setPartsId(invoice.getPartsId());
                    invoiceContainer.setQty(invoice.getTotalQty());
                    invoiceContainer.setStatus(1);
                    invoiceContainer.setCreatedBy(ChinaPlusConst.BATCH_USER_ID);
                    invoiceContainer.setCreatedDate(currSysDTForInvoice);
                    invoiceContainer.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    invoiceContainer.setUpdatedDate(currSysDTForInvoice);
                    this.doInsertTntInvoiceContainer(invoiceContainer);
                    invoiceContainerMap.put(containerKey, containerKey);
                } else {
                    // add for repeat containerNo, moduleNo, partsId, different's ipo, cpo, epo
                    TntInvoiceContainer invoiceContainer = new TntInvoiceContainer();
                    invoiceContainer.setInvoiceSummaryId(invoiceSummaryMap.get(invoice.getInvoiceNo()));
                    invoiceContainer.setContainerNo(invoice.getContainerNo());
                    invoiceContainer.setModuleNo(invoice.getModuleNo());
                    invoiceContainer.setPartsId(invoice.getPartsId());
                    invoiceContainer.setQty(invoice.getTotalQty());
                    invoiceContainer.setUpdatedBy(ChinaPlusConst.BATCH_USER_ID);
                    invoiceContainer.setUpdatedDate(currSysDTForInvoice);
                    this.doUpdateTntInvoiceContainer(invoiceContainer);
                }

                invoice.setHandleFlag(HandleFlag.PROCESS_SUCCESS);

                this.doUpdateInvoiceFlag(invoice);
            }
            logger.info("+++++++++++++++  invoice container : [" + validList.size() + "] records has been successful");

            /**
             * Update shipping status info
             */
            invoiceAdjService.doInvoiceAdjLogic(currSysDTForInvoice, null);
        }

        logger.debug("--------------- doInvoiceLogic end-------------");

        // send mail
        if (sendMailList.size() > 0 && sendEmailService.getMailSendFlag()) {
            sendMail(sendMailList, isOnLine);
        }

        if (sendMailList.size() > 0) {
            throw new BatchException();
        }

        resultMap.put("executeResult", 1);
        resultMap.put("isSystemDataNum",
            Integer.valueOf(ocSystemNumber + odSystemNumber + inSystemNumber + outSystemNumber + invSystemNumber));

        return resultMap;

    }

    /**
     * Send mail.<br>
     * 
     * @param ssmsMailList List<SsmsMailEntity>
     * @param isOnLine boolean
     */
    private void sendMail(List<SsmsMailEntity> ssmsMailList, boolean isOnLine) {
        try {
            // Get office and ssmsCustomerCode map (key: officeId,officeCode; value: ssmsCustomerCode list)
            Map<String, List<SsmsMailEntity>> sentOfficeMap = new HashMap<String, List<SsmsMailEntity>>();
            for (SsmsMailEntity entity : ssmsMailList) {
                int officeId = entity.getOfficeId();
                String officeCode = entity.getOfficeCode();
                String timeZone = entity.getTimeZone();
                String key = new StringBuffer().append(officeId).append(StringConst.COMMA).append(officeCode)
                    .append(StringConst.COMMA).append(timeZone).toString();
                List<SsmsMailEntity> list = sentOfficeMap.get(key);
                if (list == null) {
                    List<SsmsMailEntity> mailList = new ArrayList<SsmsMailEntity>();
                    mailList.add(entity);
                    sentOfficeMap.put(key, mailList);
                } else {
                    list.add(entity);
                    sentOfficeMap.put(key, list);
                }
            }

            // Loop office and ssmsCustomerCode map and send mail
            for (Map.Entry<String, List<SsmsMailEntity>> entry : sentOfficeMap.entrySet()) {
                String[] officeInfo = entry.getKey().split(StringConst.COMMA);
                int officeId = Integer.parseInt(officeInfo[IntDef.INT_ZERO]);
                String officeCode = officeInfo[IntDef.INT_ONE];
                String timeZone = officeInfo[IntDef.INT_TWO];
                List<SsmsMailEntity> mailList = entry.getValue();

                List<SsmsMailEntity> sendMailList = new ArrayList<SsmsMailEntity>();
                Map<String, String> keyMap = new HashMap<String, String>();
                String key = "";
                for (SsmsMailEntity se : mailList) {
                    key = new StringBuffer().append(se.getTtcCustomerCode()).append("|#&").append(se.getImpPoNo())
                        .append("|#&").append(se.getExpSoDate()).append("|#&").append(se.getShippingRouteCode())
                        .toString();
                    if (!keyMap.containsKey(key)) {
                        sendMailList.add(se);
                        keyMap.put(key, key);
                    }
                }

                // Get mail to address
                BaseParam paramForUser = new BaseParam();
                paramForUser.setSwapData("officeId", officeId);
                List<TnmUser> userList = baseMapper.select(this.getSqlId("getSendMailObject"), paramForUser);

                StringBuffer sbTo = new StringBuffer();
                for (TnmUser user : userList) {
                    String mailAddr = user.getMailAddr();
                    if (!StringUtils.isBlank(mailAddr)) {
                        if (sbTo.length() != 0) {
                            sbTo.append(StringConst.COMMA);
                        }
                        sbTo.append(user.getMailAddr());
                    }
                }
                String to = sbTo.toString();

                // Get mail title
                String title = MessageManager.getMessage("CPIIFB01_Batch_MailTitle_ShippingRoute",
                    Language.CHINESE.getLocale());
                String mailBodyTtcCustomerCode = MessageManager.getMessage("CPIIFB01_Batch_MailBody_TtcCustomerCode",
                    Language.CHINESE.getLocale());
                String mailBodyImpPoNo = MessageManager.getMessage("CPIIFB01_Batch_MailBody_ImpPoNo",
                    Language.CHINESE.getLocale());
                String mailBodyExpSoDate = MessageManager.getMessage("CPIIFB01_Batch_MailBody_ExpSoDate",
                    Language.CHINESE.getLocale());
                String mailBodyShippingRouteCode = MessageManager.getMessage(
                    "CPIIFB01_Batch_MailBody_ShippingRouteCode", Language.CHINESE.getLocale());
                Timestamp tDate = this.getDBDateTime(timeZone);
                String date = DateTimeUtil.formatDate(tDate);
                title = title.replaceAll("\\{0\\}", date);

                // Get mail body
                String templatePath = StringUtil.formatMessage("{0}/{1}.txt", "/templates-batch",
                    MailUtil.MAIL_TEMPLATE_SHIPPING_ROUTE);
                String body = MailUtil.getTemplate(templatePath);
                body = body.replaceAll("\\{0\\}", officeCode);
                StringBuffer mailInfo = new StringBuffer();
                mailInfo.append("<table border='1' style='border-collapse:collapse;text-align:center'>");
                mailInfo.append("<tr style='background-color:#FFFF00'><td>");
                mailInfo.append(mailBodyTtcCustomerCode);
                mailInfo.append("</td><td>");
                mailInfo.append(mailBodyImpPoNo);
                mailInfo.append("</td><td>");
                mailInfo.append(mailBodyExpSoDate);
                mailInfo.append("</td><td>");
                mailInfo.append(mailBodyShippingRouteCode);
                mailInfo.append("</td></tr>");
                for (SsmsMailEntity mailDetail : sendMailList) {
                    mailInfo.append("<tr><td>");
                    mailInfo.append(mailDetail.getTtcCustomerCode());
                    mailInfo.append("</td><td>");
                    mailInfo.append(mailDetail.getImpPoNo());
                    mailInfo.append("</td><td>");
                    mailInfo.append(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD_SOLIDUS,
                        mailDetail.getExpSoDate()));
                    mailInfo.append("</td><td>");
                    mailInfo.append(mailDetail.getShippingRouteCode());
                    mailInfo.append("</td></tr>");
                }
                mailInfo.append("</table>");
                body = body.replaceAll("\\{1\\}", mailInfo.toString());

                // Send mail
                if (isOnLine) {
                    MailUtilForOnline mailForOnline = new MailUtilForOnline(to, title, body, null, true, null);
                    mailForOnline.run();
                } else {
                    MailUtil mail = new MailUtil();
                    mail.sendMailForBatch(to, title, body, null, true, null);
                }
            }
        } catch (Exception e) {
            logger.error("get mail template failed!");
            e.getStackTrace();
        }
    }

    /**
     * getMergeInvoiceList
     * 
     * @param validList List<IFInvoiceEntity>
     * @return List<IFInvoiceEntity>
     */
    private List<IFInvoiceEntity> getMergeInvoiceList(List<IFInvoiceEntity> validList) {

        List<IFInvoiceEntity> mergeInvoiceList = new ArrayList<IFInvoiceEntity>();

        String mergeKey = "";

        Map<String, IFInvoiceEntity> keyMap = new HashMap<String, IFInvoiceEntity>();

        // String invoiceKey = "";
        // Map<String, BigDecimal> invoiceQtyMap = new HashMap<String, BigDecimal>();

        List<String> keyList = new ArrayList<String>();
        for (IFInvoiceEntity invoice : validList) {

            IFInvoiceEntity copyInvoice = new IFInvoiceEntity();
            BeanUtils.copyProperties(invoice, copyInvoice);

            mergeKey = new StringBuffer().append(copyInvoice.getMainRoute()).append("|#&")
                .append(copyInvoice.getInvoiceNo()).append("|#&").append(copyInvoice.getImpPoNo()).append("|#&")
                .append(copyInvoice.getCustomerOrderNo()).append("|#&").append(copyInvoice.getExpPoNo()).append("|#&")
                .append(copyInvoice.getPartsId()).append("|#&").append(copyInvoice.getSupplierId()).append("|#&")
                .append(copyInvoice.getExpPartsId()).toString();

            // invoiceKey = new StringBuffer().append(copyInvoice.getInvoiceNo()).append("|#&")
            // .append(copyInvoice.getImpPoNo()).append("|#&").append(copyInvoice.getCustomerOrderNo()).append("|#&")
            // .append(copyInvoice.getExpPoNo()).toString();

            // if (!invoiceQtyMap.containsKey(invoiceKey)) {
            // invoiceQtyMap.put(invoiceKey, invoice.getTotalQty());
            // } else {
            // BigDecimal invoiceQty = invoiceQtyMap.get(invoiceKey);
            // invoiceQty = DecimalUtil.add(invoiceQty, invoice.getTotalQty());
            // invoiceQtyMap.put(invoiceKey, invoiceQty);
            // }

            if (!keyMap.containsKey(mergeKey)) {
                keyMap.put(mergeKey, copyInvoice);
                keyList.add(mergeKey);
            } else {
                IFInvoiceEntity otherInvoice = keyMap.get(mergeKey);
                otherInvoice.setTotalQty(DecimalUtil.add(otherInvoice.getTotalQty(), invoice.getTotalQty()));
            }
        }
        for (String key : keyList) {
            mergeInvoiceList.add(keyMap.get(key));
        }

        // for (IFInvoiceEntity invoice : mergeInvoiceList) {
        // String key = StringUtil.toSafeString(invoice.getInvoiceNo());
        // invoice.setInvoiceTotalQty(invoiceQtyMap.get(key));
        // }
        return mergeInvoiceList;
    }

    /**
     * set supplierCodeSet
     * 
     * @param invoice Invoice
     */
    private void getSupplierCodeList(IFInvoiceEntity invoice) {
        List<IFInvoiceEntity> supplierCodeList = this.baseMapper.select(this.getSqlId("getSupplierCode"), invoice);
        if (supplierCodeList != null && supplierCodeList.size() > 1) {
            StringBuffer supplierCodeSet = new StringBuffer();
            for (int i = 0; i < supplierCodeList.size(); i++) {
                IFInvoiceEntity entity = supplierCodeList.get(i);
                if (i < supplierCodeList.size() - 1) {
                    supplierCodeSet = supplierCodeSet.append(entity.getSupplierCode()).append(",");
                } else {
                    supplierCodeSet = supplierCodeSet.append(entity.getSupplierCode());
                }
                invoice.setSupplierCode(supplierCodeSet.toString());
            }
        }
    }

    /**
     * doQeuryOrderCancelIf
     * 
     * @param orderCancel IFOrderCancelEntity
     * @return List<IFOrderEntity>
     */
    public List<IFOrderCancelEntity> doQeuryOrderCancelIf(IFOrderCancelEntity orderCancel) {

        return this.baseMapper.select(this.getSqlId("getOrderCancelIfList"), orderCancel);
    }

    /**
     * doOrderCancelDetailExist
     * 
     * @param orderCancel IFOrderCancelEntity
     * @return List<IFOrderEntity>
     */
    public IFOrderCancelEntity doOrderCancelDetailExist(IFOrderCancelEntity orderCancel) {
        List<IFOrderCancelEntity> orderCancelList = null;
        orderCancelList = this.baseMapper.select(this.getSqlId("checkOrderCancelDetailExist"), orderCancel);
        if (orderCancelList != null && !orderCancelList.isEmpty() && orderCancelList.get(0).getOrderDetailId() != null) {
            return orderCancelList.get(0);
        }
        return null;
    }

    /**
     * doOrderCancelStatusExist
     * 
     * @param orderStatus TnfOrderStatus
     * @return List<TnfOrderStatus>
     */
    public TnfOrderStatus doOrderCancelStatusExist(TnfOrderStatus orderStatus) {
        List<TnfOrderStatus> orderStatusList = new ArrayList<TnfOrderStatus>();
        orderStatusList = this.baseMapper.select(this.getSqlId("checkOrderCancelStatusExist"), orderStatus);
        if (orderStatusList != null && !orderStatusList.isEmpty() && orderStatusList.get(0) != null
                && orderStatusList.get(0).getOrderStatusId() != null) {
            return orderStatusList.get(0);
        }
        return null;
    }

    /**
     * doUpdateOrderCancelDetail
     * 
     * @param orderCancel IFOrderCancelEntity
     * @return List<IFOrderEntity>
     */
    public int doUpdateOrderCancelDetail(IFOrderCancelEntity orderCancel) {

        return this.baseMapper.update(this.getSqlId("modOrderCancelDetail"), orderCancel);
    }

    /**
     * doUpdateOrderStatus
     * 
     * @param orderStatus orderStatus
     * @return List<TnfOrderStatus>
     */
    public int doUpdateOrderStatus(TnfOrderStatus orderStatus) {

        return this.baseMapper.update(this.getSqlId("modOrderStatus"), orderStatus);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param orderCancel IFOrderCancelEntity
     * @return int
     */
    public int doUpdateOrderCancelHandleFlag(IFOrderCancelEntity orderCancel) {
        return this.baseMapper.update(this.getSqlId("modOrderCancelIfHandleFlag"), orderCancel);
    }

    /**
     * doQeuryOrderIf
     * 
     * @param order order
     * @return List<IFOrderEntity>
     */
    public List<IFOrderEntity> doQeuryOrderIf(IFOrderEntity order) {

        return this.baseMapper.select(this.getSqlId("getOrderIfList"), order);
    }

    /**
     * getMinExpInbPlanDateByOrder
     * 
     * @param order order
     * @return List<IFOrderEntity>
     */
    public List<IFOrderEntity> getMinExpInbPlanDateByOrder(IFOrderEntity order) {

        return this.baseMapper.select(this.getSqlId("getMinExpInbPlanDateByOrder"), order);
    }

    /**
     * doQeuryOrderIfExist
     * 
     * @param order order
     * @return List<IFOrderEntity>
     */
    public IFOrderEntity doQeuryOrderExist(IFOrderEntity order) {
        List<IFOrderEntity> orderList = null;
        orderList = this.baseMapper.select(this.getSqlId("checkOrderSSMSExist"), order);
        if (orderList != null && !orderList.isEmpty() && orderList.get(0) != null
                && orderList.get(0).getOrderId() != null) {
            return orderList.get(0);
        }

        return null;
    }

    /**
     * doUpdateOrderSSMS
     * 
     * @param order order
     * @return int
     */
    public int doUpdateOrderSSMS(IFOrderEntity order) {
        return this.baseMapper.update(this.getSqlId("modOrderSSMS"), order);
    }

    /**
     * doInsertOrderSSMS
     * 
     * @param order order
     * @return int
     */
    public int doInsertOrderSSMS(IFOrderEntity order) {
        return this.baseMapper.insert(this.getSqlId("addOrderSSMS"), order);
    }

    /**
     * doQeuryOrderDetailExist
     * 
     * @param order order
     * @return List<IFOrderEntity>
     */
    public IFOrderEntity doQeuryOrderDetailExist(IFOrderEntity order) {
        List<IFOrderEntity> orderList = null;
        orderList = this.baseMapper.select(this.getSqlId("checkOrderDetailExist"), order);
        if (orderList != null && !orderList.isEmpty() && orderList.get(0) != null
                && orderList.get(0).getOrderDetailId() != null) {
            return orderList.get(0);
        }
        return null;
    }

    /**
     * doUpdateOrderDetail
     * 
     * @param order order
     * @return int
     */
    public int doUpdateOrderDetail(IFOrderEntity order) {
        return this.baseMapper.update(this.getSqlId("modOrderDetail"), order);
    }

    /**
     * doInsertOrderDetail
     * 
     * @param order order
     * @return int
     */
    public int doInsertOrderDetail(IFOrderEntity order) {
        return this.baseMapper.insert(this.getSqlId("addOrderDetail"), order);
    }

    /**
     * doQeuryOrderStatusExist
     * 
     * @param orderStatus TnfOrderStatus
     * @return List<TnfOrderStatus>
     */
    public TnfOrderStatus doQeuryOrderStatusExist(TnfOrderStatus orderStatus) {
        List<TnfOrderStatus> orderList = null;
        orderList = this.baseMapper.select(this.getSqlId("checkOrderStatusExist"), orderStatus);
        if (orderList != null && !orderList.isEmpty() && orderList.get(0) != null
                && orderList.get(0).getOrderStatusId() != null) {
            return orderList.get(0);
        }
        return null;
    }

    /**
     * doInsertOrderStatus
     * 
     * @param orderStatus TnfOrderStatus
     * @return int
     */
    public int doInsertOrderStatus(TnfOrderStatus orderStatus) {
        return this.baseMapper.insert(this.getSqlId("addOrderStatus"), orderStatus);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param order order
     * @return int
     */
    public int doUpdateOrderHandleFlag(IFOrderEntity order) {
        return this.baseMapper.update(this.getSqlId("modOrderIfHandleFlag"), order);
    }

    /**
     * doQeuryInboundIf
     * 
     * @param Inbound Inbound
     * @return List<Inbound>
     */
    public List<IFInboundEntity> doQeuryInboundIf(IFInboundEntity Inbound) {
        return this.baseMapper.select(this.getSqlId("getInboundIfList"), Inbound);
    }

    /**
     * doQeuryInboundIfExist
     * 
     * @param Inbound Inbound
     * @return List<Inbound>
     */
    public IFInboundEntity doQeuryInboundSSMSExist(IFInboundEntity Inbound) {
        List<IFInboundEntity> inboundList = new ArrayList<IFInboundEntity>();
        inboundList = this.baseMapper.select(this.getSqlId("checkInboundSSMSExist"), Inbound);
        if (inboundList != null && !inboundList.isEmpty() && inboundList.get(0) != null) {
            return inboundList.get(0);
        }

        return null;
    }

    /**
     * doUpdateInboundSSMS
     * 
     * @param Inbound Inbound
     * @return int
     */
    public int doUpdateInboundSSMS(IFInboundEntity Inbound) {
        return this.baseMapper.update(this.getSqlId("modInboundSSMS"), Inbound);
    }

    /**
     * doUpdateInboundSSMS
     * 
     * @param Inbound Inbound
     * @return int
     */
    public int doDeleteInbound(IFInboundEntity Inbound) {
        return this.baseMapper.update(this.getSqlId("modInboundStatus"), Inbound);
    }

    /**
     * doInsertInboundSSMS
     * 
     * @param Inbound Inbound
     * @return int
     */
    public int doInsertInboundSSMS(IFInboundEntity Inbound) {
        return this.baseMapper.insert(this.getSqlId("addInboundSSMS"), Inbound);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param Inbound Inbound
     * @return int
     */
    public int doUpdateInboundFlag(IFInboundEntity Inbound) {
        return this.baseMapper.update(this.getSqlId("modInboundIfHandleFlag"), Inbound);
    }

    /**
     * doQeuryOutboundIf
     * 
     * @param Outbound Outbound
     * @return List<Outbound>
     */
    public List<IFOutboundEntity> doQeuryOutboundIf(IFOutboundEntity Outbound) {
        return this.baseMapper.select(this.getSqlId("getOutboundIfList"), Outbound);
    }

    /**
     * doQeuryOutboundIfExist
     * 
     * @param outbound outbound
     * @return List<Outbound>
     */
    public IFOutboundEntity doQeuryOutboundSSMSExist(IFOutboundEntity outbound) {
        List<IFOutboundEntity> outboundList = new ArrayList<IFOutboundEntity>();
        outboundList = this.baseMapper.select(this.getSqlId("checkOutboundSSMSExist"), outbound);
        if (outboundList != null && !outboundList.isEmpty() && outboundList.get(0) != null) {
            return outboundList.get(0);
        }

        return null;
    }

    /**
     * doQeuryOutboundIPExist
     * 
     * @param outbound outbound
     * @return List<IFOrderEntity>
     */
    public IFOutboundEntity doQeuryOutboundIPExist(IFOutboundEntity outbound) {
        List<IFOutboundEntity> outboundList = null;
        outboundList = this.baseMapper.select(this.getSqlId("checkOutboundIPExist"), outbound);
        if (outboundList != null && !outboundList.isEmpty() && outboundList.size() > 0 && outboundList.get(0) != null) {
            return outboundList.get(0);
        } else {
            return null;
        }
    }

    /**
     * doUpdateOutboundSSMS
     * 
     * @param outbound outbound
     * @return int
     */
    public int doUpdateOutboundSSMS(IFOutboundEntity outbound) {
        return this.baseMapper.update(this.getSqlId("modOutboundSSMS"), outbound);
    }

    /**
     * doUpdateOutboundCancle
     * 
     * @param outbound outbound
     * @return int
     */
    public int doUpdateOutboundCancle(IFOutboundEntity outbound) {
        return this.baseMapper.update(this.getSqlId("modOutboundCancel"), outbound);
    }

    /**
     * doInsertOutboundSSMS
     * 
     * @param outbound outbound
     * @return int
     */
    public int doInsertOutboundSSMS(IFOutboundEntity outbound) {
        return this.baseMapper.insert(this.getSqlId("addOutboundSSMS"), outbound);
    }

    /**
     * doSumOutboundQty
     * 
     * @param outbound outbound
     * @return int
     */
    public IFOutboundEntity doSumOutboundQty(IFOutboundEntity outbound) {
        List<IFOutboundEntity> outboundList = this.baseMapper.select(this.getSqlId("outboundSumQty"), outbound);
        if (outboundList != null && !outboundList.isEmpty() && outboundList.get(0) != null) {
            return outboundList.get(0);
        }

        return null;
    }

    /**
     * doInsertOutboundDT
     * 
     * @param outbound outbound
     * @return int
     */
    public int doInsertOutboundDT(IFOutboundEntity outbound) {
        return this.baseMapper.insert(this.getSqlId("addOutboundDT"), outbound);
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param outbound outbound
     * @return int
     */
    public int doUpdateOutboundFlag(IFOutboundEntity outbound) {
        return this.baseMapper.update(this.getSqlId("modOutboundIfHandleFlag"), outbound);
    }

    /**
     * doQeuryInvoiceIf
     * 
     * @param invoice invoice
     * @return List<Invoice>
     */
    public List<IFInvoiceEntity> doQeuryInvoiceIf(IFInvoiceEntity invoice) {
        return this.baseMapper.select(this.getSqlId("getInvoiceIfList"), invoice);
    }

    /**
     * doQeuryInvoiceSummaryExist
     * 
     * @param invoice invoice
     * @return Integer
     */
    public IFInvoiceEntity doQeuryInvoiceSummaryExist(IFInvoiceEntity invoice) {
        List<IFInvoiceEntity> invoiceList = null;
        invoiceList = this.baseMapper.select(this.getSqlId("checkInvoiceSummaryExist"), invoice);
        if (invoiceList != null && !invoiceList.isEmpty()) {
            return invoiceList.get(0);
        } else {
            return null;
        }
    }

    /**
     * doUpdateInvoiceIp
     * 
     * @param invoice invoice
     * @return int
     */
    public int doUpdateInvoiceIp(IFInvoiceEntity invoice) {
        return this.baseMapper.update(this.getSqlId("modInvoiceIp"), invoice);
    }

    /**
     * doUpdateInvoiceSummary
     * 
     * @param invoice invoice
     * @return int
     */
    public int doUpdateInvoiceSummary(IFInvoiceEntity invoice) {
        return this.baseMapper.update(this.getSqlId("modInvoiceSummary"), invoice);
    }

    /**
     * doUpdateInvoiceSummaryByInvoiceNo
     * 
     * @param invoice invoice
     * @return int
     */
    public int doUpdateInvoiceSummaryByInvoiceNo(IFInvoiceEntity invoice) {
        return this.baseMapper.update(this.getSqlId("modInvoiceSummaryByInvoiceNo"), invoice);
    }

    /**
     * doInsertInvoiceSummary
     * 
     * @param invoice invoice
     * @return int
     */
    public int doInsertInvoiceSummary(IFInvoiceEntity invoice) {
        return this.baseMapper.insert(this.getSqlId("addInvoiceSummary"), invoice);
    }

    /**
     * doInsertOrUpdateTntInvoice
     * 
     * @param invoice invoice
     * @param isCheckExists boolean
     * @return List<TntInvoice>
     */
    public List<TntInvoice> doInsertOrUpdateTntInvoice(IFInvoiceEntity invoice, boolean isCheckExists) {
        if (!isCheckExists) {
            this.baseMapper.insert(this.getSqlId("addTntInvoice"), invoice);
            return null;
        }

        TntInvoice paramInvoice = new TntInvoice();
        paramInvoice.setInvoiceNo(invoice.getInvoiceNo());
        paramInvoice.setEtd(invoice.getEtd());
        paramInvoice.setEta(invoice.getEta());
        // paramInvoice.setCcDate(invoice.getImpPlanCustomDate());
        // paramInvoice.setImpInbPlanDate(invoice.getImpInbPlanDate());
        // paramInvoice.setImpInbActualDate(invoice.getImpInbActualDate());
        List<TntInvoice> checkInvoiceList = this.baseMapper.select(this.getSqlId("checkInvoiceExists"), paramInvoice);

        if (checkInvoiceList == null || checkInvoiceList.size() == 0) {
            this.baseMapper.insert(this.getSqlId("addTntInvoice"), invoice);
            return null;
        } else {
            return checkInvoiceList;
        }
    }

    /**
     * doInsertTntInvoiceContainer
     * 
     * @param invoiceContainer TntInvoiceContainer
     * @return int
     */
    public int doInsertTntInvoiceContainer(TntInvoiceContainer invoiceContainer) {
        return this.baseMapper.insert(this.getSqlId("addTntInvoiceContainer"), invoiceContainer);
    }

    /**
     * doUpdateTntInvoiceContainer
     * 
     * @param invoiceContainer TntInvoiceContainer
     * @return int
     */
    public int doUpdateTntInvoiceContainer(TntInvoiceContainer invoiceContainer) {
        return this.baseMapper.update(this.getSqlId("modTntInvoiceContainer"), invoiceContainer);
    }

    /**
     * doInsertTntInvoiceParts
     * 
     * @param invoice invoice
     * @return int
     */
    public int doInsertOrUpdateTntInvoiceParts(IFInvoiceEntity invoice) {
        TntInvoicePart paramInvoicePart = new TntInvoicePart();
        paramInvoicePart.setInvoiceId(invoice.getInvoiceId());
        paramInvoicePart.setPartsId(invoice.getPartsId());
        paramInvoicePart.setSupplierId(invoice.getSupplierId());
        paramInvoicePart.setImpPoNo(invoice.getImpPoNo());
        paramInvoicePart.setCustomerOrderNo(invoice.getCustomerOrderNo());
        paramInvoicePart.setExpPoNo(invoice.getExpPoNo());
        List<TntInvoicePart> checkInvoicePartsList = this.baseMapper.select(this.getSqlId("checkInvoicePartsExists"),
            paramInvoicePart);

        if (checkInvoicePartsList == null || checkInvoicePartsList.size() == 0) {
            return this.baseMapper.insert(this.getSqlId("addTntInvoiceParts"), invoice);
        } else {
            return this.baseMapper.update(this.getSqlId("updateTntInvoiceParts"), invoice);
        }
    }

    /**
     * doUpdateHandleFlag
     * 
     * @param invoice invoice
     * @return int
     */
    public int doUpdateInvoiceFlag(IFInvoiceEntity invoice) {
        return this.baseMapper.update(this.getSqlId("modInvoiceIfHandleFlag"), invoice);
    }
}
