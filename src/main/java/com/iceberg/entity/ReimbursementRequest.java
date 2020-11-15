package com.iceberg.entity;

public class ReimbursementRequest {
    // basic fields discussed before
    private Integer id;
    private Integer userid;
    private String requestdate;
    private String requesttype;
    private String requststatus;
    private String picid;
    // fields synchronized with previous project
    private String title;
    private Float money;
    private String remark;
    private String groupid;

    // typeid, realname, paywayid, payway, starttime, endtime

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}
