package com.proxypool.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.proxypool.service.PictureInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * @program: muse-pay
 * @description: Picture页面控制类
 * @author: Vincent
 * @create: 2019-01-24 10:13
 **/
@RequestMapping("/pic")
@Controller
public class PictureController {

    @Autowired
    private PictureInfoService pictureInfoService;

    @RequestMapping("/index")
    public String index(ModelMap modelMap, @RequestParam(required = false, defaultValue = "1") int p,
                        @RequestParam(required = false, defaultValue = "48") int size) {
        try {
            PageInfo<Collection> pageInfo = pictureInfoService.query(p, size);
            // 检查是否为空
            Preconditions.checkNotNull(pageInfo);
            modelMap.put("static", pageInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }


}
