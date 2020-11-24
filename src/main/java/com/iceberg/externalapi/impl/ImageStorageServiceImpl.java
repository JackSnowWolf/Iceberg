package com.iceberg.externalapi.impl;

import static com.iceberg.externalapi.ImageStorageService.getFileBytes;

import com.iceberg.externalapi.ImageStorageService;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
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

  @PostConstruct
  public void init() {
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
      System.err.println(e.getMessage());
      System.exit(1);
    }
    return "";
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
      System.err.println(e.getMessage());
      System.exit(1);
    }
    return "";
  }

  @Override
  public byte[] getImageBytes(String keyName) {
    byte[] data = new byte[0];
    try {
      GetObjectRequest objectRequest = GetObjectRequest
          .builder()
          .key(keyName)
          .bucket(bucketName)
          .build();
      ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
      data = objectBytes.asByteArray();

    } catch (S3Exception e) {
      System.err.println(e.awsErrorDetails().errorMessage());
      System.exit(1);
    }
    return data;
  }
}
