package com.nucc.entity.alipay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: muse-pay
 * @description: 优惠券信息类
 * @author: Vincent
 * @create: 2018-11-26 15:46
 **/
public class VoucherDetail implements Serializable {

    private String id;

    private String name;

    private String type;

    private BigDecimal amount;

    private BigDecimal merchant_contribute;

    private BigDecimal other_contribute;

    private String memo;

    private String template_id;

    private ContributeDetail other_contribute_detail;

    private BigDecimal purchase_buyer_contribute;

    private BigDecimal purchase_merchant_contribute;

    private BigDecimal purchase_ant_contribute;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getMerchant_contribute() {
        return merchant_contribute;
    }

    public void setMerchant_contribute(BigDecimal merchant_contribute) {
        this.merchant_contribute = merchant_contribute;
    }

    public BigDecimal getOther_contribute() {
        return other_contribute;
    }

    public void setOther_contribute(BigDecimal other_contribute) {
        this.other_contribute = other_contribute;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public ContributeDetail getOther_contribute_detail() {
        return other_contribute_detail;
    }

    public void setOther_contribute_detail(ContributeDetail other_contribute_detail) {
        this.other_contribute_detail = other_contribute_detail;
    }

    public BigDecimal getPurchase_buyer_contribute() {
        return purchase_buyer_contribute;
    }

    public void setPurchase_buyer_contribute(BigDecimal purchase_buyer_contribute) {
        this.purchase_buyer_contribute = purchase_buyer_contribute;
    }

    public BigDecimal getPurchase_merchant_contribute() {
        return purchase_merchant_contribute;
    }

    public void setPurchase_merchant_contribute(BigDecimal purchase_merchant_contribute) {
        this.purchase_merchant_contribute = purchase_merchant_contribute;
    }

    public BigDecimal getPurchase_ant_contribute() {
        return purchase_ant_contribute;
    }

    public void setPurchase_ant_contribute(BigDecimal purchase_ant_contribute) {
        this.purchase_ant_contribute = purchase_ant_contribute;
    }
}
