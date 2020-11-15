package com.iceberg.configs;

import com.iceberg.controller.UserInfoController;
import com.iceberg.entity.UserInfo;
import com.iceberg.utils.Config;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * description: SessionInterceptor, web page free login
 * 
 * @date 2020/11/13
 */
public class SessionInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("token: "+request.getHeader("token"));
//        System.out.println(request.getRequestURL());
    System.out.println("session preHandler runs *******************");
    HttpSession session = request.getSession();

    // session has no user info, but cookie has
    // use info in cookies
    if (session.getAttribute(Config.CURRENT_USERNAME) == null && getCookieUser(request) != null) {
//            System.out.println(HandlerMethod.class +"==="+ handler.getClass());
      if (HandlerMethod.class.equals(handler.getClass())) {
        Object controller = ((HandlerMethod) handler).getBean();
        if (controller instanceof UserInfoController) {
          UserInfoController userInfoController = (UserInfoController) controller;
          String userinfoStr = getCookieUser(request);
          UserInfo userInfo = new UserInfo();
          userInfo.setId(Integer.parseInt(userinfoStr.split("_")[1]));
          userInfo.setUsername(userinfoStr.split("_")[0]);
          userInfo = userInfoController.getUserInfo(userInfo);
          userInfoController.setSessionUserInfo(userInfo, session);
          System.out.println("session prehandler return true");
          return true;
        }
      }
    }

    // mobile client skip
    if ("client".equals(request.getHeader("token"))) {
      System.out.println("session prehandler return true");
      return true;
    }
    String uri = request.getRequestURI();
    System.out.println("URI IS  : " + uri);
//        System.out.println("session:"+uri);
    // login page or static resource, don't intercept
    if ("/".equals(uri) || "/login.html".equals(uri) || "/login.do".equals(uri) || uri.contains("/static/")
        || uri.startsWith("/oauth")) {
//            System.out.println("---let them go---");
    } else {
      // no login page, check session, no session then redirect to login page
      if (session.getAttribute(Config.CURRENT_USERNAME) == null) {
        response.sendRedirect("/login.html");
        System.out.println("session prehandler return false!!!!!");
        return false;
      }
    }

    System.out.println("session prehandler return true");
    return true;
  }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
////        System.out.println("postHandle");
//    }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
//        System.out.println(request.getRequestURI());
    System.out.println("sessionInterceptor After Completion++++++++++++++++++processing");
    String uri = request.getRequestURI();
    if ("/logout".equals(uri) || "/".equals(uri) || "/login.html".equals(uri) || uri.startsWith("/static")
        || uri.startsWith("/oauth")) {
      // avoid repeating redirecting
      return;
    }
    if (request.getSession().getAttribute(Config.CURRENT_USERNAME) == null) {
      response.sendRedirect("/login.html");
      return;
    }
  }

  /**
   * get user info in cookie
   * 
   * @param request
   * @return
   */
  private String getCookieUser(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    } else {
      for (Cookie cookie : cookies) {
        if (Config.CURRENT_USERNAME.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
      return null;
    }
  }
}
