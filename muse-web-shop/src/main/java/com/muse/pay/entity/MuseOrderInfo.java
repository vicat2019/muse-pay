package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.pay.entity.vo.UnifiedOrderVO;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * MUSE商户支付订单信息
 */
public class MuseOrderInfo extends BaseEntityInfo {

    public static final String STATUS_WAITING_PAY = "0";
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_ERROR = "2";
    public static final String STATUS_OVERTIME = "3";

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商户号
     */
    private String merchantNo;

    /**
     * 商户订单号
     */
    private String merchantOrderNo;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 下游通知地址
     */
    private String notifyUrl;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 流水号
     */
    private String serialNo;

    /**
     * 二维码地址
     */
    private String qrCodeUrl;


    /**
     * 检查参数
     *
     * @return
     */
    public boolean verify() {
        if (StringUtils.isEmpty(merchantNo)) {
            return false;
        }
        if (StringUtils.isEmpty(merchantOrderNo)) {
            return false;
        }
        if (StringUtils.isEmpty(notifyUrl)) {
            return false;
        }
        if (StringUtils.isEmpty(subject)) {
            return false;
        }
        if (amount == null || amount.equals(BigDecimal.valueOf(0))) {
            return false;
        }
        return true;
    }

    /**
     * 以Map的方式返回值
     */
    public Map<String, String> genReturnMap() {
        Map<String, String> resultMap = new HashMap<>();

        resultMap.put("mchtid", this.merchantNo);
        resultMap.put("sysNo", this.serialNo);
        resultMap.put("transactionId", this.merchantOrderNo);
        resultMap.put("totalAmount", String.valueOf(this.amount));

        return resultMap;
    }

    /**
     * 从VO转换成PayOrderInfo
     *
     * @param unifiedOrderVO VO
     * @return MuseOrderInfo
     */
    public static MuseOrderInfo fromUnifiedParams(UnifiedOrderVO unifiedOrderVO) {
        MuseOrderInfo museOrderInfo = new MuseOrderInfo();
        museOrderInfo.setMerchantNo(unifiedOrderVO.getMerchantNo());
        museOrderInfo.setMerchantOrderNo(unifiedOrderVO.getTradeNo());
        museOrderInfo.setAmount(new BigDecimal(unifiedOrderVO.getTotalAmount()).divide(new BigDecimal(100)));
        museOrderInfo.setNotifyUrl(unifiedOrderVO.getNotifyUrl());
        museOrderInfo.setSubject(unifiedOrderVO.getSubject());

        return museOrderInfo;
    }

    @Override
    public String toString() {
        return "MuseOrderInfo{" +
                "id=" + id +
                ", status=" + status +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                ", orderNo='" + orderNo + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", merchantOrderNo='" + merchantOrderNo + '\'' +
                ", amount=" + amount +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", subject='" + subject + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                '}';
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }


}