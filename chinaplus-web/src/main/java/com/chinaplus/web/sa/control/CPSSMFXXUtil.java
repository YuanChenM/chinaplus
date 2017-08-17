/**
 * CPSSMFXXUtil.java
 *
 * @screen CPSSMF01,CPSSMF02,CPSSMF03,CPSSMFXX
 * @author ma_chao
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSSMF01ColEntity;
import com.chinaplus.web.sa.entity.CPSSMF01Entity;
import com.chinaplus.web.sa.entity.CPSSMF01PartEntity;

/**
 * CPSSMFXXUtil.
 */
@SuppressWarnings("deprecation")
public class CPSSMFXXUtil {
    // The begin of template sheet name constant define
    /** SHEET_BASE */
    public static final String SHEET_BASE = "base";
    /** SHEET_STYLE_CELL */
    public static final String SHEET_STYLE_CELL = "style_cell";
    /** SHEET_STYLE_CELL_VALUE */
    public static final String SHEET_STYLE_CELL_VALUE = "style_cell_value";
    /** SHEET_STYLE_QTY_GRAY */
    public static final String SHEET_STYLE_QTY_GRAY = "style_qty_gray";
    /** SHEET_STYLE_QTY_GRAY_RIGHT */
    public static final String SHEET_STYLE_QTY_WHITE = "style_qty_white";
    /** SHEET_BASE */
    public static final String SHEET_STYLE_QTY_GRAY_RIGHT = "style_qty_gray_right";
    /** SHEET_STYLE_QTY_WHITE_RIGHT */
    public static final String SHEET_STYLE_QTY_WHITE_RIGHT = "style_qty_white_right";
    /** SHEET_STYLE_QTY_GRAY_BOTTOM */
    public static final String SHEET_STYLE_QTY_GRAY_BOTTOM = "style_qty_gray_bottom";
    /** SHEET_STYLE_QTY_WHITE_BOTTOM */
    public static final String SHEET_STYLE_QTY_WHITE_BOTTOM = "style_qty_white_bottom";
    /** SHEET_STYLE_QTY_GRAY_LAST */
    public static final String SHEET_STYLE_QTY_GRAY_LAST = "style_qty_gray_last";
    /** SHEET_STYLE_QTY_WHITE_LAST */
    public static final String SHEET_STYLE_QTY_WHITE_LAST = "style_qty_white_last";

    // The end of template sheet name constant define

    /**
     * Check ETD(Plan), ETA(Plan), ETD(Invoice), ETA(Invoice) and JP GSCM Sales Order Create Date
     * 
     * @param param BaseParam
     * @param result BaseResult
     * @return validResult
     */
    public static boolean validDateTimeFilter(BaseParam param, BaseResult<String> result) {
        boolean validResult = true;
        if (null == param.getSwapData().get("saleDateFrom") && null == param.getSwapData().get("saleDateTo")
                && null == param.getSwapData().get("planEtdFrom") && null == param.getSwapData().get("planEtdTo")
                && null == param.getSwapData().get("planEtaFrom") && null == param.getSwapData().get("planEtaTo")
                && null == param.getSwapData().get("invoiceEtdFrom") && null == param.getSwapData().get("invoiceEtdTo")
                && null == param.getSwapData().get("invoiceEtaFrom") && null == param.getSwapData().get("invoiceEtaTo")) {
            result.addMessage(new BaseMessage(MessageCodeConst.W1037));
            validResult = false;
        }
        return validResult;
    }

    /**
     * convert param from screen CPSSMS02
     * 
     * @param param BaseParam
     */
    public static void convertParam(BaseParam param) {
        if (null != param.getSwapData().get("ttsPartsNo")) {
            String value = param.getSwapData().get("ttsPartsNo").toString();
            param.setFilter("ttsPartsNo", value);
        }
        if (null != param.getSwapData().get("ipoOrderNo")) {
            String value = param.getSwapData().get("ipoOrderNo").toString();
            param.setFilter("ipoOrderNo", value);
        }
        if (null != param.getSwapData().get("customerOrderNo")) {
            String value = param.getSwapData().get("customerOrderNo").toString();
            param.setFilter("customerOrderNo", value);
        }
        // set parameters replace 2013-08-18T00:00:00 to 2013-08-18
        if (null != param.getSwapData().get("saleDateFrom")) {
            String value = param.getSwapData().get("saleDateFrom").toString();
            param.setFilter("saleDateFrom", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("saleDateTo")) {
            String value = param.getSwapData().get("saleDateTo").toString();
            param.setFilter("saleDateTo", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("planEtdFrom")) {
            String value = param.getSwapData().get("planEtdFrom").toString();
            param.setFilter("planEtdFrom", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("planEtdTo")) {
            String value = param.getSwapData().get("planEtdTo").toString();
            param.setFilter("planEtdTo", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("planEtaFrom")) {
            String value = param.getSwapData().get("planEtaFrom").toString();
            param.setFilter("planEtaFrom", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("planEtaTo")) {
            String value = param.getSwapData().get("planEtaTo").toString();
            param.setFilter("planEtaTo", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("invoiceEtdFrom")) {
            String value = param.getSwapData().get("invoiceEtdFrom").toString();
            param.setFilter("invoiceEtdFrom", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("invoiceEtdTo")) {
            String value = param.getSwapData().get("invoiceEtdTo").toString();
            param.setFilter("invoiceEtdTo", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("invoiceEtaFrom")) {
            String value = param.getSwapData().get("invoiceEtaFrom").toString();
            param.setFilter("invoiceEtaFrom", value.replace("T00:00:00", StringConst.EMPTY));
        }
        if (null != param.getSwapData().get("invoiceEtaTo")) {
            String value = param.getSwapData().get("invoiceEtaTo").toString();
            param.setFilter("invoiceEtaTo", value.replace("T00:00:00", StringConst.EMPTY));
        }

        // change param from a,b,c to list<String> a b c
        if (null != param.getSwapData().get("expRegion")) {
            String expRegion = param.getSwapData().get("expRegion").toString();
            if (!StringUtil.isEmpty(expRegion)) {
                List<String> expRegionList = Arrays.asList(expRegion.split(StringConst.COMMA));
                param.setFilter("expRegionList", expRegionList);
            }
        }
        if (null != param.getSwapData().get("supplierCode")) {
            String supplierCode = param.getSwapData().get("supplierCode").toString();
            if (!StringUtil.isEmpty(supplierCode)) {
                List<String> supplierCodeList = Arrays.asList(supplierCode.split(StringConst.COMMA));
                param.setFilter("supplierCodeList", supplierCodeList);
            }
        }
        if (null != param.getSwapData().get("impRegion")) {
            String impRegion = param.getSwapData().get("impRegion").toString();
            if (!StringUtil.isEmpty(impRegion)) {
                List<String> impRegionList = Arrays.asList(impRegion.split(StringConst.COMMA));
                param.setFilter("impRegionList", impRegionList);
            }
        }
        if (null != param.getSwapData().get("customerCode")) {
            String customerCode = param.getSwapData().get("customerCode").toString();
            if (!StringUtil.isEmpty(customerCode)) {
                List<String> customerCodeList = Arrays.asList(customerCode.split(StringConst.COMMA));
                param.setFilter("customerCodeList", customerCodeList);
            }
        }
    }

    /**
     * map the beginRowNum and endRowNum of IPO
     * 
     * @param rowNum rowNum
     * @param list planInfoList
     * @return Map<String, Integer[]>
     */
    public static Map<String, Integer[]> mapRowNumOfIPO(int rowNum, List<CPSSMF01Entity> list) {
        Map<String, Integer[]> map = new HashMap<String, Integer[]>();
        Map<String, List<CPSSMF01Entity>> dataMap = new HashMap<String, List<CPSSMF01Entity>>();
        List<String> ipoList = new ArrayList<String>();
        for (CPSSMF01Entity entity : list) {
            String key = entity.getIpo() + StringConst.COMMA + entity.getCustomerOrderNo() + StringConst.COMMA
                    + entity.getCustomerCode();
            if (dataMap.containsKey(key)) {
                dataMap.get(key).add(entity);
            } else {
                ipoList.add(key);
                List<CPSSMF01Entity> dataList = new ArrayList<CPSSMF01Entity>();
                dataList.add(entity);
                dataMap.put(key, dataList);
            }
        }

        List<Integer> sizeList = new ArrayList<Integer>();
        for (String key : ipoList) {
            sizeList.add(dataMap.get(key).size());
        }

        int beginRowNum = rowNum;
        for (int i = 0; i < ipoList.size(); i++) {
            int rowCount = sizeList.get(i);
            map.put(ipoList.get(i), new Integer[] { beginRowNum, beginRowNum + rowCount - IntDef.INT_ONE });
            beginRowNum = beginRowNum + rowCount;
        }

        return map;
    }

    /**
     * combine row data by ipo,customerOrderNo,partsId and delayAdjustPattern
     * 
     * @param list planInfoList
     * @return rowDataList
     */
    public static List<CPSSMF01Entity> combineRowData(List<CPSSMF01Entity> list) {
        Map<String, CPSSMF01Entity> map = new HashMap<String, CPSSMF01Entity>();
        List<CPSSMF01Entity> rowDataList = new ArrayList<CPSSMF01Entity>();
        if (null != list && !list.isEmpty()) {
            for (CPSSMF01Entity entity : list) {
                String key = entity.getIpo() + StringConst.COMMA + entity.getCustomerOrderNo() + StringConst.COMMA
                        + entity.getCustomerCode() + StringConst.COMMA + entity.getPartsId() + StringConst.COMMA
                        + entity.getTtcSupplierCode() + StringConst.COMMA + entity.getEpo();
                if (!map.containsKey(key)) {
                    map.put(key, entity);
                    rowDataList.add(entity);
                }
            }
        }
        return rowDataList;
    }

    /**
     * combine the parts info to list by same plan or invoice
     * 
     * @param list row date
     * @param colList column data
     */
    public static void combinePlanOrInvoice(List<CPSSMF01Entity> list, List<CPSSMF01ColEntity> colList) {
        Map<String, CPSSMF01ColEntity> map = new HashMap<String, CPSSMF01ColEntity>();
        if (null != list && !list.isEmpty()) {
            for (CPSSMF01Entity rowData : list) {
                Integer ssPlanId = null == rowData.getSsPlanId() ? IntDef.INT_ZERO : rowData.getSsPlanId();
                Integer invoiceId = null == rowData.getInvoiceId() ? IntDef.INT_ZERO : rowData.getInvoiceId();
                String inbActualDate = null == rowData.getIpmInbActulDate() ? StringConst.ZERO : DateTimeUtil
                    .formatDate(rowData.getIpmInbActulDate());

                String key = ssPlanId + StringConst.COMMA + invoiceId + StringConst.COMMA + inbActualDate;

                if (map.containsKey(key)) {
                    CPSSMF01ColEntity colData = map.get(key);
                    List<CPSSMF01PartEntity> partsList = colData.getPartsList();
                    partsList.add(CPSSMFXXUtil.convertRowData2Part(rowData));
                    colData.setPartsList(partsList);
                } else {
                    map.put(key, CPSSMFXXUtil.convertRowData2ColData(rowData));
                }

            }
        }

        if (null != map && !map.isEmpty()) {
            for (Entry<String, CPSSMF01ColEntity> entry : map.entrySet()) {
                colList.add(entry.getValue());
            }
        }
    }

    /**
     * sort by etd,invoiceNo,eta,impInbPlanDate
     * 
     * @param list List<CPSSMF01ColEntity>
     */
    public static void sort(List<CPSSMF01ColEntity> list) {
        if (null != list && list.size() > IntDef.INT_ONE) {
            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object arg0, Object arg1) {
                    CPSSMF01ColEntity entity1 = (CPSSMF01ColEntity) arg0;
                    CPSSMF01ColEntity entity2 = (CPSSMF01ColEntity) arg1;

                    String ipoAndCpo1 = entity1.getIpo() + StringConst.COMMA + entity1.getCustomerOrderNo()
                            + StringConst.COMMA + entity1.getCustomerCode();
                    String ipoAndCpo2 = entity2.getIpo() + StringConst.COMMA + entity2.getCustomerOrderNo()
                            + StringConst.COMMA + entity2.getCustomerCode();

                    int compareResult = ipoAndCpo1.compareTo(ipoAndCpo2);
                    if (compareResult == 0 && null != entity1.getEtd() && null != entity2.getEtd()) {
                        compareResult = entity1.getEtd().compareTo(entity2.getEtd());
                        if (compareResult == 0) {
                            String invoiceNo1 = entity1.getInvoiceNo();
                            if (StringUtil.isEmpty(invoiceNo1)) {
                                invoiceNo1 = "";
                            }

                            String invoiceNo2 = entity2.getInvoiceNo();
                            if (StringUtil.isEmpty(invoiceNo2)) {
                                invoiceNo2 = "";
                            }
                            if ((!StringUtil.isEmpty(invoiceNo1) && !StringUtil.isEmpty(invoiceNo2))
                                    || (StringUtil.isEmpty(invoiceNo1) && StringUtil.isEmpty(invoiceNo2))) {
                                compareResult = invoiceNo1.compareTo(invoiceNo2);
                                if (compareResult == 0) {
                                    compareResult = entity1.getEta().compareTo(entity2.getEta());
                                    if (compareResult == 0) {
                                        compareResult = entity1.getImpInbPlanDate().compareTo(
                                            entity2.getImpInbPlanDate());
                                        if (compareResult == 0) {
                                            compareResult = entity1.getOriginalVersion().compareTo(
                                                entity2.getOriginalVersion());
                                            if (compareResult == 0) {
                                                compareResult = entity1.getRevisionVersion().compareTo(
                                                    entity2.getRevisionVersion());
                                            }
                                        }
                                    }
                                }
                            } else if (StringUtil.isEmpty(invoiceNo1) && !StringUtil.isEmpty(invoiceNo2)) {
                                compareResult = -1;
                            } else {
                                compareResult = 1;
                            }
                        }
                        // 2016/07/01 shiyang add start
                    } else if (compareResult == 0 && null == entity1.getEtd() && null != entity2.getEtd()) {
                        compareResult = -1;
                    } else if (compareResult == 0 && null != entity1.getEtd() && null == entity2.getEtd()) {
                        compareResult = 1;
                        // 2016/07/01 shiyang add end
                    }
                    return compareResult;
                }
            });
        }
    }

    /**
     * convert row data to column data
     * 
     * @param rowData CPSSMF01Entity
     * @return CPSSMF01ColEntity
     */
    private static CPSSMF01ColEntity convertRowData2ColData(CPSSMF01Entity rowData) {
        CPSSMF01ColEntity data = new CPSSMF01ColEntity();
        if (null != rowData.getSsPlanId()) {
            data.setSsPlanId(rowData.getSsPlanId());
        }
        if (null != rowData.getInvoiceId()) {
            data.setInvoiceId(rowData.getInvoiceId());
            data.setInvoiceNo(rowData.getInvoiceNo());
        }
        data.setIpo(rowData.getIpo());
        data.setCustomerOrderNo(rowData.getCustomerOrderNo());
        data.setCustomerCode(rowData.getCustomerCode());
        data.setEpo(rowData.getEpo());
        data.setTtcSupplierCode(rowData.getTtcSupplierCode());
        data.setTransportMode(rowData.getTransportMode());
        data.setEtd(rowData.getEtd());
        data.setEta(rowData.getEta());
        data.setCcDate(rowData.getCcDate());
        data.setImpInbPlanDate(rowData.getImpInbPlanDate());
        if (null != rowData.getIpmInbActulDate()) {
            data.setIpmInbActulDate(rowData.getIpmInbActulDate());
        }
        data.setOriginalVersion(rowData.getOriginalVersion());
        if (null != rowData.getRevisionVersion()) {
            data.setRevisionVersion(rowData.getRevisionVersion());
        }
        if (null != rowData.getRevisionReason()) {
            data.setRevisionReason(rowData.getRevisionReason());
        }
        List<CPSSMF01PartEntity> partsList = new ArrayList<CPSSMF01PartEntity>();
        partsList.add(convertRowData2Part(rowData));
        data.setPartsList(partsList);
        return data;
    }

    /**
     * convert row data to partEntity
     * 
     * @param rowData CPSSMF01Entity
     * @return CPSSMF01PartEntity
     */
    private static CPSSMF01PartEntity convertRowData2Part(CPSSMF01Entity rowData) {
        CPSSMF01PartEntity item = new CPSSMF01PartEntity();
        item.setPartsId(rowData.getPartsId());
        item.setTtcPartsNo(rowData.getTtcPartsNo());
        item.setTtcSupplierCode(rowData.getTtcSupplierCode());
        item.setUom(rowData.getUom());
        item.setForceCompletedQty(rowData.getForceCompletedQty());
        item.setQty(rowData.getQty());
        item.setEpo(rowData.getEpo());
        item.setIpo(rowData.getIpo());
        item.setCpo(rowData.getCustomerOrderNo());
        item.setCustomerCode(rowData.getCustomerCode());
        return item;
    }

    /**
     * generate version title of plan or actual
     * 
     * @param isPlan isPlan
     * @param originalVersion originalVersion
     * @param revisionVersion revisionVersion
     * @param modColumnFlag isModColumn
     * @return version title
     */
    public static String generateVersion(boolean isPlan, Integer originalVersion, Integer revisionVersion,
        boolean modColumnFlag) {
        StringBuilder version = new StringBuilder();

        if (isPlan) {
            version.append("Plan ");
        } else {
            version.append("Actual ");
        }

        if (modColumnFlag) {
            if (revisionVersion.intValue() <= IntDef.INT_ZERO) {
                version.append(originalVersion).append(StringConst.MIDDLE_LINE).append(IntDef.INT_ONE);
            } else {
                version.append(originalVersion).append(StringConst.MIDDLE_LINE)
                    .append(revisionVersion + IntDef.INT_ONE);
            }
        } else {
            if (revisionVersion.intValue() <= IntDef.INT_ZERO) {
                version.append(originalVersion);
            } else {
                version.append(originalVersion).append(StringConst.MIDDLE_LINE).append(revisionVersion);
            }
        }

        return version.toString();
    }

    /**
     * Add Transprot Mode
     * 
     * @param param param
     * @param sheet sheet
     * @param rowNum rowNum
     * @param columnNum columnNum
     */
    public static void addTransprotMode(BaseParam param, Sheet sheet, int rowNum, int columnNum) {
        CellRangeAddressList regions = new CellRangeAddressList(rowNum - 1, rowNum - 1, columnNum - 1, columnNum - 1);
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(CodeCategoryManager.getCodeName(
            param.getLanguage(), CodeMasterCategory.TRANSPORT_MODE));
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(false);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }
        sheet.addValidationData(dataValidation);
    }

    /**
     * Remove style sheet
     * 
     * @param wbOutput SXSSFWorkbook
     */
    public static void removeStyleSheet(SXSSFWorkbook wbOutput) {

        String[] styleSheetNames = new String[] { SHEET_STYLE_CELL, SHEET_STYLE_CELL_VALUE, SHEET_STYLE_QTY_GRAY,
            SHEET_STYLE_QTY_WHITE, SHEET_STYLE_QTY_GRAY_RIGHT, SHEET_STYLE_QTY_WHITE_RIGHT,
            SHEET_STYLE_QTY_GRAY_BOTTOM, SHEET_STYLE_QTY_WHITE_BOTTOM, SHEET_STYLE_QTY_GRAY_LAST,
            SHEET_STYLE_QTY_WHITE_LAST };

        if (null != styleSheetNames && styleSheetNames.length > 0) {
            for (String sheetName : styleSheetNames) {
                wbOutput.removeSheetAt(wbOutput.getSheetIndex(sheetName));
            }
        }
    }

    /**
     * Count different ipo and customer order no.
     * 
     * @param planInfoList planInfoList
     * @return int
     */
    public static int countIpoAndCustomerOrderNo(List<CPSSMF01Entity> planInfoList) {
        List<String> ipoList = new ArrayList<String>();
        if (null != planInfoList && !planInfoList.isEmpty()) {
            for (CPSSMF01Entity entity : planInfoList) {
                String value = entity.getIpo() + StringConst.COMMA + entity.getCustomerOrderNo() + StringConst.COMMA
                        + entity.getCustomerCode();
                if (!ipoList.contains(value)) {
                    ipoList.add(value);
                }
            }
        }
        return ipoList.size();
    }

    /**
     * calculate before today column
     * 
     * @param beginColumn beginColumn
     * @param colDataList planAndActualList
     * @param needModNew needModNew
     * @return columnNum
     */
    public static int calculateBeforeTodayColumn(int beginColumn, List<CPSSMF01ColEntity> colDataList,
        boolean needModNew) {
        int columnNum = -1;

        // get bofore today list
        List<CPSSMF01ColEntity> boforeTodayList = new ArrayList<CPSSMF01ColEntity>();
        if (null != colDataList && !colDataList.isEmpty()) {
            for (int i = 0; i < colDataList.size(); i++) {
                CPSSMF01ColEntity item = colDataList.get(i);
                if (item.isBeforeToday()) {
                    boforeTodayList.add(item);
                }
            }
        }

        sortByEtd(boforeTodayList);
        int index = -1;
        if (null != boforeTodayList && !boforeTodayList.isEmpty()) {
            int lastIndex = boforeTodayList.size() - IntDef.INT_ONE;
            Date lastBeforeTodayEtd = boforeTodayList.get(lastIndex).getEtd();

            if (null != colDataList && !colDataList.isEmpty()) {
                int size = colDataList.size();
                for (int i = size - IntDef.INT_ONE; i >= 0; i--) {
                    CPSSMF01ColEntity item = colDataList.get(i);
                    if (item.isBeforeToday()) {
                        if (lastBeforeTodayEtd.compareTo(item.getEtd()) == 0) {
                            index = i;
                            break;
                        }
                    }
                }
            }
        }

        if (index >= 0) {
            columnNum = beginColumn;
            for (int i = 0; i <= index; i++) {
                CPSSMF01ColEntity item = colDataList.get(i);

                // 2016/07/01 shiyang add start
                if (item.getEtd() == null) {
                    continue;
                }
                // 2016/07/01 shiyang add end

                boolean actualInb = false;
                if (null != item.getIpmInbActulDate()) {
                    actualInb = true;
                }

                int step = IntDef.INT_ONE;
                if (needModNew && !actualInb) {
                    step = IntDef.INT_TWO;
                }

                if (i < index) {
                    columnNum = columnNum + step;
                }
            }
        }

        return columnNum;
    }

    /**
     * sort by etd
     * 
     * @param list list
     */
    private static void sortByEtd(List<CPSSMF01ColEntity> list) {
        if (null != list && list.size() > IntDef.INT_ONE) {
            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object arg0, Object arg1) {
                    CPSSMF01ColEntity entity1 = (CPSSMF01ColEntity) arg0;
                    CPSSMF01ColEntity entity2 = (CPSSMF01ColEntity) arg1;
                    int compareResult = entity1.getEtd().compareTo(entity2.getEtd());
                    return compareResult;
                }
            });
        }
    }
}
