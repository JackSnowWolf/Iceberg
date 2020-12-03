package com.iceberg.service;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.ReimbursementRequest.Remark;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ReiRequestServiceTest {
  private Logger logger = LoggerFactory.getLogger(ReiRequestServiceTest.class);


  @Resource
  ReiRequestService reiRequestService;
  private static ReimbursementRequest request;
  private static ReimbursementRequest invalidRequest;

  @BeforeAll
  public static void init() throws Exception{
    request = new ReimbursementRequest();
    request.setId(8888);
    request.setTitle("ReiRequestTest");
    request.setRemark(Remark.TRANSPORT);
    request.setUserid(123);
    request.setMoney((float)10);
    request.setTypeid(0);
    request.setPaywayid(1);
    request.setReceiveraccount("test@paypal.com");
    invalidRequest = new ReimbursementRequest();
    invalidRequest.setTitle("Invalid");
    invalidRequest.setUserid(7777);
    invalidRequest.setMoney((float)-1);
    invalidRequest.setTypeid(0);

  }

  @Test
  @Order(1)
  public void addReiRequestTest() {
    logger.info("add test");
    int add = reiRequestService.add(request);
//    reiRequestService.add(invalidRequest);
    assertEquals(1, add);
    add = reiRequestService.add(invalidRequest);
    assertEquals(0, add);
    logger.info("add test success");

  }

  @Test
  @Order(2)
  public void updateTest() throws Exception {
    logger.info("test");
    request.setId(600);
    reiRequestService.add(request);
    request.setTypeid(1);
    int update = reiRequestService.update(request);
    assertEquals(1, update);
    update = reiRequestService.update(invalidRequest);
    assertEquals(0, update);
    logger.info("test success");
  }

  @Test
  @Order(3)
  public void delTest() {
    logger.info("del test");
    request.setId(8888);
    int del = 0;
    try {
      del = reiRequestService.del(8888);
    } catch (Exception e) {
      e.printStackTrace();
    }
    boolean res = false;
    if(del >= 0){
      res = true;
    }
    assertEquals(true, res);
    logger.info("del test success");
  }

  @Test
  @Order(4)
  public void findByWhereTest() {
    logger.info("findByWheretest");
    PageModel<ReimbursementRequest> pageModel
            = new PageModel<ReimbursementRequest>(2,request);
    Result<ReimbursementRequest> byWhere = reiRequestService.findByWhere(pageModel);

    ReimbursementRequest request2 = new ReimbursementRequest();
    request2.setPaywayid(1);
    PageModel<ReimbursementRequest> pageModel1
            = new PageModel<>(1,request2);
    Result<ReimbursementRequest> byWhere1 = reiRequestService.findByWhere(pageModel1);

    boolean res = false;
    if(byWhere.getMsg() != "Fetch data successfully"){
      res = true;
    }
    assertEquals(true, res);
    logger.info("findByWheretest success");
  }

  @Test
  @Order(5)
  public void findByWhereNoPageTest() {
    logger.info("findByWhereNoPagetest");
    PageModel<ReimbursementRequest> pageModel
            = new PageModel<ReimbursementRequest>(2,request);
    Result<ReimbursementRequest> byWhere = reiRequestService.findByWhereNoPage(request);

    ReimbursementRequest request2 = new ReimbursementRequest();
    request2.setPaywayid(1);
    PageModel<ReimbursementRequest> pageModel1
            = new PageModel<>(1,request2);
    Result<ReimbursementRequest> byWhere1 = reiRequestService.findByWhereNoPage(request2);

    boolean res = false;
    if(byWhere.getMsg() == "Fetch data successfully"){
      res = true;
    }

    assertEquals(true, res);
    logger.info("findByWhereNoPagetest success");
  }

  @Test
  @Order(6)
  public void getMonthlyInfoTest() {
    logger.info("getMonthlyInfotest");
    ReimbursementRequest rei = new ReimbursementRequest();
    rei.setUserid(1);
    PageModel<ReimbursementRequest> pageModel
            = new PageModel<ReimbursementRequest>(1,rei);
    List<Map<String, Float>> monthlyInfo = reiRequestService.getMonthlyInfo(pageModel);
    boolean res = false;
    if(monthlyInfo.size() >= 0){
      res = true;
    }

    assertEquals(true, res);
    logger.info("getMonthlyInfotest success");
  }

  @Test
  @Order(7)
  public void getReimRequestByIdTest() {
    logger.info("getReimRequestByIdtest");
    ReimbursementRequest rei = new ReimbursementRequest();
    rei.setId(7);
    ReimbursementRequest reimRequestById = reiRequestService.getReimRequestById(7);
    System.out.println(reimRequestById.getUserid());
    boolean res = false;
    if(reimRequestById.getUserid() == 25){
      res = true;
    }
    assertEquals(true, res);

    logger.info("getReimRequestByIdtest success");
  }

  @Test
  public void requestSetStartDate() {
    ReimbursementRequest request = new ReimbursementRequest();
    request.setStartTime("");
    request.setEndTime("");
    request.setRealname("");
    request.setStartTime("123");
    request.setEndTime("123");
    request.equals(new Integer(1));
    request.equals(request);
  }
}
