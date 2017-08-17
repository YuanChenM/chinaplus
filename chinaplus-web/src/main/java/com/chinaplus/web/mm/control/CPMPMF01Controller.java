/**
 * Controller of Parts Master Download
 *
 * @screen
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.consts.CodeConst.PartsStatus;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMF01Entity;
import com.chinaplus.web.mm.entity.CPMPMS01Entity;
import com.chinaplus.web.mm.service.CPMPMF01Service;
import com.chinaplus.web.mm.service.CPMPMS02Service;

/**
 * CPMPMF01Controller.
 */
@Controller
public class CPMPMF01Controller extends BaseFileController {

    private static final String STYLE = "style";

    private static final String PARTSMASTERDOWNLOADFILE_XLSX = "PartsMasterDownloadFile_{0}.xlsx";

    private static final String VV_PARTS_MASTER = "V-V_Parts_Master";

    private static final String AISIN_PARTS_MASTER = "AISIN_Parts_Master";

    /**
     * cpmpmf01Service.
     */
    @Autowired
    private CPMPMF01Service cpmpmf01Service;

    /**
     * cpmpms02Service.
     */
    @Autowired
    private CPMPMS02Service cpmpms02Service;

    @Override
    protected String getFileId() {
        return FileId.CPMPMF01;
    }

    /**
     * Parts Master Download check
     * 
     * @param param param
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     * @throws Exception e
     */
    @RequestMapping(value = "/mm/CPMPMF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {

        BaseResult<BaseEntity> result = new BaseResult<BaseEntity>();
        List<List<CPMPMF01Entity>> downloadList = getDownloadList(param);
        List<CPMPMF01Entity> vvdataList = downloadList.get(0);
        List<CPMPMF01Entity> aisinDataList = downloadList.get(1);
        if (vvdataList.size() == 0 && aisinDataList.size() == 0) {
            result.addMessage(new BaseMessage(MessageCodeConst.W1005_001));
        }

        return result;
    }

    /**
     * Parts Master Download from page
     * 
     * @param request request
     * @param response response
     */
    @RequestMapping(value = "/mm/CPMPMF01/download",
        method = RequestMethod.POST)
    @ResponseBody
    public void download(HttpServletRequest request, HttpServletResponse response) {

        BaseParam param = this.convertJsonDataForForm(BaseParam.class);

        this.setCommonParam(param, request);

        String fileName = StringUtil.formatMessage(PARTSMASTERDOWNLOADFILE_XLSX, param.getClientTime());

        this.downloadExcelWithTemplate(fileName, param, request, response);

    }

    /**
     * Parts Master Download
     *
     * @param param
     * @param wbTemplate
     * @param wbOutput
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        boolean vvFlag = false;
        boolean aisinFlag = false;
        List<List<CPMPMF01Entity>> downloadList = getDownloadList(param);
        List<CPMPMF01Entity> vvdataList = downloadList.get(0);
        List<CPMPMF01Entity> aisinDataList = downloadList.get(1);

        Map<Integer, String> maps = new HashMap<>();

        maps.put(IntDef.INT_NINE, "J");
        maps.put(IntDef.INT_TEN, "K");
        maps.put(IntDef.INT_ELEVEN, "L");
        maps.put(IntDef.INT_TWENTY_THREE, "X");
        maps.put(IntDef.INT_TWENTY_FOUR, "Y");
        maps.put(IntDef.INT_THIRTY_THREE, "AH");
        maps.put(IntDef.INT_THIRTY_SIX, "AK");
        maps.put(IntDef.INT_THIRTY_SEVEN, "AL");
        maps.put(IntDef.INT_FIFTY, "AY");
        maps.put(IntDef.INT_FIFTY_ONE, "AZ");
        maps.put(IntDef.INT_FIFTY_TWO, "BA");
        maps.put(IntDef.INT_FIFTY_THREE, "BB");
        maps.put(IntDef.INT_FIFTY_FOUR, "BC");
        maps.put(IntDef.INT_FIFTY_FIVE, "BD");
        maps.put(IntDef.INT_SIXTYSIX, "BO");

        maps.put(IntDef.INT_TWENTY_ONE, "V");

        maps.put(IntDef.INT_FOURTY_ONE, "AP");
        maps.put(IntDef.INT_FOURTY_TWO, "AQ");
        maps.put(IntDef.INT_FOURTYTHREE, "AR");
        maps.put(IntDef.INT_FOURTY_FOUR, "AS");
        maps.put(IntDef.INT_FOURTY_FIVE, "AT");
        maps.put(IntDef.INT_FOURTY_SIX, "AU");

        maps.put(IntDef.INT_SIXTYTHREE, "BL");
        maps.put(IntDef.INT_SIXTYFOUR, "BM");
        maps.put(IntDef.INT_SIXTYFIVE, "BN");

        // deal AISIN Download
        if (aisinDataList != null && aisinDataList.size() > 0) {
            aisinFlag = true;
        }
        dealAISINDownload(wbTemplate, aisinDataList, param.getLanguage(), maps);

        // deal VV Download
        if (vvdataList != null && vvdataList.size() > 0) {
            vvFlag = true;
        }
        dealVVDownload(wbTemplate, vvdataList, param.getLanguage(), maps);

        wbTemplate.setForceFormulaRecalculation(true);

        // remove sheet style
        if (!vvFlag && aisinFlag) {
            wbOutput.removeSheetAt(wbOutput.getSheetIndex(VV_PARTS_MASTER));
        }
        if (!aisinFlag && vvFlag) {
            wbOutput.removeSheetAt(wbOutput.getSheetIndex(AISIN_PARTS_MASTER));
        }
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(STYLE));
    }

    /**
     * Get download list.
     * 
     * @param param page parameter
     * @return download list
     */
    @SuppressWarnings("unchecked")
    public List<List<CPMPMF01Entity>> getDownloadList(BaseParam param) {

        List<List<CPMPMF01Entity>> downloadList = new ArrayList<List<CPMPMF01Entity>>();
        String screenId = param.getScreenId();
        List<CPMPMF01Entity> vvdataList = new ArrayList<CPMPMF01Entity>();
        List<CPMPMF01Entity> aisinDataList = new ArrayList<CPMPMF01Entity>();

        if (FileId.CPMPMS01.equals(screenId)) {
            List<CPMPMS01Entity> dataLists = (List<CPMPMS01Entity>) param.getSwapData().get("dataLists");
            // get VV data
            vvdataList = cpmpmf01Service.getCpmpmf01EntityListFP(dataLists, BusinessPattern.V_V, param);
            // get AISIN data
            aisinDataList = cpmpmf01Service.getCpmpmf01EntityListFP(dataLists, BusinessPattern.AISIN, param);
            // shiyang mod start (for send mail in CPMSMS01)
            // } else if (FileId.CPMPMS02.equals(screenId)) {
        } else if (FileId.CPMPMS02.equals(screenId) || FileId.CPMSMS01.equals(screenId)) {
            // shiyang mod end
            boolean blankFormatDownP = (boolean) param.getSwapData().get("blankFormatDownP");
            String businessPattern = (String) param.getSwapData().get("businessPattern");
            if (!blankFormatDownP) {

                String ttcPartsNo = (String) param.getSwapData().get("ttcPartsNo");
                String officeCode = (String) param.getSwapData().get("officeCode");
                String customerCode = (String) param.getSwapData().get("customerCode");
                String ttcSuppCode = (String) param.getSwapData().get("ttcSuppCode");
                String expCustCode = (String) param.getSwapData().get("expCustCode");
                String ssmsMainRoute = (String) param.getSwapData().get("ssmsMainRoute");
                String supplierCode = (String) param.getSwapData().get("supplierCode");
                String custPartsNo = (String) param.getSwapData().get("custPartsNo");
                String suppPartsNo = (String) param.getSwapData().get("suppPartsNo");
                boolean inactiveFlag = (boolean) param.getSwapData().get("inactiveFlag");
                boolean onlyDownNoRegParts = (boolean) param.getSwapData().get("onlyDownNoRegParts");
                boolean includingNotReqParts = (boolean) param.getSwapData().get("includingNotReqParts");

                BaseParam paramData = new BaseParam();
                paramData.setSwapData("ttcPartsNo", ttcPartsNo);
                paramData.setSwapData("officeCode", cpmpms02Service.setCodeStringList(officeCode));
                paramData.setSwapData("customerCode", cpmpms02Service.setCodeStringList(customerCode));
                paramData.setSwapData("ttcSuppCode", cpmpms02Service.setCodeStringList(ttcSuppCode));
                paramData.setSwapData("expCustCode", expCustCode);
                paramData.setSwapData("ssmsMainRoute", cpmpms02Service.setCodeStringList(ssmsMainRoute));
                paramData.setSwapData("supplierCode", cpmpms02Service.setCodeStringList(supplierCode));
                paramData.setSwapData("custPartsNo", custPartsNo);
                paramData.setSwapData("suppPartsNo", suppPartsNo);
                paramData.setSwapData("businessPattern", cpmpms02Service.setCodeStringList(businessPattern));

                List<Integer> inactiveFlagYN = new ArrayList<Integer>();
                if (!onlyDownNoRegParts) {
                    inactiveFlagYN.add(InactiveFlag.ACTIVE);
                    if (true == inactiveFlag) {
                        inactiveFlagYN.add(InactiveFlag.INACTIVE);
                    }
                }
                paramData.setSwapData("inactiveFlagYN", inactiveFlagYN);

                List<Integer> partsStatusList = new ArrayList<Integer>();
                if (true == onlyDownNoRegParts) {
                    partsStatusList.add(PartsStatus.NOT_REGISTERED);
                } else {
                    partsStatusList.add(PartsStatus.COMPLETED);
                    if (true == includingNotReqParts) {
                        partsStatusList.add(PartsStatus.NOT_REQUIRED);
                    }
                }

                paramData.setSwapData("partsStatusList", partsStatusList);

                int checkPartType = 0;
                if (!inactiveFlag && includingNotReqParts) {
                    checkPartType = 1;
                }
                paramData.setSwapData("checkPartType", checkPartType);
                // shiyang add start (for send mail in CPMSMS01)
                paramData.setSwapData("expCustCodes", param.getSwapData().get("expCustCodes"));
                // shiyang add end

                // get VV data
                vvdataList = cpmpmf01Service.getCpmpmf01EntityList(paramData, BusinessPattern.V_V);
                // get AISIN data
                aisinDataList = cpmpmf01Service.getCpmpmf01EntityList(paramData, BusinessPattern.AISIN);
            } else {
                if (StringUtil.isEmpty(businessPattern)
                        || businessPattern.contains(String.valueOf(BusinessPattern.V_V))) {
                    for (int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                        vvdataList.add(new CPMPMF01Entity());
                    }
                }
                if (StringUtil.isEmpty(businessPattern)
                        || businessPattern.contains(String.valueOf(BusinessPattern.AISIN))) {
                    for (int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                        aisinDataList.add(new CPMPMF01Entity());
                    }
                }
            }
        }

        downloadList.add(vvdataList);
        downloadList.add(aisinDataList);
        return downloadList;
    }

    /**
     * deal AISIN Download
     * 
     * @param wbTemplate wbTemplate
     * @param dataList dataList
     * @param lang lang
     * @param maps maps
     */
    private void dealAISINDownload(Workbook wbTemplate, List<CPMPMF01Entity> dataList, Integer lang,
        Map<Integer, String> maps) {
        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(AISIN_PARTS_MASTER);
        int size = 0;
        if (dataList != null && dataList.size() > 0) {
            size = dataList.size();
            Cell[] TemplateCells = getTemplateCells(STYLE, IntDef.INT_SIX, wbTemplate);
            SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

            for (int i = 0; i < dataList.size(); i++) {

                CPMPMF01Entity entity = dataList.get(i);
                if (null == entity) {
                    continue;
                }

                if (entity.getBusinessPattern() != null) {
                    entity.setBusinessPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUSINESS_PATTERN, Integer.valueOf(entity.getBusinessPattern())));
                }
                if (entity.getBusinessType() != null) {
                    entity.setBusinessType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE,
                        Integer.valueOf(entity.getBusinessType())));
                }
                if (entity.getPartsType() != null) {
                    entity.setPartsType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE,
                        Integer.valueOf(entity.getPartsType())));
                }
                if (entity.getOrderFcType() != null) {
                    entity.setOrderFcType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
                        Integer.valueOf(entity.getOrderFcType())));
                }
                if (entity.getOsCustStockFlag() != null) {
                    entity.setOsCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getOsCustStockFlag())));
                }
                if (entity.getSaCustStockFlag() != null) {
                    entity.setSaCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getSaCustStockFlag())));
                }
                if (entity.getInventoryBoxFlag() != null) {
                    entity.setInventoryBoxFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.INVENTORY_BY_BOX, Integer.valueOf(entity.getInventoryBoxFlag())));
                }
                if (entity.getSimulationEndDatePattern() != null) {
                    entity
                        .setSimulationEndDatePattern(CodeCategoryManager.getCodeName(lang,
                            CodeMasterCategory.SIMULATION_END_DAY_P,
                            Integer.valueOf(entity.getSimulationEndDatePattern())));
                }

                if (entity.getDelayAdjustmentPattern() != null) {
                    entity.setDelayAdjustmentPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P,
                        Integer.valueOf(entity.getDelayAdjustmentPattern())));
                }
                if (entity.getCfcAdjustmentType1() != null) {
                    entity.setCfcAdjustmentType1(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P1, Integer.valueOf(entity.getCfcAdjustmentType1())));
                }
                if (entity.getCfcAdjustmentType2() != null) {
                    entity.setCfcAdjustmentType2(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P2, Integer.valueOf(entity.getCfcAdjustmentType2())));
                }
                if (entity.getBuildoutFlag() != null) {
                    entity.setBuildoutFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUILD_OUT_INDICATOR, Integer.valueOf(entity.getBuildoutFlag())));
                }
                if (entity.getInactiveFlag() != null) {
                    entity.setInactiveFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.DISCONTINUE_INDICATOR, Integer.valueOf(entity.getInactiveFlag())));
                }

                Object[] arrayObj = new Object[] {
                    // 1-10
                    "",
                    "",
                    entity.getTtcPartsNo(),
                    entity.getTtcPartsName(),
                    entity.getPartsNameCn(),
                    entity.getOldTtcPartsNo(),
                    entity.getExpUomCode(),
                    entity.getExpRegion(),
                    entity.getTtcSuppCode(),
                    "",

                    // 11-20
                    "",
                    "",
                    entity.getSupplierName(),
                    entity.getSuppPartsNo(),
                    entity.getImpRegion(),
                    entity.getOfficeCode(),
                    entity.getCustomerCode(),
                    entity.getExpCustCode(),
                    entity.getCustomerName(),
                    entity.getCustPartsNo(),

                    // 21-30
                    entity.getCustBackNo(),
                    entity.getInvCustCode(),
                    entity.getImpWhsCode(),
                    "",
                    "",
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getOrderLot()),
                    entity.getOrderLot(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSrbq()),
                    entity.getSrbq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpq()),
                    entity.getSpq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpqM3()),
                    entity.getSpqM3(),
                    entity.getBusinessPattern(),

                    // 31-40
                    entity.getBusinessType(),
                    entity.getPartsType(),
                    entity.getCarModel(),
                    "",
                    entity.getTargetMonth(),
                    entity.getForecastNum(),
                    entity.getOrderFcType(),
                    "",
                    entity.getOsCustStockFlag(),
                    entity.getSaCustStockFlag(),

                    // 41-50
                    entity.getInventoryBoxFlag(), entity.getMinStock(), entity.getMaxStock(),
                    entity.getMinBox(),
                    entity.getMaxBox(),
                    entity.getOrderSafetyStock(),
                    entity.getRundownSafetyStock(),
                    entity.getOutboundFluctuation(),
                    entity.getSimulationEndDatePattern(),
                    entity.getShippingRouteCode(),

                    // 51-60
                    entity.getDelayAdjustmentPattern(), "", "", "", "", "",
                    entity.getAllocationFcType(),
                    entity.getCfcAdjustmentType1(),
                    entity.getCfcAdjustmentType2(),
                    entity.getRemark1(),

                    // 61-68
                    entity.getRemark2(), entity.getRemark3(), entity.getBuildoutFlag(),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getBuildoutMonth()),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getLastPoMonth()),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getLastDeliveryMonth()), "",
                    entity.getInactiveFlag() };
                cpmpmf01Service.createOneDataRowByTemplate(sheet, IntDef.INT_EIGHT + i, TemplateCells, arrayObj, lang,
                    BusinessPattern.AISIN, maps, scf, size);
            }
        } else {
            size = IntDef.INT_EIGHT;
        }
        // set value from style sheet
        for (int i = IntDef.INT_EIGHT; i < IntDef.INT_FOURTEEN; i++) {
            Cell[] styleCells = getTemplateCells(STYLE, i, wbTemplate);
            Row dataRow = sheet.createRow(IntDef.INT_TWO + size + i);

            for (int y = 0; y < styleCells.length; y++) {
                // create cell
                Cell cell = dataRow.createCell(y);
                // set cell format, formula, type
                if (styleCells != null && y < styleCells.length && null != styleCells[y]) {
                    cell.setCellStyle(styleCells[y].getCellStyle());
                    int cellType = styleCells[y].getCellType();
                    cell.setCellType(cellType);
                    if (Cell.CELL_TYPE_FORMULA == cellType) {
                        cell.setCellFormula(styleCells[y].getCellFormula());
                    }
                    cell.setCellValue(styleCells[y].getStringCellValue());
                }
            }
        }

    }

    /**
     * deal VV Download
     * 
     * @param wbTemplate wbTemplate
     * @param dataList dataList
     * @param lang lang
     * @param maps maps
     */
    private void dealVVDownload(Workbook wbTemplate, List<CPMPMF01Entity> dataList, Integer lang,
        Map<Integer, String> maps) {

        org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(VV_PARTS_MASTER);
        int size = 0;
        if (dataList != null && dataList.size() > 0) {
            size = dataList.size();
            Cell[] TemplateCells = getTemplateCells(STYLE, IntDef.INT_SIX, wbTemplate);
            SheetConditionalFormatting scf = sheet.getSheetConditionalFormatting();

            for (int i = 0; i < dataList.size(); i++) {

                CPMPMF01Entity entity = dataList.get(i);
                if (null == entity) {
                    continue;
                }

                if (entity.getBusinessPattern() != null) {
                    entity.setBusinessPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUSINESS_PATTERN, Integer.valueOf(entity.getBusinessPattern())));
                }
                if (entity.getBusinessType() != null) {
                    entity.setBusinessType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE,
                        Integer.valueOf(entity.getBusinessType())));
                }
                if (entity.getPartsType() != null) {
                    entity.setPartsType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE,
                        Integer.valueOf(entity.getPartsType())));
                }
                if (entity.getOrderFcType() != null) {
                    entity.setOrderFcType(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE,
                        Integer.valueOf(entity.getOrderFcType())));
                }
                if (entity.getExpCalendarCode() != null) {
                    entity.setExpCalendarCode(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.EXPORT_WH_CALENDER, Integer.valueOf(entity.getExpCalendarCode())));
                }
                if (entity.getOsCustStockFlag() != null) {
                    entity.setOsCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getOsCustStockFlag())));
                }
                if (entity.getSaCustStockFlag() != null) {
                    entity.setSaCustStockFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUSTOMER_STOCK_FLAG, Integer.valueOf(entity.getSaCustStockFlag())));
                }
                if (entity.getInventoryBoxFlag() != null) {
                    entity.setInventoryBoxFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.INVENTORY_BY_BOX, Integer.valueOf(entity.getInventoryBoxFlag())));
                }
                if (entity.getSimulationEndDatePattern() != null) {
                    entity
                        .setSimulationEndDatePattern(CodeCategoryManager.getCodeName(lang,
                            CodeMasterCategory.SIMULATION_END_DAY_P,
                            Integer.valueOf(entity.getSimulationEndDatePattern())));
                }

                if (entity.getDelayAdjustmentPattern() != null) {
                    entity.setDelayAdjustmentPattern(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P,
                        Integer.valueOf(entity.getDelayAdjustmentPattern())));
                }
                if (entity.getCfcAdjustmentType1() != null) {
                    entity.setCfcAdjustmentType1(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P1, Integer.valueOf(entity.getCfcAdjustmentType1())));
                }
                if (entity.getCfcAdjustmentType2() != null) {
                    entity.setCfcAdjustmentType2(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.CUST_FORECAST_ADJUST_P2, Integer.valueOf(entity.getCfcAdjustmentType2())));
                }
                if (entity.getBuildoutFlag() != null) {
                    entity.setBuildoutFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.BUILD_OUT_INDICATOR, Integer.valueOf(entity.getBuildoutFlag())));
                }

                if (entity.getPartsStatus() != null) {
                    entity.setPartsStatus(CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS,
                        Integer.valueOf(entity.getPartsStatus())));
                }

                if (entity.getInactiveFlag() != null) {
                    entity.setInactiveFlag(CodeCategoryManager.getCodeName(lang,
                        CodeMasterCategory.DISCONTINUE_INDICATOR, Integer.valueOf(entity.getInactiveFlag())));
                }

                String orderDay = "";
                if (null != entity.getOrderDay()) {
                    orderDay = "D-" + entity.getOrderDay();
                }

                Object[] arrayObj = new Object[] {
                    // 1-10
                    "",
                    "",
                    entity.getTtcPartsNo(),
                    entity.getTtcPartsName(),
                    entity.getPartsNameCn(),
                    entity.getOldTtcPartsNo(),
                    entity.getExpUomCode(),
                    entity.getExpRegion(),
                    entity.getTtcSuppCode(),
                    entity.getSsmsMainRoute(),

                    // 11-20
                    entity.getSsmsVendorRoute(),
                    entity.getExpSuppCode(),
                    entity.getSupplierName(),
                    entity.getSuppPartsNo(),
                    entity.getImpRegion(),
                    entity.getOfficeCode(),
                    entity.getCustomerCode(),
                    entity.getExpCustCode(),
                    entity.getCustomerName(),
                    entity.getCustPartsNo(),

                    // 21-30
                    entity.getCustBackNo(),
                    "",
                    entity.getImpWhsCode(),
                    entity.getWestCustCode(),
                    entity.getWestPartsNo(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getOrderLot()),
                    entity.getOrderLot(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSrbq()),
                    entity.getSrbq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpq()),
                    entity.getSpq(),
                    // StringUtil.formatBigDecimal(entity.getExpUomCode(), entity.getSpqM3()),
                    entity.getSpqM3(),
                    entity.getBusinessPattern(),

                    // 31-40
                    entity.getBusinessType(),
                    entity.getPartsType(),
                    entity.getCarModel(),
                    orderDay,
                    entity.getTargetMonth(),
                    entity.getForecastNum(),
                    entity.getOrderFcType(),
                    entity.getExpCalendarCode(),
                    entity.getOsCustStockFlag(),
                    entity.getSaCustStockFlag(),

                    // 41-50
                    entity.getInventoryBoxFlag(),
                    entity.getMinStock(),
                    entity.getMaxStock(),
                    entity.getMinBox(),
                    entity.getMaxBox(),
                    entity.getOrderSafetyStock(),
                    entity.getRundownSafetyStock(),
                    entity.getOutboundFluctuation(),
                    entity.getSimulationEndDatePattern(),
                    entity.getShippingRouteCode(),

                    // 51-60
                    entity.getDelayAdjustmentPattern(), entity.getAirEtdLeadtime(), entity.getAirEtaLeadtime(),
                    entity.getAirInboundLeadtime(), entity.getSeaEtaLeadtime(),
                    entity.getSeaInboundLeadtime(),
                    entity.getAllocationFcType(),
                    entity.getCfcAdjustmentType1(),
                    entity.getCfcAdjustmentType2(),
                    entity.getRemark1(),

                    // 61-68
                    entity.getRemark2(), entity.getRemark3(), entity.getBuildoutFlag(),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getBuildoutMonth()),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getLastPoMonth()),
                    DateTimeUtil.formatDate(DateTimeUtil.FORMAT_YYYYMM, "yyyyMM", entity.getLastDeliveryMonth()),
                    entity.getPartsStatus(), entity.getInactiveFlag() };
                cpmpmf01Service.createOneDataRowByTemplate(sheet, IntDef.INT_EIGHT + i, TemplateCells, arrayObj, lang,
                    BusinessPattern.V_V, maps, scf, size);
            }
        } else {
            size = IntDef.INT_EIGHT;
        }
        // set value from style sheet
        for (int i = 0; i < IntDef.INT_FIVE; i++) {
            Cell[] styleCells = getTemplateCells(STYLE, i, wbTemplate);
            Row dataRow = sheet.createRow(IntDef.INT_TEN + size + i);

            for (int y = 0; y < styleCells.length; y++) {
                // create cell
                Cell cell = dataRow.createCell(y);
                // set cell format, formula, type
                if (styleCells != null && y < styleCells.length && null != styleCells[y]) {
                    cell.setCellStyle(styleCells[y].getCellStyle());
                    int cellType = styleCells[y].getCellType();
                    cell.setCellType(cellType);
                    if (Cell.CELL_TYPE_FORMULA == cellType) {
                        cell.setCellFormula(styleCells[y].getCellFormula());
                    }
                    cell.setCellValue(styleCells[y].getStringCellValue());
                }
            }
        }
    }

}
