/**
 * CPKKPF11Controller.java
 * 
 * @screen CPKKPF11
 * @author shiyang
 */
package com.chinaplus.web.kbp.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.ChainStep;
import com.chinaplus.common.entity.TnmPartsMaster;
import com.chinaplus.common.service.SupplyChainService;
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
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.kbp.entity.CPKKPF11Entity;
import com.chinaplus.web.kbp.service.CPKKPF11Service;
import com.chinaplus.web.kbp.service.CPKKPF11UpdateService;

/**
 * Upload Monthly Kanban Plan Controller.
 */
@Controller
public class CPKKPF11Controller extends BaseFileController {

    /** PARTS_HEADER_END_COL */
    private static final int PARTS_HEADER_END_COL = 14;

    /** PARTS_STRAT_ROW */
    private static final int PARTS_STRAT_ROW = 5;

    /** ISSUED_PLAN_DATE_ROW */
    private static final int ISSUED_PLAN_DATE_ROW = 2;

    /** DELIVER_DATE_TO_OBU_ROW */
    private static final int DELIVER_DATE_TO_OBU_ROW = 3;

    /** DATA_START_COL */
    private static final int DATA_START_COL = 15;

    /** COL_NO */
    private static final int COL_NO = 3;

    /** SESSION_KEY_ISSUED_PLAN_DATE_LIST */
    private static final String SESSION_KEY_ISSUED_PLAN_DATE_LIST = "CPKKPF11_IssuedPlanDateList";

    /** SESSION_KEY_DELIVER_DATE_TO_OBU_LIST */
    private static final String SESSION_KEY_DELIVER_DATE_TO_OBU_LIST = "CPKKPF11_DeliverDateToObuList";

    /** SESSION_KEY_ROW_DATA_LIST */
    private static final String SESSION_KEY_ROW_DATA_LIST = "CPKKPF11_RowDataList";

    /** SESSION_KEY_FILE */
    private static final String SESSION_KEY_FILE = "CPKKPF11_File";

    /** SCALE_KANBAN_QTY */
    private static final int SCALE_KANBAN_QTY = 2;

    /** Upload Monthly Kanban Plan Service */
    @Autowired
    private CPKKPF11Service cpkkpf11Service;

    /** Upload Monthly Kanban Plan Service */
    @Autowired
    private CPKKPF11UpdateService cpkkpf11UpdateService;

    /** Supply Chain Service */
    @Autowired
    private SupplyChainService supplyChainService;

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(CPKKPF11Controller.class);

    /**
     * Do Upload
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/kbp/CPKKPF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        MultipartFile uploadFile = file;

        super.setCommonParam(param, request);

        List<String[]> issuedPlanDateList = new ArrayList<String[]>();
        List<String[]> deliverDateToObuList = new ArrayList<String[]>();
        List<CPKKPF11Entity> rowDataList = new ArrayList<CPKKPF11Entity>();

        // Check the upload file whether has more than one sheets.(w1004_024)
        // Check key column header names and positions whether is correct: ALL columns.(w1014)
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            super.uploadFileProcess(file, FileType.EXCEL, param, request, response);
            Workbook workbook = ExcelUtil.getWorkBook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Read excel data
            readExcel(sheet, issuedPlanDateList, deliverDateToObuList, rowDataList);
            context.put(SESSION_KEY_ISSUED_PLAN_DATE_LIST, issuedPlanDateList);
            context.put(SESSION_KEY_DELIVER_DATE_TO_OBU_LIST, deliverDateToObuList);
            context.put(SESSION_KEY_ROW_DATA_LIST, rowDataList);
            context.put(SESSION_KEY_FILE, uploadFile.getInputStream());
        } else {
            issuedPlanDateList = (List<String[]>) context.get(SESSION_KEY_ISSUED_PLAN_DATE_LIST);
            deliverDateToObuList = (List<String[]>) context.get(SESSION_KEY_DELIVER_DATE_TO_OBU_LIST);
            rowDataList = (List<CPKKPF11Entity>) context.get(SESSION_KEY_ROW_DATA_LIST);
        }

        CPKKPF11Entity entity = doUploadProcess(param, request, issuedPlanDateList, deliverDateToObuList, rowDataList);

        if ("0".equals(entity.getUploadResult())) {
            context.remove(SESSION_KEY_ISSUED_PLAN_DATE_LIST);
            context.remove(SESSION_KEY_DELIVER_DATE_TO_OBU_LIST);
            context.remove(SESSION_KEY_ROW_DATA_LIST);
            context.remove(SESSION_KEY_FILE);
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
     * @param issuedPlanDateList Issued plan date list ([0]:colNo; [1]:Issued plan date)
     * @param deliverDateToObuList Deliver date to Obu list ([0]:colNo; [1]:Deliver date to Obu)
     * @param rowDataList Row data list
     * @return CPKKPF11Entity (UploadResult: 0:update sucessful/2:have 2confime message/confime message id)
     */
    protected CPKKPF11Entity doUploadProcess(BaseParam param, HttpServletRequest request,
        List<String[]> issuedPlanDateList, List<String[]> deliverDateToObuList, List<CPKKPF11Entity> rowDataList) {

        List<BaseMessage> messageList = new ArrayList<BaseMessage>();
        List<BaseMessage> messageConfirmList = new ArrayList<BaseMessage>();

        // Get session's CURRENT_CUSTOMER_CODE
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        param.setSwapData("CURRENT_CUSTOMER_CODE", um.getCurrentBusPattern());

        // Check input
        CPKKPF11Entity entity = checkInput(messageList, param, issuedPlanDateList, deliverDateToObuList, rowDataList);
        if (messageList.size() > 0) {
            throw new BusinessException(messageList);
        }

        // Check business logic
        List<CPKKPF11Entity> partsMasterInfoList = new ArrayList<CPKKPF11Entity>();
        List<CPKKPF11Entity> supplyChainList = new ArrayList<CPKKPF11Entity>();
        checkBusinessLogic(messageList, messageConfirmList, issuedPlanDateList, deliverDateToObuList, rowDataList,
            partsMasterInfoList, supplyChainList, entity);
        if (messageList.size() > 0) {
            throw new BusinessException(messageList);
        }

        if (messageConfirmList.size() > 0 && UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            if (messageConfirmList.size() > 1) {
                entity.setUploadResult("2");
            } else {
                entity.setUploadResult(messageConfirmList.get(0).getMessageCode());
            }
            return entity;
        } else {
            // Update logic
            cpkkpf11UpdateService.doUpdateLogic(param, entity, issuedPlanDateList, deliverDateToObuList, rowDataList,
                partsMasterInfoList, supplyChainList, request);
            entity.setUploadResult("0");
            return entity;
        }
    }

    /**
     * Parts master info list -> Parts master info map (key: File.Row.Customer Parts No.)
     * 
     * @param partsMasterInfoList parts master info list
     * @param partsMasterInfoMap parts master info map
     */
    private void getPartsMasterInfoMap(List<CPKKPF11Entity> partsMasterInfoList,
        HashMap<String, CPKKPF11Entity> partsMasterInfoMap) {
        for (CPKKPF11Entity partsMasterInfo : partsMasterInfoList) {
            partsMasterInfoMap.put(partsMasterInfo.getSuppPartsNo(), partsMasterInfo);
        }
    }

    /**
     * Check business logic.
     * 
     * @param messageList message list
     * @param messageConfirmList confirm message list
     * @param issuedPlanDateList Issued plan date list ([0]:colNo; [1]:Issued plan date)
     * @param deliverDateToObuList Deliver date to Obu list ([0]:colNo; [1]:Deliver date to Obu)
     * @param rowDataList row data list
     * @param partsMasterInfoList parts master info list
     * @param supplyChainList supply chain list
     * @param condition (kanbanId is blank: Kanban Plan is not exist; kanbanId is not blank: Kanban Plan is exist)
     */
    private void checkBusinessLogic(List<BaseMessage> messageList, List<BaseMessage> messageConfirmList,
        List<String[]> issuedPlanDateList, List<String[]> deliverDateToObuList, List<CPKKPF11Entity> rowDataList,
        List<CPKKPF11Entity> partsMasterInfoList, List<CPKKPF11Entity> supplyChainList, CPKKPF11Entity condition) {
        // Do business logic(db) check.
        condition.setExpCustCode(rowDataList.get(0).getExpCustCode());

        // Select all parts in Parts Master
        partsMasterInfoList.addAll(cpkkpf11Service.getPartsMasterInfo(condition));

        // Based on Issued plan date range find order month.
        // If can not find order month in Master.(w1004_039)
        if (partsMasterInfoList != null && partsMasterInfoList.size() > 0) {
            condition.setCustomerId(partsMasterInfoList.get(0).getCustomerId());
            String orderMonth = cpkkpf11Service.getOrderMonth(condition);
            if (orderMonth == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_039);
                messageList.add(message);
            }
            condition.setOrderMonth(orderMonth);
            condition.setTtcSuppCode(partsMasterInfoList.get(0).getTtcSuppCode());
            condition.setCustomerCode(partsMasterInfoList.get(0).getCustomerCode());
        }

        // Loop all row data
        boolean differentSrbqExistFlag = false;
        boolean allCustomerPartsNoExistFlag = true;
        for (int i = 0; i < rowDataList.size(); i++) {
            CPKKPF11Entity entity = rowDataList.get(i);

            String rowNo = String.valueOf(entity.getRowNo());
            String suppPartsNo = entity.getSuppPartsNo();
            BigDecimal spq = DecimalUtil.getBigDecimal(entity.getSpq());

            // Loop all parts in Parts Master
            boolean customerPartsNoExistFlag = false;
            for (CPKKPF11Entity entityPartsInfo : partsMasterInfoList) {
                String suppPartsNoCompare = entityPartsInfo.getSuppPartsNo();

                if (cpkkpf11UpdateService.cutMiddleLineWithFront(suppPartsNo).equals(suppPartsNoCompare)) {
                    customerPartsNoExistFlag = true;
                    entity.setPartsId(entityPartsInfo.getPartsId());

                    int inactiveFlag = entityPartsInfo.getInactiveFlag();
                    if (inactiveFlag == CodeConst.InactiveFlag.INACTIVE) {
                        // If part has been discontinued(INACTIVE_FLAG = '1') in Master.(w1004_032)
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_032);
                        message.setMessageArgs(new String[] { rowNo });
                        messageList.add(message);
                    }

                    BigDecimal srbq = entityPartsInfo.getSrbq();
                    if (spq.compareTo(srbq) != 0) {
                        // If part Qty/Box and Parts Master's SRBQ are not the same.(c1005)
                        differentSrbqExistFlag = differentSrbqExistFlag || true;
                    }

                    // Check Qty/Box
                    checkQtyCommon(messageList, entity.getSpq(), rowNo, "CPKKPF11_Label_Qty_Box",
                        entityPartsInfo.getDigits());

                    // Check Order Qty
                    checkQtyCommon(messageList, entity.getOrder(), rowNo, "CPKKPF11_Label_Order",
                        entityPartsInfo.getDigits());

                    // Check Total Kanban Qty
                    checkQtyCommon(messageList, entity.getKanbanQty(), rowNo, "CPKKPF11_Label_Kanban_Qty",
                        SCALE_KANBAN_QTY);
                    break;
                }
            }

            if (!customerPartsNoExistFlag) {
                allCustomerPartsNoExistFlag = allCustomerPartsNoExistFlag && false;
                // Check if the part exist in (4-1)'s result, if does not exist.(w1004_031)
                // Condition: Input File's Customer Parts No.(Cut off front "-") = (4-1)'s B.SUPP_PARTS_NO
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_031);
                message.setMessageArgs(new String[] { rowNo });
                messageList.add(message);
            }
        }
        // If part Qty/Box and Parts Master's SRBQ are not the same.(c1005)
        if (differentSrbqExistFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.C1005);
            messageConfirmList.add(message);
        }
        // If all parts are exist, get plan data.
        if (allCustomerPartsNoExistFlag) {
            // Loop Issued plan date list
            HashMap<String, CPKKPF11Entity> partsMasterInfoMap = new HashMap<String, CPKKPF11Entity>();
            getPartsMasterInfoMap(partsMasterInfoList, partsMasterInfoMap);
            for (int i = 0; i < issuedPlanDateList.size(); i++) {
                String[] deliverDateToObuInfo = deliverDateToObuList.get(i);
                Date deliverDateToObu = DateTimeUtil.parseDate(deliverDateToObuInfo[1]);

                // Based on Deliver Date to Obu find shipping route.
                if (!isAllKanbanQtyEmpty(rowDataList, Integer.parseInt(deliverDateToObuInfo[0]))) {
                    String[] issuedPlanDateInfo = issuedPlanDateList.get(i);
                    Date issuedPlanDate = DateTimeUtil.parseDate(issuedPlanDateInfo[1]);

                    for (CPKKPF11Entity rowData : rowDataList) {
                        List<String[]> rowKanbanQty = rowData.getRowKanbanQty();
                        for (String[] row : rowKanbanQty) {
                            if (row[0].equals(deliverDateToObuInfo[0]) && !StringUtils.isBlank(row[1])) {
                                CPKKPF11Entity supplyChainEntity = new CPKKPF11Entity();
                                supplyChainEntity.setColNo(Integer.parseInt(deliverDateToObuInfo[0]));
                                supplyChainEntity.setRowNo(rowData.getRowNo());
                                supplyChainEntity.setChainStartDate(deliverDateToObu);
                                supplyChainEntity.setIssuedDate(issuedPlanDate);
                                supplyChainEntity.setDeliveredDate(deliverDateToObu);
                                supplyChainEntity.setTansportMode(CodeConst.TransportMode.SEA);
                                supplyChainEntity.setExpPartsId(partsMasterInfoMap.get(
                                    cpkkpf11UpdateService.cutMiddleLineWithFront(rowData.getSuppPartsNo()))
                                    .getExpPartsId());
                                supplyChainEntity.setIssuedPlanDateIndex(i);
                                supplyChainList.add(supplyChainEntity);
                            }
                        }
                    }
                }
            }

            // logger.info("input start===============================================================");
            // for (CPKKPF11Entity supplyChainEntity : supplyChainList) {
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // logger.info(sdf.format(supplyChainEntity.getChainStartDate())
            // + "; " + sdf.format(supplyChainEntity.getIssuedDate())
            // + "; " + sdf.format(supplyChainEntity.getDeliveredDate())
            // + "; " + supplyChainEntity.getTansportMode()
            // + "; " + supplyChainEntity.getExpPartsId());
            // }
            // logger.info("input end===============================================================");

            // Based on Deliver Date to Obu find shipping route.
            supplyChainService.prepareSupplyChain(supplyChainList, ChainStep.PLAN, BusinessPattern.AISIN, false);
            for (CPKKPF11Entity supplyChainEntity : supplyChainList) {
                if ((supplyChainEntity.getEtd() == null || supplyChainEntity.getEta() == null || supplyChainEntity
                    .getImpPlanInboundDate() == null)) {
                    // If can not find shipping route in Master.(w1004_040)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_040);
                    message.setMessageArgs(new String[] { String.valueOf(supplyChainEntity.getRowNo()),
                        ExcelUtil.colIndexToStr(supplyChainEntity.getColNo()) });
                    messageList.add(message);
                }
            }
        }

        // 3. Do Kanban exist check.
        if (condition.getOrderMonth() != null) {
            CPKKPF11Entity kanbanPlanInfo = cpkkpf11Service.getKanbanPlanInfo(condition);
            if (kanbanPlanInfo == null) {
                // If Kanban Plan data is not exist
                // If {Parameter.Revision Reason} is not empty, then show warnning message.(w1001_008)
                if (!StringUtils.isBlank(condition.getRevisionReason())) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1001_008);
                    messageList.add(message);
                }
            } else {
                condition.setKanbanId(kanbanPlanInfo.getKanbanId());
                condition.setKanbanPlanNo(kanbanPlanInfo.getKanbanPlanNo());
                condition.setRevisionVersion(kanbanPlanInfo.getRevisionVersion());
                condition.setAirFlag(kanbanPlanInfo.getAirFlag());

                if (StringUtils.isBlank(condition.getRevisionReason())) {
                    // If Kanban Plan data is already exist
                    // If {Parameter.Revision Reason} is empty, show warnning message, action end.(w1001_005)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
                    message.setMessageArgs(new String[] { "CPKKPF11_Label_RevisionReason" });
                    messageList.add(message);
                } else {
                    for (CPKKPF11Entity rowDataInfo : rowDataList) {
                        rowDataInfo.setKanbanPlanNo(kanbanPlanInfo.getKanbanPlanNo());
                        // If part is already with actual invoice in the exist Kanban Plan.(w1004_042)
                        if (cpkkpf11Service.existInvoiceInfo(rowDataInfo)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_042);
                            message.setMessageArgs(new String[] { String.valueOf(rowDataInfo.getRowNo()) });
                            messageList.add(message);
                        }
                    }

                    BaseMessage message = new BaseMessage(MessageCodeConst.C1006);
                    messageConfirmList.add(message);
                }
            }
        }
    }

    /**
     * Check input.
     * 
     * @param messageList message list
     * @param param base param
     * @param issuedPlanDateList Issued plan date list ([0]:colNo; [1]:Issued plan date)
     * @param deliverDateToObuList Deliver date to Obu list ([0]:colNo; [1]:Deliver date to Obu)
     * @param rowDataList Row data list
     * @return CPKKPF11Entity (kanbanId is blank: Kanban Plan is not exist; kanbanId is not blank: Kanban Plan is exist)
     */
    @SuppressWarnings("unchecked")
    private CPKKPF11Entity checkInput(List<BaseMessage> messageList, BaseParam param,
        List<String[]> issuedPlanDateList, List<String[]> deliverDateToObuList, List<CPKKPF11Entity> rowDataList) {
        CPKKPF11Entity condition = new CPKKPF11Entity();
        condition.setOfficeId(param.getCurrentOfficeId());
        condition.setSupplierId(Integer.parseInt((String) param.getSwapData().get("supplierId")));
        condition.setRevisionCodeSet((String) param.getSwapData().get("revisionCodeSet"));
        condition.setRevisionReason((String) param.getSwapData().get("revisionReason"));

        // Check Issued plan date list
        checkDateRowCommon(messageList, param, ISSUED_PLAN_DATE_ROW, "CPKKPF11_Label_Issued_Plan_Date",
            issuedPlanDateList);

        // Check Deliver date to Obu list
        checkDateRowCommon(messageList, param, DELIVER_DATE_TO_OBU_ROW, "CPKKPF11_Label_Deliver_Date_To_Obu",
            deliverDateToObuList);

        boolean issuedPlanDateCompleteRangeFlag = true;
        for (int i = 0; i < issuedPlanDateList.size(); i++) {
            String[] issuedPlanDateInfo = issuedPlanDateList.get(i);
            String[] deliverDateToObuInfo = deliverDateToObuList.get(i);

            Date issuedPlanDate = DateTimeUtil.parseDate(issuedPlanDateInfo[1]);
            Date deliverDateToObu = DateTimeUtil.parseDate(deliverDateToObuInfo[1]);

            // If Deliver date to Obu < Issued plan date.(w1004_036)
            if (issuedPlanDate != null && deliverDateToObu != null && issuedPlanDate.compareTo(deliverDateToObu) > 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_036);
                message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(issuedPlanDateInfo[0]) });
                messageList.add(message);
            }

            if (i != 0) {
                // If Issued plan date is not a complete or not a repeat range.(w1004_038)
                if (issuedPlanDate == null) {
                    issuedPlanDateCompleteRangeFlag = issuedPlanDateCompleteRangeFlag && false;
                } else {
                    Date issuedPlanDatePre = DateTimeUtil.parseDate(issuedPlanDateList.get(i - 1)[1]);
                    if (issuedPlanDatePre == null) {
                        issuedPlanDateCompleteRangeFlag = issuedPlanDateCompleteRangeFlag && false;
                    } else {
                        if (DateTimeUtil.addDay(issuedPlanDatePre, 1).compareTo(issuedPlanDate) != 0) {
                            issuedPlanDateCompleteRangeFlag = issuedPlanDateCompleteRangeFlag && false;
                        }
                    }
                }
            }

            if (i == 0) {
                condition.setFromDate(issuedPlanDate);
            } else if (i == issuedPlanDateList.size() - 1) {
                condition.setToDate(issuedPlanDate);
            }
        }
        // If Issued plan date is not a complete or not a repeat range.(w1004_038)
        if (!issuedPlanDateCompleteRangeFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_038);
            messageList.add(message);
        }

        // Check row input data
        if (rowDataList == null || rowDataList.size() < 1) {
            // If no row data, error
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageList.add(message);
        } else {
            boolean moreThanOneCustomerFlag = false;
            HashMap<String, String> checkMoreThanOneCustomerMap = new HashMap<String, String>();
            HashMap<String, String> checkSamePartsMap = new HashMap<String, String>();
            for (CPKKPF11Entity entity : rowDataList) {
                String rowNo = String.valueOf(entity.getRowNo());
                String customer = entity.getExpCustCode();
                String suppPartsNo = entity.getSuppPartsNo();
                String order = entity.getOrder();
                String spq = entity.getSpq();
                String kanbanQtyTotal = entity.getKanbanQty();
                BigDecimal bdOrder = DecimalUtil.getBigDecimal(order);
                BigDecimal bdSpq = DecimalUtil.getBigDecimal(spq);
                BigDecimal bdKanbanQtyTotal = DecimalUtil.getBigDecimal(kanbanQtyTotal);
                List<String[]> rowKanbanQtyList = entity.getRowKanbanQty();

                if (StringUtils.isBlank(suppPartsNo)) {
                    // If Customer Parts No. is blank.(w1004_001)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { rowNo, "CPKKPF11_Label_Customer_Parts_No" });
                    messageList.add(message);
                } else {
                    // If there is duplicate Part in Kanban file.(w1004_037)
                    // (Rule: If A, B and C is the same, error B and C.)
                    if (checkSamePartsMap.containsKey(suppPartsNo)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_037);
                        message.setMessageArgs(new String[] { rowNo, suppPartsNo });
                        messageList.add(message);
                    } else {
                        checkSamePartsMap.put(suppPartsNo, suppPartsNo);
                    }
                }

                if (StringUtils.isBlank(customer)) {
                    // If Customer is blank.(w1004_001)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { rowNo, "CPKKPF11_Label_Customer" });
                    messageList.add(message);
                } else {
                    // If there is more than one customer in the Kanban file.(w1004_041)
                    if (checkMoreThanOneCustomerMap.size() < 1) {
                        checkMoreThanOneCustomerMap.put(customer, customer);
                    } else if (!checkMoreThanOneCustomerMap.containsKey(customer)) {
                        moreThanOneCustomerFlag = moreThanOneCustomerFlag || true;
                    }
                }

                // Check Qty/Box
                checkQtyCommon(messageList, spq, rowNo, "CPKKPF11_Label_Qty_Box");

                // Check Order Qty
                checkQtyCommon(messageList, order, rowNo, "CPKKPF11_Label_Order");

                // Check Total Kanban Qty
                checkQtyCommon(messageList, kanbanQtyTotal, rowNo, "CPKKPF11_Label_Kanban_Qty");

                // If Qty/Box * Total Kanban Qty != Order.(w1004_034)
                if (bdOrder.compareTo(bdSpq.multiply(bdKanbanQtyTotal)) != 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_034);
                    message.setMessageArgs(new String[] { String.valueOf(rowNo) });
                    messageList.add(message);
                }

                // Check Row Kanban Qty
                BigDecimal rowKanbanQtyTotal = BigDecimal.ZERO;
                for (String[] kanbanQtyInfo : rowKanbanQtyList) {
                    String kanbanQty = kanbanQtyInfo[1];

                    if (!StringUtils.isBlank(kanbanQty)) {
                        String colNo = kanbanQtyInfo[0];

                        BigDecimal bdKanbanQty = DecimalUtil.getBigDecimalWithNUll(kanbanQty);
                        if (bdKanbanQty == null) {
                            // If vaue's format is error.(w1004_026)
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_026);
                            message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo),
                                "CPKKPF11_Label_Qty", "Common_ItemType_Decimal" });
                            messageList.add(message);
                        } else {
                            if (!ValidatorUtils.checkMaxDecimal(bdKanbanQty, new BigDecimal(ChinaPlusConst.MAX_QTY),
                                SCALE_KANBAN_QTY)) {
                                // If vaue's is more than the max value in DB.(w1004_028)
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_028);
                                message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo),
                                    "CPKKPF11_Label_Qty", "10", "2" });
                                messageList.add(message);
                            } else if (DecimalUtil.isLess(bdKanbanQty, BigDecimal.ZERO)) {
                                // If vaue's is negative.(w1004_030)
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_030);
                                message.setMessageArgs(new String[] { rowNo, ExcelUtil.colIndexToStr(colNo),
                                    "CPKKPF11_Label_Qty" });
                                messageList.add(message);
                            }
                            rowKanbanQtyTotal = rowKanbanQtyTotal.add(bdKanbanQty);
                        }
                    }
                }

                // If SUM(Kanban Qty) != Total Kanban Qty.(w1004_035)
                if (bdKanbanQtyTotal.compareTo(rowKanbanQtyTotal) != 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_035);
                    message.setMessageArgs(new String[] { rowNo });
                    messageList.add(message);
                }
            }

            if (moreThanOneCustomerFlag) {
                // If there is more than one customer in the Kanban file.(w1004_041)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_041);
                messageList.add(message);
            } else {
                // If the customer is not belong to current user.(w1004_023)
                TnmPartsMaster codition = new TnmPartsMaster();
                codition.setKanbanCustCode(rowDataList.get(0).getExpCustCode());
                TnmPartsMaster exsitCustomerCode = cpkkpf11Service.existCustomerCode(codition);

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
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
                    message.setMessageArgs(new String[] { rowDataList.get(0).getExpCustCode() });
                    messageList.add(message);
                }
            }
        }
        return condition;
    }

    /**
     * Check Date(Issued plan date and Deliver date to Obu) Row Common.
     * 
     * @param messageList message list
     * @param param base param
     * @param rowNo row number
     * @param checkItem check item
     * @param dateList date list
     */
    private void checkDateRowCommon(List<BaseMessage> messageList, BaseParam param, int rowNo, String checkItem,
        List<String[]> dateList) {
        for (String[] dateInfo : dateList) {
            String colNo = dateInfo[0];
            String value = dateInfo[1];

            if (!StringUtils.isBlank(value)) {
                if (DateTimeUtil.parseDate(value) == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_026);
                    message.setMessageArgs(new String[] { String.valueOf(rowNo), ExcelUtil.colIndexToStr(colNo),
                        checkItem, "Common_ItemType_Date" });
                    messageList.add(message);
                }
            }
        }
    }

    /**
     * Check qty common.<br>
     * 1. If vaue is blank.(w1004_001)<br>
     * 2. If vaue's format is error.(w1004_025)<br>
     * 4. If vaue's is negative.(w1004_029)<br>
     * 
     * @param messageList message list
     * @param value check value
     * @param rowNo row number
     * @param checkItem check item
     */
    private void checkQtyCommon(List<BaseMessage> messageList, String value, String rowNo, String checkItem) {
        if (StringUtils.isBlank(value)) {
            // If vaue is blank.(w1004_001)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
            message.setMessageArgs(new String[] { rowNo, checkItem });
            messageList.add(message);
        } else {
            BigDecimal bdValue = DecimalUtil.getBigDecimalWithNUll(value);
            if (bdValue == null) {
                // If vaue's format is error.(w1004_025)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                message.setMessageArgs(new String[] { rowNo, checkItem, "Common_ItemType_Decimal" });
                messageList.add(message);
            } else {
                if (DecimalUtil.isLess(bdValue, BigDecimal.ZERO)) {
                    // If vaue's is negative.(w1004_029)
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                    message.setMessageArgs(new String[] { rowNo, checkItem });
                    messageList.add(message);
                }
            }
        }
    }

    /**
     * Check qty common.<br>
     * 3. If vaue's is more than the max value in DB.(w1004_027)<br>
     * 
     * @param messageList message list
     * @param value check value
     * @param rowNo row number
     * @param checkItem check item
     * @param digits digits of parts
     */
    private void checkQtyCommon(List<BaseMessage> messageList, String value, String rowNo, String checkItem, int digits) {
        BigDecimal bdValue = DecimalUtil.getBigDecimalWithNUll(value);
        if (!ValidatorUtils.checkMaxDecimal(bdValue, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
            // If vaue's is more than the max value in DB.(w1004_027)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
            message.setMessageArgs(new String[] { rowNo, checkItem, "10", String.valueOf(digits) });
            messageList.add(message);
        }
    }

    /**
     * Read excel data.
     * 
     * @param sheet Sheet
     * @param issuedPlanDateList Issued plan date list ([0]:colNo; [1]:Issued plan date)
     * @param deliverDateToObuList Deliver date to Obu list ([0]:colNo; [1]:Deliver date to Obu)
     * @param rowDataList Row data list
     */
    private void readExcel(Sheet sheet, List<String[]> issuedPlanDateList, List<String[]> deliverDateToObuList,
        List<CPKKPF11Entity> rowDataList) {

        // Get Issued plan date list and Deliver date to Obu list
        for (int i = DATA_START_COL; i < sheet.getRow(1).getLastCellNum() + 1; i++) {
            // Get Issued plan date list
            issuedPlanDateList.add(new String[] { String.valueOf(i),
                PoiUtil.getStringCellValue(sheet, ISSUED_PLAN_DATE_ROW, i) });

            // Get Deliver date to Obu list
            deliverDateToObuList.add(new String[] { String.valueOf(i),
                PoiUtil.getStringCellValue(sheet, DELIVER_DATE_TO_OBU_ROW, i) });
        }

        // Get row data
        for (int i = PARTS_STRAT_ROW; i <= sheet.getLastRowNum() + 1; i++) {
            if (ValidatorUtils.isBlankRowFromTo(sheet, i, COL_NO, sheet.getRow(1).getLastCellNum() + 1)) {
                if (ValidatorUtils.isExcelEnd(sheet, i, COL_NO, sheet.getRow(1).getLastCellNum() + 1)) {
                    break;
                } else {
                    continue;
                }
            }
            CPKKPF11Entity entity = new CPKKPF11Entity();

            // Row No.
            entity.setRowNo(i);

            // No.(3)
            int colNo = COL_NO;
            entity.setSeqNo(cut(PoiUtil.getStringCellValue(sheet, i, COL_NO), NumberConst.IntDef.INT_FIVE));
            colNo++;

            // Plant(4)
            entity.setPlant(cut(PoiUtil.getStringCellValue(sheet, i, colNo), NumberConst.IntDef.INT_TEN));
            colNo++;

            // AISIN Parts No.(5)
            colNo++;

            // Customer Parts No.(6)
            // String customerPartsNo = PoiUtil.getStringCellValue(sheet, i, COL_CUSTOMER_PARTS_NO);
            // if (customerPartsNo.startsWith(StringConst.MIDDLE_LINE)) {
            // customerPartsNo = customerPartsNo.substring(1);
            // }
            // entity.setSuppPartsNo(customerPartsNo);
            entity.setSuppPartsNo(PoiUtil.getStringCellValue(sheet, i, colNo));
            colNo++;

            // Customer(7)
            entity.setExpCustCode(PoiUtil.getStringCellValue(sheet, i, colNo));
            colNo++;

            // Dock(8)
            entity.setDock(cut(PoiUtil.getStringCellValue(sheet, i, colNo), NumberConst.IntDef.INT_TEN));
            colNo++;

            // Box type(9)
            entity.setBoxType(cut(PoiUtil.getStringCellValue(sheet, i, colNo), NumberConst.IntDef.INT_FIVE));
            colNo++;

            // Box No.(10)
            entity.setBoxNo(cut(PoiUtil.getStringCellValue(sheet, i, colNo), NumberConst.IntDef.INT_TEN));
            colNo++;

            // Supplier(11)
            colNo++;

            // Qty/Box(12)
            entity.setSpq(PoiUtil.getStringCellValue(sheet, i, colNo));
            colNo++;

            // Order(13)
            entity.setOrder(PoiUtil.getStringCellValue(sheet, i, colNo));
            colNo++;

            // Kanban Qty(14)
            entity.setKanbanQty(PoiUtil.getStringCellValue(sheet, i, colNo));
            colNo++;

            // Row Kanban Qty
            List<String[]> rowKanbanQty = new ArrayList<String[]>();
            for (int j = PARTS_HEADER_END_COL + 1; j < sheet.getRow(1).getLastCellNum() + 1; j++) {
                rowKanbanQty.add(new String[] { String.valueOf(j), PoiUtil.getStringCellValue(sheet, i, j) });
            }
            entity.setRowKanbanQty(rowKanbanQty);

            rowDataList.add(entity);
        }
    }

    /**
     * Auto cut.
     * 
     * @param value value
     * @param maxLength max length
     * @return value
     */
    private String cut(String value, int maxLength) {
        if (value.length() > maxLength) {
            return value.substring(0, maxLength);
        }
        return value;
    }

    /**
     * Is all kanbanQty empty
     * 
     * @param rowDataList row data
     * @param colNo col No.
     * @return true:all kanbanQty is empty/false:all kanbanQty is not empty
     */
    public boolean isAllKanbanQtyEmpty(List<CPKKPF11Entity> rowDataList, int colNo) {
        for (CPKKPF11Entity rowData : rowDataList) {
            List<String[]> rowKanbanQtyList = rowData.getRowKanbanQty();
            for (String[] rowKanbanQtyInfo : rowKanbanQtyList) {
                if (rowKanbanQtyInfo[0].equals(String.valueOf(colNo))) {
                    if (!StringUtils.isBlank(rowKanbanQtyInfo[1])
                            && DecimalUtil.getBigDecimal(rowKanbanQtyInfo[1]).compareTo(BigDecimal.ZERO) != 0) {
                        return false;
                    } else {
                        break;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {
        return FileId.CPKKPF11;
    }
}
