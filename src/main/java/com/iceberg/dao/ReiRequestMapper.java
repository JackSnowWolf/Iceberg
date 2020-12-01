package com.iceberg.dao;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.utils.PageModel;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface ReiRequestMapper {

  int add(ReimbursementRequest reimbursementRequest);

  int update(ReimbursementRequest reimbursementRequest);

  int del(int id);

  List<ReimbursementRequest> findByWhere(PageModel<ReimbursementRequest> model);

  List<ReimbursementRequest> findByWhereNoPage(ReimbursementRequest model);

  int getTotalByWhere(PageModel<ReimbursementRequest> model);

  List<Map<String, Float>> getMonthlyInfo(PageModel<ReimbursementRequest> model);

  ReimbursementRequest getReimRequestById(Integer id);
}
