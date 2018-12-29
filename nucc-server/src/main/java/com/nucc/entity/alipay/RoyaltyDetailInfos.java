package com.nucc.entity.alipay;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 分账详情类
 * @author: Vincent
 * @create: 2018-11-26 15:27
 **/
public class RoyaltyDetailInfos implements Serializable {

    private int serial_no;

    private String trans_in_type;

    private String batch_no;

    private String out_relationId;

    private String trans_out;

    private String trans_in;

    private double amount;

    private String desc;

    private String amount_percentage;

    public int getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(int serial_no) {
        this.serial_no = serial_no;
    }

    public String getTrans_in_type() {
        return trans_in_type;
    }

    public void setTrans_in_type(String trans_in_type) {
        this.trans_in_type = trans_in_type;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getOut_relationId() {
        return out_relationId;
    }

    public void setOut_relationId(String out_relationId) {
        this.out_relationId = out_relationId;
    }

    public String getTrans_out() {
        return trans_out;
    }

    public void setTrans_out(String trans_out) {
        this.trans_out = trans_out;
    }

    public String getTrans_in() {
        return trans_in;
    }

    public void setTrans_in(String trans_in) {
        this.trans_in = trans_in;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAmount_percentage() {
        return amount_percentage;
    }

    public void setAmount_percentage(String amount_percentage) {
        this.amount_percentage = amount_percentage;
    }
}
