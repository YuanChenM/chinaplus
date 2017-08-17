/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.common.util;

import org.apache.commons.codec.digest.DigestUtils;

import com.chinaplus.core.util.StringUtil;

/**
 * <p>
 * EncryptManager.
 * </p>
 */
public class EncryptManager {

    /**
     * The Constructors Method.
     */
    private EncryptManager() {

    }

    /**
     * encrypt string
     * 
     * @param key the string to be encrypt
     * @return encrypted String
     */
    public static String encrypt(String key) {
        String tmp = StringUtil.isNullOrEmpty(key) ? "" : key;
        return DigestUtils.md5Hex(tmp);
    }

    /**
     * 
     * <p>
     * compare password
     * </p>
     * 
     * @param password example:Aa1234
     * @param comparePassword example:0f0d0b0a020c070c080e0100070f0f0e
     * @return boolean
     * @throws Exception
     */
    public static boolean match(String password, String comparePassword) {

        if (comparePassword == null || password == null) {
            return false;
        }
        return comparePassword.equals(encrypt(password));
    }
}
