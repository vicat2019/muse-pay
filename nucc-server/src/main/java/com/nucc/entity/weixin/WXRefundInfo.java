package com.nucc.entity.weixin;

import com.muse.common.entity.BaseEntityInfo;

public class WXRefundInfo extends BaseEntityInfo {

    private String refund_id;

    private String out_trade_no;

    private String out_refund_no;

    private Integer refund_fee;

    private String refund_fee_type;

    private String refund_desc;

    private String refund_account;

    private String limit_panotify_url;

    private int cash_fee;

    private Integer total_fee;

    private String refund_status;

    // 退款入账账户
    private String refund_recv_account;

    private int settlement_refund;


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public Integer getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Integer refund_fee) {
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

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public int getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getRefund_recv_account() {
        return refund_recv_account;
    }

    public void setRefund_recv_account(String refund_recv_account) {
        this.refund_recv_account = refund_recv_account;
    }

    public int getSettlement_refund() {
        return settlement_refund;
    }

    public void setSettlement_refund(int settlement_refund) {
        this.settlement_refund = settlement_refund;
    }
}