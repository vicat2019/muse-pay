package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.service.CodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/25 09 33
 **/
@RequestMapping("code")
@RestController
public class CodeInfoController extends BaseController {

    @Autowired
    private CodeInfoService codeService;

    @RequestMapping("/send")
    public ResultData makeAndSendCode(String email, String codeType) {
        try {
            return codeService.makeVerifyCode(email, codeType);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("请求验证码异常=" + e.getMessage());
            return ResultData.getErrResult("请求验证异常");
        }
    }






















}




