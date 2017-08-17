/**
 * @screen core
 * @author liu_yinchuan
 */
package com.chinaplus.common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.common.consts.ChinaPlusConst.ResourceType;
import com.chinaplus.core.consts.CoreConst;
import com.chinaplus.core.consts.StringConst;
import com.chinaplus.core.util.StringUtil;

/**
 * The common process for string.
 * 
 */
public class ResourceManager {

    /** logger. */
    private static Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    /** properties map. */
    private static final Map<Locale, Map<ResourceType, Map<String, String>>> PROPERTIES = new HashMap<Locale, Map<ResourceType, Map<String, String>>>();

    /**
     * Resource Manager init.
     */
    @PostConstruct
    public void initResource() {

        // get languages from property files
        List<String> langLst = ConfigManager.getSystemLanguages();

        // get from files
        for (String lang : langLst) {

            // resource type
            for (ResourceType type : ResourceType.values()) {

                // get resource mapping
                loadResourceByType(type, MessageManager.getLanguage(lang).getLocale());
            }
        }
    }

    /**
     * Load Resource by resource type.
     * 
     * @param type type
     * @param locale locale
     */
    private void loadResourceByType(ResourceType type, Locale locale) {

        ResourceBundle rb = null;

        try {

            // get rb
            rb = ResourceBundle.getBundle(CoreConst.BASE_PROPERTIES_PATH + type, locale);

            Enumeration<String> keys = rb.getKeys();
            Map<String, String> keyMap = new HashMap<String, String>();

            // set key map
            while (keys.hasMoreElements()) {

                // get key
                String key = keys.nextElement();
                // set into map
                keyMap.put(key, rb.getString(key));
            }

            Map<ResourceType, Map<String, String>> resource = PROPERTIES.get(locale);
            if (resource == null) {
                resource = new HashMap<ResourceType, Map<String, String>>();
                PROPERTIES.put(locale, resource);
            }

            // set into resource
            resource.put(type, keyMap);

            PROPERTIES.put(locale, resource);

        } catch (MissingResourceException mse) {
            logger.error(mse.getMessage());
        }
    }

    /**
     * Get resource By resource Type.
     * 
     * @param locale locale
     * @param type type
     * @return result Map
     */
    public static Map<String, String> getResourceByType(Locale locale, ResourceType type) {

        Map<ResourceType, Map<String, String>> typeResource = PROPERTIES.get(locale);

        if (typeResource == null) {
            return null;
        }

        return typeResource.get(type);
    }

    /**
     * Get resource String by type.
     * 
     * @param locale locale
     * @param type type
     * @return Json String
     */
    public static String getResourceStringByType(Locale locale, ResourceType[] type) {

        // pending '}'
        StringBuffer sb = new StringBuffer();

        // pending '{'
        sb.append(StringConst.LEFT_BRACE_B);

        // get resource
        for (ResourceType rt : type) {
            Map<String, String> resource = getResourceByType(locale, rt);

            // no resource
            if (resource == null || resource.isEmpty()) {
                continue;
            }

            // loop
            for (String key : resource.keySet()) {

                if (!StringUtil.isEmpty(key)) {
                    // pending
                    sb.append(StringConst.NEWLINE);
                    sb.append(key);
                    sb.append(StringConst.COLON);
                    sb.append(StringConst.QUOTE);
                    sb.append(resource.get(key).replace("\"", "\\\""));
                    sb.append(StringConst.QUOTE);
                    sb.append(",");
                }
            }

            // remove last one
            // if (sb.length() > 1) {
            // sb.deleteCharAt(sb.length() - 1);
            // }
        }

        // pending '}'
        sb.append(StringConst.RIGHT_BRACE_B);
        sb.append(StringConst.SEMICOLON);

        return sb.toString();
    }

}