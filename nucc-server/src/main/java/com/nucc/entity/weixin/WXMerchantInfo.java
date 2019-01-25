package com.nucc.entity.weixin;

import com.muse.common.entity.BaseEntityInfo;

public class WXMerchantInfo extends BaseEntityInfo {

    private String appid;

    private String mch_id;

    private String sub_merchant_id;

    private String merchant_name;

    private String merchant_shortname;

    private String service_phone;

    private String contact;

    private String contact_phone;

    private String contact_email;

    private String business;

    private String channel_id;

    private String contact_wechatid_type;

    private String contact_wechatid;

    private String merchant_remark;




    @Override
    public String toString() {
        return "WXMerchantInfo{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", merchant_name='" + merchant_name + '\'' +
                ", merchant_shortname='" + merchant_shortname + '\'' +
                ", service_phone='" + service_phone + '\'' +
                ", contact='" + contact + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", contact_email='" + contact_email + '\'' +
                ", business='" + business + '\'' +
                ", channel_id='" + channel_id + '\'' +
                ", contact_wechatid_type='" + contact_wechatid_type + '\'' +
                ", contact_wechatid='" + contact_wechatid + '\'' +
                ", merchant_remark='" + merchant_remark + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id == null ? null : mch_id.trim();
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name == null ? null : merchant_name.trim();
    }

    public String getMerchant_shortname() {
        return merchant_shortname;
    }

    public void setMerchant_shortname(String merchant_shortname) {
        this.merchant_shortname = merchant_shortname == null ? null : merchant_shortname.trim();
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone == null ? null : service_phone.trim();
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? null : contact.trim();
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone == null ? null : contact_phone.trim();
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email == null ? null : contact_email.trim();
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business == null ? null : business.trim();
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id == null ? null : channel_id.trim();
    }

    public String getContact_wechatid_type() {
        return contact_wechatid_type;
    }

    public void setContact_wechatid_type(String contact_wechatid_type) {
        this.contact_wechatid_type = contact_wechatid_type == null ? null : contact_wechatid_type.trim();
    }

    public String getContact_wechatid() {
        return contact_wechatid;
    }

    public void setContact_wechatid(String contact_wechatid) {
        this.contact_wechatid = contact_wechatid == null ? null : contact_wechatid.trim();
    }

    public String getMerchant_remark() {
        return merchant_remark;
    }

    public void setMerchant_remark(String merchant_remark) {
        this.merchant_remark = merchant_remark == null ? null : merchant_remark.trim();
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }
}