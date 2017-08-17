/**
 * CPMSPF11Controller.
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

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.entity.TnmReason;
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
import com.chinaplus.web.mm.service.CPMSPF11Service;

/**
 * Shipping Plan Rev Reason Upload.
 */
@Controller
public class CPMSPF11Controller extends BaseFileController {
    /** BLANK_LINE_NUM */
    private static final int BLANK_LINE_NUM = 10;
    /** DETAIL_START_COL_NO */
    private static final int DETAIL_START_COL_NO = 0;

    /** TOTAL_COL_NUM */
    private static final int TOTAL_COL_NUM = 5;

    /** TOTAL_COL_NUM */
    private static final String NEW = "NEW";

    /** MOD */
    private static final String MOD = "MOD";

    /** BUSSINESS_PATTERN */
    private static final String BUSSINESS_PATTERN = "BUSINESS_PATTERN";

    /** DISCONTINUE_INDICATOR */
    private static final String DISCONTINUE_INDICATOR = "DISCONTINUE_INDICATOR";

    /** email alert service */
    @Autowired
    private CPMSPF11Service service;

    @Override
    protected String getFileId() {
        return ChinaPlusConst.FileId.CPMSPF11;
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
    @RequestMapping(value = "/mm/CPMSPF11/upload",
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
        Map<String, Map<Integer, String>> codeMaps = CodeCategoryManager.getCodeCategaryByLang(param.getLanguage());
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Sheet sheet = workbook.getSheet(CPMSPF01Controller.SHEET_NAME);

        List<TnmReason> entityList = new ArrayList<TnmReason>();
        Map<String, String> maps = new HashMap<String, String>();

        // reduce last ten row, it is only tips
        int maxRowNum = sheet.getLastRowNum() + 1 - IntDef.INT_FIVE;
        int noDataRowcnt = 0;
        int realCount = 0;
        int loginIds = 0;
        // load data in uploadfile
        for (int i = IntDef.INT_SIX; i <= maxRowNum; i++) {
            boolean flag = false;
            TnmReason entity = new TnmReason();
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
            if (!StringUtil.isEmpty(reason) && reason.length() > IntDef.INT_SEVENTY) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004);
                messageLists.add(message);
            }
            entity.setReason(reason);

            // business pattern
            String businessPattern = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(businessPattern)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_BusinessPattern" });
                messageLists.add(message);
                flag = true;
            } else {
                if (!codeMaps.get(BUSSINESS_PATTERN).containsValue(businessPattern)) {
                    Map<Integer, String> map = codeMaps.get(BUSSINESS_PATTERN);
                    String value = StringConst.EMPTY;
                    for (Map.Entry<Integer, String> s : map.entrySet()) {
                        value = value + s.getValue() + StringConst.COMMA;
                    }
                    if (!StringUtil.isEmpty(value)) {
                        value = value.substring(0, value.length() - 1);
                    }
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_BusinessPattern", value });
                    messageLists.add(message);
                    flag = true;
                } else {
                    Map<Integer, String> levelMap = codeMaps.get(BUSSINESS_PATTERN);

                    for (Map.Entry<Integer, String> s : levelMap.entrySet()) {
                        if (s.getValue().equals(businessPattern)) {
                            entity.setBusinessPattern(s.getKey());
                        }
                    }
                }
            }

            colIndex++;

            // reason desc
            String reasonDesc = PoiUtil.getStringCellValue(sheet, i, colIndex);
            if (StringUtil.isEmpty(reasonDesc)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_SPRevReasonNObr" });
                messageLists.add(message);
                flag = true;
            }
            if (!StringUtil.isEmpty(reasonDesc) && reasonDesc.length() > IntDef.INT_EIGHTY) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_SPRevReasonNObr",
                    IntDef.INT_EIGHTY + "" });
                messageLists.add(message);
                flag = true;
            }
            entity.setReasonDesc(reasonDesc);
            colIndex++;
            // inActivity
            String inActivity = PoiUtil.getStringCellValue(sheet, i, colIndex);

            if (StringUtil.isEmpty(inActivity)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_DiscontinueIndicator" });
                messageLists.add(message);
            }

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
                message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_DiscontinueIndicator", value });
                messageLists.add(message);
            } else {
                Map<Integer, String> levelMap = codeMaps.get(DISCONTINUE_INDICATOR);

                for (Map.Entry<Integer, String> s : levelMap.entrySet()) {
                    if (s.getValue().equals(inActivity)) {
                        entity.setInactiveFlag((s.getKey()));
                    }
                }
            }
            colIndex++;
            entityList.add(entity);
            // If for this Business Pattern, Shipping Plan Revison Reason is already exist in Reason Master.(w1004_074
            // {2}：Shipping Plan Revison Reason {3}：Reason Master)
            if (NEW.equals(entity.getNewMod())) {
                if (entity.getBusinessPattern() != null && !StringUtil.isEmpty(entity.getReasonDesc())) {
                    if (!flag) {
                        param.getSwapData().put("businessPattern", entity.getBusinessPattern());
                        param.getSwapData().put("reasonDesc", entity.getReasonDesc());
                        if (service.checkExist(param) != 0) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_074);
                            message.setMessageArgs(new String[] { String.valueOf(i), entity.getReasonDesc(),
                                "CPMSPF01_Label_PageTitle" });
                            messageLists.add(message);
                        }

                    }
                }
                // If NEW/MOD is NEW and Discontinue Indicator inputed is not 'N'.(w1004_093)
                if (!"N".equals(inActivity)) {
                    Map<Integer, String> levelMap = codeMaps.get(DISCONTINUE_INDICATOR);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                    message.setMessageArgs(new String[] { String.valueOf(i), "CPMSPF01_Grid_DiscontinueIndicator",
                        levelMap.get(IntDef.INT_ZERO) });
                    messageLists.add(message);
                }
            }
            if (MOD.equals(entity.getNewMod())) {
                if (entity.getBusinessPattern() != null && !StringUtil.isEmpty(entity.getReasonDesc())) {
                    if (!flag) {
                        param.getSwapData().put("businessPattern", entity.getBusinessPattern());
                        param.getSwapData().put("reasonDesc", entity.getReasonDesc());
                        if (service.checkExist(param) == 0) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                            message.setMessageArgs(new String[] { String.valueOf(i), entity.getReasonDesc(),
                                "CPMSPF01_Label_PageTitle" });
                            messageLists.add(message);
                        }
                    }
                }
            }
            // check same data
            if (entity.getBusinessPattern() != null && !StringUtil.isEmpty(entity.getReasonDesc())) {

                String businessPatternC = entity.getBusinessPattern().toString();
                String key = businessPatternC + StringConst.COMMA + entity.getReasonDesc();
                if (maps.containsKey(key)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_078);
                    message.setMessageArgs(new String[] { maps.get(key), i + StringConst.EMPTY });
                    messageLists.add(message);
                }
                maps.put(key, i + StringConst.EMPTY);
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
