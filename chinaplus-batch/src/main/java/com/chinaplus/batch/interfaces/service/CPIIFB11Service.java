/**
 * CPIIFB11Service.java
 * 
 * @screen CPIIFB11
 * @author liu_yinchuan
 */
package com.chinaplus.batch.interfaces.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.interfaces.bean.TnfBalanceByDayEx;
import com.chinaplus.batch.interfaces.bean.TnfExpPartsStatusEx;
import com.chinaplus.batch.interfaces.bean.TnfImpStockByDayEx;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.entity.TnfBalanceByDay;
import com.chinaplus.common.entity.TnfExpPartsStatus;
import com.chinaplus.common.entity.TnfImpStockByDay;
import com.chinaplus.common.entity.TntBatchJob;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * Service of inventory by day batch..
 * 
 * @author liu_yinchuan
 */
public class CPIIFB11Service extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB11Service.class);

    /**
     * prepare all data for each parts supplier.
     * 
     * @param dayInfoList dayInfoList
     * @param partsStatusMap partsStatusMap
     */
    public void prepareDayPartsStatusList(List<TnfExpPartsStatusEx> dayInfoList,
        Map<String, Map<Date, Map<String, TnfExpPartsStatusEx>>> partsStatusMap) {

        // get map
        Map<Date, Map<String, TnfExpPartsStatusEx>> dayPartsStatusMap = null;
        Map<String, TnfExpPartsStatusEx> partsOrderStatusMap = null;
        StringBuffer partsKey = new StringBuffer();
        for (TnfExpPartsStatusEx dayInfo : dayInfoList) {

            // reset parts key
            partsKey.setLength(IntDef.INT_ZERO);

            // prepare key
            partsKey.append(dayInfo.getPartsId());
            partsKey.append(StringConst.NEW_COMMA);
            partsKey.append(dayInfo.getSupplierId());

            // get day map
            if (!partsStatusMap.containsKey(partsKey.toString())) {
                dayPartsStatusMap = new HashMap<Date, Map<String, TnfExpPartsStatusEx>>();
                partsStatusMap.put(partsKey.toString(), dayPartsStatusMap);
            } else {
                dayPartsStatusMap = partsStatusMap.get(partsKey.toString());
            }

            // if not exist in map
            if (!dayPartsStatusMap.containsKey(dayInfo.getEndDate())) {
                partsOrderStatusMap = new HashMap<String, TnfExpPartsStatusEx>();
                dayPartsStatusMap.put(dayInfo.getEndDate(), partsOrderStatusMap);
            } else {
                partsOrderStatusMap = dayPartsStatusMap.get(dayInfo.getEndDate());
            }

            // get order information
            if (!partsOrderStatusMap.containsKey(dayInfo.getOrderNo())) {
                // set into map
                partsOrderStatusMap.put(dayInfo.getOrderNo(), dayInfo);
            } else {
                // reset values
                TnfExpPartsStatusEx oldPartsStaus = partsOrderStatusMap.get(dayInfo.getOrderNo());
                // prepare balance
                if (dayInfo.getDayOrderQty() != null) {
                    oldPartsStaus.setDayOrderQty(dayInfo.getDayOrderQty());
                }
                if (dayInfo.getForeCompletedQty() != null) {
                    oldPartsStaus.setForeCompletedQty(dayInfo.getForeCompletedQty());
                }
                if (dayInfo.getDayExpPlanInboundQty() != null) {
                    oldPartsStaus.setDayExpPlanInboundQty(dayInfo.getDayExpPlanInboundQty());
                }
                if (dayInfo.getPrePlanInboundQty() != null) {
                    oldPartsStaus.setPrePlanInboundQty(dayInfo.getPrePlanInboundQty());
                }
                if (dayInfo.getDayExpInboundQty() != null) {
                    oldPartsStaus.setDayExpInboundQty(dayInfo.getDayExpInboundQty());
                }
                if (dayInfo.getPreInboundQty() != null) {
                    oldPartsStaus.setPreInboundQty(dayInfo.getPreInboundQty());
                }
                if (dayInfo.getDayInvoiceQty() != null) {
                    oldPartsStaus.setDayInvoiceQty(dayInfo.getDayInvoiceQty());
                }
                if (dayInfo.getDayDicReceivedQty() != null) {
                    oldPartsStaus.setDayDicReceivedQty(dayInfo.getDayDicReceivedQty());
                }
            }
        }
    }

    /**
     * prepare all data for each parts supplier.
     * 
     * @param partsOrderStatusMap partsOrderStatusMap
     * @param partsStatusMap partsStatusMap
     * @param directInvoiceMap directInvoiceMap
     * @param expInboundMap expInboundMap
     * @param expPlanInboundMap expPlanInboundMap
     * @param preInvoiceMap preInvoiceMap
     */
    public void mergeDayPartsStatusList(Map<String, Map<Date, Map<String, TnfExpPartsStatusEx>>> partsOrderStatusMap,
        Map<String, Map<Date, TnfExpPartsStatusEx>> partsStatusMap,
        Map<String, Map<String, BigDecimal>> directInvoiceMap, Map<String, Map<String, BigDecimal>> expInboundMap,
        Map<String, Map<String, BigDecimal>> expPlanInboundMap, Map<String, Map<String, BigDecimal>> preInvoiceMap) {

        // get bs Date
        Date bsDate = DateTimeUtil.parseDate(ConfigManager.getBatchStartDate());

        // get map
        for (String partskey : partsOrderStatusMap.keySet()) {

            // get value
            Map<Date, Map<String, TnfExpPartsStatusEx>> dayOrderStatusMap = partsOrderStatusMap.get(partskey);
            Map<Date, TnfExpPartsStatusEx> dayPartsStatusMap = new HashMap<Date, TnfExpPartsStatusEx>();
            // get map
            Map<String, BigDecimal> orderInboundMap = expInboundMap.get(partskey);
            if (orderInboundMap == null) {
                orderInboundMap = new HashMap<String, BigDecimal>();
            }
            Map<String, BigDecimal> orderPlanInboundMap = expPlanInboundMap.get(partskey);
            if (orderPlanInboundMap == null) {
                orderPlanInboundMap = new HashMap<String, BigDecimal>();
            }
            Map<String, BigDecimal> orderDirectMap = directInvoiceMap.get(partskey);
            if (orderDirectMap == null) {
                orderDirectMap = new HashMap<String, BigDecimal>();
            }
            Map<String, BigDecimal> invoiceMap = preInvoiceMap.get(partskey);
            if (invoiceMap == null) {
                invoiceMap = new HashMap<String, BigDecimal>();
            }

            // sort dates
            List<Date> dateList = new ArrayList<Date>(dayOrderStatusMap.keySet());
            Collections.sort(dateList);

            // prepare date
            for (Date endDate : dateList) {

                // defined
                TnfExpPartsStatusEx expPartsInfo = null;
                // get map
                Map<String, TnfExpPartsStatusEx> orderStatusMap = dayOrderStatusMap.get(endDate);

                // set into map
                for (TnfExpPartsStatusEx orderInfo : orderStatusMap.values()) {

                    // prepare order information
                    // if has fore complete
                    if (DecimalUtil.isGreater(orderInfo.getForeCompletedQty(), BigDecimal.ZERO)) {

                        // set balance and plan delay as zero
                        orderInfo.setExpBalanceQty(BigDecimal.ZERO);
                        orderInfo.setExpPlanDelayQty(BigDecimal.ZERO);
                    } else {

                        if (orderInfo.getBusinessPattern().equals(BusinessPattern.V_V)) {
                            // get current order QTY
                            BigDecimal orderQty = orderInfo.getDayOrderQty();

                            // get plan in-bound QTY
                            BigDecimal ttPlanInboundQty = orderPlanInboundMap.get(orderInfo.getOrderNo());
                            if (ttPlanInboundQty == null) {
                                ttPlanInboundQty = orderInfo.getPrePlanInboundQty();
                            }
                            ttPlanInboundQty = DecimalUtil.add(ttPlanInboundQty, orderInfo.getDayExpPlanInboundQty());
                            orderPlanInboundMap.put(orderInfo.getOrderNo(), ttPlanInboundQty);
                            if (bsDate != null && bsDate.equals(endDate)) {
                                orderInfo.setDayExpPlanInboundQty(ttPlanInboundQty);
                            }

                            // get actual in-bound QTY
                            BigDecimal ttInboundQty = orderInboundMap.get(orderInfo.getOrderNo());
                            if (ttInboundQty == null) {
                                ttInboundQty = orderInfo.getPreInboundQty();
                            }
                            ttInboundQty = DecimalUtil.add(ttInboundQty, orderInfo.getDayExpInboundQty());
                            orderInboundMap.put(orderInfo.getOrderNo(), ttInboundQty);
                            if (bsDate != null && bsDate.equals(endDate)) {
                                orderInfo.setDayExpInboundQty(ttInboundQty);
                            }

                            // direct QTY
                            BigDecimal ttDirectQty = orderDirectMap.get(orderInfo.getOrderNo());
                            ttDirectQty = DecimalUtil.add(ttDirectQty, orderInfo.getDayDicReceivedQty());
                            orderDirectMap.put(orderInfo.getOrderNo(), ttDirectQty);
                            if (bsDate != null && bsDate.equals(endDate)) {
                                orderInfo.setDayDicReceivedQty(ttDirectQty);
                            }

                            // prepare export balance QTY
                            BigDecimal expBalanceQty = DecimalUtil.subtract(orderQty, ttInboundQty);
                            expBalanceQty = DecimalUtil.subtract(expBalanceQty, ttDirectQty);
                            if (DecimalUtil.isGreater(BigDecimal.ZERO, expBalanceQty)) {
                                // get delay qty
                                expBalanceQty = BigDecimal.ZERO;
                            }
                            // set into entity
                            orderInfo.setExpBalanceQty(expBalanceQty);

                            // prepare day export inbound plan qty
                            BigDecimal dayDelayQty = DecimalUtil.subtract(orderInfo.getDayExpPlanInboundQty(),
                                orderInfo.getDayExpInboundQty());
                            dayDelayQty = DecimalUtil.subtract(dayDelayQty, ttDirectQty);
                            if (DecimalUtil.isGreater(BigDecimal.ZERO, dayDelayQty)) {
                                // get delay qty
                                dayDelayQty = BigDecimal.ZERO;
                            }
                            // set into info
                            orderInfo.setExpPlanDelayQty(dayDelayQty);
                        } else {
                            // get current order QTY
                            BigDecimal orderQty = orderInfo.getDayOrderQty();
                            // direct QTY
                            BigDecimal ttInvoiceQty = orderDirectMap.get(orderInfo.getOrderNo());
                            ttInvoiceQty = DecimalUtil.add(ttInvoiceQty, orderInfo.getDayInvoiceQty());
                            orderDirectMap.put(orderInfo.getOrderNo(), ttInvoiceQty);
                            if (bsDate != null && bsDate.equals(endDate)) {
                                orderInfo.setDayInvoiceQty(ttInvoiceQty);
                            }
                            // set into entity
                            BigDecimal expBalanceQty = DecimalUtil.subtract(orderQty, ttInvoiceQty);
                            if (DecimalUtil.isGreater(BigDecimal.ZERO, expBalanceQty)) {
                                // get delay qty
                                expBalanceQty = BigDecimal.ZERO;
                            }
                            orderInfo.setExpBalanceQty(expBalanceQty);
                        }
                    }

                    // if vv set export stock qty
                    if (orderInfo.getBusinessPattern().equals(BusinessPattern.V_V)) {

                        // get invocie qty
                        if (bsDate != null && bsDate.equals(endDate)) {
                            BigDecimal ttInvoiceQty = invoiceMap.get(orderInfo.getOrderNo());
                            ttInvoiceQty = DecimalUtil.add(ttInvoiceQty, orderInfo.getDayInvoiceQty());
                            orderInfo.setDayInvoiceQty(ttInvoiceQty);
                        }

                        // day active stock
                        BigDecimal activeOnshippingQty = DecimalUtil.subtract(orderInfo.getDayInvoiceQty(),
                            orderInfo.getDayDicReceivedQty());
                        // prepare day stock qty
                        BigDecimal dayExpStockQty = DecimalUtil.subtract(orderInfo.getDayExpInboundQty(),
                            activeOnshippingQty);
                        orderInfo.setExpStockQty(dayExpStockQty);
                    }

                    // prepare export on-shipping qty
                    orderInfo.setExpOnshippingQty(DecimalUtil.subtract(orderInfo.getDayInvoiceQty(),
                        orderInfo.getDayImpReceivedQty()));
                    orderInfo.setDayOnshippingQty(orderInfo.getDayInvoiceQty());

                    // do merge
                    if (expPartsInfo == null) {
                        // set as
                        expPartsInfo = orderInfo;
                    } else {
                        // merge
                        expPartsInfo.setExpBalanceQty(DecimalUtil.add(expPartsInfo.getExpBalanceQty(),
                            orderInfo.getExpBalanceQty()));
                        expPartsInfo.setExpPlanDelayQty(DecimalUtil.add(expPartsInfo.getExpPlanDelayQty(),
                            orderInfo.getExpPlanDelayQty()));
                        expPartsInfo.setExpStockQty(DecimalUtil.add(expPartsInfo.getExpStockQty(),
                            orderInfo.getExpStockQty()));
                        expPartsInfo.setExpOnshippingQty(DecimalUtil.add(expPartsInfo.getExpOnshippingQty(),
                            orderInfo.getExpOnshippingQty()));
                        expPartsInfo.setDayOnshippingQty(DecimalUtil.add(expPartsInfo.getDayOnshippingQty(),
                            orderInfo.getDayOnshippingQty()));
                    }
                }

                // set into map
                dayPartsStatusMap.put(endDate, expPartsInfo);
            }

            // put into map
            partsStatusMap.put(partskey, dayPartsStatusMap);
        }
    }

    /**
     * prepare all data for each parts supplier.
     * 
     * @param dayInfoList dayInfoList
     * @param directInvoiceMap directInvoiceMap
     */
    public void prepareAreadyInboundInfoList(List<TnfExpPartsStatusEx> dayInfoList,
        Map<String, Map<String, BigDecimal>> directInvoiceMap) {

        // get date
        Map<String, BigDecimal> orderInvoiceMap = new HashMap<String, BigDecimal>();

        // get map
        StringBuffer partsKey = new StringBuffer();
        for (TnfExpPartsStatusEx dayInfo : dayInfoList) {

            // reset parts key
            partsKey.setLength(IntDef.INT_ZERO);

            // prepare key
            partsKey.append(dayInfo.getPartsId());
            partsKey.append(StringConst.NEW_COMMA);
            partsKey.append(dayInfo.getSupplierId());

            // get map
            if (directInvoiceMap.containsKey(partsKey.toString())) {
                orderInvoiceMap = directInvoiceMap.get(partsKey.toString());
            } else {
                orderInvoiceMap = new HashMap<String, BigDecimal>();
                directInvoiceMap.put(partsKey.toString(), orderInvoiceMap);
            }

            // prepare
            if (dayInfo.getPreDicReceivedQty() != null) {
                orderInvoiceMap.put(dayInfo.getOrderNo(), dayInfo.getPreDicReceivedQty());
            }
            // prepare
            if (dayInfo.getPreInboundQty() != null) {
                orderInvoiceMap.put(dayInfo.getOrderNo(), dayInfo.getPreInboundQty());
            }
            // prepare
            if (dayInfo.getPrePlanInboundQty() != null) {
                orderInvoiceMap.put(dayInfo.getOrderNo(), dayInfo.getPrePlanInboundQty());
            }
            // prepare
            if (dayInfo.getPreInvoiceQty() != null) {
                orderInvoiceMap.put(dayInfo.getOrderNo(), dayInfo.getPreInvoiceQty());
            }
        }
    }

    /**
     * prepare all data for each parts supplier.
     * 
     * @param dayInfoList dayInfoList
     * @param partsStatusMap partsStatusMap
     * @param stockDate stock date
     */
    public void prepareExistPartsStatusList(List<TnfExpPartsStatusEx> dayInfoList,
        Map<String, Map<Date, TnfExpPartsStatusEx>> partsStatusMap, Date stockDate) {

        // get map
        Map<Date, TnfExpPartsStatusEx> dayPartsStatusMap = null;
        StringBuffer partsKey = new StringBuffer();
        for (TnfExpPartsStatusEx dayInfo : dayInfoList) {

            // reset parts key
            partsKey.setLength(IntDef.INT_ZERO);

            // prepare key
            partsKey.append(dayInfo.getPartsId());
            partsKey.append(StringConst.NEW_COMMA);
            partsKey.append(dayInfo.getSupplierId());
            // if same
            dayPartsStatusMap = partsStatusMap.get(partsKey.toString());

            // get day map
            if (dayPartsStatusMap == null) {
                dayPartsStatusMap = new HashMap<Date, TnfExpPartsStatusEx>();
                partsStatusMap.put(partsKey.toString(), dayPartsStatusMap);
            }

            // get end date
            Date endDate = dayInfo.getEndDate();
            if (endDate == null) {
                endDate = stockDate;
            }

            // if not exist in map
            dayPartsStatusMap.put(endDate, dayInfo);
        }
    }

    /**
     * Prepare day information for parts status.
     * 
     * @param dayInfoMap day information map
     * @param existPartsStatusMap parts status Map
     * @param dbTime current time
     * @param minEndDate min end date
     * @param endDate end date
     * 
     * @return parts status list
     */
    public List<TnfExpPartsStatus> prepareExpPartsStatusList(Map<String, Map<Date, TnfExpPartsStatusEx>> dayInfoMap,
        Map<String, Map<Date, TnfExpPartsStatusEx>> existPartsStatusMap, Timestamp dbTime, Date minEndDate, Date endDate) {

        // defined
        List<TnfExpPartsStatus> newPartsStatusList = new ArrayList<TnfExpPartsStatus>();
        Map<Date, TnfExpPartsStatusEx> partStatusMap = null;
        TnfExpPartsStatus tempPartsStatus = null;
        TnfExpPartsStatus prePartsStatus = null;
        TnfExpPartsStatus newPartsStatus = null;
        Map<Date, TnfExpPartsStatusEx> existPartStatusMap = null;

        // loop exist parts status
        for (String mapKey : existPartsStatusMap.keySet()) {

            // get map from map
            existPartStatusMap = existPartsStatusMap.get(mapKey);
            partStatusMap = dayInfoMap.get(mapKey);
            // set default values
            if (partStatusMap == null) {
                partStatusMap = new HashMap<Date, TnfExpPartsStatusEx>();
            }

            // get min end date of current parts
            List<Date> existDateList = new ArrayList<Date>(existPartStatusMap.keySet());
            Collections.sort(existDateList);
            List<Date> newDateList = new ArrayList<Date>(partStatusMap.keySet());
            Collections.sort(newDateList);

            // check loop date
            Date minExistDate = existDateList.get(IntDef.INT_ZERO);
            // get the oldest Date
            Date minNewDate = !newDateList.isEmpty() ? newDateList.get(IntDef.INT_ZERO) : null;

            // prepare loop start date
            Date loopDate = minNewDate;
            if (loopDate == null || loopDate.after(minExistDate)) {
                loopDate = minExistDate;
            }
            if (minEndDate.after(loopDate)) {
                loopDate = minEndDate;
            }

            // reset
            tempPartsStatus = existPartStatusMap.get(minExistDate);
            prePartsStatus = existPartStatusMap.get(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
            while (!loopDate.after(endDate)) {

                // does not has prePartsStatus
                if (prePartsStatus == null) {
                    // prepare new parts status
                    newPartsStatus = this.createNewPartsStatus(tempPartsStatus, dbTime, loopDate);
                    // set all qty to zero
                    newPartsStatus.setExpBalanceQty(BigDecimal.ZERO);
                    newPartsStatus.setExpOnshippingQty(BigDecimal.ZERO);
                    newPartsStatus.setExpPlanDelayQty(BigDecimal.ZERO);
                    newPartsStatus.setExpStockQty(BigDecimal.ZERO);
                    newPartsStatus.setExpOnshippingQty(BigDecimal.ZERO);
                } else {
                    // check current date is exist or not
                    newPartsStatus = this.createNewPartsStatus(prePartsStatus, dbTime, loopDate);
                }

                // check is exist parts status in database
                TnfExpPartsStatus existPartsStatus = existPartStatusMap.get(loopDate);
                // if exist, set parts status id
                if (existPartsStatus != null && existPartsStatus.getPartsStatusId() != null) {
                    // set parts status id
                    newPartsStatus.setPartsStatusId(existPartsStatus.getPartsStatusId());
                    // set create date and version
                    newPartsStatus.setCreatedBy(existPartsStatus.getCreatedBy());
                    newPartsStatus.setCreatedDate(existPartsStatus.getCreatedDate());
                    if (existPartsStatus.getVersion() == null) {
                        newPartsStatus.setVersion(IntDef.INT_ONE);
                    } else {
                        newPartsStatus.setVersion(existPartsStatus.getVersion().intValue() + IntDef.INT_ONE);
                    }
                } else {
                    // set create date and version
                    newPartsStatus.setCreatedBy(BatchConst.BATCH_USER_ID);
                    newPartsStatus.setCreatedDate(dbTime);
                    newPartsStatus.setVersion(IntDef.INT_ONE);
                }

                // get parts information
                TnfExpPartsStatusEx dayInfo = partStatusMap.get(loopDate);
                if (dayInfo != null) {
                    // reset
                    newPartsStatus.setExpBalanceQty(dayInfo.getExpBalanceQty());
                    newPartsStatus.setExpPlanDelayQty(dayInfo.getExpPlanDelayQty());
                    newPartsStatus.setExpStockQty(DecimalUtil.add(newPartsStatus.getExpStockQty(),
                        dayInfo.getExpStockQty()));
                    newPartsStatus.setExpOnshippingQty(DecimalUtil.add(newPartsStatus.getExpOnshippingQty(),
                        dayInfo.getExpOnshippingQty()));
                    newPartsStatus.setDayOnshippingQty(DecimalUtil.add(newPartsStatus.getDayOnshippingQty(),
                        dayInfo.getDayOnshippingQty()));
                }

                // reset
                prePartsStatus = newPartsStatus;

                // set into list
                newPartsStatusList.add(newPartsStatus);

                // next date
                loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            }
        }

        // return parts list
        return newPartsStatusList;
    }

    /**
     * Create new Parts Status.
     * 
     * @param source parts status
     * @param currentTime data date time
     * @param endDate end date
     * @return parts status information
     */
    private TnfExpPartsStatus createNewPartsStatus(TnfExpPartsStatus source, Timestamp currentTime, Date endDate) {

        // create new
        TnfExpPartsStatus effctPartsStatus = new TnfExpPartsStatus();
        effctPartsStatus.setPartsId(source.getPartsId());
        effctPartsStatus.setSupplierId(source.getSupplierId());
        effctPartsStatus.setOfficeId(source.getOfficeId());
        effctPartsStatus.setEndDate(endDate);
        effctPartsStatus.setBusinessPattern(source.getBusinessPattern());
        effctPartsStatus.setExpPlanDelayQty(source.getExpPlanDelayQty());
        effctPartsStatus.setExpBalanceQty(source.getExpBalanceQty());
        effctPartsStatus.setExpOnshippingQty(source.getExpOnshippingQty());
        effctPartsStatus.setExpStockQty(source.getExpStockQty());
        effctPartsStatus.setDayOnshippingQty(BigDecimal.ZERO);
        effctPartsStatus.setUpdatedBy(BatchConst.BATCH_USER_ID);
        effctPartsStatus.setUpdatedDate(currentTime);

        // return
        return effctPartsStatus;
    }

    /**
     * get imp received information.
     * 
     * @param partsStatusList parts status list
     * @throws BusinessException exception
     */
    public void doSavePartsStatusListIntoDb(List<TnfExpPartsStatus> partsStatusList) throws BusinessException {

        // get max parts status id
        int maxPsId = this.getMaxPartsStatusId();
        // save into database
        for (TnfExpPartsStatus partsStatus : partsStatusList) {

            // check exist?
            if (partsStatus.getPartsStatusId() != null) {

                // update
                super.baseDao.update(partsStatus);
            } else {

                // set id
                partsStatus.setPartsStatusId(maxPsId++);

                // save into database
                super.baseDao.insert(partsStatus);
            }
        }
    }

    /**
     * save imp stock information list into database.
     * 
     * @param impStockList import stock list
     * @param officeId exist import stock list
     * @param lastEndDate last end Date
     * @param minEndDate min end date
     * @param endDate end Date
     * @throws BusinessException exception
     */
    public void doSaveImpStockListIntoDb(List<TnfImpStockByDayEx> impStockList, Integer officeId, Date lastEndDate,
        Date minEndDate, Date endDate) throws BusinessException {

        // define
        Map<String, Map<Date, TnfImpStockByDayEx>> impStockMap = new HashMap<String, Map<Date, TnfImpStockByDayEx>>();
        Map<String, Map<Date, TnfImpStockByDayEx>> extImpStockMap = new HashMap<String, Map<Date, TnfImpStockByDayEx>>();
        Map<Date, TnfImpStockByDayEx> dayImpStockMap = null;
        Map<Date, TnfImpStockByDayEx> dayExtImpStockMap = null;
        List<TnfImpStockByDay> impStockInfoList = new ArrayList<TnfImpStockByDay>();
        TnfImpStockByDay impStockInfo = null;

        // get timestamp
        Timestamp dbTime = this.getDBDateTimeByDefaultTimezone();
        // define
        Date loopDate = null;

        // prepare parameter
        TnfImpStockByDayEx copyParam = new TnfImpStockByDayEx();
        copyParam.setOfficeId(officeId);
        copyParam.setCreatedBy(BatchConst.BATCH_USER_ID);
        copyParam.setCreatedDate(dbTime);
        copyParam.setUpdatedBy(BatchConst.BATCH_USER_ID);
        copyParam.setUpdatedDate(dbTime);
        // copyParam.setLastEndDate(lastEndDate);
        copyParam.setLastEndDate(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
        copyParam.setEndDate(endDate);
        // get list
        //List<TnfImpStockByDay> copyImpStockList = this.getExistImpStockListByDate(copyParam);
        // insert
        this.prepareImpstockByEachDate(copyParam, lastEndDate, endDate);
        // clear
        //copyImpStockList = null;

        // if no data
        if (impStockList.isEmpty()) {
            return;
        }

        // new map
        this.prepareDayImpStockList(impStockList, impStockMap);
        // clear 
        impStockList.clear();

        // get them from database
        TnfImpStockByDayEx impStockParam = new TnfImpStockByDayEx();
        impStockParam.setOfficeId(officeId);
        impStockParam.setLastEndDate(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
        impStockParam.setEndDate(endDate);

        // get all exist imp stock list
        List<TnfImpStockByDayEx> existImpStockList = this.getAllExistImpStockList(impStockParam);
        this.prepareDayImpStockList(existImpStockList, extImpStockMap);
        // clear
        existImpStockList = null;

        // loop exist parts status
        for (String mapKey : impStockMap.keySet()) {

            // get map from map
            dayImpStockMap = impStockMap.get(mapKey);
            dayExtImpStockMap = extImpStockMap.get(mapKey);
            // set default values
            if (dayExtImpStockMap == null) {
                dayExtImpStockMap = new HashMap<Date, TnfImpStockByDayEx>();
            }

            // get min end date of current parts
            List<Date> existDateList = new ArrayList<Date>(dayExtImpStockMap.keySet());
            Collections.sort(existDateList);
            List<Date> newDateList = new ArrayList<Date>(dayImpStockMap.keySet());
            Collections.sort(newDateList);

            // check loop date
            Date minExistDate = !existDateList.isEmpty() ? existDateList.get(IntDef.INT_ZERO) : null;
            // get the oldest Date
            Date minNewDate = newDateList.get(IntDef.INT_ZERO);

            // prepare loop start date
            loopDate = minNewDate;
            if (minExistDate != null && minExistDate.before(loopDate)) {
                loopDate = minExistDate;
            }
            if (loopDate.before(minEndDate)) {
                loopDate = minEndDate;
            }

            // reset
            TnfImpStockByDayEx tempImpStock = dayImpStockMap.get(minNewDate);
            TnfImpStockByDay preImpStock = dayExtImpStockMap.get(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
            while (!loopDate.after(endDate)) {

                // does not has prePartsStatus
                if (preImpStock == null) {
                    // prepare new parts status
                    impStockInfo = this.createNewImpStock(tempImpStock, dbTime, loopDate, true);
                } else {
                    // check current date is exist or not
                    impStockInfo = this.createNewImpStock(preImpStock, dbTime, loopDate, false);
                }

                // check is exist parts status in database
                TnfImpStockByDayEx existImpStock = dayExtImpStockMap.get(loopDate);
                this.modifyImpStockInfo(impStockInfo, existImpStock, dbTime);

                // get parts information
                TnfImpStockByDayEx dayInfo = dayImpStockMap.get(loopDate);
                if (dayInfo != null) {

                    // set vaules
                    impStockInfo.setImpInRackQty(DecimalUtil.add(impStockInfo.getImpInRackQty(),
                        dayInfo.getDayInRackQty()));
                    impStockInfo.setImpStockQty(DecimalUtil.add(impStockInfo.getImpStockQty(),
                        dayInfo.getDayImpStockQty()));
                    impStockInfo.setWhsStockQty(DecimalUtil.add(impStockInfo.getWhsStockQty(),
                        dayInfo.getDayWhsStockQty()));
                    impStockInfo.setNgQty(DecimalUtil.add(impStockInfo.getNgQty(), dayInfo.getDayNgQty()));
                    impStockInfo.setOnholdQty(DecimalUtil.add(impStockInfo.getOnholdQty(), dayInfo.getDayOnholdQty()));

                    // set day information
                    impStockInfo.setDayInboundQty(dayInfo.getDayInboundQty());
                    impStockInfo.setDayOutboundQty(dayInfo.getDayOutboundQty());
                }

                // reset
                preImpStock = impStockInfo;

                // set into list
                impStockInfoList.add(impStockInfo);

                // next date
                loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            }
        }

        // get max parts status id
        int maxIsId = this.getMaxImpStockDayId();
        // save into database
        for (TnfImpStockByDay impStock : impStockInfoList) {

            // check exist?
            if (impStock.getStockDayId() != null) {

                // update
                super.baseDao.update(impStock);
            } else {

                // set id
                impStock.setStockDayId(maxIsId++);

                // save into database
                super.baseDao.insert(impStock);
            }
        }
    }

    /**
     * prepare all data for each parts supplier.
     * 
     * @param dayInfoList dayInfoList
     * @param impStockMap impStockMap
     */
    private void prepareDayImpStockList(List<TnfImpStockByDayEx> dayInfoList,
        Map<String, Map<Date, TnfImpStockByDayEx>> impStockMap) {

        // get start date
        Date bsDate = DateTimeUtil.parseDate(ConfigManager.getBatchStartDate());
        Date endDate = null;
        Map<Date, TnfImpStockByDayEx> dayImpStockMap = null;
        String partsKey = null;
        for (TnfImpStockByDayEx dayInfo : dayInfoList) {

            // prepare key
            partsKey = this.prepareKeyForImpStock(dayInfo);

            // if same
            dayImpStockMap = impStockMap.get(partsKey);

            // get day map
            if (dayImpStockMap == null) {
                dayImpStockMap = new HashMap<Date, TnfImpStockByDayEx>();
                impStockMap.put(partsKey, dayImpStockMap);
            }

            // get end date
            endDate = dayInfo.getEndDate() == null ? bsDate : dayInfo.getEndDate();
            // if not exist in map
            if (!dayImpStockMap.containsKey(endDate)) {
                dayImpStockMap.put(endDate, dayInfo);
            }
        }
    }

    /**
     * Create new import stock information.
     * 
     * @param source import stock
     * @param dbTime data date time
     * @param endDate end date
     * @param isTemp is template
     * 
     * @return order balance information
     */
    private TnfImpStockByDay createNewImpStock(TnfImpStockByDay source, Timestamp dbTime, Date endDate, boolean isTemp) {

        // create new
        TnfImpStockByDay impStockInfo = new TnfImpStockByDay();

        // set as date before
        impStockInfo.setOfficeId(source.getOfficeId());
        impStockInfo.setWhsId(source.getWhsId());
        impStockInfo.setPartsId(source.getPartsId());
        impStockInfo.setSupplierId(source.getSupplierId());
        impStockInfo.setBusinessPattern(source.getBusinessPattern());
        impStockInfo.setEndDate(endDate);

        // set increase values
        if (!isTemp) {

            // if does not has, set as day before
            impStockInfo.setImpInRackQty(source.getImpInRackQty());
            impStockInfo.setImpStockQty(source.getImpStockQty());
            impStockInfo.setWhsStockQty(source.getWhsStockQty());
            impStockInfo.setNgQty(source.getNgQty());
            impStockInfo.setOnholdQty(source.getOnholdQty());

        } else {

            // set as zero
            impStockInfo.setImpInRackQty(BigDecimal.ZERO);
            impStockInfo.setImpStockQty(BigDecimal.ZERO);
            impStockInfo.setWhsStockQty(BigDecimal.ZERO);
            impStockInfo.setNgQty(BigDecimal.ZERO);
            impStockInfo.setOnholdQty(BigDecimal.ZERO);
        }

        // set day information
        impStockInfo.setDayInboundQty(BigDecimal.ZERO);
        impStockInfo.setDayOutboundQty(BigDecimal.ZERO);

        // set create and update information
        impStockInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
        impStockInfo.setUpdatedDate(dbTime);

        // return
        return impStockInfo;
    }

    /**
     * modify exist import stock .
     * 
     * @param target target import stock
     * @param source increase import stock
     * @param dbTime data date time
     */
    private void modifyImpStockInfo(TnfImpStockByDay target, TnfImpStockByDayEx source, Timestamp dbTime) {

        // order first create, add new order information
        if (source != null) {
            // set parts status id
            target.setStockDayId(source.getStockDayId());
            // set create date and version
            target.setCreatedBy(source.getCreatedBy());
            target.setCreatedDate(source.getCreatedDate());
            if (source.getVersion() == null) {
                target.setVersion(IntDef.INT_ONE);
            } else {
                target.setVersion(source.getVersion().intValue() + IntDef.INT_ONE);
            }
        } else {
            // set create date and version
            target.setCreatedBy(BatchConst.BATCH_USER_ID);
            target.setCreatedDate(dbTime);
            target.setVersion(IntDef.INT_ONE);
        }
    }

    /**
     * prepare key for imp stock.
     * 
     * @param impStock imp stock
     * @return key
     */
    private String prepareKeyForImpStock(TnfImpStockByDayEx impStock) {

        StringBuffer keyBuff = new StringBuffer();

        keyBuff.setLength(IntDef.INT_ZERO);
        keyBuff.append(impStock.getPartsId());
        keyBuff.append(StringConst.UNDERLINE);
        keyBuff.append(impStock.getSupplierId());
        keyBuff.append(StringConst.UNDERLINE);
        keyBuff.append(impStock.getWhsId() != null ? impStock.getWhsId() : StringConst.ALPHABET_A);

        return keyBuff.toString();
    }

    /**
     * Prepare day information for order balance.
     * 
     * @param dayInfoMap day information map
     * @param orderBalanceList parts status list
     */
    public void prepareOrderBalanceList(Map<String, Map<Date, TnfBalanceByDayEx>> dayInfoMap,
        List<TnfBalanceByDayEx> orderBalanceList) {

        // get end date
        Date bsDate = DateTimeUtil.parseDate(ConfigManager.getBatchStartDate());
        Date endDate = null;

        // get map
        Map<Date, TnfBalanceByDayEx> dayOrderBalanceMap = null;
        StringBuffer partsKey = new StringBuffer();
        String preKey = null;
        for (TnfBalanceByDayEx dayInfo : orderBalanceList) {

            // reset parts key
            partsKey.setLength(IntDef.INT_ZERO);

            // prepare key
            partsKey.append(dayInfo.getPartsId());
            partsKey.append(StringConst.COMMA);
            partsKey.append(dayInfo.getSupplierId());
            partsKey.append(StringConst.COMMA);
            partsKey.append(StringUtil.toSafeString(dayInfo.getImpPoNo()));
            partsKey.append(StringConst.COMMA);
            partsKey.append(StringUtil.toSafeString(dayInfo.getKanbanPlanNo()));

            // if same
            if (!partsKey.toString().equals(preKey)) {
                dayOrderBalanceMap = dayInfoMap.get(partsKey.toString());
            }

            // get day map
            if (dayOrderBalanceMap == null) {
                dayOrderBalanceMap = new HashMap<Date, TnfBalanceByDayEx>();
                dayInfoMap.put(partsKey.toString(), dayOrderBalanceMap);
            }

            // if not exist in map
            endDate = dayInfo.getEndDate() == null ? bsDate : dayInfo.getEndDate();
            if (!dayOrderBalanceMap.containsKey(endDate)) {
                dayOrderBalanceMap.put(endDate, dayInfo);
            } else {

                // reset values
                TnfBalanceByDayEx oldPartsStaus = dayOrderBalanceMap.get(endDate);
                if (dayInfo.getDayOrderQty() != null) {
                    oldPartsStaus.setDayOrderQty(dayInfo.getDayOrderQty());
                } else if (dayInfo.getDayReceivedQty() != null) {
                    oldPartsStaus.setDayReceivedQty(dayInfo.getDayReceivedQty());
                } else if (dayInfo.getForceCompletedQty() != null) {
                    oldPartsStaus.setForceCompletedQty(dayInfo.getForceCompletedQty());
                }
            }

            // reset
            preKey = partsKey.toString();
        }
    }

    /**
     * save order balance list.
     * 
     * @param balanceByDayMap balanceByDayMap
     * @param endDate end date
     * @param lastEndDate last End Date
     * @param minEndDate minEndDate
     * @param officeId officeId
     */
    public void doSaveOrderBalanceListIntoDb(Map<String, Map<Date, TnfBalanceByDayEx>> balanceByDayMap, Date endDate,
        Date lastEndDate, Date minEndDate, Integer officeId) {

        // get current time
        Timestamp dbTime = this.getDBDateTimeByDefaultTimezone();
        Map<String, Map<Date, TnfBalanceByDayEx>> extBalanceMap = new HashMap<String, Map<Date, TnfBalanceByDayEx>>();
        Map<Date, TnfBalanceByDayEx> dayBalanceMap = null;
        Map<Date, TnfBalanceByDayEx> dayExtBalanceMap = null;
        List<TnfBalanceByDay> balanceInfoList = new ArrayList<TnfBalanceByDay>();
        TnfBalanceByDay balanceInfo = null;

        // copy last day to next day
        Date loopDate = null;

        // prepare parameter
        TnfBalanceByDayEx copyParam = new TnfBalanceByDayEx();
        copyParam.setOfficeId(officeId);
        copyParam.setCreatedBy(BatchConst.BATCH_USER_ID);
        copyParam.setCreatedDate(dbTime);
        copyParam.setUpdatedBy(BatchConst.BATCH_USER_ID);
        copyParam.setUpdatedDate(dbTime);
        copyParam.setLastEndDate(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
        copyParam.setEndDate(endDate);
        // insert
        this.prepareOrderBalanceByEachDate(copyParam, lastEndDate, endDate);

        // check is has date ?
        if (balanceByDayMap.isEmpty()) {
            return;
        }

        // get them from database
        TnfBalanceByDayEx balanceParam = new TnfBalanceByDayEx();
        balanceParam.setOfficeId(officeId);
        balanceParam.setLastEndDate(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
        balanceParam.setEndDate(endDate);

        // get all exist imp stock list
        logger.info("Start to load exists balance information");
        List<TnfBalanceByDayEx> existBalanceList = this.getAllExistBalanceList(balanceParam);
        this.prepareOrderBalanceList(extBalanceMap, existBalanceList);
        logger.info("load exists balance information OK, size : " + existBalanceList.size());
        existBalanceList = null;

        // loop exist parts status
        for (String mapKey : balanceByDayMap.keySet()) {

            // get map from map
            dayBalanceMap = balanceByDayMap.get(mapKey);
            dayExtBalanceMap = extBalanceMap.get(mapKey);
            // set default values
            if (dayExtBalanceMap == null) {
                dayExtBalanceMap = new HashMap<Date, TnfBalanceByDayEx>();
            }

            // get min end date of current parts
            List<Date> existDateList = new ArrayList<Date>(dayExtBalanceMap.keySet());
            Collections.sort(existDateList);
            List<Date> newDateList = new ArrayList<Date>(dayBalanceMap.keySet());
            Collections.sort(newDateList);

            // check loop date
            Date minExistDate = !existDateList.isEmpty() ? existDateList.get(IntDef.INT_ZERO) : null;
            // get the oldest Date
            Date minNewDate = newDateList.get(IntDef.INT_ZERO);

            // prepare loop start date
            loopDate = minNewDate;
            if (minExistDate != null && minExistDate.before(loopDate)) {
                loopDate = minExistDate;
            }
            if (loopDate.before(minEndDate)) {
                loopDate = minEndDate;
            }

            // reset
            TnfBalanceByDayEx tempBalance = dayBalanceMap.get(minNewDate);
            TnfBalanceByDay preBalance = dayExtBalanceMap.get(DateTimeUtil.addDay(minEndDate, IntDef.INT_N_ONE));
            while (!loopDate.after(endDate)) {

                // does not has prePartsStatus
                if (preBalance == null) {
                    // prepare new parts status
                    balanceInfo = this.createNewOrderBalance(tempBalance, dbTime, loopDate, true);
                } else {
                    // check current date is exist or not
                    balanceInfo = this.createNewOrderBalance(preBalance, dbTime, loopDate, false);
                }

                // check is exist parts status in database
                TnfBalanceByDayEx existBalance = dayExtBalanceMap.get(loopDate);
                // order first create, add new order information
                if (existBalance != null) {
                    // set parts status id
                    balanceInfo.setBalanceId(existBalance.getBalanceId());
                    // set create date and version
                    balanceInfo.setCreatedBy(existBalance.getCreatedBy());
                    balanceInfo.setCreatedDate(existBalance.getCreatedDate());
                    if (existBalance.getVersion() == null) {
                        balanceInfo.setVersion(IntDef.INT_ONE);
                    } else {
                        balanceInfo.setVersion(existBalance.getVersion().intValue() + IntDef.INT_ONE);
                    }
                } else {
                    // set create date and version
                    balanceInfo.setCreatedBy(BatchConst.BATCH_USER_ID);
                    balanceInfo.setCreatedDate(dbTime);
                    balanceInfo.setVersion(IntDef.INT_ONE);
                }

                // get parts information
                TnfBalanceByDayEx dayInfo = dayBalanceMap.get(loopDate);
                if (dayInfo != null) {
                    this.modifyOrderBalance(balanceInfo, dayInfo, dbTime);
                }

                // reset
                preBalance = balanceInfo;

                // set into list
                balanceInfoList.add(balanceInfo);

                // next date
                loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            }
        }

        // get max parts status id
        int maxBlId = this.getMaxBalanceByDayId();
        // save into database
        logger.info("start of save data");
        logger.info("data count: " + balanceInfoList.size());
        for (TnfBalanceByDay balance : balanceInfoList) {

            // check exist?
            if (balance.getBalanceId() != null) {

                // update
                super.baseDao.update(balance);
            } else {

                // set id
                balance.setBalanceId(maxBlId++);

                // save into database
                super.baseDao.insert(balance);
            }
        }
        logger.info("end of save data");
    }

    /**
     * Create new order balance.
     * 
     * @param source parts status
     * @param dbTime data date time
     * @param endDate end date
     * @param isTemp is template
     * 
     * @return order balance information
     */
    private TnfBalanceByDay createNewOrderBalance(TnfBalanceByDay source, Timestamp dbTime, Date endDate, boolean isTemp) {

        // create new
        TnfBalanceByDay orderBalaceInfo = new TnfBalanceByDay();
        // order first create, add new order information
        orderBalaceInfo.setPartsId(source.getPartsId());
        orderBalaceInfo.setSupplierId(source.getSupplierId());
        orderBalaceInfo.setCustomerId(source.getCustomerId());
        orderBalaceInfo.setBusinessPattern(source.getBusinessPattern());
        orderBalaceInfo.setImpPoNo(source.getImpPoNo());
        orderBalaceInfo.setKanbanPlanNo(source.getKanbanPlanNo());
        orderBalaceInfo.setEndDate(endDate);
        orderBalaceInfo.setOrderMonth(source.getOrderMonth());
        if (!isTemp) {
            orderBalaceInfo.setOrderQty(source.getOrderQty());
            orderBalaceInfo.setForceCompletedQty(source.getForceCompletedQty());
            orderBalaceInfo.setImpInboundQty(source.getImpInboundQty());
            orderBalaceInfo.setOrderBalanceQty(source.getOrderBalanceQty());
        } else {
            orderBalaceInfo.setOrderQty(BigDecimal.ZERO);
            orderBalaceInfo.setForceCompletedQty(BigDecimal.ZERO);
            orderBalaceInfo.setImpInboundQty(BigDecimal.ZERO);
            orderBalaceInfo.setOrderBalanceQty(BigDecimal.ZERO);
        }
        orderBalaceInfo.setUpdatedBy(BatchConst.BATCH_USER_ID);
        orderBalaceInfo.setUpdatedDate(dbTime);

        // return
        return orderBalaceInfo;
    }

    /**
     * modify exist order balance.
     * 
     * @param target target parts status
     * @param source source parts status
     * @param currentTime data date time
     */
    private void modifyOrderBalance(TnfBalanceByDay target, TnfBalanceByDayEx source, Timestamp currentTime) {

        // create new
        // order first create, add new order information
        target.setOrderQty(DecimalUtil.add(target.getOrderQty(), source.getDayOrderQty()));
        target.setForceCompletedQty(DecimalUtil.add(target.getForceCompletedQty(), source.getForceCompletedQty()));
        target.setImpInboundQty(DecimalUtil.add(target.getImpInboundQty(), source.getDayReceivedQty()));

        // get balance qty
        BigDecimal balanceQty = DecimalUtil.subtract(target.getOrderQty(), target.getImpInboundQty());
        // check force complete
        if (target.getForceCompletedQty() != null) {
            // if already not over order qty, and balance qty smaller than force completed qty
            if (balanceQty.compareTo(BigDecimal.ZERO) > 0) {
                if (balanceQty.compareTo(target.getForceCompletedQty()) <= 0) {
                    // set balance qty as 0
                    balanceQty = BigDecimal.ZERO;
                } else {
                    balanceQty = DecimalUtil.subtract(balanceQty, target.getForceCompletedQty());
                }
            }
        }
        // set balance
        target.setOrderBalanceQty(balanceQty);
    }

    /**
     * get Latest Batch Time.
     * 
     * @param jobParam job Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public Timestamp getLatestBatchTime(TntBatchJob jobParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLatestBatchTime"), jobParam);
    }

    /**
     * get Latest Batch Time.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public Date getLastPartsStatusDate(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastPartsStatusDate"), statusParam);
    }

    /**
     * get the oldest end of parts status.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public Date getLastDateOfPartStatus(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastDateOfPartStatus"), statusParam);
    }

    /**
     * get the oldest end of parts status.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public Date getLastDateOfImpStock(TnfImpStockByDayEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastDateOfImpStock"), statusParam);
    }

    /**
     * get the oldest end of parts status.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public Date getLastDateOfOrderBalance(TnfBalanceByDayEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastDateOfOrderBalance"), statusParam);
    }

    /**
     * get last exp parts status
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getExistPartsStatusList(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get list
        return this.baseMapper.selectList(this.getSqlId("getExistPartsStatusList"), statusParam);
    }

    /**
     * get Export order balance information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getExpBalanceformation(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getExpBalanceformation"), statusParam);
    }

    /**
     * get exp inbound plan information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getPreExpInboundPlanInformation(TnfExpPartsStatusEx statusParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getPreExpInboundPlanInformation"), statusParam);
    }

    /**
     * get exp inbound plan information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getPreExpInboundInformation(TnfExpPartsStatusEx statusParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getPreExpInboundInformation"), statusParam);
    }

    /**
     * get exp inbound plan information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getExpInboundPlanInformation(TnfExpPartsStatusEx statusParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getExpInboundPlanInformation"), statusParam);
    }

    /**
     * get exp inbound information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getExpInboundInformation(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getExpInboundInformation"), statusParam);
    }

    /**
     * get on-shipping invoice information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getInvoiceInformation(TnfExpPartsStatusEx statusParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getInvoiceInformation"), statusParam);
    }

    /**
     * get on-shipping invoice information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getAreadyDirectInformation(TnfExpPartsStatusEx statusParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getAreadyDirectInformation"), statusParam);
    }

    /**
     * get on-shipping invoice information.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfExpPartsStatusEx> getAreadyInvoiceformation(TnfExpPartsStatusEx statusParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getAreadyInvoiceformation"), statusParam);
    }

    /**
     * get imp information list.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfImpStockByDayEx> getImpStockInformationList(TnfImpStockByDayEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.selectList(this.getSqlId("getImpStockInformationList"), statusParam);
    }

    /**
     * get imp information list.
     * 
     * @param statusParam parts status Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfImpStockByDayEx> getImpStockInformationListBefore(TnfImpStockByDayEx statusParam)
        throws BusinessException {

        // get timestamp
        return super.baseMapper.selectList(this.getSqlId("getImpStockInformationListBefore"), statusParam);
    }

    /**
     * get all exist imp information list.
     * 
     * @param statusParam imp stock Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    private List<TnfImpStockByDayEx> getAllExistImpStockList(TnfImpStockByDayEx statusParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.selectList(this.getSqlId("getAllExistImpStockList"), statusParam);
    }

    /**
     * get all exist imp information list.
     * 
     * @param balanceParam balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    private List<TnfBalanceByDayEx> getAllExistBalanceList(TnfBalanceByDayEx balanceParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.selectList(this.getSqlId("getAllExistBalanceList"), balanceParam);
    }

    /**
     * get Latest import stock parameter.
     * 
     * @param impStockParam imp stock Param
     * @throws BusinessException exception
     * 
     * @return last imp stock date
     */
    public Date getLastImpStockDate(TnfImpStockByDayEx impStockParam) throws BusinessException {

        // get timestamp
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastImpStockDate"), impStockParam);
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * 
     * @return last imp stock date
     */
    private List<TnfImpStockByDay> getExistImpStockListByDate(TnfImpStockByDayEx copyParam) {

        // get list
        List<TnfImpStockByDay> impStockList = super.baseMapper.selectList(this.getSqlId("getImpstockByEndDate"),
            copyParam);
        // if null, set as empty
        if (impStockList == null) {
            impStockList = new ArrayList<TnfImpStockByDay>();
        }

        // get list
        return impStockList;
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * @param lastEndDate lastEndDate
     * @param endDate endDate
     */
    private void prepareImpstockByEachDate(TnfImpStockByDayEx copyParam, Date lastEndDate,
        Date endDate) {
        // define
        List<TnfImpStockByDay> impStockList = null;
        Date loopDate = null;

        // check update
        if (lastEndDate != null && copyParam.getLastEndDate().before(lastEndDate)) {

            // get already exist list
            impStockList = this.getExistImpStockByEndDate(copyParam);

            // do update
            if (impStockList != null && !impStockList.isEmpty()) {

                // get loop end Date
                Date loopEndDate = null;
                if (lastEndDate.before(endDate)) {
                    loopEndDate = lastEndDate;
                } else {
                    loopEndDate = endDate;
                }

                // loop
                for (TnfImpStockByDay impStockInfo : impStockList) {
                    // reset loop date
                    loopDate = DateTimeUtil.addDay(impStockInfo.getEndDate(), IntDef.INT_ONE);
                    while (!loopDate.after(loopEndDate)) {

                        // reset endDate
                        impStockInfo.setEndDate(loopDate);

                        // update
                        this.updateTnfImpStockByDay(impStockInfo);

                        // next date
                        loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
                    }
                }
            }
        }

        // re-get order balance List
        impStockList = this.getExistImpStockListByDate(copyParam);

        // get max parts status id
        int maxIsId = this.getMaxImpStockDayId();
        for (TnfImpStockByDay impStockInfo : impStockList) {

            // get last end Date
            loopDate = DateTimeUtil.addDay(impStockInfo.getEndDate(), IntDef.INT_ONE);
            while (!loopDate.after(endDate)) {

                // prepare new entity
                TnfImpStockByDay newImpStock = new TnfImpStockByDay();

                // copy
                newImpStock.setOfficeId(impStockInfo.getOfficeId());
                newImpStock.setWhsId(impStockInfo.getWhsId());
                newImpStock.setPartsId(impStockInfo.getPartsId());
                newImpStock.setSupplierId(impStockInfo.getSupplierId());
                newImpStock.setBusinessPattern(impStockInfo.getBusinessPattern());
                newImpStock.setImpInRackQty(impStockInfo.getImpInRackQty());
                newImpStock.setImpStockQty(impStockInfo.getImpStockQty());
                newImpStock.setWhsStockQty(impStockInfo.getWhsStockQty());
                newImpStock.setNgQty(impStockInfo.getNgQty());
                newImpStock.setOnholdQty(impStockInfo.getOnholdQty());

                // reset values
                newImpStock.setStockDayId(maxIsId++);
                newImpStock.setEndDate(loopDate);
                newImpStock.setDayInboundQty(BigDecimal.ZERO);
                newImpStock.setDayOutboundQty(BigDecimal.ZERO);
                newImpStock.setCreatedDate(copyParam.getCreatedDate());
                newImpStock.setCreatedBy(copyParam.getCreatedBy());
                newImpStock.setUpdatedBy(copyParam.getUpdatedBy());
                newImpStock.setUpdatedDate(copyParam.getUpdatedDate());
                newImpStock.setVersion(IntDef.INT_ONE);

                // insert
                super.baseDao.insert(newImpStock);

                // next day
                loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            }
        }

        // flush
        super.baseDao.flush();
        super.baseDao.clear();
    }

    /**
     * get vv order information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getVVOrderInfoByEachIPO(TnfBalanceByDayEx balanceParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getVVOrderInfoByEachIPO"), balanceParam);
    }

    /**
     * get vv order information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getVVOrderInfoByEachIPOBefore(TnfBalanceByDayEx balanceParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getVVOrderInfoByEachIPOBefore"), balanceParam);
    }

    /**
     * get kanban information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getKanbanInfoByKanbanNo(TnfBalanceByDayEx balanceParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getKanbanInfoByKanbanNo"), balanceParam);
    }

    /**
     * get kanban information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getKanbanInfoByKanbanNoBefore(TnfBalanceByDayEx balanceParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getKanbanInfoByKanbanNoBefore"), balanceParam);
    }

    /**
     * get exp inbound plan information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getImpRecevieInfoByOrder(TnfBalanceByDayEx balanceParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getImpRecevieInfoByOrder"), balanceParam);
    }

    /**
     * get exp inbound plan information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getImpRecevieInfoByOrderBefore(TnfBalanceByDayEx balanceParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getImpRecevieInfoByOrderBefore"), balanceParam);
    }

    /**
     * get fore complete information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getForceCompletedInfoList(TnfBalanceByDayEx balanceParam) throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getForceCompletedInfoList"), balanceParam);
    }

    /**
     * get fore complete information.
     * 
     * @param balanceParam order balance Param
     * @throws BusinessException exception
     * 
     * @return last success batch date
     */
    public List<TnfBalanceByDayEx> getForceCompletedInfoListBefore(TnfBalanceByDayEx balanceParam)
        throws BusinessException {

        // get timestamp
        return this.baseMapper.selectList(this.getSqlId("getForceCompletedInfoListBefore"), balanceParam);
    }

    /**
     * get Latest Order Balance End Date.
     * 
     * @param balanceParam Order Balance Parameter
     * 
     * @return last imp stock date
     */
    public Date getLastBalanceDate(TnfBalanceByDayEx balanceParam) {

        // get last end date
        return super.baseMapper.getSqlSession().selectOne(this.getSqlId("getLastBalanceDate"), balanceParam);
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * 
     * @return last imp stock date
     */
    private List<TnfBalanceByDayEx> getExistOrderBalanceByEndDate(TnfBalanceByDayEx copyParam) {

        // get last end date
        List<TnfBalanceByDayEx> orderBalanceList = super.baseMapper.selectList(this.getSqlId("getOrderBalanceByEndDate"),
            copyParam);
        // if null set as empty
        if (orderBalanceList == null) {
            orderBalanceList = new ArrayList<TnfBalanceByDayEx>();
        }

        // get list
        return orderBalanceList;
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * 
     * @return last imp stock date
     */
    private List<TnfBalanceByDayEx> getExistOrderBalanceOfLastEndDate(TnfBalanceByDayEx copyParam) {

        // get last end date
        List<TnfBalanceByDayEx> orderBalanceList = super.baseMapper.selectList(
            this.getSqlId("getExistOrderBalanceOfLastEndDate"), copyParam);
        // if null set as empty
        if (orderBalanceList == null) {
            orderBalanceList = new ArrayList<TnfBalanceByDayEx>();
        }

        // get list
        return orderBalanceList;
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * 
     * @return last imp stock date
     */
    private List<TnfImpStockByDay> getExistImpStockByEndDate(TnfImpStockByDayEx copyParam) {

        // get last end date
        List<TnfImpStockByDay> impStockList = super.baseMapper.selectList(
            this.getSqlId("getExistImpStockByEndDate"), copyParam);
        // if null set as empty
        if (impStockList == null) {
            impStockList = new ArrayList<TnfImpStockByDay>();
        }

        // get list
        return impStockList;
    }

    /**
     * prepare Latest imp stock End Date.
     * 
     * @param copyParam Order Balance Parameter
     * @param lastEndDate lastEndDate
     * @param endDate endDate
     */
    private void prepareOrderBalanceByEachDate(TnfBalanceByDayEx copyParam, Date lastEndDate, Date endDate) {

        // define
        List<TnfBalanceByDayEx> orderBalanceList = null;
        Date loopDate = null;

        // check update
        if (lastEndDate != null && copyParam.getLastEndDate().before(lastEndDate)) {

            // get already exist list
            orderBalanceList = this.getExistOrderBalanceOfLastEndDate(copyParam);

            // do update
            if (orderBalanceList != null && !orderBalanceList.isEmpty()) {

                // get loop end Date
                Date loopEndDate = null;
                if (lastEndDate.before(endDate)) {
                    loopEndDate = lastEndDate;
                } else {
                    loopEndDate = endDate;
                }

                // loop
                int cnt = 0;
                int size = orderBalanceList.size();
                logger.info("start to update order balance.");
                for (TnfBalanceByDayEx orderBalance : orderBalanceList) {
                    // count
                    cnt++;
                    if (cnt % IntDef.INT_THOUSAND == IntDef.INT_ZERO) {
                        logger.info(cnt + "/" + size);
                    }
                    // do update
                    orderBalance.setLastEndDate(loopEndDate);
                    this.updateTnfBalanceByDay(orderBalance);
                }
                logger.info(cnt + "/" + size);
                // clear
                orderBalanceList = null;
            }
        }

        // re-get order balance List
        logger.info("start to create new order balance.");
        orderBalanceList = this.getExistOrderBalanceByEndDate(copyParam);

        // get max parts status id
        int maxIsId = this.getMaxBalanceByDayId();
        for (TnfBalanceByDay orderBalance : orderBalanceList) {

            // get last end Date
            loopDate = DateTimeUtil.addDay(orderBalance.getEndDate(), IntDef.INT_ONE);
            while (!loopDate.after(endDate)) {

                // new data
                TnfBalanceByDay newBalance = new TnfBalanceByDay();

                // copy
                newBalance.setImpPoNo(orderBalance.getImpPoNo());
                newBalance.setKanbanPlanNo(orderBalance.getKanbanPlanNo());
                newBalance.setOrderMonth(orderBalance.getOrderMonth());
                newBalance.setPartsId(orderBalance.getPartsId());
                newBalance.setCustomerId(orderBalance.getCustomerId());
                newBalance.setSupplierId(orderBalance.getSupplierId());
                newBalance.setBusinessPattern(orderBalance.getBusinessPattern());
                newBalance.setOrderBalanceQty(orderBalance.getOrderBalanceQty());
                newBalance.setOrderQty(orderBalance.getOrderQty());
                newBalance.setImpInboundQty(orderBalance.getImpInboundQty());
                newBalance.setForceCompletedQty(orderBalance.getForceCompletedQty());

                // reset values
                newBalance.setBalanceId(maxIsId++);
                newBalance.setEndDate(loopDate);
                newBalance.setCreatedDate(copyParam.getCreatedDate());
                newBalance.setCreatedBy(copyParam.getCreatedBy());
                newBalance.setUpdatedBy(copyParam.getUpdatedBy());
                newBalance.setUpdatedDate(copyParam.getUpdatedDate());
                newBalance.setVersion(IntDef.INT_ONE);

                // insert
                super.baseDao.insert(newBalance);

                // next day
                loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
            }
        }
        logger.info("end to create new order balance.");

        // flush
        super.baseDao.flush();
        super.baseDao.clear();
    }

    /**
     * get max parts status id.
     * 
     * @throws BusinessException exception
     * @return max parts status id
     */
    public Integer getMaxPartsStatusId() throws BusinessException {

        // parameter
        TnfExpPartsStatusEx param = new TnfExpPartsStatusEx();

        // get max parts status id
        Integer maxPartsStatusId = baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxPartsStatusId"), param);

        // return
        return maxPartsStatusId == null ? IntDef.INT_ONE : (maxPartsStatusId.intValue() + IntDef.INT_ONE);
    }

    /**
     * get max import stock day id.
     * 
     * @throws BusinessException exception
     * 
     * @return max import stock day id
     */
    public Integer getMaxImpStockDayId() throws BusinessException {

        // parameter
        TnfImpStockByDayEx param = new TnfImpStockByDayEx();

        // get max import stock day id
        Integer maxImpStockDayId = baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxImpStockDayId"), param);

        // return
        return maxImpStockDayId == null ? IntDef.INT_ONE : (maxImpStockDayId.intValue() + IntDef.INT_ONE);
    }

    /**
     * get max balance by day id.
     * 
     * @throws BusinessException exception
     * 
     * @return max balance by day id
     */
    public int getMaxBalanceByDayId() throws BusinessException {

        // parameter
        TnfBalanceByDayEx param = new TnfBalanceByDayEx();

        // get max balance id
        Integer maxBalanceId = baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxBalanceByDayId"), param);

        // return
        return maxBalanceId == null ? IntDef.INT_ONE : (maxBalanceId.intValue() + IntDef.INT_ONE);
    }

    /**
     * Update TnfBalanceByDay
     * 
     * @param param parameter
     * @return update count
     * @throws BusinessException exception
     */
    private Integer updateTnfBalanceByDay(TnfBalanceByDay param) throws BusinessException {

        // do update and return
        return baseMapper.update(this.getSqlId("updateTnfBalanceByDay"), param);
    }

    /**
     * Update TnfBalanceByDay
     * 
     * @param param parameter
     * @return update count
     * @throws BusinessException exception
     */
    private Integer updateTnfImpStockByDay(TnfImpStockByDay param) throws BusinessException {

        // do update and return
        return baseMapper.update(this.getSqlId("updateTnfImpStockByDay"), param);
    }
}
