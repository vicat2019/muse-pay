package com.nucc.controller;

import com.google.common.base.Joiner;
import com.muse.common.entity.ResultData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 测试控制类
 * @author: Vincent
 * @create: 2019-01-12 12:07
 **/
@RequestMapping("/test")
@RestController
public class TestController {

    @RequestMapping("/notify")
    public ResultData notify(HttpServletRequest request) {

        Map<String, String> params = new HashMap<>();

        Map<String, String[]> sourceParams = request.getParameterMap();
        if (sourceParams != null) {
            sourceParams.forEach((k, v) -> {
                params.put(k, Joiner.on(",").join(v));
            });
        }
        params.forEach((key, value) -> System.out.println("收到参数:" + key + "=" + value));

        return ResultData.getSuccessResult(params);
    }






}
