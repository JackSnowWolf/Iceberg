package com.iceberg.utils;

import com.iceberg.utils.MailUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MailUtilsTest {

    @Test
    public void testSendEmail() throws Exception {
        String emailAddress = "hwj97055@gmail.com";
        String res = MailUtils.sendMail(emailAddress, MailUtils.approved);
        assertNotNull(res);
    }

    @Test
    public void exceptionTest() throws Exception {
        try {
            MailUtils.sendMail(null, null);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}
