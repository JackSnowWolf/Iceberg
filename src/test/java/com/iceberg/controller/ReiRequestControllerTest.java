package com.iceberg.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.iceberg.dao.ReiRequestMapper;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.ReiRequestService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(ReiRequestController.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class ReiRequestControllerTest {

  protected MockHttpSession session;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReiRequestService reiRequestService;

  @Autowired
  private ReiRequestMapper reiRequestMapper;
  private List<ReimbursementRequest> reimbursementRequestList;

  private UserInfo userInfo;

  @BeforeAll
  void init() throws Exception {
    session = new MockHttpSession();

    session.setAttribute("id", 1);
    session.setAttribute("roleid", 1);
    session.setAttribute("password", "zsh");

    mockMvc = MockMvcBuilders.standaloneSetup(new ReiRequestController())
      .apply(sharedHttpSession()).build();

    mockMvc.perform(MockMvcRequestBuilders.get("/").session(session))
      .andDo(print())
      .andExpect(MockMvcResultMatchers.status().isOk());

    this.reimbursementRequestList = new ArrayList<>();
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setUserid(10);
    this.reimbursementRequestList.add(reimbursementRequest1);
  }

  @Test
  @Order(1)
  void shouldAddReiRequest() throws Exception {
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setUserid(1);
    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/addRequest", reimbursementRequest1))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk());


  }

}
