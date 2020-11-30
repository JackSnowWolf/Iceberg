package com.iceberg.externalapi.impl;


import com.iceberg.entity.InvoiceDetail;
import com.iceberg.externalapi.ProcessingApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;

@Service
public class ProcessingForRequestImpl {
    @Resource
    private ProcessingApi processingApi;
  
    public InvoiceDetail parseFromDocumentURL(String url){
        InvoiceDetail invoiceDetail=new InvoiceDetail();
        ResponseEntity<String> response=processingApi.processDocumentURL(url);
        JSONObject jsonObject= JSONObject.parseObject(response.getBody());
        invoiceDetail.setMoney(jsonObject.getFloat("total"));
        invoiceDetail.setVendorname(jsonObject.getJSONObject("vendor").getString("name"));
        invoiceDetail.setVendoraddr(jsonObject.getJSONObject("vendor").getString("address"));
        invoiceDetail.setDuedate(jsonObject.getString("due_date"));
        return invoiceDetail;
    }

    public InvoiceDetail parseFromDocumentBase64(String filedata){
        InvoiceDetail invoiceDetail=new InvoiceDetail();
        ResponseEntity<String> response=processingApi.processDocumentBase64(filedata);
        JSONObject jsonObject= JSONObject.parseObject(response.getBody());
        invoiceDetail.setMoney(jsonObject.getFloat("total"));
        invoiceDetail.setVendorname(jsonObject.getJSONObject("vendor").getString("name"));
        invoiceDetail.setVendoraddr(jsonObject.getJSONObject("vendor").getString("address"));
        invoiceDetail.setDuedate(jsonObject.getString("due_date"));
        return invoiceDetail;
    }

}
