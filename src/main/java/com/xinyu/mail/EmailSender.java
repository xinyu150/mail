package com.xinyu.mail;

import java.io.File;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.Multipart;

import javax.mail.internet.MimeMultipart;

import javax.mail.internet.MimeBodyPart;

import javax.mail.BodyPart;

import javax.mail.PasswordAuthentication;

import javax.mail.Authenticator;

import java.util.ArrayList;

import javax.mail.MessagingException;

import javax.mail.Transport;

import javax.mail.Message;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;

import javax.mail.Session;

import java.util.Properties;

import java.util.List;

/**
 * 邮件发送类，用来发送邮件给用户.<br />
 * 
 * @author Sam
 * 
 */
public abstract class EmailSender extends Authenticator {

    private String host;
    private String port;
    private String username;
    private String password;
    private String from;

    public EmailSender() {
        this.host = PropertiesUtil.getProperty(Contanst.EMAIL_HOST);
        this.port = PropertiesUtil.getProperty(Contanst.EMAIL_HOST_PORT);
        this.username = PropertiesUtil.getProperty(Contanst.EMAIL_USER_NAME);
        this.password = PropertiesUtil.getProperty(Contanst.EMAIL_PASSWORD);
        this.from = PropertiesUtil.getProperty(Contanst.EMAIL_SENDER);
    }

    /**
     * EmailSender构造函数，需要用户提供host,port,username,password,from等信息。<br />
     * 
     * @param host
     *            ,smtp服务器地址
     * @param port
     *            ,smtp服务器端口
     * @param username
     *            ,邮箱用户名
     * @param password
     *            ,邮箱密码
     * @param from
     *            ,邮箱发送人
     */
    public EmailSender(String host, String port, String username,
            String password, String from) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.from = from;
    }

    /**
     * 发送邮件到指定用户
     * 
     * @param to
     *            , 邮件发送对象
     * @return ture 发送成功, false发送失败
     */
    public Boolean send(String to) {

        List<String> toList = new ArrayList<>();
        toList.add(to);

        return send(toList);
    }

    /**
     * 群发邮件
     * 
     * @param toList
     *            ,需要接受邮件的用户
     * @return ture 发送成功, false发送失败
     */
    public Boolean send(List<String> toList) {

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");

        // create email authenticator.
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties, authenticator);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for (String to : toList) {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
            }

            // Set Subject: header field
            message.setSubject(getSubject(), "utf-8");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setContent(getContent(), "text/html;charset=utf-8");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // add attachment
            List<File> attchments = getAttachments();
            if (attchments != null && attchments.size() > 0) {
                for (File attachment : attchments) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachment);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachment.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();

            return false;
        }

        return true;
    }

    /**
     * 获取邮件主题，支持html格式。
     * 
     * @return
     */
    protected abstract String getSubject();

    /**
     * 获取邮件内容，支持html格式。
     * 
     * @return
     */
    protected abstract String getContent();

    /**
     * 获取附件列表，若不需要发送附件，请返回null或长度为0的List<File>列表.<br />
     * 
     * @return
     */
    protected abstract List<File> getAttachments();

}
