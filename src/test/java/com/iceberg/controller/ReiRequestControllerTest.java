package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.Status.APPROVED;
import static com.iceberg.entity.ReimbursementRequest.Status.MISSING_INFO;
import static com.iceberg.entity.ReimbursementRequest.Status.PROCESSING;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.externalapi.PayPalService;
import com.iceberg.service.ReiRequestService;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(ReiRequestController.class)
@AutoConfigureMybatis
public class ReiRequestControllerTest {

  protected static MockHttpSession session;


  private static ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReiRequestService reiRequestService;

  @MockBean
  private UserInfoService userInfoService;

  @MockBean
  private PayPalService payPalService;


  @BeforeEach
  void init() throws Exception {
    System.out.println("init test");
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

//    mockMvc = MockMvcBuilders.standaloneSetup(new ReiRequestController())
//      .apply(sharedHttpSession()).build();
  }

  @Test
  void shouldAddReiRequest() throws Exception {
    System.out.println("add reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reirequest test");
    reimbursementRequest1.setRemark("Test");
//    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    given(this.reiRequestService.add(eq(reimbursementRequest1)))
      .willReturn(1);

    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
      .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
      });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
        .contentType(MediaType.APPLICATION_JSON)
        .params(paramsMap)
        .session(session))
      .andDo(print())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));

  }

  @Test
  void shouldUpdateReiRequest() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    given(this.reiRequestService.update(reimbursementRequest1))
      .willReturn(1);

    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setId(190);
    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(1);
    given(this.reiRequestService.findByWhereNoPage(eq(reimbursementRequestSearch)))
      .willReturn(willReturnResult);

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark("Test");
    reimbursementRequest2.setTypeid(MISSING_INFO.value);
    reimbursementRequest2.setPaywayid(1);

    // Change request type from MISSING_INFO to PROCESSING
    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
      .convertValue(reimbursementRequest2, new TypeReference<Map<String, String>>() {
      });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);
    paramsMap.set("typeid", "0");
    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/updateRequest")
        .contentType(MediaType.APPLICATION_JSON)
        .params(paramsMap)
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
  }

  @Test
  void shouldDelReiRequest() throws Exception {
    System.out.println("del reirequest test");
    given(this.reiRequestService.del(eq(190))).willReturn(1);

    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/delReiRequest")
        .contentType(MediaType.APPLICATION_JSON)
        .param("id", "190")
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
  }

  @Test
  void shouldApproveReview() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");

    given(reiRequestService.getReimRequestById(190)).willReturn(reimbursementRequest1);
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("group1");
    userInfo.setPassword("m123");
    userInfo.setId(2);
    userInfo.setRoleid(2);
    userInfo.setRolename("Group Manager");
    userInfo.setRealname("123");
    userInfo.setEmail("groupmanger@gmail.com");
    session.setAttribute("currentUser", userInfo);

    String normalUserEmail = "normaluser@gmail.com";
    String receiver = "normaluser@paypal.com";
    // set userInfoService return
    UserInfo normalUserInfo = new UserInfo();
    normalUserInfo.setUsername("normal user");
    normalUserInfo.setPassword("m123");
    normalUserInfo.setId(1);
    normalUserInfo.setRoleid(2);
    normalUserInfo.setRolename("Normal User");
    normalUserInfo.setRealname("Normal User");
    normalUserInfo.setEmail(normalUserEmail);
    given(userInfoService.getUserInfoById("1")).willReturn(normalUserInfo);

    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("approve request test");
    reimbursementRequest2.setRemark("Test");
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    // mock reiRequestService update returnt
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
//    when(MailUtils.sendMail(receiver, MailUtils.approved)).thenReturn("");
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
      .willReturn(null);
    MvcResult result = this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}",
        String.valueOf(3), String.valueOf(1), String.valueOf(190))
        .contentType(MediaType.APPLICATION_JSON)
        .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();
  }

  @Test
  void shouldGetReiRequestById() throws Exception {
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("get reiquest by id test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setId(190);
    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(1);
    given(this.reiRequestService.findByWhereNoPage(eq(reimbursementRequestSearch)))
      .willReturn(willReturnResult);

    MvcResult result = this.mockMvc
      .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequestById/{id}", 190)
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
      .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
  }

  @Test
  void shouldGetReiRequestForNormalUser() throws Exception {
    MockHttpSession session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("ch");
    userInfo.setPassword("ch");
    userInfo.setId(1);
    userInfo.setRoleid(3);
    userInfo.setGroupid("1");
    userInfo.setRolename("NormalUser");
    userInfo.setRealname("ch");
    session.setAttribute("currentUser", userInfo);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setUserid(1);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should Get ReiRequst For Normal User test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);

    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(1);

    PageModel model = new PageModel<>(1, reimbursementRequestSearch);
    model.setPageSize(20);
    given(this.reiRequestService.findByWhere(eq(model)))
      .willReturn(willReturnResult);
    MvcResult result = this.mockMvc
      .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequest/{pageNo}/{pageSize}", 1, 20)
        .session(session))

      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
      .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
    assertNotEquals(0, decodedResponse.getDatas().size());
  }

  @Test
  void shouldGetReiRequestForGroupManager() throws Exception {
    MockHttpSession session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("ch");
    userInfo.setPassword("ch");
    userInfo.setId(1);
    userInfo.setRoleid(2);
    userInfo.setGroupid("1");
    userInfo.setRolename("NormalUser");
    userInfo.setRealname("ch");
    session.setAttribute("currentUser", userInfo);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setGroupid("1");

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(191);
    reimbursementRequest2.setUserid(2);
    reimbursementRequest2.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest2.setRemark("Test");
    reimbursementRequest2.setTypeid(PROCESSING.value);
    reimbursementRequest2.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);
    reimbursementRequests.add(reimbursementRequest2);

    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(2);

    PageModel model = new PageModel<>(1, reimbursementRequestSearch);
    model.setPageSize(20);
    given(this.reiRequestService.findByWhere(eq(model)))
      .willReturn(willReturnResult);
    MvcResult result = this.mockMvc
      .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequest/{pageNo}/{pageSize}", 1, 20)
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).
        andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
      .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
    assertNotEquals(0, decodedResponse.getDatas().size());
  }
}
