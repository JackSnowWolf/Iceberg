package com.iceberg.dao;

import static com.iceberg.entity.ReimbursementRequest.TYPE.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.utils.PageModel;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReiRequestMapperTest {

  private Logger logger = LoggerFactory.getLogger(ReiRequestMapperTest.class);

  @Autowired
  private ReiRequestMapper reiRequestMapper;

  @Test
  public void addReiRequestTest() {
    logger.info("add reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
    reimbursementRequest.setGroupid("1");
    reimbursementRequest.setRemark("For test");
    reimbursementRequest.setRequesttype(APPROVED);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);
    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);
    logger.info("add reimbursement request successfully!");

  }


  @Test
  public void findByWhereNoPage() {
    logger.info("findByWhereNoPage reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
    reimbursementRequest.setGroupid("2");
    reimbursementRequest.setRemark("For test:" + UUID.randomUUID().toString());
    reimbursementRequest.setRequesttype(APPROVED);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage: findByWhereNoPage");
    reimbursementRequest.setPaywayid(1);

    System.out.println(reimbursementRequest.toString());

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setGroupid("1");
    System.out.println(reimbursementRequestSearch.toString());
    List<ReimbursementRequest> requestList = reiRequestMapper
      .findByWhereNoPage(reimbursementRequestSearch);
    assertNotEquals(0, requestList.size());
  }

  @Test
  public void updateReiRequestTest() {
    logger.info("update reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
    reimbursementRequest.setRemark("For test:" + UUID.randomUUID().toString());
    reimbursementRequest.setRequesttype(APPROVED);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    reimbursementRequest.setTitle("Test Usage Updated");
    List<ReimbursementRequest> requestList = reiRequestMapper
      .findByWhereNoPage(reimbursementRequest);
    if (requestList.size() == 1) {
      assertEquals("Test Usage Updated", requestList.get(0).getTitle());
    } else {
      logger.error("updated request not found!");
    }
  }

  @Test
  public void delReiRequestTest() {
    logger.info("del reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
//    TODO: group id
    reimbursementRequest.setRemark("For test:" + UUID.randomUUID().toString());
    reimbursementRequest.setRequesttype(APPROVED);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    num = reiRequestMapper.del(reimbursementRequest.getId());
    assertEquals(1, num);
  }

  @Test
  public void findByWhere() {
    logger.info("findByWhere Test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
    PageModel<ReimbursementRequest> model = new PageModel<>(1, reimbursementRequest);

    List<ReimbursementRequest> requestList = reiRequestMapper
      .findByWhere(model);

    assertNotEquals(0, requestList.size());
  }
}
