package com.iceberg.service.impl;

import com.iceberg.dao.UserInfoMapper;
import com.iceberg.entity.Group;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import com.iceberg.utils.ResultUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Resource
  private UserInfoMapper userInfoMapper;

  @Override
  public int add(UserInfo userInfo) {
    // add user
    int result = userInfoMapper.add(userInfo);
    System.out.println(userInfo);
    if (userInfo.getRoleid() == 2) {
      // if the user is group manager, then set the managerid
      Group newGroup = new Group();
      newGroup.setManagerid(userInfo.getId());
      int r = userInfoMapper.addGroupId(newGroup);

      // successful, then bind members to this user
      if (r == 1) {
        userInfo.setGroupid(newGroup.getId().toString());
        result = userInfoMapper.update(userInfo);
      }
    }
    return result;
  }

  @Override
  public int update(UserInfo userInfo) {
    return userInfoMapper.update(userInfo);
  }

  @Override
  public boolean userIsExisted(UserInfo userInfo) {
    return userInfoMapper.userIsExisted(userInfo) > 0 ? true : false;
  }

  @Override
  public int delete(String id) {
    return userInfoMapper.delete(id);
  }

  @Override
  public UserInfo getUserInfo(UserInfo userInfo) {
    return userInfoMapper.getUserInfo(userInfo);
  }

  @Override
  public Result getUsersByWhere(PageModel<UserInfo> model) {
    try {
      List<UserInfo> users = userInfoMapper.getUsersByWhere(model);
      if (users.size() >= 0) {
        Result<UserInfo> result = ResultUtil.success(users);
        result.setTotal(userInfoMapper.getToatlByWhere(model));
        if (result.getTotal() == 0) {
          result.setMsg("No related data");
        } else {
          result.setMsg("Data fetched successfully");
        }
        return result;
      } else {
        return ResultUtil.unSuccess("no satisfied condition found！");
      }
    } catch (Exception e) {
      return ResultUtil.error(e);
    }
  }

  @Override
  public List<Role> getAllRoles() {
    return userInfoMapper.getAllRoles();
  }

  @Override
  public int addRole(Role role) {
    return userInfoMapper.addRole(role);
  }

  @Override
  public int updateRole(Role role) {
    return userInfoMapper.updateRole(role);
  }

  @Override
  public int deleteRole(String id) {
    return userInfoMapper.deleteRole(id);
  }

  @Override
  public Role getRoleById(String id) {
    return userInfoMapper.getRoleById(id);
  }

  @Override
  public UserInfo getUserInfoById(String id) {
    return userInfoMapper.getUserInfoById(id);
  }
}
