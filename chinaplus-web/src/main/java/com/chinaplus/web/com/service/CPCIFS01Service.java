/**
 * CPCIFS01Service.java
 * 
 * @screen CPCIFS01
 * @author zhang_chi
 */
package com.chinaplus.web.com.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.BatchJobStatus;
import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.web.com.entity.CPCIFS01Entity;

/**
 * Information Screen Service.
 */
@Service
public class CPCIFS01Service extends BaseService {

    /**
     * Get Sync Time Data.
     * 
     * @return Sync Time Data
     */
    public Map<String, CPCIFS01Entity> getSyncTimeData() {

        Map<String, CPCIFS01Entity> syncTimeMap = new HashMap<String, CPCIFS01Entity>();
        List<CPCIFS01Entity> syncTimeList = baseMapper.select(this.getSqlId("getSyncTimeData"), new BaseParam());
        for (CPCIFS01Entity syncTimeData : syncTimeList) {
            Integer officeId = syncTimeData.getOfficeId();
            Integer batchType = syncTimeData.getBatchType();
            String mapKey = batchType + StringConst.UNDERLINE + (officeId == null ? StringConst.EMPTY : officeId);
            syncTimeMap.put(mapKey, syncTimeData);
        }
        return syncTimeMap;
    }

    /**
     * Get SS and RD Batch Result.
     * 
     * @return SS and RD Batch Result
     */
    public List<Integer> getSsRdBatchResult() {

        List<Integer> failOfficeList = new ArrayList<Integer>();
        List<CPCIFS01Entity> results = baseMapper.select(this.getSqlId("getSsRdBatchResult"), new BaseParam());
        for (CPCIFS01Entity result : results) {
            Integer officeId = result.getOfficeId();
            Timestamp processDate = result.getProcessDate();
            if (BatchJobStatus.FAIL == result.getBatchJobStatus() && !failOfficeList.contains(officeId)) {
                Timestamp officeTime = super.getDBDateTime(result.getTimeZone());
                Date officeToday = DateTimeUtil.parseDate(DateTimeUtil.formatDate(officeTime),
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD);
                Date processDay = DateTimeUtil.parseDate(DateTimeUtil.formatDate(processDate),
                    DateTimeUtil.FORMAT_DATE_YYYYMMDD);
                if (processDay.compareTo(officeToday) >= 0) {
                    failOfficeList.add(officeId);
                }
            }
        }

        return failOfficeList;
    }

    /**
     * Get office map.
     * 
     * @return office map
     */
    public Map<Integer, String> getOfficeMap() {

        Map<Integer, String> officeMap = new HashMap<Integer, String>();
        TnmOffice officeCondition = new TnmOffice();
        officeCondition.setInactiveFlag(InactiveFlag.ACTIVE);
        List<TnmOffice> offices = super.baseDao.select(officeCondition);
        for (TnmOffice office : offices) {
            officeMap.put(office.getOfficeId(), office.getOfficeCode());
        }

        return officeMap;
    }

}
