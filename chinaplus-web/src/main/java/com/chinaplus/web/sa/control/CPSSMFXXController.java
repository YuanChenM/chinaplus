/**
 * CPSSMFXXController.java
 * 
 * @screen CPSSMFXX
 * @author ma_chao
 */
package com.chinaplus.web.sa.control;

import java.io.File;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Properties;
import com.chinaplus.common.entity.TntOrder;
import com.chinaplus.common.entity.TntSsPlan;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSSMF01Entity;
import com.chinaplus.web.sa.service.CPSSMF01Service;
import com.chinaplus.web.sa.service.CPSSMF02Service;
import com.chinaplus.web.sa.service.CPSSMF03Service;

/**
 * Download Revised Shipping Status MultiFiles Controller.
 */
@Controller
public class CPSSMFXXController extends BaseFileController {

    /** Download zip file name */
    private static final String DOWNLOAD_ZIP_FILE_NAME = "Shipping Status Docs_{0}.zip";

    /** Download excel file name 1 */
    private static final String DOWNLOAD_EXCEL_FILE_NAME_1 = "Shipping Status Doc1_{0}.xlsx";

    /** Download excel file name 2 */
    private static final String DOWNLOAD_EXCEL_FILE_NAME_2 = "Shipping Status Doc2_{0}.xlsx";

    /** Download excel file name 3 */
    private static final String DOWNLOAD_EXCEL_FILE_NAME_3 = "Shipping Status Doc3_{0}.xlsx";

    /** ZIP file content type */
    private static final String ZIP_CONTENT_TYPE = "application/x-zip-compressed;charset=UTF-8";

    /** Download Revised Shipping Status (Doc1) Service */
    @Autowired
    private CPSSMF01Service cpssmf01Service;

    /** Download Revised Shipping Status (Doc2) Service */
    @Autowired
    private CPSSMF02Service cpssmf02Service;

    /** Download Revised Shipping Status (Doc3) Service */
    @Autowired
    private CPSSMF03Service cpssmf03Service;

    /**
     * Get current file id.
     * 
     * @return file id
     */
    @Override
    protected String getFileId() {
        return null;
    }

    /**
     * Download check for Revised Shipping Status.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return check result
     * @throws ParseException 
     */
    @RequestMapping(value = "sa/CPSSMFXX/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody PageParam param, HttpServletRequest request,
        HttpServletResponse response) throws ParseException{
        BaseResult<String> result = new BaseResult<String>();
        super.setCommonParam(param, request);
        
        
        Locale lang = MessageManager.getLanguage(param.getLanguage()).getLocale();
        String Msg = null;
        String Msg2 = null;
        String Msg3 = param.getLanguage() == IntDef.INT_TWO ? "和" : "and";
        String saleDateFrom = (String)param.getSwapData().get("saleDateFrom");
        String saleDateTo = (String)param.getSwapData().get("saleDateTo");
        String planEtdFrom = (String)param.getSwapData().get("planEtdFrom");
        String planEtdTo = (String)param.getSwapData().get("planEtdTo");
        String planEtaFrom = (String)param.getSwapData().get("planEtaFrom");
        String planEtaTo = (String)param.getSwapData().get("planEtaTo");
        String invoiceEtdFrom = (String)param.getSwapData().get("invoiceEtdFrom");
        String invoiceEtdTo = (String)param.getSwapData().get("invoiceEtdTo");
        String invoiceEtaFrom = (String)param.getSwapData().get("invoiceEtaFrom");
        String invoiceEtaTo = (String)param.getSwapData().get("invoiceEtaTo");
        // saleDateFrom && saleDateTo
        if(!StringUtil.isNullOrEmpty(saleDateFrom) && !StringUtil.isNullOrEmpty(saleDateTo)){
            Msg = MessageManager.getMessage("CPSSMS02_Label_JpGscmSalesOrderCreateDateFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_JpGscmSalesOrderCreateDateTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(saleDateTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(saleDateFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if(getDateDifference(from, to)){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX)});
                    result.addMessage(message);
                }
            }
        }
        // planEtdFrom && planEtdTo
        if(!StringUtil.isNullOrEmpty(planEtdFrom) && !StringUtil.isNullOrEmpty(planEtdTo)){
            Msg = MessageManager.getMessage("CPSSMS02_Label_PlanEtdFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_PlanEtdTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtdTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtdFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if(getDateDifference(from, to)){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX)});
                    result.addMessage(message);
                }
            }
        }
        // planEtaFrom && planEtaTo
        if(!StringUtil.isNullOrEmpty(planEtaFrom) && !StringUtil.isNullOrEmpty(planEtaTo)){
            Msg = MessageManager.getMessage("CPSSMS02_Label_PlanEtaFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_PlanEtaTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtaTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(planEtaFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if(getDateDifference(from, to)){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX)});
                    result.addMessage(message);
                }
            }
        }
        // invoiceEtdFrom && invoiceEtdTo
        if(!StringUtil.isNullOrEmpty(invoiceEtdFrom) && !StringUtil.isNullOrEmpty(invoiceEtdTo)){
            Msg = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtdFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtdTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtdTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtdFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if(getDateDifference(from, to)){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX)});
                    result.addMessage(message);
                }
            }
        }
        // invoiceEtaFrom && invoiceEtaTo
        if(!StringUtil.isNullOrEmpty(invoiceEtaFrom) && !StringUtil.isNullOrEmpty(invoiceEtaTo)){
            Msg = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtaFrom", lang).replace("<br>", "");
            Msg2 = MessageManager.getMessage("CPSSMS02_Label_InvoiceEtaTo", lang);
            String to = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtaTo));
            String from = DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, DateTimeUtil.parseDate(invoiceEtaFrom));
            if (StringUtil.toSafeInteger(to) < StringUtil.toSafeInteger(from)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_002);
                message.setMessageArgs(new String[] { Msg, Msg2 });
                result.addMessage(message);
            } else {
                if(getDateDifference(from, to)){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_019);
                    String msgValue = Msg + Msg3 + Msg2;
                    message.setMessageArgs(new String[] { msgValue, String.valueOf(IntDef.INT_SIX)});
                    result.addMessage(message);
                }
            }
        }
        
        
        
        
        
        
        boolean validResult = CPSSMFXXUtil.validDateTimeFilter(param, result);
        if (validResult) {
            CPSSMFXXUtil.convertParam(param);
            boolean doc1NoData = false;
            List<TntSsPlan> planList = this.cpssmf01Service.searchSsPlanList(param);
            List<TntOrder> orderList = null;
            List<TntOrder> orderListByInvoice = null;
            if (null != planList && !planList.isEmpty()) {
                orderList = this.cpssmf01Service.searchOrderList(param);
            }
            
            if (null != orderList && !orderList.isEmpty()) {
                orderListByInvoice = this.cpssmf01Service.searchOrderListByInvoice(param);
            }
            List<CPSSMF01Entity> planInfoList = this.cpssmf01Service.searchShippingPlanInfo(param, orderList,
                orderListByInvoice, planList);
            if (null == planInfoList || planInfoList.isEmpty()) {
                doc1NoData = true;
            }

            boolean doc2NoData = false;
            planList = this.cpssmf02Service.searchSsPlanList(param);
            orderList = null;
            orderListByInvoice = null;
            if (null != planList && !planList.isEmpty()) {
                orderList = this.cpssmf02Service.searchOrderList(param);
            }
            
            if (null != orderList && !orderList.isEmpty()) {
                orderListByInvoice = this.cpssmf02Service.searchOrderListByInvoice(param);
            }
            planInfoList = this.cpssmf02Service.searchShippingPlanInfo(param, orderList, orderListByInvoice, planList);
            if (null == planInfoList || planInfoList.isEmpty()) {
                doc2NoData = true;
            }

            boolean doc3NoData = false;
            planList = this.cpssmf03Service.searchSsPlanList(param);
            orderList = null;
            orderListByInvoice = null;
            if (null != planList && !planList.isEmpty()) {
                orderList = this.cpssmf03Service.searchOrderList(param);
            }

            if (null != orderList && !orderList.isEmpty()) {
                orderListByInvoice = this.cpssmf03Service.searchOrderListByInvoice(param);
            }
            planInfoList = this.cpssmf03Service.searchShippingPlanInfo(param, orderList, orderListByInvoice, planList);
            if (null == planInfoList || planInfoList.isEmpty()) {
                doc3NoData = true;
            }

            if (doc1NoData && doc2NoData && doc3NoData) {
                result.addMessage(new BaseMessage(MessageCodeConst.W1005_001));
            }
        }

        return result;
    }

    /**
     * Download file.
     * 
     * @param param page parameter
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception the exception
     */
    @RequestMapping(value = "sa/CPSSMFXX/download")
    public void download(PageParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {

        OutputStream os = null;
        ZipOutputStream zos = null;
        try {
            super.setCommonParam(param, request);
            response.setContentType(ZIP_CONTENT_TYPE);
            response.setCharacterEncoding(ENCODE);
            response.setHeader("Content-disposition", StringUtil.formatMessage("attachment; filename=\"{0}\"",
                StringUtil.formatMessage(DOWNLOAD_ZIP_FILE_NAME, param.getClientTime())));
            os = response.getOutputStream();
            zos = new ZipOutputStream(os);

            // Generate temporary folder path
            String tempFolderPath = ConfigUtil.get(Properties.TEMPORARY_PATH) + UUID.randomUUID().toString();
            // Create temporary folder
            File tempFolder = new File(tempFolderPath);
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            String fileName1 = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME_1, param.getClientTime());
            String fileName2 = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME_2, param.getClientTime());
            String fileName3 = StringUtil.formatMessage(DOWNLOAD_EXCEL_FILE_NAME_3, param.getClientTime());

            String fileId1 = FileId.CPSSMF01;
            String fileId2 = FileId.CPSSMF02;
            String fileId3 = FileId.CPSSMF03;

            String docs = (String) param.getSwapData().get("docs");
            CPSSMFXXUtil.convertParam(param);
            if (docs.equals("doc1doc2doc3")) {
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(),
                    new String[] { fileName1, fileName2, fileName3 }, new String[] { fileId1, fileId2, fileId3 }, param,
                    zos);
            } else if (docs.equals("doc1doc2")) {
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName1, fileName2 },
                    new String[] { fileId1, fileId2 }, param, zos);
            } else if (docs.equals("doc1doc3")) {
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName1, fileName3 },
                    new String[] { fileId1, fileId3 }, param, zos);
            } else if (docs.equals("doc2doc3")) {
                super.makeZipExcelWithMultiTemplate(tempFolder.getPath(), new String[] { fileName2, fileName3 },
                    new String[] { fileId2, fileId3 }, param, zos);
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

    /**
     * Write content to excel.
     * 
     * @param param page parameter
     * @param wbTemplate the template excel
     * @param wbOutput the output excel
     * @param fileId fileId
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput,
        String fileId) {
        // Download Revised Shipping Status (Doc1)
        if (fileId.equals(FileId.CPSSMF01)) {
            CPSSMF01Controller cpssmf01 = new CPSSMF01Controller();
            cpssmf01.outputMainProcess(param, wbTemplate, wbOutput, cpssmf01Service);
        }
        // Download Revised Shipping Status (Doc2)
        else if (fileId.equals(FileId.CPSSMF02)) {
            CPSSMF02Controller cpssmf02 = new CPSSMF02Controller();
            cpssmf02.outputMainProcess(param, wbTemplate, wbOutput, cpssmf02Service);
        }
        // Download Revised Shipping Status (Doc3)
        else if (fileId.equals(FileId.CPSSMF03)) {
            CPSSMF03Controller cpssmf03 = new CPSSMF03Controller();
            cpssmf03.outputMainProcess(param, wbTemplate, wbOutput, cpssmf03Service);
        }
    }
    
    
    /**
     * Get Month Difference
     * 
     * @param custStartMonth String
     * @param custEndMonth String
     * @return difference
     * @throws ParseException E
     */
    public boolean getDateDifference(String custStartMonth, String custEndMonth) throws ParseException {
        SimpleDateFormat formatYMD = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTime(formatYMD.parse(custStartMonth));
        c.add(Calendar.MONTH, IntDef.INT_SIX);
        Date afterDate = c.getTime();
        if(Integer.valueOf(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMMDD, afterDate)) < Integer.valueOf(custEndMonth)){
            return true;
        }
       
        return false;
    }

}
