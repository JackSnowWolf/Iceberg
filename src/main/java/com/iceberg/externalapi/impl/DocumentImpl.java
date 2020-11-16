package com.iceberg.externalapi.impl;

import com.iceberg.externalapi.DocumentApi;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

public class DocumentImpl implements DocumentApi {
    private final String clientId = "vrfvVZXJ3fFUbwz1F639VpNL0aeMawcaJYMcd5F";
    private final String apiKey = "apikey jackchonghu:b7bf3a83c40fb631e003f0f0ab92d0f8";

    @Override
    public void updateDocumentDetails() {

    }

    @Override
    public void getAllDocuments() {

    }

    @Override
    public void getSingleDocuments() {
        //
        String documentId = "DOCUMENT_ID";

        String URL = "https://api.veryfi.com/api/v7/partner/documents/" + documentId + "/";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("CLIENT-ID", clientId);
        headers.add("AUTHORIZATION", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
        response.getStatusCode();
    }

    @Override
    public void deleteSingleDocument() {

    }
}
