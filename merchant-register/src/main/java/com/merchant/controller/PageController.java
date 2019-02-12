package com.merchant.controller;

import com.merchant.entity.ResultData;
import com.merchant.entity.RuiShengUserInfo;
import com.merchant.service.MerchantServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @program: muse-pay
 * @description:
 * @author: Vincent
 * @create: 2019-02-12 12:51
 **/
@RequestMapping("/merchant/p/")
@Controller
public class PageController {
    private Logger log = LoggerFactory.getLogger("PageController");

    @Autowired
    private MerchantServiceImpl merchantService;


    /**
     * 进入商户进件界面
     *
     * @return String
     */
    @RequestMapping("/t")
    public String index() {
        return "user/register";
    }

    /**
     * 商户进件操作
     *
     * @param userInfo 商户信息
     * @param modelMap 模型
     * @return String
     */
    @RequestMapping("/r")
    public String register(RuiShengUserInfo userInfo, ModelMap modelMap) {
        try {
            ResultData result = merchantService.insert(userInfo);
            if (!result.isOk()) {
                modelMap.put("user", userInfo);
                modelMap.put("msg", result.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("商户进件异常=" + e.getMessage());
        }
        return "user/register";
    }

}
