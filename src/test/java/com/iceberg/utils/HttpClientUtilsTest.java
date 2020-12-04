package com.iceberg.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpClientUtilsTest {

    @Test
    public void doGetTest(){
        String URL = "https://example.com";
        Integer status = HttpClientUtils.doGet(URL);
        Assert.assertEquals("200", String.valueOf(status));
        String URL2="https://httpbin.org/status/:code";
        Integer status2 = HttpClientUtils.doGet(URL2);
        System.out.println(status2);
        assertNotNull(status2);
    }

    @Test
    public void doPostTest(){
        String URL = "https://example.com";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("key", "value");
        String result = HttpClientUtils.doPost(URL, paramMap);
        assertNotNull(result);
        String result2=HttpClientUtils.doPost(URL, null);
        assertNotNull(result2);

        String URL2="https://httpbin.org/status/:code";
        String result3=HttpClientUtils.doPost(URL2, null);
        System.out.println(result3);
    }

    @Test
    public void exceptionTest() throws Exception{
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
        try{
            HttpClientUtils.doPost("null",null);
        }catch(Exception e3){
            System.out.print(e3);
            assertNotNull(e3);
        }
    }
}
