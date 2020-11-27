package com.iceberg.entity;

public class InvoiceDetail {
    private Float money;
    private String vendorname;
    private String vendoraddr;
    private String duedate;

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getVendoraddr() {
        return vendoraddr;
    }

    public void setVendoraddr(String vendoraddr) {
        this.vendoraddr = vendoraddr;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }
}
