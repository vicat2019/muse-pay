package com.seckill.controller;

import com.muse.common.entity.ResultData;
import com.seckill.service.ProductInfoService;
import com.seckill.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 测试Controller
 * @author: Vincent
 * @create: 2018-12-24 15:34
 **/
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping("/sk1")
    public ResultData secKill1(String code, int userId) {
        try {
            return productInfoService.startSecKill(code, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }

    @RequestMapping("/skByExecutor")
    public ResultData secKill1(String code) {
        try {
            return productInfoService.startByExecutor(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }

    @RequestMapping("/genUser")
    public ResultData genUser(int size) {
        try {
            userInfoService.genRandomUser(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultData.getSuccessResult();
    }


}
