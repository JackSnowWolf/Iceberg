package com.iceberg.entity;

import lombok.Data;

@Data
public class InvoiceDetail {
    private Float money;
    private String vendorname;
    private String vendoraddr;
    private String duedate;
}
