/**
 * CPKKPF12UpdateService.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.SupplyChainEntity;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.TransportMode;
import com.chinaplus.common.entity.TnfOrderStatus;
import com.chinaplus.common.entity.TnmCalendarDetail;
import com.chinaplus.common.entity.TnmKbIssuedDate;
import com.chinaplus.common.entity.TntInvoice;
import com.chinaplus.common.entity.TntInvoiceGroup;
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
import com.chinaplus.common.service.SupplyChainService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.kbp.entity.CPKKPF12AllPartsInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12ColEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12CompletedEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12Entity;
import com.chinaplus.web.kbp.entity.CPKKPF12HistoryCompletedEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12QtyInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12RowEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12RowFfEntity;

/**
 * Revised Kanban Plan Upload Service.
 */
@Service
public class CPKKPF12UpdateService extends BaseService {

    /** SESSION_KEY_FILE */
    private static final String SESSION_KEY_FILE = "CPKKPF12_File";

    /** SESSION_ALL_PARTS_INFO */
    private static final String SESSION_ALL_PARTS_INFO = "ALL_PARTS_INFO";
    /** SESSION_KEY_KANBAN_PLAN_NO */
    private static final String SESSION_KANBAN_PLAN_NO = "KANBAN_PLAN_NO";
    /** SESSION_KEY_PLAN_ROW_DATA */
    private static final String SESSION_ROW = "PLAN_ROW_DATA";
    /** SESSION_KEY_PLAN_ROW_FF_DATA */
    private static final String SESSION_ROW_FF = "PLAN_ROW_FF_DATA";
    /** SESSION_KEY_ACTUAL_VAN_COL_DATA */
    private static final String SESSION_COL_ACTUAL_VAN = "ACTUAL_VAN_COL_DATA";
    /** SESSION_KEY_PLAN_COL_PLAN_DATA */
    private static final String SESSION_COL_PLAN = "PLAN_COL_PLAN_DATA";
    /** SESSION_KEY_PLAN_COL_PLAN_NEW_DATA */
    private static final String SESSION_COL_PLAN_NEW = "PLAN_COL_PLAN_NEW_DATA";
    /** SESSION_KEY_PLAN_COL_DIFFERENCE_DATA */
    private static final String SESSION_COL_DIFF = "PLAN_COL_DIFFERENCE_DATA";
    /** SESSION_KEY_PLAN_COL_BOX_DATA */
    private static final String SESSION_COL_BOX = "PLAN_COL_BOX_DATA";
    /** SESSION_KEY_PLAN_COL_NIRD_DATA */
    private static final String SESSION_COL_NIRD = "PLAN_COL_NIRD_DATA";
    /** SESSION_KEY_PLAN_COL_NIRD_NEW_DATA */
    private static final String SESSION_COL_NIRD_NEW = "PLAN_COL_NIRD_NEW_DATA";

    /** MAP_KEY_ROW_PARTS_ID */
    private static final String MAP_KEY_ROW_PARTS_ID = "ROW_PARTS_ID";
    /** MAP_KEY_ROW_FC_FLAG */
    private static final String MAP_KEY_ROW_FC_FLAG = "ROW_FC_FLAG";
    /** MAP_KEY_ROW_LIST_DATA */
    private static final String MAP_KEY_ROW_LIST_DATA = "ROW_LIST_DATA";
    /** MAP_KEY_ROW_LIST_MOD */
    private static final String MAP_KEY_ROW_LIST_MOD = "ROW_LIST_MOD";

    /** REVISION_REASON_MAX_LENGTH */
    private static final int REVISION_REASON_MAX_LENGTH = 80;

    /** Upload Revised Kanban Plan Service */
    @Autowired
    private CPKKPF12Service cpkkpf12Service;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    /**
     * Update Logic
     * 
     * @param request HttpServletRequest
     * @param param BaseParam
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowQtyBox key:rowNo., value:input qty/box
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param messageList message list
     */
    public void doUpdateLogic(HttpServletRequest request, BaseParam param, SessionInfoManager context,
        TntKanban tntKanban, HashMap<String, Integer> rowPartsId, HashMap<String, BigDecimal> rowQtyBox,
        HashMap<String, Integer> rowFcFlag, List<BaseMessage> messageList) {
        CPKKPF12Entity entity = (CPKKPF12Entity) context.get(SESSION_KANBAN_PLAN_NO);
        Timestamp officeTime = cpkkpf12Service.getDBDateTime(param.getOfficeTimezone());

        // 1. Save Kanban Plan main data.
        TntKanban tntKanbanInsert = saveKanbanPlan(param, context, tntKanban, officeTime);
        // 2. Save the Kanban Plan's parts information.
        saveKanbanPlanParts(param, context, tntKanban, tntKanbanInsert, rowPartsId, rowFcFlag, messageList);
        if (messageList.size() > 0) {
            throw new BusinessException(messageList);
        }
        // 3. Save history and completed shipping plan data from the old Kanban plan.
        saveHistoryCompletedShiipingPlan(param, context, tntKanban, tntKanbanInsert);
        // 4. Save the latest shipping plan information.
        saveLatestShippingPlan(param, context, tntKanbanInsert, rowPartsId, rowQtyBox, rowFcFlag, entity);
        // 6. Save not in rundown shipping plan information.
        // 8. Adjust not in rundown shipping plan.
        saveNird(param, context, tntKanban, tntKanbanInsert, rowPartsId, rowFcFlag, entity);
        // 5. Save additional shipping plan information.
        saveAdditionalShippingPlan(param, context, tntKanbanInsert, rowPartsId, rowFcFlag, entity);
        // 7. Save new shipping plan for not in rundown update.
        saveNirdNew(param, context, tntKanban, tntKanbanInsert, rowPartsId, rowFcFlag, entity);
        // Update seaFlag and airFlag
        updateSeaAirFlag(param, tntKanbanInsert);
        // Delete KanbanPlan and KanbanPlanPart that PLAN_TYPE = 3, if PLAN_TYPE = 2 is exist.
        deleteKanbanPlanAndKanbanPlanPart(param, tntKanbanInsert);
        // 9. Save actual invoice revision information.
        saveActualInvoice(param, context);
        // 10. Save the uploaded file on the server.
        saveUploadFileToServer(tntKanbanInsert, request);
    }

    /**
     * Delete KanbanPlan and KanbanPlanPart that PLAN_TYPE = 3, if PLAN_TYPE = 2 is exist.
     * 
     * @param param BaseParam
     * @param tntKanbanInsert tntKanbanInsert the TntKanban that is new
     */
    private void deleteKanbanPlanAndKanbanPlanPart(BaseParam param, TntKanban tntKanbanInsert) {
        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
        tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
        cpkkpf12Service.deleteKanbanPlanPart(tntKanbanPlan);
        cpkkpf12Service.deleteKanbanPlan(tntKanbanPlan);
    }

    /**
     * Update seaFlag and airFlag.<br>
     * If exist SEA transport mode in the Kanban file then 1 else 0<br>
     * If exist AIR transport mode in the Kanban file then 1 else 0<br>
     * 
     * @param param BaseParam
     * @param tntKanbanInsert tntKanbanInsert the TntKanban that is new
     */
    private void updateSeaAirFlag(BaseParam param, TntKanban tntKanbanInsert) {
        cpkkpf12Service.updateSeaAirFlag(param, tntKanbanInsert);
    }

    /**
     * Save the uploaded file on the server.
     * 
     * @param tntKanbanInsert the TntKanban that is new
     * @param request HttpServletRequest
     */
    private void saveUploadFileToServer(TntKanban tntKanbanInsert, HttpServletRequest request) {
        String kanbanFolderPath = ConfigUtil.get(Properties.UPLOAD_PATH_KANBAN);

        // TNT_KANBAN.KANBAN_PLAN_NO + "_R" + TNT_KANBAN.REVISION_VERSION + "_" + TNT_KANBAN.KANBAN_ID + ".xlsx"
        int kanbanId = tntKanbanInsert.getKanbanId();
        // modify for UAT
        // String kanbanPlanNo = tntKanbanInsert.getKanbanPlanNo();
        // int revisionVersion = tntKanbanInsert.getRevisionVersion();
        // DecimalFormat df = new DecimalFormat("00");
        // String fileName = kanbanPlanNo + StringConst.UNDERLINE + StringConst.ALPHABET_R + df.format(revisionVersion)
        // + StringConst.UNDERLINE + kanbanId + CoreConst.SUFFIX_EXCEL;
        String fileName = kanbanId + CoreConst.SUFFIX_EXCEL;
        // modify end
        try {
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            FileUtil.saveFileToPath((InputStream) context.get(SESSION_KEY_FILE), kanbanFolderPath, fileName);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 1. Save Kanban Plan main data.
     * 
     * @param param BaseParam
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param officeTime office time
     * @return the TntKanban that is new
     */
    private TntKanban saveKanbanPlan(BaseParam param, SessionInfoManager context, TntKanban tntKanban,
        Timestamp officeTime) {
        // Find total informations for the old Kanban Plan.
        CPKKPF12QtyInfoEntity condition = new CPKKPF12QtyInfoEntity();
        condition.setKanbanId(tntKanban.getKanbanId());
        CPKKPF12QtyInfoEntity qtyInfo = cpkkpf12Service.getQtyInfo(condition);

        // Update old TNT_KANBAN.
        TntKanban tntKanbanUpdate = new TntKanban();
        tntKanbanUpdate.setKanbanId(tntKanban.getKanbanId());
        tntKanbanUpdate.setTotalOrderQty(qtyInfo.getOrderQty());
        tntKanbanUpdate.setTotalOnshippingQty(qtyInfo.getOnShippingQty());
        tntKanbanUpdate.setTotalInboundQty(qtyInfo.getInboundQty());
        tntKanbanUpdate.setTotalBalanceQty(qtyInfo.getOrderBalance());
        cpkkpf12Service.updateKanban(param, tntKanbanUpdate);

        // Insert into new TNT_KANBAN.
        TntKanban tntKanbanInsert = new TntKanban();
        int kanbanIdInsert = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN);
        tntKanbanInsert.setKanbanId(kanbanIdInsert);
        tntKanbanInsert.setOfficeId(param.getCurrentOfficeId());
        tntKanbanInsert.setKanbanPlanNo(tntKanban.getKanbanPlanNo());
        tntKanbanInsert.setCustomerId(tntKanban.getCustomerId());
        tntKanbanInsert.setSupplierId(tntKanban.getSupplierId());
        tntKanbanInsert.setOrderMonth(tntKanban.getOrderMonth());
        tntKanbanInsert.setUploadedBy(param.getLoginUserId());
        tntKanbanInsert.setUploadedDate(officeTime);
        tntKanbanInsert.setRevisionVersion(tntKanban.getRevisionVersion() + 1);
        tntKanbanInsert.setRevisionCodeSet((String) param.getSwapData().get("revisionCodeSet"));
        tntKanbanInsert.setRevisionReason((String) param.getSwapData().get("revisionReason"));
        tntKanbanInsert.setSeaFlag(tntKanban.getSeaFlag());
        tntKanbanInsert.setAirFlag(tntKanban.getAirFlag());
        tntKanbanInsert.setTotalOrderQty(null);
        tntKanbanInsert.setTotalOnshippingQty(null);
        tntKanbanInsert.setTotalInboundQty(null);
        tntKanbanInsert.setTotalBalanceQty(null);
        tntKanbanInsert.setUploadFileType(CodeConst.UploadFileType.UPDATED_KANBAN_FILE);
        tntKanbanInsert.setStatus(1);
        cpkkpf12Service.insertKanban(param, tntKanbanInsert);
        return tntKanbanInsert;
    }

    /**
     * Calculate Order Forecast.
     * 
     * @param param BaseParam
     * @param entityParts parts info
     * @param tntKanbanInsert the TntKanban that is new
     * @param fcQty fc qty
     * @param monthAddN 1-6
     * @param pfcDetailId pfcDetailId
     * @param qtyBox qtyBox
     * @param checkNoShippingMap checkNoShippingMap
     */
    private void updatePfcShipping(BaseParam param, CPKKPF12AllPartsInfoEntity entityParts, TntKanban tntKanbanInsert,
        BigDecimal fcQty, int monthAddN, int pfcDetailId, BigDecimal qtyBox, HashMap<String, String> checkNoShippingMap) {
        if (fcQty == null || fcQty.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        // Get the first day and the last day
        String fcMonth = DateTimeUtil.monthAddN(tntKanbanInsert.getOrderMonth(), monthAddN);
        // String fcMonthFirstDay = fcMonth + "01";
        // Date dFcMonth = DateTimeUtil.parseDate(DateTimeUtil.formatDate(fcMonthFirstDay));
        // String firstDay = DateTimeUtil.formatDate(DateTimeUtil.firstDay(dFcMonth));
        // String lastDay = DateTimeUtil.formatDate(DateTimeUtil.lastDay(dFcMonth));

        TnmKbIssuedDate tnmKbIssuedDate = new TnmKbIssuedDate();
        tnmKbIssuedDate.setOrderMonth(fcMonth);
        tnmKbIssuedDate.setCustomerId(tntKanbanInsert.getCustomerId());
        TnmKbIssuedDate kbIssuedDate = cpkkpf12Service.getKbIssuedDate(tnmKbIssuedDate);
        if (kbIssuedDate == null) {
            return;
        }

        // Get working day
        param.setSwapData("CALENDAR_DATE_FROM", kbIssuedDate.getFromDate());
        param.setSwapData("CALENDAR_DATE_TO", kbIssuedDate.getToDate());
        param.setSwapData("PARTY_TYPE", CodeConst.CalendarParty.SUPPLIER);
        param.setSwapData("SUPPLIER_ID", entityParts.getSupplierId());
        List<TnmCalendarDetail> workingDay = cpkkpf12Service.getWorkingDay(param);

        BigDecimal fcQtyBox = fcQty.divide(qtyBox, NumberConst.IntDef.INT_TEN, BigDecimal.ROUND_UP);
        // Get working day conut
        int workingDays = workingDay.size();
        // Get fcQty int (UP)
        int fcQtyInt = fcQtyBox.setScale(0, BigDecimal.ROUND_UP).intValue();
        // Get fcQty avg value for update everyday
        BigDecimal fcQtyEveryDayAvg = BigDecimal.valueOf(fcQtyInt / workingDays);
        // Get fcQty scale value for update last day
        BigDecimal fcQtySubForLast = BigDecimal.valueOf(fcQtyInt).subtract(fcQtyBox);

        // Get remainder day index
        HashMap<Integer, Integer> dayIndex = new HashMap<Integer, Integer>();
        if (fcQtyInt == 1) {
            dayIndex.put(NumberConst.IntDef.INT_FIVE, NumberConst.IntDef.INT_FIVE);
        } else {
            dayIndex = calc(fcQtyInt, workingDays);
        }

        // Call common to get plan
        List<SupplyChainEntity> scList = new ArrayList<SupplyChainEntity>();
        SupplyChainEntity scEntity = new SupplyChainEntity();
        for (int i = 0; i < workingDay.size(); i++) {
            scEntity = new SupplyChainEntity();
            scEntity.setChainStartDate(workingDay.get(i).getCalendarDate());
            scEntity.setExpPartsId(entityParts.getExpPartsId());
            scEntity.setPartsId(entityParts.getPartsId());
            scEntity.setTansportMode(CodeConst.TransportMode.SEA);
            scList.add(scEntity);
        }
        supplyChainService.prepareSupplyChain(scList, ChainStep.FORECAST, BusinessPattern.AISIN, false);

        // Insert TNT_PFC_SHIPPING
        for (int i = 0; i < scList.size(); i++) {
            BigDecimal qty = fcQtyEveryDayAvg;
            if (dayIndex.containsKey(i + 1)) {
                qty = qty.add(BigDecimal.ONE);
                if (dayIndex.size() == 1) {
                    qty = qty.subtract(fcQtySubForLast);
                }
                dayIndex.remove(i + 1);
            }
            qty = qty.multiply(qtyBox);

            SupplyChainEntity entity = scList.get(i);
            if (entity.getEtd() == null || entity.getEta() == null || entity.getImpPlanInboundDate() == null
                    || qty.compareTo(BigDecimal.ZERO) == 0) {
                if (entity.getEtd() == null || entity.getEta() == null || entity.getImpPlanInboundDate() == null) {
                    if (!checkNoShippingMap.containsKey(entity.getShippingRouteCode())) {
                        checkNoShippingMap.put(entity.getShippingRouteCode(), entity.getShippingRouteCode());
                    }
                }
                continue;
            }
            TntPfcShipping tntPfcShipping = new TntPfcShipping();
            int pfcShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_PFC_SHIPPING);
            tntPfcShipping.setPfcShippingId(pfcShippingId);
            tntPfcShipping.setPfcDetailId(pfcDetailId);
            tntPfcShipping.setOfficeId(param.getCurrentOfficeId());
            tntPfcShipping.setBusinessPattern(BusinessPattern.AISIN);
            tntPfcShipping.setFcMonth(fcMonth);
            tntPfcShipping.setPartsId(entity.getPartsId());
            tntPfcShipping.setTransportMode(CodeConst.TransportMode.SEA);
            tntPfcShipping.setVanningDate(entity.getVanningDate());
            tntPfcShipping.setEtd(entity.getEtd());
            tntPfcShipping.setEta(entity.getEta());
            tntPfcShipping.setCcDate(entity.getImpPlanCustomDate());
            tntPfcShipping.setImpInbPlanDate(entity.getImpPlanInboundDate());
            tntPfcShipping.setQty(qty);
            tntPfcShipping.setValidFlag(1);
            cpkkpf12Service.insertPfcShipping(param, tntPfcShipping);
        }
    }

    /**
     * Get day index for plus avgDay.
     * 
     * @param totalKanbanQty totalKanbanQty
     * @param workDay workDay
     * @return key:day index
     */
    private HashMap<Integer, Integer> calc(int totalKanbanQty, int workDay) {
        HashMap<Integer, Integer> dayIndex = new HashMap<Integer, Integer>();
        int avg = DecimalUtil.divide(BigDecimal.valueOf(totalKanbanQty), BigDecimal.valueOf(workDay)).intValue();
        int remainder = totalKanbanQty - avg * workDay;
        BigDecimal space = DecimalUtil.divide(BigDecimal.valueOf(workDay), BigDecimal.valueOf(remainder));
        for (int i = 0; i < remainder; i++) {
            int day = DecimalUtil.multiply(BigDecimal.valueOf(i), space).intValue() + 1;
            dayIndex.put(day, day);
        }
        return dayIndex;
    }

    /**
     * 2. Save the Kanban Plan's parts information.
     * 
     * @param param BaseParam
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param messageList message list
     */
    @SuppressWarnings("unchecked")
    private void saveKanbanPlanParts(BaseParam param, SessionInfoManager context, TntKanban tntKanban,
        TntKanban tntKanbanInsert, HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag,
        List<BaseMessage> messageList) {
        // Find parts in the old Kanban Plan.
        TntKanbanPart conditionTntKanbanPart = new TntKanbanPart();
        conditionTntKanbanPart.setKanbanId(tntKanban.getKanbanId());
        HashMap<Integer, TntKanbanPart> kanbanPartsOldInfo = cpkkpf12Service
            .getPartsInfoOfOldKanbanPlan(conditionTntKanbanPart);

        // Find order status in the Kanban Plan.
        TnfOrderStatus conditionTnfOrderStatus = new TnfOrderStatus();
        conditionTnfOrderStatus.setKanbanPlanNo(tntKanbanInsert.getKanbanPlanNo());
        HashMap<Integer, TnfOrderStatus> orderStatusInfo = cpkkpf12Service.getOrderStatusInfo(conditionTnfOrderStatus);

        // loop each part in the input file.
        HashMap<String, String> checkNoShippingMap = new HashMap<String, String>();
        List<CPKKPF12RowEntity> listRow = (List<CPKKPF12RowEntity>) context.get(SESSION_ROW);
        HashMap<Integer, CPKKPF12RowFfEntity> mapRowFf = (HashMap<Integer, CPKKPF12RowFfEntity>) context
            .get(SESSION_ROW_FF);
        HashMap<Integer, CPKKPF12AllPartsInfoEntity> allPartsInfo = (HashMap<Integer, CPKKPF12AllPartsInfoEntity>) context
            .get(SESSION_ALL_PARTS_INFO);
        for (CPKKPF12RowEntity rowData : listRow) {
            // if (rowFcFlag.get(String.valueOf(rowData.getRowNo())).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
            // continue;
            // }
            int partsId = rowPartsId.get(String.valueOf(rowData.getRowNo()));
            TntKanbanPart kanbanPartsOld = kanbanPartsOldInfo.get(partsId);
            TnfOrderStatus orderStatus = orderStatusInfo.get(partsId);
            CPKKPF12RowFfEntity rowFfData = mapRowFf.get(rowData.getRowNo());

            BigDecimal fcQty1 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty1());
            BigDecimal fcQty2 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty2());
            BigDecimal fcQty3 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty3());
            BigDecimal fcQty4 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty4());
            BigDecimal fcQty5 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty5());
            BigDecimal fcQty6 = DecimalUtil.getBigDecimalWithNUll(rowFfData.getFcQty6());

            // Insert into TNT_KANBAN_PARTS.
            TntKanbanPart tntKanbanPartInsert = new TntKanbanPart();
            int kanbanPartsIdInsert = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PARTS);
            tntKanbanPartInsert.setKanbanPartsId(kanbanPartsIdInsert);
            tntKanbanPartInsert.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanPartInsert.setSeqNo(kanbanPartsOld.getSeqNo());
            tntKanbanPartInsert.setPlant(kanbanPartsOld.getPlant());
            tntKanbanPartInsert.setDock(kanbanPartsOld.getDock());
            tntKanbanPartInsert.setBoxNo(kanbanPartsOld.getBoxNo());
            tntKanbanPartInsert.setBoxType(kanbanPartsOld.getBoxType());
            tntKanbanPartInsert.setOrderMonth(tntKanbanInsert.getOrderMonth());
            tntKanbanPartInsert.setSupplierId(tntKanbanInsert.getSupplierId());
            tntKanbanPartInsert.setCustomerId(tntKanbanInsert.getCustomerId());
            tntKanbanPartInsert.setPartsId(partsId);
            tntKanbanPartInsert.setSpq(DecimalUtil.getBigDecimal(rowData.getQtyBox()));
            tntKanbanPartInsert.setQty(DecimalUtil.getBigDecimal(rowData.getOrderQty()));
            tntKanbanPartInsert.setKanbanQty(DecimalUtil.getBigDecimal(rowData.getKanbanQty()));
            tntKanbanPartInsert.setFcQty1(fcQty1);
            tntKanbanPartInsert.setFcQty2(fcQty2);
            tntKanbanPartInsert.setFcQty3(fcQty3);
            tntKanbanPartInsert.setFcQty4(fcQty4);
            tntKanbanPartInsert.setFcQty5(fcQty5);
            tntKanbanPartInsert.setFcQty6(fcQty6);
            tntKanbanPartInsert.setRemark(rowData.getRemark());
            tntKanbanPartInsert.setForceCompletedBy(kanbanPartsOld.getForceCompletedBy());
            tntKanbanPartInsert.setForceCompletedDate(kanbanPartsOld.getForceCompletedDate());
            tntKanbanPartInsert.setStatus(kanbanPartsOld.getStatus());
            cpkkpf12Service.insertKanbanParts(param, tntKanbanPartInsert);

            // Update TNF_ORDER_STATUS.
            TnfOrderStatus tnfOrderStatusUpdate = new TnfOrderStatus();
            tnfOrderStatusUpdate.setOrderStatusId(orderStatus.getOrderStatusId());
            tnfOrderStatusUpdate.setOrderQty(DecimalUtil.getBigDecimal(rowData.getOrderQty()));
            cpkkpf12Service.updateOrderStatus(param, tnfOrderStatusUpdate);

            // Save Order Forecast Information.
            // Find exist data.
            int pfcDetailId = 0;
            TntPfcDetail tntPfcDetailCondition = new TntPfcDetail();
            tntPfcDetailCondition.setKanbanPlanNo(tntKanbanInsert.getKanbanPlanNo());
            tntPfcDetailCondition.setPartsId(partsId);
            TntPfcDetail tntPfcDetail = cpkkpf12Service.getPfcDetail(tntPfcDetailCondition);
            if (tntPfcDetail != null) {
                // Update if exist
                TntPfcDetail tntPfcDetailUpdate = new TntPfcDetail();
                pfcDetailId = tntPfcDetail.getPfcDetailId();
                tntPfcDetailUpdate.setPfcDetailId(tntPfcDetail.getPfcDetailId());
                tntPfcDetailUpdate.setFcQty1(fcQty1);
                tntPfcDetailUpdate.setFcQty2(fcQty2);
                tntPfcDetailUpdate.setFcQty3(fcQty3);
                tntPfcDetailUpdate.setFcQty4(fcQty4);
                tntPfcDetailUpdate.setFcQty5(fcQty5);
                tntPfcDetailUpdate.setFcQty6(fcQty6);
                cpkkpf12Service.updatePfcDetail(param, tntPfcDetailUpdate);
            } else {
                // Insert if not exist
                TntPfcDetail tntPfcDetailInsert = new TntPfcDetail();
                pfcDetailId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_PFC_DETAIL);
                tntPfcDetailInsert.setPfcDetailId(pfcDetailId);
                tntPfcDetailInsert.setKanbanPlanNo(tntKanbanInsert.getKanbanPlanNo());
                tntPfcDetailInsert.setPartsId(partsId);
                tntPfcDetailInsert.setSupplierId(tntKanbanInsert.getSupplierId());
                tntPfcDetailInsert.setShippingRouteCode(allPartsInfo.get(partsId).getShippingRouteCode());
                tntPfcDetailInsert.setFcQty1(fcQty1);
                tntPfcDetailInsert.setFcQty2(fcQty2);
                tntPfcDetailInsert.setFcQty3(fcQty3);
                tntPfcDetailInsert.setFcQty4(fcQty4);
                tntPfcDetailInsert.setFcQty5(fcQty5);
                tntPfcDetailInsert.setFcQty6(fcQty6);
                tntPfcDetailInsert.setRedoShippingFlag(0);
                cpkkpf12Service.insertPfcDetail(param, tntPfcDetailInsert);
            }

            // Calculate Order Forecast.
            TntPfcShipping tntPfcShipping = new TntPfcShipping();
            tntPfcShipping.setPfcDetailId(pfcDetailId);
            cpkkpf12Service.updatePfcShipping(param, tntPfcShipping);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty1, NumberConst.IntDef.INT_ONE,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty2, NumberConst.IntDef.INT_TWO,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty3, NumberConst.IntDef.INT_THREE,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty4, NumberConst.IntDef.INT_FOUR,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty5, NumberConst.IntDef.INT_FIVE,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
            updatePfcShipping(param, allPartsInfo.get(partsId), tntKanbanInsert, fcQty6, NumberConst.IntDef.INT_SIX,
                pfcDetailId, DecimalUtil.getBigDecimal(rowData.getQtyBox()), checkNoShippingMap);
        }
        // Shipping route master is not exist or not complete. Please use Shipping route master Screen to complete it.
        for (Map.Entry<String, String> entry : checkNoShippingMap.entrySet()) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_153);
            message.setMessageArgs(new String[] { entry.getKey() });
            messageList.add(message);
        }
    }

    /**
     * 3. Save history and completed shipping plan data from the old Kanban plan.
     * 
     * @param param BaseParam
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param tntKanbanInsert the TntKanban that is new
     */
    private void saveHistoryCompletedShiipingPlan(BaseParam param, SessionInfoManager context, TntKanban tntKanban,
        TntKanban tntKanbanInsert) {
        // Find history and completed shipping plan and parts qty information.
        CPKKPF12HistoryCompletedEntity conditionHc = new CPKKPF12HistoryCompletedEntity();
        conditionHc.setKanbanId(tntKanban.getKanbanId());
        List<CPKKPF12HistoryCompletedEntity> hcList = cpkkpf12Service
            .getHistoryCompletedKanbanPlanAndPartsQty(conditionHc);

        // Find completed kanban plan and parts qty information.
        CPKKPF12CompletedEntity conditionC = new CPKKPF12CompletedEntity();
        conditionC.setKanbanId(tntKanban.getKanbanId());
        List<CPKKPF12CompletedEntity> cList = cpkkpf12Service.getCompletedKanbanPlanAndPartsQty(conditionC);

        // Insert into TNT_KANBAN_SHIPPING and TNT_KANBAN_SHIPPING_PARTS.
        int kanbanShippingId = 0;
        String keyPre = StringConst.NEW_COMMA;
        for (int i = 0; i < hcList.size(); i++) {
            CPKKPF12HistoryCompletedEntity e = hcList.get(i);
            String key = StringUtil.nullToEmpty(e.getShippingUuid()) + StringConst.NEW_COMMA
                    + String.valueOf(e.getOriginalVersion()) + StringConst.NEW_COMMA
                    + StringUtil.nullToEmpty(e.getRevisionVersion());
            if (!key.equals(keyPre)) {
                String revisionVersion = e.getRevisionVersion();
                // Insert into TNT_KANBAN_SHIPPING
                kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
                TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
                tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
                tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
                tntKanbanShipping.setShippingUuid(e.getShippingUuid());
                tntKanbanShipping.setTransportMode(e.getTransportMode());
                tntKanbanShipping.setEtd(e.getEtd());
                tntKanbanShipping.setEta(e.getEta());
                tntKanbanShipping.setImpInbPlanDate(e.getImpInbPlanDate());
                tntKanbanShipping.setOriginalVersion(e.getOriginalVersion());
                if (StringUtils.isBlank(revisionVersion)) {
                    tntKanbanShipping.setRevisionVersion(null);
                } else {
                    tntKanbanShipping.setRevisionVersion(Integer.parseInt(revisionVersion));
                }
                tntKanbanShipping.setRevisionReason(e.getRevisionReason());
                tntKanbanShipping.setCompletedFlag(e.getCompletedFlag());
                tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
                cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);
            }
            keyPre = key;

            // Insert into TNT_KANBAN_SHIPPING_PARTS.
            int ksPartsId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
            TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
            tntKanbanShippingPart.setKspId(ksPartsId);
            tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
            tntKanbanShippingPart.setPartsId(e.getPartsId());
            tntKanbanShippingPart.setQty(e.getQty());
            tntKanbanShippingPart.setKanbanQty(null);
            cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);
        }

        // Insert into TNT_KANBAN_PLAN and TNT_KANBAN_PLAN_PARTS.
        int kanbanPlanId = 0;
        keyPre = StringConst.NEW_COMMA;
        for (int i = 0; i < cList.size(); i++) {
            CPKKPF12CompletedEntity e = cList.get(i);
            String key = StringUtil.nullToEmpty(e.getShippingUuid()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(e.getIssuedDate()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(e.getDeliveredDate()) + StringConst.NEW_COMMA
                    + DateTimeUtil.formatDate(e.getVanningDate());
            if (!key.equals(keyPre)) {
                // Insert into TNT_KANBAN_PLAN
                kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
                TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
                tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
                tntKanbanPlan.setShippingUuid(e.getShippingUuid());
                tntKanbanPlan.setOrderMonth(e.getOrderMonth());
                tntKanbanPlan.setPlanType(e.getPlanType());
                tntKanbanPlan.setIssuedDate(e.getIssuedDate());
                tntKanbanPlan.setIssueRemark(e.getIssueRemark());
                tntKanbanPlan.setDeliveredDate(e.getDeliveredDate());
                tntKanbanPlan.setDelivereRemark(e.getDelivereRemark());
                tntKanbanPlan.setVanningDate(e.getVanningDate());
                tntKanbanPlan.setVanningRemark(e.getVanningRemark());
                tntKanbanPlan.setRevisionReason(e.getRevisionReason());
                cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);
            }
            keyPre = key;

            // Insert into TNT_KANBAN_PLAN_PARTS
            int kpPartsId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
            TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
            tntKanbanPlanPart.setKbPlanPartsId(kpPartsId);
            tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
            tntKanbanPlanPart.setPartsId(e.getPartsId());
            tntKanbanPlanPart.setQty(e.getQty());
            tntKanbanPlanPart.setKanbanQty(e.getKanbanQty());
            cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
        }
    }

    /**
     * Merge the same plan(MOD column and one DATA column that has no modified).<br>
     * has difference/box merge to has difference/box: do merge<br>
     * has difference/box merge to no difference/box: do merge<br>
     * no difference/box merge to has difference/box: do not merge<br>
     * 
     * @param listPlan shipping plan list
     * @return mapReason
     */
    private HashMap<String, String> mergeSamePlan(List<CPKKPF12ColEntity> listPlan) {
        HashMap<String, String> mapReason = new HashMap<String, String>();
        for (int i = 0; i < listPlan.size(); i++) {
            CPKKPF12ColEntity entity = listPlan.get(i);
            int columnType = entity.getColumnTypeAll();
            boolean hasRevisionData = hasRevisionData(entity);
            boolean hasDiffBox = hasDiffBox(entity.getListBox(), entity.getListDiff());

            // If current column is DATA column and RevisionReasonThisChange is input, then compare to DATA column.
            if (hasRevisionData && columnType == ColumnTypeAll.PLAN) {
                // Get xxxRevision's value of current column.
                int colNo = entity.getColNo();
                String transportMode = entity.getTransportModeRevision();
                String etd = entity.getEtdRevision();
                String eta = entity.getEtaRevision();
                String impInbPlanDate = entity.getImpInbPlanDateRevision();
                boolean hasModData = hasModData(listPlan, i);

                // Compare to all plan DATA column
                for (int j = 0; j < listPlan.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    CPKKPF12ColEntity entityCompare = listPlan.get(j);
                    int columnTypeCompare = entityCompare.getColumnTypeAll();
                    String transportModeCompare = entityCompare.getTransportMode();
                    String etdCompare = entityCompare.getEtd();
                    String etaCompare = entityCompare.getEta();
                    String impInbPlanDateCompare = entityCompare.getImpInbPlanDate();
                    boolean hasRevisionDataCompare = hasRevisionData(entityCompare);
                    boolean hasDiffBoxCompare = hasDiffBox(entityCompare.getListBox(), entityCompare.getListDiff());

                    // If same as DATA column and this DATA column has no modified, do merge.
                    if (columnTypeCompare == ColumnTypeAll.PLAN && transportMode.equals(transportModeCompare)
                            && etd.equals(etdCompare) && eta.equals(etaCompare)
                            && impInbPlanDate.equals(impInbPlanDateCompare) && !hasRevisionDataCompare
                            && !(!hasDiffBox && hasDiffBoxCompare)) {
                        // Do merge
                        entity.setMergeFlag(MergeFlag.MERGED_SRC);
                        entityCompare.setMergeFlag(MergeFlag.MERGED_DIS);

                        StringBuffer revisionReason = new StringBuffer();
                        String key = String.valueOf(entityCompare.getColNo());
                        if (mapReason.containsKey(key)) {
                            revisionReason.append(mapReason.get(key));
                        }
                        revisionReason.append(entity.getRevisionReasonThisChange());
                        if (hasModData) {
                            CPKKPF12ColEntity entityMod = listPlan.get(i + 1);
                            revisionReason.append(entityMod.getRevisionReasonThisChange());
                        }
                        mapReason.put(key, revisionReason.toString());

                        if (!mapReason.containsKey(key + StringConst.ALPHABET_R)) {
                            StringBuffer revisionReasonMerge = new StringBuffer();
                            revisionReasonMerge.append(entityCompare.getRevisionReasonThisChange());
                            boolean hasModDataCompare = hasModData(listPlan, j);
                            if (hasModDataCompare) {
                                CPKKPF12ColEntity entityCompareMod = listPlan.get(j + 1);
                                revisionReasonMerge.append(entityCompareMod.getRevisionReasonThisChange());
                            }
                            mapReason.put(key + StringConst.ALPHABET_R, revisionReasonMerge.toString());
                        }

                        if (!hasDiffBox && !hasDiffBoxCompare) {
                            // No box/diff merge to no box/diff
                            List<Integer> listPlanMergeCompare = entityCompare.getListPlanMerge();
                            if (listPlanMergeCompare == null) {
                                listPlanMergeCompare = new ArrayList<Integer>();
                            }
                            if (hasModData) {
                                listPlanMergeCompare.add(colNo + 1);
                            } else {
                                listPlanMergeCompare.add(colNo);
                            }
                            entityCompare.setListPlanMerge(listPlanMergeCompare);
                        } else {
                            // Has box/diff merge to no box/diff, Has box/diff merge to has box/diff
                            List<Integer> listBoxMergeCompare = entityCompare.getListBoxMerge();
                            if (listBoxMergeCompare == null) {
                                listBoxMergeCompare = new ArrayList<Integer>();
                            }
                            List<Integer> listBoxMerge = entity.getListBox();
                            if (listBoxMerge != null) {
                                listBoxMergeCompare.addAll(listBoxMerge);
                            }
                            entityCompare.setListBoxMerge(listBoxMergeCompare);

                            List<Integer> listDiffMergeCompare = entityCompare.getListDiffMerge();
                            if (listDiffMergeCompare == null) {
                                listDiffMergeCompare = new ArrayList<Integer>();
                            }
                            List<Integer> listDiffMerge = entity.getListDiff();
                            if (listDiffMerge != null) {
                                listDiffMergeCompare.addAll(listDiffMerge);
                            }
                            entityCompare.setListDiffMerge(listDiffMergeCompare);
                        }
                    }
                }
            }
        }
        return mapReason;
    }

    /**
     * Total Row Data Common
     * 
     * @param rowDataList row data list
     * @param rowDataListTemp rowDataListData / rowDataListMod
     * @param rowQtyBox key:rowNo., value:input qty/box
     */
    private void totalRowDataCommon(List<String[]> rowDataList, List<String[]> rowDataListTemp,
        HashMap<String, BigDecimal> rowQtyBox) {
        for (int i = 0; i < rowDataList.size(); i++) {
            BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataListTemp.get(i)[1]);
            BigDecimal qty = DecimalUtil.getBigDecimal(rowDataList.get(i)[1]);
            if (rowQtyBox != null) {
                BigDecimal qtyBox = rowQtyBox.get(rowDataList.get(i)[0]);
                qtyTemp = qtyTemp.add(qty.multiply(qtyBox));
            } else {
                qtyTemp = qtyTemp.add(qty);
            }
            rowDataListTemp.set(i, new String[] { rowDataListTemp.get(i)[0], String.valueOf(qtyTemp) });
        }
    }

    /**
     * Total shipping plan's row data from difference and box for insert tlb[TNT_KANBAN_SHIPPING_PARTS].
     * 
     * @param listPlan shipping plan list
     * @param currentIndex current read index
     * @param mapDiff key:colNo, value:difference column data
     * @param mapBox key:colNo, value:box column data
     * @param rowDataListData data column's row data
     * @param rowDataListMod mod column's row data
     * @param hasModData has mod data flag
     * @param rowQtyBox key:rowNo., value:input qty/box
     */
    private void totalPlanRowDataFromDifBox(List<CPKKPF12ColEntity> listPlan, int currentIndex,
        HashMap<Integer, CPKKPF12ColEntity> mapDiff, HashMap<Integer, CPKKPF12ColEntity> mapBox,
        List<String[]> rowDataListData, List<String[]> rowDataListMod, boolean hasModData,
        HashMap<String, BigDecimal> rowQtyBox) {
        CPKKPF12ColEntity entityPlan = listPlan.get(currentIndex);
        boolean hasDiffBox = hasDiffBox(entityPlan.getListBox(), entityPlan.getListDiff());
        boolean hasDiffBoxMerge = hasDiffBox(entityPlan.getListBoxMerge(), entityPlan.getListDiffMerge());
        List<Integer> listPlanMerge = entityPlan.getListPlanMerge();

        // =================1. Get current plan's total data row and total mod row.
        if (!hasDiffBox) {
            // 1.1 If current shipping plan has not difference and box, then return current shipping plan's row data.
            rowDataListData.addAll(entityPlan.getRowDataList());
            if (hasModData) {
                CPKKPF12ColEntity entityNext = listPlan.get(currentIndex + 1);
                rowDataListMod.addAll(entityNext.getRowDataForUpdateList());
            }
        } else {
            // 1.2 If current shipping plan has difference or box, then total difference and box row.
            // Init rowDataListData and rowDataListMod
            for (int i = 0; i < entityPlan.getRowDataList().size(); i++) {
                String rowNo = entityPlan.getRowDataList().get(i)[0];
                rowDataListData.add(new String[] { rowNo, String.valueOf(BigDecimal.ZERO) });
                rowDataListMod.add(new String[] { rowNo, String.valueOf(BigDecimal.ZERO) });
            }
            // Total shipping plan's difference row
            List<Integer> listDiff = entityPlan.getListDiff();
            for (int colNo : listDiff) {
                CPKKPF12ColEntity entityDiff = mapDiff.get(colNo);
                int columnType = entityDiff.getColumnTypeAll();
                if (columnType == ColumnTypeAll.DIFFERENCE_DATA) {
                    List<String[]> rowDataList = entityDiff.getRowDataList();
                    totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);

                    CPKKPF12ColEntity entityDiffMod = mapDiff.get(colNo + 1);
                    if (entityDiffMod == null) {
                        totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                    }
                } else if (columnType == ColumnTypeAll.DIFFERENCE_MOD) {
                    List<String[]> rowDataList = entityDiff.getRowDataForUpdateList();
                    totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                }
            }
            // Total shipping plan's box row
            List<Integer> listBox = entityPlan.getListBox();
            for (int colNo : listBox) {
                CPKKPF12ColEntity entityBox = mapBox.get(colNo);
                int columnType = entityBox.getColumnTypeAll();
                if (columnType == ColumnTypeAll.BOX_DATA) {
                    List<String[]> rowDataList = entityBox.getRowDataList();
                    totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);

                    CPKKPF12ColEntity entityBoxMod = mapBox.get(colNo + 1);
                    if (entityBoxMod == null) {
                        totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                    }
                } else if (columnType == ColumnTypeAll.BOX_MOD) {
                    List<String[]> rowDataList = entityBox.getRowDataForUpdateList();
                    totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                }
            }
        }

        // =================2. Merge difference and box to current shipping plan.
        // =================Case1: Shipping plan that has DiffBox -> Shipping plan that has DiffBox
        // =================Case2: Shipping plan that has DiffBox -> Shipping plan that has not DiffBox
        // If merger difference/box is exist, then add merger difference/box to DATA column(rowDataListData),
        // because shipping plan can not merge to shipping plan which has modified.
        if (hasDiffBoxMerge) {
            List<Integer> listDifMergef = entityPlan.getListDiffMerge();
            for (int colNo : listDifMergef) {
                CPKKPF12ColEntity entityDiff = mapDiff.get(colNo);
                int columnType = entityDiff.getColumnTypeAll();
                if (columnType == ColumnTypeAll.DIFFERENCE_DATA) {
                    CPKKPF12ColEntity entityDiffMod = mapDiff.get(colNo + 1);
                    if (entityDiffMod == null) {
                        List<String[]> rowDataList = entityDiff.getRowDataList();
                        if (hasModData) {
                            totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                        } else {
                            totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);
                        }
                    } else {
                        List<String[]> rowDataList = entityDiffMod.getRowDataForUpdateList();
                        if (hasModData) {
                            totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                        } else {
                            totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);
                        }
                    }
                }
            }
            List<Integer> listBoxMerge = entityPlan.getListBoxMerge();
            for (int colNo : listBoxMerge) {
                CPKKPF12ColEntity entityBox = mapBox.get(colNo);
                int columnType = entityBox.getColumnTypeAll();
                if (columnType == ColumnTypeAll.BOX_DATA) {
                    CPKKPF12ColEntity entityBoxMod = mapBox.get(colNo + 1);
                    if (entityBoxMod == null) {
                        List<String[]> rowDataList = entityBox.getRowDataList();
                        if (hasModData) {
                            totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                        } else {
                            totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);
                        }
                    } else {
                        List<String[]> rowDataList = entityBoxMod.getRowDataForUpdateList();
                        if (hasModData) {
                            totalRowDataCommon(rowDataList, rowDataListMod, rowQtyBox);
                        } else {
                            totalRowDataCommon(rowDataList, rowDataListData, rowQtyBox);
                        }
                    }
                }
            }
        }

        // =================3. Merge shipping plan to current shipping plan.
        // =================Case1: Shipping plan that has not DiffBox -> Shipping plan that has not DiffBox
        if (listPlanMerge != null && listPlanMerge.size() > 0) {
            for (int colNo : listPlanMerge) {
                for (CPKKPF12ColEntity entityMerge : listPlan) {
                    if (colNo == entityMerge.getColNo()) {
                        List<String[]> rowDataListMerge = entityMerge.getRowDataForUpdateList();
                        if (rowDataListMerge == null || rowDataListMerge.size() < 1) {
                            rowDataListMerge = entityMerge.getRowDataList();
                        }

                        if (hasModData) {
                            for (int i = 0; i < rowDataListMerge.size(); i++) {
                                BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataListMod.get(i)[1]);
                                BigDecimal qty = DecimalUtil.getBigDecimal(rowDataListMerge.get(i)[1]);
                                qtyTemp = qtyTemp.add(qty);
                                rowDataListMod.set(i,
                                    new String[] { rowDataListMod.get(i)[0], String.valueOf(qtyTemp) });
                            }
                        } else {
                            for (int i = 0; i < rowDataListMerge.size(); i++) {
                                BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataListData.get(i)[1]);
                                BigDecimal qty = DecimalUtil.getBigDecimal(rowDataListMerge.get(i)[1]);
                                qtyTemp = qtyTemp.add(qty);
                                rowDataListData.set(i,
                                    new String[] { rowDataListData.get(i)[0], String.valueOf(qtyTemp) });
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Shipping Plan has input revision data.
     * 
     * @param entityPlan current shipping plan
     * @return true:Shipping Plan has input revision data/false:Shipping Plan not input revision data
     */
    private boolean hasRevisionData(CPKKPF12ColEntity entityPlan) {
        if (!StringUtils.isBlank(entityPlan.getRevisionReasonThisChange())) {
            return true;
        }
        return false;
    }

    /**
     * Shipping Plan has MOD column and has input.
     * 
     * @param listPlan shipping plan list
     * @param currentIndex current read index
     * @return true:has MOD column and has input/false:has no MOD column or has MOD column but not input.
     */
    private boolean hasModData(List<CPKKPF12ColEntity> listPlan, int currentIndex) {
        if (currentIndex != listPlan.size() - 1) {
            CPKKPF12ColEntity entityNext = listPlan.get(currentIndex + 1);
            if (entityNext.getColumnTypeAll() == ColumnTypeAll.PLAN_MOD
                    && !StringUtils.isBlank(entityNext.getRevisionReasonThisChange())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get revision reason for insert.<br>
     * Shipping plan has not MOD column, then return DATA column's RevisionReasonThisChange.<br>
     * Shipping plan has MOD column, then return [DATA column's RevisionReasonThisChange + MOD column's
     * RevisionReasonThisChange](80 byte).<br>
     * 
     * @param listPlan shipping plan list
     * @param currentIndex current read index
     * @param hasModData true:has MOD column and has input/false:has no MOD column or has MOD column but not input.
     * @return revision reason for insert
     */
    private String getModRevisionReason(List<CPKKPF12ColEntity> listPlan, int currentIndex, boolean hasModData) {
        CPKKPF12ColEntity entity = listPlan.get(currentIndex);
        if (hasModData) {
            String revisionReasonData = StringUtil.nullToEmpty(entity.getRevisionReasonThisChange());
            CPKKPF12ColEntity entityNext = listPlan.get(currentIndex + 1);
            String revisionReasonMod = StringUtil.nullToEmpty(entityNext.getRevisionReasonThisChange());
            String revisionReason = revisionReasonData + revisionReasonMod;
            if (revisionReason.length() > REVISION_REASON_MAX_LENGTH) {
                revisionReason = revisionReason.substring(0, REVISION_REASON_MAX_LENGTH);
            }
            return revisionReason;
        } else {
            return entity.getRevisionReasonThisChange();
        }
    }

    /**
     * Shipping Plan has difference and box data.
     * 
     * @param listBox box list
     * @param listDiff difference list
     * @return true:has difference or box data/false:has not difference and box data
     */
    private boolean hasDiffBox(List<Integer> listBox, List<Integer> listDiff) {
        if ((listBox == null || listBox.size() < 1) && (listDiff == null || listDiff.size() < 1)) {
            return false;
        }
        return true;
    }

    /**
     * Analytic Version
     * 
     * @param version file.version
     * @param languageIndex language index
     * @param tntKanbanInsert the TntKanban that is new
     * @return [0]:originalVersion, [1]:revisionVersion
     */
    private Integer[] analyticVersion(String version, int languageIndex, TntKanban tntKanbanInsert) {
        Integer[] ver = new Integer[NumberConst.IntDef.INT_TWO];

        Locale language = Language.ENGLISH.getLocale();
        if (languageIndex == CodeConst.CategoryLanguage.CHINESE) {
            language = Language.CHINESE.getLocale();
        }
        String labelPlan = MessageManager.getMessage("CPKKPF01_Grid_Plan", language).toUpperCase();
        String labelR = MessageManager.getMessage("CPKKPF01_Grid_PlanRevision", language).toUpperCase();
        String verTemp = version.toUpperCase().replaceAll(labelPlan, "").replaceAll(labelR, "");

        if (verTemp.contains(StringConst.UNDERLINE)) {
            String[] versions = verTemp.split(StringConst.UNDERLINE);
            if (NumberUtils.isNumber(versions[0])) {
                ver[0] = Integer.parseInt(versions[0]);
                if (NumberUtils.isNumber(versions[1])) {
                    ver[1] = Integer.parseInt(versions[1]);
                } else {
                    ver[1] = null;
                }
            } else {
                TntKanbanShipping con = new TntKanbanShipping();
                con.setKanbanId(tntKanbanInsert.getKanbanId());
                ver[0] = cpkkpf12Service.getMaxOriginalVersion(con);
                ver[1] = null;
            }
        } else {
            if (NumberUtils.isNumber(verTemp)) {
                ver[0] = Integer.parseInt(verTemp);
            } else {
                TntKanbanShipping con = new TntKanbanShipping();
                con.setKanbanId(tntKanbanInsert.getKanbanId());
                ver[0] = cpkkpf12Service.getMaxOriginalVersion(con);
            }
            ver[1] = null;
        }
        return ver;
    }

    /**
     * Is all row qty zero
     * 
     * @param rowDataList row data list
     * @return true: all row qty is zero /false: not all row qty is zero
     */
    private boolean isAllRowQtyZero(List<String[]> rowDataList) {
        if (rowDataList == null || rowDataList.size() < 1) {
            return false;
        }
        for (String[] rowData : rowDataList) {
            BigDecimal qty = DecimalUtil.getBigDecimalWithNUll(rowData[1]);
            if (qty == null || qty.compareTo(BigDecimal.ZERO) != 0) {
                return false;
            }
        }
        return true;
    }

    // /**
    // * Is all row qty empty
    // *
    // * @param rowDataList row data list
    // * @return true: all row qty is empty /false: not all row qty is empty
    // */
    // private boolean isAllRowQtyEmpty(List<String[]> rowDataList) {
    // for (String[] rowData : rowDataList) {
    // if (!StringUtils.isBlank(rowData[1])) {
    // return false;
    // }
    // }
    // return true;
    // }

    /**
     * Insert diff/box's data column for keep history.
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanbanInsert the TntKanban that is new
     * @param entityPlan current plan data
     * @param shippingUuid shippingUuid
     * @param rowInfo (rowPartsId key:rowNo., value:partsId) and (rowFcFlag key:rowNo., value:FORCE_COMPLETED)
     */
    @SuppressWarnings("unchecked")
    private void insertDiffBoxForKeepHistory(BaseParam param, SessionInfoManager context, TntKanban tntKanbanInsert,
        CPKKPF12ColEntity entityPlan, String shippingUuid, HashMap<String, HashMap<String, Integer>> rowInfo) {
        HashMap<Integer, CPKKPF12ColEntity> mapDiff = (HashMap<Integer, CPKKPF12ColEntity>) context
            .get(SESSION_COL_DIFF);
        HashMap<Integer, CPKKPF12ColEntity> mapBox = (HashMap<Integer, CPKKPF12ColEntity>) context.get(SESSION_COL_BOX);

        List<Integer> listDiffCurr = entityPlan.getListDiff();
        if (listDiffCurr != null && listDiffCurr.size() > 0) {
            for (int colNoDiff : listDiffCurr) {
                CPKKPF12ColEntity entityDiff = mapDiff.get(colNoDiff);
                int columnType = entityDiff.getColumnTypeAll();
                if (columnType == ColumnTypeAll.DIFFERENCE_DATA) {
                    insertDiffBoxData(param, new int[] { CodeConst.PlanType.DIFFERENCE, columnType }, shippingUuid,
                        entityDiff, tntKanbanInsert, rowInfo, entityDiff.getRowDataList(), null);
                }
            }
        }
        List<Integer> listBoxCurr = entityPlan.getListBox();
        if (listBoxCurr != null && listBoxCurr.size() > 0) {
            for (int colNoBox : listBoxCurr) {
                CPKKPF12ColEntity entityBox = mapBox.get(colNoBox);
                int columnType = entityBox.getColumnTypeAll();
                if (columnType == ColumnTypeAll.BOX_DATA) {
                    insertDiffBoxData(param, new int[] { CodeConst.PlanType.BOX, columnType }, shippingUuid, entityBox,
                        tntKanbanInsert, rowInfo, entityBox.getRowDataList(), null);
                }
            }
        }
    }

    /**
     * Merge same diff/box.
     * 
     * @param listCurr listCurr
     * @param listMerge listMerge
     * @param map mapDiff/mapBox
     * @param list merge result
     * @return merge reason result
     */
    private HashMap<Integer, String> mergeSameDiffBox(List<Integer> listCurr, List<Integer> listMerge,
        HashMap<Integer, CPKKPF12ColEntity> map, List<Integer> list) {
        HashMap<Integer, String> mapReason = new HashMap<Integer, String>();
        if (listMerge == null || listMerge.size() < 1) {
            list.addAll(listCurr);
        } else {
            for (int colNoMerge : listMerge) {
                CPKKPF12ColEntity entityMerge = map.get(colNoMerge);
                int columnTypeMerge = entityMerge.getColumnTypeAll();
                if (columnTypeMerge == ColumnTypeAll.BOX_MOD || columnTypeMerge == ColumnTypeAll.DIFFERENCE_MOD) {
                    continue;
                }
                Date vanningDateMerge = DateTimeUtil.parseDate(entityMerge.getVanningRemark());
                Date delivereDateMerge = DateTimeUtil.parseDate(entityMerge.getDelivereRemark());
                Date issueDateMerge = DateTimeUtil.parseDate(entityMerge.getIssueRemark());

                boolean existFlag = false;
                for (int colNoCurr : listCurr) {
                    CPKKPF12ColEntity entityCurr = map.get(colNoCurr);
                    int columnTypeCurr = entityCurr.getColumnTypeAll();
                    if (columnTypeCurr == ColumnTypeAll.BOX_MOD || columnTypeCurr == ColumnTypeAll.DIFFERENCE_MOD) {
                        continue;
                    }
                    Date vanningDateCurr = DateTimeUtil.parseDate(entityCurr.getVanningRemark());
                    Date delivereDateCurr = DateTimeUtil.parseDate(entityCurr.getDelivereRemark());
                    Date issueDateCurr = DateTimeUtil.parseDate(entityCurr.getIssueRemark());

                    String reasonMerge = "";
                    String reasonCurr = "";
                    if ((vanningDateCurr == null && vanningDateMerge == null && delivereDateCurr == null
                            && delivereDateMerge == null && issueDateCurr == null && issueDateMerge == null)
                            || (vanningDateCurr.equals(vanningDateMerge) && delivereDateCurr.equals(delivereDateMerge) && issueDateCurr
                                .equals(issueDateMerge))) {
                        List<String[]> rowDataMerge = entityMerge.getRowDataList();
                        if (columnTypeMerge == ColumnTypeAll.BOX_DATA
                                || columnTypeMerge == ColumnTypeAll.DIFFERENCE_DATA) {
                            CPKKPF12ColEntity entityMergeNext = map.get(colNoMerge + 1);
                            if (entityMergeNext != null) {
                                rowDataMerge = entityMergeNext.getRowDataForUpdateList();
                                reasonMerge = entityMergeNext.getRevisionReasonThisChange();
                            }
                        }

                        List<String[]> rowDataCurr = entityCurr.getRowDataList();
                        if (columnTypeCurr == ColumnTypeAll.BOX_DATA || columnTypeCurr == ColumnTypeAll.DIFFERENCE_DATA) {
                            CPKKPF12ColEntity entityCurrNext = map.get(colNoCurr + 1);
                            if (entityCurrNext != null) {
                                rowDataCurr = entityCurrNext.getRowDataForUpdateList();
                                reasonCurr = entityCurrNext.getRevisionReasonThisChange();
                            }
                        }

                        totalRowDataCommon(rowDataMerge, rowDataCurr, null);

                        String revisionReason = reasonCurr + reasonMerge;
                        if (mapReason.containsKey(colNoCurr)) {
                            revisionReason = mapReason.get(colNoCurr) + revisionReason;
                        }
                        if (revisionReason.length() > REVISION_REASON_MAX_LENGTH) {
                            revisionReason = revisionReason.substring(0, REVISION_REASON_MAX_LENGTH);
                        }
                        mapReason.put(colNoCurr, revisionReason);
                        existFlag = true;
                    }
                }
                if (!existFlag) {
                    listCurr.add(colNoMerge);
                    if (listMerge.contains(colNoMerge + 1)) {
                        listCurr.add(colNoMerge + 1);
                    }
                }
            }
            list.addAll(listCurr);
        }
        return mapReason;
    }

    /**
     * Insert new diff/box for new version plan.<br>
     * include mod column(if mod cell is blank, use data cell),<br>
     * and merge-from plan's mod column(if mod cell is blank, use data cell).<br>
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanbanInsert the TntKanban that is new
     * @param entityPlan current plan data
     * @param shippingUuid shippingUuid
     * @param rowInfo (rowPartsId key:rowNo., value:partsId) and (rowFcFlag key:rowNo., value:FORCE_COMPLETED)
     */
    @SuppressWarnings("unchecked")
    private void insertNewDiffBox(BaseParam param, SessionInfoManager context, TntKanban tntKanbanInsert,
        CPKKPF12ColEntity entityPlan, String shippingUuid, HashMap<String, HashMap<String, Integer>> rowInfo) {
        HashMap<Integer, CPKKPF12ColEntity> mapDiff = (HashMap<Integer, CPKKPF12ColEntity>) context
            .get(SESSION_COL_DIFF);
        HashMap<Integer, CPKKPF12ColEntity> mapBox = (HashMap<Integer, CPKKPF12ColEntity>) context.get(SESSION_COL_BOX);
        // Insert difference data
        List<Integer> listDiffCurr = entityPlan.getListDiff();
        if (listDiffCurr != null && listDiffCurr.size() > 0) {
            List<Integer> listDiff = new ArrayList<Integer>();
            HashMap<Integer, String> mapReason = mergeSameDiffBox(listDiffCurr, entityPlan.getListDiffMerge(), mapDiff,
                listDiff);
            for (int colNoDiff : listDiff) {
                CPKKPF12ColEntity entityDiff = mapDiff.get(colNoDiff);
                int columnType = entityDiff.getColumnTypeAll();
                if (columnType == ColumnTypeAll.DIFFERENCE_DATA) {
                    CPKKPF12ColEntity entityDiffNext = mapDiff.get(colNoDiff + 1);
                    if (entityDiffNext != null) {
                        // If current is data column but next column is current column's mod column, do
                        // next.
                        continue;
                    } else {
                        // If current is data column and next column is not current column's mod column, do
                        // insert.
                        insertDiffBoxData(param, new int[] { CodeConst.PlanType.DIFFERENCE, columnType }, shippingUuid,
                            entityDiff, tntKanbanInsert, rowInfo, entityDiff.getRowDataList(), mapReason);
                    }
                } else {
                    CPKKPF12ColEntity entityDiffData = mapDiff.get(colNoDiff - 1);
                    entityDiffData.setRevisionReasonThisChange(entityDiff.getRevisionReasonThisChange());
                    // If current is mod column, do insert because of current column's data column was not
                    // insert.
                    insertDiffBoxData(param, new int[] { CodeConst.PlanType.DIFFERENCE, columnType }, shippingUuid,
                        entityDiffData, tntKanbanInsert, rowInfo, entityDiff.getRowDataForUpdateList(), mapReason);
                }
            }
        }
        // Insert box data
        List<Integer> listBoxCurr = entityPlan.getListBox();
        if (listBoxCurr != null && listBoxCurr.size() > 0) {
            List<Integer> listBox = new ArrayList<Integer>();
            HashMap<Integer, String> mapReason = mergeSameDiffBox(listBoxCurr, entityPlan.getListBoxMerge(), mapBox,
                listBox);
            for (int colNoBox : listBox) {
                CPKKPF12ColEntity entityBox = mapBox.get(colNoBox);
                int columnType = entityBox.getColumnTypeAll();
                if (columnType == ColumnTypeAll.BOX_DATA) {
                    CPKKPF12ColEntity entityBoxNext = mapBox.get(colNoBox + 1);
                    if (entityBoxNext != null) {
                        // If current is data column but next column is current column's mod column, do
                        // next.
                        continue;
                    } else {
                        // If current is data column and next column is not current column's mod column, do
                        // insert.
                        insertDiffBoxData(param, new int[] { CodeConst.PlanType.BOX, columnType }, shippingUuid,
                            entityBox, tntKanbanInsert, rowInfo, entityBox.getRowDataList(), mapReason);
                    }
                } else {
                    CPKKPF12ColEntity entityBoxData = mapBox.get(colNoBox - 1);
                    entityBoxData.setRevisionReasonThisChange(entityBox.getRevisionReasonThisChange());
                    // If current is mod column, do insert because of current column's data column was not
                    // insert.
                    insertDiffBoxData(param, new int[] { CodeConst.PlanType.BOX, columnType }, shippingUuid,
                        entityBoxData, tntKanbanInsert, rowInfo, entityBox.getRowDataForUpdateList(), mapReason);
                }
            }
        }
    }

    /**
     * Insert data column plan for keep history.
     * 
     * @param param base param
     * @param tntKanbanInsert the TntKanban that is new
     * @param entityPlan current plan data
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param entity upload file base information
     * @param originalVersion originalVersion
     * @param revisionVersion revisionVersion
     * @return shippingUuid
     */
    private String insertPlanForKeepHistory(BaseParam param, TntKanban tntKanbanInsert, CPKKPF12ColEntity entityPlan,
        HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag, CPKKPF12Entity entity,
        Integer originalVersion, Integer revisionVersion) {
        int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
        int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
            CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode());
        String shippingUuid = UUID.randomUUID().toString();
        TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
        tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
        tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanShipping.setShippingUuid(shippingUuid);
        tntKanbanShipping.setTransportMode(transportMode);
        tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityPlan.getEtd()));
        tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityPlan.getEta()));
        tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityPlan.getImpInbPlanDate()));
        tntKanbanShipping.setOriginalVersion(originalVersion);
        tntKanbanShipping.setRevisionVersion(revisionVersion);
        tntKanbanShipping.setRevisionReason(entityPlan.getRevisionReasonLastChange());
        tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.COMPLETED);
        tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
        cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

        int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
        tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
        tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanPlan.setShippingUuid(shippingUuid);
        tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
        tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
        tntKanbanPlan.setIssuedDate(null);
        tntKanbanPlan.setIssueRemark(entityPlan.getIssueRemark());
        tntKanbanPlan.setDeliveredDate(null);
        tntKanbanPlan.setDelivereRemark(entityPlan.getDelivereRemark());
        tntKanbanPlan.setVanningDate(null);
        tntKanbanPlan.setVanningRemark(entityPlan.getVanningRemark());
        tntKanbanPlan.setRevisionReason(null);
        cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

        List<String[]> rowDataList = entityPlan.getRowDataList();
        for (int j = 0; j < rowDataList.size(); j++) {
            String[] rowData = rowDataList.get(j);
            String rowNo = rowData[0];
            if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                continue;
            }

            BigDecimal bdQty = DecimalUtil.getBigDecimal(rowData[1]);
            if (bdQty.compareTo(BigDecimal.ZERO) != 0) {
                // Insert into TNT_KANBAN_SHIPPING_PARTS
                int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                tntKanbanShippingPart.setKspId(kspId);
                tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanShippingPart.setQty(bdQty);
                tntKanbanShippingPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                // Insert into TNT_KANBAN_PARTS
                int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                tntKanbanPlanPart.setKbPlanPartsId(kppId);
                tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanPlanPart.setQty(bdQty);
                tntKanbanPlanPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
            }
        }
        return shippingUuid;
    }

    /**
     * Insert new plan for merge.
     * 
     * @param param base param
     * @param tntKanbanInsert the TntKanban that is new
     * @param entityPlan current plan data
     * @param rowInfo (rowPartsId key:rowNo., value:partsId) and (rowFcFlag key:rowNo., value:FORCE_COMPLETED)
     * @param entity upload file base information
     * @param version [0]:originalVersion, [1]:revisionVersion
     * @param rowDataListData rowDataListData(current plan + merge-from plan)
     * @param mapReason mapReason
     * @return shippingUuid
     */
    private String insertNewPlanForMerge(BaseParam param, TntKanban tntKanbanInsert, CPKKPF12ColEntity entityPlan,
        HashMap<String, HashMap<String, Integer>> rowInfo, CPKKPF12Entity entity, Integer[] version,
        List<String[]> rowDataListData, HashMap<String, String> mapReason) {
        HashMap<String, Integer> rowFcFlag = rowInfo.get(MAP_KEY_ROW_FC_FLAG);
        HashMap<String, Integer> rowPartsId = rowInfo.get(MAP_KEY_ROW_PARTS_ID);

        String revisionReason = "";
        String key = String.valueOf(entityPlan.getColNo());
        if (!mapReason.containsKey(key)) {
            revisionReason = entityPlan.getRevisionReasonLastChange();
        } else {
            String reason = mapReason.get(key);
            String reasonOwn = mapReason.get(key + StringConst.ALPHABET_R);
            revisionReason = reason + reasonOwn;
        }
        if (revisionReason.length() > REVISION_REASON_MAX_LENGTH) {
            revisionReason = revisionReason.substring(0, REVISION_REASON_MAX_LENGTH);
        }

        int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
        int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
            CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode());
        String shippingUuid = UUID.randomUUID().toString();
        TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
        tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
        tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanShipping.setShippingUuid(shippingUuid);
        tntKanbanShipping.setTransportMode(transportMode);
        tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityPlan.getEtd()));
        tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityPlan.getEta()));
        tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityPlan.getImpInbPlanDate()));
        tntKanbanShipping.setOriginalVersion(version[0]);
        tntKanbanShipping.setRevisionVersion(version[1]);
        // tntKanbanShipping.setRevisionReason(mapReason.get(entityPlan.getColNo()) == null ? entityPlan
        // .getRevisionReasonLastChange() : mapReason.get(entityPlan.getColNo()));
        tntKanbanShipping.setRevisionReason(revisionReason);
        tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
        tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
        cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

        // 1.2 Insert into TNT_KANBAN_PLAN
        int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
        tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
        tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanPlan.setShippingUuid(shippingUuid);
        tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
        tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
        tntKanbanPlan.setIssuedDate(null);
        tntKanbanPlan.setIssueRemark(entityPlan.getIssueRemark());
        tntKanbanPlan.setDeliveredDate(null);
        tntKanbanPlan.setDelivereRemark(entityPlan.getDelivereRemark());
        tntKanbanPlan.setVanningDate(null);
        tntKanbanPlan.setVanningRemark(entityPlan.getVanningRemark());
        tntKanbanPlan.setRevisionReason(null);
        cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

        // 1.3 Insert into TNT_KANBAN_SHIPPING_PARTS
        // 1.4 Insert into TNT_KANBAN_PARTS
        for (int j = 0; j < rowDataListData.size(); j++) {
            String[] rowData = rowDataListData.get(j);
            String rowNo = rowData[0];
            if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                continue;
            }

            BigDecimal bdQty = DecimalUtil.getBigDecimal(rowData[1]);
            if (bdQty.compareTo(BigDecimal.ZERO) != 0) {
                // Insert into TNT_KANBAN_SHIPPING_PARTS
                int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                tntKanbanShippingPart.setKspId(kspId);
                tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanShippingPart.setQty(bdQty);
                tntKanbanShippingPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                // Insert into TNT_KANBAN_PARTS
                int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                tntKanbanPlanPart.setKbPlanPartsId(kppId);
                tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanPlanPart.setQty(bdQty);
                tntKanbanPlanPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
            }
        }
        return shippingUuid;
    }

    /**
     * Insert new plan for MOD.
     * 
     * @param param base param
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowInfo (rowPartsId key:rowNo., value:partsId) and (rowFcFlag key:rowNo., value:FORCE_COMPLETED)
     * @param entity upload file base information
     * @param version [0]:originalVersion, [1]:revisionVersion
     * @param rowDataList rowDataListData, rowDataListMod
     * @param listPlan listPlan
     * @param currentIndex current index
     * @return shippingUuid
     */
    private String insertNewPlanForMod(BaseParam param, TntKanban tntKanbanInsert,
        HashMap<String, HashMap<String, Integer>> rowInfo, CPKKPF12Entity entity, Integer[] version,
        HashMap<String, List<String[]>> rowDataList, List<CPKKPF12ColEntity> listPlan, int currentIndex) {
        List<String[]> rowDataListData = rowDataList.get(MAP_KEY_ROW_LIST_DATA);
        List<String[]> rowDataListMod = rowDataList.get(MAP_KEY_ROW_LIST_MOD);
        boolean allRowQtyModZero = isAllRowQtyZero(rowDataListMod);
        // 2016/06/28 shiyang del start (if input all 0, keep the history plan which qty is all 0)
        // if (allRowQtyModZero) {
        // return null;
        // }
        // 2016/06/28 shiyang del end
        CPKKPF12ColEntity entityPlan = listPlan.get(currentIndex);
        HashMap<String, Integer> rowFcFlag = rowInfo.get(MAP_KEY_ROW_FC_FLAG);
        HashMap<String, Integer> rowPartsId = rowInfo.get(MAP_KEY_ROW_PARTS_ID);
        boolean hasModData = hasModData(listPlan, currentIndex);
        boolean hasRevisionData = hasRevisionData(entityPlan);

        // 2.1 Insert into TNT_KANBAN_SHIPPING
        int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
        String shippingUuid = UUID.randomUUID().toString();
        TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
        tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
        tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanShipping.setShippingUuid(shippingUuid);
        if (hasRevisionData) {
            int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportModeRevision());
            tntKanbanShipping.setTransportMode(transportMode);
            tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityPlan.getEtdRevision()));
            tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityPlan.getEtaRevision()));
            tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityPlan.getImpInbPlanDateRevision()));
        } else {
            int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode());
            tntKanbanShipping.setTransportMode(transportMode);
            tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityPlan.getEtd()));
            tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityPlan.getEta()));
            tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityPlan.getImpInbPlanDate()));
        }
        tntKanbanShipping.setOriginalVersion(version[0]);
        tntKanbanShipping.setRevisionVersion(version[1] == null ? 1 : version[1] + 1);
        tntKanbanShipping.setRevisionReason(getModRevisionReason(listPlan, currentIndex, hasModData));
        tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
        if (allRowQtyModZero) {
            tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.COMPLETED);
        }
        tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
        cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

        // 2.2 Insert into TNT_KANBAN_PLAN
        int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
        tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
        tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanPlan.setShippingUuid(shippingUuid);
        tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
        tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
        tntKanbanPlan.setIssuedDate(null);
        tntKanbanPlan.setIssueRemark(entityPlan.getIssueRemark());
        tntKanbanPlan.setDeliveredDate(null);
        tntKanbanPlan.setDelivereRemark(entityPlan.getDelivereRemark());
        tntKanbanPlan.setVanningDate(null);
        tntKanbanPlan.setVanningRemark(entityPlan.getVanningRemark());
        tntKanbanPlan.setRevisionReason(null);
        cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

        // 2.3 Insert into TNT_KANBAN_SHIPPING_PARTS
        // 2.4 Insert into TNT_KANBAN_PARTS
        if (!hasModData && (entityPlan.getListBoxMod() == null || entityPlan.getListBoxMod().size() < 1)
                && (entityPlan.getListDiffMod() == null || entityPlan.getListDiffMod().size() < 1)) {
            // rowDataListMod = new ArrayList<String[]>(rowDataListData);
            rowDataListMod.addAll(rowDataListData);
        }
        for (int j = 0; j < rowDataListMod.size(); j++) {
            String[] rowData = rowDataListMod.get(j);
            String rowNo = rowData[0];
            if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                continue;
            }

            BigDecimal bdQty = DecimalUtil.getBigDecimal(rowData[1]);
            if (bdQty.compareTo(BigDecimal.ZERO) != 0) {
                // Insert into TNT_KANBAN_SHIPPING_PARTS
                int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                tntKanbanShippingPart.setKspId(kspId);
                tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanShippingPart.setQty(bdQty);
                tntKanbanShippingPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                // Insert into TNT_KANBAN_PARTS
                int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                tntKanbanPlanPart.setKbPlanPartsId(kppId);
                tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanPlanPart.setQty(bdQty);
                tntKanbanPlanPart.setKanbanQty(null);
                cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
            }
        }
        return shippingUuid;
    }

    /**
     * Save latest shipping plan.
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowQtyBox key:rowNo., value:input qty/box
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param entity upload file base information
     */
    @SuppressWarnings("unchecked")
    private void saveLatestShippingPlan(BaseParam param, SessionInfoManager context, TntKanban tntKanbanInsert,
        HashMap<String, Integer> rowPartsId, HashMap<String, BigDecimal> rowQtyBox, HashMap<String, Integer> rowFcFlag,
        CPKKPF12Entity entity) {
        List<CPKKPF12ColEntity> listPlan = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_PLAN);
        HashMap<Integer, CPKKPF12ColEntity> mapDiff = (HashMap<Integer, CPKKPF12ColEntity>) context
            .get(SESSION_COL_DIFF);
        HashMap<Integer, CPKKPF12ColEntity> mapBox = (HashMap<Integer, CPKKPF12ColEntity>) context.get(SESSION_COL_BOX);

        HashMap<String, HashMap<String, Integer>> rowInfo = new HashMap<String, HashMap<String, Integer>>();
        rowInfo.put(MAP_KEY_ROW_FC_FLAG, rowFcFlag);
        rowInfo.put(MAP_KEY_ROW_PARTS_ID, rowPartsId);

        // Merge the same plan(MOD column and one DATA column that has no modified).
        HashMap<String, String> mapReason = mergeSamePlan(listPlan);

        for (int i = 0; i < listPlan.size(); i++) {
            CPKKPF12ColEntity entityPlan = listPlan.get(i);
            int columnType = entityPlan.getColumnTypeAll();
            int mergeFlag = entityPlan.getMergeFlag();

            if (columnType == ColumnTypeAll.PLAN) {
                // =================================Get originalVersion and revisionVersion.
                Integer[] ver = analyticVersion(entityPlan.getRevisionVersionFile(), entity.getLanguageIndex(),
                    tntKanbanInsert);
                int originalVersion = ver[0];
                Integer revisionVersion = ver[1];

                // =================================Total qty.
                boolean hasRevisionData = hasRevisionData(entityPlan);
                boolean hasModData = hasModData(listPlan, i);
                // Total shipping plan's row data from difference and box for insert tlb[TNT_KANBAN_SHIPPING_PARTS].
                List<String[]> rowDataListData = new ArrayList<String[]>();
                List<String[]> rowDataListMod = new ArrayList<String[]>();
                totalPlanRowDataFromDifBox(listPlan, i, mapDiff, mapBox, rowDataListData, rowDataListMod, hasModData,
                    rowQtyBox);
                boolean allRowQtyModZero = isAllRowQtyZero(rowDataListMod);
                // boolean allRowQtyModEmpty = isAllRowQtyEmpty(rowDataListMod);
                boolean hasDiffBoxMod = hasDiffBox(entityPlan.getListBoxMod(), entityPlan.getListDiffMod());
                // boolean hasDiffBoxMerge = hasDiffBox(entityPlan.getListBoxMerge(), entityPlan.getListDiffMerge());

                // =================================Completed current plan if current plan has merge src.
                if (mergeFlag == MergeFlag.MERGED_SRC) {
                    // Case: Merge to other plan
                    // Do: Close itself and insert original diff/box if original diff/box is exist.
                    String shippingUuid = insertPlanForKeepHistory(param, tntKanbanInsert, entityPlan, rowPartsId,
                        rowFcFlag, entity, originalVersion, revisionVersion);
                    insertDiffBoxForKeepHistory(param, context, tntKanbanInsert, entityPlan, shippingUuid, rowInfo);
                } else if (mergeFlag == MergeFlag.MERGED_DIS) {
                    // Case: Revice other plan's merge (has no mod info)
                    // Do: Close itself and do not insert original diff/box, then insert new plan+diff+box.
                    insertPlanForKeepHistory(param, tntKanbanInsert, entityPlan, rowPartsId, rowFcFlag, entity,
                        originalVersion, revisionVersion);
                    String shippingUuid = "";
                    if (hasModData) {
                        shippingUuid = insertNewPlanForMerge(param, tntKanbanInsert, entityPlan, rowInfo, entity,
                            new Integer[] { originalVersion, revisionVersion == null ? 1 : revisionVersion + 1 },
                            rowDataListMod, mapReason);
                    } else {
                        shippingUuid = insertNewPlanForMerge(param, tntKanbanInsert, entityPlan, rowInfo, entity,
                            new Integer[] { originalVersion, revisionVersion == null ? 1 : revisionVersion + 1 },
                            rowDataListData, mapReason);
                    }
                    int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                        CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode());
                    if (transportMode == CodeConst.TransportMode.SEA) {
                        insertNewDiffBox(param, context, tntKanbanInsert, entityPlan, shippingUuid, rowInfo);
                    }
                } else {
                    if (hasRevisionData || hasModData || (!allRowQtyModZero && hasDiffBoxMod)) {
                        // Case: Has Revision Data || Has Mod Data || Diff/Box has mod
                        Integer transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                            CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode());
                        Integer transportModeRevision = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                            CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportModeRevision());
                        if (transportModeRevision == null) {
                            transportModeRevision = transportMode.intValue();
                        }

                        HashMap<String, List<String[]>> rowDataList = new HashMap<String, List<String[]>>();
                        rowDataList.put(MAP_KEY_ROW_LIST_DATA, rowDataListData);
                        rowDataList.put(MAP_KEY_ROW_LIST_MOD, rowDataListMod);

                        String shippingUuid = insertPlanForKeepHistory(param, tntKanbanInsert, entityPlan, rowPartsId,
                            rowFcFlag, entity, originalVersion, revisionVersion);
                        if (transportMode.equals(CodeConst.TransportMode.SEA)
                                && transportModeRevision.equals(CodeConst.TransportMode.AIR)) {
                            // case: MOD(SEA -> AIR)
                            // do: Close itself and insert original diff/box if original diff/box is exist,
                            // then insert new plan(no diff+box).
                            insertDiffBoxForKeepHistory(param, context, tntKanbanInsert, entityPlan, shippingUuid,
                                rowInfo);
                            insertNewPlanForMod(param, tntKanbanInsert, rowInfo, entity, new Integer[] {
                                originalVersion, revisionVersion }, rowDataList, listPlan, i);
                        } else if (transportMode.equals(CodeConst.TransportMode.SEA)
                                && transportModeRevision.equals(CodeConst.TransportMode.SEA)) {
                            // case: MOD(SEA -> SEA)
                            // do: Close itself, then insert new plan+diff+box.
                            shippingUuid = insertNewPlanForMod(param, tntKanbanInsert, rowInfo, entity, new Integer[] {
                                originalVersion, revisionVersion }, rowDataList, listPlan, i);
                            if (shippingUuid != null) {
                                insertNewDiffBox(param, context, tntKanbanInsert, entityPlan, shippingUuid, rowInfo);
                            }
                        } else {
                            // case: MOD(AIR -> AIR or AIR -> SEA)
                            // do: Close itself, then insert new plan(no diff+box).
                            insertNewPlanForMod(param, tntKanbanInsert, rowInfo, entity, new Integer[] {
                                originalVersion, revisionVersion }, rowDataList, listPlan, i);
                        }
                    } else {
                        if (allRowQtyModZero) {
                            String shippingUuid = insertPlanForKeepHistory(param, tntKanbanInsert, entityPlan,
                                rowPartsId, rowFcFlag, entity, originalVersion, revisionVersion);
                            // 2016/06/28 shiyang mod start (if input all 0, keep the history plan which qty is all 0)
                            // insertDiffBoxForKeepHistory(param, context, tntKanbanInsert, entityPlan, shippingUuid,
                            // rowInfo);
                            HashMap<String, List<String[]>> rowDataList = new HashMap<String, List<String[]>>();
                            rowDataList.put(MAP_KEY_ROW_LIST_DATA, rowDataListData);
                            rowDataList.put(MAP_KEY_ROW_LIST_MOD, rowDataListMod);
                            shippingUuid = insertNewPlanForMod(param, tntKanbanInsert, rowInfo, entity, new Integer[] {
                                originalVersion, revisionVersion }, rowDataList, listPlan, i);
                            insertNewDiffBox(param, context, tntKanbanInsert, entityPlan, shippingUuid, rowInfo);
                            // 2016/06/28 shiyang mod end
                        } else {
                            String shippingUuid = insertNewPlanForMerge(param, tntKanbanInsert, entityPlan, rowInfo,
                                entity, new Integer[] { originalVersion, revisionVersion }, rowDataListData, mapReason);
                            insertNewDiffBox(param, context, tntKanbanInsert, entityPlan, shippingUuid, rowInfo);
                        }
                    }
                }
            }
        }
    }

    /**
     * Insert difference or box data.
     * 
     * @param param base param
     * @param type [0]: differenct/box,[1]:current columnType
     * @param shippingUuid shippingUuid
     * @param entity difference/box's entity
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowInfo (rowPartsId key:rowNo., value:partsId) and (rowFcFlag key:rowNo., value:FORCE_COMPLETED)
     * @param rowData row data
     * @param mapReason merge same box result
     */
    private void insertDiffBoxData(BaseParam param, int[] type, String shippingUuid, CPKKPF12ColEntity entity,
        TntKanban tntKanbanInsert, HashMap<String, HashMap<String, Integer>> rowInfo, List<String[]> rowData,
        HashMap<Integer, String> mapReason) {
        // 2016/06/28 shiyang del start (if input all 0, keep the history plan which qty is all 0)
        // if (isAllRowQtyZero(rowData)) {
        // return;
        // }
        // 2016/06/28 shiyang del end
        HashMap<String, Integer> rowFcFlag = rowInfo.get(MAP_KEY_ROW_FC_FLAG);
        HashMap<String, Integer> rowPartsId = rowInfo.get(MAP_KEY_ROW_PARTS_ID);
        // 4.1 Insert into TNT_KANBAN_PLAN
        int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
        TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
        tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
        tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
        tntKanbanPlan.setShippingUuid(shippingUuid);
        tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
        tntKanbanPlan.setPlanType(type[0]);
        tntKanbanPlan.setIssuedDate(DateTimeUtil.parseDate(entity.getIssueRemark()));
        tntKanbanPlan.setIssueRemark(null);
        tntKanbanPlan.setDeliveredDate(DateTimeUtil.parseDate(entity.getDelivereRemark()));
        tntKanbanPlan.setDelivereRemark(null);
        tntKanbanPlan.setVanningDate(DateTimeUtil.parseDate(entity.getVanningRemark()));
        tntKanbanPlan.setVanningRemark(null);
        if (type[1] == ColumnTypeAll.DIFFERENCE_DATA || type[1] == ColumnTypeAll.BOX_DATA) {
            String reason = "";
            if (mapReason != null) {
                reason = mapReason.get(entity.getColNo());
            }
            if (StringUtils.isBlank(reason)) {
                tntKanbanPlan.setRevisionReason(StringUtil.emptyToNull(entity.getRevisionReasonLastChange()));
            } else {
                tntKanbanPlan.setRevisionReason(reason);
            }
        } else if (type[1] == ColumnTypeAll.DIFFERENCE_MOD || type[1] == ColumnTypeAll.BOX_MOD) {
            String reason = "";
            if (mapReason != null) {
                reason = mapReason.get(entity.getColNo());
            }
            if (StringUtils.isBlank(reason)) {
                tntKanbanPlan.setRevisionReason(StringUtil.emptyToNull(entity.getRevisionReasonThisChange()));
            } else {
                tntKanbanPlan.setRevisionReason(reason);
            }
        }
        cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

        // 4.2 Insert into TNT_KANBAN_PARTS
        insertKanbanPlanParts(param, rowData, kanbanPlanId, rowPartsId, rowFcFlag);
    }

    /**
     * Insert KanbanPlanParts common for difference and box.
     * 
     * @param param base param
     * @param rowDataList row data list
     * @param kanbanPlanId kanbanPlanId
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     */
    private void insertKanbanPlanParts(BaseParam param, List<String[]> rowDataList, int kanbanPlanId,
        HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag) {
        for (String[] rowData : rowDataList) {
            String rowNo = rowData[0];
            if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                continue;
            }

            String qty = rowData[1];
            BigDecimal bdQty = DecimalUtil.getBigDecimal(qty);
            if (!StringUtils.isBlank(qty) && bdQty.compareTo(BigDecimal.ZERO) != 0) {
                int kpPartsId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                tntKanbanPlanPart.setKbPlanPartsId(kpPartsId);
                tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                tntKanbanPlanPart.setQty(null);
                tntKanbanPlanPart.setKanbanQty(bdQty);
                cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
            }
        }
    }

    /**
     * Save additional shipping plan.
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param entity upload file base information
     */
    @SuppressWarnings("unchecked")
    private void saveAdditionalShippingPlan(BaseParam param, SessionInfoManager context, TntKanban tntKanbanInsert,
        HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag, CPKKPF12Entity entity) {
        List<CPKKPF12ColEntity> listPlanNew = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_PLAN_NEW);
        for (CPKKPF12ColEntity entityPlan : listPlanNew) {
            // 1.1 Insert into TNT_KANBAN_SHIPPING
            int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
            String shippingUuid = UUID.randomUUID().toString();
            TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
            tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
            tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanShipping.setShippingUuid(shippingUuid);
            tntKanbanShipping.setTransportMode(CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                CodeMasterCategory.TRANSPORT_MODE, entityPlan.getTransportMode()));
            tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityPlan.getEtd()));
            tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityPlan.getEta()));
            tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityPlan.getImpInbPlanDate()));
            tntKanbanShipping.setOriginalVersion(cpkkpf12Service.getMaxOriginalVersion(tntKanbanShipping));
            tntKanbanShipping.setRevisionVersion(cpkkpf12Service.getMaxRevisionVersion(tntKanbanShipping));
            tntKanbanShipping.setRevisionReason(entityPlan.getRevisionReasonThisChange());
            tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
            tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
            cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

            // 1.2 Insert into TNT_KANBAN_PLAN
            int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
            TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
            tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
            tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanPlan.setShippingUuid(shippingUuid);
            tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
            tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
            tntKanbanPlan.setIssuedDate(null);
            tntKanbanPlan.setIssueRemark(entityPlan.getIssueRemark());
            tntKanbanPlan.setDeliveredDate(null);
            tntKanbanPlan.setDelivereRemark(entityPlan.getDelivereRemark());
            tntKanbanPlan.setVanningDate(null);
            tntKanbanPlan.setVanningRemark(entityPlan.getVanningRemark());
            tntKanbanPlan.setRevisionReason(null);
            cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

            // 1.3 Insert into TNT_KANBAN_SHIPPING_PARTS
            // 1.4 Insert into TNT_KANBAN_PARTS
            List<String[]> rowDataList = entityPlan.getRowDataList();
            for (String[] rowData : rowDataList) {
                String rowNo = rowData[0];
                if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                    continue;
                }

                String strQty = rowData[1];
                BigDecimal bdQty = DecimalUtil.getBigDecimal(strQty);
                if (!StringUtils.isBlank(strQty) && bdQty.compareTo(BigDecimal.ZERO) != 0) {
                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                    int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                    TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                    tntKanbanShippingPart.setKspId(kspId);
                    tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                    tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanShippingPart.setQty(bdQty);
                    tntKanbanShippingPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                    // Insert into TNT_KANBAN_PARTS
                    int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                    TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                    tntKanbanPlanPart.setKbPlanPartsId(kppId);
                    tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                    tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanPlanPart.setQty(bdQty);
                    tntKanbanPlanPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
                }
            }
        }
    }

    /**
     * Save not in rundown.
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param entity upload file base information
     */
    @SuppressWarnings("unchecked")
    private void saveNird(BaseParam param, SessionInfoManager context, TntKanban tntKanban, TntKanban tntKanbanInsert,
        HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag, CPKKPF12Entity entity) {
        List<CPKKPF12ColEntity> listColNird = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_NIRD);

        // 1. Insert nird
        for (int i = listColNird.size() - 1; i > -1; i--) {
            CPKKPF12ColEntity entityNird = listColNird.get(i);
            int transportMode = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                CodeMasterCategory.TRANSPORT_MODE, entityNird.getTransportMode());
            Date etd = DateTimeUtil.parseDate(entityNird.getEtd());
            Date eta = DateTimeUtil.parseDate(entityNird.getEta());
            Date impInbPlanDate = DateTimeUtil.parseDate(entityNird.getImpInbPlanDate());

            TntKanbanShipping conVersion = new TntKanbanShipping();
            conVersion.setKanbanId(tntKanban.getKanbanId());
            conVersion.setTransportMode(transportMode);
            conVersion.setEtd(etd);
            conVersion.setEta(eta);
            conVersion.setImpInbPlanDate(impInbPlanDate);
            TntKanbanShipping verNird = cpkkpf12Service.getVersionForNird(conVersion);
            // 1.1 Insert into TNT_KANBAN_SHIPPING
            int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
            String shippingUuid = UUID.randomUUID().toString();
            TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
            tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
            tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanShipping.setShippingUuid(shippingUuid);
            tntKanbanShipping.setTransportMode(transportMode);
            tntKanbanShipping.setEtd(etd);
            tntKanbanShipping.setEta(eta);
            tntKanbanShipping.setImpInbPlanDate(impInbPlanDate);
            int originalVersion = verNird.getOriginalVersion();
            tntKanbanShipping.setOriginalVersion(originalVersion);
            Integer revisionVersion = verNird.getRevisionVersion();
            tntKanbanShipping.setRevisionVersion(revisionVersion);
            tntKanbanShipping.setRevisionReason(entityNird.getRevisionReason());
            tntKanbanShipping.setCompletedFlag(entityNird.getCompletedFlag());
            tntKanbanShipping.setNirdFlag(entityNird.getNirdFlag());
            cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

            // 1.2 Insert into TNT_KANBAN_PLAN
            int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
            TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
            tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
            tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanPlan.setShippingUuid(shippingUuid);
            tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
            tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
            tntKanbanPlan.setIssuedDate(null);
            tntKanbanPlan.setIssueRemark(entityNird.getIssueRemark());
            tntKanbanPlan.setDeliveredDate(null);
            tntKanbanPlan.setDelivereRemark(entityNird.getDelivereRemark());
            tntKanbanPlan.setVanningDate(null);
            tntKanbanPlan.setVanningRemark(entityNird.getVanningRemark());
            tntKanbanPlan.setRevisionReason(null);
            cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

            // 1.3 Insert into TNT_KANBAN_SHIPPING_PARTS
            // 1.4 Insert into TNT_KANBAN_PARTS
            List<String[]> rowDataList = entityNird.getRowDataList();
            for (String[] rowData : rowDataList) {
                String rowNo = rowData[0];
                if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                    continue;
                }

                String strQty = rowData[1];
                BigDecimal bdQty = DecimalUtil.getBigDecimal(strQty);
                if (!StringUtils.isBlank(strQty) && bdQty.compareTo(BigDecimal.ZERO) != 0) {
                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                    int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                    TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                    tntKanbanShippingPart.setKspId(kspId);
                    tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                    tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanShippingPart.setQty(bdQty);
                    tntKanbanShippingPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                    // Insert into TNT_KANBAN_PARTS
                    int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                    TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                    tntKanbanPlanPart.setKbPlanPartsId(kppId);
                    tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                    tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanPlanPart.setQty(bdQty);
                    tntKanbanPlanPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
                }
            }

            List<String[]> rowDataForUpdateList = entityNird.getRowDataForUpdateList();
            if (rowDataForUpdateList != null && rowDataForUpdateList.size() > 0) {
                // 1.1 Insert into TNT_KANBAN_SHIPPING
                kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
                shippingUuid = UUID.randomUUID().toString();
                tntKanbanShipping = new TntKanbanShipping();
                tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
                tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
                tntKanbanShipping.setShippingUuid(shippingUuid);
                tntKanbanShipping.setTransportMode(transportMode);
                tntKanbanShipping.setEtd(etd);
                tntKanbanShipping.setEta(eta);
                tntKanbanShipping.setImpInbPlanDate(impInbPlanDate);
                tntKanbanShipping.setOriginalVersion(originalVersion);
                tntKanbanShipping.setRevisionVersion(revisionVersion == null ? 1 : revisionVersion + 1);
                tntKanbanShipping
                    .setRevisionReason(MessageManager.getMessage("Common_Label_SystemAutomaticAdjustment"));
                tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
                tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NOT_IN_RUNDOWN);
                cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

                // 1.2 Insert into TNT_KANBAN_PLAN
                kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
                tntKanbanPlan = new TntKanbanPlan();
                tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
                tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
                tntKanbanPlan.setShippingUuid(shippingUuid);
                tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
                tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
                tntKanbanPlan.setIssuedDate(null);
                tntKanbanPlan.setIssueRemark(entityNird.getIssueRemark());
                tntKanbanPlan.setDeliveredDate(null);
                tntKanbanPlan.setDelivereRemark(entityNird.getDelivereRemark());
                tntKanbanPlan.setVanningDate(null);
                tntKanbanPlan.setVanningRemark(entityNird.getVanningRemark());
                tntKanbanPlan.setRevisionReason(null);
                cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

                // 1.3 Insert into TNT_KANBAN_SHIPPING_PARTS
                // 1.4 Insert into TNT_KANBAN_PARTS
                List<String[]> rowDataListData = entityNird.getRowDataList();
                for (int j = 0; j < rowDataListData.size(); j++) {
                    String[] rowData = rowDataListData.get(j);
                    String rowNo = rowData[0];
                    if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                        continue;
                    }

                    String strQty = rowData[1];
                    BigDecimal bdQty = BigDecimal.ZERO;
                    String strQtyDiff = rowDataForUpdateList.get(j)[1];
                    if (!StringUtils.isBlank(strQtyDiff)) {
                        bdQty = DecimalUtil.getBigDecimal(strQtyDiff).abs();
                    } else {
                        bdQty = DecimalUtil.getBigDecimal(strQty);
                    }
                    if (!StringUtils.isBlank(strQty) && bdQty.compareTo(BigDecimal.ZERO) != 0) {
                        // Insert into TNT_KANBAN_SHIPPING_PARTS
                        int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                        TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                        tntKanbanShippingPart.setKspId(kspId);
                        tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                        tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                        tntKanbanShippingPart.setQty(bdQty);
                        tntKanbanShippingPart.setKanbanQty(null);
                        cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                        // Insert into TNT_KANBAN_PARTS
                        int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                        TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                        tntKanbanPlanPart.setKbPlanPartsId(kppId);
                        tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                        tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                        tntKanbanPlanPart.setQty(bdQty);
                        tntKanbanPlanPart.setKanbanQty(null);
                        cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
                    }
                }
            }
        }
    }

    /**
     * Save not in rundown(new).
     * 
     * @param param base param
     * @param context SessionInfoManager
     * @param tntKanban the TntKanban that is exsit
     * @param tntKanbanInsert the TntKanban that is new
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     * @param entity upload file base information
     */
    @SuppressWarnings("unchecked")
    private void saveNirdNew(BaseParam param, SessionInfoManager context, TntKanban tntKanban,
        TntKanban tntKanbanInsert, HashMap<String, Integer> rowPartsId, HashMap<String, Integer> rowFcFlag,
        CPKKPF12Entity entity) {
        List<CPKKPF12ColEntity> listColNirdNew = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_NIRD_NEW);
        // 2. Insert nird new
        for (int i = listColNirdNew.size() - 1; i > -1; i--) {
            CPKKPF12ColEntity entityNirdNew = listColNirdNew.get(i);
            // 2.1 Insert into TNT_KANBAN_SHIPPING
            int kanbanShippingId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING);
            String shippingUuid = UUID.randomUUID().toString();
            TntKanbanShipping tntKanbanShipping = new TntKanbanShipping();
            tntKanbanShipping.setKanbanShippingId(kanbanShippingId);
            tntKanbanShipping.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanShipping.setShippingUuid(shippingUuid);
            tntKanbanShipping.setTransportMode(CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                CodeMasterCategory.TRANSPORT_MODE, entityNirdNew.getTransportMode()));
            tntKanbanShipping.setEtd(DateTimeUtil.parseDate(entityNirdNew.getEtd()));
            tntKanbanShipping.setEta(DateTimeUtil.parseDate(entityNirdNew.getEta()));
            tntKanbanShipping.setImpInbPlanDate(DateTimeUtil.parseDate(entityNirdNew.getImpInbPlanDate()));
            tntKanbanShipping.setOriginalVersion(cpkkpf12Service.getMaxOriginalVersion(tntKanbanShipping));
            tntKanbanShipping.setRevisionVersion(cpkkpf12Service.getMaxRevisionVersion(tntKanbanShipping));
            tntKanbanShipping.setRevisionReason(entityNirdNew.getRevisionReason());
            tntKanbanShipping.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
            tntKanbanShipping.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
            cpkkpf12Service.insertKanbanShipping(param, tntKanbanShipping);

            // 2.2 Insert into TNT_KANBAN_PLAN
            int kanbanPlanId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN);
            TntKanbanPlan tntKanbanPlan = new TntKanbanPlan();
            tntKanbanPlan.setKanbanPlanId(kanbanPlanId);
            tntKanbanPlan.setKanbanId(tntKanbanInsert.getKanbanId());
            tntKanbanPlan.setShippingUuid(shippingUuid);
            tntKanbanPlan.setOrderMonth(tntKanbanInsert.getOrderMonth());
            tntKanbanPlan.setPlanType(CodeConst.PlanType.SHIPPING_PLAN);
            tntKanbanPlan.setIssuedDate(null);
            tntKanbanPlan.setIssueRemark(entityNirdNew.getIssueRemark());
            tntKanbanPlan.setDeliveredDate(null);
            tntKanbanPlan.setDelivereRemark(entityNirdNew.getDelivereRemark());
            tntKanbanPlan.setVanningDate(null);
            tntKanbanPlan.setVanningRemark(entityNirdNew.getVanningRemark());
            tntKanbanPlan.setRevisionReason(null);
            cpkkpf12Service.insertKanbanPlan(param, tntKanbanPlan);

            // 2.3 Insert into TNT_KANBAN_SHIPPING_PARTS
            // 2.4 Insert into TNT_KANBAN_PARTS
            List<String[]> rowDataList = entityNirdNew.getRowDataList();
            for (String[] rowData : rowDataList) {
                String rowNo = rowData[0];
                if (rowFcFlag.get(rowNo).equals(CodeConst.KbsCompletedFlag.COMPLETED)) {
                    continue;
                }

                String strQty = rowData[1];
                BigDecimal bdQty = DecimalUtil.getBigDecimal(strQty);
                if (!StringUtils.isBlank(strQty) && bdQty.compareTo(BigDecimal.ZERO) != 0) {
                    // Insert into TNT_KANBAN_SHIPPING_PARTS
                    int kspId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_SHIPPING_PARTS);
                    TntKanbanShippingPart tntKanbanShippingPart = new TntKanbanShippingPart();
                    tntKanbanShippingPart.setKspId(kspId);
                    tntKanbanShippingPart.setKanbanShippingId(kanbanShippingId);
                    tntKanbanShippingPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanShippingPart.setQty(bdQty);
                    tntKanbanShippingPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanShippingParts(param, tntKanbanShippingPart);

                    // Insert into TNT_KANBAN_PARTS
                    int kppId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_KANBAN_PLAN_PARTS);
                    TntKanbanPlanPart tntKanbanPlanPart = new TntKanbanPlanPart();
                    tntKanbanPlanPart.setKbPlanPartsId(kppId);
                    tntKanbanPlanPart.setKanbanPlanId(kanbanPlanId);
                    tntKanbanPlanPart.setPartsId(rowPartsId.get(rowNo));
                    tntKanbanPlanPart.setQty(bdQty);
                    tntKanbanPlanPart.setKanbanQty(null);
                    cpkkpf12Service.insertKanbanPlanParts(param, tntKanbanPlanPart);
                }
            }
        }
    }

    /**
     * 9. Save actual invoice revision information.
     * 
     * @param param BaseParam
     * @param context SessionInfoManager
     */
    @SuppressWarnings("unchecked")
    private void saveActualInvoice(BaseParam param, SessionInfoManager context) {
        List<CPKKPF12ColEntity> listColActualVan = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_ACTUAL_VAN);
        // loop each actual detail in input file.
        for (CPKKPF12ColEntity colActualVanEntity : listColActualVan) {
            String revisionReason = colActualVanEntity.getRevisionReasonThisChange();

            // If the actual detail has revised and actual inbound date is null
            if (!StringUtil.isEmpty(revisionReason)) {
                String invoiceNo = colActualVanEntity.getInvoiceNo();
                Date etd = DateTimeUtil.parseDate(colActualVanEntity.getEtdRevision());
                Date eta = DateTimeUtil.parseDate(colActualVanEntity.getEtaRevision());
                Date impInbPlanDate = DateTimeUtil.parseDate(colActualVanEntity.getImpInbPlanDateRevision());

                // Update TNT_INVOICE_SUMMARY.
                TntInvoiceSummary tntInvoiceSummary = new TntInvoiceSummary();
                tntInvoiceSummary.setInvoiceNo(invoiceNo);
                tntInvoiceSummary.setEtd(etd);
                tntInvoiceSummary.setEta(eta);
                cpkkpf12Service.updateInvoiceSummary(param, tntInvoiceSummary);

                // Find need update TNT_INVOICE data.
                TntInvoice condition = new TntInvoice();
                condition.setInvoiceNo(invoiceNo);
                TntInvoice tntInvoiceSelect = cpkkpf12Service.getInvoiceUpdateObject(condition);

                // Insert into TNT_INVOICE_HISTORY
                TntInvoiceHistory tntInvoiceHistory = new TntInvoiceHistory();
                int invoiceHistoryId = cpkkpf12Service.getNextSequence(ChinaPlusConst.Sequence.TNT_INVOICE_HISTORY);
                tntInvoiceHistory.setInvoiceHistoryId(invoiceHistoryId);
                tntInvoiceHistory.setInvoiceId(tntInvoiceSelect.getInvoiceId());
                tntInvoiceHistory.setEtd(tntInvoiceSelect.getEtd());
                tntInvoiceHistory.setEta(tntInvoiceSelect.getEta());
                tntInvoiceHistory.setImpInbPlanDate(tntInvoiceSelect.getImpInbPlanDate());
                tntInvoiceHistory.setCcDate(tntInvoiceSelect.getCcDate());
                tntInvoiceHistory.setVanningDate(tntInvoiceSelect.getVanningDate());
                tntInvoiceHistory.setOriginalVersion(tntInvoiceSelect.getOriginalVersion());
                tntInvoiceHistory.setRevisionVersion(tntInvoiceSelect.getRevisionVersion());
                tntInvoiceHistory.setRevisionReason(tntInvoiceSelect.getRevisionReason());
                cpkkpf12Service.insertInvoiceHistory(param, tntInvoiceHistory);

                // Update TNT_INVOICE
                TntInvoice tntInvoiceUpdate = new TntInvoice();
                tntInvoiceUpdate.setInvoiceId(tntInvoiceSelect.getInvoiceId());
                tntInvoiceUpdate.setEtd(etd);
                tntInvoiceUpdate.setEta(eta);
                tntInvoiceUpdate.setImpInbPlanDate(impInbPlanDate);
                tntInvoiceUpdate.setOriginalVersion(tntInvoiceSelect.getRevisionVersion());
                tntInvoiceUpdate
                    .setRevisionVersion(DecimalUtil.integerToInt(tntInvoiceSelect.getRevisionVersion()) + 1);
                tntInvoiceUpdate.setRevisionReason(revisionReason);
                cpkkpf12Service.updateInvoice(param, tntInvoiceUpdate);

                // add for UAT
                // Update TNT_INVOICE_GROUP
                if (tntInvoiceSelect.getInvoiceGroupId() != null) {
                    TntInvoiceGroup groupData = super.baseDao.findOne(TntInvoiceGroup.class,
                        tntInvoiceSelect.getInvoiceGroupId());
                    if (groupData != null && TransportMode.AIR == groupData.getTransportMode()) {
                        groupData.setEtd(etd);
                        groupData.setEta(eta);
                        groupData.setImpInbPlanDate(impInbPlanDate);
                        groupData.setUpdatedBy(param.getLoginUserId());
                        groupData.setUpdatedDate(getDBDateTimeByDefaultTimezone());
                        groupData.setVersion(groupData.getVersion() + IntDef.INT_ONE);
                        super.baseDao.update(groupData);
                    }
                }
                // add end
            }
        }
    }

    /**
     * MergeFlag.
     */
    private interface MergeFlag {

        /** MERGED */
        final static int MERGED_SRC = 1;
        /** MERGED */
        final static int MERGED_DIS = 2;
    }

    /**
     * ColumnTypeAll.
     */
    private interface ColumnTypeAll {

        /** Difference Data Column */
        final static int DIFFERENCE_DATA = 3;
        /** Difference Mod Column */
        final static int DIFFERENCE_MOD = 4;
        /** Box Data Column */
        final static int BOX_DATA = 5;
        /** Box Mod Column */
        final static int BOX_MOD = 6;
        /** Plan Column */
        final static int PLAN = 7;
        /** Plan Mod Column */
        final static int PLAN_MOD = 8;
    }
}
