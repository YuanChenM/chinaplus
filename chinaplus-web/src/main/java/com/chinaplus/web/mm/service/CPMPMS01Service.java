/**
 * @screen CPMPMS01
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.util.MessageManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;

/**
 * YMCULS01Service.
 */
@Service
public class CPMPMS01Service extends BaseService {

    /**
     * set User Office Id
     * 
     * @param userInfo userInfo
     * @return List<Integer> userOffIds
     */
    public List<Integer> setUserOffIds(UserInfo userInfo) {
        List<Integer> userOffIds = new ArrayList<Integer>();
        List<UserOffice> offices = userInfo.getUserOffice();
        if (offices != null && offices.size() > 0) {
            for (UserOffice uo : offices) {
                userOffIds.add(uo.getOfficeId());
            }
        }
        return userOffIds;
    }

    /**
     * set User Office Id
     * 
     * @param userInfo userInfo
     * @param businessPattern businessPattern
     * 
     * @return List<Integer> userOffIds
     */
    public List<Integer> setUserOffIds(UserInfo userInfo, int businessPattern) {
        List<Integer> userOffIds = new ArrayList<Integer>();
        List<UserOffice> offices = userInfo.getUserOffice();
        if (offices != null && offices.size() > 0) {
            for (UserOffice uo : offices) {
                // get flag
                boolean flag = businessPattern == BusinessPattern.V_V ? uo.getVvFlag() : uo.getAisinFlag();
                if (flag) {
                    userOffIds.add(uo.getOfficeId());
                }
            }
        }
        return userOffIds;
    }

    /**
     * Get Imp Office Code
     * 
     * @param lang lang
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getImpOfficeCode(String lang) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getImpOfficeCode"), param);
        return comboDataList;
    }

    /**
     * Get TTC Customer Code
     * 
     * @param lang lang
     * @param impOfficeCodeList impOfficeCodeList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getTtcCustomerCode(String lang, List<String> impOfficeCodeList) {
        BaseParam param = new BaseParam();
        param.setSwapData("impOfficeCodeList", impOfficeCodeList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getTtcCustomerCode"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get export Region
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getExportCountry() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getExportCountry"), param);
        return comboDataList;
    }

    /**
     * Get TCC Supplier Code
     * 
     * @param exportCountryList exportCountryList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getTccSupCode(List<String> exportCountryList) {
        BaseParam param = new BaseParam();
        param.setSwapData("exportCountryList", exportCountryList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getTccSupCode"), param);
        return comboDataList;
    }

    /**
     * Get SSMS Main Route
     * 
     * @param lang lang
     * @param exportCountryList exportCountryList
     * @param ttcSuppCdList ttcSuppCdList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getSsmsMainRoute(String lang, List<String> exportCountryList, List<String> ttcSuppCdList) {
        BaseParam param = new BaseParam();
        param.setSwapData("exportCountryList", exportCountryList);
        param.setSwapData("ttcSuppCdList", ttcSuppCdList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getSsmsMainRoute"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get SSMS Vendor Route
     * 
     * @param lang lang
     * @param exportCountryList exportCountryList
     * @param ttcSuppCdList ttcSuppCdList
     * @param ssmsMainRouteList ssmsMainRouteList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getSSMSVendorRoute(String lang, List<String> exportCountryList, List<String> ttcSuppCdList,
        List<String> ssmsMainRouteList) {
        BaseParam param = new BaseParam();
        param.setSwapData("exportCountryList", exportCountryList);
        param.setSwapData("ttcSuppCdList", ttcSuppCdList);
        param.setSwapData("ssmsMainRouteList", ssmsMainRouteList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getSSMSVendorRoute"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get Mail Invoice Customer Code
     * 
     * @param lang lang
     * @param impOfficeCodeList impOfficeCodeList
     * @param ttcCustCdList ttcCustCdList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getMailInvoiceCustomerCode(String lang, List<String> impOfficeCodeList,
        List<String> ttcCustCdList) {
        BaseParam param = new BaseParam();
        param.setSwapData("impOfficeCodeList", impOfficeCodeList);
        param.setSwapData("ttcCustCdList", ttcCustCdList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getMailInvoiceCustomerCode"), param);

        List<ComboData> cdList = new ArrayList<ComboData>();
        if (comboDataList != null && comboDataList.size() > 0) {
            for (ComboData cd : comboDataList) {
                String[] texts = cd.getText().split(",");
                for (String text : texts) {
                    ComboData cbd = new ComboData();
                    cbd.setId(text);
                    cbd.setText(text);
                    cdList.add(cbd);
                }
            }
        }

        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (cdList != null && cdList.size() > 0) {
            cdList.add(0, cd);
        } else {
            cdList.add(cd);
        }
        return cdList;
    }

    /**
     * Get TTC Imp W/H Code
     * 
     * @param lang lang
     * @param impOfficeCodeList impOfficeCodeList
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getTtcImpWHCode(String lang, List<String> impOfficeCodeList) {
        BaseParam param = new BaseParam();
        param.setSwapData("impOfficeCodeList", impOfficeCodeList);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getTtcImpWHCode"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get Part Type
     * 
     * @param language language
     * @param lang lang
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getPartType(Integer language, String lang) {
        BaseParam param = new BaseParam();
        param.setSwapData("language", language);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getPartType"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get Part Status
     * 
     * @param language language
     * @param lang lang
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getPartStatus(Integer language, String lang) {
        BaseParam param = new BaseParam();
        param.setSwapData("language", language);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getPartStatus"), param);
        String id = MessageManager.getMessage("CPMPMS01_Grid_Blank", Language.ENGLISH.getLocale());
        String text = MessageManager.getMessage("CPMPMS01_Grid_Blank", lang);
        ComboData cd = new ComboData();
        cd.setId(id);
        cd.setText(text);
        if (comboDataList != null && comboDataList.size() > 0) {
            comboDataList.add(0, cd);
        } else {
            comboDataList = new ArrayList<ComboData>();
            comboDataList.add(cd);
        }
        return comboDataList;
    }
}
