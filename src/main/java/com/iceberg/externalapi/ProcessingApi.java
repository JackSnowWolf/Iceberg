package com.iceberg.externalapi;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ProcessingApi {
    ResponseEntity<String> processDocumentBinary(String inputfilename) throws IOException;
    ResponseEntity<String> processDocumentBase64(String filedata);
    ResponseEntity<String> processDocumentURL(String url);
}
