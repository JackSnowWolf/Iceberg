package com.iceberg.externalapi;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProcessingImplTest {
    private Logger logger = LoggerFactory.getLogger(ProcessingImplTest.class);
    private File testImageFile;
    private String filename;

    @Resource
    private ProcessingApi processingApi;

    @BeforeEach
    void init() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL result = classLoader.getResource("images/invoice-test-01.png");
        assertNotNull(result);
        filename=result.getFile();
        testImageFile = new File(result.getFile());
    }

    @Test
    public void processDocumentURLTest(){
        ResponseEntity<String> response=processingApi.processDocumentURL("https://s3.amazonaws.com/isc.pricescout.media/receipts/be04115eb8824719914cfbcfd7e792c2.jpg");
        JSONObject test= JSONObject.parseObject(response.getBody());
        String money=test.getString("total");
        String vendor_name=test.getJSONObject("vendor").getString("name");
        logger.info(money);
        logger.info(vendor_name);
        assertNotNull(response);
    }

    @Test
    public void processDocumentBase64Test(){
        byte[] fileBytes = getFileBytes(testImageFile.getAbsolutePath());
        String encoded= Base64.getEncoder().encodeToString(fileBytes);
        ResponseEntity<String> response=processingApi.processDocumentBase64(encoded);
        JSONObject test= JSONObject.parseObject(response.getBody());
        String money=test.getString("total");
        String vendor_name=test.getJSONObject("vendor").getString("name");
        logger.info(money);
        logger.info(vendor_name);
        assertNotNull(response);
    }

    @Test
    public void processDocumentBinaryTest() throws IOException {

    }
}
