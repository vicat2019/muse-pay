package com.muse.pay.controller;

import com.github.pagehelper.PageInfo;
import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.service.MerchantInfoService;
import com.muse.pay.service.PayAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城后台管理入口
 */
@Controller
@RequestMapping("/admin")
public class PayAdminController extends BaseController {

    @Autowired
    private PayAdminService payAdminService;

    @Autowired
    private MerchantInfoService merchantInfoService;


    @RequestMapping("/index")
    public String index() {
        return "admin/index";
    }


    @RequestMapping("/order")
    public String orderIndex(String name, String startTime, String endTime,
                             @RequestParam(required = false, defaultValue = "-1") int status,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "20") int pageSize,
                             ModelMap modelMap) {

        try {
            ResultData resultData = payAdminService.queryOrder(name, startTime, endTime, status, pageNum, pageSize);
            if (!resultData.isOk()) {
                modelMap.addAttribute("msg", resultData.getMessage());
            } else {
                PageInfo<OrderInfo> pageInfo = (PageInfo<OrderInfo>) resultData.getData();
                modelMap.addAttribute("data", pageInfo);
            }

            modelMap.addAttribute("name", name);
            modelMap.addAttribute("startTime", startTime);
            modelMap.addAttribute("endTime", endTime);
            modelMap.addAttribute("status", status);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "admin/index";
    }


    @RequestMapping("/ajax/order")
    @ResponseBody
    public ResultData orderList(String name, String startTime, String endTime,
                             @RequestParam(required = false, defaultValue = "-1") int status,
                             @RequestParam(required = false, defaultValue = "1") int pageNum,
                             @RequestParam(required = false, defaultValue = "20") int pageSize) {

        try {
            ResultData result = payAdminService.queryOrder(name, startTime, endTime, status, pageNum, pageSize);
            if (result.isOk() && result.resultIsNotEmpty()) {
                PageInfo pageInfo = (PageInfo) result.getData();
                List<OrderInfo> dataList = pageInfo.getList();


                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("Rows", dataList);
                resultMap.put("Total", pageInfo.getTotal());

                return ResultData.getSuccessResult(resultMap);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getErrResult("查询异常");
    }

    @RequestMapping("/merchant")
    public String merchant(String merchantName, String payKey, String paySecret,
                           @RequestParam(required = false, defaultValue = "") String status,
                           @RequestParam(required = false, defaultValue = "1") int pageNum,
                           @RequestParam(required = false, defaultValue = "20") int pageSize,
                           ModelMap modelMap) {

        try {
            ResultData resultData = merchantInfoService.queryMerchant(merchantName, status, payKey, paySecret, pageNum, pageSize);
            if (!resultData.isOk()) {
                modelMap.addAttribute("msg", resultData.getMessage());
            } else {
                PageInfo<OrderInfo> pageInfo = (PageInfo<OrderInfo>) resultData.getData();
                modelMap.addAttribute("data", pageInfo);
            }

            modelMap.addAttribute("merchantName", merchantName);
            modelMap.addAttribute("payKey", payKey);
            modelMap.addAttribute("paySecret", paySecret);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "admin/merchant";
    }

    @RequestMapping("/merchantStatus")
    @ResponseBody
    public ResultData setStatus(Integer id, String status) {

        ResultData result;
        try {
            result = merchantInfoService.setStatus(id, status);

        } catch (Exception e) {
            e.printStackTrace();
            result = ResultData.getErrResult("修改状态异常");
        }

        return result;
    }


}
