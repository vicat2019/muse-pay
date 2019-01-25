package com.nucc.service.activemq;

import com.nucc.entity.NuccMessageInfo;

public interface ActiveMQService {

    /**
     * 发送消息
     *
     * @param messageInfo
     * @throws Exception
     */
    void sendMessage(NuccMessageInfo messageInfo) throws Exception;

}
