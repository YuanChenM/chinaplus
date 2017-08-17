/**
 * CPMCMS01Controller.java
 * 
 * @screen CPMCMS01
 * @author shi_yuxi
 */
package com.chinaplus.web.mm.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaplus.common.bean.BusinessPattern;
import com.chinaplus.common.bean.ComboData;
import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.bean.UserOffice;
import com.chinaplus.common.consts.ChinaPlusConst.UploadConst;
import com.chinaplus.common.consts.CodeConst;
import com.chinaplus.common.control.MasterDataController;
import com.chinaplus.common.util.SessionInfoManager;
import com.chinaplus.common.util.UserManager;
import com.chinaplus.core.base.BaseController;
import com.chinaplus.core.bean.BaseMessage;
import com.chinaplus.core.bean.BaseParam;
import com.chinaplus.core.bean.BaseResult;
import com.chinaplus.core.bean.ObjectParam;
import com.chinaplus.core.bean.PageParam;
import com.chinaplus.core.bean.PageResult;
import com.chinaplus.core.consts.MessageCodeConst;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.exception.BusinessException;
import com.chinaplus.core.util.DateTimeUtil;
import com.chinaplus.core.util.StringUtil;
import com.chinaplus.web.mm.entity.CPMCMS01Entity;
import com.chinaplus.web.mm.service.CPMCMS01Service;
import com.chinaplus.web.mm.service.CPMPMS01Service;
import com.chinaplus.web.mm.service.CPMPMS02Service;

/**
 * calendar master
 * 
 */
@Controller
public class CPMCMS01Controller extends BaseController {
    /** session key */
    private static final String FILE_UPLOAD_SESSION_KEY = "sessionMapKey";
    /** PartyCode */
    private static final String PRITY_CODE = "CPMCMS01_Label_PartyCode";
    /** Supplier */
    private static final String UPPLIER = "CPMCMS01_Label_Supplier";
    /** TTCOfficeCode */
    private static final String OFFICE_CODE = "CPMCMS01_Label_TTCOfficeCode";
    /** CALENDAR_CODE */
    private static final String CALENDAR_CODE = "CPMCMS01_Label_CalendarCode";
    /** COUNTRY */
    private static final String COUNTRY = "CPMCMS01_Label_EXPIMPCountryCountry";
    /** COUNTRY_PAGE */
    private static final String COUNTRY_PAGE = "country";
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

    /** PARTY_CODE_PAGE */
    private static final String DATA = "data";
    /** service for CPMCMS01Service */
    @Autowired
    private CPMCMS01Service cpmcms01Service;
    /** cpmpms02Service */
    @Autowired
    private CPMPMS02Service cpmpms02Service;
    /** cpmpms01Service */
    @Autowired
    private CPMPMS01Service cpmpms01Service;
    @Autowired
    private MasterDataController masterDataController;
    
    /**
     * office in loginUserOffice
     * 
     * @param request the request
     * @param response the response
     * @param param BaseParam
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/master/CPMCMS01/getOfficeFlg",
            method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Boolean> getOfficeFlg(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        BaseResult<Boolean> result = new BaseResult<Boolean>();
        String office = (String) param.getSwapData().get("officeId");
        if(StringUtil.isNullOrEmpty(office)){
            result.setData(true);
        } else {
            UserInfo loginUser = getLoginUser(request);
            List<UserOffice> list = loginUser.getUserOffice();
            for (UserOffice entity : list) {
                if(String.valueOf(entity.getOfficeId()).equals(office)){
                    result.setData(true);
                    break;
                }
            }
            if(result.getData()== null){
                result.setData(false);
            }
        
        }
        
        return result;
    }
    
    
    /**
     * checkCustomer
     * 
     * @param request the request
     * @param response the response
     * @param param BaseParam
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "/master/CPMCMS01/checkCustomer",
            method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Boolean> checkCustomer(@RequestBody BaseParam param, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        BaseResult<Boolean> result = new BaseResult<Boolean>();

        result.setData(false);
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);
        List<BusinessPattern> bplist = um.getCurrentForAll();
        if(bplist.size() > 0){
            result.setData(true);
        }
        
        return result;
    }
    
    
    
    
    
    
    
    
    
    
    /**
     * Load Office Code By Id combo data.
     * 
     * @param request the request
     * @param response the response
     * @param params BaseParam
     * @return result
     * @throws Exception e
     */
    @RequestMapping(value = "master/CPMCMS01/loadOfficeCodeByIds",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadOfficeCodeByIds(HttpServletRequest request, HttpServletResponse response, @RequestBody BaseParam params)
        throws Exception {
        
        this.setCommonParam(params, request);
        PageResult<ComboData> result = new PageResult<ComboData>();
        String pageFrom = (String) params.getSwapData().get("pageFrom");
        if(StringUtil.isNullOrEmpty(pageFrom)){
            
            List<ComboData> list = cpmcms01Service.doOfficeCombo(params);
            result.setDatas(list);

        } else {
            // convert to combo data
            UserInfo loginUser = getLoginUser(request);
            List<UserOffice> offices = loginUser.getUserOffice();
            List<ComboData> comboOffices = new ArrayList<ComboData>();
            for (UserOffice office : offices) {
                ComboData data = new ComboData();
                data.setId(String.valueOf(office.getOfficeId()));
                data.setText(office.getOfficeCode());
                comboOffices.add(data);
            }
            result.setDatas(comboOffices);
        }
        return result;
    }

    /**
     * load country Combo
     * get calendar list from customer table
     * 
     * @param param PageParam
     * @return List<ComboData>
     */
    @RequestMapping(value = "/master/CPMCMS01/loadCountryCombo",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCountryCombo(@RequestBody BaseParam param) {
        PageResult<ComboData> result = new PageResult<ComboData>();
        List<ComboData> list = cpmcms01Service.doLoadCtryCombo(param);
        result.setDatas(list);
        return result;
    }

    /**
     * load Calendar Code Combo
     * 
     * @param param PageParam
     * @return List<ComboData>
     */
    @RequestMapping(value = "/master/CPMCMS01/loadCalendarCodeCombo",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadCalendarCodeCombo(@RequestBody PageParam param) {
        PageResult<ComboData> result = new PageResult<ComboData>();
        String party = (String) param.getSwapData().get("party");
        String pageFrom = (String) param.getSwapData().get("pageFrom");
        String officeId = (String) param.getSwapData().get("officeId");
        if(!StringUtil.isNullOrEmpty(party) && !"modify".equals(pageFrom)){
            List<ComboData> list = cpmcms01Service.doLoadCalendarCodeCombo(param);
            result.setDatas(list);
            return result;
        } else if(!StringUtil.isNullOrEmpty(party) && party.endsWith("7")){
            List<ComboData> list = cpmcms01Service.doLoadCalendarCodeCombo(param);
            result.setDatas(list);
            return result;
        } else if(!StringUtil.isNullOrEmpty(officeId) && "modify".equals(pageFrom)){
            List<ComboData> list = cpmcms01Service.doLoadCalendarCodeCombo(param);
            result.setDatas(list);
            return result;
        }
        return result;
        
        
    }
    
    /**
     * Get Party Code
     * 
     * @param request request
     * @param param param
     * @return result
     * @throws Exception e
     */
    @SuppressWarnings("null")
    @RequestMapping(value = "master/CPMCMS01/loadPartyCode",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadPartyCode(HttpServletRequest request, @RequestBody BaseParam param)
        throws Exception {
        PageResult<ComboData> result = new PageResult<ComboData>();
        UserInfo userInfo = getLoginUser(request);
        SessionInfoManager sm = SessionInfoManager.getContextInstance(request);
        UserManager um = UserManager.getLocalInstance(sm);

        List<Integer> userOffIds = cpmcms01Service.setUserOffIds(userInfo);
        String party = (String) param.getSwapData().get("party");
        String country = (String) param.getSwapData().get("country");
        String pageFrom = (String) param.getSwapData().get("pageFrom");
        String cmImpOfficeCode = (String) param.getSwapData().get("officeId");
        // convert to combo data
        List<ComboData> partyCode = null;
        if (userOffIds != null && userOffIds.size() > 0 && party != null) {
            if(!StringUtil.isNullOrEmpty(pageFrom) && "modify".equals(pageFrom) && party.equals("4")){
                //TODO 
                
                if(!StringUtil.isNullOrEmpty(cmImpOfficeCode)){
                    partyCode = cpmcms01Service.getPartyCodeByOffice(cmImpOfficeCode, userOffIds, userInfo.getUserId(), country);
                }       
                result.setDatas(partyCode);
            } else {
                partyCode = cpmcms01Service.getPartyCode(userInfo.getLanguage().getCode(), party, country, 
                    cmImpOfficeCode, userOffIds, um.getVVFlag(), um.getAisinFlag(), pageFrom);
                result.setDatas(partyCode);
            }
        }
        return result;
    }

    /**
     * load year Code Combo
     * 
     * @param param PageParam
     * @return List<ComboData>
     */
    @RequestMapping(value = "/master/CPMCMS01/loadYearCombo",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<ComboData> loadYearCombo(@RequestBody BaseParam param) {
        PageResult<ComboData> result = new PageResult<ComboData>();

        String calendarId = (String) param.getSwapData().get("calendarId");
        String calendarRawValue = (String) param.getSwapData().get("calendarRawValue");
        String today = (String) param.getSwapData().get("today");
        String officeId = (String) param.getSwapData().get("officeId");
        if (StringUtil.isNullOrEmpty(calendarId)){
            return null;
        }
//        if(!calendarId.matches("[0-9]+")){
//            return null;
//        }

        Date dateNow = DateTimeUtil.parseDate(today);
        List<ComboData> list = cpmcms01Service.doLoadyearCombo(calendarId, calendarRawValue,officeId,  dateNow);
        result.setDatas(list);
       
        return result;
    }

    /**
     * get Calendar of year
     *
     *
     * @param valuesEntity the valuesEntity
     * @return PageResult<CPMCMS01Entity>
     *
     */
    @RequestMapping(value = "/master/CPMCMS01/getYearCalendar",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMCMS01Entity> getYearCalendar(@RequestBody ObjectParam<CPMCMS01Entity> valuesEntity) {

        String country = valuesEntity.getData().getCountry();
        String calendarCodeRawValue = valuesEntity.getData().getCalendarCodeRawValue();
        String calendarCode = valuesEntity.getData().getCalendarCode();
        String year = valuesEntity.getData().getYear();
        
        //String party = valuesEntity.getData().getParty();
        Integer party = StringUtil.toInteger(valuesEntity.getData().getParty());
        String ttcOfficeCode = valuesEntity.getData().getTtcOfficeCode();
        //String partyCode = valuesEntity.getData().getPartyCode();
        //String partyCodeCombo = valuesEntity.getData().getPartyCodeCombo();
        // If Party selected is Supplier, TTC Import Office, TTC Imp Warehouse or Customer and Party Code is
        // blank.(w1003_001)


        if (StringUtil.isNumeric(year) && !StringUtil.isNullOrEmpty(calendarCode)
                && !StringUtil.isEmpty(StringUtil.toString(party)) && !StringUtil.isEmpty(ttcOfficeCode)) {
            PageResult<CPMCMS01Entity> ret = cpmcms01Service.getYearCalendar(ttcOfficeCode, party,calendarCodeRawValue ,calendarCode,
                Integer.valueOf(year));
            return ret;
        }

        return null;
    }

    /**
     *
     * save year calendar
     *
     * @param request the HttpServletRequest
     * @param valuesEntity the valuesEntity
     * @return PageResult<CPMCMS01Entity>
     * @throws ParseException 
     *
     */
    @RequestMapping(value = "master/CPMCMS01/saveYearCalendar",
        method = RequestMethod.POST)
    @ResponseBody
    public PageResult<CPMCMS01Entity> saveYearCalendar(HttpServletRequest request,
        @RequestBody ObjectParam<CPMCMS01Entity> valuesEntity) throws ParseException {
        PageResult<CPMCMS01Entity> ret = new PageResult<CPMCMS01Entity>();
        SessionInfoManager context = SessionInfoManager.getContextInstance(request);
        setCommonParam(valuesEntity, request);
        List<BaseMessage> messageLists = new ArrayList<BaseMessage>();
        CPMCMS01Entity en = new CPMCMS01Entity();
        String country = null;
        String calendarCode = null;
        String year = null;
        Integer party = null;
        String ttcOfficeCode = null;
        String partyCode = null;
        if(valuesEntity.getData() != null)
        {
            country = valuesEntity.getData().getCountry();
            calendarCode = valuesEntity.getData().getCalendarCode();
            year = valuesEntity.getData().getYear();
            party = StringUtil.toInteger(valuesEntity.getData().getParty());
            ttcOfficeCode = valuesEntity.getData().getTtcOfficeCode();
            partyCode = valuesEntity.getData().getPartyCode();
            // set value to session
            en.setCountry(country);
            en.setCalendarCode(calendarCode);
            en.setYear(year);
            en.setParty(StringUtil.toString(party));
            en.setPartyCode(partyCode);
            en.setTtcOfficeCode(ttcOfficeCode);
            en.setNonWorkingDays(valuesEntity.getData().getNonWorkingDays());
            
        }

        if (UploadConst.UPLOAD_PROCESS_CHECK.equals(valuesEntity.getUploadProcess())) {

            context.put(valuesEntity.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString(), en);

            // check calendar exist in master

            // If Calendar Code inputed existing in Calender Master but select Country is not related with calendar
            // code's country.(w1004_069 {1}：Calendar Code {2}：Country)
            CPMCMS01Entity check = new CPMCMS01Entity();
            CPMCMS01Entity checkOfficeCode = new CPMCMS01Entity();
            CPMCMS01Entity checkExist = new CPMCMS01Entity();
            String regionCode = cpmcms01Service.getRegionCode(valuesEntity);
            valuesEntity.getSwapData().put("regionCode", regionCode);
            String calendarId = valuesEntity.getData().getCalendarCode();
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher mt = pattern.matcher(calendarId);
            if (mt.matches()) {
                checkOfficeCode.setCalendarId(Integer.parseInt(calendarId));
                check.setCalendarId(Integer.parseInt(calendarId));
                checkExist.setCalendarId(Integer.parseInt(calendarId));
            } else {
                checkExist.setCalendarCode(calendarId);
                checkOfficeCode.setCalendarCode(calendarId);
                check.setCalendarCode(calendarId);
            }

            /*check.setCountry(regionCode);
            checkOfficeCode.setTtcOfficeCode(ttcOfficeCode);
            valuesEntity.setSwapData(DATA, checkExist);
            List<CPMCMS01Entity> checkListExist = cpmcms01Service.checkCalCodeAndCounty(valuesEntity);
            if (checkListExist.size() != 0) {
                valuesEntity.setSwapData(DATA, check);
                List<CPMCMS01Entity> checkList = cpmcms01Service.checkCalCodeAndCounty(valuesEntity);
                if (checkList.size() == 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { CALENDAR_CODE, COUNTRY });
                    messageLists.add(message);
                }
                valuesEntity.setSwapData(DATA, checkOfficeCode);
                List<CPMCMS01Entity> checkListForOfficeCode = cpmcms01Service.checkCalCodeAndCounty(valuesEntity);
                if (checkListForOfficeCode.size() == 0) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { CALENDAR_CODE, OFFICE_CODE });
                    messageLists.add(message);
                }
            }*/

            // If Party selected is Supplier, TTC Import Office, TTC Imp Warehouse or Customer and Party Code is
            // blank.(w1003_001)
            if (party != null 
                    && (CodeConst.CalendarParty.SUPPLIER == party.intValue()
                    || CodeConst.CalendarParty.TTC_IMP_WAREHOUSE == party.intValue() 
                    || CodeConst.CalendarParty.CUSTOMER == party.intValue()) 
                    && StringUtil.isEmpty(partyCode)) {
                BaseMessage message = new BaseMessage(MessageCodeConst.W1003_001);
                message.setMessageArgs(new String[] { PRITY_CODE });
                messageLists.add(message);
            }
            
            if(messageLists.size() > 0){
                throw new BusinessException(messageLists);
            }

            /*if (party != null && CodeConst.CalendarParty.TTC_IMPORT_OFFICE == party.intValue()) {
                boolean isRealtedOffice = cpmcms01Service.isRelatedWithOffice(valuesEntity);
                if (!isRealtedOffice) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { COUNTRY, PRITY_CODE });
                    messageLists.add(message);
                }
            }

            if (party != null && CodeConst.CalendarParty.TTC_IMP_WAREHOUSE == party.intValue()) {
                boolean isRealtedWareHouse = cpmcms01Service.isRelatedWithWareHouse(valuesEntity);
                if (!isRealtedWareHouse) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { COUNTRY, PRITY_CODE });
                    messageLists.add(message);
                }
            }
            if (party != null && CodeConst.CalendarParty.CUSTOMER == party.intValue()) {
                boolean isRealtedCustomer = cpmcms01Service.isRelatedWithCustomer(valuesEntity);
                if (!isRealtedCustomer) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { COUNTRY, PRITY_CODE });
                    messageLists.add(message);
                }
            }*/
            // Check Country's relationship with other items.(w1004_069 {1}：Country inputed {2}：Party Code inputed)
            /*if (party != null && CodeConst.CalendarParty.SUPPLIER == party.intValue()) {
                boolean isRealtedSupplier = cpmcms01Service.isRelatedWithSupplier(valuesEntity);
                if (!isRealtedSupplier) {
                    BaseMessage message = new BaseMessage(MessageCodeConst.W1003_024);
                    message.setMessageArgs(new String[] { COUNTRY, PRITY_CODE });
                    messageLists.add(message);
                }

                String[] partys = valuesEntity.getData().getPartyCode().split(StringConst.COMMA);

                for (String partyCodeOne : partys) {
                    valuesEntity.getSwapData().put("partyCodeOne", partyCodeOne);
                    List<CPMCMS01Entity> list = cpmcms01Service.getOffices(valuesEntity);
                    if (list.size() != IntDef.INT_ONE) {
                        // C1005_001Click Yes, process continue, else break.
                        BaseMessage message = new BaseMessage(MessageCodeConst.C1018);
                        message.setMessageArgs(new String[] { UPPLIER, OFFICE_CODE });
                        throw new BusinessException(message);
                    }
                }
            }*/

        }
        if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(valuesEntity.getUploadProcess())
                || UploadConst.UPLOAD_PROCESS_CHECK.equals(valuesEntity.getUploadProcess())) {
            CPMCMS01Entity entityFrom = en;
            if (UploadConst.UPLOAD_PROCESS_CONFIRMED.equals(valuesEntity.getUploadProcess())) {
                String mapKey = valuesEntity.getSwapData().get(FILE_UPLOAD_SESSION_KEY).toString();
                entityFrom = (CPMCMS01Entity) context.get(mapKey);
                context.remove(mapKey);
            }

            List<String> nonWorkingDays = Arrays.asList(entityFrom.getNonWorkingDays());
            if (StringUtil.isNumeric(entityFrom.getYear())
                    && !StringUtil.isNullOrEmpty(entityFrom.getCalendarCode())) {
                valuesEntity.setSwapData(COUNTRY_PAGE, entityFrom.getCountry());
                valuesEntity.setSwapData(CALENDAR_CODE_PAGE, entityFrom.getCalendarCode());
                valuesEntity.setSwapData(YEAR_PAGE, entityFrom.getYear());
                valuesEntity.setSwapData(OFFICE_CODE_PAGE, entityFrom.getTtcOfficeCode());
                valuesEntity.setSwapData(PARTY_PAGE, entityFrom.getParty());
                valuesEntity.setSwapData(PARTY_CODE_PAGE, entityFrom.getPartyCode());

                ret = cpmcms01Service.doSaveYearCalendar(valuesEntity, nonWorkingDays);
                return ret;
            }
        }

        return null;
    }

}
