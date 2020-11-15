package com.iceberg.service;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.utils.Result;
import com.iceberg.utils.PageModel;
import java.util.List;
import java.util.Map;

public interface ReiRequestService {

    int add(ReimbursementRequest reimbursementRequest);

    int update(ReimbursementRequest reimbursementRequest);

    int del(int id);

    Result<ReimbursementRequest> findByWhere(PageModel model);

    Result<ReimbursementRequest> findByWhereNoPage(ReimbursementRequest reimbursementRequest);

    List<Map<String, Float>> getMonthlyInfo(PageModel<ReimbursementRequest> model);

}
