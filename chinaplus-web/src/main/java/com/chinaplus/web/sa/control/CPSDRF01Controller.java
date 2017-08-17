/**
 * CPSDRF01Controller.java
 * 
 * @screen CPSKSS01
 * @author shi_yuxi
 */
package com.chinaplus.web.sa.control;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSDRF01Entity;
import com.chinaplus.web.sa.service.CPSDRF01Service;

/**
 * kpi download
 */
@Controller
public class CPSDRF01Controller extends BaseFileController {
    private static final String SHEET_STYLE = "style";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    private static final String DOWNLOAD_ZIP_FILE_NAME = "ETD Delay Report_{0}.zip";

    private static final String VV = "(V-V)";
    private static final String AISIN = "(AISIN)";

    private static final String VV_ENTITY = "ETD Delay Report_[V-V]_{0}.xlsx";
    private static final String AISIN_ENTITY = "ETD Delay Report_[AISIN]_{0}.xlsx";

    private static final String SHEET_NAME = "ETD_Delay_Report";
    private static final String QTY_STYLE_SHEET = "qty_style";
    private static final String GREY_QTY_STYLE_SHEET = "grey_qty_style";
    private static final String RED_QTY_STYLE_SHEET = "red_qty_style";
    @Autowired
    private CPSDRF01Service service;

    @Override
    protected String getFileId() {
        return FileId.CPSDRF01;
    }

    /**
     * Order Calculation Supporting Data Summary Downloadcheck
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @RequestMapping(value = "/sa/CPSDRF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        String businessPattern = (String) param.getSwapData().get("businessPattern");
        Date oidFrom = DateTimeUtil.parseDate((String) param.getSwapData().get("oidFromString"),
            DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        Date oidTo = DateTimeUtil.parseDate((String) param.getSwapData().get("oidToString"),
            DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        Date orderMonthFrom = DateTimeUtil.parseDate((String) param.getSwapData().get("orderMonthFrom"),
            DateTimeUtil.FORMAT_YEAR_MONTH);
        Date orderMonthTo = DateTimeUtil.parseDate((String) param.getSwapData().get("orderMonthTo"),
            DateTimeUtil.FORMAT_YEAR_MONTH);
        Date etdFrom = DateTimeUtil.parseDate((String) param.getSwapData().get("etdFromString"),
            DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        Date etdTo = DateTimeUtil.parseDate((String) param.getSwapData().get("etdToString"),
            DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        String compareBp = String.valueOf(BusinessPattern.V_V) + StringConst.COMMA
                + String.valueOf(BusinessPattern.AISIN);
        String compareBp2 = String.valueOf(BusinessPattern.AISIN) + StringConst.COMMA
                + String.valueOf(BusinessPattern.V_V);
        if (StringUtil.isEmpty(businessPattern)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPSKSS01_Label_BusinessPattern" });
            messageLists.add(message);
        } else {
            if (String.valueOf(BusinessPattern.V_V).equals(businessPattern)) {
                if (oidFrom == null || oidTo == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPSKSS01_Label_SalesOrderDate" });
                    messageLists.add(message);
                }
            } else if (String.valueOf(BusinessPattern.AISIN).equals(businessPattern)) {
                if (orderMonthFrom == null || orderMonthTo == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPSKSS01_Label_OrderMonth" });
                    messageLists.add(message);
                }
            } else if (compareBp.equals(businessPattern) || compareBp2.equals(businessPattern)) {
                if (oidFrom == null || oidTo == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPSKSS01_Label_SalesOrderDate" });
                    messageLists.add(message);
                }
                if (orderMonthFrom == null || orderMonthTo == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                    message.setMessageArgs(new String[] { "CPSKSS01_Label_OrderMonth" });
                    messageLists.add(message);
                }
            }
        }
        if (etdFrom == null || etdTo == null) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
            message.setMessageArgs(new String[] { "CPSKSS01_Label_ETD" });
            messageLists.add(message);
        } else {
            if (etdFrom.getTime() > etdTo.getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { "CPSDRF01_Start_Etd", "CPSDRF01_End_Etd" });
                messageLists.add(message);
            }
        }
        if (oidFrom != null && oidTo != null) {
            if (oidFrom.getTime() > oidTo.getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_021);
                message.setMessageArgs(new String[] { "CPSDRF01_End_oid", "CPSDRF01_Start_oid" });
                messageLists.add(message);
            }
        }
        if (orderMonthFrom != null && orderMonthTo != null) {
            if (orderMonthFrom.getTime() > orderMonthTo.getTime()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_021);
                message.setMessageArgs(new String[] { "CPSDRF01_End_orderMonth", "CPSDRF01_Start_orderMonth" });
                messageLists.add(message);
            }
        }
        if (messageLists.size() != 0) {
            throw new BusinessException(messageLists);
        }
        super.setCommonParam(param, request);
        // change String to date
        param.getSwapData().put("oidFrom", oidFrom);
        param.getSwapData().put("oidTo", oidTo);
        param.getSwapData().put("etdFrom", etdFrom);
        param.getSwapData().put("etdTo", etdTo);

        List<CPSDRF01Entity> vvList = new ArrayList<CPSDRF01Entity>();
        List<CPSDRF01Entity> aisinList = new ArrayList<CPSDRF01Entity>();
        if (String.valueOf(BusinessPattern.V_V).equals(businessPattern)) {
            vvList = service.getVvInfo(param);
            if (vvList == null || vvList.size() == 0) {
                throw new BusinessException(MessageCodeConst.W1005_001);
            }
        } else if (String.valueOf(BusinessPattern.AISIN).equals(businessPattern)) {
            aisinList = service.getAisinInfo(param);
            if (aisinList == null || aisinList.size() == 0) {
                throw new BusinessException(MessageCodeConst.W1005_001);
            }
        } else {
            vvList = service.getVvInfo(param);
            aisinList = service.getAisinInfo(param);
            if ((vvList == null || vvList.size() == 0) && (aisinList == null || aisinList.size() == 0)) {
                throw new BusinessException(MessageCodeConst.W1005_001);
            }
        }
        return new BaseResult<BaseEntity>();
    }

    /**
     * 
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/sa/CPSDRF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // set common parameters by session
        BaseParam param = this.convertJsonDataForForm(BaseParam.class);
        this.setCommonParam(param, request);
        // checkbox or filters
        // ==================================================================
        String clientTime = param.getClientTime();
        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);

            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader(
                "Content-disposition",
                StringUtil.formatMessage("attachment; filename=\"{0}\"",
                    StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, clientTime)));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }
            String[] fileNames = null;
            String[] fileIds = null;
            String businessPattern = (String) param.getSwapData().get("businessPattern");
            if (String.valueOf(BusinessPattern.V_V).equals(businessPattern)) {
                fileNames = new String[] { StringUtil.formatMessage(VV_ENTITY, clientTime) };
                fileIds = new String[] { this.getFileId() };
                param.getSwapData().put("flag", true);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), fileNames, fileIds, param, zos);
            } else if (String.valueOf(BusinessPattern.AISIN).equals(businessPattern)) {
                fileNames = new String[] { StringUtil.formatMessage(AISIN_ENTITY, clientTime) };
                fileIds = new String[] { this.getFileId() };
                param.getSwapData().put("flag", false);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), fileNames, fileIds, param, zos);
            } else {
                // vv file
                fileNames = new String[] { StringUtil.formatMessage(VV_ENTITY, clientTime) };
                fileIds = new String[] { this.getFileId() };
                param.getSwapData().put("flag", true);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), fileNames, fileIds, param, zos);
                // aisin file
                fileNames = new String[] { StringUtil.formatMessage(AISIN_ENTITY, clientTime) };
                fileIds = new String[] { this.getFileId() };
                param.getSwapData().put("flag", false);
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), fileNames, fileIds, param, zos);
            }
            // Delete temporary folder
            tempFolder.delete();
            zos.close();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
            if (zos != null) {
                try {
                    zos.close();
                } catch (Exception ex) {
                    // logger.warn("OutputStream close fail.", ex);
                }
            }
        }
    }

    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fieldId) {
        // change String to date
        Date oidFrom = DateTimeUtil.parseDate((String) param.getSwapData().get("oidFromString"));
        param.getSwapData().put("oidFrom", oidFrom);
        Date oidTo = DateTimeUtil.parseDate((String) param.getSwapData().get("oidToString"));
        param.getSwapData().put("oidTo", oidTo);
        Date etdFrom = DateTimeUtil.parseDate((String) param.getSwapData().get("etdFromString"));
        param.getSwapData().put("etdFrom", etdFrom);
        Date etdTo = DateTimeUtil.parseDate((String) param.getSwapData().get("etdToString"));
        param.getSwapData().put("etdTo", etdTo);

        Cell[] templateCells = getTemplateCells(SHEET_STYLE, IntDef.INT_ZERO, wbTemplate);
        boolean flag = (Boolean) param.getSwapData().get("flag");
        List<CPSDRF01Entity> vvList = new ArrayList<CPSDRF01Entity>();
        List<CPSDRF01Entity> aisinList = new ArrayList<CPSDRF01Entity>();
        Sheet sheetVv = wbTemplate.getSheetAt(0);
        Sheet sheetAisin = wbTemplate.getSheetAt(1);
        if (flag) {
            vvList = service.getVvInfo(param);
            for (int i = 0; i < vvList.size(); i++) {
                CPSDRF01Entity entity = vvList.get(i);
                
                int digits = MasterManager.getUomDigits(entity.getUomCode());
                CellStyle qtyStyle = super.getDecimalStyle(wbTemplate, QTY_STYLE_SHEET, digits);
                CellStyle grey = super.getDecimalStyle(wbTemplate, GREY_QTY_STYLE_SHEET, digits);
                CellStyle red = super.getDecimalStyle(wbTemplate, RED_QTY_STYLE_SHEET, digits);
                templateCells[IntDef.INT_SEVEN].setCellStyle(qtyStyle);
                templateCells[IntDef.INT_NINE].setCellStyle(qtyStyle);
                templateCells[IntDef.INT_TEN].setCellStyle(qtyStyle);
                // engish
                String expSoDateString = null;
                String etdString = null;
                if (param.getLanguage() == IntDef.INT_ONE) {
                    expSoDateString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, entity.getExpSoDate());
                    etdString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, entity.getEtd());
                } else {
                    expSoDateString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, entity.getExpSoDate());
                    etdString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, entity.getEtd());
                }

                String disInvNo = StringUtil.toSafeString(entity.getInvoiceNo());
                if (entity.isNeedShowQty()) {
                    disInvNo += StringUtil.toSafeString(entity.getInvoiceNoQty());
                }
                Object[] arrayObj = new Object[] { entity.getTtcPart(), entity.getCustomerCode(), entity.getRegion(),
                    entity.getSupplierCode(), expSoDateString, entity.getImpPoNo(), etdString, entity.getSumQty(),
                    disInvNo, entity.getInvoiceQty(), entity.getDelay() };
                createOneDataRowByTemplate(sheetVv, IntDef.INT_THREE + i, templateCells, arrayObj);
                if (entity.getEtd() == null) {
                    PoiUtil.setCellStyle(sheetVv, IntDef.INT_FOUR + i, IntDef.INT_SEVEN, grey);
                    PoiUtil.setCellStyle(sheetVv, IntDef.INT_FOUR + i, IntDef.INT_EIGHT, grey);
                    PoiUtil.setCellStyle(sheetVv, IntDef.INT_FOUR + i, IntDef.INT_TEN, grey);
                }
                if (entity.getDelay() != null && DecimalUtil.isEquals(entity.getDelay(), BigDecimal.ZERO)) {
                    PoiUtil.setCellStyle(sheetVv, IntDef.INT_FOUR + i, IntDef.INT_ELEVEN, grey);
                } else if (entity.getDelay() != null && DecimalUtil.isGreater(entity.getDelay(), BigDecimal.ZERO)) {
                    PoiUtil.setCellStyle(sheetVv, IntDef.INT_FOUR + i, IntDef.INT_ELEVEN, red);
                }
            }
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(RED_QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(GREY_QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(SHEET_STYLE));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(AISIN));
            wbTemplate.setSheetName(0, SHEET_NAME);
        } else {
            aisinList = service.getAisinInfo(param);
            for (int i = 0; i < aisinList.size(); i++) {
                CPSDRF01Entity entity = aisinList.get(i);

                int digits = MasterManager.getUomDigits(entity.getUomCode());
                CellStyle qtyStyle = super.getDecimalStyle(wbTemplate, QTY_STYLE_SHEET, digits);
                CellStyle grey = super.getDecimalStyle(wbTemplate, GREY_QTY_STYLE_SHEET, digits);
                CellStyle red = super.getDecimalStyle(wbTemplate, RED_QTY_STYLE_SHEET, digits);
                templateCells[IntDef.INT_SEVEN].setCellStyle(qtyStyle);
                templateCells[IntDef.INT_NINE].setCellStyle(qtyStyle);
                templateCells[IntDef.INT_TEN].setCellStyle(qtyStyle);
                // engish
                String orderMonthString = null;
                String etdString = null;
                if (param.getLanguage() == IntDef.INT_ONE) {
                    orderMonthString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_MMMYYYY,
                        DateTimeUtil.FORMAT_YEAR_MONTH, entity.getOrderMonth());
                    etdString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DDMMMYYYY, entity.getEtd());
                } else {
                    orderMonthString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM,
                        DateTimeUtil.FORMAT_YEAR_MONTH, entity.getOrderMonth());
                    etdString = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, entity.getEtd());
                }

                String disInvNo = StringUtil.toSafeString(entity.getInvoiceNo());
                if (entity.isNeedShowQty()) {
                    disInvNo += StringUtil.toSafeString(entity.getInvoiceNoQty());
                }
                Object[] arrayObj = new Object[] { entity.getTtcPart(), entity.getCustomerCode(), entity.getRegion(),
                    entity.getSupplierCode(), orderMonthString, entity.getPlanNo(), etdString, entity.getSumQty(),
                    disInvNo, entity.getInvoiceQty(), entity.getDelay() };
                createOneDataRowByTemplate(sheetAisin, IntDef.INT_THREE + i, templateCells, arrayObj);
                // if (param.getLanguage() == IntDef.INT_ONE) {
                // PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_FIVE, yearMonthEN);
                // } else {
                // PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_FIVE, yearMonthCN);
                // }
                if (entity.getEtd() == null) {
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_SEVEN, grey);
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_EIGHT, grey);
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_TEN, grey);
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_ELEVEN, grey);
                }
                if (entity.getDelay() != null && DecimalUtil.isEquals(entity.getDelay(), BigDecimal.ZERO)) {
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_ELEVEN, grey);
                } else if (entity.getDelay() != null && DecimalUtil.isGreater(entity.getDelay(), BigDecimal.ZERO)) {
                    PoiUtil.setCellStyle(sheetAisin, IntDef.INT_FOUR + i, IntDef.INT_ELEVEN, red);
                }
            }
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(RED_QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(GREY_QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(QTY_STYLE_SHEET));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(SHEET_STYLE));
            wbTemplate.removeSheetAt(wbTemplate.getSheetIndex(VV));
            wbTemplate.setSheetName(0, SHEET_NAME);
        }
    }
}
