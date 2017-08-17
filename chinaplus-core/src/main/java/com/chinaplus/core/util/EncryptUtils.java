/**
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.chinaplus.core.exception.BusinessException;

/**
 * The encrypt utils for MD5,SHA,...
 */
public final class EncryptUtils {

    /** encrypt type:MD5 */
    public static String MD5 = "MD5";
    /** encrypt type:SHA-1 */
    public static String SHA = "SHA-1";
    /** encrypt type:SHA256 */
    public static String SHA256 = "SHA-256";
    /** encrypt type:SHA512 */
    public static String SHA512 = "SHA-512";
    // /** encrypt type:DES */
    // public static String DES = "DES";
    /** encrypt type:DESede */
    public static String DESEDE = "DESede";
    // /** encrypt type:Blowfish */
    // public static String BLOWFISH = "Blowfish";
    /** encrypt type:AES */
    public static String AES = "AES";
    // /** encrypt type:BASE64 */
    // public static String BASE64 = "BASE64";
    /** FF */
    private static final int MAX_HEX = 0XFF;

    /** encrypt key */
    private final static String KEY_STR = "hCareCryptKey";
    /** char encode */
    private final static String CHARSETNAME = "UTF-8";

    // /** logger */
    // private static Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

    /** crypt utils */
    private static EncryptUtils cryptUtils;
    /** encrypt type **/
    private String cryptType;
    /** key */
    private Key key;

    /**
     * The Constructors Method.
     */
    private EncryptUtils() {
        cryptType = null;
    }

    /**
     * Create CryptUtils instance calculation
     * 
     * @param cryptType encrypt type
     * @return CryptUtils
     */
    public static EncryptUtils getInstance(String cryptType) {
        if (cryptUtils == null) {
            cryptUtils = new EncryptUtils();
        }
        cryptUtils.cryptType = cryptType;

        if (DESEDE.equals(cryptType) || AES.equals(cryptType)) {
            // when type is DESede or AES, can decrypt
            try {
                KeyGenerator generator = KeyGenerator.getInstance(cryptType);
                generator.init(new SecureRandom(KEY_STR.getBytes()));
                cryptUtils.key = generator.generateKey();
                generator = null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return cryptUtils;
    }

    /**
     * Do encrypt calculation.
     * 
     * @param value the value to encrypt
     * @return encrypted value
     */
    public String encrypt(String value) {
        String encryptValue = null;
        if (MD5.equals(this.cryptType)) {
            encryptValue = EncryptUtils.doEncrypt(value, MD5);
        } else if (SHA.equals(this.cryptType)) {
            encryptValue = EncryptUtils.doEncrypt(value, SHA);
        } else if (SHA256.equals(this.cryptType)) {
            encryptValue = EncryptUtils.doEncrypt(value, SHA256);
        } else if (SHA512.equals(this.cryptType)) {
            encryptValue = EncryptUtils.doEncrypt(value, SHA512);
        } else if (DESEDE.equals(this.cryptType)) {
            encryptValue = cryptUtils.getEncryptString(value);
        } else if (AES.equals(this.cryptType)) {
            encryptValue = cryptUtils.getEncryptString(value);
        } else {
            encryptValue = EncryptUtils.doEncrypt(value, this.cryptType);
        }
        return encryptValue;
    }

    /**
     * Do encrypt calculation.
     * 
     * @param value the value to encrypt
     * @param type the encrypt type
     * @return encrypted value
     */
    private static String doEncrypt(String value, String type) {
        byte[] digesta = null;
        try {
            // get a md5 message digest
            MessageDigest alga = MessageDigest.getInstance(type);
            // put the value to messaeg digest
            alga.update(value.getBytes());
            // get th message digest
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException("Encrypt fail.", e);
        }
        // convert the message digest to string
        return EncryptUtils.byte2hex(digesta);
    }

    /**
     * Encrypt a string.
     * 
     * @param str the string that will be encrypt
     * @return encrypted string
     */
    private String getEncryptString(String str) {
        sun.misc.BASE64Encoder base64encoder = new sun.misc.BASE64Encoder();
        try {
            byte[] bytes = str.getBytes(CHARSETNAME);
            Cipher cipher = Cipher.getInstance(this.cryptType);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return base64encoder.encode(doFinal);
        } catch (Exception e) {
            throw new BusinessException("Encrypt fail.", e);
        }
    }

    /**
     * Convert the byte array to HEX string.
     * 
     * @param b the byte array
     * @return the HEX string
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & MAX_HEX));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * Decrypt a encrypted string.
     * 
     * @param value the encrypted string
     * @return decrypted string
     */
    public String decrypt(String value) {
        String decryptValue = null;
        if (DESEDE.equals(this.cryptType)) {
            decryptValue = cryptUtils.getDecryptString(value);
        } else if (AES.equals(this.cryptType)) {
            decryptValue = cryptUtils.getDecryptString(value);
        } else {
            throw new BusinessException("不可解密文字");
        }
        return decryptValue;
    }

    /**
     * Decrypt a encrypted string.
     * 
     * @param str the encrypted string
     * @return decrypted string
     */
    private String getDecryptString(String str) {
        sun.misc.BASE64Decoder base64decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] bytes = base64decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(this.cryptType);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSETNAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
