/**
 * CPMCMS01Service.java
 * 
 * @screen CPMCMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.consts.CodeConst.ShippingRouteType;
import com.chinaplus.common.entity.TnmCalendarDetail;
import com.chinaplus.common.entity.TnmCalendarMaster;
import com.chinaplus.common.entity.TnmCustomer;
import com.chinaplus.common.entity.TnmOffice;
import com.chinaplus.common.entity.TnmRegion;
import com.chinaplus.common.entity.TnmSupplier;
import com.chinaplus.core.base.BaseService;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.AisinCommonEntity;
import com.chinaplus.web.mm.entity.CPMCMS01Entity;
import com.chinaplus.web.mm.entity.CalendarDateEntity;
import com.chinaplus.web.mm.entity.CalendarPartyEntity;

/**
 * The service for CPMCMS01Service .
 */
@Service
public class CPMCMS01Service extends BaseService {
    /** REGION_CODE */
    public static final String REGION_CODE = "regionCode";
    /** CALENDAR_CODE_PAGE */
    private static final String CALENDAR_CODE_PAGE = "calendarCode";
    /** year */
    private static final String YEAR_PAGE = "year";
    /** OFFICE_CODE_PAGE */
    private static final String OFFICE_CODE_PAGE = "ttcOfficeCode";
    /** OFFICE_CODE_PAGE */
    private static final String PARTY_PAGE = "party";
    /** PARTY_CODE_PAGE */
    private static final String PARTY_CODE_PAGE = "partyCode";
    /** WORKING_FLAG */
    private static final String WORKING_FLAG = "workingFlag";
    /** OFFICE_ID */
    private static final String OFFICE_ID = "officeId";
    /** ENTITY */
    private static final String ENTITY = "entity";
    /** CALENDAR_PARTY */
    private static final String CALENDAR_PARTY = "CalendarParty";
    /** PARTY_CODE_ONE */
    private static final String PARTY_CODE_ONE = "partyCodeOne";
    /** PARTY_ENTITY */
    private static final String PARTY_ENTITY = "partyEntity";
    /** CALENDAR_PARTY */
    private static final String CALENDAR_PARTY_OTHER = "calendarParty";
    /** CALENDAR_ID */
    private static final String CALENDAR_ID = "calendarId";
    /** DATE */
    private static final String DATE = "date";

    private static final String STRING_ONE = "1";
    private static final String STRING_TWO = "2";
    private static final String STRING_THREE = "3";
    private static final String STRING_FOUR = "4";
    private static final String STRING_SEVEN = "7";
    private static final Integer INTEGER_365 = 365;
    private static final Integer INTEGER_366 = 366;

    /** Aisin Common Service */
    @Autowired
    protected AisinCommonService aisinCommonService;

    /**
     * doLoadCtryCombo
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<ComboData> doOfficeCombo(BaseParam param) {

        List<ComboData> result = new ArrayList<ComboData>();
        List<TnmOffice> list = new ArrayList<TnmOffice>();
        list = baseDao.select("FROM TNM_OFFICE");
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                TnmOffice region = list.get(i);
                if (null == region) {
                    continue;
                }
                ComboData code = new ComboData();
                code.setId(region.getOfficeId() + "");
                code.setText(region.getOfficeCode());
                result.add(code);
            }
        }
        return result;
    }

    /**
     * doLoadCtryCombo
     * 
     * @param param BaseParam
     * @return List<ComboData>
     */
    public List<ComboData> doLoadCtryCombo(BaseParam param) {

        List<ComboData> result = new ArrayList<ComboData>();
        List<TnmRegion> regionlist = new ArrayList<TnmRegion>();
        regionlist = baseDao.select("FROM TNM_REGION");
        String partyType = (String) param.getSwapData().get("party");
        String officeId = (String) param.getSwapData().get("officeId");
        if (!StringUtil.isNullOrEmpty(partyType) && ("1").equals(partyType)) {
            List<TnmSupplier> list = new ArrayList<TnmSupplier>();
            list = baseDao.select("FROM TNM_SUPPLIER");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    TnmSupplier region = list.get(i);
                    if (null == region) {
                        continue;
                    }
                    ComboData code = new ComboData();
                    for (TnmRegion entrty : regionlist) {
                        if (entrty.getRegionCode().equals(region.getRegionCode())) {
                            code.setId(entrty.getRegionId() + "");
                            break;
                        }
                    }
                    code.setText(region.getRegionCode());
                    result.add(code);
                }
            }
        } else if (!StringUtil.isNullOrEmpty(partyType) && String.valueOf(IntDef.INT_FOUR).equals(partyType)) {
            List<TnmCustomer> list = new ArrayList<TnmCustomer>();
            list = baseDao.select("FROM TNM_CUSTOMER");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    TnmCustomer region = list.get(i);
                    if (null == region) {
                        continue;
                    }
                    ComboData code = new ComboData();
                    if (StringUtil.isNullOrEmpty(officeId)) {
                        for (TnmRegion entrty : regionlist) {
                            if (entrty.getRegionCode().equals(region.getRegionCode())) {
                                code.setId(entrty.getRegionId() + "");
                                break;
                            }
                        }
                        code.setText(region.getRegionCode());
                        result.add(code);
                    } else if (String.valueOf(region.getOfficeId()).equals(officeId)) {
                        for (TnmRegion entrty : regionlist) {
                            if (entrty.getRegionCode().equals(region.getRegionCode())) {
                                code.setId(entrty.getRegionId() + "");
                                break;
                            }
                        }
                        code.setText(region.getRegionCode());
                        result.add(code);
                    }
                }
            }
        } else if (!StringUtil.isNullOrEmpty(partyType)
                && (String.valueOf(IntDef.INT_TWO).equals(partyType)
                        || String.valueOf(IntDef.INT_THREE).equals(partyType)
                        || String.valueOf(IntDef.INT_FIVE).equals(partyType) || String.valueOf(IntDef.INT_SIX).equals(
                    partyType))) {
            List<TnmOffice> list = new ArrayList<TnmOffice>();
            list = baseDao.select("FROM TNM_OFFICE");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    TnmOffice region = list.get(i);
                    if (null == region) {
                        continue;
                    }
                    ComboData code = new ComboData();

                    if (StringUtil.isNullOrEmpty(officeId)) {
                        for (TnmRegion entrty : regionlist) {
                            if (entrty.getRegionCode().equals(region.getRegionCode())) {
                                code.setId(entrty.getRegionId() + "");
                                break;
                            }
                        }
                        code.setText(region.getRegionCode());
                        result.add(code);
                    } else if (String.valueOf(region.getOfficeId()).equals(officeId)) {
                        for (TnmRegion entrty : regionlist) {
                            if (entrty.getRegionCode().equals(region.getRegionCode())) {
                                code.setId(entrty.getRegionId() + "");
                                break;
                            }
                        }
                        code.setText(region.getRegionCode());
                        result.add(code);
                    }

                }
            }
        } else {
            if (null != regionlist && regionlist.size() > 0) {
                for (int i = 0; i < regionlist.size(); i++) {
                    TnmRegion region = regionlist.get(i);
                    if (null == region) {
                        continue;
                    }
                    ComboData code = new ComboData();
                    code.setId(region.getRegionId() + "");
                    code.setText(region.getRegionCode());
                    result.add(code);
                }
            }
        }
        return result;
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
     * @param pageFrom String
     * @return List<ComboData> comboDataList
     */
    public List<ComboData> getPartyCode(Integer language, String party, String country, String cmImpOfficeCode,
        List<Integer> userOffIds, boolean vvFlag, Boolean aisinFlag, String pageFrom) {
        BaseParam param = new BaseParam();
        param.setSwapData("userOffIds", userOffIds);
        param.setSwapData("country", country);

        List<ComboData> comboDataList = null;
        // party selected is Supplier
        if (party.equals(STRING_ONE)) {
            if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
                param.setSwapData("userOffIds", setCodeStringList(cmImpOfficeCode));
            }
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeSupp"), param);
        }
        // party selected is TTC Imp Warehouse
        else if (party.equals(STRING_THREE)) {
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeTtcImpWah"), param);
        }
        // party selected is Customer
        else if (party.equals(STRING_FOUR)) {
            List<String> busPatternList = new ArrayList<String>();

            if (vvFlag) {
                busPatternList.add(STRING_ONE);
            }
            if (aisinFlag) {
                busPatternList.add(STRING_TWO);
            }

            if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
                param.setSwapData("userOffIds", setCodeStringList(cmImpOfficeCode));
                param.setSwapData("busPatternList", busPatternList);
                comboDataList = baseMapper.select(this.getSqlId("getPartyCodeCust"), param);
            }

        }
        // party selected is Exp Warehouse
        else if (party.equals(STRING_SEVEN)) {
            param.setSwapData("language", language);
            comboDataList = baseMapper.select(this.getSqlId("getPartyCodeExpWah"), param);
        } else {

            comboDataList = null;

        }

        return comboDataList;
    }

    /**
     * Get Login Id Code By userOffIds
     *
     * @param cmImpOfficeCode cmImpOfficeCode
     * @param userOffIds userOffIds
     * @param userId userId
     * @param country country
     * @return List<ComboData> comboDataList
     * 
     */
    public List<ComboData> getPartyCodeByOffice(String cmImpOfficeCode, List<Integer> userOffIds, Integer userId,
        String country) {
        BaseParam param = new BaseParam();
        param.setSwapData("userOffIds", userOffIds);
        param.setSwapData("userId", userId);
        param.setSwapData("country", country);
        List<ComboData> comboDataList = null;
        if (!StringUtil.isNullOrEmpty(cmImpOfficeCode)) {
            ArrayList<Integer> userOffId = new ArrayList<Integer>();
            userOffId.add(Integer.valueOf(cmImpOfficeCode));
            param.setSwapData("userOffIds", userOffId);

        }

        comboDataList = baseMapper.select(this.getSqlId("getPartyCodeCustByOffice"), param);

        if (comboDataList.size() == 0) {
            List<ComboData> comboDataListAll = baseMapper.select(this.getSqlId("getPartyCodeCustAll"), param);
            comboDataList = comboDataListAll;
        }

        return comboDataList;
    }

    /**
     * check PartyCodeByOfficeList
     *
     * @param userOffIds userOffIds
     * @param userId userId
     * @return List<ComboData> comboDataList
     * 
     */
    public List<ComboData> checkPartyCodeByOfficeList(List<Integer> userOffIds, Integer userId) {
        BaseParam param = new BaseParam();
        param.setSwapData("userOffIds", userOffIds);
        param.setSwapData("userId", userId);
        List<ComboData> comboDataList = null;

        comboDataList = baseMapper.select(this.getSqlId("getPartyCodeCustByOfficeList"), param);

        return comboDataList;
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
     * do Load CalendarCode Combo
     * 
     * @param param PageParam
     * @return List<ComboData>
     */
    public List<ComboData> doLoadCalendarCodeCombo(PageParam param) {
        List<ComboData> comboDataList = new ArrayList<ComboData>();
        comboDataList = baseMapper.select(this.getSqlId("getCalendarCode"), param);

        // String hql = "FROM TNM_REGION WHERE REGION_ID = ? ";
        // Object[] param = new Object[] { countryIDStr };
        // List<TnmRegion> list = baseDao.select(hql, param);
        // if (list != null && list.size() > 0) {
        // for (TnmRegion ngr : list) {
        // String hql2 = "FROM TNM_CALENDAR_MASTER WHERE REGION_CODE = ? ";
        // Object[] param2 = new Object[] { ngr.getRegionCode() };
        // List<TnmCalendarMaster> list2 = baseDao.select(hql2, param2);
        // if (list2 != null && list2.size() > 0) {
        // for (TnmCalendarMaster cm : list2) {
        // if (null == cm) {
        // continue;
        // }
        // ComboData code = new ComboData();
        // code.setId(cm.getCalendarId() + StringConst.EMPTY);
        // code.setText(cm.getCalendarCode());
        // result.add(code);
        // }
        // }
        // return result;
        // }
        // }
        return comboDataList;
    }

    /**
     * do Load year Combo
     * 
     * @param calendarId String calendarId
     * @param calendarRawValue String calendarRawValue
     * @param officeId String
     * @param dateTimeNow the dateTimeNow
     * @return List<ComboData>
     */
    public List<ComboData> doLoadyearCombo(String calendarId, String calendarRawValue, String officeId, Date dateTimeNow) {
        List<ComboData> result = new ArrayList<ComboData>();

        boolean isHaveThisCalenCode = false;
        String calendarCode = StringConst.EMPTY;

        TnmCalendarMaster conditon = new TnmCalendarMaster();
        if (null != StringUtil.toInteger(calendarId)) {
            conditon.setCalendarId(StringUtil.toInteger(calendarId));
        }
        if (null != StringUtil.toInteger(officeId)) {
            if(!calendarRawValue.equals("TLS") && !calendarRawValue.equals("KUC") && !calendarRawValue.equals("JPBC")){
                conditon.setOfficeId(StringUtil.toInteger(officeId));
            }
        }
        conditon.setCalendarCode(calendarRawValue);

        List<TnmCalendarMaster> list = baseDao.select(conditon);
        if (list != null && list.size() > 0) {
            for (TnmCalendarMaster tcm : list) {
                isHaveThisCalenCode = true;
                calendarCode = tcm.getCalendarCode();
                break;
            }
        }

        if (!isHaveThisCalenCode) {
            Calendar calen = Calendar.getInstance();
            calen.setTime(dateTimeNow);
            if (calen.get(Calendar.MONTH) == 0 && calen.get(Calendar.DAY_OF_MONTH) == 1) {
                ComboData code = new ComboData();
                code.setId(calen.get(Calendar.YEAR) - 1 + StringConst.EMPTY);
                code.setText(calen.get(Calendar.YEAR) - 1 + StringConst.EMPTY);
                result.add(code);
            } else {
                ComboData code = new ComboData();
                code.setId(calen.get(Calendar.YEAR) + StringConst.EMPTY);
                code.setText(calen.get(Calendar.YEAR) + StringConst.EMPTY);
                result.add(code);
            }

        } else {
            BaseParam baseParam = new BaseParam();
            baseParam.setFilter(CALENDAR_CODE_PAGE, calendarCode);
            baseParam.setFilter(OFFICE_ID, StringUtil.toInteger(officeId));
            PageResult<TnmCalendarDetail> partsResult = super.getAllList("getCombYear", baseParam);
            List<TnmCalendarDetail> listyear = partsResult.getDatas();
            if (listyear != null && listyear.size() > 0) {
                for (TnmCalendarDetail item : listyear) {
                    ComboData code = new ComboData();
                    code.setId(item.getCalendarYear() + StringConst.EMPTY);
                    code.setText(item.getCalendarYear() + StringConst.EMPTY);
                    result.add(code);
                }
            }
        }

        return result;
    }

    /**
     * get the year Calendar
     * 
     * @param ttcOfficeCode String
     * @param party Integer
     * @param calendarCodeRawValue String
     * @param calendarRawValue String
     * @param year int calendarId
     * @return List<ComboData>
     */
    public PageResult<CPMCMS01Entity> getYearCalendar(String ttcOfficeCode, Integer party, String calendarCodeRawValue,
        String calendarRawValue, int year) {
        PageResult<CPMCMS01Entity> ret = null;
        boolean isHaveThisCalenCode = false;
        String calendarCode = StringConst.EMPTY;
        TnmCalendarMaster conditon = new TnmCalendarMaster();
        if (null != StringUtil.toInteger(ttcOfficeCode)) {
            if (party != IntDef.INT_SEVEN) {
                conditon.setOfficeId(StringUtil.toInteger(ttcOfficeCode));
            }
        }
        conditon.setCalendarCode(calendarCodeRawValue);
        List<TnmCalendarMaster> list = baseDao.select(conditon);
        if (list != null && list.size() > 0) {
            for (TnmCalendarMaster tcm : list) {
                isHaveThisCalenCode = true;
                calendarCode = tcm.getCalendarCode();
                break;
            }
        }

        if (!isHaveThisCalenCode) {
            ret = new PageResult<CPMCMS01Entity>();

        } else {
            BaseParam baseParam = new BaseParam();
            baseParam.setFilter(CALENDAR_CODE_PAGE, calendarCode);
            baseParam.setFilter(YEAR_PAGE, year);
            // Working Flag: 0 : (non-working day) ; (1 : working day)
            baseParam.setFilter(WORKING_FLAG, 0);
            baseParam.setFilter(OFFICE_ID, StringUtil.toInteger(ttcOfficeCode));

            ret = super.getAllList("getYearCalendar", baseParam);
            if (ret != null && ret.getDatas() != null && ret.getDatas().size() != 0) {
                for (CPMCMS01Entity item : ret.getDatas()) {
                    item.setCalendarDate(DateTimeUtil.parseDate(DateTimeUtil.formatDate(
                        DateTimeUtil.FORMAT_DATE_YYYYMMDD, item.getCalendarDate())));
                    item.setDisCalendarDate(DateTimeUtil.formatDate(DateTimeUtil.FORMAT_DATE_YYYYMMDD,
                        item.getCalendarDate()));
                }
            }
        }

        return ret;
    }

    /**
     * 
     * @param param the paramter from page
     * @param nonWorkingDays param the paramter from page
     * @return PageResult<CPMCMS01Entity>
     * @throws ParseException Exception
     */
    public PageResult<CPMCMS01Entity> doSaveYearCalendar(ObjectParam<CPMCMS01Entity> param, List<String> nonWorkingDays)
        throws ParseException {
        Timestamp timestamp = super.getDBDateTimeByDefaultTimezone();

        PageResult<CPMCMS01Entity> result = new PageResult<CPMCMS01Entity>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int currentYear = DateTimeUtil.getYear(calendar.getTime());
        int screenYear = Integer.valueOf((String) param.getSwapData().get("year"));

        // calendar.clear();
        // calendar.set(screenYear, 0, 1);
        int maxDays = new GregorianCalendar().isLeapYear(screenYear) ? INTEGER_366 : INTEGER_365;
        List<TnmCalendarDetail> listDetails = new ArrayList<TnmCalendarDetail>(maxDays);
        String yyyyMMdd = null;
        Integer calendarIdReal = 0;
        CPMCMS01Entity tnmCalendarmst = null;
        // get countryCode
        /*
         * String regionCode = "";
         * String hql2 = "FROM TNM_REGION WHERE REGION_ID = ? ";
         * Object[] param2 = new Object[] { (String) param.getSwapData().get(COUNTRY_PAGE) };
         * List<TnmRegion> reglist = baseDao.select(hql2, param2);
         * if (reglist != null && reglist.size() > 0) {
         * for (TnmRegion tr : reglist) {
         * regionCode = tr.getRegionCode();
         * break;
         * }
         * }
         */

        boolean isHaveThisCalenCode = false;
        boolean isHaveThisCalenCodeInMaster = false;
        String calendarId = (String) param.getSwapData().get(CALENDAR_CODE_PAGE);
        CPMCMS01Entity entity = new CPMCMS01Entity();

        // entity.setCountry(regionCode);
        entity.setTtcOfficeCode((String) param.getSwapData().get(OFFICE_CODE_PAGE));

        List<CPMCMS01Entity> entityList = null;
        List<CPMCMS01Entity> masterEntityList = null;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher mt = pattern.matcher(calendarId);

        if (null != StringUtil.toInteger(calendarId)) {
            entity.setCalendarId(Integer.parseInt(calendarId));
        }
        entity.setCalendarCode(param.getData().getCalendarCodeRawValue());
        entity.setParty((String) param.getSwapData().get("party"));
        entity.setPartyCode((String) param.getSwapData().get("partyCode"));
        param.getSwapData().put("entity", entity);
        entityList = baseMapper.select(getSqlId("getCalendarInfo"), param);
        masterEntityList = baseMapper.select(getSqlId("getCalendarInfoInMaster"), param);
        if (entityList != null && entityList.size() > 0) {
            for (CPMCMS01Entity tcm : entityList) {
                tnmCalendarmst = tcm;
                isHaveThisCalenCode = true;
                break;
            }
        }
        if (masterEntityList != null && masterEntityList.size() > 0) {
            isHaveThisCalenCodeInMaster = true;
        }

        // When Calendar code is not exist in TNM_CALENDAR_MASTER
        if (!isHaveThisCalenCodeInMaster) {
            // Insert table TNM_CALENDAR_MASTER
            TnmCalendarMaster calendarMasterInsert = new TnmCalendarMaster();
            calendarMasterInsert.setCalendarCode((String) param.getSwapData().get("calendarCode"));
            calendarMasterInsert.setRegionCode(null);
            calendarMasterInsert.setOfficeId(Integer.parseInt((String) param.getSwapData().get("ttcOfficeCode")));
            calendarMasterInsert.setInactiveFlag(IntDef.INT_ZERO);
            calendarMasterInsert.setCreatedBy(param.getLoginUserId());
            calendarMasterInsert.setCreatedDate(timestamp);
            calendarMasterInsert.setUpdatedBy(param.getLoginUserId());
            calendarMasterInsert.setUpdatedDate(timestamp);
            calendarMasterInsert.setVersion(IntDef.INT_ONE);
            baseDao.insert(calendarMasterInsert);
            baseDao.flush();
        }

        // 2-9-6-3. When Calendar code is not exist in DB, Insert new Calendar into table.
        if (!isHaveThisCalenCode) {
            // Insert table TNM_CALENDAR_PARTY
            TnmCalendarMaster conditon = new TnmCalendarMaster();
            if (null != StringUtil.toInteger(calendarId)) {
                conditon.setCalendarId(StringUtil.toInteger(calendarId));
            }
            if (null != StringUtil.toInteger((String) param.getSwapData().get("ttcOfficeCode"))) {
                conditon.setOfficeId(StringUtil.toInteger((String) param.getSwapData().get("ttcOfficeCode")));
            }
            conditon.setCalendarCode(param.getData().getCalendarCodeRawValue());

            List<TnmCalendarMaster> list = baseDao.select(conditon);

            // get calendaridreal
            calendarIdReal = list.get(0).getCalendarId();

            CalendarPartyEntity intoParty = new CalendarPartyEntity();
            intoParty.setCalendarId(calendarIdReal);
            // String party = (String) param.getSwapData().get(PARTY_PAGE);
            Integer party = StringUtil.toInteger(param.getSwapData().get(PARTY_PAGE));
            // intoParty.setPartyType(Integer.parseInt(party));
            intoParty.setPartyType(party);
            int officeId = Integer.parseInt((String) param.getSwapData().get("ttcOfficeCode"));
            intoParty.setOfficeId(officeId);
            String partyCodeString = (String) param.getSwapData().get(PARTY_CODE_PAGE);
            if (!StringUtil.isEmpty(partyCodeString)) {
                String[] partys = partyCodeString.split(StringConst.COMMA);
                for (String partyCodeOne : partys) {
                    // Delete from table TNM_CALENDAR_PARTY
                    CalendarPartyEntity isRealted = new CalendarPartyEntity();
                    isRealted.setCalendarId(calendarIdReal);
                    Integer partyType = StringUtil.toInteger(param.getSwapData().get(PARTY_PAGE));
                    isRealted.setPartyType(partyType);
                    param.getSwapData().put(PARTY_ENTITY, isRealted);
                    param.getSwapData().put(PARTY_CODE_ONE, partyCodeOne);
                    List<CalendarPartyEntity> isRealtedList = baseMapper.select(getSqlId("isRelated"), param);
                    if (isRealtedList.size() != 0) {
                        CalendarPartyEntity calendarParty = new CalendarPartyEntity();
                        for (CalendarPartyEntity calendarPartyEntity : isRealtedList) {
                            calendarParty.setCalendarPartyId(calendarPartyEntity.getCalendarPartyId());
                            param.getSwapData().put(CALENDAR_PARTY_OTHER, calendarParty);
                            baseMapper.delete(getSqlId("deleteParty"), param);
                        }
                    }

                    // insert into tableTNM_CALENDAR_PARTY
                    Integer partyCode = 0;
                    if (partyCodeOne != null && partyCodeOne != StringConst.EMPTY) {
                        partyCode = Integer.parseInt(partyCodeOne);
                    }
                    // if (CodeConst.CalendarParty.TTC_IMPORT_OFFICE == party.intValue()) {
                    // intoParty.setOfficeId(partyCode);
                    // }
                    if (CodeConst.CalendarParty.SUPPLIER == party.intValue()) {
                        intoParty.setSupplierId(partyCode);
                    }
                    if (CodeConst.CalendarParty.CUSTOMER == party.intValue()) {
                        intoParty.setCustomerId(partyCode);
                    }
                    if (CodeConst.CalendarParty.TTC_IMP_WAREHOUSE == party.intValue()) {
                        intoParty.setWhsId(partyCode);
                    }
                    intoParty.setCreatedBy(param.getLoginUserId());
                    intoParty.setCreatedDate(timestamp);
                    intoParty.setUpdatedBy(param.getLoginUserId());
                    intoParty.setUpdatedDate(timestamp);
                    intoParty.setVersion(IntDef.INT_ONE);
                    param.getSwapData().put(CALENDAR_PARTY, intoParty);
                    baseMapper.insert(getSqlId("addCalendarParty"), param);
                }
            } else {
                // 2.Imp Sea Port Customs, 5.Imp Airport Customs, 6.TTC Import Office

                List<CalendarPartyEntity> isRealtedList = baseMapper.select(getSqlId("isRelated"), param);
                if (isRealtedList.size() != 0) {
                    CalendarPartyEntity calendarParty = new CalendarPartyEntity();
                    for (CalendarPartyEntity calendarPartyEntity : isRealtedList) {
                        calendarParty.setCalendarPartyId(calendarPartyEntity.getCalendarPartyId());
                        param.getSwapData().put(CALENDAR_PARTY_OTHER, calendarParty);
                        baseMapper.delete(getSqlId("deleteParty"), param);
                    }
                }

                intoParty.setCreatedBy(param.getLoginUserId());
                intoParty.setCreatedDate(timestamp);
                intoParty.setUpdatedBy(param.getLoginUserId());
                intoParty.setUpdatedDate(timestamp);
                intoParty.setVersion(IntDef.INT_ONE);
                param.getSwapData().put(CALENDAR_PARTY, intoParty);
                baseMapper.insert(getSqlId("addCalendarParty"), param);
            }

            // Insert table TNM_CALENDAR_DETAIL
            TnmCalendarMaster masterForDetail = new TnmCalendarMaster();
            for (int i = 1; i <= maxDays; i++) {
                masterForDetail.setCalendarId(list.get(0).getCalendarId());
                calendar.set(Integer.valueOf((String) param.getSwapData().get(YEAR_PAGE)), 0, i);
                yyyyMMdd = dateFormat.format(calendar.getTime());

                TnmCalendarDetail tcd = new TnmCalendarDetail();
                tcd.setTnmCalendarMaster(masterForDetail);
                tcd.setCalendarYear(String.valueOf((String) param.getSwapData().get(YEAR_PAGE)));
                tcd.setCalendarDate(dateFormat.parse(yyyyMMdd));
                if (nonWorkingDays.contains(yyyyMMdd)) {
                    tcd.setWorkingFlag(0);
                } else {
                    tcd.setWorkingFlag(1);
                }
                listDetails.add(tcd);
            }
            doSaveYearCalendar(listDetails, param.getLoginUserId());

            if (party == IntDef.INT_ONE) {
                if (currentYear <= screenYear) {
                    if (!StringUtil.isEmpty(partyCodeString)) {
                        calendar.set(Integer.valueOf((String) param.getSwapData().get(YEAR_PAGE)), 0, 1);
                        Date firstDate = DateTimeUtil.firstDay(DateTimeUtil.getFirstMonthOfYear(calendar.getTime()));
                        Date lastDate = DateTimeUtil.lastDay(DateTimeUtil.getLastMonthOfYear(calendar.getTime()));

                        doShippingRoute(calendarIdReal, firstDate, lastDate, officeId, param.getLoginUserId());

                    }
                }
            }

        } else {
            isHaveThisCalenCode = false;
            List<CPMCMS01Entity> entityListFromYear = null;
            entity.setYear((String) param.getSwapData().get(YEAR_PAGE));
            if (mt.matches()) {
                entity.setCalendarId(Integer.parseInt(calendarId));
                entity.setParty((String) param.getSwapData().get("party"));
                entity.setPartyCode((String) param.getSwapData().get("partyCode"));
                param.getSwapData().put(ENTITY, entity);
                entityListFromYear = baseMapper.select(getSqlId("getCalendarInfo"), param);
                if (entityListFromYear != null && entityListFromYear.size() > 0) {
                    for (CPMCMS01Entity tcm : entityListFromYear) {
                        tnmCalendarmst = tcm;
                        isHaveThisCalenCode = true;
                        break;
                    }
                }
            } else {
                entity.setCalendarCode(calendarId);
                entity.setParty((String) param.getSwapData().get("party"));
                entity.setPartyCode((String) param.getSwapData().get("partyCode"));
                param.getSwapData().put(ENTITY, entity);
                entityListFromYear = baseMapper.select(getSqlId("getCalendarInfo"), param);
                if (entityListFromYear != null && entityListFromYear.size() > 0) {
                    for (CPMCMS01Entity tcm : entityListFromYear) {
                        tnmCalendarmst = tcm;
                        isHaveThisCalenCode = true;
                        break;
                    }
                }
            }
            // get calendarid real
            calendarIdReal = tnmCalendarmst.getCalendarId();
            TnmCalendarMaster tnmCalendarMaster = new TnmCalendarMaster();

            // 2-9-6-2. When displayed Calender code has exist in DB, but displayed Year has not yet exist in DB,
            // insert the displayed Calendar into table.
            if (!isHaveThisCalenCode) {
                for (int i = 1; i <= maxDays; i++) {
                    tnmCalendarMaster.setCalendarId(tnmCalendarmst.getCalendarId());
                    calendar.set(Integer.valueOf((String) param.getSwapData().get(YEAR_PAGE)), 0, i);
                    yyyyMMdd = dateFormat.format(calendar.getTime());

                    TnmCalendarDetail tcd = new TnmCalendarDetail();
                    tcd.setTnmCalendarMaster(tnmCalendarMaster);
                    tcd.setCalendarYear(String.valueOf((String) param.getSwapData().get(YEAR_PAGE)));
                    tcd.setCalendarDate(dateFormat.parse(yyyyMMdd));
                    if (nonWorkingDays.contains(yyyyMMdd)) {
                        tcd.setWorkingFlag(0);
                    } else {
                        tcd.setWorkingFlag(1);
                    }
                    listDetails.add(tcd);
                }
                doSaveYearCalendar(listDetails, param.getLoginUserId());
            } else {
                // 2-9-6-1. When displayed Calendar Code, displayed Year are exist in DB, update the displayed
                // calendar.

                for (int i = 1; i <= maxDays; i++) {
                    tnmCalendarMaster.setCalendarId(tnmCalendarmst.getCalendarId());
                    calendar.set(Integer.valueOf((String) param.getSwapData().get(YEAR_PAGE)), 0, i);
                    yyyyMMdd = dateFormat.format(calendar.getTime());

                    TnmCalendarDetail tcd = new TnmCalendarDetail();
                    tcd.setCalendarDetailId(entityListFromYear.get(i - 1).getCalendarDetailId());

                    tcd.setTnmCalendarMaster(tnmCalendarMaster);
                    tcd.setCalendarYear(String.valueOf((String) param.getSwapData().get(YEAR_PAGE)));
                    tcd.setCalendarDate(dateFormat.parse(yyyyMMdd));
                    if (nonWorkingDays.contains(yyyyMMdd)) {
                        tcd.setWorkingFlag(0);
                    } else {
                        tcd.setWorkingFlag(1);
                    }
                    listDetails.add(tcd);
                }
                doSaveYearCalendar(listDetails, param.getLoginUserId());
            }
            if (!StringUtil.isEmpty((String) param.getSwapData().get(PARTY_CODE_PAGE))) {
                // 2-9-6-4. When Calendar code and Year is already exist in DB, but selected party and party code
                // not
                // related with calender code, Insert new mapping into table.
                String partyCodes = (String) param.getSwapData().get(PARTY_CODE_PAGE);
                String[] partys = partyCodes.split(StringConst.COMMA);
                Integer party = StringUtil.toInteger(param.getSwapData().get(PARTY_PAGE));
                for (String partyOne : partys) {

                    CalendarPartyEntity isRealted = new CalendarPartyEntity();
                    isRealted.setCalendarId(calendarIdReal);

                    // String party = (String) param.getSwapData().get(PARTY_PAGE);
                    isRealted.setPartyType(party);
                    param.getSwapData().put(PARTY_ENTITY, isRealted);
                    param.getSwapData().put(PARTY_CODE_ONE, partyOne);
                    List<CalendarPartyEntity> isRealtedList = baseMapper.select(getSqlId("isRelated"), param);
                    /*
                     * // Update table TNM_CALENDAR_MASTER common opration
                     * TnmCalendarMaster masterOld = baseDao.findOne(TnmCalendarMaster.class, calendarIdReal);
                     * masterOld.setRegionCode(regionCode);
                     * masterOld.setUpdatedBy(param.getLoginUserId());
                     * masterOld.setUpdatedDate(timestamp);
                     * masterOld.setVersion(masterOld.getVersion() + 1);
                     * baseDao.update(masterOld);
                     */
                    if (isRealtedList.size() != 0) {
                        // Delete from table TNM_CALENDAR_PARTY
                        CalendarPartyEntity calendarParty = new CalendarPartyEntity();
                        for (CalendarPartyEntity calendarPartyEntity : isRealtedList) {
                            calendarParty.setCalendarPartyId(calendarPartyEntity.getCalendarPartyId());
                            param.getSwapData().put(CALENDAR_PARTY_OTHER, calendarParty);
                            baseMapper.delete(getSqlId("deleteParty"), param);
                        }
                    }
                    // Insert table TNM_CALENDAR_PARTY
                    CalendarPartyEntity intoParty = new CalendarPartyEntity();
                    intoParty.setCalendarId(calendarIdReal);
                    intoParty.setPartyType(party);
                    Integer partyCode = Integer.parseInt(partyOne);

                    int officeId = Integer.parseInt((String) param.getSwapData().get("ttcOfficeCode"));
                    intoParty.setOfficeId(officeId);
                    // if (party != null && CodeConst.CalendarParty.TTC_IMPORT_OFFICE == party.intValue()) {
                    // intoParty.setOfficeId(partyCode);
                    // }
                    if (party != null && CodeConst.CalendarParty.SUPPLIER == party.intValue()) {
                        intoParty.setSupplierId(partyCode);
                    }
                    if (party != null && CodeConst.CalendarParty.CUSTOMER == party.intValue()) {
                        intoParty.setCustomerId(partyCode);
                    }
                    if (party != null
                            && (CodeConst.CalendarParty.TTC_IMP_WAREHOUSE == party.intValue() || CodeConst.CalendarParty.EXP_WAREHOUSE == party
                                .intValue())) {
                        intoParty.setWhsId(partyCode);
                    }
                    intoParty.setCreatedBy(param.getLoginUserId());
                    intoParty.setCreatedDate(timestamp);
                    intoParty.setUpdatedBy(param.getLoginUserId());
                    intoParty.setUpdatedDate(timestamp);
                    intoParty.setVersion(IntDef.INT_ONE);
                    param.getSwapData().put(CALENDAR_PARTY, intoParty);
                    baseMapper.insert(getSqlId("addCalendarParty"), param);
                }

                if (party == IntDef.INT_ONE) {
                    if (currentYear <= screenYear) {
                        calendar.set(Integer.valueOf((String) param.getSwapData().get(YEAR_PAGE)), 0, 1);
                        Date firstDate = DateTimeUtil.firstDay(DateTimeUtil.getFirstMonthOfYear(calendar.getTime()));
                        Date lastDate = DateTimeUtil.lastDay(DateTimeUtil.getLastMonthOfYear(calendar.getTime()));

                        int officeId = Integer.parseInt((String) param.getSwapData().get("ttcOfficeCode"));
                        doShippingRoute(calendarIdReal, firstDate, lastDate, officeId, param.getLoginUserId());
                    }
                }
            }

        }

        return result;
    }

    /**
     * get regionCode by officeid
     * 
     * @param param the paramter from page
     * @return String
     */
    public String getRegionCode(ObjectParam<CPMCMS01Entity> param) {
        String regionCode = "";
        String hql2 = "FROM TNM_REGION WHERE REGION_ID = ? ";
        Object[] param2 = new Object[] { param.getData().getCountry() };
        List<TnmRegion> reglist = baseDao.select(hql2, param2);
        if (reglist != null && reglist.size() > 0) {
            for (TnmRegion tr : reglist) {
                regionCode = tr.getRegionCode();
                return regionCode;
            }
        }
        return regionCode;
    }

    /**
     * get officeCode by officeid
     * 
     * @param param the paramter from page
     * @return String
     */
    public String getOfficeCode(ObjectParam<CPMCMS01Entity> param) {
        TnmOffice office = this.baseDao.findOne(TnmOffice.class, Integer.parseInt(param.getData().getTtcOfficeCode()));
        return office.getOfficeCode();
    }

    /**
     * Save the year Calendar
     * 
     * @param listDetails String listDetails
     * @param loginUserId int loginUserId
     * 
     */
    public void doSaveYearCalendar(List<TnmCalendarDetail> listDetails, int loginUserId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp timestamp = super.getDBDateTimeByDefaultTimezone();

        if (listDetails != null && listDetails.size() > 0) {
            for (TnmCalendarDetail item : listDetails) {
                TnmCalendarDetail ret = isHaveDetail(item.getTnmCalendarMaster().getCalendarId(),
                    dateFormat.format(item.getCalendarDate()));
                if (ret == null) {
                    item.setCreatedBy(loginUserId);
                    item.setCreatedDate(timestamp);
                    item.setUpdatedBy(loginUserId);
                    item.setUpdatedDate(timestamp);
                    item.setVersion(IntDef.INT_ONE);
                    baseDao.insert(item);
                } else {
                    if (ret.getWorkingFlag().compareTo(item.getWorkingFlag()) != 0) {
                        ret.setWorkingFlag(item.getWorkingFlag());
                        ret.setUpdatedBy(loginUserId);
                        ret.setUpdatedDate(timestamp);
                        ret.setVersion(ret.getVersion() + 1);
                        baseDao.update(ret);
                    }
                }

            }
            // flush
            baseDao.flush();
        }
    }

    /**
     * delete the year Calendar
     * 
     * @param calendarId String calendarId
     * @param year int calendarId
     */
    public void doDeleteYearCalendar(String calendarId, int year) {
        String hql = "FROM TNM_CALENDAR_DETAIL WHERE CALENDAR_ID = ? and CALENDAR_YEAR = ? and WORKING_FLAG = 0";
        Object[] param = new Object[] { calendarId, year };
        List<TnmCalendarDetail> list = baseDao.select(hql, param);
        if (list != null && list.size() > 0) {
            for (TnmCalendarDetail tcd : list) {
                baseDao.delete(tcd);
            }
            baseDao.flush();
        }
    }

    /**
     * delete the year Calendar
     * 
     * @param calendarId String calendarId
     * @param date int date
     * @return TnmCalendarDetail
     */
    public TnmCalendarDetail isHaveDetail(int calendarId, String date) {
        BaseParam baseParam = new BaseParam();
        baseParam.getSwapData().put(CALENDAR_ID, calendarId);
        baseParam.getSwapData().put(DATE, date);
        TnmCalendarMaster master = new TnmCalendarMaster();
        master.setCalendarId(calendarId);
        List<TnmCalendarDetail> list = baseMapper.select(getSqlId("getCalDetial"), baseParam);
        if (list != null && list.size() > 0) {
            for (TnmCalendarDetail tcd : list) {
                tcd.setTnmCalendarMaster(master);
                return tcd;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * If Country selected is not related with selected Supplier's region.
     * 
     * @param param param from page
     * @return boolean boolean
     */
    public boolean isRelatedWithSupplier(ObjectParam<CPMCMS01Entity> param) {
        String[] partys = param.getData().getPartyCode().split(StringConst.COMMA);

        for (String partyCode : partys) {
            param.getSwapData().put(PARTY_CODE_ONE, partyCode);
            CPMCMS01Entity entity = this.baseMapper.findOne(getSqlId("getSupplierRegion"), param);
            String regionCode = (String) param.getSwapData().get(REGION_CODE);
            if (!regionCode.equals(entity.getCountry())) {
                return false;
            }
        }
        return true;
    }

    /**
     * If Country selected is not related with selected TTC Import Office's region.
     * 
     * @param param param from page
     * @return boolean boolean
     */
    public boolean isRelatedWithOffice(ObjectParam<CPMCMS01Entity> param) {
        String[] partys = param.getData().getPartyCode().split(StringConst.COMMA);

        for (String partyCode : partys) {
            String hql = "FROM TNM_OFFICE WHERE OFFICE_ID = ?";
            Object[] objectParam = new Object[] { partyCode };
            TnmOffice office = baseDao.findOne(hql, objectParam);
            String regionCode = (String) param.getSwapData().get(REGION_CODE);
            if (!regionCode.equals(office.getRegionCode())) {
                return false;
            }
        }
        return true;
    }

    /**
     * If Country selected is not related with selected TTC Imp Warehouse's region.
     * 
     * @param param param from page
     * @return boolean boolean
     */
    public boolean isRelatedWithWareHouse(ObjectParam<CPMCMS01Entity> param) {

        String[] partys = param.getData().getPartyCode().split(StringConst.COMMA);

        for (String partyCode : partys) {
            param.getSwapData().put(PARTY_CODE_ONE, partyCode);
            CPMCMS01Entity entity = this.baseMapper.findOne(getSqlId("getWareHouseRegion"), param);
            String regionCode = (String) param.getSwapData().get(REGION_CODE);
            if (!regionCode.equals(entity.getCountry())) {
                return false;
            }
        }
        return true;
    }

    /**
     * If Country selected is not related with selected TTC Imp Warehouse's region.
     * 
     * @param param param from page
     * @return boolean boolean
     */
    public boolean isRelatedWithCustomer(ObjectParam<CPMCMS01Entity> param) {
        String[] partys = param.getData().getPartyCode().split(StringConst.COMMA);

        for (String partyCode : partys) {
            param.getSwapData().put(PARTY_CODE_ONE, partyCode);

            CPMCMS01Entity entity = this.baseMapper.findOne(getSqlId("getCustomerRegion"), param);
            String regionCode = (String) param.getSwapData().get(REGION_CODE);
            if (!regionCode.equals(entity.getCountry())) {
                return false;
            }
        }
        return true;
    }

    /**
     * If Party selected is Supplier and selected supplier code belongs to more then one office.(c1018 {1}：Supplier code
     * which belongs to more then one office {2}：Office)
     * 
     * @param param param from page
     * @return List<CPMCMS01Entity>
     */
    public List<CPMCMS01Entity> getOffices(ObjectParam<CPMCMS01Entity> param) {
        return this.baseMapper.select(getSqlId("getOffices"), param);
    }

    /**
     * If Calendar Code inputed existing in Calender Master but select Country is not related with calendar code's
     * country.(w1004_069 {1}：Calendar Code {2}：Country)
     * 
     * @param param param from page
     * @return List<CPMCMS01Entity>
     */
    public List<CPMCMS01Entity> checkCalCodeAndCounty(BaseParam param) {
        return this.baseMapper.select(getSqlId("checkCalCodeAndCounty"), param);
    }

    /**
     * shipping route
     * 
     * @param calendarId calendar Id
     * @param firstDate first day of year
     * @param lastDate last day of year
     * @param officeId officeId
     * @param userId userId
     */
    public void doShippingRoute(Integer calendarId, Date firstDate, Date lastDate, Integer officeId, Integer userId) {

        BaseParam param = new BaseParam();
        param.setSwapData("FistDate", DateTimeUtil.formatDate(firstDate));
        param.setSwapData("LastDate", DateTimeUtil.formatDate(lastDate));
        param.setSwapData("OfficeId", officeId);
        param.setSwapData("CalendarId", calendarId);
        List<AisinCommonEntity> modList = baseMapper.select(getSqlId("getSrMasterInfo"), param);
        if (modList != null && modList.size() > 0) {
            List<Integer> yearList = new ArrayList<Integer>();
            Integer minYear = DateTimeUtil.getYear(modList.get(0).getFirstEtd()) - 1;
            Integer curYear = DateTimeUtil.getYear(firstDate);
            for (int i = minYear; i <= curYear; i++) {
                yearList.add(i);
            }
            param.setSwapData("dataAllList", modList);
            param.setSwapData("yearList", yearList);
            // get supplier laster calendar info
            List<CalendarDateEntity> lastYearcalendarDateList = baseMapper.select(
                this.getSqlId("getLastYearcalendarDateList"), param);
            Map<String, List<Date>> cdateMaps = new HashMap<String, List<Date>>();
            for (CalendarDateEntity cde : lastYearcalendarDateList) {
                Date calendarDate = cde.getCalendarDate();
                Integer supplierId = cde.getSupplierId();
                Integer office = cde.getOfficeId();
                String key = office + StringConst.COMMA + supplierId;
                List<Date> dateList = cdateMaps.get(key);
                if (dateList == null) {
                    dateList = new ArrayList<Date>();
                    cdateMaps.put(key, dateList);
                }
                dateList.add(calendarDate);
            }

            List<AisinCommonEntity> shModList = new ArrayList<AisinCommonEntity>();
            List<AisinCommonEntity> tjModList = new ArrayList<AisinCommonEntity>();
            for (AisinCommonEntity shippingEntity : modList) {
                if (ShippingRouteType.AISIN_TTTJ == shippingEntity.getShippingRouteType()) {
                    tjModList.add(shippingEntity);
                } else {
                    shModList.add(shippingEntity);
                }
            }

            if (tjModList.size() > 0) {
                dealShippingRouteDetail(tjModList, ShippingRouteType.AISIN_TTTJ, cdateMaps, userId);
            }
            if (shModList.size() > 0) {
                dealShippingRouteDetail(shModList, ShippingRouteType.AISIN_TTSH, cdateMaps, userId);
            }
        }
    }

    /**
     * Deal shipping route detail
     * 
     * @param modList mod list
     * @param type type
     * @param cdateMaps date map
     * @param userId user Id
     */
    @SuppressWarnings("unchecked")
    private void dealShippingRouteDetail(List<AisinCommonEntity> modList, Integer type,
        Map<String, List<Date>> cdateMaps, Integer userId) {

        Map<String, Object> maps = new HashMap<String, Object>();
        Map<String, String> msgCalMaps = new HashMap<String, String>();
        Map<String, String> msgVanMaps = new HashMap<String, String>();
        if (ShippingRouteType.AISIN_TTTJ == type) {
            maps = aisinCommonService.dealTJModList(modList, cdateMaps);
        } else {
            maps = aisinCommonService.dealSHModList(modList, cdateMaps);
        }
        msgCalMaps = (Map<String, String>) maps.get("msgCalMaps");
        msgVanMaps = (Map<String, String>) maps.get("msgVanMaps");
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        if (null != msgCalMaps && !msgCalMaps.isEmpty()) {
            for (Map.Entry<String, String> entry : msgCalMaps.entrySet()) {
                String key = entry.getKey();
                String[] idCode = key.split(StringConst.COMMA);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_143);
                message.setMessageArgs(new String[] { idCode[0], idCode[1] });
                messageLists.add(message);
            }
        }
        if (null != msgVanMaps && !msgVanMaps.isEmpty()) {
            for (Map.Entry<String, String> entry : msgVanMaps.entrySet()) {
                String key = entry.getKey();
                String[] idCode = key.split(StringConst.COMMA);
                BaseMessage message = new BaseMessage(MessageCodeConst.W1004_178);
                message.setMessageArgs(new String[] { idCode[0], idCode[1] });
                messageLists.add(message);
            }
        }
        if (messageLists != null && messageLists.size() > 0) {
            throw new BusinessException(messageLists);
        }

        List<AisinCommonEntity> modChangeDetailList = (List<AisinCommonEntity>) maps.get("modChangeDetailList");
        if (modChangeDetailList != null && modChangeDetailList.size() > 0) {
            // delete TNM_SR_DETAIL
            BaseParam param = new BaseParam();
            param.setSwapData("dataLists", modList);
            param.setSwapData("type", type);
            this.baseMapper.delete(this.getSqlId("deleteDataList"), param);

            // insert TNM_SR_DETAIL
            Timestamp dbTime = super.getDBDateTimeByDefaultTimezone();
            for (AisinCommonEntity ce : modChangeDetailList) {
                ce.setCreatedBy(userId);
                ce.setCreatedDate(dbTime);
                ce.setUpdatedBy(userId);
                ce.setUpdatedDate(dbTime);
                this.baseMapper.insert(this.getSqlId("insertDataList"), ce);
            }
        }
    }

}
