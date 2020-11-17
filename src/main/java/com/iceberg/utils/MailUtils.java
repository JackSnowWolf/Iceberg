package com.iceberg.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import java.util.Properties;



import javax.mail.Authenticator;

import javax.mail.Message;

import javax.mail.Message.RecipientType;

import javax.mail.MessagingException;

import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.AddressException;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;

public class MailUtils {

  public static final String posted = "You have successfully posted a reimbursement request"
      + ") on our website. "
      + "Your request is being processed. You will be noticed when your request's status changes.\n\n"
      + "Iceberg Team";

  public static final String approved = "Your reimbursement request has been approved. "
      + "Please check your account.\n" + "Thanks for your patience.\n\n" + "Iceberg Team";

  public static final String denied = "Sorry to inform you that your reimbursement"
      + "has been denied. " + "For more information, please reach out to your account.\n\n" + "Iceberg Team";

  public static String sendMail(String to, String content) throws Exception{


    String host = "smtp.qq.com";

    Properties props = System.getProperties();

    props.setProperty("mail.smtp.host", host);

    props.put("mail.smtp.auth", "true");

    //SSL

    MailSSLSocketFactory sf = new MailSSLSocketFactory();

    sf.setTrustAllHosts(true);

    props.put("mail.smtp.ssl.enable","true");

    props.put("mail.smtp.ssl.socketFactory", sf);

    //props：用来设置服务器地址，主机名；Authenticator：认证信息

    Session session = Session.getDefaultInstance(props,new Authenticator() {

      @Override

      //通过密码认证信息

      protected PasswordAuthentication getPasswordAuthentication() {

        //new PasswordAuthentication(用户名, password);

        //这个用户名密码就可以登录到邮箱服务器了,用它给别人发送邮件

        return new PasswordAuthentication("1603118971@qq.com","urjbvhzgycqvhfdh");

      }

    });

    try {

      Message message = new MimeMessage(session);

      //2.1sender

      message.setFrom(new InternetAddress("1603118971@qq.com"));

      //2.2设置收件人 这个TO就是收件人

      message.setRecipient(RecipientType.TO, new InternetAddress(to));

      //2.3邮件的主题

      message.setSubject("Update about your reimbursement request in our website!");

      //2.4设置邮件的正文 第一个参数是邮件的正文内容 第二个参数是：是文本还是html的连接

      message.setText(content);

      //3.发送一封激活邮件

      Transport.send(message);

      return "success";

    }catch(MessagingException mex){

      mex.printStackTrace();
      return "failed";
    }
  }
}
