package com.merchant.entity;

import com.google.common.base.Preconditions;

/**
 * @program: muse-pay
 * @description: 商户基础信息
 * @author: Vincent
 * @create: 2019-03-06 10:06
 **/
public class MerchantBaseInfo extends BaseEntityInfo {

    private String name;

    private String doorPicName;

    private String doorPicPath;

    private String provinceName;

    private String provinceCode;

    private String cityName;

    private String cityCode;

    private String areaName;

    private String areaCode;

    private String address;


    /**
     * 默认构造方法
     */
    public MerchantBaseInfo() {

    }

    /**
     * 构造方法
     *
     * @param name
     * @param doorPicName
     * @param doorPicPath
     * @param provinceName
     * @param provinceCode
     * @param cityName
     * @param cityCode
     * @param areaName
     * @param areaCode
     * @param address
     * @param status
     */
    public MerchantBaseInfo(String name, String doorPicName, String doorPicPath, String provinceName, String provinceCode, String cityName,
                            String cityCode, String areaName, String areaCode, String address, String status) {
        this.name = name;
        this.doorPicName = doorPicName;
        this.doorPicPath = doorPicPath;
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.areaName = areaName;
        this.areaCode = areaCode;
        this.address = address;
        this.status = status;
    }

    /**
     * 校验对象属性是否为空
     *
     * @throws Exception 异常
     */
    public void verify() throws Exception {
        Preconditions.checkNotNull(provinceCode, "省份编号不能为空");
        Preconditions.checkNotNull(cityCode, "城市编号不能为空");
        Preconditions.checkNotNull(areaCode, "区县编号不能为空");

        Preconditions.checkNotNull(name, "商户名称不能为空");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoorPicName() {
        return doorPicName;
    }

    public void setDoorPicName(String doorPicName) {
        this.doorPicName = doorPicName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoorPicPath() {
        return doorPicPath;
    }

    public void setDoorPicPath(String doorPicPath) {
        this.doorPicPath = doorPicPath;
    }
}
