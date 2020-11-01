package com.iceberg.entity;

public class ReimbursementRequest {
    private Integer id;
    private Integer userid;
    private String requestdate;
    private String requesttype;
    private String requststatus;
    private String picid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getRequststatus() {
        return requststatus;
    }

    public void setRequststatus(String requststatus) {
        this.requststatus = requststatus;
    }

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }
}
