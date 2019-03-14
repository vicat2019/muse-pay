package com.merchant.entity;


import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.merchant.util.Rsaencrypt;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: roncoo-pay
 * @description: RuiSheng商户进件信息
 * @author: Vincent
 * @create: 2019-02-12 10:09
 **/
public class RuiShengUserInfo extends BaseEntityInfo {

    // 代理商编号
    private String mchid;
    // 子商户编号
    private String submchid;

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
    private String type = "1";
    // 商户证件类型 1是身份证，2是营业执照，默认为1
    private String certtype = "1";
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
    private String rate;
    private String fee = "0";

    // 省市区
    private String provincename;
    private String cityname;
    private String areaname;

    // 开户省市区
    private String cardprovincename;
    private String cardcityname;
    private String cardareaname;

    // 开户行和支行名称
    private String bankname;
    private String branchname;

    // 备注
    private String remark;

    public RuiShengUserInfo() {

    }

    public static RuiShengUserInfo genUserInfo(String mchid, String name, String certno, String payphone, String cardno, String ecardno,
                                               String bankno, String bankname, String branchno, String branchname, String cardprovince,
                                               String cardprovincename, String cardcity, String cardcityname, String cardarea, String cardareaname,
                                               String buslicpic, String frontpic, String backpic, String handpic) {
        RuiShengUserInfo userInfo = new RuiShengUserInfo();
        userInfo.setMchid(mchid);

        userInfo.setPayname(name);
        userInfo.setLegelname(name);
        userInfo.setLegelcertno(certno);
        userInfo.setCertno(certno);
        userInfo.setPayphone(payphone);

        userInfo.setEcardno(ecardno);
        userInfo.setCardno(cardno);

        userInfo.setBankno(bankno);
        userInfo.setBankname(bankname);
        userInfo.setBranchno(branchno);
        userInfo.setBranchname(branchname);

        userInfo.setCardprovince(cardprovince);
        userInfo.setCardprovincename(cardprovincename);
        userInfo.setCardcity(cardcity);
        userInfo.setCardcityname(cardcityname);
        userInfo.setCardarea(cardarea);
        userInfo.setCardareaname(cardareaname);

        userInfo.setBuslicpic(buslicpic);
        userInfo.setLegfrontpic(frontpic);
        userInfo.setLegbackpic(backpic);
        userInfo.setHandpic(handpic);

        return userInfo;
    }


    /**
     * 返回Map格式的商户信息
     *
     * @return Map
     * @throws Exception 异常
     */
    public Map<String, Object> toMap(String desKey) throws Exception {
        Rsaencrypt rsaencrypt = new Rsaencrypt();
        Map<String, Object> paramMap = Maps.newHashMap();
        // 基本信息
        paramMap.put("mchid", mchid);
        paramMap.put("name", name);
        paramMap.put("province", province);
        paramMap.put("city", city);
        paramMap.put("area", area);
        paramMap.put("address", address);
        paramMap.put("legelname", legelname);
        paramMap.put("legelcertno", rsaencrypt.java_openssl_encrypt(legelcertno, desKey));
        paramMap.put("email", email);
        paramMap.put("phone", phone);

        // 收款卡信息
        paramMap.put("bankno", bankno);
        paramMap.put("branchno", branchno);
        paramMap.put("cardno", rsaencrypt.java_openssl_encrypt(cardno, desKey));
        paramMap.put("ecardno", rsaencrypt.java_openssl_encrypt(ecardno, desKey));
        paramMap.put("payname", rsaencrypt.java_openssl_encrypt(payname, desKey));
        paramMap.put("payphone", payphone);
        paramMap.put("cardprovince", cardprovince);
        paramMap.put("cardcity", cardcity);
        paramMap.put("cardarea", cardarea);

        paramMap.put("type", type);
        paramMap.put("certtype", certtype);
        paramMap.put("certno", rsaencrypt.java_openssl_encrypt(certno, desKey));

        // 证件图片地址
        paramMap.put("buslicpic", buslicpic);
        paramMap.put("legfrontpic", legfrontpic);
        paramMap.put("legbackpic", legbackpic);
        paramMap.put("handpic", handpic);
        paramMap.put("doorpic", doorpic);
        paramMap.put("accopenpic", accopenpic);
        paramMap.put("cashierpic", cashierpic);

        paramMap.put("remark", remark);

        // 费率
        if (!StringUtils.isEmpty(rate)) {
            Map<String, Object> sub = Maps.newHashMap();
            sub.put("rate", rate);
            sub.put("fee", 0);
            Map<String, Object> rateMap = new HashMap<>();
            rateMap.put("bnzfbQR", sub);

            Map<String, Object> sub2 = Maps.newHashMap();
            sub2.put("rate", rate);
            sub2.put("fee", 0);
            rateMap.put("bnwxQR", sub2);

            Map<String, Object> sub3 = Maps.newHashMap();
            sub3.put("rate", rate);
            sub3.put("fee", 0);
            rateMap.put("bnzfbFix", sub3);

            Map<String, Object> sub4 = Maps.newHashMap();
            sub4.put("rate", rate);
            sub4.put("fee", 0);
            rateMap.put("bnwxFix", sub4);

            paramMap.put("channelinfo", JSONObject.toJSONString(rateMap));
        }

        return paramMap;
    }

    /**
     * 检查属性是否有为空的
     *
     * @throws Exception 异常
     */
    public void checkPropertiesEmpay() throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(mchid), "代理商编号不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(name), "商户名称不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(province), "省编号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(city), "市编号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(area), "区县编号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(address), "商户所在详细地址不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(legelname), "商户法人姓名不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(legelcertno), "商户法人身份证号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(email), "商户邮件不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(phone), "商户客服电话不能为空");


        Preconditions.checkArgument(!StringUtils.isEmpty(bankno), "收款卡对应的银行代码不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(branchno), "收款卡对应的银行支行代码不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(cardno), "收款卡号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(ecardno), "电子收款卡号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(payname), "收款人姓名不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(payphone), "收款卡对应的手机号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(cardprovince), "收款卡所在省份不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(cardcity), "收款卡所在城市不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(cardarea), "收款卡所在区县不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(certno), "商户证件号不能为空");


        Preconditions.checkArgument(!StringUtils.isEmpty(buslicpic), "商户营业执照照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(legfrontpic), "商户身份证正面照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(handpic), "商户手持身份证照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(legbackpic), "商户身份证反面照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(doorpic), "商户门头照照片不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(rate), "商户通道费率不能为空");
    }

    /**
     * 补充属性值
     *
     * @param bankCardInfo
     * @param merchantName
     * @param province
     * @param city
     * @param area
     * @param address
     * @param doorpic
     * @param pname
     * @param cname
     * @param aname
     * @param phone
     * @param email
     * @param remark
     * @return
     */
    public static RuiShengUserInfo supplementParams(BankCardInfo bankCardInfo, String merchantName, String province, String city, String area, String address,
                                                    String doorpic, String pname, String cname, String aname, String phone,
                                                    String email, String remark) {
        RuiShengUserInfo userInfo = new RuiShengUserInfo();
        userInfo.setMchid(bankCardInfo.getMchId());

        userInfo.setPayname(bankCardInfo.getPayName());
        userInfo.setLegelname(bankCardInfo.getPayName());
        userInfo.setLegelcertno(bankCardInfo.getLegelCertNo());
        userInfo.setCertno(bankCardInfo.getCertNo());
        userInfo.setPayphone(bankCardInfo.getPayPhone());

        userInfo.setEcardno(bankCardInfo.getEcardNo());
        userInfo.setCardno(bankCardInfo.getCardNo());

        userInfo.setBankno(bankCardInfo.getBankNo());
        userInfo.setBankname(bankCardInfo.getBankName());
        userInfo.setBranchno(bankCardInfo.getBranchNo());
        userInfo.setBranchname(bankCardInfo.getBranchName());

        userInfo.setCardprovince(bankCardInfo.getCardProvince());
        userInfo.setCardprovincename(bankCardInfo.getCardProvinceName());
        userInfo.setCardcity(bankCardInfo.getCardCity());
        userInfo.setCardcityname(bankCardInfo.getCardCityName());
        userInfo.setCardarea(bankCardInfo.getCardArea());
        userInfo.setCardareaname(bankCardInfo.getCardAreaName());

        userInfo.setBuslicpic(bankCardInfo.getBuslicPic());
        userInfo.setLegfrontpic(bankCardInfo.getLegFrontPic());
        userInfo.setLegbackpic(bankCardInfo.getLegBackPic());
        userInfo.setHandpic(bankCardInfo.getHandPic());

        userInfo.setName(merchantName);
        userInfo.setProvince(province);
        userInfo.setCity(city);
        userInfo.setArea(area);
        userInfo.setAddress(address);
        userInfo.setEmail(email);
        userInfo.setPhone(phone);

        userInfo.setType("1");
        userInfo.setCerttype("1");
        userInfo.setDoorpic(doorpic);

        userInfo.setRate("31");
        userInfo.setRemark(remark);

        userInfo.setProvincename(pname);
        userInfo.setCityname(cname);
        userInfo.setAreaname(aname);

        return userInfo;
    }

    @Override
    public String toString() {
        return "RuiShengUserInfo{" +
                "mchid='" + mchid + '\'' +
                ", submchid='" + submchid + '\'' +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", legelname='" + legelname + '\'' +
                ", legelcertno='" + legelcertno + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", bankno='" + bankno + '\'' +
                ", branchno='" + branchno + '\'' +
                ", cardno='" + cardno + '\'' +
                ", ecardno='" + ecardno + '\'' +
                ", payname='" + payname + '\'' +
                ", payphone='" + payphone + '\'' +
                ", cardprovince='" + cardprovince + '\'' +
                ", cardcity='" + cardcity + '\'' +
                ", cardarea='" + cardarea + '\'' +
                ", type='" + type + '\'' +
                ", certtype='" + certtype + '\'' +
                ", certno='" + certno + '\'' +
                ", buslicpic='" + buslicpic + '\'' +
                ", legfrontpic='" + legfrontpic + '\'' +
                ", handpic='" + handpic + '\'' +
                ", legbackpic='" + legbackpic + '\'' +
                ", doorpic='" + doorpic + '\'' +
                ", accopenpic='" + accopenpic + '\'' +
                ", cashierpic='" + cashierpic + '\'' +
                ", rate='" + rate + '\'' +
                ", fee='" + fee + '\'' +
                ", provincename='" + provincename + '\'' +
                ", cityname='" + cityname + '\'' +
                ", areaname='" + areaname + '\'' +
                ", cardprovincename='" + cardprovincename + '\'' +
                ", cardcityname='" + cardcityname + '\'' +
                ", cardareaname='" + cardareaname + '\'' +
                ", bankname='" + bankname + '\'' +
                ", branchname='" + branchname + '\'' +
                '}';
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSubmchid() {
        return submchid;
    }

    public void setSubmchid(String submchid) {
        this.submchid = submchid;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getCardprovincename() {
        return cardprovincename;
    }

    public void setCardprovincename(String cardprovincename) {
        this.cardprovincename = cardprovincename;
    }

    public String getCardcityname() {
        return cardcityname;
    }

    public void setCardcityname(String cardcityname) {
        this.cardcityname = cardcityname;
    }

    public String getCardareaname() {
        return cardareaname;
    }

    public void setCardareaname(String cardareaname) {
        this.cardareaname = cardareaname;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
