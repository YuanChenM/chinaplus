/**
 * @screen CPMPMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.BusinessPatternName;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.OrionPlusFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;

/**
 * CPMPMF11VVService.
 */
@Service
public class CPMPMF11VVService extends BaseService {

    /**
     * cpmpmf11DBService.
     */
    @Autowired
    private CPMPMF11DBService cpmpmf11DBService;

    /**
     * cpmpmf11CommonService.
     */
    @Autowired
    private CPMPMF11CommonService cpmpmf11CommonService;

    /**
     * cpmsrf11Service.
     */
    @Autowired
    private CPMSRF11Service cpmsrf11Service;

    /**
     * deal vv data detail
     * 
     * @param dataMaps dataMaps
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> dealDataDetail(Map<String, Object> dataMaps) {
        List<CPMPMF11Entity> resultDataList = new ArrayList<CPMPMF11Entity>();

        List<CPMPMF11Entity> dataList = (List<CPMPMF11Entity>) dataMaps.get("dataListVV");
        List<CPMPMF11Entity> newDataList = (List<CPMPMF11Entity>) dataMaps.get("newDataListVV");
        List<CPMPMF11Entity> modDataList = (List<CPMPMF11Entity>) dataMaps.get("modDataListVV");
        // shiyang add start
        List<String> ttcPartNoList = (List<String>) dataMaps.get("ttcPartNoListVV");
        // shiyang add end

        Integer userId = (Integer) dataMaps.get("userId");
        Integer lang = (Integer) dataMaps.get("lang");
        Locale langs = (Locale) dataMaps.get("langs");

        // officeCode Map
        Map<String, Integer> officeCodeMap = (Map<String, Integer>) dataMaps.get("officeCodeMap");
        // customerCode Map
        // Map<String, String> customerCodeMap = (Map<String, String>) dataMaps.get("customerCodeMap");
        // officeCode + customerCode Map
        Map<String, String> officeCustCodeMap = (Map<String, String>) dataMaps.get("officeCustCodeMap");

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Map<String, Object> maps = new HashMap<String, Object>();

        // type new partsId is null query ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
        Map<String, Object> newDataMapDB = cpmpmf11DBService.getDataMap(newDataList, "noNeed", "");
        // type new query ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode +
        // officeCode
        Map<String, String> newDataCMapDB = cpmpmf11DBService.getKeyDataMap(newDataList, "");
        // type new query expCustCode
        Map<String, String> newExpCustCodeMapDB = cpmpmf11DBService.getNewExpCustCodeMap(newDataList);

        // type mod partsId is null query ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
        Map<String, Object> modDataMapDB = cpmpmf11DBService.getDataMap(modDataList, "noNeed", ShipType.MOD);
        // type mod partsId all query ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
        Map<String, Object> modAllDataMapDB = cpmpmf11DBService.getDataMap(modDataList, "", "");
        // type mod query ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode +
        // officeCode
        Map<String, String> modDataCMapDB = cpmpmf11DBService.getKeyDataMap(modDataList, ShipType.MOD);

        // type all partsId is not null query ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
        Map<String, Object> allDataMapDB = cpmpmf11DBService.getDataMap(dataList, "need", "");
        // type all partsId is not null query ttcPartsNo + customerCode + officeCode
        Map<String, Integer> allDataTCEOMapDB = cpmpmf11DBService.getTCEOMap(dataList);

        // shiyang del start
        // // xlsx ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode
        // Map<String, String> ttseceMaps = new HashMap<String, String>();
        // shiyang del end
        // xlsx officeCode + ttcPartsNo + customerCode
        Map<String, Object> otctMaps = new HashMap<String, Object>();

        // xlsx officeCode + ttcPartsNo + customerCode,partsId
        Map<String, Integer> otcpMaps = new HashMap<String, Integer>();

        // xlsx ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode ,related to more then one ttcSuppCode
        Map<String, String> tseeMaps = new HashMap<String, String>();
        // db ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode ,related to more then one ttcSuppCode
        Map<String, String> tseeMapsDB = cpmpmf11DBService.getTtcSuppCodeMap(dataList);

        // DB all query officeCode + ttcPartsNo + customerCode , bean
        Map<String, Object> otctMapDB = cpmpmf11DBService.getOTCTMap(dataList, BusinessPattern.V_V);
        Map<String, Object> otctMapsDB = (Map<String, Object>) otctMapDB.get("otctMaps");
        // DB all query count (officeCode + ttcPartsNo + customerCode)
        Map<String, Integer> otctCountMapsDB = (Map<String, Integer>) otctMapDB.get("otctCountMaps");
        // DB all query officeCode + ttcPartsNo + customerCode ,inactiveFlag
        Map<String, String> otciYNMapsDB = (Map<String, String>) otctMapDB.get("otciYNMapsDB");

        // xlsx all count (officeCode + ttcPartsNo + customerCode)
        Map<String, Object> otciModMapsDB = cpmpmf11CommonService.getOtctCountMaps(dataList, otctCountMapsDB,
            otciYNMapsDB, BusinessPattern.V_V);
        otctCountMapsDB = (Map<String, Integer>) otciModMapsDB.get("otctCountMapsDB");
        Map<String, Integer> xlsxCountMaps = (Map<String, Integer>) otciModMapsDB.get("xlsxCountMaps");

        // DB query westCustCode + westPartsNo
        // shiyang mod start
        // Map<String, String> wwMapsDB = cpmpmf11DBService.getWWMap(dataList);
        Map<String, List<String>> wwMapsDB = cpmpmf11DBService.getWWMap(dataList);
        // shiyang mod end
        // DB query expCalendarCode
        Map<String, String> expCalendarCodeMapsDB = cpmpmf11DBService.getExpCalendarCodeMap(dataList);

        // get CodeCategory name
        // shiyang del start
        // String[] partsTypeList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE);
        // shiyang del end
        String partsStatusN = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS,
            PartsStatus.COMPLETED);
        String partsStatusM = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS,
            PartsStatus.NOT_REQUIRED);
        String discontinueIndicatorY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR,
            IntDef.INT_ONE);
        String[] orderForecastType = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE);
        String[] exportWhCalender = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.EXPORT_WH_CALENDER);
        String orderForecastTypeT = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
            IntDef.INT_NINE);
        String[] onShipDelayAdjuP = CodeCategoryManager
            .getCodeName(lang, CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P);

        // shiyang add start
        // check dulicate key: ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
        List<String> checkDuliFile4Key = new ArrayList<String>();
        // check dulicate key: ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode +
        // officeCode
        List<String> checkDuliFile6Key = new ArrayList<String>();

        // query all ttcPartsNo
        Map<String, String> ttcPartsNoMaps = (Map<String, String>) dataMaps.get("ttcPartsNoMaps");
        Map<String, String> oldTtcPartsNoMaps = (Map<String, String>) dataMaps.get("oldTtcPartsNoMaps");
        Map<String, String> oldTtcPartsNoFileMaps = new HashMap<String, String>();

        // query all expUomCode
        Map<String, String> expUomCodeMap = (Map<String, String>) dataMaps.get("expUomCodeMap");
        // query all expRegion,impRegion
        Map<String, String> regionMap = (Map<String, String>) dataMaps.get("regionMap");
        // query all impRegion by customerCode + officeCode
        Map<String, String> impRegionMap = (Map<String, String>) dataMaps.get("impRegionMap");
        // query all customerCode
        Map<String, String> customerMap = (Map<String, String>) dataMaps.get("customerMap");
        // query officeCode + expRegion + toEtd + inactiveFlag + shippingRouteType by shippingRouteCode
        Map<String, String> shippingRouteCodeMap = cpmpmf11DBService.getShippingRouteCode(dataList);

        String[] busTypeList = (String[]) dataMaps.get("busTypeList");
        String[] partsTypeList = (String[]) dataMaps.get("partsTypeList");
        String[] custStockFlagList = (String[]) dataMaps.get("custStockFlagList");
        String[] inventoryByBoxList = (String[]) dataMaps.get("inventoryByBoxList");
        String[] simulationEndDayPList = (String[]) dataMaps.get("simulationEndDayPList");
        String[] custForecastAdjustP1List = (String[]) dataMaps.get("custForecastAdjustP1List");
        String[] custForecastAdjustP2List = (String[]) dataMaps.get("custForecastAdjustP2List");
        String[] buildOutIndicatorList = (String[]) dataMaps.get("buildOutIndicatorList");
        String[] discontinueIndicatorList = (String[]) dataMaps.get("discontinueIndicatorList");

        String discontinueIndicatorN = (String) dataMaps.get("discontinueIndicatorN");

        // DB time now
        long dbTime = (long) dataMaps.get("dbTime");

        // xlsx ttcSuppCode + supplierName
        Map<String, String> ttcSuppCodeNameMap = new HashMap<String, String>();
        // xlsx ttcSuppCode + expRegion
        Map<String, String> ttcSuppCodeExpRegMap = new HashMap<String, String>();
        // xlsx ttcSuppCode + supplierName + expRegion msg
        Map<String, String> ttcSCNameRegMsgMap = new HashMap<String, String>();
        // xlsx shippingRouteCode + officeCode
        Map<String, String> shipRCodeOffCgMap = new HashMap<String, String>();
        // xlsx shippingRouteCode + expRegion
        Map<String, String> shipRCodeExpRgMap = new HashMap<String, String>();
        // shippingRouteCode + officeCode + expRegion msg
        Map<String, String> shipRCodeOEMsgMap = new HashMap<String, String>();

        // DB query impWhsCode + impRegion
        Map<String, String> impWhsCodeRegionMapDB = cpmpmf11DBService.getImpWhsCodeRegion(dataList);
        // xlsx impWhsCode + impRegion
        Map<String, String> impWhsCodeRegionMap = new HashMap<String, String>();
        // check impWhsCode + impRegion msg
        Map<String, String> impWhsCRegionMsgMaps = new HashMap<String, String>();
        // shiyang add end

        // check all data
        for (CPMPMF11Entity cf : dataList) {
            String rowNum = StringUtil.toSafeString(cf.getRowNum());
            String sheetName = cf.getSheetName();

            // check officeCode
            String officeCode = cf.getOfficeCode();
            Integer officeCodeM = officeCodeMap.get(officeCode);
            if (null == officeCodeM) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                message.setMessageArgs(new String[] { rowNum, sheetName, officeCode, "CPMPMF01_Grid_PartsMasterData" });
                messageLists.add(message);
                continue;
            } else {
                cf.setOfficeId(officeCodeM);
            }

            // check customerCode
            String customerCode = cf.getCustomerCode();
            String partsStatus = cf.getPartsStatus();
            // String valueCustomerCode = customerCodeMap.get(customerCode);
            // if (null == valueCustomerCode) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
            // message
            // .setMessageArgs(new String[] { rowNum, sheetName, customerCode, "CPMPMF01_Grid_PartsMasterData" });
            // messageLists.add(message);
            // continue;
            // } else {
            String customerCodeM = officeCustCodeMap.get(officeCode + StringConst.UNDERLINE + customerCode);
            if (customerCodeM == null) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                message
                    .setMessageArgs(new String[] { rowNum, sheetName, customerCode, "CPMPMF01_Grid_PartsMasterData" });
                messageLists.add(message);
                continue;
            } else {
                String[] valueCustomerCodes = customerCodeM.split(StringConst.UNDERLINE);
                if ((InactiveFlag.INACTIVE + "").equals(valueCustomerCodes[IntDef.INT_TWO])
                        && !partsStatusM.equals(partsStatus) && !discontinueIndicatorY.equals(cf.getInactiveFlag())) {
                    // 2016/07/26 mod shiyang start
                    // // 2016/06/29 shiyang mod start for UAT No.121
                    // // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                    // // message.setMessageArgs(new String[] { rowNum, sheetName, customerCode,
                    // // "CPMPMF01_Grid_PartsMasterData" });
                    // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
                    // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                    // "CPMPMF01_Grid_CustomerMaster" });
                    // // 2016/06/29 shiyang mod end
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_185);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator",
                        discontinueIndicatorY, "CPMPMF01_Grid_TTCCustCd" });
                    // 2016/07/26 mod shiyang end
                    messageLists.add(message);
                    continue;
                } else if (!(BusinessPattern.V_V + "").equals(valueCustomerCodes[0])) {
                    // 2016/07/26 mod shiyang start
                    // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                    // message.setMessageArgs(new String[] { rowNum, sheetName, BusinessPatternName.V_V,
                    // "CPMPMF01_Grid_PartsMasterData" });
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_184);
                    message.setMessageArgs(new String[] { rowNum, sheetName, BusinessPatternName.V_V });
                    // 2016/07/26 mod shiyang end
                    messageLists.add(message);
                    continue;
                } else {
                    cf.setCustomerId(Integer.valueOf(valueCustomerCodes[1]));
                }
            }

            // set creatby
            cf.setCreatedBy(userId);
            // set updatedBy
            cf.setUpdatedBy(userId);

            // // shiyang add start
            // if (cf.isPartsStatusFlag()) {
            // continue;
            // }
            // // shiyang add end

            String ttcPartsNo = cf.getTtcPartsNo();
            String ttcSuppCode = cf.getTtcSuppCode();
            String ssmsMainRoute = cf.getSsmsMainRoute();
            String expSuppCode = cf.getExpSuppCode();
            String expCustCode = cf.getExpCustCode();
            String key = ttcPartsNo + StringConst.UNDERLINE + ssmsMainRoute + StringConst.UNDERLINE + expSuppCode
                    + StringConst.UNDERLINE + expCustCode;
            // shiyang mod start
            // String keyc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + ssmsMainRoute
            // + StringConst.UNDERLINE + expSuppCode + StringConst.UNDERLINE + customerCode
            // + StringConst.UNDERLINE + expCustCode;
            String keyc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + ssmsMainRoute
                    + StringConst.UNDERLINE + expSuppCode + StringConst.UNDERLINE + customerCode
                    + StringConst.UNDERLINE + expCustCode + StringConst.UNDERLINE + officeCode;
            // shiyang mod end
            // shiyang del start (duplicate the "key")
            // String keyt = ttcPartsNo + StringConst.UNDERLINE + ssmsMainRoute + StringConst.UNDERLINE + expSuppCode
            // + StringConst.UNDERLINE + expCustCode;
            // shiyang del end

            boolean repFlag = true;
            // shiyang mod start
            // // check xlsx repeat ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode + expCustCode
            // String vlaueXlsx = ttseceMaps.get(keyc);
            // if (!StringUtil.isNullOrEmpty(vlaueXlsx)) {
            // repFlag = false;
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_010);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile", ttcPartsNo,
            // ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
            // messageLists.add(message);
            // } else {
            // ttseceMaps.put(keyc, ttcPartsNo);
            // }
            // If there has same part with the condition of 4-3-2-1 in upload file.(w1004_010 {2}：Upload file)
            if (checkDuliFile6Key.contains(keyc)) {
                repFlag = false;
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_010);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile", ttcPartsNo,
                    ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
                messageLists.add(message);
            } else {
                checkDuliFile6Key.add(keyc);

                if (checkDuliFile4Key.contains(key)) {
                    repFlag = false;
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_010);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile", ttcPartsNo,
                        ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
                    messageLists.add(message);
                } else {
                    checkDuliFile4Key.add(key);
                }
            }
            // shiyang mod end

            // deal type new
            String type = cf.getType();
            if (type.equals(ShipType.NEW)) {
                if (repFlag) {
                    // check DB repeat ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode +
                    // expCustCode + officeCode
                    String newCValue = newDataCMapDB.get(keyc);
                    if (!StringUtil.isNullOrEmpty(newCValue)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_010);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                            ttcPartsNo, ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
                        messageLists.add(message);
                        // shiyang del start
                        // continue;
                        // shiyang del end
                    } else {
                        // check DB ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
                        String newValue = (String) newDataMapDB.get(key);
                        if (!StringUtil.isNullOrEmpty(newValue)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_010);
                            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                                ttcPartsNo, ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
                            messageLists.add(message);
                            // shiyang del start
                            // continue;
                            // shiyang del end
                        }
                    }
                }

            }

            // deal type mod
            String modCValue = "";
            String modValue = "";
            if (type.equals(ShipType.MOD)) {
                boolean partsStuFlag = true;
                String modCValues = modDataCMapDB.get(keyc);
                modValue = (String) modDataMapDB.get(key);

                // check DB repeat ttcPartsNo + ttcSuppCode + ssmsMainRoute + expSuppCode + customerCode +
                // expCustCode + officeCode
                if (repFlag && StringUtil.isNullOrEmpty(modCValues)) {
                    // check DB ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode
                    if (StringUtil.isNullOrEmpty(modValue)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_021);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                            ttcPartsNo, ttcSuppCode, ssmsMainRoute, expSuppCode, customerCode, expCustCode });
                        messageLists.add(message);
                        // shiyang mod start
                        // continue;
                        // } else {
                        // partsStuFlag = false;
                        // }
                        partsStuFlag = false;
                    }
                    // shiyang mod end
                }
                // add for UAT issue 19,20
                if (partsStatusM.equals(partsStatus) && !discontinueIndicatorY.equals(cf.getInactiveFlag())) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator",
                        discontinueIndicatorY });
                    messageLists.add(message);
                }
                // add end

                if (!StringUtil.isNullOrEmpty(modCValues)) {
                    // shiyang mod start
                    // String[] modCValuesList = modCValues.split(StringConst.DOUBLE_UNDERLINE);
                    // modCValue = modCValuesList[0];
                    modCValue = modCValues;
                    // shiyang mod end
                }

                // set expPartsId
                Integer expPartsId = (Integer) modAllDataMapDB.get(key);
                if (expPartsId != null) {
                    cf.setExpPartsId(expPartsId);
                }

                // check partsStatus is Not Required
                // if (partsStatusN.equals(partsStatus) && !partsStuFlag) {
                // shiyang mod start
                // if (partsStatusM.equals(partsStatus) && !partsStuFlag) {
                if (partsStatusM.equals(partsStatus) && partsStuFlag) {
                    // shiyang mod end
                    cf.setPartsStatusFlag(true);
                    resultDataList.add(cf);
                    continue;
                }
            }

            if (type.equals(ShipType.MOD)) {
                // check partsStatus
                if (!partsStatusN.equals(partsStatus) && !partsStatusM.equals(partsStatus)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartStatus",
                        (partsStatusN + StringConst.COMMA + partsStatusM) });
                    messageLists.add(message);
                } else {
                    Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.PARTS_STATUS,
                        partsStatus);
                    cf.setPartsStatus(codeValue + "");
                }
            }

            if (type.equals(ShipType.NEW)) {
                // check DB expCustCode
                // shiyang mod start
                // String newExpCustCode = newExpCustCodeMapDB.get(expCustCode);
                String newExpCustCode = newExpCustCodeMapDB.get(expCustCode + StringConst.COMMA + officeCode);
                // shiyang mod end
                if (StringUtil.isNullOrEmpty(newExpCustCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustCdinSSMSORKANB",
                        "CPMPMF01_Grid_SSMSCustomerMaster" });
                    messageLists.add(message);
                } else {
                    // shiyang mod start
                    // String[] newExpCustCodes = newExpCustCode.split(StringConst.UNDERLINE);
                    // if (!(OrionPlusFlag.Y + "").equals(newExpCustCodes[0])) {
                    if (!(OrionPlusFlag.Y + "").equals(newExpCustCode)) {
                        // shiyang mod end
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_126);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                        messageLists.add(message);
                    }
                }

                // check partsStatus
                if (!partsStatusN.equals(partsStatus)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                    message
                        .setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartStatus", partsStatusN });
                    messageLists.add(message);
                } else {
                    Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.PARTS_STATUS,
                        partsStatus);
                    cf.setPartsStatus(codeValue + "");
                }

                // shiyang add start
                // For one set of TTC Part No. + Customer Code in SSMS OR KANB invoice & Supplier Kanban Issue Plan file
                // + SSMS Supplier Code + SSMS Main Route related to more then one TTC Supplier Code.
                if (tseeMaps.containsKey(key)) {
                    // check file
                    String ttcSuppCodeTemp = tseeMaps.get(key);
                    if (!ttcSuppCode.equals(ttcSuppCodeTemp)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_150);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile" });
                        messageLists.add(message);
                    }
                } else {
                    tseeMaps.put(key, ttcSuppCode);

                    // check DB
                    if (tseeMapsDB.containsKey(key)) {
                        String ttcSuppCodeTemp = tseeMapsDB.get(key);
                        if (!ttcSuppCode.equals(ttcSuppCodeTemp)) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_150);
                            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData" });
                            messageLists.add(message);
                        }
                    }
                }
                // shiyang add end
            }

            // check partsId
            String partsIdValue = (String) allDataMapDB.get(key);
            String keyp = ttcPartsNo + StringConst.UNDERLINE + customerCode + StringConst.UNDERLINE + officeCode;
            Integer partsIdTCEOValue = allDataTCEOMapDB.get(keyp);
            if (partsIdValue != null) {
                String[] partsIdValues = partsIdValue.split(StringConst.UNDERLINE);
                String sexpInnerPartsId = partsIdValues[0];
                if (!StringUtil.isNullOrEmpty(sexpInnerPartsId)) {
                    cf.setExpInnerPartsId(Integer.valueOf(sexpInnerPartsId));
                    if (null != partsIdTCEOValue && !sexpInnerPartsId.equals(partsIdTCEOValue + "")) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCSuppCd",
                            "CPMPMF01_Grid_TTCCustCd" });
                        messageLists.add(message);
                    }
                }
            }
            if (partsIdTCEOValue != null) {
                cf.setPartsId(partsIdTCEOValue);
            }

            // check westCustCode + westPartsNo
            boolean wwFlag = true;
            String westCustCode = cf.getWestCustCode();
            String westPartsNo = cf.getWestPartsNo();
            String keyww = westCustCode + StringConst.UNDERLINE + westPartsNo;
            // shiyang mod start
            // String valueww = wwMapsDB.get(keyww);
            List<String> valueww = wwMapsDB.get(keyww);
            // shiyang mod end

            // shiyang mod start
            // if (valueww != null) {
            if (valueww != null && !(valueww.size() == 1 && valueww.contains(keyc))) {
                // shiyang mod end
                if (type.equals(ShipType.MOD) && (!keyww.equals(modCValue) && !keyww.equals(modValue))) {
                    wwFlag = false;
                } else if (type.equals(ShipType.NEW) && null == partsIdTCEOValue) {
                    wwFlag = false;
                }
            }
            // add by yinchuan liu for remove current check start 
            wwFlag= true;
            // add by yinchuan liu end
            if (!wwFlag) {
                String wwin = MessageManager.getMessage("CPMPMF01_Grid_WESTCustCd", langs) + StringConst.COMMA
                        + MessageManager.getMessage("CPMPMF01_Grid_WESTPN", langs);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_012);
                message.setMessageArgs(new String[] { rowNum, sheetName, wwin, "CPMPMF01_Grid_PartsMasterData" });
                messageLists.add(message);
            }

            // shiyang add start
            // col. 6
            cpmpmf11CommonService.checkOldTtcPartsNo(messageLists, cf, sheetName, rowNum, ttcPartsNoMaps,
                oldTtcPartsNoMaps, oldTtcPartsNoFileMaps, ttcPartNoList);
            // col. 7
            cpmpmf11CommonService.checkExpUomCode(messageLists, cf, sheetName, rowNum, expUomCodeMap);
            // col. 8
            cpmpmf11CommonService.checkExpRegion(messageLists, cf, sheetName, rowNum, regionMap);
            // col. 15
            cpmpmf11CommonService.checkImpRegion(messageLists, cf, sheetName, rowNum, regionMap, impRegionMap);
            // col. 17
            cpmpmf11CommonService.checkCustomerCode(messageLists, cf, sheetName, rowNum, customerMap);
            // col. 26 - col. 29
            cpmpmf11CommonService.checkOrderLotToSpqM3(messageLists, cf, sheetName, rowNum);
            // col. 31
            cpmpmf11CommonService.checkBusinessType(messageLists, cf, sheetName, rowNum, lang, busTypeList);
            // col. 32
            cpmpmf11CommonService.checkPartsType(messageLists, cf, sheetName, rowNum, lang, partsTypeList);
            // col. 34
            String orderTime = cf.getOrderTime();
            String[] orderTimes = orderTime.split(StringConst.MIDDLE_LINE);
            if (orderTimes.length != NumberConst.IntDef.INT_TWO || !"D".equals(orderTimes[0])) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
                    "CPMPMF01_Grid_DNumberDay" });
                messageLists.add(message);
            } else {
                if (!StringUtil.isNumeric(orderTimes[1])) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
                        "CPMPMF01_Grid_DNumberDay" });
                    messageLists.add(message);
                } else {
                    Integer orderDay = Integer.valueOf(orderTimes[1]);
                    if (orderDay > IntDef.INT_THIRTY_ONE) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_018);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
                            IntDef.INT_THIRTY_ONE + "" });
                        messageLists.add(message);
                    } else if (orderDay < IntDef.INT_ONE) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
                            IntDef.INT_ONE + "" });
                        messageLists.add(message);
                    } else {
                        cf.setOrderDay(orderDay);
                    }
                }
            }
            // col. 35
            cpmpmf11CommonService.checkTargetMonth(messageLists, cf, sheetName, rowNum);
            // col. 37
            // check orderFcType
            String orderFcType = cf.getOrderFcType();
            List<BaseMessage> orderFcTypeMsgs = cpmpmf11CommonService.checkCodeCategory(orderFcType, orderForecastType,
                "CPMPMF01_Grid_TypeofOrderForecast", rowNum, sheetName);
            // shiyang del start
            // if (orderFcTypeMsgs != null && orderFcTypeMsgs.size() > 0) {
            // messageLists.addAll(orderFcTypeMsgs);
            // }
            // shiyang del end
            // check orderFcType is aisin
            // shiyang del start
            // else if (orderFcType.equals(orderForecastTypeT)) {
            // shiyang del end
            if (orderFcType.equals(orderForecastTypeT) || (orderFcTypeMsgs != null && orderFcTypeMsgs.size() > 0)) {
                List<BaseMessage> orderFcTypeTMsgs = cpmpmf11CommonService.giveUpOneCodeCategory(orderForecastTypeT,
                    orderForecastType, "CPMPMF01_Grid_TypeofOrderForecast", rowNum, sheetName);
                if (orderFcTypeTMsgs != null && orderFcTypeTMsgs.size() > 0) {
                    messageLists.addAll(orderFcTypeTMsgs);
                }
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
                    orderFcType);
                cf.setOrderFcType(codeValue + "");
            }
            // col. 36
            cpmpmf11CommonService.checkForecastNum(messageLists, cf, sheetName, rowNum);
            // col. 38
            // check expCalendarCode
            String expCalendarCode = cf.getExpCalendarCode();
            List<BaseMessage> expCalendarCodeMsgs = cpmpmf11CommonService.checkCodeCategory(expCalendarCode,
                exportWhCalender, "CPMPMF01_Grid_ExportWHCalendar", rowNum, sheetName);
            if (expCalendarCodeMsgs != null && expCalendarCodeMsgs.size() > 0) {
                messageLists.addAll(expCalendarCodeMsgs);
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.EXPORT_WH_CALENDER,
                    expCalendarCode);
                cf.setExpCalendarCode(codeValue + "");
            }

            // check db expCalendarCode
            String vauleExpCalendarCode = expCalendarCodeMapsDB.get(expCalendarCode);
            if (StringUtil.isNullOrEmpty(vauleExpCalendarCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportWHCalendar",
                    "CPMPMF01_Grid_CalenderMaster" });
                messageLists.add(message);
            }
            // col. 39
            cpmpmf11CommonService.checkOsCustStockFlag(messageLists, cf, sheetName, rowNum, lang, custStockFlagList);
            // col. 40
            cpmpmf11CommonService.checkSaCustStockFlag(messageLists, cf, sheetName, rowNum, lang, custStockFlagList);
            // col. 41
            cpmpmf11CommonService.checkInventoryBoxFlag(messageLists, cf, sheetName, rowNum, lang, inventoryByBoxList);
            // col. 48
            cpmpmf11CommonService.checkOutboundFluctuation(messageLists, cf, sheetName, rowNum);
            // col. 49
            cpmpmf11CommonService.checkSimulationEndDatePattern(messageLists, cf, sheetName, rowNum, lang,
                simulationEndDayPList);
            // col. 50
            cpmpmf11CommonService.checkShippingRouteCode(messageLists, cf, sheetName, rowNum, langs, dbTime,
                shipRCodeOffCgMap, shipRCodeOEMsgMap, shipRCodeExpRgMap, shippingRouteCodeMap);
            // col. 51
            // check delayAdjustmentPattern
            String delayAdjustmentPattern = cf.getDelayAdjustmentPattern();
            List<BaseMessage> delayAdjustmentPatternMsgs = cpmpmf11CommonService.checkCodeCategory(
                delayAdjustmentPattern, onShipDelayAdjuP, "CPMPMF01_Grid_OnShipDelayAdjustPatt", rowNum, sheetName);
            if (delayAdjustmentPatternMsgs != null && delayAdjustmentPatternMsgs.size() > 0) {
                messageLists.addAll(delayAdjustmentPatternMsgs);
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang,
                    CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, delayAdjustmentPattern);
                cf.setDelayAdjustmentPattern(codeValue + "");
            }
            // col. 52 - col. 56
            // check airInboundLeadtime,airEtaLeadtime,airEtdLeadtime,seaEtaLeadtime,seaInboundLeadtime
            Integer airInboundLeadtime = cf.getAirInboundLeadtime();
            if (airInboundLeadtime < 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforAir",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            Integer airEtaLeadtime = cf.getAirEtaLeadtime();
            if (airEtaLeadtime < 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_LTfromETDtoETAforAir",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            Integer airEtdLeadtime = cf.getAirEtdLeadtime();
            if (airEtdLeadtime < 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromExpIbdtoETDforAir",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            Integer seaEtaLeadtime = cf.getSeaEtaLeadtime();
            if (seaEtaLeadtime < 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_LTfromETDtoETAforSea",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            Integer seaInboundLeadtime = cf.getSeaInboundLeadtime();
            if (seaInboundLeadtime < 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforSea",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            // col. 57
            cpmpmf11CommonService.checkAllocationType(messageLists, cf, sheetName, rowNum);
            // col. 58
            cpmpmf11CommonService.checkCfcAdjustmentType1(messageLists, cf, sheetName, rowNum, lang,
                custForecastAdjustP1List);
            // col. 59
            cpmpmf11CommonService.checkCfcAdjustmentType2(messageLists, cf, sheetName, rowNum, lang,
                custForecastAdjustP2List);
            // col. 63
            cpmpmf11CommonService.checkBuildoutFlag(messageLists, cf, sheetName, rowNum, lang, buildOutIndicatorList);
            // col. 68
            cpmpmf11CommonService.checkInactiveFlag(messageLists, cf, sheetName, rowNum, lang, discontinueIndicatorN,
                discontinueIndicatorList);
            // other
            cpmpmf11CommonService.checkRelationInLoop(messageLists, cf, sheetName, rowNum, ttcSuppCodeNameMap,
                ttcSCNameRegMsgMap, ttcSuppCodeExpRegMap, impWhsCodeRegionMap, impWhsCodeRegionMapDB,
                impWhsCRegionMsgMaps);
            // shiyang add end

            // shiyang del start
            // // check orderDay
            // String orderTime = cf.getOrderTime();
            // boolean orderTimeF = orderTime.startsWith("D-");
            // if (orderTimeF) {
            // orderTime = orderTime.replace("D-", "");
            // if (!StringUtil.isNumeric(orderTime)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
            // "CPMPMF01_Grid_DNumberDay" });
            // messageLists.add(message);
            // } else {
            // Integer orderDay = Integer.valueOf(orderTime);
            // if (orderDay > IntDef.INT_THIRTY_ONE) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_018);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
            // IntDef.INT_THIRTY_ONE + "" });
            // messageLists.add(message);
            // } else if (orderDay < IntDef.INT_ONE) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
            // IntDef.INT_ONE + "" });
            // messageLists.add(message);
            // } else {
            // cf.setOrderDay(orderDay);
            // }
            // }
            // } else {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_017);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderTime",
            // "CPMPMF01_Grid_DNumberDay" });
            // messageLists.add(message);
            // }
            //
            // // check orderFcType
            // String orderFcType = cf.getOrderFcType();
            // List<BaseMessage> orderFcTypeMsgs = cpmpmf11CommonService.checkCodeCategory(orderFcType,
            // orderForecastType,
            // "CPMPMF01_Grid_TypeofOrderForecast", rowNum, sheetName);
            // if (orderFcTypeMsgs != null && orderFcTypeMsgs.size() > 0) {
            // messageLists.addAll(orderFcTypeMsgs);
            // }
            // // check orderFcType is aisin
            // else if (orderFcType.equals(orderForecastTypeT)) {
            // List<BaseMessage> orderFcTypeTMsgs = cpmpmf11CommonService.giveUpOneCodeCategory(orderForecastTypeT,
            // orderForecastType, "CPMPMF01_Grid_TypeofOrderForecast", rowNum, sheetName);
            // if (orderFcTypeMsgs != null && orderFcTypeMsgs.size() > 0) {
            // messageLists.addAll(orderFcTypeTMsgs);
            // }
            // } else {
            // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
            // orderFcType);
            // cf.setOrderFcType(codeValue + "");
            // }
            //
            // // check expCalendarCode
            // String expCalendarCode = cf.getExpCalendarCode();
            // List<BaseMessage> expCalendarCodeMsgs = cpmpmf11CommonService.checkCodeCategory(expCalendarCode,
            // exportWhCalender, "CPMPMF01_Grid_ExportWHCalendar", rowNum, sheetName);
            // if (expCalendarCodeMsgs != null && expCalendarCodeMsgs.size() > 0) {
            // messageLists.addAll(expCalendarCodeMsgs);
            // } else {
            // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.EXPORT_WH_CALENDER,
            // expCalendarCode);
            // cf.setExpCalendarCode(codeValue + "");
            // }
            //
            // // check db expCalendarCode
            // String vauleExpCalendarCode = expCalendarCodeMapsDB.get(expCalendarCode);
            // if (StringUtil.isNullOrEmpty(vauleExpCalendarCode)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportWHCalendar",
            // "CPMPMF01_Grid_CalenderMaster" });
            // messageLists.add(message);
            // }
            //
            // // check airInboundLeadtime,airEtaLeadtime,airEtdLeadtime,seaEtaLeadtime,seaInboundLeadtime
            // Integer airInboundLeadtime = cf.getAirInboundLeadtime();
            // if (airInboundLeadtime < 0) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforAir",
            // StringUtil.toSafeString(0) });
            // messageLists.add(message);
            // }
            // Integer airEtaLeadtime = cf.getAirEtaLeadtime();
            // if (airEtaLeadtime < 0) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_LTfromETDtoETAforAir",
            // StringUtil.toSafeString(0) });
            // messageLists.add(message);
            // }
            // Integer airEtdLeadtime = cf.getAirEtdLeadtime();
            // if (airEtdLeadtime < 0) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromExpIbdtoETDforAir",
            // StringUtil.toSafeString(0) });
            // messageLists.add(message);
            // }
            // Integer seaEtaLeadtime = cf.getSeaEtaLeadtime();
            // if (seaEtaLeadtime < 0) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_LTfromETDtoETAforSea",
            // StringUtil.toSafeString(0) });
            // messageLists.add(message);
            // }
            // Integer seaInboundLeadtime = cf.getSeaInboundLeadtime();
            // if (seaInboundLeadtime < 0) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_WorkLTfromETAtoImpIbforSea",
            // StringUtil.toSafeString(0) });
            // messageLists.add(message);
            // }
            //
            // // check partsType
            // String partsType = cf.getPartsType();
            // if (!StringUtil.isNullOrEmpty(partsType)) {
            // List<BaseMessage> partsTypeMsgs = cpmpmf11CommonService.checkCodeCategory(partsType, partsTypeList,
            // "CPMPMF01_Grid_PartType", rowNum, sheetName);
            // if (partsTypeMsgs != null && partsTypeMsgs.size() > 0) {
            // messageLists.addAll(partsTypeMsgs);
            // } else {
            // Integer codeValue = CodeCategoryManager
            // .getCodeValue(lang, CodeMasterCategory.PARTS_TYPE, partsType);
            // cf.setPartsType(codeValue + "");
            // }
            // }
            //
            // // check ttcPartsNo + ssmsMainRoute + expSuppCode + expCustCode ,related to more then one ttcSuppCode
            // // check xlsx
            // String valueTsee = tseeMaps.get(keyt);
            // if (valueTsee == null || ttcSuppCode.equals(valueTsee)) {
            // tseeMaps.put(valueTsee, ttcSuppCode);
            // // check db
            // String valueTseeDB = tseeMapsDB.get(keyt);
            // if (valueTseeDB != null && !ttcSuppCode.equals(valueTseeDB)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_150);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData" });
            // messageLists.add(message);
            // }
            // } else {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_150);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile" });
            // messageLists.add(message);
            // }
            // shiyang del end

            // shiyang del start
            // // check delayAdjustmentPattern
            // String delayAdjustmentPattern = cf.getDelayAdjustmentPattern();
            // List<BaseMessage> buildoutFlagMsgs = cpmpmf11CommonService.checkCodeCategory(delayAdjustmentPattern,
            // onShipDelayAdjuP, "CPMPMF01_Grid_BuildOutIndicator", rowNum, sheetName);
            // if (buildoutFlagMsgs != null && buildoutFlagMsgs.size() > 0) {
            // messageLists.addAll(buildoutFlagMsgs);
            // } else {
            // Integer codeValue = CodeCategoryManager.getCodeValue(lang,
            // CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, delayAdjustmentPattern);
            // cf.setDelayAdjustmentPattern(codeValue + "");
            // }
            // shiyang del end

            // check ttcPartsNo + customerCode in officeCode has more then one ttcSuppCode
            boolean tctFlag = true;
            String keytcot = officeCode + StringConst.UNDERLINE + ttcPartsNo + StringConst.UNDERLINE + customerCode;
            CPMPMF11Entity beanValue = (CPMPMF11Entity) otctMaps.get(keytcot);
            if (beanValue == null) {
                if ((InactiveFlag.ACTIVE + "").equals(cf.getInactiveFlag())) {
                    otctMaps.put(keytcot, cf);
                }
                // shiyang del start
                // tctFlag = false;
                // shiyang del end
            } else {
                // check xlsx is or not change
                List<BaseMessage> beanChangeMsgs = cpmpmf11CommonService.checkItemChange(cf, beanValue, rowNum,
                    sheetName, "file", BusinessPattern.V_V);
                if (beanChangeMsgs != null && beanChangeMsgs.size() > 0) {
                    messageLists.addAll(beanChangeMsgs);
                    // shiyang del start
                    // } else {
                    // shiyang del end
                    tctFlag = false;
                }
            }
            // shiyang mod start
            // if (!tctFlag) {
            if (tctFlag) {
                // shiyang mod end
                // check DB is or not change
                Integer valuetcotDB = otctCountMapsDB.get(keytcot);
                Integer valuetcot = xlsxCountMaps.get(keytcot);
                if ((valuetcotDB != null && !valuetcotDB.equals(valuetcot))
                        || (valuetcot != null && !valuetcot.equals(valuetcotDB))) {
                    CPMPMF11Entity beanValueDB = (CPMPMF11Entity) otctMapsDB.get(keytcot);
                    List<BaseMessage> beanChangeDBMsgs = cpmpmf11CommonService.checkItemChange(cf, beanValueDB, rowNum,
                        sheetName, "db", BusinessPattern.V_V);
                    if (beanChangeDBMsgs != null && beanChangeDBMsgs.size() > 0) {
                        messageLists.addAll(beanChangeDBMsgs);
                    }
                } else {
                    // add partsId
                    otcpMaps.put(keytcot, partsIdTCEOValue);
                }
            }

            resultDataList.add(cf);
        }

        // shiyang add start
        cpmpmf11CommonService.checkRelationOutLoop(messageLists, impWhsCRegionMsgMaps, ttcSCNameRegMsgMap,
            shipRCodeOEMsgMap);
        // shiyang add end

        // deal all inactiveFlag is Y
        List<Integer> idList = new ArrayList<Integer>();
        if (otctCountMapsDB != null && otctCountMapsDB.size() > 0) {
            for (Map.Entry<String, Integer> entry : otctCountMapsDB.entrySet()) {
                Integer value = entry.getValue();
                if (0 == value) {
                    Integer valueid = otcpMaps.get(entry.getKey());
                    if (valueid != null) {
                        idList.add(valueid);
                    }
                }
            }
        }

        maps.put("dataList", resultDataList);
        maps.put("messageLists", messageLists);
        maps.put("idList", idList);
        return maps;
    }
}
