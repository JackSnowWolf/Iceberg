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
  private TYPE requesttype;
  private String imageid;
  private String title;
  private Float money;
  private String remark;
  private String groupid;
  private int paywayid;

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

  public TYPE getRequesttype() {
    return requesttype;
  }

  public void setRequesttype(TYPE requesttype) {
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

  public int getPaywayid() {
    return this.paywayid;
  }

  public void setPaywayid(int paywayid) {
    this.paywayid = paywayid;
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
