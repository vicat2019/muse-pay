package com.muse.pay.entity.common;

import com.muse.common.util.MD5;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一下单结果类
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/8 16 44
 **/
public class UnifiedOrderResultData implements Serializable {
    public static final String STATUS_ERR = "-1";

    /**
     * 状态
     */
    private String status;
    /**
     * 状态信息
     */
    private String statusMessage;
    /**
     * 结果码
     */
    private String resultCode;
    /**
     * 流水编码
     */
    private String sysNo;
    /**
     * 二维码
     */
    private String qrCode;
    /**
     * 签名
     */
    private String sign;

    /**
     * 默认构造方法
     */
    public UnifiedOrderResultData() {

    }

    /**
     * 构造方法
     *
     * @param status        状态码
     * @param statusMessage 状态消息
     * @param resultCode    结果码
     * @param sysNo         商户订单号
     * @param qrCode        二维码地址
     */
    public UnifiedOrderResultData(String status, String statusMessage, String resultCode, String sysNo, String qrCode) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.resultCode = resultCode;
        this.sysNo = sysNo;
        this.qrCode = qrCode;
    }

    /**
     * 生成签名
     *
     * @param merchantSecret KEY值
     * @return String
     * @throws UnsupportedEncodingException 异常
     */
    public String makeSign(String merchantSecret) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();

        params.put("status", this.status);
        params.put("statusMessage", this.statusMessage);
        params.put("resultCode", this.resultCode);
        params.put("sysNo", this.sysNo);
        params.put("qrCode", this.qrCode);

        this.sign = MD5.sign(params, merchantSecret);
        return this.sign;
    }

    /**
     * 参数异常结果
     *
     * @param errMsg 异常信息
     * @return UnifiedOrderResultData
     */
    public static UnifiedOrderResultData getErrorResult(String errMsg) {
        return new UnifiedOrderResultData(STATUS_ERR, errMsg, STATUS_ERR, "", "");
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
