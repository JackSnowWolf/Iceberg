package com.iceberg.service.impl;

import com.iceberg.dao.PrivilegeMapper;
import com.iceberg.entity.Privilege;
import com.iceberg.service.PrivilegeService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

  @Resource
  private PrivilegeMapper mapper;

  @Override
  public List<Privilege> getPrivilegeByRoleid(int roleid) {
    return this.mapper.getPrivilegeByRoleid(roleid);
  }

  @Override
  public int addDefaultPrivilegesWhenAddRole(String roleid) {
    return mapper.addDefaultPrivilegesWhenAddRole(roleid);
  }

  @Override
  public int delPrivilegesWenDelRole(String roleid) {
    return mapper.delPrivilegesWenDelRole(roleid);
  }
}
