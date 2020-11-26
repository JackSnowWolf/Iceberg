package com.iceberg.dao;

import static com.iceberg.entity.ReimbursementRequest.Status.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.ReimbursementRequest.Remark;
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
    reimbursementRequest.setUserid(123);
    reimbursementRequest.setGroupid("1");
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setTypeid(APPROVED.value);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);
    reimbursementRequest.setImageid("test.png");
    reimbursementRequest.setReceiveraccount("test@paypal.com");
    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);
    logger.info("add reimbursement request successfully!");
  }


  @Test
  public void findByWhereNoPage() {
    logger.info("findByWhereNoPage reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(123);
    reimbursementRequest.setGroupid("2");
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setTypeid(APPROVED.value);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setReceiveraccount("test@paypal.com");
    reimbursementRequest.setTitle("Test Usage: findByWhereNoPage");
    reimbursementRequest.setPaywayid(1);

    System.out.println(reimbursementRequest.toString());

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    reimbursementRequestSearch.setUserid(123);
    reimbursementRequestSearch.setReceiveraccount("test@paypal.com");
    reimbursementRequestSearch.setRemark(Remark.TRANSPORT);
    System.out.println(reimbursementRequestSearch.toString());
    List<ReimbursementRequest> requestList = reiRequestMapper
        .findByWhereNoPage(reimbursementRequestSearch);
    assertNotEquals(0, requestList.size());
  }

  @Test
  public void shouldGetReimRequestById(){
    logger.info("get reimbursement request by id test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(123);
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setTypeid(APPROVED.value);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);
    ReimbursementRequest resultRequest = reiRequestMapper.getReimRequestById(reimbursementRequest.getId());

    assertEquals(reimbursementRequest.getTitle(), resultRequest.getTitle());
    assertEquals(reimbursementRequest.getPaywayid(), resultRequest.getPaywayid());
    assertEquals(reimbursementRequest.getRemark(), resultRequest.getRemark());
    assertEquals(reimbursementRequest.getMoney(), resultRequest.getMoney());
    assertEquals(reimbursementRequest.getTypeid(), resultRequest.getTypeid());
    assertEquals(reimbursementRequest.getUserid(), resultRequest.getUserid());
  }

  @Test
  public void updateReiRequestTest() {
    logger.info("update reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(123);
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setTypeid(APPROVED.value);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setImageid("test.png");
    reimbursementRequest.setPaywayid(1);

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    ReimbursementRequest updatedReimbursementRequest = new ReimbursementRequest();
    updatedReimbursementRequest.setId(reimbursementRequest.getId());
    updatedReimbursementRequest.setTitle("Test Usage Updated");

    num = reiRequestMapper.update(updatedReimbursementRequest);
    assertEquals(1, num);

    reimbursementRequest = reiRequestMapper.getReimRequestById(reimbursementRequest.getId());
    assertEquals(updatedReimbursementRequest.getTitle(), reimbursementRequest.getTitle());
  }

  @Test
  public void delReiRequestTest() {
    logger.info("del reimbursement request test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(123);
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setTypeid(APPROVED.value);
    reimbursementRequest.setRemark(Remark.TRANSPORT);
    reimbursementRequest.setMoney((float) 100.0);
    reimbursementRequest.setTitle("Test Usage");
    reimbursementRequest.setPaywayid(1);

    int num = reiRequestMapper.add(reimbursementRequest);
    assertEquals(1, num);

    num = reiRequestMapper.del(reimbursementRequest.getId());
    assertEquals(1, num);
  }

  @Test
  public void findByWhereForNormalUser() {
    logger.info("findByWhere Test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setUserid(1);
    PageModel<ReimbursementRequest> model = new PageModel<>(1, reimbursementRequest);
    model.setPageSize(10);
    List<ReimbursementRequest> requestList = reiRequestMapper
        .findByWhere(model);

    assertEquals(10, requestList.size());
  }

  @Test
  public void findByWhereForGroupManager() {
    logger.info("findByWhere Test");
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setGroupid("1");
    PageModel<ReimbursementRequest> model = new PageModel<>(1, reimbursementRequest);
    model.setPageSize(10);
    List<ReimbursementRequest> requestList = reiRequestMapper
        .findByWhere(model);

    assertEquals(10, requestList.size());
  }

}
