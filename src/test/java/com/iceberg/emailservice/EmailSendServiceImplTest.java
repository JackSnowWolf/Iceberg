package com.iceberg.emailservice;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.emailservice.impl.EmailSendServiceImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class EmailSendServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(EmailSendServiceImplTest.class);
    @Autowired
    private JavaMailSender javaMailSender;
    private EmailSendService emailSendService = new EmailSendServiceImpl(javaMailSender);
    private UserInfo userInfo;
    private ReimbursementRequest reimbursementRequest;

    /**
     * Test confirmation email sending when a user
     * posts a rei request
     */
    @Test
    public void postConfirmTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("chenzeyang6@gmail.com");
        userInfo.setUsername("czy");
        ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
        reimbursementRequest.setId(12345);
        String s = emailSendService.postConfirm(userInfo, reimbursementRequest);
        assertEquals("success", s);
    }

    /**
     * Test confirmation email sending when a
     * rei request is approved.
     */
    @Test
    public void approveConfirmTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("czy");
        userInfo.setEmail("chenzeyang6@gmail.com");
        ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
        reimbursementRequest.setId(12345);
        String s = emailSendService.approveConfirm(userInfo, reimbursementRequest);
        assertEquals("success", s);

    }

    /**
     * Test confirmation email sending when a
     * rei request is denied.
     */
    @Test
    public void denyConfirmTest(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("czy");
        userInfo.setEmail("chenzeyang6@gmail.com");
        ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
        reimbursementRequest.setId(12345);
        String s = emailSendService.denyConfirm(userInfo, reimbursementRequest);
        assertEquals("success", s);
    }

}
