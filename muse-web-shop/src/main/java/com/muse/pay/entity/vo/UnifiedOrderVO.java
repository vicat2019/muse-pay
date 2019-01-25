package com.muse.pay.entity.vo;

import com.alibaba.fastjson.JSONObject;
import com.muse.common.util.TextUtils;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;

/**
 * 统一下单参数对象
 * @Author: Administrator
 * @Date: 2018 2018/8/9 17 53
 **/
public class UnifiedOrderVO implements Serializable {

    /**
     * 商户编号
     */
    private String merchantNo;
    /**
     * 订单号
     */
    private String tradeNo;
    /**
     * 总金额
     */
    private String totalAmount;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 商品名称
     */
    private String subject;
    /**
     * 签名
     */
    private String sign;

    /**
     * 根据JSON生成对象
     *
     * @param json json对象
     * @return UnifiedOrderVO
     */
    public static UnifiedOrderVO fromJson(JSONObject json) {
        UnifiedOrderVO payParamVO = new UnifiedOrderVO();
        payParamVO.setMerchantNo((String) json.get("mcht_no"));
        payParamVO.setTradeNo((String) json.get("trade_no"));
        payParamVO.setTotalAmount((String) json.get("totalAmount"));
        payParamVO.setNotifyUrl((String) json.get("notify_url"));
        payParamVO.setSubject((String) json.get("subject"));
        payParamVO.setSign((String) json.get("sign"));

        if (StringUtils.isEmpty(payParamVO.getMerchantNo())
                || StringUtils.isEmpty(payParamVO.getTradeNo())
                || StringUtils.isEmpty(payParamVO.getTotalAmount())) {
            return null;
        }

        return payParamVO;
    }

    /**
     * 从字符串中生成对象
     *
     * @param context 字符串
     * @return
     */
    public static UnifiedOrderVO fromStreamContext(String context) {
        JSONObject obj = TextUtils.fromStreamContext(context);
        if (obj != null) {
            return UnifiedOrderVO.fromJson(obj);
        }
        return null;
    }

    @Override
    public String toString() {
        return "UnifiedOrderVO{" +
                "merchantNo='" + merchantNo + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", subject='" + subject + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
