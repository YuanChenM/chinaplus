/**
 * @screen CPMSRF02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.web.mm.entity.CPMSRF02Entity;

/**
 * CPMSRF02Service.
 */
@Service
public class CPMSRF02Service extends BaseService {

    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMSRF01Service cpmsrf01Service;

    /**
     * get CPMSRF02Entity List
     * 
     * @param impOfficeId impOfficeId
     * @param shipRouteCode shipRouteCode
     * @param effFromEtd effFromEtd
     * @param effToEtd effToEtd
     * @return cpmsrf02EntityList cpmsrf02EntityList
     */
    public List<CPMSRF02Entity> getCPMSRF02EntityList(String impOfficeId, String shipRouteCode, Date effFromEtd,
        Date effToEtd) {
        BaseParam param = new BaseParam();
        param.setSwapData("impOfficeId", cpmsrf01Service.setCodeList(impOfficeId));
        param.setSwapData("shipRouteCode", cpmsrf01Service.setCodeStringList(shipRouteCode));
        param.setSwapData("effFromEtd", effFromEtd);
        param.setSwapData("effToEtd", effToEtd);
        List<CPMSRF02Entity> cpmsrf02EntityList = baseMapper.select(this.getSqlId("getCPMSRF02EntityList"), param);
        return cpmsrf02EntityList;
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
     * @throws BusinessException the exception
     */
    public void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data, Integer lang)
        throws BusinessException {
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

                        if (j == IntDef.INT_TEN) {
                            String formulaQ = "IF(J6=\"\",\"\",IF(MOD(J6+L6,7)=0,7,MOD(J6+L6,7)))";
                            int number = rowNum + IntDef.INT_ONE;
                            formulaQ = formulaQ.replaceAll("J6", "J" + number);
                            formulaQ = formulaQ.replaceAll("L6", "L" + number);
                            cell.setCellFormula(formulaQ);
                        } else if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[j].getCellFormula());
                        }
                    }

                    if (j == IntDef.INT_THIRTEEN) {
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
}
