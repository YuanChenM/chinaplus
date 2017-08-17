/**
 * Controller of V-V Business Shipping Route Master Download
 * 
 * @screen CPMSRF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.chinaplus.common.consts.ChinaPlusConst.FileId;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMSRF01Entity;
import com.chinaplus.web.mm.service.CPMSRF01Service;

/**
 * Controller of V-V Business Shipping Route Master Download
 */
@Controller
public class CPMSRF01Controller extends BaseFileController {

    private static final String STYLE = "style";

    private static final String VV_SHIPPING_ROUTE = "V-V_Shipping_Route";

    /**
     * cpmpms01Service.
     */
    @Autowired
    private CPMSRF01Service cpmsrf01Service;

    @Override
    protected String getFileId() {
        return FileId.CPMSRF01;
    }

    /**
     * download shipRouteType is V-V
     *
     * @param param
     * @param wbTemplate
     * @param wbOutput
     * @see com.chinaplus.core.base.BaseFileController#writeContentToExcel(com.chinaplus.core.bean.BaseParam,
     *      org.apache.poi.ss.usermodel.Workbook, org.apache.poi.xssf.streaming.SXSSFWorkbook)
     */
    @Override
    protected <T extends BaseParam> void writeContentToExcel(T param, Workbook wbTemplate, SXSSFWorkbook wbOutput) {

        boolean blankFormatDownSR = (boolean) param.getSwapData().get("blankFormatDownSR");
        List<CPMSRF01Entity> cpmsrf01EntityList = new ArrayList<CPMSRF01Entity>();

        if (!blankFormatDownSR) {
            String impOfficeId = StringUtil.toSafeString(param.getSwapData().get("srImpOfficeCode"));
            String shipRouteCode = (String) param.getSwapData().get("shipRouteCode");
            String effFromEtd = (String) param.getSwapData().get("effFromEtd");
            String effToEtd = (String) param.getSwapData().get("effToEtd");
            cpmsrf01EntityList = cpmsrf01Service.getCPMSRF01EntityList(impOfficeId, shipRouteCode,
                DateTimeUtil.parseDate(effFromEtd, "yyyy-MM-dd"), DateTimeUtil.parseDate(effToEtd, "yyyy-MM-dd"));
        } else {
            for(int i = IntDef.INT_ZERO; i < IntDef.INT_TEN; i++) {
                cpmsrf01EntityList.add(new CPMSRF01Entity());
            }
        }

        if (cpmsrf01EntityList != null && cpmsrf01EntityList.size() > 0) {

            Cell[] TemplateCells = getTemplateCells(STYLE, IntDef.INT_TWO, wbTemplate);
            org.apache.poi.ss.usermodel.Sheet sheet = wbTemplate.getSheet(VV_SHIPPING_ROUTE);
            Map<String, String> codeMaps = cpmsrf01Service.getCode();

            for (int i = 0; i < cpmsrf01EntityList.size(); i++) {
                CPMSRF01Entity entity = cpmsrf01EntityList.get(i);
                if (null == entity) {
                    continue;
                }
                Object[] arrayObj = null;

                if (entity.getInactiveFlag() != null) {
                    entity.setInactiveFlag(CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.DISCONTINUE_INDICATOR, Integer.valueOf(entity.getInactiveFlag())));
                }

                arrayObj = new Object[] { "", "", entity.getShippingRouteCode(), entity.getDeliveryStart(), "",
                    entity.getDeliveryEnd(), "", entity.getPackingEnd(), "", entity.getLastVanning(), "",
                    entity.getShippingInstruction(), "", entity.getDocsPreparation(), "", entity.getCustomClearance(),
                    "", entity.getCyCut(), "", entity.getPortIn(), "", entity.getEtd(), "", entity.getEta(), "",
                    entity.getImpCcLeadtime(), entity.getImpInboundLeadtime(), entity.getInactiveFlag() };

                cpmsrf01Service.createOneDataRowByTemplate(sheet, IntDef.INT_TWELVE + i, TemplateCells, arrayObj,
                    codeMaps, param.getLanguage());
            }
        }

        wbTemplate.setForceFormulaRecalculation(true);
        // remove sheet style
        wbOutput.removeSheetAt(wbOutput.getSheetIndex(STYLE));
    }

}
