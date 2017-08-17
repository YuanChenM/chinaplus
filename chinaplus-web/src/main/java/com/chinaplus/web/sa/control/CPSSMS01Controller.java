/**
 * CPSSMS01Controller.java
 * 
 * @screen CPSSMS01
 * @author li_feng
 */
package com.chinaplus.web.sa.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseFileController;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.sa.entity.CPSSMS01Entity;
import com.chinaplus.web.sa.service.CPSSMS01Service;

/**
 * Download Shipping Status Revison History Controller.
 */
@Controller
public class CPSSMS01Controller extends BaseFileController {

    /** Condition:ipoOrderNo */
    private static final String CONDITION_IPOORDERNO = "ipoOrderNo";

    /** Condition:customerOrderNo */
    private static final String CONDITION_CUSTOMERORDERNO = "customerOrderNo";
    
    /** Condition:uploadDateTimeFrom */
    private static final String CONDITION_UPLOADDATETIMEFROM = "uploadDateTimeFrom";
    
    /** Condition:uploadDateTimeTo */
    private static final String CONDITION_UPLOADDATETIMETO = "uploadDateTimeTo";
    
    /** Download Shipping Status Revison History Service */
    @Autowired
    private CPSSMS01Service service;
    
    @Override
    protected String getFileId() {
        // TODO 
        return null;
    }

    /**
     * get customer forecast for screen CPSSMS01 by filter.
     * 
     * @param param PageParam
     * @param request HttpServletRequest
     * @return PageResult
     * @throws Exception e
     */
    @RequestMapping(value = "/sa/CPSSMS01/getShippingStatusRevisonHistoryList",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPSSMS01Entity> getShippingStatusRevisonHistoryList(@RequestBody PageParam param,
        HttpServletRequest request) throws Exception {

        super.setCommonParam(param, request);
        param.setSwapData("language", param.getLanguage());
        // find data by paging
        
        StringUtil.buildLikeCondition(param, CONDITION_IPOORDERNO);
        StringUtil.buildLikeCondition(param, CONDITION_CUSTOMERORDERNO);
        StringUtil.buildDateTimeCondition(param, CONDITION_UPLOADDATETIMEFROM);
        StringUtil.buildDateTimeCondition(param, CONDITION_UPLOADDATETIMETO);
        
        @SuppressWarnings("unchecked")
        ArrayList<String> a = (ArrayList<String>) param.getFilters().get("transportMode");
        if(a != null && a.size() == 1){
            param.setSwapData("transportMode", a.get(0));
        }
        PageResult<CPSSMS01Entity> result = service.getPageList(param);
        List<CPSSMS01Entity> datas = result.getDatas();
        List<CPSSMS01Entity> newDataList = new ArrayList<CPSSMS01Entity>();
        
        // prepare pageDatas
        if(datas != null){
            //Map<Integer, Integer> rowNumMap = new LinkedHashMap<Integer, Integer>();
            Integer lastRowNum = null;
            StringBuffer sbContract = new StringBuffer();
            List<String> regionCodeLst = new ArrayList<String>();
            List<String> supplierCodeLst = new ArrayList<String>();
            for (CPSSMS01Entity cpssms01Entity : datas) {
                //CPSSMS01Entity newData = new CPSSMS01Entity();
                
                /*if(newDataList == null){
                    newData.setCustomerOrderNo(cpssms01Entity.getCustomerOrderNo());
                    newData.setIpoOrderNo(cpssms01Entity.getIpoOrderNo());
                    newData.setExpCountry(cpssms01Entity.getExpCountry());
                    newData.setTtcSupplierCode(cpssms01Entity.getTtcSupplierCode());
                    newData.setTtcCustomerCode(cpssms01Entity.getTtcCustomerCode());
                    newData.setTransportMode(cpssms01Entity.getTransportMode());
                    newData.setRevisionReason(cpssms01Entity.getRevisionReason());
                    newData.setUploadID(cpssms01Entity.getUploadID());
                    newData.setUploadDateTime(cpssms01Entity.getUploadDateTime());
                    newData.setSsId(cpssms01Entity.getSsId());
                    
                    rowNumMap.put(cpssms01Entity.getRownumb(), cpssms01Entity.getRownumb());
                    newDataList.add(newData);
                } else if(newDataList != null ){
                    if(null == rowNumMap.get(cpssms01Entity.getRownumb())){
                        newData.setCustomerOrderNo(cpssms01Entity.getCustomerOrderNo());
                        newData.setIpoOrderNo(cpssms01Entity.getIpoOrderNo());
                        newData.setExpCountry(cpssms01Entity.getExpCountry());
                        newData.setTtcSupplierCode(cpssms01Entity.getTtcSupplierCode());
                        newData.setTtcCustomerCode(cpssms01Entity.getTtcCustomerCode());
                        newData.setTransportMode(cpssms01Entity.getTransportMode());
                        newData.setRevisionReason(cpssms01Entity.getRevisionReason());
                        newData.setUploadID(cpssms01Entity.getUploadID());
                        newData.setUploadDateTime(cpssms01Entity.getUploadDateTime());
                        newData.setSsId(cpssms01Entity.getSsId());
                        
                        rowNumMap.put(cpssms01Entity.getRownumb(), cpssms01Entity.getRownumb());
                        newDataList.add(newData);
                    
                    }
                }
                */
                if (lastRowNum == null || lastRowNum.compareTo(cpssms01Entity.getRownumb()) != IntDef.INT_ZERO) {
                    
                    CPSSMS01Entity newData = new CPSSMS01Entity();
                    newData.setCustomerOrderNo(cpssms01Entity.getCustomerOrderNo());
                    newData.setIpoOrderNo(cpssms01Entity.getIpoOrderNo());
                    newData.setExpCountry(cpssms01Entity.getExpCountry());
                    newData.setTtcSupplierCode(cpssms01Entity.getTtcSupplierCode());
                    newData.setTtcCustomerCode(cpssms01Entity.getTtcCustomerCode());
                    newData.setTransportMode(CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.TRANSPORT_MODE, StringUtil.toInteger(cpssms01Entity.getTransportMode())));
                    newData.setRevisionReason(cpssms01Entity.getRevisionReason());
                    newData.setUploadID(cpssms01Entity.getUploadID());
                    newData.setUploadDateTime(cpssms01Entity.getUploadDateTime());
                    newData.setSsId(cpssms01Entity.getSsId());
                    
                    newDataList.add(newData);
                    
                    // reset 
                    regionCodeLst.clear();
                    regionCodeLst.add(cpssms01Entity.getExpCountry());
                    supplierCodeLst.clear();
                    supplierCodeLst.add(cpssms01Entity.getTtcSupplierCode());
                
                } else {
                    // get exists one
                    CPSSMS01Entity extData = newDataList.get(newDataList.size() - IntDef.INT_ONE);
                    
                    // customer 
                    if (!regionCodeLst.contains(cpssms01Entity.getExpCountry())) {
                        sbContract.setLength(IntDef.INT_ZERO);
                        sbContract.append(extData.getExpCountry());
                        sbContract.append(StringConst.COMMA);
                        sbContract.append(cpssms01Entity.getExpCountry());
                        extData.setExpCountry(sbContract.toString());
                        regionCodeLst.add(cpssms01Entity.getExpCountry());
                    }
                    
                    // supplier
                    if (!supplierCodeLst.contains(cpssms01Entity.getTtcSupplierCode())) {
                        sbContract.setLength(IntDef.INT_ZERO);
                        sbContract.append(extData.getTtcSupplierCode());
                        sbContract.append(StringConst.COMMA);
                        sbContract.append(cpssms01Entity.getTtcSupplierCode());
                        extData.setTtcSupplierCode(sbContract.toString());
                        supplierCodeLst.add(cpssms01Entity.getTtcSupplierCode());
                    }
                    
                    // transport
                    String transport = CodeCategoryManager.getCodeName(param.getLanguage(),
                        CodeMasterCategory.TRANSPORT_MODE, StringUtil.toInteger(cpssms01Entity.getTransportMode()));
                    // check
                    if (transport != null && extData.getTransportMode().indexOf(transport) < IntDef.INT_ZERO) {
                        sbContract.setLength(IntDef.INT_ZERO);
                        sbContract.append(extData.getTransportMode());
                        sbContract.append(StringConst.COMMA);
                        sbContract.append(transport);
                        extData.setTransportMode(sbContract.toString());
                    }
                }
                
                // reset
                lastRowNum = cpssms01Entity.getRownumb();
            }
        
        }
        
        
        
        /*for (CPSSMS01Entity t : newDataList) {
            if(t.getExpCountry() != null){
                String expCountryTemp = t.getExpCountry().replaceAll("<TNM_REGION>", "").replaceAll("</TNM_REGION>", "");
                t.setExpCountry(expCountryTemp.substring(0, expCountryTemp.length()-1));
            }
            if(t.getTtcSupplierCode() != null){
                String ttcSupplierCodeTemp = t.getTtcSupplierCode().replaceAll("<TNM_SUPPLIER>", "").replaceAll("</TNM_SUPPLIER>", "");
                t.setTtcSupplierCode(ttcSupplierCodeTemp.substring(0, ttcSupplierCodeTemp.length()-1));
            }
            if(t.getTransportMode() != null){
                String transportModeTemp = t.getTransportMode().replaceAll("<TNM_CODE_CATEGORY>", "").replaceAll("</TNM_CODE_CATEGORY>", "");
                t.setTransportMode(transportModeTemp.substring(0, transportModeTemp.length()-1));
            }
            
        }*/
        
        //result.setTotalCount(newDataList == null ? 0 : newDataList.size());
        result.setDatas(newDataList);
        

        return result;
    }
    

    
}