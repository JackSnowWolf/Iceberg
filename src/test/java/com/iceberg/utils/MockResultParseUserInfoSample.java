package com.iceberg.utils;

import com.iceberg.entity.UserInfo;
import java.util.List;

public class MockResultParseUserInfoSample {
  private Integer code;
  private String msg;
  private List<UserInfo> data;

  public List<UserInfo> getData() {
    return data;
  }

  private List<UserInfo> datas;
  private Integer total;

  public void setData(List<UserInfo> data) {
    this.data = data;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public List<UserInfo> getDatas() {
    return datas;
  }

  public void setDatas(List<UserInfo> datas) {
    this.datas = datas;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
