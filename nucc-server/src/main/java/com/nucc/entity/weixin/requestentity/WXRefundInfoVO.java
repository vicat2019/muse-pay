package com.nucc.entity.weixin.requestentity;

import com.nucc.entity.weixin.WXRefundInfo;

/**
 * @program: muse-pay
 * @description: 退款参数类
 * @author: Vincent
 * @create: 2018-12-02 17:34
 **/
public class WXRefundInfoVO {


    private String appid;

    private String mch_id;

    private String sub_mch_id;

    private String channel_id;

    private String nonce_str;

    private String idc_flag;

    private String sign;


    private String out_trade_no;

    private String out_refund_no;

    private int refund_fee;

    private String refund_fee_type;

    private String refund_desc;

    private String refund_account;

    private String limit_panotify_url;

    private int total_fee;


    /**
     * 获取退款信息
     *
     * @return WXRefundInfo
     */
    public WXRefundInfo getRefundInfo() {
        WXRefundInfo refundInfo = new WXRefundInfo();
        refundInfo.setOut_trade_no(this.out_refund_no);
        refundInfo.setOut_refund_no(this.out_refund_no);
        refundInfo.setRefund_fee(this.refund_fee);
        refundInfo.setRefund_fee_type(this.refund_fee_type);
        refundInfo.setRefund_desc(this.refund_desc);
        refundInfo.setRefund_account(this.refund_account);
        refundInfo.setTotal_fee(this.total_fee);
        refundInfo.setLimit_panotify_url(this.limit_panotify_url);

        return refundInfo;
    }

    @Override
    public String toString() {
        return "WXRefundInfoVO{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", sub_mch_id='" + sub_mch_id + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", idc_flag='" + idc_flag + '\'' +
                ", sign='" + sign + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", out_refund_no='" + out_refund_no + '\'' +
                ", refund_fee=" + refund_fee +
                ", refund_fee_type='" + refund_fee_type + '\'' +
                ", refund_desc='" + refund_desc + '\'' +
                ", refund_account='" + refund_account + '\'' +
                ", limit_panotify_url='" + limit_panotify_url + '\'' +
                ", total_fee=" + total_fee +
                '}';
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

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getIdc_flag() {
        return idc_flag;
    }

    public void setIdc_flag(String idc_flag) {
        this.idc_flag = idc_flag;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getRefund_fee_type() {
        return refund_fee_type;
    }

    public void setRefund_fee_type(String refund_fee_type) {
        this.refund_fee_type = refund_fee_type;
    }

    public String getRefund_desc() {
        return refund_desc;
    }

    public void setRefund_desc(String refund_desc) {
        this.refund_desc = refund_desc;
    }

    public String getRefund_account() {
        return refund_account;
    }

    public void setRefund_account(String refund_account) {
        this.refund_account = refund_account;
    }

    public String getLimit_panotify_url() {
        return limit_panotify_url;
    }

    public void setLimit_panotify_url(String limit_panotify_url) {
        this.limit_panotify_url = limit_panotify_url;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }
}
