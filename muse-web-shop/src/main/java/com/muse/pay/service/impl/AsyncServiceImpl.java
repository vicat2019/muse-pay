package com.muse.pay.service.impl;

import com.muse.common.entity.ResultData;
import com.muse.common.util.MD5;
import com.muse.pay.entity.MuseMerchantInfo;
import com.muse.pay.entity.MuseOrderInfo;
import com.muse.pay.service.AsyncService;
import com.muse.pay.service.MuseMerchantInfoService;
import com.muse.pay.service.MuseOrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/8 19 11
 **/
@Service("asyncService")
public class AsyncServiceImpl implements AsyncService {
    private static Logger log = LoggerFactory.getLogger(AsyncServiceImpl.class);

    @Autowired
    private MuseOrderInfoService museOrderInfoService;

    @Autowired
    private MuseMerchantInfoService museMerchantInfoService;

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(String orderNo) {

        try {
            ResultData resultData = museOrderInfoService.getByOrderNo(orderNo);
            if (!resultData.isOk() || resultData.getData() == null) {
                log.error("处理回调异常，查询订单[" + orderNo + "]失败=" + resultData.getMessage());
                return;
            }

            MuseOrderInfo museOrderInfo = (MuseOrderInfo) resultData.getData();
            String notifyUrl = museOrderInfo.getNotifyUrl();
            if (StringUtils.isEmpty(notifyUrl)) {
                log.error("处理回调异常，订单[" + orderNo + "]的通知地址为空");
                return;
            }
            if (museOrderInfo.getStatus().equals("0")) {
                log.error("处理回调异常，订单[" + orderNo + "]状态为等待支付");
                return;
            }

            // 查询商户信息
            ResultData merchantResult = museMerchantInfoService.getByUserNo(museOrderInfo.getMerchantNo());
            if (!merchantResult.isOk() || merchantResult.getData() == null) {
                log.error("查询商户信息异常，" + museOrderInfo.getMerchantNo());
                return;
            }
            MuseMerchantInfo merchant = (MuseMerchantInfo) merchantResult.getData();

            // 整理参数
            Map<String, String> resultMap = museOrderInfo.genReturnMap();
            resultMap.put("resultCode", "0");
            resultMap.put("resultMsg", "成功");
            resultMap.put("sign", MD5.sign(resultMap, merchant.getSecret()));

            // 根据结果生成回调
            RestTemplate restTemplate = new RestTemplate();
            String bodyValTemplate = getValueForStr(resultMap);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity entity = new HttpEntity(bodyValTemplate, headers);
            ResponseEntity response = restTemplate.exchange(notifyUrl, HttpMethod.POST, entity, String.class);

            log.info("发送请求返回状态=" + response.getStatusCode() + "，返回内容="
                    + (response.getBody() != null ? response.getBody().toString() : "返回内容为空"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("回调处理异常=" + e.getMessage());
        }
    }


    /**
     * 以String的方式返回值
     *
     * @return String
     */
    private String getValueForStr(Map<String, String> valueMap) throws Exception {

        String resultStr = "";
        for (Map.Entry item : valueMap.entrySet()) {
            resultStr += item.getKey() + "=" + URLEncoder.encode(item.getValue().toString(), "utf-8") + "&";
        }
        if (resultStr.length() > 1) {
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }

        return resultStr;
    }
}
