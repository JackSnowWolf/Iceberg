package com.iceberg.externalapi.impl;

import com.iceberg.externalapi.ProcessingApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.List;

@Service
public class ProcessingForRequestImpl {
    @Resource
    private ProcessingApi processingApi;

    public String[] parseFromDocumentURL(String url){
        String[] res=new String[4];
        ResponseEntity<String> response=processingApi.processDocumentURL(url);
        JSONObject jsonObject= JSONObject.parseObject(response.getBody());
        res[0]=jsonObject.getString("total");
        res[1]=jsonObject.getJSONObject("vendor").getString("name");
        res[2]=jsonObject.getJSONObject("vendor").getString("address");
        res[3]=jsonObject.getString("due_date");
        return res;
    }

    public String[] parseFromDocumentBase64(String filedata){
        String[] res=new String[4];
        ResponseEntity<String> response=processingApi.processDocumentBase64(filedata);
        JSONObject jsonObject= JSONObject.parseObject(response.getBody());
        res[0]=jsonObject.getString("total");
        res[1]=jsonObject.getJSONObject("vendor").getString("name");
        res[2]=jsonObject.getJSONObject("vendor").getString("address");
        res[3]=jsonObject.getString("due_date");
        return res;
    }

    public String[] parseFromDocumentBinary(String inputfilename) throws IOException {
        String[] res=new String[4];
        ResponseEntity<String> response=processingApi.processDocumentBinary(inputfilename);
        JSONObject jsonObject= JSONObject.parseObject(response.getBody());
        res[0]=jsonObject.getString("total");
        res[1]=jsonObject.getJSONObject("vendor").getString("name");
        res[2]=jsonObject.getJSONObject("vendor").getString("address");
        res[3]=jsonObject.getString("due_date");
        return res;
    }
}
