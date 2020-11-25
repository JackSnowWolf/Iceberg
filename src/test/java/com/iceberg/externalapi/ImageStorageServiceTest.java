package com.iceberg.externalapi;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImageStorageServiceTest {

  private Logger logger = LoggerFactory.getLogger(ImageStorageServiceTest.class);

  @Resource
  private ImageStorageService imageStorageService;
}
