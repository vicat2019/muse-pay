package com.proxypool.controller;

import com.muse.common.entity.ResultData;
import com.proxypool.service.MeBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: muse-pay
 * @description: 电子书Controller
 * @author: Vincent
 * @create: 2019-01-29 10:02
 **/
@RequestMapping("/mebook")
@Controller
public class MeBookController {
    private Logger log = LoggerFactory.getLogger("MeBookController");

    @Autowired
    private MeBookService meBookService;

    @RequestMapping("/index")
    public String index() {
        return "book/index";
    }


    @RequestMapping("/list")
    @ResponseBody
    public ResultData queryBook(@RequestParam(required = false, defaultValue = "1") int p,
                                @RequestParam(required = false, defaultValue = "30") int s,
                                @RequestParam(required = false, defaultValue = "") String title,
                                @RequestParam(required = false, defaultValue = "") String author,
                                @RequestParam(required = false, defaultValue = "") String desc,
                                @RequestParam(required = false, defaultValue = "") String category,
                                ModelMap modelMap) {
        ResultData result;

        try {
            result = meBookService.queryBook(title, author, category, desc, p, s);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询电子书分页异常=" + e.getMessage());
            result = ResultData.getErrResult("分页查询异常");
        }
        return result;
    }


}
