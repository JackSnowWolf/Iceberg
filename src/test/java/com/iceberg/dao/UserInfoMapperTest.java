package com.iceberg.dao;

import com.iceberg.entity.Role;
import com.iceberg.entity.UserInfo;
import com.iceberg.utils.PageModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserInfoMapperTest {

    private Logger logger = LoggerFactory.getLogger(UserInfoMapper.class);

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * Get user's information by their partial information
     */
    @Test
    public void getUserInfoTest() {
        logger.info("getUserInfoTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
//        userInfo.setUsername("zsh");
        UserInfo userInfo1 = userInfoMapper.getUserInfo(userInfo);
        int id = userInfo1.getId();
        assertEquals(1, id);

        logger.info("getUserInfoTest success");
    }

    /**
     * Given user information, add a user.
     */
    @Test
    public void addUserTest() {
        logger.info("addUserTest");
        UserInfo userInfo = new UserInfo(8888, "czy", "1234@qq.com");
        userInfoMapper.delete("8888");
        int add = userInfoMapper.add(userInfo);
        assertEquals(1, add);
        logger.info("addUserTest success");
    }

    /**
     * Given user information, find if the user exists.
     */
    @Test
    public void userIsExistedTest() {
        logger.info("userIsExistedTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("hwj");
        int num = userInfoMapper.userIsExisted(userInfo);
        assertEquals(1, num);
        logger.info("userIsExistedTest success");
    }

    @Test
    public void getUsersByWhereTest() {
        logger.info("getUsersByWhereTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setRoleid(1);
        userInfo.setUsername("hwj");
        PageModel<UserInfo> userInfoPageModel = new PageModel<UserInfo>(2,userInfo);
        List<UserInfo> usersByWhere = userInfoMapper.getUsersByWhere(userInfoPageModel);
        assertEquals(0, usersByWhere.size());
        logger.info("getUsersByWhereTest success");
    }

    @Test
    public void getToatlByWhereTest() {
        logger.info("getToatlByWhereTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setRoleid(1);
        userInfo.setUsername("hwj");
        PageModel<UserInfo> userInfoPageModel = new PageModel<>(2, userInfo);
        int toatlByWhere = userInfoMapper.getToatlByWhere(userInfoPageModel);
        assertEquals(1, toatlByWhere);
        logger.info("getToatlByWhereTest success");
    }

    /**
     * Find a user by id and update the user's information.
     */
    @Test
    public void updateTest() {
        logger.info("updateTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(8888);
        userInfo.setUsername("testtest");
        userInfo.setRealname("testuser");
        int update = userInfoMapper.update(userInfo);
        assertEquals(1, update);
        logger.info("updateTest success");
    }

    /**
     * Delete a user by id.
     */
    @Test
    public void deleteTest() {
        logger.info("deleteTest");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(9999);
        userInfo.setRoleid(3);
        userInfo.setUsername("deltest");
        userInfo.setRealname("truedeltest");
        userInfoMapper.add(userInfo);
        int delete = userInfoMapper.delete("9999");
        assertEquals(1, delete);
        logger.info("deleteTest success");
    }

    /**
     * Find all existing roles.
     */
    @Test
    public void getAllRolesTest() {
        logger.info("getAllRolesTest");
        List<Role> allRoles = userInfoMapper.getAllRoles();
        for (Role r :
                allRoles) {
            System.out.println(r.getRolename());
        }
        String rolename = allRoles.get(0).getRolename();
        assertEquals("Administrator", rolename);
        logger.info("getAllRolesTest success");
    }

    /**
     * Add a role. Role id is auto generated.
     */
    @Test
    public void addRoleTest() {
        logger.info("addRoleTest");
        Role role = new Role();
        role.setRolename("testRole");
        int i = userInfoMapper.addRole(role);
        assertEquals(1, i);
        logger.info("addRoleTest success");
    }

    /**
     * Find role by id and update role name.
     */
    @Test
    public void updateRoleTest() {
        logger.info("updateRoleTest");
        Role role = new Role();
        role.setRoleid(6);
        role.setRolename("test");
        userInfoMapper.deleteRole("6");
        userInfoMapper.addRole(role);
        role.setRoleid(6);
        role.setRolename("testtestRole");
        int i = userInfoMapper.updateRole(role);
        assertEquals(1, i);
        logger.info("updateRoleTest success");
    }

//    /**
//     * Given group's information, add the group.
//     */
//    @Test
//    public void addGroupIdTest() {
//        logger.info("addGroupIdTest");
//        Group group = new Group();
//        group.setManagerid(12);
//        group.setGroupname("testGroup");
//        userInfoMapper.addGroupId(group);
//        logger.info("addGroupIdTest success");
//    }

    /**
     * Delete a role by id.
     */
    @Test
    public void deleteRoleTest() {
        logger.info("deleteRoleTest");
        int i = userInfoMapper.deleteRole("6");
        assertEquals(1, i);
        logger.info("deleteRoleTest success");
    }

    /**
     * Find role name by id.
     */
    @Test
    public void getRoleByIdTest() {
        logger.info("getRoleByIdTest");
        Role roleById = userInfoMapper.getRoleById("1");
        assertEquals("Administrator", roleById.getRolename());
        logger.info("getRoleByIdTest success");
    }




}
