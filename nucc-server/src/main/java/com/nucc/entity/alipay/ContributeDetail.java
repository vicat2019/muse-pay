package com.nucc.entity.alipay;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: muse-pay
 * @description: 出资方详情
 * @author: Vincent
 * @create: 2018-11-26 15:49
 **/
public class ContributeDetail implements Serializable {

    private String contribute_type;

    private BigDecimal contribute_amount;

    public String getContribute_type() {
        return contribute_type;
    }

    public void setContribute_type(String contribute_type) {
        this.contribute_type = contribute_type;
    }

    public BigDecimal getContribute_amount() {
        return contribute_amount;
    }

    public void setContribute_amount(BigDecimal contribute_amount) {
        this.contribute_amount = contribute_amount;
    }
}
