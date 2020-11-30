package com.iceberg.externalapi;

import com.iceberg.entity.InvoiceDetail;
import com.iceberg.externalapi.impl.ProcessingForRequestImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.Base64;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProcessingForRequestImplTest {
//    private Logger logger = LoggerFactory.getLogger(ProcessingForRequestImplTest.class);
//    private File testImageFile;
//
//    @Resource
//    private ProcessingForRequestImpl processingForRequest;
//
//    @BeforeEach
//    void init() {
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL result = classLoader.getResource("images/invoice-test-01.png");
//        assertNotNull(result);
//        testImageFile = new File(result.getFile());
//    }
//
//    @Test
//    public void parseFromDocumentBase64Test(){
//        byte[] fileBytes = getFileBytes(testImageFile.getAbsolutePath());
//        String encoded= Base64.getEncoder().encodeToString(fileBytes);
//        InvoiceDetail invoiceDetail=processingForRequest.parseFromDocumentBase64(encoded);
//        logger.info(invoiceDetail.toString());
//        assertEquals("4520.0",invoiceDetail.getMoney().toString());
//    }
//
//    @Test
//    public void parseFromDocumentURLTest(){
//        InvoiceDetail invoiceDetail=processingForRequest.parseFromDocumentURL("https://s3.amazonaws.com/isc.pricescout.media/receipts/be04115eb8824719914cfbcfd7e792c2.jpg");
//        logger.info(invoiceDetail.toString());
//        assertNotNull(invoiceDetail);
//    }

}
