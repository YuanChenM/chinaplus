/**
 * @screen CPMPMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.common.consts.CodeConst.TypeYN;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DecimalUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;

/**
 * CPMPMF11CommonService.
 */
@Service
public class CPMPMF11CommonService extends BaseService {

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

    /**
     * cpmpmf11AisinService.
     */
    @Autowired
    private CPMPMF11StyleService cpmpmf11StyleService;

    /**
     * check Both VV and AISIN
     * 
     * @param dataMap dataMap
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> checkBothVVAISIN(Map<String, Object> dataMap) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        List<CPMPMF11Entity> rusultDataList = (List<CPMPMF11Entity>) dataMap.get("rusultDataList");
        // shiyang del start
        // Integer lang = (Integer) dataMap.get("lang");
        // Locale langs = (Locale) dataMap.get("langs");
        //
        // // query all ttcPartsNo
        // Map<String, Object> ttcPartsNosMap = cpmpmf11DBService.getTtcPartsNo();
        // Map<String, String> ttcPartsNoMaps = (Map<String, String>) ttcPartsNosMap.get("ttcPartsNoMaps");
        // Map<String, String> oldTtcPartsNoMaps = (Map<String, String>) ttcPartsNosMap.get("ttcPartsNoMaps");
        //
        // // query all expUomCode
        // Map<String, String> expUomCodeMap = cpmpmf11DBService.getExpUomCode();
        // // query all expRegion,impRegion
        // Map<String, String> regionMap = cpmpmf11DBService.getRegion();
        // // query all impRegion by customerCode + officeCode
        // Map<String, String> impRegionMap = cpmpmf11DBService.getImpRegion();
        // // query all customerCode + customerName
        // Map<String, String> customerCodeNameMap = cpmpmf11DBService.getCustomerCodeName();
        // // query officeCode + expRegion + toEtd + inactiveFlag + shippingRouteType by shippingRouteCode
        // Map<String, String> shippingRouteCodeMap = cpmpmf11DBService.getShippingRouteCode(rusultDataList);
        //
        // String[] busTypeList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE);
        // String[] custStockFlagList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG);
        // String[] inventoryByBoxList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX);
        // String[] simulationEndDayPList = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.SIMULATION_END_DAY_P);
        // String[] custForecastAdjustP1List = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.CUST_FORECAST_ADJUST_P1);
        // String[] custForecastAdjustP2List = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.CUST_FORECAST_ADJUST_P2);
        // String[] buildOutIndicatorList = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.BUILD_OUT_INDICATOR);
        // String[] discontinueIndicatorList = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.DISCONTINUE_INDICATOR);
        //
        // String discontinueIndicatorN = CodeCategoryManager.getCodeName(lang,
        // CodeMasterCategory.DISCONTINUE_INDICATOR,
        // 0);
        // shiyang del start
        // String inventoryByBoxN = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX,
        // IntDef.INT_ZERO);
        // String inventoryByBoxY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX,
        // IntDef.INT_ONE);
        // shiyang del end

        // // DB time now
        // long dbTime = cpmsrf11Service.getDBtime();
        //
        // // xlsx ttcSuppCode + supplierName
        // Map<String, String> ttcSuppCodeNameMap = new HashMap<String, String>();
        // // xlsx ttcSuppCode + expRegion
        // Map<String, String> ttcSuppCodeExpRegMap = new HashMap<String, String>();
        // // xlsx ttcSuppCode + supplierName + expRegion msg
        // Map<String, String> ttcSCNameRegMsgMap = new HashMap<String, String>();
        // // xlsx shippingRouteCode + officeCode
        // Map<String, String> shipRCodeOffCgMap = new HashMap<String, String>();
        // // xlsx shippingRouteCode + expRegion
        // Map<String, String> shipRCodeExpRgMap = new HashMap<String, String>();
        // // shippingRouteCode + officeCode + expRegion msg
        // Map<String, String> shipRCodeOEMsgMap = new HashMap<String, String>();
        //
        // // DB query impWhsCode + impRegion
        // Map<String, String> impWhsCodeRegionMapDB = cpmpmf11DBService.getImpWhsCodeRegion(rusultDataList);
        // // xlsx impWhsCode + impRegion
        // Map<String, String> impWhsCodeRegionMap = new HashMap<String, String>();
        // // check impWhsCode + impRegion msg
        // Map<String, String> impWhsCRegionMsgMaps = new HashMap<String, String>();

        // for (CPMPMF11Entity cf : rusultDataList) {

        // if (cf.isPartsStatusFlag()) {
        // continue;
        // }

        // String sheetName = cf.getSheetName();
        // String rowNum = StringUtil.toSafeString(cf.getRowNum());

        // // check oldTtcPartsNo
        // String oldTtcPartsNo = cf.getOldTtcPartsNo();
        // if (!StringUtil.isNullOrEmpty(oldTtcPartsNo)) {
        // String valueTtcPartsNo = ttcPartsNoMaps.get(oldTtcPartsNo);
        // if (valueTtcPartsNo == null) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OldTTCPN",
        // "CPMPMF01_Grid_PartsMasterData" });
        // messageLists.add(message);
        // } else {
        // String valueOldTtcPartsNo = oldTtcPartsNoMaps.get(oldTtcPartsNo);
        // String ttcPartsNo = cf.getTtcPartsNo();
        // if (!StringUtil.isNullOrEmpty(valueOldTtcPartsNo) && !ttcPartsNo.equals(valueOldTtcPartsNo)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_012);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OldTTCPN",
        // "CPMPMF01_Grid_TTCPN" });
        // messageLists.add(message);
        // }
        // }
        // }

        // // check expUomCode
        // String expUomCode = cf.getExpUomCode();
        // String valueExpUomCode = expUomCodeMap.get(expUomCode);
        // if (valueExpUomCode == null) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
        // message
        // .setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_UOM", "CPMPMF01_Grid_UOMMaster" });
        // messageLists.add(message);
        // }

        // // check expRegion
        // String expRegion = cf.getExpRegion();
        // String valueExpRegion = regionMap.get(expRegion);
        // if (valueExpRegion == null) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportCountry",
        // "CPMPMF01_Grid_RegionMaster" });
        // messageLists.add(message);
        // }

        // // check impRegion
        // String impRegion = cf.getImpRegion();
        // String valueImpRegion = regionMap.get(impRegion);
        // if (valueImpRegion == null) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ImpCountry",
        // "CPMPMF01_Grid_RegionMaster" });
        // messageLists.add(message);
        // } else {
        // String valueImpRegionTI = impRegionMap.get(cf.getOfficeCode());
        // if (valueImpRegionTI == null) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
        // "CPMPMF01_Grid_ImpOfficeCd" });
        // messageLists.add(message);
        // }
        // }

        // // check customerName
        // String customerCode = cf.getCustomerCode();
        // String valueCustomerName = customerCodeNameMap.get(customerCode);
        // String customerName = cf.getCustomerName();
        // if (!customerName.equals(valueCustomerName)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustName",
        // "CPMPMF01_Grid_TTCCustCd" });
        // messageLists.add(message);
        // }

        // // check orderLot,srbq,spq,spqM3
        // BigDecimal zeroBD = new BigDecimal(0);
        // BigDecimal orderLot = cf.getOrderLot();
        // if (orderLot.compareTo(zeroBD) <= 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderLot",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }
        // BigDecimal srbq = cf.getSrbq();
        // if (srbq.compareTo(zeroBD) <= 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SRBQ",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }
        // BigDecimal spq = cf.getSpq();
        // if (spq.compareTo(zeroBD) <= 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SPQ",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }
        // BigDecimal spqM3 = cf.getSpqM3();
        // if (spqM3 != null) {
        // if (spqM3.compareTo(zeroBD) <= 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_M3perbox",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }
        // }

        // // check businessType
        // String businessType = cf.getBusinessType();
        // List<BaseMessage> busTypeMsgs = checkCodeCategory(businessType, busTypeList, "CPMPMF01_Grid_BusinessType",
        // rowNum, sheetName);
        // if (busTypeMsgs != null && busTypeMsgs.size() > 0) {
        // messageLists.addAll(busTypeMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.BUSINESS_TYPE,
        // businessType);
        // cf.setBusinessType(codeValue + "");
        // }

        // // check targetMonth
        // Integer targetMonth = cf.getTargetMonth();
        // if (targetMonth < 1) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TargetMonth",
        // StringUtil.toSafeString(1) });
        // messageLists.add(message);
        // }

        // // check forecastNum
        // Integer forecastNum = cf.getForecastNum();
        // if (forecastNum < 1 || forecastNum > IntDef.INT_SIX) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_NoofOrderForeccast",
        // "CPMPMF01_Grid_ANumberOf" });
        // messageLists.add(message);
        // }

        // // check osCustStockFlag
        // String osCustStockFlag = cf.getOsCustStockFlag();
        // List<BaseMessage> osCustStockFlagMsgs = checkCodeCategory(osCustStockFlag, custStockFlagList,
        // "CPMPMF01_Grid_OrderSuggAlarm3includCustStockF", rowNum, sheetName);
        // if (osCustStockFlagMsgs != null && osCustStockFlagMsgs.size() > 0) {
        // messageLists.addAll(osCustStockFlagMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG,
        // osCustStockFlag);
        // cf.setOsCustStockFlag(codeValue + "");
        // }

        // // check saCustStockFlag
        // String saCustStockFlag = cf.getSaCustStockFlag();
        // List<BaseMessage> saCustStockFlagMsgs = checkCodeCategory(saCustStockFlag, custStockFlagList,
        // "CPMPMF01_Grid_SSAlarm12RundownincludCustStockF", rowNum, sheetName);
        // if (saCustStockFlagMsgs != null && saCustStockFlagMsgs.size() > 0) {
        // messageLists.addAll(saCustStockFlagMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG,
        // saCustStockFlag);
        // cf.setSaCustStockFlag(codeValue + "");
        // }

        // // check inventoryBoxFlag
        // String inventoryBoxFlag = cf.getInventoryBoxFlag();
        // List<BaseMessage> inventoryBoxFlagMsgs = checkCodeCategory(inventoryBoxFlag, inventoryByBoxList,
        // "CPMPMF01_Grid_InventoryControlbyBox", rowNum, sheetName);
        // if (inventoryBoxFlagMsgs != null && inventoryBoxFlagMsgs.size() > 0) {
        // messageLists.addAll(inventoryBoxFlagMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.INVENTORY_BY_BOX,
        // inventoryBoxFlag);
        // cf.setInventoryBoxFlag(codeValue + "");

        // shiyang del start
        // if (inventoryByBoxN.equals(inventoryBoxFlag)) {
        // // check maxStock
        // String maxStockS = cf.getMaxStockS();
        // List<BaseMessage> maxStockMsgs = cpmpmf11StyleService.checkInteger(maxStockS, IntDef.INT_THREE,
        // "CPMPMF01_Grid_MaxStockDays", rowNum, sheetName);
        // if (maxStockMsgs != null && maxStockMsgs.size() > 0) {
        // messageLists.addAll(maxStockMsgs);
        // } else {
        // cf.setMaxStock(Integer.valueOf(maxStockS));
        // }
        //
        // // check minStock
        // String minStockS = cf.getMinStockS();
        // List<BaseMessage> minStockMsgs = cpmpmf11StyleService.checkInteger(minStockS, IntDef.INT_THREE,
        // "CPMPMF01_Grid_MinStockDays", rowNum, sheetName);
        // if (minStockMsgs != null && minStockMsgs.size() > 0) {
        // messageLists.addAll(minStockMsgs);
        // } else {
        // cf.setMinStock(Integer.valueOf(minStockS));
        // }
        //
        // cf.setMaxBox(null);
        // cf.setMinBox(null);
        // } else if (inventoryByBoxY.equals(inventoryBoxFlag)) {
        // // check maxBox
        // String maxBoxS = cf.getMaxBoxS();
        // List<BaseMessage> maxBoxMsgs = cpmpmf11StyleService.checkInteger(maxBoxS, IntDef.INT_THREE,
        // "CPMPMF01_Grid_MaxNoOfBox", rowNum, sheetName);
        // if (maxBoxMsgs != null && maxBoxMsgs.size() > 0) {
        // messageLists.addAll(maxBoxMsgs);
        // } else {
        // cf.setMaxBox(Integer.valueOf(maxBoxS));
        // }
        //
        // // check minBox
        // String minBoxS = cf.getMinBoxS();
        // List<BaseMessage> minBoxMsgs = cpmpmf11StyleService.checkInteger(minBoxS, IntDef.INT_THREE,
        // "CPMPMF01_Grid_MinNoOfBox", rowNum, sheetName);
        // if (minBoxMsgs != null && minBoxMsgs.size() > 0) {
        // messageLists.addAll(minBoxMsgs);
        // } else {
        // cf.setMinBox(Integer.valueOf(minBoxS));
        // }
        //
        // cf.setMaxStock(null);
        // cf.setMinStock(null);
        // }
        // shiyang del end
        // }

        // // check outboundFluctuation
        // double outboundFluctuation = cf.getOutboundFluctuation();
        // if (outboundFluctuation < 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCImpWHObFlucPerc",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }

        // // check simulationEndDatePattern
        // String simulationEndDatePattern = cf.getSimulationEndDatePattern();
        // List<BaseMessage> simulationEndDayPMsgs = checkCodeCategory(simulationEndDatePattern,
        // simulationEndDayPList, "CPMPMF01_Grid_SimulationEndDatePatt", rowNum, sheetName);
        // if (simulationEndDayPMsgs != null && simulationEndDayPMsgs.size() > 0) {
        // messageLists.addAll(simulationEndDayPMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.SIMULATION_END_DAY_P,
        // simulationEndDatePattern);
        // cf.setSimulationEndDatePattern(codeValue + "");
        // }

        // // check shippingRouteCode + officeCode
        // String shippingRouteCode = cf.getShippingRouteCode();
        // String officeCode = cf.getOfficeCode();
        // String valueOffC = shipRCodeOffCgMap.get(shippingRouteCode);
        // if (null == valueOffC) {
        // shipRCodeOffCgMap.put(shippingRouteCode, officeCode);
        // } else if (!valueOffC.equals(officeCode)) {
        // shipRCodeOEMsgMap.put(sheetName + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ImpOfficeCd",
        // MessageCodeConst.W1004_151);
        // }
        //
        // // check shippingRouteCode + expRegion
        // String valueExpR = shipRCodeExpRgMap.get(shippingRouteCode);
        // if (null == valueExpR) {
        // shipRCodeExpRgMap.put(shippingRouteCode, expRegion);
        // } else if (!valueExpR.equals(expRegion)) {
        // shipRCodeOEMsgMap.put(sheetName + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ExportCountry",
        // MessageCodeConst.W1004_151);
        // }
        //
        // // check shippingRouteCode
        // String valueOETT = shippingRouteCodeMap.get(shippingRouteCode);
        // if (valueOETT != null) {
        // String[] valueOETTList = valueOETT.split(StringConst.DOUBLE_UNDERLINE);
        // // compare officeCode
        // String officeCodeDB = valueOETTList[0];
        // if (!StringUtil.isNullOrEmpty(officeCodeDB) && !officeCode.equals(officeCodeDB)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
        // "CPMPMF01_Grid_ImpOfficeCd" });
        // messageLists.add(message);
        // }
        // // compare expRegion
        // String expRegionDB = valueOETTList[1];
        // if (!StringUtil.isNullOrEmpty(expRegionDB) && !expRegion.equals(expRegionDB)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
        // "CPMPMF01_Grid_ExportCountry" });
        // messageLists.add(message);
        // }
        //
        // // compare toEtd
        // String toEtdDB = valueOETTList[IntDef.INT_TWO];
        // long toEtdLDB = Long.parseLong(toEtdDB);
        // if (toEtdLDB < dbTime) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_120);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode" });
        // messageLists.add(message);
        // }
        //
        // // compare shippingRouteType
        // String shippingRouteTypeDB = valueOETTList[IntDef.INT_THREE];
        // String shippingRouteType = cf.getBusinessPattern();
        // if ((BusinessPattern.V_V + "").equals(shippingRouteType)) {
        // if (!(ShippingRouteType.VV + "").equals(shippingRouteTypeDB)) {
        // String shipBP = shippingRouteCode + MessageManager.getMessage("CPMPMF01_Grid_Of", langs)
        // + MessageManager.getMessage("CPMPMF01_Grid_BusinessPattern", langs);
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, shipBP,
        // "CPMPMF01_Grid_PartsBusinessPattern" });
        // messageLists.add(message);
        // }
        // } else if ((BusinessPattern.AISIN + "").equals(shippingRouteType)) {
        // if (!(ShippingRouteType.AISIN_TTSH + "").equals(shippingRouteTypeDB)
        // && !(ShippingRouteType.AISIN_TTTJ + "").equals(shippingRouteTypeDB)) {
        // String shipBP = shippingRouteCode + MessageManager.getMessage("CPMPMF01_Grid_Of", langs)
        // + MessageManager.getMessage("CPMPMF01_Grid_BusinessPattern", langs);
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, shipBP,
        // "CPMPMF01_Grid_PartsBusinessPattern" });
        // messageLists.add(message);
        // }
        // }
        // } else {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
        // "CPMSRF11_Grid_ShippingRouteMaster" });
        // messageLists.add(message);
        // }

        // // check allocationFcType
        // boolean allocationFcTypeFlag = checkAllocationType(cf.getAllocationFcType());
        // if (!allocationFcTypeFlag) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_009);
        // message.setMessageArgs(new String[] { rowNum, sheetName,
        // "CPMPMF01_Grid_DailyAllocTypeforMonthCustForecast" });
        // messageLists.add(message);
        // }

        // // check cfcAdjustmentType1
        // String cfcAdjustmentType1 = cf.getCfcAdjustmentType1();
        // List<BaseMessage> cfcAdjustmentType1Msgs = checkCodeCategory(cfcAdjustmentType1, custForecastAdjustP1List,
        // "CPMPMF01_Grid_CustForecastPAdjustPatternCase1", rowNum, sheetName);
        // if (cfcAdjustmentType1Msgs != null && cfcAdjustmentType1Msgs.size() > 0) {
        // messageLists.addAll(cfcAdjustmentType1Msgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P1,
        // cfcAdjustmentType1);
        // cf.setCfcAdjustmentType1(codeValue + "");
        // }

        // // check cfcAdjustmentType2
        // String cfcAdjustmentType2 = cf.getCfcAdjustmentType2();
        // List<BaseMessage> cfcAdjustmentType2Msgs = checkCodeCategory(cfcAdjustmentType2, custForecastAdjustP2List,
        // "CPMPMF01_Grid_CustForecastPAdjustPatternCase2", rowNum, sheetName);
        // if (cfcAdjustmentType2Msgs != null && cfcAdjustmentType2Msgs.size() > 0) {
        // messageLists.addAll(cfcAdjustmentType2Msgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P2,
        // cfcAdjustmentType2);
        // cf.setCfcAdjustmentType2(codeValue + "");
        // }

        // // check buildoutFlag
        // String buildoutFlag = cf.getBuildoutFlag();
        // List<BaseMessage> buildoutFlagMsgs = checkCodeCategory(buildoutFlag, buildOutIndicatorList,
        // "CPMPMF01_Grid_BuildOutIndicator", rowNum, sheetName);
        // if (buildoutFlagMsgs != null && buildoutFlagMsgs.size() > 0) {
        // messageLists.addAll(buildoutFlagMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.BUILD_OUT_INDICATOR,
        // buildoutFlag);
        // cf.setBuildoutFlag(codeValue + "");
        // }

        // // check inactiveFlag
        // String inactiveFlag = cf.getInactiveFlag();
        // if (cf.getType().equals(ShipType.NEW)) {
        // if (!discontinueIndicatorN.equals(inactiveFlag)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator",
        // discontinueIndicatorN });
        // messageLists.add(message);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang,
        // CodeMasterCategory.DISCONTINUE_INDICATOR, inactiveFlag);
        // cf.setInactiveFlag(codeValue + "");
        // }
        // } else if (cf.getType().equals(ShipType.MOD)) {
        // List<BaseMessage> inactiveFlagMsgs = checkCodeCategory(inactiveFlag, discontinueIndicatorList,
        // "CPMPMF01_Grid_DiscontinueIndicator", rowNum, sheetName);
        // if (inactiveFlagMsgs != null && inactiveFlagMsgs.size() > 0) {
        // messageLists.addAll(inactiveFlagMsgs);
        // } else {
        // Integer codeValue = CodeCategoryManager.getCodeValue(lang,
        // CodeMasterCategory.DISCONTINUE_INDICATOR, inactiveFlag);
        // cf.setInactiveFlag(codeValue + "");
        // }
        // }

        // // check ttcSuppCode + supplierName
        // String ttcSuppCode = cf.getTtcSuppCode();
        // String supplierName = cf.getSupplierName();
        // String valuets = ttcSuppCodeNameMap.get(ttcSuppCode);
        // if (valuets == null) {
        // ttcSuppCodeNameMap.put(ttcSuppCode, supplierName);
        // } else if (!supplierName.equals(valuets)) {
        // String keyts = ttcSuppCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_SuppName"
        // + StringConst.DOUBLE_UNDERLINE + sheetName;
        // ttcSCNameRegMsgMap.put(keyts, ttcSuppCode);
        // }
        //
        // // check ttcSuppCode + expRegion
        // String valuete = ttcSuppCodeExpRegMap.get(ttcSuppCode);
        // if (valuete == null) {
        // ttcSuppCodeExpRegMap.put(ttcSuppCode, expRegion);
        // } else if (!expRegion.equals(valuete)) {
        // String keyte = ttcSuppCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ExportCountry"
        // + StringConst.DOUBLE_UNDERLINE + sheetName;
        // ttcSCNameRegMsgMap.put(keyte, ttcSuppCode);
        // }
        //
        // // check xlsx impWhsCode + impRegion
        // String impWhsCode = cf.getImpWhsCode();
        // String valueii = impWhsCodeRegionMap.get(impWhsCode);
        // if (valueii == null || impRegion.equals(valueii)) {
        // impWhsCodeRegionMap.put(impWhsCode, impRegion);
        // // check DB impWhsCode + impRegion
        // String valueiiDB = impWhsCodeRegionMapDB.get(impWhsCode);
        // if (valueiiDB == null) {
        // cf.setImpWhsCodeType(ShipType.NEW);
        // } else if (!impRegion.equals(valueiiDB)) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCImpWHCd",
        // "CPMPMF01_Grid_ImpCountry" });
        // messageLists.add(message);
        // }
        // } else if (!impRegion.equals(valueii)) {
        // String keyii = impWhsCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ImpCountry"
        // + StringConst.UNDERLINE + sheetName;
        // impWhsCRegionMsgMaps.put(keyii, impWhsCode);
        // }

        // }

        // if (impWhsCRegionMsgMaps != null && impWhsCRegionMsgMaps.size() > 0) {
        // for (Map.Entry<String, String> entry : impWhsCRegionMsgMaps.entrySet()) {
        // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_151);
        // message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[1], keys[0],
        // "CPMSRF11_Grid_UploadFile" });
        // messageLists.add(message);
        // }
        // }
        // if (ttcSCNameRegMsgMap != null && ttcSCNameRegMsgMap.size() > 0) {
        // for (Map.Entry<String, String> entry : ttcSCNameRegMsgMap.entrySet()) {
        // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_151);
        // message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[1], keys[0],
        // "CPMSRF11_Grid_UploadFile" });
        // messageLists.add(message);
        // }
        // }
        // if (shipRCodeOEMsgMap != null && shipRCodeOEMsgMap.size() > 0) {
        // for (Map.Entry<String, String> entry : shipRCodeOEMsgMap.entrySet()) {
        // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
        // BaseMessage message = new BaseMessage(entry.getValue());
        // message.setMessageArgs(new String[] { keys[0], "CPMPMF01_Grid_ShippingRouteCode", keys[1],
        // "CPMSRF11_Grid_UploadFile" });
        // messageLists.add(message);
        // }
        // }
        // shiyang del end

        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("messageLists", messageLists);
        maps.put("rusultDataList", rusultDataList);
        return maps;
    }

    /**
     * check confirm Both VV and AISIN
     * 
     * @param dataList dataList
     * @param vvFlag vvFlag
     * @return maps maps
     */
    public Map<String, Object> checkConfirmBothVVAISIN(List<CPMPMF11Entity> dataList, boolean vvFlag) {
        Map<String, Object> maps = new HashMap<String, Object>();
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Map<String, String> confirmMap = new HashMap<String, String>();
        Map<String, String> confirmESMap = new HashMap<String, String>();

        // query expRegion,supplierName by ttcSuppCode
        Map<String, String> expReSuNameMap = cpmpmf11DBService.getExpReSuNameBySupCode(dataList);
        // query ssmsVendorRoute by expCustCode
        Map<String, String> ssmsVendorRouteMap = new HashMap<String, String>();
        if (vvFlag) {
            ssmsVendorRouteMap = cpmpmf11DBService.getSsmsVendorRoute(dataList);
        }

        for (CPMPMF11Entity cf : dataList) {

            if (cf.isPartsStatusFlag()) {
                continue;
            }

            String rowNum = StringUtil.toSafeString(cf.getRowNum());
            String sheetName = cf.getSheetName();

            // check ttcSuppCode
            String ttcSuppCode = cf.getTtcSuppCode();
            String valuetes = expReSuNameMap.get(ttcSuppCode);
            if (valuetes == null) {
                cf.setTtcSuppCodeType(ShipType.NEW);
            } else {
                String[] valuetess = valuetes.split(StringConst.DOUBLE_UNDERLINE);
                // check expRegion + ttcSuppCode
                String expRegion = cf.getExpRegion();
                if (!expRegion.equals(valuetess[0])) {
                    cf.setTtcSuppCodeType(ShipType.MOD);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportCountry",
                        "CPMPMF01_Grid_TTCSuppCd" });
                    messageLists.add(message);
                    confirmMap.put("CPMPMF01_Grid_ExportCountry", MessageCodeConst.C1011);
                }

                // check supplierName + ttcSuppCode
                String supplierName = cf.getSupplierName();
                if (!supplierName.equals(valuetess[1])) {
                    cf.setTtcSuppCodeType(ShipType.MOD);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppName",
                        "CPMPMF01_Grid_TTCSuppCd" });
                    messageLists.add(message);
                    confirmMap.put("CPMPMF01_Grid_SuppName", MessageCodeConst.C1011);
                }

                // set supplierId
                String supplierId = valuetess[IntDef.INT_TWO];
                if (!StringUtil.isNullOrEmpty(supplierId)) {
                    cf.setSupplierId(Integer.valueOf(supplierId));
                }
            }

            if ((BusinessPattern.V_V + "").equals(cf.getBusinessPattern())) {
                // expCustCode + ssmsVendorRoute
                String keyes = cf.getExpCustCode() + StringConst.UNDERLINE + cf.getSsmsVendorRoute();
                String valuees = ssmsVendorRouteMap.get(keyes);
                if (valuees == null) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SSMSVendorRoute",
                        "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                    messageLists.add(message);
                    confirmESMap.put("CPMPMF01_Grid_SuppName", MessageCodeConst.C1025);
                }
            }
        }

        // add confirm msg
        if (confirmMap != null && confirmMap.size() > 0) {
            for (Map.Entry<String, String> entry : confirmMap.entrySet()) {
                BaseMessage message = new BaseMessage(entry.getValue());
                message.setMessageArgs(new String[] { entry.getKey() });
                messageLists.add(message);
            }
        }
        if (confirmESMap != null && confirmESMap.size() > 0) {
            for (Map.Entry<String, String> entry : confirmESMap.entrySet()) {
                BaseMessage message = new BaseMessage(entry.getValue());
                messageLists.add(message);
            }
        }

        maps.put("confirmMsgList", messageLists);
        maps.put("dataList", dataList);

        return maps;
    }

    /**
     * check is or not change
     * 
     * @param cf cf
     * @param value value
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @param type type
     * @param typeVA typeVA
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkItemChange(CPMPMF11Entity cf, CPMPMF11Entity value, String rowNum, String sheetName,
        String type, Integer typeVA) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (value == null) {
            return messageLists;
        }
        List<String> msg = new ArrayList<String>();

        if (BusinessPattern.V_V == typeVA) {
            // if (!cf.getSsmsMainRoute().equals(value.getSsmsMainRoute())) {
            // msg.add("CPMPMF01_Grid_SSMSMainRoute");
            // }
            // if (!cf.getSsmsVendorRoute().equals(value.getSsmsVendorRoute())) {
            // msg.add("CPMPMF01_Grid_SSMSVendorRoute");
            // }
            if (!cf.getWestCustCode().equals(value.getWestCustCode())) {
                msg.add("CPMPMF01_Grid_WESTCustCd");
            }
            if ("file".equals(type)) {
                if (!cf.getOrderTime().equals(value.getOrderTime())) {
                    msg.add("CPMPMF01_Grid_OrderTime");
                }
            }
            if ("db".equals(type)) {
                if (!cf.getOrderTime().equals("D-" + value.getOrderDay())) {
                    msg.add("CPMPMF01_Grid_OrderTime");
                }
            }
            if (!cf.getExpCalendarCode().equals(value.getExpCalendarCode())) {
                msg.add("CPMPMF01_Grid_ExportWHCalendar");
            }
            // if (!cf.getPartsStatus().equals(value.getPartsStatus())) {
            // msg.add("CPMPMF01_Grid_PartStatus");
            // }
            if (!cf.getWestPartsNo().equals(value.getWestPartsNo())) {
                msg.add("CPMPMF01_Grid_WESTPN");
            }
        }

        if (BusinessPattern.AISIN == typeVA) {
            if (!cf.getInvCustCode().equals(value.getInvCustCode())) {
                msg.add("CPMPMF01_Grid_MailInvCustCd");
            }
            if (!cf.getExpCustCode().equals(value.getExpCustCode())) {
                msg.add("CPMPMF01_Grid_CustCdinSSMSORKANB");
            }
        }

        // 1-10
        if (!cf.getTtcPartsName().equals(value.getTtcPartsName())) {
            msg.add("CPMPMF01_Grid_PartsDescriptionEn");
        }
        if (!cf.getPartsNameCn().equals(value.getPartsNameCn())) {
            msg.add("CPMPMF01_Grid_PartsDescriptionCh");
        }
        if (!checkNullItem(cf.getOldTtcPartsNo(), value.getOldTtcPartsNo())) {
            msg.add("CPMPMF01_Grid_OldTTCPN");
        }
        if (!cf.getExpUomCode().equals(value.getExpUomCode())) {
            msg.add("CPMPMF01_Grid_UOM");
        }
        if (!cf.getImpRegion().equals(value.getImpRegion())) {
            msg.add("CPMPMF01_Grid_ImpCountry");
        }
        if (!cf.getCustomerName().equals(value.getCustomerName())) {
            msg.add("CPMPMF01_Grid_CustName");
        }
        if (!cf.getCustPartsNo().equals(value.getCustPartsNo())) {
            msg.add("CPMPMF01_Grid_CustPN");
        }
        if (!checkNullItem(cf.getCustBackNo(), value.getCustBackNo())) {
            msg.add("CPMPMF01_Grid_CustBackNo");
        }
        if (!cf.getImpWhsCode().equals(value.getImpWhsCode())) {
            msg.add("CPMPMF01_Grid_TTCImpWHCd");
        }

        // 11-20
        // shiyang mod start
        // if (!cf.getOrderLot().equals(value.getOrderLot())) {
        if (DecimalUtil.getBigDecimal(cf.getOrderLot()).compareTo(DecimalUtil.getBigDecimal(value.getOrderLot())) != 0) {
            // shiyang mod end
            msg.add("CPMPMF01_Grid_OrderLot");
        }
        // shiyang mod start
        // if (!cf.getSpq().equals(value.getSpq())) {
        if (DecimalUtil.getBigDecimal(cf.getSpq()).compareTo(DecimalUtil.getBigDecimal(value.getSpq())) != 0) {
            // shiyang mod end
            msg.add("CPMPMF01_Grid_SPQ");
        }
        // shiyang mod start
        // if (!checkNullItem(cf.getSpqM3() + "", value.getSpqM3() + "")) {
        if (DecimalUtil.getBigDecimal(cf.getSpqM3()).compareTo(DecimalUtil.getBigDecimal(value.getSpqM3())) != 0) {
            // shiyang mod end
            msg.add("CPMPMF01_Grid_M3perbox");
        }
        if (!cf.getBusinessPattern().equals(value.getBusinessPattern())) {
            msg.add("CPMPMF01_Grid_BusinessPattern");
        }
        if (!cf.getBusinessType().equals(value.getBusinessType())) {
            msg.add("CPMPMF01_Grid_BusinessType");
        }

        if (!checkNullItem(cf.getPartsType(), value.getPartsType())) {
            msg.add("CPMPMF01_Grid_PartType");
        }
        if (!checkNullItem(cf.getCarModel(), value.getCarModel())) {
            msg.add("CPMPMF01_Grid_CarModel");
        }
        if (!cf.getTargetMonth().equals(value.getTargetMonth())) {
            msg.add("CPMPMF01_Grid_TargetMonth");
        }
        if (!cf.getForecastNum().equals(value.getForecastNum())) {
            msg.add("CPMPMF01_Grid_NoofOrderForeccast");
        }
        if (!cf.getOrderFcType().equals(value.getOrderFcType())) {
            msg.add("CPMPMF01_Grid_TypeofOrderForecast");
        }
        // 21-30
        if (!cf.getOsCustStockFlag().equals(value.getOsCustStockFlag())) {
            msg.add("CPMPMF01_Grid_OrderSuggAlarm3includCustStockF");
        }
        if (!cf.getSaCustStockFlag().equals(value.getSaCustStockFlag())) {
            msg.add("CPMPMF01_Grid_SSAlarm12RundownincludCustStockF");
        }
        if (!cf.getInventoryBoxFlag().equals(value.getInventoryBoxFlag())) {
            msg.add("CPMPMF01_Grid_InventoryControlbyBox");
        }
        if (!checkNullItem(cf.getMinStock() + "", value.getMinStock() + "")) {
            msg.add("CPMPMF01_Grid_MinStockDays");
        }
        if (!checkNullItem(cf.getMaxStock() + "", value.getMaxStock() + "")) {
            msg.add("CPMPMF01_Grid_MaxStockDays");
        }
        if (!checkNullItem(cf.getMinBox() + "", value.getMinBox() + "")) {
            msg.add("CPMPMF01_Grid_MinNoOfBox");
        }
        if (!checkNullItem(cf.getMaxBox() + "", value.getMaxBox() + "")) {
            msg.add("CPMPMF01_Grid_MaxNoOfBox");
        }
        if (!cf.getOrderSafetyStock().equals(value.getOrderSafetyStock())) {
            msg.add("CPMPMF01_Grid_SafeStockDayForOrderSuggAlarm3");
        }
        if (!cf.getRundownSafetyStock().equals(value.getRundownSafetyStock())) {
            msg.add("CPMPMF01_Grid_SafeStockDayForRundown");
        }
        if (!cf.getOutboundFluctuation().equals(value.getOutboundFluctuation())) {
            msg.add("CPMPMF01_Grid_TTCImpWHObFlucPerc");
        }

        // 31-34
        if (!cf.getSimulationEndDatePattern().equals(value.getSimulationEndDatePattern())) {
            msg.add("CPMPMF01_Grid_SimulationEndDatePatt");
        }
        if (!cf.getAllocationFcType().equals(value.getAllocationFcType())) {
            msg.add("CPMPMF01_Grid_DailyAllocTypeforMonthCustForecast");
        }
        if (!cf.getCfcAdjustmentType1().equals(value.getCfcAdjustmentType1())) {
            msg.add("CPMPMF01_Grid_CustForecastPAdjustPatternCase1");
        }
        if (!cf.getCfcAdjustmentType2().equals(value.getCfcAdjustmentType2())) {
            msg.add("CPMPMF01_Grid_CustForecastPAdjustPatternCase2");
        }

        if (msg != null && msg.size() > 0) {
            String code = "";
            String msgArgs = "";
            if ("db".equals(type)) {
                code = MessageCodeConst.W1004_149;
                // shiyang mod start
                // msgArgs = "CPMPMF01_Grid_PartsMasterData";
                msgArgs = value.getTtcPartsNo();
                // shiyang mod end
            }
            if ("file".equals(type)) {
                code = MessageCodeConst.W1004_061;
                msgArgs = "CPMSRF11_Grid_UploadFile";
            }
            for (String s : msg) {
                BaseMessage message = new BaseMessage(code);
                // shiyang mod start
                // message.setMessageArgs(new String[] { rowNum, sheetName, s, msgArgs });
                message.setMessageArgs(new String[] { rowNum, sheetName, s, msgArgs, value.getCustomerCode() });
                // shiyang mod end
                messageLists.add(message);
            }
        }
        return messageLists;
    }

    /**
     * check Null Item
     * 
     * @param name name
     * @param nameV nameV
     * @return boolean boolean
     */
    private boolean checkNullItem(String name, String nameV) {
        if (!StringUtil.isNullOrEmpty(name) && !StringUtil.isNullOrEmpty(nameV)) {
            if (!name.equals(nameV)) {
                return false;
            }
        } else if (StringUtil.isNullOrEmpty(name) && !StringUtil.isNullOrEmpty(nameV)) {
            return false;
        } else if (!StringUtil.isNullOrEmpty(name) && StringUtil.isNullOrEmpty(nameV)) {
            return false;
        }
        return true;
    }

    /**
     * check Code Category
     * 
     * @param name name
     * @param nameList nameList
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> checkCodeCategory(String name, String[] nameList, String itemName, String rowNum,
        String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        boolean flag = false;
        StringBuilder sb = new StringBuilder();
        if (nameList != null && nameList.length > 0) {
            for (String s : nameList) {
                sb.append(s + ",");
                // shiyang mod start
                // if (name.equals(s)) {
                if (name.equalsIgnoreCase(s)) {
                    // shiyang mod end
                    flag = true;
                }
            }
        }
        if (!flag) {
            String msg = sb.toString();
            if (msg != null) {
                msg = msg.substring(0, msg.length() - 1);
            }
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, msg });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * give up one Code Category
     * 
     * @param name name
     * @param nameList nameList
     * @param itemName itemName
     * @param rowNum rowNum
     * @param sheetName sheetName
     * @return messageLists messageLists
     */
    public List<BaseMessage> giveUpOneCodeCategory(String name, String[] nameList, String itemName, String rowNum,
        String sheetName) {
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (nameList != null && nameList.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : nameList) {
                // shiyang mod start
                // if (!name.equals(s)) {
                if (!name.equalsIgnoreCase(s)) {
                    // shiyang mod end
                    sb.append(s + ",");
                }
            }
            String msg = sb.toString();
            if (msg != null) {
                msg = msg.substring(0, msg.length() - 1);
            }
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
            message.setMessageArgs(new String[] { rowNum, sheetName, itemName, msg });
            messageLists.add(message);
        }
        return messageLists;
    }

    /**
     * check allocationType
     * 
     * @param allocationType allocationType
     * @return boolean boolean
     */
    private boolean checkAllocationType(String allocationType) {

        // {D}
        Pattern patternDaily = Pattern.compile("^D$");
        Matcher matcherDaily = patternDaily.matcher(allocationType);
        if (matcherDaily.matches()) {
            return checkRepeatAllocationTypes(allocationType);
        }

        // {D1},{D1,D2}
        Pattern patternFixedDay = Pattern
            .compile("(^D([1-9]\\b|[1-2][0-9]\\b|3[0-1]\\b)(,\\s{0,1}D([1-9]\\b|[1-2][0-9]\\b|3[0-1]\\b)){0,30})$");
        Matcher matcherFixedDay = patternFixedDay.matcher(allocationType);
        if (matcherFixedDay.matches()) {
            return checkRepeatAllocationTypes(allocationType);
        }

        // {W1},{W1,W2}
        Pattern patternWeekly = Pattern.compile("(^W[1-5](,\\s{0,1}W[1-5]){0,4})$");
        Matcher matcherWeekly = patternWeekly.matcher(allocationType);
        if (matcherWeekly.matches()) {
            return checkRepeatAllocationTypes(allocationType);
        }

        // {W1:Mon},{W1:Mon,W2:Tue}
        Pattern patternWD = Pattern
            .compile("^W[1-5]:\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)(,\\s{0,1}W[1-5]:\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)){0,30}$");
        Matcher matcherWD = patternWD.matcher(allocationType);
        if (matcherWD.matches()) {
            return checkRepeatAllocationTypes(allocationType);
        }

        // {Mon},{Mon,Tue}
        Pattern patternEvetyWD = Pattern
            .compile("^(Mon|Tue|Wed|Thu|Fri|Sat|Sun)(,\\s{0,1}(Mon|Tue|Wed|Thu|Fri|Sat|Sun)){0,6}$");
        Matcher matcherEvetyWD = patternEvetyWD.matcher(allocationType);
        if (matcherEvetyWD.matches()) {
            return checkRepeatAllocationTypes(allocationType);
        }

        return false;
    }

    /**
     * check repeat
     * 
     * @param allocationType allocationType
     * @return boolean boolean
     */
    private boolean checkRepeatAllocationTypes(String allocationType) {
        String[] allocationTypes = allocationType.split(StringConst.COMMA);
        List<String> list = Arrays.asList(allocationTypes);
        if ((new HashSet<String>(list).size() == list.size())) {
            return true;
        }
        return false;
    }

    /**
     * xlsx mod count (officeCode + ttcPartsNo + customerCode)
     * 
     * @param dataList dataList
     * @param otctCountMapsDB otctCountMapsDB
     * @param otciYNMapsDB otciYNMapsDB
     * @param typeVA typeVA
     * @return maps maps
     */
    public Map<String, Object> getOtctCountMaps(List<CPMPMF11Entity> dataList, Map<String, Integer> otctCountMapsDB,
        Map<String, String> otciYNMapsDB, int typeVA) {
        Map<String, Object> maps = new HashMap<String, Object>();

        Map<String, Integer> xlsxCountMaps = new HashMap<String, Integer>();

        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                String keyc = cf.getOfficeCode() + StringConst.UNDERLINE + cf.getTtcPartsNo() + StringConst.UNDERLINE
                        + cf.getCustomerCode();
                String key = "";
                // vv
                if (BusinessPattern.V_V == typeVA) {
                    key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode() + StringConst.UNDERLINE
                            + cf.getSsmsMainRoute() + StringConst.UNDERLINE + cf.getExpSuppCode()
                            + StringConst.UNDERLINE + cf.getCustomerCode() + StringConst.UNDERLINE
                            + cf.getExpCustCode();
                }
                // aisin
                if (BusinessPattern.AISIN == typeVA) {
                    key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getTtcSuppCode() + StringConst.UNDERLINE
                            + cf.getCustomerCode() + StringConst.UNDERLINE + cf.getSuppPartsNo()
                            + StringConst.UNDERLINE + cf.getExpCustCode() + StringConst.UNDERLINE + cf.getCustPartsNo();
                }
                String value = otciYNMapsDB.get(key);
                if (value != null) {
                    String inactiveFlag = cf.getInactiveFlag();
                    if (TypeYN.N.equals(inactiveFlag) && (InactiveFlag.INACTIVE + "").equals(value)) {
                        Integer count = otctCountMapsDB.get(keyc);
                        if (count == null) {
                            otctCountMapsDB.put(keyc, 1);
                        } else {
                            count = count + IntDef.INT_ONE;
                            otctCountMapsDB.put(keyc, count);
                        }
                    } else if (TypeYN.Y.equals(inactiveFlag) && (InactiveFlag.ACTIVE + "").equals(value)) {
                        Integer count = otctCountMapsDB.get(keyc);
                        count = count - IntDef.INT_ONE;
                        otctCountMapsDB.put(keyc, count);
                    }
                }

                if (TypeYN.N.equals(cf.getInactiveFlag())) {
                    Integer countc = xlsxCountMaps.get(keyc);
                    if (countc == null) {
                        xlsxCountMaps.put(keyc, IntDef.INT_ONE);
                    } else {
                        countc = countc + IntDef.INT_ONE;
                        xlsxCountMaps.put(keyc, countc);
                    }
                }
            }
        }

        maps.put("xlsxCountMaps", xlsxCountMaps);
        maps.put("otctCountMapsDB", otctCountMapsDB);

        return maps;
    }

    /**
     * check OldTtcPartsNo common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param ttcPartsNoMaps ttcPartsNoMaps
     * @param oldTtcPartsNoMaps oldTtcPartsNoMaps
     * @param oldTtcPartsNoFileMaps oldTtcPartsNoFileMaps
     * @param ttcPartNoList ttcPartNoList in file
     */
    public void checkOldTtcPartsNo(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> ttcPartsNoMaps, Map<String, String> oldTtcPartsNoMaps,
        Map<String, String> oldTtcPartsNoFileMaps, List<String> ttcPartNoList) {
        String oldTtcPartsNo = cf.getOldTtcPartsNo();
        if (!StringUtil.isNullOrEmpty(oldTtcPartsNo)) {
            String valueTtcPartsNo = ttcPartsNoMaps.get(oldTtcPartsNo);
            if (valueTtcPartsNo == null && !ttcPartNoList.contains(oldTtcPartsNo)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OldTTCPN",
                    "CPMPMF01_Grid_PartsMasterData" });
                messageLists.add(message);
            } else {
                String ttcPartsNo = cf.getTtcPartsNo();
                boolean checkOldTtcPart = false;
                // check file
                String ttcPartsNoFile = oldTtcPartsNoFileMaps.get(oldTtcPartsNo);
                if (!StringUtil.isNullOrEmpty(ttcPartsNoFile)) {
                    if (!ttcPartsNo.equals(ttcPartsNoFile)) {
                        checkOldTtcPart = true;
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_012);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OldTTCPN",
                            "CPMPMF01_Grid_TTCPN" });
                        // 2016/07/01 shiyang del start
                        // messageLists.add(message);
                        // 2016/07/01 shiyang del end
                    }
                } else {
                    oldTtcPartsNoFileMaps.put(oldTtcPartsNo, ttcPartsNo);
                }

                // check db
                String ttcPartsNoDb = oldTtcPartsNoMaps.get(oldTtcPartsNo);
                if (!checkOldTtcPart && !StringUtil.isNullOrEmpty(ttcPartsNoDb) && !ttcPartsNo.equals(ttcPartsNoDb)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_012);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OldTTCPN",
                        "CPMPMF01_Grid_TTCPN" });
                    // 2016/07/01 shiyang del start
                    // messageLists.add(message);
                    // 2016/07/01 shiyang del end
                }
            }
        }
    }

    /**
     * check ExpUomCode common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param expUomCodeMap expUomCodeMap
     */
    public void checkExpUomCode(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> expUomCodeMap) {
        String expUomCode = cf.getExpUomCode();
        if (!expUomCodeMap.containsKey(expUomCode)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_UOM", "CPMPMF01_Grid_UOMMaster" });
            messageLists.add(message);
        }
    }

    /**
     * check ExpUomCode common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param regionMap regionMap
     */
    public void checkExpRegion(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> regionMap) {
        String expRegion = cf.getExpRegion();
        if (!regionMap.containsKey(expRegion)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ExportCountry",
                "CPMPMF01_Grid_RegionMaster" });
            messageLists.add(message);
        }
    }

    /**
     * check ImpRegion common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param regionMap regionMap
     * @param impRegionMap impRegionMap
     */
    public void checkImpRegion(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> regionMap, Map<String, String> impRegionMap) {
        String impRegion = cf.getImpRegion();
        if (!regionMap.containsKey(impRegion)) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ImpCountry",
                "CPMPMF01_Grid_RegionMaster" });
            messageLists.add(message);
        } else {
            String valueImpRegionTI = impRegionMap.get(cf.getOfficeCode());
            if (valueImpRegionTI == null || !valueImpRegionTI.equals(impRegion)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ImpCountry",
                    "CPMPMF01_Grid_ImpOfficeCd" });
                messageLists.add(message);
            }
        }
    }

    /**
     * check CustomerCode common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param customerMap customerMap
     */
    public void checkCustomerCode(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> customerMap) {
        String customerCode = cf.getCustomerCode();
        String officeCode = cf.getOfficeCode();
        String businessPattern = cf.getBusinessPattern();
        String key = customerCode + StringConst.COMMA + officeCode + StringConst.COMMA + businessPattern;
        if (customerMap.containsKey(key)) {
            String customerNameTemp = customerMap.get(key);
            if (!cf.getCustomerName().equals(customerNameTemp)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustName",
                    "CPMPMF01_Grid_TTCCustCd" });
                messageLists.add(message);
            }
        } else {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                "CPMPMF01_Grid_CustomerMaster" });
            messageLists.add(message);
        }
    }

    /**
     * check OrderLotToSpqM3 common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     */
    public void checkOrderLotToSpqM3(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum) {
        BigDecimal zeroBD = new BigDecimal(0);
        int digits = MasterManager.getUomDigits(cf.getExpUomCode());

        BigDecimal orderLot = cf.getOrderLot();
        if (orderLot.compareTo(zeroBD) <= 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OrderLot",
                StringUtil.toSafeString(0) });
            messageLists.add(message);
        }
        if (!ValidatorUtils.checkMaxDecimal(orderLot, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
            // If vaue's is more than the max value in DB.(w1004_027)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
            message.setMessageArgs(new String[] { rowNum, "CPMPMF01_Grid_OrderLot", "10", String.valueOf(digits) });
            messageLists.add(message);
        }

        BigDecimal srbq = cf.getSrbq();
        if (srbq.compareTo(zeroBD) <= 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            message
                .setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SRBQ", StringUtil.toSafeString(0) });
            messageLists.add(message);
        }
        if (!ValidatorUtils.checkMaxDecimal(srbq, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
            // If vaue's is more than the max value in DB.(w1004_027)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
            message.setMessageArgs(new String[] { rowNum, "CPMPMF01_Grid_SRBQ", "10", String.valueOf(digits) });
            messageLists.add(message);
        }

        BigDecimal spq = cf.getSpq();
        if (spq.compareTo(zeroBD) <= 0) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SPQ", StringUtil.toSafeString(0) });
            messageLists.add(message);
        }
        if (!ValidatorUtils.checkMaxDecimal(spq, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
            // If vaue's is more than the max value in DB.(w1004_027)
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
            message.setMessageArgs(new String[] { rowNum, "CPMPMF01_Grid_SPQ", "10", String.valueOf(digits) });
            messageLists.add(message);
        }

        BigDecimal spqM3 = cf.getSpqM3();
        if (spqM3 != null) {
            if (spqM3.compareTo(zeroBD) <= 0) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_015);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_M3perbox",
                    StringUtil.toSafeString(0) });
                messageLists.add(message);
            }
            // if (!ValidatorUtils.checkMaxDecimal(spqM3, new BigDecimal(ChinaPlusConst.MAX_QTY), digits)) {
            // // If vaue's is more than the max value in DB.(w1004_027)
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_027);
            // message.setMessageArgs(new String[] { rowNum, "CPMPMF01_Grid_M3perbox", "10", String.valueOf(digits) });
            // messageLists.add(message);
            // }
        }
    }

    /**
     * check BusinessType common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param busTypeList busTypeList
     */
    public void checkBusinessType(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        int lang, String[] busTypeList) {
        String businessType = cf.getBusinessType();
        List<BaseMessage> busTypeMsgs = checkCodeCategory(businessType, busTypeList, "CPMPMF01_Grid_BusinessType",
            rowNum, sheetName);
        if (busTypeMsgs != null && busTypeMsgs.size() > 0) {
            messageLists.addAll(busTypeMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.BUSINESS_TYPE, businessType);
            cf.setBusinessType(codeValue + "");
        }
    }

    /**
     * check PartsType common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param partsTypeList partsTypeList
     */
    public void checkPartsType(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        int lang, String[] partsTypeList) {
        String partsType = cf.getPartsType();
        if (!StringUtil.isNullOrEmpty(partsType)) {
            List<BaseMessage> partsTypeMsgs = checkCodeCategory(partsType, partsTypeList, "CPMPMF01_Grid_PartType",
                rowNum, sheetName);
            if (partsTypeMsgs != null && partsTypeMsgs.size() > 0) {
                messageLists.addAll(partsTypeMsgs);
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.PARTS_TYPE, partsType);
                cf.setPartsType(codeValue + "");
            }
        }
    }

    /**
     * check TargetMonth common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     */
    public void checkTargetMonth(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum) {
        Integer targetMonth = cf.getTargetMonth();
        if (targetMonth < 1) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TargetMonth",
                StringUtil.toSafeString(1) });
            messageLists.add(message);
        }
    }

    /**
     * check ForecastNum common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     */
    public void checkForecastNum(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum) {
        Integer forecastNum = cf.getForecastNum();
        if (forecastNum < 1 || forecastNum > IntDef.INT_SIX) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_NoofOrderForeccast",
                "CPMPMF01_Grid_ANumberOf" });
            messageLists.add(message);
        }
    }

    /**
     * check OsCustStockFlag common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param custStockFlagList custStockFlagList
     */
    public void checkOsCustStockFlag(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] custStockFlagList) {
        String osCustStockFlag = cf.getOsCustStockFlag();
        List<BaseMessage> osCustStockFlagMsgs = checkCodeCategory(osCustStockFlag, custStockFlagList,
            "CPMPMF01_Grid_OrderSuggAlarm3includCustStockF", rowNum, sheetName);
        if (osCustStockFlagMsgs != null && osCustStockFlagMsgs.size() > 0) {
            messageLists.addAll(osCustStockFlagMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG,
                osCustStockFlag);
            cf.setOsCustStockFlag(codeValue + "");
        }
    }

    /**
     * check SaCustStockFlag common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param custStockFlagList custStockFlagList
     */
    public void checkSaCustStockFlag(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] custStockFlagList) {
        String saCustStockFlag = cf.getSaCustStockFlag();
        List<BaseMessage> saCustStockFlagMsgs = checkCodeCategory(saCustStockFlag, custStockFlagList,
            "CPMPMF01_Grid_SSAlarm12RundownincludCustStockF", rowNum, sheetName);
        if (saCustStockFlagMsgs != null && saCustStockFlagMsgs.size() > 0) {
            messageLists.addAll(saCustStockFlagMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG,
                saCustStockFlag);
            cf.setSaCustStockFlag(codeValue + "");
        }
    }

    /**
     * check InventoryBoxFlag common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param inventoryByBoxList inventoryByBoxList
     */
    public void checkInventoryBoxFlag(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] inventoryByBoxList) {
        String inventoryBoxFlag = cf.getInventoryBoxFlag();
        List<BaseMessage> inventoryBoxFlagMsgs = checkCodeCategory(inventoryBoxFlag, inventoryByBoxList,
            "CPMPMF01_Grid_InventoryControlbyBox", rowNum, sheetName);
        if (inventoryBoxFlagMsgs != null && inventoryBoxFlagMsgs.size() > 0) {
            messageLists.addAll(inventoryBoxFlagMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.INVENTORY_BY_BOX,
                inventoryBoxFlag);
            cf.setInventoryBoxFlag(codeValue + "");
        }
    }

    /**
     * check OutboundFluctuation common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     */
    public void checkOutboundFluctuation(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum) {
        // check in the style servcie
        // double outboundFluctuation = cf.getOutboundFluctuation();
        // if (outboundFluctuation < 0) {
        // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_019);
        // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCImpWHObFlucPerc",
        // StringUtil.toSafeString(0) });
        // messageLists.add(message);
        // }
    }

    /**
     * check SimulationEndDatePattern common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param simulationEndDayPList simulationEndDayPList
     */
    public void checkSimulationEndDatePattern(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] simulationEndDayPList) {
        String simulationEndDatePattern = cf.getSimulationEndDatePattern();
        List<BaseMessage> simulationEndDayPMsgs = checkCodeCategory(simulationEndDatePattern, simulationEndDayPList,
            "CPMPMF01_Grid_SimulationEndDatePatt", rowNum, sheetName);
        if (simulationEndDayPMsgs != null && simulationEndDayPMsgs.size() > 0) {
            messageLists.addAll(simulationEndDayPMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.SIMULATION_END_DAY_P,
                simulationEndDatePattern);
            cf.setSimulationEndDatePattern(codeValue + "");
        }
    }

    /**
     * check ShippingRouteCode common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param langs language
     * @param dbTime db date-time
     * @param shipRCodeOffCgMap shipRCodeOffCgMap
     * @param shipRCodeOEMsgMap shipRCodeOEMsgMap
     * @param shipRCodeExpRgMap shipRCodeExpRgMap
     * @param shippingRouteCodeMap shippingRouteCodeMap
     */
    public void checkShippingRouteCode(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, Locale langs, long dbTime, Map<String, String> shipRCodeOffCgMap,
        Map<String, String> shipRCodeOEMsgMap, Map<String, String> shipRCodeExpRgMap,
        Map<String, String> shippingRouteCodeMap) {
        String shippingRouteCode = cf.getShippingRouteCode();
        String officeCode = cf.getOfficeCode();
        String valueOffC = shipRCodeOffCgMap.get(shippingRouteCode);
        if (null == valueOffC) {
            shipRCodeOffCgMap.put(shippingRouteCode, officeCode);
        } else if (!valueOffC.equals(officeCode)) {
            shipRCodeOEMsgMap.put(sheetName + StringConst.COMMA + "CPMPMF01_Grid_ImpOfficeCd",
                MessageCodeConst.W1004_151);
        }

        // check shippingRouteCode + expRegion
        String expRegion = cf.getExpRegion();
        String valueExpR = shipRCodeExpRgMap.get(shippingRouteCode);
        if (null == valueExpR) {
            shipRCodeExpRgMap.put(shippingRouteCode, expRegion);
        } else if (!valueExpR.equals(expRegion)) {
            shipRCodeOEMsgMap.put(sheetName + StringConst.COMMA + "CPMPMF01_Grid_ExportCountry",
                MessageCodeConst.W1004_151);
        }

        // check shippingRouteCode
        String valueOETT = shippingRouteCodeMap.get(shippingRouteCode);
        if (valueOETT != null) {
            String[] valueOETTList = valueOETT.split(StringConst.COMMA);
            // compare officeCode
            String officeCodeDB = valueOETTList[0];
            if (!StringUtil.isNullOrEmpty(officeCodeDB) && !officeCode.equals(officeCodeDB)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                    "CPMPMF01_Grid_ImpOfficeCd" });
                messageLists.add(message);
            }
            // compare expRegion
            String expRegionDB = valueOETTList[1];
            if (!StringUtil.isNullOrEmpty(expRegionDB) && !expRegion.equals(expRegionDB)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                    "CPMPMF01_Grid_ExportCountry" });
                messageLists.add(message);
            }

            // compare toEtd
            String toEtdDB = valueOETTList[IntDef.INT_TWO];
            long toEtdLDB = Long.parseLong(toEtdDB);
            if (toEtdLDB < dbTime) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_120);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode" });
                messageLists.add(message);
            }

            // compare shippingRouteType
            String shippingRouteTypeDB = valueOETTList[IntDef.INT_THREE];
            String shippingRouteType = cf.getBusinessPattern();
            if ((BusinessPattern.V_V + "").equals(shippingRouteType)) {
                if (!(ShippingRouteType.VV + "").equals(shippingRouteTypeDB)) {
                    // String shipBP = shippingRouteCode + MessageManager.getMessage("CPMPMF01_Grid_Of", langs)
                    // + MessageManager.getMessage("CPMPMF01_Grid_BusinessPattern", langs);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                        "CPMPMF01_Grid_PartsBusinessPattern" });
                    messageLists.add(message);
                }
            } else if ((BusinessPattern.AISIN + "").equals(shippingRouteType)) {
                if (!(ShippingRouteType.AISIN_TTSH + "").equals(shippingRouteTypeDB)
                        && !(ShippingRouteType.AISIN_TTTJ + "").equals(shippingRouteTypeDB)) {
                    // String shipBP = shippingRouteCode + MessageManager.getMessage("CPMPMF01_Grid_Of", langs)
                    // + MessageManager.getMessage("CPMPMF01_Grid_BusinessPattern", langs);
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                        "CPMPMF01_Grid_PartsBusinessPattern" });
                    messageLists.add(message);
                }
            }
        } else {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_013);
            message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_ShippingRouteCode",
                "CPMSRF11_Grid_ShippingRouteMaster" });
            messageLists.add(message);
        }
    }

    /**
     * check AllocationType common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     */
    public void checkAllocationType(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum) {
        boolean allocationFcTypeFlag = checkAllocationType(cf.getAllocationFcType());
        if (!allocationFcTypeFlag) {
            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_009);
            message
                .setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DailyAllocTypeforMonthCustForecast" });
            messageLists.add(message);
        }
    }

    /**
     * check CfcAdjustmentType1 common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param custForecastAdjustP1List custForecastAdjustP1List
     */
    public void checkCfcAdjustmentType1(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] custForecastAdjustP1List) {
        String cfcAdjustmentType1 = cf.getCfcAdjustmentType1();
        List<BaseMessage> cfcAdjustmentType1Msgs = checkCodeCategory(cfcAdjustmentType1, custForecastAdjustP1List,
            "CPMPMF01_Grid_CustForecastPAdjustPatternCase1", rowNum, sheetName);
        if (cfcAdjustmentType1Msgs != null && cfcAdjustmentType1Msgs.size() > 0) {
            messageLists.addAll(cfcAdjustmentType1Msgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P1,
                cfcAdjustmentType1);
            cf.setCfcAdjustmentType1(codeValue + "");
        }
    }

    /**
     * check CfcAdjustmentType2 common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param custForecastAdjustP2List custForecastAdjustP2List
     */
    public void checkCfcAdjustmentType2(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName,
        String rowNum, int lang, String[] custForecastAdjustP2List) {
        String cfcAdjustmentType2 = cf.getCfcAdjustmentType2();
        List<BaseMessage> cfcAdjustmentType2Msgs = checkCodeCategory(cfcAdjustmentType2, custForecastAdjustP2List,
            "CPMPMF01_Grid_CustForecastPAdjustPatternCase2", rowNum, sheetName);
        if (cfcAdjustmentType2Msgs != null && cfcAdjustmentType2Msgs.size() > 0) {
            messageLists.addAll(cfcAdjustmentType2Msgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P2,
                cfcAdjustmentType2);
            cf.setCfcAdjustmentType2(codeValue + "");
        }
    }

    /**
     * check BuildoutFlag common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param buildOutIndicatorList buildOutIndicatorList
     */
    public void checkBuildoutFlag(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        int lang, String[] buildOutIndicatorList) {
        String buildoutFlag = cf.getBuildoutFlag();
        List<BaseMessage> buildoutFlagMsgs = checkCodeCategory(buildoutFlag, buildOutIndicatorList,
            "CPMPMF01_Grid_BuildOutIndicator", rowNum, sheetName);
        if (buildoutFlagMsgs != null && buildoutFlagMsgs.size() > 0) {
            messageLists.addAll(buildoutFlagMsgs);
        } else {
            Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.BUILD_OUT_INDICATOR,
                buildoutFlag);
            cf.setBuildoutFlag(codeValue + "");
        }
    }

    /**
     * check InactiveFlag common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param lang language
     * @param discontinueIndicatorN discontinueIndicatorN
     * @param discontinueIndicatorList discontinueIndicatorList
     */
    public void checkInactiveFlag(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        int lang, String discontinueIndicatorN, String[] discontinueIndicatorList) {
        String inactiveFlag = cf.getInactiveFlag();
        if (cf.getType().equals(ShipType.NEW)) {
            if (!discontinueIndicatorN.equals(inactiveFlag)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_DiscontinueIndicator",
                    discontinueIndicatorN });
                messageLists.add(message);
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.DISCONTINUE_INDICATOR,
                    inactiveFlag);
                cf.setInactiveFlag(codeValue + "");
            }
        } else if (cf.getType().equals(ShipType.MOD)) {
            List<BaseMessage> inactiveFlagMsgs = checkCodeCategory(inactiveFlag, discontinueIndicatorList,
                "CPMPMF01_Grid_DiscontinueIndicator", rowNum, sheetName);
            if (inactiveFlagMsgs != null && inactiveFlagMsgs.size() > 0) {
                messageLists.addAll(inactiveFlagMsgs);
            } else {
                Integer codeValue = CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.DISCONTINUE_INDICATOR,
                    inactiveFlag);
                cf.setInactiveFlag(codeValue + "");
            }
        }
    }

    /**
     * check RelationInLoop common
     * 
     * @param messageLists message list
     * @param cf input data
     * @param sheetName sheet name
     * @param rowNum row number
     * @param ttcSuppCodeNameMap ttcSuppCodeNameMap
     * @param ttcSCNameRegMsgMap ttcSCNameRegMsgMap
     * @param ttcSuppCodeExpRegMap ttcSuppCodeExpRegMap
     * @param impWhsCodeRegionMap impWhsCodeRegionMap
     * @param impWhsCodeRegionMapDB impWhsCodeRegionMapDB
     * @param impWhsCRegionMsgMaps impWhsCRegionMsgMaps
     */
    public void checkRelationInLoop(List<BaseMessage> messageLists, CPMPMF11Entity cf, String sheetName, String rowNum,
        Map<String, String> ttcSuppCodeNameMap, Map<String, String> ttcSCNameRegMsgMap,
        Map<String, String> ttcSuppCodeExpRegMap, Map<String, String> impWhsCodeRegionMap,
        Map<String, String> impWhsCodeRegionMapDB, Map<String, String> impWhsCRegionMsgMaps) {
        String ttcSuppCode = cf.getTtcSuppCode();
        String supplierName = cf.getSupplierName();
        String valuets = ttcSuppCodeNameMap.get(ttcSuppCode);
        if (valuets == null) {
            ttcSuppCodeNameMap.put(ttcSuppCode, supplierName);
        } else if (!supplierName.equals(valuets)) {
            // shiyang mod start
            // String keyts = ttcSuppCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_SuppName"
            // + StringConst.DOUBLE_UNDERLINE + sheetName;
            String keyts = "CPMPMF01_Grid_TTCSuppCd" + StringConst.COMMA + "CPMPMF01_Grid_SuppName" + StringConst.COMMA
                    + sheetName;
            // shiyang mod end
            ttcSCNameRegMsgMap.put(keyts, ttcSuppCode);
        }

        // check ttcSuppCode + expRegion
        String expRegion = cf.getExpRegion();
        String valuete = ttcSuppCodeExpRegMap.get(ttcSuppCode);
        if (valuete == null) {
            ttcSuppCodeExpRegMap.put(ttcSuppCode, expRegion);
        } else if (!expRegion.equals(valuete)) {
            // shiyang mod start
            // String keyte = ttcSuppCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ExportCountry"
            // + StringConst.DOUBLE_UNDERLINE + sheetName;
            String keyte = "CPMPMF01_Grid_TTCSuppCd" + StringConst.COMMA + "CPMPMF01_Grid_ExportCountry"
                    + StringConst.COMMA + sheetName;
            // shiyang mod end
            ttcSCNameRegMsgMap.put(keyte, ttcSuppCode);
        }

        // check xlsx impWhsCode + impRegion
        String impRegion = cf.getImpRegion();
        String impWhsCode = cf.getImpWhsCode();
        // shiyang add start
        if (!ChinaPlusConst.IMP_WH_CODE_DIRECT_SENDING.equals(impWhsCode)) {
            // shiyang add end
            String valueii = impWhsCodeRegionMap.get(impWhsCode);
            if (valueii == null || impRegion.equals(valueii)) {
                impWhsCodeRegionMap.put(impWhsCode, impRegion);
                // check DB impWhsCode + impRegion
                String valueiiDB = impWhsCodeRegionMapDB.get(impWhsCode);
                if (valueiiDB == null) {
                    cf.setImpWhsCodeType(ShipType.NEW);
                } else if (!impRegion.equals(valueiiDB)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_014);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCImpWHCd",
                        "CPMPMF01_Grid_ImpCountry" });
                    messageLists.add(message);
                }
            } else if (!impRegion.equals(valueii)) {
                // shiyang mod start
                // String keyii = impWhsCode + StringConst.DOUBLE_UNDERLINE + "CPMPMF01_Grid_ImpCountry"
                // + StringConst.UNDERLINE + sheetName;
                String keyii = "CPMPMF01_Grid_TTCImpWHCd" + StringConst.COMMA + "CPMPMF01_Grid_ImpCountry"
                        + StringConst.COMMA + sheetName;
                // shiyang mod end
                impWhsCRegionMsgMaps.put(keyii, impWhsCode);
            }
        }
    }

    /**
     * check RelationOutLoop common (output message)
     * 
     * @param messageLists message list
     * @param impWhsCRegionMsgMaps impWhsCRegionMsgMaps
     * @param ttcSCNameRegMsgMap ttcSCNameRegMsgMap
     * @param shipRCodeOEMsgMap shipRCodeOEMsgMap
     */
    public void checkRelationOutLoop(List<BaseMessage> messageLists, Map<String, String> impWhsCRegionMsgMaps,
        Map<String, String> ttcSCNameRegMsgMap, Map<String, String> shipRCodeOEMsgMap) {
        if (impWhsCRegionMsgMaps != null && impWhsCRegionMsgMaps.size() > 0) {
            for (Map.Entry<String, String> entry : impWhsCRegionMsgMaps.entrySet()) {
                // shiyang mod start
                // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
                String[] keys = entry.getKey().split(StringConst.COMMA);
                // shiyang mod end
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_151);
                // shiyang mod start
                // message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[1], keys[0],
                // "CPMSRF11_Grid_UploadFile" });
                message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[0], keys[1],
                    "CPMSRF11_Grid_UploadFile" });
                // shiyang mod end
                messageLists.add(message);
            }
        }
        if (ttcSCNameRegMsgMap != null && ttcSCNameRegMsgMap.size() > 0) {
            for (Map.Entry<String, String> entry : ttcSCNameRegMsgMap.entrySet()) {
                // shiyang mod start
                // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
                String[] keys = entry.getKey().split(StringConst.COMMA);
                // shiyang mod end
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_151);
                // shiyang mod start
                // message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[1], keys[0],
                // "CPMSRF11_Grid_UploadFile" });
                message.setMessageArgs(new String[] { keys[IntDef.INT_TWO], keys[0], keys[1],
                    "CPMSRF11_Grid_UploadFile" });
                // shiyang mod end
                messageLists.add(message);
            }
        }
        if (shipRCodeOEMsgMap != null && shipRCodeOEMsgMap.size() > 0) {
            for (Map.Entry<String, String> entry : shipRCodeOEMsgMap.entrySet()) {
                // shiyang mod start
                // String[] keys = entry.getKey().split(StringConst.DOUBLE_UNDERLINE);
                String[] keys = entry.getKey().split(StringConst.COMMA);
                // shiyang mod end
                BaseMessage message = new BaseMessage(entry.getValue());
                message.setMessageArgs(new String[] { keys[0], "CPMPMF01_Grid_ShippingRouteCode", keys[1],
                    "CPMSRF11_Grid_UploadFile" });
                messageLists.add(message);
            }
        }
    }
}
