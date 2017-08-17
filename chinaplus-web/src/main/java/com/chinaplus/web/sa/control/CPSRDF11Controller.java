/**
 * Controller of Rundown Remarks Upload
 * 
 * @screen CPSRDF11
 * @author zhang_chi
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
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
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.util.CodeCategoryManager;
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
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.sa.entity.CPSRDF11Entity;
import com.chinaplus.web.sa.service.CPSRDF11Service;

/**
 * Controller of Rundown Remarks Upload
 */
@Controller
public class CPSRDF11Controller extends BaseFileController {

    private static final String RUNDOWN = "Rundown";

    /**
     * cpsrdf11Service.
     */
    @Autowired
    private CPSRDF11Service cpsrdf11Service;

    @Override
    protected String getFileId() {
        return FileId.CPSRDF11;
    }

    /**
     * V-V Business Shipping Route Master Upload
     * 
     * @param file MultipartFile
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @RequestMapping(value = "/sa/CPSRDF11/upload",
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
     * @param file uploaded file
     * @param workbook uploaded excel
     * @param param the parameters
     * @param <T> the parameter class type
     * @param request the HttpServletRequest
     * @return result message
     */
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {
        UserInfo userInfo = getLoginUser(request);
        // get work sheet sheet
        Sheet sheet = workbook.getSheet(RUNDOWN);
        readDataXlsx(sheet, param, userInfo, request);
        return new ArrayList<BaseMessage>();
    }

    /**
     * read Data Xlsx
     * 
     * @param sheet sheet
     * @param param param
     * @param userInfo userInfo
     * @param request request
     */
    private void readDataXlsx(Sheet sheet, BaseParam param, UserInfo userInfo, HttpServletRequest request) {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        List<CPSRDF11Entity> dataList = new ArrayList<CPSRDF11Entity>();

        boolean dataFlag = false;

        // read excel
        int sheetMaxRow = sheet.getLastRowNum() + 1;
        int READ_START_COL = IntDef.INT_THREE;
        int READ_TOTAL_COL = IntDef.INT_TWENTY_ONE;

        for (int startRow = IntDef.INT_EIGHT; startRow <= sheetMaxRow; startRow++) {
            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {

                String type = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_ONE));

                if (!ValidatorUtils.requiredValidator(type)) {
                    continue;
                }
                dataFlag = true;
                String startRowNum = StringUtil.toSafeString(startRow);
                if (!type.equals(ShipType.MOD)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_141);
                    message.setMessageArgs(new String[] { startRowNum });
                    messageLists.add(message);
                    continue;
                }

                CPSRDF11Entity entity = new CPSRDF11Entity();
                boolean conFlag = true;

                // set ttcPartNo
                String ttcPartNo = StringUtil.toSafeString(PoiUtil
                    .getStringCellValue(sheet, startRow, IntDef.INT_THREE));
                if (!ValidatorUtils.requiredValidator(ttcPartNo)) {
                    conFlag = false;
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPSRDF01_Grid_TTCPartNo" });
                    messageLists.add(message);
                } else {
                    entity.setTtcPartNo(ttcPartNo);
                }

                // set impOfficeCode
                String impOfficeCode = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow,
                    IntDef.INT_EIGHT));
                if (!ValidatorUtils.requiredValidator(impOfficeCode)) {
                    conFlag = false;
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPSRDF01_Grid_ImpOfficeCode" });
                    messageLists.add(message);
                } else {
                    entity.setImpOfficeCode(impOfficeCode);
                }

                // set ttcCustomerCode
                String ttcCustomerCode = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow,
                    IntDef.INT_NINE));
                if (!ValidatorUtils.requiredValidator(ttcCustomerCode)) {
                    conFlag = false;
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPSRDF01_Grid_TTCCustomerCode" });
                    messageLists.add(message);
                } else {
                    entity.setTtcCustomerCode(ttcCustomerCode);
                }

                // set rundownRemark
                String rundownRemark = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow,
                    IntDef.INT_TWENTY_ONE));
                if (ValidatorUtils.requiredValidator(rundownRemark)) {
                    if (!ValidatorUtils.maxLengthValidator(rundownRemark, IntDef.INT_TWO_HUNDRED_FIFTY_FIVE)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                        message.setMessageArgs(new String[] { startRowNum, "CPSRDF01_Grid_RundownRemark",
                            IntDef.INT_TWO_HUNDRED_FIFTY_FIVE + "" });
                        messageLists.add(message);
                    } else {
                        entity.setRundownRemark(rundownRemark);
                    }
                }

                if (conFlag) {
                    // set row number
                    entity.setRowNum(startRow);

                    // add data
                    dataList.add(entity);
                }
            } else {
                if (ValidatorUtils.isExcelEnd(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                    break;
                }
            }
        }

        // check data all blank
        if (!dataFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
            throw new BusinessException(messageLists);
        }

        if (dataList != null && dataList.size() > 0) {
            SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
            UserManager um = UserManager.getLocalInstance(sm);
            UserOffice userOffice = um.getCurrentOfficeInfo();
            // deal data detail
            dealDataDetail(dataList, userInfo, messageLists, userOffice);
        } else {
            throw new BusinessException(messageLists);
        }

    }

    /**
     * deal data detail
     * 
     * @param dataList dataList
     * @param userInfo userInfo
     * @param messageLists messageLists
     * @param userOffice userOffice
     */
    @SuppressWarnings("unchecked")
    private void dealDataDetail(List<CPSRDF11Entity> dataList, UserInfo userInfo, List<BaseMessage> messageLists,
        UserOffice userOffice) {

        // all DB query ttcPartNo + impOfficeCode + ttcCustomerCode ,partsId,businessPattern
        Map<String, String> titpMapDB = cpsrdf11Service.getTITPMap(dataList);

        Integer userId = userInfo.getUserId();

        // get current BusinessPattern Map
        Map<String, Object> busPatternMap = cpsrdf11Service.getBusinessPatternMap(userOffice);
        // officeCode Map
        Map<String, Integer> officeCodeMap = (Map<String, Integer>) busPatternMap.get("officeCodeMap");
        // customerCode Map
        // Map<String, Integer> customerCodeMap = (Map<String, Integer>) busPatternMap.get("customerCodeMap");
        // officeCode + customerCode Map
        Map<String, String> officeCustCodeMap = (Map<String, String>) busPatternMap.get("officeCustCodeMap");

        // check all data
        for (CPSRDF11Entity ce : dataList) {
            String rowNum = StringUtil.toSafeString(ce.getRowNum());

            // check impOfficeCode
            String impOfficeCode = ce.getImpOfficeCode();
            Integer officeCodeM = officeCodeMap.get(impOfficeCode);
            if (null == officeCodeM) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                message.setMessageArgs(new String[] { rowNum, impOfficeCode, "CPSRDF01_Grid_RundownMasterData" });
                messageLists.add(message);
                continue;
            }

            // check ttcCustomerCode
            String ttcCustomerCode = ce.getTtcCustomerCode();
            String customerCodeM = officeCustCodeMap.get(impOfficeCode + StringConst.UNDERLINE + ttcCustomerCode);
            String businessPatternDB = "";
            if (customerCodeM == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                message.setMessageArgs(new String[] { rowNum, ttcCustomerCode, "CPSRDF01_Grid_RundownMasterData" });
                messageLists.add(message);
                continue;
            } else {
                String[] customerCodeMs = customerCodeM.split(StringConst.UNDERLINE);
                businessPatternDB = customerCodeMs[0];
                if ((InactiveFlag.INACTIVE + "").equals(customerCodeMs[1])) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                    message.setMessageArgs(new String[] { rowNum, ttcCustomerCode, "CPSRDF01_Grid_RundownMasterData" });
                    messageLists.add(message);
                    continue;
                }
            }

            // check ttcPartNo + impOfficeCode + ttcCustomerCode
            String key = ce.getTtcPartNo() + StringConst.UNDERLINE + impOfficeCode + StringConst.UNDERLINE
                    + ttcCustomerCode;
            String value = titpMapDB.get(key);
            if (null == value) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_142);
                message.setMessageArgs(new String[] { rowNum });
                messageLists.add(message);
            } else {
                String[] values = value.split(StringConst.UNDERLINE);
                Integer partsId = Integer.valueOf(values[0]);
                Integer businessPattern = Integer.valueOf(values[1]);
                ce.setPartsId(partsId);
                ce.setUpdatedBy(userId);
                if (!StringUtil.isEmpty(businessPatternDB)
                        && !businessPattern.equals(StringUtil.toInteger(businessPatternDB))) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_087);
                    message.setMessageArgs(new String[] {
                        rowNum,
                        CodeCategoryManager.getCodeName(userInfo.getLanguage().getCode(),
                            CodeMasterCategory.BUSINESS_PATTERN, businessPattern), "CPSRDF01_Grid_RundownMasterData" });
                    messageLists.add(message);
                }
            }
        }

        // throw exception
        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // deal TNT_RUNDOWN_MASTER data
        cpsrdf11Service.doDealDBData(dataList);
    }

}
