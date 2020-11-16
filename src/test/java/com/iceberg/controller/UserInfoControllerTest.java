package com.iceberg.controller;

import com.iceberg.entity.UserInfo;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value= UserInfoController.class,secure = false)
@AutoConfigureMybatis
public class UserInfoControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void testToLoginPage() throws Exception {
    String URI1 = "/";
    String URI2 = "/login.html";

    RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get(URI1).accept(
        MediaType.APPLICATION_JSON);
    RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get(URI2).accept(
        MediaType.APPLICATION_JSON);

    MvcResult result1 = mockMvc.perform(requestBuilder1).andReturn();
    MvcResult result2 = mockMvc.perform(requestBuilder2).andReturn();

    assertEquals(200,result1.getResponse().getStatus());
    assertEquals(200,result2.getResponse().getStatus());
  }

//  @Test
//  public void testLogin() throws Exception {
//    String URI = "/login.do";
//    UserInfo userInfo = new UserInfo();
//    userInfo.setUsername("hwj");
//    userInfo.setPassword("hwj");
//    RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI, userInfo).accept(MediaType.APPLICATION_JSON);
//
//    MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//
//    assertEquals(result.getResponse().getErrorMessage(), "login successful");
//
//  }

}
