package com.iceberg.externalapi.impl;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.externalapi.EmailSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSendServiceImpl implements EmailSendService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public String postConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest) {
    String to = userInfo.getUsername();
    int requestId = reimbursementRequest.getId();
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("iceberg4156@gmail.com");
    msg.setTo(to);
    msg.setSubject("Reimbursement Post Confirmation");
    String text = "Dear " + to + ",\n" + "You have successfully posted a reimbursement request (" + requestId
        + ") on our website. "
        + "Your request is being processed. You will be noticed when your request's status changes.\n\n"
        + "Iceberg Team";
    msg.setText(text);
    try {
      javaMailSender.send(msg);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return "success";
  }

  @Override
  public String approveConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest) {
    String to = userInfo.getUsername();
    int requestId = reimbursementRequest.getId();
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("iceberg4156@gmail.com");
    msg.setTo(to);
    msg.setSubject("Reimbursement Request Approved");
    String text = "Dear " + to + ",\n" + "Your reimbursement request (" + requestId + ") has been approved. "
        + "Please check your account.\n" + "Thanks for your patience.\n\n" + "Iceberg Team";
    msg.setText(text);
    try {
      javaMailSender.send(msg);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return "success";
  }

  @Override
  public String denyConfirm(UserInfo userInfo, ReimbursementRequest reimbursementRequest) {
    String to = userInfo.getUsername();
    int requestId = reimbursementRequest.getId();
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("iceberg4156@gmail.com");
    msg.setTo(to);
    msg.setSubject("Reimbursement Request Approved");
    String text = "Dear " + to + ",\n" + "Sorry to inform you that your reimbursement request (" + requestId
        + ") has been denied. " + "For more information, please reach out to your account.\n\n" + "Iceberg Team";
    msg.setText(text);
    try {
      javaMailSender.send(msg);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    return "success";
  }
}
