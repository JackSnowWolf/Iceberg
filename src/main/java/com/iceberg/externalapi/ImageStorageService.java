package com.iceberg.externalapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface ImageStorageService {

  static byte[] getFileBytes(String filePath) {
    FileInputStream fileInputStream = null;
    byte[] bytesArray = null;

    try {
      File file = new File(filePath);
      bytesArray = new byte[(int) file.length()];
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytesArray);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fileInputStream != null) {
        try {
          fileInputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return bytesArray;
  }

  static void writeToFile(byte[] data, String filePath) {
    try {
      // Write the data to a local file
      File myFile = new File(filePath);
      OutputStream os = new FileOutputStream(myFile);
      os.write(data);
      System.out.println("Successfully obtained bytes from an S3 object");
      os.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  String putImage(String objectKey, String objectPath);

  String putImage(String objectKey, byte[] objectContent);

  byte[] getImageBytes(String keyName);
}
