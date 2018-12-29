package com.nucc.entity;

import java.util.concurrent.DelayQueue;

/**
 * @program: muse-pay
 * @description: 消息队列
 * @author: Vincent
 * @create: 2018-12-06 10:34
 **/
public class NuccMessageQueue {
    private static DelayQueue<NuccMessageInfo> delayQueue;

    private static NuccMessageQueue instance;

    /**
     * 获取单例
     *
     * @return
     */
    public static NuccMessageQueue getInstance() {
        if (instance == null) {
            instance = new NuccMessageQueue();
        }
        return instance;
    }

    /**
     * 构造方法
     */
    private NuccMessageQueue() {
        delayQueue = new DelayQueue<NuccMessageInfo>();
    }

    /**
     * 返回消息
     *
     * @return NuccMessageInfo
     */
    public NuccMessageInfo take() {
        try {
            if (delayQueue != null) {
                return delayQueue.take();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 添加消息
     *
     * @param messageInfo 消息
     */
    public void put(NuccMessageInfo messageInfo) {
        if (delayQueue == null) {
            getInstance().put(messageInfo);
        } else {
            delayQueue.put(messageInfo);
        }
    }

    /**
     * 返回列表的长度
     *
     * @return long
     */
    public long size() {
        if (delayQueue == null) return 0;
        return delayQueue.size();
    }

}
