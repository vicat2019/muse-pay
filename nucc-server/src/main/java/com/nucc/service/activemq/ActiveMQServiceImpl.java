package com.nucc.service.activemq;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.NuccMessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @program: muse-pay
 * @description: ActiveMQ消息处理类
 * @author: Vincent
 * @create: 2018-12-06 09:47
 **/
@Service("activeMQService")
public class ActiveMQServiceImpl implements ActiveMQService {
    private Logger log = LoggerFactory.getLogger("ActiveMQServiceImpl");

    @Value("${mq_destination}")
    private String destination;

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送MQ消息
     *
     * @param messageInfo
     */
    @Override
    public void sendMessage(NuccMessageInfo messageInfo) throws Exception {
        if (messageInfo == null) {
            log.error("发送消息，消息为空");
            throw new Exception("要发送的消息为空");
        }

        // 转换成字符串
        String jsonStr = JSON.toJSONString(messageInfo);
        jmsTemplate.convertAndSend(destination, jsonStr);
    }


}
