/**
 * CPSSMF11Controller.java
 * 
 * @screen CPSSMF11
 * @author gu_chengchen
 */
package com.chinaplus.web.sa.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.NirdFlag;
import com.chinaplus.common.entity.TnmReason;
import com.chinaplus.common.service.CommonService;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.ExcelUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.sa.entity.CPSSMF11PartEntity;
import com.chinaplus.web.sa.entity.CPSSMF11PlanEntity;
import com.chinaplus.web.sa.entity.CPSSMF11ResultEntity;
import com.chinaplus.web.sa.entity.CPSSMF11UploadEntity;
import com.chinaplus.web.sa.service.CPSSMF11Service;

/**
 * Revised Shipping Status Upload Controller.
 */
@Controller
public class CPSSMF11Controller extends BaseFileController {

    /** separator */
    public static final String SEPARATOR = "#;!";

    /** Parameter: Revision Code Set */
    private static final String PARAM_REVISION_CODE_SET = "revisionCodeSet";

    /** Parameter: Detailed Reason */
    private static final String PARAM_DETAILED_REASON = "detailedReason";

    /** Parameter: Upload Result */
    private static final String PARAM_UPLOAD_RESULT = "uploadResult";

    /** Part start row */
    private static final int PART_START_ROW = 13;

    /** Part start column */
    private static final int PART_START_COL = 1;

    /** Part total column */
    private static final int PART_TOTAL_COL = 17;

    /** Plan start row */
    private static final int PLAN_START_ROW = 3;

    /** Plan start column */
    private static final int PLAN_START_COL = 18;

    /** Title row index */
    private static final int TITLE_ROW_INDEX = 11;

    /** Total title row */
    private static final int TOTAL_TITLE_ROW = 4;

    /** NIRD reason row */
    private static final int NIRD_REASON_ROW = 10;

    /** Blank start column */
    private static final int BLANK_START_COLUMN = 5;

    /** Common Service */
    @Autowired
    private CommonService commonService;

    /** Revised Shipping Status Upload Service */
    @Autowired
    private CPSSMF11Service cpssmf11Service;

    /**
     * Revised Shipping Status Upload.
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/sa/CPSSMF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        super.setCommonParam(param, request);

        // Check revision reason
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        String revisionCodeSet = (String) param.getSwapData().get(PARAM_REVISION_CODE_SET);
        String detailedReason = (String) param.getSwapData().get(PARAM_DETAILED_REASON);
        if (StringUtil.isEmpty(revisionCodeSet)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
            message.setMessageArgs(new String[] { "CPSSMS03_Label_RevisionReason" });
            messageLists.add(message);
        } else if (revisionCodeSet.contains(StringConst.REASON_OTHER_CODE)) {
            if (StringUtil.isEmpty(detailedReason)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_005);
                message.setMessageArgs(new String[] { "CPSSMS03_Label_DetailedRevisionReason" });
                messageLists.add(message);
            } else if (detailedReason.length() > IntDef.INT_EIGHTY) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1001_006);
                message.setMessageArgs(new String[] { "CPSSMS03_Label_DetailedRevisionReason", "80" });
                messageLists.add(message);
            }
        }
        if (messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // File upload
        BaseResult<CPSSMF11ResultEntity> baseResult = super.uploadFileProcess(file, FileType.EXCEL, param, request,
            response);
        CPSSMF11ResultEntity resultEntity = new CPSSMF11ResultEntity();
        resultEntity.setUploadResult((int) param.getSwapData().get(PARAM_UPLOAD_RESULT));
        baseResult.setData(resultEntity);
        super.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file upload file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request HttpServletRequest
     * @return result message
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {

        // Get excel current locale
        Sheet sheet = workbook.getSheetAt(0);
        Locale excelLocale = null;
        String titleCn = MessageManager.getMessage("CPSSMF11_Label_Title", Language.CHINESE.getLocale());
        String excelTitle = PoiUtil.getStringCellValue(sheet, 1, 1);
        if (titleCn.equals(excelTitle)) {
            excelLocale = Language.CHINESE.getLocale();
        } else {
            excelLocale = Language.ENGLISH.getLocale();
        }

        // Excel format check
        String downloadTimeLabel = MessageManager.getMessage("CPSSMF11_Label_DownloadTime", excelLocale);
        String ssmsTimeLabel = MessageManager.getMessage("CPSSMF11_Label_SsmsTime", excelLocale);
        String interfaceTimeLabel = MessageManager.getMessage("CPSSMF11_Label_InterfaceTime", excelLocale);
        String excelDownloadTime = PoiUtil.getStringCellValue(sheet, sheet.getLastRowNum() - 1, 1);
        String excelSsmsTime = PoiUtil.getStringCellValue(sheet, sheet.getLastRowNum(), 1);
        String excelInterfaceTime = PoiUtil.getStringCellValue(sheet, sheet.getLastRowNum() + 1, 1);
        if (!(excelDownloadTime.contains(downloadTimeLabel) && excelSsmsTime.contains(ssmsTimeLabel) && excelInterfaceTime
            .contains(interfaceTimeLabel))) {
            throw new BusinessException(MessageCodeConst.W1014);
        }

        // Get current office ID and customer IDs
        Integer officeId = param.getCurrentOfficeId();
        Map<String, Integer> customerMap = new HashMap<String, Integer>();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        List<com.chinaplus.common.bean.BusinessPattern> currentCustoemrs = um.getCurrentBusPattern();
        for (com.chinaplus.common.bean.BusinessPattern currentCustoemr : currentCustoemrs) {
            if (BusinessPattern.V_V == currentCustoemr.getBusinessPattern()) {
                customerMap.put(currentCustoemr.getCustomerCode(), currentCustoemr.getCustomerId());
            }
        }

        // Get and check part row
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<String> ipoCpoList = new ArrayList<String>();
        List<String> partKeyList = new ArrayList<String>();
        List<Integer> fcPartsList = new ArrayList<Integer>();
        Map<Integer, CPSSMF11PartEntity> partMap = new LinkedHashMap<Integer, CPSSMF11PartEntity>();
        getAndCheckPartRow(messageLists, ipoCpoList, partMap, sheet, officeId, customerMap, partKeyList, fcPartsList);

        // Do error check
        if (partMap.size() == 0 || ipoCpoList.size() == 0) {
            throw new BusinessException(MessageCodeConst.W1004_005);
        } else if (ipoCpoList.size() > 1) {
            throw new BusinessException(MessageCodeConst.W1004_129);
        }

        // Find exist order not completed shipping
        String[] ipoCpoArray = ipoCpoList.get(0).split(SEPARATOR, -1);
        String ipo = ipoCpoArray[0];
        String cpo = ipoCpoArray[1];
        String custCode = ipoCpoArray[IntDef.INT_TWO];
        Integer custId = customerMap.get(custCode);
        if (!cpssmf11Service.isOrderExist(ipo, cpo, custId)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_171);
            message.setMessageArgs(new String[] { ipo, cpo, custCode });
            throw new BusinessException(message);
        }
        Integer orderId = null;
        Integer lastSsId = null;
        Integer lastVersion = null;
        Map<String, CPSSMF11PlanEntity> notCompletedPlanMap = new HashMap<String, CPSSMF11PlanEntity>();
        List<String> nirdPlanList = new ArrayList<String>();
        List<CPSSMF11PlanEntity> notCompletedPlans = cpssmf11Service.getNotCompletedPlans(officeId, ipo, cpo, custId);
        for (CPSSMF11PlanEntity notCompletedPlan : notCompletedPlans) {
            orderId = notCompletedPlan.getOrderId();
            lastSsId = notCompletedPlan.getLastSsId();
            lastVersion = notCompletedPlan.getLastVersion();
            if (notCompletedPlan.getSsPlanId() != null) {
                StringBuffer sbMapKey = new StringBuffer();
                sbMapKey.append(notCompletedPlan.getTransportMode());
                sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(notCompletedPlan.getEtd()));
                sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(notCompletedPlan.getEta()));
                sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(notCompletedPlan.getCcDate()));
                sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(notCompletedPlan.getInboundPlanDate()));
                if (NirdFlag.NOT_IN_RUNDOWN == notCompletedPlan.getNirdFlag()) {
                    nirdPlanList.add(sbMapKey.toString());
                }
                sbMapKey.append(SEPARATOR).append(notCompletedPlan.getNirdFlag());
                notCompletedPlanMap.put(sbMapKey.toString(), notCompletedPlan);
            }
        }

        // Find exist invoices
        Map<String, CPSSMF11PlanEntity> allInvoiceMap = new HashMap<String, CPSSMF11PlanEntity>();
        List<CPSSMF11PlanEntity> allInvoices = cpssmf11Service.getAllInvoices(officeId, ipo, cpo, custId);
        for (CPSSMF11PlanEntity allInvoice : allInvoices) {
            StringBuffer sbMapKey = new StringBuffer();
            sbMapKey.append(allInvoice.getTransportMode());
            sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(allInvoice.getEtd()));
            sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(allInvoice.getEta()));
            sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(allInvoice.getCcDate()));
            sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(allInvoice.getInboundPlanDate()));
            sbMapKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(allInvoice.getInboundActualDate()));
            sbMapKey.append(SEPARATOR).append(allInvoice.getInvoiceNo());
            allInvoiceMap.put(sbMapKey.toString(), allInvoice);
        }

        // Get and check plan column
        List<CPSSMF11PlanEntity> excelPlanList = new ArrayList<CPSSMF11PlanEntity>();
        boolean isFormatError = getAndCheckPlanColumn(messageLists, partMap, sheet, excelLocale, allInvoiceMap,
            notCompletedPlanMap, excelPlanList, nirdPlanList);

        // Check format error
        if (isFormatError) {
            throw new BusinessException(MessageCodeConst.W1014);
        }

        // Check all not completed shipping&invoice exist
        if (notCompletedPlanMap.size() > 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_165);
            messageLists.add(message);
        }
        if (allInvoiceMap.size() > 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_166);
            messageLists.add(message);
        }
        if (messageLists.size() != 0) {
            throw new BusinessException(messageLists);
        }

        // Get revision reason
        List<TnmReason> revisionReasons = commonService.getActiveRevisionReasons(BusinessPattern.V_V);
        StringBuffer sbReasonSet = new StringBuffer();
        String revisionCodeSet = (String) param.getSwapData().get(PARAM_REVISION_CODE_SET);
        String detailedReason = (String) param.getSwapData().get(PARAM_DETAILED_REASON);
        String[] revisionCodeArray = revisionCodeSet.split(StringConst.COMMA);
        Arrays.sort(revisionCodeArray);
        for (String revisionCode : revisionCodeArray) {
            if (StringConst.REASON_OTHER_CODE.equals(revisionCode)) {
                continue;
            }
            for (TnmReason reason : revisionReasons) {
                if (revisionCode.equals(reason.getReasonCode())) {
                    sbReasonSet.append(StringConst.COMMA).append(reason.getReasonDesc());
                    break;
                }
            }
        }
        if (!StringUtil.isEmpty(detailedReason)) {
            sbReasonSet.append(StringConst.COMMA).append(detailedReason);
        }
        String reasonSet = sbReasonSet.substring(1);

        // Ignore force completed parts's revision
        int ignoreResult = ignoreFcPartsRevision(excelPlanList, fcPartsList);
        param.getSwapData().put(PARAM_UPLOAD_RESULT, ignoreResult);

        // Do upload logic
        CPSSMF11UploadEntity uploadEntity = new CPSSMF11UploadEntity();
        uploadEntity.setOrderId(orderId);
        uploadEntity.setLastSsId(lastSsId);
        uploadEntity.setLastVersion(lastVersion);
        uploadEntity.setReasonCodeSet(revisionCodeSet);
        uploadEntity.setReasonSet(reasonSet);
        uploadEntity.setPartMap(partMap);
        uploadEntity.setPartKeyList(partKeyList);
        uploadEntity.setExcelPlanList(excelPlanList);
        cpssmf11Service.doUpload(uploadEntity, param);

        return messageLists;
    }

    /**
     * Ignore force completed parts's revision.
     * 
     * @param excelPlanList excel plan list
     * @param fcPartsList force completed parts list
     * @return ignore result
     */
    private int ignoreFcPartsRevision(List<CPSSMF11PlanEntity> excelPlanList, List<Integer> fcPartsList) {

        int ignoreResult = IntDef.INT_ZERO;
        for (CPSSMF11PlanEntity plan : excelPlanList) {
            if (plan.getColumnType() == ColumnType.PLAN_NEW || plan.getColumnType() == ColumnType.NIRD_NEW
                    || plan.getColumnType() == ColumnType.NIRD_MOD) {
                // NEW plan check
                Map<Integer, BigDecimal> partQtyMap = plan.getQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (fcPartsList.contains(qtyEntry.getKey())) {
                        if (DecimalUtil.isGreater(qtyEntry.getValue(), BigDecimal.ZERO)) {
                            ignoreResult = IntDef.INT_ONE;
                        }
                        partQtyMap.put(qtyEntry.getKey(), BigDecimal.ZERO);
                    }
                }
                boolean needRemove = true;
                for (Map.Entry<Integer, BigDecimal> qtyEntry : partQtyMap.entrySet()) {
                    if (DecimalUtil.isGreater(qtyEntry.getValue(), BigDecimal.ZERO)) {
                        needRemove = false;
                        break;
                    }
                }
                if (needRemove) {
                    plan.setRemoved(true);
                }
            } else if (plan.getColumnType() == ColumnType.PLAN_DATA && plan.getModEntity() != null) {
                // MOD plan check
                Map<Integer, BigDecimal> partQtyMap = plan.getModEntity().getQtyMap();
                Map<Integer, BigDecimal> originalQtyMap = plan.getModEntity().getOriginalQtyMap();
                for (Map.Entry<Integer, BigDecimal> qtyEntry : originalQtyMap.entrySet()) {
                    if (fcPartsList.contains(qtyEntry.getKey())) {
                        if (DecimalUtil.isGreater(qtyEntry.getValue(), BigDecimal.ZERO)) {
                            ignoreResult = IntDef.INT_ONE;
                        }
                        originalQtyMap.put(qtyEntry.getKey(), BigDecimal.ZERO);
                        partQtyMap.put(qtyEntry.getKey(), BigDecimal.ZERO);
                    }
                }
                // boolean needRemove = true;
                // if (plan.getModEntity().getEtd() != null) {
                // needRemove = false;
                // }
                // if (needRemove) {
                // for (Map.Entry<Integer, BigDecimal> qtyEntry : originalQtyMap.entrySet()) {
                // if (DecimalUtil.isGreater(qtyEntry.getValue(), BigDecimal.ZERO)) {
                // needRemove = false;
                // break;
                // }
                // }
                // }
                // if (needRemove) {
                // plan.setModEntity(null);
                // }
            }
        }

        return ignoreResult;
    }

    /**
     * Get and check part row.
     * 
     * @param messageLists message list
     * @param ipoCpoList IPO CPO list
     * @param partMap part map
     * @param sheet excel sheet
     * @param officeId office ID
     * @param customerMap customer map
     * @param partKeyList part key list
     * @param fcPartsList force completed parts list
     */
    private void getAndCheckPartRow(List<BaseMessage> messageLists, List<String> ipoCpoList,
        Map<Integer, CPSSMF11PartEntity> partMap, Sheet sheet, Integer officeId, Map<String, Integer> customerMap,
        List<String> partKeyList, List<Integer> fcPartsList) {

        List<String> errCusList = new ArrayList<String>();
        int partCol = PART_START_COL;
        for (int partRow = PART_START_ROW; partRow < sheet.getLastRowNum() - 1; partRow++) {
            if (ValidatorUtils.isBlankRow(sheet, partRow, PART_START_COL, PART_TOTAL_COL)) {
                continue;
            }
            CPSSMF11PartEntity partEntity = new CPSSMF11PartEntity();
            partEntity.setOfficeId(officeId);
            partMap.put(partRow, partEntity);

            // TTC Parts No.
            String ttcPartsNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setTtcPartsNo(ttcPartsNo);

            // Part Name (English)
            partCol++;

            // Part Name (Chinese)
            partCol++;

            // Exp Country
            partCol++;

            // TTC Supplier Code
            String ttcSuppCode = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setTtcSuppCode(ttcSuppCode);

            // Imp Country
            partCol++;

            // TTC Customer Code
            String ttcCustCode = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setTtcCustCode(ttcCustCode);
            if (!StringUtil.isEmpty(ttcCustCode) && !customerMap.containsKey(ttcCustCode)) {
                addDistinct(errCusList, ttcCustCode);
            }

            // Customer Parts No.
            String custPartsNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setCustPartsNo(custPartsNo);

            // Customer Order No.
            String customerOrderNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setCustomerOrderNo(customerOrderNo);

            // Back No.
            partCol++;

            // IPO No.
            String impPoNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setImpPoNo(impPoNo);
            if (!StringUtil.isEmpty(impPoNo) || !StringUtil.isEmpty(customerOrderNo)
                    || !StringUtil.isEmpty(ttcCustCode)) {
                addDistinct(ipoCpoList, impPoNo + SEPARATOR + customerOrderNo + SEPARATOR + ttcCustCode);
            }

            // EPO No.
            String expPoNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            partEntity.setExpPoNo(expPoNo);

            // Order Type
            partCol++;

            // On Shipping Delay Pattern
            partCol++;

            // SPQ
            partCol++;

            // GSCM Sales Order Issued Date
            partCol++;

            // Find part info
            CPSSMF11PartEntity partCondition = new CPSSMF11PartEntity();
            BeanUtils.copyProperties(partEntity, partCondition);
            CPSSMF11PartEntity partInfo = cpssmf11Service.getPartInfo(partCondition);
            if (partInfo == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_172);
                message.setMessageArgs(new String[] { String.valueOf(partRow) });
                messageLists.add(message);
            } else {
                String partKey = partInfo.getOrderStatusId() + SEPARATOR + partInfo.getPartsId();
                if (!partKeyList.contains(partKey)) {
                    partKeyList.add(partKey);
                } else {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_124);
                    message.setMessageArgs(new String[] { String.valueOf(partRow) });
                    messageLists.add(message);
                }
                partEntity.setOrderId(partInfo.getOrderId());
                partEntity.setCustomerId(partInfo.getCustomerId());
                partEntity.setOrderStatusId(partInfo.getOrderStatusId());
                partEntity.setSupplierId(partInfo.getSupplierId());
                partEntity.setPartsId(partInfo.getPartsId());
                partEntity.setUomCode(partInfo.getUomCode());
                partEntity.setOrderQty(partInfo.getOrderQty());
                partEntity.setForceCompletedQty(partInfo.getForceCompletedQty());
                if (partInfo.getForceCompletedQty() != null
                        && !DecimalUtil.isEquals(partInfo.getForceCompletedQty(), BigDecimal.ZERO)) {
                    partEntity.setForceCompleted(true);
                    fcPartsList.add(partRow);
                } else {
                    partEntity.setForceCompleted(false);
                }
            }

            // Total Order Qty
            BigDecimal orderQty = null;
            String strOrderQty = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
            if (!StringUtil.isEmpty(strOrderQty)) {
                orderQty = DecimalUtil.getBigDecimalWithNUll(strOrderQty);
            }
            if (partInfo != null) {
                // Check UOM
                int digits = MasterManager.getUomDigits(partEntity.getUomCode());
                if (orderQty != null && orderQty.scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(partRow), "CPSSMF11_Label_TotalOrderQty",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }

                // Check total qty is equals with DB qty
                BigDecimal dbQty = partInfo.getOrderQty();
                BigDecimal dbFormatQty = StringUtil.toBigDecimal(DecimalUtil.format(dbQty, partEntity.getUomCode())
                    .replaceAll(StringConst.COMMA, StringConst.EMPTY));
                if (!DecimalUtil.isEquals(orderQty, dbFormatQty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_182);
                    message.setMessageArgs(new String[] { String.valueOf(partRow) });
                    messageLists.add(message);
                }
            }
            partEntity.setOrderQty(orderQty);

            // reset part start col
            partCol = PART_START_COL;
        }

        // Check role
        List<BaseMessage> roleMessages = new ArrayList<BaseMessage>();
        for (String cusCode : errCusList) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
            message.setMessageArgs(new String[] { cusCode });
            roleMessages.add(message);
        }
        if (roleMessages.size() > 0) {
            throw new BusinessException(roleMessages);
        }
    }

    /**
     * Get and check plan column.
     * 
     * @param messageLists message list
     * @param partMap part map
     * @param sheet excel sheet
     * @param excelLocale current locale
     * @param allInvoiceMap all invoice map
     * @param notCompletedPlanMap not completed plan map
     * @param excelPlanList excel plan list
     * @param nirdPlanList not in rundown plan list
     * @return check result
     */
    private boolean getAndCheckPlanColumn(List<BaseMessage> messageLists, Map<Integer, CPSSMF11PartEntity> partMap,
        Sheet sheet, Locale excelLocale, Map<String, CPSSMF11PlanEntity> allInvoiceMap,
        Map<String, CPSSMF11PlanEntity> notCompletedPlanMap, List<CPSSMF11PlanEntity> excelPlanList,
        List<String> nirdPlanList) {

        String modLabel = MessageManager.getMessage("CPSSMF11_Label_Mod", excelLocale);
        String newLabel = MessageManager.getMessage("CPSSMF11_Label_New", excelLocale);
        String nirdLabel = MessageManager.getMessage("CPSSMF11_Label_NotInRundown", excelLocale);
        String tsQtyLabel = MessageManager.getMessage("CPSSMF11_Label_TotalShipmentQty", excelLocale);
        String tbQtyLabel = MessageManager.getMessage("CPSSMF11_Label_TotalShipmentBalanceQty", excelLocale);
        String fcQtyLabel = MessageManager.getMessage("CPSSMF11_Label_ForceComplete", excelLocale);
        String nirdTmLabel = MessageManager.getMessage("CPSSMF11_Label_TransportationMode", excelLocale);
        boolean isFormatError = true;
        int nirdTmCol = 0;
        Map<Integer, BigDecimal> newNirdQtyMap = new HashMap<Integer, BigDecimal>();
        CPSSMF11PlanEntity previousPlan = null;
        int planRow = PLAN_START_ROW;
        for (int planCol = PLAN_START_COL; planCol <= sheet.getRow(TITLE_ROW_INDEX).getLastCellNum(); planCol++) {
            // Format check
            if (planCol <= nirdTmCol) {
                continue;
            }
            String excelTsQty = PoiUtil.getStringCellValue(sheet, TOTAL_TITLE_ROW, planCol);
            if (tsQtyLabel.equals(excelTsQty)) {
                String excelTbQty = PoiUtil.getStringCellValue(sheet, TOTAL_TITLE_ROW, planCol + IntDef.INT_ONE);
                String excelFcQty = PoiUtil.getStringCellValue(sheet, TOTAL_TITLE_ROW, planCol + IntDef.INT_TWO);
                String excelNirdTm = PoiUtil.getStringCellValue(sheet, TOTAL_TITLE_ROW + IntDef.INT_ONE, planCol
                        + IntDef.INT_FOUR);
                if (tbQtyLabel.equals(excelTbQty) && fcQtyLabel.equals(excelFcQty)
                        && (StringUtil.isEmpty(excelNirdTm) || nirdTmLabel.equals(excelNirdTm))) {
                    isFormatError = false;
                    nirdTmCol = planCol + IntDef.INT_FOUR;
                    continue;
                } else {
                    break;
                }
            }

            // Read excel data
            CPSSMF11PlanEntity planEntity = new CPSSMF11PlanEntity();

            // MOD/NEW
            String modType = PoiUtil.getStringCellValue(sheet, planRow++, planCol);

            // Invoice No.
            String invoiceNo = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            planEntity.setInvoiceNo(invoiceNo);

            // Transportation Mode
            String strTransportMode = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Integer transportMode = null;
            if (!StringUtil.isEmpty(strTransportMode)) {
                transportMode = CodeCategoryManager.getCodeValue(Language.ENGLISH.getCode(),
                    CodeMasterCategory.TRANSPORT_MODE, strTransportMode);
                if (transportMode == null) {
                    transportMode = CodeCategoryManager.getCodeValue(Language.CHINESE.getCode(),
                        CodeMasterCategory.TRANSPORT_MODE, strTransportMode);
                }
            }
            planEntity.setTransportMode(transportMode);

            // ETD
            String strEtd = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Date etd = null;
            if (!StringUtil.isEmpty(strEtd)) {
                etd = DateTimeUtil.parseDate(strEtd);
            }
            planEntity.setEtd(etd);

            // ETA
            String strEta = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Date eta = null;
            if (!StringUtil.isEmpty(strEta)) {
                eta = DateTimeUtil.parseDate(strEta);
            }
            planEntity.setEta(eta);

            // Customer Clearance Date
            String strCCDate = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Date ccDate = null;
            if (!StringUtil.isEmpty(strCCDate)) {
                ccDate = DateTimeUtil.parseDate(strCCDate);
            }
            planEntity.setCcDate(ccDate);

            // Imp Inbound Plan Date
            String strInboundPlanDate = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Date inboundPlanDate = null;
            if (!StringUtil.isEmpty(strInboundPlanDate)) {
                inboundPlanDate = DateTimeUtil.parseDate(strInboundPlanDate);
            }
            planEntity.setInboundPlanDate(inboundPlanDate);

            // Imp Inbound Actual Date
            String strInboundActualDate = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            Date inboundActualDate = null;
            if (!StringUtil.isEmpty(strInboundActualDate)) {
                inboundActualDate = DateTimeUtil.parseDate(strInboundActualDate);
            }
            planEntity.setInboundActualDate(inboundActualDate);

            // Revision Reason
            String revisionReason = PoiUtil.getStringCellValue(sheet, planRow++, planCol);
            planEntity.setRevisionReason(revisionReason);

            // Plan Name
            planRow++;

            // Check column type
            int columnType = ColumnType.DUMMY;
            if (StringUtil.isEmpty(modType)) {
                if (StringUtil.isEmpty(invoiceNo)) {
                    // Plan Data
                    columnType = ColumnType.PLAN_DATA;
                } else {
                    // Actual Data
                    columnType = ColumnType.ACTUAL_DATA;
                }
            } else if (nirdLabel.equalsIgnoreCase(modType)) {
                // NIRD
                columnType = ColumnType.NIRD_DATA;
            } else if (modLabel.equalsIgnoreCase(modType)) {
                if (previousPlan != null) {
                    if (previousPlan.getColumnType() == ColumnType.PLAN_DATA) {
                        // Plan MOD
                        columnType = ColumnType.PLAN_MOD;
                    } else if (previousPlan.getColumnType() == ColumnType.ACTUAL_DATA) {
                        // Actual MOD
                        columnType = ColumnType.ACTUAL_MOD;
                    } else if (previousPlan.getColumnType() == ColumnType.NIRD_DATA) {
                        // NIRD MOD
                        columnType = ColumnType.NIRD_MOD;
                    }
                }
            } else if (newLabel.equalsIgnoreCase(modType)) {
                if (nirdTmCol != 0) {
                    if (previousPlan != null && previousPlan.getColumnType() == ColumnType.NIRD_DATA) {
                        // NIRD MOD
                        columnType = ColumnType.NIRD_MOD;
                    } else {
                        // NIRD NEW
                        columnType = ColumnType.NIRD_NEW;
                    }
                } else {
                    // Plan NEW
                    columnType = ColumnType.PLAN_NEW;
                }
            }
            planEntity.setColumnType(columnType);
            if (columnType == ColumnType.NIRD_DATA || columnType == ColumnType.NIRD_MOD
                    || columnType == ColumnType.NIRD_NEW) {
                revisionReason = PoiUtil.getStringCellValue(sheet, NIRD_REASON_ROW, planCol);
                planEntity.setRevisionReason(revisionReason);
            }
            if (columnType == ColumnType.PLAN_DATA
                    && ValidatorUtils.isBlankColFromTo(sheet, planCol, BLANK_START_COLUMN, sheet.getLastRowNum()
                            - IntDef.INT_TWO)) {
                planRow = PLAN_START_ROW;
                continue;
            }

            // Check MOD or NEW plan same as NIRD plan
            if (columnType == ColumnType.PLAN_MOD || columnType == ColumnType.PLAN_NEW
                    || columnType == ColumnType.NIRD_MOD || columnType == ColumnType.NIRD_NEW) {
                StringBuffer columnKey = new StringBuffer();
                columnKey.append(transportMode);
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(etd));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(eta));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(ccDate));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(inboundPlanDate));
                if (nirdPlanList.contains(columnKey.toString())) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_181);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol) });
                    messageLists.add(message);
                }
            }

            // Check column data
            boolean isPlanInput = false;
            if (columnType == ColumnType.PLAN_DATA || columnType == ColumnType.ACTUAL_DATA
                    || columnType == ColumnType.NIRD_DATA) {
                // Actual data
                StringBuffer columnKey = new StringBuffer();
                columnKey.append(transportMode);
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(etd));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(eta));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(ccDate));
                columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(inboundPlanDate));
                if (columnType == ColumnType.ACTUAL_DATA) {
                    columnKey.append(SEPARATOR).append(DateTimeUtil.getDisDate(inboundActualDate));
                    columnKey.append(SEPARATOR).append(invoiceNo);
                    CPSSMF11PlanEntity existActual = allInvoiceMap.get(columnKey.toString());
                    if (existActual == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_135);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol) });
                        messageLists.add(message);
                    } else {
                        planEntity.setInvoiceSummaryId(existActual.getInvoiceSummaryId());
                        planEntity.setInvoiceId(existActual.getInvoiceId());
                        planEntity.setOriginalVersion(existActual.getOriginalVersion());
                        planEntity.setRevisionVersion(existActual.getRevisionVersion());
                        planEntity.setRevisionReason(existActual.getRevisionReason());
                        planEntity.setVanningDate(existActual.getVanningDate());
                        allInvoiceMap.remove(columnKey.toString());
                    }
                } else {
                    // Plan data & NIRD data
                    if (columnType == ColumnType.PLAN_DATA) {
                        columnKey.append(SEPARATOR).append(NirdFlag.NORMAL);
                    } else {
                        columnKey.append(SEPARATOR).append(NirdFlag.NOT_IN_RUNDOWN);
                    }
                    CPSSMF11PlanEntity existPlan = notCompletedPlanMap.get(columnKey.toString());
                    if (existPlan == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_136);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol) });
                        messageLists.add(message);
                    } else {
                        planEntity.setSsPlanId(existPlan.getSsPlanId());
                        planEntity.setOriginalVersion(existPlan.getOriginalVersion());
                        planEntity.setRevisionVersion(existPlan.getRevisionVersion());
                        planEntity.setRevisionReason(existPlan.getRevisionReason());
                        planEntity.setNirdFlag(existPlan.getNirdFlag());
                        notCompletedPlanMap.remove(columnKey.toString());
                    }
                }
            } else if (columnType != ColumnType.DUMMY) {
                boolean isInvoice = columnType == ColumnType.ACTUAL_MOD ? true : false;
                isPlanInput = isPlanInput(strTransportMode, strEtd, strEta, strCCDate, strInboundPlanDate, isInvoice);
                if (isPlanInput) {
                    // Transportation Mode
                    if (!isInvoice) {
                        if (StringUtil.isEmpty(strTransportMode)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                                "CPSSMF11_Label_TransportationMode" });
                            messageLists.add(message);
                        } else if (transportMode == null) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_054);
                            message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                                "CPSSMF11_Label_TransportationMode" });
                            messageLists.add(message);
                        }
                    }

                    // ETD
                    if (StringUtil.isEmpty(strEtd)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Etd" });
                        messageLists.add(message);
                    } else if (etd == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Etd",
                            "Common_ItemType_Date" });
                        messageLists.add(message);
                    }

                    // ETA
                    if (StringUtil.isEmpty(strEta)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Eta" });
                        messageLists.add(message);
                    } else if (eta == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Eta",
                            "Common_ItemType_Date" });
                        messageLists.add(message);
                    } else if (etd != null && eta != null && eta.compareTo(etd) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Eta",
                            "CPSSMF11_Label_Etd" });
                        messageLists.add(message);
                    }

                    // Customer Clearance Date
                    if (StringUtil.isEmpty(strCCDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_CustomerClearanceDate" });
                        messageLists.add(message);
                    } else if (ccDate == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_CustomerClearanceDate", "Common_ItemType_Date" });
                        messageLists.add(message);
                    } else if (eta != null && ccDate != null && ccDate.compareTo(eta) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_CustomerClearanceDate", "CPSSMF11_Label_Eta" });
                        messageLists.add(message);
                    }

                    // Imp Inbound Plan Date
                    if (StringUtil.isEmpty(strInboundPlanDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_ImpInboundPlanDate" });
                        messageLists.add(message);
                    } else if (inboundPlanDate == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_ImpInboundPlanDate", "Common_ItemType_Date" });
                        messageLists.add(message);
                    } else if (ccDate != null && inboundPlanDate != null && inboundPlanDate.compareTo(ccDate) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                            "CPSSMF11_Label_ImpInboundPlanDate", "CPSSMF11_Label_CustomerClearanceDate" });
                        messageLists.add(message);
                    }
                }
            }

            // Part Qty
            boolean isQtyInput = false;
            Map<Integer, BigDecimal> nirdDataQtyMap = null;
            if (previousPlan != null && previousPlan.getColumnType() == ColumnType.NIRD_DATA) {
                nirdDataQtyMap = previousPlan.getQtyMap();
            }
            if (columnType != ColumnType.DUMMY && columnType != ColumnType.ACTUAL_MOD) {
                Map<Integer, BigDecimal> qtyMap = new HashMap<Integer, BigDecimal>();
                Map<Integer, BigDecimal> originalQtyMap = new HashMap<Integer, BigDecimal>();
                for (; planRow < sheet.getLastRowNum() - 1; planRow++) {
                    String strQty = PoiUtil.getStringCellValue(sheet, planRow, planCol);
                    BigDecimal qty = null;
                    BigDecimal originalQty = null;
                    if (!StringUtil.isEmpty(strQty)) {
                        if (columnType == ColumnType.PLAN_MOD || columnType == ColumnType.PLAN_NEW
                                || columnType == ColumnType.NIRD_MOD || columnType == ColumnType.NIRD_NEW) {
                            isQtyInput = true;
                        }
                        qty = DecimalUtil.getBigDecimalWithNUll(strQty);
                        originalQty = qty;
                        if (qty == null) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_026);
                            message
                                .setMessageArgs(new String[] { String.valueOf(planRow),
                                    ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Quantity",
                                    "Common_ItemType_Decimal" });
                            messageLists.add(message);
                        } else {
                            if (!ValidatorUtils.checkMaxDecimal(qty)) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_028);
                                message.setMessageArgs(new String[] { String.valueOf(planRow),
                                    ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Quantity", "10", "6" });
                                messageLists.add(message);
                            }
                            if (DecimalUtil.isLess(qty, BigDecimal.ZERO)) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_030);
                                message.setMessageArgs(new String[] { String.valueOf(planRow),
                                    ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Quantity" });
                                messageLists.add(message);
                            }
                            CPSSMF11PartEntity partInfo = partMap.get(planRow);
                            if (partInfo != null && !StringUtil.isEmpty(partInfo.getUomCode())) {
                                int digits = MasterManager.getUomDigits(partInfo.getUomCode());
                                if (qty.scale() > digits) {
                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_160);
                                    message.setMessageArgs(new String[] { String.valueOf(planRow),
                                        ExcelUtil.colIndexToStr(planCol), "CPSSMF11_Label_Quantity",
                                        String.valueOf(digits) });
                                    messageLists.add(message);
                                }
                            }
                            if (columnType == ColumnType.NIRD_MOD && nirdDataQtyMap != null) {
                                previousPlan.setNirdUpdate(true);
                                Map<Integer, BigDecimal> leftQtyMap = previousPlan.getLeftQtyMap();
                                BigDecimal nirdDataQty = nirdDataQtyMap.get(planRow);
                                leftQtyMap.put(planRow, DecimalUtil.subtract(nirdDataQty, qty));
                                if (DecimalUtil.isGreater(qty, nirdDataQty)) {
                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_169);
                                    message.setMessageArgs(new String[] { String.valueOf(planRow),
                                        ExcelUtil.colIndexToStr(planCol) });
                                    messageLists.add(message);
                                }
                            }
                            if (columnType == ColumnType.NIRD_NEW) {
                                BigDecimal newNirdQty = newNirdQtyMap.get(planRow);
                                newNirdQtyMap.put(planRow, DecimalUtil.add(newNirdQty, qty));
                            }
                        }
                    } else {
                        originalQty = null;
                        if (columnType == ColumnType.PLAN_MOD && previousPlan != null
                                && previousPlan.getColumnType() == ColumnType.PLAN_DATA) {
                            Map<Integer, BigDecimal> preQtyMap = previousPlan.getQtyMap();
                            qty = preQtyMap.get(planRow);
                        } else {
                            qty = BigDecimal.ZERO;
                        }
                    }
                    qtyMap.put(planRow, qty);
                    originalQtyMap.put(planRow, originalQty);
                }
                planEntity.setQtyMap(qtyMap);
                if (columnType == ColumnType.PLAN_MOD) {
                    planEntity.setOriginalQtyMap(originalQtyMap);
                }
                if (columnType == ColumnType.NIRD_DATA) {
                    planEntity.setLeftQtyMap(new HashMap<>(qtyMap));
                }
            }

            // Check revision reason
            if (isPlanInput || isQtyInput) {
                if (StringUtil.isEmpty(revisionReason)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_131);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                        "CPSSMF11_Label_RevisionReason" });
                    messageLists.add(message);
                } else if (revisionReason.length() > IntDef.INT_EIGHTY) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_137);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol),
                        "CPSSMF11_Label_RevisionReason", "80" });
                    messageLists.add(message);
                }
            }

            // Check Plan&Qty input relation
            if (columnType == ColumnType.PLAN_NEW || columnType == ColumnType.NIRD_MOD
                    || columnType == ColumnType.NIRD_NEW) {
                if (isPlanInput && !isQtyInput) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_168);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol) });
                    messageLists.add(message);
                } else if (!isPlanInput && isQtyInput) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_167);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(planCol) });
                    messageLists.add(message);
                }
            }

            // reset plan start row
            if (columnType != ColumnType.DUMMY) {
                if (columnType == ColumnType.PLAN_DATA || columnType == ColumnType.ACTUAL_DATA
                        || columnType == ColumnType.NIRD_DATA) {
                    excelPlanList.add(planEntity);
                } else if (isPlanInput || isQtyInput) {
                    if (columnType == ColumnType.PLAN_MOD || columnType == ColumnType.ACTUAL_MOD) {
                        previousPlan.setModEntity(planEntity);
                    } else {
                        excelPlanList.add(planEntity);
                    }
                }
                previousPlan = planEntity;
            }
            planRow = PLAN_START_ROW;
        }

        // Check part QTY
        for (Map.Entry<Integer, CPSSMF11PartEntity> partEntity : partMap.entrySet()) {
            if (partEntity.getValue().isForceCompleted()) {
                continue;
            }
            Integer partRow = partEntity.getKey();
            BigDecimal orderQty = partEntity.getValue().getOrderQty();
            BigDecimal totalDetailQty = BigDecimal.ZERO;
            BigDecimal newNirdQty = newNirdQtyMap.get(partRow);
            for (CPSSMF11PlanEntity planEntity : excelPlanList) {
                if (planEntity.getColumnType() == ColumnType.ACTUAL_DATA
                        || planEntity.getColumnType() == ColumnType.PLAN_DATA
                        || planEntity.getColumnType() == ColumnType.PLAN_NEW
                        || planEntity.getColumnType() == ColumnType.NIRD_DATA) {
                    Map<Integer, BigDecimal> qtyMap = planEntity.getQtyMap();
                    if (planEntity.getColumnType() == ColumnType.PLAN_DATA && planEntity.getModEntity() != null) {
                        qtyMap = planEntity.getModEntity().getQtyMap();
                    }
                    BigDecimal detailQty = qtyMap.get(partRow);
                    totalDetailQty = DecimalUtil.add(totalDetailQty, detailQty);
                    if (planEntity.getColumnType() == ColumnType.NIRD_DATA) {
                        Map<Integer, BigDecimal> leftQtyMap = planEntity.getLeftQtyMap();
                        BigDecimal leftQty = leftQtyMap.get(partRow);
                        if (DecimalUtil.isLessEquals(newNirdQty, BigDecimal.ZERO)) {
                            continue;
                        } else if (DecimalUtil.isGreater(newNirdQty, leftQty)) {
                            leftQtyMap.put(partRow, BigDecimal.ZERO);
                            newNirdQty = DecimalUtil.subtract(newNirdQty, leftQty);
                            planEntity.setNirdUpdate(true);
                        } else {
                            leftQtyMap.put(partRow, DecimalUtil.subtract(leftQty, newNirdQty));
                            newNirdQty = BigDecimal.ZERO;
                            planEntity.setNirdUpdate(true);
                        }
                    }
                }
            }

            // Not in Rundown QTY
            if (DecimalUtil.isGreater(newNirdQty, BigDecimal.ZERO)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_170);
                message.setMessageArgs(new String[] { String.valueOf(partRow) });
                messageLists.add(message);
            }

            // Order QTY
            if (!DecimalUtil.isEquals(orderQty, totalDetailQty)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_056);
                message.setMessageArgs(new String[] { String.valueOf(partRow) });
                messageLists.add(message);
            }
        }

        return isFormatError;
    }

    /**
     * Check whether the plan is input.
     * 
     * @param strTransportMode transport mode
     * @param strEtd ETD
     * @param strEta ETA
     * @param strCCDate CC Date
     * @param strInboundPlanDate inbound plan date
     * @param isInvoice invoice flag
     * @return check result
     */
    private boolean isPlanInput(String strTransportMode, String strEtd, String strEta, String strCCDate,
        String strInboundPlanDate, boolean isInvoice) {

        if (!isInvoice && !StringUtil.isEmpty(strTransportMode)) {
            return true;
        }
        if (!StringUtil.isEmpty(strEtd)) {
            return true;
        }
        if (!StringUtil.isEmpty(strEta)) {
            return true;
        }
        if (!StringUtil.isEmpty(strCCDate)) {
            return true;
        }
        if (!StringUtil.isEmpty(strInboundPlanDate)) {
            return true;
        }

        return false;
    }

    /**
     * Add distinct value to list.
     * 
     * @param <T> Object
     * @param list List
     * @param value Value
     */
    private <T extends Object> void addDistinct(List<T> list, T value) {

        if (!list.contains(value)) {
            list.add(value);
        }
    }

    /**
     * Column type.
     */
    public interface ColumnType {

        /** Dummy */
        public static final int DUMMY = 0;

        /** Plan Data */
        public static final int PLAN_DATA = 1;

        /** Plan MOD */
        public static final int PLAN_MOD = 2;

        /** Plan NEW */
        public static final int PLAN_NEW = 3;

        /** Actual Data */
        public static final int ACTUAL_DATA = 4;

        /** Actual MOD */
        public static final int ACTUAL_MOD = 5;

        /** NIRD Data */
        public static final int NIRD_DATA = 6;

        /** NIRD MOD */
        public static final int NIRD_MOD = 7;

        /** NIRD NEW */
        public static final int NIRD_NEW = 8;
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPSSMF11;
    }

}
