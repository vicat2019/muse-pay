package com.muse.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.muse.common.controller.BaseController;
import com.muse.common.entity.ResultData;
import com.muse.common.util.HttpUtils;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.service.OrderInfoService;
import com.muse.pay.service.OrderPayService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/13 16 35
 **/
@RequestMapping("/orderPay")
@Controller
public class OrderPayController extends BaseController {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private OrderPayService orderPayService;

    @Autowired
    private HttpUtils httpUtils;


    /**
     * 订单结算
     *
     * @param orderNo  订单号
     * @param modelMap 模型
     * @return String
     */
    @RequestMapping("/settlement")
    @RequiresAuthentication
    public String settlement(String orderNo, ModelMap modelMap, HttpServletRequest request) {

        try {
            // 查询订单
            ResultData resultData = orderInfoService.getOrderByNo(orderNo);
            if (!resultData.isOk() || resultData.getData() == null) {
                return "order/index";
            }
            OrderInfo order = (OrderInfo) resultData.getData();

            // 设置订单到模型中
            modelMap.addAttribute("data", order);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("结算，查询订单异常=" + e.getMessage());
        }

        return "order/pay";
    }

    /**
     * 支付订单
     *
     * @param userId   用户ID
     * @param orderNo  订单号
     * @param modelMap 模型
     * @return String
     */
    @RequestMapping("/pay")
    @RequiresAuthentication
    public String pay(int userId, String orderNo, ModelMap modelMap) {

        try {
            ResultData resultData = orderPayService.doPay(userId, orderNo);
            if (!resultData.whetherOk() || resultData.resultIsEmpty()) {
                modelMap.addAttribute("msg", resultData.getMessage());
                return "order/middlePay";
            }

            Map<String, String> resultMap = (Map<String, String>) resultData.getData();
            modelMap.addAttribute("qrcode", resultMap.get("payMessage"));
            modelMap.addAttribute("productName", resultMap.get("productName"));
            modelMap.addAttribute("amount", resultMap.get("amount"));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付异常=" + e.getMessage());
            modelMap.addAttribute("msg", "支付异常");
        }

        return "order/middlePay";
    }

    /**
     * 支付结果通知
     *
     * @param request 请求对象
     * @return String
     */
    @RequestMapping("/notify")
    @ResponseBody
    public String orderResult(HttpServletRequest request, HttpSession session) {

        String orderTime = request.getParameter("orderTime");
        String outTradeNo = request.getParameter("outTradeNo");
        String payKey = request.getParameter("payKey");
        String productName = request.getParameter("productName");
        String productType = request.getParameter("productType");
        String remark = request.getParameter("remark");
        String successTime = request.getParameter("successTime");
        String tradeStatus = request.getParameter("tradeStatus");
        String trxNo = request.getParameter("trxNo");
        String sign = request.getParameter("sign");

        log.info("支付后收到的回调, payKey=" + payKey + ", productName=" + productName
                + ", outTradeNo=" + outTradeNo
                + ", productType=" + productType
                + ", tradeStatus=" + tradeStatus
                + ", successTime=" + successTime
                + ", orderTime=" + orderTime
                + ", trxNo=" + trxNo
                + ", sign=" + sign
                + ", remark=" + remark
        );

        Map<String, String> receiveParams = new HashMap<>();
        receiveParams.put("orderTime", orderTime);
        receiveParams.put("outTradeNo", outTradeNo);
        receiveParams.put("payKey", payKey);
        receiveParams.put("productName", productName);
        receiveParams.put("productType", productType);
        receiveParams.put("remark", remark);
        receiveParams.put("successTime", successTime);
        receiveParams.put("tradeStatus", tradeStatus);
        receiveParams.put("trxNo", trxNo);
        receiveParams.put("sign", sign);

        try {
            ResultData notifyResult = orderPayService.payNotify(receiveParams, session);
            if (!notifyResult.isOk()) {
                return "ERROR";
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付回调异常=" + e.getMessage());
        }

        return "SUCCESS";
    }

    @RequestMapping("/test")
    public String test() {
        return "order/finishPay";
    }

    @RequestMapping("/directPay")
    public String directPay(String url, ModelMap modelMap) {
        if (StringUtils.isEmpty(url)) {
            modelMap.addAttribute("msg", "参数异常");
            return "order/middlePay";
        }

        try {
            String decodeUrl = URLDecoder.decode(url, "utf-8");
            log.info("directPay收到的支付链接地址=" + url + ", 解密后地址=" + decodeUrl);

            ResultData httpResult = httpUtils.post(decodeUrl, new HashMap<>());
            if (!httpResult.isOk() || httpResult.resultIsEmpty()) {
                modelMap.addAttribute("msg", "请求异常");
                return "order/middlePay";
            }

            String content = (String) httpResult.getData();
            JSONObject jsonResult = JSONObject.parseObject(content);
            log.info("支付收到的返回内容=" + content);

            modelMap.addAttribute("result", jsonResult.get("message"));

        } catch (Exception e) {
            e.printStackTrace();
            log.error("直接支付异常=" + e.getMessage());
        }

        return "order/finishPay";
    }









}
