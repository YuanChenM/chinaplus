/**
 * CPKKPF12Controller.java
 * 
 * @screen CPKKPF12
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.entity.TntKanban;
import com.chinaplus.common.entity.TntKanbanShipping;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.kbp.entity.CPKKPF12AllActualInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12AllPartsInfoEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12ColEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12Entity;
import com.chinaplus.web.kbp.entity.CPKKPF12RowEntity;
import com.chinaplus.web.kbp.entity.CPKKPF12RowFfEntity;
import com.chinaplus.web.kbp.service.CPKKPF12Service;
import com.chinaplus.web.kbp.service.CPKKPF12UpdateService;

/**
 * Upload Revised Kanban Plan Controller.
 */
@Controller
public class CPKKPF12Controller extends BaseFileController {

    /** TITLE_POSITION */
    private static final int[] TITLE_POSITION = { 1, 1 };
    /** KANBAN_PLAN_NO_POSITION */
    private static final int[] KANBAN_PLAN_NO_POSITION = { 2, 1 };

    /** PARTS_INFO_START_COL */
    private static final int PARTS_INFO_START_COL = 1;
    /** PARTS_INFO_END_COL */
    private static final int PARTS_INFO_END_COL = 11;
    /** PLAN_INFO_END_ROW */
    private static final int PLAN_INFO_END_ROW = 18;

    /** PARTS_TTC_PART_NO_COL */
    private static final int PARTS_TTC_PART_NO_COL = 2;
    /** PARTS_OLD_PART_NO_COL */
    private static final int PARTS_OLD_PART_NO_COL = 3;
    /** PARTS_TTC_CUSTOMER_CODE_COL */
    private static final int PARTS_TTC_CUSTOMER_CODE_COL = 4;
    /** PARTS_KANBAN_CUSTOMER_CODE_COL */
    private static final int PARTS_KANBAN_CUSTOMER_CODE_COL = 5;
    /** PARTS_SUPPLIER_PART_NO_COL */
    private static final int PARTS_SUPPLIER_PART_NO_COL = 6;
    /** PARTS_SUPPLIER_CODE_COL */
    private static final int PARTS_SUPPLIER_CODE_COL = 7;
    /** PARTS_REMARK_COL */
    private static final int PARTS_REMARK_COL = 8;
    /** PARTS_QTY_BOX_COL */
    private static final int PARTS_QTY_BOX_COL = 9;
    /** PARTS_ORDER_QTY_COL */
    private static final int PARTS_ORDER_QTY_COL = 10;
    /** PARTS_KANBAN_QTY_COL */
    private static final int PARTS_KANBAN_QTY_COL = 11;

    /** PLAN_TRANSPORT_MODE_ROW */
    private static final int PLAN_TRANSPORT_MODE_ROW = 3;
    /** PLAN_TRANSPORT_MODE_REVISION_ROW */
    private static final int PLAN_TRANSPORT_MODE_REVISION_ROW = 4;
    /** PLAN_ISSUED_PLAN_DATE_ROW */
    private static final int PLAN_ISSUED_PLAN_DATE_ROW = 5;
    /** PLAN_DELIVER_DATE_ROW */
    private static final int PLAN_DELIVER_DATE_ROW = 6;
    /** PLAN_VAN_ROW */
    private static final int PLAN_VAN_ROW = 7;
    /** PLAN_INVOICE_NO_ROW */
    private static final int PLAN_INVOICE_NO_ROW = 8;
    /** PLAN_ETD_ROW */
    private static final int PLAN_ETD_ROW = 9;
    /** PLAN_ETD_REVISION_ROW */
    private static final int PLAN_ETD_REVISION_ROW = 10;
    /** PLAN_ETA_ROW */
    private static final int PLAN_ETA_ROW = 11;
    /** PLAN_ETA_REVISION_ROW */
    private static final int PLAN_ETA_REVISION_ROW = 12;
    /** PLAN_INBOUND_ROW */
    private static final int PLAN_INBOUND_ROW = 13;
    /** PLAN_INBOUND_REVISION_ROW */
    private static final int PLAN_INBOUND_REVISION_ROW = 14;
    /** PLAN_ACTUAL_INBOUND_ROW */
    private static final int PLAN_ACTUAL_INBOUND_ROW = 15;
    /** PLAN_REASON_LAST_CHANGE_ROW */
    private static final int PLAN_REASON_LAST_ROW = 16;
    /** PLAN_REASON_THIS_CHANGE_ROW */
    private static final int PLAN_REASON_THIS_ROW = 17;
    /** PLAN_REASON_VERSION_ROW */
    private static final int PLAN_REASON_VERSION_ROW = 18;

    /** NIRD_TRANSPORT_MODE_ROW */
    private static final int NIRD_TRANSPORT_MODE_ROW = 8;
    /** NIRD_ETD_ROW */
    private static final int NIRD_ETD_ROW = 9;
    /** NIRD_ETA_ROW */
    private static final int NIRD_ETA_ROW = 11;
    /** NIRD_IMP_INBOUND_DATE_ROW */
    private static final int NIRD_IMP_INBOUND_DATE_ROW = 13;
    /** NIRD_REVISON_REASON_ROW */
    private static final int NIRD_REVISON_REASON_ROW = 15;

    /** KEY_ROW */
    private static final String KEY_ROW = "ROW";
    /** KEY_COL */
    private static final String KEY_COL = "COL";

    /** MAX_LEN */
    private static final int MAX_LEN = 80;
    /** MAX_LEN_INVOCIE_NO */
    private static final int MAX_LEN_INVOCIE_NO = 30;
    /** DIGITS_KANBAN_QTY */
    private static final int DIGITS_KANBAN_QTY = 2;

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
    /** SESSION_KEY_ACTUAL_COL_DATA */
    private static final String SESSION_COL_ACTUAL = "ACTUAL_COL_DATA";
    /** SESSION_KEY_ACTUAL_VAN_COL_DATA */
    private static final String SESSION_COL_ACTUAL_VAN = "ACTUAL_VAN_COL_DATA";
    /** SESSION_KEY_PLAN_COL_PLAN_DATA */
    private static final String SESSION_COL_PLAN = "PLAN_COL_PLAN_DATA";
    /** SESSION_KEY_PLAN_COL_PLAN_NEW_DATA */
    private static final String SESSION_COL_PLAN_NEW = "PLAN_COL_PLAN_NEW_DATA";
    /** SESSION_KEY_PLAN_COL_DIFFERENCE_DATA */
    private static final String SESSION_COL_DIF = "PLAN_COL_DIFFERENCE_DATA";
    /** SESSION_KEY_PLAN_COL_BOX_DATA */
    private static final String SESSION_COL_BOX = "PLAN_COL_BOX_DATA";
    /** SESSION_KEY_PLAN_COL_NIRD_DATA */
    private static final String SESSION_COL_NIRD = "PLAN_COL_NIRD_DATA";
    /** SESSION_KEY_PLAN_COL_NIRD_NEW_DATA */
    private static final String SESSION_COL_NIRD_NEW = "PLAN_COL_NIRD_NEW_DATA";
    /** SESSION_COL_ALL */
    private static final String SESSION_COL_ALL = "PLAN_COL_ALL";

    /** Upload Revised Kanban Plan Service */
    @Autowired
    private CPKKPF12Service cpkkpf12Service;

    /** Upload Revised Kanban Plan Service */
    @Autowired
    private CPKKPF12UpdateService cpkkpf12UpdateService;

    /**
     * Do Upload
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/kbp/CPKKPF12/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        super.setCommonParam(param, request);
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            super.uploadFileProcess(file, FileType.EXCEL, param, request, response);
            Workbook workbook = ExcelUtil.getWorkBook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            // Read excel data
            readExcel(sheet, context);
            context.put(SESSION_KEY_FILE, file.getInputStream());
        }

        CPKKPF12Entity entity = doUploadProcess(param, request, context);
        if ("0".equals(entity.getUploadResult())) {
            context.remove(SESSION_KEY_FILE);
            context.remove(SESSION_ALL_PARTS_INFO);
            context.remove(SESSION_KANBAN_PLAN_NO);
            context.remove(SESSION_ROW);
            context.remove(SESSION_ROW_FF);
            context.remove(SESSION_COL_ACTUAL);
            context.remove(SESSION_COL_ACTUAL_VAN);
            context.remove(SESSION_COL_PLAN);
            context.remove(SESSION_COL_PLAN_NEW);
            context.remove(SESSION_COL_DIF);
            context.remove(SESSION_COL_BOX);
            context.remove(SESSION_COL_NIRD);
            context.remove(SESSION_COL_NIRD_NEW);
            context.remove(SESSION_COL_ALL);
        }
        BaseResult<BaseEntity> baseResult = new BaseResult<BaseEntity>();
        baseResult.setData(entity);
        super.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param param the parameters
     * @param request HttpServletRequest
     * @param context SessionInfoManager
     * @return CPKKPF12Entity (UploadResult: 0:update sucessful/2:have 2confime message/xxx:confime message id)
     */
    protected CPKKPF12Entity doUploadProcess(BaseParam param, HttpServletRequest request, SessionInfoManager context) {
        CPKKPF12Entity entity = new CPKKPF12Entity();
        List<BaseMessage> messageList = new ArrayList<BaseMessage>();
        List<BaseMessage> messageConfirmList = new ArrayList<BaseMessage>();

        // Check kanban.
        TntKanban tntKanban = checkKanban(param, request, context, messageList, messageConfirmList);
        if (messageList.size() > 0) {
            throw new BusinessException(messageList);
        }
        if (messageConfirmList.size() > 0 && UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            entity.setUploadResult(messageConfirmList.get(0).getMessageCode());
            return entity;
        }

        // Get session's CURRENT_CUSTOMER_CODE
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        param.setSwapData("CURRENT_CUSTOMER_CODE", um.getCurrentBusPattern());

        HashMap<String, Integer> rowPartsId = new HashMap<String, Integer>();
        HashMap<String, BigDecimal> rowQtyBox = new HashMap<String, BigDecimal>();
        HashMap<String, Integer> rowFcFlag = new HashMap<String, Integer>();
        checkInput(param, context, messageList, messageConfirmList, tntKanban, rowPartsId, rowQtyBox, rowFcFlag);
        if (messageList.size() > 0) {
            throw new BusinessException(messageList);
        }

        if (messageConfirmList.size() > 0
                && ("confirm".equals(param.getUploadProcess()) || UploadConst.UPLOAD_PROCESS_CHECK.equals(param
                    .getUploadProcess()))) {
            if (messageConfirmList.size() > 1) {
                entity.setUploadResult("3");
            } else {
                entity.setUploadResult(messageConfirmList.get(0).getMessageCode());
            }
            return entity;
        } else {
            // Update logic
            cpkkpf12UpdateService.doUpdateLogic(request, param, context, tntKanban, rowPartsId, rowQtyBox, rowFcFlag,
                messageList);
            entity.setUploadResult("0");
            return entity;
        }
    }

    /**
     * If additional shipping plan is same as the already exist plan.(w1004_066)
     * 
     * @param messageList message list
     * @param listPlanCol new plan col
     * @param language language
     */
    private void checkSameNewPlan(List<BaseMessage> messageList, List<CPKKPF12ColEntity> listPlanCol, Locale language) {
        HashMap<String, String> checkSamePlanNewMap = new HashMap<String, String>();
        for (CPKKPF12ColEntity colPlanEntity : listPlanCol) {
            String key = colPlanEntity.getTransportMode() + DateTimeUtil.formatDate(colPlanEntity.getEtd())
                    + DateTimeUtil.formatDate(colPlanEntity.getEta())
                    + DateTimeUtil.formatDate(colPlanEntity.getImpInbPlanDate());
            if (!StringUtils.isBlank(key)) {
                if (checkSamePlanNewMap.containsKey(key)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_066);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colPlanEntity.getColNo()) });
                    messageList.add(message);
                } else {
                    checkSamePlanNewMap.put(key, key);
                }
            }
        }
    }

    /**
     * Check input and input relevance.
     * 
     * @param param the parameters
     * @param context SessionInfoManager
     * @param messageList message list
     * @param messageConfirmList confirm message list
     * @param tntKanban kanban information
     * @param rowPartsId key:rowNo., value:partsId
     * @param rowQtyBox key:rowNo., value:input qty/box
     * @param rowFcFlag key:rowNo., value:FORCE_COMPLETED
     */
    @SuppressWarnings("unchecked")
    private void checkInput(BaseParam param, SessionInfoManager context, List<BaseMessage> messageList,
        List<BaseMessage> messageConfirmList, TntKanban tntKanban, HashMap<String, Integer> rowPartsId,
        HashMap<String, BigDecimal> rowQtyBox, HashMap<String, Integer> rowFcFlag) {
        CPKKPF12Entity entity = (CPKKPF12Entity) context.get(SESSION_KANBAN_PLAN_NO);
        int languageIndex = entity.getLanguageIndex();
        HashMap<String, Integer> rowPartsDigits = new HashMap<String, Integer>();
        List<BigDecimal> totalQty = new ArrayList<BigDecimal>();

        // 1. Check row data
        List<CPKKPF12RowEntity> listPlanRow = (List<CPKKPF12RowEntity>) context.get(SESSION_ROW);
        // Get all parts information in this Kanban Plan from DB.
        CPKKPF12AllPartsInfoEntity conditionParts = new CPKKPF12AllPartsInfoEntity();
        conditionParts.setKanbanId(tntKanban.getKanbanId());
        HashMap<String, CPKKPF12AllPartsInfoEntity> allPartsInfo = new HashMap<String, CPKKPF12AllPartsInfoEntity>();
        HashMap<Integer, CPKKPF12AllPartsInfoEntity> allPartsInfoPartsId = new HashMap<Integer, CPKKPF12AllPartsInfoEntity>();
        cpkkpf12Service.getAllPartsInfo(conditionParts, allPartsInfo, allPartsInfoPartsId);
        context.put(SESSION_ALL_PARTS_INFO, allPartsInfoPartsId);

        // Get Force Complete Qty, Order Forecast 1 - Order Forecast 6 in the upload file.
        HashMap<Integer, CPKKPF12RowFfEntity> mapPlanRowFf = (HashMap<Integer, CPKKPF12RowFfEntity>) context
            .get(SESSION_ROW_FF);
        // Get Difference and Box data.
        HashMap<Integer, CPKKPF12ColEntity> mapDif = (HashMap<Integer, CPKKPF12ColEntity>) context.get(SESSION_COL_DIF);
        HashMap<Integer, CPKKPF12ColEntity> mapBox = (HashMap<Integer, CPKKPF12ColEntity>) context.get(SESSION_COL_BOX);

        HashMap<String, String> checkKanbanCustomerCodeMap = new HashMap<String, String>();
        HashMap<String, String> checkDuplicateMap = new HashMap<String, String>();
        boolean srbqSameFlag = true;
        boolean forecastNumFlag = true;

        for (CPKKPF12RowEntity rowEntity : listPlanRow) {
            String rowNo = String.valueOf(rowEntity.getRowNo());
            BigDecimal qtyBox = DecimalUtil.getBigDecimal(rowEntity.getQtyBox());
            BigDecimal kanbanQty = DecimalUtil.getBigDecimal(rowEntity.getKanbanQty());
            BigDecimal orderQty = DecimalUtil.getBigDecimal(rowEntity.getOrderQty());
            rowQtyBox.put(rowNo, qtyBox);
            totalQty.add(BigDecimal.ZERO);

            // If TTC Part No. is blank.(w1004_001)
            checkBlank(messageList, "CPKKPF12_Grid_TTCPartNo", rowEntity.getTtcPartsNo(), KEY_ROW, rowNo, null);

            String key = rowEntity.getTtcPartsNo() + rowEntity.getTtcCustomerCode() + rowEntity.getKanbanCustomerCode()
                    + rowEntity.getSuppPartsNo() + rowEntity.getSupplierCode();
            if (!allPartsInfo.containsKey(key)) {
                // If the part does not exist (w1004_057)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_057);
                message.setMessageArgs(new String[] { String.valueOf(rowNo) });
                messageList.add(message);
                continue;
            }

            // Parts info DB
            CPKKPF12AllPartsInfoEntity partsInfoDb = allPartsInfo.get(key);
            rowPartsId.put(rowNo, partsInfoDb.getPartsId());
            rowPartsDigits.put(rowNo, partsInfoDb.getDigits());

            // If input parts is exist and completed, skip
            if (partsInfoDb.getStatus() == CodeConst.KanbanPartsStatus.FORCE_COMPLETED) {
                rowEntity.setForceCompletedFlag(CodeConst.KbsCompletedFlag.COMPLETED);
                rowFcFlag.put(rowNo, CodeConst.KbsCompletedFlag.COMPLETED);
                continue;
            } else {
                rowEntity.setForceCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
                rowFcFlag.put(rowNo, CodeConst.KbsCompletedFlag.NORMAL);
            }

            int digits = partsInfoDb.getDigits();
            BigDecimal srbqDb = partsInfoDb.getSrbq();
            int forecastNumDb = partsInfoDb.getForecastNum();

            // If there is duplicate Part in Kanban file.(w1004_037)
            if (!StringUtils.isBlank(rowEntity.getTtcPartsNo())) {
                if (checkDuplicateMap.containsKey(rowEntity.getTtcPartsNo())) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_037);
                    message.setMessageArgs(new String[] { rowNo, rowEntity.getTtcPartsNo() });
                    messageList.add(message);
                } else {
                    checkDuplicateMap.put(rowEntity.getTtcPartsNo(), rowEntity.getTtcPartsNo());
                }
            }

            // If Remark's length > 80.(w1004_050)
            checkLength(messageList, "CPKKPF12_Grid_Remark", rowEntity.getRemark(), MAX_LEN, KEY_ROW, rowNo, null);
            // Check Qty/Box
            checkRowQty(messageList, "CPKKPF12_Grid_QtyBox", rowEntity.getQtyBox(), rowNo, true, digits);
            // Check Order Qty
            checkRowQty(messageList, "CPKKPF12_Grid_Order", rowEntity.getOrderQty(), rowNo, true, digits);
            // Check Total Kanban Qty
            checkRowQty(messageList, "CPKKPF12_Grid_KanbanQty", rowEntity.getKanbanQty(), rowNo, true,
                DIGITS_KANBAN_QTY);

            // If Qty/Box * Total Kanban Qty != Total Order Qty.(w1004_034)
            if (orderQty.compareTo(kanbanQty.multiply(qtyBox)) != 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_034);
                message.setMessageArgs(new String[] { rowNo });
                messageList.add(message);
            }

            // If part Qty/Box in the input file and (7-1)'s SRBQ are not the same.(c1005)
            if (qtyBox.compareTo(srbqDb) != 0) {
                srbqSameFlag = srbqSameFlag && false;
            }

            // 1.2 Check row Force Complete Qty and Order Forecast 1 - Order Forecast 6
            CPKKPF12RowFfEntity rowPlanFfEntity = mapPlanRowFf.get(rowEntity.getRowNo());

            // If the number of Order Forecast < (7-1).FORECAST_NUM.(c1010)
            int forecastNum = rowPlanFfEntity.getForecastNum();
            if (forecastNum != forecastNumDb) {
                forecastNumFlag = forecastNumFlag && false;
            }

            // Check Order Forecast 1 - Order Forecast 6
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast1", rowPlanFfEntity.getFcQty1(), rowNo, false, digits);
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast2", rowPlanFfEntity.getFcQty2(), rowNo, false, digits);
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast3", rowPlanFfEntity.getFcQty3(), rowNo, false, digits);
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast4", rowPlanFfEntity.getFcQty4(), rowNo, false, digits);
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast5", rowPlanFfEntity.getFcQty5(), rowNo, false, digits);
            checkRowQty(messageList, "CPKKPF12_Grid_OrderForecast6", rowPlanFfEntity.getFcQty6(), rowNo, false, digits);

            // If the customer is not belong to current user.(w1004_023)
            TnmPartsMaster codition = new TnmPartsMaster();
            codition.setKanbanCustCode(rowEntity.getKanbanCustomerCode());
            TnmPartsMaster exsitCustomerCode = cpkkpf12Service.existCustomerCode(codition);
            boolean hasCustomerRolu = false;
            if (exsitCustomerCode != null) {
                List<com.chinaplus.common.bean.BusinessPattern> currentCustomerCode = (List<com.chinaplus.common.bean.BusinessPattern>) param
                    .getSwapData().get("CURRENT_CUSTOMER_CODE");
                for (com.chinaplus.common.bean.BusinessPattern customerCode : currentCustomerCode) {
                    if (BusinessPattern.AISIN == customerCode.getBusinessPattern().intValue()) {
                        if (customerCode.getCustomerCode().equals(exsitCustomerCode.getCustomerCode())) {
                            hasCustomerRolu = true;
                            break;
                        }
                    }
                }
            }
            if (!hasCustomerRolu) {
                checkKanbanCustomerCodeMap.put(rowNo, rowEntity.getKanbanCustomerCode());
            }
        }

        // If the customer is not belong to current user.(w1004_023)
        List<String> sameList = new ArrayList<String>();
        Iterator<Entry<String, String>> iter = checkKanbanCustomerCodeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String> e = (Entry<String, String>) iter.next();
            if (!sameList.contains(e.getValue())) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
                message.setMessageArgs(new String[] { e.getValue() });
                messageList.add(message);
                sameList.add(e.getValue());
            }
        }
        // If part Qty/Box in the input file and (7-1)'s SRBQ are not the same.(c1005)
        if (!srbqSameFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.C1005);
            messageConfirmList.add(message);
        }
        // If the number of Order Forecast < (7-1).FORECAST_NUM.(c1010)
        if (!forecastNumFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.C1010);
            messageConfirmList.add(message);
        }
        // If the number of parts in the input file < (7-1) result's size.(w1004_058)
        if (allPartsInfo.size() != listPlanRow.size()) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_058);
            messageList.add(message);
        }

        // Find all actual invoice data from DB.
        CPKKPF12AllActualInfoEntity conditionInvoice = new CPKKPF12AllActualInfoEntity();
        conditionInvoice.setKanbanPlanNo(tntKanban.getKanbanPlanNo());
        HashMap<String, CPKKPF12AllActualInfoEntity> mapHeader = new HashMap<String, CPKKPF12AllActualInfoEntity>();
        HashMap<String, HashMap<Integer, CPKKPF12AllActualInfoEntity>> mapDetail = new HashMap<String, HashMap<Integer, CPKKPF12AllActualInfoEntity>>();
        getAllActualMap(cpkkpf12Service.getAllActualInvoiceInfo(conditionInvoice), mapHeader, mapDetail);

        String sea = CodeCategoryManager.getCodeName(languageIndex, CodeMasterCategory.TRANSPORT_MODE,
            CodeConst.TransportMode.SEA);
        String air = CodeCategoryManager.getCodeName(languageIndex, CodeMasterCategory.TRANSPORT_MODE,
            CodeConst.TransportMode.AIR);

        // 2. Check col data
        List<String[]> rowDataNirdNewTotal = new ArrayList<String[]>();
        List<String[]> rowDataNirdTotal = new ArrayList<String[]>();
        List<CPKKPF12ColEntity> checkSameColList = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> checkInvoiceCountList = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listPlanColAll = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_ALL);
        for (int i = 0; i < listPlanColAll.size(); i++) {
            CPKKPF12ColEntity colEntity = listPlanColAll.get(i);
            String colNo = String.valueOf(colEntity.getColNo());
            int columnTypeAll = colEntity.getColumnTypeAll();

            if (columnTypeAll == ColumnTypeAll.ACTUAL) {
                // modify for UAT
                // // Check Transportation Mode
                // int transportMode = checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode",
                // colEntity.getTransportMode(), colNo, true, sea, air);
                //
                // boolean allBlankFlag;
                // if (transportMode == CodeConst.TransportMode.AIR) {
                // checkInvoiceCountList.add(colEntity);
                //
                // // Check col's row qty
                // allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits,
                // totalQty);
                // } else {
                // // Check col's row qty
                // allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits, null);
                // }
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode", colEntity.getTransportMode(),
                    colNo, true, sea, air);
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits,
                    null);
                // modify end

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // Check ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Check Plan Inbound
                checkDate(messageList, "CPKKPF12_Grid_PlanInbound", colEntity.getImpInbPlanDate(), colNo, true);
                // Check Actual Inbound
                checkDate(messageList, "CPKKPF12_Grid_ActualInbound", colEntity.getImpInbActualDate(), colNo, false);
                // Check Reason for Last Change
                checkLength(messageList, "CPKKPF12_Grid_LastChangeReason", colEntity.getRevisionReasonLastChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");

            } else if (columnTypeAll == ColumnTypeAll.ACTUAL_VAN) {
                // add for UAT
                if (!StringUtil.isEmpty(colEntity.getTransportMode())) {
                    checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode",
                        colEntity.getTransportMode(), colNo, true, sea, air);
                }
                // add end
                checkInvoiceCountList.add(colEntity);

                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits,
                    totalQty);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check VAN
                // modify for UAT
                // checkDate(messageList, "CPKKPF12_Grid_VAN", colEntity.getVanningRemark(), colNo, true);
                checkDate(messageList, "CPKKPF12_Grid_VAN", colEntity.getVanningRemark(), colNo, false);
                // modify end
                // Check Invoice No.
                checkBlank(messageList, "CPKKPF12_Grid_InvoiceNo", colEntity.getInvoiceNo(), KEY_COL, null, colNo);
                checkLength(messageList, "CPKKPF12_Grid_InvoiceNo", colEntity.getInvoiceNo(), MAX_LEN_INVOCIE_NO,
                    KEY_COL, null, colNo);
                // Check ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // Check ETD Revision
                checkDate(messageList, "CPKKPF12_Grid_ETDRevision", colEntity.getEtdRevision(), colNo, false);
                // Check ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Check ETA Revision
                checkDate(messageList, "CPKKPF12_Grid_ETARevision", colEntity.getEtaRevision(), colNo, false);
                // Check Plan Inbound
                checkDate(messageList, "CPKKPF12_Grid_PlanInbound", colEntity.getImpInbPlanDate(), colNo, true);
                // Check Plan Inbound Revision
                checkDate(messageList, "CPKKPF12_Grid_PlanInboundRevision", colEntity.getImpInbPlanDateRevision(),
                    colNo, false);
                // Check Actual Inbound
                checkDate(messageList, "CPKKPF12_Grid_ActualInbound", colEntity.getImpInbActualDate(), colNo, false);
                // Check Reason for Last Change
                checkLength(messageList, "CPKKPF12_Grid_LastChangeReason", colEntity.getRevisionReasonLastChange(),
                    MAX_LEN, KEY_COL, null, colNo);
                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");
                // Check blank group (ETD Revision, ETA Revision, ImpInbPlanDate Revision, RevisionReasonThisChange)
                checkBlankGroup(messageList, colNo, colEntity.getEtdRevision(), colEntity.getEtaRevision(),
                    colEntity.getImpInbPlanDateRevision(), colEntity.getRevisionReasonThisChange());
                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtdRevision(), colEntity.getEtaRevision(),
                    "CPKKPF12_Grid_ETDRevision", "CPKKPF12_Grid_ETARevision");
                checkDateFromTo(messageList, colNo, colEntity.getEtaRevision(), colEntity.getImpInbPlanDateRevision(),
                    "CPKKPF12_Grid_ETARevision", "CPKKPF12_Grid_PlanInboundRevision");

                StringBuffer sbMapKey = new StringBuffer();
                sbMapKey.append(colEntity.getInvoiceNo());
                sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(colEntity.getEtd()));
                sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(colEntity.getEta()));
                sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(colEntity.getImpInbPlanDate()));
                sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(colEntity.getImpInbActualDate()));
                if (mapHeader.containsKey(sbMapKey.toString())) {
                    // input parts info
                    List<String[]> listDetail = colEntity.getRowDataList();
                    // DB parts info
                    HashMap<Integer, CPKKPF12AllActualInfoEntity> mapDetailDb = mapDetail.get(sbMapKey.toString());
                    for (String[] partsInfo : listDetail) {
                        if (rowFcFlag.get(partsInfo[0]) != null
                                && rowFcFlag.get(partsInfo[0]) == CodeConst.KbsCompletedFlag.NORMAL) {
                            // input PartsId
                            int partsId = rowPartsId.get(partsInfo[0]);
                            // input qty
                            BigDecimal qty = DecimalUtil.getBigDecimal(partsInfo[1]);
                            // DB qty
                            BigDecimal qtyDb = BigDecimal.ZERO;
                            if (mapDetailDb.get(partsId) != null) {
                                qtyDb = mapDetailDb.get(partsId).getQty();
                            }
                            // If the qty in the input file is not same as (7-10)'s QTY.(w1004_064)
                            if (qty.compareTo(qtyDb) != 0) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_064);
                                message.setMessageArgs(new String[] { partsInfo[0], ExcelUtil.colIndexToStr(colNo) });
                                messageList.add(message);
                            }
                        }
                    }
                } else {
                    // If not exist show warning message.(w1004_063)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_063);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo) });
                    messageList.add(message);
                }
                // if (mapHeader.containsKey(colEntity.getInvoiceNo())) {
                // String etd = DateTimeUtil.formatDate(colEntity.getEtd());
                // String eta = DateTimeUtil.formatDate(colEntity.getEta());
                // String impInbPlanDate = DateTimeUtil.formatDate(colEntity.getImpInbPlanDate());
                // String impInbActualDate = DateTimeUtil.formatDate(colEntity.getImpInbActualDate());
                //
                // CPKKPF12AllActualInfoEntity invoiceInfo = mapHeader.get(colEntity.getInvoiceNo());
                // // DB invoice info
                // String etdDb = DateTimeUtil.formatDate(invoiceInfo.getEtd());
                // String etaDb = DateTimeUtil.formatDate(invoiceInfo.getEta());
                // String impInbPlanDateDb = DateTimeUtil.formatDate(invoiceInfo.getImpInbPlanDate());
                // String impInbActualDateDb = DateTimeUtil.formatDate(invoiceInfo.getImpInbActualDate());
                // if (!etd.equals(etdDb) || !eta.equals(etaDb) || !impInbPlanDate.equals(impInbPlanDateDb)
                // || !impInbActualDate.equals(impInbActualDateDb)) {
                // // If not exist show warning message.(w1004_063)
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_063);
                // message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo) });
                // messageList.add(message);
                // } else {
                // // input parts info
                // List<String[]> listDetail = colEntity.getRowDataList();
                // // DB parts info
                // HashMap<Integer, CPKKPF12AllActualInfoEntity> mapDetailDb = mapDetail.get(colEntity
                // .getInvoiceNo());
                // for (String[] partsInfo : listDetail) {
                // if (rowFcFlag.get(partsInfo[0]) != null
                // && rowFcFlag.get(partsInfo[0]) == CodeConst.KbsCompletedFlag.NORMAL) {
                // // input PartsId
                // int partsId = rowPartsId.get(partsInfo[0]);
                // // input qty
                // BigDecimal qty = DecimalUtil.getBigDecimal(partsInfo[1]);
                // // DB qty
                // BigDecimal qtyDb = BigDecimal.ZERO;
                // if (mapDetailDb.get(partsId) != null) {
                // qtyDb = mapDetailDb.get(partsId).getQty();
                // }
                // // If the qty in the input file is not same as (7-10)'s QTY.(w1004_064)
                // if (qty.compareTo(qtyDb) != 0) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_064);
                // message.setMessageArgs(new String[] { partsInfo[0], ExcelUtil.colIndexToStr(colNo) });
                // messageList.add(message);
                // }
                // }
                // }
                // }
                // } else {
                // // If not exist show warning message.(w1004_063)
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_063);
                // message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo) });
                // messageList.add(message);
                // }

            } else if (columnTypeAll == ColumnTypeAll.DIFFERENCE_DATA) {
                // Check col's row qty (do not check negative)
                boolean allBlankFlag = checkQtyOfColIsDiffBox(messageList, colEntity, null, false, DIGITS_KANBAN_QTY,
                    totalQty, rowQtyBox, mapDif);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check Reason for Last Change
                checkLength(messageList, "CPKKPF12_Grid_LastChangeReason", colEntity.getRevisionReasonLastChange(),
                    MAX_LEN, KEY_COL, null, colNo);

            } else if (columnTypeAll == ColumnTypeAll.DIFFERENCE_MOD) {
                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColIsDiffBox(messageList, colEntity, listPlanColAll.get(i - 1), true,
                    DIGITS_KANBAN_QTY, totalQty, rowQtyBox, mapDif);

                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // If Difference-Qty Revision is not empty and Difference-Reason for This Change is empty.(w1004_059)
                checkBlankThisChangeForModCol(messageList, allBlankFlag, colEntity, colNo);

            } else if (columnTypeAll == ColumnTypeAll.BOX_DATA) {
                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColIsDiffBox(messageList, colEntity, null, true, DIGITS_KANBAN_QTY,
                    totalQty, rowQtyBox, mapBox);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check Box-Issued plan date
                checkDate(messageList, "CPKKPF12_Grid_Issuedplandate", colEntity.getIssueRemark(), colNo, true);
                // Check Box-Deliver date to Obu
                checkDate(messageList, "CPKKPF12_Grid_Deliverdate", colEntity.getDelivereRemark(), colNo, true);
                // Check Box-VAN
                checkDate(messageList, "CPKKPF12_Grid_VAN", colEntity.getVanningRemark(), colNo, true);
                // Check Reason for Last Change
                checkLength(messageList, "CPKKPF12_Grid_LastChangeReason", colEntity.getRevisionReasonLastChange(),
                    MAX_LEN, KEY_COL, null, colNo);

            } else if (columnTypeAll == ColumnTypeAll.BOX_MOD) {
                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColIsDiffBox(messageList, colEntity, listPlanColAll.get(i - 1), true,
                    DIGITS_KANBAN_QTY, totalQty, rowQtyBox, mapBox);

                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // If Box-Qty Revision is not empty and Box-Reason for This Change is empty.(w1004_059)
                checkBlankThisChangeForModCol(messageList, allBlankFlag, colEntity, colNo);

            } else if (columnTypeAll == ColumnTypeAll.PLAN) {
                checkSameColList.add(colEntity);
                boolean hasModCol = false;
                if (i != listPlanColAll.size() - 1) {
                    CPKKPF12ColEntity colEntityNext = listPlanColAll.get(i + 1);
                    if (colEntityNext.getColumnTypeAll() == ColumnTypeAll.PLAN_MOD) {
                        hasModCol = true;
                    }
                }

                // Check col's row qty
                boolean allBlankFlag = false;
                if (!hasModCol && colEntity.getListBox().size() < 1 && colEntity.getListDiff().size() < 1) {
                    allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, null, totalQty);
                } else {
                    allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, null, null);
                }

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check Transportation Mode
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode", colEntity.getTransportMode(),
                    colNo, true, sea, air);
                // Check Transportation Mode Revision
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransModeRevision",
                    colEntity.getTransportModeRevision(), colNo, false, sea, air);
                // Check ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // Check ETD Revision
                checkDate(messageList, "CPKKPF12_Grid_ETDRevision", colEntity.getEtdRevision(), colNo, false);
                // Check ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Check ETA Revision
                checkDate(messageList, "CPKKPF12_Grid_ETARevision", colEntity.getEtaRevision(), colNo, false);
                // Check Plan Inbound
                checkDate(messageList, "CPKKPF12_Grid_PlanInbound", colEntity.getImpInbPlanDate(), colNo, true);
                // Check Plan Inbound Revision
                checkDate(messageList, "CPKKPF12_Grid_PlanInboundRevision", colEntity.getImpInbPlanDateRevision(),
                    colNo, false);
                // Check Reason for Last Change
                checkLength(messageList, "CPKKPF12_Grid_LastChangeReason", colEntity.getRevisionReasonLastChange(),
                    MAX_LEN, KEY_COL, null, colNo);
                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");
                // Check blank group (TransportMode Revision, ETD Revision, ETA Revision, ImpInbPlanDate Revision,
                // RevisionReasonThisChange)
                checkBlankGroup(messageList, colNo, colEntity.getTransportModeRevision(), colEntity.getEtdRevision(),
                    colEntity.getEtaRevision(), colEntity.getImpInbPlanDateRevision(),
                    colEntity.getRevisionReasonThisChange());
                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtdRevision(), colEntity.getEtaRevision(),
                    "CPKKPF12_Grid_ETDRevision", "CPKKPF12_Grid_ETARevision");
                checkDateFromTo(messageList, colNo, colEntity.getEtaRevision(), colEntity.getImpInbPlanDateRevision(),
                    "CPKKPF12_Grid_ETARevision", "CPKKPF12_Grid_PlanInboundRevision");

            } else if (columnTypeAll == ColumnTypeAll.PLAN_MOD) {
                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, listPlanColAll.get(i - 1),
                    colNo, rowPartsDigits, totalQty);

                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // If Plan-Qty Revision is not empty and Plan-Reason for This Change is empty.(w1004_059)
                checkBlankThisChangeForModCol(messageList, allBlankFlag, colEntity, colNo);

            } else if (columnTypeAll == ColumnTypeAll.PLAN_NEW) {
                checkSameColList.add(colEntity);

                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits,
                    totalQty);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check Transportation Mode
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode", colEntity.getTransportMode(),
                    colNo, true, sea, air);
                // Check Issued plan date (input is string, not date)
                checkLength(messageList, "CPKKPF12_Grid_Issuedplandate", colEntity.getIssueRemark(), MAX_LEN, KEY_COL,
                    null, colNo);
                // Check Deliver date to Obu (input is string, not date)
                checkLength(messageList, "CPKKPF12_Grid_Deliverdate", colEntity.getDelivereRemark(), MAX_LEN, KEY_COL,
                    null, colNo);
                // Check VAN (input is string, not date)
                checkLength(messageList, "CPKKPF12_Grid_VAN", colEntity.getVanningRemark(), MAX_LEN, KEY_COL, null,
                    colNo);
                // Check ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // Check ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Check Plan Inbound
                checkDate(messageList, "CPKKPF12_Grid_PlanInbound", colEntity.getImpInbPlanDate(), colNo, true);
                // Check Reason for This Change
                checkLength(messageList, "CPKKPF12_Grid_ThisChangeReason", colEntity.getRevisionReasonThisChange(),
                    MAX_LEN, KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");

            } else if (columnTypeAll == ColumnTypeAll.PLAN_NIRD_DATA) {
                checkSameColList.add(colEntity);

                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, null, totalQty);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);

                // Check Transportation Mode
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode", colEntity.getTransportMode(),
                    colNo, true, sea, air);
                // ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Imp Inbound Date
                checkDate(messageList, "CPKKPF12_Grid_ImpInboundDate", colEntity.getImpInbPlanDate(), colNo, true);
                // Revison Reason
                checkLength(messageList, "CPKKPF12_Grid_RevisonReason", colEntity.getRevisionReason(), MAX_LEN,
                    KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");

                // Get qty total for nird col
                List<String[]> rowDataNird = colEntity.getRowDataList();
                if (rowDataNirdTotal.size() < 1) {
                    rowDataNirdTotal.addAll(rowDataNird);
                } else {
                    for (int j = 0; j < rowDataNird.size(); j++) {
                        BigDecimal qty = DecimalUtil.getBigDecimal(rowDataNird.get(j)[1]);
                        BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataNirdTotal.get(j)[1]);
                        qtyTemp = qtyTemp.add(qty);
                        rowDataNirdTotal.set(j, new String[] { rowDataNird.get(j)[0], String.valueOf(qtyTemp) });
                    }
                }
            } else if (columnTypeAll == ColumnTypeAll.PLAN_NIRD_NEW) {
                checkSameColList.add(colEntity);

                // Check col's row qty
                boolean allBlankFlag = checkQtyOfColNotDiffBox(messageList, colEntity, null, colNo, rowPartsDigits,
                    null);

                // If all qty is blank
                checkBlankDataColAllQty(messageList, allBlankFlag, colNo);
                // Check Transportation Mode
                checkTransportMode(languageIndex, messageList, "CPKKPF12_Grid_TransMode", colEntity.getTransportMode(),
                    colNo, true, sea, air);
                // ETD
                checkDate(messageList, "CPKKPF12_Grid_ETD", colEntity.getEtd(), colNo, true);
                // ETA
                checkDate(messageList, "CPKKPF12_Grid_ETA", colEntity.getEta(), colNo, true);
                // Imp Inbound Date
                checkDate(messageList, "CPKKPF12_Grid_ImpInboundDate", colEntity.getImpInbPlanDate(), colNo, true);
                // Revison Reason
                checkLength(messageList, "CPKKPF12_Grid_RevisonReason", colEntity.getRevisionReason(), MAX_LEN,
                    KEY_COL, null, colNo);

                // Check data from-to
                checkDateFromTo(messageList, colNo, colEntity.getEtd(), colEntity.getEta(), "CPKKPF12_Grid_ETD",
                    "CPKKPF12_Grid_ETA");
                checkDateFromTo(messageList, colNo, colEntity.getEta(), colEntity.getImpInbPlanDate(),
                    "CPKKPF12_Grid_ETA", "CPKKPF12_Grid_PlanInbound");

                // Get qty total for nird new col
                List<String[]> rowDataNirdNew = colEntity.getRowDataList();
                if (rowDataNirdNewTotal.size() < 1) {
                    rowDataNirdNewTotal.addAll(rowDataNirdNew);
                } else {
                    for (int j = 0; j < rowDataNirdNew.size(); j++) {
                        BigDecimal qty = DecimalUtil.getBigDecimal(rowDataNirdNew.get(j)[1]);
                        BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataNirdNewTotal.get(j)[1]);
                        qtyTemp = qtyTemp.add(qty);
                        rowDataNirdNewTotal.set(j, new String[] { rowDataNirdNew.get(j)[0], String.valueOf(qtyTemp) });
                    }
                }
            }
        }

        // If the number of invoice in the input file is not same as (7-10) invoice's count.(w1004_065)
        if (mapHeader.size() != checkInvoiceCountList.size()) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_065);
            messageList.add(message);
        }

        // 4. If additional shipping plan is same as the already exist plan.(w1004_066)
        checkSameNewPlan(messageList, checkSameColList, entity.getLanguage());

        // 5. If SUM(Detail Order) != Total Order Qty.(w1004_056)
        for (int i = 0; i < listPlanRow.size(); i++) {
            CPKKPF12RowEntity rowData = listPlanRow.get(i);
            if (rowData.getForceCompletedFlag() != CodeConst.KbsCompletedFlag.COMPLETED) {
                BigDecimal orderQty = DecimalUtil.getBigDecimalWithNUll(rowData.getOrderQty());
                if (orderQty != null) {
                    BigDecimal totalDetailQty = totalQty.get(i);
                    if (orderQty.compareTo(totalDetailQty) != 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_056);
                        message.setMessageArgs(new String[] { String.valueOf(rowData.getRowNo()) });
                        messageList.add(message);
                    }
                }
            }
        }

        // 6. Check not in rundown
        List<CPKKPF12ColEntity> listColNird = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_NIRD);
        List<CPKKPF12ColEntity> listColNirdNew = (List<CPKKPF12ColEntity>) context.get(SESSION_COL_NIRD_NEW);
        // 6.1 If have no not in rundown but have not in rundown new, error.
        if (listColNird.size() < 1 && listColNirdNew.size() > 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_175);
            messageList.add(message);
        } else {
            // 6.2 Check nird data column is exist
            TntKanbanShipping condition = new TntKanbanShipping();
            condition.setKanbanId(tntKanban.getKanbanId());
            HashMap<String, TntKanbanShipping> nirdPlanMap = cpkkpf12Service.existNirdPlan(condition);
            for (int i = listColNird.size() - 1; i > -1; i--) {
                CPKKPF12ColEntity entityNird = listColNird.get(i);
                String key = CodeCategoryManager.getCodeValue(entity.getLanguageIndex(),
                    CodeMasterCategory.TRANSPORT_MODE, entityNird.getTransportMode())
                        + StringConst.COMMA
                        + DateTimeUtil.formatDate(entityNird.getEtd())
                        + StringConst.COMMA
                        + DateTimeUtil.formatDate(entityNird.getEta())
                        + StringConst.COMMA
                        + DateTimeUtil.formatDate(entityNird.getImpInbPlanDate());
                if (!nirdPlanMap.containsKey(key)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_174);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(entityNird.getColNo()) });
                    messageList.add(message);
                } else {
                    entityNird.setCompletedFlag(CodeConst.KbsCompletedFlag.NORMAL);
                    entityNird.setNirdFlag(CodeConst.KbsNirdFlag.NOT_IN_RUNDOWN);
                }
            }
            // 6.3 Check nird qty
            if (rowDataNirdNewTotal.size() > 0) {
                HashMap<String, String> errorRowMap = new HashMap<String, String>();
                for (int i = listColNird.size() - 1; i > -1; i--) {
                    CPKKPF12ColEntity entityNird = listColNird.get(i);
                    List<String[]> rowDataNird = entityNird.getRowDataList();
                    List<String[]> listCurrDiff = new ArrayList<String[]>(rowDataNirdNewTotal);
                    for (int j = 0; j < rowDataNird.size(); j++) {
                        BigDecimal qtyNew = DecimalUtil.getBigDecimal(rowDataNirdNewTotal.get(j)[1]);
                        BigDecimal qty = DecimalUtil.getBigDecimal(rowDataNird.get(j)[1]);
                        if (qtyNew.compareTo(BigDecimal.ZERO) != 0 && qty.compareTo(BigDecimal.ZERO) != 0) {
                            BigDecimal qtyDiff = qtyNew.subtract(qty);
                            if (qtyDiff.compareTo(BigDecimal.ZERO) >= 0) {
                                listCurrDiff.set(j, new String[] { rowDataNird.get(j)[0], "0" });
                            } else {
                                listCurrDiff.set(j, new String[] { rowDataNird.get(j)[0], String.valueOf(qtyDiff) });
                            }
                            rowDataNirdNewTotal.set(j,
                                new String[] { rowDataNird.get(j)[0],
                                    qtyDiff.compareTo(BigDecimal.ZERO) <= 0 ? "0" : String.valueOf(qtyDiff) });
                        } else {
                            listCurrDiff.set(j, new String[] { rowDataNird.get(j)[0], rowDataNird.get(j)[1] });
                        }

                        BigDecimal qtyTemp = DecimalUtil.getBigDecimal(rowDataNirdTotal.get(j)[1]);
                        if (qtyTemp.compareTo(qtyNew) < 0) {
                            errorRowMap.put(rowDataNird.get(j)[0], rowDataNird.get(j)[0]);
                        }
                    }
                    boolean isAllEmpty = true;
                    boolean isExistLessZero = false;
                    for (String[] diff : listCurrDiff) {
                        if (!StringUtils.isBlank(diff[1])) {
                            isAllEmpty = false;
                            if (DecimalUtil.getBigDecimal(diff[1]).compareTo(BigDecimal.ZERO) < 0) {
                                isExistLessZero = true;
                                break;
                            }
                        }
                    }
                    if (!isAllEmpty || isExistLessZero) {
                        entityNird.setCompletedFlag(CodeConst.KbsCompletedFlag.COMPLETED);
                        entityNird.setNirdFlag(CodeConst.KbsNirdFlag.NORMAL);
                        if (!isAllRowQtyZero(listCurrDiff)) {
                            entityNird.setRowDataForUpdateList(listCurrDiff);
                        }
                    }
                    if (isAllRowQtyZeroEmpty(rowDataNirdNewTotal)) {
                        break;
                    }
                }
                iter = errorRowMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, String> e = (Entry<String, String>) iter.next();
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_170);
                    message.setMessageArgs(new String[] { e.getKey() });
                    messageList.add(message);
                }
                context.put(SESSION_COL_NIRD, listColNird);
            }
        }
    }

    /**
     * Is all row qty zero/empty
     * 
     * @param rowDataList row data list
     * @return true: all row qty is zero /false: not all row qty is zero
     */
    private boolean isAllRowQtyZeroEmpty(List<String[]> rowDataList) {
        for (String[] rowData : rowDataList) {
            BigDecimal qty = DecimalUtil.getBigDecimal(rowData[1]);
            if (qty.compareTo(BigDecimal.ZERO) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is all row qty zero/empty
     * 
     * @param rowDataList row data list
     * @return true: all row qty is zero /false: not all row qty is zero
     */
    private boolean isAllRowQtyZero(List<String[]> rowDataList) {
        for (String[] rowData : rowDataList) {
            BigDecimal qty = DecimalUtil.getBigDecimal(rowData[1]);
            if (qty.compareTo(BigDecimal.ZERO) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * DB result -> 2 HashMap.
     * 
     * @param dataList search db result
     * @param mapHeader key:invoiceNo, value:invoice info
     * @param mapDetail key:invoiceNo, value:partsId and qty
     */
    private void getAllActualMap(List<CPKKPF12AllActualInfoEntity> dataList,
        HashMap<String, CPKKPF12AllActualInfoEntity> mapHeader,
        HashMap<String, HashMap<Integer, CPKKPF12AllActualInfoEntity>> mapDetail) {

        for (CPKKPF12AllActualInfoEntity dataEntity : dataList) {
            StringBuffer sbMapKey = new StringBuffer();
            sbMapKey.append(dataEntity.getInvoiceNo());
            sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(dataEntity.getEtd()));
            sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(dataEntity.getEta()));
            sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(dataEntity.getImpInbPlanDate()));
            sbMapKey.append(StringConst.COMMA).append(DateTimeUtil.formatDate(dataEntity.getImpInbActualDate()));
            mapHeader.put(sbMapKey.toString(), dataEntity);
            HashMap<Integer, CPKKPF12AllActualInfoEntity> partsMap = mapDetail.get(sbMapKey.toString());
            if (partsMap == null) {
                partsMap = new HashMap<Integer, CPKKPF12AllActualInfoEntity>();
                mapDetail.put(sbMapKey.toString(), partsMap);
            }
            partsMap.put(dataEntity.getPartsId(), dataEntity);
        }

        // HashMap<Integer, CPKKPF12AllActualInfoEntity> map = new HashMap<Integer, CPKKPF12AllActualInfoEntity>();
        // String invoiceNoNext = "";
        // for (int i = 0; i < dataList.size(); i++) {
        // CPKKPF12AllActualInfoEntity dataEntity = dataList.get(i);
        // map.put(dataEntity.getPartsId(), dataEntity);
        // String invoiceNo = dataEntity.getInvoiceNo();
        // mapHeader.put(invoiceNo, dataEntity);
        //
        // if (i != dataList.size() - 1) {
        // invoiceNoNext = dataList.get(i + 1).getInvoiceNo();
        // } else {
        // invoiceNoNext = "";
        // }
        // if (!invoiceNoNext.equals(invoiceNo)) {
        // mapDetail.put(invoiceNo, map);
        // map = new HashMap<Integer, CPKKPF12AllActualInfoEntity>();
        // }
        // }
    }

    /**
     * Check row qty of column which is not difference or box.
     * 
     * @param messageList message list
     * @param entity col data
     * @param entityData data col data (entityData != null if current column is mod column)
     * @param colNo col No.
     * @param rowPartsDigits (key: rowNo. value: digits)
     * @param totalQty row total qty
     * @return true:all blank/false:not all blank
     */
    private boolean checkQtyOfColNotDiffBox(List<BaseMessage> messageList, CPKKPF12ColEntity entity,
        CPKKPF12ColEntity entityData, String colNo, HashMap<String, Integer> rowPartsDigits, List<BigDecimal> totalQty) {
        List<String[]> rowqty = entity.getRowDataList();
        boolean allBlankFlag = true;
        for (int i = 0; i < rowqty.size(); i++) {
            String[] qtyInfo = rowqty.get(i);
            if (rowPartsDigits != null) {
                if (rowPartsDigits.containsKey(qtyInfo[0])) {
                    checkQty(messageList, "CPKKPF12_Grid_Qty", qtyInfo[1], qtyInfo[0], colNo, true,
                        rowPartsDigits.get(qtyInfo[0]).intValue());
                } else {
                    checkQty(messageList, "CPKKPF12_Grid_Qty", qtyInfo[1], qtyInfo[0], colNo, true, -1);
                }
            }
            if (!StringUtils.isBlank(qtyInfo[1])) {
                allBlankFlag = allBlankFlag && false;
                // If current cell is not blank(MOD or DATA), use current cell to add.
                if (totalQty != null) {
                    BigDecimal qtyTemp = totalQty.get(i).add(DecimalUtil.getBigDecimal(qtyInfo[1]));
                    totalQty.set(i, qtyTemp);
                }
            } else if (entityData != null) {
                // If current cell is blank and it's MOD column, use DATA column of this MOD column to add.
                if (totalQty != null) {
                    List<String[]> rowqtyData = entityData.getRowDataList();
                    BigDecimal qtyTemp = totalQty.get(i).add(DecimalUtil.getBigDecimal(rowqtyData.get(i)[1]));
                    totalQty.set(i, qtyTemp);
                }
            }
        }
        return allBlankFlag;
    }

    /**
     * Check row qty of column which is difference or box.
     * 
     * @param messageList message list
     * @param entity col data
     * @param entityData data col data (entityData != null if current column is mod column)
     * @param checkNeg true:check negative/false:do not check negative
     * @param digits digits
     * @param totalQty row total qty
     * @param rowQtyBox row qty/box
     * @param map difference data / box data
     * @return true:all blank/false:not all blank
     */
    private boolean checkQtyOfColIsDiffBox(List<BaseMessage> messageList, CPKKPF12ColEntity entity,
        CPKKPF12ColEntity entityData, boolean checkNeg, int digits, List<BigDecimal> totalQty,
        HashMap<String, BigDecimal> rowQtyBox, HashMap<Integer, CPKKPF12ColEntity> map) {
        List<String[]> rowqty = entity.getRowDataList();
        int colNo = entity.getColNo();
        boolean allBlankFlag = true;
        for (int i = 0; i < rowqty.size(); i++) {
            String[] qtyInfo = rowqty.get(i);
            checkQty(messageList, "CPKKPF12_Grid_Qty", qtyInfo[1], qtyInfo[0], String.valueOf(colNo), checkNeg, digits);
            if (!StringUtils.isBlank(qtyInfo[1])) {
                allBlankFlag = allBlankFlag && false;
                if ((StringUtils.isBlank(map.get(colNo).getColumnType()) && map.get(colNo + 1) == null)
                        || !StringUtils.isBlank(map.get(colNo).getColumnType())) {
                    // If current cell is not blank and has no mod column, use current cell to add.
                    BigDecimal qtyTemp = totalQty.get(i).add(
                        DecimalUtil.getBigDecimal(qtyInfo[1]).multiply(rowQtyBox.get(qtyInfo[0])));
                    totalQty.set(i, qtyTemp);
                }
            } else if (entityData != null) {
                // If current cell is blank and it's MOD column, use DATA column of this MOD column to add.
                List<String[]> rowqtyData = entityData.getRowDataList();
                BigDecimal qtyTemp = totalQty.get(i).add(
                    DecimalUtil.getBigDecimal(rowqtyData.get(i)[1]).multiply(rowQtyBox.get(qtyInfo[0])));
                totalQty.set(i, qtyTemp);
            }
        }
        return allBlankFlag;
    }

    /**
     * If Box-Qty Revision is not empty and Box-Reason for This Change is empty.(w1004_059)<br>
     * If Difference-Qty Revision is not empty and Difference-Reason for This Change is empty.(w1004_059)<br>
     * If Plan-Qty Revision is not empty and Plan-Reason for This Change is empty.(w1004_059)<br>
     * 
     * @param messageList message list
     * @param allBlankFlag true:all blank/false: not all blank
     * @param entity col data
     * @param colNo col No.
     */
    private void checkBlankThisChangeForModCol(List<BaseMessage> messageList, boolean allBlankFlag,
        CPKKPF12ColEntity entity, String colNo) {
        if (!allBlankFlag && StringUtils.isBlank(entity.getRevisionReasonThisChange())) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_059);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo) });
            messageList.add(message);
        }
    }

    /**
     * If all of data column's qty are blank.
     * 
     * @param messageList message list
     * @param allBlankFlag true:all blank/false: not all blank
     * @param colNo col No.
     */
    private void checkBlankDataColAllQty(List<BaseMessage> messageList, boolean allBlankFlag, String colNo) {
        if (allBlankFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_052);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), "CPKKPF12_Grid_Qty" });
            messageList.add(message);
        }
    }

    /**
     * Check data from-to.
     * 
     * @param messageList message list
     * @param colNo col number
     * @param dateFrom date from
     * @param dateTo date to
     * @param nameFrom item name of date from
     * @param nameTo item name of date to
     */
    private void checkDateFromTo(List<BaseMessage> messageList, String colNo, String dateFrom, String dateTo,
        String nameFrom, String nameTo) {
        Date dateStart = DateTimeUtil.parseDate(dateFrom);
        Date dateEnd = DateTimeUtil.parseDate(dateTo);
        if (dateStart != null && dateEnd != null && dateStart.after(dateEnd)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), nameTo, nameFrom });
            messageList.add(message);
        }
    }

    /**
     * Check blank Group.<br>
     * All blank or all not blank is ok, else error.<br>
     * 
     * @param messageList message list
     * @param colNo col number
     * @param value check value
     */
    private void checkBlankGroup(List<BaseMessage> messageList, String colNo, String... value) {
        boolean allNotBlank = true;
        boolean allBlank = true;
        for (String i : value) {
            if (StringUtils.isBlank(i)) {
                allNotBlank = allNotBlank && false;
            } else {
                allBlank = allBlank && false;
            }
        }
        if (!allNotBlank && !allBlank) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_060);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo) });
            messageList.add(message);
        }
    }

    /**
     * Check transport mode common.
     * 
     * @param languageIndex language index
     * @param messageList message list
     * @param name check name
     * @param value check value
     * @param colNo col number
     * @param checkBlank true:check blank/false:do not check blank
     * @param sea sea
     * @param air air
     * @return sea/air value
     */
    private int checkTransportMode(int languageIndex, List<BaseMessage> messageList, String name, String value,
        String colNo, boolean checkBlank, String sea, String air) {
        if (checkBlank && StringUtils.isBlank(value)) {
            // If value is blank.
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_052);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), name });
            messageList.add(message);
        } else if (!StringUtils.isBlank(value)) {
            // If value is not SEA and not AIR.
            if (!sea.equalsIgnoreCase(value) && !air.equalsIgnoreCase(value)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_163);
                message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), name });
                messageList.add(message);
            }
        }
        if (sea.equalsIgnoreCase(value)) {
            return CodeConst.TransportMode.SEA;
        } else if (air.equalsIgnoreCase(value)) {
            return CodeConst.TransportMode.AIR;
        }
        return 0;
    }

    /**
     * Check blank common.
     * 
     * @param messageList message list
     * @param name check name
     * @param value check value
     * @param type ROW/COL
     * @param rowNo row number
     * @param colNo col number
     */
    private void checkBlank(List<BaseMessage> messageList, String name, String value, String type, String rowNo,
        String colNo) {
        if (StringUtils.isBlank(value)) {
            if (KEY_ROW.equals(type)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { rowNo, name });
                messageList.add(message);
            } else if (KEY_COL.equals(type)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_052);
                message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), name });
                messageList.add(message);
            }
        }
    }

    /**
     * Check max length common.
     * 
     * @param messageList message list
     * @param name check name
     * @param value check value
     * @param len max length
     * @param type ROW/COL
     * @param rowNo row number
     * @param colNo col number
     */
    private void checkLength(List<BaseMessage> messageList, String name, String value, int len, String type,
        String rowNo, String colNo) {
        if (!StringUtils.isBlank(value) && value.length() > len) {
            if (KEY_ROW.equals(type)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                message.setMessageArgs(new String[] { rowNo, name, String.valueOf(len) });
                messageList.add(message);
            } else if (KEY_COL.equals(type)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_051);
                message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), name, String.valueOf(len) });
                messageList.add(message);
            }
        }
    }

    /**
     * Check row qty common.<br>
     * 1. If vaue is blank.(w1004_001)<br>
     * 2. If vaue's format is error.(w1004_025)<br>
     * 3. If vaue's is negative.(w1004_029)<br>
     * 4. If vaue's is more than the max value in UOM.(w1004_176)<br>
     * 
     * @param messageList message list
     * @param item check item
     * @param value check value
     * @param rowNo row number
     * @param checkBlank true:check blank/false:do not check blank
     * @param digits -1:do not check digits;other:check digits
     */
    private void checkRowQty(List<BaseMessage> messageList, String item, String value, String rowNo,
        boolean checkBlank, int digits) {
        if (checkBlank && StringUtils.isBlank(value)) {
            // If vaue is blank.(w1004_001)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
            message.setMessageArgs(new String[] { rowNo, item });
            messageList.add(message);
        } else if (!StringUtils.isBlank(value)) {
            BigDecimal bdValue = DecimalUtil.getBigDecimalWithNUll(value);
            if (bdValue == null) {
                // If vaue's format is error.(w1004_025)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                message.setMessageArgs(new String[] { rowNo, item, "Common_ItemType_Decimal" });
                messageList.add(message);
            } else {
                if (DecimalUtil.isLess(bdValue, BigDecimal.ZERO)) {
                    // If vaue's is negative.(w1004_029)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { rowNo, item });
                    messageList.add(message);
                }
                if (digits != -1
                        && !ValidatorUtils.checkMaxDecimal(bdValue, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
                    // If vaue's is more than the max value in UOM.(w1004_176)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_176);
                    message.setMessageArgs(new String[] { rowNo, item, "10", String.valueOf(digits) });
                    messageList.add(message);
                }
            }
        }
    }

    /**
     * Check qty common.<br>
     * 1. If vaue's format is error.(w1004_026)<br>
     * 2. If vaue's is negative.(w1004_030)<br>
     * 3. If vaue's is more than the max value in DB.(w1004_177)<br>
     * 
     * @param messageList message list
     * @param item check item
     * @param value check value
     * @param rowNo row number
     * @param colNo col number
     * @param checkNeg true:check negative/false:do not check negative
     * @param digits -1:do not check digits;other:check digits
     */
    private void checkQty(List<BaseMessage> messageList, String item, String value, String rowNo, String colNo,
        boolean checkNeg, int digits) {
        if (!StringUtils.isBlank(value)) {
            BigDecimal bdValue = DecimalUtil.getBigDecimalWithNUll(value);
            if (bdValue == null) {
                // If vaue's format is error.(w1004_026)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_026);
                message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo), item,
                    "Common_ItemType_Decimal" });
                messageList.add(message);
            } else {
                if (checkNeg && DecimalUtil.isLess(bdValue, BigDecimal.ZERO)) {
                    // If vaue's is negative.(w1004_030)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_030);
                    message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo), item });
                    messageList.add(message);
                }
                if (digits != -1
                        && !ValidatorUtils.checkMaxDecimal(bdValue, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
                    // If vaue's is more than the max value in DB.(w1004_177)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_177);
                    message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo), item, "10",
                        String.valueOf(digits) });
                    messageList.add(message);
                }
            }
        }
    }

    /**
     * Check date common.
     * 
     * @param messageList message list
     * @param item check item
     * @param value check value
     * @param colNo col number
     * @param checkBlank true:check blank/false:do not check blank
     */
    private void checkDate(List<BaseMessage> messageList, String item, String value, String colNo, boolean checkBlank) {
        if (checkBlank && StringUtils.isBlank(value)) {
            // If vaue is blank.(w1004_052)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_052);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), item });
            messageList.add(message);
        } else if (!StringUtils.isBlank(value) && DateTimeUtil.parseDate(value) == null) {
            // If value's format is error.(w1004_053)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(colNo), item, "Common_ItemType_Date" });
            messageList.add(message);
        }
    }

    /**
     * Check kanban.
     * 
     * @param param the parameters
     * @param request HttpServletRequest
     * @param context SessionInfoManager
     * @param messageList message list
     * @param messageConfirmList confirm message list
     * @return TntKanban kanban information
     */
    private TntKanban checkKanban(BaseParam param, HttpServletRequest request, SessionInfoManager context,
        List<BaseMessage> messageList, List<BaseMessage> messageConfirmList) {
        CPKKPF12Entity entity = (CPKKPF12Entity) context.get(SESSION_KANBAN_PLAN_NO);
        TntKanban condition = new TntKanban();
        condition.setKanbanPlanNo(entity.getKanbanPlanNo());
        condition.setOfficeId(param.getCurrentOfficeId().intValue());
        TntKanban tntKanban = cpkkpf12Service.existKanbanPlan(condition);
        if (tntKanban == null) {
            // Check if the Kanban Plan is not exist, show error message(w1004_049), action end.
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_049);
            messageList.add(message);
        } else {
            // If sheet name is not same as correct sheet name, show confirm message.(c1009)
            String kanbanPlanNo = tntKanban.getKanbanPlanNo();
            String revisionVersion = String.valueOf(tntKanban.getRevisionVersion());
            if (!StringUtils.isBlank(revisionVersion) && !"0".equals(revisionVersion)) {
                DecimalFormat df = new DecimalFormat("00");
                kanbanPlanNo = kanbanPlanNo + StringConst.UNDERLINE + StringConst.ALPHABET_R
                        + df.format(Integer.parseInt(revisionVersion));
            }
            if (!entity.getSheetName().equals(kanbanPlanNo)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.C1009);
                messageConfirmList.add(message);
            }
        }
        return tntKanban;
    }

    /**
     * Kanban Plan No.(Cut off "_RXX").
     * 
     * @param kanbanPlanNo Kanban Plan No.
     * @return Cut off "_RXX"
     */
    private String cutOffVersion(String kanbanPlanNo) {
        int rIndex = kanbanPlanNo.indexOf(StringConst.UNDERLINE + StringConst.ALPHABET_R);
        if (rIndex > -1) {
            return kanbanPlanNo.substring(0, rIndex);
        }
        return kanbanPlanNo;
    }

    /**
     * Read excel data.
     * 
     * @param sheet Sheet
     * @param context SessionInfoManager
     */
    private void readExcel(Sheet sheet, SessionInfoManager context) {
        List<CPKKPF12RowEntity> listRowData = new ArrayList<CPKKPF12RowEntity>();
        HashMap<Integer, CPKKPF12RowFfEntity> mapRowFfData = new HashMap<Integer, CPKKPF12RowFfEntity>();
        List<CPKKPF12ColEntity> listColActualData = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listColActualVanData = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listColPlanData = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listColPlanNewData = new ArrayList<CPKKPF12ColEntity>();
        HashMap<Integer, CPKKPF12ColEntity> mapColDiffData = new HashMap<Integer, CPKKPF12ColEntity>();
        HashMap<Integer, CPKKPF12ColEntity> mapColBoxData = new HashMap<Integer, CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listColNirdData = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listColNirdNewData = new ArrayList<CPKKPF12ColEntity>();
        List<CPKKPF12ColEntity> listAllColData = new ArrayList<CPKKPF12ColEntity>();

        // Get language of input file
        CPKKPF12Entity entity = new CPKKPF12Entity();
        entity.setLanguageIndex(CodeConst.CategoryLanguage.ENGLISH);
        Locale language = Language.ENGLISH.getLocale();
        String titleFile = PoiUtil.getStringCellValue(sheet, TITLE_POSITION[0], TITLE_POSITION[1]);
        String titleCn = MessageManager.getMessage("CPKKPF12_Label_Title", Language.CHINESE.getLocale());
        if (titleFile.equals(titleCn)) {
            language = Language.CHINESE.getLocale();
            entity.setLanguageIndex(CodeConst.CategoryLanguage.CHINESE);
        }

        // File format check: if the last row is not "Last Orion Data Time:", error.
        if (PoiUtil.getStringCellValue(sheet, sheet.getLastRowNum() + 1, PARTS_INFO_START_COL).indexOf(
            MessageManager.getMessage("CPKKPF12_Label_LastOrionTime", language)) < 0) {
            throwFileFormatError();
        }
        // File format check: if the last row but one is not "Downloaded Server Time:", error.
        if (PoiUtil.getStringCellValue(sheet, sheet.getLastRowNum(), PARTS_INFO_START_COL).indexOf(
            MessageManager.getMessage("CPKKPF12_Label_DownloadTime", language)) < 0) {
            throwFileFormatError();
        }

        String labelNew = MessageManager.getMessage("CPKKPF12_Grid_New", language).toUpperCase();
        String labelMod = MessageManager.getMessage("CPKKPF12_Grid_Mod", language).toUpperCase();
        String labelPcs = MessageManager.getMessage("CPKKPF12_Grid_Pcs", language).toUpperCase();
        String labelInvoiceQty = MessageManager.getMessage("CPKKPF12_Grid_InvoiceQty", language).toUpperCase();
        String labelDiscoIndi = MessageManager.getMessage("CPKKPF12_Grid_DiscontinueIndicator", language).toUpperCase();
        String labelForCompQty = MessageManager.getMessage("CPKKPF12_Grid_ForceCompleteQty", language).toUpperCase();
        String labelStockTranOut = MessageManager.getMessage("CPKKPF12_Grid_StockTransferOut", language).toUpperCase();
        String labelTransMode = MessageManager.getMessage("CPKKPF12_Grid_TransMode", language).toUpperCase();
        String labelActual = MessageManager.getMessage("CPKKPF12_Grid_Actual", language).toUpperCase();
        String labelActualVan = MessageManager.getMessage("CPKKPF12_Grid_ActualVan", language).toUpperCase();
        String labelPlan = MessageManager.getMessage("CPKKPF12_Grid_Plan", language).toUpperCase();
        String labelBox = MessageManager.getMessage("CPKKPF12_Grid_Box", language).toUpperCase();
        String labelDifference = MessageManager.getMessage("CPKKPF12_Grid_Difference", language).toUpperCase();

        // 1. Get title information(kanbanPlanNo)
        entity.setSheetName(sheet.getSheetName());
        entity.setKanbanPlanNo(cutOffVersion(PoiUtil.getStringCellValue(sheet, KANBAN_PLAN_NO_POSITION[0],
            KANBAN_PLAN_NO_POSITION[1])));
        entity.setLanguage(language);

        // 2. Get parts row data
        for (int i = PLAN_INFO_END_ROW + 1; i <= sheet.getLastRowNum(); i++) {
            int readNextRowOrStopFlag = readNextRowOrStop(sheet, i, language);
            if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.READ_NEXT) {
                continue;
            } else if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.STOP) {
                break;
            }

            CPKKPF12RowEntity planRowEntity = new CPKKPF12RowEntity();
            // Row No.
            planRowEntity.setRowNo(i);
            // TTC Part No.
            planRowEntity.setTtcPartsNo(PoiUtil.getStringCellValue(sheet, i, PARTS_TTC_PART_NO_COL));
            // Old Part No.
            planRowEntity.setOldPartsNo(PoiUtil.getStringCellValue(sheet, i, PARTS_OLD_PART_NO_COL));
            // TTC Customer Code
            planRowEntity.setTtcCustomerCode(PoiUtil.getStringCellValue(sheet, i, PARTS_TTC_CUSTOMER_CODE_COL));
            // Kanban Customer Code
            planRowEntity.setKanbanCustomerCode(PoiUtil.getStringCellValue(sheet, i, PARTS_KANBAN_CUSTOMER_CODE_COL));
            // Supplier Part No.
            planRowEntity.setSuppPartsNo(PoiUtil.getStringCellValue(sheet, i, PARTS_SUPPLIER_PART_NO_COL));
            // Supplier Code
            planRowEntity.setSupplierCode(PoiUtil.getStringCellValue(sheet, i, PARTS_SUPPLIER_CODE_COL));
            // Remark
            planRowEntity.setRemark(PoiUtil.getStringCellValue(sheet, i, PARTS_REMARK_COL));
            // Qty/Box
            planRowEntity.setQtyBox(PoiUtil.getStringCellValue(sheet, i, PARTS_QTY_BOX_COL));
            // Order
            planRowEntity.setOrderQty(PoiUtil.getStringCellValue(sheet, i, PARTS_ORDER_QTY_COL));
            // Kanban Qty
            planRowEntity.setKanbanQty(PoiUtil.getStringCellValue(sheet, i, PARTS_KANBAN_QTY_COL));
            listRowData.add(planRowEntity);
        }
        if (listRowData == null || listRowData.size() < 1) {
            List<BaseMessage> messageList = new ArrayList<BaseMessage>();
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageList.add(message);
            throw new BusinessException(messageList);
        }

        // 3. Get plan col data (include data column, mod column, new column)
        int colNoQtyWithInvoice = 0;
        List<Integer> listDiff = new ArrayList<Integer>();
        List<Integer> listBox = new ArrayList<Integer>();
        List<Integer> listDiffMod = new ArrayList<Integer>();
        List<Integer> listBoxMod = new ArrayList<Integer>();
        for (int i = PARTS_INFO_END_COL + 1; i <= sheet.getRow(PLAN_INFO_END_ROW - 1).getLastCellNum(); i++) {
            String title = PoiUtil.getStringCellValue(sheet, 1, i);
            String revisionVersionFile = PoiUtil.getStringCellValue(sheet, PLAN_REASON_VERSION_ROW, i).toUpperCase();

            // File format check of shipping plan new column
            if ((title.equalsIgnoreCase(labelNew) && !revisionVersionFile.equalsIgnoreCase(labelPcs))
                    || (!title.equalsIgnoreCase(labelNew) && revisionVersionFile.equalsIgnoreCase(labelPcs))) {
                throwFileFormatError();
            }

            if (StringUtils.isBlank(revisionVersionFile) && StringUtils.isBlank(title)) {
                // If current column's "revisionVersion" and title(blank/MOD/NEW) is blank, stop read.
                break;
            } else if (revisionVersionFile.indexOf(labelInvoiceQty) > -1) {
                // If current column is "Qty with invoice", stop read and get current column No.
                colNoQtyWithInvoice = i;
                break;
            } else if (isBlankCol(sheet, i, PLAN_TRANSPORT_MODE_ROW, listRowData, PLAN_REASON_VERSION_ROW)) {
                // If current column(from "Transportation Mode" to last row data) is blank, read the next
                // column.
                continue;
            }

            CPKKPF12ColEntity planColumnEntity = new CPKKPF12ColEntity();
            // Column Type
            planColumnEntity.setColumnType(title);
            // Merge Flag init
            planColumnEntity.setMergeFlag(MergeFlag.NOT_MERGED);
            // Col No.
            planColumnEntity.setColNo(i);
            // Transportation Mode
            planColumnEntity.setTransportMode(PoiUtil.getStringCellValue(sheet, PLAN_TRANSPORT_MODE_ROW, i));
            // Transportation Mode Revision
            planColumnEntity.setTransportModeRevision(PoiUtil.getStringCellValue(sheet,
                PLAN_TRANSPORT_MODE_REVISION_ROW, i));
            // Issued plan date
            planColumnEntity.setIssueRemark(PoiUtil.getStringCellValue(sheet, PLAN_ISSUED_PLAN_DATE_ROW, i));
            // Deliver date to Obu
            planColumnEntity.setDelivereRemark(PoiUtil.getStringCellValue(sheet, PLAN_DELIVER_DATE_ROW, i));
            // VAN
            planColumnEntity.setVanningRemark(PoiUtil.getStringCellValue(sheet, PLAN_VAN_ROW, i));
            // Invoice No.
            planColumnEntity.setInvoiceNo(PoiUtil.getStringCellValue(sheet, PLAN_INVOICE_NO_ROW, i));
            // ETD
            planColumnEntity.setEtd(PoiUtil.getStringCellValue(sheet, PLAN_ETD_ROW, i));
            // ETD Revision
            planColumnEntity.setEtdRevision(PoiUtil.getStringCellValue(sheet, PLAN_ETD_REVISION_ROW, i));
            // ETA
            planColumnEntity.setEta(PoiUtil.getStringCellValue(sheet, PLAN_ETA_ROW, i));
            // ETA Revision
            planColumnEntity.setEtaRevision(PoiUtil.getStringCellValue(sheet, PLAN_ETA_REVISION_ROW, i));
            // Plan Inbound
            planColumnEntity.setImpInbPlanDate(PoiUtil.getStringCellValue(sheet, PLAN_INBOUND_ROW, i));
            // Plan Inbound Revision
            planColumnEntity.setImpInbPlanDateRevision(PoiUtil.getStringCellValue(sheet, PLAN_INBOUND_REVISION_ROW, i));
            // Actual Inbound
            planColumnEntity.setImpInbActualDate(PoiUtil.getStringCellValue(sheet, PLAN_ACTUAL_INBOUND_ROW, i));
            // Reason for Last Change
            planColumnEntity.setRevisionReasonLastChange(PoiUtil.getStringCellValue(sheet, PLAN_REASON_LAST_ROW, i));
            // Reason for This Change
            planColumnEntity.setRevisionReasonThisChange(PoiUtil.getStringCellValue(sheet, PLAN_REASON_THIS_ROW, i));
            // Reason Version
            planColumnEntity.setRevisionVersionFile(revisionVersionFile);
            // Row Qty Data
            List<String[]> rowQtyData = new ArrayList<String[]>();
            for (int j = PLAN_INFO_END_ROW + 1; j <= sheet.getLastRowNum(); j++) {
                int readNextRowOrStopFlag = readNextRowOrStop(sheet, j, language);
                if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.READ_NEXT) {
                    continue;
                } else if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.STOP) {
                    break;
                }
                rowQtyData.add(new String[] { String.valueOf(j), PoiUtil.getStringCellValue(sheet, j, i) });
            }
            planColumnEntity.setRowDataList(rowQtyData);

            // modify for UAT
            // if (revisionVersionFile.indexOf(labelActual) > -1 && revisionVersionFile.indexOf(labelActualVan) > -1) {
            if (revisionVersionFile.indexOf(labelActual) > -1
                    && (revisionVersionFile.indexOf(labelActualVan) > -1 || !StringUtil.isEmpty(planColumnEntity
                        .getInvoiceNo()))) {
                // modify end
                // Current is Actual Van column.
                planColumnEntity.setColumnTypeAll(ColumnTypeAll.ACTUAL_VAN);
                listColActualVanData.add(planColumnEntity);
            } else if (revisionVersionFile.indexOf(labelActual) > -1) {
                // Current is Actual column.
                planColumnEntity.setColumnTypeAll(ColumnTypeAll.ACTUAL);
                listColActualData.add(planColumnEntity);
            } else if (revisionVersionFile.indexOf(labelPlan) > -1 || revisionVersionFile.indexOf(labelPcs) > -1) {
                // Current is Shipping Plan column or Shipping Plan NEW column.
                if (StringUtils.isBlank(title)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.PLAN);
                    planColumnEntity.setListDiff(listDiff);
                    planColumnEntity.setListBox(listBox);
                    planColumnEntity.setListDiffMod(listDiffMod);
                    planColumnEntity.setListBoxMod(listBoxMod);
                    listDiff = new ArrayList<Integer>();
                    listBox = new ArrayList<Integer>();
                    listDiffMod = new ArrayList<Integer>();
                    listBoxMod = new ArrayList<Integer>();
                    listColPlanData.add(planColumnEntity);
                } else if (title.equalsIgnoreCase(labelMod)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.PLAN_MOD);
                    planColumnEntity.setRowDataForUpdateList(getRowDataForUpdate(
                        listAllColData.get(listAllColData.size() - 1).getRowDataList(), rowQtyData));
                    listColPlanData.add(planColumnEntity);
                } else if (title.equalsIgnoreCase(labelNew)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.PLAN_NEW);
                    listColPlanNewData.add(planColumnEntity);
                } else {
                    throwFileFormatError();
                }
            } else if (revisionVersionFile.indexOf(labelBox) > -1) {
                // Current is Shipping Plan Box column.
                if (StringUtils.isBlank(title)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.BOX_DATA);
                    listBox.add(planColumnEntity.getColNo());
                } else if (title.equalsIgnoreCase(labelMod)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.BOX_MOD);
                    planColumnEntity.setRowDataForUpdateList(getRowDataForUpdate(
                        listAllColData.get(listAllColData.size() - 1).getRowDataList(), rowQtyData));
                    listBox.add(planColumnEntity.getColNo());
                    listBoxMod.add(planColumnEntity.getColNo());
                } else {
                    throwFileFormatError();
                }
                mapColBoxData.put(planColumnEntity.getColNo(), planColumnEntity);
            } else if (revisionVersionFile.indexOf(labelDifference) > -1) {
                // Current is Shipping Plan Difference column.
                if (StringUtils.isBlank(title)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.DIFFERENCE_DATA);
                    listDiff.add(planColumnEntity.getColNo());
                } else if (title.equalsIgnoreCase(labelMod)) {
                    planColumnEntity.setColumnTypeAll(ColumnTypeAll.DIFFERENCE_MOD);
                    planColumnEntity.setRowDataForUpdateList(getRowDataForUpdate(
                        listAllColData.get(listAllColData.size() - 1).getRowDataList(), rowQtyData));
                    listDiff.add(planColumnEntity.getColNo());
                    listDiffMod.add(planColumnEntity.getColNo());
                } else {
                    throwFileFormatError();
                }
                mapColDiffData.put(planColumnEntity.getColNo(), planColumnEntity);
            } else {
                throwFileFormatError();
            }
            listAllColData.add(planColumnEntity);
        }

        // File format check of [Force Complete Qty] title
        int colNoForceCompleteQty = colNoQtyWithInvoice + NumberConst.IntDef.INT_FOUR;
        if (!labelForCompQty.equalsIgnoreCase(PoiUtil.getStringCellValue(sheet, PLAN_INFO_END_ROW,
            colNoForceCompleteQty))) {
            throwFileFormatError();
        }
        // File format check of [Order Forecast 1 - 6] title
        int colNoStockTransferOut = 0;
        for (int i = 1; i <= NumberConst.IntDef.INT_SEVEN; i++) {
            int colNo = i + colNoForceCompleteQty;
            if (labelStockTranOut.equalsIgnoreCase(PoiUtil.getStringCellValue(sheet, PLAN_INFO_END_ROW, colNo))) {
                colNoStockTransferOut = colNo;
                break;
            } else if (!MessageManager.getMessage("CPKKPF12_Grid_OrderForecast" + String.valueOf(i), language)
                .equalsIgnoreCase(PoiUtil.getStringCellValue(sheet, PLAN_INFO_END_ROW, colNo))) {
                throwFileFormatError();
            }
        }

        // 4. Get parts Force Complete Qty and Order Forecast 1 - 6 data
        for (int i = PLAN_INFO_END_ROW + 1; i <= sheet.getLastRowNum(); i++) {
            int readNextRowOrStopFlag = readNextRowOrStop(sheet, i, language);
            if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.READ_NEXT) {
                continue;
            } else if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.STOP) {
                break;
            }

            CPKKPF12RowFfEntity planRowFfEntity = new CPKKPF12RowFfEntity();
            planRowFfEntity.setRowNo(i);
            planRowFfEntity.setForceCompletedQty(PoiUtil.getStringCellValue(sheet, i, colNoForceCompleteQty));
            int forecastNum = 0;
            for (int j = 1; j < colNoStockTransferOut - colNoForceCompleteQty; j++) {
                String fcQty = PoiUtil.getStringCellValue(sheet, i, colNoForceCompleteQty + j);
                if (!StringUtils.isBlank(fcQty)) {
                    forecastNum++;
                }
                switch (j) {
                    case NumberConst.IntDef.INT_ONE:
                        planRowFfEntity.setFcQty1(fcQty);
                        break;
                    case NumberConst.IntDef.INT_TWO:
                        planRowFfEntity.setFcQty2(fcQty);
                        break;
                    case NumberConst.IntDef.INT_THREE:
                        planRowFfEntity.setFcQty3(fcQty);
                        break;
                    case NumberConst.IntDef.INT_FOUR:
                        planRowFfEntity.setFcQty4(fcQty);
                        break;
                    case NumberConst.IntDef.INT_FIVE:
                        planRowFfEntity.setFcQty5(fcQty);
                        break;
                    case NumberConst.IntDef.INT_SIX:
                        planRowFfEntity.setFcQty6(fcQty);
                        break;
                }
            }
            planRowFfEntity.setForecastNum(forecastNum);
            mapRowFfData.put(i, planRowFfEntity);
        }

        // 5. Get plan nird data (include data column and nird new column)
        for (int i = sheet.getRow(PLAN_INFO_END_ROW - 1).getLastCellNum(); i > 0; i--) {
            String title = PoiUtil.getStringCellValue(sheet, 1, i);
            String etdDelay = PoiUtil.getStringCellValue(sheet, NumberConst.IntDef.INT_SEVEN, i);
            String transportationMode = PoiUtil.getStringCellValue(sheet, NIRD_TRANSPORT_MODE_ROW, i);
            String revisionVersionFile = PoiUtil.getStringCellValue(sheet, PLAN_REASON_VERSION_ROW, i).toUpperCase();

            if (StringUtils.isBlank(title) && StringUtils.isBlank(etdDelay) && StringUtils.isBlank(transportationMode)
                    && StringUtils.isBlank(revisionVersionFile)) {
                continue;
            } else if (labelDiscoIndi.equalsIgnoreCase(revisionVersionFile)) {
                break;
            }

            if (labelTransMode.equalsIgnoreCase(transportationMode) && StringUtils.isBlank(title)
                    && StringUtils.isBlank(etdDelay)) {
                // If current column's value is "Transportation Mode", stop read.
                break;
            } else if (StringUtils.isBlank(title) && StringUtils.isBlank(etdDelay)) {
                // If current column's title and "etdDelay" is blank, read the next column.
                continue;
            } else if (isBlankCol(sheet, i, NIRD_TRANSPORT_MODE_ROW, listRowData)) {
                // If current column(from "Transportation Mode" to the last row data) is blank, read the next column.
                continue;
            }

            String etd = PoiUtil.getStringCellValue(sheet, NIRD_ETD_ROW, i);
            String eta = PoiUtil.getStringCellValue(sheet, NIRD_ETA_ROW, i);
            String impInboundDate = PoiUtil.getStringCellValue(sheet, NIRD_IMP_INBOUND_DATE_ROW, i);
            String revisionReason = PoiUtil.getStringCellValue(sheet, NIRD_REVISON_REASON_ROW, i);

            CPKKPF12ColEntity planColumnNirdEntity = new CPKKPF12ColEntity();
            planColumnNirdEntity.setColNo(i);
            planColumnNirdEntity.setTransportMode(transportationMode);
            planColumnNirdEntity.setEtd(etd);
            planColumnNirdEntity.setEta(eta);
            planColumnNirdEntity.setImpInbPlanDate(impInboundDate);
            planColumnNirdEntity.setRevisionReason(revisionReason);

            // Row Qty Data
            List<String[]> rowQtyData = new ArrayList<String[]>();
            for (int j = PLAN_INFO_END_ROW + 1; j <= sheet.getLastRowNum(); j++) {
                int readNextRowOrStopFlag = readNextRowOrStop(sheet, j, language);
                if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.READ_NEXT) {
                    continue;
                } else if (readNextRowOrStopFlag == ReadNextRowOrStopFlag.STOP) {
                    break;
                }
                rowQtyData.add(new String[] { String.valueOf(j), PoiUtil.getStringCellValue(sheet, j, i) });
            }
            planColumnNirdEntity.setRowDataList(rowQtyData);

            if (StringUtils.isBlank(title)) {
                planColumnNirdEntity.setColumnTypeAll(ColumnTypeAll.PLAN_NIRD_DATA);
                listColNirdData.add(planColumnNirdEntity);
            } else if (title.equalsIgnoreCase(labelNew)) {
                planColumnNirdEntity.setColumnTypeAll(ColumnTypeAll.PLAN_NIRD_NEW);
                listColNirdNewData.add(planColumnNirdEntity);
            } else {
                throwFileFormatError();
            }
        }

        for (int i = listColNirdData.size() - 1; i > -1; i--) {
            listAllColData.add(listColNirdData.get(i));
        }
        for (int i = listColNirdNewData.size() - 1; i > -1; i--) {
            listAllColData.add(listColNirdNewData.get(i));
        }

        context.put(SESSION_KANBAN_PLAN_NO, entity);
        context.put(SESSION_ROW, listRowData);
        context.put(SESSION_ROW_FF, mapRowFfData);
        context.put(SESSION_COL_ACTUAL, listColActualData);
        context.put(SESSION_COL_ACTUAL_VAN, listColActualVanData);
        context.put(SESSION_COL_PLAN, listColPlanData);
        context.put(SESSION_COL_PLAN_NEW, listColPlanNewData);
        context.put(SESSION_COL_DIF, mapColDiffData);
        context.put(SESSION_COL_BOX, mapColBoxData);
        context.put(SESSION_COL_NIRD, listColNirdData);
        context.put(SESSION_COL_NIRD_NEW, listColNirdNewData);
        context.put(SESSION_COL_ALL, listAllColData);
    }

    /**
     * Get row data for update.
     * 
     * @param rowQtyDataCol row qty of column Data
     * @param rowQtyModCol row qty of column MOD
     * @return row data for update
     */
    private List<String[]> getRowDataForUpdate(List<String[]> rowQtyDataCol, List<String[]> rowQtyModCol) {
        List<String[]> rowQtyForUpdate = new ArrayList<String[]>();
        for (int i = 0; i < rowQtyModCol.size(); i++) {
            if (StringUtils.isBlank(rowQtyModCol.get(i)[1])) {
                rowQtyForUpdate.add(rowQtyDataCol.get(i));
            } else {
                rowQtyForUpdate.add(rowQtyModCol.get(i));
            }
        }
        return rowQtyForUpdate;
    }

    /**
     * Is blank column (plan + qty info of current column).
     * 
     * @param sheet Sheet
     * @param colNo col No.
     * @param startRowNo start row No.
     * @param listPlanRowData row data
     * @param skipRowNo skip row No.
     * @return true:is blank col/false:is not blank
     */
    private boolean isBlankCol(Sheet sheet, int colNo, int startRowNo, List<CPKKPF12RowEntity> listPlanRowData,
        int... skipRowNo) {
        return ValidatorUtils.isBlankColFromTo(sheet, colNo, startRowNo, listPlanRowData
            .get(listPlanRowData.size() - 1).getRowNo(), skipRowNo);
    }

    /**
     * Read next row or stop.
     * 
     * @param sheet Sheet
     * @param rowNo row No.
     * @param language language
     * @return 0:do nothing/1:read next row/2:stop
     */
    private int readNextRowOrStop(Sheet sheet, int rowNo, Locale language) {
        if (ValidatorUtils.isBlankRowFromTo(sheet, rowNo, PARTS_INFO_START_COL, sheet.getRow(PLAN_INFO_END_ROW - 1)
            .getLastCellNum())) {
            // If current row is blank row, read the next row.
            return ReadNextRowOrStopFlag.READ_NEXT;
        } else if (PoiUtil.getStringCellValue(sheet, rowNo, PARTS_INFO_START_COL).indexOf(
            MessageManager.getMessage("CPKKPF12_Label_DownloadTime", language)) > -1) {
            // If current row is "download time" row, stop read.
            return ReadNextRowOrStopFlag.STOP;
        }
        return ReadNextRowOrStopFlag.DO_NOTHING;
    }

    /**
     * ReadNextRowOrStopFlag.
     */
    private interface ReadNextRowOrStopFlag {

        /** DO_NOTHING */
        final static int DO_NOTHING = 0;
        /** READ_NEXT */
        final static int READ_NEXT = 1;
        /** STOP */
        final static int STOP = 2;
    }

    /**
     * MergeFlag.
     */
    private interface MergeFlag {

        /** NOT_MERGED */
        final static int NOT_MERGED = 0;
    }

    /**
     * ColumnTypeAll.
     */
    private interface ColumnTypeAll {

        /** Actual Column */
        final static int ACTUAL = 0;
        /** Actual Van Column */
        final static int ACTUAL_VAN = 1;
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
        /** Plan New Column */
        final static int PLAN_NEW = 9;
        /** Plan Nird Data Column */
        final static int PLAN_NIRD_DATA = 10;
        /** Plan Nird New Column */
        final static int PLAN_NIRD_NEW = 11;
    }

    /**
     * Throw File Format Error
     */
    private void throwFileFormatError() {
        List<BaseMessage> messageList = new ArrayList<BaseMessage>();
        BaseMessage message = new BaseMessage(MessageCodeConst.W1014);
        messageList.add(message);
        throw new BusinessException(messageList);
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {
        return FileId.CPKKPF12;
    }
}
