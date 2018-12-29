package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.OrderItemInfo;
import com.muse.pay.service.OrderItemInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
* Created by code generator  on 2018/07/26.
*/
@RestController
@RequestMapping("/order/item/info")
public class OrderItemInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger("OrderItemInfoController");

    @Resource
    private OrderItemInfoService orderItemInfoService;

    @RequestMapping("/add")
    public ResultData add(@Valid OrderItemInfo orderItemInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if(resultData != null) {
            return resultData;
        }

        try {
            resultData = orderItemInfoService.add(orderItemInfo);
            return resultData;
        } catch (Exception e) {
            logger.error("添加数据异常=" + e.getMessage());
            return ResultData.getErrResult("添加失败");
        }
    }

    @RequestMapping("/delete")
    public ResultData delete(@RequestParam Integer id) {
        try {
            return orderItemInfoService.del(id);
        } catch (Exception e) {
            logger.error("删除数据异常=" + e.getMessage());
            return ResultData.getErrResult("删除失败");
        }
    }

    @RequestMapping("/update")
    public ResultData update(@Valid OrderItemInfo orderItemInfo, Errors errors) {
        // 检查参数
        ResultData resultData = handleErrors(errors);
        if(resultData != null) {
            return resultData;
        }

        try {
            resultData = orderItemInfoService.update(orderItemInfo);
            return resultData;
        } catch (Exception e) {
            logger.error("更新数据异常=" + e.getMessage());
            return ResultData.getErrResult("更新失败");
        }
    }

    @RequestMapping("/detail")
    public ResultData detail(@RequestParam Integer id) {
        try {
            ResultData resultData = orderItemInfoService.get(id);
            return resultData;
        } catch (Exception e) {
            logger.error("查询数据异常=" + e.getMessage());
            return ResultData.getErrResult("查询失败");
        }
    }

}
