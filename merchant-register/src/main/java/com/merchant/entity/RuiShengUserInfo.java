package com.merchant.entity;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: roncoo-pay
 * @description: RuiSheng商户进件信息
 * @author: Vincent
 * @create: 2019-02-12 10:09
 **/
public class RuiShengUserInfo implements Serializable {

    // 代理商编号
    private String mchid;

    // 商户名称
    private String name;

    // 省编号
    private String province;
    // 市编号
    private String city;
    // 区县编号
    private String area;
    // 商户所在详细地址
    private String address;

    // 商户法人姓名
    private String legelname;
    // 商户法人身份证号
    private String legelcertno;

    // 商户邮件
    private String email;
    // 商户客服电话
    private String phone;


    // 收款卡对应的银行代码
    private String bankno;
    // 收款卡对应的银行支行代码
    private String branchno;
    // 收款卡号
    private String cardno;
    // 电子收款卡号
    private String ecardno;
    // 收款人姓名
    private String payname;
    // 收款卡对应的手机号
    private String payphone;

    // 收款卡所在省份
    private String cardprovince;
    // 收款卡所在城市
    private String cardcity;
    // 收款卡所在区县
    private String cardarea;


    // 商户类型 1是个人，2是企业，默认为1
    private String type;
    // 商户证件类型 1是身份证，2是营业执照，默认为1
    private String certtype;
    // 商户证件号
    private String certno;


    // 商户营业执照照片
    private String buslicpic;

    // 商户身份证正面照片
    private String legfrontpic;

    // 商户手持身份证照片
    private String handpic;

    // 商户身份证反面照片
    private String legbackpic;
    // 商户门头照照片
    private String doorpic;
    // 商户银行开户照片
    private String accopenpic;
    // 商户收银台照片
    private String cashierpic;
    // 商户通道费率参数
    private String channelinfo;


    public Map<String, Object> toMap() throws Exception {
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("mchid", mchid);
        paramMap.put("name", name);
        paramMap.put("province", province);
        paramMap.put("city", city);
        paramMap.put("area", area);
        paramMap.put("address", address);
        paramMap.put("legelname", legelname);
        paramMap.put("legelcertno", legelcertno);
        paramMap.put("email", email);
        paramMap.put("phone", phone);


        paramMap.put("bankno", bankno);
        paramMap.put("branchno", branchno);
        paramMap.put("cardno", cardno);
        paramMap.put("ecardno", ecardno);
        paramMap.put("payname", payname);
        paramMap.put("payphone", payphone);
        paramMap.put("cardprovince", cardprovince);
        paramMap.put("cardcity", cardcity);
        paramMap.put("cardarea", cardarea);


        paramMap.put("type", type);
        paramMap.put("certtype", certtype);
        paramMap.put("certno", certno);


        paramMap.put("buslicpic", buslicpic);
        paramMap.put("legfrontpic", legfrontpic);
        paramMap.put("handpic", handpic);
        paramMap.put("legbackpic", legbackpic);
        paramMap.put("doorpic", doorpic);
        paramMap.put("accopenpic", accopenpic);
        paramMap.put("cashierpic", cashierpic);


        if (!StringUtils.isEmpty(channelinfo)) {
            Map<String, Object> rate = new HashMap<>();
            Map<String, Integer> sub = Maps.newHashMap();
            sub.put("rate", 60);
            sub.put("fee", 0);
            rate.put("bnwxFix", sub);
            paramMap.put("channelinfo", JSONObject.toJSONString(rate));
        }

        return paramMap;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLegelname() {
        return legelname;
    }

    public void setLegelname(String legelname) {
        this.legelname = legelname;
    }

    public String getLegelcertno() {
        return legelcertno;
    }

    public void setLegelcertno(String legelcertno) {
        this.legelcertno = legelcertno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankno() {
        return bankno;
    }

    public void setBankno(String bankno) {
        this.bankno = bankno;
    }

    public String getBranchno() {
        return branchno;
    }

    public void setBranchno(String branchno) {
        this.branchno = branchno;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getEcardno() {
        return ecardno;
    }

    public void setEcardno(String ecardno) {
        this.ecardno = ecardno;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public String getPayphone() {
        return payphone;
    }

    public void setPayphone(String payphone) {
        this.payphone = payphone;
    }

    public String getCardprovince() {
        return cardprovince;
    }

    public void setCardprovince(String cardprovince) {
        this.cardprovince = cardprovince;
    }

    public String getCardcity() {
        return cardcity;
    }

    public void setCardcity(String cardcity) {
        this.cardcity = cardcity;
    }

    public String getCardarea() {
        return cardarea;
    }

    public void setCardarea(String cardarea) {
        this.cardarea = cardarea;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getBuslicpic() {
        return buslicpic;
    }

    public void setBuslicpic(String buslicpic) {
        this.buslicpic = buslicpic;
    }

    public String getLegfrontpic() {
        return legfrontpic;
    }

    public void setLegfrontpic(String legfrontpic) {
        this.legfrontpic = legfrontpic;
    }

    public String getHandpic() {
        return handpic;
    }

    public void setHandpic(String handpic) {
        this.handpic = handpic;
    }

    public String getLegbackpic() {
        return legbackpic;
    }

    public void setLegbackpic(String legbackpic) {
        this.legbackpic = legbackpic;
    }

    public String getDoorpic() {
        return doorpic;
    }

    public void setDoorpic(String doorpic) {
        this.doorpic = doorpic;
    }

    public String getAccopenpic() {
        return accopenpic;
    }

    public void setAccopenpic(String accopenpic) {
        this.accopenpic = accopenpic;
    }

    public String getCashierpic() {
        return cashierpic;
    }

    public void setCashierpic(String cashierpic) {
        this.cashierpic = cashierpic;
    }

    public String getChannelinfo() {
        return channelinfo;
    }

    public void setChannelinfo(String channelinfo) {
        this.channelinfo = channelinfo;
    }
}
