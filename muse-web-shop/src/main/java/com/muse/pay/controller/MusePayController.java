package com.muse.pay.controller;

import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.pay.entity.common.UnifiedOrderResultData;
import com.muse.pay.service.MusePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 支付相关服务入口
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/8 16 17
 **/
@RequestMapping("/muse")
@RestController
public class MusePayController extends BaseController {

    @Autowired
    private MusePayService musePayService;

    /**
     * 预下单
     *
     * @param content 预下单的参数
     * @return UnifiedOrderResultData
     */
    @RequestMapping("/initPay")
    public UnifiedOrderResultData unifiedOrder(@RequestBody String content) {

        try {
            return musePayService.unifiedOrder(content);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("统一下单异常=" + e.getMessage());
        }

        return UnifiedOrderResultData.getErrorResult("统一下单异常");
    }

    /**
     * 支付
     *
     * @param orderNo 订单编码
     * @return ResultData
     */
    @RequestMapping("/pay/{code}")
    public ResultData doPay(@PathVariable("code") String orderNo) {

        try {
            ResultData<String> payResult = musePayService.pay(orderNo);
            if (!payResult.isOk()) {
                return payResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付异常=" + e.getMessage());
        }

        return ResultData.getSuccessResult("支付成功");
    }

    /**
     * 查询订单
     *
     * @param content
     * @return
     */
    @RequestMapping("/queryOrder")
    @ResponseBody
    public String queryOrder(@RequestBody String content) {

        try {
            ResultData orderResult = musePayService.queryOrder(content);
            if (!orderResult.isOk() || orderResult.getData() == null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询订单异常=" + e.getMessage());
        }

        return "";
    }


}
