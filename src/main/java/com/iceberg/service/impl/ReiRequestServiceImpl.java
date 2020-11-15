package com.iceberg.service.impl;

import com.iceberg.dao.ReiRequestMapper;
import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.service.ReiRequestService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ReiRequestServiceImpl implements ReiRequestService {

    @Resource
    private ReiRequestMapper mapper;

    @Override
    public int add(ReimbursementRequest reimbursementRequest) {
        return mapper.add(reimbursementRequest);
    }

    @Override
    public int update(ReimbursementRequest reimbursementRequest) {
        return mapper.update(reimbursementRequest);
    }

    @Override
    public int del(int id) {
        return mapper.del(mapper.del(id));
    }

    @Override
    public Result<ReimbursementRequest> findByWhere(PageModel model) {
        try {
            List<ReimbursementRequest> reimbursementRequests = mapper.findByWhere(model);
            if (reimbursementRequests.size() >= 0) {
                Result<ReimbursementRequest> result = ResultUtil.success(reimbursementRequests);
                result.setTotal(mapper.getTotalByWhere(model));
                if (result.getTotal() == 0) {
                    result.setMsg("No related data");
                } else {
                    result.setMsg("Fetch data successfully");
                }
                return result;
            } else {
                return ResultUtil.unSuccess("Get data failure");
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }
    }

    @Override
    public Result<ReimbursementRequest> findByWhereNoPage(
        ReimbursementRequest reimbursementRequest) {
        try {
            List<ReimbursementRequest> reimbursementRequests = mapper
                .findByWhereNoPage(reimbursementRequest);
            if (reimbursementRequests.size() >= 0) {
                Result<ReimbursementRequest> result = ResultUtil.success(reimbursementRequests);
                result.setMsg("Fetch data successfully");
                return result;
            } else {
                return ResultUtil.unSuccess("No satisfied conditions found!");
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }
    }

    @Override
    public List<Map<String, Float>> getMonthlyInfo(PageModel<ReimbursementRequest> model) {
        return mapper.getMonthlyInfo(model);
    }
}
