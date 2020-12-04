package com.iceberg.controller;

import com.iceberg.entity.Privilege;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.PrivilegeService;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.Config;
import com.iceberg.utils.HttpClientUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.Generated;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OAuthController {

  @Resource
  private UserInfoService userInfoService;
  @Resource
  private PrivilegeService privilegeService;

  /**
   * github login api.
   *
   * @param code access code
   * @param request http request
   * @param httpResponse http response
   * @return redirect url
   * @throws IOException exception
   */
  @Generated
  @RequestMapping("/oauth/github/callback")
  public String githubLogin(String code, HttpServletRequest request,
      HttpServletResponse httpResponse) throws IOException {
    // code is sent by github, need use it to exchange accessToken to get userinfo
    // 1. get accessToken
    String s3 = "https://github.com/login/oauth/access_token";
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("client_id", "d644cfff862d6e5d155a");
    paramMap.put("client_secret", "ac4a5c749e66dcb25bc7f9a216a7983b6326dc5a");
    paramMap.put("code", code);
    paramMap.put("redirect_uri", "http://localhost:8080/oauth/github/callback");
    String accessTokenResponse = HttpClientUtils.doPost(s3, paramMap);
    String accessToken = parseAccessTokenResponse(accessTokenResponse);
    // 2. accessToken get user info
    URL url = new URL("https://api.github.com/user");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestMethod("GET");
    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    String output;
    StringBuffer response = new StringBuffer();
    while ((output = in.readLine()) != null) {
      response.append(output);
    }
    in.close();
    // 3. down to database, set auth-provider to be github
    Map<String, String> userInfoMap = parseGithubObject(response.toString());
    Integer id = Integer.parseInt(userInfoMap.get("id"));
    String username = userInfoMap.get("username");
    String email = userInfoMap.get("email");
    UserInfo user = new UserInfo(id, username, email);
    // if user is existed
    if (userInfoService.userIsExisted(user)) {
    } else {
      userInfoService.add(user);
    }
    setSessionUserInfo(user, request.getSession());
    // 4. configure user session in order to login in

    return "redirect:http://localhost:8080/";
  }

  /**
   * parse access token response.
   *
   * @param accessTokenResponse access token response
   * @return access token
   */
  public static String parseAccessTokenResponse(String accessTokenResponse) {
    if (accessTokenResponse == null) {
      return null;
    }
    String accessToken = null;
    String[] res = accessTokenResponse.split("&");
    String accessTokenKV = res[0];
    accessToken = accessTokenKV.substring(13);
    return accessToken;
  }

  /**
   * parse github object.
   *
   * @param response response
   * @return key-value pairs.
   */
  public static Map<String, String> parseGithubObject(String response) {
    if (response == null) {
      return null;
    }
    String email = null;
    String[] res = response.toString().split(",");
    String emailKV = res[res.length - 10];
    email = emailKV.substring(9, emailKV.length() - 1);
    String username = res[0].substring(1);
    String id = res[1];
    Map<String, String> userMap = new HashMap<>();
    String value = username.substring(9);
    String usernameValue = value.substring(0, value.length() - 1);
    userMap.put("username", usernameValue);
    userMap.put("id", id.substring(5));
    userMap.put("email", email);
    return userMap;

  }

  /**
   * get user privilege info through user info and save it in session.
   */
  public UserInfo setSessionUserInfo(UserInfo userInfo, HttpSession session) {
    List<Privilege> privileges = privilegeService.getPrivilegeByRoleid(userInfo.getRoleid());
    userInfo.setPrivileges(privileges);
    session.setAttribute(Config.CURRENT_USERNAME, userInfo);
    return userInfo;
  }
}
