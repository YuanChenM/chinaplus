/**
 * @screen core
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaplus.common.bean.TnmAuthorizationEx;
import com.chinaplus.common.bean.TnmScreenEx;
import com.chinaplus.common.service.ScreenInfoService;
import com.chinaplus.core.util.StringUtil;

/**
 * The common process for string.
 * 
 */
@Component
public class ScreenInfoManager {

    private static List<TnmScreenEx> screenInfoLst = new ArrayList<TnmScreenEx>();

    @Autowired
    ScreenInfoService screenInfoService;

    /**
     * Resource Manager init.
     */
    @PostConstruct
    public void initScreenResource() {

        // get languages from property files
        List<TnmAuthorizationEx> authScreenLst = screenInfoService.selectAllScreenAuthInfo();
        
        // new 
        TnmScreenEx screenInfo = null;
        // split all data into one list
        for(TnmAuthorizationEx auth : authScreenLst) {
            // adjust
            if (screenInfo == null
                    || !(auth.getScreenId().equals(screenInfo.getScreenId())
                            && auth.getAuthScreenId().equals(screenInfo.getAuthScreenId()))) {
                
                // create new TnmScreenEx
                screenInfo = new TnmScreenEx();
                screenInfo.setScreenId(auth.getScreenId());
                screenInfo.setAuthScreenId(auth.getAuthScreenId());
                screenInfo.setScreenUrl(auth.getScreenUrl());
                screenInfo.setProjectFlag(auth.getProjectFlag());
                
                // set into screen information
                screenInfoLst.add(screenInfo);
            }
            
            // set into 
            screenInfo.setAuthList(auth);
        }
        
        // add last one
        if (screenInfo != null) {
            screenInfoLst.add(screenInfo);
        }
    }

    /**
     * Get screen url by screen id.
     * 
     * @param rootId rootId
     * @param screenId screenId
     * @return screen url
     */
    public static String getScreenUrlByScreenId(String rootId, String screenId) {
        if (!screenInfoLst.isEmpty()) {
            for (TnmScreenEx screen : screenInfoLst) {
                if (screen.getScreenId().equals(screenId)
                        && (screen.getAuthScreenId().equals(rootId)
                                || screen.getAuthScreenId().equals(screenId))) {
                    return screen.getScreenUrl();
                }
            }
        }

        return null;
    }

    /**
     * Get screen by root id.
     * 
     * @param rootId rootId
     * @param screenId screenId
     * @return screen auth
     */
    public static boolean checkScreenAuth(String rootId, String screenId) {
        
        // other check
        if (!screenInfoLst.isEmpty()) {
            for (TnmScreenEx screen : screenInfoLst) {
                if (screen.getScreenId().equals(screenId)
                        && (screen.getAuthScreenId().equals(rootId)
                                || screen.getAuthScreenId().equals(screenId))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Get screen auth type by root id.
     * 
     * @param rootId rootId
     * @param screenId screenId
     * @return screen auth type
     */
    public static Integer getScreenAuthType(String rootId, String screenId) {
        
        // other check
        if (!screenInfoLst.isEmpty()) {
            for (TnmScreenEx screen : screenInfoLst) {
                if (screen.getScreenId().equals(screenId) 
                        && screen.getAuthScreenId().equals(rootId)) {
                    return screen.getAuthList().get(0).getAuthType();
                }
            }
        }

        return null;
    }

    /**
     * Get screen auth type by root id.
     * 
     * @param rootId rootId
     * @param screenId screenId
     * @param authCode authCode
     * @return screen auth type
     */
    public static Integer getMinAccessLevel(String rootId, String screenId, String authCode) {
        
        // other check
        TnmScreenEx screen = getScreenAuthInfo(rootId, screenId);
        if (screen != null && !StringUtil.isEmpty(authCode)) {
            for (TnmAuthorizationEx auth : screen.getAuthList()) {
                if (authCode.equalsIgnoreCase(auth.getAuthCode())) {
                    return auth.getAccessLevel();
                }
            }
        }

        return null;
    }

    /**
     * get Screen auth Info By Root Id.
     * 
     * @param rootId rootId
     * @param screenId screenId
     * @return screen url
     */
    public static TnmScreenEx getScreenAuthInfo(String rootId, String screenId) {
        
        if (!screenInfoLst.isEmpty() 
                && !StringUtil.isEmpty(rootId) 
                && !StringUtil.isEmpty(screenId)) {
            for (TnmScreenEx screen : screenInfoLst) {
                if (rootId.equals(screen.getAuthScreenId()) 
                        && screenId.equals(screen.getScreenId())) {
                    return screen;
                }
            }
        }

        return null;
    }

    /**
     * get Screen auth Info By Root Id.
     * 
     * @param rootId rootId
     * @return screen url
     */
    public static List<TnmScreenEx> getScreenAuthInfoByRoot(String rootId) {
        List<TnmScreenEx> result = new ArrayList<TnmScreenEx>();
        if (!screenInfoLst.isEmpty() && !StringUtil.isEmpty(rootId)) {
            for (TnmScreenEx screen : screenInfoLst) {
                if (rootId.equals(screen.getAuthScreenId())) {
                    result.add(screen);
                }
            }
        }

        return result;
    }
    

    /**
     * return all screenInforlst.
     * 
     * @return screen url
     */
    public static List<TnmScreenEx> getScreenInfoLst() {

        return screenInfoLst;
    }
}