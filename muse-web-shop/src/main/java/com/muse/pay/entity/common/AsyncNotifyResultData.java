package com.muse.pay.entity.common;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/8 18 16
 **/
public class AsyncNotifyResultData {

    private String mchtid;

    private String sysNo;

    private String transactionId;

    private String totalAmount;

    private String resultCode;

    private String resultMsg;

    private String sign;

    /**
     * 默认构造方法
     */
    public AsyncNotifyResultData() {

    }

    /**
     * 构造方法
     *
     * @param mchtid        商户ID
     * @param sysNo         序列号
     * @param transactionId 事物序号
     * @param totalAmount   总金额
     * @param resultCode    返回码
     * @param resultMsg     返回消息
     */
    public AsyncNotifyResultData(String mchtid, String sysNo, String transactionId, String totalAmount, String resultCode, String resultMsg) {
        this.mchtid = mchtid;
        this.sysNo = sysNo;
        this.transactionId = transactionId;
        this.totalAmount = totalAmount;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public String getMchtid() {
        return mchtid;
    }

    public void setMchtid(String mchtid) {
        this.mchtid = mchtid;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
