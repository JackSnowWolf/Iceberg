package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.Status.APPROVED;
import static com.iceberg.entity.ReimbursementRequest.Status.DENIED;
import static com.iceberg.entity.ReimbursementRequest.Status.MISSING_INFO;
import static com.iceberg.entity.ReimbursementRequest.Status.PROCESSING;
import static com.iceberg.externalapi.ImageStorageService.getFileBytes;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iceberg.entity.InvoiceDetail;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.ReimbursementRequest.Remark;
import com.iceberg.entity.UserInfo;
import com.iceberg.externalapi.ImageStorageService;
import com.iceberg.externalapi.PayPalService;
import com.iceberg.externalapi.impl.ProcessingForRequestImpl;
import com.iceberg.service.ReiRequestService;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
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

  private Logger logger = LoggerFactory.getLogger(ReiRequestControllerTest.class);
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReiRequestService reiRequestService;

  @MockBean
  private UserInfoService userInfoService;

  @MockBean
  private PayPalService payPalService;

  @MockBean
  private ImageStorageService imageStorageService;

  @MockBean
  private ProcessingForRequestImpl ocrService;

  private File testImageFile;

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

    ClassLoader classLoader = getClass().getClassLoader();
    URL result = classLoader.getResource("images/invoice-test-01.png");
    assertNotNull(result);
    testImageFile = new File(result.getFile());
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
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
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
  void shouldAddReiRequestForIllegalPayway() throws Exception {
    System.out.println("add reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reirequest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);

    given(this.reiRequestService.add(eq(reimbursementRequest1)))
        .willReturn(1);
    // fill request params
    reimbursementRequest1.setPaywayid(-1);
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
        });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);

    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .params(paramsMap)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
  }

  @Test
  void shouldAddReiRequestForMissingPayway() throws Exception {
    System.out.println("add reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reirequest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);

    given(this.reiRequestService.add(eq(reimbursementRequest1)))
        .willReturn(1);
    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
        });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);

    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .params(paramsMap)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
  }

  @Test
  void shouldNotAddReiRequestForErrorServiceCode() throws Exception {
    System.out.println("add reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reirequest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    //    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
        });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);

    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .params(paramsMap)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldNotAddReiRequestForServiceException() throws Exception {
    System.out.println("add reirequest test");

    MockHttpSession session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("hwj");
    userInfo.setPassword("hwj");
    userInfo.setId(1);
    userInfo.setRoleid(1);
    userInfo.setRolename("Administrator");
    userInfo.setRealname("hwj");
    session.setAttribute("currentUser", userInfo);

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reirequest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    //    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
        });
    maps.put("email", null);
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);
    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/addRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .params(paramsMap)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500));
  }

  @Test
  void shouldUpdateReiRequest() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(0);
    given(this.reiRequestService.update(reimbursementRequest1))
        .willReturn(1);
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(MISSING_INFO.value);
    reimbursementRequest2.setPaywayid(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(reimbursementRequest2);
    // Change request type from MISSING_INFO to PROCESSING
    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest2, new TypeReference<Map<String, String>>() {
        });
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);
    paramsMap.set("typeid", "0");
    paramsMap.set("paywayid", "-1");

    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/updateRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .params(paramsMap)
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
  }

  @Test
  void shouldNotUpdateReiRequestForNoRequestExist() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    given(this.reiRequestService.update(reimbursementRequest1))
        .willReturn(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(null);

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldNotUpdateReiRequestForOtherNormalUser() throws Exception {
    System.out.println("Update reirequest test");

    MockHttpSession session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("ch");
    userInfo.setPassword("ch");
    userInfo.setId(10);
    userInfo.setRoleid(3);
    userInfo.setGroupid("1");
    userInfo.setRolename("NormalUser");
    userInfo.setRealname("ch");
    session.setAttribute("currentUser", userInfo);

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    given(this.reiRequestService.update(reimbursementRequest1))
        .willReturn(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(reimbursementRequest1);

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Permission denied!"));
  }


  @Test
  void shouldNotUpdateReiRequestForApprovedType() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(0);
    given(this.reiRequestService.update(reimbursementRequest1))
        .willReturn(1);
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(reimbursementRequest2);
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldNotUpdateReiRequestForErrorServiceCode() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(0);
    given(this.reiRequestService.update(reimbursementRequest1))
        .willReturn(0);
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(MISSING_INFO.value);
    reimbursementRequest2.setPaywayid(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(reimbursementRequest2);
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldNotUpdateReiRequestForUpdateException() throws Exception {
    System.out.println("Update reirequest test");

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("add reiquest test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(0);
    given(this.reiRequestService.update(any())).willThrow(new Exception("update failed!"));
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("add reiquest test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(MISSING_INFO.value);
    reimbursementRequest2.setPaywayid(1);

    given(this.reiRequestService.getReimRequestById(eq(reimbursementRequest1.getId())))
        .willReturn(reimbursementRequest2);
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
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500));
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
  void shouldNotDelReiRequestForErrorServiceCode() throws Exception {
    System.out.println("del reirequest test");
    given(this.reiRequestService.del(eq(190))).willReturn(0);

    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/delReiRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "190")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldNotDelReiRequestForDelException() throws Exception {
    System.out.println("del reirequest test");
    given(this.reiRequestService.del(eq(190))).willThrow(new Exception("delete fails!"));

    this.mockMvc
        .perform(MockMvcRequestBuilders.post("/reirequest/delReiRequest")
            .contentType(MediaType.APPLICATION_JSON)
            .param("id", "190")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500));
  }

  @Test
  void shouldApproveReview() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForPermissionDenied() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

    given(reiRequestService.getReimRequestById(190)).willReturn(reimbursementRequest1);
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("normal user");
    userInfo.setPassword("m123");
    userInfo.setId(2);
    userInfo.setRoleid(3);
    userInfo.setRolename("Normal User");
    userInfo.setRealname("123");
    userInfo.setEmail("normaluser@gmail.com");
    session.setAttribute("currentUser", userInfo);
    this.mockMvc.perform(
        MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
            String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
            .contentType(MediaType.APPLICATION_JSON)
            .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath("$.msg")
            .value("Permission denied. Don't have review access."));
  }

  @Test
  void shouldApproveReviewForMissingEmail() throws Exception {
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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

    String receiver = "normaluser@paypal.com";
    // set userInfoService return
    UserInfo normalUserInfo = new UserInfo();
    normalUserInfo.setUsername("normal user");
    normalUserInfo.setPassword("m123");
    normalUserInfo.setId(1);
    normalUserInfo.setRoleid(2);
    normalUserInfo.setRolename("Normal User");
    normalUserInfo.setRealname("Normal User");
    given(userInfoService.getUserInfoById("1")).willReturn(normalUserInfo);

    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(190);
    reimbursementRequest2.setUserid(1);
    reimbursementRequest2.setTitle("approve request test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    MvcResult result = this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForMissingMoney() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    //    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
  }

  @Test
  void shouldApproveReviewForMissingReceiver() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setComments("good");

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

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400)).andReturn();
    reimbursementRequest1.setReceiveraccount("");
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400)).andReturn();
  }

  @Test
  void shouldApproveReviewWhenTransferServiceException() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willThrow(new IOException("Pay pal service Exception"));
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForProcessingType() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(PROCESSING.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(PROCESSING.value), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForDeniedType() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(DENIED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(1);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(DENIED.value), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForErrorUpdate() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2)).willReturn(0);
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400)).andReturn();
  }

  @Test
  void shouldNotApproveReviewForUpdateException() throws Exception {

    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("approve request test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);
    reimbursementRequest1.setMoney((float) 10);
    reimbursementRequest1.setReceiveraccount("normaluser@paypal.com");
    reimbursementRequest1.setComments("good");

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
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(APPROVED.value);
    reimbursementRequest2.setPaywayid(1);
    reimbursementRequest2.setMoney((float) 10);
    reimbursementRequest2.setReceiveraccount(receiver);
    reimbursementRequest2.setComments("good");
    // mock reiRequestService update return
    given(reiRequestService.update(reimbursementRequest2))
        .willThrow(new Exception("update failed!"));
    // mock transfer and send email return
    given(payPalService.createPayout(receiver, "USD", reimbursementRequest2.getMoney().toString()))
        .willReturn(null);
    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post("/reirequest/review/{typeid}/{userid}/{reimid}/{comments}",
                String.valueOf(3), String.valueOf(1), String.valueOf(190), "good")
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500)).andReturn();
  }

  @Test
  void shouldGetReiRequestById() throws Exception {
    // set reimbursement request id for test
    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("get reiquest by id test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
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
  void shouldGetReiRequestByUserId() throws Exception {
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
    reimbursementRequest1.setTitle("should Get ReiRequst By User id For Normal User test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
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
        .perform(MockMvcRequestBuilders
            .get("/reirequest/getReiRequestByUserId/{userid}/{pageNo}/{pageSize}", 1, 1, 20)
            .session(session))

        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).
            andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
        .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
    assertNotEquals(0, decodedResponse.getDatas().size());
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
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
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
  void shouldGetReiRequestForCertainType() throws Exception {
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

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setRealname("test user");
    reimbursementRequest1.setTitle("should Get ReiRequst For Normal User test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);

    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(1);

    PageModel model = new PageModel<>(1, reimbursementRequest1);
    model.setPageSize(20);
    given(this.reiRequestService.findByWhere(eq(model)))
        .willReturn(willReturnResult);

    // fill request params
    MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
    Map<String, String> maps = objectMapper
        .convertValue(reimbursementRequest1, new TypeReference<Map<String, String>>() {
        });
    maps.put("id", null);
    maps.values().removeAll(Collections.singleton(null));
    paramsMap.setAll(maps);
    paramsMap.set("typeid", "0");

    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequest/{pageNo}/{pageSize}", 1, 20)
            .session(session).params(paramsMap))
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
    userInfo.setRolename("Group Manager");
    userInfo.setRealname("ch");
    session.setAttribute("currentUser", userInfo);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setGroupid("1");

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(191);
    reimbursementRequest2.setUserid(2);
    reimbursementRequest2.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
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


  @Test
  void shouldGetReiRequestByNoPageForNormalUser() throws Exception {
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
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);

    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(1);

    given(this.reiRequestService.findByWhereNoPage(eq(reimbursementRequestSearch)))
        .willReturn(willReturnResult);
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequestByNoPage")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).
            andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
        .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
    assertNotEquals(0, decodedResponse.getDatas().size());
  }

  @Test
  void shouldGetReiRequestByNoPageForGroupManager() throws Exception {
    MockHttpSession session = new MockHttpSession();

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername("ch");
    userInfo.setPassword("ch");
    userInfo.setId(1);
    userInfo.setRoleid(2);
    userInfo.setGroupid("1");
    userInfo.setRolename("Group Manager");
    userInfo.setRealname("ch");
    session.setAttribute("currentUser", userInfo);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setGroupid("1");

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    ReimbursementRequest reimbursementRequest2 = new ReimbursementRequest();
    reimbursementRequest2.setId(191);
    reimbursementRequest2.setUserid(2);
    reimbursementRequest2.setTitle("should Get ReiRequst For Group Manager test");
    reimbursementRequest2.setRemark(Remark.TRANSPORT);
    reimbursementRequest2.setTypeid(PROCESSING.value);
    reimbursementRequest2.setPaywayid(1);

    // simulate response
    List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
    reimbursementRequests.add(reimbursementRequest1);
    reimbursementRequests.add(reimbursementRequest2);

    Result<ReimbursementRequest> willReturnResult = ResultUtil.success(reimbursementRequests);
    willReturnResult.setTotal(2);

    given(this.reiRequestService.findByWhereNoPage(eq(reimbursementRequestSearch)))
        .willReturn(willReturnResult);
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/getReiRequestByNoPage")
            .session(session))
        .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).
            andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200)).andReturn();

    Result decodedResponse = objectMapper
        .readValue(result.getResponse().getContentAsString(), Result.class);
    assertNotEquals(0, decodedResponse.getTotal());
    assertNotEquals(0, decodedResponse.getDatas().size());
  }


  @Test
  void shouldUploadImage() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(reiRequestService.update(any())).willReturn(1);
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn("eTag");
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldNotUploadImageForRequestNotFound() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(null);
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn("eTag");
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldNotUploadImageForPermissionDenied() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(10);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn("eTag");
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldNotUploadImageForFileError() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(reiRequestService.update(any())).willReturn(1);
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn(null);
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldNotUploadImageForFileException() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(reiRequestService.update(any())).willReturn(1);
    given(imageStorageService.putImage(any(), eq(imageFileData)))
        .willThrow(new IOException("Put image Exception"));
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldUploadImageForUpdateErrorCode() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(reiRequestService.update(any())).willReturn(0);
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn("eTag");
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldUploadImageForUpdateException() throws Exception {
    logger.info("test should upload image");
    byte[] imageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, imageFileData);

    ReimbursementRequest reimbursementRequest1 = new ReimbursementRequest();
    reimbursementRequest1.setId(190);
    reimbursementRequest1.setUserid(1);
    reimbursementRequest1.setTitle("should upload image For for corresponding request");
    reimbursementRequest1.setRemark(Remark.TRANSPORT);
    reimbursementRequest1.setTypeid(PROCESSING.value);
    reimbursementRequest1.setPaywayid(1);

    given(reiRequestService.getReimRequestById(any())).willReturn(reimbursementRequest1);
    given(reiRequestService.update(any())).willThrow(new Exception("Update Exception"));
    given(imageStorageService.putImage(any(), eq(imageFileData))).willReturn("eTag");
    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/{requestId}/image", 190)
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500))
        .andReturn();
    logger.debug("finished");
  }

  @Test
  void shouldGetImageByRequestId() throws Exception {
    logger.info("test should get image by request id");
    String imageName = "invoice-test-01.png";
    byte[] expectedImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    int requestId = 190;
    ReimbursementRequest returnedRequest = new ReimbursementRequest();
    returnedRequest.setId(190);
    returnedRequest.setImageid(imageName);
    given(reiRequestService.getReimRequestById(requestId)).willReturn(returnedRequest);
    given(imageStorageService.getImageBytes(eq(imageName))).willReturn(expectedImageFileData);

    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/{requestId}/image", requestId)
            .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    String contentType = result.getResponse().getContentType();
    byte[] resultData = result.getResponse().getContentAsByteArray();

    assertEquals(MediaType.IMAGE_PNG_VALUE, contentType);
    assertArrayEquals(expectedImageFileData, resultData);

    logger.debug("finished");
  }

  @Test
  void shouldGetImageByRequestIdForOtherType() throws Exception {
    logger.info("test should get image by request id");
    String imageName = "invoice-test-01.jpg";
    byte[] expectedImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    int requestId = 190;
    ReimbursementRequest returnedRequest = new ReimbursementRequest();
    returnedRequest.setId(190);
    returnedRequest.setImageid(imageName);
    given(reiRequestService.getReimRequestById(requestId)).willReturn(returnedRequest);
    given(imageStorageService.getImageBytes(eq(imageName))).willReturn(expectedImageFileData);

    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/{requestId}/image", requestId)
            .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    String contentType = result.getResponse().getContentType();
    byte[] resultData = result.getResponse().getContentAsByteArray();

    assertEquals(MediaType.IMAGE_JPEG_VALUE, contentType);
    assertArrayEquals(expectedImageFileData, resultData);

    imageName = "invoice-test-01.gif";
    returnedRequest.setImageid(imageName);
    given(reiRequestService.getReimRequestById(requestId)).willReturn(returnedRequest);
    given(imageStorageService.getImageBytes(eq(imageName))).willReturn(expectedImageFileData);

    result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/{requestId}/image", requestId)
            .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    contentType = result.getResponse().getContentType();
    resultData = result.getResponse().getContentAsByteArray();

    assertEquals(MediaType.IMAGE_GIF_VALUE, contentType);
    assertArrayEquals(expectedImageFileData, resultData);

    logger.debug("finished");
  }

  @Test
  void shouldGetImageByImageId() throws Exception {
    logger.info("test should get image by image id");
    String imageId = "invoice-test-01.png";
    byte[] expectedImageFileData = getFileBytes(testImageFile.getAbsolutePath());

    given(imageStorageService.getImageBytes(eq(imageId))).willReturn(expectedImageFileData);

    MvcResult result = this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/image/{imageId}", imageId)
            .session(session)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    String contentType = result.getResponse().getContentType();
    byte[] resultData = result.getResponse().getContentAsByteArray();

    assertEquals(MediaType.IMAGE_PNG_VALUE, contentType);
    assertArrayEquals(expectedImageFileData, resultData);

    logger.debug("finished");
  }

  @Test
  void shouldNotGetImageByImageIdForOtherType() throws Exception {
    logger.info("test should get image by image id");
    String imageId = "invoice-test-01.pdf";

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/image/{imageId}", imageId)
            .session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    logger.debug("finished");
  }

  @Test
  void shouldNotGetImageByImageId() throws Exception {
    logger.info("test should get image by image id");
    String imageId = "invoice-test-01.png";
    given(imageStorageService.getImageBytes(eq(imageId))).willReturn(null);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/image/{imageId}", imageId)
            .session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    logger.debug("finished");
  }

  @Test
  void shouldNotGetImageByImageIdForInvalidType() throws Exception {
    logger.info("test should get image by image id");
    String imageId = "invoice-test-01.pdf";
    given(imageStorageService.getImageBytes(eq(imageId))).willReturn(new byte[2]);

    this.mockMvc
        .perform(MockMvcRequestBuilders.get("/reirequest/image/{imageId}", imageId)
            .session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
    logger.debug("finished");
  }

  @Test
  void ShouldAnalysisImage() throws Exception {
    logger.info("test should analysis image");
    byte[] mockImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    String mockImageFileBase64 = Base64.getEncoder().encodeToString(mockImageFileData);
    InvoiceDetail mockInvoiceDetail = new InvoiceDetail();
    mockInvoiceDetail.setVendorname("example vendor");
    mockInvoiceDetail.setVendoraddr("example vendor address");
    mockInvoiceDetail.setDuedate("2020-12-31");
    mockInvoiceDetail.setMoney((float) 12.0);
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, mockImageFileData);
    given(ocrService.parseFromDocumentBase64(eq(mockImageFileBase64)))
        .willReturn(mockInvoiceDetail);
    given(imageStorageService.putImage(any(), eq(mockImageFileData))).willReturn("eTag");
    this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/image/analysis")
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data.money").value(mockInvoiceDetail.getMoney()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.vendoraddr")
            .value(mockInvoiceDetail.getVendoraddr()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.vendorname")
            .value(mockInvoiceDetail.getVendorname()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.duedate")
            .value(mockInvoiceDetail.getDuedate()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data.imageid").exists())
        .andReturn();
    logger.info("finished");
  }

  @Test
  void ShouldNotAnalysisImageForPutImageError() throws Exception {
    logger.info("test should analysis image");
    byte[] mockImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    String mockImageFileBase64 = Base64.getEncoder().encodeToString(mockImageFileData);
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, mockImageFileData);

    given(imageStorageService.putImage(any(), eq(mockImageFileData))).willReturn(null);
    this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/image/analysis")
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
    logger.info("finished");
  }

  @Test
  void ShouldNotAnalysisImageForPutImageException() throws Exception {
    logger.info("test should analysis image");
    byte[] mockImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.png",
        MediaType.IMAGE_PNG_VALUE, mockImageFileData);

    given(imageStorageService.putImage(any(), eq(mockImageFileData)))
        .willThrow(new IOException("Put Image Exception"));
    this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/image/analysis")
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(500));
    logger.info("finished");
  }

  @Test
  void ShouldNotAnalysisImageForFileError() throws Exception {
    logger.info("test should analysis image");
    byte[] mockImageFileData = getFileBytes(testImageFile.getAbsolutePath());
    String mockImageFileBase64 = Base64.getEncoder().encodeToString(mockImageFileData);
    MockMultipartFile mockImageFile = new MockMultipartFile("imageFile", "invoice-test-01.pdf",
        MediaType.IMAGE_PNG_VALUE, mockImageFileData);
    given(imageStorageService.putImage(any(), eq(mockImageFileData)))
        .willThrow(new IOException("Put image Exception"));
    this.mockMvc
        .perform(MockMvcRequestBuilders.multipart("/reirequest/image/analysis")
            .file(mockImageFile).session(session)).andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));
    logger.info("finished");
  }
}
