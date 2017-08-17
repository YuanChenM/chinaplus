/**
 * @screen CPMPMF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ConditionalFormattingRule;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.PatternFormatting;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.common.util.MasterManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMF01Entity;
import com.chinaplus.web.mm.entity.CPMPMS01Entity;

/**
 * CPMPMF01Service.
 */
@Service
public class CPMPMF01Service extends BaseService {

    /**
     * get CPMPMF01Entity List from page
     * 
     * @param dataLists dataLists
     * @param type type
     * @param conditionParam condition parameter
     * @return cpmpmf01EntityList cpmpmf01EntityList
     */
    public List<CPMPMF01Entity> getCpmpmf01EntityListFP(List<CPMPMS01Entity> dataLists, int type,
        BaseParam conditionParam) {

        List<CPMPMF01Entity> cpmpmf01EntityList = null;
        if (dataLists != null && dataLists.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("dataLists", dataLists);
            param.setSwapData("type", type);
            cpmpmf01EntityList = baseMapper.select(this.getSqlId("getCpmpmf01EntityListFP"), param);
        } else {
            conditionParam.setSwapData("type", type);
            StringUtil.buildLikeCondition(conditionParam, "ttcPartNo");
            StringUtil.buildLikeCondition(conditionParam, "partsDesEnglish");
            StringUtil.buildLikeCondition(conditionParam, "partsDesChinese");
            StringUtil.buildLikeCondition(conditionParam, "oldTTCPartNo");
            StringUtil.buildLikeCondition(conditionParam, "ssmsSuppCd");
            StringUtil.buildLikeCondition(conditionParam, "suppPartNo");
            StringUtil.buildLikeCondition(conditionParam, "ssmskbCustCd");
            StringUtil.buildLikeCondition(conditionParam, "custPartNo");
            StringUtil.buildLikeCondition(conditionParam, "westCustCd");
            StringUtil.buildLikeCondition(conditionParam, "westPartNo");
            StringUtil.buildLikeCondition(conditionParam, "carModel");
            StringUtil.buildLikeCondition(conditionParam, "createdBy");
            StringUtil.buildLikeCondition(conditionParam, "lastModifiedBy");
            StringUtil.buildDateTimeCondition(conditionParam, "createdDate");
            StringUtil.buildDateTimeCondition(conditionParam, "lastModifiedDate");
            cpmpmf01EntityList = baseMapper.select(this.getSqlId("getCpmpmf01EntityListFPCondition"), conditionParam);
        }

        return cpmpmf01EntityList;
    }

    /**
     * get CPMPMF01Entity List
     * 
     * @param param param
     * @param type type
     * @return cpmpmf01EntityList cpmpmf01EntityList
     */
    public List<CPMPMF01Entity> getCpmpmf01EntityList(BaseParam param, int type) {
        param.setSwapData("type", type);
        List<CPMPMF01Entity> cpmpmf01EntityList = baseMapper.select(this.getSqlId("getCpmpmf01EntityList"), param);
        return cpmpmf01EntityList;
    }

    /**
     * Create data rows from start row.
     * The start row is template row, include cells style.
     * templateCells's type is Decimal,set cell style with
     * parameter(decimalStyle)
     * 
     * @param sheet the sheet
     * @param rowNum the row number
     * @param templateCells template cells
     * @param data the data those will be output (data are formated)
     * @param lang lang
     * @param type type
     * @param maps maps
     * @param scf scf
     * @param totalSize totalSize
     * @throws BusinessException the exception
     */
    public void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data, Integer lang,
        int type, Map<Integer, String> maps, SheetConditionalFormatting scf, int totalSize) throws BusinessException {
        if (sheet != null && data != null) {

            // output data rows
            int templateCellCount = 0;
            if (templateCells != null) {
                templateCellCount = templateCells.length;
            }

            if (data != null) {
                int rowItemCount = data.length;
                Integer uomDigits = MasterManager.getUomDigits((String) data[IntDef.INT_SIX]);
                Integer realNum = IntDef.INT_SIXTYEIGHT + uomDigits;
                // create new data row
                Row dataRow = sheet.createRow(rowNum);
                for (int j = 0; j < rowItemCount; j++) {
                    // create cell
                    Cell cell = dataRow.createCell(j);

                    // set cell format, formula, type
                    if (templateCells != null && j < templateCellCount && null != templateCells[j]) {
                        Integer jj = j;
                        if (j == IntDef.INT_TWENTY_FIVE || j == IntDef.INT_TWENTY_SIX || j == IntDef.INT_TWENTY_SEVEN) {
                            jj = realNum;
                        }
                        cell.setCellStyle(templateCells[jj].getCellStyle());
                        int cellType = templateCells[jj].getCellType();
                        cell.setCellType(cellType);
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[jj].getCellFormula());
                        }
                    }

                    // Set excel condition style
                    if (IntDef.INT_EIGHT == rowNum) {
                        String cfrRule = "";
                        int ruleNum = rowNum + 1;
                        if (j == IntDef.INT_NINE || j == IntDef.INT_TEN || j == IntDef.INT_ELEVEN
                                || j == IntDef.INT_TWENTY_THREE || j == IntDef.INT_TWENTY_FOUR
                                || j == IntDef.INT_THIRTY_THREE || j == IntDef.INT_THIRTY_SEVEN
                                || j == IntDef.INT_FIFTY_ONE || j == IntDef.INT_FIFTY_TWO
                                || j == IntDef.INT_FIFTY_THREE || j == IntDef.INT_FIFTY_FOUR
                                || j == IntDef.INT_FIFTY_FIVE || j == IntDef.INT_SIXTYSIX) {
                            cfrRule = StringConst.DOLLAR + "AD" + ruleNum + StringConst.EQUATE + "\"" + "AISIN" + "\"";
                        } else if (j == IntDef.INT_TWENTY_ONE) {
                            cfrRule = StringConst.DOLLAR + "AD" + ruleNum + StringConst.EQUATE + "\"" + "V-V" + "\"";
                        } else if (j == IntDef.INT_FOURTY_ONE || j == IntDef.INT_FOURTY_TWO) {
                            cfrRule = StringConst.DOLLAR + "AO" + ruleNum + StringConst.EQUATE + "\"" + "Y" + "\"";
                        } else if (j == IntDef.INT_FOURTYTHREE || j == IntDef.INT_FOURTY_FOUR) {
                            cfrRule = StringConst.DOLLAR + "AO" + ruleNum + StringConst.EQUATE + "\"" + "N" + "\"";
                        } else if (j == IntDef.INT_SIXTYTHREE || j == IntDef.INT_SIXTYFOUR || j == IntDef.INT_SIXTYFIVE) {
                            cfrRule = StringConst.DOLLAR + "BK" + ruleNum + StringConst.EQUATE + "\"" + "N" + "\"";
                        }
                        if (!StringUtil.isNullOrEmpty(cfrRule)) {
                            String rule = maps.get(j);
                            String craRule = rule + ruleNum + StringConst.COLON + rule + (rowNum + totalSize);
                            ConditionalFormattingRule cfr = scf.createConditionalFormattingRule(cfrRule);
                            PatternFormatting pf = cfr.createPatternFormatting();
                            pf.setFillBackgroundColor(HSSFColor.GREY_50_PERCENT.index);
                            ConditionalFormattingRule[] cfRules = { cfr };
                            CellRangeAddress[] regions = { CellRangeAddress.valueOf(craRule) };
                            scf.addConditionalFormatting(regions, cfRules);
                        }

                        String[] pos = null;
                        if (j == IntDef.INT_TWENTY_NINE) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_PATTERN);
                        } else if (j == IntDef.INT_THIRTY) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUSINESS_TYPE);
                        } else if (j == IntDef.INT_THIRTY_ONE) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_TYPE);
                        } else if (j == IntDef.INT_THIRTY_FIVE) {
                            pos = new String[] { "1", "2", "3", "4", "5", "6" };
                        } else if (j == IntDef.INT_THIRTY_SIX) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ORDER_FORECAST_TYPE);
                        } else if (j == IntDef.INT_THIRTY_SEVEN) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.EXPORT_WH_CALENDER);
                        } else if (j == IntDef.INT_THIRTY_EIGTH || j == IntDef.INT_THIRTY_NINE) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUSTOMER_STOCK_FLAG);
                        } else if (j == IntDef.INT_FOURTY) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.INVENTORY_BY_BOX);
                        } else if (j == IntDef.INT_FOURTY_EIGTH) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.SIMULATION_END_DAY_P);
                        } else if (j == IntDef.INT_FIFTY) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.ON_SHIPPING_DELAY_ADJUST_P);
                        } else if (j == IntDef.INT_FIFTY_SEVEN) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P1);
                        } else if (j == IntDef.INT_FIFTYEIGHT) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.CUST_FORECAST_ADJUST_P2);
                        } else if (j == IntDef.INT_SIXTY_TWO) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.BUILD_OUT_INDICATOR);
                        } else if (j == IntDef.INT_SIXTYSIX) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.PARTS_STATUS);
                        } else if (j == IntDef.INT_SIXTYSEVEN) {
                            pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR);
                        }

                        if (pos != null) {
                            CellRangeAddressList addressList = new CellRangeAddressList(rowNum, rowNum + totalSize - 1,
                                j, j);
                            DataValidationHelper helper = sheet.getDataValidationHelper();
                            DataValidationConstraint constraint = helper.createExplicitListConstraint(pos);
                            DataValidation dataValidation = helper.createValidation(constraint, addressList);
                            if (dataValidation instanceof XSSFDataValidation) {
                                dataValidation.setSuppressDropDownArrow(true);
                                dataValidation.setShowErrorBox(true);
                            } else {
                                dataValidation.setSuppressDropDownArrow(false);
                            }
                            sheet.addValidationData(dataValidation);
                        }
                    }

                    // set cell value
                    Object cellValue = data[j];
                    if (cellValue == null) {
                        // nothing to set
                        ;
                    } else if (cellValue instanceof Date) {
                        cell.setCellValue((Date) cellValue);
                    } else if (cellValue instanceof Calendar) {
                        cell.setCellValue((Calendar) cellValue);
                    } else if (cellValue instanceof String) {
                        cell.setCellValue(cellValue.toString());
                    } else if (cellValue instanceof Boolean) {
                        cell.setCellValue(((Boolean) cellValue));
                    } else if (cellValue instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) cellValue).doubleValue());
                    } else if (cellValue instanceof Integer) {
                        cell.setCellValue(((Integer) cellValue).doubleValue());
                    } else if (cellValue instanceof Double) {
                        cell.setCellValue(((Double) cellValue).doubleValue());
                    } else {
                        cell.setCellValue(String.valueOf(cellValue));
                    }
                }
            }
        }
    }
}
