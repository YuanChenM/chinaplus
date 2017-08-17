/**
 * CPVIVF13Controller.java
 * 
 * @screen CPVIVF13
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.chinaplus.common.entity.TntInvoiceSummary;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseEntity;
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
import com.chinaplus.web.inv.entity.CPVIVF13Entity;
import com.chinaplus.web.inv.service.CPVIVF13Service;

/**
 * New Invoice Upload Controller.
 */
@Controller
public class CPVIVF13Controller extends BaseFileController {

    /** Invoice start row */
    private static final int INVOICE_START_ROW = 2;

    /** Invoice start column */
    private static final int INVOICE_START_COL = 9;

    /** Invoice number */
    private static final int INVOICE_NUMBER = 6;

    /** Part start row */
    private static final int PART_START_ROW = 10;

    /** Part start column */
    private static final int PART_START_COL = 1;

    /** Part total column */
    private static final int PART_TOTAL_COL = 14;

    /** Title Row Index */
    private static final int TITLE_ROW_INDEX = 8;

    /** New Invoice Upload Service */
    @Autowired
    private CPVIVF13Service cpvivf13Service;

    /**
     * New Invoice Upload.
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVF13/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        super.setCommonParam(param, request);
        BaseResult<BaseEntity> baseResult = super.uploadFileProcess(file, FileType.EXCEL, param, request, response);
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

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Sheet sheet = workbook.getSheetAt(0);
        int totalCols = sheet.getRow(TITLE_ROW_INDEX).getLastCellNum();
        if (totalCols != PART_TOTAL_COL) {
            throw new BusinessException(MessageCodeConst.W1014);
        }

        // Get and check invoice data
        Map<Integer, TntInvoiceSummary> invoiceMap = new HashMap<Integer, TntInvoiceSummary>();
        getAndCheckInvoice(param, messageLists, sheet, invoiceMap);

        // Get and check part data
        List<CPVIVF13Entity> partList = new ArrayList<CPVIVF13Entity>();
        getAndCheckPart(param, messageLists, sheet, invoiceMap, partList, request);

        // Relevance check for invoice and part
        checkRelevance(messageLists, invoiceMap, partList);

        // Save upload data to DB
        if (messageLists.size() == 0) {
            cpvivf13Service.doInvoiceInsert(param, invoiceMap, partList);
        }

        return messageLists;
    }

    /**
     * Get and check invoice data.
     * 
     * @param param the parameters
     * @param messageLists message list
     * @param sheet excel work sheet
     * @param invoiceMap upload invoice map
     */
    private void getAndCheckInvoice(BaseParam param, List<BaseMessage> messageLists, Sheet sheet,
        Map<Integer, TntInvoiceSummary> invoiceMap) {

        List<String> invoiceNoList = new ArrayList<String>();
        int invoiceRow = INVOICE_START_ROW;
        for (int invoiceCol = INVOICE_START_COL; invoiceCol < INVOICE_START_COL + INVOICE_NUMBER; invoiceCol++) {
            // Invoice Qty From
            String strInvoiceQtyFrom = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Integer invoiceQtyFrom = null;
            if (!StringUtil.isEmpty(strInvoiceQtyFrom)) {
                invoiceQtyFrom = CodeCategoryManager.getCodeValue(Language.ENGLISH.getCode(),
                    CodeMasterCategory.INVOICE_ISSUE_TYPE, strInvoiceQtyFrom);
                if (invoiceQtyFrom == null) {
                    invoiceQtyFrom = CodeCategoryManager.getCodeValue(Language.CHINESE.getCode(),
                        CodeMasterCategory.INVOICE_ISSUE_TYPE, strInvoiceQtyFrom);
                }
                if (invoiceQtyFrom == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_054);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol),
                        "CPVIVF03_Grid_InvoiceQtyFrom" });
                    messageLists.add(message);
                }
            }

            // Invoice No.
            String invoiceNo = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            if (!StringUtil.isEmpty(invoiceNo)) {
                if (!ValidatorUtils.isHalf(invoiceNo)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol),
                        "CPVIVF03_Grid_InvoiceNo", "Common_ItemType_Half" });
                    messageLists.add(message);
                }
                if (!ValidatorUtils.maxLengthValidator(invoiceNo, IntDef.INT_THIRTY)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_081);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol),
                        "CPVIVF03_Grid_InvoiceNo", "30" });
                    messageLists.add(message);
                }
                if (invoiceNoList.contains(invoiceNo) || cpvivf13Service.isInvoiceExist(invoiceNo)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_091);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), invoiceNo });
                    messageLists.add(message);
                } else {
                    invoiceNoList.add(invoiceNo);
                }
            }

            // Transport Mode
            String strTransportMode = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Integer transportMode = null;
            if (!StringUtil.isEmpty(strTransportMode)) {
                transportMode = CodeCategoryManager.getCodeValue(Language.ENGLISH.getCode(),
                    CodeMasterCategory.TRANSPORT_MODE, strTransportMode);
                if (transportMode == null) {
                    transportMode = CodeCategoryManager.getCodeValue(Language.CHINESE.getCode(),
                        CodeMasterCategory.TRANSPORT_MODE, strTransportMode);
                }
                if (transportMode == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_054);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol),
                        "CPVIVF03_Grid_TransportMode" });
                    messageLists.add(message);
                }
            }

            // ETD
            String strEtd = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Date etd = null;
            if (!StringUtil.isEmpty(strEtd)) {
                etd = DateTimeUtil.parseDate(strEtd);
                if (etd == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_ETD",
                        "Common_ItemType_Date" });
                    messageLists.add(message);
                }
            }

            // ETA
            String strEta = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Date eta = null;
            if (!StringUtil.isEmpty(strEta)) {
                eta = DateTimeUtil.parseDate(strEta);
                if (eta == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_ETA",
                        "Common_ItemType_Date" });
                    messageLists.add(message);
                }
                if (etd != null && eta != null && eta.compareTo(etd) < 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_ETA",
                        "CPVIVF03_Grid_ETD" });
                    messageLists.add(message);
                }
            }

            // GR Date
            String strGrDate = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Date grDate = null;
            if (!StringUtil.isEmpty(strGrDate)) {
                grDate = DateTimeUtil.parseDate(strGrDate);
                if (grDate == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_GRDate",
                        "Common_ItemType_Date" });
                    messageLists.add(message);
                }
            }

            // GI Date
            String strGiDate = PoiUtil.getStringCellValue(sheet, invoiceRow++, invoiceCol);
            Date giDate = null;
            if (!StringUtil.isEmpty(strGiDate)) {
                giDate = DateTimeUtil.parseDate(strGiDate);
                if (giDate == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_053);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_GIDate",
                        "Common_ItemType_Date" });
                    messageLists.add(message);
                }
                if (grDate != null && giDate != null && giDate.compareTo(grDate) < 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_055);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol), "CPVIVF03_Grid_GIDate",
                        "CPVIVF03_Grid_GRDate" });
                    messageLists.add(message);
                }
            }

            // check whether all input
            boolean isOneInput = !StringUtil.isEmpty(strInvoiceQtyFrom) || !StringUtil.isEmpty(invoiceNo)
                    || !StringUtil.isEmpty(strTransportMode) || !StringUtil.isEmpty(strEtd)
                    || !StringUtil.isEmpty(strEta) || !StringUtil.isEmpty(strGrDate) || !StringUtil.isEmpty(strGiDate);
            boolean isOneEmpty = StringUtil.isEmpty(strInvoiceQtyFrom) || StringUtil.isEmpty(invoiceNo)
                    || StringUtil.isEmpty(strTransportMode) || StringUtil.isEmpty(strEtd)
                    || StringUtil.isEmpty(strGrDate) || StringUtil.isEmpty(strGiDate);
            if (isOneInput && isOneEmpty) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_082);
                message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(invoiceCol) });
                messageLists.add(message);
            }

            // create invoice information data
            TntInvoiceSummary invoiceInfo = null;
            if (isOneInput) {
                invoiceInfo = new TntInvoiceSummary();
                invoiceInfo.setIssueType(invoiceQtyFrom);
                invoiceInfo.setInvoiceNo(invoiceNo);
                invoiceInfo.setTransportMode(transportMode);
                invoiceInfo.setEtd(etd);
                invoiceInfo.setEta(eta);
                invoiceInfo.setGrDate(grDate);
                invoiceInfo.setGiDate(giDate);
            }
            invoiceMap.put(invoiceCol, invoiceInfo);

            // reset invoice start row
            invoiceRow = INVOICE_START_ROW;
        }
    }

    /**
     * Get and check part data.
     * 
     * @param param the parameters
     * @param messageLists message list
     * @param sheet excel work sheet
     * @param invoiceMap upload invoice map
     * @param partList upload part list
     * @param request HttpServletRequest
     */
    private void getAndCheckPart(BaseParam param, List<BaseMessage> messageLists, Sheet sheet,
        Map<Integer, TntInvoiceSummary> invoiceMap, List<CPVIVF13Entity> partList, HttpServletRequest request) {

        List<String> customerCodes = new ArrayList<String>();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        List<com.chinaplus.common.bean.BusinessPattern> currentCustoemrs = um.getCurrentBusPattern();
        for (com.chinaplus.common.bean.BusinessPattern currentCustoemr : currentCustoemrs) {
            if (BusinessPattern.V_V == currentCustoemr.getBusinessPattern()) {
                customerCodes.add(currentCustoemr.getCustomerCode());
            }
        }

        List<Integer> emptyInvoiceList = new ArrayList<Integer>();
        List<String> partKeyList = new ArrayList<String>();
        List<String> errCusList = new ArrayList<String>();
        int partCol = PART_START_COL;
        for (int partRow = PART_START_ROW; partRow <= sheet.getLastRowNum() + 1; partRow++) {
            if (ValidatorUtils.isBlankRow(sheet, partRow, PART_START_COL, PART_TOTAL_COL)) {
                if (ValidatorUtils.isExcelEnd(sheet, partRow, PART_START_COL, PART_TOTAL_COL)) {
                    break;
                } else {
                    continue;
                }
            } else {
                CPVIVF13Entity partData = new CPVIVF13Entity();
                partList.add(partData);

                // TTC Part No.
                String ttcPartNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
                partData.setTtcPartNo(ttcPartNo);

                // Parts Description (Chinese)
                partCol++;

                // Import Order No.
                String impOrderNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
                partData.setImpOrderNo(impOrderNo);

                // Export Order No.
                String expOrderNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
                partData.setExpOrderNo(expOrderNo);

                // Customer Order No.
                String cusOrderNo = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
                partData.setCusOrderNo(cusOrderNo);

                // TTC Customer Code
                String ttcCustomerCode = PoiUtil.getStringCellValue(sheet, partRow, partCol++);
                partData.setTtcCustomerCode(ttcCustomerCode);

                // TTC Supplier Code
                partCol++;

                // Check part repeated
                String partKey = ttcPartNo + StringConst.COMMA + impOrderNo + StringConst.COMMA + expOrderNo
                        + StringConst.COMMA + cusOrderNo + StringConst.COMMA + ttcCustomerCode;
                if (!partKeyList.contains(partKey)) {
                    partKeyList.add(partKey);
                } else {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_124);
                    message.setMessageArgs(new String[] { String.valueOf(partRow) });
                    messageLists.add(message);
                }

                // Check part exist
                CPVIVF13Entity checkConditon = new CPVIVF13Entity();
                checkConditon.setOfficeId(param.getCurrentOfficeId());
                checkConditon.setImpOrderNo(impOrderNo);
                checkConditon.setExpOrderNo(expOrderNo);
                checkConditon.setCusOrderNo(cusOrderNo);
                checkConditon.setTtcPartNo(ttcPartNo);
                checkConditon.setTtcCustomerCode(ttcCustomerCode);
                if (!StringUtil.isEmpty(ttcCustomerCode) && !customerCodes.contains(ttcCustomerCode)) {
                    addDistinct(errCusList, ttcCustomerCode);
                }
                CPVIVF13Entity partInfo = cpvivf13Service.getPartInfo(checkConditon);
                if (partInfo == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_083);
                    message.setMessageArgs(new String[] { String.valueOf(partRow) });
                    messageLists.add(message);
                } else {
                    partData.setPartsId(partInfo.getPartsId());
                    partData.setSupplierId(partInfo.getSupplierId());
                    partData.setSupplierCode(partInfo.getSupplierCode());
                    partData.setExpRegion(partInfo.getExpRegion());
                    partData.setImpRegion(partInfo.getImpRegion());
                }

                // Invoice Qty
                Map<Integer, BigDecimal> invoiceQtyMap = new HashMap<Integer, BigDecimal>();
                partData.setInvoiceQtyMap(invoiceQtyMap);
                for (int i = INVOICE_START_COL; i < INVOICE_START_COL + INVOICE_NUMBER; i++) {
                    String invoiceQty = PoiUtil.getStringCellValue(sheet, partRow, i);
                    BigDecimal mapQty = null;
                    if (!StringUtil.isEmpty(invoiceQty)) {
                        BigDecimal decimalQty = DecimalUtil.getBigDecimalWithNUll(invoiceQty);
                        if (decimalQty == null) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_026);
                            message.setMessageArgs(new String[] { String.valueOf(partRow), ExcelUtil.colIndexToStr(i),
                                "CPVIVF03_Grid_InvoiceQty", "Common_ItemType_Decimal" });
                            messageLists.add(message);
                        } else {
                            if (!DecimalUtil.isEquals(decimalQty, BigDecimal.ZERO)) {
                                mapQty = decimalQty;
                            }
                            if (!ValidatorUtils.checkMaxDecimal(decimalQty)) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_028);
                                message.setMessageArgs(new String[] { String.valueOf(partRow),
                                    ExcelUtil.colIndexToStr(i), "CPVIVF03_Grid_InvoiceQty", "10", "6" });
                                messageLists.add(message);
                            }
                            if (DecimalUtil.isLess(decimalQty, BigDecimal.ZERO)) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_030);
                                message.setMessageArgs(new String[] { String.valueOf(partRow),
                                    ExcelUtil.colIndexToStr(i), "CPVIVF03_Grid_InvoiceQty" });
                                messageLists.add(message);
                            }
                            if (partInfo != null) {
                                int digits = MasterManager.getUomDigits(partInfo.getUomCode());
                                if (decimalQty.scale() > digits) {
                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_160);
                                    message
                                        .setMessageArgs(new String[] { String.valueOf(partRow),
                                            ExcelUtil.colIndexToStr(i), "CPVIVF03_Grid_InvoiceQty",
                                            String.valueOf(digits) });
                                    messageLists.add(message);
                                }
                            }
                        }
                    }
                    if (mapQty != null && invoiceMap.get(i) == null && !emptyInvoiceList.contains(i)) {
                        emptyInvoiceList.add(i);
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_123);
                        message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(i) });
                        messageLists.add(message);
                    }
                    invoiceQtyMap.put(i, mapQty);
                }

                // reset part start col
                partCol = PART_START_COL;
            }
        }

        // Check role
        for (String cusCode : errCusList) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
            message.setMessageArgs(new String[] { cusCode });
            messageLists.add(message);
        }
    }

    /**
     * Relevance check for invoice and part.
     * 
     * @param messageLists message list
     * @param invoiceMap upload invoice map
     * @param partList upload part list
     */
    private void checkRelevance(List<BaseMessage> messageLists, Map<Integer, TntInvoiceSummary> invoiceMap,
        List<CPVIVF13Entity> partList) {

        for (Map.Entry<Integer, TntInvoiceSummary> entry : invoiceMap.entrySet()) {
            boolean hasInvoiceQty = false;
            BigDecimal totalQty = BigDecimal.ZERO;
            List<String> expRegionList = new ArrayList<String>();
            List<String> impRegionList = new ArrayList<String>();
            List<String> supplierCodeList = new ArrayList<String>();
            TntInvoiceSummary invoiceData = entry.getValue();
            if (invoiceData != null) {
                for (CPVIVF13Entity partData : partList) {
                    Map<Integer, BigDecimal> invoiceQtyMap = partData.getInvoiceQtyMap();
                    BigDecimal invoiceQty = invoiceQtyMap.get(entry.getKey());
                    if (invoiceQty != null) {
                        totalQty = DecimalUtil.add(totalQty, invoiceQty);
                        hasInvoiceQty = true;
                        if (!StringUtil.isEmpty(partData.getExpRegion())
                                && !expRegionList.contains(partData.getExpRegion())) {
                            expRegionList.add(partData.getExpRegion());
                        }
                        if (!StringUtil.isEmpty(partData.getImpRegion())
                                && !impRegionList.contains(partData.getImpRegion())) {
                            impRegionList.add(partData.getImpRegion());
                        }
                        if (!StringUtil.isEmpty(partData.getSupplierCode())
                                && !supplierCodeList.contains(partData.getSupplierCode())) {
                            supplierCodeList.add(partData.getSupplierCode());
                        }
                    }
                }

                // Set total invoice qty
                invoiceData.setInvoiceQty(totalQty);

                // Set supplier code
                if (supplierCodeList.size() > 0) {
                    Collections.sort(supplierCodeList);
                    StringBuffer sbCode = new StringBuffer();
                    for (String code : supplierCodeList) {
                        sbCode.append(StringConst.COMMA).append(code);
                    }
                    invoiceData.setSupplierCodeSet(sbCode.toString().substring(1));
                }

                // If the whole qty in one invoice No. is 0 or blank
                if (!hasInvoiceQty) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_085);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(entry.getKey()) });
                    messageLists.add(message);
                }

                // If with invoice qty's part Exp Region is not unique
                if (expRegionList.size() > 1) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_084);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(entry.getKey()),
                        "CPVIVF03_Grid_ExpRegion" });
                    messageLists.add(message);
                } else if (expRegionList.size() != 0) {
                    invoiceData.setExpRegion(expRegionList.get(0));
                }

                // If with invoice qty's part Imp Region is not unique
                if (impRegionList.size() > 1) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_084);
                    message.setMessageArgs(new String[] { ExcelUtil.colIndexToStr(entry.getKey()),
                        "CPVIVF03_Grid_ImpRegion" });
                    messageLists.add(message);
                } else if (impRegionList.size() != 0) {
                    invoiceData.setImpRegion(impRegionList.get(0));
                }
            }
        }
    }

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPVIVF13;
    }

    /**
     * Add distinct value to list.
     * 
     * @param <T> the object
     * @param list the list
     * @param value the add value
     */
    private <T extends Object> void addDistinct(List<T> list, T value) {

        if (!list.contains(value)) {
            list.add(value);
        }
    }

}
