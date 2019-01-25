package com.muse.pay.service.impl;

import com.muse.pay.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 用户扫描二维码支付后，往MQ中发送消息，本类处理该消息-调用通知下游支付结果
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/8 19 00
 **/
@Component
public class PayNotifyConsumer {
    private Logger log = LoggerFactory.getLogger("PayNotifyConsumer");

    @Autowired
    private AsyncService asyncService;


    @JmsListener(destination = "${mq_destination}", containerFactory = "jmsQueueListener")
    public void receiveQueue(final TextMessage message, Session session) {
        System.out.println("监听MQ，收到的消息内容=" + message);
        log.info("监听MQ，收到的消息内容=" + message);

        try {
            asyncService.executeAsync(message.getText());
            message.acknowledge();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("监听MQ消息异常=" + e.getMessage());
            try {
                session.recover();
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        }
    }

























}
