package com.seckill.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/20 17 00
 **/
@Configuration
public class ActiveMQConfig {
    private static Logger log = LoggerFactory.getLogger("ActiveMQConfig");

    @Value("${spring.activemq.user}")
    private String mqName;

    @Value("${spring.activemq.password}")
    private String mqPass;

    @Value("${spring.activemq.broker-url}")
    private String mqUrl;

    @Value("${mq_destination}")
    private String destination;


    @Bean
    public Queue queue() {
        return new ActiveMQQueue(destination);
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        // 是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        // 重发次数,默认为6次 这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(5);
        // 重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        // 第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        // 是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        // 设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public JmsTemplate jmsTemplate(Queue queue) {
        ActiveMQConnectionFactory activeMQConnectionFactory = connectionFactory();
        JmsTemplate jmsTemplate = new JmsTemplate();
        // 进行持久化配置 1表示非持久化，2表示持久化
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory);
        // 此处可不设置默认，在发送消息时也可设置队列
        jmsTemplate.setDefaultDestination(queue);
        // 客户端签收模式
        jmsTemplate.setSessionAcknowledgeMode(4);
        return jmsTemplate;
    }

    // 定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = connectionFactory();
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);
        // 设置连接数
        factory.setConcurrency("1-10");
        // 重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(4);
        return factory;
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        //此链接信息可放入配置文件中
        log.info("mqName=" + mqName + ", mqPass=" + mqPass + ", mqUrl=" + mqUrl);
        return new ActiveMQConnectionFactory(mqName, mqPass, mqUrl);
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory) {
        return new JmsMessagingTemplate(connectionFactory);
    }


}
