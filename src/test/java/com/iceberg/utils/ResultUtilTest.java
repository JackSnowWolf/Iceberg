package com.iceberg.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResultUtilTest {
    @Test
    public void errorTest(){
        assertNotNull(ResultUtil.error(new Exception()));
    }
}
