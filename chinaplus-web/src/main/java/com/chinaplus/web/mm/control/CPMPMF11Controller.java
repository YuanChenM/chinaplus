/**
 * Controller of Parts Master upload
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.entity.TnmExpPart;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.exception.UploadException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;
import com.chinaplus.web.mm.service.CPMPMF11AisinService;
import com.chinaplus.web.mm.service.CPMPMF11CommonService;
import com.chinaplus.web.mm.service.CPMPMF11DBService;
import com.chinaplus.web.mm.service.CPMPMF11StyleService;
import com.chinaplus.web.mm.service.CPMPMF11VVService;
import com.chinaplus.web.mm.service.CPMSRF11Service;

/**
 * CPMPMF11Controller.
 */
@Controller
public class CPMPMF11Controller extends BaseFileController {

    /** session key */
    private static final String FILE_UPLOAD_SESSION_KEY = "sessionKey";

    /** VV_PARTS_MASTER */
    private static final String VV_PARTS_MASTER = "V-V_Parts_Master";

    /** AISIN_PARTS_MASTER */
    private static final String AISIN_PARTS_MASTER = "AISIN_Parts_Master";

    /**
     * cpmpmf11Service.
     */
    @Autowired
    private CPMPMF11DBService cpmpmf11Service;

    /**
     * cpmpmf11StyleService.
     */
    @Autowired
    private CPMPMF11CommonService cpmpmf11CommonService;

    /**
     * cpmpmf11VVService.
     */
    @Autowired
    private CPMPMF11VVService cpmpmf11VVService;

    /**
     * cpmpmf11AisinService.
     */
    @Autowired
    private CPMPMF11AisinService cpmpmf11AisinService;

    /**
     * cpmpmf11AisinService.
     */
    @Autowired
    private CPMPMF11StyleService cpmpmf11StyleService;

    /**
     * cpmpmf11DBService.
     */
    @Autowired
    private CPMPMF11DBService cpmpmf11DBService;

    /**
     * cpmsrf11Service.
     */
    @Autowired
    private CPMSRF11Service cpmsrf11Service;

    @Override
    protected String getFileId() {
        return FileId.CPMPMF11;
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
     * @throws UploadException UploadException
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> List<BaseMessage> doExcelUploadProcess(MultipartFile file, Workbook workbook,
        T param, HttpServletRequest request) throws UploadException {
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        List<CPMPMF11Entity> dataList = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> notRequiredPartsList = new ArrayList<CPMPMF11Entity>();
        List<Integer> idLists = new ArrayList<Integer>();

        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            // get work sheet sheet
            Sheet sheetVV = workbook.getSheet(VV_PARTS_MASTER);
            Sheet sheetAisin = workbook.getSheet(AISIN_PARTS_MASTER);
            boolean flagVV = true;
            boolean flagAisin = true;
            if (sheetVV != null) {
                flagVV = cpmpmf11StyleService.checkStatic(sheetVV, BusinessPattern.V_V);
            }
            if (sheetAisin != null) {
                flagAisin = cpmpmf11StyleService.checkStatic(sheetAisin, BusinessPattern.AISIN);
            }

            if (!flagVV || !flagAisin || (sheetVV == null && sheetAisin == null)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1014);
                throw new BusinessException(message);
            }

            Map<String, Object> dataMaps = new HashMap<String, Object>();
            UserManager um = UserManager.getLocalInstance(context);
            UserInfo userInfo = um.getUserInfo();
            Integer userId = userInfo.getUserId();
            Integer lang = userInfo.getLanguage().getCode();
            Locale langs = userInfo.getLanguage().getLocale();// MessageManager.getLanguage(lang).getLocale();
            dataMaps.put("lang", lang);
            dataMaps.put("langs", langs);
            boolean allVVFlag = um.getAllVVFlag();
            boolean allAisinFlag = um.getAllAisinFlag();
            dataMaps.put("allVVFlag", allVVFlag);
            dataMaps.put("allAisinFlag", allAisinFlag);
            dataMaps.put("userId", userId);

            List<BaseMessage> messageListsVV = new ArrayList<BaseMessage>();
            List<BaseMessage> messageListsAisin = new ArrayList<BaseMessage>();
            boolean typeVVFlag = false;
            boolean typeAisinFlag = false;

            Map<String, Object> mapsVV = new HashMap<String, Object>();
            Map<String, Object> mapsAisin = new HashMap<String, Object>();

            if (sheetVV != null) {
                mapsVV = readDataXlsx(sheetVV, dataMaps);
                typeVVFlag = (boolean) mapsVV.get("typeFlag");
                messageListsVV = (List<BaseMessage>) mapsVV.get("messageLists");
            }
            if (sheetAisin != null) {
                mapsAisin = readDataXlsx(sheetAisin, dataMaps);
                typeAisinFlag = (boolean) mapsAisin.get("typeFlag");
                messageListsAisin = (List<BaseMessage>) mapsAisin.get("messageLists");
            }

            List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
            // check type all blank
            if (!typeVVFlag && !typeAisinFlag) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_005);
                messageLists.add(message);
                throw new BusinessException(messageLists);
            }
            if (messageListsVV != null && messageListsVV.size() > 0) {
                messageLists.addAll(messageListsVV);
            }
            if (messageListsAisin != null && messageListsAisin.size() > 0) {
                messageLists.addAll(messageListsAisin);
            }
            if (messageLists != null && messageLists.size() > 0) {
                throw new BusinessException(messageLists);
            }

            // get BusinessPattern Map
            Map<String, Object> busPatternMap = cpmpmf11Service.getBusinessPatternMap(userInfo);
            // officeCode Map
            Map<String, String> officeCodeMap = (Map<String, String>) busPatternMap.get("officeCodeMap");
            // customerCode Map
            // Map<String, Integer> customerCodeMap = (Map<String, Integer>) busPatternMap.get("customerCodeMap");
            // officeCode + customerCode Map
            Map<String, String> officeCustCodeMap = (Map<String, String>) busPatternMap.get("officeCustCodeMap");
            dataMaps.put("officeCodeMap", officeCodeMap);
            // dataMaps.put("customerCodeMap", customerCodeMap);
            dataMaps.put("officeCustCodeMap", officeCustCodeMap);

            Map<String, Object> maps = dealDataDetail(dataMaps, mapsVV, mapsAisin, request, param);
            dataList = (List<CPMPMF11Entity>) maps.get("rusultDataList");
            idLists = (List<Integer>) maps.get("idLists");
            notRequiredPartsList = (List<CPMPMF11Entity>) maps.get("notRequiredPartsList");
        }
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(param.getUploadProcess())) {
            // get file content from session.
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(param.getUploadProcess())) {
                String mapKey = param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString();
                dataList = (List<CPMPMF11Entity>) context.get(mapKey);
                idLists = (List<Integer>) context.get(mapKey + "idLists");
                notRequiredPartsList = (List<CPMPMF11Entity>) context.get(mapKey + "_notRequiredPartsList");
                context.remove(mapKey);
            }
            // deal TNM_SUPPLIER , TNM_WAREHOUSE
            List<CPMPMF11Entity> swDataList = cpmpmf11Service.doSupplierWarehouse(dataList);

            // deal new and mod data
            try {
                cpmpmf11Service.doNewAndModData(swDataList, idLists, notRequiredPartsList);
            } catch (DuplicateKeyException de) {
                throw new BusinessException(MessageCodeConst.W1022);
            }
        }
        return new ArrayList<BaseMessage>();
    }

    /**
     * read Data Xlsx
     * 
     * @param sheet sheet
     * @param dataMaps dataMaps
     * @return maps maps
     */
    private Map<String, Object> readDataXlsx(Sheet sheet, Map<String, Object> dataMaps) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<CPMPMF11Entity> dataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> newDataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> modDataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> dataListAisin = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> newDataListAisin = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> modDataListAisin = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> notRequiredPartsList = new ArrayList<CPMPMF11Entity>();

        Integer lang = (Integer) dataMaps.get("lang");
        boolean allVVFlag = (boolean) dataMaps.get("allVVFlag");
        boolean allAisinFlag = (boolean) dataMaps.get("allAisinFlag");
        String[] busPatternList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN);
        String busPatternV = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN,
            BusinessPattern.V_V);
        String busPatternA = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN,
            BusinessPattern.AISIN);
        String buildOutIndicatorY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUILD_OUT_INDICATOR,
            IntDef.INT_ONE);
        // shiyang add start
        String inventoryByBoxN = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX,
            IntDef.INT_ZERO);
        String inventoryByBoxY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX,
            IntDef.INT_ONE);
        List<String> ttcPartNoListVV = new ArrayList<String>();
        List<String> ttcPartNoListAisin = new ArrayList<String>();
        // shiyang add end

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        // read excel
        String sheetName = sheet.getSheetName();
        String legend = MessageManager.getMessage("CPMSRF02_Grid_Legend", Language.CHINESE.getLocale());
        boolean typeFlag = false;
        int READ_START_COL = IntDef.INT_ONE;
        int READ_TOTAL_COL = IntDef.INT_SIXTYEIGHT;
        for (int startRow = IntDef.INT_NINE; startRow <= (sheet.getLastRowNum() + IntDef.INT_TEN); startRow++) {
            if (!ValidatorUtils.isBlankRow(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                String rowNum = StringUtil.toSafeString(startRow);
                // check type
                int startCol = IntDef.INT_ONE;
                String type = StringUtil.toSafeString(PoiUtil.getStringCellValue(sheet, startRow, startCol));
                if (!ValidatorUtils.requiredValidator(type)) {
                    continue;
                } else if (legend.equals(type)) {
                    break;
                }
                typeFlag = true;
                if (!type.equals(ShipType.NEW) && !type.equals(ShipType.MOD)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_008);
                    message.setMessageArgs(new String[] { rowNum, sheetName });
                    messageLists.add(message);
                    continue;
                }

                CPMPMF11Entity entity = new CPMPMF11Entity();
                // set businessPattern
                Integer valueb = null;
                String businessPattern = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_THIRTY);
                if (!ValidatorUtils.requiredValidator(businessPattern)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_BusinessPattern" });
                    messageLists.add(message);
                } else {
                    List<BaseMessage> busPatternMsgs = cpmpmf11CommonService.checkCodeCategory(businessPattern,
                        busPatternList, "CPMPMF01_Grid_BusinessPattern", rowNum, sheetName);
                    if (busPatternMsgs != null && busPatternMsgs.size() > 0) {
                        messageLists.addAll(busPatternMsgs);
                    } else if (busPatternV.equals(businessPattern) && !allVVFlag) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                        message.setMessageArgs(new String[] { rowNum, sheetName, busPatternV,
                            "CPMPMF01_Grid_PartsMasterData" });
                        messageLists.add(message);
                    } else if (busPatternA.equals(businessPattern) && !allAisinFlag) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                        message.setMessageArgs(new String[] { rowNum, sheetName, busPatternA,
                            "CPMPMF01_Grid_PartsMasterData" });
                        messageLists.add(message);
                    } else {
                        valueb = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.BUSINESS_PATTERN,
                            businessPattern);
                        entity.setBusinessPattern(valueb + "");
                    }
                }

                // 1-10
                // set type
                entity.setType(type);

                // set ttcPartsNo
                startCol += IntDef.INT_TWO;
                String ttcPartsNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> ttcPartsNoMsgs = cpmpmf11StyleService.checkAlphameric(ttcPartsNo,
                    IntDef.INT_EIGHTEEN, "CPMPMF01_Grid_TTCPN", rowNum, sheetName);
                if (ttcPartsNoMsgs != null && ttcPartsNoMsgs.size() > 0) {
                    messageLists.addAll(ttcPartsNoMsgs);
                } else {
                    entity.setTtcPartsNo(ttcPartsNo);
                }

                // add by liu_yc start
                if (null != valueb && BusinessPattern.V_V == valueb && entity.getTtcPartsNo() != null) {
                    // check
                    boolean isEffectParts = true;

                    // / do exp customer code
                    String expCustCode = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_EIGHTEEN);
                    List<BaseMessage> messges = cpmpmf11StyleService.checkAlphameric(expCustCode, IntDef.INT_TEN,
                        "CPMPMF01_Grid_CustCdinSSMSORKANB", rowNum, sheetName);
                    if (!messges.isEmpty()) {
                        isEffectParts = false;
                    } else {
                        entity.setExpCustCode(expCustCode);
                    }

                    // set ssmsMainRoute
                    String ssmsMainRoute = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_TEN);
                    messges = cpmpmf11StyleService.checkAlphameric(ssmsMainRoute, IntDef.INT_TWO,
                        "CPMPMF01_Grid_SSMSMainRoute", rowNum, sheetName);
                    if (!messges.isEmpty()) {
                        isEffectParts = false;
                    } else {
                        entity.setSsmsMainRoute(ssmsMainRoute);
                    }

                    // set expSuppCode
                    String expSuppCode = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_TWELVE);
                    messges = cpmpmf11StyleService.checkAlphameric(expSuppCode, IntDef.INT_TWENTY,
                        "CPMPMF01_Grid_SSMSSuppCd", rowNum, sheetName);
                    if (!messges.isEmpty()) {
                        isEffectParts = false;
                    } else {
                        entity.setExpSuppCode(expSuppCode);
                    }

                    // check is not required
                    String partsStatus = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_SIXTYSEVEN);
                    if (!ValidatorUtils.requiredValidator(partsStatus)) {
                        isEffectParts = false;
                    } else {
                        entity.setPartsStatus(partsStatus);
                    }

                    // add for UAT issue 19,20
                    String partsStatusM = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS,
                        PartsStatus.NOT_REQUIRED);
                    String discontinueIndicatorY = CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.DISCONTINUE_INDICATOR, IntDef.INT_ONE);
                    String discontinueIndicator = PoiUtil.getStringCellValue(sheet, startRow, IntDef.INT_SIXTYEIGHT);
                    if (partsStatusM.equals(partsStatus) && !discontinueIndicatorY.equals(discontinueIndicator)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator",
                            discontinueIndicatorY });
                        messageLists.add(message);
                    }
                    // add end

                    // if not required, will not check
                    if (isEffectParts && partsStatus.equals(partsStatusM)) {
                        // check status of current exp parts
                        TnmExpPart param = new TnmExpPart();
                        param.setTtcPartsNo(ttcPartsNo);
                        param.setSsmsCustCode(expCustCode);
                        param.setSsmsMainRoute(ssmsMainRoute);
                        param.setExpSuppCode(expSuppCode);
                        // find
                        TnmExpPart expParts = cpmpmf11Service.getOneByEntity(param);

                        // do check
                        if (expParts != null && expParts.getPartsId() == null) {
                            // set flag
                            entity.setPartsStatusFlag(true);
                            // set update by
                            entity.setExpPartsId(expParts.getExpPartsId());
                            entity.setUpdatedBy(StringUtil.toInteger(dataMaps.get("userId")));

                            // add all data
                            notRequiredPartsList.add(entity);
                            continue;
                        }
                    }
                }
                // add by liu_yc end

                // set ttcPartsName
                startCol++;
                String ttcPartsName = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> ttcPartsNameMsgs = cpmpmf11StyleService.checkAlphameric(ttcPartsName,
                    IntDef.INT_HUNDRED, "CPMPMF01_Grid_PartsDescriptionEn", rowNum, sheetName);
                if (ttcPartsNameMsgs != null && ttcPartsNameMsgs.size() > 0) {
                    messageLists.addAll(ttcPartsNameMsgs);
                } else {
                    entity.setTtcPartsName(ttcPartsName);
                }

                // set partsNameCn
                startCol++;
                String partsNameCn = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> partsNameCnMsgs = cpmpmf11StyleService.checkAlphameric(partsNameCn,
                    IntDef.INT_HUNDRED, "CPMPMF01_Grid_PartsDescriptionCh", rowNum, sheetName);
                if (partsNameCnMsgs != null && partsNameCnMsgs.size() > 0) {
                    messageLists.addAll(partsNameCnMsgs);
                } else {
                    entity.setPartsNameCn(partsNameCn);
                }

                // set oldTtcPartsNo
                startCol++;
                String oldTtcPartsNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(oldTtcPartsNo)) {
                    List<BaseMessage> oldTtcPartsNoMsgs = cpmpmf11StyleService.checkAlphamericIsNeed(oldTtcPartsNo,
                        IntDef.INT_EIGHTEEN, "CPMPMF01_Grid_OldTTCPN", rowNum, sheetName);
                    if (oldTtcPartsNoMsgs != null && oldTtcPartsNoMsgs.size() > 0) {
                        messageLists.addAll(oldTtcPartsNoMsgs);
                    } else {
                        entity.setOldTtcPartsNo(oldTtcPartsNo);
                    }
                }

                // set expUomCode
                startCol++;
                String expUomCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> expUomCodeMsgs = cpmpmf11StyleService.checkAlphameric(expUomCode, IntDef.INT_TEN,
                    "CPMPMF01_Grid_UOM", rowNum, sheetName);
                if (expUomCodeMsgs != null && expUomCodeMsgs.size() > 0) {
                    messageLists.addAll(expUomCodeMsgs);
                } else {
                    entity.setExpUomCode(expUomCode);
                }

                // set expRegion
                startCol++;
                String expRegion = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> expRegionMsgs = cpmpmf11StyleService.checkAlphameric(expRegion, IntDef.INT_TWO,
                    "CPMPMF01_Grid_ExportCountry", rowNum, sheetName);
                if (expRegionMsgs != null && expRegionMsgs.size() > 0) {
                    messageLists.addAll(expRegionMsgs);
                } else {
                    entity.setExpRegion(expRegion);
                }

                // set ttcSuppCode
                startCol++;
                String ttcSuppCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> ttcSuppCodeMsgs = cpmpmf11StyleService.checkAlphameric(ttcSuppCode,
                    IntDef.INT_FIFTEEN, "CPMPMF01_Grid_TTCSuppCd", rowNum, sheetName);
                if (ttcSuppCodeMsgs != null && ttcSuppCodeMsgs.size() > 0) {
                    messageLists.addAll(ttcSuppCodeMsgs);
                } else {
                    entity.setTtcSuppCode(ttcSuppCode);
                }

                if (null != valueb && BusinessPattern.V_V == valueb) {
                    // set ssmsMainRoute
                    startCol++;
                    String ssmsMainRoute = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> ssmsMainRouteMsgs = cpmpmf11StyleService.checkAlphameric(ssmsMainRoute,
                        IntDef.INT_TWO, "CPMPMF01_Grid_SSMSMainRoute", rowNum, sheetName);
                    if (ssmsMainRouteMsgs != null && ssmsMainRouteMsgs.size() > 0) {
                        messageLists.addAll(ssmsMainRouteMsgs);
                    } else {
                        entity.setSsmsMainRoute(ssmsMainRoute);
                    }
                    // 11-20
                    // set ssmsVendorRoute
                    startCol++;
                    String ssmsVendorRoute = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> ssmsVendorRouteMsgs = cpmpmf11StyleService.checkAlphameric(ssmsVendorRoute,
                        IntDef.INT_TWO, "CPMPMF01_Grid_SSMSVendorRoute", rowNum, sheetName);
                    if (ssmsVendorRouteMsgs != null && ssmsVendorRouteMsgs.size() > 0) {
                        messageLists.addAll(ssmsVendorRouteMsgs);
                    } else {
                        entity.setSsmsVendorRoute(ssmsVendorRoute);
                    }
                    // set expSuppCode
                    startCol++;
                    String expSuppCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> expSuppCodeMsgs = cpmpmf11StyleService.checkAlphameric(expSuppCode,
                        IntDef.INT_TWENTY, "CPMPMF01_Grid_SSMSSuppCd", rowNum, sheetName);
                    if (expSuppCodeMsgs != null && expSuppCodeMsgs.size() > 0) {
                        messageLists.addAll(expSuppCodeMsgs);
                    } else {
                        entity.setExpSuppCode(expSuppCode);
                    }
                } else {
                    startCol += IntDef.INT_THREE;
                }

                // set supplierName
                startCol++;
                String supplierName = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> supplierNameMsgs = cpmpmf11StyleService.checkAlphameric(supplierName,
                    IntDef.INT_HUNDRED, "CPMPMF01_Grid_SuppName", rowNum, sheetName);
                if (supplierNameMsgs != null && supplierNameMsgs.size() > 0) {
                    messageLists.addAll(supplierNameMsgs);
                } else {
                    entity.setSupplierName(supplierName);
                }

                // set suppPartsNo
                startCol++;
                String suppPartsNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> suppPartsNoMsgs = cpmpmf11StyleService.checkAlphameric(suppPartsNo,
                    IntDef.INT_THIRTY, "CPMPMF01_Grid_SuppPN", rowNum, sheetName);
                if (suppPartsNoMsgs != null && suppPartsNoMsgs.size() > 0) {
                    messageLists.addAll(suppPartsNoMsgs);
                } else {
                    entity.setSuppPartsNo(suppPartsNo);
                }

                // set impRegion
                startCol++;
                String impRegion = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> impRegionMsgs = cpmpmf11StyleService.checkAlphameric(impRegion, IntDef.INT_TWO,
                    "CPMPMF01_Grid_ImpCountry", rowNum, sheetName);
                if (impRegionMsgs != null && impRegionMsgs.size() > 0) {
                    messageLists.addAll(impRegionMsgs);
                } else {
                    entity.setImpRegion(impRegion);
                }

                // set officeCode
                startCol++;
                String officeCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(officeCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ImpOfficeCd" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(officeCode, IntDef.INT_TEN)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ImpOfficeCd",
                        StringUtil.toSafeString(IntDef.INT_TEN) });
                    messageLists.add(message);
                } else {
                    entity.setOfficeCode(officeCode);
                }

                // set customerCode
                startCol++;
                String customerCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> customerCodeMsgs = cpmpmf11StyleService.checkAlphameric(customerCode,
                    IntDef.INT_FIFTEEN, "CPMPMF01_Grid_TTCCustCd", rowNum, sheetName);
                if (customerCodeMsgs != null && customerCodeMsgs.size() > 0) {
                    messageLists.addAll(customerCodeMsgs);
                } else {
                    entity.setCustomerCode(customerCode);
                }

                // set expCustCode
                startCol++;
                String expCustCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> expCustCodeMsgs = cpmpmf11StyleService.checkAlphameric(expCustCode, IntDef.INT_TEN,
                    "CPMPMF01_Grid_CustCdinSSMSORKANB", rowNum, sheetName);
                if (expCustCodeMsgs != null && expCustCodeMsgs.size() > 0) {
                    messageLists.addAll(expCustCodeMsgs);
                } else {
                    entity.setExpCustCode(expCustCode);
                }

                // set customerName
                startCol++;
                String customerName = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> customerNameMsgs = cpmpmf11StyleService.checkAlphameric(customerName,
                    IntDef.INT_HUNDRED, "CPMPMF01_Grid_CustName", rowNum, sheetName);
                if (customerNameMsgs != null && customerNameMsgs.size() > 0) {
                    messageLists.addAll(customerNameMsgs);
                } else {
                    entity.setCustomerName(customerName);
                }

                // set custPartsNo
                startCol++;
                String custPartsNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> custPartsNoMsgs = cpmpmf11StyleService.checkAlphameric(custPartsNo,
                    IntDef.INT_THIRTY, "CPMPMF01_Grid_CustPN", rowNum, sheetName);
                if (custPartsNoMsgs != null && custPartsNoMsgs.size() > 0) {
                    messageLists.addAll(custPartsNoMsgs);
                } else {
                    entity.setCustPartsNo(custPartsNo);
                }

                // 21-30
                // set custBackNo
                startCol++;
                String custBackNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(custBackNo)) {
                    List<BaseMessage> custBackNoMsgs = cpmpmf11StyleService.checkAlphamericIsNeed(custBackNo,
                        IntDef.INT_THIRTY, "CPMPMF01_Grid_CustBackNo", rowNum, sheetName);
                    if (custBackNoMsgs != null && custBackNoMsgs.size() > 0) {
                        messageLists.addAll(custBackNoMsgs);
                    } else {
                        entity.setCustBackNo(custBackNo);
                    }
                }

                startCol++;
                if (null != valueb && BusinessPattern.AISIN == valueb) {
                    // set invCustCode
                    String invCustCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (!ValidatorUtils.requiredValidator(invCustCode)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_MailInvCustCd" });
                        messageLists.add(message);
                    } else {
                        String[] strs = invCustCode.split(StringConst.COMMA);
                        // shiyang mod start
                        // List<Integer> intList = new ArrayList<Integer>();
                        // boolean vdNumericFlag = true;
                        // boolean repFlag = true;
                        // for (int j = 0; j < strs.length; j++) {
                        // String str = strs[j];
                        // if (!StringUtil.isNumeric(str)) {
                        // vdNumericFlag = false;
                        // break;
                        // }
                        // Integer strInt = Integer.valueOf(str);
                        // for (Integer inte : intList) {
                        // if (inte == strInt) {
                        // repFlag = false;
                        // break;
                        // }
                        // }
                        // intList.add(strInt);
                        // }
                        // if (!vdNumericFlag) {
                        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
                        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_MailInvCustCd",
                        // "CPMSRF11_Grid_Integer" });
                        // messageLists.add(message);
                        // } else if (!repFlag) {
                        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_156);
                        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_MailInvCustCd",
                        // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                        // messageLists.add(message);
                        // } else {
                        // entity.setInvCustCode(invCustCode);
                        // }
                        boolean isNumChar = true;
                        boolean isNotDuplicate = true;
                        List<String> valByCommaList = new ArrayList<String>();
                        for (String valByComma : strs) {
                            // Is number or char
                            if (!valByComma.matches("^[\\da-zA-Z]*$")) {
                                isNumChar = isNumChar && false;
                            }
                            // Is duplicate
                            if (valByCommaList.contains(valByComma)) {
                                isNotDuplicate = isNotDuplicate && false;
                            } else {
                                valByCommaList.add(valByComma);
                            }
                        }
                        if (!isNumChar) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
                            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_MailInvCustCd",
                                "CPMSRF11_Grid_IntegerChar" });
                            messageLists.add(message);
                        }
                        if (!isNotDuplicate) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_156);
                            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_MailInvCustCd",
                                "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                            messageLists.add(message);
                        }
                        entity.setInvCustCode(invCustCode);
                        // shiyang mod end
                    }
                }

                // set impWhsCode
                startCol++;
                String impWhsCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> impWhsCodeMsgs = cpmpmf11StyleService.checkAlphameric(impWhsCode, IntDef.INT_TWENTY,
                    "CPMPMF01_Grid_TTCImpWHCd", rowNum, sheetName);
                if (impWhsCodeMsgs != null && impWhsCodeMsgs.size() > 0) {
                    messageLists.addAll(impWhsCodeMsgs);
                } else {
                    entity.setImpWhsCode(impWhsCode);
                }

                if (null != valueb && BusinessPattern.V_V == valueb) {
                    // set westCustCode
                    startCol++;
                    String westCustCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> westCustCodeMsgs = cpmpmf11StyleService.checkAlphameric(westCustCode,
                        IntDef.INT_TWENTY, "CPMPMF01_Grid_WESTCustCd", rowNum, sheetName);
                    if (westCustCodeMsgs != null && westCustCodeMsgs.size() > 0) {
                        messageLists.addAll(westCustCodeMsgs);
                    } else {
                        entity.setWestCustCode(westCustCode);
                    }
                    // set westPartsNo
                    startCol++;
                    String westPartsNo = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> westPartsNoMsgs = cpmpmf11StyleService.checkAlphameric(westPartsNo,
                        IntDef.INT_THIRTY, "CPMPMF01_Grid_WESTPN", rowNum, sheetName);
                    if (westPartsNoMsgs != null && westPartsNoMsgs.size() > 0) {
                        messageLists.addAll(westPartsNoMsgs);
                    } else {
                        entity.setWestPartsNo(westPartsNo);
                    }
                } else {
                    startCol += IntDef.INT_TWO;
                }

                // set orderLot
                startCol++;
                String orderLot = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> orderLotMsgs = cpmpmf11StyleService.checkNumber(orderLot, "CPMPMF01_Grid_OrderLot",
                    rowNum, sheetName);
                if (orderLotMsgs != null && orderLotMsgs.size() > 0) {
                    messageLists.addAll(orderLotMsgs);
                } else {
                    entity.setOrderLot(new BigDecimal(orderLot));
                }

                // set srbq
                startCol++;
                String srbq = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> srbqMsgs = cpmpmf11StyleService.checkNumber(srbq, "CPMPMF01_Grid_SRBQ", rowNum,
                    sheetName);
                if (srbqMsgs != null && srbqMsgs.size() > 0) {
                    messageLists.addAll(srbqMsgs);
                } else {
                    entity.setSrbq(new BigDecimal(srbq));
                }

                // set spq
                startCol++;
                String spq = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> spqMsgs = cpmpmf11StyleService.checkNumber(spq, "CPMPMF01_Grid_SPQ", rowNum,
                    sheetName);
                if (spqMsgs != null && spqMsgs.size() > 0) {
                    messageLists.addAll(spqMsgs);
                } else {
                    entity.setSpq(new BigDecimal(spq));
                }

                // set spqM3
                startCol++;
                String spqM3 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(spqM3)) {
                    List<BaseMessage> spqM3Msgs = cpmpmf11StyleService.checkNumberIsNeed(spqM3,
                        "CPMPMF01_Grid_M3perbox", rowNum, sheetName);
                    if (spqM3Msgs != null && spqM3Msgs.size() > 0) {
                        messageLists.addAll(spqM3Msgs);
                    } else {
                        entity.setSpqM3(new BigDecimal(spqM3));
                    }
                }

                // 31-40
                // set businessType
                startCol += IntDef.INT_TWO;
                String businessType = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(businessType)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_BusinessType" });
                    messageLists.add(message);
                } else {
                    entity.setBusinessType(businessType);
                }
                // set partsType
                startCol++;
                String partsType = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(partsType)) {
                    entity.setPartsType(partsType);
                }
                // set carModel
                startCol++;
                String carModel = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(carModel)) {
                    List<BaseMessage> carModelMsgs = cpmpmf11StyleService.checkAlphamericIsNeed(carModel,
                        IntDef.INT_THIRTY, "CPMPMF01_Grid_CarModel", rowNum, sheetName);
                    if (carModelMsgs != null && carModelMsgs.size() > 0) {
                        messageLists.addAll(carModelMsgs);
                    } else {
                        entity.setCarModel(carModel);
                    }
                }

                // set orderDay
                startCol++;
                if (null != valueb && BusinessPattern.V_V == valueb) {
                    String orderDay = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (!ValidatorUtils.requiredValidator(orderDay)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime" });
                        messageLists.add(message);
                    } else if (!ValidatorUtils.maxLengthValidator(orderDay, IntDef.INT_FOUR)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
                            StringUtil.toSafeString(IntDef.INT_FOUR) });
                        messageLists.add(message);
                    } else {
                        entity.setOrderTime(orderDay);
                    }
                }

                // set targetMonth
                startCol++;
                String targetMonth = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> targetMonthMsgs = cpmpmf11StyleService.checkInteger(targetMonth, IntDef.INT_ONE,
                    "CPMPMF01_Grid_TargetMonth", rowNum, sheetName);
                if (targetMonthMsgs != null && targetMonthMsgs.size() > 0) {
                    messageLists.addAll(targetMonthMsgs);
                } else {
                    entity.setTargetMonth(Integer.valueOf(targetMonth));
                }

                // set forecastNum
                startCol++;
                String forecastNum = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> forecastNumMsgs = cpmpmf11StyleService.checkInteger(forecastNum, IntDef.INT_ONE,
                    "CPMPMF01_Grid_NoofOrderForeccast", rowNum, sheetName);
                if (forecastNumMsgs != null && forecastNumMsgs.size() > 0) {
                    messageLists.addAll(forecastNumMsgs);
                } else {
                    entity.setForecastNum(Integer.valueOf(forecastNum));
                }

                // set orderFcType
                startCol++;
                String orderFcType = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(orderFcType)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TypeofOrderForecast" });
                    messageLists.add(message);
                } else {
                    entity.setOrderFcType(orderFcType);
                }
                // set expCalendarCode
                startCol++;
                if (null != valueb && BusinessPattern.V_V == valueb) {
                    String expCalendarCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (!ValidatorUtils.requiredValidator(expCalendarCode)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportWHCalendar" });
                        messageLists.add(message);
                    } else {
                        entity.setExpCalendarCode(expCalendarCode);
                    }
                }
                // set osCustStockFlag
                startCol++;
                String osCustStockFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(osCustStockFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName,
                        "CPMPMF01_Grid_OrderSuggAlarm3includCustStockF" });
                    messageLists.add(message);
                } else {
                    entity.setOsCustStockFlag(osCustStockFlag);
                }
                // set saCustStockFlag
                startCol++;
                String saCustStockFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(saCustStockFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName,
                        "CPMPMF01_Grid_SSAlarm12RundownincludCustStockF" });
                    messageLists.add(message);
                } else {
                    entity.setSaCustStockFlag(saCustStockFlag);
                }
                // 41-50
                // set inventoryBoxFlag
                startCol++;
                String inventoryBoxFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(inventoryBoxFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_InventoryControlbyBox" });
                    messageLists.add(message);
                } else {
                    entity.setInventoryBoxFlag(inventoryBoxFlag);
                }

                // set minStock
                startCol++;
                String minStock = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                // shiyang mod start
                // entity.setMinStockS(minStock);
                if (inventoryByBoxN.equals(inventoryBoxFlag)) {
                    List<BaseMessage> minStockMsgs = cpmpmf11StyleService.checkInteger(minStock, IntDef.INT_THREE,
                        "CPMPMF01_Grid_MinStockDays", rowNum, sheetName);
                    if (minStockMsgs != null && minStockMsgs.size() > 0) {
                        messageLists.addAll(minStockMsgs);
                    } else {
                        entity.setMinStockS(minStock);
                        entity.setMinStock(DecimalUtil.getBigDecimal(minStock).intValue());
                    }
                }
                // shiyang mod end

                // set maxStock
                startCol++;
                String maxStock = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                // shiyang mod start
                // entity.setMaxStockS(maxStock);
                if (inventoryByBoxN.equals(inventoryBoxFlag)) {
                    List<BaseMessage> maxStockMsgs = cpmpmf11StyleService.checkInteger(maxStock, IntDef.INT_THREE,
                        "CPMPMF01_Grid_MaxStockDays", rowNum, sheetName);
                    if (maxStockMsgs != null && maxStockMsgs.size() > 0) {
                        messageLists.addAll(maxStockMsgs);
                    } else {
                        entity.setMaxStockS(maxStock);
                        entity.setMaxStock(DecimalUtil.getBigDecimal(maxStock).intValue());
                    }
                }
                // shiyang mod end

                // set minBox
                startCol++;
                String minBox = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                // shiyang mod start
                // entity.setMinBoxS(minBox);
                if (inventoryByBoxY.equals(inventoryBoxFlag)) {
                    List<BaseMessage> minBoxMsgs = cpmpmf11StyleService.checkInteger(minBox, IntDef.INT_THREE,
                        "CPMPMF01_Grid_MinNoOfBox", rowNum, sheetName);
                    if (minBoxMsgs != null && minBoxMsgs.size() > 0) {
                        messageLists.addAll(minBoxMsgs);
                    } else {
                        entity.setMinBoxS(minBox);
                        entity.setMinBox(DecimalUtil.getBigDecimal(minBox).intValue());
                    }
                }
                // shiyang mod end

                // set maxBox
                startCol++;
                String maxBox = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                // shiyang mod start
                // entity.setMaxBoxS(maxBox);
                if (inventoryByBoxY.equals(inventoryBoxFlag)) {
                    List<BaseMessage> maxBoxMsgs = cpmpmf11StyleService.checkInteger(maxBox, IntDef.INT_THREE,
                        "CPMPMF01_Grid_MaxNoOfBox", rowNum, sheetName);
                    if (maxBoxMsgs != null && maxBoxMsgs.size() > 0) {
                        messageLists.addAll(maxBoxMsgs);
                    } else {
                        entity.setMaxBoxS(maxBox);
                        entity.setMaxBox(DecimalUtil.getBigDecimal(maxBox).intValue());
                    }
                }
                // shiyang mod end

                // set orderSafetyStock
                startCol++;
                String orderSafetyStock = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> orderSafetyStockMsgs = cpmpmf11StyleService.checkInteger(orderSafetyStock,
                    IntDef.INT_THREE, "CPMPMF01_Grid_SafeStockDayForOrderSuggAlarm3", rowNum, sheetName);
                if (orderSafetyStockMsgs != null && orderSafetyStockMsgs.size() > 0) {
                    messageLists.addAll(orderSafetyStockMsgs);
                } else {
                    entity.setOrderSafetyStock(Integer.valueOf(orderSafetyStock));
                }

                // set rundownSafetyStock
                startCol++;
                String rundownSafetyStock = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> rundownSafetyStockMsgs = cpmpmf11StyleService.checkInteger(rundownSafetyStock,
                    IntDef.INT_THREE, "CPMPMF01_Grid_SafeStockDayForRundown", rowNum, sheetName);
                if (rundownSafetyStockMsgs != null && rundownSafetyStockMsgs.size() > 0) {
                    messageLists.addAll(rundownSafetyStockMsgs);
                } else {
                    entity.setRundownSafetyStock(Integer.valueOf(rundownSafetyStock));
                }

                // set outboundFluctuation
                startCol++;
                String outboundFluctuation = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                List<BaseMessage> outboundFluctuationMsgs = cpmpmf11StyleService.checkPercentage(outboundFluctuation,
                    "CPMPMF01_Grid_TTCImpWHObFlucPerc", rowNum, sheetName);
                if (outboundFluctuationMsgs != null && outboundFluctuationMsgs.size() > 0) {
                    messageLists.addAll(outboundFluctuationMsgs);
                } else {
                    if (outboundFluctuation.contains("%")) {
                        double outboundFluctuationd = Double.valueOf(outboundFluctuation.replace("%", ""))
                                / IntDef.INT_HUNDRED;
                        entity.setOutboundFluctuation(outboundFluctuationd);
                    } else {
                        double outboundFluctuationd = Double.valueOf(outboundFluctuation);
                        entity.setOutboundFluctuation(outboundFluctuationd);
                    }
                }

                // set simulationEndDatePattern
                startCol++;
                String simulationEndDatePattern = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(simulationEndDatePattern)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SimulationEndDatePatt" });
                    messageLists.add(message);
                } else {
                    entity.setSimulationEndDatePattern(simulationEndDatePattern);
                }

                // set shippingRouteCode
                startCol++;
                String shippingRouteCode = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(shippingRouteCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode" });
                    messageLists.add(message);
                } else if (!ValidatorUtils.maxLengthValidator(shippingRouteCode, IntDef.INT_THIRTY)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                        StringUtil.toSafeString(IntDef.INT_THIRTY) });
                    messageLists.add(message);
                } else {
                    entity.setShippingRouteCode(shippingRouteCode);
                }

                // 51-60
                // set delayAdjustmentPattern
                startCol++;
                String delayAdjustmentPattern = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(delayAdjustmentPattern)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OnShipDelayAdjustPatt" });
                    messageLists.add(message);
                } else {
                    entity.setDelayAdjustmentPattern(delayAdjustmentPattern);
                }

                if (null != valueb && BusinessPattern.V_V == valueb) {
                    // set airEtdLeadtime
                    startCol++;
                    String airEtdLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> airEtdLeadtimeMsgs = cpmpmf11StyleService.checkInteger(airEtdLeadtime,
                        IntDef.INT_THREE, "CPMPMF01_Grid_WorkLTfromExpIbdtoETDforAir", rowNum, sheetName);
                    if (airEtdLeadtimeMsgs != null && airEtdLeadtimeMsgs.size() > 0) {
                        messageLists.addAll(airEtdLeadtimeMsgs);
                    } else {
                        entity.setAirEtdLeadtime(Integer.valueOf(airEtdLeadtime));
                    }
                    // set airEtaLeadtime
                    startCol++;
                    String airEtaLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> airEtaLeadtimeMsgs = cpmpmf11StyleService.checkInteger(airEtaLeadtime,
                        IntDef.INT_THREE, "CPMPMF01_Grid_LTfromETDtoETAforAir", rowNum, sheetName);
                    if (airEtaLeadtimeMsgs != null && airEtaLeadtimeMsgs.size() > 0) {
                        messageLists.addAll(airEtaLeadtimeMsgs);
                    } else {
                        entity.setAirEtaLeadtime(Integer.valueOf(airEtaLeadtime));
                    }
                    // set airInboundLeadtime
                    startCol++;
                    String airInboundLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> airInboundLeadtimeMsgs = cpmpmf11StyleService.checkInteger(airInboundLeadtime,
                        IntDef.INT_THREE, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforAir", rowNum, sheetName);
                    if (airInboundLeadtimeMsgs != null && airInboundLeadtimeMsgs.size() > 0) {
                        messageLists.addAll(airInboundLeadtimeMsgs);
                    } else {
                        entity.setAirInboundLeadtime(Integer.valueOf(airInboundLeadtime));
                    }
                    // set seaEtaLeadtime
                    startCol++;
                    String seaEtaLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> seaEtaLeadtimeMsgs = cpmpmf11StyleService.checkInteger(seaEtaLeadtime,
                        IntDef.INT_THREE, "CPMPMF01_Grid_LTfromETDtoETAforSea", rowNum, sheetName);
                    if (seaEtaLeadtimeMsgs != null && seaEtaLeadtimeMsgs.size() > 0) {
                        messageLists.addAll(seaEtaLeadtimeMsgs);
                    } else {
                        entity.setSeaEtaLeadtime(Integer.valueOf(seaEtaLeadtime));
                    }
                    // set seaInboundLeadtime
                    startCol++;
                    String seaInboundLeadtime = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    List<BaseMessage> seaInboundLeadtimeMsgs = cpmpmf11StyleService.checkInteger(seaInboundLeadtime,
                        IntDef.INT_THREE, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforSea", rowNum, sheetName);
                    if (seaInboundLeadtimeMsgs != null && seaInboundLeadtimeMsgs.size() > 0) {
                        messageLists.addAll(seaInboundLeadtimeMsgs);
                    } else {
                        entity.setSeaInboundLeadtime(Integer.valueOf(seaInboundLeadtime));
                    }
                } else {
                    startCol += IntDef.INT_FIVE;
                }

                // set allocationFcType
                startCol++;
                String allocationFcType = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(allocationFcType)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName,
                        "CPMPMF01_Grid_DailyAllocTypeforMonthCustForecast" });
                    messageLists.add(message);
                } else {
                    entity.setAllocationFcType(allocationFcType);
                }

                // set cfcAdjustmentType1
                startCol++;
                String cfcAdjustmentType1 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(cfcAdjustmentType1)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName,
                        "CPMPMF01_Grid_CustForecastPAdjustPatternCase1" });
                    messageLists.add(message);
                } else {
                    entity.setCfcAdjustmentType1(cfcAdjustmentType1);
                }

                // set cfcAdjustmentType2
                startCol++;
                String cfcAdjustmentType2 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(cfcAdjustmentType2)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName,
                        "CPMPMF01_Grid_CustForecastPAdjustPatternCase2" });
                    messageLists.add(message);
                } else {
                    entity.setCfcAdjustmentType2(cfcAdjustmentType2);
                }

                // set remark1
                startCol++;
                String remark1 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(remark1)) {
                    if (!ValidatorUtils.maxLengthValidator(remark1, IntDef.INT_EIGHTY)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartRemark1",
                            StringUtil.toSafeString(IntDef.INT_EIGHTY) });
                        messageLists.add(message);
                    } else {
                        entity.setRemark1(remark1);
                    }
                }

                // 61-68
                // set remark2
                startCol++;
                String remark2 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(remark2)) {
                    if (!ValidatorUtils.maxLengthValidator(remark2, IntDef.INT_EIGHTY)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartRemark2",
                            StringUtil.toSafeString(IntDef.INT_EIGHTY) });
                        messageLists.add(message);
                    } else {
                        entity.setRemark2(remark2);
                    }
                }

                // set remark3
                startCol++;
                String remark3 = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (ValidatorUtils.requiredValidator(remark3)) {
                    if (!ValidatorUtils.maxLengthValidator(remark3, IntDef.INT_EIGHTY)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_144);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartRemark3",
                            StringUtil.toSafeString(IntDef.INT_EIGHTY) });
                        messageLists.add(message);
                    } else {
                        entity.setRemark3(remark3);
                    }
                }

                // set buildoutFlag
                startCol++;
                String buildoutFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(buildoutFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_BuildOutIndicator" });
                    messageLists.add(message);
                } else {
                    entity.setBuildoutFlag(buildoutFlag);
                }

                if (buildOutIndicatorY.equals(buildoutFlag)) {
                    // set buildoutMonth
                    startCol++;
                    String buildoutMonth = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (ValidatorUtils.requiredValidator(buildoutMonth)) {
                        Date buildoutMonthDate = DateTimeUtil.parseMonth(buildoutMonth);
                        List<BaseMessage> buildoutMonthMsgs = cpmpmf11StyleService.checkDate(buildoutMonthDate,
                            "CPMPMF01_Grid_BuildOutMonth", rowNum, sheetName);
                        if (buildoutMonthMsgs != null && buildoutMonthMsgs.size() > 0) {
                            messageLists.addAll(buildoutMonthMsgs);
                        } else {
                            entity.setBuildoutMonth(sdf.format(buildoutMonthDate));
                        }
                    }

                    // set lastPoMonth
                    startCol++;
                    String lastPoMonth = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (ValidatorUtils.requiredValidator(lastPoMonth)) {
                        Date lastPoMonthDate = DateTimeUtil.parseMonth(lastPoMonth);
                        List<BaseMessage> lastPoMonthMsgs = cpmpmf11StyleService.checkDate(lastPoMonthDate,
                            "CPMPMF01_Grid_LastPOMonth", rowNum, sheetName);
                        if (lastPoMonthMsgs != null && lastPoMonthMsgs.size() > 0) {
                            messageLists.addAll(lastPoMonthMsgs);
                        } else {
                            entity.setLastPoMonth(sdf.format(lastPoMonthDate));
                        }
                    }

                    // set lastDeliveryMonth
                    startCol++;
                    String lastDeliveryMonth = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (ValidatorUtils.requiredValidator(lastDeliveryMonth)) {
                        Date lastDeliveryMonthDate = DateTimeUtil.parseMonth(lastDeliveryMonth);
                        List<BaseMessage> lastDeliveryMonthMsgs = cpmpmf11StyleService.checkDate(lastDeliveryMonthDate,
                            "CPMPMF01_Grid_LastSupplierDeliveryMonth", rowNum, sheetName);
                        if (lastDeliveryMonthMsgs != null && lastDeliveryMonthMsgs.size() > 0) {
                            messageLists.addAll(lastDeliveryMonthMsgs);
                        } else {
                            entity.setLastDeliveryMonth(sdf.format(lastDeliveryMonthDate));
                        }
                    }
                } else {
                    startCol += IntDef.INT_THREE;
                }

                // set partsStatus
                startCol++;
                if (null != valueb && BusinessPattern.V_V == valueb) {
                    String partsStatus = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                    if (!ValidatorUtils.requiredValidator(partsStatus)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartStatus" });
                        messageLists.add(message);
                    } else {
                        entity.setPartsStatus(partsStatus);
                    }
                }

                // set inactiveFlag
                startCol++;
                String inactiveFlag = PoiUtil.getStringCellValue(sheet, startRow, startCol);
                if (!ValidatorUtils.requiredValidator(inactiveFlag)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_020);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator" });
                    messageLists.add(message);
                } else {
                    entity.setInactiveFlag(inactiveFlag);
                }

                // set row number
                entity.setRowNum(startRow);
                // set sheet name
                entity.setSheetName(sheetName);

                if (null != valueb && BusinessPattern.V_V == valueb) {
                    // add new data
                    if (type.equals(ShipType.NEW)) {
                        newDataListVV.add(entity);
                    }
                    // add mod data
                    else if (type.equals(ShipType.MOD)) {
                        modDataListVV.add(entity);
                    }
                    // add all data
                    dataListVV.add(entity);

                    // shiyang add start
                    ttcPartNoListVV.add(ttcPartsNo);
                    // shiyang add end
                } else if (null != valueb && BusinessPattern.AISIN == valueb) {
                    // add new data
                    if (type.equals(ShipType.NEW)) {
                        newDataListAisin.add(entity);
                    }
                    // add mod data
                    else if (type.equals(ShipType.MOD)) {
                        modDataListAisin.add(entity);
                    }
                    // add all data
                    dataListAisin.add(entity);

                    // shiyang add start
                    ttcPartNoListAisin.add(ttcPartsNo);
                    // shiyang add end
                }
            } else {
                if (ValidatorUtils.isExcelEnd(sheet, startRow, READ_START_COL, READ_TOTAL_COL)) {
                    break;
                }
            }
        }

        Map<String, Object> resultMaps = new HashMap<String, Object>();
        resultMaps.put("dataListVV", dataListVV);
        resultMaps.put("newDataListVV", newDataListVV);
        resultMaps.put("modDataListVV", modDataListVV);
        resultMaps.put("dataListAisin", dataListAisin);
        resultMaps.put("newDataListAisin", newDataListAisin);
        resultMaps.put("modDataListAisin", modDataListAisin);
        resultMaps.put("notRequiredPartsList", notRequiredPartsList);
        resultMaps.put("messageLists", messageLists);
        resultMaps.put("typeFlag", typeFlag);
        // shiyang add start
        resultMaps.put("ttcPartNoListVV", ttcPartNoListVV);
        resultMaps.put("ttcPartNoListAisin", ttcPartNoListAisin);
        // shiyang add end
        return resultMaps;
    }

    /**
     * deal data detail
     * 
     * @param dataMaps dataMaps
     * @param mapsVV mapsVV
     * @param mapsAisin mapsAisin
     * @param request request
     * @param param param
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> dealDataDetail(Map<String, Object> dataMaps, Map<String, Object> mapsVV,
        Map<String, Object> mapsAisin, HttpServletRequest request, BaseParam param) {

        List<CPMPMF11Entity> dataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> newDataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> modDataListVV = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> dataListAisin = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> newDataListAisin = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> modDataListAisin = new ArrayList<CPMPMF11Entity>();
        // shiyang add start
        List<String> ttcPartNoListVv = new ArrayList<String>();
        List<String> ttcPartNoListAisin = new ArrayList<String>();
        // shiyang add end

        // vv
        List<CPMPMF11Entity> dataListVVMV = (List<CPMPMF11Entity>) mapsVV.get("dataListVV");
        List<CPMPMF11Entity> newDataListVVMV = (List<CPMPMF11Entity>) mapsVV.get("newDataListVV");
        List<CPMPMF11Entity> modDataListVVMV = (List<CPMPMF11Entity>) mapsVV.get("modDataListVV");
        List<CPMPMF11Entity> dataListAisinMV = (List<CPMPMF11Entity>) mapsVV.get("dataListAisin");
        List<CPMPMF11Entity> newDataListAisinMV = (List<CPMPMF11Entity>) mapsVV.get("newDataListAisin");
        List<CPMPMF11Entity> modDataListAisinMV = (List<CPMPMF11Entity>) mapsVV.get("modDataListAisin");
        List<CPMPMF11Entity> notRequiredPartsList = (List<CPMPMF11Entity>) mapsVV.get("notRequiredPartsList");
        // shiyang add start
        List<String> ttcPartNoListVvOwn = (List<String>) mapsVV.get("ttcPartNoListVV");
        List<String> ttcPartNoListAisinTemp = (List<String>) mapsVV.get("ttcPartNoListAisin");
        // shiyang add end

        // aisin
        List<CPMPMF11Entity> dataListVVMA = (List<CPMPMF11Entity>) mapsAisin.get("dataListVV");
        List<CPMPMF11Entity> newDataListVVMA = (List<CPMPMF11Entity>) mapsAisin.get("newDataListVV");
        List<CPMPMF11Entity> modDataListVVMVA = (List<CPMPMF11Entity>) mapsAisin.get("modDataListVV");
        List<CPMPMF11Entity> dataListAisinMA = (List<CPMPMF11Entity>) mapsAisin.get("dataListAisin");
        List<CPMPMF11Entity> newDataListAisinMA = (List<CPMPMF11Entity>) mapsAisin.get("newDataListAisin");
        List<CPMPMF11Entity> modDataListAisinMA = (List<CPMPMF11Entity>) mapsAisin.get("modDataListAisin");
        // shiyang add start
        List<String> ttcPartNoListVvTemp = (List<String>) mapsAisin.get("ttcPartNoListVV");
        List<String> ttcPartNoListAisinOwn = (List<String>) mapsAisin.get("ttcPartNoListAisin");
        // shiyang add end

        // add vv
        if (dataListVVMV != null && dataListVVMV.size() > 0) {
            dataListVV.addAll(dataListVVMV);
        }
        if (dataListVVMA != null && dataListVVMA.size() > 0) {
            dataListVV.addAll(dataListVVMA);
        }
        if (newDataListVVMV != null && newDataListVVMV.size() > 0) {
            newDataListVV.addAll(newDataListVVMV);
        }
        if (newDataListVVMA != null && newDataListVVMA.size() > 0) {
            newDataListVV.addAll(newDataListVVMA);
        }
        if (modDataListVVMV != null && modDataListVVMV.size() > 0) {
            modDataListVV.addAll(modDataListVVMV);
        }
        if (modDataListVVMVA != null && modDataListVVMVA.size() > 0) {
            modDataListVV.addAll(modDataListVVMVA);
        }
        // shiyang add start
        if (ttcPartNoListVvOwn != null && ttcPartNoListVvOwn.size() > 0) {
            ttcPartNoListVv.addAll(ttcPartNoListVvOwn);
        }
        if (ttcPartNoListVvTemp != null && ttcPartNoListVvTemp.size() > 0) {
            ttcPartNoListVv.addAll(ttcPartNoListVvTemp);
        }
        // shiyang add end

        // add aisin
        if (dataListAisinMV != null && dataListAisinMV.size() > 0) {
            dataListAisin.addAll(dataListAisinMV);
        }
        if (dataListAisinMA != null && dataListAisinMA.size() > 0) {
            dataListAisin.addAll(dataListAisinMA);
        }
        if (newDataListAisinMV != null && newDataListAisinMV.size() > 0) {
            newDataListAisin.addAll(newDataListAisinMV);
        }
        if (newDataListAisinMA != null && newDataListAisinMA.size() > 0) {
            newDataListAisin.addAll(newDataListAisinMA);
        }

        if (modDataListAisinMV != null && modDataListAisinMV.size() > 0) {
            modDataListAisin.addAll(modDataListAisinMV);
        }
        if (modDataListAisinMA != null && modDataListAisinMA.size() > 0) {
            modDataListAisin.addAll(modDataListAisinMA);
        }
        // shiyang add start
        if (ttcPartNoListAisinOwn != null && ttcPartNoListAisinOwn.size() > 0) {
            ttcPartNoListAisin.addAll(ttcPartNoListAisinOwn);
        }
        if (ttcPartNoListAisinTemp != null && ttcPartNoListAisinTemp.size() > 0) {
            ttcPartNoListAisin.addAll(ttcPartNoListAisinTemp);
        }
        // shiyang add end

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        List<BaseMessage> confirmLists = new ArrayList<BaseMessage>();
        Map<String, Object> maps = new HashMap<String, Object>();
        List<CPMPMF11Entity> rusultVVDataList = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> rusultAisinDataList = new ArrayList<CPMPMF11Entity>();
        List<Integer> idLists = new ArrayList<Integer>();

        // shiyang add start
        Integer lang = (Integer) dataMaps.get("lang");
        // query all ttcPartsNo
        Map<String, Object> ttcPartsNosMap = cpmpmf11DBService.getTtcPartsNo();
        Map<String, String> ttcPartsNoMaps = (Map<String, String>) ttcPartsNosMap.get("ttcPartsNoMaps");
        Map<String, String> oldTtcPartsNoMaps = (Map<String, String>) ttcPartsNosMap.get("oldTtcPartsNoMaps");

        // query all expUomCode
        Map<String, String> expUomCodeMap = cpmpmf11DBService.getExpUomCode();
        // query all expRegion,impRegion
        Map<String, String> regionMap = cpmpmf11DBService.getRegion();
        // query all impRegion by customerCode + officeCode
        Map<String, String> impRegionMap = cpmpmf11DBService.getImpRegion();
        // query all customerCode
        Map<String, String> customerMap = new HashMap<String, String>();
        cpmpmf11DBService.getCustomerInfo(customerMap);

        String[] busTypeList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE);
        String[] partsTypeList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE);
        String[] custStockFlagList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG);
        String[] inventoryByBoxList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX);
        String[] simulationEndDayPList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.SIMULATION_END_DAY_P);
        String[] custForecastAdjustP1List = CodeCategoryManager.getCodeName(lang,
            CodeMasterCategory.CUST_FORECAST_ADJUST_P1);
        String[] custForecastAdjustP2List = CodeCategoryManager.getCodeName(lang,
            CodeMasterCategory.CUST_FORECAST_ADJUST_P2);
        String[] buildOutIndicatorList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUILD_OUT_INDICATOR);
        String[] discontinueIndicatorList = CodeCategoryManager.getCodeName(lang,
            CodeMasterCategory.DISCONTINUE_INDICATOR);

        String discontinueIndicatorN = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR,
            0);

        // DB time now
        long dbTime = cpmsrf11Service.getDBtime();

        dataMaps.put("ttcPartsNoMaps", ttcPartsNoMaps);
        dataMaps.put("oldTtcPartsNoMaps", oldTtcPartsNoMaps);
        dataMaps.put("expUomCodeMap", expUomCodeMap);
        dataMaps.put("regionMap", regionMap);
        dataMaps.put("impRegionMap", impRegionMap);
        dataMaps.put("customerMap", customerMap);

        dataMaps.put("busTypeList", busTypeList);
        dataMaps.put("partsTypeList", partsTypeList);
        dataMaps.put("custStockFlagList", custStockFlagList);
        dataMaps.put("inventoryByBoxList", inventoryByBoxList);
        dataMaps.put("simulationEndDayPList", simulationEndDayPList);
        dataMaps.put("custForecastAdjustP1List", custForecastAdjustP1List);
        dataMaps.put("custForecastAdjustP2List", custForecastAdjustP2List);
        dataMaps.put("buildOutIndicatorList", buildOutIndicatorList);
        dataMaps.put("discontinueIndicatorList", discontinueIndicatorList);

        dataMaps.put("discontinueIndicatorN", discontinueIndicatorN);
        dataMaps.put("dbTime", dbTime);
        // shiyang add end

        // deal vv
        if (dataListVV != null && dataListVV.size() > 0) {
            dataMaps.put("dataListVV", dataListVV);
            dataMaps.put("newDataListVV", newDataListVV);
            dataMaps.put("modDataListVV", modDataListVV);
            // shiyang add start
            dataMaps.put("ttcPartNoListVV", ttcPartNoListVv);
            // shiyang add end
            // deal vv
            Map<String, Object> resultVVMaps = cpmpmf11VVService.dealDataDetail(dataMaps);
            List<BaseMessage> msgLists = (List<BaseMessage>) resultVVMaps.get("messageLists");
            if (msgLists != null && msgLists.size() > 0) {
                messageLists.addAll(msgLists);
            }
            rusultVVDataList = (List<CPMPMF11Entity>) resultVVMaps.get("dataList");
            List<Integer> idList = (List<Integer>) resultVVMaps.get("idList");
            if (idList != null && idList.size() > 0) {
                idLists.addAll(idList);
            }
        }
        // deal aisin
        if (dataListAisin != null && dataListAisin.size() > 0) {
            dataMaps.put("dataListAisin", dataListAisin);
            dataMaps.put("newDataListAisin", newDataListAisin);
            dataMaps.put("modDataListAisin", modDataListAisin);
            // shiyang add start
            dataMaps.put("ttcPartNoListAisin", ttcPartNoListAisin);
            // shiyang add end
            // deal aisin
            Map<String, Object> resultAISINMaps = cpmpmf11AisinService.dealDataDetail(dataMaps);

            List<BaseMessage> msgLists = (List<BaseMessage>) resultAISINMaps.get("messageLists");
            if (msgLists != null && msgLists.size() > 0) {
                messageLists.addAll(msgLists);
            }
            rusultAisinDataList = (List<CPMPMF11Entity>) resultAISINMaps.get("dataList");
            List<Integer> idList = (List<Integer>) resultAISINMaps.get("idList");
            if (idList != null && idList.size() > 0) {
                idLists.addAll(idList);
            }
        }

        // deal both vv aisin
        List<CPMPMF11Entity> rusultDataList = new ArrayList<CPMPMF11Entity>();
        boolean vvFlag = false;
        if (rusultVVDataList != null && rusultVVDataList.size() > 0) {
            vvFlag = true;
            rusultDataList.addAll(rusultVVDataList);
        }
        if (rusultAisinDataList != null && rusultAisinDataList.size() > 0) {
            rusultDataList.addAll(rusultAisinDataList);
        }

        if (rusultDataList != null && rusultDataList.size() > 0) {
            Map<String, Object> bothDataMap = new HashMap<String, Object>();
            // shiyang del start
            // Integer lang = (Integer) dataMaps.get("lang");
            // shiyang del end
            Locale langs = (Locale) dataMaps.get("langs");
            bothDataMap.put("rusultDataList", rusultDataList);
            bothDataMap.put("lang", lang);
            bothDataMap.put("langs", langs);
            Map<String, Object> conmonMaps = cpmpmf11CommonService.checkBothVVAISIN(bothDataMap);

            List<BaseMessage> msgLists = (List<BaseMessage>) conmonMaps.get("messageLists");
            if (msgLists != null && msgLists.size() > 0) {
                messageLists.addAll(msgLists);
            }

            if (messageLists != null && messageLists.size() > 0) {
                throw new BusinessException(messageLists);
            }

            rusultDataList = (List<CPMPMF11Entity>) conmonMaps.get("rusultDataList");

            Map<String, Object> resultVVMaps = cpmpmf11CommonService.checkConfirmBothVVAISIN(rusultDataList, vvFlag);
            confirmLists = (List<BaseMessage>) resultVVMaps.get("confirmMsgList");
            rusultDataList = (List<CPMPMF11Entity>) resultVVMaps.get("dataList");

            maps.put("rusultDataList", rusultDataList);
            maps.put("idLists", idLists);

            if (confirmLists != null && confirmLists.size() > 0) {
                SessionInfoManager context = SessionInfoManager.getContextInstance(request);
                context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString(), rusultDataList);
                context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY.toString()) + "idLists", idLists);
                context.put(param.getSwapData().get(FILE_UPLOAD_SESSION_KEY.toString()) + "_notRequiredPartsList",
                    notRequiredPartsList);
                throw new BusinessException(confirmLists);
            }
        } else {
            // if has error messages
            if (messageLists != null && !messageLists.isEmpty()) {
                throw new BusinessException(messageLists);
            }
        }

        // set into maps
        maps.put("notRequiredPartsList", notRequiredPartsList);

        return maps;
    }
}