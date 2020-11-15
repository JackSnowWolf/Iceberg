package com.iceberg.utils;

import java.util.List;

public class Result<T> {

  private int code; // status code
  private String msg; // reminder information
  private T data; // data
  private List<T> datas; // data collections
  private int total; // total information number

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public List<T> getDatas() {
    return datas;
  }

  public void setDatas(List<T> datas) {
    this.datas = datas;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

}