package com.iceberg.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

public class ReimbursementRequest {

  // basic fields discussed before
  private Integer id;
  private Integer userid;
  private String requestdate;
  private TYPE typeid;
  private String imageid;
  private String title;
  private Float money;
  private String remark;
  private String groupid;
  private String groupname;
  private Integer paywayid;
  private String receiveraccount;
  private String startTime;
  private String endTime;
  private String realname;
  private String time;

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    if ("".equals(startTime.trim())) {
      return;
    }
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    if ("".equals(endTime.trim())) {
      return;
    }
    ;
    this.endTime = endTime;
  }


  public String getReceiveraccount() {
    return receiveraccount;
  }

  public void setReceiveraccount(String receiveraccount) {
    this.receiveraccount = receiveraccount;
  }

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


  public TYPE getTypeid() {
    return typeid;
  }

  public void setTypeid(int typeid) {
    this.typeid = TYPE.valueOf(typeid);
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

  public Integer getPaywayid() {
    return this.paywayid;
  }

  public void setPaywayid(Integer paywayid) {
    this.paywayid = paywayid;
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
    if ("".equals(realname.trim())) {
      return;
    }
    this.realname = realname;
  }

  public String getGroupname() {
    return this.groupname;
  }

  public void setGroupname(String groupname) {
    this.groupname = groupname;
  }

  @Override
  public boolean equals(Object obj) {
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    return this.toString().equals(obj.toString());
  }

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }

  public enum TYPE {
    PROCESSING(0),
    MISSING_INFO(1),
    DENIED(2),
    APPROVED(3);

    private static Map map = new HashMap<>();

    static {
      for (TYPE type : TYPE.values()) {
        map.put(type.value, type);
      }
    }

    public int value;

    TYPE(int value) {
      this.value = value;
    }

    public static TYPE valueOf(int pageType) {
      return (TYPE) map.get(pageType);
    }

    public int getValue() {
      return value;
    }


  }
}
