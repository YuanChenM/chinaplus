/**
 * @screen CPMPMF11
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.chinaplus.common.consts.CodeConst.InactiveFlag;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.web.mm.entity.CPMPMF11Entity;

/**
 * CPMPMF11DBAisinService.
 */
@Service
public class CPMPMF11DBAisinService extends BaseService {

    /**
     * query customerCode + expCustCode
     * 
     * @param cutExpCodeMapsDB cutExpCodeMapsDB
     * @param cutCodeExpMapsDB cutCodeExpMapsDB
     */
    public void getCutExpCode(Map<String, String> cutExpCodeMapsDB, Map<String, String> cutCodeExpMapsDB) {

        List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getCutExpCode"), new BaseParam());
        if (dataList != null && dataList.size() > 0) {
            for (CPMPMF11Entity cf : dataList) {
                cutExpCodeMapsDB.put(cf.getCustomerCode() + StringConst.NEW_COMMA + cf.getOfficeCode(),
                    cf.getExpCustCode());
                cutCodeExpMapsDB.put(cf.getExpCustCode(),
                    cf.getCustomerCode() + StringConst.NEW_COMMA + cf.getOfficeCode());
            }
        }
    }

    /**
     * query new ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, Object> getTTCSECNewMap(List<CPMPMF11Entity> initDataList) {
        Map<String, Object> maps = new HashMap<String, Object>();
        // new DB ttcPartsNo + ttcSuppCode + customerCode + officeCode
        Map<String, String> ttcNXMapsDB = new HashMap<String, String>();
        // new DB suppPartsNo + ttcSuppCode + expCustCode
        Map<String, String> tteNXMapsDB = new HashMap<String, String>();
        // new DB custPartsNo + ttcSuppCode + customerCode + officeCode
        Map<String, String> ctcNXMapsDB = new HashMap<String, String>();

        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getTTCSECNew"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String expCustCode = cf.getExpCustCode();
                    String ttcPartsNo = cf.getTtcPartsNo();
                    String ttcSuppCode = cf.getTtcSuppCode();
                    String customerCode = cf.getCustomerCode();
                    String suppPartsNo = cf.getSuppPartsNo();
                    String custPartsNo = cf.getCustPartsNo();
                    // shiyang add start
                    String officeCode = cf.getOfficeCode();
                    // shiyang add end

                    // shiyang mod start
                    // String keyttc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                    // + customerCode;
                    String keyttc = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                            + customerCode + StringConst.UNDERLINE + officeCode;
                    // shiyang mod end
                    String keytte = suppPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                            + expCustCode;
                    // shiyang mod start
                    // String keyctc = custPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                    // + customerCode;
                    String keyctc = custPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                            + customerCode + StringConst.UNDERLINE + officeCode;
                    // shiyang mod end
                    ttcNXMapsDB.put(keyttc, ttcPartsNo);
                    tteNXMapsDB.put(keytte, ttcPartsNo);
                    ctcNXMapsDB.put(keyctc, ttcPartsNo);
                }
            }
        }
        maps.put("ttcNXMapsDB", ttcNXMapsDB);
        maps.put("tteNXMapsDB", tteNXMapsDB);
        maps.put("ctcNXMapsDB", ctcNXMapsDB);
        return maps;
    }

    /**
     * query mod ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, Object> getTTCSECModMap(List<CPMPMF11Entity> initDataList) {
        Map<String, Object> maps = new HashMap<String, Object>();
        // mod DB ttcPartsNo + ttcSuppCode + customerCode + suppPartsNo + expCustCode + custPartsNo
        Map<String, String> ttcsecMXMapsDB = new HashMap<String, String>();
        // shiyang add strat
        // mod DB ttcPartsNo + officeCode + ttcSuppCode + customerCode
        Map<String, String> toscMXMapsDB = new HashMap<String, String>();
        // mod DB ttcPartsNo + officeCode + customerCode
        Map<String, String> tocMXMapsDB = new HashMap<String, String>();
        // shiyang add end
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getTTCSECMod"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String expCustCode = cf.getExpCustCode();
                    String ttcPartsNo = cf.getTtcPartsNo();
                    String ttcSuppCode = cf.getTtcSuppCode();
                    String customerCode = cf.getCustomerCode();
                    String suppPartsNo = cf.getSuppPartsNo();
                    String custPartsNo = cf.getCustPartsNo();
                    // shiyang add start
                    String officeCode = cf.getOfficeCode();
                    // shiyang add end
                    String key = ttcPartsNo + StringConst.UNDERLINE + ttcSuppCode + StringConst.UNDERLINE
                            + customerCode + StringConst.UNDERLINE + suppPartsNo + StringConst.UNDERLINE + expCustCode
                            + StringConst.UNDERLINE + custPartsNo;
                    // shiyang mod start
                    // String value = cf.getExpPartsId() + StringConst.UNDERLINE + cf.getShippingRouteCode();
                    String value = cf.getExpPartsId() + StringConst.NEW_COMMA + cf.getShippingRouteCode();
                    // shiyang mod end
                    ttcsecMXMapsDB.put(key, value);
                    // shiyang add start
                    String keytosc = ttcPartsNo + StringConst.UNDERLINE + officeCode + StringConst.UNDERLINE
                            + ttcSuppCode + StringConst.UNDERLINE + customerCode;
                    String keytoc = ttcPartsNo + StringConst.UNDERLINE + officeCode + StringConst.UNDERLINE
                            + customerCode;
                    toscMXMapsDB.put(keytosc, value);
                    tocMXMapsDB.put(keytoc, ttcSuppCode);
                    // shiyang add end
                }
            }
        }
        maps.put("ttcsecMXMapsDB", ttcsecMXMapsDB);
        // shiyang add start
        maps.put("toscMXMapsDB", toscMXMapsDB);
        maps.put("tocMXMapsDB", tocMXMapsDB);
        // shiyang add end
        return maps;
    }

    /**
     * query all ttcPartsNo + customerCode + expCustCode + custPartsNo + officeCode
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, Object> getTCSECMap(List<CPMPMF11Entity> initDataList) {
        Map<String, Object> maps = new HashMap<String, Object>();
        // DB ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode
        Map<String, String> tcceoMapsDB = new HashMap<String, String>();
        // DB count(ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode)
        Map<String, Integer> tcceoCountMapsDB = new HashMap<String, Integer>();
        // DB ttcPartsNo + customerCode + custPartsNo + expCustCode + officeCode,partsId
        Map<String, Integer> tcceoIdMapsDB = new HashMap<String, Integer>();
        // shiyang add start
        // DB ttcPartsNo + officeCode + customerCode, partsId
        Map<String, Integer> partsIdMapsDB = new HashMap<String, Integer>();
        // shiyang add end

        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getTCSEC"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    String inactiveFlag = cf.getInactiveFlag();
                    // shiyang add start
                    // String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getCustomerCode()
                    // + StringConst.UNDERLINE + cf.getCustPartsNo() + StringConst.UNDERLINE + cf.getExpCustCode()
                    // + StringConst.UNDERLINE + cf.getOfficeCode();
                    String key = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getCustomerCode()
                            + StringConst.UNDERLINE + cf.getCustPartsNo() + StringConst.UNDERLINE + cf.getExpCustCode()
                            + StringConst.UNDERLINE + cf.getOfficeCode() + StringConst.UNDERLINE + cf.getTtcSuppCode();
                    // shiyang add end
                    tcceoMapsDB.put(key, inactiveFlag);
                    tcceoIdMapsDB.put(key, cf.getPartsId());
                    if ((InactiveFlag.ACTIVE + "").equals(inactiveFlag)) {
                        Integer num = tcceoCountMapsDB.get(key);
                        if (null == num) {
                            tcceoCountMapsDB.put(key, 1);
                        } else {
                            num++;
                            tcceoCountMapsDB.put(key, num);
                        }
                    }

                    // shiyang add start
                    String keyParts = cf.getTtcPartsNo() + StringConst.UNDERLINE + cf.getOfficeCode()
                            + StringConst.UNDERLINE + cf.getCustomerCode();
                    partsIdMapsDB.put(keyParts, cf.getPartsId());
                    // shiyang add end
                }
            }
        }

        maps.put("tcceoMapsDB", tcceoMapsDB);
        maps.put("tcceoCountMapsDB", tcceoCountMapsDB);
        maps.put("tcceoIdMapsDB", tcceoIdMapsDB);
        // shiyang add start
        maps.put("partsIdMapsDB", partsIdMapsDB);
        // shiyang add end
        return maps;
    }

    /**
     * query suppPartsNo related to more then one ttcPartsNo
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getSuppPNo(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getSuppPNo"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    maps.put(cf.getSuppPartsNo(), cf.getTtcPartsNo());
                }
            }
        }
        return maps;
    }

    /**
     * query custPartsNo related to more then one ttcPartsNo
     * 
     * @param initDataList initDataList
     * @return maps maps
     */
    public Map<String, String> getCustPNo(List<CPMPMF11Entity> initDataList) {
        Map<String, String> maps = new HashMap<String, String>();
        if (initDataList != null && initDataList.size() > 0) {
            BaseParam param = new BaseParam();
            param.setSwapData("initDataList", initDataList);
            List<CPMPMF11Entity> dataList = baseMapper.select(this.getSqlId("getCustPNo"), param);
            if (dataList != null && dataList.size() > 0) {
                for (CPMPMF11Entity cf : dataList) {
                    maps.put(cf.getCustPartsNo(), cf.getTtcPartsNo());
                }
            }
        }
        return maps;
    }

}
