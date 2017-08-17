package com.chinaplus.core.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaplus.common.consts.ChinaPlusConst.Language;
import com.chinaplus.core.bean.MailConfig;
import com.chinaplus.core.consts.NumberConst.IntDef;
import com.chinaplus.core.consts.StringConst;

/**
 * 
 * MailUtil
 * 
 * @author xiang_chao
 *
 */
public class MailUtil {
    /** Mail Template File: Not Registered Parts */
    public static final String MAIL_TEMPLATE_NOT_REGISTERED_PARTS = "Mail_NotRegisteredParts";

    /** Mail Template File: Shipping Route */
    public static final String MAIL_TEMPLATE_SHIPPING_ROUTE = "Mail_ShippingRoute";

    /** Mail Template File: Stock Alarm */
    public static final String MAIL_TEMPLATE_STOCK_ALARM = "Mail_StockAlarm";

    /** Mail Template File: Stock Alarm */
    public static final String MAIL_TEMPLATE_FILE_TYPE = "{0}/{1}.txt";

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(MailUtil.class);

    private Integer mailType;
    /** MimeMessage */
    private MimeMessage mimeMsg;
    /** Mail Session */
    private Session session;
    /** Multipart */
    private Multipart mp;

    /**
     * 
     * The Constructors Method.
     *
     * @throws AddressException e
     * @throws MessagingException e
     */
    public MailUtil() throws AddressException, MessagingException {
        logger.info("Create a mail for type:" + mailType);
        MailConfig mailConfig = ConfigUtil.getMailConfig();

        mailConfig.setTransport(ConfigUtil.get("mail.transport.protocol"));
        mailConfig.setAuth(ConfigUtil.get("mail.smtp.auth"));
        mailConfig.setAuthType(ConfigUtil.get("mail.smtp.auth.type"));
        mailConfig.setSmtpPort(ConfigUtil.get("mail.smtp.port"));
        mailConfig.setSendMailAddr(ConfigUtil.get("mail.host.user"));
        mailConfig.setSendMailPwd(ConfigUtil.get("mail.host.pwd"));
        mailConfig.setSmtpHost(ConfigUtil.get("mail.smtp.host"));
        mailConfig.setMailTempletHost(ConfigUtil.get("mail.templet.host"));
        mailConfig.setMailTempletSupporter(ConfigUtil.get("mail.templet.supporter"));

        logger.info("Creating mail properties..");
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", mailConfig.getAuth());
        props.setProperty("mail.smtp.host", mailConfig.getSmtpHost());
        props.setProperty("mail.transport.protocol", mailConfig.getTransport());
        if ("tls".equals(mailConfig.getAuthType().toLowerCase())) {
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.ssl.checkserveridentity", "false");
            props.setProperty("mail.smtp.ssl.trust", mailConfig.getSmtpHost());
        } else if ("ssl".equals(mailConfig.getAuthType().toLowerCase())) {
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.protocol.socketFactory.port", mailConfig.getSmtpPort());
        }
        props.setProperty("mail.smtp.port", mailConfig.getSmtpPort());

        logger.info("Creating mail session..");
        session = Session.getDefaultInstance(props, null);

        logger.info("Creating mime message...");
        mimeMsg = new MimeMessage(session);
        mimeMsg.setFrom(new InternetAddress(mailConfig.getSendMailAddr()));

        mp = new MimeMultipart("mixed");

        logger.info("Mail for type:" + mailType + " initialled");
    }

    /**
     * Send Mail
     * 
     * @param to To address
     * @param title Email title
     * @param body Email body
     * @param files attachment file object
     * @param isHtmlMail true: is html mail; false: is text mail
     * @param tempFolder the temp file(folder) which is need to delete after send mail
     * @return true: successful, false: fail
     * @throws Exception e
     */
    private boolean send(String to, String title, String body, final Vector<File> files,
        boolean isHtmlMail, File tempFolder) throws Exception {
        boolean result = false;
        logger.info("Set infomation for mail type:" + mailType);
        MailConfig mailConfig = ConfigUtil.getMailConfig();
        mimeMsg.setSubject(title);
        mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

        BodyPart bp = new MimeBodyPart();
        String content = new String(body.getBytes(), "utf-8");
        if (isHtmlMail) {
            // html mail
            bp.setContent(content, "text/html;charset=utf-8");
        } else {
            // text mail
            bp.setContent(content, "text/plain;charset=utf-8");
        }
        mp.addBodyPart(bp);

        // set mail file
        if (files != null && files.size() > 0) {
            Enumeration<File> efile = files.elements();
            while (efile.hasMoreElements()) {
                MimeBodyPart mbpFile = new MimeBodyPart();
                String fileName = efile.nextElement().toString();
                FileDataSource fds = new FileDataSource(fileName);
                mbpFile.setDataHandler(new DataHandler(fds));
                String filename = new String(fds.getName().getBytes(), "ISO-8859-1");
                mbpFile.setFileName(filename);
                mp.addBodyPart(mbpFile);
            }
            files.removeAllElements();
        }
        mimeMsg.setContent(mp);

        mimeMsg.saveChanges();

        logger.info("Mail Sending......");
        Transport transport = session.getTransport("smtp");
        try {
            logger.info("Mail Server Conection... : " + mailConfig.getSmtpHost() + " " + mailConfig.getSmtpPort() + " "
                    + mailConfig.getSendMailAddr() + " " + mailConfig.getSendMailPwd());
            transport.connect(mailConfig.getSmtpHost(), Integer.parseInt(mailConfig.getSmtpPort()),
                mailConfig.getSendMailAddr(), mailConfig.getSendMailPwd());
            transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
        } catch (Exception e) {
            logger.error("Mail Server Conection error.");
            throw e;
        } finally {
            transport.close();

            // delete temp folder
            if (tempFolder != null && tempFolder.exists()) {
                FileUtil.deleteAllFile(tempFolder);
            }
        }
        result = true;
        logger.info("Mail Send OK.");

        return result;
    }

    /**
     * send mail for batch
     * 
     * @param to To address
     * @param title Email title
     * @param body Email body
     * @param files attachment file object
     * @param isHtmlMail true: is html mail; false: is text mail
     * @param tempFolder the temp file(folder) which is need to delete after send mail
     */
    public void sendMailForBatch(String to, String title, String body, final Vector<File> files,
        boolean isHtmlMail, File tempFolder) {
        try {
            send(to, title, body, files, isHtmlMail, tempFolder);
        } catch (Exception e) {
            logger.error("Mail Send error." + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Get mail template
     * 
     * @param templatePath mail template path
     * @param lang language
     * @return mail template
     * @throws IOException IOException
     */
    public static String getTemplate(String templatePath, Language lang) throws IOException {

        // get last index of point
        int offSet = templatePath.lastIndexOf(StringConst.DOT);

        // must be txt file.
        StringBuffer sbTemplatePath = new StringBuffer();
        sbTemplatePath.append(templatePath.substring(IntDef.INT_ZERO, offSet));
        sbTemplatePath.append(StringConst.UNDERLINE);
        sbTemplatePath.append(lang.getName());
        sbTemplatePath.append(templatePath.substring(offSet));

        // get
        return getTemplate(sbTemplatePath.toString());
    }

    /**
     * Get mail template for batch
     * 
     * @param templatePath mail template path
     * @return mail template
     * @throws IOException IOException
     */
    public static String getTemplate(String templatePath) throws IOException {

        InputStream is = MailUtil.class.getResourceAsStream(templatePath);

        String fileContent = "";
        try {
            fileContent = IOUtils.toString(is, Charsets.toCharset("utf-8"));
        } finally {
            IOUtils.closeQuietly(is);
        }

        return fileContent;
    }
}
