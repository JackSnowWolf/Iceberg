package com.iceberg.externalapi.impl;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;

import com.iceberg.externalapi.ImageStorageService;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {

  private final String bucketName = "iceberg-image-bucket";
  private final Region region = Region.US_EAST_1;
  private S3Client s3Client;

  private Logger logger = LoggerFactory.getLogger(ImageStorageService.class);

  /**
   * init ImageStorageServiceImpl.
   */
  @PostConstruct
  public void init() {
    logger.info("Setting aws access key.");
    System.setProperty("aws.accessKeyId", "AKIAZUGB5BLKN2OTIUWG");
    System.setProperty("aws.secretAccessKey", "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");

    s3Client = S3Client.builder().region(this.region)
        .credentialsProvider(SystemPropertyCredentialsProvider.create())
        .build();
  }

  @Override
  public String putImage(String objectKey, String objectPath) {
    try {
      PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
              .bucket(bucketName)
              .key(objectKey)
              .build(),
          RequestBody.fromBytes(getFileBytes(objectPath)));

      return response.eTag();

    } catch (S3Exception e) {
      logger.error(e.awsErrorDetails().errorMessage());
    }
    return null;
  }

  @Override
  public String putImage(String objectKey, byte[] objectContent) {
    try {
      PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
              .bucket(bucketName)
              .key(objectKey)
              .build(),
          RequestBody.fromBytes(objectContent));

      return response.eTag();

    } catch (S3Exception e) {
      logger.error(e.awsErrorDetails().errorMessage());
    }
    return null;
  }

  @Override
  public byte[] getImageBytes(String keyName) {
    byte[] data;
    try {
      GetObjectRequest objectRequest = GetObjectRequest
          .builder()
          .key(keyName)
          .bucket(bucketName)
          .build();
      ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
      data = objectBytes.asByteArray();

    } catch (S3Exception e) {
      logger.error(e.awsErrorDetails().errorMessage());
      return null;
    }
    return data;
  }
}
