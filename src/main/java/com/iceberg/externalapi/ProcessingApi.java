package com.iceberg.externalapi;

import java.io.IOException;

public interface ProcessingApi {
    void processDocumentBinary(String inputfilename) throws IOException;
    void processDocumentBase64(String filedata);
    void processDocumentURL(String url);
}
