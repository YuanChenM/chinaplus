/**
 * Controller of shanghai Shipping Route Master upload
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.common.consts.CodeConst.TypeYN;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.entity.CPMSRF11DateEntity;
import com.chinaplus.web.mm.entity.CPMSRF13Entity;
import com.chinaplus.web.mm.service.AisinCommonService;
import com.chinaplus.web.mm.service.CPMPMS01Service;
import com.chinaplus.web.mm.service.CPMSRF11Service;
import com.chinaplus.web.mm.service.CPMSRF13Service;

/**
 * CPMSRF13Controller.
 */
@Controller
public class CPMSRF13Controller extends BaseFileController {

    /** session key */
    private static final String FILE_UPLOAD_SESSION_KEY = "sessionKey";
    /** TIANJINAISIN_SHIPPING_ROUTE */
    private static final String SHANGHAIAISIN_SHIPPING_ROUTE = "SHANGHAIAISIN_Shipping_Route";

    /**
     * aisinCommonController.
     */
    @Autowired
    private AisinCommonController aisinCommonController;
    /**
     * aisinCommonService.
     */
    @Autowired
    private AisinCommonService aisinCommonService;
    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMPMS01Service cpmpms01Service;
    /**
     * cpmsrf11Service.
     */
    @Autowired
    private CPMSRF11Service cpmsrf11Service;
    /**
     * cpmsrf13Service.
     */
    @Autowired
    private CPMSRF13Service cpmsrf13Service;

    @Override
    protected String getFileId() {
        return FileId.CPMSRF13;
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
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) {

        SessionInfoManager context = SessionInfoManager.getContextInstance(request);

        List<CPMSRF13Entity> dataList = new ArrayList<CPMSRF13Entity>();
        // MOD change data List
        List<AisinCommonEntity> modChangeDetailList = new ArrayList<AisinCommonEntity>();
        List<AisinCommonEntity> modChangeList = new ArrayList<AisinCommonEntity>();

        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get work sheet sheet
            Sheet sheet = workbook.getSheet(SHANGHAIAISIN_SHIPPING_ROUTE);

            Map<String, Object> maps = readDataXlsx(sheet, request, param);

            dataList = (List<CPMSRF13Entity>) maps.get("cpmsrf13EntityList");
            modChangeDetailList = (List<AisinCommonEntity>) maps.get("modChangeDetailList");
            modChangeList = (List<AisinCommonEntity>) maps.get("modChangeMaster");
        }
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {

                String mapKey = param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString();
                dataList = (List<CPMSRF13Entity>) context.get(mapKey);
                modChangeDetailList = (List<AisinCommonEntity>) context.get(mapKey + "__modChangeDetailList");
                modChangeList = (List<AisinCommonEntity>) context.get(mapKey + "__modChangeMaster");
                context.remove(mapKey);
            }

            // upload
            cpmsrf13Service.doNewAndModData(dataList, modChangeDetailList, modChangeList);

        }

        return new ArrayList<BaseMessage>();
    }

    /**
     * read Data Xlsx
     * 
     * @param sheet sheet
     * @param request request
     * @param param param
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> readDataXlsx(Sheet sheet, HttpServletRequest request, BaseParam param) {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        List<CPMSRF13Entity> cpmsrf13EntityList = new ArrayList<CPMSRF13Entity>();
        List<CPMSRF13Entity> cpmsrf13EntityNewList = new ArrayList<CPMSRF13Entity>();
        List<CPMSRF13Entity> cpmsrf13EntityModList = new ArrayList<CPMSRF13Entity>();
        List<CPMSRF13Entity> cpmsrf13EntityModYList = new ArrayList<CPMSRF13Entity>();

        boolean shippingRouteCodeFlag = false;

        // read excel
        int sheetMaxRow = sheet.getLastRowNum() + IntDef.INT_TEN;
        String legend = MessageManager.getMessage("CPMSRF02_Grid_Legend", Language.CHINESE.getLocale());
        int READ_START_COL = IntDef.INT_ONE;
        int READ_TOTAL_COL = IntDef.INT_SEVENTEEN;

        for (int startRow = IntDef.INT_SIX; startRow <= sheetMaxRow; startRow++) {
            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                int startCol = IntDef.INT_ONE;
                String type = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol));

                if (!ValidatorUtils.requiredValidator(type)) {
                    continue;
                }
                if (legend.equals(type)) {
                    break;
                }
                shippingRouteCodeFlag = true;
                String startRowNum = StringUtil.toSafeString(startRow);
                if (!type.equals(ShipType.NEW) && !type.equals(ShipType.MOD)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_094);
                    message.setMessageArgs(new String[] { startRowNum });
                    messageLists.add(message);
                    continue;
                }

                CPMSRF13Entity entity = new CPMSRF13Entity();

                // set type
                entity.setType(type);

                // set ShippingRouteCode
                startCol += IntDef.INT_TWO;
                String shippingRouteCode = StringUtil.toSafeString(PoiUtil
                    .getStringCellValue(sheet, startRow, startCol));
                if (!ValidatorUtils.requiredValidator(shippingRouteCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingRouteCd" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(shippingRouteCode, IntDef.INT_THIRTY)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingRouteCd",
                        StringUtil.toSafeString(IntDef.INT_THIRTY) });
                    messageLists.add(message);
                } else {
                    entity.setShippingRouteCode(shippingRouteCode);
                }

                // set firstEtd
                startCol++;
                String firstEtd = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(firstEtd)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_EffectiveStartDate" });
                    messageLists.add(message);
                } else {
                    Date firstEtdDate = DateTimeUtil.parseDate(firstEtd);
                    if (!ValidatorUtils.requiredValidator(firstEtdDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_EffectiveStartDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        entity.setFirstEtd(firstEtdDate);
                    }
                }

                // set lastEtd
                startCol++;
                String lastEtd = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(lastEtd)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_EffectiveEndDate" });
                    messageLists.add(message);
                } else {
                    Date lastEtdDate = DateTimeUtil.parseDate(lastEtd);
                    if (!ValidatorUtils.requiredValidator(lastEtdDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_EffectiveEndDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        entity.setLastEtd(lastEtdDate);
                    }
                }

                // set workingDays
                startCol++;
                String workingDays = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(workingDays)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(workingDays, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(workingDays)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(workingDays) < IntDef.INT_ZERO
                        || Integer.valueOf(workingDays) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    entity.setWorkingDays(Integer.valueOf(workingDays));
                }

                // set deliveryStartDate
                startCol++;
                String deliveryStartDate = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(deliveryStartDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_DeliverDatetoObuStartDate" });
                    messageLists.add(message);
                } else {
                    Date deliveryStartDateDate = DateTimeUtil.parseDate(deliveryStartDate);
                    if (!ValidatorUtils.requiredValidator(deliveryStartDateDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_DeliverDatetoObuStartDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        entity.setDeliveryStartDate(deliveryStartDateDate);
                    }
                }

                // set expVanningLeadtime
                startCol += IntDef.INT_TWO;
                String expVanningLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(expVanningLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTofObutoVanning" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(expVanningLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTofObutoVanning",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(expVanningLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTofObutoVanning",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(expVanningLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(expVanningLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_LTofObutoVanning",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    entity.setExpVanningLeadtime(Integer.valueOf(expVanningLeadtime));
                }

                // set VanningDate
                startCol++;
                String vanningDate = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(vanningDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_VanningDate" });
                    messageLists.add(message);
                } else {
                    String[] strs = vanningDate.split(StringConst.COMMA);
                    List<Integer> intList = new ArrayList<Integer>();
                    boolean vdNumericFlag = true;
                    boolean vdNumFlag = true;
                    boolean repFlag = true;
                    for (int j = 0; j < strs.length; j++) {
                        String str = strs[j];
                        if (!StringUtil.isNumeric(str)) {
                            vdNumericFlag = false;
                            break;
                        }
                        Integer strInt = Integer.valueOf(str);
                        if (strInt < IntDef.INT_ONE || strInt > IntDef.INT_SEVEN) {
                            vdNumFlag = false;
                            break;
                        }
                        for (Integer inte : intList) {
                            if (inte.equals(strInt)) {
                                repFlag = false;
                                break;
                            }
                        }
                        intList.add(strInt);
                    }
                    if (!vdNumericFlag) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_VanningDate",
                            "CPMSRF11_Grid_Integer" });
                        messageLists.add(message);
                    } else if (!vdNumFlag) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_107);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_VanningDate",
                            StringUtil.toSafeString(IntDef.INT_ONE), StringUtil.toSafeString(IntDef.INT_SEVEN) });
                        messageLists.add(message);
                    } else if (!repFlag) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_155);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_VanningDate",
                            "CPMSRF03_Grid_DeliverDatetoObuStartDate" });
                        messageLists.add(message);
                    } else {
                        entity.setVanningDateList(intList);
                        entity.setVanningDates(vanningDate);
                    }
                }

                // set etdWeek
                startCol++;
                String etdWeek = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(etdWeek)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDWeek" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(etdWeek, IntDef.INT_ONE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDWeek",
                        StringUtil.toSafeString(IntDef.INT_ONE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(etdWeek)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message
                        .setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDWeek", "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(etdWeek) < IntDef.INT_ONE || Integer.valueOf(etdWeek) > IntDef.INT_FOUR) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_107);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDWeek",
                        StringUtil.toSafeString(IntDef.INT_ONE), StringUtil.toSafeString(IntDef.INT_FOUR) });
                    messageLists.add(message);
                } else {
                    entity.setEtdWeek(Integer.valueOf(etdWeek));
                }

                // set etdDate
                startCol++;
                String etdDate = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(etdDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDDate" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(etdDate, IntDef.INT_ONE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDDate",
                        StringUtil.toSafeString(IntDef.INT_ONE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(etdDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message
                        .setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDDate", "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(etdDate) < IntDef.INT_ONE || Integer.valueOf(etdDate) > IntDef.INT_SEVEN) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_107);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETDDate",
                        StringUtil.toSafeString(IntDef.INT_ONE), StringUtil.toSafeString(IntDef.INT_SEVEN) });
                    messageLists.add(message);
                } else {
                    entity.setEtdDate(Integer.valueOf(etdDate));
                }

                // set etaDate
                startCol++;
                String etaDate = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(etaDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETADate" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(etaDate, IntDef.INT_ONE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETADate",
                        StringUtil.toSafeString(IntDef.INT_ONE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(etaDate)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message
                        .setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETADate", "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(etaDate) < IntDef.INT_ONE || Integer.valueOf(etaDate) > IntDef.INT_SEVEN) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_107);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ETADate",
                        StringUtil.toSafeString(IntDef.INT_ONE), StringUtil.toSafeString(IntDef.INT_SEVEN) });
                    messageLists.add(message);
                } else {
                    entity.setEtaDate(Integer.valueOf(etaDate));
                }

                // set deliveryLeadtime
                startCol++;
                String deliveryLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(deliveryLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingLTfromETDtoETA" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(deliveryLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingLTfromETDtoETA",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(deliveryLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingLTfromETDtoETA",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(deliveryLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(deliveryLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_ShippingLTfromETDtoETA",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    entity.setDeliveryLeadtime(Integer.valueOf(deliveryLeadtime));
                }

                // set impCcLeadtime
                startCol++;
                String impCcLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(impCcLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(impCcLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(impCcLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(impCcLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(impCcLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    entity.setImpCcLeadtime(Integer.valueOf(impCcLeadtime));
                }

                // set impInboundLeadtime
                startCol++;
                String impInboundLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(impInboundLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromCusttoInbound" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(impInboundLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromCusttoInbound",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(impInboundLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromCusttoInbound",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(impInboundLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(impInboundLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_WorkingLTfromCusttoInbound",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    entity.setImpInboundLeadtime(Integer.valueOf(impInboundLeadtime));
                }

                // set discontinueIndicator
                startCol++;
                String discontinueIndicator = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(discontinueIndicator)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_DiscontinueIndicator" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(discontinueIndicator, IntDef.INT_ONE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF03_Grid_DiscontinueIndicator",
                        StringUtil.toSafeString(IntDef.INT_ONE) });
                    messageLists.add(message);
                } else {
                    entity.setDiscontinueIndicator(discontinueIndicator);
                }

                // set row number
                entity.setRowNum(startRow);

                // add new data
                if (type.equals(ShipType.NEW)) {
                    cpmsrf13EntityNewList.add(entity);
                }

                // add mod data
                if (type.equals(ShipType.MOD)) {
                    cpmsrf13EntityModList.add(entity);
                    if (TypeYN.Y.equals(discontinueIndicator)) {
                        cpmsrf13EntityModYList.add(entity);
                    }
                }

                // add all data
                cpmsrf13EntityList.add(entity);
            } else {
                if (ValidatorUtils.isExcelEnd(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                    break;
                }
            }
        }

        // check shippingRouteCode all blank
        if (!shippingRouteCodeFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
            messageLists.add(message);
            throw new BusinessException(messageLists);
        }

        // throw exception
        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        // check all detail List<BaseMessage> messageLists
        List<BaseMessage> configMsgList = new ArrayList<BaseMessage>();
        List<CPMSRF13Entity> dataLists = new ArrayList<CPMSRF13Entity>();
        Map<String, Object> maps = new HashMap<String, Object>();
        // MOD change data List
        List<AisinCommonEntity> modChangeDetailList = new ArrayList<AisinCommonEntity>();
        if (cpmsrf13EntityList != null && cpmsrf13EntityList.size() > 0) {
            maps = dealDataDetail(cpmsrf13EntityList, cpmsrf13EntityNewList, cpmsrf13EntityModList,
                cpmsrf13EntityModYList, request);
            List<BaseMessage> msgList = (List<BaseMessage>) maps.get("messageLists");
            if (msgList != null && msgList.size() > 0) {
                messageLists.addAll(msgList);
            }
        }

        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        configMsgList = (List<BaseMessage>) maps.get("configMsgList");
        // shippingRouteCode , firstEtd + lastEtd not continuous
        Map<String, Object> shipRCodeMaps = (Map<String, Object>) maps.get("shipRCodeFLMap");
        if (shipRCodeMaps != null && shipRCodeMaps.size() > 0) {
            Map<String, Object> shipRCodeMapsDB = (Map<String, Object>) maps.get("shipRCodeFLDB");
            for (Map.Entry<String, Object> entry : shipRCodeMaps.entrySet()) {
                List<CPMSRF11DateEntity> dateList = (List<CPMSRF11DateEntity>) entry.getValue();
                List<CPMSRF11DateEntity> dateListDB = (List<CPMSRF11DateEntity>) shipRCodeMapsDB.get(entry.getKey());
                boolean continuousFlag = cpmsrf11Service.checkDateContinuous(dateList, dateListDB);
                if (!continuousFlag) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.C1028);
                    message.setMessageArgs(new String[] { "CPMSRF03_Grid_EffectiveEndDate",
                        "CPMSRF03_Grid_EffectiveStartDate" });
                    configMsgList.add(message);
                    break;
                }
            }
        }

        if (configMsgList != null && configMsgList.size() > 0) {
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            dataLists = (List<CPMSRF13Entity>) maps.get("cpmsrf13EntityList");
            modChangeDetailList = (List<AisinCommonEntity>) maps.get("modChangeDetailList");
            List<AisinCommonEntity> modChangeList = (List<AisinCommonEntity>) maps.get("modChangeMaster");
            context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString(), dataLists);
            context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString() + "__modChangeDetailList",
                modChangeDetailList);
            context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString() + "__modChangeMaster",
                modChangeList);
            throw new BusinessException(configMsgList);
        }

        return maps;
    }

    /**
     * deal data detail
     * 
     * @param cpmsrf13EntityList cpmsrf13EntityList
     * @param newList newList
     * @param modList modList
     * @param modYList modYList
     * @param request request
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> dealDataDetail(List<CPMSRF13Entity> cpmsrf13EntityList, List<CPMSRF13Entity> newList,
        List<CPMSRF13Entity> modList, List<CPMSRF13Entity> modYList, HttpServletRequest request) {

        Map<String, Object> maps = new HashMap<String, Object>();

        // xlsx vanningDate in have 1~5
        Map<String, Object> newVanningDMaps = new HashMap<String, Object>();
        if (newList != null && newList.size() > 0) {
            for (CPMSRF13Entity entity : newList) {
                List<Integer> vanningDateList = entity.getVanningDateList();
                for (Integer vanningDate : vanningDateList) {
                    if (vanningDate > IntDef.INT_FIVE) {
                        continue;
                    }
                    String key = entity.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE
                            + entity.getFirstEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + entity.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + entity.getDeliveryStartDate().getTime();
                    List<Integer> value = (List<Integer>) newVanningDMaps.get(key);
                    if (value == null) {
                        List<Integer> intList = new ArrayList<Integer>();
                        intList.add(vanningDate);
                        newVanningDMaps.put(key, intList);
                    } else {
                        boolean inflag = true;
                        for (Integer integer : value) {
                            if (integer.equals(vanningDate)) {
                                inflag = false;
                                break;
                            }
                        }
                        if (inflag) {
                            value.add(vanningDate);
                            newVanningDMaps.put(key, value);
                        }
                    }
                }
            }
        }

        // get userOffId
        UserInfo userInfo = getLoginUser(request);
        List<Integer> userOffIds = cpmpms01Service.setUserOffIds(userInfo, BusinessPattern.AISIN);
        Integer userId = userInfo.getUserId();
        Integer lang = userInfo.getLanguage().getCode();
        Locale langs = MessageManager.getLanguage(lang).getLocale();

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> configMsgList = new ArrayList<BaseMessage>();
        Map<String, String> configMsgMap = new HashMap<String, String>();

        // DB time now
        long dbTime = cpmsrf11Service.getDBtime();

        // query new discontinueIndicator
        Map<String, Object> newInactiveMapsDB = cpmsrf11Service.getNewInactiveFlag();
        // query mod discontinueIndicator
        Map<String, Object> modInactiveMapsDB = cpmsrf11Service.getModInactiveFlag();

        // query New shippingRouteCode,firstEtd,lastEtd,vanningDate,deliveryStartDate
        List<CPMSRF13Entity> newDataList = cpmsrf13Service.getNewShipRCodeFLV(newList);
        Map<String, String> mewDataMaps = new HashMap<String, String>();
        // not tianjiang shippingRouteType shippingRouteCode
        Map<String, String> newSSMaps = new HashMap<String, String>();

        // query Mod shippingRouteCode,firstEtd,lastEtd,vanningDate,deliveryStartDate
        List<CPMSRF13Entity> moddataList = cpmsrf13Service.getModShipRCodeFLV(modList);
        Map<String, Object> modDataMapsDB = new HashMap<String, Object>();
        Map<String, String> modDataYNMapsDB = new HashMap<String, String>();

        // check mod is or not change
        Map<String, Object> modChangeMapsDB = new HashMap<String, Object>();

        for (CPMSRF13Entity entity : moddataList) {

            long deliveryStartDate = 0;
            if (null != entity.getDeliveryStartDate()) {
                deliveryStartDate = entity.getDeliveryStartDate().getTime();
            }
            String key = entity.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE + entity.getFirstEtd().getTime()
                    + StringConst.DOUBLE_UNDERLINE + entity.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                    + entity.getVanningDate() + StringConst.DOUBLE_UNDERLINE + deliveryStartDate;

            modDataMapsDB.put(key, StringUtil.toSafeString(entity.getSrId()));

            String value = entity.getWorkingDays() + StringConst.DOUBLE_UNDERLINE + entity.getExpVanningLeadtime()
                    + StringConst.DOUBLE_UNDERLINE + entity.getEtdWeek() + StringConst.DOUBLE_UNDERLINE
                    + entity.getEtdDate() + StringConst.DOUBLE_UNDERLINE + entity.getDeliveryLeadtime();

            modChangeMapsDB.put(key, value);

            String discontinueIndicator = entity.getDiscontinueIndicator();
            modDataYNMapsDB.put(key, discontinueIndicator);
        }

        // xlsx shippingRouteCode,firstEtd,lastEtd,vanningDate
        Map<String, String> shipRCodeFLVMap = new HashMap<String, String>();

        // query DB
        // shippingRouteCode,workingDays,expVanningLeadtime,impCcLeadtime,firstEtd,lastEtd,discontinueIndicator,officeId
        List<CPMSRF13Entity> sweiflDataList = cpmsrf13Service.getAllShipRCodeWEI(cpmsrf13EntityList);

        // db shippingRouteCode,firstEtd+lastEtd
        Map<String, Object> shipRCodeFLDB = new HashMap<String, Object>();
        // xlsx shippingRouteCode,firstEtd+lastEtd
        Map<String, Object> shipRCodeFLMap = new HashMap<String, Object>();

        // shippingRouteCode,workingDays
        Map<String, String> shipRCodeWMap = new HashMap<String, String>();
        Map<String, String> shipRCodeWMapDB = new HashMap<String, String>();
        Map<String, Integer> shipRCodeCountMapDB = new HashMap<String, Integer>();
        Map<String, Integer> shipRCodeWCountMap = new HashMap<String, Integer>();

        // shippingRouteCode,expVanningLeadtime
        Map<String, String> shipRCodeEMap = new HashMap<String, String>();
        Map<String, String> shipRCodeEMapDB = new HashMap<String, String>();
        Map<String, Integer> shipRCodeECountMap = new HashMap<String, Integer>();

        // shippingRouteCode,impCcLeadtime
        Map<String, String> shipRCodeICMap = new HashMap<String, String>();
        Map<String, String> shipRCodeICMapDB = new HashMap<String, String>();
        Map<String, Integer> shipRCodeICountMap = new HashMap<String, Integer>();

        // shippingRouteCode,impInboundLeadtime
        Map<String, String> shipRCodeIBMap = new HashMap<String, String>();
        Map<String, String> shipRCodeIBMapDB = new HashMap<String, String>();
        Map<String, Integer> shipRCodeIBCountMap = new HashMap<String, Integer>();

        // db shippingRouteCode not have officeId
        Map<String, String> shipRCodeOMapDBN = new HashMap<String, String>();
        // db shippingRouteCode have officeId
        Map<String, Integer> shipRCodeOMapDBY = new HashMap<String, Integer>();

        // db shippingRouteCode have exp region
        Map<String, String> expRegionMap = new HashMap<String, String>();

        if (newDataList != null && newDataList.size() > 0) {
            for (CPMSRF13Entity ce : newDataList) {
                String shippingRouteCode = ce.getShippingRouteCode();
                Date firstEtd = ce.getFirstEtd();
                Date lastEtd = ce.getLastEtd();
                Integer vanningDate = ce.getVanningDate();
                long deliveryStartDate = 0;
                if (null != ce.getDeliveryStartDate()) {
                    deliveryStartDate = ce.getDeliveryStartDate().getTime();
                }

                // set New shippingRouteCode,firstEtd,lastEtd,vanningDate,deliveryStartDate maps
                String key = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + firstEtd.getTime()
                        + StringConst.DOUBLE_UNDERLINE + lastEtd.getTime() + StringConst.DOUBLE_UNDERLINE + vanningDate
                        + StringConst.DOUBLE_UNDERLINE + deliveryStartDate;
                mewDataMaps.put(key, shippingRouteCode);

                String shippingRouteType = ce.getShippingRouteType();
                if (!StringUtil.isNullOrEmpty(shippingRouteType)
                        && !StringUtil.toSafeString(ShippingRouteType.AISIN_TTSH).equals(shippingRouteType)) {
                    newSSMaps.put(shippingRouteCode, shippingRouteCode);
                }
            }
        }

        if (sweiflDataList != null && sweiflDataList.size() > 0) {
            for (CPMSRF13Entity ce : sweiflDataList) {
                String shippingRouteCode = ce.getShippingRouteCode();
                if (ce.getDiscontinueIndicator().equals(StringUtil.toSafeString(InactiveFlag.ACTIVE))) {
                    String workingDays = StringUtil.toSafeString(ce.getWorkingDays());
                    String expVanningLeadtime = StringUtil.toSafeString(ce.getExpVanningLeadtime());
                    String impCcLeadtime = StringUtil.toSafeString(ce.getImpCcLeadtime());
                    String impInboundLeadtime = StringUtil.toSafeString(ce.getImpInboundLeadtime());

                    shipRCodeWMapDB.put(shippingRouteCode, workingDays);
                    shipRCodeEMapDB.put(shippingRouteCode, expVanningLeadtime);
                    shipRCodeICMapDB.put(shippingRouteCode, impCcLeadtime);
                    shipRCodeIBMapDB.put(shippingRouteCode, impInboundLeadtime);

                    Date firstEtd = ce.getFirstEtd();
                    Date lastEtd = ce.getLastEtd();
                    List<CPMSRF11DateEntity> lists = (List<CPMSRF11DateEntity>) shipRCodeFLDB.get(shippingRouteCode);
                    CPMSRF11DateEntity cf = new CPMSRF11DateEntity();
                    cf.setStart(firstEtd);
                    cf.setEnd(lastEtd);
                    if (lists != null && lists.size() > 0) {
                        lists.add(cf);
                    } else {
                        lists = new ArrayList<CPMSRF11DateEntity>();
                        lists.add(cf);
                    }
                    shipRCodeFLDB.put(shippingRouteCode, lists);

                    Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                    if (count == null) {
                        shipRCodeCountMapDB.put(shippingRouteCode, IntDef.INT_ONE);
                    } else {
                        count = count + IntDef.INT_ONE;
                        shipRCodeCountMapDB.put(shippingRouteCode, count);
                    }
                }

                Integer officeId = ce.getOfficeId();
                if (officeId != null) {
                    boolean flag = cpmsrf11Service.checkOfficeId(userOffIds, officeId);
                    if (!flag) {
                        shipRCodeOMapDBN.put(shippingRouteCode, shippingRouteCode);
                    } else {
                        shipRCodeOMapDBY.put(shippingRouteCode, officeId);
                    }
                }

                String expRegion = ce.getExpRegion();
                if (!StringUtil.isEmpty(expRegion)) {
                    expRegionMap.put(shippingRouteCode, expRegion);
                }
            }
        }

        // mod is change Y,N to sum shipRCodeCountMapDB
        if (modList != null && modList.size() > 0) {
            for (CPMSRF13Entity entity : modList) {
                List<Integer> vanningDateList = entity.getVanningDateList();
                for (Integer vanningDate : vanningDateList) {
                    String shippingRouteCode = entity.getShippingRouteCode();
                    String key = entity.getShippingRouteCode() + StringConst.DOUBLE_UNDERLINE
                            + entity.getFirstEtd().getTime() + StringConst.DOUBLE_UNDERLINE
                            + entity.getLastEtd().getTime() + StringConst.DOUBLE_UNDERLINE + vanningDate
                            + StringConst.DOUBLE_UNDERLINE + entity.getDeliveryStartDate().getTime();
                    String value = modDataYNMapsDB.get(key);
                    if (value != null) {
                        String discontinueIndicator = entity.getDiscontinueIndicator();
                        if (TypeYN.N.equals(discontinueIndicator) && (InactiveFlag.INACTIVE + "").equals(value)) {
                            Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                            if (count == null) {
                                shipRCodeCountMapDB.put(shippingRouteCode, 1);
                            } else {
                                count = count + IntDef.INT_ONE;
                                shipRCodeCountMapDB.put(shippingRouteCode, count);
                            }
                        } else if (TypeYN.Y.equals(discontinueIndicator) && (InactiveFlag.ACTIVE + "").equals(value)) {
                            Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                            count = count - IntDef.INT_ONE;
                            shipRCodeCountMapDB.put(shippingRouteCode, count);
                        }
                    }
                    if (TypeYN.N.equals(entity.getDiscontinueIndicator())) {
                        Integer workingDays = entity.getWorkingDays();
                        Integer expVanningLeadtime = entity.getExpVanningLeadtime();
                        Integer impCcLeadtime = entity.getImpCcLeadtime();
                        Integer impInboundLeadtime = entity.getImpInboundLeadtime();

                        String keyw = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + workingDays;
                        String keye = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + expVanningLeadtime;
                        String keyi = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + impCcLeadtime;
                        String keyib = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + impInboundLeadtime;

                        Integer countw = shipRCodeWCountMap.get(keyw);
                        if (countw == null) {
                            shipRCodeWCountMap.put(keyw, IntDef.INT_ONE);
                        } else {
                            countw = countw + IntDef.INT_ONE;
                            shipRCodeWCountMap.put(keyw, countw);
                        }

                        Integer counte = shipRCodeECountMap.get(keye);
                        if (counte == null) {
                            shipRCodeECountMap.put(keye, IntDef.INT_ONE);
                        } else {
                            counte = counte + IntDef.INT_ONE;
                            shipRCodeECountMap.put(keye, counte);
                        }

                        Integer counti = shipRCodeICountMap.get(keyi);
                        if (counti == null) {
                            shipRCodeICountMap.put(keyi, IntDef.INT_ONE);
                        } else {
                            counti = counti + IntDef.INT_ONE;
                            shipRCodeICountMap.put(keyi, counti);
                        }

                        Integer countib = shipRCodeIBCountMap.get(keyib);
                        if (countib == null) {
                            shipRCodeIBCountMap.put(keyib, IntDef.INT_ONE);
                        } else {
                            countib = countib + IntDef.INT_ONE;
                            shipRCodeIBCountMap.put(keyib, counti);
                        }
                    }
                }
            }
        }

        // mod data type is Y
        if (modYList != null && modYList.size() > 0) {
            for (CPMSRF13Entity ce : modYList) {
                String shippingRouteCode = ce.getShippingRouteCode();
                List<CPMSRF11DateEntity> listsDB = (List<CPMSRF11DateEntity>) shipRCodeFLDB.get(shippingRouteCode);
                if (listsDB != null && listsDB.size() > 0) {
                    for (int i = 0; i < listsDB.size(); i++) {
                        CPMSRF11DateEntity cd = listsDB.get(i);
                        if (cd.getStart().equals(ce.getFirstEtd()) && cd.getEnd().equals(ce.getLastEtd())) {
                            listsDB.remove(i);
                            break;
                        }
                    }
                    if (listsDB != null && listsDB.size() > 0) {
                        shipRCodeFLDB.put(shippingRouteCode, listsDB);
                    }
                    if (listsDB.isEmpty()) {

                        shipRCodeWMapDB.remove(shippingRouteCode);
                        shipRCodeEMapDB.remove(shippingRouteCode);
                        shipRCodeICMapDB.remove(shippingRouteCode);
                        shipRCodeIBMapDB.remove(shippingRouteCode);
                    }
                }
            }
        }

        // MOD change data List
        List<AisinCommonEntity> modChangeList = new ArrayList<AisinCommonEntity>();
        // max change lastEtd
        long maxLastEtd = 0;
        long minFirstEtd = 0;
        int modListFalg = 0;
        // officeId not exist
        Map<String, String> officeIdMaps = new HashMap<String, String>();
        // shippingRouteCode repeat workingDays + impCcLeadtime+ expVanningLeadtime+impInboundLeadtime
        Map<String, String> sameWIEMaps = new HashMap<String, String>();
        // shippingRouteCode db repeat workingDays + impCcLeadtime+ expVanningLeadtime
        Map<String, String> sameWIEMapsDB = new HashMap<String, String>();
        // for one shippingRouteCode check xlsx firstEtd + lastEtd in one time period msg
        Map<String, String> sameSFLMaps = new HashMap<String, String>();
        boolean comfirmLD = true;

        // for check c1016
        Map<String, Date> maxLastEtdMap = new HashMap<String, Date>();
        // check all data
        for (int i = 0; i < cpmsrf13EntityList.size(); i++) {

            CPMSRF13Entity ce = cpmsrf13EntityList.get(i);
            String shippingRouteCode = ce.getShippingRouteCode();

            // check shippingRouteCode,officeId
            String officeId = (String) shipRCodeOMapDBN.get(shippingRouteCode);
            if (!StringUtil.isNullOrEmpty(officeId)) {
                officeIdMaps.put(shippingRouteCode, shippingRouteCode);
                continue;
            } else {
                Integer officeIdY = shipRCodeOMapDBY.get(shippingRouteCode);
                if (officeIdY != null) {
                    ce.setOfficeId(officeIdY);
                }
            }
            String expRegion = expRegionMap.get(shippingRouteCode);
            if (!StringUtil.isEmpty(expRegion)) {
                ce.setExpRegion(expRegion);
            }

            String rowNum = StringUtil.toSafeString(ce.getRowNum());
            long firstEtd = ce.getFirstEtd().getTime();
            long lastEtd = ce.getLastEtd().getTime();
            if (i == 0) {
                minFirstEtd = firstEtd;
            }
            String discontinueIndicator = ce.getDiscontinueIndicator();
            long deliveryStartDate = ce.getDeliveryStartDate().getTime();

            List<Integer> vanningDateList = ce.getVanningDateList();
            String[] flvkeys = new String[vanningDateList.size()];
            for (int j = 0; j < vanningDateList.size(); j++) {
                Integer vanningDate = vanningDateList.get(j);
                flvkeys[j] = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + firstEtd + StringConst.DOUBLE_UNDERLINE
                        + lastEtd + StringConst.DOUBLE_UNDERLINE + vanningDate + StringConst.DOUBLE_UNDERLINE
                        + deliveryStartDate;
            }
            boolean modflag = true;
            boolean lfflag = true;
            boolean repFlag = true;

            // check xlsx repeat shippingRouteCode,firstEtd,lastEtd,vanningDate,deliveryStartDate
            for (String flvkey : flvkeys) {
                String sflvVlaue = shipRCodeFLVMap.get(flvkey);
                if (!StringUtil.isNullOrEmpty(sflvVlaue)) {
                    repFlag = false;
                    sameSFLMaps.put(rowNum, MessageCodeConst.W1004_078);
                } else {
                    shipRCodeFLVMap.put(flvkey, shippingRouteCode);
                }
            }
            // check lastEtd and firstEtd
            if (lastEtd < firstEtd) {
                repFlag = false;
                lfflag = false;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_EffectiveEndDate",
                    "CPMSRF03_Grid_EffectiveStartDate" });
                messageLists.add(message);
            }

            // check mod
            String type = ce.getType();
            if (ShipType.MOD.equals(type) && repFlag) {
                // check DB shippingRouteCode+firstEtd+lastEtd+vanningDate
                boolean oldFLValueF = true;
                for (String flvkey : flvkeys) {
                    String oldFLValue = (String) modDataMapsDB.get(flvkey);
                    if (oldFLValue == null) {
                        oldFLValueF = false;
                        break;
                    }
                }
                if (!oldFLValueF) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                    message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_ShippingRouteCd",
                        "CPMSRF11_Grid_ShippingRouteMaster" });
                    messageLists.add(message);
                    continue;
                }
            }

            // check new
            if (ShipType.NEW.equals(type)) {

                if (repFlag) {
                    // check DB shippingRouteCode+firstEtd+lastEtd+vanningDate+deliveryStartDate
                    boolean oldFLValueF = true;
                    for (String flvkey : flvkeys) {
                        String oldFLValue = mewDataMaps.get(flvkey);
                        if (oldFLValue != null) {
                            oldFLValueF = false;
                            break;
                        }
                    }
                    if (!oldFLValueF) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_074);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_ShippingRouteCd",
                            "CPMSRF11_Grid_ShippingRouteMaster" });
                        messageLists.add(message);
                        continue;
                    }

                    // check DB shippingRouteType
                    String shippingRouteType = newSSMaps.get(shippingRouteCode);
                    if (!StringUtil.isNullOrEmpty(shippingRouteType)) {
                        officeIdMaps.put(shippingRouteCode, shippingRouteCode);
                        continue;
                    }
                }

                // check confirm lastEtd and dbTime
                if (lastEtd < dbTime) {
                    comfirmLD = false;
                }

                // get max lastEtd
                Date maxNewLastEtd = (Date) maxLastEtdMap.get(shippingRouteCode);
                if (null == maxNewLastEtd) {
                    maxLastEtdMap.put(shippingRouteCode, ce.getLastEtd());
                } else {
                    if (maxNewLastEtd.before(ce.getLastEtd())) {
                        maxLastEtdMap.put(shippingRouteCode, ce.getLastEtd());
                    }
                }

                // check xlsx vanningDate not all have 1~5
                String sflkey = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + firstEtd
                        + StringConst.DOUBLE_UNDERLINE + lastEtd + StringConst.DOUBLE_UNDERLINE + deliveryStartDate;
                List<Integer> inteList = (List<Integer>) newVanningDMaps.get(sflkey);
                if (inteList == null) {
                    configMsgMap.put(MessageCodeConst.C1020, shippingRouteCode);
                } else if (inteList.size() < IntDef.INT_FIVE) {
                    configMsgMap.put(MessageCodeConst.C1020, shippingRouteCode);
                }

                // check discontinueIndicator
                if (!StringUtil.isNullOrEmpty(discontinueIndicator)) {
                    String inactiveDB = (String) newInactiveMapsDB.get(discontinueIndicator);
                    if (StringUtil.isNullOrEmpty(inactiveDB)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_DiscontinueIndicator", TypeYN.N });
                        messageLists.add(message);
                    } else {
                        ce.setDiscontinueIndicator(inactiveDB);
                    }
                }
            }

            // for one shippingRouteCode check firstEtd + lastEtd in one time period
            if (repFlag && (InactiveFlag.ACTIVE + "").equals(ce.getDiscontinueIndicator())) {
                // List<CPMSRF11DateEntity> modDataYList = (List<CPMSRF11DateEntity>)
                // modDataYmaps.get(shippingRouteCode);
                // check xlsx
                boolean repPeFlag = true;
                List<CPMSRF11DateEntity> lists = (List<CPMSRF11DateEntity>) shipRCodeFLMap.get(shippingRouteCode);
                CPMSRF11DateEntity cf = new CPMSRF11DateEntity();
                cf.setStart(ce.getFirstEtd());
                cf.setEnd(ce.getLastEtd());
                if (lists != null && lists.size() > 0) {
                    boolean flag = cpmsrf11Service.intervals(lists, firstEtd, lastEtd);
                    if (!flag) {
                        modflag = false;
                        repPeFlag = false;
                        sameSFLMaps.put(rowNum, MessageCodeConst.W1004_078);
                    } else {
                        lists.add(cf);
                        shipRCodeFLMap.put(shippingRouteCode, lists);
                    }
                } else {
                    lists = new ArrayList<CPMSRF11DateEntity>();
                    lists.add(cf);
                    shipRCodeFLMap.put(shippingRouteCode, lists);
                }
                if (repPeFlag) {
                    // check DB
                    List<CPMSRF11DateEntity> listsDb = (List<CPMSRF11DateEntity>) shipRCodeFLDB.get(shippingRouteCode);
                    boolean periodFlagDB = cpmsrf11Service.intervals(listsDb, firstEtd, lastEtd);
                    if (!periodFlagDB) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_078);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF11_Grid_ShippingRouteMaster" });
                        messageLists.add(message);
                    }
                }
            }
            if (lfflag) {
                // check 4-5-3 calclulated deliveryStartDate,firstEtd,lastEtd
                Date vanningDateC = aisinCommonService.getAfterDate(ce.getDeliveryStartDate(),
                    ce.getExpVanningLeadtime());
                boolean calcluDFLFlag = true;
                for (Integer vanningDate : vanningDateList) {
                    int period = (ce.getEtdWeek() - IntDef.INT_ONE) * IntDef.INT_SEVEN + vanningDate;
                    Date deliveryStartDateC = aisinCommonService.getAfterDate(vanningDateC, period);
                    long deliveryStartDateCL = deliveryStartDateC.getTime();
                    if ((deliveryStartDateCL < firstEtd) || (deliveryStartDateCL > lastEtd)) {
                        calcluDFLFlag = false;
                    }
                }
                if (!calcluDFLFlag) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_107);
                    message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_DeliverDatetoObuStartDate",
                        "CPMSRF03_Grid_EffectiveStartDate", "CPMSRF03_Grid_EffectiveEndDate" });
                    messageLists.add(message);
                }
            }

            // check repeat shippingRouteCode + workingDays
            // check xlsx
            String workingDays = StringUtil.toSafeString(ce.getWorkingDays());
            String swValue = shipRCodeWMap.get(shippingRouteCode);
            if (StringUtil.isNullOrEmpty(swValue) || workingDays.equals(swValue)) {
                if (TypeYN.N.equals(discontinueIndicator)) {
                    shipRCodeWMap.put(shippingRouteCode, workingDays);
                }
                // check db
                String swValueDB = shipRCodeWMapDB.get(shippingRouteCode);
                if (!StringUtil.isNullOrEmpty(swValueDB) && !workingDays.equals(swValueDB)) {
                    Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                    Integer countw = shipRCodeWCountMap.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + workingDays);
                    if (!count.equals(countw)) {
                        modflag = false;
                        sameWIEMapsDB.put("CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu" + StringConst.DOUBLE_UNDERLINE
                                + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + swValueDB, shippingRouteCode);
                    }
                }
            } else if (!swValue.equals(workingDays)) {
                modflag = false;
                sameWIEMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                        + "CPMSRF03_Grid_LTfromKbIPDatetoDDtoObu", "CPMSRF03_Grid_ShippingRouteCd");
            }

            // check repeat shippingRouteCode + expVanningLeadtime
            // check xlsx
            String expVanningLeadtime = StringUtil.toSafeString(ce.getExpVanningLeadtime());
            String seValue = shipRCodeEMap.get(shippingRouteCode);
            if (StringUtil.isNullOrEmpty(seValue) || expVanningLeadtime.equals(seValue)) {
                if (TypeYN.N.equals(discontinueIndicator)) {
                    shipRCodeEMap.put(shippingRouteCode, expVanningLeadtime);
                }
                // check db
                String seValueDB = shipRCodeEMapDB.get(shippingRouteCode);
                if (!StringUtil.isNullOrEmpty(seValueDB) && !expVanningLeadtime.equals(seValueDB)) {
                    Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                    Integer counte = shipRCodeECountMap.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + expVanningLeadtime);
                    if (!count.equals(counte)) {
                        modflag = false;
                        sameWIEMapsDB.put("CPMSRF03_Grid_LTofObutoVanning" + StringConst.DOUBLE_UNDERLINE
                                + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + seValueDB, shippingRouteCode);
                    }
                }
            } else if (!seValue.equals(expVanningLeadtime)) {
                modflag = false;
                sameWIEMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE + "CPMSRF03_Grid_LTofObutoVanning",
                    "CPMSRF03_Grid_ShippingRouteCd");
            }

            // check repeat shippingRouteCode + impCcLeadtime
            // check xlsx
            String impCcLeadtime = StringUtil.toSafeString(ce.getImpCcLeadtime());
            String sicValue = shipRCodeICMap.get(shippingRouteCode);
            if (StringUtil.isNullOrEmpty(sicValue) || impCcLeadtime.equals(sicValue)) {
                if (TypeYN.N.equals(discontinueIndicator)) {
                    shipRCodeICMap.put(shippingRouteCode, impCcLeadtime);
                }
                // check db
                String siValueDB = shipRCodeICMapDB.get(shippingRouteCode);
                if (!StringUtil.isNullOrEmpty(siValueDB) && !impCcLeadtime.equals(siValueDB)) {
                    Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                    Integer counti = shipRCodeICountMap.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + impCcLeadtime);
                    if (!count.equals(counti)) {
                        modflag = false;
                        sameWIEMapsDB.put("CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc" + StringConst.DOUBLE_UNDERLINE
                                + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + siValueDB, shippingRouteCode);
                    }
                }
            } else if (!sicValue.equals(impCcLeadtime)) {
                modflag = false;
                sameWIEMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                        + "CPMSRF03_Grid_WorkingLTfromETAtoImpCustCc", "CPMSRF03_Grid_ShippingRouteCd");
            }

            // check repeat shippingRouteCode + impInboundLeadtime
            // check xlsx
            String impInboundLeadtime = StringUtil.toSafeString(ce.getImpInboundLeadtime());
            String sibValue = shipRCodeIBMap.get(shippingRouteCode);
            if (StringUtil.isNullOrEmpty(sibValue) || impInboundLeadtime.equals(sibValue)) {
                if (TypeYN.N.equals(discontinueIndicator)) {
                    shipRCodeIBMap.put(shippingRouteCode, impInboundLeadtime);
                }
                // check db
                String sibValueDB = shipRCodeIBMapDB.get(shippingRouteCode);
                if (!StringUtil.isNullOrEmpty(sibValueDB) && !impInboundLeadtime.equals(sibValueDB)) {
                    Integer count = shipRCodeCountMapDB.get(shippingRouteCode);
                    Integer countib = shipRCodeIBCountMap.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + impInboundLeadtime);
                    if (!count.equals(countib)) {
                        modflag = false;
                        sameWIEMapsDB.put("CPMSRF03_Grid_WorkingLTfromCusttoInbound" + StringConst.DOUBLE_UNDERLINE
                                + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + sibValueDB, shippingRouteCode);
                    }
                }
            } else if (!sibValue.equals(impInboundLeadtime)) {
                modflag = false;
                sameWIEMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                        + "CPMSRF03_Grid_WorkingLTfromCusttoInbound", "CPMSRF03_Grid_ShippingRouteCd");
            }

            // if etdWeek is 1
            Integer etdWeek = ce.getEtdWeek();
            Integer etaDate = ce.getEtaDate();
            Integer etdDate = ce.getEtdDate();

            if (etdWeek == 1) {
                Integer maxVanningDate = 0;
                for (Integer vanningDate : vanningDateList) {
                    if (vanningDate > maxVanningDate) {
                        maxVanningDate = vanningDate;
                    }
                }

                // check etdDate + max VanningDate
                if (etdDate < maxVanningDate) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message
                        .setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_ETDDate", "CPMSRF03_Grid_VanningDate" });
                    messageLists.add(message);
                }
            }

            // check etaDate , deliveryLeadtime + etdDate
            Integer deliveryLeadtime = ce.getDeliveryLeadtime();
            Integer etaDateN = (etdDate + deliveryLeadtime) % IntDef.INT_SEVEN;
            if (0 == etaDateN) {
                etaDateN = IntDef.INT_SEVEN;
            }
            if (!etaDate.equals(etaDateN)) {
                String esin = MessageManager.getMessage("CPMSRF03_Grid_ETDDate", langs) + StringConst.PLUS
                        + MessageManager.getMessage("CPMSRF03_Grid_ShippingLTfromETDtoETA", langs);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_069);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_ETADate", esin });
                messageLists.add(message);
            }

            // check mod
            if (ShipType.MOD.equals(type)) {
                // check discontinueIndicator
                if (!StringUtil.isNullOrEmpty(discontinueIndicator)) {
                    String inactiveDB = (String) modInactiveMapsDB.get(discontinueIndicator);
                    if (StringUtil.isNullOrEmpty(inactiveDB)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                        message
                            .setMessageArgs(new String[] { rowNum, "CPMSRF03_Grid_DiscontinueIndicator", TypeYN.Y_N });
                        messageLists.add(message);
                    } else {
                        ce.setDiscontinueIndicator(inactiveDB);
                    }
                }
            }

            if (modflag) {
                // get all change mod data
                if ((modListFalg < messageLists.size())) {
                    modListFalg = messageLists.size();
                } else {
                    // get new data
                    if (ShipType.NEW.equals(type)) {
                        for (String flvkey : flvkeys) {
                            AisinCommonEntity ace = new AisinCommonEntity();
                            ace.setDeliveryStartDate(ce.getDeliveryStartDate());
                            ace.setShippingRouteCode(shippingRouteCode);
                            ace.setFirstEtd(ce.getFirstEtd());
                            ace.setLastEtd(ce.getLastEtd());
                            String[] vds = flvkey.split(StringConst.DOUBLE_UNDERLINE);
                            ace.setVanningDay(Integer.valueOf(vds[IntDef.INT_THREE]));
                            ace.setWorkingDays(ce.getWorkingDays());
                            ace.setExpVanningLeadtime(ce.getExpVanningLeadtime());
                            ace.setEtdWeek(ce.getEtdWeek());
                            ace.setEtdDate(ce.getEtdDate());
                            ace.setDeliveryLeadtime(ce.getDeliveryLeadtime());
                            ace.setInactiveFlag(ce.getDiscontinueIndicator());
                            ace.setCreatedBy(userId);
                            ace.setUpdatedBy(userId);
                            ace.setOfficeId(ce.getOfficeId());
                            modChangeList.add(ace);
                            if (maxLastEtd < lastEtd) {
                                maxLastEtd = lastEtd;
                            }
                        }
                    } else {
                        String changeValue = ce.getWorkingDays() + StringConst.DOUBLE_UNDERLINE
                                + ce.getExpVanningLeadtime() + StringConst.DOUBLE_UNDERLINE + ce.getEtdWeek()
                                + StringConst.DOUBLE_UNDERLINE + ce.getEtdDate() + StringConst.DOUBLE_UNDERLINE
                                + ce.getDeliveryLeadtime();
                        for (String flvkey : flvkeys) {
                            String changeValueDB = (String) modChangeMapsDB.get(flvkey);
                            if (!changeValue.equals(changeValueDB) || ("0".equals(ce.getDiscontinueIndicator()))) {
                                String srId = (String) modDataMapsDB.get(flvkey);
                                AisinCommonEntity ace = new AisinCommonEntity();
                                ace.setSrId(Integer.valueOf(srId));
                                ace.setDeliveryStartDate(ce.getDeliveryStartDate());
                                ace.setShippingRouteCode(shippingRouteCode);
                                ace.setFirstEtd(ce.getFirstEtd());
                                ace.setLastEtd(ce.getLastEtd());
                                String[] vds = flvkey.split(StringConst.DOUBLE_UNDERLINE);
                                ace.setVanningDay(Integer.valueOf(vds[IntDef.INT_THREE]));
                                ace.setWorkingDays(ce.getWorkingDays());
                                ace.setExpVanningLeadtime(ce.getExpVanningLeadtime());
                                ace.setEtdWeek(ce.getEtdWeek());
                                ace.setEtdDate(ce.getEtdDate());
                                ace.setDeliveryLeadtime(ce.getDeliveryLeadtime());
                                ace.setInactiveFlag(ce.getDiscontinueIndicator());
                                ace.setCreatedBy(userId);
                                ace.setUpdatedBy(userId);
                                ace.setOfficeId(ce.getOfficeId());
                                modChangeList.add(ace);
                                if (maxLastEtd < lastEtd) {
                                    maxLastEtd = lastEtd;
                                }
                                if (minFirstEtd > firstEtd) {
                                    minFirstEtd = firstEtd;
                                }
                            }
                        }
                    }
                }
            }

            // set creatby
            ce.setCreatedBy(userId);
            // set updatedBy
            ce.setUpdatedBy(userId);
        }

        if (officeIdMaps != null && officeIdMaps.size() > 0) {
            for (Map.Entry<String, String> entry : officeIdMaps.entrySet()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_140);
                message.setMessageArgs(new String[] { entry.getValue(), "CPMSRF11_Grid_ShippingRouteData" });
                messageLists.add(message);
            }
        }
        if (sameWIEMaps != null && sameWIEMaps.size() > 0) {
            for (Map.Entry<String, String> entry : sameWIEMaps.entrySet()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_079);
                String enkey = entry.getKey();
                String[] keyList = enkey.split(StringConst.DOUBLE_UNDERLINE);
                message.setMessageArgs(new String[] { keyList[1], entry.getValue(), keyList[0] });
                messageLists.add(message);
            }
        }
        if (sameWIEMapsDB != null && sameWIEMapsDB.size() > 0) {
            for (Map.Entry<String, String> entry : sameWIEMapsDB.entrySet()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_080);
                String enkey = entry.getKey();
                String[] keyList = enkey.split(StringConst.DOUBLE_UNDERLINE);
                message.setMessageArgs(new String[] { keyList[0], keyList[1], keyList[IntDef.INT_TWO] });
                messageLists.add(message);
            }
        }
        if (sameSFLMaps != null && sameSFLMaps.size() > 0) {
            for (Map.Entry<String, String> entry : sameSFLMaps.entrySet()) {
                BaseMessage message = new BaseMessage(entry.getValue());
                message.setMessageArgs(new String[] { entry.getKey(), "CPMSRF11_Grid_UploadFile" });
                messageLists.add(message);
            }
        }
        if (!comfirmLD) {
            BaseMessage message = new BaseMessage(MessageCodeConst.C1015);
            configMsgList.add(message);
        }
        if (maxLastEtdMap.size() > 0) {
            if (!checkShortDate(mewDataMaps, maxLastEtdMap, dbTime)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.C1016);
                configMsgList.add(message);
            }
        }
        if (configMsgMap != null && configMsgMap.size() > 0) {
            for (Map.Entry<String, String> entry : configMsgMap.entrySet()) {
                BaseMessage message = new BaseMessage(entry.getKey());
                message.setMessageArgs(new String[] { entry.getValue() });
                configMsgList.add(message);
            }
        }

        // common deal
        Map<String, Object> modChangeMaps = new HashMap<String, Object>();
        // MOD change data detail List
        List<AisinCommonEntity> modChangeDetailList = new ArrayList<AisinCommonEntity>();
        if (modChangeList != null && modChangeList.size() > 0) {
            modChangeMaps = aisinCommonController.dealUpdateData(modChangeList, minFirstEtd, maxLastEtd,
                ShippingRouteType.AISIN_TTSH);
            List<BaseMessage> msgList = (List<BaseMessage>) modChangeMaps.get("modChangeMessageList");
            if (msgList != null && msgList.size() > 0) {
                messageLists.addAll(msgList);
            }
            modChangeDetailList = (List<AisinCommonEntity>) modChangeMaps.get("modChangeDetailList");
        }

        maps.put("messageLists", messageLists);
        maps.put("configMsgList", configMsgList);
        maps.put("cpmsrf13EntityList", cpmsrf13EntityList);
        maps.put("modChangeDetailList", modChangeDetailList);
        maps.put("shipRCodeFLMap", shipRCodeFLMap);
        maps.put("shipRCodeFLDB", shipRCodeFLDB);
        maps.put("modChangeMaster", modChangeList);

        return maps;
    }

    /**
     * check dbTime + lastEtd
     * 
     * @param mewDataMaps mewDataMaps
     * @param maxLastEtdMap maxLastEtdMap
     * @param dbTime dbTime
     * @return flag flag
     */
    private boolean checkShortDate(Map<String, String> mewDataMaps, Map<String, Date> maxLastEtdMap, long dbTime) {

        boolean flag = true;
        for (Map.Entry<String, String> entry : mewDataMaps.entrySet()) {
            String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
            Date value = maxLastEtdMap.get(keys[0]);
            if (null != value) {
                long endDate = Long.parseLong(keys[IntDef.INT_TWO]);
                if (endDate > value.getTime()) {
                    maxLastEtdMap.put(keys[0], new Date(endDate));
                }
            }
        }

        Date startAft = DateTimeUtil.addMonth(new Date(dbTime), IntDef.INT_SIX);
        for (Map.Entry<String, Date> entry : maxLastEtdMap.entrySet()) {
            Date value = (Date) entry.getValue();
            if (startAft.after(value)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

}
