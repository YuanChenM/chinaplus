/**
 * CPVIVF12Controller.java
 * 
 * @screen CPVIVF12
 * @author gu_chengchen
 */
package com.chinaplus.web.inv.control;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
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
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.inv.entity.CPVIVF12Entity;
import com.chinaplus.web.inv.service.CPVIVF11Service;
import com.chinaplus.web.inv.service.CPVIVF12Service;

/**
 * Invoice Supplementary Upload Controller.
 */
@Controller
public class CPVIVF12Controller extends BaseFileController {

    /** Data start row */
    private static final int DATA_START_ROW = 5;

    /** Data start column */
    private static final int DATA_START_COL = 1;

    /** Data total column */
    private static final int DATA_TOTAL_COL = 16;

    /** separator */
    private static final String SEPARATOR = "#;!";

    /** Invoice Upload Service */
    @Autowired
    private CPVIVF11Service cpvivf11Service;

    /** Invoice Supplementary Upload Service */
    @Autowired
    private CPVIVF12Service cpvivf12Service;

    /**
     * Invoice Supplementary Upload.
     * 
     * @param file upload file
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the Exception
     */
    @RequestMapping(value = "/inv/CPVIVF12/upload",
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
     * @throws Exception the Exception
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) throws Exception {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Sheet sheet = workbook.getSheetAt(0);

        // Do input value check
        List<CPVIVF12Entity> dataList = new ArrayList<CPVIVF12Entity>();
        doInputCheck(messageLists, sheet, dataList);

        // Do relevance check
        doRelevanceCheck(messageLists, dataList);

        // Do business logic check
        doLogicCheck(param, messageLists, dataList, request);

        // Save upload data to DB
        if (messageLists.size() == 0) {
            // Check whether has upload data
            if (dataList.size() == 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
                messageLists.add(message);
            } else {
                // Save upload file to server
                String uploadId = dataList.get(0).getUploadId();
                File existFile = new File(ConfigUtil.get(Properties.UPLOAD_PATH_INVOICE), uploadId + StringConst.DOT
                        + FileType.ZIP.getSuffix());
                if (existFile.exists()) {
                    String tempPath = ConfigUtil.get(Properties.TEMPORARY_PATH);
                    File existFolder = new File(tempPath + uploadId);
                    FileUtil.extract(existFile, tempPath);
                    FileUtil.saveFileToPath(file.getInputStream(), tempPath + uploadId, file.getOriginalFilename());
                    FileUtil.compress(existFile, existFolder);
                    FileUtil.deleteAllFile(existFolder);
                }

                // Update supplementary invoice
                cpvivf12Service.doInvoiceUpdate(param, dataList);
            }
        }

        return messageLists;
    }

    /**
     * Do input value check.
     * 
     * @param messageLists message list
     * @param sheet excel work sheet
     * @param dataList upload data list
     */
    private void doInputCheck(List<BaseMessage> messageLists, Sheet sheet, List<CPVIVF12Entity> dataList) {

        int dataCol = DATA_START_COL;
        for (int dataRow = DATA_START_ROW; dataRow <= sheet.getLastRowNum() + 1; dataRow++) {
            if (ValidatorUtils.isBlankRow(sheet, dataRow, DATA_START_COL, DATA_TOTAL_COL)) {
                if (ValidatorUtils.isExcelEnd(sheet, dataRow, DATA_START_COL, DATA_TOTAL_COL)) {
                    break;
                } else {
                    continue;
                }
            } else {
                CPVIVF12Entity data = new CPVIVF12Entity();
                data.setRowNum(dataRow);
                dataList.add(data);

                // WEST Invoice No.
                String invoiceNo = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setInvoiceNo(invoiceNo);

                // ETD
                String strEtd = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                Date etd = DateTimeUtil.parseDate(strEtd);
                data.setEtd(etd);

                // TTC Customer Code
                String ttcCustomerCode = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setTtcCustomerCode(ttcCustomerCode);

                // Mail Invoice Customer Code
                String mailCustomerCode = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setMailCustomerCode(mailCustomerCode);

                // TTC Part No.
                String ttcPartNo = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setTtcPartNo(ttcPartNo);

                // Supplier Part No.
                String supplierPartNo = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setSupplierPartNo(supplierPartNo);

                // Total Qty
                String strTotalQty = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                BigDecimal totalQty = DecimalUtil.getBigDecimalWithNUll(strTotalQty);
                data.setTotalQty(totalQty);

                // Transport Mode
                String transportMode = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setTransportMode(transportMode);

                // TTC Supplier Code
                String ttcSupplierCode = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                data.setTtcSupplierCode(ttcSupplierCode);

                // Supplier Qty
                String strSupplierQty = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                BigDecimal supplierQty = null;
                if (StringUtil.isEmpty(strSupplierQty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_SupplierQty" });
                    messageLists.add(message);
                } else {
                    supplierQty = DecimalUtil.getBigDecimalWithNUll(strSupplierQty);
                    if (supplierQty == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_SupplierQty",
                            "Common_ItemType_Decimal" });
                        messageLists.add(message);
                    } else {
                        if (!ValidatorUtils.checkMaxDecimal(supplierQty)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_SupplierQty",
                                "10", "6" });
                            messageLists.add(message);
                        }
                        if (DecimalUtil.isLess(supplierQty, BigDecimal.ZERO)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                            message
                                .setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_SupplierQty" });
                            messageLists.add(message);
                        }
                    }
                    data.setSupplierQty(supplierQty);
                }

                // Order Month-1
                String strOrderMonth1 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strOrderMonth1)) {
                    Date orderMonth1 = DateTimeUtil.parseMonth(strOrderMonth1);
                    if (orderMonth1 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_OrderMonth1",
                            "Common_ItemType_Month" });
                        messageLists.add(message);
                    }
                    data.setOrderMonth1(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonth1));
                }

                // Qty-1
                String strQty1 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strQty1)) {
                    BigDecimal qty1 = DecimalUtil.getBigDecimalWithNUll(strQty1);
                    if (qty1 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty1",
                            "Common_ItemType_Decimal" });
                        messageLists.add(message);
                    } else {
                        if (!ValidatorUtils.checkMaxDecimal(qty1)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty1", "10",
                                "6" });
                            messageLists.add(message);
                        }
                        if (DecimalUtil.isLess(qty1, BigDecimal.ZERO)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty1" });
                            messageLists.add(message);
                        }
                    }
                    data.setQty1(qty1);
                }

                // Order Month-2
                String strOrderMonth2 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strOrderMonth2)) {
                    Date orderMonth2 = DateTimeUtil.parseMonth(strOrderMonth2);
                    if (orderMonth2 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_OrderMonth2",
                            "Common_ItemType_Month" });
                        messageLists.add(message);
                    }
                    data.setOrderMonth2(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonth2));
                }

                // Qty-2
                String strQty2 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strQty2)) {
                    BigDecimal qty2 = DecimalUtil.getBigDecimalWithNUll(strQty2);
                    if (qty2 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty2",
                            "Common_ItemType_Decimal" });
                        messageLists.add(message);
                    } else {
                        if (!ValidatorUtils.checkMaxDecimal(qty2)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty2", "10",
                                "6" });
                            messageLists.add(message);
                        }
                        if (DecimalUtil.isLess(qty2, BigDecimal.ZERO)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty2" });
                            messageLists.add(message);
                        }
                    }
                    data.setQty2(qty2);
                }

                // Order Month-3
                String strOrderMonth3 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strOrderMonth3)) {
                    Date orderMonth3 = DateTimeUtil.parseMonth(strOrderMonth3);
                    if (orderMonth3 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_OrderMonth3",
                            "Common_ItemType_Month" });
                        messageLists.add(message);
                    }
                    data.setOrderMonth3(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH, orderMonth3));
                }

                // Qty-3
                String strQty3 = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                if (!StringUtil.isEmpty(strQty3)) {
                    BigDecimal qty3 = DecimalUtil.getBigDecimalWithNUll(strQty3);
                    if (qty3 == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty3",
                            "Common_ItemType_Decimal" });
                        messageLists.add(message);
                    } else {
                        if (!ValidatorUtils.checkMaxDecimal(qty3)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty3", "10",
                                "6" });
                            messageLists.add(message);
                        }
                        if (DecimalUtil.isLess(qty3, BigDecimal.ZERO)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_029);
                            message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_Qty3" });
                            messageLists.add(message);
                        }
                    }
                    data.setQty3(qty3);
                }

                // ETA
                String strEta = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                Date eta = null;
                if (StringUtil.isEmpty(strEta)) {
                    if (DecimalUtil.isGreater(supplierQty, BigDecimal.ZERO)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_ETA" });
                        messageLists.add(message);
                    }
                } else {
                    eta = DateTimeUtil.parseDate(strEta);
                    if (eta == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_ETA",
                            "Common_ItemType_Date" });
                        messageLists.add(message);
                    }
                }
                data.setEta(eta);

                // Inbound Date
                String strInboundDate = PoiUtil.getStringCellValue(sheet, dataRow, dataCol++);
                Date inboundDate = null;
                if (StringUtil.isEmpty(strInboundDate)) {
                    if (DecimalUtil.isGreater(supplierQty, BigDecimal.ZERO)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_InboundDate" });
                        messageLists.add(message);
                    }
                } else {
                    inboundDate = DateTimeUtil.parseDate(strInboundDate);
                    if (inboundDate == null) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_InboundDate",
                            "Common_ItemType_Date" });
                        messageLists.add(message);
                    }
                    if (eta != null && inboundDate != null && inboundDate.compareTo(eta) < 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_108);
                        message.setMessageArgs(new String[] { String.valueOf(dataRow), "CPVIVF01_Grid_InboundDate",
                            "CPVIVF01_Grid_ETA" });
                        messageLists.add(message);
                    }
                }
                data.setInboundDate(inboundDate);

                // reset data start col
                dataCol = DATA_START_COL;
            }
        }
    }

    /**
     * Do relevance check.
     * 
     * @param messageLists message list
     * @param dataList upload data list
     */
    private void doRelevanceCheck(List<BaseMessage> messageLists, List<CPVIVF12Entity> dataList) {

        List<String> dataKeyList = new ArrayList<String>();
        Map<String, BigDecimal[]> qtyMap = new LinkedHashMap<String, BigDecimal[]>();
        for (CPVIVF12Entity data : dataList) {
            int rowNum = data.getRowNum();
            String orderMonth1 = StringUtil.toSafeString(data.getOrderMonth1());
            String orderMonth2 = StringUtil.toSafeString(data.getOrderMonth2());
            String orderMonth3 = StringUtil.toSafeString(data.getOrderMonth3());
            BigDecimal qty1 = data.getQty1();
            BigDecimal qty2 = data.getQty2();
            BigDecimal qty3 = data.getQty3();
            BigDecimal supplierQty = data.getSupplierQty();
            BigDecimal totalQty = data.getTotalQty();
            String mapKey = StringUtil.toSafeString(data.getInvoiceNo()) + SEPARATOR
                    + StringUtil.toSafeString(data.getTtcCustomerCode()) + SEPARATOR
                    + StringUtil.toSafeString(data.getSupplierPartNo());
            BigDecimal[] qtyArray = qtyMap.get(mapKey);
            if (qtyArray == null) {
                if (totalQty == null) {
                    totalQty = BigDecimal.ZERO;
                }
                qtyArray = new BigDecimal[IntDef.INT_TWO];
                qtyArray[IntDef.INT_ZERO] = totalQty;
                qtyMap.put(mapKey, qtyArray);
            }
            qtyArray[IntDef.INT_ONE] = DecimalUtil.add(qtyArray[IntDef.INT_ONE], supplierQty);
            String dataKey = mapKey + SEPARATOR + StringUtil.toSafeString(data.getTtcSupplierCode());
            if (!dataKeyList.contains(dataKey)) {
                dataKeyList.add(dataKey);
            } else {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_124);
                message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                messageLists.add(message);
            }

            // If there are duplicate in Order Month-1~Order Month-3
            if ((!StringUtil.isEmpty(orderMonth1) && orderMonth1.equals(orderMonth2))
                    || (!StringUtil.isEmpty(orderMonth1) && orderMonth1.equals(orderMonth3))
                    || (!StringUtil.isEmpty(orderMonth2) && orderMonth2.equals(orderMonth3))) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_110);
                message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                messageLists.add(message);
            }

            // If SUM(Qty-1~Qty-3) != Supplier Qty
            BigDecimal sumQty = DecimalUtil.add(DecimalUtil.add(qty1, qty2), qty3);
            if (supplierQty == null) {
                supplierQty = BigDecimal.ZERO;
            }
            if (!DecimalUtil.isEquals(sumQty, supplierQty)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_111);
                message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                messageLists.add(message);
            }

            // If Order Month-X is not blank and Qty-X is blank or Order Month-X is blank and Qty-X is not blank
            boolean isMonth1Error = (StringUtil.isEmpty(orderMonth1) && qty1 != null)
                    || (!StringUtil.isEmpty(orderMonth1) && qty1 == null);
            boolean isMonth2Error = (StringUtil.isEmpty(orderMonth2) && qty2 != null)
                    || (!StringUtil.isEmpty(orderMonth2) && qty2 == null);
            boolean isMonth3Error = (StringUtil.isEmpty(orderMonth3) && qty3 != null)
                    || (!StringUtil.isEmpty(orderMonth3) && qty3 == null);
            if (isMonth1Error || isMonth2Error || isMonth3Error) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_112);
                message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                messageLists.add(message);
            }
        }

        // If SUM(Supplier Qty) GROUP BY TTC Part No. != Part Total Qty
        for (Map.Entry<String, BigDecimal[]> entry : qtyMap.entrySet()) {
            String mapKey = entry.getKey();
            String[] keyArray = mapKey.split(SEPARATOR);
            BigDecimal[] qtyArray = entry.getValue();
            if (!DecimalUtil.isEquals(qtyArray[0], qtyArray[1])) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_109);
                message.setMessageArgs(new String[] { keyArray[IntDef.INT_ZERO], keyArray[IntDef.INT_ONE],
                    keyArray[IntDef.INT_TWO] });
                messageLists.add(message);
            }
        }
    }

    /**
     * Do business logic check.
     * 
     * @param param the parameters
     * @param messageLists message list
     * @param dataList upload data list
     * @param request HttpServletRequest
     */
    private void doLogicCheck(BaseParam param, List<BaseMessage> messageLists, List<CPVIVF12Entity> dataList,
        HttpServletRequest request) {

        List<String> customerCodes = new ArrayList<String>();
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        List<com.chinaplus.common.bean.BusinessPattern> currentCustoemrs = um.getCurrentBusPattern();
        for (com.chinaplus.common.bean.BusinessPattern currentCustoemr : currentCustoemrs) {
            if (BusinessPattern.AISIN == currentCustoemr.getBusinessPattern()) {
                customerCodes.add(currentCustoemr.getCustomerCode());
            }
        }
        List<String> errCusList = new ArrayList<String>();
        List<String> uploadIdList = new ArrayList<String>();
        List<String> uploadKeyList = new ArrayList<String>();
        boolean isInvoiceRouteError = false;
        Map<String, Date[]> invoiceDateMap = new HashMap<String, Date[]>();
        for (CPVIVF12Entity data : dataList) {
            int rowNum = data.getRowNum();
            BigDecimal totalQty = data.getTotalQty();
            String invoiceNo = data.getInvoiceNo();
            Date etd = data.getEtd();
            Date eta = data.getEta();
            Date inboundDate = data.getInboundDate();
            String orderMonth1 = data.getOrderMonth1();
            String orderMonth2 = data.getOrderMonth2();
            String orderMonth3 = data.getOrderMonth3();
            uploadKeyList.add(data.getInvoiceNo() + SEPARATOR + data.getTtcCustomerCode() + SEPARATOR
                    + data.getSupplierPartNo());
            if (!isInvoiceRouteError) {
                Date[] invoiceDates = invoiceDateMap.get(invoiceNo);
                if (invoiceDates == null) {
                    invoiceDates = new Date[IntDef.INT_THREE];
                    invoiceDates[IntDef.INT_ZERO] = etd;
                    invoiceDates[IntDef.INT_ONE] = eta;
                    invoiceDates[IntDef.INT_TWO] = inboundDate;
                    invoiceDateMap.put(invoiceNo, invoiceDates);
                } else if (!isDateEqual(invoiceDates[IntDef.INT_ZERO], etd)
                        || !isDateEqual(invoiceDates[IntDef.INT_ONE], eta)
                        || !isDateEqual(invoiceDates[IntDef.INT_TWO], inboundDate)) {
                    isInvoiceRouteError = true;
                }
            }

            // Find invoice&part information from DB
            if (!StringUtil.isEmpty(data.getTtcCustomerCode()) && !customerCodes.contains(data.getTtcCustomerCode())) {
                addDistinct(errCusList, data.getTtcCustomerCode());
            }
            data.setOfficeId(param.getCurrentOfficeId());
            CPVIVF12Entity invoicePart = cpvivf12Service.getInvoicePart(data);
            if (invoicePart == null) {
                // Not exists
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_113);
                message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                messageLists.add(message);
            } else {
                String uploadId = invoicePart.getUploadId();
                if (!uploadIdList.contains(uploadId)) {
                    uploadIdList.add(uploadId);
                }
                Integer customerId = invoicePart.getCustomerId();
                Integer supplierId = invoicePart.getSupplierId();

                // Set DB data to upload data
                data.setUploadId(uploadId);
                data.setInvoiceSummaryId(invoicePart.getInvoiceSummaryId());
                data.setPartsId(invoicePart.getPartsId());
                data.setCustomerId(customerId);
                data.setSupplierId(supplierId);

                // Check UOM
                int digits = MasterManager.getUomDigits(invoicePart.getUomCode());
                // Totol Qty
                if (totalQty != null && totalQty.scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_TotalQty",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }
                // Supplier Qty
                if (data.getSupplierQty() != null && data.getSupplierQty().scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_SupplierQty",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }
                // Qty-1
                if (data.getQty1() != null && data.getQty1().scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_Qty1",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }
                // Qty-2
                if (data.getQty2() != null && data.getQty2().scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_Qty2",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }
                // Qty-3
                if (data.getQty3() != null && data.getQty3().scale() > digits) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_159);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_Qty3",
                        String.valueOf(digits) });
                    messageLists.add(message);
                }

                // Check total qty is equals with DB qty
                BigDecimal dbQty = invoicePart.getQty();
                BigDecimal dbFormatQty = StringUtil.toBigDecimal(DecimalUtil.format(dbQty, invoicePart.getUomCode())
                    .replaceAll(StringConst.COMMA, StringConst.EMPTY));
                if (!DecimalUtil.isEquals(totalQty, dbFormatQty)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_114);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                    messageLists.add(message);
                }

                // Check ETA
                Date dbEta = invoicePart.getEta();
                if (dbEta != null && !dbEta.equals(eta)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_116);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_ETA" });
                    messageLists.add(message);
                }

                // Check Inbound Plan Date
                Date dbInboundDate = invoicePart.getInboundDate();
                if (dbInboundDate != null && !dbInboundDate.equals(inboundDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_116);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum), "CPVIVF01_Grid_InboundDate" });
                    messageLists.add(message);
                }

                // Check Kanban Plan
                boolean isKanbanError = false;
                if (!StringUtil.isEmpty(orderMonth1)) {
                    String kanbanPlanNo1 = cpvivf11Service.getKanbanPlanNo(param.getCurrentOfficeId(), customerId,
                        supplierId, orderMonth1);
                    if (StringUtil.isEmpty(kanbanPlanNo1)) {
                        isKanbanError = true;
                    } else {
                        data.setKanbanPlanNo1(kanbanPlanNo1);
                    }
                }
                if (!StringUtil.isEmpty(orderMonth2)) {
                    String kanbanPlanNo2 = cpvivf11Service.getKanbanPlanNo(param.getCurrentOfficeId(), customerId,
                        supplierId, orderMonth2);
                    if (StringUtil.isEmpty(kanbanPlanNo2)) {
                        isKanbanError = true;
                    } else {
                        data.setKanbanPlanNo2(kanbanPlanNo2);
                    }
                }
                if (!StringUtil.isEmpty(orderMonth3)) {
                    String kanbanPlanNo3 = cpvivf11Service.getKanbanPlanNo(param.getCurrentOfficeId(), customerId,
                        supplierId, orderMonth3);
                    if (StringUtil.isEmpty(kanbanPlanNo3)) {
                        isKanbanError = true;
                    } else {
                        data.setKanbanPlanNo3(kanbanPlanNo3);
                    }
                }
                if (isKanbanError) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_117);
                    message.setMessageArgs(new String[] { String.valueOf(rowNum) });
                    messageLists.add(message);
                }
            }
        }

        // Check role
        for (String cusCode : errCusList) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
            message.setMessageArgs(new String[] { cusCode });
            messageLists.add(message);
        }

        // Check Upload ID
        if (uploadIdList.size() > 0) {
            if (uploadIdList.size() > 1) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_118);
                messageLists.add(message);
            } else {
                String uploadId = uploadIdList.get(0);
                List<CPVIVF12Entity> supplementaryDatas = cpvivf12Service.getAllSupplementaryData(uploadId);
                if (supplementaryDatas != null && supplementaryDatas.size() > 0) {
                    for (CPVIVF12Entity supplementaryData : supplementaryDatas) {
                        String invoiceNo = supplementaryData.getInvoiceNo();
                        String ttcCustomerCode = supplementaryData.getTtcCustomerCode();
                        String supplierPartNo = supplementaryData.getSupplierPartNo();
                        String key = invoiceNo + SEPARATOR + ttcCustomerCode + SEPARATOR + supplierPartNo;
                        if (!uploadKeyList.contains(key)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_119);
                            message.setMessageArgs(new String[] { invoiceNo, ttcCustomerCode, supplierPartNo });
                            messageLists.add(message);
                        }
                    }
                }
            }
        }

        // Check invoice ETD/ETA/Inbound Date
        if (isInvoiceRouteError) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_173);
            messageLists.add(message);
        }
    }

    /**
     * Check date equal.
     * 
     * @param date1 date 1
     * @param date2 date 2
     * @return check result
     */
    private boolean isDateEqual(Date date1, Date date2) {

        if (date1 == null || date2 == null) {
            return true;
        }

        return date1.equals(date2);
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

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {

        return FileId.CPVIVF12;
    }

}
