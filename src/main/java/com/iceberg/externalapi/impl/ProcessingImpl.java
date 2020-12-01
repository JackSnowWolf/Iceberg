package com.iceberg.externalapi.impl;

import com.iceberg.externalapi.ProcessingApi;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProcessingImpl implements ProcessingApi {
    private final String clientId = "vrf9AXE1sCrpn0G8nonehgPcBPBnf74LZ4REixK";
    private final String apiKey = "apikey 229054463:43088d557fcef244e31d34829440abde";
    private final String URL = "https://api.veryfi.com/api/v7/partner/documents/";

    @Override
    public ResponseEntity<String> processDocumentBase64(String filedata) {
        String fileName = "example.png";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("file_name", fileName);
        requestBody.put("file_data", filedata);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("CLIENT-ID", clientId);
        headers.add("AUTHORIZATION", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        return restTemplate.postForEntity(URL, entity, String.class);
    }

    @Override
    public ResponseEntity<String> processDocumentURL(String fileurl) {
        String fileName = "invoice.png";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("file_name", fileName);
        requestBody.put("file_url", fileurl);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("CLIENT-ID", clientId);
        headers.add("AUTHORIZATION", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
        return restTemplate.postForEntity(URL, entity, String.class);
    }
}