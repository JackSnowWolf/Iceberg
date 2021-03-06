package com.iceberg.configs;

import com.iceberg.entity.UserInfo;
import com.iceberg.utils.Config;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * description: Log, Helpful for debug.
 *
 * @author Weijie Huang
 * @date 2020/11/13
 */
public class LoggerInterceptor implements HandlerInterceptor {

  private static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
  private static StringBuilder sb = new StringBuilder();
  private HttpSession session;
  private String userid;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    System.out.println("logger preHandler runs @@@@@@@@@@@@@@@@@@@@@@@");
    if ((session = request.getSession()) != null) {
      System.out.println("Log preHandler comes in");
      UserInfo userInfo = (UserInfo) session.getAttribute(Config.CURRENT_USERNAME);
      userid = userInfo == null ? request.getHeader("userid") : userInfo.getId().toString();
      System.out.println("now user id is ： " + userid);
    }
    sb.setLength(0);

    sb.append("User ID【").append(userid).append("】is visiting：")
      .append(request.getRequestURL().toString());
    logger.info(sb.toString());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    if (!request.getRequestURI().contains("/static/")) {
      sb.setLength(0);
      sb.append("Method:").append(((HandlerMethod) handler).getShortLogMessage());
      logger.info(sb.toString());
      Map<String, String[]> parameters = request.getParameterMap();
      if (parameters.size() > 0) {
        sb.setLength(0);
        sb.append("Parameters: {");
        for (Map.Entry<String, String[]> entrySet : parameters.entrySet()) {
          String value = entrySet.getValue()[0];
          if (value != null && !value.isEmpty()) {
            sb.append(entrySet.getKey() + ":" + value + ",");
          }
        }
        if (sb.lastIndexOf(",") != -1) {
          sb.deleteCharAt(sb.lastIndexOf(","));
        }
        sb.append("}");
        logger.info(sb.toString());
      }
    }

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

    sb.setLength(0);
    sb.append("User ID [").append(userid).append("]").append(request.getRequestURL().toString())
      .append("visiting ends... ");

    // logger.info(sb.toString());

  }
}
