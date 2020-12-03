package com.iceberg.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpClientUtilsTest {

    @Test
    public void doGetTest(){
        String URL = "https://example.com";
        Integer status = HttpClientUtils.doGet(URL);
        Assert.assertEquals("200", String.valueOf(status));
    }

    @Test
    public void doPostTest(){
        String URL = "https://example.com";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("key", "value");
        String result = HttpClientUtils.doPost(URL, paramMap);
        assertNotNull(result);
    }

    @Test
    public void exceptionTest() throws Exception{
//        Throwable throwable1=assertThrows(Exception.class,()->{
//            HttpClientUtils.doGet("not a url");
//        });
        try{
            HttpClientUtils.doGet(null);
        }catch(Exception e){
            System.out.print(e);
            assertNotNull(e);
        }
        try{
            HttpClientUtils.doPost(null,null);
        }catch(Exception e2){
            System.out.print(e2);
            assertNotNull(e2);
        }
    }
}
