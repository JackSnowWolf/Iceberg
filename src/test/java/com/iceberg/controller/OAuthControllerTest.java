package com.iceberg.controller;

import static org.junit.Assert.assertEquals;

import com.iceberg.utils.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value= OAuthController.class,secure = false)
@AutoConfigureMybatis
public class OAuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  /**
   * test github login,
   * it will automatically call back to
   * our endpoint.
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
}
