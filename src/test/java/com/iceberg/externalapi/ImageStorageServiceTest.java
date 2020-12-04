package com.iceberg.externalapi;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;
import static com.iceberg.externalapi.ImageStorageService.writeToFile;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageStorageServiceTest {

  private File testImageFile;

  private Logger logger = LoggerFactory.getLogger(ImageStorageServiceTest.class);

  @BeforeEach
  void init() {
    ClassLoader classLoader = getClass().getClassLoader();
    URL result = classLoader.getResource("images/invoice-test-01.png");
    assertNotNull(result);
    testImageFile = new File(result.getFile());
  }

  @Test
  void shouldNotGetFileBytesForInvalidPath() {
    logger.info("test should not get file bytes");
    ClassLoader classLoader = getClass().getClassLoader();

    String imageName = "invoice-test-03.png";
    String folderPath = Objects.requireNonNull(classLoader.getResource("images")).getPath();
    String imagePath = Paths.get(folderPath, imageName).toAbsolutePath().toString();
    byte[] imageFileData = getFileBytes(imagePath);
    assertArrayEquals(new byte[0], imageFileData);
  }

  @Test
  void shouldWriteToFile() {
    logger.info("test should write to file");
    String outputImageName = "invoice-test-02.png";
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    ClassLoader classLoader = getClass().getClassLoader();
    String outputFolderPath = Objects.requireNonNull(classLoader.getResource("images")).getPath();
    String outputImagePath = Paths.get(outputFolderPath, outputImageName).toAbsolutePath()
        .toString();
    writeToFile(imageFileData, outputImagePath);
    assertNotNull(classLoader.getResource(Paths.get("images", outputImageName).toString()));
  }

  @Test
  void shouldWriteToFileIfFileExists() {
    logger.info("test should write to file");
    String outputImageName = "invoice-test-02.png";
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    ClassLoader classLoader = getClass().getClassLoader();
    String outputFolderPath = Objects.requireNonNull(classLoader.getResource("images")).getPath();
    String outputImagePath = Paths.get(outputFolderPath, outputImageName).toAbsolutePath()
        .toString();
    writeToFile(imageFileData, outputImagePath);
    assertNotNull(classLoader.getResource(Paths.get("images", outputImageName).toString()));
    writeToFile(imageFileData, outputImagePath);
  }

}
