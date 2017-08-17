/**
 * Controller of V-V Business Shipping Route Master Upload
 * 
 * @screen CPMSRF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.chinaplus.web.mm.entity.CPMSRF11DateEntity;
import com.chinaplus.web.mm.entity.CPMSRF11Entity;
import com.chinaplus.web.mm.service.CPMPMS01Service;
import com.chinaplus.web.mm.service.CPMSRF11Service;

/**
 * Controller of V-V Business Shipping Route Master Upload
 */
@Controller
public class CPMSRF11Controller extends BaseFileController {

    /** session key */
    private static final String FILE_UPLOAD_SESSION_KEY = "sessionKey";
    /** V-V_Shipping_Route */
    private static final String VV_SHIPPING_ROUTE = "V-V_Shipping_Route";
    /** Discontinue Indicator */
    private static final String DISCONTINUE_INDICATOR = "Discontinue Indicator";
    /** Fixed: Y,N */
    private static final String FIXED_Y_N = "Fixed: Y,N (1)";

    /**
     * cpmsrf11Service.
     */
    @Autowired
    private CPMSRF11Service cpmsrf11Service;

    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMPMS01Service cpmpms01Service;

    @Override
    protected String getFileId() {
        return FileId.CPMSRF11;
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

        List<CPMSRF11Entity> dataList = new ArrayList<CPMSRF11Entity>();
        List<String> shipRCodelist = new ArrayList<String>();

        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get work sheet sheet
            Sheet sheet = workbook.getSheet(VV_SHIPPING_ROUTE);
            boolean flag = checkStatic(sheet);

            Map<String, Object> maps = readDataXlsx(sheet, request, param, flag);

            dataList = (List<CPMSRF11Entity>) maps.get("cpmsrf11EntityList");
            shipRCodelist = (List<String>) maps.get("shipRCodelist");

        }

        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {

            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {

                String mapKey = param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString();
                dataList = (List<CPMSRF11Entity>) context.get(mapKey);
                shipRCodelist = (List<String>) context.get(mapKey + "shipRCodelist");
                context.remove(mapKey);
            }

            // upload
            cpmsrf11Service.doNewAndModData(dataList, shipRCodelist);

        }

        return new ArrayList<BaseMessage>();
    }

    /**
     * read Data Xlsx
     * 
     * @param sheet sheet
     * @param request request
     * @param param param
     * @param inactiveFlag inactiveFlag
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> readDataXlsx(Sheet sheet, HttpServletRequest request, BaseParam param,
        boolean inactiveFlag) {

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        List<CPMSRF11Entity> cpmsrf11EntityList = new ArrayList<CPMSRF11Entity>();
        List<CPMSRF11Entity> cpmsrf11EntityNewList = new ArrayList<CPMSRF11Entity>();
        List<CPMSRF11Entity> cpmsrf11EntityModList = new ArrayList<CPMSRF11Entity>();
        List<CPMSRF11Entity> cpmsrf11EntityModYList = new ArrayList<CPMSRF11Entity>();

        boolean shippingRouteCodeFlag = false;

        // read excel
        int sheetMaxRow = sheet.getLastRowNum() + 1;
        int READ_START_COL = IntDef.INT_ONE;
        int READ_TOTAL_COL = IntDef.INT_TWENTY_SEVEN;

        for (int startRow = IntDef.INT_THIRTEEN; startRow <= sheetMaxRow; startRow++) {
            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                int startCol = IntDef.INT_ONE;
                String type = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol));

                if (!ValidatorUtils.requiredValidator(type)) {
                    continue;
                }
                shippingRouteCodeFlag = true;
                String startRowNum = StringUtil.toSafeString(startRow);
                if (!type.equals(ShipType.NEW) && !type.equals(ShipType.MOD)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_094);
                    message.setMessageArgs(new String[] { startRowNum });
                    messageLists.add(message);
                    continue;
                }

                CPMSRF11Entity cpmsrf11Entity = new CPMSRF11Entity();

                // set type
                cpmsrf11Entity.setType(type);

                // set ShippingRouteCode
                startCol += IntDef.INT_TWO;
                String shippingRouteCode = StringUtil.toSafeString(PoiUtil
                    .getStringCellValue(sheet, startRow, startCol));
                if (!ValidatorUtils.requiredValidator(shippingRouteCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ShippingRouteCd" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(shippingRouteCode, IntDef.INT_THIRTY)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ShippingRouteCd",
                        StringUtil.toSafeString(IntDef.INT_THIRTY) });
                    messageLists.add(message);
                } else {
                    cpmsrf11Entity.setShippingRouteCode(shippingRouteCode);
                }

                // set DeliveryStart
                startCol++;
                String deliveryStart = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(deliveryStart)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DeliverytoWHStartDate" });
                    messageLists.add(message);
                } else {
                    Date deliveryStartDate = DateTimeUtil.parseDate(deliveryStart);
                    if (!ValidatorUtils.requiredValidator(deliveryStartDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DeliverytoWHStartDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setDeliveryStart(deliveryStartDate);
                    }
                }

                // set DeliveryEnd
                startCol += IntDef.INT_TWO;
                String deliveryEnd = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(deliveryEnd)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DeliverytoWHEndDate" });
                    messageLists.add(message);
                } else {
                    Date deliveryEndDate = DateTimeUtil.parseDate(deliveryEnd);
                    if (!ValidatorUtils.requiredValidator(deliveryEndDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DeliverytoWHEndDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setDeliveryEnd(deliveryEndDate);
                    }
                }

                // set PackingEnd
                startCol += IntDef.INT_TWO;
                String packingEnd = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(packingEnd)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_PackingCompletion" });
                    messageLists.add(message);
                } else {
                    Date packingEndDate = DateTimeUtil.parseDate(packingEnd);
                    if (!ValidatorUtils.requiredValidator(packingEndDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_PackingCompletion",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setPackingEnd(packingEndDate);
                    }
                }

                // set LastVanning
                startCol += IntDef.INT_TWO;
                String lastVanning = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(lastVanning)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_LastVanningDate" });
                    messageLists.add(message);
                } else {
                    Date lastVanningDate = DateTimeUtil.parseDate(lastVanning);
                    if (!ValidatorUtils.requiredValidator(lastVanningDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_LastVanningDate",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setLastVanning(lastVanningDate);
                    }
                }

                // set ShippingInstruction
                startCol += IntDef.INT_TWO;
                String shippingInstruction = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(shippingInstruction)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ShippingInstruction" });
                    messageLists.add(message);
                } else {
                    Date shippingInstructionDate = DateTimeUtil.parseDate(shippingInstruction);
                    if (!ValidatorUtils.requiredValidator(shippingInstructionDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ShippingInstruction",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setShippingInstruction(shippingInstructionDate);
                    }
                }

                // set DocsPreparation
                startCol += IntDef.INT_TWO;
                String docsPreparation = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(docsPreparation)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DocsPreparation" });
                    messageLists.add(message);
                } else {
                    Date docsPreparationDate = DateTimeUtil.parseDate(docsPreparation);
                    if (!ValidatorUtils.requiredValidator(docsPreparationDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DocsPreparation",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setDocsPreparation(docsPreparationDate);
                    }
                }

                // set CustomClearance
                startCol += IntDef.INT_TWO;
                String customClearance = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(customClearance)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_CustomClearance" });
                    messageLists.add(message);
                } else {
                    Date customClearanceDate = DateTimeUtil.parseDate(customClearance);
                    if (!ValidatorUtils.requiredValidator(customClearanceDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_CustomClearance",
                            "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setCustomClearance(customClearanceDate);
                    }
                }

                // set CyCut
                startCol += IntDef.INT_TWO;
                String cyCut = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(cyCut)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_CYCut" });
                    messageLists.add(message);
                } else {
                    Date cyCutDate = DateTimeUtil.parseDate(cyCut);
                    if (!ValidatorUtils.requiredValidator(cyCutDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message
                            .setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_CYCut", "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setCyCut(cyCutDate);
                    }
                }

                // set PortIn
                startCol += IntDef.INT_TWO;
                String portIn = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(portIn)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_Portin" });
                    messageLists.add(message);
                } else {
                    Date portInDate = DateTimeUtil.parseDate(portIn);
                    if (!ValidatorUtils.requiredValidator(portInDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message
                            .setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_Portin", "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setPortIn(portInDate);
                    }
                }

                // set Etd
                startCol += IntDef.INT_TWO;
                String etd = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(etd)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ETD" });
                    messageLists.add(message);
                } else {
                    Date etdDate = DateTimeUtil.parseDate(etd);
                    if (!ValidatorUtils.requiredValidator(etdDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ETD", "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setEtd(etdDate);
                    }
                }

                // set Eta
                startCol += IntDef.INT_TWO;
                String eta = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(eta)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ETA" });
                    messageLists.add(message);
                } else {
                    Date etaDate = DateTimeUtil.parseDate(eta);
                    if (!ValidatorUtils.requiredValidator(etaDate)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_ETA", "CPMSRF11_Grid_Date" });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setEta(etaDate);
                    }
                }

                // set ImpCcLeadtime
                startCol += IntDef.INT_TWO;
                String impCcLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(impCcLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTETAtoImpCC" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(impCcLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTETAtoImpCC",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(impCcLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTETAtoImpCC",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(impCcLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(impCcLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTETAtoImpCC",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    cpmsrf11Entity.setImpCcLeadtime(Integer.valueOf(impCcLeadtime));
                }

                // set impInboundLeadtime
                startCol++;
                String impInboundLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(impInboundLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTCustomstoInbound" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(impInboundLeadtime, IntDef.INT_THREE)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTCustomstoInbound",
                        StringUtil.toSafeString(IntDef.INT_THREE) });
                    messageLists.add(message);
                } else if (!StringUtil.isNumeric(impInboundLeadtime)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_025);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTCustomstoInbound",
                        "CPMSRF11_Grid_Integer" });
                    messageLists.add(message);
                } else if (Integer.valueOf(impInboundLeadtime) < IntDef.INT_ZERO
                        || Integer.valueOf(impInboundLeadtime) > IntDef.INT_NINE_NINE_NINE_FOR_SQL_IN_LIMIT) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                    message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_WorkingLTCustomstoInbound",
                        StringUtil.toSafeString(IntDef.INT_ZERO) });
                    messageLists.add(message);
                } else {
                    cpmsrf11Entity.setImpInboundLeadtime(Integer.valueOf(impInboundLeadtime));
                }

                // set inactiveFlag
                startCol++;
                String inactive = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (inactiveFlag) {
                    if (!ValidatorUtils.requiredValidator(inactive)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_001);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DiscontinueIndicator" });
                        messageLists.add(message);
                    } else if (!ValidatorUtils.maxLengthValidator(inactive, IntDef.INT_ONE)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_050);
                        message.setMessageArgs(new String[] { startRowNum, "CPMSRF01_Grid_DiscontinueIndicator",
                            StringUtil.toSafeString(IntDef.INT_ONE) });
                        messageLists.add(message);
                    } else {
                        cpmsrf11Entity.setInactiveFlag(inactive);
                    }
                }

                // set row number
                cpmsrf11Entity.setRowNum(startRow);

                // add NEW data
                if (type.equals(ShipType.NEW)) {
                    cpmsrf11EntityNewList.add(cpmsrf11Entity);
                }
                // add mod data
                else if (type.equals(ShipType.MOD)) {
                    cpmsrf11EntityModList.add(cpmsrf11Entity);
                    if (TypeYN.Y.equals(inactive)) {
                        cpmsrf11EntityModYList.add(cpmsrf11Entity);
                    }
                }

                // add data
                cpmsrf11EntityList.add(cpmsrf11Entity);
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
        List<CPMSRF11Entity> dataLists = new ArrayList<CPMSRF11Entity>();
        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, Integer> shipRCodeCountMapsDB = new HashMap<String, Integer>();
        if (cpmsrf11EntityList != null && cpmsrf11EntityList.size() > 0) {
            maps = dealDataDetail(cpmsrf11EntityList, cpmsrf11EntityNewList, cpmsrf11EntityModList,
                cpmsrf11EntityModYList, request);
        }

        List<BaseMessage> msgList = (List<BaseMessage>) maps.get("messageLists");
        if (msgList != null && msgList.size() > 0) {
            throw new BusinessException(msgList);
        }

        configMsgList = (List<BaseMessage>) maps.get("configMsgList");
        // shippingRouteCode , deliveryStart + deliveryEnd not continuous
        Map<String, Object> shipRCodeMaps = (Map<String, Object>) maps.get("shipRCodeMaps");
        if (shipRCodeMaps != null && shipRCodeMaps.size() > 0) {
            Map<String, Object> shipRCodeMapsDB = (Map<String, Object>) maps.get("shipRCodeMapsDB");
            for (Map.Entry<String, Object> entry : shipRCodeMaps.entrySet()) {
                List<CPMSRF11DateEntity> dateList = (List<CPMSRF11DateEntity>) entry.getValue();
                List<CPMSRF11DateEntity> dateListDB = (List<CPMSRF11DateEntity>) shipRCodeMapsDB.get(entry.getKey());
                boolean continuousFlag = cpmsrf11Service.checkDateContinuous(dateList, dateListDB);
                if (!continuousFlag) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.C1028);
                    message.setMessageArgs(new String[] { "CPMSRF01_Grid_DeliverytoWHEndDate",
                        "CPMSRF01_Grid_DeliverytoWHStartDate" });
                    configMsgList.add(message);
                    break;
                }
            }
        }

        // deal all inactiveFlag is Y
        shipRCodeCountMapsDB = (Map<String, Integer>) maps.get("shipRCodeCountMapsDB");
        if (cpmsrf11EntityNewList != null && cpmsrf11EntityNewList.size() > 0) {
            for (CPMSRF11Entity ce : cpmsrf11EntityNewList) {
                String shippingRouteCode = ce.getShippingRouteCode();
                Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                if (count != null) {
                    count = count + IntDef.INT_ONE;
                    shipRCodeCountMapsDB.put(shippingRouteCode, count);
                }
            }
        }
        List<String> shipRCodelist = new ArrayList<String>();
        if (shipRCodeCountMapsDB != null && shipRCodeCountMapsDB.size() > 0) {
            for (Map.Entry<String, Integer> entry : shipRCodeCountMapsDB.entrySet()) {
                if (0 == entry.getValue()) {
                    shipRCodelist.add(entry.getKey());
                }
            }
        }
        maps.put("shipRCodelist", shipRCodelist);

        if (configMsgList != null && configMsgList.size() > 0) {
            dataLists = (List<CPMSRF11Entity>) maps.get("cpmsrf11EntityList");
            SessionInfoManager context = SessionInfoManager.getContextInstance(request);
            context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString(), dataLists);
            context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY.toString()) + "shipRCodelist", shipRCodelist);
            throw new BusinessException(configMsgList);
        }
        return maps;
    }

    /**
     * deal data detail
     * 
     * @param cpmsrf11EntityList cpmsrf11EntityList
     * @param newList newList
     * @param modList modList
     * @param modYList modYList
     * @param request request
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> dealDataDetail(List<CPMSRF11Entity> cpmsrf11EntityList, List<CPMSRF11Entity> newList,
        List<CPMSRF11Entity> modList, List<CPMSRF11Entity> modYList, HttpServletRequest request) {

        // get userOffId
        UserInfo userInfo = getLoginUser(request);
        List<Integer> userOffIds = cpmpms01Service.setUserOffIds(userInfo, BusinessPattern.V_V);
        Integer userId = userInfo.getUserId();

        Map<String, Object> maps = new HashMap<String, Object>();

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> configMsgList = new ArrayList<BaseMessage>();

        // query new shippingRouteCode + deliveryStart + deliveryEnd
        List<CPMSRF11Entity> newDataList = cpmsrf11Service.getNewShipRCodeWSEMaps(newList);

        // not vv shippingRouteType shippingRouteCode
        Map<String, String> newSSMaps = new HashMap<String, String>();

        // DB time now
        long dbTime = cpmsrf11Service.getDBtime();

        Map<String, String> shipRCodeWSEMapsDB = new HashMap<String, String>();
        Map<String, Object> shipRCodeMapsDB = new HashMap<String, Object>();
        Map<String, Object> shipRCodeMaps = new HashMap<String, Object>();
        // set db map
        if (newDataList != null && newDataList.size() > 0) {
            for (CPMSRF11Entity entity : newDataList) {
                String shippingRouteCode = entity.getShippingRouteCode();
                Date deliveryStart = entity.getDeliveryStart();
                Date deliveryEnd = entity.getDeliveryEnd();

                String key = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + deliveryStart.getTime()
                        + StringConst.DOUBLE_UNDERLINE + deliveryEnd.getTime();

                shipRCodeWSEMapsDB.put(key, entity.getInactiveFlag());

                String shippingRouteType = entity.getShippingRouteType();
                if (!StringUtil.isNullOrEmpty(shippingRouteType)
                        && !StringUtil.toSafeString(ShippingRouteType.VV).equals(shippingRouteType)) {
                    newSSMaps.put(shippingRouteCode, shippingRouteCode);
                }
            }
        }

        // query db mod shippingRouteCode + deliveryStart + deliveryEnd
        Map<String, Object> shipRCodeWSEMapMDB = cpmsrf11Service.getModShipRCodeWSEMaps(modList);

        Map<String, Object> shipRCodeWSEMapsMDB = (Map<String, Object>) shipRCodeWSEMapMDB.get("shipRCodeWSEMapsMDB");
        Map<String, String> modDataYNMapsDB = (Map<String, String>) shipRCodeWSEMapMDB.get("modDataYNMapsDB");

        // query new inactiveFlag
        Map<String, Object> newInactiveMapsDB = cpmsrf11Service.getNewInactiveFlag();
        // query mod inactiveFlag
        Map<String, Object> modInactiveMapsDB = cpmsrf11Service.getModInactiveFlag();

        // query db shippingRouteCode + impCcLeadtime + impInboundLeadtime + etd + officeId
        List<CPMSRF11Entity> siceList = cpmsrf11Service.getSice(cpmsrf11EntityList);

        // shippingRouteCode + impCcLeadtime maps
        Map<String, Object> shipRCodeImpCclMapsDB = new HashMap<String, Object>();
        // shippingRouteCode + count(impCcLeadtime) DB maps
        Map<String, Integer> shipRCodeCountMapsDB = new HashMap<String, Integer>();
        // shippingRouteCode + count(impCcLeadtime) maps
        Map<String, Integer> shipRCodeCountCMaps = new HashMap<String, Integer>();
        // shippingRouteCode + count(impInboundLeadtime) maps
        Map<String, Integer> shipRCodeCountIMaps = new HashMap<String, Integer>();
        // shippingRouteCode + impInboundLeadtime maps
        Map<String, Object> shipRCodeImpInbMapsDB = new HashMap<String, Object>();
        // shippingRouteCode + etd maps
        Map<String, Object> shipRCodeMaMiEtdMapsDB = new HashMap<String, Object>();
        // db shippingRouteCode not have officeId
        Map<String, String> shipRCodeOMapDBN = new HashMap<String, String>();
        // db shippingRouteCode have officeId
        Map<String, Integer> shipRCodeOMapDBY = new HashMap<String, Integer>();
        // db shippingRouteCode + srId
        Map<String, Integer> shipRCSrIdMapDB = new HashMap<String, Integer>();

        if (siceList != null && siceList.size() > 0) {
            for (CPMSRF11Entity entity : siceList) {
                String shippingRouteCode = entity.getShippingRouteCode();

                if ((InactiveFlag.ACTIVE + "").equals(entity.getInactiveFlag())) {
                    Integer impCcLeadtime = entity.getImpCcLeadtime();
                    Integer impInboundLeadtime = entity.getImpInboundLeadtime();

                    // set shippingRouteCode + impCcLeadtime
                    shipRCodeImpCclMapsDB.put(shippingRouteCode, StringUtil.toSafeString(impCcLeadtime));
                    // set shippingRouteCode + impInboundLeadtime
                    shipRCodeImpInbMapsDB.put(shippingRouteCode, StringUtil.toSafeString(impInboundLeadtime));

                    // set db max etd and min etd
                    CPMSRF11DateEntity cpmsrf11DateEntity = (CPMSRF11DateEntity) shipRCodeMaMiEtdMapsDB
                        .get(shippingRouteCode);

                    if (cpmsrf11DateEntity != null) {
                        if (cpmsrf11DateEntity.getStart().getTime() > entity.getFromEtd().getTime()) {
                            cpmsrf11DateEntity.setStart(entity.getFromEtd());
                        }
                        if (cpmsrf11DateEntity.getEnd().getTime() < entity.getToEtd().getTime()) {
                            cpmsrf11DateEntity.setEnd(entity.getToEtd());
                        }
                    } else {
                        cpmsrf11DateEntity = new CPMSRF11DateEntity();
                        cpmsrf11DateEntity.setStart(entity.getFromEtd());
                        cpmsrf11DateEntity.setEnd(entity.getToEtd());
                    }

                    shipRCodeMaMiEtdMapsDB.put(shippingRouteCode, cpmsrf11DateEntity);
                }

                Integer officeId = entity.getOfficeId();
                if (officeId != null) {
                    boolean flag = cpmsrf11Service.checkOfficeId(userOffIds, officeId);
                    if (!flag) {
                        shipRCodeOMapDBN.put(shippingRouteCode, shippingRouteCode);
                    } else {
                        shipRCodeOMapDBY.put(shippingRouteCode, officeId);
                    }
                }

                shipRCSrIdMapDB.put(shippingRouteCode, entity.getSrId());
            }
        }

        // query db shippingRouteCode + etd + + deliveryStart + deliveryEnd + inactiveFlag
        List<CPMSRF11Entity> seoList = cpmsrf11Service.getShipRCodeEta(cpmsrf11EntityList);

        Map<String, Object> shipRCodeEtaMapsDB = new HashMap<String, Object>();
        Map<String, Object> shipRCodeEtaCMapsDB = new HashMap<String, Object>();

        for (CPMSRF11Entity ce : seoList) {
            if (ce.getInactiveFlag().equals(StringUtil.toSafeString(InactiveFlag.ACTIVE))) {
                String shippingRouteCode = ce.getShippingRouteCode();
                Date deliveryStart = ce.getDeliveryStart();
                Date deliveryEnd = ce.getDeliveryEnd();
                Date etd = ce.getEtd();
                long etdLong = etd.getTime();

                String key = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + etdLong;
                String value = StringUtil.toSafeString(etdLong);
                // shipRCodeEtaMapsDB.put(key, value);
                shipRCodeEtaMapsDB.put(key, shippingRouteCode + StringConst.DOUBLE_UNDERLINE + deliveryStart.getTime()
                        + StringConst.DOUBLE_UNDERLINE + deliveryEnd.getTime());

                String keyc = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + deliveryStart.getTime()
                        + StringConst.DOUBLE_UNDERLINE + deliveryEnd.getTime() + StringConst.DOUBLE_UNDERLINE + etdLong;
                shipRCodeEtaCMapsDB.put(keyc, value);

                List<CPMSRF11DateEntity> lists = (List<CPMSRF11DateEntity>) shipRCodeMapsDB.get(shippingRouteCode);
                CPMSRF11DateEntity cf = new CPMSRF11DateEntity();
                cf.setStart(deliveryStart);
                cf.setEnd(deliveryEnd);
                if (lists != null && lists.size() > 0) {
                    lists.add(cf);
                } else {
                    lists = new ArrayList<CPMSRF11DateEntity>();
                    lists.add(cf);
                }
                shipRCodeMapsDB.put(shippingRouteCode, lists);

                Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                if (count == null) {
                    shipRCodeCountMapsDB.put(shippingRouteCode, IntDef.INT_ONE);
                } else {
                    count = count + IntDef.INT_ONE;
                    shipRCodeCountMapsDB.put(shippingRouteCode, count);
                }
            }
        }

        Map<String, Object> shipRCodeWSEMaps = new HashMap<String, Object>();
        Map<String, Object> shipRCodeImpCclMaps = new HashMap<String, Object>();
        Map<String, Object> shipRCodeImpInbMaps = new HashMap<String, Object>();
        Map<String, Object> shipRCodeEtaMaps = new HashMap<String, Object>();

        Map<String, Object> shipRCodeMaMiEtdMaps = new HashMap<String, Object>();

        // set xlsx max etd and min etd
        for (CPMSRF11Entity ce : cpmsrf11EntityList) {
            // if (TypeYN.N.equals(ce.getInactiveFlag())) {
            String shippingRouteCode = ce.getShippingRouteCode();
            long etd = ce.getEtd().getTime();

            CPMSRF11DateEntity cpmsrf11DateEntity = (CPMSRF11DateEntity) shipRCodeMaMiEtdMaps.get(shippingRouteCode);

            if (cpmsrf11DateEntity != null) {
                if (cpmsrf11DateEntity.getEnd().getTime() < etd) {
                    cpmsrf11DateEntity.setEnd(ce.getEtd());
                }
                if (cpmsrf11DateEntity.getStart().getTime() > etd) {
                    cpmsrf11DateEntity.setStart(ce.getEtd());
                }
            } else {
                cpmsrf11DateEntity = new CPMSRF11DateEntity();
                cpmsrf11DateEntity.setEnd(ce.getEtd());
                cpmsrf11DateEntity.setStart(ce.getEtd());
            }

            shipRCodeMaMiEtdMaps.put(shippingRouteCode, cpmsrf11DateEntity);
            // }
        }

        // mod data type is Y
        // Map<String, Object> modDataYmaps = new HashMap<String, Object>();
        if (modYList != null && modYList.size() > 0) {
            for (CPMSRF11Entity ce : modYList) {
                String shippingRouteCode = ce.getShippingRouteCode();
                List<CPMSRF11DateEntity> listsDB = (List<CPMSRF11DateEntity>) shipRCodeMapsDB.get(shippingRouteCode);
                if (listsDB != null && listsDB.size() > 0) {
                    for (int i = 0; i < listsDB.size(); i++) {
                        CPMSRF11DateEntity cd = listsDB.get(i);
                        if (cd.getStart().getTime() == ce.getDeliveryStart().getTime()
                                && cd.getEnd().getTime() == ce.getDeliveryEnd().getTime()) {
                            listsDB.remove(i);
                            break;
                        }
                    }
                    if (listsDB != null && listsDB.size() > 0) {
                        shipRCodeMapsDB.put(shippingRouteCode, listsDB);
                    }
                }
            }
        }

        List<String> modKeyList = new ArrayList<String>();
        if (modList != null && modList.size() > 0) {
            for (CPMSRF11Entity ce : modList) {
                Date firstEtd = ce.getDeliveryStart();
                Date lastEtd = ce.getDeliveryEnd();
                String shippingRouteCode = ce.getShippingRouteCode();
                String key = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + firstEtd.getTime()
                        + StringConst.DOUBLE_UNDERLINE + lastEtd.getTime();
                String value = modDataYNMapsDB.get(key);
                modKeyList.add(key);

                if (value != null) {
                    String discontinueIndicator = ce.getInactiveFlag();
                    if (null == discontinueIndicator && (InactiveFlag.ACTIVE + "").equals(value)) {
                        ce.setInactiveFlag(TypeYN.N);
                    } else if (null == discontinueIndicator && (InactiveFlag.INACTIVE + "").equals(value)) {
                        ce.setInactiveFlag(TypeYN.Y);
                    }

                    if (TypeYN.N.equals(discontinueIndicator) && (InactiveFlag.INACTIVE + "").equals(value)) {
                        Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                        if (count == null) {
                            shipRCodeCountMapsDB.put(shippingRouteCode, 1);
                        } else {
                            count = count + IntDef.INT_ONE;
                            shipRCodeCountMapsDB.put(shippingRouteCode, count);
                        }

                        String valueWSE = shipRCodeWSEMapsDB.get(key);
                        if (null != valueWSE) {
                            shipRCodeWSEMapsDB.put(key, InactiveFlag.ACTIVE + "");
                        }
                    } else if (TypeYN.Y.equals(discontinueIndicator) && (InactiveFlag.ACTIVE + "").equals(value)) {
                        Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                        count = count - IntDef.INT_ONE;
                        shipRCodeCountMapsDB.put(shippingRouteCode, count);

                        String valueWSE = shipRCodeWSEMapsDB.get(key);
                        if (null != valueWSE) {
                            shipRCodeWSEMapsDB.put(key, InactiveFlag.INACTIVE + "");
                        }
                    }
                }

                if (TypeYN.N.equals(ce.getInactiveFlag())) {
                    Integer impCcLeadtime = ce.getImpCcLeadtime();
                    Integer impInboundLeadtime = ce.getImpInboundLeadtime();
                    String keyc = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + impCcLeadtime;
                    String keyi = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + impInboundLeadtime;

                    Integer countc = shipRCodeCountCMaps.get(keyc);
                    if (countc == null) {
                        shipRCodeCountCMaps.put(keyc, IntDef.INT_ONE);
                    } else {
                        countc = countc + IntDef.INT_ONE;
                        shipRCodeCountCMaps.put(keyc, countc);
                    }

                    Integer counti = shipRCodeCountIMaps.get(keyi);
                    if (counti == null) {
                        shipRCodeCountIMaps.put(keyi, IntDef.INT_ONE);
                    } else {
                        counti = counti + IntDef.INT_ONE;
                        shipRCodeCountIMaps.put(keyi, counti);
                    }
                }
            }
        }

        // officeId not exist
        Map<String, String> officeIdMaps = new HashMap<String, String>();
        // for one shippingRouteCode check xlsx firstEtd + lastEtd in one time period msg
        Map<String, String> sameSFLMaps = new HashMap<String, String>();
        // shippingRouteCode repeat impCcLeadtimeimp + InboundLeadtime
        Map<String, String> sameIIMaps = new HashMap<String, String>();
        List<String> ccShippingRouteList = new ArrayList<String>();
        List<String> inboundShippingRouteList = new ArrayList<String>();
        // shippingRouteCode db repeat impCcLeadtimeimp + InboundLeadtime
        Map<String, String> sameIIMapsDB = new HashMap<String, String>();

        // c1015
        Map<String, String> comfirmEDMap = new HashMap<String, String>();
        // shippingRouteCode + max(DeliveryEnd)
        Map<String, Object> shipRCodeEndMap = new HashMap<String, Object>();

        // check all data
        for (CPMSRF11Entity ce : cpmsrf11EntityList) {

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

            String rowNum = StringUtil.toSafeString(ce.getRowNum());
            long deliveryStart = ce.getDeliveryStart().getTime();
            Date deliveryEndDate = ce.getDeliveryEnd();
            long deliveryEnd = deliveryEndDate.getTime();
            String inactiveFlag = ce.getInactiveFlag();
            String key = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + deliveryStart
                    + StringConst.DOUBLE_UNDERLINE + deliveryEnd;
            boolean repFlag = true;

            // check repeat ShippingRouteCode + DeliveryStart + DeliveryEnd
            String newVlaueWSE = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + deliveryStart
                    + StringConst.DOUBLE_UNDERLINE + deliveryEnd;
            String oldVlaueWSE = (String) shipRCodeWSEMaps.get(newVlaueWSE);
            if (newVlaueWSE.equals(oldVlaueWSE)) {
                repFlag = false;
                sameSFLMaps.put(rowNum, MessageCodeConst.W1004_078);
            } else {
                shipRCodeWSEMaps.put(newVlaueWSE, newVlaueWSE);
            }

            // check deliveryEnd and deliveryStart
            if (deliveryEnd < deliveryStart) {
                repFlag = false;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_DeliverytoWHEndDate",
                    "CPMSRF01_Grid_DeliverytoWHStartDate" });
                messageLists.add(message);
            }

            // check type NEW
            if (ce.getType().equals(ShipType.NEW)) {
                if (repFlag) {
                    // check db shippingRouteCode + deliveryStart + deliveryEnd
                    String value = shipRCodeWSEMapsDB.get(key);
                    if (null != value) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_074);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_ShippingRouteCd",
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
                // check confirm deliveryEnd and dbTime
                if (deliveryEnd < dbTime) {
                    comfirmEDMap.put(shippingRouteCode, shippingRouteCode);
                }

                // get max deliveryEnd
                Date deliveryEndx = (Date) shipRCodeEndMap.get(shippingRouteCode);
                if (null == deliveryEndx) {
                    shipRCodeEndMap.put(shippingRouteCode, deliveryEndDate);
                } else {
                    if (deliveryEndx.before(deliveryEndDate)) {
                        shipRCodeEndMap.put(shippingRouteCode, deliveryEndDate);
                    }
                }

                // check inactiveFlag
                if (!StringUtil.isNullOrEmpty(inactiveFlag)) {
                    String inactiveDB = (String) newInactiveMapsDB.get(inactiveFlag);
                    if (StringUtil.isNullOrEmpty(inactiveDB)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF02_Grid_DiscontinueIndicator", TypeYN.N });
                        messageLists.add(message);
                    } else {
                        ce.setInactiveFlag(inactiveDB);
                    }
                } else {
                    ce.setInactiveFlag(InactiveFlag.ACTIVE + "");
                }
            }

            // check type MOD
            if (ce.getType().equals(ShipType.MOD)) {
                if (repFlag) {
                    // check shippingRouteCode + deliveryStart + deliveryEnd
                    String value = (String) shipRCodeWSEMapsMDB.get(key);
                    if (!shippingRouteCode.equals(value)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_075);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_ShippingRouteCd",
                            "CPMSRF11_Grid_ShippingRouteMaster" });
                        messageLists.add(message);
                        continue;
                    }
                }
                // check inactiveFlag
                if (!StringUtil.isNullOrEmpty(inactiveFlag)) {
                    String inactiveDB = (String) modInactiveMapsDB.get(inactiveFlag);
                    if (StringUtil.isNullOrEmpty(inactiveDB)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_076);
                        message
                            .setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_DiscontinueIndicator", TypeYN.Y_N });
                        messageLists.add(message);
                    } else {
                        ce.setInactiveFlag(inactiveDB);
                    }
                } else {
                    String inactiveFlagDB = modDataYNMapsDB.get(key);
                    ce.setInactiveFlag(inactiveFlagDB);
                }
            }

            // set srid
            Integer srId = shipRCSrIdMapDB.get(shippingRouteCode);
            if (null != srId) {
                ce.setSrId(srId);
            }

            // check deliveryStart + deliveryEnd in one time period
            if (repFlag && (InactiveFlag.ACTIVE + "").equals(ce.getInactiveFlag())) {
                // List<CPMSRF11DateEntity> modDataYList = (List<CPMSRF11DateEntity>)
                // modDataYmaps.get(shippingRouteCode);
                // check xlsx
                boolean repPeFlag = true;
                List<CPMSRF11DateEntity> lists = (List<CPMSRF11DateEntity>) shipRCodeMaps.get(shippingRouteCode);
                CPMSRF11DateEntity cf = new CPMSRF11DateEntity();
                cf.setStart(ce.getDeliveryStart());
                cf.setEnd(ce.getDeliveryEnd());
                // check list
                if (lists != null && lists.size() > 0) {
                    // boolean flag = cpmsrf11Service.intervals(lists, deliveryStart, deliveryEnd, modDataYList);
                    boolean flag = cpmsrf11Service.intervals(lists, deliveryStart, deliveryEnd);
                    if (!flag) {
                        repPeFlag = false;
                        sameSFLMaps.put(rowNum, MessageCodeConst.W1004_078);
                    } else {
                        lists.add(cf);
                        shipRCodeMaps.put(shippingRouteCode, lists);
                    }
                } else {
                    lists = new ArrayList<CPMSRF11DateEntity>();
                    lists.add(cf);
                    shipRCodeMaps.put(shippingRouteCode, lists);
                }

                if (repPeFlag) {
                    // check DB
                    List<CPMSRF11DateEntity> listsDb = (List<CPMSRF11DateEntity>) shipRCodeMapsDB
                        .get(shippingRouteCode);
                    // boolean periodFlagDB = cpmsrf11Service.intervals(listsDb, deliveryStart, deliveryEnd,
                    // modDataYList);
                    boolean periodFlagDB = cpmsrf11Service.intervals(listsDb, deliveryStart, deliveryEnd);
                    if (!periodFlagDB) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_078);
                        message.setMessageArgs(new String[] { rowNum, "CPMSRF11_Grid_ShippingRouteMaster" });
                        messageLists.add(message);
                    }
                }
            }

            // check PackingEnd and deliveryEnd
            long packingEnd = ce.getPackingEnd().getTime();
            if (packingEnd < deliveryEnd) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_PackingCompletion",
                    "CPMSRF01_Grid_DeliverytoWHEndDate" });
                messageLists.add(message);
            }

            // check LastVanning and PackingEnd
            long lastVanning = ce.getLastVanning().getTime();
            if (lastVanning < packingEnd) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_LastVanningDate",
                    "CPMSRF01_Grid_PackingCompletion" });
                messageLists.add(message);
            }

            // check shippingInstruction and lastVanning
            long shippingInstruction = ce.getShippingInstruction().getTime();
            if (shippingInstruction < lastVanning) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_ShippingInstruction",
                    "CPMSRF01_Grid_LastVanningDate" });
                messageLists.add(message);
            }

            // check DocsPreparation and shippingInstruction
            long docsPreparation = ce.getDocsPreparation().getTime();
            if (docsPreparation < shippingInstruction) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_DocsPreparation",
                    "CPMSRF01_Grid_ShippingInstruction" });
                messageLists.add(message);
            }

            // check customClearance and docsPreparation
            long customClearance = ce.getCustomClearance().getTime();
            if (customClearance < docsPreparation) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_CustomClearance",
                    "CPMSRF01_Grid_DocsPreparation" });
                messageLists.add(message);
            }

            // check cyCut and customClearance
            long cyCut = ce.getCyCut().getTime();
            if (cyCut < customClearance) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_CYCut", "CPMSRF01_Grid_CustomClearance" });
                messageLists.add(message);
            }

            // check portIn and cyCut
            long portIn = ce.getPortIn().getTime();
            if (portIn < cyCut) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_Portin", "CPMSRF01_Grid_CYCut" });
                messageLists.add(message);
            }

            // check etd and portIn
            long etd = ce.getEtd().getTime();
            if (etd < portIn) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_ETD", "CPMSRF01_Grid_Portin" });
                messageLists.add(message);
            }

            // set xlsx fromEtd and endEtd
            CPMSRF11DateEntity cpmsrf11DateEntity = (CPMSRF11DateEntity) shipRCodeMaMiEtdMaps.get(shippingRouteCode);
            if (cpmsrf11DateEntity != null) {
                if (etd > cpmsrf11DateEntity.getStart().getTime()) {
                    ce.setFromEtd(cpmsrf11DateEntity.getStart());
                } else {
                    ce.setFromEtd(ce.getEtd());
                }
                if (etd < cpmsrf11DateEntity.getEnd().getTime()) {
                    ce.setToEtd(cpmsrf11DateEntity.getEnd());
                } else {
                    ce.setToEtd(ce.getEtd());
                }
            }
            // set db fromEtd and endEtd
            CPMSRF11DateEntity cpmsrf11DateEntityDB = (CPMSRF11DateEntity) shipRCodeMaMiEtdMapsDB
                .get(shippingRouteCode);
            if (cpmsrf11DateEntityDB != null) {
                if (ce.getFromEtd().getTime() > cpmsrf11DateEntityDB.getStart().getTime()) {
                    ce.setFromEtd(cpmsrf11DateEntityDB.getStart());
                }
                if (ce.getToEtd().getTime() < cpmsrf11DateEntityDB.getEnd().getTime()) {
                    ce.setToEtd(cpmsrf11DateEntityDB.getEnd());
                }
            }

            // check eta and etd
            long eta = ce.getEta().getTime();
            if (eta < etd) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_077);
                message.setMessageArgs(new String[] { rowNum, "CPMSRF01_Grid_ETA", "CPMSRF01_Grid_ETD" });
                messageLists.add(message);
            }

            if ((InactiveFlag.ACTIVE + "").equals(ce.getInactiveFlag())) {
                // check repeat ShippingRouteCode + impCcLeadtime
                // check list
                String oldVlaueImpCcl = (String) shipRCodeImpCclMaps.get(shippingRouteCode);
                String impCcLeadtime = StringUtil.toSafeString(ce.getImpCcLeadtime());
                if (StringUtil.isNullOrEmpty(oldVlaueImpCcl) || impCcLeadtime.equals(oldVlaueImpCcl)) {
                    if ((InactiveFlag.ACTIVE + "").equals(ce.getInactiveFlag())) {
                        shipRCodeImpCclMaps.put(shippingRouteCode, impCcLeadtime);
                    }
                    // check DB
                    String oldVlaueImpCclDB = (String) shipRCodeImpCclMapsDB.get(shippingRouteCode);
                    if (!StringUtil.isNullOrEmpty(oldVlaueImpCclDB) && !impCcLeadtime.equals(oldVlaueImpCclDB)) {
                        Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                        Integer countc = shipRCodeCountCMaps.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                                + impCcLeadtime);
                        if (!count.equals(countc)) {
                            sameIIMapsDB.put("CPMSRF01_Grid_WorkingLTETAtoImpCC" + StringConst.DOUBLE_UNDERLINE
                                    + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + oldVlaueImpCclDB,
                                shippingRouteCode);
                        }
                    }
                } else if (!impCcLeadtime.equals(oldVlaueImpCcl)) {
                    sameIIMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + "CPMSRF01_Grid_WorkingLTETAtoImpCC", "CPMSRF01_Grid_ShippingRouteCd");
                    ccShippingRouteList.add(shippingRouteCode);
                }

                // check repeat ShippingRouteCode + impInboundLeadtime
                // check list
                String oldVlaueImpInb = (String) shipRCodeImpInbMaps.get(shippingRouteCode);
                String impInboundLeadtime = StringUtil.toSafeString(ce.getImpInboundLeadtime());
                if (StringUtil.isNullOrEmpty(oldVlaueImpInb) || impInboundLeadtime.equals(oldVlaueImpInb)) {
                    if ((InactiveFlag.ACTIVE + "").equals(ce.getInactiveFlag())) {
                        shipRCodeImpInbMaps.put(shippingRouteCode, impInboundLeadtime);
                    }
                    // check DB
                    String oldVlaueImpInbDB = (String) shipRCodeImpInbMapsDB.get(shippingRouteCode);
                    if (!StringUtil.isNullOrEmpty(oldVlaueImpInbDB) && !impInboundLeadtime.equals(oldVlaueImpInbDB)) {
                        Integer count = shipRCodeCountMapsDB.get(shippingRouteCode);
                        Integer counti = shipRCodeCountIMaps.get(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                                + impInboundLeadtime);
                        if (!count.equals(counti)) {
                            sameIIMapsDB.put("CPMSRF01_Grid_WorkingLTCustomstoInbound" + StringConst.DOUBLE_UNDERLINE
                                    + shippingRouteCode + StringConst.DOUBLE_UNDERLINE + oldVlaueImpInbDB,
                                shippingRouteCode);
                        }
                    }
                } else if (!impInboundLeadtime.equals(oldVlaueImpInb)) {
                    sameIIMaps.put(shippingRouteCode + StringConst.DOUBLE_UNDERLINE
                            + "CPMSRF01_Grid_WorkingLTCustomstoInbound", "CPMSRF01_Grid_ShippingRouteCd");
                    inboundShippingRouteList.add(shippingRouteCode);
                }

                // check repeat ShippingRouteCode + etd
                // check list
                String oldVlaueEta = (String) shipRCodeEtaMaps.get(shippingRouteCode);
                String etdString = StringUtil.toSafeString(etd);
                if (etdString.equals(oldVlaueEta)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_095);
                    message.setMessageArgs(new String[] { rowNum });
                    messageLists.add(message);
                } else {
                    if ((InactiveFlag.ACTIVE + "").equals(ce.getInactiveFlag())) {
                        shipRCodeEtaMaps.put(shippingRouteCode, etdString);
                    }
                    // check DB
                    String newVlaueEtaCDB = key + StringConst.DOUBLE_UNDERLINE + etdString;
                    String oldVlaueEtaCDB = (String) shipRCodeEtaCMapsDB.get(newVlaueEtaCDB);
                    if (StringUtil.isNullOrEmpty(oldVlaueEtaCDB)) {
                        String newVlaueEtaDB = shippingRouteCode + StringConst.DOUBLE_UNDERLINE + etdString;
                        String oldVlaueEtaDB = (String) shipRCodeEtaMapsDB.get(newVlaueEtaDB);
                        if (!StringUtil.isNullOrEmpty(oldVlaueEtaDB) && !modKeyList.contains(oldVlaueEtaDB)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_096);
                            message.setMessageArgs(new String[] { rowNum });
                            messageLists.add(message);
                        }
                    }
                }
            }

            // set createdDate
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
        if (sameSFLMaps != null && sameSFLMaps.size() > 0) {
            for (Map.Entry<String, String> entry : sameSFLMaps.entrySet()) {
                BaseMessage message = new BaseMessage(entry.getValue());
                message.setMessageArgs(new String[] { entry.getKey(), "CPMSRF11_Grid_UploadFile" });
                messageLists.add(message);
            }
        }
        if (sameIIMaps != null && sameIIMaps.size() > 0) {
            for (Map.Entry<String, String> entry : sameIIMaps.entrySet()) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_079);
                String enkey = entry.getKey();
                String[] keyList = enkey.split(StringConst.DOUBLE_UNDERLINE);
                message.setMessageArgs(new String[] { keyList[1], entry.getValue(), keyList[0] });
                messageLists.add(message);
            }
        }
        if (sameIIMapsDB != null && sameIIMapsDB.size() > 0) {
            for (Map.Entry<String, String> entry : sameIIMapsDB.entrySet()) {
                String enkey = entry.getKey();
                String[] keyList = enkey.split(StringConst.DOUBLE_UNDERLINE);
                if ("CPMSRF01_Grid_WorkingLTETAtoImpCC".equals(keyList[0]) && ccShippingRouteList.contains(keyList[1])) {
                    continue;
                }
                if ("CPMSRF01_Grid_WorkingLTCustomstoInbound".equals(keyList[0])
                        && inboundShippingRouteList.contains(keyList[1])) {
                    continue;
                }
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_080);
                message.setMessageArgs(new String[] { keyList[0], keyList[1], keyList[IntDef.INT_TWO] });
                messageLists.add(message);
            }
        }
        if (comfirmEDMap != null && comfirmEDMap.size() > 0) {
            // for (Map.Entry<String, String> entry : comfirmEDMap.entrySet()) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.C1015);
            // message.setMessageArgs(new String[] { entry.getKey() });
            // configMsgList.add(message);
            // }
            BaseMessage message = new BaseMessage(MessageCodeConst.C1015);
            configMsgList.add(message);
        }

        // check dbTime + deliveryEnd
        if (shipRCodeEndMap != null && shipRCodeEndMap.size() > 0) {
            boolean comfirmSE = checkShortDate(shipRCodeWSEMapsDB, shipRCodeEndMap, dbTime);
            if (!comfirmSE) {
                BaseMessage message = new BaseMessage(MessageCodeConst.C1016);
                configMsgList.add(message);
            }
        }

        maps.put("messageLists", messageLists);
        maps.put("configMsgList", configMsgList);
        maps.put("cpmsrf11EntityList", cpmsrf11EntityList);
        maps.put("shipRCodeCountMapsDB", shipRCodeCountMapsDB);
        maps.put("shipRCodeMaps", shipRCodeMaps);
        maps.put("shipRCodeMapsDB", shipRCodeMapsDB);
        return maps;
    }

    /**
     * check Static style
     * 
     * @param sheet this sheet
     * @return boolean
     */
    private boolean checkStatic(Sheet sheet) {
        // staticcheck
        String lastNameNi = PoiUtil.getStringCellValue(sheet, IntDef.INT_NINE, IntDef.INT_TWENTY_EIGHT);
        String lastNameTen = PoiUtil.getStringCellValue(sheet, IntDef.INT_TEN, IntDef.INT_TWENTY_EIGHT);
        String lastNameEl = PoiUtil.getStringCellValue(sheet, IntDef.INT_ELEVEN, IntDef.INT_TWENTY_EIGHT);

        String msg = MessageManager.getMessage("CPMSRF01_Grid_DiscontinueIndicator", Language.CHINESE.getLocale());
        if (!msg.equals(lastNameNi)) {
            return false;
        }
        if (!DISCONTINUE_INDICATOR.equals(lastNameTen)) {
            return false;
        }
        if (!FIXED_Y_N.equals(lastNameEl)) {
            return false;
        }

        return true;
    }

    /**
     * check dbTime + deliveryEnd
     * 
     * @param shipRCodeWSEMapsDB shipRCodeWSEMapsDB
     * @param shipRCodeEndMap shipRCodeEndMap
     * @param dbTime dbTime
     * @return flag flag
     */
    private boolean checkShortDate(Map<String, String> shipRCodeWSEMapsDB, Map<String, Object> shipRCodeEndMap,
        long dbTime) {
        boolean flag = true;
        if (shipRCodeWSEMapsDB != null && shipRCodeWSEMapsDB.size() > 0) {
            for (Map.Entry<String, String> entry : shipRCodeWSEMapsDB.entrySet()) {
                if ((InactiveFlag.ACTIVE + "").equals(entry.getValue())) {
                    String key = entry.getKey();
                    String[] keys = key.split(StringConst.DOUBLE_UNDERLINE);

                    Date value = (Date) shipRCodeEndMap.get(keys[0]);
                    if (null != value) {
                        long endDate = Long.parseLong(keys[IntDef.INT_TWO]);
                        if (endDate > value.getTime()) {
                            shipRCodeEndMap.put(keys[0], new Date(endDate));
                        }
                    }
                }
            }
        }

        Date startAft = DateTimeUtil.addMonth(new Date(dbTime), IntDef.INT_SIX);
        for (Map.Entry<String, Object> entry : shipRCodeEndMap.entrySet()) {
            // if month < 6
            Date value = (Date) entry.getValue();
            if (startAft.after(value)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

}
