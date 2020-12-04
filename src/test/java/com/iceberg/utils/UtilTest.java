package com.iceberg.utils;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilTest {

    @Test
    public void dataToStrTest(){
        Date date=new Date();
        assertNotNull(Utils.dateToStr(date));
        assertNotNull(Utils.dateToStr(null));
    }
}
