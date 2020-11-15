package com.iceberg.entity;

public class ReimbursementRequest {
//    // basic fields discussed before
//    private Integer id;
//    private Integer userid;
//    private String requestdate;
//    private String requesttype;
//    private String requststatus;
//    private String picid;
//    // fields synchronized with previous project
//    private String title;
//    private Float money;
//    private String remark;
//    private String groupid;
//
//    // typeid, realname, paywayid, payway, starttime, endtime


    private Integer id;
    private String title;

    // Can change the variable type name similar to status: submitted, approved, rejected
    private String type;   //rei type: 1 rei output (reimbursement request); 2 rei input (approved reimbursement request)
    private Integer typeid;   //rei type： 1 rei output 2 rei intput

    private Float money;
    private Integer userid;

    public String getPicid() {
        return picid;
    }

    public void setPicid(String picid) {
        this.picid = picid;
    }

    private String realname;

    // This is like the type in Chong Hu's mind. We can set remark to: taxi fee, hospital fee, education...
    private String remark;

    private Integer paywayid;
    private String payway;
    private String time;
    private String startTime;
    private String endTime;
    private String groupid;

    private String picid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if("".equals(title.trim())) return;
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        if("".equals(remark.trim())) return;
        this.remark = remark;
    }

    public Integer getPaywayid() {
        return paywayid;
    }

    public void setPaywayid(Integer paywayid) {
        this.paywayid = paywayid;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        if("".equals(realname.trim())) return;
        this.realname = realname;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        if("".equals(startTime.trim())) return;
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        if("".equals(endTime.trim())) return;;
        this.endTime = endTime;
    }

    public String getgroupid() {
        return groupid;
    }

    public void setgroupid(String groupid) {
        this.groupid = groupid;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", typeid='" + typeid + '\'' +
                ", money=" + money +
                ", userid=" + userid +
                ", realname='" + realname + '\'' +
                ", remark='" + remark + '\'' +
                ", paywayid='" + paywayid + '\'' +
                ", payway='" + payway + '\'' +
                ", time='" + time + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", groupid='" + groupid + '\'' +
                '}';
    }

}
