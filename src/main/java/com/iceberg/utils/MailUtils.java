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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtils {

  public static final String posted = "You have successfully posted a reimbursement request"
      + ") on our website. "
      + "Your request is being processed. You will be noticed when your request's "
      + "status changes.\n\n"
      + "Iceberg Team";

  public static final String approved = "Your reimbursement request has been approved. "
      + "Please check your account.\n" + "Thanks for your patience.\n\n" + "Iceberg Team";

  public static final String denied = "Sorry to inform you that your reimbursement"
      + "has been denied. " + "For more information, please reach out to your account.\n\n"
      + "Iceberg Team";

  /**
   * Send email to user.
   * @param to user email to receive email.
   * @param content email content
   * @return http response
   * @throws Exception exception
   */
  public static String sendMail(String to, String content) throws Exception {

    String host = "smtp.qq.com";

    String result= "failed";

    Properties props = System.getProperties();

    props.setProperty("mail.smtp.host", host);

    props.put("mail.smtp.auth", "true");

    //SSL

    MailSSLSocketFactory sf = new MailSSLSocketFactory();

    sf.setTrustAllHosts(true);

    props.put("mail.smtp.ssl.enable", "true");

    props.put("mail.smtp.ssl.socketFactory", sf);

    Session session = Session.getDefaultInstance(props, new Authenticator() {

      @Override

      protected PasswordAuthentication getPasswordAuthentication() {

        //new PasswordAuthentication(username, password);
        return new PasswordAuthentication("icebergteam@qq.com", "sfdkjvbhuzynbdfg");
      }

    });

    try {

      Message message = new MimeMessage(session);

      message.setFrom(new InternetAddress("icebergteam@qq.com"));

      message.setRecipient(RecipientType.TO, new InternetAddress(to));

      message.setSubject("Update about your reimbursement request in our website!");

      message.setText(content);

      Transport.send(message);

      result= "success";

    } catch (Exception mex) {
      mex.printStackTrace();
    }
    return result;
  }
}
