package com.proxypool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: muse-pay
 * @description: TEST 2
 * @author: Vincent
 * @create: 2018-12-24 10:40
 **/
@RequestMapping("/test2")
@Controller
public class Test2Controller {

    @RequestMapping("/t")
    public String test() {
        return "test";
    }
}
