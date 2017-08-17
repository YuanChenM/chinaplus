/**
 * @screen CPMPMS02
 * @author zhang_chi
 */
package com.chinaplus.web.mm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.consts.CodeConst.BusinessPattern;
import com.chinaplus.common.consts.CodeConst.CodeMasterCategory;
import com.chinaplus.common.util.CodeCategoryManager;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.consts.NumberConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMPMS02Entity;

/**
 * YMCULS02Service.
 */
@Service
public class CPMPMS02Service extends BaseService {

    static final String STRING_ONE = "1";
    static final String STRING_TWO = "2";
    static final String STRING_THREE = "3";
    static final String STRING_FOUR = "4";
    static final String STRING_SIX = "6";
    static final String STRING_SEVEN = "7";

    /**
     * set User Office Id
     * 
     * @param Code Code
     * @return List<Integer> codeList
     */
    public List<Integer> setCodeList(String Code) {
        List<Integer> codeList = new ArrayList<Integer>();
        if (!StringUtil.isNullOrEmpty(Code)) {
            String[] Codes = Code.split(",");
            for (String c : Codes) {
                codeList.add(Integer.valueOf(c));
            }
        }
        return codeList;
    }

    /**
     * set User Office Id
     * 
     * @param Code Code
     * @return List<Integer> codeList
     */
    public List<String> setCodeStringList(String Code) {
        List<String> codeList = new ArrayList<String>();
        if (!StringUtil.isNullOrEmpty(Code)) {
            String[] Codes = Code.split(",");
            for (String c : Codes) {
                codeList.add(c);
            }
        }
        return codeList;
    }

    /**
     * Get Office Code By userOffIds
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getOfficeCodeByIds() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getOfficeCodeByIds"), param);
        return comboDataList;
    }

    /**
     * Get Customer Code By userOffIds
     * 
     * @param userOffIds userOffIds
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getCustomerCodeByIds(List<Integer> userOffIds) {
        BaseParam param = new BaseParam();
        param.setSwapData("userOffIds", userOffIds);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getCustomerCodeByIds"), param);
        return comboDataList;
    }

    /**
     * Get TCC Supplier Code By userOffIds
     * 
     * @param userOffIds userOffIds
     * @param ttcCustCd ttcCustCd
     * @param vvFlag vvFlag
     * @param aisinFlag aisinFlag
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getTccSupCodeByIds(List<Integer> userOffIds, String ttcCustCd, boolean vvFlag,
        boolean aisinFlag) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        if ((userOffIds != null && userOffIds.size() > 0) || !StringUtil.isNullOrEmpty(ttcCustCd)) {
            param.setSwapData("userOffIds", userOffIds);
            param.setSwapData("ttcCustCd", setCodeStringList(ttcCustCd));
            List<Integer> businessPatten = new ArrayList<Integer>();
            if (vvFlag) {
                businessPatten.add(BusinessPattern.V_V);
            }
            if (aisinFlag) {
                businessPatten.add(BusinessPattern.AISIN);
            }
            param.setSwapData("businessPatten", businessPatten);
            comboDataList = baseMapper.select(this.getSqlId("getTccSupCodeByIds"), param);
        } else {
            comboDataList = baseMapper.select(this.getSqlId("getTccSupCode"), param);
        }
        return comboDataList;
    }

    /**
     * Get SSMS Main Route Code By userOffIds
     * 
     * @param userOffIds userOffIds
     * @param ttcCustCd ttcCustCd
     * @param ttcSuppCd ttcSuppCd
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getSsmsMainRouteCodeByIds(List<Integer> userOffIds, String ttcCustCd, String ttcSuppCd) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        if ((userOffIds != null && userOffIds.size() > 0) || !StringUtil.isNullOrEmpty(ttcCustCd)
                || !StringUtil.isNullOrEmpty(ttcSuppCd)) {
            param.setSwapData("userOffIds", userOffIds);
            param.setSwapData("ttcCustCd", setCodeStringList(ttcCustCd));
            param.setSwapData("ttcSuppCd", setCodeStringList(ttcSuppCd));
            comboDataList = baseMapper.select(this.getSqlId("getSsmsMainRouteCodeByIds"), param);
        } else {
            comboDataList = baseMapper.select(this.getSqlId("getSsmsMainRouteCode"), param);
        }
        return comboDataList;
    }

    /**
     * Get SSMS Supplier Code By userOffIds
     * 
     * @param userOffIds userOffIds
     * @param ttcCustCd ttcCustCd
     * @param ttcSuppCd ttcSuppCd
     * @param ssmsMainRoute ssmsMainRoute
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getSsmsSuppCodeByIdsByIds(List<Integer> userOffIds, String ttcCustCd, String ttcSuppCd,
        String ssmsMainRoute) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        if ((userOffIds != null && userOffIds.size() > 0) || !StringUtil.isNullOrEmpty(ttcCustCd)
                || !StringUtil.isNullOrEmpty(ttcSuppCd) || !StringUtil.isNullOrEmpty(ssmsMainRoute)) {
            param.setSwapData("userOffIds", userOffIds);
            param.setSwapData("ttcCustCd", setCodeStringList(ttcCustCd));
            param.setSwapData("ttcSuppCd", setCodeStringList(ttcSuppCd));
            param.setSwapData("ssmsMainRoute", setCodeStringList(ssmsMainRoute));
            comboDataList = baseMapper.select(this.getSqlId("getSsmsSuppCodeByIds"), param);
        } else {
            comboDataList = baseMapper.select(this.getSqlId("getSsmsSuppCode"), param);
        }
        return comboDataList;
    }

    /**
     * Get Business Pattern Code By userOffIds
     * 
     * @param language language
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getBusinessPatternByIds(Integer language) {
        BaseParam param = new BaseParam();
        param.setSwapData("language", language);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getBusinessPatternByIds"), param);
        return comboDataList;
    }

    /**
     * Get Shipping Route Code
     * 
     * @param param parameter
     * 
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getShipRouteCode(BaseParam param) {
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getShipRouteCode"), param);
        return comboDataList;
    }

    /**
     * Get Shipping Route Type
     * 
     * @param userOffIds userOffIds
     * @param shipRouteCode shipRouteCode
     * @param language language
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getShipRouteType(List<Integer> userOffIds, String shipRouteCode, Integer language) {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = null;
        param.setSwapData("language", language);
        if (!userOffIds.isEmpty()) {
            param.setSwapData("userOffIds", userOffIds);
            comboDataList = baseMapper.select(this.getSqlId("getShipRouteTypeByCode"), param);
        } else {
            comboDataList = baseMapper.select(this.getSqlId("getShipRouteType"), param);
        }
        return comboDataList;
    }

    /**
     * GET Effective from ETD and Effective to ETD
     * 
     * @param shipRouteCode shipRouteCode
     * @param lang lang
     * @return cpmpms02Entity cpmpms02Entity
     */
    public CPMPMS02Entity getEffFromToEtd(String shipRouteCode, Integer lang) {
        BaseParam param = new BaseParam();
        param.setSwapData("shipRouteCode", setCodeStringList(shipRouteCode));
        CPMPMS02Entity cef = baseMapper.findOne(this.getSqlId("getEffFromEtd"), param);
        CPMPMS02Entity cet = baseMapper.findOne(this.getSqlId("getEffToEtd"), param);

        CPMPMS02Entity cpmpms02Entity = new CPMPMS02Entity();
        if (cef != null && cet != null) {
            SimpleDateFormat sdfE = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            SimpleDateFormat sdfZ = new SimpleDateFormat("yyyy/MM/dd");
            if (lang == IntDef.INT_ONE) {
                cpmpms02Entity.setEffFromEtdStr(sdfZ.format(cef.getEffFromEtd()));
                cpmpms02Entity.setEffTotdStr(sdfZ.format(cet.getEffTotd()));
            } else {
                cpmpms02Entity.setEffFromEtdStr(sdfE.format(cef.getEffFromEtd()));
                cpmpms02Entity.setEffTotdStr(sdfE.format(cet.getEffTotd()));
            }
        }
        return cpmpms02Entity;
    }

    // /**
    // * GET Effective from ETD and Effective to ETD
    // *
    // * @param kbImpOfficeCode kbImpOfficeCode
    // * @param kbttcCustomerCode kbttcCustomerCode
    // * @param lang lang
    // * @return cpmpms02Entity cpmpms02Entity
    // * @throws ParseException ParseException
    // */
    // public CPMPMS02Entity getOrderMonthFromTo(String kbImpOfficeCode, String kbttcCustomerCode, Integer lang)
    // throws ParseException {
    // BaseParam param = new BaseParam();
    // param.setSwapData("kbImpOfficeCode", setCodeStringList(kbImpOfficeCode));
    // param.setSwapData("kbttcCustomerCode", setCodeStringList(kbttcCustomerCode));
    // CPMPMS02Entity cef = baseMapper.findOne(this.getSqlId("getOrderMonthFrom"), param);
    // CPMPMS02Entity cet = baseMapper.findOne(this.getSqlId("getOrderMonthTo"), param);
    //
    // CPMPMS02Entity cpmpms02Entity = new CPMPMS02Entity();
    //
    // if (cef != null && cet != null) {
    // String omf = cef.getOrderMonthFrom();
    // String omt = cet.getOrderMonthTo();
    //
    // String omfs = omf.substring(NumberConst.IntDef.INT_ZERO, NumberConst.IntDef.INT_FOUR) + "/"
    // + omf.substring(NumberConst.IntDef.INT_FOUR, NumberConst.IntDef.INT_SIX);
    // String omts = omt.substring(NumberConst.IntDef.INT_ZERO, NumberConst.IntDef.INT_FOUR) + "/"
    // + omt.substring(NumberConst.IntDef.INT_FOUR, NumberConst.IntDef.INT_SIX);
    //
    // SimpleDateFormat sdfE = new SimpleDateFormat("MMM yyyy", Locale.US);
    // SimpleDateFormat sdfZ = new SimpleDateFormat("yyyy/MM");
    // if (lang == IntDef.INT_ONE) {
    // cpmpms02Entity.setOrderMonthFrom(sdfZ.format(sdfZ.parse(omfs)));
    // cpmpms02Entity.setOrderMonthTo(sdfZ.format(sdfZ.parse(omts)));
    // } else {
    // cpmpms02Entity.setOrderMonthFrom(sdfE.format(sdfZ.parse(omfs)));
    // cpmpms02Entity.setOrderMonthTo(sdfE.format(sdfZ.parse(omts)));
    // }
    //
    // }
    //
    // return cpmpms02Entity;
    // }

    /**
     * Get country Code By userOffIds
     *
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getCountry() {
        BaseParam param = new BaseParam();
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getCountry"), param);
        return comboDataList;
    }

    /**
     * Get calendar Code By userOffIds
     *
     * @param userOffIds userOffIds
     * @param country country
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getCalendarCode(List<Integer> userOffIds, String country) {
        BaseParam param = new BaseParam();
        param.setSwapData("userOffIds", userOffIds);
        param.setSwapData("country", country);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getCalendarCode"), param);
        return comboDataList;

    }

    /**
     * Get Year Code By country,calendarCode
     *
     * @param country country
     * @param calendarCode calendarCode
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getYear(String country, String calendarCode) {
        BaseParam param = new BaseParam();
        param.setSwapData("country", country);
        param.setSwapData("calendarCode", calendarCode);
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getYear"), param);
        if (comboDataList == null || comboDataList.size() == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.FORMAT_DATE_YYYYMMDD);
            String nowDate = sdf.format(new Date());
            nowDate = nowDate.substring(NumberConst.IntDef.INT_ZERO, NumberConst.IntDef.INT_FOUR);

            ComboData cd = new ComboData();
            cd.setId(nowDate + StringConst.BLANK);
            cd.setText(nowDate + StringConst.BLANK);
            comboDataList.add(cd);
        }
        return comboDataList;
    }

    /**
     * Get Login Id Code By userOffIds
     *
     * @param language language
     * @param vvFlag vvFlag
     * @param aisinFlag aisinFlag
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getParty(Integer language, boolean vvFlag, Boolean aisinFlag) {
        BaseParam param = new BaseParam();
        param.setSwapData("language", language);
        List<ComboData> comboDataList = null;

        if (vvFlag && !aisinFlag) {
            comboDataList = baseMapper.select(this.getSqlId("getPartyVv"), param);
        } else if (!vvFlag && aisinFlag) {
            comboDataList = baseMapper.select(this.getSqlId("getPartyAisin"), param);
        } else if (vvFlag && aisinFlag) {
            comboDataList = baseMapper.select(this.getSqlId("getPartyVvAisin"), param);
        }

        return comboDataList;
    }

    /**
     * Get Login Id Code By userOffIds
     *
     * @param language language
     * @param party party
     * @param country country
     * @param cmImpOfficeCode cmImpOfficeCode
     * @param userOffIds userOffIds
     * @param vvFlag vvFlag
     * @param aisinFlag aisinFlag
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getPartyCode(Integer language, String party, String country, String cmImpOfficeCode,
        List<Integer> userOffIds, boolean vvFlag, Boolean aisinFlag) {
        BaseParam param = new BaseParam();
        param.setSwapData("country", country);
        param.setSwapData("userOffIds", userOffIds);

        List<ComboData> comboDataList = null;
        // party selected is Supplier
        if (party.equals(STRING_ONE)) {
            if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
                param.setSwapData("userOffIds", setCodeStringList(cmImpOfficeCode));
            }
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeSupp"), param);
        }
        // party selected is TTC Imp Warehouse
        else if (!StringUtil.isNullOrEmpty(country) && party.equals(STRING_THREE)) {
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeTtcImpWah"), param);
        }
        // party selected is TTC Import Office
        else if (!StringUtil.isNullOrEmpty(country) && party.equals(STRING_SIX)) {
            if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
                param.setSwapData("userOffIds", setCodeStringList(cmImpOfficeCode));
            }
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeTtcImpOff"), param);
        }
        // party selected is Customer
        else if (!StringUtil.isNullOrEmpty(country) && party.equals(STRING_FOUR)) {
            List<String> busPatternList = new ArrayList<String>();
            if (vvFlag) {
                busPatternList.add(STRING_ONE);
            }
            if (aisinFlag) {
                busPatternList.add(STRING_TWO);
            }
            if (busPatternList != null && busPatternList.size() > NumberConst.IntDef.INT_ZERO) {
                if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
                    param.setSwapData("userOffIds", setCodeStringList(cmImpOfficeCode));
                }
                param.setSwapData("busPatternList", busPatternList);
                comboDataList = baseMapper.select(this.getSqlId("getPartyCodeCust"), param);
            }
        }
        // party selected is Exp Warehouse
        else if (party.equals(STRING_SEVEN)) {
            param.setSwapData("language", language);
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeExpWah"), param);
        }

        return comboDataList;
    }

    /**
     * Get Login Id Code By userOffIds
     * 
     * @param param parameter
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getLoginIdByIds(BaseParam param) {
        List<ComboData> comboDataList = baseMapper.select(this.getSqlId("getLogindDByIds"), param);
        return comboDataList;
    }

    /**
     * Get Alert Level
     * 
     * @param lang language
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getAlertLevel(Integer lang) {
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        // if (loginId == null || loginId.equals("")) {
        /*
         * for (int i = 1; i < NumberConst.IntDef.INT_FOUR; i++) {
         * ComboData cd = new ComboData();
         * cd.setId(i + "");
         * cd.setText(i + "");
         * comboDataList.add(cd);
         * }
         */
        Map<Integer, String> alertLevelMap = CodeCategoryManager.getCodeCategaryByLang(lang,
            CodeMasterCategory.EMAIL_ALERT_LEVEL);

        // get values
        if (alertLevelMap != null) {
            Set<Integer> keySet = alertLevelMap.keySet();
            List<Integer> keyList = new ArrayList<Integer>(keySet);
            Collections.sort(keyList);
            for (int i = 0; i < keyList.size(); i++) {
                ComboData cd = new ComboData();
                Integer key = keyList.get(i);
                cd.setId(StringUtil.toString(key));
                cd.setText(alertLevelMap.get(key));
                comboDataList.add(cd);
            }
        }

        return comboDataList;
        // } else {
        //
        // BaseParam param = new BaseParam();
        // param.setSwapData("loginId", setCodeStringList(loginId));
        // param.setSwapData("userOffIds", userOffIds);
        // comboDataList = baseMapper.select(this.getSqlId("getAlertLevel"), param);
        // return comboDataList;
        // }
    }

}
