/**
 * CPIIFB15Service.java
 * 
 * @screen CPIIFB15
 * @author yang_jia1
 */
package com.chinaplus.batch.interfaces.service;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chinaplus.batch.common.consts.BatchConst;
import com.chinaplus.batch.common.consts.BatchConst.IFBatchFileName;
import com.chinaplus.batch.common.consts.BatchConst.IFServiceId;
import com.chinaplus.batch.common.util.InterfaceReader;
import com.chinaplus.batch.interfaces.bean.Customer;
import com.chinaplus.batch.interfaces.bean.Parts;
import com.chinaplus.common.entity.BaseInterfaceEntity;
import com.chinaplus.common.entity.IFInboundEntity;
import com.chinaplus.common.entity.IFInvoiceEntity;
import com.chinaplus.common.entity.IFOrderCancelEntity;
import com.chinaplus.common.entity.IFOrderEntity;
import com.chinaplus.common.entity.IFOutboundEntity;
import com.chinaplus.common.util.ConfigManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BatchException;
import com.chinaplus.core.util.StringUtil;

/**
 * 
 * CPIIFB15Service.
 * ssms table sync service
 * 
 * @author yang_jia1
 */
@Service
@Component(IFServiceId.SMSS_SYNC)
public class CPIIFB15Service extends BaseService {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(CPIIFB01Service.class);

    /**
     * doSyncFilesToIfTable
     * 
     * @param lstFiles List<File>
     * @return Map<String, Object>
     * @throws Exception e
     */
    public Map<String, Object> doSyncFilesToIfTable(List<File> lstFiles) throws Exception {

        logger.debug("---------------batch doSyncFilesToIfTable start-------------");

        List<File> delFiles = new ArrayList<File>();
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<Customer> cmList = new ArrayList<Customer>();
        List<Parts> ptList = new ArrayList<Parts>();
        List<IFOrderEntity> odList = new ArrayList<IFOrderEntity>();
        List<IFOrderCancelEntity> ocList = new ArrayList<IFOrderCancelEntity>();
        List<IFInboundEntity> ibList = new ArrayList<IFInboundEntity>();
        List<IFOutboundEntity> obList = new ArrayList<IFOutboundEntity>();
        List<IFInvoiceEntity> ivList = new ArrayList<IFInvoiceEntity>();

        // do file read and save
        for (File file : lstFiles) {
            int result = 0;

            if (file.getName().startsWith(IFBatchFileName.SSMS_CUSTOMER)) {
                try {
                    cmList = InterfaceReader.readFileForCustomer(file, Customer.class, IFBatchFileName.SSMS_CUSTOMER);
                } catch (BatchException e) {
                    logger.error("Customer file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNM_IF_CUSTOMER.");
                if (cmList != null && cmList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfCustomerId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfCustomerId"));
                    if (currentIfCustomerId == null) {
                        currentIfCustomerId = 1;
                    }

                    for (Customer item : cmList) {
                        item.setIfCustomerId(currentIfCustomerId);
                        result = insertIfTables(item, nowTime, "addSSMSCustomerIf");
                        currentIfCustomerId++;
                    }

                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_PARTS)) {
                try {
                    ptList = InterfaceReader.readFile(file, Parts.class, IFBatchFileName.SSMS_PARTS);
                } catch (BatchException e) {
                    logger.error("Parts file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNM_IF_PARTS.");
                if (ptList != null && ptList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfPartId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfPartId"));
                    if (currentIfPartId == null) {
                        currentIfPartId = 1;
                    }

                    for (Parts item : ptList) {
                        item.setIfPartsId(currentIfPartId);
                        result = insertIfTables(item, nowTime, "addSSMSPartsIf");
                        currentIfPartId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_ORDER)) {
                try {
                    odList = InterfaceReader.readFile(file, IFOrderEntity.class, IFBatchFileName.SSMS_ORDER);
                } catch (BatchException e) {
                    logger.error("Order file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNT_IF_EXP_ORDER.");
                if (odList != null && odList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfOrderId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfOrderId"));
                    if (currentIfOrderId == null) {
                        currentIfOrderId = 1;
                    }
                    
                    for (IFOrderEntity item : odList) {
                        item.setIfOrderId(currentIfOrderId);
                        result = insertIfTables(item, nowTime, "addSSMSOrderIf");
                        currentIfOrderId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_ORDERCANCEL)) {
                try {
                    ocList = InterfaceReader.readFile(file, IFOrderCancelEntity.class, IFBatchFileName.SSMS_ORDERCANCEL);
                } catch (BatchException e) {
                    logger.error("OrderCancel file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNT_IF_CANCEL_ORDER.");
                if (ocList != null && ocList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfOrderCancelId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfOrderCancelId"));
                    if (currentIfOrderCancelId == null) {
                        currentIfOrderCancelId = 1;
                    }
                    
                    for (IFOrderCancelEntity item : ocList) {
                        item.setIfCancelId(currentIfOrderCancelId);
                        result = insertIfTables(item, nowTime, "addOrderCancelIf");
                        currentIfOrderCancelId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_EXP_INBOUND)) {
                try {
                    ibList = InterfaceReader.readFile(file, IFInboundEntity.class, IFBatchFileName.SSMS_EXP_INBOUND);
                } catch (BatchException e) {
                    logger.error("Inbound file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNT_IF_EXP_INBOUND.");
                if (ibList != null && ibList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfInboundId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfInboundId"));
                    if (currentIfInboundId == null) {
                        currentIfInboundId = 1;
                    }
                    
                    for (IFInboundEntity item : ibList) {
                        item.setIfInboundId(currentIfInboundId);
                        result = insertIfTables(item, nowTime, "addInboundIf");
                        currentIfInboundId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_EXP_OUTBOUND)) {
                try {
                    obList = InterfaceReader.readFile(file, IFOutboundEntity.class, IFBatchFileName.SSMS_EXP_OUTBOUND);
                } catch (BatchException e) {
                    logger.error("Outbound file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNT_IF_EXP_OUTBOUND.");
                if (obList != null && obList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfOutboundId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfOutBoundId"));
                    if (currentIfOutboundId == null) {
                        currentIfOutboundId = 1;
                    }
                    
                    for (IFOutboundEntity item : obList) {
                        if (StringUtil.isEmpty(item.getCancelFlag())) {
                            if (item.getIpNo() == null || item.getIpNo().length() < IntDef.INT_EIGHT) {
                                if (item.getIpNo() != null && item.getIpNo().length() > IntDef.INT_SIX) {
                                    item.setDtNo(item.getIpNo().substring(IntDef.INT_ZERO, IntDef.INT_SIX));
                                } else {
                                    item.setDtNo(item.getIpNo());
                                }
                            } else {
                                item.setDtNo(item.getIpNo().substring(IntDef.INT_TWO, IntDef.INT_EIGHT));
                            }
                        }
                        item.setIfOutboundId(currentIfOutboundId);
                        result = insertIfTables(item, nowTime, "addOutboundIf");
                        currentIfOutboundId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (file.getName().startsWith(IFBatchFileName.SSMS_INVOICE)) {
                try {
                    ivList = InterfaceReader.readFile(file, IFInvoiceEntity.class, IFBatchFileName.SSMS_INVOICE);
                } catch (BatchException e) {
                    logger.error("Invoice file format is not correct .");
                    throw e;
                }

                logger.debug("insert into TNT_IF_EXP_INVOICE.");
                if (ivList != null && ivList.size() > 0) {
                    Timestamp nowTime = super.getDBDateTimeByDefaultTimezone();
                    
                    Integer currentIfInvoiceId = this.baseMapper.getSqlSession().selectOne(this.getSqlId("getMaxIfInvoiceId"));
                    if (currentIfInvoiceId == null) {
                        currentIfInvoiceId = 1;
                    }
                    
                    for (IFInvoiceEntity item : ivList) {
                        item.setIfInvoiceId(currentIfInvoiceId);
                        result = insertIfTables(item, nowTime, "addInvoiceIf");
                        currentIfInvoiceId++;
                    }
                } else {
                    result = BatchConst.INT_ONE;
                }
            }

            if (result != BatchConst.INT_ZERO) {
                // if ok, then remove file
                delFiles.add(file);
            }

        }

        logger.debug("---------------batch doSyncFilesToIfTable end-------------");
        resultMap.put("result", "success");
        resultMap.put("delFiles", delFiles);
        return resultMap;

    }
    
    /**
     * insert into IF tables
     * @param baseEntity BaseInterfaceEntity
     * @param nowTime Timestamp
     * @param sqlId String
     * @return insert count
     */
    private int insertIfTables(BaseInterfaceEntity baseEntity, Timestamp nowTime, String sqlId) {
        int result = 0;
    
        baseEntity.setCreatedBy(BatchConst.BATCH_USER_ID);
        baseEntity.setCreatedDate(nowTime);
        baseEntity.setUpdatedBy(BatchConst.BATCH_USER_ID);
        baseEntity.setUpdatedDate(nowTime);
        Timestamp fileIfDateTime = baseEntity.getFileCreateDate();
        baseEntity.setIfDateTime(generateIfDateTime(fileIfDateTime));
        baseEntity.setVersion(NumberConst.IntDef.INT_ONE);
        baseEntity.setValidFlag(BatchConst.INT_ZERO);
        baseEntity.setHandleFlag(BatchConst.INT_ZERO);
    
        result = this.baseMapper.insert(this.getSqlId(sqlId), baseEntity);
        
        return result;
    }

    /**
     * generateIfDateTime
     * 
     * @param fileIfDateTime fileIfDateTime
     * @return Timestamp
     */
    @SuppressWarnings("deprecation")
    private Timestamp generateIfDateTime(Timestamp fileIfDateTime) {
        // 当前时间
        Calendar curCal = Calendar.getInstance();
//        Timestamp nowTimeStamp = this.getDBDateTimeByDefaultTimezone();

        // 临界时间 当天0点
        curCal.setTime(fileIfDateTime);
        curCal.set(Calendar.HOUR_OF_DAY, 0);
        curCal.set(Calendar.MINUTE, 0);
        curCal.set(Calendar.SECOND, 0);
        Date delimitationDate1 = curCal.getTime();
        Timestamp delimition1 = new Timestamp(delimitationDate1.getTime());

        // 临界时间 当天11点
        String criticalTime = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.CRITICALTIME);
        int criticalTimeInt = 0;
        if (StringUtil.isEmpty(criticalTime)) {
            criticalTimeInt = IntDef.INT_ELEVEN;
        } else {
            try {
                criticalTimeInt = Integer.valueOf(criticalTime).intValue();
            } catch (Exception e) {
                criticalTimeInt = IntDef.INT_ELEVEN;
            }
        }
        curCal.set(Calendar.HOUR_OF_DAY, criticalTimeInt);
        curCal.set(Calendar.MINUTE, 0);
        curCal.set(Calendar.SECOND, 0);
        Date delimitationDate2 = curCal.getTime();
        Timestamp delimition2 = new Timestamp(delimitationDate2.getTime());

        // 临界时间 当天24点
        curCal.set(Calendar.HOUR_OF_DAY, IntDef.INT_TWENTY_THREE);
        curCal.set(Calendar.MINUTE, IntDef.INT_FIFTYNINE);
        curCal.set(Calendar.SECOND, IntDef.INT_FIFTYNINE);
        Date delimitationDate3 = curCal.getTime();
        Timestamp delimition3 = new Timestamp(delimitationDate3.getTime());

        String morningDivide = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.MORNING);
        String afternoonDivide = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.AFTERNOON);

        // 上午
        if (!fileIfDateTime.before(delimition1) && fileIfDateTime.before(delimition2)) {
            fileIfDateTime.setHours(Integer.parseInt(morningDivide));
            fileIfDateTime.setMinutes(BatchConst.INT_ZERO);
            fileIfDateTime.setSeconds(BatchConst.INT_ZERO);
            return fileIfDateTime;
        }

        // 上午
        if (!fileIfDateTime.before(delimition2) && !fileIfDateTime.after(delimition3)) {
            fileIfDateTime.setHours(Integer.parseInt(afternoonDivide));
            fileIfDateTime.setMinutes(BatchConst.INT_ZERO);
            fileIfDateTime.setSeconds(BatchConst.INT_ZERO);
            return fileIfDateTime;
        }

        return null;
    }

    /**
     * getDateTimeKey
     * 
     * @param fileName String
     * @return String
     */
    public String getDateTimeKey(String fileName) {
        // 当前时间
        String dateTime = fileName.substring(fileName.lastIndexOf(StringConst.DOT) + 1);
        if (dateTime.length() < IntDef.INT_TEN) {
            return dateTime;
        }

        // 临界时间 当天11点
        String criticalTime = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.CRITICALTIME);
        if (criticalTime.length() == 1) {
            criticalTime = "0" + criticalTime;
        }
        int criticalTimeInt = 0;
        if (StringUtil.isEmpty(criticalTime)) {
            criticalTimeInt = IntDef.INT_ELEVEN;
        } else {
            try {
                criticalTimeInt = Integer.valueOf(criticalTime).intValue();
            } catch (Exception e) {
                criticalTimeInt = IntDef.INT_ELEVEN;
            }
        }
        
        int hour = 0;
        try {
            hour = Integer.valueOf(dateTime.substring(IntDef.INT_EIGHT, IntDef.INT_TEN));
        } catch (Exception e) {
            hour = criticalTimeInt;
        }
        
        String morningDivide = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.MORNING);
        if (morningDivide.length() == 1) {
            morningDivide = "0" + morningDivide;
        }
        String afternoonDivide = ConfigManager.getProperty(BatchConst.BATCH_RELATION_PREFIX + BatchConst.AFTERNOON);
        if (afternoonDivide.length() == 1) {
            afternoonDivide = "0" + afternoonDivide;
        }

        // 上午
        if (hour >= IntDef.INT_ZERO && hour < criticalTimeInt) {
            return new StringBuffer().append(dateTime.substring(IntDef.INT_ZERO, IntDef.INT_EIGHT)).append(morningDivide).toString();
        }

        // 上午
        if (hour >= criticalTimeInt && hour <= IntDef.INT_TWENTY_THREE) {
            return new StringBuffer().append(dateTime.substring(IntDef.INT_ZERO, IntDef.INT_EIGHT)).append(afternoonDivide).toString();
        }
        
        return dateTime;
    }
}
