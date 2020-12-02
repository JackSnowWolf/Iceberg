package com.iceberg.utils;

import com.iceberg.entity.Role;
import java.util.List;

public class MockResultParseRoleSample {
  private Integer code;
  private String msg;
  private List<Role> data;
  private List<Role> datas;
  private Integer total;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public List<Role> getData() {
    return data;
  }

  public void setData(List<Role> data) {
    this.data = data;
  }

  public List<Role> getDatas() {
    return datas;
  }

  public void setDatas(List<Role> datas) {
    this.datas = datas;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }
}
