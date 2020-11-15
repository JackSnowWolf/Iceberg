package com.iceberg.entity;

import java.util.List;

public class UserInfo {
  private Integer id;

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  private String username;
  private String password;
  private String realname;
  private Integer roleid; //
  private String rolename;
  private String groupid;
  private String iconUrl; // icon path
  private String signature; // self-defined signature
  private String oauthProvider; // default Github

  public String getOauthProvider() {
    return oauthProvider;
  }

  public void setOauthProvider(String oauthProvider) {
    this.oauthProvider = oauthProvider;
  }

  private List<Privilege> privileges;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  private String email;

  public UserInfo(Integer id, String username, String email) {
    this.id = id;
    this.username = username;
    this.realname = username;
    this.email = email;
    this.password = "123456";
    // normal user default
    this.roleid = 3;
  }

  public UserInfo() {

  }
//    public AuthenticationProvider getAuthenticationProvider() {
//        return authenticationProvider;
//    }
//
//    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }
//
//    //OAuth2 provider info
//    private AuthenticationProvider authenticationProvider;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    if ("".equals(username.trim()))
      return;
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRealname() {
    return realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public Integer getRoleid() {
    return roleid;
  }

  public void setRoleid(Integer roleid) {
    this.roleid = roleid;
  }

  public String getRolename() {
    return rolename;
  }

  public void setRolename(String rolename) {
    this.rolename = rolename;
  }

  public String getGroupid() {
    return groupid;
  }

  public void setGroupid(String groupid) {
    this.groupid = groupid;
  }

  public List<Privilege> getPrivileges() {
    return privileges;
  }

  public void setPrivileges(List<Privilege> privileges) {
    this.privileges = privileges;
  }

  @Override
  public String toString() {
    return "UserInfo{" + "id=" + id + ", username='" + username + '\'' + ", password='" + password + '\''
        + ", realname='" + realname + '\'' + ", roleid=" + roleid + ", rolename='" + rolename + '\'' + ", groupid='"
        + groupid + '\'' + '}';
  }
}
