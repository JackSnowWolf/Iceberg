package com.iceberg.dao;


import com.iceberg.entity.Privilege;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
//@MapperScan(basePackages = {"com.iceberg.dao","resources.mappers"})
public class PrivilegeMapperTest {

    private Logger logger = LoggerFactory.getLogger(PrivilegeMapperTest.class);

    @Autowired
    private PrivilegeMapper privilegeMapper;


    @Test
    public void getPrivilegeByRoleId() {
        logger.info("getPrivilegeByRoleId test");
        List<Privilege> all = privilegeMapper.getPrivilegeByRoleid(1);
        System.out.println(all.get(0).toString());
        logger.info("getPrivilegeByRoleId test success");

    }

    @Test
    public void addDefaultPrivilegesWhenAddRoleTest() {
        logger.info("addDefaultPrivilegesWhenAddRoleTest test");
        int res = privilegeMapper.addDefaultPrivilegesWhenAddRole("1");
        System.out.println(res);
        logger.info("addDefaultPrivilegesWhenAddRoleTest test success");


    }

    @Test
    public void delPrivilegesWenDelRoleTest() {
        logger.info("delPrivilegesWenDelRoleTest test");
        int res = privilegeMapper.delPrivilegesWenDelRole("1");
        System.out.println(res);
        logger.info("delPrivilegesWenDelRoleTest test success");
    }

}
