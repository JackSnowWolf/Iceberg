package com.iceberg.utils;

import com.iceberg.entity.UserInfo;

import javax.servlet.http.HttpSession;

public class Config {

    public static String CURRENT_USERNAME = "currentUser";

    //Result
    public static int SUCCESS=200; //success
    public static int UNSUCCESS=400;   //failure
    public static int ERROR=500;   //exception

    //enable customeized log
    public static boolean ENABLE_CUSTOMEIZE_LOG = true;


    public static UserInfo getSessionUser(HttpSession session){
        return (UserInfo)session.getAttribute(CURRENT_USERNAME);
    }
}