package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;

/**
 * @Description: 支付时使用的商户信息
 * @Author: Vincent
 * @Date: 2018/9/26
 */
public class MerchantInfo extends BaseEntityInfo {

    /**
     * 当前正在使用的商户
     */
    public static String CACHE_KEY = "CURRENT_MERCHANT";

    private String merchantName;

    private String payKey;

    private String paySecret;

    @Override
    public String toString() {
        return "MerchantInfo{" +
                "merchantName='" + merchantName + '\'' +
                ", payKey='" + payKey + '\'' +
                ", paySecret='" + paySecret + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName == null ? null : merchantName.trim();
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey == null ? null : payKey.trim();
    }

    public String getPaySecret() {
        return paySecret;
    }

    public void setPaySecret(String paySecret) {
        this.paySecret = paySecret == null ? null : paySecret.trim();
    }

}