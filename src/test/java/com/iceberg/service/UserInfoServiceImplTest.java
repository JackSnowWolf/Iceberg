package com.iceberg.service;


import com.iceberg.entity.Group;
import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.utils.PageModel;
import com.iceberg.utils.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testGetUserInfoWithOnlyId(){
        logger.info("user with only id get test");
        UserInfo userInfoWithOnlyId=new UserInfo();
        userInfoWithOnlyId.setId(1);

        UserInfo outputuserinfo=userInfoService.getUserInfo(userInfoWithOnlyId);

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
//        userInfo2.setRoleid(4);

        int output=userInfoService.add(userInfo2);
        boolean result=false;
        if(output>0){
            result=true;
        }
        assertEquals(true,result);

        UserInfo invalidUserInfo = new UserInfo();
        invalidUserInfo.setRoleid(4);
        invalidUserInfo.setUsername("invalid");
        invalidUserInfo.setPassword("invalid");
        output = userInfoService.add(invalidUserInfo);
        if(output == 0){
            result = false;
        }
        assertEquals(false,result);

        logger.info("user add test success");

        //add group user
        //UserInfo group = new UserInfo();
        //group.setUsername("hwjtest");
        //group.setRealname("hwjtest");
        //group.setPassword("123456");
        //group.setGroupid("100");
        //group.setRoleid(2);
        //group.setId(12345);
        //int output2 = userInfoService.add(group);
        //if (output2 <= 0) {
        //    result = false;
        //}
        //assertEquals(true,result);
        //logger.info("user add test success");
        //delete
        //userInfoService.delete("12345");
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
    public void add2Test(){
        logger.info("add test");
        UserInfo userInfo3 = new UserInfo();
        userInfo3.setId(7878);
        userInfo3.setUsername("additionalTest");
        userInfo3.setPassword("12345");
        userInfo3.setRealname("tttest");
        userInfo3.setRoleid(2);
        int add = userInfoService.add(userInfo3);
        userInfoService.delete("7878");
        assertEquals(1, add);
        logger.info("add test success");
    }

    @Test
    public void testGetUsersByWhere() {
        logger.info("get users by where test");
        PageModel<UserInfo> userInfoPageModel = new PageModel<UserInfo>(1,userInfo);
        Result usersByWhere = userInfoService.getUsersByWhere(userInfoPageModel);
        System.out.println(usersByWhere.getMsg());
        boolean res = false;
        if(usersByWhere.getMsg() == "Data fetched successfully"){
            res = true;
        }
        UserInfo userInfo3 = new UserInfo();
        userInfo3.setRoleid(6666);
        userInfo3.setUsername("dsaf");
        PageModel<UserInfo> userInfoPageModel2 = new PageModel<UserInfo>(1,userInfo3);
        usersByWhere = userInfoService.getUsersByWhere(userInfoPageModel2);
        System.out.println(usersByWhere.getMsg());


        assertEquals(true, res);

        logger.info("get users by where test success");

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
    public void testGroup() {
        Group group = new Group();
        group.setManagerid(100);
        group.setId(123);
        group.setGroupname("abcd");
        int id = group.getId();
        String name = group.getGroupname();
        int id2 = group.getManagerid();
    }

    @Test
    public void testSetNullUserName() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("");
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

    @Test

    public void getUserInfoByIdTest() {
        logger.info("getUserInfoById test");
        UserInfo userInfoById = userInfoService.getUserInfoById("1");
        assertEquals("hwj",userInfoById.getUsername());

        logger.info("getUserInfoById test success");
    }

    @Test
    public void testGetUserById() {
        UserInfo userInfo = userInfoService.getUserInfoById("1");
        assertEquals("hwj", userInfo.getRealname());
        assertEquals("hwj", userInfo.getUsername());
    }

    @Test
    public void testGetUserByNoWhere1() {
        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUsername("hwj");
        userInfo3.setPassword("hwj");
        userInfo3.setId(1);
        userInfo3.setRoleid(1);
        userInfo3.setRealname("hwj");
        PageModel model = new PageModel<>(1, userInfo3);
        model.setPageSize(10);
        Result res = userInfoService.getUsersByWhere(model);
        assertEquals(res.getMsg(), "Data fetched successfully");
        userInfo3.setUsername("notexistedname");
        userInfo3.setRealname("notexistedname");
        PageModel model1 = new PageModel<>(1, userInfo3);
        Result res1 = userInfoService.getUsersByWhere(model1);
        assertEquals(res1.getMsg(), "No related data");
    }

    @Test
    public void exceptionTest(){
        try{
            userInfoService.getUsersByWhere(null);
        }catch (Exception e){
            assertNotNull(e);
        }
    }
}
