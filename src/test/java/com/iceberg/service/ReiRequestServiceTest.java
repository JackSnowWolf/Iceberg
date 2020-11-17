package com.iceberg.service;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.service.impl.ReiRequestServiceImpl;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;

public class ReiRequestServiceTest {

  @Resource
  ReiRequestServiceImpl reiRequestService;

  @Test
  public void addReiRequestTest() {
    ReimbursementRequest request = new ReimbursementRequest();
    request.setId(10000);
    request.setUserid(1);
    request.setTypeid(0);
    request.setTitle("testtestestest");
    request.setMoney((float) 10000);

  }

}
