/**
 * CPMCMF01Service
 * 
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.entity.TnmCalendarDetail;
import com.chinaplus.common.entity.TnmRegion;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.util.DateTimeUtil;

/**
 * CPMCMF01Service.
 */
@Service
public class CPMCMF01Service extends BaseService {

    /**
     * get regionCode by regionID
     * if found,return regionCode
     * if no found,return regionId
     * 
     * @param regionId String
     * @return String
     */
    public String getRegionCodeById(String regionId) {
        String regionCode = "";
        String hql = "FROM TNM_REGION WHERE REGION_ID = ? ";
        Object[] param2 = new Object[] { regionId };
        List<TnmRegion> reglist = baseDao.select(hql, param2);
        if (reglist != null && reglist.size() > 0) {
            for (TnmRegion tr : reglist) {
                regionCode = tr.getRegionCode();
                return regionCode;
            }
        }
        return regionId;
    }
    
    /**
     * get nonWorkingDay
     * 
     * @param calendarId Integer
     * @param year String
     * @return List<String>
     */
    public  List<String> getCalendar(Integer calendarId, String year) {
        List<String> datas = new ArrayList<String>();
        List<TnmCalendarDetail> list = new ArrayList<TnmCalendarDetail>();

        PageParam param = new PageParam();
        param.setSwapData("calendarId", calendarId);
        param.setSwapData("year", year);
        param.setSwapData("workingFlg", 0);
        list = baseMapper.select(getSqlId("getCalendar"), param);
        
        for (TnmCalendarDetail tnmCalendarDetail : list) {
            datas.add(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD, tnmCalendarDetail.getCalendarDate()));
        }
        
        return datas;
    }
    
    
    
}
