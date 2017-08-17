/**
 * CPOCSF11Controller.java
 * 
 * @screen CPOCSS01
 * @author li_feng
 */
package com.chinaplus.web.om.control;

import java.util.ArrayList;
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
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
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
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.om.entity.CPOCSF11Entity;
import com.chinaplus.web.om.service.CPOCSF11Service;

/**
 * Customer Stock Upload Screen Controller.
 */
@Controller
public class CPOCSF11Controller extends BaseFileController {

    /** the sheet name : Customer Stock */
    private static final String SHEET_NAME = "CustomerStock";
    /** business pattern */
    private static final String BSPTN = "Business Pattern";
    /** business pattern */
    private static final String BSPTN_CN = "业务区分";
    /** business pattern V-V */
    private static final String BSPTN_VV = "V-V";
    /** business pattern AISIN */
    private static final String BSPTN_AISIN = "AISIN";
    /** business pattern V-V No */
    private static final int BSPTN_VV_NO = 1;
    /** business pattern AISIN NO */
    private static final int BSPTN_AISIN_NO = 2;
    /** business pattern number row number */
    private static final int BSPTN_ROW_NUM = 3;
    /** business pattern column number */
    private static final int BSPTN_COL_NUM = 3;
    /** business pattern number row number in excel */
    private static final String BSPTN_ROW_NUM_IN_EXCEL = "3";
    /** read excel begin column number */
    private static final int READ_START_COL = 2;
    /** read excel end column number */
    private static final int READ_TOTAL_COL = 5;
    /** detail data begin row number */
    private static final int DETAIL_START_LINE = 7;
    /** separator */
    private static final String KEY_SEPARATOR = "***separator***";
    /** max length for db */
    private static final String INTEGER_DIGITS = "10";
    /** decimal digits */
    private static final String DECIMAL_DIGITS = "6";

    /** Customer Stock Upload Screen Service */
    @Autowired
    private CPOCSF11Service service;

    @Override
    protected String getFileId() {
        return FileId.CPOCSF11;
    }

    /**
     * Customer Stock Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/om/CPOCSF11/upload",
        method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(@RequestParam(value = "fileData",
        required = true) MultipartFile file, BaseParam param, HttpServletRequest request, HttpServletResponse response)
        throws Exception {

        this.setCommonParam(param, request);
        BaseResult<BaseEntity> baseResult = uploadFileProcess(file, FileType.EXCEL, param, request, response);
        this.setUploadResult(request, response, baseResult);
    }

    /**
     * Process uploaded excel.
     * 
     * @param file the upload file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook, T param,
        HttpServletRequest request) {

        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        List<CPOCSF11Entity> checkList = new ArrayList<CPOCSF11Entity>();

        // get and check data from excel file
        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            Map<String, Object> dataMap = new HashMap<String, Object>();

            dataMap = getAndCheckDataFromFile(param, workbook, request);

            if (dataMap.get("MessageList") != null) {
                throw new BusinessException((List<BaseMessage>) dataMap.get("MessageList"));
            }

            checkList = (List<CPOCSF11Entity>) dataMap.get("CheckList");
            param.setSwapData("businessPattern", dataMap.get("BusinessPattern").toString());
        }

        // upload logic
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {

                String mapKey = param.getSessionKey();
                checkList = (List<CPOCSF11Entity>) context.get(mapKey);

                param.setSwapData("businessPattern", context.get(mapKey + "_BSPTN").toString());
                context.remove(mapKey);
            }

            // upload
            service.doUpdateList(param, checkList);
        }

        return new ArrayList<BaseMessage>();

    }

    /**
     * get and check data from excel file
     * 
     * @param param BasepParam
     * @param workbook workbook
     * @param request HttpServletRequest
     * @return list
     */
    private Map<String, Object> getAndCheckDataFromFile(BaseParam param, Workbook workbook, HttpServletRequest request) {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        List<CPOCSF11Entity> checkList = new ArrayList<CPOCSF11Entity>();
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> confirmMessageLists = new ArrayList<BaseMessage>();
        
        // get work sheet sheet
        Sheet sheet = workbook.getSheet(SHEET_NAME);

        // 3.1 Check if upload file have effective data.
        // 3.1.1-1 business pattern is blank
        String businessPattern = PoiUtil.getStringCellValue(sheet, BSPTN_ROW_NUM, BSPTN_COL_NUM);
        int businessPatternNum = 0;
        Integer businessPatternNo = 0;
        if (StringUtil.isEmpty(businessPattern)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
            if (param.getLanguage() == IntDef.INT_TWO) {
                message.setMessageArgs(new String[] { "3", BSPTN_CN });
            } else {
                message.setMessageArgs(new String[] { "3", BSPTN });
            }
            
           
            messageLists.add(message);
            dataMap.put("MessageList", messageLists);
            return dataMap;
        } else {
            // 3.1.1-2 business pattern is not an effective business pattern
            List<CPOCSF11Entity> list = service.getBusinessPatterns();
            Boolean categoryFlg = false;
            for (CPOCSF11Entity entity : list) {
                if (entity.getCodeName().equals(businessPattern)) {
                    categoryFlg = true;
                    break;
                }
            }
            if (!categoryFlg) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_068);
                message.setMessageArgs(new String[] { BSPTN_ROW_NUM_IN_EXCEL, BSPTN });
                messageLists.add(message);
                dataMap.put("MessageList", messageLists);
                return dataMap;
            } else {
                // get businessPattern
                if (businessPattern.equals(BSPTN_VV)) {
                    dataMap.put("BusinessPattern", BusinessPattern.V_V);
                    param.setSwapData("businessPattern", BusinessPattern.V_V);
                    businessPatternNum=BusinessPattern.V_V;
                    businessPatternNo = BSPTN_VV_NO;
                } else if (businessPattern.equals(BSPTN_AISIN)) {
                    businessPatternNo = BSPTN_AISIN_NO;
                    dataMap.put("BusinessPattern", BusinessPattern.AISIN);
                    businessPatternNum=BusinessPattern.AISIN;
                    param.setSwapData("businessPattern", BusinessPattern.AISIN);
                }
            }
        }
        // read excel
        int sheetMaxRow = sheet.getLastRowNum() + 1;
        String detailKey = "";
        int startCol = READ_START_COL;
        for (int startRow = DETAIL_START_LINE; startRow <= sheetMaxRow; startRow++) {
            
            if(!ValidatorUtils.isBlankRow(sheet,startRow,READ_START_COL,READ_TOTAL_COL)){
                CPOCSF11Entity cpocsf11Entity = new CPOCSF11Entity();
                cpocsf11Entity.setPartNo(StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol++)));
                cpocsf11Entity.setCustomerCode(StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow,
                    startCol++)));
                cpocsf11Entity.setCustomerStock(DecimalUtil.getBigDecimal(PoiUtil.getStringCellValue(sheet, startRow,
                    startCol)));
                cpocsf11Entity.setCustomerStockStr(StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow,
                    startCol++)));
                cpocsf11Entity.setCustomerStockDateStr(PoiUtil.getStringCellValue(sheet, startRow, startCol));

                checkList.add(cpocsf11Entity);
                startCol = READ_START_COL;
            } else {
                if(ValidatorUtils.isExcelEnd(sheet,startRow,READ_START_COL,READ_TOTAL_COL)){
                    break;
                }
            } 
        }
        
        // upload data check
        // Error Check
        // 2 Check if upload file have no data
        if(0 == checkList.size()){
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);   
            throw new BusinessException(messageLists);
        }
        

        super.setCommonParam(param, request);

        dataMap.put("CheckList", checkList);
        param.setSwapData("CPOCSF11EntityList", checkList);
        context.put(param.getSessionKey() + "_BSPTN", businessPatternNum);
        context.put(param.getSessionKey(), checkList);

        
        
        // get checkInfoList
        List<CPOCSF11Entity> checkInfoList = service.getCheckInfo(param);

        // get login user customer code
        // Map<String, String> customerCodeMap = service.getUserCustomerCode(param);

        // get all businessPattern
        Map<String, Integer> businessPatternMap = new HashMap<String, Integer>();

        // get customer stock lockFlg
        Map<String, Boolean> customerStockLockFlgMap = new HashMap<String, Boolean>();

        // get registered partNo
        Map<String, String> getRegisteredPartNoMap = service.getRegisteredPartNo(param);

        // get ending stock date
        Map<String, Date> getEndingStockDate = service.getEndingStockDate(param);
        
        // get all businessPattern & customer stock lockFlg
        String businessPatternMapKey = null;
        String stockLockFlgMapKey = null;
        for (CPOCSF11Entity entity : checkInfoList) {
            if (businessPattern.equals(BSPTN_VV)) {
                businessPatternMapKey = new StringBuilder().append(entity.getTtcPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                businessPatternMap.put(businessPatternMapKey, entity.getBusinessPattern());

                stockLockFlgMapKey = new StringBuilder().append(entity.getTtcPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                customerStockLockFlgMap.put(stockLockFlgMapKey,
                    (entity.getOsCustStockFlag() == 0 && entity.getSaCustStockFlag() == 0) ? false : true);
            } else if (businessPattern.equals(BSPTN_AISIN)) {
                businessPatternMapKey = new StringBuilder().append(entity.getCustPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                businessPatternMap.put(businessPatternMapKey, entity.getBusinessPattern());

                stockLockFlgMapKey = new StringBuilder().append(entity.getCustPartsNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                customerStockLockFlgMap.put(stockLockFlgMapKey,
                    (entity.getOsCustStockFlag() == 0 && entity.getSaCustStockFlag() == 0) ? false : true);
            }
        }

        // primary key check begin
        int index = DETAIL_START_LINE;
        String lang = MessageManager.getLanguage(param.getLanguage()).toString();
        String Msg = null;
        String Msg2 = null;
        Boolean customerCodeDiffFlg = false;
        Boolean customerStockFlg = false;
        Boolean partsFlg;
        List<String> messageList = new ArrayList<String>();
        List<String> messageList2 = new ArrayList<String>();
        List<String> messageList3 = new ArrayList<String>();

        CPOCSF11Entity cpocsf11Entity = new CPOCSF11Entity();
        
        // login user's customer
        UserManager um = UserManager.getLocalInstance(context);
        List<com.chinaplus.common.bean.BusinessPattern> userCustomer = um.getCurrentBusPattern();

        Map<String, com.chinaplus.common.bean.BusinessPattern> userCusInfo = null;
        if (null != userCustomer && !userCustomer.isEmpty()) {
            userCusInfo = new HashMap<String, com.chinaplus.common.bean.BusinessPattern>();
            for (int m = 0; m < userCustomer.size(); m++) {
                com.chinaplus.common.bean.BusinessPattern userCus = userCustomer.get(m);
                userCusInfo.put(userCus.getCustomerCode(), userCus);
            }
        }

        Map<String, CPOCSF11Entity> detailLists = new HashMap<String, CPOCSF11Entity>();
        for (CPOCSF11Entity entity : checkList) {

            entity.setDataEffectiveFlg(true);
            partsFlg = true;
            param.setSwapData(cpocsf11Entity.getCodeCategory(), cpocsf11Entity.getCodeCategory());

            if (!(StringUtil.isEmpty(entity.getPartNo()) || StringUtil.isEmpty(entity.getCustomerCode()))) {
                detailKey = new StringBuilder().append(entity.getPartNo()).append(KEY_SEPARATOR)
                    .append(entity.getCustomerCode()).toString();
                if (detailLists.containsKey(detailKey)) {
                    // 3.2 primary key check
                    // 3.2.2-2 TTC P/N(V-V), Customer P/N(AISIN) and TTC Customer Code is not repeat with other rows
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_046);
                    String b5 = businessPattern.equals(BSPTN_VV) ? MessageManager.getMessage("CPOCSF11_Grid_TTCPN1",
                        lang) : MessageManager.getMessage("CPOCSF11_Grid_TTCPN2", lang);
                    String c5 = MessageManager.getMessage("CPOCSF11_Grid_TTCCustomerCode", lang);
                    message
                        .setMessageArgs(new String[] { b5, entity.getPartNo(), c5, entity.getCustomerCode() });
                    messageLists.add(message);
                } else {
                    detailLists.put(detailKey, entity);
                }

                // 3.3 format check
                // 3.3.3-1 the stock is wrong format
                if(!ValidatorUtils.checkMaxDecimal(entity.getCustomerStock())){
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
                    Msg = MessageManager.getMessage("CPOCSF11_Grid_CustomerStock", lang);
                    message
                        .setMessageArgs(new String[] {Integer.toString(index) ,Msg ,INTEGER_DIGITS ,DECIMAL_DIGITS});
                    messageLists.add(message);
                }
                

                // 3.4 business check
                // 3.4.4-1 check the object customer.
                if(null != userCusInfo && null != userCusInfo.get(entity.getCustomerCode())){
                    customerCodeDiffFlg = true;
                }

                if (!customerCodeDiffFlg) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_023);
                    message.setMessageArgs(new String[] { entity.getCustomerCode() });
                    messageLists.add(message);
                }

                // 3.4 business check
                // 3.4.4-2 check the same business pattern
                if (null != businessPatternMap.get(detailKey) && !(businessPatternNo.equals(businessPatternMap.get(detailKey)))) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_067);
                    message.setMessageArgs(new String[] { SHEET_NAME, entity.getCustomerCode() });
                    messageLists.add(message);
                }
                

                // 3.4 business check
                // 3.4.4-4 if the customer stock is not be used in the setting of the master
                if (null != customerStockLockFlgMap.get(detailKey) && !customerStockLockFlgMap.get(detailKey)) {
                    customerStockFlg = true;
                    messageList.add(entity.getPartNo());
                }
                

                // Confirm Check
                // 1-2.1 Check parts master
                if(null != getRegisteredPartNoMap.get(detailKey)){
                    partsFlg = false;
                }
                if (partsFlg) {
                    messageList2.add(entity.getPartNo());
                    entity.setDataEffectiveFlg(false);
                }

            } else {

                // 3.2 primary key check
                // 3.2.2-1 TTC P/N(V-V), Customer P/N(AISIN), TTC Customer Code is not blank
                if (StringUtil.isEmpty(entity.getPartNo())) {
                    Msg = MessageManager.getMessage("CPOCSF11_Grid_TTCPN1", lang);
                    Msg2 = MessageManager.getMessage("CPOCSF11_Grid_TTCPN2", lang);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { Integer.toString(index), Msg + " , " + Msg2 });
                    messageLists.add(message);
                }
                if (StringUtil.isEmpty(entity.getCustomerCode())) {
                    Msg = MessageManager.getMessage("CPOCSF11_Grid_TTCCustomerCode", lang);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { Integer.toString(index), Msg });
                    messageLists.add(message);
                }
            }
            
            // CustomerStock is not blank
            if(StringUtil.isEmpty(entity.getCustomerStockStr())){
                Msg = MessageManager.getMessage("CPOCSF11_Grid_CustomerStock", lang);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { Integer.toString(index), Msg });
                messageLists.add(message);
            }
            
            // 3.2 primary key check
            // 3.2.2-1 Ending Stock is not blank
            if (StringUtil.isEmpty(entity.getCustomerStockDateStr())) {
                Msg = MessageManager.getMessage("CPOCSF11_Grid_EndingStock", lang);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { Integer.toString(index), Msg });
                messageLists.add(message);
            } else {
                // 3.3 format check
                // TODO -- 3.3.3-2 the ending stock is not a format of [dd mmm yyyy]
                if(null == DateTimeUtil.parseDate(entity.getCustomerStockDateStr())){
                    Msg = MessageManager.getMessage("CPOCSF11_Grid_EndingStock", lang);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { Integer.toString(index), Msg , "dd mmm yyyy"});
                    messageLists.add(message);
                    
                }
                else{
                    entity.setCustomerStockDate(DateTimeUtil.parseDate(entity.getCustomerStockDateStr()));
                    // 3.4 business check
                    // TODO -- 3.4.4-3 if ending stock > today
                    if (entity.getCustomerStockDate().getTime() > service.getDBDateTime(param.getOfficeTimezone()).getTime()) {
                        Msg = MessageManager.getMessage("CPOCSF11_Grid_EndingStock", lang);
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_047);
                        message.setMessageArgs(new String[] { Integer.toString(index), Msg });
                        messageLists.add(message);
                    }

                    // Confirm Check
                    // 1-2.2 Check stock ending date
                    if (null != getEndingStockDate.get(detailKey)
                            && entity.getCustomerStockDate().getTime() < getEndingStockDate.get(detailKey).getTime()) {
                        messageList3.add(entity.getPartNo());
                    }
                }

            }

            index++;
            customerCodeDiffFlg = false;
            Msg = null;
            Msg2 = null;
        }

        // 3.4 business check
        // 3.4.4-4 set messages
        if (customerStockFlg) {
            StringBuilder parts = new StringBuilder();
            if (messageList != null && messageList.size() > 0) {
                for (int i = 0; i < messageList.size(); i++) {
                    if (i < messageList.size() - 1) {
                        parts.append(messageList.get(i) + ",");
                    } else {
                        parts.append(messageList.get(i));
                    }
                }
            }
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_048);
            message.setMessageArgs(new String[] {
                MessageManager.getMessage("CPMPMF01_Grid_OrderSuggAlarm3includCustStockF", lang),
                MessageManager.getMessage("CPMPMF01_Grid_SSAlarm12RundownincludCustStockF", lang),
                "( " + parts.toString() + " )" });
            messageLists.add(message);
        }

        if (messageList.isEmpty()) {

            // Confirm Check
            // 1-2.1 set message
            if (!messageList2.isEmpty()) {
                StringBuilder parts = new StringBuilder();
                if (messageList2 != null && messageList2.size() > 0) {
                    for (int i = 0; i < messageList2.size(); i++) {
                        if (i < messageList2.size() - 1) {
                            parts.append(messageList2.get(i) + ",");
                        } else {
                            parts.append(messageList2.get(i));
                        }
                    }
                }
                BaseMessage message = new BaseMessage(MessageCodeConst.C1007);
                message.setMessageArgs(new String[] { parts.toString() });
                confirmMessageLists.add(message);
            }

            // Confirm Check
            // 1-2.2 set message
            if (!messageList3.isEmpty()) {
                StringBuilder parts = new StringBuilder();
                if (messageList3 != null && messageList3.size() > 0) {
                    for (int i = 0; i < messageList3.size(); i++) {
                        if (i < messageList3.size() - 1) {
                            parts.append(messageList3.get(i) + ",");
                        } else {
                            parts.append(messageList3.get(i));
                        }
                    }
                }

                BaseMessage message = new BaseMessage(MessageCodeConst.C1008);
                message.setMessageArgs(new String[] { "( " + parts.toString() + " )" });
                confirmMessageLists.add(message);
            }

        }

        if (!confirmMessageLists.isEmpty() && messageLists.isEmpty()) {

            dataMap.put("MessageList", confirmMessageLists);

        } else if (!messageLists.isEmpty()) {
            dataMap.put("MessageList", messageLists);
        }

        return dataMap;
    }
}