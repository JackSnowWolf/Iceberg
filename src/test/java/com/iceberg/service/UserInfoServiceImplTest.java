package com.iceberg.service;


import com.iceberg.entity.Privilege;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;


import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class UserInfoServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(UserInfoServiceImplTest.class);

    @Resource
    private UserInfoService userInfoService;

    private static UserInfo userInfo;
    private static UserInfo userInfo2;

    @BeforeAll
    public static void init() throws Exception{
        userInfo=new UserInfo();
        userInfo.setUsername("hwj");
        userInfo.setPassword("hwj");
        userInfo.setId(1);
        userInfo.setRoleid(1);
        userInfo.setRealname("hwj");

        userInfo2=new UserInfo();
        userInfo2.setUsername("test1116");
        userInfo2.setPassword("test1116");
//        userInfo2.setId(1116);
        userInfo2.setRoleid(3);
    }


    @Test
    @Order(1)
    public void testGetUserInfo() {
        logger.info("user get test");
        UserInfo outputuserinfo=userInfoService.getUserInfo(userInfo);

        assertEquals(userInfo.getId(),outputuserinfo.getId());
        assertEquals(userInfo.getUsername(),outputuserinfo.getUsername());

        logger.info("user get test success");
    }

    @Test
    @Order(2)
    public void testUserIsExisted() {
        logger.info("user existed test");

        boolean result=false;
        result=userInfoService.userIsExisted(userInfo);
        assertEquals(true,result);

        logger.info("user existed test success");
    }

    @Test
    @Order(2)
    public void testAdd() {
        logger.info("user add test");

        int output=userInfoService.add(userInfo2);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        logger.info("user add test success");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        logger.info("user update test");
        UserInfo tempuser=new UserInfo();
        tempuser=new UserInfo();
        tempuser.setUsername("hwj");
        tempuser.setPassword("hwj");
        tempuser.setId(1);
        tempuser.setRoleid(1);
        int output=userInfoService.update(tempuser);
        boolean result=false;
        if(output>0){
            result=true;
        }

        assertEquals(true,result);

        logger.info("user update test success");
    }


    @Test
    @Order(4)
    public void testDelete() {
        logger.info("user delete test");
        UserInfo tempuser2=new UserInfo();
        tempuser2.setUsername("stest1116");
        tempuser2.setPassword("stest1116");
        tempuser2.setId(2021);
        tempuser2.setRoleid(3);
        int output=userInfoService.add(tempuser2);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        int output2=userInfoService.delete("2021");
        boolean result2=false;
        if(output2>0){
            result2=true;
        }
        assertEquals(true,result2);

        logger.info("user delete test success");
    }

    @Test
    public void testGetUsersByWhere() {

    }

    @Test
    public void testGetAllRoles() {
        logger.info("get all roles test");
        List<Role> roleList=userInfoService.getAllRoles();
        assertEquals("Administrator",roleList.get(0).getRolename());
        assertEquals("Group Manager",roleList.get(1).getRolename());
        assertEquals("Normal User",roleList.get(2).getRolename());
        logger.info("get all roles test success");
    }

    @Test
    public void testAddRole() {
        logger.info("role add test");
        Role role=new Role();
        role.setRoleid(100);
        role.setRolename("test");
        int output=userInfoService.addRole(role);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        int output2=userInfoService.deleteRole("100");
        boolean result2=false;
        if(output2>0){
            result2=true;
        }
        assertEquals(true,result2);

        logger.info("role add test success");
    }

    @Test
    public void testUpdateRole() {
        logger.info("role update test");
        Role role=new Role();
        role.setRoleid(3);
        role.setRolename("Normal User");
        int output=userInfoService.updateRole(role);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        logger.info("role update test success");
    }

    @Test
    public void testDeleteRole() {
        logger.info("role delete test");
        Role role=new Role();
        role.setRoleid(100);
        role.setRolename("test");
        int output=userInfoService.addRole(role);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        int output2=userInfoService.deleteRole("100");
        boolean result2=false;
        if(output2>0){
            result2=true;
        }
        assertEquals(true,result2);
        logger.info("role delete test success");
    }

    @Test
    public void testGetRoleById() {
        logger.info("role get test");
        Role role=userInfoService.getRoleById("1");
        assertEquals("Administrator",role.getRolename());

        logger.info("role get test success");
    }
}
