package com.nucc.controller;

import com.alibaba.fastjson.JSON;
import com.muse.common.entity.ResultData;
import com.nucc.entity.NuccMessageInfo;
import com.nucc.service.activemq.ActiveMQServiceImpl;
import com.nucc.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: muse-pay
 * @description: RocketMQ测试
 * @author: Vincent
 * @create: 2018-12-04 18:22
 **/
@RequestMapping("/rocket")
@RestController
public class RocketMqController {
    private Logger log = LoggerFactory.getLogger("RocketMqController");

    @Autowired
    private ActiveMQServiceImpl activeMQService;



    @RequestMapping("/test")
    public ResultData test() {
        try {

            for (int i=0; i<10; i++) {
                String msgNo = TextUtils.getRandomStr(10);
                String msgContext = TextUtils.getRandomStr(30);
                NuccMessageInfo messageInfo = new NuccMessageInfo(msgNo, msgContext, 30*(i+1));

                activeMQService.sendMessage(messageInfo);
                log.info((i+1) + "、发送消息： " + JSON.toJSONString(messageInfo));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultData.getSuccessResult();
    }


}
