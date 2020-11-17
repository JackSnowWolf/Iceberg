package com.iceberg.dao;

import com.iceberg.entity.Privilege;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@MapperScan(basePackages = {"com.iceberg.dao","resources.mappers"})
public class PrivilegeMapperTest {

    private Logger logger = LoggerFactory.getLogger(PrivilegeMapperTest.class);

    @Autowired
    private PrivilegeMapper privilegeMapper;

    /**
     * When a new user is created, he will be given basic 6 privileges
     * by default.
     */
    @Test
    public void addDefaultPrivilegesWhenAddRoleTest() {
        logger.info("addDefaultPrivilegesWhenAddRoleTest test");
        int res = privilegeMapper.addDefaultPrivilegesWhenAddRole("3");
        System.out.println(res);
        assertEquals(6, res);
        logger.info("addDefaultPrivilegesWhenAddRoleTest test success");


    }

    /**
     * Given a role id, find what privileges this kind of role has.
     */
    @Test
    public void getPrivilegeByRoleId() {
        logger.info("getPrivilegeByRoleId test");
        List<Privilege> all = privilegeMapper.getPrivilegeByRoleid(1);
//        System.out.println(all.get(0).toString());
        assertEquals(10, all.size());
        logger.info("getPrivilegeByRoleId test success");

    }


    /**
     * Given a role id, delete all its related privileges.
     */
    @Test
    public void delPrivilegesWenDelRoleTest() {
        logger.info("delPrivilegesWenDelRoleTest test");
        int res = privilegeMapper.delPrivilegesWenDelRole("3");
        System.out.println(res);
        assertEquals(6, res);
        logger.info("delPrivilegesWenDelRoleTest test success");
    }

}
