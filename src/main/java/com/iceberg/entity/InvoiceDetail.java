package com.iceberg.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
