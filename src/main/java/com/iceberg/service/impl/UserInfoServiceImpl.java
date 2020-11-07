package com.iceberg.service.impl;

import com.iceberg.dao.UserInfoMapper;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.service.UserInfoService;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public int add(UserInfo userInfo) {
        return 0;
    }

    @Override
    public int update(UserInfo userInfo) {
        return 0;
    }

    @Override
    public boolean userIsExisted(UserInfo userInfo) {
        return userInfoMapper.userIsExisted(userInfo) > 0 ? true : false;
    }

    @Override
    public int delete(String id) {
        return 0;
    }

    @Override
    public UserInfo getUserInfo(UserInfo userInfo) {
        return userInfoMapper.getUserInfo(userInfo);
    }

    @Override
    public Result getUsersByWhere(PageModel<UserInfo> model) {
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        return null;
    }

    @Override
    public int addRole(Role role) {
        return 0;
    }

    @Override
    public int updateRole(Role role) {
        return 0;
    }

    @Override
    public int deleteRole(String id) {
        return 0;
    }

    @Override
    public Role getRoleById(String id) {
        return null;
    }

}
