package com.iceberg.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import com.iceberg.entity.Privilege;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.PrivilegeService;
import com.iceberg.service.UserInfoService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;

@WebMvcTest(OAuthController.class)
@AutoConfigureMybatis
public class OAuthControllerMockTest {

  protected static MockHttpSession session;

  private final Logger logger = LoggerFactory.getLogger(OAuthControllerMockTest.class);

  @Autowired
  OAuthController oAuthController = new OAuthController();

  @MockBean
  private UserInfoService userInfoService;

  @MockBean
  private PrivilegeService privilegeService;

  @BeforeEach
  void init() throws Exception {
    logger.info("init test");
    session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("hwj");
    userInfo.setPassword("hwj");
    userInfo.setId(1);
    userInfo.setRoleid(1);
    userInfo.setRolename("Administrator");
    userInfo.setRealname("hwj");
    userInfo.setEmail("admin@example.com");
    session.setAttribute("currentUser", userInfo);
  }

  @Test
  void shouldSetSessionUserInfo() {
    logger.info("Should set session user info");
    HttpSession httpSession = new MockHttpSession();
    Privilege privilege = new Privilege();
    privilege.setId(1);
    privilege.setPrivilegeName("test");
    privilege.setPrivilegeNumber("1");
    List<Privilege> privileges = new ArrayList<>();
    privileges.add(privilege);
    given(privilegeService.getPrivilegeByRoleid(1)).willReturn(privileges);

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("hwj");
    userInfo.setPassword("hwj");
    userInfo.setId(1);
    userInfo.setRoleid(1);
    oAuthController.setSessionUserInfo(userInfo, httpSession);
    assertEquals(privileges, userInfo.getPrivileges());

    logger.info("finished!");
  }
}
