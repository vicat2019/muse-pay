package com.nucc.entity.alipay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: muse-pay
 * @description: 账单类
 * @author: Vincent
 * @create: 2018-11-26 15:44
 **/
public class TradeFundBill implements Serializable {

    private String fund_channel;

    private String bank_code;

    private BigDecimal amount;

    private BigDecimal real_amount;

    private String fund_type;

    public String getFund_channel() {
        return fund_channel;
    }

    public void setFund_channel(String fund_channel) {
        this.fund_channel = fund_channel;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getReal_amount() {
        return real_amount;
    }

    public void setReal_amount(BigDecimal real_amount) {
        this.real_amount = real_amount;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }
}
