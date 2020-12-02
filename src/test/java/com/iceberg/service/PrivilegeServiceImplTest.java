package com.iceberg.service;


import com.iceberg.entity.Privilege;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PrivilegeServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(PrivilegeServiceImplTest.class);

    @Resource
    private PrivilegeService privilegeService;

    @Test
    public void addDefaultPrivilegesWhenAddRoleTest() {
        logger.info("addDefaultPrivilegesWhenAddRoleTest test");
        int res = privilegeService.addDefaultPrivilegesWhenAddRole("3");
        assertEquals(6, res);
        res = privilegeService.addDefaultPrivilegesWhenAddRole("qwe");
        System.out.println(res);
        assertEquals(0, res);
        logger.info("addDefaultPrivilegesWhenAddRoleTest test success");


    }

    @Test
    public void getPrivilegeByRoleId() {
        logger.info("getPrivilegeByRoleId test");
        List<Privilege> all = privilegeService.getPrivilegeByRoleid(1);
//        System.out.println(all.get(0).toString());
        assertEquals(10, all.size());
        logger.info("getPrivilegeByRoleId test success");

    }

    @Test
    public void delPrivilegesWenDelRoleTest() {
        logger.info("delPrivilegesWenDelRoleTest test");
        int res = privilegeService.delPrivilegesWenDelRole("3");
        System.out.println(res);
        boolean re = false;
        if(res > 0){
            re = true;
        }
        assertEquals(true, re);
        res = privilegeService.delPrivilegesWenDelRole("yuy");
        assertEquals(0, res);
        logger.info("delPrivilegesWenDelRoleTest test success");
    }

}
