package com.chinaplus.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Common
 * 
 * @author Common
 * @version 1.0
 */
public final class CookieUtils {
    
    /** max age of cookie(seconds) */
    private final static int MAX_AGE = 3600 * 24 * 30;

    /** HTTP SERVLET response */
    private HttpServletResponse response;
    /** HTTP SERVLET request */
    private HttpServletRequest request;

    /**
     * constractor
     * 
     * @param request request
     * @param response response
     */
    public CookieUtils(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * set cookie.
     * 
     * @param key key
     * @param value value
     */
    public void addCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(MAX_AGE);
        cookie.setPath("/");
        this.response.addCookie(cookie);
    }

    /**
     * set cookie.
     * 
     * @param key key
     * @return value
     */
    public String getCookie(String key) {
        String value = null;

        if (!StringUtil.isEmpty(key)) {
            Cookie[] cookieList = this.request.getCookies();
            if (cookieList != null) {
                for (Cookie cookie : cookieList) {
                    if (cookie.getName().equals(key)) {
                        value = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return value;
    }
}
