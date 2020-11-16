package com.iceberg.dao;

import com.iceberg.entity.Privilege;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeMapper {

  List<Privilege> getPrivilegeByRoleid(int roleid);

  int addDefaultPrivilegesWhenAddRole(String roleid);

  int delPrivilegesWenDelRole(String roleid);
}
