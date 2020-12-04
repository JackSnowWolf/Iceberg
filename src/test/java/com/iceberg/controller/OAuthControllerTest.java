package com.iceberg.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.iceberg.utils.HttpClientUtils;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


@WebMvcTest(OAuthController.class)
@AutoConfigureMybatis
public class OAuthControllerTest {


  /**
   * test github login, it will automatically call back to our endpoint.
   */
  @Test
  public void testGithubLogin() throws Exception {
    String URL = "https://github.com/login/oauth/authorize?client_id="
        + "d644cfff862d6e5d155a&redirect_uri=http://localhost:8080/oauth/github/callback";
    Integer status = HttpClientUtils.doGet(URL);
//    System.out.println(status);
    assertEquals("200", String.valueOf(status));
  }

  @Test
  public void testParseAccessToken() throws Exception {
    String originToken = "access_token=abcdefghijklmnopq&type=bearer";
    assertEquals("abcdefghijklmnopq", OAuthController.parseAccessTokenResponse(originToken));
  }

  @Test
  public void testParseAccessTokenForNullValue() throws Exception {
    assertNull(OAuthController.parseAccessTokenResponse(null));
  }

  @Test
  public void testparseGithubObject() throws Exception {
    String githubResponse =
        "''login'':mark2447',''id:54239290,node_id:MDQ6VXNlcjU0MjM5Mjkw,avatar_url"
            + "https://avatars0.githubusercontent.com/u/54239290?v=4,gr"
            + "avatar_id:,url:https://api.github.com/users/mark2447,html_url:https://githu"
            + "b.com/mark2447,followers_url:https://api.github.com/users/mark2447/followers,follow"
            + "ing_url:https://api.github.com/users/mark2447/following{/other_user},gists_url:https:"
            + "//api.github.com/users/mark2447/gists{/gist_id},starred_url:https://api.github.com/users/ma"
            + "rk2447/starred{/owner}{/repo},subscriptions_url:https://api.github.com/users/mark2447/subsc"
            + "riptions,organizations_url:https://api.github.com/users/mark2447/orgs,repos_url:https:/"
            + "/api.github.com/users/mark2447/repos,events_url:https://api.github.com/users/mark2447/event"
            + "s{/privacy},received_events_url:https://api.github.com/users/mark2447/received_events,typ"
            + "e:User,site_admin:false,name:null,company:null,blog:,location:null,email:wh24"
            + "47@columbia.edu,hireable:null,bio:null,twitter_username:null,public_repos:15,public_g"
            + "ists:0,followers:3,following:2,created_at:2019-08-18T12:58:07Z,updated_at:2020-11-1"
            + "5T12:07:44Z";
    Map<String, String> userMap = OAuthController.parseGithubObject(githubResponse);
    assertEquals("54239290", userMap.get("id"));
    assertEquals("mark2447", userMap.get("username"));
  }

  @Test
  public void testparseGithubObjectForNull() throws Exception {
    assertNull(OAuthController.parseGithubObject(null));
  }
}
