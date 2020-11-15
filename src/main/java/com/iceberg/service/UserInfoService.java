package com.iceberg.service;

import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;

import java.util.List;

public interface UserInfoService {

  int add(UserInfo userInfo);

  int update(UserInfo userInfo);

  boolean userIsExisted(UserInfo userInfo);

  int delete(String id);

  UserInfo getUserInfo(UserInfo userInfo);

  Result getUsersByWhere(PageModel<UserInfo> model);

  List<Role> getAllRoles();

  int addRole(Role role);

  int updateRole(Role role);

  int deleteRole(String id);

  Role getRoleById(String id);

}