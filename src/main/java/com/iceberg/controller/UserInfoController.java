package com.iceberg.controller;

import com.iceberg.entity.Privilege;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.PrivilegeService;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.Config;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import com.iceberg.utils.Utils;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * description: this class is mainly focus on user's account information.
 */

@Controller
public class UserInfoController {

  @Resource
  private UserInfoService userInfoService;
  @Resource
  private PrivilegeService privilegeService;

  /**
   * login handler.
   * @param request http request
   * @param response http response
   * @return redirect path.
   */
  @RequestMapping(value = {"/", "login.html"})
  public String toLogin(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    if (session.getAttribute(Config.CURRENT_USERNAME) == null) {
      System.out.println("session attribute is null");
      return "login";
    } else {
      try {
        response.sendRedirect("/pages/index");
        return null;
      } catch (IOException e) {
        return "login";
      }
    }
  }

  /**
   * get user info for login.
   * @param userInfo user info
   * @param request http request
   * @param response http response
   * @return result message.
   */
  @RequestMapping(value = "/login.do")
  @ResponseBody
  public Result getUserInfo(UserInfo userInfo, HttpServletRequest request,
      HttpServletResponse response) {
    boolean userIsExisted = userInfoService.userIsExisted(userInfo);
    System.out.println(userIsExisted + " - " + request.getHeader("token"));
    userInfo = getUserInfo(userInfo);
    if (userIsExisted && userInfo == null) {
      return ResultUtil.unSuccess("wrong username or password!");
    } else {
      // save user info in session
      userInfo = setSessionUserInfo(userInfo, request.getSession());
      // save user info in cookie
      // setCookieUser(request,response);
      return ResultUtil.success("login successful", userInfo);
    }
  }

  /**
   * get user info from service.
   * @param userInfo user info.
   * @return user info in the database.
   */
  public UserInfo getUserInfo(UserInfo userInfo) {
    return userInfoService.getUserInfo(userInfo);
  }


  /**
   * get user privilege info through user info and save it in session.
   *
   * @param userInfo user info.
   * @param session http session.
   * @return user info.
   */
  public UserInfo setSessionUserInfo(UserInfo userInfo, HttpSession session) {
    List<Privilege> privileges = privilegeService.getPrivilegeByRoleid(userInfo.getRoleid());
    userInfo.setPrivileges(privileges);
    session.setAttribute(Config.CURRENT_USERNAME, userInfo);
    return userInfo;

  }

  /**
   * get user info by where.
   * @param userInfo query user info.
   * @param pageNo page no
   * @param pageSize page size.
   * @param session http session.
   * @return result message
   */
  @RequestMapping("/users/getUsersByWhere/{pageNo}/{pageSize}")
  public @ResponseBody
  Result getUsersByWhere(UserInfo userInfo, @PathVariable int pageNo, @PathVariable int pageSize,
      HttpSession session) {
    if ("".equals(userInfo.getGroupid()) || userInfo.getGroupid() == null) {
      userInfo.setGroupid(null);
    }
    //    if (userInfo.getRoleid() == -1) {
    //      //System.out.println("*****" + Config.getSessionUser(session));
    //      userInfo.setRoleid(Config.getSessionUser(session).getRoleid());
    //    }
    //group manager cannot search administrator's userinfo.
    if (userInfo.getGroupid() != null && userInfo.getRoleid() == 1) {
      //cannot search user with role 1
      userInfo.setRoleid(2);
    }
    Utils.log(userInfo.toString());
    PageModel model = new PageModel<>(pageNo, userInfo);
    model.setPageSize(pageSize);
    return userInfoService.getUsersByWhere(model);
  }

  /**
   * add user.
   * @param userInfo user info
   * @return result message in response body.
   */
  @RequestMapping("/user/add")
  public @ResponseBody
  Result addUser(UserInfo userInfo) {
    System.out.println(userInfo);
    try {
      int num = userInfoService.add(userInfo);
      if (num > 0) {
        return ResultUtil.success();
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * update user info.
   * @param userInfo user info.
   * @return result message in response body.
   */
  @RequestMapping("/user/update")
  public @ResponseBody
  Result updateUser(UserInfo userInfo, HttpSession session) {
    try {
      int num = userInfoService.update(userInfo);
      //if he updates his own info, then update the session user.
      if (num > 0) {
        System.out.println();
        UserInfo sessionUser = Config.getSessionUser(session);
        System.out.println("sessionUser is null?" + sessionUser);
        System.out.println(sessionUser.getId());
        System.out.println(userInfo.getId());
        if (sessionUser.getId().equals(userInfo.getId())) {
          System.out.println("same user update");
          sessionUser.setUsername(userInfo.getUsername());
          sessionUser.setRealname(userInfo.getRealname());
          sessionUser.setEmail(userInfo.getEmail());
          sessionUser.setPassword(userInfo.getPassword());
        }
        System.out.println("SessionUser now is :" + sessionUser);
        return ResultUtil.success();
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * delete user.
   * @param id user id.
   * @return result message in response body.
   */
  @RequestMapping("/user/del/{id}")
  public @ResponseBody
  Result deleteUser(@PathVariable String id) {
    try {
      int num = userInfoService.delete(id);
      if (num > 0) {
        return ResultUtil.success();
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * get session user.
   * @param session http session.
   * @return result message.
   */
  @RequestMapping("/getSessionUser")
  @ResponseBody
  public UserInfo getSessionUser(HttpSession session) {
    UserInfo sessionUser = (UserInfo) session.getAttribute(Config.CURRENT_USERNAME);
    sessionUser.setPassword(null);
    return sessionUser;
  }

  /**
   * logout.
   * @param request http request.
   * @param response http response
   * @return redirect url.
   */
  @RequestMapping("/logout")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    //  delCookieUser(request, response);
    request.getSession().removeAttribute(Config.CURRENT_USERNAME);
    return "login";
  }

  /**
   * redirect pages.
   * @param page request page.
   * @return redirect url.
   */
  @RequestMapping("/pages/{page}")
  public String toPage(@PathVariable String page) {
    return page.replace("_", "/");
  }

  /**
   * get all roles.
   * @return all roles.
   */
  @RequestMapping("/getAllRoles")
  public @ResponseBody
  Result<Role> getAllRoles() {
    try {
      List<Role> roles = userInfoService.getAllRoles();
      return ResultUtil.success(roles);
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * add role.
   * @param role role
   * @return result message
   */
  @RequestMapping("/role/add")
  public @ResponseBody
  Result addRole(Role role) {
    try {
      int num = userInfoService.addRole(role);
      if (num > 0) {
        privilegeService.addDefaultPrivilegesWhenAddRole(role.getRoleid().toString());
        return ResultUtil.success();
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * update role.
   * @param role role
   * @return result message.
   */
  @RequestMapping("/role/update")
  public @ResponseBody
  Result updateRole(Role role) {
    try {
      int num = userInfoService.updateRole(role);
      if (num > 0) {
        return ResultUtil.success();
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * delete role.
   * @param roleid role id.
   * @return result message
   */
  @RequestMapping("/role/del/{roleid}")
  public @ResponseBody
  Result deleteRole(@PathVariable String roleid) {
    try {
      privilegeService.delPrivilegesWenDelRole(roleid);
      int num = userInfoService.deleteRole(roleid);
      if (num > 0) {
        return ResultUtil.success();
      } else {
        privilegeService.addDefaultPrivilegesWhenAddRole(roleid);
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  /**
   * get role by id.
   * @param id role id
   * @return result message
   */
  @RequestMapping("/getRole/{id}")
  public @ResponseBody
  Result getRoleById(@PathVariable String id) {
    try {
      Role role = userInfoService.getRoleById(id);
      if (role != null) {
        return ResultUtil.success(role);
      } else {
        return ResultUtil.unSuccess();
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }
}
