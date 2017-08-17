/**
 * FixSameModuleNoService.java
 * 
 * @screen FixSameModuleNoService
 * @author cheng_xingfei
 */
package com.chinaplus.batch.migration.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.bean.SameModuleRepairForEntity;
import com.chinaplus.core.base.BaseService;

/**
 * 
 * FixSameModuleNoService.
 * 
 * @author cheng_xingfei
 */
public class CpPatchService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CpPatchService.class);
    
    @Autowired
    private SameModuleRepairForService repService;

    @Autowired
    private ImpIpRepairForVvService repVvService;

    /**
     * Get max RundowMaster id.
     * 
     * @param param MigrationComParam
     * @throws InvocationTargetException IllegalAccessException
     * @throws IllegalAccessException IllegalAccessException
     * 
     */
    public void doPatchProcess(MigrationComParam param) throws IllegalAccessException, InvocationTargetException {

        logger.info("Start of patch");
        
        // do invoice
        List<SameModuleRepairForEntity> invoiceEntList = repService.doFixSameModuleNoWithInvList(param);
        
        if (invoiceEntList == null || invoiceEntList.isEmpty()) {
            return;
        }
        
        // Repair Invoice information
        List<String> invNoList = new ArrayList<String>();
        for(SameModuleRepairForEntity invoiceEnt : invoiceEntList) {
            invNoList.add(invoiceEnt.getInvoiceNo());
        }
        // include TNT_INVOCE_SUMMAY/TNT_INVOCE_CONTAINER/TNT_INVOCE/TNT_INVOCE_PARTS/INVOICE_HISTORY
        repVvService.doRepairInvoiceInfoByInvoiceList(param.getOfficeId(), invNoList);

        // Repair TNF_ORDER_STATUS AND TNF_IMP_STOCK
        repVvService.doRepairInventoryInfo(param.getOfficeId());
        
        logger.info("End of patch");
      
    }
   
}
