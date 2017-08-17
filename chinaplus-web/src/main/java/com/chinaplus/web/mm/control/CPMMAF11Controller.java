/**
 * CPMMAF11Controller.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
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

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.CPMMAF01Entity;
import com.chinaplus.web.mm.service.CPMMAF11Service;

/**
 * Email Alert Master upload.
 */
@Controller
public class CPMMAF11Controller extends BaseFileController {
    /** BLANK_LINE_NUM */
    private static final int BLANK_LINE_NUM = 10;
    /** DETAIL_START_COL_NO */
    private static final int DETAIL_START_COL_NO = 0;

    /** TOTAL_COL_NUM */
    private static final int TOTAL_COL_NUM = 8;

    /** TOTAL_COL_NUM */
    private static final String NEW = "NEW";

    /** MOD */
    private static final String MOD = "MOD";

    /** EMAIL_ALERT_LEVEL */
    private static final String EMAIL_ALERT_LEVEL = "EMAIL_ALERT_LEVEL";

    /** DISCONTINUE_INDICATOR */
    private static final String DISCONTINUE_INDICATOR = "DISCONTINUE_INDICATOR";

    /** email alert service */
    @Autowired
    private CPMMAF11Service service;

    /**
     *
     * @return
     * @see com.chinaplus.core.base.BaseFileController#getFileId()
     */
    @Override
    protected String getFileId() {
        return ChinaPlusConst.FileId.CPMMAF11;
    }

    /**
     * email alert upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/mm/CPMMAF11/upload",
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
        Sheet sheet = workbook.getSheet(CPMMAF01Controller.SHEET_EMAIL);

        List<CPMMAF01Entity> entityList = new ArrayList<CPMMAF01Entity>();
        Map<String, String> maps = new HashMap<String, String>();

        // reduce last ten row, it is only tips
        int maxRowNum = sheet.getLastRowNum() + 1 - IntDef.INT_TEN;
        int noDataRowcnt = 0;
        int realCount = 0;
        int loginIds = 0;
        // load data in uploadfile
        Map<String, Map<Integer, String>> codeMaps = CodeCategoryManager.getCodeCategaryByLang(param.getLanguage());
        for (int i = IntDef.INT_SIX; i <= maxRowNum; i++) {
            CPMMAF01Entity entity = new CPMMAF01Entity();
            int colIndex = IntDef.INT_THREE;

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
            if (StringUtil.isEmpty(tempNewMod)) {
                loginIds++;
                continue;
            }
            if (!NEW.equals(tempNewMod) && !MOD.equals(tempNewMod)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_094);
                message.setMessageArgs(new String[] { String.valueOf(i) });
                messageLists.add(message);
                continue;
            }
            entity.setNewMod(tempNewMod);
            // reason
            String reason = PoiUtil.getStringCellValue(sheet, i, IntDef.INT_TWO);
            if (!StringUtil.isEmpty(reason)
                    && !ValidatorUtils.maxLengthValidator(reason, IntDef.INT_TWO_HUNDRED_FIFTY_FIVE)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_ReasonsforChange",
                    IntDef.INT_TWO_HUNDRED_FIFTY_FIVE + "" });
                messageLists.add(message);
            }
            entity.setReason(reason);

            // login id
            String loginId = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(loginId)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_LoginID" });
                messageLists.add(message);
            } else {

                if (!ValidatorUtils.maxLengthValidator(loginId, IntDef.INT_TWENTY)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_LoginID",
                        IntDef.INT_TWENTY + StringConst.EMPTY });
                    messageLists.add(message);
                } else {
                    entity.setLoginId(loginId);
                }
            }
            colIndex++;

            // impOfficeCode
            String impOfficeCode = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(impOfficeCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_ImpOfficeCd" });
                messageLists.add(message);
            } else {
                if (!ValidatorUtils.maxLengthValidator(impOfficeCode, IntDef.INT_TEN)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_ImpOfficeCd",
                        IntDef.INT_TEN + "" });
                    messageLists.add(message);
                } else {
                    // user office
                    List<String> userOfficeCodes = new ArrayList<String>();
                    UserInfo loginUser = getLoginUser(request);
                    if (loginUser.getUserOffice() != null && !loginUser.getUserOffice().isEmpty()) {
                        for (UserOffice office : loginUser.getUserOffice()) {
                            if (office != null) {
                                userOfficeCodes.add(office.getOfficeCode());
                            }
                        }
                    }
                    // If user has no permission to upload some office's Email Alert Master.(w1004_087)
                    if (!userOfficeCodes.contains(impOfficeCode)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                        message.setMessageArgs(new String[] { String.valueOf(i), impOfficeCode,
                            "CPMMAF01_Label_EmailAlertData" });
                        messageLists.add(message);

                    } else {
                        entity.setImpOfficeCode(impOfficeCode);
                    }
                }
            }
            colIndex++;

            // ttcCustomerCode
            String ttcCustomerCode = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(ttcCustomerCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_TTCCustCd" });
                messageLists.add(message);
            } else {
                String[] ttcCustomers = ttcCustomerCode.split(StringConst.COMMA);
                boolean flag = true;
                for (String c : ttcCustomers) {
                    if (!ValidatorUtils.maxLengthValidator(c, IntDef.INT_FIFTEEN)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                        message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_TTCCustCd",
                            IntDef.INT_FIFTEEN + StringConst.EMPTY });
                        messageLists.add(message);
                        flag = false;
                    }
                }
                if (flag) {
                    entity.setTtcCustomerCode(ttcCustomerCode);
                }
            }
            colIndex++;
            // alertLevel
            String alertLevel = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(alertLevel)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_AlertLevel" });
                messageLists.add(message);
            } else {
                if (!codeMaps.get(EMAIL_ALERT_LEVEL).containsValue(alertLevel)) {
                    Map<Integer, String> map = codeMaps.get(EMAIL_ALERT_LEVEL);
                    String value = StringConst.EMPTY;
                    for (Map.Entry<Integer, String> s : map.entrySet()) {
                        value = value + s.getValue() + StringConst.COMMA;
                    }
                    if (!StringUtil.isEmpty(value)) {
                        value = value.substring(0, value.length() - 1);
                    }
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_AlertLevel", value });
                    messageLists.add(message);
                } else {
                    Map<Integer, String> levelMap = codeMaps.get(EMAIL_ALERT_LEVEL);

                    for (Map.Entry<Integer, String> s : levelMap.entrySet()) {
                        if (s.getValue().equals(alertLevel)) {
                            entity.setAlertLevel(s.getKey().toString());
                        }
                    }
                }
            }
            colIndex++;
            // remark
            String remark = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (!StringUtil.isEmpty(remark) && remark.length() > IntDef.INT_EIGHTY) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                message
                    .setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_Remark", IntDef.INT_EIGHTY + "" });
                messageLists.add(message);
            } else {
                entity.setRemark(remark);
            }
            colIndex++;

            // inActivity
            String inActivity = PoiUtil.getStringCellValue(sheet, i, colIndex);

            if (StringUtil.isEmpty(inActivity)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_DiscontinueIndicator" });
                messageLists.add(message);
            } else {
                if (!codeMaps.get(DISCONTINUE_INDICATOR).containsValue(inActivity)) {
                    Map<Integer, String> map = codeMaps.get(DISCONTINUE_INDICATOR);
                    String value = StringConst.EMPTY;
                    for (Map.Entry<Integer, String> s : map.entrySet()) {
                        value = value + s.getValue() + StringConst.COMMA;
                    }
                    if (!StringUtil.isEmpty(value)) {
                        value = value.substring(0, value.length() - 1);
                    }

                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMMAF01_Grid_DiscontinueIndicator",
                        value });
                    messageLists.add(message);
                } else {
                    Map<Integer, String> levelMap = codeMaps.get(DISCONTINUE_INDICATOR);

                    for (Map.Entry<Integer, String> s : levelMap.entrySet()) {
                        if (s.getValue().equals(inActivity)) {
                            entity.setInActiveFlag((s.getKey()));
                        }
                    }
                }
            }
            colIndex++;
            // excel line
            entity.setExcelLine(i);
            entityList.add(entity);
            // If TTC Customer Code's office code is not related to Imp Office Code inputed.(1004_069 {1}：TTC Customer
            // Code inputed {2}：Imp Office Code inputed)
            if (!StringUtil.isEmpty(entity.getTtcCustomerCode()) && !StringUtil.isEmpty(entity.getImpOfficeCode())) {
                String[] ttcCuses = entity.getTtcCustomerCode().split(StringConst.COMMA);
                for (String s : ttcCuses) {
                    param.getSwapData().put("ttcCustomerCode", s);
                    param.getSwapData().put("impOfficeCode", entity.getImpOfficeCode());
                    if (service.getCusOffice(param) == 0) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_069);
                        message.setMessageArgs(new String[] { String.valueOf(i), s, entity.getImpOfficeCode() });
                        messageLists.add(message);
                    }
                }
            }
            //
            if (!StringUtil.isEmpty(entity.getLoginId()) && !StringUtil.isEmpty(entity.getImpOfficeCode())) {
                param.getSwapData().put("loginId", entity.getLoginId());
                param.getSwapData().put("impOfficeCode", entity.getImpOfficeCode());
                if (service.getUserOffice(param) == 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_069);
                    message.setMessageArgs(new String[] { String.valueOf(i), entity.getLoginId(),
                        entity.getImpOfficeCode() });
                    messageLists.add(message);
                }
            }
            if (NEW.equals(entity.getNewMod())) {
                // If Email Alert data is already exist in Email Alert Master.(w1004_074 {2}：inputed TTC Customer Code
                // {3}：Email Alert Master)
                if (!StringUtil.isEmpty(entity.getLoginId()) && !StringUtil.isEmpty(entity.getImpOfficeCode())
                        && !StringUtil.isEmpty(entity.getTtcCustomerCode())) {
                    String[] ttcCuses = entity.getTtcCustomerCode().split(StringConst.COMMA);
                    List<String> list = new ArrayList<String>();
                    for (String s : ttcCuses) {
                        if (!list.contains(s)) {
                            list.add(s);
                        }
                    }
                    for (String s : list) {
                        param.getSwapData().put("ttcCustomerCode", s);
                        param.getSwapData().put("impOfficeCode", entity.getImpOfficeCode());
                        param.getSwapData().put("loginId", entity.getLoginId());
                        if (service.checkExist(param) != 0) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_074);
                            message.setMessageArgs(new String[] { String.valueOf(i), s, "CPMMAF01_Label_PageTitle" });
                            messageLists.add(message);
                        }
                    }
                }
                // If NEW/MOD is NEW and Discontinue Indicator inputed is not 'N'.(w1004_093)
                if (!StringUtil.isEmpty(inActivity) && !CodeConst.TypeYN.N.equals(inActivity)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_093);
                    message.setMessageArgs(new String[] { String.valueOf(i) });
                    messageLists.add(message);
                }
            }
            if (MOD.equals(entity.getNewMod())) {
                if (!StringUtil.isEmpty(entity.getLoginId()) && !StringUtil.isEmpty(entity.getImpOfficeCode())
                        && !StringUtil.isEmpty(entity.getTtcCustomerCode())) {
                    String[] ttcCuses = entity.getTtcCustomerCode().split(StringConst.COMMA);
                    List<String> list = new ArrayList<String>();
                    for (String s : ttcCuses) {
                        if (!list.contains(s)) {
                            list.add(s);
                        }
                    }
                    for (String s : list) {
                        param.getSwapData().put("ttcCustomerCode", s);
                        param.getSwapData().put("impOfficeCode", entity.getImpOfficeCode());
                        param.getSwapData().put("loginId", entity.getLoginId());

                        if (service.checkExist(param) == 0) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                            message.setMessageArgs(new String[] { String.valueOf(i), s, "CPMMAF01_Label_PageTitle" });
                            messageLists.add(message);
                        }
                    }
                }
            }
            if (!StringUtil.isEmpty(entity.getLoginId()) && !StringUtil.isEmpty(entity.getImpOfficeCode())
                    && !StringUtil.isEmpty(entity.getTtcCustomerCode())) {
                List<String> list = new ArrayList<String>();
                String[] ttcCuses = entity.getTtcCustomerCode().split(",");
                boolean flag = true;
                boolean lineFlag = true;
                for (String s : ttcCuses) {
                    String key = entity.getLoginId() + StringConst.COMMA + entity.getImpOfficeCode()
                            + StringConst.COMMA + s;
                    if (list.contains(key)) {
                        if (lineFlag) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_155);
                            message
                                .setMessageArgs(new String[] { i + StringConst.EMPTY, s, "CPMMAF01_Label_PageTitle" });
                            messageLists.add(message);
                        }
                        lineFlag = false;
                    } else {
                        list.add(key);
                    }
                    if (maps.containsKey(key)) {
                        if (flag && lineFlag) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_078);
                            message.setMessageArgs(new String[] { i + StringConst.EMPTY, "CPMMAF01_Grid_UploadFile" });
                            messageLists.add(message);
                        }
                        flag = false;
                    }
                    maps.put(key, i + StringConst.EMPTY);
                }
            }

        }
        if (loginIds == realCount) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
        }
        if (messageLists.size() == 0) {
            service.saveList(entityList, param);
        }

        return messageLists;

    }
}
