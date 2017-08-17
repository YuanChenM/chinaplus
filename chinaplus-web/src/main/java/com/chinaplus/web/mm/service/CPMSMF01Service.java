/**
 * @screen CPMSMF01
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

import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.web.mm.entity.CPMSMF01Entity;

/**
 * CPMSMF01Service.
 */
@Service
public class CPMSMF01Service extends BaseService {

    /**
     * cpmpms02Service.
     */
    @Autowired
    private CPMPMS02Service cpmpms02Service;

    /**
     * Get all office code.
     * 
     * @return all office code
     */
    public String[] getAllOfficeCode() {

        TnmOffice officeCondition = new TnmOffice();
        officeCondition.setInactiveFlag(InactiveFlag.ACTIVE);
        List<TnmOffice> offices = super.baseDao.select(officeCondition);
        String[] allOfficeCode = new String[offices.size()];
        for (int i = 0; i < offices.size(); i++) {
            allOfficeCode[i] = offices.get(i).getOfficeCode();
        }

        return allOfficeCode;
    }

    /**
     * get CPMSRF01Entity List
     * 
     * @param ssmsCustomerCode ssmsCustomerCode
     * @param vendorRoute vendorRoute
     * @return cpmsmf01EntityList cpmsmf01EntityList
     */
    public List<CPMSMF01Entity> getCPMSMF01EntityList(String ssmsCustomerCode, String vendorRoute) {

        BaseParam param = new BaseParam();
        param.setSwapData("ssmsCustomerCode", ssmsCustomerCode);
        param.setSwapData("vendorRoute", cpmpms02Service.setCodeStringList(vendorRoute));

        List<CPMSMF01Entity> cpmsmf01EntityList = baseMapper.select(this.getSqlId("getCPMSMF01EntityList"), param);
        return cpmsmf01EntityList;
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
     * @param orionPlusNames orion plus names
     * @param allOfficeCode all office code
     * @throws BusinessException the exception
     */
    public void createOneDataRowByTemplate(Sheet sheet, int rowNum, Cell[] templateCells, Object[] data, Integer lang,
        String[] orionPlusNames, String[] allOfficeCode) throws BusinessException {
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
                        if (Cell.CELL_TYPE_FORMULA == cellType) {
                            cell.setCellFormula(templateCells[j].getCellFormula());
                        }
                    }

                    if (j == IntDef.INT_FIVE || j == IntDef.INT_SIX) {
                        String[] listDatas = null;
                        if (j == IntDef.INT_FIVE) {
                            listDatas = allOfficeCode;
                        } else {
                            listDatas = orionPlusNames;
                        }

                        CellRangeAddressList addressList = new CellRangeAddressList(rowNum, rowNum, j, j);
                        DataValidationHelper helper = sheet.getDataValidationHelper();
                        DataValidationConstraint constraint = helper.createExplicitListConstraint(listDatas);
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
