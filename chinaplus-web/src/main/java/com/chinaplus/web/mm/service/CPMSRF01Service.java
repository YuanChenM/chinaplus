/**
 * @screen CPMSRF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMSRF01Entity;

/**
 * CPMSRF01Service.
 */
@Service
public class CPMSRF01Service extends BaseService {

    /**
     * set String to list
     * 
     * @param Code Code
     * @return List<Integer> codeList
     */
    public List<String> setCodeStringList(String Code) {
        List<String> codeList = new ArrayList<String>();
        if (!StringUtil.isNullOrEmpty(Code)) {
            String[] Codes = Code.split(",");
            for (String c : Codes) {
                codeList.add(c);
            }
        }
        return codeList;
    }

    /**
     * set User Office Id
     * 
     * @param Code Code
     * @return List<Integer> codeList
     */
    public List<Integer> setCodeList(String Code) {
        List<Integer> codeList = new ArrayList<Integer>();
        if (!StringUtil.isNullOrEmpty(Code)) {
            String[] Codes = Code.split(",");
            for (String c : Codes) {
                codeList.add(Integer.valueOf(c));
            }
        }
        return codeList;
    }

    /**
     * get CPMSRF01Entity List
     * 
     * @param impOfficeId impOfficeId
     * @param shipRouteCode shipRouteCode
     * @param effFromEtd effFromEtd
     * @param effToEtd effToEtd
     * @return cpmsrf01EntityList cpmsrf01EntityList
     */
    public List<CPMSRF01Entity> getCPMSRF01EntityList(String impOfficeId, String shipRouteCode, Date effFromEtd,
        Date effToEtd) {

        BaseParam param = new BaseParam();
        param.setSwapData("impOfficeId", setCodeList(impOfficeId));
        param.setSwapData("shipRouteCode", setCodeStringList(shipRouteCode));
        param.setSwapData("effFromEtd", effFromEtd);
        param.setSwapData("effToEtd", effToEtd);

        List<CPMSRF01Entity> cpmsrf01EntityList = baseMapper.select(this.getSqlId("getCPMSRF01EntityList"), param);

        return cpmsrf01EntityList;
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
     * @param codeMaps codeMaps
     * @param lang lang
     * @throws BusinessException the exception
     */
    public void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data,
        Map<String, String> codeMaps, Integer lang) throws BusinessException {
        if (sheet != null && data != null) {

            // output data rows
            int templateCellCount = 0;
            if (templateCells != null) {
                templateCellCount = templateCells.length;
            }

            if (data != null) {
                int rowItemCount = data.length;
                // create new data row
                Row dataRow = sheet.createRow(rowNum);
                for (int j = 0; j < rowItemCount; j++) {
                    // create cell
                    Cell cell = dataRow.createCell(j);

                    // set cell format, formula, type
                    if (templateCells != null && j < templateCellCount && null != templateCells[j]) {
                        cell.setCellStyle(templateCells[j].getCellStyle());
                        int cellType = templateCells[j].getCellType();
                        cell.setCellType(cellType);

                        if (j == IntDef.INT_FOUR || j == IntDef.INT_SIX || j == IntDef.INT_EIGHT || j == IntDef.INT_TEN
                                || j == IntDef.INT_TWELVE || j == IntDef.INT_FOURTEEN || j == IntDef.INT_SIXTEEN
                                || j == IntDef.INT_EIGHTEEN || j == IntDef.INT_TWENTY || j == IntDef.INT_TWENTY_TWO
                                || j == IntDef.INT_TWENTY_FOUR) {
                            String code = codeMaps.get(j + "");
                            int number = rowNum + IntDef.INT_ONE;
                            String formulaQ = new StringBuilder().append("IF(").append(code).append(number)
                                .append("=\"\", \"\",").append("TEXT(WEEKDAY(").append(code).append(number)
                                .append("),\"(ddd)\"))").toString();
                            cell.setCellFormula(formulaQ);
                        } else if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[j].getCellFormula());
                        }
                    }

                    if (j == IntDef.INT_TWENTY_SEVEN) {
                        String[] pos = CodeCategoryManager.getCodeName(lang, CodeMasterCategory.DISCONTINUE_INDICATOR);
                        CellRangeAddressList addressList = new CellRangeAddressList(rowNum, rowNum, j, j);
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

    /**
     * set column code
     * 
     * @return maps
     */
    public Map<String, String> getCode() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("4", "D");
        maps.put("6", "F");
        maps.put("8", "H");
        maps.put("10", "J");
        maps.put("12", "L");
        maps.put("14", "N");
        maps.put("16", "P");
        maps.put("18", "R");
        maps.put("20", "T");
        maps.put("22", "V");
        maps.put("24", "X");
        return maps;
    }

}
