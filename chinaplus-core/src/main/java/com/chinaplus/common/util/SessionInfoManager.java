/**
 * SessionInfoManager.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.util;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.chinaplus.common.bean.UserInfo;
import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.StringConst;

/**
 * Session manager class
 * 
 */
public final class SessionInfoManager {

    /** the key of login user info in the session. */
    private final static String LOGIN_INFO = "LOGIN_INFO";

    /** the key of login user info in the session. */
    private final static String CLICK_MENU = "CLICK_MENU";

    /** the key of login user info in the session. */
    private final static String LINK_PAGE = "LINK_PAGE";

    /** default locale. */
    //private final static Locale DEFAULT_LOCALE = ChinaPlusConst.Language.ENGLISH.getLocale();
    private final static Locale DEFAULT_LOCALE = ChinaPlusConst.Language.CHINESE.getLocale();

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(SessionInfoManager.class);

    /** HTTP Session. */
    private HttpSession session;

    /**
     * 
     * The Constructors Method.
     *
     * @param session the session
     */
    private SessionInfoManager(HttpSession session) {
        this.session = session;
    }

    /**
     * 
     * The Constructors Method.
     *
     */
    public SessionInfoManager() {
    }

    /**
     * Get session manager instance.
     * 
     * @param request the request
     * @return session manager instance
     */
    public static SessionInfoManager getContextInstance(HttpServletRequest request) {
        if (request != null) {
            return new SessionInfoManager(request.getSession());
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("SessionInfoManager.getContextInstance(request=null)");
            }
            return new SessionInfoManager(null);
        }
    }


    /**
     * Get session manager instance from request context holder.
     * 
     * @return session manager instance
     */
    public static SessionInfoManager getContextInstance() {
        // get ServletRequestAttributes
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        
        // if not null
        if (servletRequestAttributes != null) {
            // get request 
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (request != null) {
                return new SessionInfoManager(request.getSession());
            } else {
                return new SessionInfoManager(null);
            }
        } else {
            return new SessionInfoManager(null);
        }
    }

    /**
     * Destory curennt session.
     */
    public void destoryContext() {
        this.clear();
        if (this.session != null) {
            session.invalidate();
            session = null;
        }
    }

    /**
     * <p>
     * Clear the login user info from the session.
     * </p>
     * 
     */
    public void clear() {
        try {
            if (this.session != null) {
                // login user info
                this.session.removeAttribute(LOGIN_INFO);

                // condition info
                Enumeration<?> attributes = this.session.getAttributeNames();
                while (attributes.hasMoreElements()) {
                    Object element = attributes.nextElement();
                    if (element != null && element instanceof String) {
                        String sessionName = (String) element;
                        this.session.removeAttribute(sessionName);
                        if (logger.isDebugEnabled()) {
                            logger.debug("clear session:{}", sessionName);
                        }
                    }
                }
            }
        } catch (IllegalStateException e) {
            logger.error("exception occurred  when clear session.", e);
        }
    }

    /**
     * Ger login user information from session
     * 
     * @return login user information
     */
    public UserInfo getLoginInfo() {
        UserInfo loginInfo = null;
        if (this.session != null) {
            Object userInfo = this.session.getAttribute(LOGIN_INFO);
            if (userInfo != null && userInfo instanceof UserInfo) {
                loginInfo = (UserInfo) userInfo;
            }
        }

        return loginInfo;
    }

    /**
     * Save login user information to session.
     * 
     * @param loginInfo login user information
     * 
     */
    public void setLoginInfo(UserInfo loginInfo) {

        if (this.session != null) {
            if (loginInfo != null) {
                this.session.setAttribute(LOGIN_INFO, loginInfo);
            }
        }

        if (loginInfo != null) {
            // language
            this.setLoginLocale(loginInfo.getLanguage());
            // set token
            this.setToken();
        }

    }

    /**
     * Get current locale by user language.
     * 
     * @return locale
     */
    public Locale getLoginLocale() {
        Locale locale = (Locale) this.get(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
        if (locale == null) {
            // use default when no define
            locale = DEFAULT_LOCALE;
        }

        return locale;
    }

    /**
     * Set current local by specified language.
     * 
     * @param language the language
     * 
     * @return current language
     */
    public String setLoginLocale(Language language) {
        // language
        Locale locale = language.getLocale();
        this.put(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

        return locale.toString();
    }

    /**
     * Set current local by specified language.
     * 
     * @param menu the resource
     */
    public void setMainMenuResource(String menu) {
        // language
        this.put(CLICK_MENU, menu);
    }

    /**
     * Set current local by specified language.
     * 
     * @param linkPage the resource
     */
    public void setYanmarLinkPage(String linkPage) {
        // language
        this.put(LINK_PAGE, linkPage);
    }

    /**
     * Get Current click menu.
     * 
     * 
     * @return current language
     */
    public String getMainMenuResource() {

        // token id
        Object menu = this.get(CLICK_MENU);

        return menu == null ? StringConst.BLANK : menu.toString();
    }

    /**
     * Set current local by specified language.
     * 
     * @return current token
     */
    public String setToken() {
        
        // token id
        String token = StringConst.BLANK;
        // set Token
        if (this.session != null) {

            token = this.session.getId();
            this.put(CoreConst.KEY_SESSION_TOKEN, this.session.getId());
        }

        return token;
    }

    /**
     * Set current local by specified language.
     * 
     * @return current language
     */
    public String getToken() {
        // token id
        Object token = this.get(CoreConst.KEY_SESSION_TOKEN);

        return token == null ? StringConst.BLANK : token.toString();
    }

    /**
     * if user is login, then return true.
     * 
     * @return true is logined
     */
    public boolean isLogined() {
        boolean isLogined = false;
        if (this.getLoginInfo() != null) {
            isLogined = true;
        }
        return isLogined;
    }

    /**
     * Save data to session.
     * 
     * @param key data key
     * @param value data value
     */
    public void put(String key, Object value) {
        if (this.session != null) {
            this.session.setAttribute(key, value);
        }
    }

    /**
     * Get data value from session.
     * 
     * @param key data key
     * @return data value
     */
    public Object get(String key) {
        Object value = null;
        if (this.session != null) {
            value = this.session.getAttribute(key);
        }
        return value;
    }

    /**
     * Remove data from session by key.
     * 
     * @param key data key
     */
    public void remove(String key) {
        if (this.session != null) {
            this.session.removeAttribute(key);
        }
    }
}
