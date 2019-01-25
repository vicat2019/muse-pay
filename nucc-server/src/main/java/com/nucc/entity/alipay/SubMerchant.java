package com.nucc.entity.alipay;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 子商户类
 * @author: Vincent
 * @create: 2018-11-26 15:28
 **/
public class SubMerchant implements Serializable {

    private String sub_merchant_id;

    private String merchant_type;

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }

    public String getMerchant_type() {
        return merchant_type;
    }

    public void setMerchant_type(String merchant_type) {
        this.merchant_type = merchant_type;
    }
}
