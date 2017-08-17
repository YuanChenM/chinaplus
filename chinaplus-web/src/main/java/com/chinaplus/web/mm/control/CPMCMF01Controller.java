/**
 * CPMCMF01Control.
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.PoiUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.service.CPMCMF01Service;

/**
 * Calendar Master Download file
 */
@Controller
public class CPMCMF01Controller extends BaseFileController {

    /** The main sheet name */
    private static final String SHEET_NAME = "display Year";
    /** The style sheet name */
    private static final String SHEET_STYLE = "styleSheet";

    @Autowired
    private CPMCMF01Service service;

    /**
     * getFileId
     * 
     * @return String
     * @see com.yanmar.core.base.BaseFileController#getFileId()
     */
    @Override
    protected String getFileId() {
        return ChinaPlusConst.FileId.CPMCMF01;
    }

    /**
     * check before download
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<String>
     */
    @RequestMapping(value = "/master/CPMCMF01/downloadcheck")
    @ResponseBody
    public BaseResult<String> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {
        BaseResult<String> result = new BaseResult<String>();
        return result;
    }

    /**
     * download
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    @RequestMapping(value = "/master/CPMCMF01/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        PageParam param = this.convertJsonDataForForm(PageParam.class);
        try {
            // checkbox or filters
            if (null != param && null != param.getSwapData()) {
                @SuppressWarnings("unchecked")
                ArrayList<String> list = (ArrayList<String>) param.getSwapData().get("nonWorkingDays");
                param.setSelectedDatas(list);
            }
        } catch (Exception e) {}
        String fileName = "CP_WorkingCalendar_" + param.getClientTime() + ".xlsx";

        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    /**
     * addZero 补零
     * 
     * @param num int
     * @return String
     */
    public String addZero(int num) {
        try {
            if (num < IntDef.INT_TEN) {
                return "0" + num;
            } else {
                return "" + num;
            }
        } catch (Exception e) {
            return num + StringConst.EMPTY;
        }
    }

    @Override
    protected <T extends BaseParam> void writeDynamicTemplate(T tparam, Workbook workbook) {
        PageParam param = null;
        try {
            param = (PageParam) tparam;
        } catch (Exception e) {
            param = new PageParam();
            param.setSwapData("regionId", tparam.getSwapData().get("regionId"));
            param.setSwapData("calendarId", tparam.getSwapData().get("calendarId"));
            param.setSwapData("year", tparam.getSwapData().get("year"));
            param.setSwapData("office", tparam.getSwapData().get("office"));
            param.setSwapData("partyType", tparam.getSwapData().get("partyType"));
            param.setSwapData("partyCode", tparam.getSwapData().get("partyCode"));
            param.setSwapData("calendarRawId", tparam.getSwapData().get("calendarRawId"));
            
            
        }
        String year = "";
        String country = null;
        String code = null;
        Integer rawId = null;
        String office = "";
        String party = "";
        String partyCode = "";
        if (null != param && null != param.getSwapData()) {
            String regionId = StringUtil.toSafeString(param.getSwapData().get("regionId"));
            String calendarId = StringUtil.toSafeString(param.getSwapData().get("calendarId"));
            year = StringUtil.toSafeString(param.getSwapData().get("year"));
            office = StringUtil.toSafeString(param.getSwapData().get("office"));
            party = StringUtil.toSafeString(param.getSwapData().get("partyType"));
            partyCode = StringUtil.toSafeString(param.getSwapData().get("partyCode"));
            code = calendarId;
            if(!StringUtil.isNullOrEmpty(regionId)){
                country = service.getRegionCodeById(regionId);
            }
            rawId = StringUtil.toInteger(param.getSwapData().get("calendarRawId"));
        }
        if (StringUtil.isEmpty(year)) {
            Calendar rightNow = Calendar.getInstance();
            year = rightNow.get(Calendar.YEAR) + "";
        }
        Map<String, Integer> nonWorkingDays = new HashMap<String, Integer>(0);

        List<String> dates = param.getSelectedDatas();
        if (dates != null && !dates.isEmpty()) {
            nonWorkingDays = new HashMap<String, Integer>(dates.size());
            for (String str : dates) {
                nonWorkingDays.put(str, 1);
            }
        } else {
            dates = service.getCalendar(rawId, year);
            nonWorkingDays = new HashMap<String, Integer>(dates.size());
            for (String str : dates) {
                nonWorkingDays.put(str, 1);
            }
        }

        Sheet fromControl = workbook.getSheet(SHEET_NAME);
        Sheet styleSheet = workbook.getSheet(SHEET_STYLE);
        Sheet sheetControl = workbook.createSheet(year);

        PoiUtil.copySheet(workbook, fromControl, sheetControl);

        CellStyle nonWorkingDayStyle = PoiUtil.getCell(styleSheet, IntDef.INT_TWO, IntDef.INT_TWO).getCellStyle();

        PoiUtil.setCellValue(sheetControl, IntDef.INT_FOUR, IntDef.INT_SIX, office);
        PoiUtil.setCellValue(sheetControl, IntDef.INT_FIVE, IntDef.INT_SIX, party);
        PoiUtil.setCellValue(sheetControl, IntDef.INT_FIVE, IntDef.INT_THIRTEEN, country);
        PoiUtil.setCellValue(sheetControl, IntDef.INT_FIVE, IntDef.INT_TWENTY, partyCode);
        PoiUtil.setCellValue(sheetControl, IntDef.INT_SIX, IntDef.INT_THIRTEEN, year);
        
        PoiUtil.setCellValue(sheetControl, IntDef.INT_SIX, IntDef.INT_SIX, code);
        
        

        int row = IntDef.INT_EIGHT;
        int col = IntDef.INT_THREE;
        int rlength = IntDef.INT_SIX;
        int clength = IntDef.INT_SEVEN;
        int size = rlength * clength;

        PoiUtil.setCellValue(sheetControl, row, col + IntDef.INT_TEN, year);
        int row1 = row + IntDef.INT_THREE;
        List<Cell> jan = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row1 + r, col + c);
                jan.add(cell);
            }
        }
        col += clength;
        List<Cell> feb = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row1 + r, col + c);
                feb.add(cell);
            }
        }
        col += clength;
        List<Cell> mar = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row1 + r, col + c);
                mar.add(cell);
            }
        }
        int row2 = row1 + rlength + IntDef.INT_TWO;
        col = IntDef.INT_THREE;
        List<Cell> apr = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row2 + r, col + c);
                apr.add(cell);
            }
        }
        col += clength;
        List<Cell> may = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row2 + r, col + c);
                may.add(cell);
            }
        }
        col += clength;
        List<Cell> jun = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row2 + r, col + c);
                jun.add(cell);
            }
        }
        int row3 = row2 + rlength + IntDef.INT_TWO;
        col = IntDef.INT_THREE;
        List<Cell> jul = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row3 + r, col + c);
                jul.add(cell);
            }
        }
        col += clength;
        List<Cell> aug = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row3 + r, col + c);
                aug.add(cell);
            }
        }
        col += clength;
        List<Cell> sep = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row3 + r, col + c);
                sep.add(cell);
            }
        }
        int row4 = row3 + rlength + IntDef.INT_TWO;
        col = IntDef.INT_THREE;
        List<Cell> oct = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row4 + r, col + c);
                oct.add(cell);
            }
        }
        col += clength;
        List<Cell> nov = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row4 + r, col + c);
                nov.add(cell);
            }
        }
        col += clength;
        List<Cell> dec = new ArrayList<Cell>(size);
        for (int r = 0; r < rlength; r++) {
            for (int c = 0; c < clength; c++) {
                Cell cell = PoiUtil.getOrCreateCell(sheetControl, row4 + r, col + c);
                dec.add(cell);
            }
        }

        List<List<Cell>> months = new ArrayList<List<Cell>>(IntDef.INT_TWELVE);
        months.add(jan);
        months.add(feb);
        months.add(mar);
        months.add(apr);
        months.add(may);
        months.add(jun);
        months.add(jul);
        months.add(aug);
        months.add(sep);
        months.add(oct);
        months.add(nov);
        months.add(dec);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = -1;
        int dayOfMonth = -1;
        List<Cell> month = null;
        int j;
        int[] end = new int[IntDef.INT_TWELVE];
        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_YYYYMMDD);
        for (int i = 0; i < IntDef.INT_TWELVE; i++) {
            calendar.set(Integer.parseInt(year), i, 1);
            dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            month = months.get(i);
            for (j = dayOfWeek; j <= size && calendar.get(Calendar.MONTH) == i; j++) {
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                month.get(j).setCellValue(dayOfMonth);
                if (nonWorkingDays.containsKey(sdf.format(calendar.getTime()))) {
                    month.get(j).setCellStyle(nonWorkingDayStyle);
                }
                calendar.set(Integer.parseInt(year), i, dayOfMonth + 1);
            }
            end[i] = j;
        }
        int i = 0;
        if (end[0] < IntDef.INT_THIRTY_SIX && end[1] < IntDef.INT_THIRTY_SIX
                && end[IntDef.INT_TWO] < IntDef.INT_THIRTY_SIX) {
            sheetControl.shiftRows(row1 + IntDef.INT_FIVE, sheetControl.getLastRowNum(), -1);
            i++;
        }
        if (end[IntDef.INT_THREE] < IntDef.INT_THIRTY_SIX && end[IntDef.INT_FOUR] < IntDef.INT_THIRTY_SIX
                && end[IntDef.INT_FIVE] < IntDef.INT_THIRTY_SIX) {
            sheetControl.shiftRows(row2 + IntDef.INT_FIVE - i, sheetControl.getLastRowNum(), -1);
            i++;
        }
        if (end[IntDef.INT_SIX] < IntDef.INT_THIRTY_SIX && end[IntDef.INT_SEVEN] < IntDef.INT_THIRTY_SIX
                && end[IntDef.INT_EIGHT] < IntDef.INT_THIRTY_SIX) {
            sheetControl.shiftRows(row3 + IntDef.INT_FIVE - i, sheetControl.getLastRowNum(), -1);
            i++;
        }
        if (end[IntDef.INT_NINE] < IntDef.INT_THIRTY_SIX && end[IntDef.INT_TEN] < IntDef.INT_THIRTY_SIX
                && end[IntDef.INT_ELEVEN] < IntDef.INT_THIRTY_SIX) {
            sheetControl.shiftRows(row4 + IntDef.INT_FIVE - i, sheetControl.getLastRowNum(), -1);
        }
        workbook.removeSheetAt(workbook.getSheetIndex(SHEET_STYLE));
        workbook.removeSheetAt(workbook.getSheetIndex(SHEET_NAME));
    }

}
