package com.iceberg.externalapi.impl;

import com.iceberg.externalapi.ProcessingApi;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

@Service
public class ProcessingImpl implements ProcessingApi {
    private final String clientId = "vrf9P6zMD702DTo06CoKx84BkWfcTn6IzSO1BbO";
    private final String apiKey = "apikey cchenwenjie0901:bcf35a2054c64c3f203ab6ff05d54c93";
    private final String URL = "https://api.veryfi.com/api/v7/partner/documents/";

    @Override
    public ResponseEntity<String> processDocumentBinary(String inputfilename) throws IOException{
        //
        String filename = "example.png";
        filename=inputfilename;

        File imgPath = new File(filename);
        BufferedImage bufferedImage = ImageIO.read(imgPath);
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(filename)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(data.getData(), fileMap);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);
        body.add("file_name", filename);
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);
            return response;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> processDocumentBase64(String filedata) {
        //
        String fileData = "image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVQYV2NgYAAAAAMAAWgmWQ0AAAAASUVORK5CYII=";
        fileData=filedata;

        String fileName = "example.png";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("file_name", fileName);
        requestBody.put("file_data", fileData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("CLIENT-ID", clientId);
        headers.add("AUTHORIZATION", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);
        return response;
    }

    @Override
    public ResponseEntity<String> processDocumentURL(String fileurl) {
        //
        String fileURL = "https://cdn.veryfi.com/receipts/71f19687-a0f5-48ef-a4d7-1458e83aa6b2_2.png";
        fileURL=fileurl;

        String fileName = "invoice.png";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("file_name", fileName);
        requestBody.put("file_url", fileURL);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("CLIENT-ID", clientId);
        headers.add("AUTHORIZATION", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);
        return response;
    }
}