/**
 * ifTableSync from TT-logic
 * 
 * @screen CPIIFB16
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.command;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.batch.common.base.BaseBatch;
import com.chinaplus.batch.common.bean.BaseBatchParam;
import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchFileName;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchId;
import com.chinaplus.batch.common.util.CsvReader;
import com.chinaplus.batch.interfaces.bean.CPIIFB01Param;
import com.chinaplus.batch.interfaces.bean.ttlogic.InnerPackage;
import com.chinaplus.batch.interfaces.service.CPIIFB16Service;
import com.chinaplus.common.consts.CodeConst.ActionType;
import com.chinaplus.common.consts.CodeConst.OnHoldFlag;
import com.chinaplus.core.consts.CoreConst.Direction;
import com.chinaplus.core.consts.FileType;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.FileUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.core.util.ValidatorUtils;

/**
 * ifTableSync from TT-logic
 * 
 * @author yang_jia1
 */
@Component(IFBatchId.TTLOGIC_IFTABLESYNC)
public class CPIIFB16Batch extends BaseBatch {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB16Batch.class);

    /** data header */
    private static String[] header = { "indicator", "sequenceNo", "dataSource", "impOffice", "sourceIPNo",
        "ttlogicPidNo", "fromWhCode", "warehouseTransferDate", "warehouseCode", "supplierCode", "fromCustomerCode",
        "stockTransferDate", "customerCode", "ttcPartsNo", "qty", "customsClearanceDate", "customsClearanceInvoiceNo",
        "containerNo", "customsNo", "devannedJobNo", "devannedDate", "devannedInvoiceNo", "moduleNo", "inboundJobNo",
        "inboundInvoiceNo", "inboundModuleNo", "inboundType", "inboundDate", "outboundNo", "outboundType",
        "outboundPackageNo", "deliveryNoteNo", "outboundDateTime", "dispatchedDateTime", "onholdFlag", "onholdDate",
        "stockAdjustmentDate", "stockAdjustmentQty", "decantDate", "fromIpNo", "status" };

    /** The CPIIFB16Batch service */
    @Autowired
    private CPIIFB16Service c16Service;

    @Override
    public BaseBatchParam createBatchParam(String[] args) {

        // prepare parameter
        CPIIFB01Param param = null;

        // parameter check
        // Check batch arguments
        if (args == null || args.length != BatchConst.INT_ONE) {

            logger.error("The arguments number of batch is incorrect.");
            throw new BatchException();
        }

        // get file path
        File file = new File(args[BatchConst.INT_ZERO]);
        // check file path
        if (!file.exists() && !file.isDirectory()) {
            logger.error("The Data File Path is incorrect.");
            throw new BatchException();
        }

        // set parameters
        param = new CPIIFB01Param();
        param.setFilePath(args[BatchConst.INT_ZERO]);
        param.setProcessDate(c16Service.getDBDateTimeByDefaultTimezone());

        // return
        return param;
    }

    /**
     * doOperate
     *
     * @param param
     * @return
     * @throws Exception
     * @see com.chinaplus.batch.common.base.BaseBatch#doOperate(com.chinaplus.batch.common.bean.BaseBatchParam)
     */
    @Override
    public boolean doOperate(BaseBatchParam param) throws Exception {
        logger.info("doOperate", "batch CPIIFB16Batch start......");

        // if no parameter, return
        if (param == null) {
            logger.error("The batch parameters is incorrect, please at least have one subBatchId.");
            throw new RuntimeException();
        }

        // cast
        CPIIFB01Param castPara = (CPIIFB01Param) param;
        // get files
        List<File> effFiles = this.getEffectiveFiles(castPara);
        if (effFiles.isEmpty()) {
            logger.warn("TTLogix file does not exist.");
            return false;
        }

        // sort files
        Collections.sort(effFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                // sort by name
                return o1.getName().compareTo(o2.getName());
            }
        });

        // do file read and save
        List<InnerPackage> ipList = null;
        for (File file : effFiles) {
            // get content list
            ipList = CsvReader.readFile(file, InnerPackage.class, IFBatchFileName.TTLOGIC, header);

            // check file content size
            if (ipList.size() > 0) {

                // do check
                if (!doFieldActCheck(ipList)) {
                    return false;
                }

                // sort files
                Collections.sort(ipList, new Comparator<InnerPackage>() {
                    @Override
                    public int compare(InnerPackage o1, InnerPackage o2) {
                        // sort by name
                        if (o1.getTtlogicPidNo() == null && o2.getTtlogicPidNo() == null) {
                            return IntDef.INT_ZERO;
                        } else if (o1.getTtlogicPidNo() == null) {
                            return IntDef.INT_N_ONE;
                        } else if (o2.getTtlogicPidNo() == null) {
                            return IntDef.INT_ONE;
                        } else {
                            return o1.getTtlogicPidNo().compareTo(o2.getTtlogicPidNo());
                        }
                    }
                });

                // do save
                c16Service.doSyncFilesToIfTable(ipList);
            } else {
                // warning
                logger.warn(String.format("File %s does not has any items.", new Object[] { file.getName() }));
            }

            // delete
            FileUtil.deleteFile(file);
        }

        logger.info("doOperate", "batch CPIIFB16Batch end......");
        return true;
    }

    /**
     * Do field check.
     * 
     * @param ipInfoList ipInfoList
     * 
     * @return check result
     */
    private boolean doFieldActCheck(List<InnerPackage> ipInfoList) {

        boolean checkRes = true;
        int line = IntDef.INT_ONE;
        for (InnerPackage ipInfo : ipInfoList) {

            // check length
            // Sequence no.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getSequenceNo(), IntDef.INT_SEVEN)) {
                logger.error(String.format("Length of Sequence no. can over then 7.(data line: %d)", line));
                checkRes = false;
            }

            // Data Source
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDataSource(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Data Source can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Imp Office
            if (!ValidatorUtils.requiredValidator(ipInfo.getImpOffice())) {
                logger.error(String.format("Imp Office can not be empty.(data line: %d)", line));
                checkRes = false;
            } else if (!ValidatorUtils.maxLengthValidator(ipInfo.getImpOffice(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of Imp Office can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // Source IP No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getSourceIPNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Source IP No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // TT-Logix PID No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getTtlogicPidNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Sequence no. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // FROM WH Code
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getFromWhCode(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of FROM WH Code can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // Warehouse Transfer Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getWarehouseTransferDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Warehouse Transfer Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // WareHouse Code
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getWarehouseCode(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of WareHouse Code can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // Supplier Code
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getSupplierCode(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of Supplier Code can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // FROM Customer Code
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getFromCustomerCode(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of FROM Customer Code can over then 20.(data line: %d)", line));
                checkRes = false;
            }
            // Stock Transfer Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getStockTransferDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Stock Transfer Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Customer Code
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getCustomerCode(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of Customer Code can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // TTC Parts No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getTtcPartsNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of TTC Parts No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Qty
            // trim
            ipInfo.setQty(this.trimZeroForQty(ipInfo.getQty()));
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getQty(), IntDef.INT_SIXTEEN)) {
                logger.error(String.format("Length of Qty can over then 16.(data line: %d)", line));
                checkRes = false;
            } else if (!ValidatorUtils.decimalValidator(ipInfo.getQty(), false)) {
                logger.error(String.format("Qty must be number.(data line: %d)", line));
                checkRes = false;
            }

            // Customs Clearance Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getCustomsClearanceDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Customs Clearance Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Invoice No
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getCustomsClearanceInvoiceNo(),
                IntDef.INT_ONE_HUNDRED_AND_FIFTY)) {
                logger.error(String.format("Length of Invoice No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }
            
            // Customs No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getContainerNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Container No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Customs No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getCustomsNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Customs No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Devanned Job No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDevannedJobNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Devanned Job No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Devanned Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDevannedDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Devanned Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Invoice No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDevannedInvoiceNo(), IntDef.INT_ONE_HUNDRED_AND_FIFTY)) {
                logger.error(String.format("Length of Invoice No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Module No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getModuleNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Module No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Inbound Job No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getInboundJobNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Inbound Job No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Invoice No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getInboundInvoiceNo(), IntDef.INT_ONE_HUNDRED_AND_FIFTY)) {
                logger.error(String.format("Length of Invoice No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Inbound Module No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getInboundModuleNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Inbound Module No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Inbound Type
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getInboundType(), IntDef.INT_ONE)) {
                logger.error(String.format("Length of Inbound Type can over then 1.(data line: %d)", line));
                checkRes = false;
            }

            // Inbound Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getInboundDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Inbound Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Outbound No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOutboundNo(), IntDef.INT_TWENTY)) {
                logger.error(String.format("Length of Outbound No. can over then 20.(data line: %d)", line));
                checkRes = false;
            }

            // Outbound Type
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOutboundType(), IntDef.INT_ONE)) {
                logger.error(String.format("Length of Outbound Type can over then 1.(data line: %d)", line));
                checkRes = false;
            }

            // Outbound Package no.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOutboundPackageNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Outbound Package no. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Delivery Note No.
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDeliveryNoteNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of Delivery Note No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Outbound Date Time
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOutboundDateTime(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Outbound Date Time can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Dispatched Date Time
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDispatchedDateTime(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Dispatched Date Time can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Onhold Flag
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOnholdFlag(), IntDef.INT_ONE)) {
                logger.error(String.format("Length of Onhold Flag can over then 1.(data line: %d)", line));
                checkRes = false;
            }

            // Onhold Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getOnholdDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Onhold Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Stock Adjustment Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getStockAdjustmentDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Stock Adjustment Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // Stock Adjustment Qty
            // trim
            ipInfo.setStockAdjustmentQty(this.trimZeroForQty(ipInfo.getStockAdjustmentQty()));
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getStockAdjustmentQty(), IntDef.INT_SIXTEEN)) {
                logger.error(String.format("Length of Stock Adjustment Qty can over then 16.(data line: %d)", line));
                checkRes = false;
            } else if (!ValidatorUtils.decimalValidator(ipInfo.getStockAdjustmentQty(), false)) {
                logger.error(String.format("Stock Adjustment Qty must be number.(data line: %d)", line));
                checkRes = false;
            }

            // Decant Date
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getDecantDate(), IntDef.INT_FIFTEEN)) {
                logger.error(String.format("Length of Decant Date can over then 15.(data line: %d)", line));
                checkRes = false;
            }

            // From IP No
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getFromIpNo(), IntDef.INT_THIRTY)) {
                logger.error(String.format("Length of From IP No. can over then 30.(data line: %d)", line));
                checkRes = false;
            }

            // Status
            if (!ValidatorUtils.maxLengthValidator(ipInfo.getStatus(), IntDef.INT_TWO)) {
                logger.error(String.format("Length of Status can over then 2.(data line: %d)", line));
                checkRes = false;
            } else if (!ValidatorUtils.numberValidator(ipInfo.getStatus(), false)) {
                logger.error(String.format("Status must be number.(data line: %d)", line));
                checkRes = false;
            }

            // get action type
            this.prepareActionType(ipInfo);
            // check action type
            if (ipInfo.getActionType() == null) {
                // check action type
                logger.error(String.format("Has data with out any date times.(data line: %d)", line));
                checkRes = false;
            } else if (ipInfo.getProcessDate() == null) {
                // check process date
                logger.error(String.format("Has data with out any effective date times.(data line: %d)", line));
                checkRes = false;
            }

            // next line
            line++;
        }

        return checkRes;
    }

    /**
     * prepare inner packing information.
     * 
     * @param ipInfo ip information
     */
    private void prepareActionType(InnerPackage ipInfo) {

        // define
        Timestamp processDate = null;
        Integer actionType = null;

        // check
        if (!StringUtil.isNullOrEmpty(ipInfo.getCustomsClearanceDate())) {

            // set action type
            actionType = ActionType.CUSTOMS_CLEARANCE;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getCustomsClearanceDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getDevannedDate())) {

            // set action type
            actionType = ActionType.DEVANNED;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getDevannedDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getInboundDate())) {

            // set action type
            actionType = ActionType.IMP_INBOUND;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getInboundDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getWarehouseTransferDate())) {

            // set action type
            actionType = ActionType.WHS_TRANSFER;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getWarehouseTransferDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getStockTransferDate())) {

            // set action type
            actionType = ActionType.STOCK_TRANSFER;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getStockTransferDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getStockAdjustmentDate())) {

            // set action type
            actionType = ActionType.STOCK_ADJUST;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getStockAdjustmentDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getOnholdDate())) {

            // set action type
            if (ipInfo.getOnholdFlag() == null) {
                actionType = null;
            } else if (ipInfo.getOnholdFlag().equals(String.valueOf(OnHoldFlag.NG_ON_HOLD))) {
                actionType = ActionType.NG;
            } else if (ipInfo.getOnholdFlag().equals(String.valueOf(OnHoldFlag.ECI_ON_HOLD))) {
                actionType = ActionType.ECI_ONHOLD;
            } else if (ipInfo.getOnholdFlag().equals(String.valueOf(OnHoldFlag.NORMAL))) {
                actionType = ActionType.RELEASE_ONHOLD;
            }
            // set process date
            processDate = DateTimeUtil.parseDateTime(ipInfo.getOnholdDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getDecantDate())) {

            // set action type
            actionType = ActionType.DECANT;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getDecantDate(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (!StringUtil.isNullOrEmpty(ipInfo.getDispatchedDateTime())) {

            // set action type
            actionType = ActionType.IMP_OUTBOUND;
            processDate = DateTimeUtil.parseDateTime(ipInfo.getDispatchedDateTime(), DateTimeUtil.FORMAT_IP_DATE);

        } else if (StringUtil.isNullOrEmpty(ipInfo.getTtlogicPidNo())
                && !StringUtil.isNullOrEmpty(ipInfo.getInboundInvoiceNo())) {

            // set action type
            actionType = ActionType.CANCEL_INVOICE;
            processDate = ipInfo.getIfDateTime();
            ipInfo.setInboundDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_IP_DATE, processDate));

        }

        // set into actionType
        ipInfo.setActionType(actionType);
        // set into processDate
        ipInfo.setProcessDate(processDate);
    }

    /**
     * get effective files.
     * 
     * @param param parameter
     * @return boolean boolean
     */
    private List<File> getEffectiveFiles(CPIIFB01Param param) {

        // check file exist
        File filePath = new File(param.getFilePath());
        List<File> listFiles = FileUtil.getFilesFromPath(filePath, IFBatchFileName.TTLOGIC, FileType.CSV.getSuffix());
        List<File> activeFiles = new ArrayList<File>();
        // check start with
        for (int i = 0; i < listFiles.size(); i++) {
            // get file
            File file = listFiles.get(i);
            // check file name
            if (file.getName().startsWith(IFBatchFileName.TTLOGIC)) {
                activeFiles.add(file);
            }
        }

        return activeFiles;
    }

    /**
     * trim zero for Qty.
     * 
     * @param val val
     * @return trimed val
     */
    private String trimZeroForQty(String val) {

        // empty
        if (StringUtil.isEmpty(val)) {
            return val;
        }

        // check has point
        if (val.indexOf(StringConst.DOT) > IntDef.INT_ZERO) {
            return StringUtil.trim(StringUtil.trim(val, Direction.Right, '0'), Direction.Right, '.');
        } else {
            return val;
        }
    }
}
