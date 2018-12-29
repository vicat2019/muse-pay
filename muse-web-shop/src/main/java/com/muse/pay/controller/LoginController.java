package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/29 14 53
 **/
@Controller
public class LoginController extends BaseController {
    private Logger log = LoggerFactory.getLogger("LoginController");

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String toLogin() {
        return "user/login";
    }

    @RequestMapping("/doLogin")
    public String login(String email, String password, HttpSession session, ModelMap modelMap) {
        try {

            ResultData resultDate = userService.login(email, password, session);
            if (!resultDate.isOk()) {
                modelMap.addAttribute("msg", resultDate.getMessage());
                return "user/login";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户登录异常=" + e.getMessage());

            modelMap.addAttribute("msg", "用户登录异常");
            return "user/login";
        }

        return "redirect:/book/recommend";
    }

    /**
     * 退出
     *
     * @return
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {

        // subject的实现类DelegatingSubject的logout方法，将本subject对象的session清空了
        // 即使session托管给了redis ，redis有很多个浏览器的session
        // 只要调用退出方法，此subject的、此浏览器的session就没了
        try {
            //退出
            SecurityUtils.getSubject().logout();

        } catch (Exception e) {
            log.error("退出登录异常=" + e.getMessage());
        }
        return "user/to_login";
    }

    @RequestMapping(value = "403")
    public String unAuth() {
        return "403";
    }


}
