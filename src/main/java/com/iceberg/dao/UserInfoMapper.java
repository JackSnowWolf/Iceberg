package com.iceberg.dao;

import com.iceberg.entity.Group;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.utils.PageModel;
import java.util.List;
import org.springframework.stereotype.Service;

@Service("UserInfoMapper")
public interface UserInfoMapper {

  UserInfo getUserInfo(UserInfo userInfo);

  int addUser(UserInfo userInfo);

  /**
   * whether user exists.
   *
   * @param userInfo user info.
   * @return whether user exists
   */
  int userIsExisted(UserInfo userInfo);

  /**
   * get user with page.
   *
   * @param model page model.
   * @return list of user info.
   */
  List<UserInfo> getUsersByWhere(PageModel<UserInfo> model);

  int getToatlByWhere(PageModel<UserInfo> model);

  int add(UserInfo userInfo);

  int update(UserInfo userInfo);

  int delete(String id);

  List<Role> getAllRoles();

  int addRole(Role role);

  int updateRole(Role role);

  int deleteRole(String id);

  Role getRoleById(String id);

  int addGroupId(Group group);

  UserInfo getUserInfoById(String id);

  int deleteGroup(String groupId);
}
