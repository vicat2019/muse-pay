package com.nucc.controller;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.alipay.base.BaseRequestEntity;
import com.nucc.entity.alipay.base.BaseResponseEntity;
import com.nucc.entity.alipay.requestentity.MerchantInfoVO;
import com.nucc.entity.alipay.requestentity.TradeInfoVO;
import com.nucc.service.alipay.MerchantInfoService;
import com.nucc.service.alipay.NuccAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: muse-pay
 * @description: 网联支付宝控制类
 * @author: Vincent
 * @create: 2018-11-26 14:58
 **/
@RestController
@RequestMapping("/nucc/alipay")
public class NuccAlipayController {
    private Logger log = LoggerFactory.getLogger("NuccAlipayController");

    @Autowired
    private NuccAlipayService nuccAlipayService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @RequestMapping("/trade")
    public Map<String, Object> nuccPay(BaseRequestEntity requestEntity) {
        if (requestEntity == null || !requestEntity.legalParam()) {
            String method = "Unknown";
            if (requestEntity != null) {
                return BaseResponseEntity.getErrResult("参数不能为空").genResultMap(requestEntity.getMethod());
            } else {
                return BaseResponseEntity.getErrResult("参数不能为空").genResultMap(method);
            }
        }
        log.info("支付类接口，收到参数=" + requestEntity.toString());

        BaseResponseEntity response;

        try {
            TradeInfoVO tradePayParam = JSON.parseObject(requestEntity.getBiz_content(), TradeInfoVO.class);
            if (tradePayParam == null) {
                log.error("参数为空");
                return BaseResponseEntity.getErrResult("业务参数异常=" + requestEntity.getBiz_content()).genResultMap(requestEntity.getMethod());
            }
            log.info("支付类接口，解析后的参数对象=" + tradePayParam.toString());

            if (requestEntity.getMethod().equals("alipay.trade.pay")) {
                response = nuccAlipayService.tradePay(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.query")) {
                response = nuccAlipayService.tradeQuery(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.precreate")) {
                response = nuccAlipayService.tradePrecreate(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.create")) {
                response = nuccAlipayService.tradeCreate(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.cancel")) {
                response = nuccAlipayService.tradeCancel(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.refund")) {
                response = nuccAlipayService.tradeRefund(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.fastpay.refund.query")) {
                response = nuccAlipayService.tradeFastpayRefundQuery(tradePayParam);

            } else if (requestEntity.getMethod().equals("alipay.trade.close")) {
                response = nuccAlipayService.tradeClose(tradePayParam);
            } else {
                return BaseResponseEntity.getFailInstance().genResultMap(requestEntity.getMethod());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponseEntity.getFailInstance().genResultMap(requestEntity.getMethod());
        }

        return response.genResultMap(requestEntity.getMethod());
    }

    @RequestMapping("/qrcode/{trade_no}")
    public BaseResponseEntity scanQrCode(@PathVariable("trade_no") String trade_no) {
        BaseResponseEntity result;
        try {
            result = nuccAlipayService.scanQrCode(trade_no);
        } catch (Exception e) {
            e.printStackTrace();
            result = BaseResponseEntity.getErrResult(e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/merchant")
    public Map<String, Object> nuccMerchant(BaseRequestEntity requestEntity) {
        log.info("商户报备类交易参数=" + (requestEntity != null ? requestEntity.toString() : "null"));
        if (requestEntity == null) {
            return BaseResponseEntity.getErrResult("参数不能为空").genResultMap("Unknown");
        }

        BaseResponseEntity result;
        try {
            // 解析出业务参数
            MerchantInfoVO merchantInfoVO = null;
            if (requestEntity.legalParam()) {
                merchantInfoVO = JSON.parseObject(requestEntity.getBiz_content(), MerchantInfoVO.class);
            }
            if (merchantInfoVO == null) {
                return BaseResponseEntity.getErrResult("业务参数异常=" + requestEntity.getBiz_content()).genResultMap(requestEntity.getMethod());
            }

            if (requestEntity.getMethod().equals("ant.merchant.expand.indirect.create")) {
                result = merchantInfoService.add(merchantInfoVO);

            } else if (requestEntity.getMethod().equals("ant.merchant.expand.indirect.query")) {
                result = merchantInfoService.queryMerchantByKey(merchantInfoVO);

            } else if (requestEntity.getMethod().equals("ant.merchant.expand.indirect.modify")) {
                result = merchantInfoService.update(merchantInfoVO);

            } else {
                result = BaseResponseEntity.getErrResult();
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = BaseResponseEntity.getErrResult(e.getMessage());
        }

        log.info("商户报备类交易返回结果=" + result.genResultMap(requestEntity.getMethod()));
        return result.genResultMap(requestEntity.getMethod());
    }

    @RequestMapping("/notify")
    public String test(HttpEntity<byte[]> requestEntity) {
        try {
            byte[] body = requestEntity.getBody();
            if (body == null) {
                log.error("参数内容为空");
                return "FAIL";
            }

            // 解析数据
            String jsonContent = new String(body, "UTF-8");
            log.info("回调内容=" + jsonContent);
            Map<String, String> dataObj = JSON.parseObject(jsonContent, Map.class);
            log.info("解析回调内容后的对象=" + dataObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "SUCCESS";
    }

    @RequestMapping("/tt")
    public String test() {
        return "test---";
    }





}
