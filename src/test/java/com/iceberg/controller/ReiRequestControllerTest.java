package com.iceberg.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.google.gson.Gson;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.ReiRequestService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ReiRequestController.class)
@AutoConfigureMybatis
public class ReiRequestControllerTest {

  protected static MockHttpSession session;

  private static Gson gson = new Gson();

  @Autowired
  private MockMvc mockMvc;


  @MockBean
  private ReiRequestService reiRequestService;
  private List<ReimbursementRequest> reimbursementRequestList;

  private UserInfo userInfo;


  @BeforeEach
  void init() throws Exception {
    System.out.println("init test");
    session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setId(1);
    userInfo.setRoleid(1);
    userInfo.setPassword("zsh");
    userInfo.setUsername("zsh");
    session.setAttribute("currentUser", userInfo);

//    mockMvc = MockMvcBuilders.standaloneSetup(new ReiRequestController())
//      .apply(sharedHttpSession()).build();
  }

  @Test
  void shouldAddReiRequest() throws Exception {
    System.out.println("add reirequest test");
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setRemark("Test");
    given(this.reiRequestService.add(reimbursementRequest1))
      .willReturn(1);

    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
        .content(gson.toJson(reimbursementRequest1))
        .session(session)).andDo(print());

    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
        .content(gson.toJson(reimbursementRequest1))
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());


  }

}
