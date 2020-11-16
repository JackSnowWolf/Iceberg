package com.iceberg.controller;

import com.alibaba.fastjson.JSONObject;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import com.iceberg.utils.MockResultParseRoleSample;
import com.iceberg.utils.MockResultParseUserInfoSample;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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
  private String randomUUIDForEveryTimeRunTest = String.valueOf(UUID.randomUUID());
  /**
   * test we visiting login page without any session.
   */

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

  /**
   * test we login in the app.
   * username : hwj.
   * password : hwj.
   */

  @Test
  public void testLogin() throws Exception {
    String URI = "/login.do";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
      .param("username", "hwj")
      .param("password", "hwj"))
      .andReturn();

    //Now we have login in to the app, find the user's info other than username and password like roleId
    assertEquals(200, result.getResponse().getStatus());
    //parse string result to object
    //Attention: this info is saved in "data"!!!
    UserInfo userInfoResult = JSONObject.parseObject(result.getResponse().getContentAsString(),
        MockResultParseUserInfoSample.class).getData().get(0);
    assertEquals("hwj", userInfoResult.getRealname());
  }

  /**
   * test getting user by where with session.
   */
  @Test
  public void testGetUserByWhere() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/users/getUsersByWhere/{pageNo}/{pageSize}";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI, 1, 100).contentType(MediaType.APPLICATION_JSON)
        .param("username", "hwj")
        .param("password", "hwj")
        .param("roleid","1")
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());

//    System.out.println("result 1 ************" + result.getResponse().getContentAsString());
    //Attention: this info is saved in "datas"!!!!!
    UserInfo userInfoResult = JSONObject.parseObject(result.getResponse().getContentAsString(),
        MockResultParseUserInfoSample.class).getDatas().get(0);
    System.out.println("result 2 ******" + userInfoResult);
    assertEquals("Administrator", userInfoResult.getRolename());
    assertEquals("hwj97055@gmail.com", userInfoResult.getEmail());
  }

  /**
   * test adding user with session.
   * Using UUID to generate random username.
   */
  @Test
  public void testUserAdd() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/add";
    String randomUserName = randomUUIDForEveryTimeRunTest;
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
        .param("username", "test" + randomUserName)
        .param("password", "123456")
        .param("roleid","3")
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
    String msg = JSONObject.parseObject(result.getResponse().getContentAsString(),
        MockResultParseUserInfoSample.class).getMsg();
    assertEquals(msg, "Operation successful！");
  }

  /**
   * test updating user with session.
   * Using UUID to generate random password.
   * for username: house2.
   */
  @Test
  public void testUserUpdate() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/update";
    String randomPassword = randomUUIDForEveryTimeRunTest;
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
        .param("username", "house2")
        .param("id","21")
        .param("password", randomPassword)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
    String msg = JSONObject.parseObject(result.getResponse().getContentAsString(),
        MockResultParseUserInfoSample.class).getMsg();
    assertEquals(msg, "Operation successful！");
  }

  /**
   * test deleting user with session.
   * first add an user and then delete it.
   * specify user id = 10000;
   *
   */
  @Test
  public void testUserDel() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/add";
    String randomUserName = randomUUIDForEveryTimeRunTest;
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
        .param("id", "10000")
        .param("username", "test" + randomUserName)
        .param("password", "123456")
        .param("roleid","3")
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());

    String URI2 = "/user/del/{id}";
    MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.post(URI2, 10000).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result2.getResponse().getStatus());
    String msg = JSONObject.parseObject(result2.getResponse().getContentAsString(),
        MockResultParseUserInfoSample.class).getMsg();
    assertEquals(msg, "Operation successful！");
  }

  /**
   * test get session userinfo.
   */
  @Test
  public void testGetSessionUser() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/getSessionUser";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
    UserInfo sessionUser = JSONObject.parseObject(result.getResponse().getContentAsString(),UserInfo.class);
    assertEquals("hwj", sessionUser.getUsername());
    assertEquals("hwj", sessionUser.getRealname());
    assertEquals("Administrator", sessionUser.getRolename());
  }

  /**
   * test logout and redirect to login page.
   */
  @Test
  public void testLogOut() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/logout";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * test {page} to be sys_roleadd.
   * and redirect to sys/roleadd.html;
   * substitute "_" with "/".
   */
  @Test
  public void testToPage() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/pages/{page}";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI, "sys_roleadd").contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    //redirect to sys/roleadd.html successfully.
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * test get allroles with session.
   *
   */
  @Test
  public void testGetAllRoles() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/getAllRoles";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
    Role roleGet= JSONObject
        .parseObject(result.getResponse().getContentAsString(), MockResultParseRoleSample.class).getDatas().get(0);
    assertEquals("1", String.valueOf(roleGet.getRoleid()));
    assertEquals("Administrator", roleGet.getRolename());
//    System.out.println(result.getResponse().getContentAsString());
  }

  /**
   * test adding role.
   * roleid = 99;
   */
  @Test
  public void testRoleAdd() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/add";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
        .param("roleid", "99")
        .param("rolename", "CU Students")
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * test updating role. find the last role in db.
   * roleid = 99.
   */
  @Test
  public void testRoleUpdate() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/update";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
        .param("rolename", "Columbia Students")
        .param("roleid", "99")
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * test deleting role.
   * delete role with roleid = 99.
   */
  @Test
  public void testRoleDel() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/del/{roleid}";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URI, 99).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
  }

  /**
   * test get role by roleid.
   */
  @Test
  public void testGetRoleById() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/getRole/{roleid}";
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URI, 1).contentType(MediaType.APPLICATION_JSON)
        .session(session))
        .andReturn();
    assertEquals(200, result.getResponse().getStatus());
    Role roleGet= JSONObject
        .parseObject(result.getResponse().getContentAsString(), MockResultParseRoleSample.class).getData().get(0);
//    System.out.println(result.getResponse().getContentAsString());
    assertEquals("1", String.valueOf(roleGet.getRoleid()));
    assertEquals("Administrator", roleGet.getRolename());
  }

  private MockHttpSession getDefaultSession() {
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("hwj");
    userInfo.setPassword("hwj");
    userInfo.setId(1);
    userInfo.setRoleid(1);
    userInfo.setRolename("Administrator");
    userInfo.setRealname("hwj");
    MockHttpSession session = new MockHttpSession();
    session.setAttribute("currentUser", userInfo);
    return session;
  }

}
