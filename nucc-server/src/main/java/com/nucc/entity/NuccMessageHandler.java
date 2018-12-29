package com.nucc.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: muse-pay
 * @description: 消息处理
 * @author: Vincent
 * @create: 2018-12-06 10:31
 **/
@Service(value = "nuccMessageHandler")
public class NuccMessageHandler implements Runnable {
    public static boolean isRunning = false;

    private NuccMessageQueue delayQueue;

    @Autowired
    private NuccMessageTask nuccMessageTask;

    /**
     * 构造方法
     */
    public NuccMessageHandler() {
        this.delayQueue = NuccMessageQueue.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            isRunning = true;
            try {
                // 获取消息(阻塞)
                NuccMessageInfo messageInfo = delayQueue.take();
                nuccMessageTask.handle(messageInfo);

            } catch (Exception e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
    }
}
