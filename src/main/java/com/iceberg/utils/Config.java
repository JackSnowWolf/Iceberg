package com.iceberg.utils;

import com.iceberg.entity.UserInfo;

import javax.servlet.http.HttpSession;

public class Config {

  public static final String CURRENT_USERNAME = "currentUser";

  // Result
  public static final int SUCCESS = 200; // success
  public static final int UNSUCCESS = 400; // failure
  public static final int ERROR = 500; // exception

  // enable customeized log
  public static final boolean ENABLE_CUSTOMEIZE_LOG = true;

  public static UserInfo getSessionUser(HttpSession session) {
    return (UserInfo) session.getAttribute(CURRENT_USERNAME);
  }
}