package com.iceberg.controller;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.iceberg.entity.UserInfo;
import com.iceberg.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ReiRequestController.class)
@AutoConfigureMybatis
public class UserInfoControllerMockTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UserInfoService userInfoService;

  @Test
  public void testAddRole1() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/add";
    given(userInfoService.addRole(any())).willReturn(1);
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("roleid", "99")
            .param("rolename", "CU Students")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testAddRole2() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/add";
    given(userInfoService.addRole(any())).willReturn(-1);
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("roleid", "99")
            .param("rolename", "CU Students")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testAddRole3() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/add";
    given(userInfoService.addRole(any())).willThrow(new RuntimeException("fails"));
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("roleid", "99")
            .param("rolename", "CU Students")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testAddUser1() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/add";
    String randomUserName = "randomUUIDForEveryTimeRunTest";
    given(userInfoService.addRole(any())).willReturn(1);
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("username", "test" + randomUserName)
            .param("password", "123456")
            .param("roleid", "3")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testAddUser2() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/add";
    String randomUserName = "randomUUIDForEveryTimeRunTest";
    given(userInfoService.addRole(any())).willReturn(-1);
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("username", "test" + randomUserName)
            .param("password", "123456")
            .param("roleid", "3")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testAddUser3() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/add";
    String randomUserName = "randomUUIDForEveryTimeRunTest";
    given(userInfoService.addRole(any())).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("username", "test" + randomUserName)
            .param("password", "123456")
            .param("roleid", "3")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testLogin() throws Exception {
    String URI = "/login.do";
    given(userInfoService.userIsExisted(any())).willReturn(true);
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("username", "hwj")
            .param("password", "hwj"))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testUpdateUser1() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/user/update";
    String randomPassword = "123456";
    given(userInfoService.update(any())).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("username", "house2")
            .param("id", "21")
            .param("password", randomPassword)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testDelUser1() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI2 = "/user/del/{id}";
    given(userInfoService.delete(any())).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI2, 10000).contentType(MediaType.APPLICATION_JSON)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testGetAllRoles() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/getAllRoles";
    given(userInfoService.getAllRoles()).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.get(URI).contentType(MediaType.APPLICATION_JSON)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testUpdateRole() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/role/update";
    given(userInfoService.updateRole(any())).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.post(URI).contentType(MediaType.APPLICATION_JSON)
            .param("rolename", "Columbia Students")
            .param("roleid", "99")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void testGetRoleById() throws Exception {
    MockHttpSession session = getDefaultSession();
    String URI = "/getRole/{roleid}";
    given(userInfoService.getRoleById(any())).willThrow(new RuntimeException(""));
    mockMvc
        .perform(MockMvcRequestBuilders.get(URI, 1).contentType(MediaType.APPLICATION_JSON)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
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
