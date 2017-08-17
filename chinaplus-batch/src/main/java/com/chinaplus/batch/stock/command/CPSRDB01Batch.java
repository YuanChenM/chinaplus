/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.stock.command;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.chinaplus.batch.stock.service.CPSRDB01Service;
import com.chinaplus.common.bean.TnmCalendarDetailEx;
import com.chinaplus.common.bean.TnmCalendarPartyEx;
import com.chinaplus.common.bean.TntRdDetailAttachEx;
import com.chinaplus.common.consts.CodeConst.AttachType;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CalendarParty;
import com.chinaplus.common.consts.CodeConst.ImpStockFlag;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.InventoryByBox;
import com.chinaplus.common.consts.CodeConst.NoCfcFlag;
import com.chinaplus.common.consts.CodeConst.NoPfcFlag;
import com.chinaplus.common.consts.CodeConst.NotInRDReasonType;
import com.chinaplus.common.consts.CodeConst.SimulationEndDayPattern;
import com.chinaplus.common.consts.CodeConst.SimulationType;
import com.chinaplus.common.consts.CodeConst.WorkingDay;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TntCfcDay;
import com.chinaplus.common.entity.TntNotInRundown;
import com.chinaplus.common.entity.TntRdAttachCfc;
import com.chinaplus.common.entity.TntRdDetail;
import com.chinaplus.common.entity.TntRdMaster;
import com.chinaplus.common.util.CalendarManager;
import com.chinaplus.common.util.PartSortManager;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component(StockBatchId.CPSRDB01)
public class CPSRDB01Batch extends BaseFileBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPSRDB01Batch.class);

    /** The Service of Run-Down Batch */
    @Autowired
    private CPSRDB01Service cpsrdb01Service;

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

            // set process date
            param.setProcessDate(baseService.getDBDateTime(timeZone));
        }

        // return
        return param;
    }

    /**
     * Main process logic for rundown batch.
     * 
     * @param BaseBatchParam.
     * @return the result of current operation
     */
    @Override
    public boolean doOperate(BaseBatchParam baseParam) throws Exception {

        // batch process logic start
        logger.info("batch CPSRDB01Batch start......");

        // cast to StockComParam
        StockComParam param = (StockComParam) baseParam;

        // search parameter
        BasePartsInfoEntity offInfo = new BasePartsInfoEntity();
        offInfo.setOfficeCode(param.getOfficeCode());

        // pick up all office, and then loop by each office
        List<BasePartsInfoEntity> officeList = cpsrdb01Service.getOfficesFromDatabase(offInfo);
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
            List<BasePartsInfoEntity> partsList = doPrepareData(office);

            // Make Run-down Detail Information
            List<TntRdMaster> rundownList = doMakeRundownInfo(partsList);

            // do save all data
            cpsrdb01Service.doUpdateRundownInfo(office, rundownList);

            // batch process logic end
            logger.info("office(" + office.getOfficeCode() + ") process end......");
        }
        // batch process logic end
        logger.info("batch CPSRDB01Batch end......");

        // return OK.
        return true;
    }

    /**
     * Prepare Data from database for make Run-down.
     * 
     * @param paramInfo batch parameter
     * @return all data for run-down
     */
    private List<BasePartsInfoEntity> doPrepareData(BasePartsInfoEntity paramInfo) {

        // prepare parameter
        // run start date
        Date startDate = null;
        if (paramInfo.getOnlineFlag() != null) {
            // if online use current date
            startDate = paramInfo.getStockDate();
        } else {
            // if offline use next date
            startDate = DateTimeUtil.addDay(paramInfo.getStockDate(), IntDef.INT_ONE);
        }
        // last month
        Date lastMonth = DateTimeUtil.addMonth(startDate, IntDef.INT_N_ONE);
        // last month
        Date lastShareMonth = DateTimeUtil.addMonth(startDate, IntDef.INT_N_THREE);
        // lastEndStockDate
        paramInfo.setLastEndStockDate(DateTimeUtil.addMonth(startDate, IntDef.INT_N_TWO));
        // firstDayOfLastMonth
        paramInfo.setFirstDayOfLastMonth(DateTimeUtil.firstDay(lastMonth));
        // lastDayOfLastMonth
        paramInfo.setLastDayOfLastMonth(DateTimeUtil.lastDay(lastMonth));
        // firstShareDate
        paramInfo.setFirstShareDate(DateTimeUtil.firstDay(lastShareMonth));
        // lastShareDate
        paramInfo.setLastShareDate(DateTimeUtil.lastDay(startDate));
        // cfcMonth
        paramInfo.setCfcMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, startDate));

        // Basic Part Information
        List<BasePartsInfoEntity> partsMasterList = cpsrdb01Service.getPartsMasterInfo(paramInfo);

        // check data exists
        if (partsMasterList == null || partsMasterList.isEmpty()) {
            return new ArrayList<BasePartsInfoEntity>();
        }
        
        // define 
        Map<String, TntRdDetailAttachEx> effOfcMap = new HashMap<String, TntRdDetailAttachEx>();
        Map<String, Integer> calendarPartyMap = new HashMap<String, Integer>();
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap = new HashMap<Integer, List<TnmCalendarDetailEx>>();

        // Order Forecast Detail
        List<TntRdDetailAttachEx> ofcDetailList = cpsrdb01Service.getOrderForecastDetail(paramInfo);

        // effective Order Forecast Detail
        List<TntRdDetailAttachEx> effOfcList =  cpsrdb01Service.getEffectiveOrderForecast(paramInfo);
        this.makeEffectiveOfcMap(effOfcMap, effOfcList);

        // Order/Kanban Plan Detail
        List<TntRdDetailAttachEx> vvOrderList = cpsrdb01Service.getVVOrderDetail(paramInfo);
        List<TntRdDetailAttachEx> aisinKanbanList = cpsrdb01Service.getKanbanPlanDetail(paramInfo);

        // On-Shipping Detail
        List<TntRdDetailAttachEx> onShippingList = cpsrdb01Service.getOnShippingDetail(paramInfo);

        // Customer Daily Usage List
        List<TntCfcDay> dailyUsageList = cpsrdb01Service.getCustomerDailyUsageList(paramInfo);

        // Not in Rundown Detail
        List<TntNotInRundown> etdDelayVvList = cpsrdb01Service.getEtdDelayForVV(paramInfo);
        List<TntNotInRundown> etdDelayAisinList = cpsrdb01Service.getEtdDelayForAISIN(paramInfo);
        List<TntNotInRundown> ibDelayList = cpsrdb01Service.getInboundDelay(paramInfo);
        List<TntNotInRundown> ngOnHoldList = cpsrdb01Service.getNgOnHold(paramInfo);

        // Calendar List
        List<TnmCalendarPartyEx> cldPartyInfoList = cpsrdb01Service.getCalendarPartyInfo(paramInfo);
        List<TnmCalendarDetailEx> cldDetailList = cpsrdb01Service.getCalendarList(paramInfo);

        // Customer Usage Share List
        List<TntRdAttachCfc> usgShareList = cpsrdb01Service.getCustomerUsageShareList(paramInfo);

        // defined index
        int foreIdex = IntDef.INT_ZERO;
        int orderIdex = IntDef.INT_ZERO;
        int kanbanIdex = IntDef.INT_ZERO;
        int shipIdex = IntDef.INT_ZERO;
        int usageIdex = IntDef.INT_ZERO;
        int edtvvIdex = IntDef.INT_ZERO;
        int edtasIdex = IntDef.INT_ZERO;
        int indIdex = IntDef.INT_ZERO;
        int ngIdex = IntDef.INT_ZERO;
        int shaIdex = IntDef.INT_ZERO;

        // prepare parts master from
        for (BasePartsInfoEntity partsInfo : partsMasterList) {

            // set stock date
            partsInfo.setStockDate(DateTimeUtil.addDay(startDate, IntDef.INT_N_ONE));

            // prepare Calendar List
            prepareCalendarInfo(partsInfo, cldPartyInfoList, cldDetailList, calendarPartyMap, calendarMap);

            // prepare plan information for parts
            // prepare Order Forecast Detail
            foreIdex = prepareEffectiveOFCInfo(partsInfo, ofcDetailList, foreIdex, effOfcMap);
            // if does not has any order, will not do any plan and others
            if (partsInfo.getEffectOrderMonth() == null) {
                continue;
            }

            // prepare Order Detail
            if (partsInfo.getBusinessPattern().equals(BusinessPattern.V_V)) {
                orderIdex = preparePlanDetailInfo(partsInfo, vvOrderList, AttachType.PLAN, orderIdex);
            } else {
                // prepare Kanban Plan Detail
                kanbanIdex = preparePlanDetailInfo(partsInfo, aisinKanbanList, AttachType.PLAN, kanbanIdex);
            }
            // prepare On-Shipping Detail
            shipIdex = preparePlanDetailInfo(partsInfo, onShippingList, AttachType.ON_SHIPPING, shipIdex);

            // prepare Not in Run-down Detail
            // prepare etd delay for VV
            if (partsInfo.getBusinessPattern().equals(BusinessPattern.V_V)) {
                edtvvIdex = prepareNotInRundown(partsInfo, etdDelayVvList, null, edtvvIdex);
            } else {
                // prepare etd delay for AISIN
                edtasIdex = prepareNotInRundown(partsInfo, etdDelayAisinList, NotInRDReasonType.ETD_DELAY_A, edtasIdex);
            }
            // prepare inbound delay
            indIdex = prepareNotInRundown(partsInfo, ibDelayList, NotInRDReasonType.INBOUND_DELAY, indIdex);
            // prepare NG on-hold information
            ngIdex = prepareNotInRundown(partsInfo, ngOnHoldList, NotInRDReasonType.NG_ONHOLD, ngIdex);

            // prepare Customer Daily Usage List
            usageIdex = prepareDailyUsage(partsInfo, dailyUsageList, usageIdex);

            // prepare Customer Usage Share List
            shaIdex = prepareShareInfo(partsInfo, usgShareList, shaIdex);
        }
        
        // sort
        PartSortManager.sort(partsMasterList, "ttcPartsNo", "oldTtcPartsNo");

        // return parts master information
        return partsMasterList;
    }
    
    /**
     * Make order forecast map from order forecast list.
     * 
     * @param effOfcMap effOfcMap
     * @param effOfcList effOfcList
     */
    private void makeEffectiveOfcMap(Map<String, TntRdDetailAttachEx> effOfcMap, List<TntRdDetailAttachEx> effOfcList) {

        // define
        StringBuffer mapKey = new StringBuffer();
        // loop each list
        if (effOfcList != null && !effOfcList.isEmpty()) {
            for (TntRdDetailAttachEx effOfc : effOfcList) {
                if (effOfc.getEffectOfcCnt() != null && effOfc.getEffectOfcCnt().intValue() > IntDef.INT_ZERO) {

                    // reset map key
                    mapKey.setLength(IntDef.INT_ZERO);
                    mapKey.append(effOfc.getPartsId());
                    mapKey.append(StringConst.COMMA);
                    mapKey.append(effOfc.getOrderMonth());

                    // put into maps
                    effOfcMap.put(mapKey.toString(), effOfc);
                }
            }
        }
    }

    /**
     * Prepare plan information.
     * 
     * @param partsInfo basic parts information
     * @param orderForecastList order forecast detail information
     * @param startIdx start index
     * @param effOfcMap effOfcMap
     * @return end index
     */
    private int prepareEffectiveOFCInfo(BasePartsInfoEntity partsInfo, List<TntRdDetailAttachEx> orderForecastList,
        int startIdx, Map<String, TntRdDetailAttachEx> effOfcMap) {

        // If Part Master.INACTIVE_FLAG = 1:Y, no need simulation forecast and also no need do order again
        if (partsInfo.getInactiveFlag().equals(InactiveFlag.INACTIVE)) {

            // set order month
            partsInfo.setEffectOrderMonth(partsInfo.getLastOrderMonth());
            return startIdx;
        }
        
        // get current month
        Date runStartDate = DateTimeUtil.addDay(partsInfo.getStockDate(), IntDef.INT_ONE);
        String currMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, runStartDate);

        // compare current month already has been ordered
        int comp = IntDef.INT_ONE;
        if (!StringUtil.isNullOrEmpty(partsInfo.getLastOrderMonth())) {
            comp = currMonth.compareTo(partsInfo.getLastOrderMonth());
        }

        // Check Current Month is already has been ordered or not
        String effOrderMonth = null;
        if (comp <= IntDef.INT_ZERO) {

            // set effective order month
            effOrderMonth = partsInfo.getLastOrderMonth();
        } else {
            // check order days
            if (partsInfo.getOrderDay() == null) {

                // If current parts is AISIN Parts, in this case we need maintain as current month
                effOrderMonth = partsInfo.getLastOrderMonth();
            } else {

                // If active parts, must simulation to last forecast.
                comp = IntDef.INT_ONE;
                if (partsInfo.getFcOrderMonth() != null) {
                    comp = currMonth.compareTo(partsInfo.getFcOrderMonth());
                }

                // If does not received PIC uploaded Order Forecast for current month
                if (comp > IntDef.INT_ZERO) {

                    // set Order Month = Last month of Current Month(Current Month - 1 Month).
                    effOrderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                        DateTimeUtil.addMonth(runStartDate, IntDef.INT_N_ONE));
                } else {

                    // check is already past order timing or not for V-V parts
                    List<TnmCalendarDetailEx> cldDetailList = partsInfo.getOfficeCalendarList();

                    // Set loopDate = Next day of (Last day of Current Month)
                    Date firstDay = CalendarManager.getNextWorkingDay(
                        DateTimeUtil.firstDay(DateTimeUtil.addMonth(runStartDate, IntDef.INT_ONE)), cldDetailList,
                        IntDef.INT_ONE);

                    // Get The Last Order Date
                    Date lastOrderDate = CalendarManager.getDateByWorkingDay(partsInfo.getOrderDay(), firstDay,
                        cldDetailList, IntDef.INT_N_ONE);

                    // Check Last Order Date
                    if (runStartDate.after(lastOrderDate)) {

                        // We need simulation till last forecast of current month
                        effOrderMonth = currMonth;

                    } else {
                        // means not past order timing
                        effOrderMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                            DateTimeUtil.addMonth(runStartDate, IntDef.INT_N_ONE));
                    }
                }
            }
        }
        
        // check if aisin parts, and no kanban, set kanban order month as current month
        // add by yinchuan LIU for user change at 2016/07/12
        if (partsInfo.getBusinessPattern().equals(BusinessPattern.AISIN)) {
            // add as current month
            if (effOrderMonth == null) {
                // set as current month
                effOrderMonth = currMonth;
            }
        }

        // set effective order month
        partsInfo.setEffectOrderMonth(effOrderMonth);

        // check effective order month
        if (effOrderMonth == null) {
            // this is the parts which does not has any order, does not need do simulation
            return startIdx;
        }

        // we will use parts order month
        TntRdDetailAttachEx forecastDetail = null;
        List<TntRdDetailAttachEx> effFCList = new ArrayList<TntRdDetailAttachEx>();
        Date loopMonth = null;
        Date endOFCDate = null;
        BigDecimal totalOFCQty = BigDecimal.ZERO;
        
        // get effective order forecast
        int effOfcNum = IntDef.INT_ZERO;
        StringBuffer mapKey = new StringBuffer();
        mapKey.append(partsInfo.getPartsId());
        mapKey.append(StringConst.COMMA);
        mapKey.append(effOrderMonth);
        // get from map
        if (effOfcMap.containsKey(mapKey.toString())) {
            effOfcNum = effOfcMap.get(mapKey.toString()).getEffectOfcCnt().intValue();
        }
        // if more than forecast number in parts master, set as parts master
        if (effOfcNum > partsInfo.getForecastNum().intValue()) {
            effOfcNum = partsInfo.getForecastNum().intValue();
        }
        
        // so get last
        int i = startIdx;
        if (effOfcNum > IntDef.INT_ZERO) {
            
            // get end date
            Date lastFCMonth = DateTimeUtil.lastDay(DateTimeUtil.addMonth(
                DateTimeUtil.parseDate(effOrderMonth, DateTimeUtil.FORMAT_YEAR_MONTH), effOfcNum));

            // loop data
            for (; i < orderForecastList.size(); i++) {

                // get order detail
                forecastDetail = orderForecastList.get(i);

                // get part id compare
                comp = forecastDetail.getPartsId().compareTo(partsInfo.getPartsId());

                // if smaller
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }

                // get part id compare
                comp = forecastDetail.getOrderMonth().compareTo(effOrderMonth);
                // if smaller
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }

                // check continuity of forecast month
                loopMonth = DateTimeUtil.lastDay(DateTimeUtil.parseDate(forecastDetail.getPfcMonth(),
                    DateTimeUtil.FORMAT_YEAR_MONTH));

                // check month
                if (loopMonth == null) {
                    break;
                }

                // get month diff
                if (loopMonth.after(lastFCMonth)) {
                    // break;
                    break;
                }

                // set Date
                endOFCDate = forecastDetail.getImpInbPlanDate();

                // set qty
                totalOFCQty = DecimalUtil.add(totalOFCQty, forecastDetail.getQty());

                // set attach type
                forecastDetail.setAttachType(AttachType.ORDER_FORECAST);

                // set into list
                effFCList.add(forecastDetail);
            }
            
            // set into plan list
            partsInfo.addPlanDetailList(effFCList);
            
            // get end date of last ofc mont
            Date lastOfcMonDate = DateTimeUtil.lastDay(DateTimeUtil.addMonth(lastFCMonth, partsInfo.getTargetMonth()));

            // check lastFCMonth
            // set order forecast end date
            if (endOFCDate == null) {
                partsInfo.setEndOFCDate(lastOfcMonDate);
            } else {
                partsInfo.setEndOFCDate(endOFCDate.after(lastOfcMonDate) ? endOFCDate : lastOfcMonDate);
            }

            // set order forecast end date
            partsInfo.setTotalOFCQty(totalOFCQty);
        }

        // return
        return i;
    }

    /**
     * Prepare plan information.
     * 
     * @param partsInfo basic parts information
     * @param planDetailLst plan detail information
     * @param attachType attach type
     * @param startIdx start index
     * @return end index
     */
    private int preparePlanDetailInfo(BasePartsInfoEntity partsInfo, List<TntRdDetailAttachEx> planDetailLst,
        Integer attachType, int startIdx) {

        // define
        TntRdDetailAttachEx planDetail = null;
        Date endPlanDate = partsInfo.getEndPlanDate();
        Date endOrderPlanDate = partsInfo.getEndOrderPlanDate();

        // loop data
        int i = startIdx;
        List<TntRdDetailAttachEx> currList = new ArrayList<TntRdDetailAttachEx>();
        for (; i < planDetailLst.size(); i++) {

            // get order detail
            planDetail = planDetailLst.get(i);

            // get part id compare
            int comp = planDetail.getPartsId().compareTo(partsInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set attach type
            planDetail.setAttachType(attachType);

            // reset endPlanDate
            if (endPlanDate == null || endPlanDate.before(planDetail.getImpInbPlanDate())) {
                // set
                endPlanDate = planDetail.getImpInbPlanDate();
            }
            // check endOrderPlanDate
            if (AttachType.PLAN == attachType) {
                if (endOrderPlanDate == null || endOrderPlanDate.before(planDetail.getImpInbPlanDate())) {
                    // set
                    endOrderPlanDate = planDetail.getImpInbPlanDate();
                }
            }

            // set into list
            currList.add(planDetail);
        }

        // set into list
        partsInfo.addPlanDetailList(currList);
        // reset end plan Date
        partsInfo.setEndPlanDate(endPlanDate);
        // reset end plan Date
        partsInfo.setEndOrderPlanDate(endOrderPlanDate);

        // return
        return i;
    }

    /**
     * Prepare customer daily usage information for each parts.
     * 
     * @param partsInfo basic parts information
     * @param dailyUsageList customer daily usage information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareDailyUsage(BasePartsInfoEntity partsInfo, List<TntCfcDay> dailyUsageList, int startIdx) {

        // define
        List<TntCfcDay> dUsageList = new ArrayList<TntCfcDay>();
        TntCfcDay usageDetail = null;

        // loop data
        int i = startIdx;
        for (; i < dailyUsageList.size(); i++) {

            // get order detail
            usageDetail = dailyUsageList.get(i);

            // get part id compare
            int comp = usageDetail.getPartsId().compareTo(partsInfo.getPartsId());

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
        partsInfo.setDailyUsageList(dUsageList);

        // return
        return i;
    }

    /**
     * Prepare not in run-down information for each parts.
     * 
     * @param partsInfo basic parts information
     * @param notInRundownList not in rundown list
     * @param reasonType not in rundown reason type
     * @param startIdx start index
     * @return end index
     */
    private int prepareNotInRundown(BasePartsInfoEntity partsInfo, List<TntNotInRundown> notInRundownList,
        Integer reasonType, int startIdx) {

        // define
        TntNotInRundown notInrundown = null;

        // loop data
        int i = startIdx;
        List<TntNotInRundown> currList = new ArrayList<TntNotInRundown>();
        BigDecimal notInQty = BigDecimal.ZERO;
        for (; i < notInRundownList.size(); i++) {

            // get detail
            notInrundown = notInRundownList.get(i);

            // get part id compare
            int comp = notInrundown.getPartsId().compareTo(partsInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // reset reason type
            if (reasonType != null) {
                notInrundown.setReasonType(reasonType);
            }

            // set into list
            currList.add(notInrundown);
            notInQty = DecimalUtil.add(notInQty, notInrundown.getQty());
        }
        
        // check
        if (reasonType == null || reasonType.equals(NotInRDReasonType.ETD_DELAY_A)
                || reasonType.equals(NotInRDReasonType.ETD_DELAY_B)) {
            // set ETD delay QTY
            partsInfo.setEtdDelayQty(DecimalUtil.add(partsInfo.getEtdDelayQty(), notInQty));
        } else if (reasonType.equals(NotInRDReasonType.INBOUND_DELAY)) {
            // set INBOUND delay QTY
            partsInfo.setInbDelayQty(DecimalUtil.add(partsInfo.getInbDelayQty(), notInQty));
        }

        // set into list
        partsInfo.addNotInRundownList(currList);

        // return
        return i;
    }

    /**
     * Prepare customer daily usage share information list for each parts.
     * 
     * @param partsInfo basic parts information
     * @param shareInfoList customer daily usage share information list
     * @param startIdx start index
     * @return end index
     */
    private int prepareShareInfo(BasePartsInfoEntity partsInfo, List<TntRdAttachCfc> shareInfoList, int startIdx) {

        // check planDetailLst has data or not
        if (shareInfoList == null || shareInfoList.isEmpty()) {
            return startIdx;
        }

        // define
        List<TntRdAttachCfc> shareIfList = new ArrayList<TntRdAttachCfc>();
        TntRdAttachCfc shareInfo = null;

        // loop data
        int i = startIdx;
        for (; i < shareInfoList.size(); i++) {

            // get order detail
            shareInfo = shareInfoList.get(i);

            // get part id compare
            int comp = shareInfo.getPartsId().compareTo(partsInfo.getPartsId());

            // if smaller
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // set into list
            shareIfList.add(shareInfo);
        }

        // set into parts master
        partsInfo.setShareInfoList(shareIfList);

        // return
        return i;
    }

    /**
     * Prepare each calendar for each parts.
     * 
     * @param partsInfo basic parts information
     * @param cldPartyInfoList calendar party information list
     * @param cldDetailList calendar detail information list
     * @param calendarPartyMap calendar party map
     * @param calendarMap Calendar Map
     */
    private void prepareCalendarInfo(BasePartsInfoEntity partsInfo, List<TnmCalendarPartyEx> cldPartyInfoList,
        List<TnmCalendarDetailEx> cldDetailList, Map<String, Integer> calendarPartyMap,
        Map<Integer, List<TnmCalendarDetailEx>> calendarMap) {

        // get office calendar information
        partsInfo.setOfficeCalendarList(CalendarManager.findCalendarInfo(CalendarParty.TTC_IMPORT_OFFICE,
            partsInfo.getOfficeId(), partsInfo.getOfficeId(), calendarMap, cldDetailList, calendarPartyMap,
            cldPartyInfoList));

        // get customer calendar information
        partsInfo.setCustCalendarList(CalendarManager.findCalendarInfo(CalendarParty.CUSTOMER, partsInfo.getOfficeId(),
            partsInfo.getCustomerId(), calendarMap, cldDetailList, calendarPartyMap, cldPartyInfoList));
    }

    /**
     * Make Run-down Detail Information.
     * 
     * @param partsList parts information list
     * @return rundown master list
     */
    private List<TntRdMaster> doMakeRundownInfo(List<BasePartsInfoEntity> partsList) {

        // loop each parts in parts list and then make rundown information
        List<TntRdMaster> rundownList = new ArrayList<TntRdMaster>();
        TntRdMaster rdMaster = null;
        // loop each parts
        for (BasePartsInfoEntity baseParts : partsList) {
            
            // check effective parts
            if (baseParts.getEffectOrderMonth() == null) {
                continue;
            }

            // Prepare Run-down Master Data
            rdMaster = doMakeRundownMaster(baseParts);
            if (rdMaster == null) {
                continue;
            }

            // Prepare Run-down Detail Data for Parts
            doMakeRundownDetail(baseParts, rdMaster);

            // set into list
            rundownList.add(rdMaster);
        }

        // return rundown information
        return rundownList;
    }

    /**
     * Prepare run-down master information.
     * 
     * @param partsInfo parts information
     * @return run-down master information
     */
    private TntRdMaster doMakeRundownMaster(BasePartsInfoEntity partsInfo) {

        // rundown master defined
        TntRdMaster runMaster = new TntRdMaster();

        // get supply chain qty
        BigDecimal supplyChainQty = BigDecimal.ZERO;

        // Prepare basic Run-down Master Data From Parts Master
        // PARTS_ID
        runMaster.setPartsId(partsInfo.getPartsId());
        // IMP_STOCK_FLAG
        runMaster.setImpStockFlag(partsInfo.getImpStockFlag());
        // SA_CUST_STOCK_FLAG
        runMaster.setSaCustStockFlag(partsInfo.getSaCustStockFlag());
        // SIMULATION_END_DATE_PATTERN
        runMaster.setSimulationEndDatePattern(partsInfo.getSimulationEndDatePattern());
        // INVENTORY_BOX_FLAG
        runMaster.setInventoryBoxFlag(partsInfo.getInventoryBoxFlag());
        // RUNDOWN_SAFETY_STOCK
        runMaster.setRundownSafetyStock(partsInfo.getRundownSafetyStock());
        // MIN_STOCK
        runMaster.setMinStock(partsInfo.getMinStock());
        // MIN_BOX
        runMaster.setMinBox(partsInfo.getMinBox());
        // EXP_BALANCE_ORDER
        runMaster.setExpBalanceOrder(partsInfo.getExpBalanceQty());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getExpBalanceQty());
        // EXP_WHS_STOCK
        runMaster.setExpWhsStock(partsInfo.getExpWhsStock());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getExpWhsStock());
        if (runMaster.getImpStockFlag().equals(ImpStockFlag.WITH_CLEARANCE)) {
            // ON_SHIPPING_STOCK
            runMaster.setOnShippingStock(partsInfo.getOnShippingStock());
            supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getOnShippingStock());
            // IN_RACK_QTY
            runMaster.setInRackQty(partsInfo.getInRackQty());
            supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getInRackQty());
        } else {
            // ON_SHIPPING_STOCK
            runMaster.setOnShippingStock(DecimalUtil.add(partsInfo.getOnShippingStock(), partsInfo.getInRackQty()));
            supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getOnShippingStock());
            // IN_RACK_QTY
            runMaster.setInRackQty(BigDecimal.ZERO);
            supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getInRackQty());
        }
        // IMP_WHS_QTY
        runMaster.setImpWhsQty(partsInfo.getImpWhsAvailableQty());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getImpWhsAvailableQty());
        // PREPARED_OB_QTY
        runMaster.setPreparedObQty(partsInfo.getPreparedOutboundQty());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getPreparedOutboundQty());
        // NG_QTY
        runMaster.setNgQty(partsInfo.getNgQty());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getNgQty());
        // ECI_ONHOLOD_QTY
        runMaster.setEciOnholdQty(partsInfo.getOnHoldQty());
        supplyChainQty = DecimalUtil.add(supplyChainQty, partsInfo.getOnHoldQty());
        // CUST_STOCK_END_DATE
        runMaster.setCustStockEndDate(partsInfo.getCustomerStockDate());
        // CUST_STOCK_QTY
        runMaster.setCustStockQty(partsInfo.getCustomerStockQty());
        // END_CFC_DATE
        runMaster.setEndCfcDate(partsInfo.getEndCfcDate());
        // DELI_QTY_OF_LAST_MONTH
        runMaster.setDeliQtyOfLastMonth(partsInfo.getLastDeliveryQty());
        // DELI_QTY_OF_LAST_MONTH
        runMaster.setEtdDelayQty(partsInfo.getEtdDelayQty());
        // DELI_QTY_OF_LAST_MONTH
        runMaster.setInboundDelayQty(partsInfo.getInbDelayQty());

        // check effective
        // if all qty is zero and parts already discontinue, need not do simulation
        if (supplyChainQty.compareTo(BigDecimal.ZERO) <= IntDef.INT_ZERO) {
            if (partsInfo.getInactiveFlag().equals(InactiveFlag.INACTIVE)) {
                return null;
            }
        }

        // Get Run-down Date range
        // ORDER_MONTH
        runMaster.setOrderMonth(partsInfo.getEffectOrderMonth());

        // TARGET_MONTH
        Date orderMonth = DateTimeUtil.parseDate(runMaster.getOrderMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
        Date targMonth = DateTimeUtil.addMonth(orderMonth, partsInfo.getTargetMonth());
        runMaster.setTargetMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, targMonth));

        // RUN_START_DATE
        runMaster.setRunStartDate(DateTimeUtil.addDay(partsInfo.getStockDate(), IntDef.INT_ONE));

        // END_PFC_DATE
        if (partsInfo.getEndOFCDate() != null) {
            runMaster.setEndPfcDate(partsInfo.getEndOFCDate());
        } else {
            runMaster.setEndPfcDate(partsInfo.getLastTgMonthDate());
        }
        
        // RUN_END_DATE
        Date runEndDate = DateTimeUtil.lastDay(DateTimeUtil.addMonth(targMonth, partsInfo.getForecastNum()));
        if (partsInfo.getEndPlanDate() != null && runEndDate.before(partsInfo.getEndPlanDate())) {
            runEndDate = partsInfo.getEndPlanDate();
        }
        if (runMaster.getEndPfcDate() != null && runEndDate.before(runMaster.getEndPfcDate())) {
            runEndDate = runMaster.getEndPfcDate();
        }
        if (runEndDate.before(runMaster.getRunStartDate())) {
            runEndDate = DateTimeUtil.lastDay(DateTimeUtil.addMonth(runMaster.getRunStartDate(), IntDef.INT_ONE));
        }
        
        // add by Yinchuan LIU for rundown end date after three month of last day
        //runEndDate = DateTimeUtil.addMonth(runEndDate, IntDef.INT_THREE);
        // run end of after three month
        partsInfo.setRunEndDate(DateTimeUtil.addMonth(runEndDate, IntDef.INT_THREE));
        
        // set into rundown master
        runMaster.setRunEndDate(runEndDate);

        // PFC_QTY
        runMaster.setPfcQty(partsInfo.getTotalOFCQty());

        // Prepare Not in Run-down Detail Information
        runMaster.setTntNotInRundowns(partsInfo.getNotInRundownList());

        // Prepare Run-down Master Attach Detail Information
        runMaster.setTntRundownAttachs(partsInfo.getShareInfoList());

        // rundownRemark
        runMaster.setRemark(partsInfo.getRundownRemark());

        // return prepared rundown master
        return runMaster;
    }

    /**
     * Prepare run-down detail information.
     * 
     * @param partsInfo parts information
     * @param rundownMaster rundown master information
     */
    private void doMakeRundownDetail(BasePartsInfoEntity partsInfo, TntRdMaster rundownMaster) {

        // prepare Simulation End Date for each month
        this.getSimulationEndDate(partsInfo);

        // Prepare Run-down Detail
        Date loopDate = rundownMaster.getRunStartDate();
        BigDecimal endingStock = DecimalUtil.add(rundownMaster.getInRackQty(), rundownMaster.getImpWhsQty());
        endingStock = DecimalUtil.add(endingStock, rundownMaster.getPreparedObQty());
        endingStock = DecimalUtil.add(endingStock, rundownMaster.getEciOnholdQty());
        endingStock = DecimalUtil.add(endingStock, rundownMaster.getCustStockQty());

        // do run-down detail by each date
        int ofcIdx = IntDef.INT_ZERO;
        int planIdx = IntDef.INT_ZERO;
        int sipIdx = IntDef.INT_ZERO;
        int usgIdx = IntDef.INT_ZERO;
        BigDecimal stockDays = BigDecimal.ZERO;
        // modify by Yinchuan LIU for rundown end date after three month of last day
        //while (!loopDate.after(rundownMaster.getRunEndDate())) {
        while (!loopDate.after(partsInfo.getRunEndDate())) {

            // get new run-down detail information
            TntRdDetail detail = new TntRdDetail();
            rundownMaster.addTntRundownDetail(detail);
            // prepare imp inbound plan date
            detail.setImpInbPlanDate(loopDate);
            detail.setPartsId(partsInfo.getPartsId());
            prepareSimulType(loopDate, partsInfo, rundownMaster, detail);

            // Prepare Order Forecast
            ofcIdx = prepareTotalRunQty(ofcIdx, AttachType.ORDER_FORECAST, loopDate, partsInfo.getPlanDetailList(),
                detail);
            // Prepare Order/Kanban Plan Detail
            planIdx = prepareTotalRunQty(planIdx, AttachType.PLAN, loopDate, partsInfo.getPlanDetailList(), detail);
            // Prepare On-Shipping Detail
            sipIdx = prepareTotalRunQty(sipIdx, AttachType.ON_SHIPPING, loopDate, partsInfo.getPlanDetailList(), detail);
            // Prepare Daily usage
            usgIdx = prepareDaliyUsageQty(usgIdx, loopDate, partsInfo.getDailyUsageList(), detail,
                rundownMaster.getEndCfcDate());

            // Prepare Ending Stock and Stock Days for Run-down Detail
            prepareEndingStock(usgIdx, loopDate, partsInfo, rundownMaster, detail, endingStock, stockDays);
            endingStock = detail.getEndingStock();
            stockDays = detail.getStockDays();

            // define
            BigDecimal minAddOnQty = BigDecimal.ZERO;
            // check inventory by box?
            if (partsInfo.getInventoryBoxFlag().equals(InventoryByBox.Y)) {

                // if inventory by box, Part Master.SPQ * Part Master.MIN_BOX - Ending Stock
                BigDecimal minStockQty = DecimalUtil.multiply(partsInfo.getSpq(),
                    StringUtil.toBigDecimal(partsInfo.getMinBox()));

                // check
                if (endingStock == null) {
                    minAddOnQty = null;
                } else if (endingStock != null && endingStock.compareTo(minStockQty) < IntDef.INT_ZERO) {
                    minAddOnQty = DecimalUtil.subtract(minStockQty, endingStock);
                }
            } else {

                // if no stock day, means no master
                if (stockDays == null) {

                    // set as null
                    minAddOnQty = null;
                } else {

                    // if stock days < min stock days need do
                    if (stockDays.intValue() < partsInfo.getMinStock().intValue()) {

                        // If No CFC Flag = true, means cause no customer forecast, in this case, will
                        if (detail.getNoCfcFlag().equals(NoCfcFlag.Y)) {

                            // set as null
                            minAddOnQty = null;
                        } else {

                            // Prepare Add-On Qty for Min Stock
                            minAddOnQty = getAddOnQty(usgIdx, partsInfo.getMinStock(), loopDate, rundownMaster,
                                partsInfo, endingStock);
                        }
                    }
                }
            }

            // Prepare Add-On Qty for Safety Stock
            BigDecimal saftyAddOnQty = BigDecimal.ZERO;
            if (stockDays == null) {

                // set as null
                saftyAddOnQty = null;
            } else {

                // If Stock Days < Part Master.RUNDOWN_SAFETY_STOCK, then
                if (stockDays.intValue() < partsInfo.getRundownSafetyStock().intValue()) {

                    // If No CFC Flag = true, means cause no customer forecast, in this case, will
                    if (detail.getNoCfcFlag().intValue() == NoCfcFlag.Y) {

                        // set as null
                        saftyAddOnQty = null;
                    } else {

                        // Prepare Add-On Qty for Min Stock
                        saftyAddOnQty = getAddOnQty(usgIdx, partsInfo.getRundownSafetyStock(), loopDate, rundownMaster,
                            partsInfo, endingStock);
                    }
                }
            }

            // set into detail
            detail.setAddOnMinQty(minAddOnQty);
            detail.setAddOnSaftyQty(saftyAddOnQty);

            // next date
            loopDate = DateTimeUtil.addDay(loopDate, IntDef.INT_ONE);
        }
    }

    /**
     * Prepare simulation type and forecast number.
     * 
     * @param runDate rundown date
     * @param partsInfo parts information
     * @param rundownMaster rundownMaster
     * @param detail rundown detail
     */
    private void prepareSimulType(Date runDate, BasePartsInfoEntity partsInfo, TntRdMaster rundownMaster,
        TntRdDetail detail) {

        // prepare simulation type and forecast number.
        if (!runDate.after(partsInfo.getLastTgMonthDate())) {
            detail.setSimulationType(SimulationType.TARGET);
        } else if (partsInfo.getLastFC1Date() != null && !runDate.after(partsInfo.getLastFC1Date())) {
            detail.setSimulationType(SimulationType.FORECAST1);
        } else if (partsInfo.getLastFC2Date() != null && !runDate.after(partsInfo.getLastFC2Date())) {
            detail.setSimulationType(SimulationType.FORECAST2);
        } else if (partsInfo.getLastFC3Date() != null && !runDate.after(partsInfo.getLastFC3Date())) {
            detail.setSimulationType(SimulationType.FORECAST3);
        } else if (partsInfo.getLastFC4Date() != null && !runDate.after(partsInfo.getLastFC4Date())) {
            detail.setSimulationType(SimulationType.FORECAST4);
        } else if (partsInfo.getLastFC5Date() != null && !runDate.after(partsInfo.getLastFC5Date())) {
            detail.setSimulationType(SimulationType.FORECAST5);
        } else if (partsInfo.getLastFC6Date() != null && !runDate.after(partsInfo.getLastFC6Date())) {
            detail.setSimulationType(SimulationType.FORECAST6);
        } else if (!runDate.after(rundownMaster.getRunEndDate())) {
            detail.setSimulationType(SimulationType.OTHERS);
        } else {
            detail.setSimulationType(SimulationType.NOT_REQUIRE);
        }
    }

    /**
     * Prepare total run-down qty.
     * 
     * @param idx start index
     * @param type Attach Type
     * @param runDate run-down date
     * @param planList plan information list
     * @param detail run-down detail entity
     * @return last index
     */
    private int prepareTotalRunQty(int idx, Integer type, Date runDate, List<TntRdDetailAttachEx> planList,
        TntRdDetail detail) {

        // define
        int lIdx = idx;
        BigDecimal totalQty = null;

        // loop
        for (; lIdx < planList.size(); lIdx++) {

            // get new run-down detail information
            TntRdDetailAttachEx plan = planList.get(lIdx);

            // compare type
            int comp = plan.getAttachType().compareTo(type);
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // compare date
            comp = plan.getImpInbPlanDate().compareTo(runDate);
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // if same, summary
            totalQty = DecimalUtil.add(totalQty, plan.getQty());

            // set into detail
            detail.addTntRdDetailAttach(plan);
        }

        // set into run-down list
        switch (type.intValue()) {

            case AttachType.ORDER_FORECAST:
                detail.setOrderForecastQty(totalQty);
                break;
            case AttachType.PLAN:
                detail.setNotEtdQty(totalQty);
                break;
            case AttachType.ON_SHIPPING:
                detail.setOnShippingQty(totalQty);
                break;
            default:
                break;
        }

        // return last index
        return lIdx;
    }

    /**
     * Prepare customer daily usage qty.
     * 
     * @param idx start index
     * @param runDate run-down date
     * @param usageList customer daily usage information list
     * @param detail run-down detail entity
     * @param endCfcDate end customer usage date
     * @return last index
     */
    private int prepareDaliyUsageQty(int idx, Date runDate, List<TntCfcDay> usageList, TntRdDetail detail,
        Date endCfcDate) {

        // define
        int lIdx = idx;

        // loop
        for (; lIdx < usageList.size(); lIdx++) {

            // get new run-down detail information
            TntCfcDay usage = usageList.get(lIdx);

            // compare date
            int comp = usage.getCfcDate().compareTo(runDate);
            if (comp < IntDef.INT_ZERO) {
                continue;
            } else if (comp > IntDef.INT_ZERO) {
                break;
            }

            // if same, set
            detail.setDaliyUsageQty(usage.getCfcQty());
        }

        // do check, if before end of cfc date, set as zero
        if (detail.getDaliyUsageQty() == null) {
            // check cfc Date\
            if (endCfcDate != null && !endCfcDate.before(runDate)) {
                detail.setDaliyUsageQty(BigDecimal.ZERO);
            }
        }

        // return last index
        return lIdx;
    }

    /**
     * Prepare Ending Stock and Stock Days for Run-down Detail.
     * 
     * @param startIdx last index
     * @param runDate run-down date
     * @param partsInfo parts information
     * @param master run-down detail
     * @param detail run-down detail
     * @param lastEndStock ending stock of last day
     * @param lastStockDays stock days of last day
     */
    private void prepareEndingStock(int startIdx, Date runDate, BasePartsInfoEntity partsInfo, TntRdMaster master,
        TntRdDetail detail, BigDecimal lastEndStock, BigDecimal lastStockDays) {

        // define
        BigDecimal endingStock = null;
        BigDecimal stockDays = null;
        boolean noCFCFlag = false;
        boolean noPFCFlag = false;
        boolean isWorkingDay = false;

        // If loopDate is after TNT_RUNDOWN_MASTER.END_PFC_DATE
        if (master.getEndPfcDate() == null) {
            if (runDate.after(partsInfo.getLastTgMonthDate())) {
                noPFCFlag = true;
            }
        } else if (runDate.after(master.getEndPfcDate())) {
            noPFCFlag = true;
        }

        // If loopDate is after TNT_RUNDOWN_MASTER.END_CFC_DATE
        if (master.getEndCfcDate() == null || runDate.after(master.getEndCfcDate())) {
            noCFCFlag = true;
        }

        // If loopDate is before both Parts Master.END_CFC_DATE and Parts Master.END_PFC_DATE
        if (!noPFCFlag && !noCFCFlag) {

            // Get Ending Stock by Order Forecast and Customer Forecast
            endingStock = DecimalUtil.add(lastEndStock, detail.getOrderForecastQty());
            endingStock = DecimalUtil.add(endingStock, detail.getNotEtdQty());
            endingStock = DecimalUtil.add(endingStock, detail.getOnShippingQty());
            endingStock = DecimalUtil.subtract(endingStock, detail.getDaliyUsageQty());

            // Get Stock Days by Customer Usage Qty List and Customer Calendar
            // check ending stock
            int comp = endingStock.compareTo(BigDecimal.ZERO);
            if (comp < IntDef.INT_ZERO) {

                // If Ending Stock < 0, then
                // If Ending Stock(last Day) of last day also be negative then
                if (lastEndStock.compareTo(BigDecimal.ZERO) < 0) {

                    // If Customer Usage Qty > 0 then
                    if (detail.getDaliyUsageQty() != null
                            && detail.getDaliyUsageQty().compareTo(BigDecimal.ZERO) > IntDef.INT_ZERO) {
                        stockDays = DecimalUtil.subtract(lastStockDays, BigDecimal.ONE);
                    } else {
                        // If Customer Usage Qty = 0 then
                        stockDays = lastStockDays;
                    }
                } else {

                    // If Ending Stock(last Day) of last day is not negative then
                    stockDays = DecimalUtil.divide(endingStock, detail.getDaliyUsageQty(), IntDef.INT_ONE,
                        RoundingMode.UP);
                }
            } else {

                // Set loopStock = Ending Stock
                BigDecimal loopStock = endingStock;
                // Set nextDate = loopDate + 1 day
                Date nextDate = DateTimeUtil.addDay(runDate, IntDef.INT_ONE);

                // if not end date
                // modify by Yinchuan LIU for rundown end date after three month of last day
                //if (!nextDate.after(master.getRunEndDate())) {
                if (!nextDate.after(partsInfo.getRunEndDate())) {

                    // Do loop while nextDate <= Run-Down End Date
                    int usgIdx = startIdx;
                    BigDecimal usageQty = null;
                    List<TntCfcDay> usageList = partsInfo.getDailyUsageList();
                    // modify by Yinchuan LIU for rundown end date after three month of last day
                    //while (!nextDate.after(master.getRunEndDate())) {
                    while (!nextDate.after(partsInfo.getRunEndDate())) {

                        // If nextDate is not before TNT_RUNDOWN_MASTER.END_CFC_DATE
                        if (master.getEndCfcDate() == null || nextDate.after(master.getEndCfcDate())) {
                            noCFCFlag = true;
                            break;
                        }

                        // Get Customer Daily Usage from Customer Daily Usage List
                        for (int i = usgIdx; i < usageList.size(); i++) {

                            // set into next
                            usgIdx = i;

                            // get new run-down detail information
                            TntCfcDay usage = usageList.get(i);

                            // compare date
                            comp = usage.getCfcDate().compareTo(nextDate);
                            if (comp < IntDef.INT_ZERO) {
                                continue;
                            } else if (comp > IntDef.INT_ZERO) {
                                break;
                            }

                            // if same, set
                            usageQty = usage.getCfcQty();
                        }

                        // get working flag
                        isWorkingDay = CalendarManager.checkWorkingDay(partsInfo.getCustCalendarList(), nextDate);

                        // if not after
                        if (usageQty == null) {
                            usageQty = BigDecimal.ZERO;
                        }

                        // do check
                        if (loopStock.compareTo(usageQty) >= IntDef.INT_ZERO) {

                            // means we could add one day
                            if (isWorkingDay || usageQty.compareTo(BigDecimal.ZERO) > IntDef.INT_ZERO) {
                                // day up
                                stockDays = DecimalUtil.add(stockDays, BigDecimal.ONE);
                            }
                        } else {
                            // get usage days
                            BigDecimal shortageDays = DecimalUtil.divide(loopStock, usageQty, IntDef.INT_ONE,
                                RoundingMode.DOWN);

                            // reset stock days
                            stockDays = DecimalUtil.add(stockDays, shortageDays);

                            // stop
                            break;
                        }

                        // Deduct Customer Daily Usage from loop Stock
                        loopStock = DecimalUtil.subtract(loopStock, usageQty);

                        // do next
                        nextDate = DateTimeUtil.addDay(nextDate, IntDef.INT_ONE);
                    }
                } else {
                    // set as no cfc 
                    noCFCFlag = true;
                }
            }
        }

        // get working flag
        isWorkingDay = CalendarManager.checkWorkingDay(partsInfo.getCustCalendarList(), runDate);

        // set into detail
        detail.setEndingStock(endingStock);
        detail.setStockDays(stockDays);
        detail.setNoCfcFlag(noCFCFlag ? NoCfcFlag.Y : NoCfcFlag.N);
        detail.setNoPfcFlag(noPFCFlag ? NoPfcFlag.Y : NoPfcFlag.N);
        detail.setWorkingFlag(isWorkingDay ? WorkingDay.WORKING_DAY : WorkingDay.REST_DAY);
    }

    /**
     * get Simulation End Date of current parts.
     * 
     * @param partsInfo parts information
     */
    private void getSimulationEndDate(BasePartsInfoEntity partsInfo) {

        // get last day of target month
        Date orderMonth = DateTimeUtil.parseDate(partsInfo.getEffectOrderMonth(), DateTimeUtil.FORMAT_YEAR_MONTH);
        Date lastDayOfTarget = DateTimeUtil.lastDay(DateTimeUtil.addMonth(orderMonth, partsInfo.getTargetMonth()));

        // set last day of target month
        partsInfo.setLastTgMonthDate(lastDayOfTarget);

        // get forecast number
        for (int i = 1; i <= partsInfo.getForecastNum(); i++) {
            Date lastForecastDate = DateTimeUtil.lastDay(DateTimeUtil.addMonth(lastDayOfTarget, i));
            // case when
            switch (i) {
                case IntDef.INT_ONE:
                    partsInfo.setLastFC1Date(lastForecastDate);
                    break;
                case IntDef.INT_TWO:
                    partsInfo.setLastFC2Date(lastForecastDate);
                    break;
                case IntDef.INT_THREE:
                    partsInfo.setLastFC3Date(lastForecastDate);
                    break;
                case IntDef.INT_FOUR:
                    partsInfo.setLastFC4Date(lastForecastDate);
                    break;
                case IntDef.INT_FIVE:
                    partsInfo.setLastFC5Date(lastForecastDate);
                    break;
                case IntDef.INT_SIX:
                    partsInfo.setLastFC6Date(lastForecastDate);
                    break;
                default:
                    break;
            }
        }

        // check Simulation End Date Pattern
        if (partsInfo.getSimulationEndDatePattern().equals(SimulationEndDayPattern.PATTERN_B)) {

            // get last plan date
            if (partsInfo.getEndOrderPlanDate() != null) {
                // set into last day of target month
                if (partsInfo.getEndOrderPlanDate().after(partsInfo.getLastTgMonthDate())) {
                    // reset
                    partsInfo.setLastTgMonthDate(partsInfo.getEndOrderPlanDate());
                }
            }

            // prepare order forecast
            int planIdx = IntDef.INT_ZERO;
            Date lastPlanDate = null;
            List<TntRdDetailAttachEx> forecastList = partsInfo.getPlanDetailList();
            for (int i = 1; i <= partsInfo.getForecastNum(); i++) {

                // get forecast date
                String forecastMonth = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                    DateTimeUtil.addMonth(orderMonth, i));

                // prepare last order forecast date
                // get from effective order forecast
                for (; planIdx < forecastList.size(); planIdx++) {

                    // get object
                    TntRdDetailAttachEx planDetail = forecastList.get(planIdx);

                    // if order forecast, do next
                    if (planDetail.getAttachType().compareTo(AttachType.ORDER_FORECAST) > IntDef.INT_ZERO) {

                        break;
                    }

                    // check last plan date
                    int comp = planDetail.getPfcMonth().compareTo(forecastMonth);
                    if (comp < IntDef.INT_ZERO) {
                        continue;
                    } else if (comp > IntDef.INT_ZERO) {
                        break;
                    }

                    // compare
                    if (lastPlanDate == null || lastPlanDate.before(planDetail.getImpInbPlanDate())) {
                        lastPlanDate = planDetail.getImpInbPlanDate();
                    }
                }

                // case when
                switch (i) {
                    case IntDef.INT_ONE:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC1Date())) {
                            partsInfo.setLastFC1Date(lastPlanDate);
                        }
                        break;
                    case IntDef.INT_TWO:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC2Date())) {
                            partsInfo.setLastFC2Date(lastPlanDate);
                        }
                        break;
                    case IntDef.INT_THREE:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC3Date())) {
                            partsInfo.setLastFC3Date(lastPlanDate);
                        }
                        break;
                    case IntDef.INT_FOUR:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC4Date())) {
                            partsInfo.setLastFC4Date(lastPlanDate);
                        }
                        break;
                    case IntDef.INT_FIVE:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC5Date())) {
                            partsInfo.setLastFC5Date(lastPlanDate);
                        }
                        break;
                    case IntDef.INT_SIX:
                        if (lastPlanDate != null && lastPlanDate.after(partsInfo.getLastFC6Date())) {
                            partsInfo.setLastFC6Date(lastPlanDate);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Prepare add on qty for Run-down Detail.
     * 
     * @param startIdx start index
     * @param addOnDays add on days
     * @param runDate run date
     * @param master rundown master
     * @param partsInfo parts master information
     * @param endingStock ending stock
     * @return addOnQty
     */
    private BigDecimal getAddOnQty(int startIdx, int addOnDays, Date runDate, TntRdMaster master,
        BasePartsInfoEntity partsInfo, BigDecimal endingStock) {

        // Do loop while nextDate <= Run-Down End Date
        int usgIdx = startIdx;
        int workingDay = addOnDays;
        BigDecimal usageQty = null;
        BigDecimal neededQty = null;
        Date nextDate = DateTimeUtil.addDay(runDate, IntDef.INT_ONE);
        List<TntCfcDay> usageList = partsInfo.getDailyUsageList();

        // do loop
        // modify by Yinchuan LIU for rundown end date after three month of last day
        //while (!nextDate.after(master.getRunEndDate())) {
        while (!nextDate.after(partsInfo.getRunEndDate())) {

            // If nextDate is not before TNT_RUNDOWN_MASTER.END_CFC_DATE
            if (master.getEndCfcDate() == null || !nextDate.before(master.getEndCfcDate())) {
                // set not get add on qty
                break;
            }

            // Get Customer Daily Usage from Customer Daily Usage List
            int comp = IntDef.INT_ZERO;
            for (int i = usgIdx; i < usageList.size(); i++) {

                // set into next
                usgIdx = i;

                // get new run-down detail information
                TntCfcDay usage = usageList.get(i);

                // compare date
                comp = usage.getCfcDate().compareTo(nextDate);
                if (comp < IntDef.INT_ZERO) {
                    continue;
                } else if (comp > IntDef.INT_ZERO) {
                    break;
                }

                // if same, set
                usageQty = usage.getCfcQty();
            }

            // Deduct Customer Daily Usage from loop Stock
            neededQty = DecimalUtil.add(neededQty, usageQty);

            // check
            if ((usageQty != null && usageQty.compareTo(BigDecimal.ZERO) > IntDef.INT_ZERO)
                    || CalendarManager.checkWorkingDay(partsInfo.getCustCalendarList(), nextDate)) {

                // day up
                workingDay--;

                // if over then break
                if (workingDay <= IntDef.INT_ZERO) {
                    break;
                }
            }

            // do next
            nextDate = DateTimeUtil.addDay(nextDate, IntDef.INT_ONE);
        }

        // if no master, insert zero
        BigDecimal addOnQty = null;
        if (workingDay > IntDef.INT_ZERO) {
            addOnQty = null;
        } else {
            // get add on qty
            addOnQty = DecimalUtil.subtract(neededQty, endingStock);
        }

        return addOnQty;
    }

}
