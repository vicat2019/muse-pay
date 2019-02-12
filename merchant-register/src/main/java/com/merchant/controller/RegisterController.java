package com.merchant.controller;

import com.merchant.service.MerchantServiceImpl;
import com.muse.common.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: 进件控制类
 * @author: Vincent
 * @create: 2019-02-12 11:22
 **/
@RequestMapping("/merchant")
@RestController
public class RegisterController {

    @Autowired
    private MerchantServiceImpl merchantService;


    @RequestMapping("/r")
    public ResultData register() {
        try {
            return merchantService.add(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("添加异常=" + e.getMessage());
        }
    }


    @RequestMapping("/modRate")
    public ResultData modifyRate(int rate) {
        try {
            return merchantService.modifyRate(rate, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.getErrResult("修改费率异常，" + e.getMessage());
        }
    }


}
