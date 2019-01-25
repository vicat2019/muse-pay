package com.muse.pay.controller;

import com.muse.common.entity.ResultData;
import com.muse.pay.entity.UserInfo;
import com.muse.pay.service.ConcurrencyService;
import com.muse.pay.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 09 53
 **/
@RequestMapping("/concurrency")
@RestController
public class ConcurrencyController {

    private static Logger log = LoggerFactory.getLogger("ConcurrencyController");

    @Autowired
    private UserService userService;

    @Autowired
    private ConcurrencyService concurrencyService;


    @RequestMapping("/user_test")
    public String user(String userName, String password) {
        log.info("收到参数, userName=" + userName + ", password=" + password);
        return "success";
    }

    @RequestMapping("/user/add")
    public ResultData addUser(UserInfo user) {
        ResultData resultData = null;
        try {
            resultData = userService.addUser(user);
        } catch (Exception e) {
            log.error("查询用户信息异常=" + e.getMessage());
        }

        return resultData;
    }

    @RequestMapping("/user")
    public ResultData listUser(@RequestParam("id") int id) {
        ResultData resultData = null;
        try {
            resultData = userService.getUser(id);
        } catch (Exception e) {
            log.error("查询用户信息异常=" + e.getMessage());
        }

        return resultData;
    }

    @RequestMapping("/user/mod")
    public ResultData modUser(UserInfo userInfo, HttpSession session) {
        ResultData resultData = null;

        try {
            resultData = userService.updateUser(userInfo, session);
        } catch (Exception e) {
            log.error("修改用户信息异常=" + e.getMessage());
        }

        return resultData;
    }


    @RequestMapping("/start")
    public ResultData concurrency(String userId, String bookId) {
        ResultData resultData = null;

        try {
            resultData = concurrencyService.concurrency(userId, bookId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("并发测试异常=" + e.getMessage());
        }

        return resultData;
    }


}
