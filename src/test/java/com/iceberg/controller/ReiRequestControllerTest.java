package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.TYPE.APPROVED;
import static com.iceberg.entity.ReimbursementRequest.TYPE.MISSING_INFO;
import static com.iceberg.entity.ReimbursementRequest.TYPE.PROCESSING;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.ReiRequestService;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(ReiRequestController.class)
@AutoConfigureMybatis
public class ReiRequestControllerTest {

  protected static MockHttpSession session;

  private static Gson gson = new Gson();

  private static ObjectMapper objectMapper = new ObjectMapper();
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
    userInfo.setUsername("hwj");
    userInfo.setPassword("hwj");
    userInfo.setId(1);
    userInfo.setRoleid(1);
    userInfo.setRolename("Administrator");
    userInfo.setRealname("hwj");
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
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark("Test");
    reimbursementRequest1.setRequesttype(PROCESSING);
    reimbursementRequest1.setPaywayid(1);
    given(this.reiRequestService.add(eq(reimbursementRequest1)))
      .willReturn(1);

    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
      .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
      });
    paramsMap.setAll(maps);
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
    reimbursementRequest1.setRequesttype(PROCESSING);
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
    reimbursementRequest2.setRequesttype(MISSING_INFO);
    reimbursementRequest2.setPaywayid(1);

    // Change request type from MISSING_INFO to PROCESSING
    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
      .convertValue(reimbursementRequest2, new TypeReference<Map<String, String>>() {
      });
    paramsMap.setAll(maps);
    this.mockMvc
      .perform(MockMvcRequestBuilders.post("/reirequest/updateRequest")
        .contentType(MediaType.APPLICATION_JSON)
        .params(paramsMap)
        .session(session))
      .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    ;
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
    ;
  }
}
