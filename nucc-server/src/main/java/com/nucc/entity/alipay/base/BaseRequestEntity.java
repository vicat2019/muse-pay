package com.nucc.entity.alipay.base;

import org.thymeleaf.util.StringUtils;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 请求基础类
 * @author: Vincent
 * @create: 2018-11-26 14:20
 **/
public class BaseRequestEntity implements Serializable {

    // 网联分配给收单机构的 pid 2088100020003000W01
    protected String pid;

    // 接口名称alipay.trade.pay
    protected String method;

    // 仅支持JSON JSON
    protected String format;

    // 请求使用的编码格式，如utf-8,gbk,gb2312等utf-8
    protected String charset;

    // 商户生成签名字符串所使用的签名算法类型，目前支持RSA和SM2，具体见“3.1报文的签名机制” RSA
    protected String sign_type;

    // 商户请求参数的签名串
    protected String sign;

    // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss" 2014-7-24 3:07:50
    protected String timestamp;

    // 调用的接口版本，固定为：1.0 1.0
    protected String version;

    // 第三方应用授权
    protected String appAuth_toke;

    // 网联服务器主动通知商户服务器里指定的页面https路径
    protected String notify_url;

    // 请求参数的集合，最大长度不限，除公共参数外，所有请求参数都必须放在这个参数中传递
    protected String biz_content;

    /**
     * 业务参数是否为空
     * @return
     */
    public boolean legalParam() {
        return !StringUtils.isEmpty(this.biz_content);
    }

    @Override
    public String toString() {
        return "BaseRequestEntity{" +
                "pid='" + pid + '\'' +
                ", method='" + method + '\'' +
                ", format='" + format + '\'' +
                ", charset='" + charset + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", version='" + version + '\'' +
                ", appAuth_toke='" + appAuth_toke + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", biz_content='" + biz_content + '\'' +
                '}';
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppAuth_toke() {
        return appAuth_toke;
    }

    public void setAppAuth_toke(String appAuth_toke) {
        this.appAuth_toke = appAuth_toke;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }
}
