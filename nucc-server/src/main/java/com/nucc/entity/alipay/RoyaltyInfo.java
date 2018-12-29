package com.nucc.entity.alipay;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 分账类
 * @author: Vincent
 * @create: 2018-11-26 15:26
 **/
public class RoyaltyInfo implements Serializable {

    private String royalty_type;

    private String royalty_detail_infos;

    public String getRoyalty_type() {
        return royalty_type;
    }

    public void setRoyalty_type(String royalty_type) {
        this.royalty_type = royalty_type;
    }

    public String getRoyalty_detail_infos() {
        return royalty_detail_infos;
    }

    public void setRoyalty_detail_infos(String royalty_detail_infos) {
        this.royalty_detail_infos = royalty_detail_infos;
    }
}
