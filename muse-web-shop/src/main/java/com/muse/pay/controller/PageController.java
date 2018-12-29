package com.muse.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/20 17 40
 **/
@RequestMapping("/page")
@Controller
public class PageController {


    @RequestMapping("/mq")
    public String toSend() {
        return "page/send_mq";
    }













}
