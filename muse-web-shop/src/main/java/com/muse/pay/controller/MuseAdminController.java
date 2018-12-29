package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.common.util.TextUtils;
import com.muse.pay.entity.MuseMerchantInfo;
import com.muse.pay.service.MuseMerchantInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 下游商户管理类
 */
@RequestMapping("/muse/admin")
@Controller
public class MuseAdminController extends BaseController {
    private Logger log = LoggerFactory.getLogger("MuseAdminController");

    @Autowired
    private MuseMerchantInfoService museMerchantInfoService;


    /**
     * 商户列表
     *
     * @param payMerchantInfo 查询参数
     * @param pageNum         页码
     * @param modelMap        模型
     * @return String
     */
    @RequestMapping("/list")
    public String toList(@RequestParam(required = false) MuseMerchantInfo payMerchantInfo,
                         @RequestParam(required = false, defaultValue = "1") int pageNum,
                         ModelMap modelMap) {

        try {
            ResultData result = museMerchantInfoService.list(payMerchantInfo, pageNum);
            if (result.isOk()) {
                modelMap.addAttribute("data", result.getData());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询商户信息异常=" + e.getMessage());
        }

        return "muse/index";
    }

    /**
     * 去添加商户界面
     *
     * @param modelMap 模型
     * @return String
     */
    @RequestMapping("/addUI")
    public String toAdd(ModelMap modelMap) {
        modelMap.addAttribute("secret", TextUtils.getRandomStr(18).toUpperCase());
        modelMap.addAttribute("userNo", TextUtils.getNumRandomStr(15));

        return "muse/add";
    }

    /**
     * 添加商户
     *
     * @param payMerchantInfo 商户信息
     * @param modelMap        模型
     * @return String
     */
    @RequestMapping("/add")
    public String add(MuseMerchantInfo payMerchantInfo, ModelMap modelMap) {

        if (payMerchantInfo == null || !payMerchantInfo.verify()) {
            modelMap.addAttribute("msg", "参数不能为空");
            modelMap.addAttribute("secret", TextUtils.getRandomStr(18).toUpperCase());
            modelMap.addAttribute("userNo", TextUtils.getNumRandomStr(15));
            return "muse/add";
        }

        try {
            ResultData addResult = museMerchantInfoService.add(payMerchantInfo);
            if (!addResult.isOk()) {
                modelMap.addAttribute("msg", addResult.getMessage());
                modelMap.addAttribute("secret", TextUtils.getRandomStr(18).toUpperCase());
                modelMap.addAttribute("userNo", TextUtils.getNumRandomStr(15));
                return "muse/add";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("商户进件异常=" + e.getMessage());
        }

        return "redirect:/muse/admin/list";
    }

    /**
     * 删除商户
     *
     * @param id 商户ID
     * @return String
     */
    @RequestMapping("/del")
    @ResponseBody
    public ResultData del(int id) {
        if (id == 0) {
            return ResultData.getErrResult("参数不能为空");
        }

        try {
            museMerchantInfoService.del(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除商户异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult("删除成功");
    }


}
