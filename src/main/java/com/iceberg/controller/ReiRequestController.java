package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.status.*;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.ReimbursementRequest.status.*;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.ReiRequestService;
import com.iceberg.utils.*;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/reirequest")
public class ReiRequestController {

    @Resource
    private ReiRequestService reiRequestService;

    @RequestMapping(value = "/addRequest", method = RequestMethod.POST)
    public Result add(ReimbursementRequest reimbursementRequest, HttpSession session) {
        if (Config.getSessionUser(session) != null) {
            reimbursementRequest.setUserid(Config.getSessionUser(session).getId());
        }
        Utils.log(reimbursementRequest.toString());
        try {
            int num = reiRequestService.add(reimbursementRequest);
            if (num > 0) {
                int reimbursementRequestId = reimbursementRequest.getId();
                reimbursementRequest = new ReimbursementRequest();
                reimbursementRequest.setId(reimbursementRequestId);
                reimbursementRequest.setRequststatus(PROCESSING);
                return ResultUtil.success("Reimbursement Request Successfully!",
                    reiRequestService.findByWhereNoPage(reimbursementRequest));
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }
    }

    /**
     * the owen of the reimbursement request can update the reimbursement
     *
     * @param reimbursementRequest reimbursement request
     * @param session http session
     * @return result information
     */
    public Result update(ReimbursementRequest reimbursementRequest, HttpSession session) {
        if (Config.getSessionUser(session) == null) {
            return ResultUtil.unSuccess("No user for current session");
        }
        int userId = Config.getSessionUser(session).getId();
        int roleId = Config.getSessionUser(session).getRoleid();
        ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
        reimbursementRequestSearch.setId(reimbursementRequest.getId());
        Result<ReimbursementRequest> result = reiRequestService
            .findByWhereNoPage(reimbursementRequestSearch);
        if (result.getTotal() == 0) {
            return ResultUtil.unSuccess("Such reimbursement request doesn't exist!");
        } else if (result.getTotal() > 2) {
            return ResultUtil.unSuccess("reimbursement request duplicate!");
        } else {
            if (roleId == 3 && result.getData().getUserid() != userId) {
                return ResultUtil.unSuccess("Permission denied!");
            } else if (result.getData().getRequststatus() == APPROVED) {
                return ResultUtil.unSuccess("Request has already been approved!");
            }
            try {
                reimbursementRequest.setRequststatus(PROCESSING);
                int num = reiRequestService.update(reimbursementRequest);
                if (num > 0) {
                    return ResultUtil.success("Update successfully!", null);
                } else {
                    return ResultUtil.unSuccess();
                }
            } catch (Exception e) {
                return ResultUtil.error(e);
            }
        }
    }

    /**
     * review reimbursement request only for admin and group manager
     *
     * @param reimbursementRequest reimbursement request
     * @param session http session
     * @return result information whether the request has been approved.
     */
    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public Result review(ReimbursementRequest reimbursementRequest, HttpSession session) {
        if (Config.getSessionUser(session) == null) {
            return ResultUtil.unSuccess("No user for current session");
        }
        if (Config.getSessionUser(session).getRoleid() > 2) {
            return ResultUtil.unSuccess("Permission denied. Don't have review access.");
        }
        if (reimbursementRequest.getRequststatus() == APPROVED) {
            // TODO: approve such request
        } else if (reimbursementRequest.getRequststatus() == PROCESSING) {
            return ResultUtil.unSuccess("Not valid review");
        }
        try {
            int num = reiRequestService.update(reimbursementRequest);
            if (num > 0) {
                return ResultUtil.success(String.format("Update successfully! Status : %s",
                    reimbursementRequest.getRequststatus()), null);
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }
    }


}
