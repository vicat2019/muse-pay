package com.nucc.entity.weixin.requestentity;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 交易请求基础类
 * @author: Vincent
 * @create: 2018-12-02 15:24
 **/
public class WXTradeBaseInfoVO implements Serializable {

    private String idc_flag;

    private String nonce_str;

    private String sign;


    public String getIdc_flag() {
        return idc_flag;
    }

    public void setIdc_flag(String idc_flag) {
        this.idc_flag = idc_flag;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
