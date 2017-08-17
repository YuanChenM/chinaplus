/**
 * MessageManager.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chinaplus.common.consts.ChinaPlusConst;
import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.core.util.StringUtil;

/**
 * The message manager class.
 */
@Component
public class MessageManager implements ApplicationContextAware {

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(MessageManager.class);
    private static ApplicationContext context;

    /**
     * check properties key exists.
     * 
     * @param key properties key
     * @return ture is exists
     */
    public static boolean containsKey(String key) {
        boolean result = false;
        if (!StringUtil.isEmpty(key)) {
            if (context.getMessage(key, null, getLocale()) != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * check properties key exists.
     * 
     * @param key properties key
     * @param locale locale
     * 
     * @return true is exists
     */
    public static boolean containsKey(String key, Locale locale) {
        boolean result = false;
        if (!StringUtil.isEmpty(key)) {
            if (context.getMessage(key, null, locale) != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * get Message by current locale
     * 
     * @param key properties key
     * @return the message
     */
    public static String getMessage(String key) {
        return getMessage(key, null, getLocale());
    }

    /**
     * get Message by language
     * 
     * @param key properties key
     * @param language language
     * @return the message
     */
    public static String getMessage(String key, String language) {
        return getMessage(key, null, getLanguage(language).getLocale());
    }

    /**
     * get Message by locale
     * 
     * @param key properties key
     * @param locale Locale
     * @return the message
     */
    public static String getMessage(String key, Locale locale) {
        return getMessage(key, null, locale);
    }

    /**
     * get Message with parameters by current locale
     * 
     * @param key properties key
     * @param obj the parameters
     * @return the message
     */
    public static String getMessage(String key, Object[] obj) {
        return getMessage(key, obj, getLocale());
    }

    /**
     * get Message with parameters by locale
     * 
     * @param key properties key
     * @param obj the parameters
     * @param language language
     * @return the message
     */
    public static String getMessage(String key, Object[] obj, String language) {
        return getMessage(key, obj, getLanguage(language).getLocale());
    }

    /**
     * get Message with parameters by locale
     * 
     * @param key properties key
     * @param obj the parameters
     * @param locale Locale
     * @return the message
     */
    public static String getMessage(String key, Object[] obj, Locale locale) {
        String value = "";
        if (!StringUtil.isEmpty(key)) {
            Object[] params = null;
            if (context != null) {
                // get i18n parameters
                if (obj != null) {
                    params = new Object[obj.length];
                    for (int i = 0; i < obj.length; i++) {
                        if (obj[i] != null && obj[i] instanceof String) {
                            params[i] = getMessage(StringUtil.toSafeString(obj[i]));
                        } else {
                            params[i] = obj[i];
                        }
                    }
                }

                value = context.getMessage(key, params, locale);
            }

            if (StringUtil.isEmpty(value) || value.equals(key)) {
                // if (logger.isWarnEnabled()) {
                // logger.warn("Resouce Code [{}] is not found.", key);
                // }
                if (obj != null) {
                    value = StringUtil.formatMessage(key, params);
                }
            }
        }
        return value;
    }

    /**
     * get the Locale info
     * 
     * @return the Locale info
     */
    public static Locale getLocale() {

        HttpServletRequest request = getHttpServletRequest();
        return (Locale) SessionInfoManager.getContextInstance(request).getLoginLocale();
    }

    /**
     * Get locale by language.
     * 
     * @param language language
     * @return locale
     */
    public static Language getLanguage(String language) {
        // Locale locale = YanmarConst.Language.ENGLISH.getLocale();
        Language lang = Language.CHINESE;
        if (ChinaPlusConst.Language.ENGLISH.getName().equalsIgnoreCase(language)) {
            lang = Language.ENGLISH;
        } else if (ChinaPlusConst.Language.CHINESE.getName().equalsIgnoreCase(language)) {
            lang = Language.CHINESE;
        }
        return lang;
    }

    /**
     * Get locale by language.
     * 
     * @param language language
     * @return locale
     */
    public static Language getLanguage(int language) {
        // Locale locale = YanmarConst.Language.ENGLISH.getLocale();
        Language lang = Language.CHINESE;
        if (ChinaPlusConst.Language.ENGLISH.getCode() == language) {
            lang = Language.ENGLISH;
        } else if (ChinaPlusConst.Language.CHINESE.getCode() == language) {
            lang = Language.CHINESE;
        }
        return lang;
    }

    // /**
    // *
    // * <p>
    // * Get the language from IE Browser.
    // * </p>
    // *
    // * @return the IE Browser's language
    // */
    // @Deprecated
    // public static String getIELanguage() {
    //
    // return getHttpServletRequest().getLocale().getLanguage();
    // }

    /**
     * <p>
     * get the request.
     * </p>
     * 
     * @return the request
     */
    @Deprecated
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
            .getRequestAttributes();
        if (servletRequestAttributes != null) {
            return servletRequestAttributes.getRequest();
        } else {
            return null;
        }
    }

    /**
     * <p>
     * Get Message without html break.
     * </p>
     * 
     * @param key key
     * @return message that replace break with space.
     */
    public static String getMessageWithoutHtmlBreak(String key) {
        String retValue = getMessage(key);
        return retValue.replaceAll("(?i)<br[/]{0,1}>", " ");
    }

    /**
     * @param arg0 ApplicationContext
     * @throws BeansException BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        context = arg0;
    }
}
