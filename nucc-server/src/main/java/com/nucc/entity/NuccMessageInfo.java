package com.nucc.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @program: muse-pay
 * @description: 消息类
 * @author: Vincent
 * @create: 2018-12-06 10:12
 **/
public class NuccMessageInfo implements Delayed, Serializable {
    private Logger log = LoggerFactory.getLogger("NuccMessageInfo");

    public static final String MSG_TYPE_WX_PAY = "MSG_TYPE_WX_PAY";
    public static final String MSG_TYPE_ALI_PAY = "MSG_TYPE_ALI_PAY";

    // 消息编号
    private String msgNo;

    // 消息类型
    private String msgType;

    // 消息内容
    private String msgContent;

    // 消息时间
    private Date timestamp;

    // 延迟时长，这个是必须的属性因为要按照这个判断延时时长
    private long expireTime;

    /**
     * 默认构造方法
     */
    public NuccMessageInfo() {

    }

    /**
     * 构造方法
     *
     * @param msgNo
     * @param msgContent
     * @param delaySecond
     */
    public NuccMessageInfo(String msgNo, String msgContent, long delaySecond) {
        this.msgNo = msgNo;
        this.msgContent = msgContent;
        this.expireTime = (System.currentTimeMillis() + (delaySecond * 1000));
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "NuccMessageInfo{" +
                "msgNo='" + msgNo + '\'' +
                ", msgType='" + msgType + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", timestamp=" + timestamp +
                ", expireTime=" + expireTime +
                '}';
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    /**
     * 设置延迟时间
     *
     * @param delaySecond 延迟(秒)
     */
    public void setDelayTime(long delaySecond) {
        this.expireTime = (System.currentTimeMillis() + (delaySecond * 1000));
    }


    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(String msgNo) {
        this.msgNo = msgNo;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

}
