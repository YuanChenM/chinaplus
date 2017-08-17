/**
 * CPMKBF11Controller.java
 * 
 * @screen CPMKBF11
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.CPMKBF01Entity;
import com.chinaplus.web.mm.entity.OfficeAndCustmorEntity;
import com.chinaplus.web.mm.entity.UserCustmorEntity;
import com.chinaplus.web.mm.entity.UserOfficeCodesEntity;
import com.chinaplus.web.mm.service.CPMKBF11Service;

/**
 * CPMKBF11Controller.
 * Kanban Issued Plan Date Master Upload
 */
@Controller
public class CPMKBF11Controller extends BaseFileController {

    /** NEW */
    public static final String NEW = "NEW";

    /** MOD */
    public static final String MOD = "MOD";

    /** DETAIL_START_LINE */
    private static final int DETAIL_START_LINE = 6;

    /** COL_THREE */
    private static final int COL_THREE = 3;

    /** DETAIL_START_COL_NO */
    private static final int DETAIL_START_COL_NO = 0;

    /** TOTAL_COL_NUM */
    private static final int TOTAL_COL_NUM = 7;

    /** BLANK_LINE_NUM */
    private static final int BLANK_LINE_NUM = 10;

    /** SPRATOR */
    private static final String SPRATOR = "|";

    /** SPRATOR_TO */
    private static final String SPRATOR_TO = "\\|";

    /** KNABAN_STRING */
    private static final String KNABAN_STRING = "CPMKBF11_Grid_KanbanIssuedPlanDate";

    /** KANBAN_MASTER */
    private static final String KANBAN_MASTER = "CPMKBF11_Grid_PlanDateMaster";

    /** OFFICECODE */
    private static final String OFFICECODE = "CPMKBF11_Grid_OfficeCode";

    /** CUSTOMERCODE */
    private static final String CUSTOMERCODE = "CPMKBF11_Grid_CustomerCode";

    /** FROMDATE */
    private static final String FROMDATE = "CPMKBF11_Grid_FromDate";

    /** TODATE */
    private static final String TODATE = "CPMKBF11_Grid_ToDate";

    /** ORDERMONTH */
    private static final String ORDERMONTH = "CPMKBF11_Grid_OrderMonth";

    /** Kanban Issued Plan Date Service */
    @Autowired
    private CPMKBF11Service service;

    @Override
    protected String getFileId() {
        return ChinaPlusConst.FileId.CPMKBF11;
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
    @RequestMapping(value = "/mm/CPMKBF11/upload",
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
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Sheet sheet = workbook.getSheet(CPMKBF01Controller.SHEET_KANBAN);

        List<CPMKBF01Entity> entityList = new ArrayList<CPMKBF01Entity>();
        // some userinfo in login user"s useroffices
        Map<String, List<UserCustmorEntity>> usercusMap = new HashMap<String, List<UserCustmorEntity>>();
        UserInfo userInfo = getLoginUser(request);
        for (UserOffice userOffice : userInfo.getUserOffice()) {
            List<UserCustmorEntity> listuc = new ArrayList<UserCustmorEntity>();
            List<BusinessPattern> businessPatternList = userOffice.getBusinessPatternList();
            if (businessPatternList != null) {
                for (BusinessPattern bp : businessPatternList) {
                    UserCustmorEntity uc = new UserCustmorEntity();
                    uc.setCustmorCode(bp.getCustomerCode());
                    uc.setBussinessPattern(bp.getBusinessPattern());
                    listuc.add(uc);
                }
            }
            usercusMap.put(userOffice.getOfficeCode(), listuc);
        }

        // all kanbandata in database
        List<CPMKBF01Entity> existList = service.getList((BaseParam) param);

        // all kanbandata in database
        List<CPMKBF01Entity> existListDB = new ArrayList<CPMKBF01Entity>(existList);
        // custmorcode alloffices
        List<OfficeAndCustmorEntity> ocList = service.getOfficeAndCustmor((BaseParam) param);

        // reduce last five row, it is only tips
        int maxRowNum = sheet.getLastRowNum() + 1 - IntDef.INT_FIVE;
        int noDataRowcnt = 0;
        UserOfficeCodesEntity uoce = new UserOfficeCodesEntity();
        UserCustmorEntity uce = new UserCustmorEntity();
        int realCount = 0;
        int loginIds = 0;
        // load data in uploadfile
        for (int i = DETAIL_START_LINE; i <= maxRowNum; i++) {
            CPMKBF01Entity entity = new CPMKBF01Entity();
            entity.setFlag(true);
            int colIndex = COL_THREE;

            if (ValidatorUtils.isBlankRow(sheet, i, DETAIL_START_COL_NO, TOTAL_COL_NUM)) {
                noDataRowcnt++;
                if (noDataRowcnt > BLANK_LINE_NUM - 1) {
                    break;
                }
                continue;
            } else {
                noDataRowcnt = 0;
            }
            realCount++;
            // new.mod
            String tempNewMod = PoiUtil.getStringCellValue(sheet, i, IntDef.INT_ONE);
            entity.setNewMod(tempNewMod);
            if (StringUtil.isEmpty(tempNewMod)) {
                loginIds++;
                continue;
            } else if (!NEW.equals(tempNewMod) && !MOD.equals(tempNewMod)) {
                // else if not 'NEW' or 'MOD', show error and goto next part.(w1004_094)
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_094);
                message.setMessageArgs(new String[] { i + StringConst.COMMA });
                messageLists.add(message);
                continue;
            }
            // reason
            entity.setChangeReason(PoiUtil.getStringCellValue(sheet, i, IntDef.INT_TWO));
            // office code
            String officeCodeString = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(officeCodeString)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), OFFICECODE });
                messageLists.add(message);
                entity.setFlag(false);
            }
            entity.setOfficeCode(officeCodeString);
            colIndex++;
            // customer code
            if (StringUtil.isEmpty(PoiUtil.getStringCellValue(sheet, i, colIndex))) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), CUSTOMERCODE });
                messageLists.add(message);
                entity.setFlag(false);
            }
            entity.setCustomerCode(PoiUtil.getStringCellValue(sheet, i, colIndex));
            colIndex++;

            // order month
            if (StringUtil.isEmpty(PoiUtil.getStringCellValue(sheet, i, colIndex))) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), ORDERMONTH });
                messageLists.add(message);
                entity.setFlag(false);
            } else {
                Date orderMonthFrom = DateTimeUtil.parseMonth(PoiUtil.getStringCellValue(sheet, i, colIndex));
                if (orderMonthFrom != null) {
                    entity.setOrderMonthDate(orderMonthFrom);
                    entity.setOrderMonth(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YEAR_MONTH,
                        entity.getOrderMonthDate()));
                } else {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { String.valueOf(i), ORDERMONTH, "Common_ItemType_Month" });
                    messageLists.add(message);
                    entity.setFlag(false);
                }
            }
            colIndex++;
            // begin date
            if (StringUtil.isEmpty(PoiUtil.getStringCellValue(sheet, i, colIndex))) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), FROMDATE });
                messageLists.add(message);
                entity.setFlag(false);
            } else {
                Date fromDateFrom = DateTimeUtil.parseDate(PoiUtil.getStringCellValue(sheet, i, colIndex));
                if (fromDateFrom != null) {
                    entity.setFromDate(fromDateFrom);
                    entity.setFromDateString(PoiUtil.getStringCellValue(sheet, i, colIndex));
                } else {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { String.valueOf(i), FROMDATE, "Common_ItemType_Date" });
                    messageLists.add(message);
                    entity.setFlag(false);
                }
            }
            colIndex++;
            // end date
            if (StringUtil.isEmpty(PoiUtil.getStringCellValue(sheet, i, colIndex))) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), TODATE });
                messageLists.add(message);
                entity.setFlag(false);
            } else {
                Date toDateFrom = DateTimeUtil.parseDate(PoiUtil.getStringCellValue(sheet, i, colIndex));
                if (toDateFrom != null) {
                    entity.setToDate(toDateFrom);
                    entity.setToDateString(PoiUtil.getStringCellValue(sheet, i, colIndex));
                } else {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { String.valueOf(i), TODATE, "Common_ItemType_Date" });
                    messageLists.add(message);
                    entity.setFlag(false);
                }
            }
            // excel line
            entity.setExcelLine(i);

            // If Effective End Date of Kanban Issued Date <= Effective Start Date of Kanban Issued Date
            // inputed.(w1004_092 {1}：Effective End Date of Kanban Issued Date {2}：Effective Start Date of Kanban Issued
            // Date)
            if (entity.isFlag()) {
                if (!StringUtil.isEmpty(entity.getFromDateString()) && !StringUtil.isEmpty(entity.getToDateString())) {
                    if (entity.getFromDate().after(entity.getToDate())) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_092);
                        message
                            .setMessageArgs(new String[] { String.valueOf(entity.getExcelLine()), TODATE, FROMDATE });
                        messageLists.add(message);
                    }
                }
            }

            if (entity.isFlag()) {
                // If inputed Imp Office Code is not belongs to login user's office list.({1}：inputed Imp Office Code
                // {2}：Kanban Issued Plan Date)

                uoce.setOfficeCodes(entity.getOfficeCode());
                if (!StringUtil.isEmpty(entity.getOfficeCode())) {
                    if (!usercusMap.containsKey(entity.getOfficeCode())) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                        message
                            .setMessageArgs(new String[] { String.valueOf(i), entity.getOfficeCode(), KNABAN_STRING });
                        messageLists.add(message);
                        entity.setFlag(false);
                    }
                }
                uce.setCustmorCode(entity.getCustomerCode());
                uce.setBussinessPattern(CodeConst.BusinessPattern.AISIN);
                if (!StringUtil.isEmpty(entity.getOfficeCode()) && !StringUtil.isEmpty(entity.getCustomerCode())) {

                    List<UserCustmorEntity> list = usercusMap.get(entity.getOfficeCode());
                    if (list == null || !list.contains(uce)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                        message.setMessageArgs(new String[] { String.valueOf(i), entity.getCustomerCode(),
                            KNABAN_STRING });
                        messageLists.add(message);
                        entity.setFlag(false);
                    }
                }
            }
            if (entity.isFlag()) {
                entityList.add(entity);
            }
        }
        if (loginIds == realCount) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
        }
        List<BaseMessage> otherMessageList = new ArrayList<BaseMessage>();
        if (entityList.size() != 0) {
            // If Order Month is not continuous in upload file.
            Collections.sort(entityList);
            // office and custmer max date
            Map<String, List<CPMKBF01Entity>> maxMap = new HashMap<String, List<CPMKBF01Entity>>();
            // all form excel data
            OfficeAndCustmorEntity tempOC = new OfficeAndCustmorEntity();

            for (CPMKBF01Entity entityTo : entityList) {
                String tempKey = entityTo.getOfficeCode() + SPRATOR + entityTo.getCustomerCode();
                if (NEW.equals(entityTo.getNewMod())) {
                    if (maxMap.containsKey(tempKey)) {
                        maxMap.get(tempKey).add(entityTo);
                    } else {
                        List<CPMKBF01Entity> newEntity = new ArrayList<CPMKBF01Entity>();
                        newEntity.add(entityTo);
                        maxMap.put(tempKey, newEntity);
                    }

                    // If in one office, Kanban Issued Plan Date is already exist in Kanban Issued Plan Date.(w1004_074
                    // {2}：Kanban Issued Plan Date {3}：Kanban Issued Plan Date Master)
                    if (!StringUtil.isEmpty(entityTo.getOfficeCode())
                            && !StringUtil.isEmpty(entityTo.getCustomerCode()) && entityTo.getOrderMonthDate() != null) {
                        if (entityTo.isFlag()) {
                            if (existListDB.contains(entityTo)) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_074);
                                message.setMessageArgs(new String[] { String.valueOf(entityTo.getExcelLine()),
                                    KNABAN_STRING, KANBAN_MASTER });
                                otherMessageList.add(message);
                                entityTo.setFlag(false);
                            } else {
                                existList.add(entityTo);
                            }
                        }
                    }

                }
                // If in one office, Kanban Issued Plan Date is not already exist in Kanban Issued Plan Date.(w1004_075
                // {2}：Kanban Issued Plan Date {3}：Kanban Issued Plan Date Master)
                else if (MOD.equals(entityTo.getNewMod())) {
                    if (!StringUtil.isEmpty(entityTo.getOfficeCode())
                            && !StringUtil.isEmpty(entityTo.getCustomerCode())
                            && !StringUtil.isEmpty(entityTo.getOrderMonth())) {
                        if (!existListDB.contains(entityTo)) {
                            if (entityTo.isFlag()) {
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                                message.setMessageArgs(new String[] { String.valueOf(entityTo.getExcelLine()),
                                    KNABAN_STRING, KANBAN_MASTER });
                                otherMessageList.add(message);
                                entityTo.setFlag(false);
                            }
                        } else {
                            for (CPMKBF01Entity eEntity : existList) {
                                if (eEntity.getOfficeCode().equals(entityTo.getOfficeCode())
                                        && eEntity.getCustomerCode().equals(entityTo.getCustomerCode())
                                        && eEntity.getOrderMonthDate().equals(entityTo.getOrderMonthDate())) {
                                    BeanUtils.copyProperties(entityTo, eEntity);
                                }
                            }
                        }
                    }
                }

                // Check for both NEW/MOD is NEW and MOD.

                // If in one office, Kanban Issued Plan Date is not already exist in Kanban Issued Plan Date.(w1004_075
                // {2}：Kanban Issued Plan Date {3}：Kanban Issued Plan Date Master)

                tempOC.setCustomerCode(entityTo.getCustomerCode());
                tempOC.setOfficeCode(entityTo.getOfficeCode());
                if (!StringUtil.isEmpty(entityTo.getOfficeCode()) && !StringUtil.isEmpty(entityTo.getCustomerCode())) {
                    if (entityTo.isFlag()) {
                        if (!ocList.contains(tempOC)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_069);
                            message.setMessageArgs(new String[] { String.valueOf(entityTo.getExcelLine()), OFFICECODE,
                                CUSTOMERCODE });
                            messageLists.add(message);
                        }
                    }
                }
            }
            for (Map.Entry<String, List<CPMKBF01Entity>> entry : maxMap.entrySet()) {
                if (entry.getValue().size() < IntDef.INT_TWELVE) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_089);
                    message.setMessageArgs(new String[] { entry.getKey().split(SPRATOR_TO)[1] });
                    otherMessageList.add(message);
                }
            }

            if (messageLists.size() == 0) {
                // If Effective End Date of Kanban Issued Date inputed is not continuous with next Effective Start Date
                // of
                // Kanban Issued Date in upload file.
                Collections.sort(existList);
                List<CPMKBF01Entity> list = existList;

                // If first Effective Start Date of Kanban Issued Date in upload file not continuous with last order
                // month
                // in
                // DB’s Effective End Date of Kanban Issued Date.
                if (list.size() > 0) {
                    for (int i = 0; i < existList.size() - 1; i++) {
                        if (existList.get(i).getOfficeCode().equals(existList.get(i + 1).getOfficeCode())
                                && existList.get(i).getCustomerCode().equals(existList.get(i + 1).getCustomerCode())) {

                            if (IntDef.INT_ONE != DateTimeUtil.getDiffMonths(existList.get(i).getOrderMonthDate(),
                                existList.get(i + 1).getOrderMonthDate())) {
                                String customerCode = null;
                                int excelLine = 0;
                                if (existList.get(i + 1).getExcelLine() == 0) {
                                    customerCode = existList.get(i).getCustomerCode();
                                    excelLine = existList.get(i).getExcelLine();
                                } else {
                                    customerCode = existList.get(i + 1).getCustomerCode();
                                    excelLine = existList.get(i + 1).getExcelLine();
                                }
                                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_088);
                                message.setMessageArgs(new String[] { String.valueOf(excelLine), customerCode,
                                    "CPMKBF11_Grid_OrderMonth" });
                                messageLists.add(message);
                            } else {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(existList.get(i).getToDate());
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                                Date fromDate = cal.getTime();

                                if (!fromDate.equals(existList.get(i + 1).getFromDate())) {
                                    String customerCode = null;
                                    int excelLine = 0;
                                    if (existList.get(i + 1).getExcelLine() == 0) {
                                        customerCode = existList.get(i).getCustomerCode();
                                        excelLine = existList.get(i).getExcelLine();
                                    } else {
                                        customerCode = existList.get(i + 1).getCustomerCode();
                                        excelLine = existList.get(i + 1).getExcelLine();
                                    }
                                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_088);
                                    message.setMessageArgs(new String[] { String.valueOf(excelLine), customerCode,
                                        "CPMKBF11_Grid_KanbanIssuedDate" });
                                    messageLists.add(message);
                                }
                            }
                        }
                    }

                }
            }
            messageLists.addAll(otherMessageList);
        }

        if (messageLists.size() <= 0) {
            service.doSaveKbData(entityList, (BaseParam) param);
        }
        return messageLists;
    }

}
