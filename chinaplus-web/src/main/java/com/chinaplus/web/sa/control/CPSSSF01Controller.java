/**
 * Stock Status Report download controller
 * 
 * @screen CPSSSF01
 * @author liu_yinchuan
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.TntStockStatusEx;
import com.chinaplus.common.bean.TntStockStatusHeader;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.service.StockStatusService;
import com.chinaplus.common.util.StockStatusManager;
import com.chinaplus.core.base.BaseEntity;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.StringUtil;

/**
 * Stock Status Report download
 */
@Controller
public class CPSSSF01Controller extends BaseFileController {

    /** DOWNLOAD_NAME */
    private static final String DOWNLOAD_NAME = "StockStatus_{0}.xlsx";

    /** HEADER_MAP */
    private static final String HEADER_MAP = "HEADER_MAP";
    
    @Autowired
    private StockStatusService service;

    @Override
    protected String getFileId() {
        return FileId.CPSSSF01;
    }

    /**
     * Stock Status Report download check.
     * 
     * @param param BaseParam
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return BaseResult<BaseEntity>
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/sa/CPSSSF01/downloadcheck",
        method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<BaseEntity> downloadCheck(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) {

        // message list
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();

        // get stock status ids
        List<String> stockStatusIds = (List<String>) param.getSwapData().get("stockStatusIds");
        if (stockStatusIds != null && !stockStatusIds.isEmpty()) {

            // set entity list
            param.setFilters(new HashMap<String, Object>());
        } else {
            // build like conditions
            service.makeLikeCondition(param);
        }

        // set common information
        this.setCommonParam(param, request);
        // check vv and aisin flag
        UserOffice office = super.getLoginUser(request).getCurrentOffice();
        // prepare picked up status
        List<Integer> buiPattList = new ArrayList<Integer>();
        // if has vv data
        if (office.getVvFlag()) {
            // add vv parts
            buiPattList.add(BusinessPattern.V_V);
        }
        // if has aisin
        if (office.getAisinFlag()) {
            // add AISIN parts
            buiPattList.add(BusinessPattern.AISIN);
        }
        // set into list
        param.setSwapData("BusinessPatternList", buiPattList);

        // check count
        int count = service.getSelectDatasCount(param);

        // if no data
        if (count <= IntDef.INT_ZERO) {
            // set entity list
            BaseMessage message = new BaseMessage(MessageCodeConst.W1005_001);
            messageLists.add(message);
        }

        // if has message, throw them
        if (messageLists.size() != 0) {
            throw new BusinessException(messageLists);
        }

        // if ok
        return new BaseResult<BaseEntity>();
    }

    /**
     * Do download.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/sa/CPSSSF01/download",
        method = RequestMethod.POST)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        // set common parameters by session
        BaseParam param = this.convertJsonDataForForm(BaseParam.class);
        
        // do check
        List<String> stockStatusIds = (List<String>) param.getSwapData().get("stockStatusIds");
        if (stockStatusIds != null && !stockStatusIds.isEmpty()) {
            // set entity list
            param.setFilters(new HashMap<String, Object>());
        } else {
            // build like conditions
            service.makeLikeCondition(param);
        }
        
        // set common information
        this.setCommonParam(param, request);
        // check vv and aisin flag
        UserOffice office = super.getLoginUser(request).getCurrentOffice();
        // prepare picked up status
        List<Integer> buiPattList = new ArrayList<Integer>();
        // if has vv data
        if (office.getVvFlag()) {
            // add vv parts
            buiPattList.add(BusinessPattern.V_V);
        }
        // if has aisin
        if (office.getAisinFlag()) {
            // add AISIN parts
            buiPattList.add(BusinessPattern.AISIN);
        }
        // set into list
        param.setSwapData("BusinessPatternList", buiPattList);
        
        // get file name
        String fileName = StringUtil.formatMessage(DOWNLOAD_NAME, param.getClientTime());

        // do excel with template
        this.downloadExcelWithTemplate(fileName, param, request, response);
    }

    /**
     * Get the excel template file path by file id.
     * 
     * @param workbook the template excel
     * @param param the parameter
     * @param <T> the parameter class type
     */
    @Override
    protected <T extends BaseParam> void writeDynamicTemplate(T param, Workbook workbook) {
        
        // do nothing in base
        Map<Integer, String> headMap = new HashMap<Integer, String>();
        Language lang = null;
        
        // get language
        if (param.getLanguage() != null && Language.CHINESE.getCode() == param.getLanguage().intValue()) {
            lang = Language.CHINESE;
        } else {
            lang = Language.ENGLISH;
        }
        
        // get stock status header
        TntStockStatusHeader header = service.getStockStatusHeader(param);
        
        // prepare header
        StockStatusManager.setHeaderForStockStatus(workbook, lang, headMap, header);
        
        // set into parameter
        param.setSwapData(HEADER_MAP, headMap);
    }

    /**
     * Write content to excel.
     *
     * @param param parameter
     * @param wbTemplate workbook template
     * @param wbOutput workbook out put
     * @param fieldId field Id
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam, org.apache.poi.ss.usermodel.Workbook,
     *      org.apache.poi.xssf.streaming.SXSSFWorkbook, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {
        
        // get header map
        Map<Integer, String> headMap = (Map<Integer, String>) param.getSwapData().get(HEADER_MAP);
        Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
        Date serverTime = service.getDBDateTime(param.getOfficeTimezone());
        Language lang = null;
        // get language
        if (param.getLanguage() != null && Language.CHINESE.getCode() == param.getLanguage().intValue()) {
            lang = Language.CHINESE;
        } else {
            lang = Language.ENGLISH;
        }
        
        // get stock status list
        List<TntStockStatusEx> stockStatusList = service.getStockStatusInfoList(param, lang);
        
        // get style sheet
        StockStatusManager.prepStyleMapStockStatus(wbTemplate, styleMap, lang);
        
        // prepare date list
        Date[] dateList = new Date[IntDef.INT_FOUR];
        dateList[IntDef.INT_ZERO] = serverTime;
        // prepare Sync time
        service.prepareSyncDateTime(param.getCurrentOfficeId(), dateList);
        
        // prepare data
        StockStatusManager.setStockStatusDetailInfo(wbOutput, stockStatusList, lang, styleMap, headMap,
            param.getCurrentOfficeCode(), dateList);
    }
}
