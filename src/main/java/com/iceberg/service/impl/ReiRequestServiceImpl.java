package com.iceberg.service.impl;

import com.iceberg.dao.ReiRequestMapper;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.service.ReiRequestService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ReiRequestServiceImpl implements ReiRequestService {

  @Resource
  private ReiRequestMapper mapper;

  @Override
  public int add(ReimbursementRequest reimbursementRequest) {
    Float money = reimbursementRequest.getMoney();
    if(money > (float)0 && money <= (float)1000) {
      return mapper.add(reimbursementRequest);
    } else {
      return 0;
    }
  }

  @Override
  public int update(ReimbursementRequest reimbursementRequest) throws Exception {
    Float money = reimbursementRequest.getMoney();
    if(money > (float)0 && money <= (float)1000) {
      return mapper.update(reimbursementRequest);
    } else {
      return 0;
    }
  }

  @Override
  public int del(int id) throws Exception {
    return mapper.del(id);
  }

  @Override
  public Result<ReimbursementRequest> findByWhere(PageModel model) {
    try {
      List<ReimbursementRequest> reimbursementRequests = mapper.findByWhere(model);
      Result<ReimbursementRequest> result = ResultUtil.success(reimbursementRequests);
      result.setTotal(mapper.getTotalByWhere(model));
      if (result.getTotal() == 0) {
        result.setMsg("No related data");
      } else {
        result.setMsg("Fetch data successfully");
      }
      return result;

    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  @Override
  public Result<ReimbursementRequest> findByWhereNoPage(ReimbursementRequest reimbursementRequest) {
    try {
      List<ReimbursementRequest> reimbursementRequests = mapper
          .findByWhereNoPage(reimbursementRequest);
//      if (reimbursementRequests.size() >= 0) {
      Result<ReimbursementRequest> result = ResultUtil.success(reimbursementRequests);
      result.setMsg("Fetch data successfully");
      return result;
//      } else {
//        return ResultUtil.unSuccess("No satisfied conditions found!");
//      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  @Override
  public List<Map<String, Float>> getMonthlyInfo(PageModel<ReimbursementRequest> model) {
    return mapper.getMonthlyInfo(model);
  }

  @Override
  public ReimbursementRequest getReimRequestById(Integer id) {
    return mapper.getReimRequestById(id);
  }
}
