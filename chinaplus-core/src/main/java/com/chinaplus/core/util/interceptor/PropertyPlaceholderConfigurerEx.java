/**
 * PropertyPlaceholderConfigurerEx.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.util.interceptor;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.chinaplus.common.util.ExcelConfigManager;
import com.chinaplus.core.bean.MailConfig;
import com.chinaplus.core.util.ConfigUtil;
import com.chinaplus.core.util.EncryptUtils;

/**
 * Properties file processing class
 * 
 */
public class PropertyPlaceholderConfigurerEx extends PropertyPlaceholderConfigurer {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(PropertyPlaceholderConfigurerEx.class);

    /** whether the need for encryption */
    private boolean needEncrypt = true;
    /** the items these need crypt */
    private String[] encryptItems = {};

    /**
     * Set whether the need for encryption.
     * 
     * @param needEncrypt true:need encryption
     */
    public void setNeedEncrypt(boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }

    /**
     * Set a location of a properties file to be loaded.
     * 
     * @param encryptItem the item that need encryption
     */
    public void setEncryptItem(String encryptItem) {
        this.encryptItems = new String[] { encryptItem };
    }

    /**
     * Set locations of properties files to be loaded.
     * 
     * @param encryptItems the item these need encryption
     */
    public void setEncryptItems(String... encryptItems) {
        this.encryptItems = encryptItems;
    }

    /**
     * Visit each bean definition in the given bean factory and attempt to replace ${...} property
     * placeholders with values from the given properties.
     * 
     * @param beanFactory 工厂类
     * @param props 参数
     * @throws BeansException 异常
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
        throws BeansException {
        EncryptUtils cryptUtil = EncryptUtils.getInstance(EncryptUtils.AES);

        for (String propertyName : props.stringPropertyNames()) {
            if (ExcelConfigManager.isExcelConfig(propertyName)) {
                // memory excel configurations in excel config util
                ExcelConfigManager.setConfig(propertyName, props.getProperty(propertyName));
                logger.debug("excel config:{}={}", propertyName, props.getProperty(propertyName));
            } else {
                // memory all config in config util, except excel configurations
                ConfigUtil.add(propertyName, props.getProperty(propertyName));
                logger.debug("config:{}={}", propertyName, props.getProperty(propertyName));
            }
        }

        // decrypt for encryted items
        for (String propertyKey : encryptItems) {
            String value = props.getProperty(propertyKey);
            if (value != null) {
                if (this.needEncrypt) {
                    props.setProperty(propertyKey, cryptUtil.decrypt(value));
                    logger.debug("Decrypt config:{}=***********", propertyKey);
                }
            }
        }

        initMailConfig();

        super.processProperties(beanFactory, props);
    }
    
    /**
     * Init mail config.
     */
    private void initMailConfig() {
        MailConfig mailConfig = ConfigUtil.getMailConfig();
        mailConfig.setTransport(ConfigUtil.get("mail.transport.protocol"));
        mailConfig.setAuth(ConfigUtil.get("mail.smtp.auth"));
        mailConfig.setAuthType(ConfigUtil.get("mail.smtp.auth.type"));
        mailConfig.setSmtpPort(ConfigUtil.get("mail.smtp.port"));
        mailConfig.setSendMailAddr(ConfigUtil.get("mail.host.user"));
        mailConfig.setSendMailPwd(ConfigUtil.get("mail.host.pwd"));
        mailConfig.setSmtpHost(ConfigUtil.get("mail.smtp.host1"));
        mailConfig.setMailTempletHost(ConfigUtil.get("mail.templet.host"));
        mailConfig.setMailTempletSupporter(ConfigUtil.get("mail.templet.supporter"));
    }
}
