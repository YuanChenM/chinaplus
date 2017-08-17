/**
 * @screen CPMSMF01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmUser;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.web.mm.entity.CPMSMF11Entity;
import com.chinaplus.web.mm.entity.TnmIfPart;

/**
 * CPMSMF01Service.
 */
@Service
public class CPMSMF11Service extends BaseService {

    private static final String ZG = "ZG";

    /**
     * cpmpms02Service.
     */
    @Autowired
    private CPMPMS02Service cpmpms02Service;

    /**
     * query ssmsCustomerCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getSsmsCustomerCode(List<CPMSMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        BaseParam param = new BaseParam();
        param.setSwapData("initDataList", initDataList);
        List<CPMSMF11Entity> dataList = baseMapper.select(this.getSqlId("getSsmsCustomerCode"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMSMF11Entity ce : dataList) {
                maps.put(ce.getSsmsCustomerCode(), ce.getVendorRoute());
            }
        }
        return maps;
    }

    /**
     * query ssmsCustomerCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, Integer> getCustomerOfficeMap(List<CPMSMF11Entity> initDataList) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        BaseParam param = new BaseParam();
        param.setSwapData("initDataList", initDataList);
        List<CPMSMF11Entity> dataList = baseMapper.select(this.getSqlId("getCustomerOfficeMap"), param);
        if (dataList != null && dataList.size() > 0) {
            for (CPMSMF11Entity ce : dataList) {
                maps.put(ce.getSsmsCustomerCode(), ce.getOfficeId());
            }
        }
        return maps;
    }

    /**
     * Get all office code.
     * 
     * @return all office code
     */
    public Map<String, Integer> getAllOffice() {

        Map<String, Integer> officeMap = new HashMap<String, Integer>();
        TnmOffice officeCondition = new TnmOffice();
        officeCondition.setInactiveFlag(InactiveFlag.ACTIVE);
        List<TnmOffice> offices = super.baseDao.select(officeCondition);
        for (int i = 0; i < offices.size(); i++) {
            officeMap.put(offices.get(i).getOfficeCode(), offices.get(i).getOfficeId());
        }

        return officeMap;
    }

    /**
     * get TNM_EXP_PARTS data
     * 
     * @param initDataList initDataList
     * @param custCodeVRouteMaps custCodeVRouteMaps
     * @param customerOfficeMap customer office map
     * @param userId userId
     */
    public void doExpPartsData(List<CPMSMF11Entity> initDataList, Map<String, String> custCodeVRouteMaps,
        Map<String, Integer> customerOfficeMap, Integer userId) {

        // get db time
        Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();

        // update TNM_SSMS_CUSTOMER
        for (CPMSMF11Entity cpmsmf11entity : initDataList) {
            // set date
            cpmsmf11entity.setUpdatedDate(dbTime);
            // update
            this.baseMapper.update(this.getSqlId("updateSsmsCustomer"), cpmsmf11entity);
        }

        BaseParam param = new BaseParam();
        param.setSwapData("initDataList", initDataList);
        List<TnmIfPart> ifPartDataList = baseMapper.select(this.getSqlId("getIfParts"), param);
        if (ifPartDataList != null && ifPartDataList.size() > 0) {
            // param.setSwapData("ifPartDataList", ifPartDataList);
            // List<TnmIfPart> expPartDataList = baseMapper.select(this.getSqlId("getExpParts"), param);
            // Map<String, String> mtcsMaps = new HashMap<String, String>();
            // if (expPartDataList != null && expPartDataList.size() > 0) {
            // for (TnmIfPart ip : expPartDataList) {
            // String customerCode = ip.getCustomerCode();
            // String key = ip.getMainRoute() + StringConst.UNDERLINE + ip.getTtcPartsNo() + StringConst.UNDERLINE
            // + customerCode + StringConst.UNDERLINE + ip.getSupplierCode();
            // mtcsMaps.put(key, customerCode);
            // }
            // }

            List<TnmIfPart> dataList = new ArrayList<TnmIfPart>();
            for (TnmIfPart ip : ifPartDataList) {
                String customerCode = ip.getCustomerCode();
                // String key = ip.getMainRoute() + StringConst.UNDERLINE + ip.getTtcPartsNo() + StringConst.UNDERLINE
                // + customerCode + StringConst.UNDERLINE + ip.getSupplierCode();
                // String value = mtcsMaps.get(key);
                // if (StringUtil.isNullOrEmpty(value)) {
                String vendorRoute = custCodeVRouteMaps.get(customerCode);
                ip.setVendorRoute(vendorRoute);
                if (ZG.equals(ip.getBuildoutFlag())) {
                    ip.setInactiveFlag(InactiveFlag.INACTIVE);
                } else {
                    ip.setInactiveFlag(InactiveFlag.ACTIVE);
                }
                ip.setOfficeId(customerOfficeMap.get(customerCode));
                ip.setCreatedBy(userId);
                ip.setUpdatedBy(userId);
                dataList.add(ip);
                // }
            }

            // Deal DB Data
            dealDBData(dataList);
        }
    }

    /**
     * deal DB Data
     * 
     * @param dataList dataList
     */
    private void dealDBData(List<TnmIfPart> dataList) {
        if (dataList != null && dataList.size() > 0) {
            // get db date
            Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
            // loop
            for (TnmIfPart iip : dataList) {

                // set date
                iip.setCreatedDate(dbTime);
                iip.setUpdatedDate(dbTime);

                // insert TNM_EXP_PARTS
                this.baseMapper.insert(this.getSqlId("insertExpParts"), iip);

                // update TNM_IF_PARTS
                this.baseMapper.update(this.getSqlId("updateIfParts"), iip);
            }
        }
    }

    /**
     * Get send mail object(user info)
     * 
     * @param officeId office id
     * @return send mail object(user info)
     * @author shiyang
     */
    public List<TnmUser> getSendMailObject(int officeId) {
        BaseParam param = new BaseParam();
        param.setSwapData("OFFICE_ID", officeId);
        return baseMapper.select(this.getSqlId("getSendMailObject"), param);
    }
}
