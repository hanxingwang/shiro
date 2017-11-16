package com.github.zhangkaitao.shiro.chapter3;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by SD on 2017/11/16.
 */
public class RoleTest {
    private void login(String configFile, String username, String password) {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);

        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        subject.login(token);
    }

    @Test
    public void testHasRole() {
        login("classpath:shiro-role.ini", "zhang", "123");

        Subject subject = SecurityUtils.getSubject();

        Assert.assertTrue(subject.hasRole("role1"));
        Assert.assertTrue(subject.hasAllRoles(Arrays.asList("role1", "role2")));

        boolean[] result = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));

        Assert.assertEquals(result[0], true);
        Assert.assertEquals(result[1], true);
        Assert.assertEquals(result[2], false);
    }

    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission() {
        login("classpath:shiro-permission.ini", "zhang", "123");

        Subject subject = SecurityUtils.getSubject();

        subject.checkPermission("user:create");
        subject.checkPermissions("user:delete","user:update");
        subject.checkPermissions("user:view");

    }
}
