package com.iceberg.entity;

public class Group {
  private Integer id;
  private String groupname;
  private Integer managerid;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getGroupname() {
    return groupname;
  }

  public void setGroupname(String groupname) {
    this.groupname = groupname;
  }

  public Integer getManagerid() {
    return managerid;
  }

  public void setManagerid(Integer managerid) {
    this.managerid = managerid;
  }
}
