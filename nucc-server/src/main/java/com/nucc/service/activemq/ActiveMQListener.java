package com.nucc.service.activemq;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.NuccMessageHandler;
import com.nucc.entity.NuccMessageInfo;
import com.nucc.entity.NuccMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * @program: muse-pay
 * @description: 消息监听器
 * @author: Vincent
 * @create: 2018-12-06 09:38
 **/
@Component
public class ActiveMQListener {
    private Logger log = LoggerFactory.getLogger("ActiveMQListener");

    @Autowired
    private NuccMessageHandler nuccMessageHandler;


    @JmsListener(destination = "${mq_destination}", containerFactory = "jmsQueueListener")
    public void receiveQueue(final TextMessage message) {
        try {
            // 消息队列
            NuccMessageQueue queue = NuccMessageQueue.getInstance();

            // 解析成消息对象
            NuccMessageInfo messageInfo = JSON.parseObject(message.getText(), NuccMessageInfo.class);
            if (messageInfo == null) {
                log.error("解析接收到的消息内容异常");
            }

            // 添加到队列中
            queue.put(messageInfo);
            if (!NuccMessageHandler.isRunning) {
                new Thread(nuccMessageHandler).start();
            }

            message.acknowledge();

        } catch (JMSException e) {
            e.printStackTrace();
            log.error("ActiveMQ消息监听器监听，异常=" + e.getMessage());
        }
    }


}
