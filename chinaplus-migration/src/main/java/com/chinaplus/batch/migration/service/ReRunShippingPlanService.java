/**
 * ReRunShippingPlanService.java
 * 
 * @screen ReRunShippingPlan
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.migration.bean.ReRunShippingPlanEntity;
import com.chinaplus.common.entity.SsmsCommonParam;
import com.chinaplus.common.service.InvoiceAdjService;
import com.chinaplus.common.service.SsPlanService;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.exception.BatchException;

/**
 * 
 * ReRunShippingPlanService.
 * 
 * @author cheng_xingfei
 */
@Service
public class ReRunShippingPlanService extends BaseService {

    @Autowired
    private SsPlanService ssPlanService;

    @Autowired
    private InvoiceAdjService invoiceAdjService;

    /**
     * getExpPartsIdList
     * 
     * @param officeCode String
     * @param businessPattern Integer
     * @return partsList
     */
    public List<ReRunShippingPlanEntity> getExpPartsIdList(String officeCode, Integer businessPattern) {
        ReRunShippingPlanEntity param = new ReRunShippingPlanEntity();
        param.setOfficeCode(officeCode);
        param.setBusinessPattern(businessPattern);

        List<ReRunShippingPlanEntity> partsList = this.baseMapper.select(this.getSqlId("getExpPartsIdList"), param);

        return partsList;
    }

    /**
     * doSSMSLogic
     * 
     * @param expPartsIdList List<Integer>
     * @param officeCode String
     * @param businessPattern Integer
     * @return Map<String, Integer>
     */
    public Map<String, Integer> doSSMSLogic(List<Integer> expPartsIdList, String officeCode, Integer businessPattern) {

        SsmsCommonParam param = new SsmsCommonParam();
        param.setExpPartsId(expPartsIdList);
        param.setOfficeCode(officeCode);
        return doSSMSLogic(param, businessPattern);

    }

    /**
     * doSSMSBusinessLogic
     * 
     * @param param param
     * @param businessPattern Integer
     * @return excuteResult
     * @throws BatchException e
     */
    public Map<String, Integer> doSSMSLogic(SsmsCommonParam param, Integer businessPattern) throws BatchException {

        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        ssPlanService.doOrderPlanCreateForReRun(param.getOfficeCode(), businessPattern);

        invoiceAdjService.doInvoiceAdjLogic(null, null);

        resultMap.put("executeResult", 1);
        return resultMap;
    }
}
