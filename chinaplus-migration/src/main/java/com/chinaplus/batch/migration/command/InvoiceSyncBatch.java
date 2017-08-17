/**
 * @screen Run-Down Batch main process
 * @author liu_yinchuan
 */
package com.chinaplus.batch.migration.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.migration.bean.MigrationComParam;
import com.chinaplus.batch.migration.bean.TntMgInvoiceEx;
import com.chinaplus.batch.migration.service.InvoiceSyncService;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.exception.BusinessException;

/**
 * Main Batch process for run-down batch.
 * 
 * @author liu_yinchuan
 */
@Component("InvoiceSyn")
public class InvoiceSyncBatch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(InvoiceSyncBatch.class);

    /** The InvoiceSyncService service */
    @Autowired
    private InvoiceSyncService invoiceSyncService;

    /**
     * Prepare run-down parameter.
     * 
     * @param args batch parameter
     */
    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // prepare parameter
        MigrationComParam param = null;

        // parameter check
        // Check batch arguments
        if (args == null || args.length != BatchConst.INT_ONE) {
            logger.error("The arguments number of batch is incorrect.");
            throw new BatchException();
        }

        // set parameters
        param = new MigrationComParam();
        param.setOfficeCode(args[BatchConst.INT_ZERO]);

        // get date
        // check
        TnmOffice office = baseService.getOfficeInfo(param.getOfficeCode());
        if (office == null) {
            logger.error("Office code does not an effective office.");
            throw new BusinessException();
        }
        // set office id
        param.setOfficeId(office.getOfficeId());
        // set porcess date
        param.setProcessDate(baseService.getDBDateTime(office.getTimeZone()));

        // return
        return param;
    }

    /**
     * Main process logic for rundown batch.
     * 
     * @param BaseBatchParam.
     * @return the result of current operation
     */
    @Override
    public boolean doOperate(BaseBatchParam baseParam) throws Exception {

        // batch process logic start
        logger.info("doOperate", "batch InvoiceSyncBatch start......");

        // get office Id
        Integer officeId = baseParam.getOfficeId();

        // Do Check
        this.doInvoiceInfoCheck(officeId);

        // do update
        invoiceSyncService.doPrepareInvoiceInformation(officeId);

        // batch process logic end
        logger.info("doOperate", "batch InvoiceSyncBatch end......");

        // return OK.
        return true;
    }

    /**
     * do check invoice information is OK or not.
     * 
     * @param officeId officeId
     */
    private void doInvoiceInfoCheck(Integer officeId) {

        boolean result = true;

        // get invoice information
        List<TntMgInvoiceEx> invPartsInfoList = invoiceSyncService.getInvociePartsInfoListForCheck(officeId);

        // do basic check
        result = this.basicCheckForInvoiceParts(invPartsInfoList);

        if (!result) {
            throw new BatchException();
        }
    }

    /**
     * do basic check for invoice parts list.
     * 
     * @param invPartsInfoList invPartsInfoList
     * @return check result
     */
    private boolean basicCheckForInvoiceParts(List<TntMgInvoiceEx> invPartsInfoList) {

        // define
        boolean checkResult = true;

        // loop check
        for (TntMgInvoiceEx invPartsInfo : invPartsInfoList) {

            // check parts exist or not
            if (invPartsInfo.getPartsId() == null) {
                checkResult = false;
                logger.error("Parts can not found in Parts master. MG_INVOICE_ID: " + invPartsInfo.getMgInvoiceId());
                continue;
            }

            // check issue date is upload or not
            if (invPartsInfo.getOrderMonth() == null) {
                checkResult = false;
                logger.error("Can not find Issue Date range from Kanban Issue Master. MG_INVOICE_ID: "
                        + invPartsInfo.getMgInvoiceId());
                continue;
            }

            // check kanban upload or not
            if (invPartsInfo.getKanbanPlanNo() == null) {
                checkResult = false;
                logger
                    .error("Can not find Kanban Plan No from Kanban. MG_INVOICE_ID: " + invPartsInfo.getMgInvoiceId());
                continue;
            }
        }

        return checkResult;
    }

}
