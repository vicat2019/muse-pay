package com.muse.pay.controller;

import com.github.pagehelper.PageInfo;
import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.entity.vo.OrderInfoVO;
import com.muse.pay.service.OrderInfoService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by code generator  on 2018/07/26.
 */
@Controller
@RequestMapping("/order")
public class OrderInfoController extends BaseController {
    private Logger log = LoggerFactory.getLogger("OrderInfoController");

    @Resource
    private OrderInfoService orderInfoService;

    @RequestMapping("/add")
    @ResponseBody
    public ResultData add(@RequestBody OrderInfoVO orderVO) {
        try {
            return orderInfoService.add(orderVO);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加数据异常=" + e.getMessage());
        }
        return ResultData.getErrResult("添加失败");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultData delete(@RequestParam Integer id) {
        try {
            return orderInfoService.del(id);
        } catch (Exception e) {
            log.error("删除数据异常=" + e.getMessage());
            return ResultData.getErrResult("删除失败");
        }
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResultData update(@Valid OrderInfo orderInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if (resultData != null) {
            return resultData;
        }

        try {
            return orderInfoService.update(orderInfo);
        } catch (Exception e) {
            log.error("更新数据异常=" + e.getMessage());
            return ResultData.getErrResult("更新失败");
        }
    }

    @RequestMapping("/detail")
    public ResultData detail(@RequestParam Integer id) {
        try {
            return orderInfoService.get(id);
        } catch (Exception e) {
            log.error("查询数据异常=" + e.getMessage());
            return ResultData.getErrResult("查询失败");
        }
    }


    @RequestMapping("/list/{userId}")
    @RequiresAuthentication
    public String listOrder(@PathVariable("userId") int userId,
                            @RequestParam(required = false, defaultValue = "") String startTime,
                            @RequestParam(required = false, defaultValue = "") String endTime,
                            @RequestParam(required = false, defaultValue = "-1") int status,
                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                            ModelMap modelMap) {
        ResultData resultData = null;
        try {
            resultData = orderInfoService.getUserOrders(userId, startTime, endTime, status, pageNum, pageSize);
            if (resultData.isOk()) {
                PageInfo<OrderInfo> pageInfo = (PageInfo<OrderInfo>) resultData.getData();
                modelMap.addAttribute("data", pageInfo);

                ResultData countResult = orderInfoService.getUserOrderCount(userId);
                if (countResult.isOk() && !countResult.resultIsEmpty()) {
                    modelMap.addAttribute("orderSize", countResult.getData());
                } else {
                    modelMap.addAttribute("orderSize", 0);
                }
                modelMap.addAttribute("startTime", startTime);
                modelMap.addAttribute("endTime", endTime);
                modelMap.addAttribute("status", status);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户订单异常=" + e.getMessage());
        }

        return "order/index";
    }

    @RequestMapping("/count/{userId}")
    @ResponseBody
    public ResultData getOrderCount(@PathVariable("userId") int userId) {
        try {
            return orderInfoService.getUserOrderCount(userId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户订单数异常=" + e.getMessage());
        }

        return ResultData.getErrResult("查询订单数异常");
    }
















}
