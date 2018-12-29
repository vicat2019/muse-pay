package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;

public class ConsigneeInfo extends BaseEntityInfo {

    private Integer userId;

    private String name;

    private String mobile;

    private String address;

    /**
     * 默认构造方法
     */
    public ConsigneeInfo() {

    }

    /**
     * 构造方法
     *
     * @param userId
     */
    public ConsigneeInfo(int userId) {
        this.userId = userId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

}