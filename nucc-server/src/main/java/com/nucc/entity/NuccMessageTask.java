package com.nucc.entity;

import com.alibaba.fastjson.JSON;
import com.muse.common.entity.ResultData;
import com.muse.common.util.HttpUtils;
import com.nucc.entity.alipay.AlipayNotifyCommonEntity;
import com.nucc.entity.alipay.TradeInfo;
import com.nucc.entity.weixin.WXTradeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Map;

/**
 * @program: muse-pay
 * @description: 消息处理类
 * @author: Vincent
 * @create: 2018-12-06 11:29
 **/
@Service("nuccMessageTask")
public class NuccMessageTask {
    private Logger log = LoggerFactory.getLogger("NuccMessageTask");

    @Qualifier("httpUtils")
    private HttpUtils httpUtils;


    @Async("asyncServiceExecutor")
    public void handle(NuccMessageInfo messageInfo) {
        if (messageInfo == null) {
            log.error("收到消息为空");
            return;
        }
        // 消息内容
        String content = messageInfo.getMsgContent();
        if (StringUtils.isEmpty(content)) {
            log.error("收到的消息内容为空=" + messageInfo.toString());
            return;
        }

        // 如果是微信的通知回调
        if (NuccMessageInfo.MSG_TYPE_WX_PAY.equals(messageInfo.getMsgType())) {
            // 解析成对象
            WXTradeInfo tradeInfo = JSON.parseObject(content, WXTradeInfo.class);
            if (tradeInfo == null) {
                log.error("解析消息内容，异常=" + messageInfo.toString());
                return;
            }
            // 生成微信支付返回内容
            WXNotificationResponse response = WXNotificationResponse.getSuccessResult(tradeInfo);
            try {
                ResultData httpResult = httpUtils.post(tradeInfo.getNotify_url(), JSON.toJSONString(response));
                log.info("微信支付回调结果，返回内容=" + httpResult.getData().toString());
            } catch (Exception e) {
                log.error("微信支付回调，异常=" + e.getMessage());
                e.printStackTrace();
            }


        } else if (NuccMessageInfo.MSG_TYPE_ALI_PAY.equals(messageInfo.getMsgType())) {
            // 如果是支付宝的通知回调
            TradeInfo tradeInfo = JSON.parseObject(content, TradeInfo.class);
            if (tradeInfo == null) {
                log.error("解析消息内容，异常=" + messageInfo.toString());
                return;
            }
            // 检查回调地址是否为空
            if (StringUtils.isEmpty(tradeInfo.getNotify_url())) {
                log.error("订单[" + tradeInfo.getOut_trade_no() + "]回调地址为空");
                return;
            }
            // 解析成返回信息
            Map<String, Object> response = AlipayNotifyCommonEntity.getAlipayPayNotify(tradeInfo);
            String jsonContent = JSON.toJSONString(response);

            try {
                // 调用回调
                ResultData result = httpUtils.post(tradeInfo.getNotify_url(), jsonContent);
                log.info("支付宝回调返回内容=" + result.getData());
            } catch (Exception e) {
                e.printStackTrace();
                log.error("支付宝支付回调，异常=" + e.getMessage());
            }
        }


        log.info("处理消息=" + messageInfo.toString());
    }
}
