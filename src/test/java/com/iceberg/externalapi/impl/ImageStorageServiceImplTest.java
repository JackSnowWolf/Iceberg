package com.iceberg.externalapi.impl;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Base64;
import javax.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ImageStorageServiceImplTest {

  private File testImageFile;

  private Logger logger = LoggerFactory.getLogger(ImageStorageServiceImplTest.class);

  @Resource
  private ImageStorageServiceImpl imageStorageService;

  @BeforeEach
  void init() {
    ClassLoader classLoader = getClass().getClassLoader();
    URL result = classLoader.getResource("images/invoice-test-01.png");
    assertNotNull(result);
    testImageFile = new File(result.getFile());
  }

  @Test
  void shouldPutImageFromFile() {
    logger.info("test put image from file");
    String response = imageStorageService
        .putImage("invoice-test-01.png", testImageFile.getAbsolutePath());
    assertNotEquals("", response);
  }

  @Test
  void shouldPutImageFromBytes() {
    logger.info("test put image from bytes");
    byte[] data = getFileBytes(testImageFile.getAbsolutePath());
    String response = imageStorageService.putImage("invoice-test-01.png", data);
    assertNotEquals("", response);
  }

  @Test
  void shouldGetImageBytes() {
    logger.info("test get image bytes");
    byte[] expectedData = getFileBytes(testImageFile.getAbsolutePath());
    byte[] data = imageStorageService.getImageBytes("invoice-test-01.png");

    assertEquals(Base64.getEncoder().encodeToString(expectedData),
        Base64.getEncoder().encodeToString(data));
  }
}
