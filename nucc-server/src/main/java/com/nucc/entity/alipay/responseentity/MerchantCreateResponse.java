package com.nucc.entity.alipay.responseentity;

import com.nucc.entity.alipay.AddressInfo;
import com.nucc.entity.alipay.BankCardInfo;
import com.nucc.entity.alipay.ContactInfo;
import com.nucc.entity.alipay.MerchantInfo;
import com.nucc.entity.alipay.base.BaseResponseEntity;

import java.util.Arrays;

/**
 * @program: muse-pay
 * @description: 商户入驻返回结果
 * @author: Vincent
 * @create: 2018-11-27 09:30
 **/
public class MerchantCreateResponse extends BaseResponseEntity {

    // 商户在网联入驻成功后, 生成的全局唯一的商户编号
    private String sub_mserchant_id;

    // 商户编号，有机构定义，需要保证在机构下唯一
    private String external_id;

    // 间连商户等级
    private String indeirect_level;

    // 商户名称
    private String name;

    // 商户简称
    private String alias_name;

    // 商户客服电话
    private String service_phone;

    // 商户经营类目
    private String category_id;

    // 商户来源机构标识，填写机构在支付宝的pid
    private String source;

    // 商户证件编号
    private String business_license;

    // 商户证件类型
    private String business_license_type;

    // 商户联系人信息
    private ContactInfo[] contact_info;

    // 商户地址信息
    private AddressInfo[] address_info;

    // 商户对银行所开立的结算卡信息
    private BankCardInfo[] bankcard_info;

    // 商户的支付二维码中的信息，用于营销活动
    private String[] pay_code_info;

    // 商户的支付宝账号
    private String[] logon_id;

    // 商户备注信息，可写额外信息
    private String memo;

    /**
     * 默认构造方法
     */
    public MerchantCreateResponse() { }

    /**
     * 构造方法
     *
     * @param sub_mserchant_id
     */
    public MerchantCreateResponse(String sub_mserchant_id) {
        this.sub_mserchant_id = sub_mserchant_id;
    }

    /**
     * 获取返回实例
     *
     * @param sub_mserchant_id
     * @return
     */
    public static MerchantCreateResponse getInstance(String sub_mserchant_id) {
        return new MerchantCreateResponse(sub_mserchant_id);
    }

    /**
     * 根据商户信息获取返回实例
     * @param merchantInfo
     * @return
     */
    public static MerchantCreateResponse getInstanceFrom(MerchantInfo merchantInfo) {
        MerchantCreateResponse response = new MerchantCreateResponse();
        response.setAddress_info(merchantInfo.getAddress_info());
        response.setAlias_name(merchantInfo.getAlias_name());
        response.setBankcard_info(merchantInfo.getBankcard_info());
        response.setBusiness_license(merchantInfo.getBusiness_license());
        response.setBusiness_license_type(merchantInfo.getBusiness_license_type());
        response.setCategory_id(merchantInfo.getCategory_id());
        response.setContact_info(merchantInfo.getContact_info());
        response.setExternal_id(merchantInfo.getExternal_id());
        response.setIndeirect_level(merchantInfo.getIndeirect_level());
        response.setLogon_id(merchantInfo.getLogon_id());
        response.setMemo(merchantInfo.getMemo());
        response.setName(merchantInfo.getName());
        response.setPay_code_info(merchantInfo.getPay_code_info());
        response.setService_phone(merchantInfo.getService_phone());
        response.setSource(merchantInfo.getSource());
        response.setSub_mserchant_id(merchantInfo.getSub_merchant_id());

        return response;
    }

    @Override
    public String toString() {
        return "MerchantCreateResponse{" +
                "sub_mserchant_id='" + sub_mserchant_id + '\'' +
                ", external_id='" + external_id + '\'' +
                ", indeirect_level='" + indeirect_level + '\'' +
                ", name='" + name + '\'' +
                ", alias_name='" + alias_name + '\'' +
                ", service_phone='" + service_phone + '\'' +
                ", category_id='" + category_id + '\'' +
                ", source='" + source + '\'' +
                ", business_license='" + business_license + '\'' +
                ", business_license_type='" + business_license_type + '\'' +
                ", contact_info=" + Arrays.toString(contact_info) +
                ", address_info=" + Arrays.toString(address_info) +
                ", bankcard_info=" + Arrays.toString(bankcard_info) +
                ", pay_code_info=" + Arrays.toString(pay_code_info) +
                ", logon_id=" + Arrays.toString(logon_id) +
                ", memo='" + memo + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", sub_code='" + sub_code + '\'' +
                ", sub_msg='" + sub_msg + '\'' +
                '}';
    }

    public String getSub_mserchant_id() {
        return sub_mserchant_id;
    }

    public void setSub_mserchant_id(String sub_mserchant_id) {
        this.sub_mserchant_id = sub_mserchant_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getIndeirect_level() {
        return indeirect_level;
    }

    public void setIndeirect_level(String indeirect_level) {
        this.indeirect_level = indeirect_level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(String business_license) {
        this.business_license = business_license;
    }

    public String getBusiness_license_type() {
        return business_license_type;
    }

    public void setBusiness_license_type(String business_license_type) {
        this.business_license_type = business_license_type;
    }

    public ContactInfo[] getContact_info() {
        return contact_info;
    }

    public void setContact_info(ContactInfo[] contact_info) {
        this.contact_info = contact_info;
    }

    public AddressInfo[] getAddress_info() {
        return address_info;
    }

    public void setAddress_info(AddressInfo[] address_info) {
        this.address_info = address_info;
    }

    public BankCardInfo[] getBankcard_info() {
        return bankcard_info;
    }

    public void setBankcard_info(BankCardInfo[] bankcard_info) {
        this.bankcard_info = bankcard_info;
    }

    public String[] getPay_code_info() {
        return pay_code_info;
    }

    public void setPay_code_info(String[] pay_code_info) {
        this.pay_code_info = pay_code_info;
    }

    public String[] getLogon_id() {
        return logon_id;
    }

    public void setLogon_id(String[] logon_id) {
        this.logon_id = logon_id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
