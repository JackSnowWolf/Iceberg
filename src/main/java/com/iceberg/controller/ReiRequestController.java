package com.iceberg.controller;

import static com.iceberg.entity.ReimbursementRequest.TYPE.APPROVED;
import static com.iceberg.entity.ReimbursementRequest.TYPE.PROCESSING;

import com.iceberg.entity.ReimbursementRequest;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.ReiRequestService;
import com.iceberg.utils.Config;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import com.iceberg.utils.Utils;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reirequest")
public class ReiRequestController {

  private Logger logger = LoggerFactory.getLogger(ReiRequestController.class);
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
        reimbursementRequest.setRequesttype(PROCESSING);
        logger.debug(reimbursementRequest.toString());
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
      } else if (result.getData().getRequesttype() == APPROVED) {
        return ResultUtil.unSuccess("Request has already been approved!");
      }
      try {
        reimbursementRequest.setRequesttype(PROCESSING);
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
    if (reimbursementRequest.getRequesttype() == APPROVED) {
      // TODO: approve such request
    } else if (reimbursementRequest.getRequesttype() == PROCESSING) {
      return ResultUtil.unSuccess("Not valid review");
    }
    try {
      int num = reiRequestService.update(reimbursementRequest);
      if (num > 0) {
        return ResultUtil.success(String.format("Update successfully! Status : %s",
          reimbursementRequest.getRequesttype()), null);
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  @RequestMapping("/getReiRequestlById/{id}")
  public Result getReiRequestById(@PathVariable Integer id) {
    ReimbursementRequest reimbursementRequestCondition = new ReimbursementRequest();
    reimbursementRequestCondition.setId(id);

    try {
      return reiRequestService.findByWhereNoPage(reimbursementRequestCondition);
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  @RequestMapping("/getReiRequestByUserid/{userid}/{pageNo}/{pageSize}")
  public Result getReiRequestByUserid(@PathVariable int userid, @PathVariable int pageNo,
    @PathVariable int pageSize) {
    ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
    reimbursementRequest.setId(userid);

    PageModel model = new PageModel<>(pageNo, reimbursementRequest);
    model.setPageSize(pageSize);

    return reiRequestService.findByWhere(model);
  }

  @RequestMapping("/getReiRequestByNoPage")
  public Result getReiRequestByGroup(ReimbursementRequest reimbursementRequest,
    HttpSession session) {
    ReimbursementRequest reimbursementRequestSearch = new ReimbursementRequest();
    UserInfo currentUser = Config.getSessionUser(session);
    // search for group reimbursement information if group manage
    if (currentUser.getRoleid() == 2) {
      reimbursementRequestSearch.setGroupid(currentUser.getGroupid());
    } else if (currentUser.getRoleid() == 3) {
      reimbursementRequestSearch.setUserid(currentUser.getId());
    }
    return reiRequestService.findByWhereNoPage(reimbursementRequestSearch);
  }

}
