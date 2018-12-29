package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * MUSE支付的商户
 */
public class MuseMerchantInfo extends BaseEntityInfo {

    /** 商户编号 */
    private String userNo;

    /** 商户名称 */
    private String name;

    /** 支付秘钥 */
    private String secret;

    /** 商户金额 */
    private BigDecimal amount;

    /**
     * 检查属性不为空
     * @return boolean
     */
    public boolean verify() {
        return !StringUtils.isEmpty(userNo) && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(secret);
    }

    public String getCode() {
        return userNo;
    }

    public void setCode(String code) {
        this.userNo = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}