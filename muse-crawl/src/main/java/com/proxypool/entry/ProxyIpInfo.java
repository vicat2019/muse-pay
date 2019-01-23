package com.proxypool.entry;

import com.muse.common.entity.BaseEntityInfo;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @Description: 代理信息类
 * @Author: Vincent
 * @Date: 2018/10/15
 */
public class ProxyIpInfo extends BaseEntityInfo {
    // IP地址
    private String ip;
    // 编号
    private String code;
    // 端口
    private String port;
    // 类型HTTP, HTTPS
    private String type;
    // 相应速度
    private String responseSpeed;
    // 地址
    private String location;
    // 上次检查时间
    private String lastCheckTime;
    // 是否匿名
    private String anonymous;
    // 活跃时间
    private String survivalTime;
    // 国家
    private String country;
    // 来源
    private String source;
    // 成功次数
    private int successCount;
    // 检查次数
    private int checkCount;
    // 上次检查成功时间
    private LocalDateTime lastSuccessTime;
    // 删除状态
    private int delStatus;


    // 要检查是否还可用, 失败3次弃用
    private int failCount;



    /**
     * 默认构造方法
     */
    public ProxyIpInfo() {

    }

    /**
     * 构造方法
     *
     * @param ip
     * @param port
     * @param type
     * @param responseSpeed
     * @param location
     * @param lastCheckTime
     * @param anonymous
     * @param survivalTime
     * @param country
     * @param source
     */
    public ProxyIpInfo(String ip, String port, String type, String responseSpeed, String location, String lastCheckTime,
                       String anonymous, String survivalTime, String country, String source) {
        this.ip = ip;
        this.port = port;
        this.type = type;
        this.responseSpeed = responseSpeed;
        this.location = location;
        this.lastCheckTime = lastCheckTime;
        this.anonymous = anonymous;
        this.survivalTime = survivalTime;
        this.country = country;
        this.code = String.valueOf(hashCode());
        this.source = source;
    }

    /**
     * 获取实例
     *
     * @param ip
     * @param port
     * @param type
     * @param responseSpeed
     * @param location
     * @param lastCheckTime
     * @param anonymous
     * @param survivalTime
     * @param country
     * @return
     */
    public static ProxyIpInfo getInstance(String ip, String port, String type, String responseSpeed, String location, String lastCheckTime,
                                          String anonymous, String survivalTime, String country, String source) {
        return new ProxyIpInfo(ip, port, type, responseSpeed, location, lastCheckTime, anonymous, survivalTime, country, source);
    }

    /**
     * 构造方法
     *
     * @param ip
     * @param status
     */
    public ProxyIpInfo(String ip, String status) {
        this.ip = ip;
        this.status = status;
    }

    /**
     * 是否合法：IP地址和Port端口号是否为空
     *
     * @return boolean
     */
    public boolean legal() {
        return !StringUtils.isEmpty(this.getIp()) && !StringUtils.isEmpty(this.getPort());
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProxyIpInfo) {
            ProxyIpInfo proxy = (ProxyIpInfo) obj;
            return ip.equalsIgnoreCase(proxy.getIp().trim())
                    && port.equals(proxy.getPort());
        }

        return false;
    }

    /**
     * 是否可以搁置不用
     * @return boolean
     */
    public boolean available() {
        return failCount < 3;
    }

    @Override
    public String toString() {
        return "ProxyIpInfo{" +
                "ip='" + ip + '\'' +
                ", code='" + code + '\'' +
                ", port='" + port + '\'' +
                ", type='" + type + '\'' +
                ", responseSpeed='" + responseSpeed + '\'' +
                ", location='" + location + '\'' +
                ", lastCheckTime='" + lastCheckTime + '\'' +
                ", anonymous='" + anonymous + '\'' +
                ", survivalTime='" + survivalTime + '\'' +
                ", country='" + country + '\'' +
                ", source='" + source + '\'' +
                ", successCount=" + successCount +
                ", checkCount=" + checkCount +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getResponseSpeed() {
        return responseSpeed;
    }

    public void setResponseSpeed(String responseSpeed) {
        this.responseSpeed = responseSpeed == null ? null : responseSpeed.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(String lastCheckTime) {
        this.lastCheckTime = lastCheckTime == null ? null : lastCheckTime.trim();
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous == null ? null : anonymous.trim();
    }

    public String getSurvivalTime() {
        return survivalTime;
    }

    public void setSurvivalTime(String survivalTime) {
        this.survivalTime = survivalTime == null ? null : survivalTime.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public LocalDateTime getLastSuccessTime() {
        return lastSuccessTime;
    }

    public void setLastSuccessTime(LocalDateTime lastSuccessTime) {
        this.lastSuccessTime = lastSuccessTime;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
}