/**
 * @screen Stock Status Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.command;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseFileBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.OnlineFlag;
import com.chinaplus.batch.common.consts.BatchConst.StockBatchId;
import com.chinaplus.batch.common.consts.BatchConst.StockCommonParam;
import com.chinaplus.batch.stock.bean.BasePartsInfoEntity;
import com.chinaplus.batch.stock.bean.StockComParam;
import com.chinaplus.batch.stock.service.CPSSSB01Service;
import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.bean.TntStockStatusHeader;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.AlarmFlag;
import com.chinaplus.common.consts.CodeConst.AlertLevel;
import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.CustStockFlag;
import com.chinaplus.common.consts.CodeConst.ImpStockFlag;
import com.chinaplus.common.consts.CodeConst.InventoryByBox;
import com.chinaplus.common.consts.CodeConst.NoCfcFlag;
import com.chinaplus.common.consts.CodeConst.NoPfcFlag;
import com.chinaplus.common.consts.CodeConst.SimulationType;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TnfImpStockByDay;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntRdDetail;
import com.chinaplus.common.service.SendEmailService;
import com.chinaplus.common.service.StockStatusService;
import com.chinaplus.common.util.CalendarManager;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.common.util.StockStatusManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.MailUtil;
import com.chinaplus.core.util.MailUtilForOnline;
import com.chinaplus.core.util.StringUtil;

/**
 * Main Batch process for Stock Status batch.
 * 
 * @author liu_yinchuan
 */
@Component(StockBatchId.CPSSSB01)
public class CPSSSB01Batch extends BaseFileBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPSSSB01Batch.class);

    /** Stock status report file id */
    private static String STOCK_STATUS_FILE_ID = "CPSSSF01";

    /** Stock status report file name */
    private static String STOCK_STATUS_FILE_NAME = "Stock Status";

    /** The Service of Run-Down Batch */
    @Autowired
    private CPSSSB01Service cpsssb01Service;

    /** The Service of Run-Down Batch */
    @Autowired
    private StockStatusService stockStatusService;

    /**
     * SendEmailService.
     */
    @Autowired
    private SendEmailService sendEmailService;

    /** head map */
    private Map<Integer, String> headMap;

    /** styleMap */
    private Map<String, CellStyle> styleMap;

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // parameter object initialization
        StockComParam param = null;

        // Check batch arguments
        // if (args == null || (args.length != BatchConst.INT_TWO && args.length != BatchConst.INT_THREE)) {
        if (args == null || args.length != BatchConst.INT_THREE) {

            // error
            logger.error("The arguments number of batch is incorrect.");
            throw new BusinessException();
        } else {

            // set parameters
            param = new StockComParam();
            param.setOnlineFlag(StringUtil.toInteger(args[StockCommonParam.IS_ONLINE]));
            // if date is null, error
            if (param.getOnlineFlag() == null) {
                logger.error("Online Flag is incorrect(Please set On-Line Flag as 1: On-Line or 2: Off-Line).");
                throw new BusinessException();
            }

            // set parameters
            param.setStockDate(DateTimeUtil.parseDate(args[StockCommonParam.STOCK_DATE], DateTimeUtil.FORMAT_YYYYMMDD));

            // if date is null, error
            if (param.getStockDate() == null) {
                logger.error("Stock date is incorrect(Please set Stock Date like:2016-02-23).");
                throw new BusinessException();
            }

            // if office exist, set office code
            String timeZone = null;
            // if (args.length > StockCommonParam.OFFICE_CODE) {
            if (!StringUtil.isEmpty(args[StockCommonParam.OFFICE_CODE])) {
                param.setOfficeCode(args[StockCommonParam.OFFICE_CODE]);
                // check
                TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
                if (office == null) {
                    logger.error("Office code does not an effective office.");
                    throw new BusinessException();
                }
                // set office id
                param.setOfficeId(office.getOfficeId());
                timeZone = office.getTimeZone();
            } else {
                logger.error("Office Code is incorrect, can not be empty.");
                throw new BusinessException();
            }
            // }

            // set process date
            param.setProcessDate(baseService.getDBDateTime(timeZone));
        }

        // return
        return param;
    }

    /**
     * Main process logic for stock status batch.
     * 
     * @param BaseBatchParam.
     * @return the result of current operation
     */
    @Override
    public boolean doOperate(BaseBatchParam baseParam) throws Exception {

        // batch process logic start
        logger.info("batch CPSSSB01Batch start......");

        // cast to StockComParam
        StockComParam param = (StockComParam) baseParam;

        // search parameter
        BasePartsInfoEntity offInfo = new BasePartsInfoEntity();
        offInfo.setOfficeCode(param.getOfficeCode());

        // pick up all office, and then loop by each office
        List<BasePartsInfoEntity> officeList = cpsssb01Service.getOfficesFromDatabase(offInfo);
        if (officeList == null || officeList.isEmpty()) {
            return true;
        }

        // loop each office
        for (BasePartsInfoEntity office : officeList) {

            // batch process logic end
            logger.info("office(" + office.getOfficeCode() + ") process end......");
            // set stock date
            office.setStockDate(param.getStockDate());
            // set is online
            if (param.getOnlineFlag().equals(OnlineFlag.ONLINE)) {
                office.setOnlineFlag(OnlineFlag.ONLINE);
            }

            // Prepare Run-down Data
            List<TntStockStatusEx> stockStatusList = doPrepareData(office);

            // Make Run-down Detail Information
            doMakeStockStatusList(stockStatusList);

            // do save all data
            cpsssb01Service.doUpdateStockStatusInfo(office, stockStatusList);

            // Emial Alert Information
            prepareEmailAlert(office, param.getOnlineFlag());
            // batch process logic end
            logger.info("office(" + office.getOfficeCode() + ") process end......");
        }
        // batch process logic end
        logger.info("batch CPSSSB01Batch end......");

        // return OK.
        return true;
    }

    /**
     * Prepare Data from database for make Run-down.
     * 
     * @param paramInfo batch parameter
     * @return all data for run-down
     */
    private List<TntStockStatusEx> doPrepareData(BasePartsInfoEntity paramInfo) {

        // prepare parameter
        Date actualStockDate = null;
        if (paramInfo.getOnlineFlag() != null) {
            // if online use current date
            actualStockDate = paramInfo.getStockDate();
        } else {
            // if offline use next date
            actualStockDate = DateTimeUtil.addDay(paramInfo.getStockDate(), IntDef.INT_ONE);
        }
        
        // first Share Date
        paramInfo.setFirstShareDate(DateTimeUtil.firstDay(actualStockDate));
        // last Share Date
        paramInfo.setLastShareDate(DateTimeUtil.lastDay(actualStockDate));

        // Basic Part Information
        List<TntStockStatusEx> stockStatusList = cpsssb01Service.getPartsMasterInfo(paramInfo);

        // check data exists
        if (stockStatusList == null || stockStatusList.isEmpty()) {
            return new ArrayList<TntStockStatusEx>();
        }

        // Imp Actual Out bound Detail In Current Month
        List<TnfImpStockByDay> actOutboundList = cpsssb01Service.getImpActualOutboundDetail(paramInfo);

        // Run down Detail Information
        List<TntRdDetail> rdDetailList = cpsssb01Service.getRundownDetailInfo(paramInfo);

        // Get Next In-bound Plan List
        List<TntRdDetailAttachEx> onShippingList = cpsssb01Service.getNextInboundPlanList(paramInfo);

        // Customer Daily Usage List
        List<TntCfcDay> dailyUsageList = cpsssb01Service.getCustomerDailyUsageList(paramInfo);

        // Calendar List
        List<TnmCalendarPartyEx> cldPartyInfoList = cpsssb01Service.getCalendarPartyInfo(paramInfo);
        List<TnmCalendarDetailEx> cldDetailList = cpsssb01Service.getCalendarList(paramInfo);

        // defined index
        int actIdx = IntDef.INT_ZERO;
        int rdDetIdx = IntDef.INT_ZERO;
        int shipIdx = IntDef.INT_ZERO;
        int usageIdex = IntDef.INT_ZERO;

        // prepare parts master from
        Map<String, Integer> calendarPartyMap = new HashMap<String, Integer>();
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap = new HashMap<Integer, List<TnmCalendarDetailEx>>();
        List<TntStockStatusEx> effectList = new ArrayList<TntStockStatusEx>();
        TntStockStatusEx preStockStatusInfo = null;
        for (TntStockStatusEx stockStatusInfo : stockStatusList) {

            // if null
            if (preStockStatusInfo == null) {

                // reset
                preStockStatusInfo = stockStatusInfo;
                // prepare exp parts set
                StringBuffer sb = new StringBuffer();
                sb.append(StringConst.COMMA);
                sb.append(stockStatusInfo.getExpPartsId());
                sb.append(StringConst.COMMA);
                preStockStatusInfo.setExpPartsSet(sb.toString());
                continue;
            } else if (preStockStatusInfo.getPartsId().equals(stockStatusInfo.getPartsId())
                    && preStockStatusInfo.getSupplierId().equals(stockStatusInfo.getSupplierId())) {

                // prepare exp parts set
                StringBuffer sb = new StringBuffer();
                sb.append(preStockStatusInfo.getExpPartsSet());
                sb.append(stockStatusInfo.getExpPartsId());
                sb.append(StringConst.COMMA);
                preStockStatusInfo.setExpPartsSet(sb.toString());
                continue;
            }

            // set stock date
            preStockStatusInfo.setStockDate(DateTimeUtil.addDay(actualStockDate, IntDef.INT_N_ONE));

            // prepare Customer Calendar List
            preStockStatusInfo.setCustCalendarList(CalendarManager.findCalendarInfo(CalendarParty.CUSTOMER,
                paramInfo.getOfficeId(), preStockStatusInfo.getCustomerId(), calendarMap, cldDetailList,
                calendarPartyMap, cldPartyInfoList));

            // prepare Imp Actual Out bound Detail In Current Month
            actIdx = prepareActOutboundInfo(preStockStatusInfo, actOutboundList, actIdx);

            // prepare Run down Detail Information
            rdDetIdx = prepareRunDownDetailInfo(preStockStatusInfo, rdDetailList, rdDetIdx);

            // prepare Next In-bound Plan List
            shipIdx = prepareNextInboundInfo(preStockStatusInfo, onShippingList, shipIdx);

            // prepare Customer Daily Usage List
            usageIdex = prepareDailyUsage(preStockStatusInfo, dailyUsageList, usageIdex);

            // set into stock data
            effectList.add(preStockStatusInfo);

            // reset
            preStockStatusInfo = stockStatusInfo;
            // prepare exp parts set
            StringBuffer sb = new StringBuffer();
            sb.append(StringConst.COMMA);
            sb.append(stockStatusInfo.getExpPartsId());
            sb.append(StringConst.COMMA);
            preStockStatusInfo.setExpPartsSet(sb.toString());
        }

        // process last one
        if (preStockStatusInfo != null) {

            // set stock date
            preStockStatusInfo.setStockDate(DateTimeUtil.addDay(actualStockDate, IntDef.INT_N_ONE));

            // prepare Customer Calendar List
            preStockStatusInfo.setCustCalendarList(CalendarManager.findCalendarInfo(CalendarParty.CUSTOMER,
                paramInfo.getOfficeId(), preStockStatusInfo.getCustomerId(), calendarMap, cldDetailList,
                calendarPartyMap, cldPartyInfoList));

            // prepare Imp Actual Out bound Detail In Current Month
            actIdx = prepareActOutboundInfo(preStockStatusInfo, actOutboundList, actIdx);

            // prepare Run down Detail Information
            rdDetIdx = prepareRunDownDetailInfo(preStockStatusInfo, rdDetailList, rdDetIdx);

            // prepare Next In-bound Plan List
            shipIdx = prepareNextInboundInfo(preStockStatusInfo, onShippingList, shipIdx);

            // prepare Customer Daily Usage List
            usageIdex = prepareDailyUsage(preStockStatusInfo, dailyUsageList, usageIdex);

            // set into stock data
            effectList.add(preStockStatusInfo);
        }
        
        // sort
        PartSortManager.sort(effectList, "ttcPartsNo", "oldPartsNo", false);
        // Do finally sort
        Collections.sort(effectList, new Comparator<TntStockStatusEx>() {
            @Override
            public int compare(TntStockStatusEx bean1, TntStockStatusEx bean2) {
                int res = bean1.getSortGroupNo().compareTo(bean2.getSortGroupNo());
                if (res == 0) {
                    res = bean1.getSortGroupType().compareTo(bean2.getSortGroupType());
                }
                if (res == 0) {
                    try {
                        res = bean1.getTtcPartsNo().compareTo(bean2.getTtcPartsNo());
                    } catch (Exception e) {
                        res = 0;
                    }
                }
                if (res == 0) {
                    res = bean1.getPartsId().compareTo(bean2.getPartsId());
                }
                if (res == 0) {
                    res = bean1.getSupplierId().compareTo(bean2.getSupplierId());
                }
                return res;
            }
        });

        // return parts master information
        return effectList;
    }

    /**
     * prepare Imp Actual Out bound Detail In Current Month.
     * 
     * @param stockStatusInfo basic stock status information
     * @param actOutboundList actual out bound information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareActOutboundInfo(TntStockStatusEx stockStatusInfo, List<TnfImpStockByDay> actOutboundList,
        int startIdx) {

        // define
        List<TnfImpStockByDay> actOBList = new ArrayList<TnfImpStockByDay>();
        TnfImpStockByDay actOutbound = null;

        // loop data
        int i = startIdx;
        for (; i < actOutboundList.size(); i++) {

            // get order detail
            actOutbound = actOutboundList.get(i);

            // get part id compare
            int comp = actOutbound.getPartsId().compareTo(stockStatusInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set into list
            actOBList.add(actOutbound);
        }

        // set into parts master
        stockStatusInfo.setActOutboundList(actOBList);

        // return
        return i;
    }

    /**
     * prepare Run down Detail Information.
     * 
     * @param stockStatusInfo basic stock status information
     * @param rdDetailList run down detail information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareRunDownDetailInfo(TntStockStatusEx stockStatusInfo, List<TntRdDetail> rdDetailList, int startIdx) {

        // define
        List<TntRdDetail> curRdDetailList = new ArrayList<TntRdDetail>();
        TntRdDetail rdDetail = null;

        // loop data
        int i = startIdx;
        for (; i < rdDetailList.size(); i++) {

            // get order detail
            rdDetail = rdDetailList.get(i);

            // get part id compare
            int comp = rdDetail.getPartsId().compareTo(stockStatusInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set into list
            curRdDetailList.add(rdDetail);
        }

        // set into parts master
        stockStatusInfo.setRdDetailList(curRdDetailList);

        // return
        return i;
    }

    /**
     * prepare Next In-bound Plan List.
     * 
     * @param stockStatusInfo basic stock status information
     * @param onShippingList on shipping information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareNextInboundInfo(TntStockStatusEx stockStatusInfo, List<TntRdDetailAttachEx> onShippingList,
        int startIdx) {
        // define
        TntRdDetailAttachEx ibDetailInfo = null;
        // Set Inbound Plan Qty = empty
        BigDecimal planQty = null;
        // Set Invoice No. = empty
        StringBuffer invoiceNo = null;
        // Set In bound Plan Date = empty
        Date planDate = null;

        // loop data
        int i = startIdx;
        for (; i < onShippingList.size(); i++) {

            // get on-shipping detail
            ibDetailInfo = onShippingList.get(i);

            // get part id compare
            int comp = ibDetailInfo.getPartsId().compareTo(stockStatusInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set plan Date
            planDate = ibDetailInfo.getImpInbPlanDate();
            // set invoice
            if (invoiceNo == null) {
                invoiceNo = new StringBuffer().append(ibDetailInfo.getInvoiceNo());
            } else {
                invoiceNo.append(StringConst.SEMICOLON).append(ibDetailInfo.getInvoiceNo());
            }
            // set Inbound Plan Qty
            planQty = DecimalUtil.add(planQty, ibDetailInfo.getQty());
        }

        // set into parts master
        if (planDate != null) {
            // if has next invoice
            stockStatusInfo.setNextInboundDate(planDate);
            stockStatusInfo.setNextInboundQty(planQty);
            stockStatusInfo.setNextInvoice(invoiceNo.toString());
        }

        // return
        return i;
    }

    /**
     * Prepare customer daily usage information for each parts.
     * 
     * @param stockStatusInfo basic stock status information
     * @param dailyUsageList customer daily usage information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareDailyUsage(TntStockStatusEx stockStatusInfo, List<TntCfcDay> dailyUsageList, int startIdx) {
        // define
        List<TntCfcDay> dUsageList = new ArrayList<TntCfcDay>();
        TntCfcDay usageDetail = null;

        // loop data
        int i = startIdx;
        for (; i < dailyUsageList.size(); i++) {

            // get order detail
            usageDetail = dailyUsageList.get(i);

            // get part id compare
            int comp = usageDetail.getPartsId().compareTo(stockStatusInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set into list
            dUsageList.add(usageDetail);
        }

        // set into parts master
        stockStatusInfo.setDailyUsageList(dUsageList);

        // return
        return i;
    }

    /**
     * Make Stock Status Detail Information.
     * 
     * @param stockStatusList stock status information list
     */
    private void doMakeStockStatusList(List<TntStockStatusEx> stockStatusList) {

        // loop each stock status in list and then make other information information
        // last stock status information
        TntStockStatusEx preStockStatus = null;
        for (TntStockStatusEx stockStatusInfo : stockStatusList) {
            
            // reset qty
            if (stockStatusInfo.getImpStockFlag().equals(ImpStockFlag.WITHOUT_CLEARANCE)) {
                // reset on shipping qty
                stockStatusInfo.setExpOnshippingQty(DecimalUtil.add(stockStatusInfo.getExpOnshippingQty(),
                    stockStatusInfo.getInRackQty()));
                stockStatusInfo.setInRackQty(BigDecimal.ZERO);
            }

            // if same parts copy information
            if (preStockStatus != null && preStockStatus.getPartsId().equals(stockStatusInfo.getPartsId())) {
                // copy
                doCopyFromLastParts(preStockStatus, stockStatusInfo);
                continue;
            }

            // Prepare Alarm 1A for Stock Status
            prepareAlarm1ForStock(stockStatusInfo, false);

            // Prepare Alarm 1B for Stock Status
            if (stockStatusInfo.getImpStockFlag().equals(ImpStockFlag.WITH_CLEARANCE)) {
                // prepare
                prepareAlarm1ForStock(stockStatusInfo, true);
            }

            // Prepare Alarm2 for Stock Status
            prepareAlarm2ForStock(stockStatusInfo);

            // Prepare Alarm3 for Stock Status
            prepareAlarm3ForStock(stockStatusInfo);

            // Prepare Alarm4 for Stock Status
            prepareAlarm4ForStock(stockStatusInfo);

            // Prepare Alert Level of Stock status
            prepareAlertLevelForStock(stockStatusInfo);

            // reset
            preStockStatus = stockStatusInfo;
        }
    }
    
    /**
     * Copy alarm information form same parts.
     * 
     * @param source source
     * @param target target
     */
    private void doCopyFromLastParts(TntStockStatusEx source, TntStockStatusEx target) {

        // alarm 1
        target.setForecastNum(source.getForecastNum());

        // set alarm 1A
        target.setMinAlarm1a(source.getMinAlarm1a());
        target.setMaxAlarm1a(source.getMaxAlarm1a());
        target.setNoMaster1a(source.getNoMaster1a());
        target.setStockBoxes1a(source.getStockBoxes1a());
        target.setStockDays1a(source.getStockDays1a());
        target.setStockQty1a(source.getStockQty1a());
        // set alarm 1A plan
        target.setNoMasterPlan1a(source.getNoMasterPlan1a());
        target.setStockDaysPlan1a(source.getStockDaysPlan1a());
        target.setNextInboundDate(source.getNextInboundDate());
        target.setNextInboundQty(source.getNextInboundQty());
        target.setNextInvoice(source.getNextInvoice());
        // set alarm 1B
        target.setMinAlarm1b(source.getMinAlarm1b());
        target.setMaxAlarm1b(source.getMaxAlarm1b());
        target.setNoMaster1b(source.getNoMaster1b());
        target.setStockBoxes1b(source.getStockBoxes1b());
        target.setStockDays1b(source.getStockDays1b());
        target.setStockQty1b(source.getStockQty1b());
        // set alarm 1b plan
        target.setNoMasterPlan1b(source.getNoMasterPlan1b());
        target.setStockDaysPlan1b(source.getStockDaysPlan1b());

        // alarm 2
        // set end date and shortage date
        target.setEndDateTm(source.getEndDateTm());
        target.setShortageDateTm(source.getShortageDateTm());
        // set alarm
        target.setMinAlarmTm(source.getMinAlarmTm());
        target.setMaxAlarmTm(source.getMaxAlarmTm());
        // set min stock and min box
        target.setMinStockDayTm(source.getMinStockDayTm());
        target.setMaxStockDayTm(source.getMaxStockDayTm());
        target.setMinBoxesTm(source.getMinBoxesTm());
        target.setMaxBoxesTm(source.getMaxBoxesTm());
        // set no cfc flag
        target.setNoMasterMinTm(source.getNoMasterMinTm());
        target.setNoMasterMaxTm(source.getNoMasterMaxTm());
        // set end date and shortage date
        target.setEndDateF1(source.getEndDateF1());
        target.setShortageDateF1(source.getShortageDateF1());
        // set min stock and min box
        target.setMinStockDayF1(source.getMinStockDayF1());
        target.setMaxStockDayF1(source.getMaxStockDayF1());
        target.setMinBoxesF1(source.getMinBoxesF1());
        target.setMaxBoxesF1(source.getMaxBoxesF1());
        // set alarm
        target.setMinAlarmF1(source.getMinAlarmF1());
        target.setMaxAlarmF1(source.getMaxAlarmF1());
        // set no cfc flag
        target.setNoCfcFlagF1(source.getNoCfcFlagF1());
        target.setNoPfcFlagF1(source.getNoPfcFlagF1());
        target.setNoPfcMinF1(source.getNoPfcMinF1());
        target.setNoPfcMaxF1(source.getNoPfcMaxF1());
        // set end date and shortage date
        target.setEndDateF2(source.getEndDateF2());
        target.setShortageDateF2(source.getShortageDateF2());
        // set min stock and min box
        target.setMinStockDayF2(source.getMinStockDayF2());
        target.setMaxStockDayF2(source.getMaxStockDayF2());
        target.setMinBoxesF2(source.getMinBoxesF2());
        target.setMaxBoxesF2(source.getMaxBoxesF2());
        // set alarm
        target.setMinAlarmF2(source.getMinAlarmF2());
        target.setMaxAlarmF2(source.getMaxAlarmF2());
        // set no cfc flag
        target.setNoCfcFlagF2(source.getNoCfcFlagF2());
        target.setNoPfcFlagF2(source.getNoPfcFlagF2());
        target.setNoPfcMinF2(source.getNoPfcMinF2());
        target.setNoPfcMaxF2(source.getNoPfcMaxF2());
        // set end date and shortage date
        target.setEndDateF3(source.getEndDateF3());
        target.setShortageDateF3(source.getShortageDateF3());
        // set min stock and min box
        target.setMinStockDayF3(source.getMinStockDayF3());
        target.setMaxStockDayF3(source.getMaxStockDayF3());
        target.setMinBoxesF3(source.getMinBoxesF3());
        target.setMaxBoxesF3(source.getMaxBoxesF3());
        // set alarm
        target.setMinAlarmF3(source.getMinAlarmF3());
        target.setMaxAlarmF3(source.getMaxAlarmF3());
        // set no cfc flag
        target.setNoCfcFlagF3(source.getNoCfcFlagF3());
        target.setNoPfcFlagF3(source.getNoPfcFlagF3());
        target.setNoPfcMinF3(source.getNoPfcMinF3());
        target.setNoPfcMaxF3(source.getNoPfcMaxF3());
        target.setEndDateF4(source.getEndDateF4());
        target.setShortageDateF4(source.getShortageDateF4());
        // set min stock and min box
        target.setMinStockDayF4(source.getMinStockDayF4());
        target.setMaxStockDayF4(source.getMaxStockDayF4());
        target.setMinBoxesF4(source.getMinBoxesF4());
        target.setMaxBoxesF4(source.getMaxBoxesF4());
        // set alarm
        target.setMinAlarmF4(source.getMinAlarmF4());
        target.setMaxAlarmF4(source.getMaxAlarmF4());
        // set no cfc flag
        target.setNoCfcFlagF4(source.getNoCfcFlagF4());
        target.setNoPfcFlagF4(source.getNoPfcFlagF4());
        target.setNoPfcMinF4(source.getNoPfcMinF4());
        target.setNoPfcMaxF4(source.getNoPfcMaxF4());
        // set end date and shortage date
        target.setEndDateF5(source.getEndDateF5());
        target.setShortageDateF5(source.getShortageDateF5());
        // set min stock and min box
        target.setMinStockDayF5(source.getMinStockDayF5());
        target.setMaxStockDayF5(source.getMaxStockDayF5());
        target.setMinBoxesF5(source.getMinBoxesF5());
        target.setMaxBoxesF5(source.getMaxBoxesF5());
        // set alarm
        target.setMinAlarmF5(source.getMinAlarmF5());
        target.setMaxAlarmF5(source.getMaxAlarmF5());
        // set no cfc flag
        target.setNoCfcFlagF5(source.getNoCfcFlagF5());
        target.setNoPfcFlagF5(source.getNoPfcFlagF5());
        target.setNoPfcMinF5(source.getNoPfcMinF5());
        target.setNoPfcMaxF5(source.getNoPfcMaxF5());
        // set end date and shortage date
        target.setEndDateF6(source.getEndDateF6());
        target.setShortageDateF6(source.getShortageDateF6());
        // set min stock and min box
        target.setMinStockDayF6(source.getMinStockDayF6());
        target.setMaxStockDayF6(source.getMaxStockDayF6());
        target.setMinBoxesF6(source.getMinBoxesF6());
        target.setMaxBoxesF6(source.getMaxBoxesF6());
        // set alarm
        target.setMinAlarmF6(source.getMinAlarmF6());
        target.setMaxAlarmF6(source.getMaxAlarmF6());
        // set no cfc flag
        target.setNoCfcFlagF6(source.getNoCfcFlagF6());
        target.setNoPfcFlagF6(source.getNoPfcFlagF6());
        target.setNoPfcMinF6(source.getNoPfcMinF6());
        target.setNoPfcMaxF6(source.getNoPfcMaxF6());

        // alarm 3
        target.setEndDate3(source.getEndDate3());
        target.setShortageDate3(source.getShortageDate3());
        target.setStockDay3(source.getStockDay3());
        target.setNoMaster3(source.getNoMaster3());
        target.setShortageAddOn3(source.getShortageAddOn3());
        target.setSafetyAlarm3(source.getSafetyAlarm3());
        target.setSafetyAddOn3(source.getSafetyAddOn3());

        // alarm 4
        target.setTotalCfcQty4(source.getTotalCfcQty4());
        target.setOverRatioDate4(source.getOverRatioDate4());
        target.setFluctuationRatio4(source.getFluctuationRatio4());
        target.setGapValue4(source.getGapValue4());

        // alert level
        target.setAlertLevel(source.getAlertLevel());
    }

    /**
     * Prepare Alarm 1A for Stock Status.
     * 
     * @param stockStatus stock Status information
     * @param isAlarmB is it alarm 1B
     */
    private void prepareAlarm1ForStock(TntStockStatusEx stockStatus, boolean isAlarmB) {

        // End of Last day Stock Qty
        BigDecimal lastStockQty = stockStatus.getTotalImpWhsQty();
        lastStockQty = DecimalUtil.add(lastStockQty, stockStatus.getTotalPreparedObQty());
        lastStockQty = DecimalUtil.add(lastStockQty, stockStatus.getTotalEciOnholdQty());
        if (isAlarmB) {
            lastStockQty = DecimalUtil.add(lastStockQty, stockStatus.getTotalInRackQty());
        }

        // If Part Master.SA_CUST_STOCK_FLAG = 1:Y, means that we need consider Customer Stock, so set
        if (stockStatus.getSaCustStockFlag().equals(CustStockFlag.Y)) {
            lastStockQty = DecimalUtil.add(lastStockQty, stockStatus.getCustStockQty());
        }

        // prepare stock days for alarm 1A
        prepareStockDayForAlarm(IntDef.INT_ZERO, lastStockQty, stockStatus, false);

        // Prepare next inbound plan for Alarm 1A
        prepareNextInboundPlan(lastStockQty, stockStatus);
    }

    /**
     * Prepare stock days for alarm 1A.
     * 
     * @param strIdx start index
     * @param lastStockQty last stock qty
     * @param stockStatus stock status
     * @param isNext is next inbound?
     */
    private void prepareStockDayForAlarm(Integer strIdx, BigDecimal lastStockQty, TntStockStatusEx stockStatus,
        boolean isNext) {

        // get default value
        Integer minAlarm = AlarmFlag.N;
        Integer maxAlarm = AlarmFlag.N;
        Integer noMaster = AlarmFlag.N;
        BigDecimal stockDays = null;
        BigDecimal boxes = null;

        // If Control by Box(Part Master.INVENTORY_BOX_FLAG = 1:Y), then
        if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.Y) && !isNext) {

            // Set No. of Boxes = End of Last day Stock Qty / Part Master.SPQ
            boxes = DecimalUtil.divide(lastStockQty, stockStatus.getSpq(), IntDef.INT_TWO);

            // Set Min Alarm Flag
            if (boxes.compareTo(StringUtil.toBigDecimal(stockStatus.getMinBox())) < IntDef.INT_ZERO) {
                // show min alarm
                minAlarm = AlarmFlag.Y;
            }

            // Set Max Alarm Flag
            if (boxes.compareTo(StringUtil.toBigDecimal(stockStatus.getMaxBox())) > IntDef.INT_ZERO) {
                // show min alarm
                maxAlarm = AlarmFlag.Y;
            }
        } else {
            // control by stock days
            // set stock days
            BigDecimal runQty = lastStockQty;

            // If Stock Qty not negative, will get Stock Information from Rundown detail information
            if (!DecimalUtil.isLess(runQty, BigDecimal.ZERO)) {

                // index
                int lpIdx = strIdx.intValue();
                List<TntRdDetail> rdDetailList = stockStatus.getRdDetailList();

                // Loop Rundown Detail in Rundown Detail Information List Start
                for (; lpIdx < rdDetailList.size(); lpIdx++) {

                    // get
                    TntRdDetail rdDetail = rdDetailList.get(lpIdx);

                    // If Rundown Detail.IMP_INB_PLAN_DATE > Part Master.END_CFC_DATE then break
                    if (stockStatus.getEndCfcDate() == null
                            || rdDetail.getImpInbPlanDate().after(stockStatus.getEndCfcDate())) {
                        break;
                    }

                    // If Run Qty < Rundown Detail.DAILY_USAGE_QTY then reset Stock Days and break
                    if (runQty.compareTo(rdDetail.getDaliyUsageQty()) < IntDef.INT_ZERO) {

                        // get stock days(because has qty, must be working day)
                        BigDecimal digitDays = DecimalUtil.divide(runQty, rdDetail.getDaliyUsageQty(), IntDef.INT_ONE,
                            RoundingMode.DOWN);
                        // stock days up
                        stockDays = DecimalUtil.add(stockDays, digitDays);
                        // reset run qty(DecimalUtil.subtract(runQty, rdDetail.getDaliyUsageQty())?)
                        runQty = new BigDecimal(IntDef.INT_N_ONE);

                        // break
                        break;
                    } else {

                        // If Run-down Detail.WORKING_FLAG = 1:Y or Run-down Detail.DAILY_USAGE_QTY is not empty
                        if (DecimalUtil.isGreater(rdDetail.getDaliyUsageQty(), BigDecimal.ZERO)
                                || rdDetail.getWorkingFlag().equals(WorkingDay.WORKING_DAY)) {
                            // day up
                            stockDays = DecimalUtil.add(stockDays, BigDecimal.ONE);
                        }
                    }
                    
                    // reset runqty
                    runQty = DecimalUtil.subtract(runQty, rdDetail.getDaliyUsageQty());
                }
            }

            // If Stock Days = empty, then
            if (stockDays != null) {

                // Set Min Alarm Flag
                if (!isNext) {
                    if (stockDays.compareTo(StringUtil.toBigDecimal(stockStatus.getMinStock())) < IntDef.INT_ZERO) {
                        // show min alarm
                        minAlarm = AlarmFlag.Y;
                    }

                    // Set Max Alarm Flag
                    if (stockDays.compareTo(StringUtil.toBigDecimal(stockStatus.getMaxStock())) > IntDef.INT_ZERO) {
                        // show min alarm
                        maxAlarm = AlarmFlag.Y;
                    }
                }

                // if always has qty
                if (!DecimalUtil.isLess(runQty, BigDecimal.ZERO)) {
                    noMaster = NoCfcFlag.Y;
                }
            } else {
                // no cfc flag
                noMaster = NoCfcFlag.Y;
            }
        }

        // set into stock status
        if (stockStatus.getNoMaster1a() == null && !isNext) {

            // set alarm 1A
            stockStatus.setMinAlarm1a(minAlarm);
            stockStatus.setMaxAlarm1a(maxAlarm);
            stockStatus.setNoMaster1a(noMaster);
            stockStatus.setStockBoxes1a(boxes);
            stockStatus.setStockDays1a(stockDays);
            stockStatus.setStockQty1a(lastStockQty);
        } else if (stockStatus.getNoMasterPlan1a() == null && isNext) {

            // set alarm 1A plan
            stockStatus.setNoMasterPlan1a(noMaster);
            stockStatus.setStockDaysPlan1a(stockDays);
        } else if (stockStatus.getNoMaster1b() == null && !isNext) {

            // set alarm 1B
            stockStatus.setMinAlarm1b(minAlarm);
            stockStatus.setMaxAlarm1b(maxAlarm);
            stockStatus.setNoMaster1b(noMaster);
            stockStatus.setStockBoxes1b(boxes);
            stockStatus.setStockDays1b(stockDays);
            stockStatus.setStockQty1b(lastStockQty);
        } else {

            // set alarm 1b plan
            stockStatus.setNoMasterPlan1b(noMaster);
            stockStatus.setStockDaysPlan1b(stockDays);
        }
    }

    /**
     * Prepare next inbound plan for Alarm.
     * 
     * @param lastEndingStock ending stock of last
     * @param stockStatus stock status
     */
    private void prepareNextInboundPlan(BigDecimal lastEndingStock, TntStockStatusEx stockStatus) {

        // check is has next inbound or not
        if (stockStatus.getNextInboundDate() != null) {

            // If Inbound Plan Date >= Part Master.END_CFC_DATE
            if (stockStatus.getEndCfcDate() == null
                    || stockStatus.getNextInboundDate().after(stockStatus.getEndCfcDate())) {

                // set value
                if (stockStatus.getNoMasterPlan1a() == null) {
                    stockStatus.setNoMasterPlan1a(NoCfcFlag.Y);
                } else {
                    stockStatus.setNoMasterPlan1b(NoCfcFlag.Y);
                }
            } else {

                // Get ending stock day of In-bound Plan Date
                BigDecimal stockDays = null;
                BigDecimal loopStock = lastEndingStock;
                int idx = IntDef.INT_ZERO;

                // get rundown detail
                List<TntRdDetail> rdDetailList = stockStatus.getRdDetailList();

                // Loop Rundown Detail in Rundown Detail Information List Start
                for (; idx < rdDetailList.size(); idx++) {

                    // get detail
                    TntRdDetail rdDetail = rdDetailList.get(idx);

                    // If Rundown Detail.IMP_INB_PLAN_DATE > Inbound Plan Date then break
                    if (rdDetail.getImpInbPlanDate().after(stockStatus.getNextInboundDate())) {
                        break;
                    }

                    // If Ending Stock < Rundown Detail.DAILY_USAGE_QTY then
                    if (loopStock.compareTo(rdDetail.getDaliyUsageQty()) < IntDef.INT_ZERO) {
                        // set stockDays
                        if (stockDays == null) {

                            // If Negative Days = 0 then
                            BigDecimal digitDays = DecimalUtil.divide(loopStock, rdDetail.getDaliyUsageQty(),
                                IntDef.INT_ONE, RoundingMode.DOWN);
                            // set stock days
                            stockDays = DecimalUtil.subtract(digitDays, BigDecimal.ONE);
                        } else {

                            // add -1
                            if (DecimalUtil.isGreater(rdDetail.getDaliyUsageQty(), BigDecimal.ZERO)) {

                                // if is working day or has qty
                                stockDays = DecimalUtil.subtract(stockDays, BigDecimal.ONE);
                            }
                        }
                    } else {
                        stockDays = null;
                    }

                    // set ending stock
                    loopStock = DecimalUtil.subtract(loopStock, rdDetail.getDaliyUsageQty());
                }

                // Re-Calculate Ending Stock, add Inbound plan Qty
                loopStock = DecimalUtil.add(loopStock, stockStatus.getNextInboundQty());

                // If still Ending Stock < 0, then
                if (loopStock.compareTo(BigDecimal.ZERO) >= 0) {

                    // prepare
                    prepareStockDayForAlarm(idx, loopStock, stockStatus, true);
                } else {
                    // set value
                    if (stockStatus.getNoMasterPlan1a() == null) {

                        // set 1A
                        stockStatus.setNoMasterPlan1a(NoCfcFlag.N);
                        stockStatus.setStockDaysPlan1a(stockDays);
                    } else {

                        // set 1B
                        stockStatus.setNoMasterPlan1b(NoCfcFlag.N);
                        stockStatus.setStockDaysPlan1b(stockDays);
                    }
                }
            }
        }
    }

    /**
     * Prepare Alarm 2 for Stock Status.
     * 
     * @param stockStatus stock Status information
     */
    private void prepareAlarm2ForStock(TntStockStatusEx stockStatus) {

        // define
        int index = IntDef.INT_ZERO;
        // Prepare Alarm information for Target Month
        index = getDetailInfoForAlarm2(stockStatus, SimulationType.TARGET, index);
        // Prepare Alarm information for each Forecast
        int pfcNum = stockStatus.getForecastNum().intValue();
        for (int fcn = IntDef.INT_ONE; fcn <= pfcNum; fcn++) {
            // Prepare Alarm information for Target Month
            getDetailInfoForAlarm2(stockStatus, fcn + IntDef.INT_ONE, index);
        }
    }

    /**
     * get detail alarm information from rundown detail.
     * 
     * @param stockStatus stock status information
     * @param simulationType Simulation Type
     * @param strIdx start index
     * 
     * @return last index of detail information
     */
    private int getDetailInfoForAlarm2(TntStockStatusEx stockStatus, int simulationType, int strIdx) {

        // Set Min Stock Days = empty
        BigDecimal minStockDays = null;
        // Set Max Stock Days = empty
        BigDecimal maxStockDays = null;
        // Set Min No. of Boxes = empty
        BigDecimal minStockBoxes = null;
        // Set Max No. of Boxes = empty
        BigDecimal maxStockBoxes = null;
        // Set No CFC Flag = empty
        Integer noCfcFlag = NoCfcFlag.N;
        // No Min Flag
        Integer noMinFlag = NoCfcFlag.N;
        // No Max Flag
        Integer noMaxFlag = NoCfcFlag.N;
        // Set No PFC Flag = empty
        Integer noPfcFlag = NoPfcFlag.N;
        // Set Simulation End Date = empty
        Date simuEndDate = null;
        // Set Shortage Date = empty
        Date shortDate = null;

        // set index
        int idx = strIdx;
        List<TntRdDetail> rdDetailList = stockStatus.getRdDetailList();

        // Loop Rundown Detail in Rundown Detail Information List Start
        for (; idx < rdDetailList.size(); idx++) {

            // get detail
            TntRdDetail detail = rdDetailList.get(idx);

            // if does not has any simulation
            if (detail.getSimulationType() == null) {
                break;
            }

            // get simulation type
            if (simulationType > detail.getSimulationType().intValue()) {
                continue;
            } else if (simulationType < detail.getSimulationType().intValue()) {
                break;
            }

            // if same, Reset each data like
            simuEndDate = detail.getImpInbPlanDate();
            noPfcFlag = detail.getNoPfcFlag();

            // check inventory by box
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.N)) {
                // if has stock days
                if (detail.getStockDays() != null) {

                    // If Min Stock Days > Rundown Detail.STOCK_DAYS
                    if (minStockDays == null || DecimalUtil.isGreater(minStockDays, detail.getStockDays())) {
                        minStockDays = detail.getStockDays();
                        noMinFlag = detail.getNoCfcFlag();
                    }

                    // If Max Stock Days > Rundown Detail.STOCK_DAYS
                    if (maxStockDays == null || DecimalUtil.isLess(maxStockDays, detail.getStockDays())) {
                        maxStockDays = detail.getStockDays();
                        noMaxFlag = detail.getNoCfcFlag();
                    }
                }
            } else {
                // if has ending stock
                if (detail.getEndingStock() != null) {

                    // get boxes
                    BigDecimal boxes = DecimalUtil
                        .divide(detail.getEndingStock(), stockStatus.getSpq(), IntDef.INT_TWO);

                    // If Min No. of Boxes > Rundown Detail.ENDING_STOCK
                    if (minStockBoxes == null || DecimalUtil.isGreater(minStockBoxes, boxes)) {
                        minStockBoxes = boxes;
                    }

                    // If Max No. of Boxes > Rundown Detail.ENDING_STOCK
                    if (maxStockBoxes == null || DecimalUtil.isLess(maxStockBoxes, boxes)) {
                        maxStockBoxes = boxes;
                    }
                }
            }

            // If Rundown Detail.ENDING_STOCK < 0 and Shortage Date is empty
            if (shortDate == null && DecimalUtil.isLess(detail.getEndingStock(), BigDecimal.ZERO)) {

                // set shortage date
                shortDate = detail.getImpInbPlanDate();
            }
        }

        // define
        Integer minAlarm = AlarmFlag.N;
        Integer maxAlarm = AlarmFlag.N;

        // if simuEndDate is null, mean current date is not in run-down range
        if (simuEndDate == null) {
            
            // get last order target month
            Date lastTargetMonth = DateTimeUtil.parseDate(stockStatus.getLastTargetMonth(),
                DateTimeUtil.FORMAT_YEAR_MONTH);
            // get target month
            Date targetMonth = DateTimeUtil.addMonth(lastTargetMonth, simulationType - IntDef.INT_ONE);
            // get last day of target month
            simuEndDate = DateTimeUtil.lastDay(targetMonth);
        } else {

            // reset
            if (stockStatus.getEndCfcDate() == null || simuEndDate.after(stockStatus.getEndCfcDate())) {
                noCfcFlag = NoCfcFlag.Y;
            }

            // check
            if (stockStatus.getInventoryBoxFlag().equals(InventoryByBox.N)) {
                // check min alarm
                if (minStockDays != null
                        && minStockDays.compareTo(new BigDecimal(stockStatus.getMinStock())) < IntDef.INT_ZERO) {
                    minAlarm = AlarmFlag.Y;
                } else if (noPfcFlag.equals(NoPfcFlag.Y) || noCfcFlag.equals(NoCfcFlag.Y)) {
                    minAlarm = null;
                    minStockDays = null;
                    noMinFlag = null;
                }
                // check max alarm
                if (maxStockDays != null
                        && maxStockDays.compareTo(new BigDecimal(stockStatus.getMaxStock())) > IntDef.INT_ZERO) {
                    maxAlarm = AlarmFlag.Y;
                } else if (noPfcFlag.equals(NoPfcFlag.Y) || noCfcFlag.equals(NoCfcFlag.Y)) {
                    maxAlarm = null;
                    maxStockDays = null;
                    noMaxFlag = null;
                }
            } else {
                // check min alarm
                if (minStockBoxes != null
                        && minStockBoxes.compareTo(new BigDecimal(stockStatus.getMinBox())) < IntDef.INT_ZERO) {
                    minAlarm = AlarmFlag.Y;
                } else if (noPfcFlag.equals(NoPfcFlag.Y) || noCfcFlag.equals(NoCfcFlag.Y)) {
                    minAlarm = null;
                    minStockBoxes = null;
                }
                // check max alarm
                if (maxStockBoxes != null
                        && maxStockBoxes.compareTo(new BigDecimal(stockStatus.getMaxBox())) > IntDef.INT_ZERO) {
                    maxAlarm = AlarmFlag.Y;
                } else if (noPfcFlag.equals(NoPfcFlag.Y) || noCfcFlag.equals(NoCfcFlag.Y)) {
                    maxAlarm = null;
                    maxStockBoxes = null;
                }
            }
        }

        // case simulationType
        switch (simulationType) {

            case SimulationType.TARGET:
                // set end date and shortage date
                stockStatus.setEndDateTm(simuEndDate);
                stockStatus.setShortageDateTm(shortDate);

                // set alarm
                stockStatus.setMinAlarmTm(minAlarm);
                stockStatus.setMaxAlarmTm(maxAlarm);

                // set min stock and min box
                stockStatus.setMinStockDayTm(minStockDays);
                stockStatus.setMaxStockDayTm(maxStockDays);
                stockStatus.setMinBoxesTm(minStockBoxes);
                stockStatus.setMaxBoxesTm(maxStockBoxes);

                // set no cfc flag
                stockStatus.setNoMasterMinTm(noMinFlag);
                stockStatus.setNoMasterMaxTm(noMaxFlag);

                break;
            case SimulationType.FORECAST1:
                // set end date and shortage date
                stockStatus.setEndDateF1(simuEndDate);
                stockStatus.setShortageDateF1(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF1(minStockDays);
                stockStatus.setMaxStockDayF1(maxStockDays);
                stockStatus.setMinBoxesF1(minStockBoxes);
                stockStatus.setMaxBoxesF1(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF1(minAlarm);
                stockStatus.setMaxAlarmF1(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF1(noCfcFlag);
                stockStatus.setNoPfcFlagF1(noPfcFlag);
                stockStatus.setNoPfcMinF1(noMinFlag);
                stockStatus.setNoPfcMaxF1(noMaxFlag);
                break;
            case SimulationType.FORECAST2:
                // set end date and shortage date
                stockStatus.setEndDateF2(simuEndDate);
                stockStatus.setShortageDateF2(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF2(minStockDays);
                stockStatus.setMaxStockDayF2(maxStockDays);
                stockStatus.setMinBoxesF2(minStockBoxes);
                stockStatus.setMaxBoxesF2(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF2(minAlarm);
                stockStatus.setMaxAlarmF2(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF2(noCfcFlag);
                stockStatus.setNoPfcFlagF2(noPfcFlag);
                stockStatus.setNoPfcMinF2(noMinFlag);
                stockStatus.setNoPfcMaxF2(noMaxFlag);
                break;
            case SimulationType.FORECAST3:
                // set end date and shortage date
                stockStatus.setEndDateF3(simuEndDate);
                stockStatus.setShortageDateF3(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF3(minStockDays);
                stockStatus.setMaxStockDayF3(maxStockDays);
                stockStatus.setMinBoxesF3(minStockBoxes);
                stockStatus.setMaxBoxesF3(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF3(minAlarm);
                stockStatus.setMaxAlarmF3(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF3(noCfcFlag);
                stockStatus.setNoPfcFlagF3(noPfcFlag);
                stockStatus.setNoPfcMinF3(noMinFlag);
                stockStatus.setNoPfcMaxF3(noMaxFlag);
                break;
            case SimulationType.FORECAST4:// set end date and shortage date
                stockStatus.setEndDateF4(simuEndDate);
                stockStatus.setShortageDateF4(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF4(minStockDays);
                stockStatus.setMaxStockDayF4(maxStockDays);
                stockStatus.setMinBoxesF4(minStockBoxes);
                stockStatus.setMaxBoxesF4(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF4(minAlarm);
                stockStatus.setMaxAlarmF4(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF4(noCfcFlag);
                stockStatus.setNoPfcFlagF4(noPfcFlag);
                stockStatus.setNoPfcMinF4(noMinFlag);
                stockStatus.setNoPfcMaxF4(noMaxFlag);
                break;
            case SimulationType.FORECAST5:
                // set end date and shortage date
                stockStatus.setEndDateF5(simuEndDate);
                stockStatus.setShortageDateF5(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF5(minStockDays);
                stockStatus.setMaxStockDayF5(maxStockDays);
                stockStatus.setMinBoxesF5(minStockBoxes);
                stockStatus.setMaxBoxesF5(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF5(minAlarm);
                stockStatus.setMaxAlarmF5(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF5(noCfcFlag);
                stockStatus.setNoPfcFlagF5(noPfcFlag);
                stockStatus.setNoPfcMinF5(noMinFlag);
                stockStatus.setNoPfcMaxF5(noMaxFlag);
                break;
            case SimulationType.FORECAST6:
                // set end date and shortage date
                stockStatus.setEndDateF6(simuEndDate);
                stockStatus.setShortageDateF6(shortDate);

                // set min stock and min box
                stockStatus.setMinStockDayF6(minStockDays);
                stockStatus.setMaxStockDayF6(maxStockDays);
                stockStatus.setMinBoxesF6(minStockBoxes);
                stockStatus.setMaxBoxesF6(maxStockBoxes);

                // set alarm
                stockStatus.setMinAlarmF6(minAlarm);
                stockStatus.setMaxAlarmF6(maxAlarm);

                // set no cfc flag
                stockStatus.setNoCfcFlagF6(noCfcFlag);
                stockStatus.setNoPfcFlagF6(noPfcFlag);
                stockStatus.setNoPfcMinF6(noMinFlag);
                stockStatus.setNoPfcMaxF6(noMaxFlag);
                break;
            default:
                break;
        }

        // return index
        return idx;
    }

    /**
     * Prepare Alarm 3 for Stock Status.
     * 
     * @param stockStatus stock Status information
     */
    private void prepareAlarm3ForStock(TntStockStatusEx stockStatus) {

        // Get Simulation End Date of Alarm3, cause Simulation End Date is End of Target month, so
        Date tagerMonth = DateTimeUtil.parseDate(stockStatus.getLastTargetMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
        Date simuEndDate = DateTimeUtil.lastDay(tagerMonth);

        // If Simulation End Date <= Stock Date, then all data for alarm3 will set as empty
        if (!simuEndDate.after(stockStatus.getStockDate())) {
            return;
        }

        // Total Available Qty
        BigDecimal totalAvaiQty = stockStatus.getTotalImpWhsQty();
        totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getTotalPreparedObQty());
        totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getTotalEciOnholdQty());
        totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getTotalInRackQty());
        totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getTotalNgOnholdQty());
        totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getImpBalanceQty());
        //totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getOnShippingQty());
        if (stockStatus.getOsCustStockFlag().equals(CustStockFlag.Y)) {
            totalAvaiQty = DecimalUtil.add(totalAvaiQty, stockStatus.getCustStockQty());
        }

        // Run Qty
        BigDecimal runQty = totalAvaiQty;
        // Shortage Date
        Date shortageDate = null;
        // Negative Days
        BigDecimal negativeDays = null;
        // boolean
        boolean isNoCfcFlag = false;

        // Loop Customer Daily Usage in Customer Daily Usage List Start
        // set index
        List<TntRdDetail> rdDetailList = stockStatus.getRdDetailList();
        // loop
        for (int idx = IntDef.INT_ZERO; idx < rdDetailList.size(); idx++) {

            // get usage
            TntRdDetail dailyUsage = rdDetailList.get(idx);

            // If Customer Daily Usage.CFC_DATE > Simulation End Date then break
            if (dailyUsage.getImpInbPlanDate().after(simuEndDate)) {
                break;
            }

            // If Customer Daily Usage.CFC_DATE > End customer forecast date then break
            if (stockStatus.getEndCfcDate() == null
                    || dailyUsage.getImpInbPlanDate().after(stockStatus.getEndCfcDate())) {
                isNoCfcFlag = true;
                break;
            }

            // Prepare Negative Days
            if (runQty.compareTo(dailyUsage.getDaliyUsageQty()) < IntDef.INT_ZERO) {

                // check negativeDays
                if (negativeDays == null) {
                    // get negative days
                    negativeDays = DecimalUtil.divide(runQty, dailyUsage.getDaliyUsageQty(), IntDef.INT_ONE,
                        RoundingMode.DOWN);
                    negativeDays = DecimalUtil.subtract(negativeDays, BigDecimal.ONE);
                } else {

                    // if working day, day down
                    if (DecimalUtil.isGreater(dailyUsage.getDaliyUsageQty(), BigDecimal.ZERO)) {

                        // set days
                        negativeDays = DecimalUtil.subtract(negativeDays, BigDecimal.ONE);
                    }
                }

                // shortage date
                if (shortageDate == null) {
                    shortageDate = dailyUsage.getImpInbPlanDate();
                }
            } else {
                negativeDays = null;
            }

            // reset runQty
            runQty = DecimalUtil.subtract(runQty, dailyUsage.getDaliyUsageQty());
        }

        // reset
        stockStatus.setEndDate3(simuEndDate);
        stockStatus.setShortageDate3(shortageDate);
        stockStatus.setNoMaster3(NoCfcFlag.N);

        // Get Ending Stock Days From Rundown Information
        if (!isNoCfcFlag) {

            // is need do Stock days?
            boolean isNeedDays = true;
            boolean isNeedAddOn = true;

            // if Ending Stock < 0
            if (negativeDays != null) {

                // set stock days 3
                stockStatus.setStockDay3(negativeDays);
                stockStatus.setShortageAddOn3(DecimalUtil.subtract(BigDecimal.ZERO, runQty));
                stockStatus.setSafetyAlarm3(AlarmFlag.Y);
                isNeedDays = false;
            } else {

                // Ending Stock >=0 means does not has any shortage, so
                stockStatus.setShortageAddOn3(BigDecimal.ZERO);
            }

            // usage days
            int usDays = IntDef.INT_ZERO;
            BigDecimal totalQty = BigDecimal.ZERO;
            // Get Ending Stock Days and safety stock
            List<TntCfcDay> usageList = stockStatus.getDailyUsageList();
            // loop
            for (TntCfcDay usage : usageList) {

                // get usage
                if (!usage.getCfcDate().after(simuEndDate)) {
                    continue;
                }

                // check is days?
                if (DecimalUtil.isZero(usage.getCfcQty())) {
                    // get working days
                    if (CalendarManager.checkWorkingDay(stockStatus.getCustCalendarList(), usage.getCfcDate())) {
                        usDays++;
                    }
                } else {
                    usDays++;
                }

                // set total qty
                totalQty = DecimalUtil.add(totalQty, usage.getCfcQty());

                // already exceeded
                if (isNeedDays && totalQty.compareTo(runQty) > IntDef.INT_ZERO) {

                    // get digit days
                    BigDecimal passQty = DecimalUtil.subtract(totalQty, runQty);
                    BigDecimal passDay = DecimalUtil
                        .divide(passQty, usage.getCfcQty(), IntDef.INT_ONE, RoundingMode.UP);

                    // get stock days
                    BigDecimal stockDays = DecimalUtil.subtract(new BigDecimal(usDays), passDay);

                    // set into
                    stockStatus.setStockDay3(stockDays);
                    isNeedDays = false;
                }

                // get add on qty
                if (isNeedAddOn && usDays == stockStatus.getOrderSafetyStock().intValue()) {
                    // if total over run qty
                    if (totalQty.compareTo(runQty) > IntDef.INT_ZERO) {
                        // set
                        stockStatus.setSafetyAddOn3(DecimalUtil.subtract(totalQty, runQty));
                        stockStatus.setSafetyAlarm3(AlarmFlag.Y);
                    } else {
                        // set as zero
                        stockStatus.setSafetyAddOn3(BigDecimal.ZERO);
                        stockStatus.setSafetyAlarm3(AlarmFlag.N);
                    }

                    // reset
                    isNeedAddOn = false;
                }

                // break
                if (!isNeedDays && !isNeedAddOn) {
                    break;
                }
            }

            // set Safety Alarm 3
            if (isNeedAddOn) {
                // set as alarm
                stockStatus.setSafetyAlarm3(AlarmFlag.Y);
            }

            // set no master flag 3
            if (isNeedDays) {

                // set no master flag
                stockStatus.setNoMaster3(NoCfcFlag.Y);

                // set stock days
                stockStatus.setStockDay3(new BigDecimal(usDays));
            }
        } else {
            stockStatus.setNoMaster3(NoCfcFlag.Y);
        }
    }

    /**
     * Prepare Alarm 4 for Stock Status.
     * 
     * @param stockStatus stock Status information
     */
    private void prepareAlarm4ForStock(TntStockStatusEx stockStatus) {

        // If Part Master.TOTAL_CFC_QTY is null, means no customer usage for current month
        if (stockStatus.getTotalCfcQty4() == null) {
            return;
        }

        // Set Total Outbound Qty
        BigDecimal totalObQty = null;
        // Set Max Outbound Qty = (1 + Part Master.OUTBOUND_FLUCTUATION) * Part Master.TOTAL_CFC_QTY
        BigDecimal maxObFluc = DecimalUtil.add(BigDecimal.ONE, stockStatus.getOutboundFluctuation());
        BigDecimal maxObQty = DecimalUtil.multiply(maxObFluc, stockStatus.getTotalCfcQty4());

        // Loop Actual Outbound Detial in Imp Actual Outbound Detial In Current Month List Start
        for (TnfImpStockByDay actOutbound : stockStatus.getActOutboundList()) {

            // add into total out bound qty
            totalObQty = DecimalUtil.add(totalObQty, actOutbound.getDayOutboundQty());

            // check Over Ratio Date
            if (stockStatus.getOverRatioDate4() == null) {

                // if over
                if (totalObQty.compareTo(maxObQty) > IntDef.INT_ZERO) {
                    // set over ratio Date
                    stockStatus.setOverRatioDate4(actOutbound.getEndDate());
                }
            }
        }

        // Get Fluctuation ratio of Actual Outbound and Monthly Plan
        BigDecimal fluctRatio = DecimalUtil.multiply(totalObQty, new BigDecimal(IntDef.INT_HUNDRED));
        fluctRatio = DecimalUtil.divide(fluctRatio, stockStatus.getTotalCfcQty4(), IntDef.INT_ONE);
        stockStatus.setFluctuationRatio4(fluctRatio);

        // Get The Gap between Actual Outbound and Monthly Plan
        stockStatus.setGapValue4(DecimalUtil.subtract(totalObQty, stockStatus.getTotalCfcQty4()));
    }

    /**
     * Prepare Alert Level of Stock status.
     * 
     * @param stockStatus stock Status information
     */
    private void prepareAlertLevelForStock(TntStockStatusEx stockStatus) {

        // set alert level
        Integer alertLevel = AlertLevel.LEVEL0;

        // get alert level
        if (stockStatus.getShortageDateTm() != null) {
            alertLevel = AlertLevel.LEVEL1;
        } else if (stockStatus.getMinAlarm1a() != null && stockStatus.getMinAlarm1a().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL1;
        } else if (stockStatus.getMinAlarm1b() != null && stockStatus.getMinAlarm1b().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL1;
        } else if (stockStatus.getMinAlarm1b() != null && stockStatus.getMinAlarm1b().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL1;
        } else if (stockStatus.getMinAlarmTm() != null && stockStatus.getMinAlarmTm().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL2;
        } else if (stockStatus.getSafetyAlarm3() != null && stockStatus.getSafetyAlarm3().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL2;
        } else if (stockStatus.getShortageDate3() != null) {
            alertLevel = AlertLevel.LEVEL2;
        } else if (stockStatus.getMinAlarmF1() != null && stockStatus.getMinAlarmF1().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getMinAlarmF2() != null && stockStatus.getMinAlarmF2().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getMinAlarmF3() != null && stockStatus.getMinAlarmF3().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getMinAlarmF4() != null && stockStatus.getMinAlarmF4().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getMinAlarmF5() != null && stockStatus.getMinAlarmF5().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getMinAlarmF6() != null && stockStatus.getMinAlarmF6().equals(AlarmFlag.Y)) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF1() != null) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF2() != null) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF3() != null) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF4() != null) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF5() != null) {
            alertLevel = AlertLevel.LEVEL3;
        } else if (stockStatus.getShortageDateF6() != null) {
            alertLevel = AlertLevel.LEVEL3;
        }

        // set level
        stockStatus.setAlertLevel(alertLevel);
    }

    /**
     * Prepare email alert information.
     * 
     * @param office office information
     * @param onlineFlag online Flag
     */
    private void prepareEmailAlert(BasePartsInfoEntity office, Integer onlineFlag) {
        
        // check is need send mail or not 
        if (!sendEmailService.getMailSendFlag()) {
            return;
        }

        // if refresh, no alarm mail(add by Yinchuan Liu for revise from wee tat)
        if (onlineFlag.equals(OnlineFlag.ONLINE)) {
            return;
        }

        // Pick up all user customer which need send mail from TNM_EMAIL_ALERT and TNT_STOCK_STATUS
        List<BasePartsInfoEntity> userMailList = cpsssb01Service.getUserMailInfoList(office);
        Timestamp officeTime = cpsssb01Service.getDBDateTime(office.getTimeZone());
        String clintTime = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_FULL, officeTime);
        String tempFilePath = this.getTempFolder(UUID.randomUUID().toString());
        File tempDirectory = new File(tempFilePath);
        FileUtil.createDirectory(tempDirectory);

        // do mail for each user in current office
        for (BasePartsInfoEntity userInfo : userMailList) {

            // set stock date?
            //userInfo.setStockDate(office.getStockDate());
            userInfo.setOfficeCode(office.getOfficeCode());
            userInfo.setClintTime(clintTime);
            userInfo.setOfficeTime(officeTime);
            userInfo.setTempFilePath(tempFilePath);

            // prepare language
            Language lang = Language.ENGLISH;
            if (userInfo.getLang() != null) {
                lang = MessageManager.getLanguage(userInfo.getLang());
            } else {
                userInfo.setLang(lang.getCode());
            }

            // prepare alert information files in template
            super.createSXSSExcelWithTemplate(STOCK_STATUS_FILE_ID, userInfo, lang);

            // get create file name
            String fullFilePath = getFileSavePathAndName(STOCK_STATUS_FILE_ID, userInfo, lang);

            // get mail information
            List<BasePartsInfoEntity> mailInfoList = cpsssb01Service.getUserMailAlarmInfoList(userInfo);

            // send mail to user
            try {
                this.sendUserMail(userInfo, fullFilePath, mailInfoList, onlineFlag);
            } catch (IOException e) {
                logger.error("Send mail to User(UserId: " + userInfo.getUserId() + ") failture");
            }
        }
        
        // delete files
        FileUtil.deleteAllFile(tempDirectory);
    }
    
    /**
     * Send mail to user.
     * 
     * @param userInfo user information
     * @param fullFilePath attached file full path
     * @param mailInfoList mail information list
     * @param onlineFlag onlineFlag
     * @throws IOException  exception
     */
    private void sendUserMail(BasePartsInfoEntity userInfo, String fullFilePath,
        List<BasePartsInfoEntity> mailInfoList, Integer onlineFlag) throws IOException {

        // get user language
        Language lang = MessageManager.getLanguage(userInfo.getLang());

        // get mail address
        String mailTo = userInfo.getMailAddr();

        // prepare mail title
        String title = MessageManager.getMessage("CPSSSB01_MailTitle_StockAlarm", lang.getLocale());
        title = MessageFormat.format(title, new Object[] { DateTimeUtil.formatDate(userInfo.getOfficeTime()) });

        // Get mail body
        String templatePath = StringUtil.formatMessage(MailUtil.MAIL_TEMPLATE_FILE_TYPE,
            DOWNLOAD_TEMPLATES_PATH, MailUtil.MAIL_TEMPLATE_STOCK_ALARM);
        
        // get body content
        String bodyContent = MailUtil.getTemplate(templatePath, lang);
        
        // prepare body mail table
        StringBuffer alarmInfoSb = new StringBuffer();
        alarmInfoSb.append("<table border='1' style='border-collapse:collapse;text-align:center'>");
        alarmInfoSb.append("<tr style='background-color:#FFFF00'><td width='80px'>");
        alarmInfoSb.append(MessageManager.getMessage("CPSSSB01_MailBody_No", lang.getLocale()));
        alarmInfoSb.append("</td><td width='100px'>");
        alarmInfoSb.append(MessageManager.getMessage("CPSSSB01_MailBody_CustomerCode", lang.getLocale()));
        alarmInfoSb.append("</td><td width='180px'>");
        alarmInfoSb.append(MessageManager.getMessage("CPSSSB01_MailBody_Alarm1", lang.getLocale()));
        alarmInfoSb.append("</td><td width='180px'>");
        alarmInfoSb.append(MessageManager.getMessage("CPSSSB01_MailBody_Alarm2", lang.getLocale()));
        alarmInfoSb.append("</td><td width='180px'>");
        alarmInfoSb.append(MessageManager.getMessage("CPSSSB01_MailBody_Alarm3", lang.getLocale()));
        alarmInfoSb.append("</td></tr>");
        
        // preapre content
        int row = IntDef.INT_ONE;
        for(BasePartsInfoEntity mailInfo : mailInfoList) {
            alarmInfoSb.append("<tr><td style='text-align:right'>");
            alarmInfoSb.append(row++);
            alarmInfoSb.append("</td><td style='text-align:left'>");
            alarmInfoSb.append(mailInfo.getCustomerCode());
            alarmInfoSb.append("</td><td style='text-align:right'>");
            alarmInfoSb.append(mailInfo.getAlert1Cnt());
            alarmInfoSb.append("</td><td style='text-align:right'>");
            alarmInfoSb.append(mailInfo.getAlert2Cnt());
            alarmInfoSb.append("</td><td style='text-align:right'>");
            alarmInfoSb.append(mailInfo.getAlert3Cnt());
            alarmInfoSb.append("</td></tr>");
        }
        
        // append last one
        alarmInfoSb.append("</table>");
        
        // replace
        bodyContent = MessageFormat.format(bodyContent,
            new Object[] { userInfo.getOfficeCode(), alarmInfoSb.toString() });

        // create temp folder
        Vector<File> files = new Vector<File>();
        files.add(new File(fullFilePath));

        // Send mail

        try {
            if (onlineFlag.equals(OnlineFlag.ONLINE)) {
                MailUtilForOnline mailForOnline = new MailUtilForOnline(mailTo, title, bodyContent, files, true, null);
                mailForOnline.run();
            } else {
                MailUtil mail = new MailUtil();
                mail.sendMailForBatch(mailTo, title, bodyContent, files, true, null);
            }
        } catch (Exception e) {
            logger.error("get mail template failed!");
            e.printStackTrace();
        }
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     * @param lang Language
     */
    @Override
    protected <T extends BaseEntity> void writeDynamicTemplate(T param, Workbook workbook, Language lang) {

        // cast parameter
        BasePartsInfoEntity castUser = (BasePartsInfoEntity) param;

        // define maps
        headMap = new HashMap<Integer, String>();

        // set values
        BaseParam bParam = new BaseParam();
        bParam.setCurrentOfficeId(castUser.getOfficeId());
        bParam.setSwapData("userId", castUser.getUserId());

        // get stock status header
        TntStockStatusHeader header = stockStatusService.getStockStatusHeader(bParam);

        // prepare header
        StockStatusManager.setHeaderForStockStatus(workbook, lang, headMap, header);
    }

    /**
     * Write content to excel.
     *
     * @param param parameter
     * @param wbTemplate workbook template
     * @param wbOutput workbook out put
     * @param fieldId field Id
     * @param lang Language
     */
    @Override
    protected <T extends BaseEntity> void writeContentToExcel(T param, Workbook wbTemplate, Workbook wbOutput, Language lang) {
        
        // cast parameter
        BasePartsInfoEntity castUser = (BasePartsInfoEntity) param;

        // get header map
        styleMap = new HashMap<String, CellStyle>();
        Date serverTime = stockStatusService.getDBDateTime(castUser.getTimeZone());

        // set values
        BaseParam bParam = new BaseParam();
        bParam.setCurrentOfficeId(castUser.getOfficeId());
        bParam.setSwapData("userId", castUser.getUserId());

        // get stock status list
        List<TntStockStatusEx> stockStatusList = stockStatusService.getStockStatusInfoList(bParam, lang);

        // get style sheet
        StockStatusManager.prepStyleMapStockStatus(wbTemplate, styleMap, lang);

        // do cast
        SXSSFWorkbook castWorkBoook = (SXSSFWorkbook) wbOutput;
        
        // prepare date list
        Date[] dateList = new Date[IntDef.INT_FOUR];
        dateList[IntDef.INT_ZERO] = serverTime;
        // prepare Sync time
        stockStatusService.prepareSyncDateTime(castUser.getOfficeId(), dateList);

        // prepare data
        StockStatusManager.setStockStatusDetailInfo(castWorkBoook, stockStatusList, lang, styleMap, headMap,
            castUser.getOfficeCode(), dateList);
    }
    

    /**
     * Prepare file save path.
     * 
     * @param fileId the fileId
     * @param param parameter
     * @param <T> as BaseEntity
     * @param lang Language
     * @return file path
     */
    @Override
    protected <T extends BaseEntity> String getFileSavePathAndName(String fileId, T param, Language lang) {
        // do nothing in base
        logger.debug("getFileSavePathAndName in BaseFileBatch");

        // cast parameter
        BasePartsInfoEntity castUser = (BasePartsInfoEntity) param;
        // get file path
        StringBuffer fullName = new StringBuffer();
        fullName.append(castUser.getTempFilePath());
        fullName.append(File.separatorChar);
        fullName.append(STOCK_STATUS_FILE_NAME);
        fullName.append(StringConst.UNDERLINE);
        fullName.append(castUser.getClintTime());
        fullName.append(StringConst.DOT);
        fullName.append(FileType.EXCEL.getSuffix());

        // return full name
        return fullName.toString();
    }

    /**
     * Prepare file save path.
     * 
     * @param tempFilePath tempFilePath
     * @return temp file path
     */
    private String getTempFolder(String tempFilePath) {
        // do nothing in base
        logger.debug("getFileSavePathAndName in BaseFileBatch");
        StringBuffer fullName = new StringBuffer();
        fullName.append(ConfigManager.getSystemTempPath());
        fullName.append(File.separatorChar);
        fullName.append(tempFilePath);

        // return full name
        return fullName.toString();
    }
}
