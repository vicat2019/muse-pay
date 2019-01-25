package com.muse.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.muse.common.entity.ResultData;
import com.muse.common.util.HttpUtils;
import com.muse.common.util.RedisUtil;
import com.muse.pay.entity.MerchantInfo;
import com.muse.pay.entity.MuseOrderInfo;
import com.muse.pay.entity.OrderInfo;
import com.muse.pay.entity.OrderItemInfo;
import com.muse.pay.service.MerchantInfoService;
import com.muse.pay.service.OrderInfoService;
import com.muse.pay.service.OrderPayService;
import com.muse.pay.service.UserService;
import com.muse.pay.util.MerchantApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单支付服务类
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/13 18 10
 **/
@Service("orderPayService")
public class OrderPayServiceImpl implements OrderPayService {
    private Logger log = LoggerFactory.getLogger("OrderPayServiceImpl");

    @Value("${payKey}")
    private String payKey;

    @Value("${paySecret}")
    private String paySecret;

    @Value("${productType}")
    private String productType;

    @Value("${payUrl}")
    private String payUrl;

    @Value("${notifyUrl}")
    private String notifyUrl;

    @Value("${localIp}")
    private String ip;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private UserService userService;


    /**
     * 调用支付接口，支付
     *
     * @param userId  用户ID
     * @param orderNo 订单编号
     * @return ResultData
     * @throws Exception
     */
    @Override
    public ResultData doPay(int userId, String orderNo) throws Exception {

        // 检查参数
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("订单编号不能为空");
        }
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        // 先从缓存中获取订单
        OrderInfo orderInfo = (OrderInfo) redisUtil.get("ORDER_" + orderNo);
        if (orderInfo == null) {
            // 获取订单信息
            ResultData queryOrderResult = orderInfoService.getOrderByNo(orderNo);
            if (!queryOrderResult.isOk() || queryOrderResult.resultIsEmpty()) {
                return queryOrderResult;
            }
            orderInfo = (OrderInfo) queryOrderResult.getData();
        }
        if (orderInfo == null) {
            return ResultData.getErrResult("订单不存在=" + orderNo);
        }

        // 生成产品名称
        StringBuilder productNameSb = new StringBuilder();
        String productName = "";
        for (OrderItemInfo item : orderInfo.getItemList()) {
            productNameSb.append(item.getTitle());
            productNameSb.append("/");
        }
        if (productNameSb.length() > 50) {
            productName = productNameSb.toString().substring(0, 50);
        } else if (productNameSb.length() > 0) {
            productName = productNameSb.toString().substring(0, productNameSb.length() - 1);
        }
        // 空格用-代替
        productName = productName.replaceAll("\\s+", "-");

        // 生成支付参数
        long start = System.currentTimeMillis();
        Map<String, Object> payParams = parsePayParams(orderInfo.getOrderNo(), productName, String.valueOf(orderInfo.getAmount()),
                ip, "", notifyUrl, "购买图书");

        // 发送请求，解析返回结果
        ResultData httpResult = httpUtils.post(payUrl, payParams);
        log.info("向上游发送预下单请求，地址=" + payUrl + "，参数=" + payParams.toString() + "，结果=" + httpResult.getData()
                + "，STG耗时=" + ((System.currentTimeMillis() - start) / 1000d));

        if (!httpResult.isOk()) {
            return httpResult;
        }
        JSONObject jsonObject = JSON.parseObject((String) httpResult.getData());
        Object payMessage = jsonObject.get("payMessage");

        // 返回结果
        Map<String, String> resultMap = new HashMap<>();
        if (!"0000".equals(jsonObject.get("resultCode"))) {
            resultMap.put("msg", (String) jsonObject.get("errMsg"));
            // 更新订单状态，异常
            orderInfo.setStatus("4");
            orderInfo.setRemark((String) jsonObject.get("errMsg"));

            orderInfoService.updateOrderByNo(orderInfo);
        }
        resultMap.put("productName", productName);
        resultMap.put("payMessage", payMessage.toString());
        resultMap.put("amount", String.valueOf(orderInfo.getAmount()));
        return ResultData.getSuccessResult(resultMap);
    }


    /**
     * 调用支付接口，支付
     *
     * @param userId  用户ID
     * @param orderNo 订单编号
     * @return ResultData
     * @throws Exception
     */
    @Override
    public ResultData doRechargePay(int userId, String orderNo) throws Exception {

        // 检查参数
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("订单编号不能为空");
        }
        if (userId == 0) {
            return ResultData.getErrResult("用户ID不能为空");
        }

        // 先从缓存中获取订单
        OrderInfo orderInfo = (OrderInfo) redisUtil.get("ORDER_" + orderNo);
        if (orderInfo == null) {
            // 获取订单信息
            ResultData queryOrderResult = orderInfoService.getOrderByNo(orderNo);
            if (!queryOrderResult.isOk() || queryOrderResult.getData() == null) {
                return queryOrderResult;
            }
            orderInfo = (OrderInfo) queryOrderResult.getData();
        }
        if (orderInfo == null) {
            return ResultData.getErrResult("订单不存在=" + orderNo);
        }

        // 生成产品名称
        String productName = "测试TEST充值";

        // 生成支付参数
        long start = System.currentTimeMillis();
        Map<String, Object> payParams = parseRechargePayParams(orderInfo.getOrderNo(), productName, String.valueOf(orderInfo.getAmount()),
                ip, "充值");

        // 发送请求，解析返回结果
        ResultData httpResult = httpUtils.post("http://192.168.0.90:8080/gateway/cnpPay/initPay", payParams);
        log.info("向上游发送预下单请求，地址=" + payUrl + "，参数=" + payParams.toString() + "，结果=" + httpResult.getData()
                + "，STG耗时=" + ((System.currentTimeMillis() - start) / 1000d));

        if (!httpResult.isOk()) {
            return httpResult;
        }
        JSONObject jsonObject = JSON.parseObject((String) httpResult.getData());
        Object payMessage = jsonObject.get("payMessage");

        // 返回结果
        Map<String, String> resultMap = new HashMap<>();
        if (!"0000".equals(jsonObject.get("resultCode"))) {
            resultMap.put("msg", (String) jsonObject.get("errMsg"));
            // 更新订单状态，异常
            orderInfo.setStatus("4");
            orderInfo.setRemark((String) jsonObject.get("errMsg"));

            orderInfoService.updateOrderByNo(orderInfo);
        }
        resultMap.put("productName", productName);
        resultMap.put("payMessage", payMessage.toString());
        resultMap.put("amount", String.valueOf(orderInfo.getAmount()));
        return ResultData.getSuccessResult(resultMap);
    }

    /**
     * 组装支付参数
     *
     * @param orderNo     订单号
     * @param productName 产品名称
     * @param orderPrice  订单价格
     * @param orderIp     下单IP
     * @param returnUrl   返回URL
     * @param notifyUrl   通知URL
     * @param remark      备注
     * @return Map
     * @throws Exception 异常
     */
    private Map<String, Object> parsePayParams(String orderNo, String productName, String orderPrice, String orderIp,
                                               String returnUrl, String notifyUrl, String remark) throws Exception {

        // 选定的商户
        MerchantInfo currentMerchant = getCurrentMerchant();
        if (currentMerchant == null) return null;

        String payKey = currentMerchant.getPayKey();

        // 请求参数
        Map<String, Object> payParamMap = new HashMap<>();
        payParamMap.put("payKey", payKey);
        payParamMap.put("productType", productType);

        // 订单金额 , 单位:元
        payParamMap.put("orderPrice", orderPrice);

        // 订单编号
        payParamMap.put("outTradeNo", orderNo);

        // 订单时间
        String orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        payParamMap.put("orderTime", orderTime);

        // 商品名称
        payParamMap.put("productName", productName);

        // 下单IP
        payParamMap.put("orderIp", orderIp);

        // 页面通知返回URL
        payParamMap.put("returnUrl", returnUrl);

        // 后台消息通知URL
        payParamMap.put("notifyUrl", notifyUrl);

        // 支付备注
        payParamMap.put("remark", remark);

        // 签名及生成请求API的方法
        String sign = MerchantApiUtil.getSign(payParamMap, currentMerchant.getPaySecret());
        payParamMap.put("sign", sign);

        return payParamMap;
    }

    /**
     * 组装支付参数
     *
     * @param orderNo     订单号
     * @param productName 产品名称
     * @param orderPrice  订单价格
     * @param orderIp     下单IP
     * @param remark      备注
     * @return Map
     * @throws Exception 异常
     */
    private Map<String, Object> parseRechargePayParams(String orderNo, String productName, String orderPrice,
                                                       String orderIp, String remark) throws Exception {
        // 请求参数
        Map<String, Object> payParamMap = new HashMap<>();
        payParamMap.put("payKey", "3401ce1590b9495a86953da05425f8e9");
        payParamMap.put("productType", "20000203");

        // 订单金额 , 单位:元
        payParamMap.put("orderPrice", orderPrice);

        // 订单编号
        payParamMap.put("outTradeNo", orderNo);

        // 订单时间
        String orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        payParamMap.put("orderTime", orderTime);

        // 商品名称
        payParamMap.put("productName", productName);

        // 下单IP
        payParamMap.put("orderIp", orderIp);

        // 页面通知返回URL
        payParamMap.put("returnUrl", "http://192.168.0.90:8080/gateway/cnpPayNotify/returnNotify/");

        // 后台消息通知URL
        payParamMap.put("notifyUrl", "http://192.168.0.90:8080/gateway/cnpPayNotify/notify/");

        // 支付备注
        payParamMap.put("remark", remark);

        // 签名及生成请求API的方法
        String sign = MerchantApiUtil.getSign(payParamMap, "4f224dba265842eaba50b1543cd5d3aa");
        payParamMap.put("sign", sign);

        return payParamMap;
    }


    /**
     * 支付后的回调通知
     *
     * @param notifyParamMap 回调通知参数
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData<Void> payNotify(Map<String, String> notifyParamMap, HttpSession session) throws Exception {

        // 订单编号
        String orderNo = notifyParamMap.get("outTradeNo");
        if (StringUtils.isEmpty(orderNo)) {
            log.error("订单支付回调异常，返回订单号为空");
            return ResultData.getErrResult("订单号为空");
        }

        // 订单结果
        String payStatus = notifyParamMap.get("tradeStatus");
        if (StringUtils.isEmpty(payStatus)) {
            log.error("订单支付回调异常，返回的支付状态异常payStataus=" + payStatus);
            return ResultData.getErrResult("支付结果状态为空");
        }

        // 查询订单
        ResultData<OrderInfo> queryOrderResult = orderInfoService.getOrderByNo(orderNo);
        if (!queryOrderResult.isOk() || queryOrderResult.resultIsEmpty()) {
            log.error("订单支付回调异常，订单编号为 " + payStatus + " 的订单不存在");
            return ResultData.getErrResult("订单不存在");
        }
        OrderInfo orderInfo = queryOrderResult.getData();

        // 修改订单的状态
        String status;
        if ("SUCCESS".equals(payStatus)) {
            status = MuseOrderInfo.STATUS_SUCCESS;
        } else {
            status = MuseOrderInfo.STATUS_ERROR;
        }

        if (status != orderInfo.getStatus()) {
            ResultData resultData = orderInfoService.setOrderStatus(orderNo, Integer.valueOf(status));
            if (!resultData.isOk()) {
                return resultData;
            }
        } else {
            log.info("订单支付回调异常，订单状态没有改变，status=" + status + ", order status=" + orderInfo.getStatus());
        }

        // 如果是支付订单，则修改用户余额
        if (OrderInfo.ORDER_TYPE_RECHARGE.equals(orderInfo.getOrderType())) {
            ResultData modifyResult = userService.modifyUserAmount(orderInfo.getUserId(), orderInfo.getAmount(), session);
            if (!modifyResult.isOk()) {
                return ResultData.getErrResult("修改用户账户余额异常");
            }
        }

        return ResultData.getSuccessResult();
    }

    /**
     * 获取当前激活的商户
     *
     * @return MerchantInfo
     */
    private MerchantInfo getCurrentMerchant() {
        MerchantInfo merchant = null;
        try {
            ResultData resultData = merchantInfoService.getRandomActiveMerchant();
            log.info("获取随机激活的商户结果=" + resultData.toString());
            if (resultData.isOk() && resultData.resultIsNotEmpty()) {
                merchant = (MerchantInfo) resultData.getData();
            }
            log.info("----------------------------------------------------");

            if (merchant == null) {
                merchant = new MerchantInfo();
                merchant.setPayKey(payKey);
                merchant.setPaySecret(paySecret);
            }

        } catch (Exception e) {
            e.printStackTrace();
            merchant = new MerchantInfo();
            merchant.setPayKey(payKey);
            merchant.setPaySecret(paySecret);
        }

        log.info("当前使用的商户=" + merchant.toString());
        return merchant;
    }


}
