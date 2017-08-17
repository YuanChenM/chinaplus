/**
 * CPIIFB16Service.java
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.migration.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.base.BaseService;

/**
 * 
 * CPIIFB16Service.
 * 
 * @author yang_jia1
 */
public class ImpIpRepairForAisinService extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ImpIpRepairForAisinService.class);

    /**
     * Do Imp IP Repair for AISIN.
     * 
     * @param officeId Office ID
     * @throws Exception Exception
     * 
     */
    public void doImpIpRepair(Integer officeId) throws Exception {

        // start logger
        logger.info("Start of do Imp Ip Repair for AISIN.");

        // end logger
        logger.info("End of do Imp Ip Repair for AISIN.");
    }

}
