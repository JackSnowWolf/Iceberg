package com.iceberg.service;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import java.util.List;
import java.util.Map;

public interface ReiRequestService {

  int add(ReimbursementRequest reimbursementRequest);

  int update(ReimbursementRequest reimbursementRequest) throws Exception;

  int del(int id) throws Exception;

  Result<ReimbursementRequest> findByWhere(PageModel model);

  Result<ReimbursementRequest> findByWhereNoPage(ReimbursementRequest reimbursementRequest);

  List<Map<String, Float>> getMonthlyInfo(PageModel<ReimbursementRequest> model);

  ReimbursementRequest getReimRequestById(Integer id);

}
