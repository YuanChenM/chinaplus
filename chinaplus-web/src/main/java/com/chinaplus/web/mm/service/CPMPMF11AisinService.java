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
import com.chinaplus.common.consts.CodeConst.ShipType;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;

/**
 * CPMPMF11AisinService.
 */
@Service
public class CPMPMF11AisinService extends BaseService {

    /**
     * cpmpmf11DBService.
     */
    @Autowired
    private CPMPMF11DBService cpmpmf11DBService;

    /**
     * cpmpmf11DBAisinService.
     */
    @Autowired
    private CPMPMF11DBAisinService cpmpmf11DBAisinService;

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
     * deal aisin data detail
     * 
     * @param dataMaps dataMaps
     * @return maps maps
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> dealDataDetail(Map<String, Object> dataMaps) {
        List<CPMPMF11Entity> resultDataList = new ArrayList<CPMPMF11Entity>();
        List<CPMPMF11Entity> dataList = (List<CPMPMF11Entity>) dataMaps.get("dataListAisin");
        List<CPMPMF11Entity> newDataList = (List<CPMPMF11Entity>) dataMaps.get("newDataListAisin");
        List<CPMPMF11Entity> modDataList = (List<CPMPMF11Entity>) dataMaps.get("modDataListAisin");
        // shiyang add start
        List<String> ttcPartNoList = (List<String>) dataMaps.get("ttcPartNoListAisin");
        // shiyang add end

        Integer userId = (Integer) dataMaps.get("userId");
        Integer lang = (Integer) dataMaps.get("lang");
        // shiyang add start
        Locale langs = (Locale) dataMaps.get("langs");
        // shiyang add end

        // officeCode Map
        Map<String, Integer> officeCodeMap = (Map<String, Integer>) dataMaps.get("officeCodeMap");
        // customerCode Map
        // Map<String, String> customerCodeMap = (Map<String, String>) dataMaps.get("customerCodeMap");
        // officeCode + customerCode Map
        Map<String, String> officeCustCodeMap = (Map<String, String>) dataMaps.get("officeCustCodeMap");

        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        Map<String, Object> maps = new HashMap<String, Object>();

        // all DB customerCode + expCustCode
        Map<String, String> cutExpCodeMapsDB = new HashMap<String, String>();
        Map<String, String> cutCodeExpMapsDB = new HashMap<String, String>();
        cpmpmf11DBAisinService.getCutExpCode(cutExpCodeMapsDB, cutCodeExpMapsDB);
        // new xlsx customerCode + expCustCode
        Map<String, String> cutExpCodeMaps = new HashMap<String, String>();
        Map<String, String> cutCodeExpMaps = new HashMap<String, String>();
        // shiyang del start
        // Map<String, String> ttcsecMXMaps = new HashMap<String, String>();
        // shiyang del end
        // all xlsx expCustCode + invCustCode
        Map<String, String> eiAXMaps = new HashMap<String, String>();

        // new DB query ttcPartsNo + ttcSuppCode + customerId + suppPartsNo + expCustCode + custPartsNo
        Map<String, Object> ttcsecNewMapsDB = cpmpmf11DBAisinService.getTTCSECNewMap(newDataList);
        // new DB ttcPartsNo + ttcSuppCode + customerCode
        Map<String, String> ttcNXMapsNewDB = (Map<String, String>) ttcsecNewMapsDB.get("ttcNXMapsDB");
        // new DB suppPartsNo + ttcSuppCode + expCustCode
        Map<String, String> tteNXMapsNewDB = (Map<String, String>) ttcsecNewMapsDB.get("tteNXMapsDB");
        // new DB custPartsNo + ttcSuppCode + customerCode
        Map<String, String> ctcNXMapsNewDB = (Map<String, String>) ttcsecNewMapsDB.get("ctcNXMapsDB");

        // mod DB query ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
        Map<String, Object> ttcsecModMapsDB = cpmpmf11DBAisinService.getTTCSECModMap(modDataList);
        // Map<String, String> ttcsecMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("ttcsecMXMapsDB");
        // shiyang add start
        // // mod DB ttcPartsNo + ttcSuppCode + customerCode
        // Map<String, String> ttcMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("ttcMXMapsDB");
        // // mod DB suppPartsNo + ttcSuppCode + expCustCode
        // Map<String, String> tteMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("tteMXMapsDB");
        // // mod DB custPartsNo + ttcSuppCode + customerCode
        // Map<String, String> ctcMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("ctcMXMapsDB");
        // mod DB custPartsNo + officeCode + ttcSuppCode + customerCode
        Map<String, String> toscMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("toscMXMapsDB");
        // mod DB custPartsNo + officeCode + customerCode
        Map<String, String> tocMXMapsDB = (Map<String, String>) ttcsecModMapsDB.get("tocMXMapsDB");
        // mod FILE custPartsNo + officeCode + customerCode
        Map<String, String> tocMXMapsFile = new HashMap<String, String>();
        // shiyang add end

        // 4-4-2-2 check suppPartsNo related to more then one ttcPartsNo
        Map<String, String> stNXMapsModDB = cpmpmf11DBAisinService.getSuppPNo(modDataList);
        // 4-4-2-3 check custPartsNo related to more then one ttcPartsNo
        Map<String, String> ctNXMapsModDB = cpmpmf11DBAisinService.getCustPNo(modDataList);
        // shiyang add start
        Map<String, String> stNXMapsModFile = new HashMap<String, String>();
        Map<String, String> ctNXMapsModFile = new HashMap<String, String>();
        // shiyang add end

        // xlsx officeCode + ttcPartsNo + customerCode
        Map<String, Object> otctMaps = new HashMap<String, Object>();
        // DB all query officeCode + ttcPartsNo + customerCode , bean
        Map<String, Object> otctMapDB = cpmpmf11DBService.getOTCTMap(dataList, BusinessPattern.AISIN);
        Map<String, Object> otctMapsDB = (Map<String, Object>) otctMapDB.get("otctMaps");
        // DB all query count (officeCode + ttcPartsNo + customerCode)
        Map<String, Integer> otctCountMapsDB = (Map<String, Integer>) otctMapDB.get("otctCountMaps");
        // DB all query officeCode + ttcPartsNo + customerCode ,inactiveFlag
        Map<String, String> otciYNMapsDB = (Map<String, String>) otctMapDB.get("otciYNMapsDB");
        // shiyang add start
        // DB all query customerPartNo + officeCode + customerCode
        Map<String, String> custPartOfficeCustMapDB = (Map<String, String>) otctMapDB.get("custPartOfficeCustMapDB");
        // shiyang add end

        // xlsx mod count (officeCode + ttcPartsNo + customerCode)
        Map<String, Object> otciModMapsDB = cpmpmf11CommonService.getOtctCountMaps(dataList, otctCountMapsDB,
            otciYNMapsDB, BusinessPattern.AISIN);
        otctCountMapsDB = (Map<String, Integer>) otciModMapsDB.get("otctCountMapsDB");
        Map<String, Integer> xlsxCountMaps = (Map<String, Integer>) otciModMapsDB.get("xlsxCountMaps");

        // get CodeCategory name
        // shiyang del start
        // String[] partsTypeList = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE);
        // shiyang del end
        String onShipDelayAdjustPO = CodeCategoryManager.getCodeName(lang,
            CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, 1);
        String orderForecastTypeT = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
            IntDef.INT_NINE);
        String discontinueIndicatorY = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR,
            IntDef.INT_ONE);

        // DB all ttcPartsNo + customerCode + expCustCode + custPartsNo + officeCode
        Map<String, Object> tcecoMapDB = cpmpmf11DBAisinService.getTCSECMap(dataList);
        // DB ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode,inactiveFlag
        Map<String, String> tcceoMapsDB = (Map<String, String>) tcecoMapDB.get("tcceoMapsDB");
        // shiyang del start
        // DB count(ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode)
        Map<String, Integer> tcceoCountMapsDB = (Map<String, Integer>) tcecoMapDB.get("tcceoCountMapsDB");
        // shiyang del end
        // DB ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode,partsId
        Map<String, Integer> tcceoIdMapsDB = (Map<String, Integer>) tcecoMapDB.get("tcceoIdMapsDB");
        // shiyang add start
        // DB ttcPartsNo + officeCode + customerCode, partsId
        Map<String, Integer> partsIdMapsDB = (Map<String, Integer>) tcecoMapDB.get("partsIdMapsDB");
        // shiyang add end

        // shiyang add start
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
        // Get relation of Mail Invoice Customer Code and Customer Code in SSMS OR KANB invoice & Supplier Kanban Issue
        // Plan file.
        Map<String, String> invKanbanCustMap = cpmpmf11DBService.getRelaInvKanbanCust();

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
        // check duplicate for NEW and MOD in file (ttcPartsNo + ttcSuppCode + customerCode)
        List<String> checkFileDupTtcList = new ArrayList<String>();
        // check duplicate for NEW and MOD in file (suppPartsNo + ttcSuppCode + expCustCode)
        List<String> checkFileDupSupList = new ArrayList<String>();
        // check duplicate for NEW and MOD in file (custPartsNo + ttcSuppCode + customerCode)
        List<String> checkFileDupCustList = new ArrayList<String>();
        // key: Customer Part No. + Imp Office Code + TTC Customer Code, value: TTC Part No.
        Map<String, String> custPartOfficeCustMap = new HashMap<String, String>();

        // DB query impWhsCode + impRegion
        Map<String, String> impWhsCodeRegionMapDB = cpmpmf11DBService.getImpWhsCodeRegion(dataList);
        // xlsx impWhsCode + impRegion
        Map<String, String> impWhsCodeRegionMap = new HashMap<String, String>();
        // check impWhsCode + impRegion msg
        Map<String, String> impWhsCRegionMsgMaps = new HashMap<String, String>();
        // the list of partsId which has to update to inactive (all update to active,
        // then update to inactive if one of N is exist)
        // List<Integer> idList = new ArrayList<Integer>();
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
                        && !discontinueIndicatorY.equals(cf.getInactiveFlag())) {
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
                } else if (!(BusinessPattern.AISIN + "").equals(valueCustomerCodes[0])) {
                    // 2016/07/26 mod shiyang start
                    // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_011);
                    // message.setMessageArgs(new String[] { rowNum, sheetName, BusinessPatternName.AISIN,
                    // "CPMPMF01_Grid_PartsMasterData" });
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_184);
                    message.setMessageArgs(new String[] { rowNum, sheetName, BusinessPatternName.AISIN });
                    // 2016/07/26 mod shiyang end
                    messageLists.add(message);
                    continue;
                } else {
                    cf.setCustomerId(Integer.valueOf(valueCustomerCodes[1]));
                }
            }

            String expCustCode = cf.getExpCustCode();
            String ttcPartsNo = cf.getTtcPartsNo();
            String ttcSuppCode = cf.getTtcSuppCode();
            String suppPartsNo = cf.getSuppPartsNo();
            String custPartsNo = cf.getCustPartsNo();

            // shiyang mod start
            // String keyttc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + customerCode;
            String keyttc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + customerCode
                    + StringConst.UNDERLINE + officeCode;
            // shiyang mod end
            String keytte = suppPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + expCustCode;
            // shiyang mod start
            // String keyctc = custPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + customerCode;
            String keyctc = custPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE + customerCode
                    + StringConst.UNDERLINE + officeCode;
            // shiyang mod end

            // shiyang mod start
            // String keyttcsec = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE +
            // customerCode
            // + StringConst.UNDERLINE + suppPartsNo + StringConst.UNDERLINE + expCustCode + StringConst.UNDERLINE
            // + custPartsNo;
            // String keyccteo = ttcPartsNo + StringConst.UNDERLINE + customerCode + StringConst.UNDERLINE + custPartsNo
            // + StringConst.UNDERLINE + expCustCode + StringConst.UNDERLINE + officeCode;
            String keytosc = ttcPartsNo + StringConst.UNDERLINE + officeCode + StringConst.UNDERLINE + ttcSuppCode
                    + StringConst.UNDERLINE + customerCode;
            String keytoc = ttcPartsNo + StringConst.UNDERLINE + officeCode + StringConst.UNDERLINE + customerCode;
            String keyccteo = ttcPartsNo + StringConst.UNDERLINE + customerCode + StringConst.UNDERLINE + custPartsNo
                    + StringConst.UNDERLINE + expCustCode + StringConst.UNDERLINE + officeCode + StringConst.UNDERLINE
                    + ttcSuppCode;
            // shiyang mod end

            // shiyang del start
            // boolean repFlag = true;
            // // check xlsx ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
            // String valuettcsec = ttcsecMXMaps.get(keyttcsec);
            // if (!StringUtil.isNullOrEmpty(valuettcsec)) {
            // repFlag = false;
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_158);
            // message.setMessageArgs(new String[] { sheetName, "CPMSRF11_Grid_UploadFile", rowNum });
            // messageLists.add(message);
            // } else {
            // ttcsecMXMaps.put(keyttcsec, ttcPartsNo);
            // }
            // shiyang del end

            // deal type mod
            String type = cf.getType();
            if (type.equals(ShipType.MOD)) {
                // shiyang mod start
                // if (repFlag) {
                // // check DB ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
                // String valuettcsecDB = ttcsecMXMapsDB.get(keyttcsec);
                // if (null == valuettcsecDB) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_125);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                // "CPMPMF01_Grid_TTCPN", ttcPartsNo });
                // messageLists.add(message);
                // continue;
                // } else {
                // String[] valuettcsecDBs = valuettcsecDB.split(StringConst.UNDERLINE);
                // cf.setExpPartsId(Integer.valueOf(valuettcsecDBs[0]));
                //
                // // set shipRouteCodeChange
                // String shippingRouteCode = cf.getShippingRouteCode();
                // if (!shippingRouteCode.equals(valuettcsecDBs[1])) {
                // cf.setShipRouteCodeChange(true);
                // }
                // }
                // }
                if (!toscMXMapsDB.containsKey(keytosc)) {
                    // check DB exist ttcPartsNo + officeCode + ttcSuppCode + customerCode
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_125);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                        "CPMPMF01_Grid_TTCPN", ttcPartsNo });
                    messageLists.add(message);
                } else {
                    String valuettcsecDB = toscMXMapsDB.get(keytosc);
                    // shiyang mod start
                    // String[] valuettcsecDBs = valuettcsecDB.split(StringConst.UNDERLINE);
                    String[] valuettcsecDBs = valuettcsecDB.split(StringConst.NEW_COMMA);
                    // shiyang mod end
                    cf.setExpPartsId(Integer.valueOf(valuettcsecDBs[0]));
                    // set shipRouteCodeChange
                    String shippingRouteCode = cf.getShippingRouteCode();
                    if (!shippingRouteCode.equals(valuettcsecDBs[1])) {
                        cf.setShipRouteCodeChange(true);
                    }
                }
                // shiyang mod end

                // // shiyang add start (check in type=mod, not type=new)
                // // check File suppPartsNo related to more then one ttcPartsNo
                // if (stNXMapsModFile.containsKey(suppPartsNo)) {
                // String ttcPartsNoTemp = stNXMapsModFile.get(suppPartsNo);
                // if (!ttcPartsNo.equals(ttcPartsNoTemp)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                // } else {
                // stNXMapsModFile.put(suppPartsNo, ttcPartsNo);
                //
                // // check DB suppPartsNo related to more then one ttcPartsNo
                // String valuest = stNXMapsModDB.get(suppPartsNo);
                // if (!StringUtil.isNullOrEmpty(valuest) && !ttcPartsNo.equals(valuest)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                // }
                //
                // // check File custPartsNo related to more then one ttcPartsNo
                // if (ctNXMapsModFile.containsKey(custPartsNo)) {
                // String ttcPartsNoTemp = ctNXMapsModFile.get(custPartsNo);
                // if (!ttcPartsNo.equals(ttcPartsNoTemp)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                // } else {
                // ctNXMapsModFile.put(custPartsNo, ttcPartsNo);
                //
                // // check DB custPartsNo related to more then one ttcPartsNo
                // String valuect = ctNXMapsModDB.get(custPartsNo);
                // if (!StringUtil.isNullOrEmpty(valuect) && !ttcPartsNo.equals(valuect)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                // }
                // // shiyang add end
            }

            // deal type new
            boolean errorDuplSupCode = false;
            if (type.equals(ShipType.NEW)) {
                // shiyang del start
                // if (repFlag) {
                // int i = 0;
                // shiyang del end
                // check DB exist ttcPartsNo + ttcSuppCode + customerCode + officeCode
                String valuettcDB = ttcNXMapsNewDB.get(keyttc);
                if (!StringUtil.isNullOrEmpty(valuettcDB)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                        "CPMPMF01_Grid_TTCPN", ttcPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                        "CPMPMF01_Grid_TTCCustCd", customerCode });
                    messageLists.add(message);
                    // shiyang del start
                    // i++;
                    // shiyang del end
                }
                // check DB exist suppPartsNo + ttcSuppCode + expCustCode
                String valuetteDB = tteNXMapsNewDB.get(keytte);
                if (!StringUtil.isNullOrEmpty(valuetteDB)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                        "CPMPMF01_Grid_SuppPN", suppPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                        "CPMPMF01_Grid_CustCdinSSMSORKANB", expCustCode });
                    messageLists.add(message);
                    // shiyang del start
                    // i++;
                    // shiyang del end
                }
                // check DB exist custPartsNo + ttcSuppCode + customerCode + officeCode
                String valuectcDB = ctcNXMapsNewDB.get(keyctc);
                if (!StringUtil.isNullOrEmpty(valuectcDB)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_PartsMasterData",
                        "CPMPMF01_Grid_CustPN", custPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                        "CPMPMF01_Grid_TTCCustCd", customerCode });
                    messageLists.add(message);
                    // shiyang del start
                    // i++;
                    // shiyang del end
                }

                // shiyang del start
                // if (i > 0) {
                // continue;
                // }
                // }
                // shiyang del end

                // check xlsx customerCode + expCustCode
                // shiyang mod start
                // String valueExpCustCode = cutExpCodeMaps.get(customerCode);
                // if (valueExpCustCode == null || expCustCode.equals(valueExpCustCode)) {
                // cutExpCodeMaps.put(customerCode, expCustCode);
                // //check DB customerCode + expCustCode
                // String keyceDB = customerCode + StringConst.UNDERLINE + expCustCode;
                // String valueceDB = cutExpCodeMapsDB.get(keyceDB);
                // if (StringUtil.isNullOrEmpty(valueceDB)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                // messageLists.add(message);
                // }
                // } else {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                // messageLists.add(message);
                // }

                // 2016/08/24 del by shiyang start (not do this check only for NEW, for NEW and MOD)
                // boolean isKanbanCustError = false;
                // String keyCustOffice = customerCode + StringConst.NEW_COMMA + officeCode;
                // if (cutExpCodeMapsDB.containsKey(keyCustOffice)
                // && !expCustCode.equals(cutExpCodeMapsDB.get(keyCustOffice))) {
                // isKanbanCustError = true;
                // }
                // if (cutCodeExpMapsDB.containsKey(expCustCode)
                // && !keyCustOffice.equals(cutCodeExpMapsDB.get(expCustCode))) {
                // isKanbanCustError = true;
                // }
                // if (!isKanbanCustError) {
                // if (cutExpCodeMaps.containsKey(keyCustOffice)
                // && !expCustCode.equals(cutExpCodeMaps.get(keyCustOffice))) {
                // isKanbanCustError = true;
                // }
                // if (cutCodeExpMaps.containsKey(expCustCode)
                // && !keyCustOffice.equals(cutCodeExpMaps.get(expCustCode))) {
                // isKanbanCustError = true;
                // }
                // }
                // if (isKanbanCustError) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                // messageLists.add(message);
                // }
                // if (!cutExpCodeMaps.containsKey(keyCustOffice)) {
                // cutExpCodeMaps.put(keyCustOffice, expCustCode);
                // }
                // if (!cutCodeExpMaps.containsKey(expCustCode)) {
                // cutCodeExpMaps.put(expCustCode, keyCustOffice);
                // }
                // 2016/08/24 del by shiyang end

                // if (cutExpCodeMapsDB.containsKey(keyCustOffice)) {
                // String expCustCodeDb = cutExpCodeMapsDB.get(keyCustOffice);
                // if (!expCustCode.equals(expCustCodeDb)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                // messageLists.add(message);
                // }
                // } else {
                // if (cutExpCodeMaps.containsKey(keyCustOffice)) {
                // String expCustCodeFile = cutExpCodeMaps.get(keyCustOffice);
                // if (!expCustCode.equals(expCustCodeFile)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                // "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                // messageLists.add(message);
                // }
                // }
                // }
                // if (!cutExpCodeMaps.containsKey(keyCustOffice)) {
                // cutExpCodeMaps.put(keyCustOffice, expCustCode);
                // }
                // shiyang mod end

                // shiyang del start (check in type=mod, not type=new)
                // // 4-4-2-2 check suppPartsNo related to more then one ttcPartsNo
                // String valuest = stNXMapsModDB.get(suppPartsNo);
                // if (!StringUtil.isNullOrEmpty(valuest) && !ttcPartsNo.equals(valuest)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                //
                // // 4-4-2-3 check custPartsNo related to more then one ttcPartsNo
                // String valuect = ctNXMapsModDB.get(custPartsNo);
                // if (!StringUtil.isNullOrEmpty(valuect) && !ttcPartsNo.equals(valuect)) {
                // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPN",
                // "CPMPMF01_Grid_TTCPN" });
                // messageLists.add(message);
                // }
                // shiyang del end

                // set shipRouteCodeChange
                cf.setShipRouteCodeChange(true);

                // For one set of TTC Part No. + Imp Office Code + TTC Customer Code, if there have duplicate TTC
                // Supplier Code.(w1004_156)
                if (tocMXMapsDB.containsKey(keytoc)) {
                    String ttcSuppCodeTemp = tocMXMapsDB.get(keytoc);
                    if (ttcSuppCodeTemp.equals(ttcSuppCode)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_156);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCSuppCd",
                            "CPMPMF01_Grid_TTC_Office_Cust" });
                        messageLists.add(message);
                        errorDuplSupCode = true;
                    }
                }
            }

            // 2016/08/24 add by shiyang start (not do this check only for NEW, for NEW and MOD)
            // Check if TTC Customer Code is not related with Customer Code in SSMS OR KANB invoice & Supplier Kanban
            // Issue Plan file one to one.
            // For same TTC Customer Code has different Customer Code in SSMS OR KANB invoice & Supplier Kanban Issue
            // Plan file in upload file.(w1004_022）
            // For same TTC Customer Code has different Customer Code in SSMS OR KANB invoice & Supplier Kanban Issue
            // Plan file in DB.(w1004_022）
            boolean isKanbanCustError = false;
            String keyCustOffice = customerCode + StringConst.NEW_COMMA + officeCode;
            if (cutExpCodeMapsDB.containsKey(keyCustOffice) && !expCustCode.equals(cutExpCodeMapsDB.get(keyCustOffice))) {
                isKanbanCustError = true;
            }
            if (cutCodeExpMapsDB.containsKey(expCustCode) && !keyCustOffice.equals(cutCodeExpMapsDB.get(expCustCode))) {
                isKanbanCustError = true;
            }
            if (!isKanbanCustError) {
                if (cutExpCodeMaps.containsKey(keyCustOffice) && !expCustCode.equals(cutExpCodeMaps.get(keyCustOffice))) {
                    isKanbanCustError = true;
                }
                if (cutCodeExpMaps.containsKey(expCustCode) && !keyCustOffice.equals(cutCodeExpMaps.get(expCustCode))) {
                    isKanbanCustError = true;
                }
            }
            if (isKanbanCustError) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_022);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCCustCd",
                    "CPMPMF01_Grid_CustCdinSSMSORKANB" });
                messageLists.add(message);
            }
            if (!cutExpCodeMaps.containsKey(keyCustOffice)) {
                cutExpCodeMaps.put(keyCustOffice, expCustCode);
            }
            if (!cutCodeExpMaps.containsKey(expCustCode)) {
                cutCodeExpMaps.put(expCustCode, keyCustOffice);
            }
            // 2016/08/24 add by shiyang end

            // shiyang add start
            // For one set of TTC Part No. + Imp Office Code + TTC Customer Code, if there have duplicate TTC
            // Supplier Code.(w1004_156)
            if (!errorDuplSupCode && tocMXMapsFile.containsKey(keytoc)) {
                String ttcSuppCodeTemp = tocMXMapsFile.get(keytoc);
                if (ttcSuppCodeTemp.equals(ttcSuppCode)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_156);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TTCSuppCd",
                        "CPMPMF01_Grid_TTC_Office_Cust" });
                    messageLists.add(message);
                }
            }
            if (!tocMXMapsFile.containsKey(keytoc)) {
                tocMXMapsFile.put(keytoc, ttcSuppCode);
            }
            // check duplicate for NEW and MOD in file
            if (checkFileDupTtcList.contains(keyttc)) {
                // key1: ttcPartsNo + ttcSuppCode + customerCode
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile",
                    "CPMPMF01_Grid_TTCPN", ttcPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                    "CPMPMF01_Grid_TTCCustCd", customerCode });
                messageLists.add(message);
            } else {
                checkFileDupTtcList.add(keyttc);
            }
            if (checkFileDupSupList.contains(keytte)) {
                // key2: suppPartsNo + ttcSuppCode + expCustCode
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile",
                    "CPMPMF01_Grid_SuppPN", suppPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                    "CPMPMF01_Grid_CustCdinSSMSORKANB", expCustCode });
                messageLists.add(message);
            } else {
                checkFileDupSupList.add(keytte);
            }
            if (checkFileDupCustList.contains(keyctc)) {
                // key3: custPartsNo + ttcSuppCode + customerCode
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_062);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMSRF11_Grid_UploadFile",
                    "CPMPMF01_Grid_CustPN", custPartsNo, "CPMPMF01_Grid_TTCSuppCd", ttcSuppCode,
                    "CPMPMF01_Grid_TTCCustCd", customerCode });
                messageLists.add(message);
            } else {
                checkFileDupCustList.add(keyctc);
            }
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
            // col. 22 expCustCode + invCustCode
            String invCustCode = cf.getInvCustCode();
            String[] invCustCodeList = invCustCode.split(StringConst.COMMA);
            for (String invc : invCustCodeList) {
                if (invKanbanCustMap.containsKey(invc)) {
                    if (!expCustCode.equals(invKanbanCustMap.get(invc))) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_070);
                        message.setMessageArgs(new String[] { rowNum, sheetName, invc });
                        messageLists.add(message);
                    }
                } else {
                    if (eiAXMaps.containsKey(invc)) {
                        if (!expCustCode.equals(eiAXMaps.get(invc))) {
                            BaseMessage message = new BaseMessage(MessageCodeConst.W1004_070);
                            message.setMessageArgs(new String[] { rowNum, sheetName, invc });
                            messageLists.add(message);
                        }
                    }
                }
                if (!eiAXMaps.containsKey(invc)) {
                    eiAXMaps.put(invc, expCustCode);
                }
            }
            // col. 26 - col. 29
            cpmpmf11CommonService.checkOrderLotToSpqM3(messageLists, cf, sheetName, rowNum);
            // col. 31
            cpmpmf11CommonService.checkBusinessType(messageLists, cf, sheetName, rowNum, lang, busTypeList);
            // col. 32
            cpmpmf11CommonService.checkPartsType(messageLists, cf, sheetName, rowNum, lang, partsTypeList);
            // col. 35
            cpmpmf11CommonService.checkTargetMonth(messageLists, cf, sheetName, rowNum);
            // col. 36
            cpmpmf11CommonService.checkForecastNum(messageLists, cf, sheetName, rowNum);
            // col. 37
            String orderFcType = cf.getOrderFcType();
            if (!orderForecastTypeT.equals(orderFcType)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TypeofOrderForecast",
                    orderForecastTypeT });
                messageLists.add(message);
            } else {
                cf.setOrderFcType(CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
                    orderFcType) + "");
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
            String delayAdjustmentPattern = cf.getDelayAdjustmentPattern();
            if (!onShipDelayAdjustPO.equals(delayAdjustmentPattern)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
                message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_OnShipDelayAdjustPatt",
                    onShipDelayAdjustPO });
                messageLists.add(message);
            } else {
                cf.setDelayAdjustmentPattern(CodeCategoryManager.getCodeValue(lang,
                    CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, delayAdjustmentPattern) + "");
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
            // For one set of Customer Part No. + Imp Office Code + TTC Customer Code, if there have more then one
            // PARTS_ID.(w1004_127)
            String custPartOfficeCust = custPartsNo + StringConst.COMMA + officeCode + StringConst.COMMA + customerCode;
            if (custPartOfficeCustMap.containsKey(custPartOfficeCust)) {
                String ttcPartNoTemp = custPartOfficeCustMap.get(custPartOfficeCust);
                if (!ttcPartNoTemp.equals(ttcPartsNo)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPart_Office_Cust",
                        "CPMPMF01_Grid_TTCPN" });
                    messageLists.add(message);
                }
            } else {
                custPartOfficeCustMap.put(custPartOfficeCust, ttcPartsNo);

                if (custPartOfficeCustMapDB.containsKey(custPartOfficeCust)) {
                    String ttcPartNoTemp = custPartOfficeCustMapDB.get(custPartOfficeCust);
                    if (!ttcPartNoTemp.equals(ttcPartsNo)) {
                        BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                        message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPart_Office_Cust",
                            "CPMPMF01_Grid_TTCPN" });
                        messageLists.add(message);
                    }
                }
            }
            // 2016/07/05 shiyang add start (check in type=mod and type=new)
            // check File suppPartsNo related to more then one ttcPartsNo
            if (stNXMapsModFile.containsKey(suppPartsNo)) {
                String ttcPartsNoTemp = stNXMapsModFile.get(suppPartsNo);
                if (!ttcPartsNo.equals(ttcPartsNoTemp)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppPN",
                        "CPMPMF01_Grid_TTCPN" });
                    messageLists.add(message);
                }
            } else {
                stNXMapsModFile.put(suppPartsNo, ttcPartsNo);

                // check DB suppPartsNo related to more then one ttcPartsNo
                String valuest = stNXMapsModDB.get(suppPartsNo);
                if (!StringUtil.isNullOrEmpty(valuest) && !ttcPartsNo.equals(valuest)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_SuppPN",
                        "CPMPMF01_Grid_TTCPN" });
                    messageLists.add(message);
                }
            }

            // check File custPartsNo related to more then one ttcPartsNo
            if (ctNXMapsModFile.containsKey(custPartsNo)) {
                String ttcPartsNoTemp = ctNXMapsModFile.get(custPartsNo);
                if (!ttcPartsNo.equals(ttcPartsNoTemp)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPN",
                        "CPMPMF01_Grid_TTCPN" });
                    messageLists.add(message);
                }
            } else {
                ctNXMapsModFile.put(custPartsNo, ttcPartsNo);

                // check DB custPartsNo related to more then one ttcPartsNo
                String valuect = ctNXMapsModDB.get(custPartsNo);
                if (!StringUtil.isNullOrEmpty(valuect) && !ttcPartsNo.equals(valuect)) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1004_127);
                    message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_CustPN",
                        "CPMPMF01_Grid_TTCPN" });
                    messageLists.add(message);
                }
            }
            // 2016/07/05 shiyang add end
            // shiyang add end

            // set partsId
            // shiyang mod start
            // Integer valuePartsId = tcceoIdMapsDB.get(keyccteo);
            Integer valuePartsId = partsIdMapsDB.get(keytoc);
            // shiyang mod end
            if (null != valuePartsId) {
                cf.setPartsId(valuePartsId);
            }

            // shiyang del start
            // // check xlsx expCustCode + invCustCode
            // String invCustCode = cf.getInvCustCode();
            // String[] invCustCodeList = invCustCode.split(StringConst.COMMA);
            // for (String invc : invCustCodeList) {
            // String valuee = eiAXMaps.get(invc);
            // if (null == valuee) {
            // eiAXMaps.put(invc, expCustCode);
            // } else if (!expCustCode.equals(valuee)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_070);
            // message.setMessageArgs(new String[] { rowNum, sheetName, invc });
            // messageLists.add(message);
            // }
            // }
            //
            // check orderFcType
            // String orderFcType = cf.getOrderFcType();
            // if (!orderForecastTypeT.equals(orderFcType)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TypeofOrderForecast",
            // orderForecastTypeT });
            // messageLists.add(message);
            // } else {
            // cf.setOrderFcType(CodeCategoryManager.getCodeValue(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
            // orderFcType) + "");
            // }
            //
            // // check delayAdjustmentPattern
            // String delayAdjustmentPattern = cf.getDelayAdjustmentPattern();
            // if (!onShipDelayAdjustPO.equals(delayAdjustmentPattern)) {
            // BaseMessage message = new BaseMessage(MessageCodeConst.W1004_016);
            // message.setMessageArgs(new String[] { rowNum, sheetName, "CPMPMF01_Grid_TypeofOrderForecast",
            // onShipDelayAdjustPO });
            // messageLists.add(message);
            // } else {
            // cf.setDelayAdjustmentPattern(CodeCategoryManager.getCodeValue(lang,
            // CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P, delayAdjustmentPattern) + "");
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
                    sheetName, "file", BusinessPattern.AISIN);
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
                        sheetName, "db", BusinessPattern.AISIN);
                    if (beanChangeDBMsgs != null && beanChangeDBMsgs.size() > 0) {
                        messageLists.addAll(beanChangeDBMsgs);
                    }
                }
            }

            // shiyang mod start (DB + FILE: all Y then Y, else N)
            // deal inactiveFlag Y--> N N--> Y
            String valuetcceo = tcceoMapsDB.get(keyccteo);
            if (valuetcceo != null) {
                String inactiveFlag = cf.getInactiveFlag();
                if ((InactiveFlag.ACTIVE + "").equals(inactiveFlag) && (InactiveFlag.INACTIVE + "").equals(valuetcceo)) {
                    Integer count = tcceoCountMapsDB.get(keyccteo);
                    // shiyang add start
                    if (count == null) {
                        count = 1;
                    } else {
                        // shiyang add end
                        count = count + IntDef.INT_ONE;
                    }
                    tcceoCountMapsDB.put(keyccteo, count);
                } else if ((InactiveFlag.INACTIVE + "").equals(inactiveFlag)
                        && (InactiveFlag.ACTIVE + "").equals(valuetcceo)) {
                    Integer count = tcceoCountMapsDB.get(keyccteo);
                    count = count - IntDef.INT_ONE;
                    tcceoCountMapsDB.put(keyccteo, count);
                }
            }
            // if (tcceoMapsDB.containsKey(keyccteo)) {
            // tcceoMapsDB.remove(keyccteo);
            // tcceoIdMapsDB.remove(keyccteo);
            // if ((InactiveFlag.ACTIVE + "").equals(cf.getInactiveFlag())) {
            // Integer partsId = tcceoIdMapsDB.get(keyccteo);
            // if (partsId == null) {
            // partsId = partsIdMapsDB.get(keytoc);
            // }
            // if (!idList.contains(partsId)) {
            // idList.add(partsId);
            // }
            // }
            // }
            // shiyang mod end

            // set creatby
            cf.setCreatedBy(userId);
            // set updatedBy
            cf.setUpdatedBy(userId);
            resultDataList.add(cf);
        }

        // shiyang add start
        cpmpmf11CommonService.checkRelationOutLoop(messageLists, impWhsCRegionMsgMaps, ttcSCNameRegMsgMap,
            shipRCodeOEMsgMap);
        // shiyang add end

        // shiyang mod start (DB + FILE: all Y then Y, else N)
        // deal all inactiveFlag is Y
        List<Integer> idList = new ArrayList<Integer>();
        if (tcceoCountMapsDB != null && tcceoCountMapsDB.size() > 0) {
            for (Map.Entry<String, Integer> entry : tcceoCountMapsDB.entrySet()) {
                Integer value = entry.getValue();
                if (0 == value) {
                    Integer valueid = tcceoIdMapsDB.get(entry.getKey());
                    // shiyang add start
                    if (valueid == null) {
                        String[] keys = entry.getKey().split(StringConst.UNDERLINE);
                        valueid = partsIdMapsDB.get(keys[0] + StringConst.UNDERLINE + keys[1] + StringConst.UNDERLINE
                                + keys[NumberConst.IntDef.INT_FOUR]);
                    }
                    // shiyang add end
                    if (valueid != null) {
                        idList.add(valueid);
                    }
                }
            }
        }
        // for (Entry<String, String> entry : tcceoMapsDB.entrySet()) {
        // if ((InactiveFlag.ACTIVE + "").equals(entry.getValue())) {
        // Integer partsId = tcceoIdMapsDB.get(entry.getKey());
        // if (!idList.contains(partsId)) {
        // idList.add(partsId);
        // }
        // }
        // }
        // shiyang mod end

        maps.put("dataList", resultDataList);
        maps.put("messageLists", messageLists);
        maps.put("idList", idList);
        return maps;
    }
}
