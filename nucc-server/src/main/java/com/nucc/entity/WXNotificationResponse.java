package com.nucc.entity;

import com.nucc.entity.weixin.WXBaseResponse;
import com.nucc.entity.weixin.WXTradeInfo;
import com.nucc.util.TextUtils;

/**
 * @program: muse-pay
 * @description: 微信支付成功回调信息类
 * @author: Vincent
 * @create: 2018-12-06 15:08
 **/
public class WXNotificationResponse extends WXBaseResponse {

    private String result_code;

    private String err_code;
    private String err_code_des;

    private String appid;

    private String mch_id;

    private String sub_mch_id;

    private String nonce_str;

    private String sign;

    private String trade_type;

    private String bank_type;

    private int total_fee;

    private int cash_fee;

    private int settlement_total_fee;

    private String transaction_id;

    private String out_trade_no;

    private String time_end;


    /**
     * 获取成功返回内容
     *
     * @return
     */
    public static WXNotificationResponse getSuccessResult() {
        WXNotificationResponse response = new WXNotificationResponse();
        response.setSuccessValue();
        response.result_code = WXBaseResponse.RESULT_SUCCESS;

        return response;
    }

    /**
     * 获取成功返回内容
     *
     * @return
     */
    public static WXNotificationResponse getSuccessResult(WXTradeInfo tradeInfo) {
        WXNotificationResponse response = getSuccessResult();

        // 设置属性值
        response.appid = tradeInfo.getAppid();
        response.mch_id = tradeInfo.getMch_id();
        response.sub_mch_id = tradeInfo.getSub_mch_id();
        response.nonce_str = TextUtils.getRandomStr(28);
        response.trade_type = tradeInfo.getTrade_type();
        response.bank_type = tradeInfo.getBank_type();
        response.total_fee = tradeInfo.getTotal_fee();
        response.cash_fee = tradeInfo.getCash_fee();
        response.settlement_total_fee = tradeInfo.getSettlement_total_fee();
        response.transaction_id = tradeInfo.getTransaction_id();
        response.out_trade_no = tradeInfo.getOut_trade_no();
        response.time_end = tradeInfo.getTime_end();

        return response;
    }

    @Override
    public String toString() {
        return "WXNotificationResponse{" +
                "result_code='" + result_code + '\'' +
                ", err_code='" + err_code + '\'' +
                ", err_code_des='" + err_code_des + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", sub_mch_id='" + sub_mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", bank_type='" + bank_type + '\'' +
                ", total_fee=" + total_fee +
                ", cash_fee=" + cash_fee +
                ", settlement_total_fee=" + settlement_total_fee +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", time_end='" + time_end + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                '}';
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getSub_mch_id() {
        return sub_mch_id;
    }

    public void setSub_mch_id(String sub_mch_id) {
        this.sub_mch_id = sub_mch_id;
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

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public int getSettlement_total_fee() {
        return settlement_total_fee;
    }

    public void setSettlement_total_fee(int settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
}
