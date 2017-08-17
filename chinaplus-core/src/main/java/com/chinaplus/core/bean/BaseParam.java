/**
 * BaseParam.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.core.util.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The parameter from page request.
 */
public class BaseParam implements Cloneable, Serializable {

    /** The limit for no paging */
    protected static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
    /** The page No. for no paging */
    protected static final int NO_PAGE = 0;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(BaseParam.class);

    /** the limit */
    protected int limit = NO_ROW_LIMIT;
    /** the filters in screen */
    private Map<String, Object> filters;

    /** the current office id */
    private Integer currentOfficeId;
    /** the current office id */
    private String currentOfficeCode;
    /** the current office's timezone */
    private String officeTimezone;
    /** the current office id */
    private Timestamp officeSyncTime;
    // /** the current office code */
    // private String officeCode;
    /** the login user id */
    private Integer loginUserId;
    /** the login user id */
    private String loginId;
    /** login user's access level for current office */
    private int currentAccessLevel;
    /** current language */
    private Integer language;
    /** client date time */
    private String clientTime;
    /** the token */
    private String token;
    /** the screenId */
    private String screenId;
    /** the parameters outside of filter */
    private Map<String, Object> swapData;
    /** the office ids of user */
    private List<Integer> userOfficeIds;
    /** json data for form submit */
    private String jsonData;
    
    /** upload process */
    private String uploadProcess;
    
    /** session Key */
    private String sessionKey;

    /**
     * The Constructors Method.
     */
    public BaseParam() {
        filters = new HashMap<String, Object>();
        swapData = new HashMap<String, Object>();
        userOfficeIds = new ArrayList<Integer>();
    }

    /**
     * Get the filters.
     * 
     * @return filters
     */
    public Map<String, Object> getFilters() {
        if (this.filters == null) {
            return new HashMap<String, Object>();
        }
        return this.filters;
    }

    /**
     * Set the filters.
     * 
     * @param filters filters
     */
    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    /**
     * Set a filter condiiton.
     * 
     * @param key filter key
     * @param value filter value
     */
    public void setFilter(String key, Object value) {
        if (this.filters == null) {
            this.filters = new HashMap<String, Object>();
        }

        filters.put(key, value);
    }

    /**
     * Get the token.
     * 
     * @return token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Set the token.
     * 
     * @param token token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the currentOfficeId
     */
    public Integer getCurrentOfficeId() {
        return currentOfficeId;
    }

    /**
     * @param currentOfficeId the currentOfficeId to set
     */
    public void setCurrentOfficeId(Integer currentOfficeId) {
        this.currentOfficeId = currentOfficeId;
    }

    /**
     * Get the currentOfficeCode.
     *
     * @return currentOfficeCode
     */
    public String getCurrentOfficeCode() {
        return this.currentOfficeCode;
    }

    /**
     * Set the currentOfficeCode.
     *
     * @param currentOfficeCode currentOfficeCode
     */
    public void setCurrentOfficeCode(String currentOfficeCode) {
        this.currentOfficeCode = currentOfficeCode;
        
    }

    /**
     * @return the officeTimezone
     */
    public String getOfficeTimezone() {
        return officeTimezone;
    }

    /**
     * @param officeTimezone the officeTimezone to set
     */
    public void setOfficeTimezone(String officeTimezone) {
        this.officeTimezone = officeTimezone;
    }

    // /**
    // * @return the officeCode
    // */
    // public String getOfficeCode() {
    // return officeCode;
    // }
    //
    // /**
    // * @param officeCode the officeCode to set
    // */
    // public void setOfficeCode(String officeCode) {
    // this.officeCode = officeCode;
    // }

    /**
     * @return the loginUserId
     */
    public Integer getLoginUserId() {
        return this.loginUserId;
    }

    /**
     * @param loginUserId the loginUserId to set
     */
    public void setLoginUserId(Integer loginUserId) {
        this.loginUserId = loginUserId;
    }

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * Get the language.
     * 
     * @return language
     */
    public Integer getLanguage() {
        return this.language;
    }

    /**
     * Set the language.
     * 
     * @param language language
     */
    public void setLanguage(Integer language) {
        this.language = language;
    }

    /**
     * Get the swapData.
     * 
     * @return swapData
     */
    public Map<String, Object> getSwapData() {
        if (this.swapData == null) {
            return new HashMap<String, Object>();
        }
        return this.swapData;
    }

    /**
     * Set the swapData.
     * 
     * @param swapData the parameters form client
     */
    public void setSwapData(Map<String, Object> swapData) {
        this.swapData = swapData;
    }

    /**
     * Set a swapData condiiton.
     * 
     * @param key swapData key
     * @param value swapData value
     */
    public void setSwapData(String key, Object value) {
        if (this.swapData == null) {
            this.swapData = new HashMap<String, Object>();
        }

        swapData.put(key, value);
    }

    /**
     * Get this page is paging.
     * 
     * @return true: when this page is paging
     */
    public boolean isPaging() {
        return this.limit != NO_ROW_LIMIT && this.limit != NO_PAGE;
    }

    /**
     * 
     * 
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Get the screenId.
     *
     * @return screenId
     */
    public String getScreenId() {
        return this.screenId;
    }

    /**
     * Set the screenId.
     *
     * @param screenId screenId
     */
    public void setScreenId(String screenId) {
        this.screenId = screenId;

    }

    /**
     * Get the clientTime.
     *
     * @return clientTime
     */
    public String getClientTime() {
        return this.clientTime;
    }

    /**
     * Set the clientTime.
     *
     * @param clientTime clientTime
     */
    public void setClientTime(String clientTime) {
        this.clientTime = clientTime;
    }

    /**
     * Get the currentAccessLevel.
     *
     * @return currentAccessLevel
     */
    public int getCurrentAccessLevel() {
        return this.currentAccessLevel;
    }

    /**
     * Set the currentAccessLevel.
     *
     * @param currentAccessLevel currentAccessLevel
     */
    public void setCurrentAccessLevel(int currentAccessLevel) {
        this.currentAccessLevel = currentAccessLevel;
    }

    /**
     * Get the userOfficeIds.
     *
     * @return userOfficeIds
     */
    public List<Integer> getUserOfficeIds() {
        return this.userOfficeIds;
    }

    /**
     * Set the userOfficeIds.
     *
     * @param userOfficeIds userOfficeIds
     */
    public void setUserOfficeIds(List<Integer> userOfficeIds) {
        this.userOfficeIds = userOfficeIds;
    }

    /**
     * Get the uploadProcess.
     *
     * @return uploadProcess
     */
    public String getUploadProcess() {
        return this.uploadProcess;
    }

    /**
     * Set the uploadProcess.
     *
     * @param uploadProcess uploadProcess
     */
    public void setUploadProcess(String uploadProcess) {
        this.uploadProcess = uploadProcess;
    }

    /**
     * Get the sessionKey.
     *
     * @return sessionKey
     */
    public String getSessionKey() {
        return this.sessionKey;
    }

    /**
     * Set the sessionKey.
     *
     * @param sessionKey sessionKey
     */
    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    /**
     * Get the jsonData.
     *
     * @return the jsonData
     */
    public String getJsonData() {
        return this.jsonData;
    }

    /**
     * Set the jsonData.
     *
     * @param jsonData jsonData
     */
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;

        try {

            // if json data is null
            if (!StringUtil.isEmpty(this.jsonData)) {

                // get mapper
                ObjectMapper mapper = new ObjectMapper();
                // ignore
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                // read parameter
                BaseParam param = mapper.readValue(this.jsonData, this.getClass());
                if (param != null) {
                    // swap data
                    if (param.getSwapData() != null && !param.getSwapData().isEmpty()) {
                        if (this.swapData == null) {
                            this.swapData = new HashMap<String, Object>();
                        }
                        this.swapData.putAll(param.getSwapData());
                    }
                    // filters
                    if (param.getFilters() != null && !param.getFilters().isEmpty()) {
                        if (this.filters == null) {
                            this.filters = new HashMap<String, Object>();
                        }
                        this.filters.putAll(param.getFilters());
                    }
                    /** client date time */
                    if (!StringUtil.isEmpty(param.getClientTime())) {
                        this.clientTime = param.getClientTime();
                    }
                    /** the token */
                    if (!StringUtil.isEmpty(param.getToken())) {
                        this.token = param.getToken();
                    }
                    /** the screenId */
                    if (!StringUtil.isEmpty(param.getScreenId())) {
                        this.screenId = param.getScreenId();
                    }
                }
            }

        } catch (Exception e) {

            // do nothing
            logger.warn("json data convert fail. {}. {}", e.getMessage(), this.jsonData);
        }
    }

    /**
     * Add an office id for user.
     *
     * @param officeId office id
     */
    public void addUserOfficeIds(Integer officeId) {
        if (this.userOfficeIds == null) {
            this.userOfficeIds = new ArrayList<Integer>();
        }

        if (officeId != null & !this.userOfficeIds.contains(officeId)) {
            this.userOfficeIds.add(officeId);
        }
    }

    /**
     * Get the officeSyncTime.
     *
     * @return officeSyncTime
     *
     * @author liu_yinchuan
     */
    public Timestamp getOfficeSyncTime() {
        return this.officeSyncTime;
    }

    /**
     * Set the officeSyncTime.
     *
     * @param officeSyncTime officeSyncTime
     *
     * @author liu_yinchuan
     */
    public void setOfficeSyncTime(Timestamp officeSyncTime) {
        this.officeSyncTime = officeSyncTime;
        
    }

}
