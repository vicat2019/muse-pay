package com.muse.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.muse.common.entity.ResultData;
import com.muse.common.util.RedisUtil;
import com.muse.common.util.TextUtils;
import com.muse.pay.entity.MuseMerchantInfo;
import com.muse.pay.entity.MuseOrderInfo;
import com.muse.pay.entity.common.UnifiedOrderResultData;
import com.muse.pay.entity.vo.UnifiedOrderVO;
import com.muse.pay.service.MuseMerchantInfoService;
import com.muse.pay.service.MuseOrderInfoService;
import com.muse.pay.service.MusePayService;
import com.muse.pay.util.MerchantApiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上游支付服务
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/13 14 48
 **/
@Service("musePayService")
public class MusePayServiceImpl implements MusePayService {
    private Logger log = LoggerFactory.getLogger("MusePayServiceImpl");

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MuseOrderInfoService museOrderInfoService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MuseMerchantInfoService museMerchantInfoService;

    @Value("${musePayUrl}")
    private String musePayUrl;

    @Value("${mq_destination}")
    private String destination;


    /**
     * 统一下单
     *
     * @param params 参数内容
     * @return UnifiedOrderResultData
     * @throws Exception 异常
     */
    @Override
    public UnifiedOrderResultData unifiedOrder(String params) throws Exception {

        //获取到JSONObject
        if (StringUtils.isEmpty(params)) {
            return UnifiedOrderResultData.getErrorResult("参数异常");
        }

        // 根据参数内容生成对象
        UnifiedOrderVO unifiedOrder = UnifiedOrderVO.fromStreamContext(params);
        log.info("下游请求的unifiedOrder统一下单，参数=" + unifiedOrder.toString());
        if (unifiedOrder == null) {
            log.error("下游请求的unifiedOrder统一下单，参数为空");
            return UnifiedOrderResultData.getErrorResult("参数异常");
        }

        // 保存提交的数据
        MuseOrderInfo museOrderInfo = MuseOrderInfo.fromUnifiedParams(unifiedOrder);
        // 检查参数
        if (!museOrderInfo.verify()) {
            return UnifiedOrderResultData.getErrorResult("参数异常");
        }

        // 检查是否已经提交过
        ResultData<MuseOrderInfo> queryResult = museOrderInfoService.getByMerchantAndOrderCode(unifiedOrder.getMerchantNo(),
                unifiedOrder.getTradeNo());
        // 订单已经存在
        if (queryResult.isOk() && queryResult.resultIsNotEmpty()) {
            MuseOrderInfo orderInfo = queryResult.getData();
            // 是否是等待支付
            if (MuseOrderInfo.STATUS_WAITING_PAY.equals(orderInfo.getStatus())) {
                return new UnifiedOrderResultData("0", "成功", "000000", unifiedOrder.getTradeNo(),
                        orderInfo.getQrCodeUrl());
            } else {
                return new UnifiedOrderResultData("2", "异常", "2", unifiedOrder.getTradeNo(), "");
            }
        }

        // 生成订单编码
        museOrderInfo.setOrderNo(TextUtils.makeMuseOrderNo());
        museOrderInfo.setQrCodeUrl(musePayUrl + "/" + museOrderInfo.getOrderNo());
        museOrderInfo.setSerialNo(TextUtils.makeSerialNo());
        museOrderInfo.setStatus(MuseOrderInfo.STATUS_WAITING_PAY);
        museOrderInfoService.add(museOrderInfo);

        // 获取商户的支付秘钥
        ResultData secretResult = museMerchantInfoService.getSecretByUserNo(museOrderInfo.getMerchantNo());
        if (!secretResult.isOk()) {
            return new UnifiedOrderResultData("3", "Secret为空", "3", unifiedOrder.getTradeNo(), "");
        }
        String secret = (String) secretResult.getData();

        // 返回给下游结果
        UnifiedOrderResultData unifiedOrderResult = new UnifiedOrderResultData("0", "交易成功", "000000",
                unifiedOrder.getTradeNo(), museOrderInfo.getQrCodeUrl());
        unifiedOrderResult.makeSign(secret);

        log.info("下游请求的unifiedOrder统一下单，返回内容=" + unifiedOrder.toString());
        return unifiedOrderResult;
    }


    /**
     * 支付订单
     *
     * @param orderNo 订单编号
     * @return ResultData
     * @throws Exception 异常
     */
    @Override
    public ResultData<String> pay(String orderNo) throws Exception {

        // 检查参数
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("支付失败，参数异常");
        }

        // 查询订单
        ResultData<MuseOrderInfo> resultData = museOrderInfoService.getByOrderNo(orderNo);
        if (!resultData.isOk() || resultData.resultIsEmpty()) {
            return ResultData.getErrResult(resultData.getMessage());
        }

        // 设置支付成功 0等待支付 1支付成功 2支付失败 3超时
        MuseOrderInfo museOrderInfo = resultData.getData();

        // 更新用户价格
        ResultData merchantResult = museMerchantInfoService.getByUserNo(museOrderInfo.getMerchantNo());
        if (!merchantResult.isOk() || merchantResult.resultIsEmpty()) {
            return ResultData.getErrResult("商户不存在");
        }
        MuseMerchantInfo merchantInfo = (MuseMerchantInfo) merchantResult.getData();

        // 订单是否已经结束或者已经支付
        if (museOrderInfo.getStatus().equals(MuseOrderInfo.STATUS_WAITING_PAY)) {
            // 更新订单状态
            museOrderInfo.setStatus(MuseOrderInfo.STATUS_SUCCESS);
            museOrderInfoService.update(museOrderInfo);

            // 更新用户余额
            if (merchantInfo.getAmount() == null) merchantInfo.setAmount(BigDecimal.valueOf(0));
            merchantInfo.setAmount(merchantInfo.getAmount().add(museOrderInfo.getAmount()));
            museMerchantInfoService.update(merchantInfo);

        } else if (museOrderInfo.getStatus().equals(MuseOrderInfo.STATUS_SUCCESS)) {
            return ResultData.getSuccessResult("该订单已经支付");

        } else if (museOrderInfo.getStatus().equals(MuseOrderInfo.STATUS_ERROR)
                || museOrderInfo.getStatus().equals(MuseOrderInfo.STATUS_OVERTIME)) {
            return ResultData.getErrResult("订单异常，请重新下单");
        }

        // 支付成功后的回调
        jmsTemplate.convertAndSend(destination, orderNo);
        log.info("MUSE支付成功，订单号=" + orderNo);

        // 返回结果
        return ResultData.getSuccessResult("支付成功");
    }

    /**
     * 查询订单
     *
     * @param content 字符串参数
     * @return
     * @throws Exception
     */
    @Override
    public ResultData queryOrder(String content) throws Exception {

        if (StringUtils.isEmpty(content)) {
            return ResultData.getErrResult("参数不能为空");
        }

        JSONObject paramJson = TextUtils.fromStreamContext(content);
        if (paramJson == null) {
            return ResultData.getErrResult("参数异常");
        }

        String orderNo = paramJson.getString("out_trade_no");
        if (StringUtils.isEmpty(orderNo)) {
            return ResultData.getErrResult("订单号不能为空");
        }

        ResultData<MuseOrderInfo> queryResult = museOrderInfoService.getByOrderNo(orderNo);
        if (!queryResult.isOk() || queryResult.getData() == null) {
            return ResultData.getErrResult("订单不存在");
        }
        MuseOrderInfo orderInfo = queryResult.getData();

        // 查询商户
        ResultData merchantResult = museMerchantInfoService.getByUserNo((String) paramJson.get("mch_id"));
        if (!merchantResult.isOk() || merchantResult.getData() == null) {
            return ResultData.getErrResult("商户不存在");
        }
        MuseMerchantInfo merchantInfo = (MuseMerchantInfo) merchantResult.getData();

        // 结果
        Map<String, Object> resultMap = getOrderQueryResult(orderInfo.getOrderNo(), String.valueOf(orderInfo.getAmount()),
                merchantInfo.getSecret());
        return ResultData.getSuccessResult(resultMap);
    }

    /**
     * 生成查询订单返回结果
     *
     * @param orderNo 订单号
     * @param amount  金额
     * @param secret  支付秘钥
     * @return Map
     */
    public static Map<String, Object> getOrderQueryResult(String orderNo, String amount, String secret) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultCode", "0000");
        resultMap.put("outTradeNo", orderNo);
        resultMap.put("orderPrice", amount);
        resultMap.put("trxNo", "0");
        resultMap.put("orderStatus", "SUCCESS");
        resultMap.put("completeDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        resultMap.put("errMsg", "");
        resultMap.put("sign", MerchantApiUtil.getSign(resultMap, secret));

        return resultMap;
    }


}
