package com.nucc.entity.alipay;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 用户信息扩展
 * @author: Vincent
 * @create: 2018-11-26 15:29
 **/
public class ExtUserInfo implements Serializable {

    private String name;

    private String mobile;

    private String cert_type;

    private String cert_no;

    private String min_age;

    private String fix_buyer;

    private String need_check_info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCert_type() {
        return cert_type;
    }

    public void setCert_type(String cert_type) {
        this.cert_type = cert_type;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getMin_age() {
        return min_age;
    }

    public void setMin_age(String min_age) {
        this.min_age = min_age;
    }

    public String getFix_buyer() {
        return fix_buyer;
    }

    public void setFix_buyer(String fix_buyer) {
        this.fix_buyer = fix_buyer;
    }

    public String getNeed_check_info() {
        return need_check_info;
    }

    public void setNeed_check_info(String need_check_info) {
        this.need_check_info = need_check_info;
    }
}
