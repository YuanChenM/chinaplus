/**
 * ExcelConfig.java
 * 
 * @screen core
 * @author ma_b
 */
package com.chinaplus.core.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration for mail.
 */
public class MailConfig {
    
    /** transport */
    private String transport;
    
    /** Smtp Host */
    private String smtpHost;
    
    /** Smtp auth */
    private String auth;
    
    /** Smtp auth Type */
    private String authType;
    
    /** Send Mail Port */
    private String smtpPort;
    
    /** Send Mail Addr */
    private String sendMailAddr;
    
    /** Send Mail Pwd */
    private String sendMailPwd;
    
    /** mail templet host */
    private String mailTempletHost;
    
    /** mail templet supporter */
    private String mailTempletSupporter;
    
    /** Mail Title */
    private Map<Integer, String> mailTitleMap = new HashMap<Integer, String>();
    
    /** Mail Content */
    private Map<Integer, String> mailContentMap = new HashMap<Integer, String>();

    /**
     * Get the transport.
     *
     * @return transport
     */
    public String getTransport() {
        return this.transport;
    }

    /**
     * Set the transport.
     *
     * @param transport transport
     */
    public void setTransport(String transport) {
        this.transport = transport;
    }

    /**
     * Get the smtpHost.
     *
     * @return smtpHost
     */
    public String getSmtpHost() {
        return this.smtpHost;
    }

    /**
     * Set the smtpHost.
     *
     * @param smtpHost smtpHost
     */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
     * Get the auth.
     *
     * @return auth
     */
    public String getAuth() {
        return this.auth;
    }

    /**
     * Set the auth.
     *
     * @param auth auth
     */
    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
     * Get the authType.
     *
     * @return authType
     */
    public String getAuthType() {
        return this.authType;
    }

    /**
     * Set the authType.
     *
     * @param authType authType
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
     * Get the mailPort.
     *
     * @return mailPort
     */
    public String getSmtpPort() {
        return this.smtpPort;
    }

    /**
     * Set the mailPort.
     *
     * @param smtpPort smtpPort
     */
    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * Get the sendMailAddr.
     *
     * @return sendMailAddr
     */
    public String getSendMailAddr() {
        return this.sendMailAddr;
    }

    /**
     * Set the sendMailAddr.
     *
     * @param sendMailAddr sendMailAddr
     */
    public void setSendMailAddr(String sendMailAddr) {
        this.sendMailAddr = sendMailAddr;
    }

    /**
     * Get the sendMailPwd.
     *
     * @return sendMailPwd
     */
    public String getSendMailPwd() {
        return this.sendMailPwd;
    }

    /**
     * Set the sendMailPwd.
     *
     * @param sendMailPwd sendMailPwd
     */
    public void setSendMailPwd(String sendMailPwd) {
        this.sendMailPwd = sendMailPwd;
    }

    /**
     * Get the mailTitle.
     * 
     * @param mailType mailType
     * @return mailTitle
     */
    public String getMailTitle(Integer mailType) {
        return this.mailTitleMap.get(mailType);
    }

    /**
     * Set the mailTitle.
     * 
     * @param mailType mailType
     * @param mailTitle mailTitle
     */
    public void addMailTitle(Integer mailType, String mailTitle) {
        this.mailTitleMap.put(mailType, mailTitle);
    }

    /**
     * Get the mailContent.
     *
     * @param mailType mailType
     * @return mailContent
     */
    public String getMailContent(Integer mailType) {
        return this.mailContentMap.get(mailType);
    }

    /**
     * Set the mailContent.
     *
     * @param mailType mailType
     * @param mailContent mailContent
     */
    public void addMailContent(Integer mailType, String mailContent) {
        this.mailContentMap.put(mailType, mailContent);
    }

    /**
     * Get the mailTempletHost.
     *
     * @return mailTempletHost
     */
    public String getMailTempletHost() {
        return this.mailTempletHost;
    }

    /**
     * Set the mailTempletHost.
     *
     * @param mailTempletHost mailTempletHost
     */
    public void setMailTempletHost(String mailTempletHost) {
        this.mailTempletHost = mailTempletHost;
    }

    /**
     * Get the mailTempletSupporter.
     *
     * @return mailTempletSupporter
     */
    public String getMailTempletSupporter() {
        return this.mailTempletSupporter;
    }

    /**
     * Set the mailTempletSupporter.
     *
     * @param mailTempletSupporter mailTempletSupporter
     */
    public void setMailTempletSupporter(String mailTempletSupporter) {
        this.mailTempletSupporter = mailTempletSupporter;
    }
    
}
