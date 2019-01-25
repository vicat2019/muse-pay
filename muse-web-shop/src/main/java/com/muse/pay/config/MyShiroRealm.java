package com.muse.pay.config;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/29 11 14
 **/
public class MyShiroRealm extends AuthorizingRealm {
    private Logger log = LoggerFactory.getLogger("MyShiroRealm");

    @Autowired
    private UserService userService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.info("权限验证：" + principalCollection.toString());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /*
        //赋予角色
        for (Role userRole : user.getRoles()) {
            info.addRole(userRole.getName());
        }
        //赋予权限
        for (Permission permission : permissionService.getByUserId(user.getId())) {
            //  if(StringUtils.isNotBlank(permission.getPermCode()))
            info.addStringPermission(permission.getName());
        }*/

        // 设置登录次数、时间
        // userService.updateUserLogin(user);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("登录验证：" + authenticationToken.toString());

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getUsername();
        UserInfo user = null;

        try {
            ResultData resultData = userService.getUserBy(new UserInfo(userName));
            if (resultData.isOk()) {
                user = (UserInfo) resultData.getData();
            } else {
                log.error("根据名称查询用户信息异常=" + resultData.getMessage());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户信息异常=" + e.getMessage());
            throw new AuthenticationException("账号不存在");
        }

        if (user != null) {
            //设置用户session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute("user", user);
            return new SimpleAuthenticationInfo(userName, user.getPassword(), getName());

        } else {
            return null;
        }
    }
}