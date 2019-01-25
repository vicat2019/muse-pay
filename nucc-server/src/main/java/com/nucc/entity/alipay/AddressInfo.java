package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import org.thymeleaf.util.StringUtils;

/**
 * @program: muse-pay
 * @description: 地址信息类
 * @author: Vincent
 * @create: 2018-11-27 09:17
 **/
public class AddressInfo extends BaseEntityInfo {

    private String sub_merchant_id;

    // 城市编码
    private String city_code;

    // 区县编码
    private String district_code;

    // 地址
    private String address;

    // 省份编码
    private String province_code;

    // 经度
    private String longitude;

    // 纬度
    private String latitude;

    // 地址类型
    private String type;


    /**
     * 检查地址参数是否合法
     *
     * @return
     */
    public ResultData legalParam() {
        if (StringUtils.isEmpty(sub_merchant_id)) {
            return ResultData.getErrResult("商戶编号不能为空");
        }
        if (StringUtils.isEmpty(city_code)) {
            return ResultData.getErrResult("城市编码不能为空");
        }
        if (StringUtils.isEmpty(district_code)) {
            return ResultData.getErrResult("区县编码不能为空");
        }
        if (StringUtils.isEmpty(address)) {
            return ResultData.getErrResult("地址不能为空");
        }

        return ResultData.getSuccessResult();
    }


    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }
}
