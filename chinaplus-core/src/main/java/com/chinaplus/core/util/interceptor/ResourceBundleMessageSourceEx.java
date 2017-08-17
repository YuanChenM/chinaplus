/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util.interceptor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;

import com.chinaplus.core.util.StringUtil;

/**
 * The message resource bundle class.
 * 
 * @author ma_b
 * 
 */
public class ResourceBundleMessageSourceEx extends ResourceBundleMessageSource {
    /** logger */
    private static Logger logger = LoggerFactory.getLogger(ResourceBundleMessageSourceEx.class);

    // private String[] extendBaseNames;
    /** message resource locations */
    private Resource[] messageLocations;

    // // support wildcard matching
    // private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    /**
     * Set message property files location, and get base name.
     * 
     * @param messageLocations the message resources location
     */
    public void setMessageLocations(Resource[] messageLocations) {
        this.messageLocations = messageLocations;

        // get base name from locations
        List<String> baseNames = new ArrayList<String>();
        if (this.messageLocations != null && this.messageLocations.length > 0) {
            for (Resource messageLocation : this.messageLocations) {
                if (messageLocation == null) {
                    continue;
                }

                String fileName = "";
                try {
                    fileName = messageLocation.getURI().getPath();
                } catch (IOException e) {
                    logger.error("Parsed message resource fail: " + fileName, e);
                    continue;
                }
                String baseName = getBasenameFromLocation(fileName);
                if (!baseNames.contains(baseName)) {
                    baseNames.add(baseName);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Parsed message resource file: {}", baseName);
                    }
                }
            }
        }

        String[] baseNameArray = new String[baseNames.size()];
        super.setBasenames(baseNames.toArray(baseNameArray));
    }

    /**
     * Check string by regex.
     * 
     * @param location message resource location
     * @return true is matched
     */
    private static String getBasenameFromLocation(String location) {
        String baseName = "";

        final int matchIndex = 1;
        final String regex = "(^.*)(_.*_.*\\.properties$)";

        if (!StringUtil.isEmpty(location)) {
            // remove base path
            String basePath = ResourceBundleMessageSourceEx.class.getClassLoader().getResource("").getPath();
            if (location.startsWith(basePath)) {
                baseName = location.replaceFirst(basePath, "");
            }

            if (StringUtil.matchs(baseName, regex)) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(baseName);
                m.find();
                baseName = m.group(matchIndex);
            }
        }
        return baseName;
    }

    // /**
    // *
    // * <p>
    // * Set Message properties file base names.
    // * </p>
    // *
    // * @param basenames
    // * message file array
    // * @see org.springframework.context.support.ResourceBundleMessageSource#setBasenames(java.lang.String[])
    // */
    // @Override
    // public void setBasenames(String... basenames) {
    // String tempBaseName = basenames[0].toString();
    // String[] nameArray = tempBaseName.split(",");
    // int length = nameArray.length;
    // extendBaseNames = new String[length];
    // List<String> baseNames = new ArrayList<String>();
    //
    // for (int i = 0; i < length; i++) {
    // String name = nameArray[i];
    // extendBaseNames[i] = name;
    // try {
    // Resource[] resources = patternResolver.getResources(name);
    // if (resources != null && resources.length > 0) {
    // for (Resource resource : resources) {
    // String fileName = resource.getFilename();
    // if (fileName.endsWith(".properties")) {
    // fileName = fileName.substring(0, fileName.length() - ".properties".length());
    // baseNames.add(fileName);
    // }
    // }
    // }
    // } catch (IOException e) {
    // logger.error("Resource soucre configuration error.[{" + name + "}]", e);
    // }
    // }
    //
    // String[] baseNameArray = new String[baseNames.size()];
    // for (int i = 0; i < baseNames.size(); i++) {
    // baseNameArray[i] = baseNames.get(i);
    // }
    // super.setBasenames(baseNameArray);
    // }

    /**
     * Create a MessageFormat for the given message and Locale.
     * 
     * @param msg
     *        the message to create a MessageFormat for
     * @param locale
     *        the Locale to create a MessageFormat for
     * @return the MessageFormat instance
     */
    @Override
    protected MessageFormat createMessageFormat(String msg, Locale locale) {
        String escapedMsg = msg;
        if (escapedMsg == null) {
            escapedMsg = "";
        }
        escapedMsg = escapedMsg.replaceAll("'", "''");
        return new MessageFormat(escapedMsg, locale);
    }

    /**
     * Get key set
     * 
     * @param basename base name
     * @param locale locale
     * @return key set
     */
    public Set<String> getKeys(String basename, Locale locale) {
        ResourceBundle bundle = getResourceBundle(basename, locale);
        if (bundle == null) {
            return new HashSet<String>();
        } else {
            return bundle.keySet();
        }
    }
}
