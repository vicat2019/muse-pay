package com.proxypool.controller;

import com.google.common.collect.Maps;
import com.proxypool.service.RecruitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @program: muse-pay
 * @description: JL控制类
 * @author: Vincent
 * @create: 2019-01-25 16:19
 **/
@RequestMapping("/recruit")
@Controller
public class RecruitController {

    @Autowired
    private RecruitInfoService recruitInfoService;


    @RequestMapping("/index")
    public String index() {
        return "recruit/index";
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryRecruite(@RequestParam(required = false, defaultValue = "1") int p,
                                    @RequestParam(required = false, defaultValue = "30") int s) {
        Map<String, Object> resultMap;
        try {
            resultMap = recruitInfoService.queryRecruit(p, s);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap = Maps.newHashMap();
        }
        return resultMap;
    }


}
