package com.iceberg.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReimbursementRequest {

  // basic fields discussed before
  private Integer id;
  private Integer userid;
  private String requestdate;
  private type requesttype;
  private String imageid;
  private String title;
  private Float money;
  private String remark;
  private String groupid;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  // typeid, realname, paywayid, payway, starttime, endtime

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

  public type getRequesttype() {
    return requesttype;
  }

  public void setRequesttype(type requesttype) {
    this.requesttype = requesttype;
  }

  public String getImageid() {
    return imageid;
  }

  public void setImageid(String imageid) {
    this.imageid = imageid;
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

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }

  public enum type {
    PROCESSING,
    MISSING_INFO,
    DENIED,
    APPROVED
  }

}
