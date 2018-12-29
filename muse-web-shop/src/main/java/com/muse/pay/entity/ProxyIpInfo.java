package com.muse.pay.entity;

import java.util.Date;

public class ProxyIpInfo {
    private Integer id;

    private String ip;

    private String port;

    private String type;

    private String responseSpeed;

    private String location;

    private String lastCheckTime;

    private String anonymous;

    private String survivalTime;

    private String country;

    private Integer version;

    private Date modifyTime;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}