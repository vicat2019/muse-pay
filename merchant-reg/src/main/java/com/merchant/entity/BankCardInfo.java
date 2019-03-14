package com.merchant.entity;

import com.google.common.base.Preconditions;
import org.thymeleaf.util.StringUtils;

public class BankCardInfo extends BaseEntityInfo {

    private String mchId;

    private String legelName;

    private String legelCertNo;

    private String bankNo;

    private String bankName;

    private String branchNo;

    private String branchName;

    private String cardNo;

    private String ecardNo;

    private String payName;

    private String payPhone;

    private String cardProvince;

    private String cardProvinceName;

    private String cardCity;

    private String cardCityName;

    private String cardArea;

    private String cardAreaName;

    private String certNo;

    private String buslicPic;

    private String legFrontPic;

    private String legBackPic;

    private String handPic;

    private String doorPic;

    private String channelNo;

    private Integer registerCount;

    private Integer registerMaxCount;

    private String remark;


    /**
     * @param mchId
     * @param name
     * @param certNo
     * @param payPhone
     * @param cardNo
     * @param ecardNo
     * @param bankNo
     * @param bankName
     * @param branchNo
     * @param branchName
     * @param cardProvinceCode
     * @param cardProvinceName
     * @param cardCityCode
     * @param cardCityName
     * @param cardAreaCode
     * @param cardAreaName
     * @param buslicPic
     * @param frontPic
     * @param backPic
     * @param handPic
     * @return BankCardInfo
     */
    public static BankCardInfo getInstance(String mchId, String name, String certNo, String payPhone, String cardNo, String ecardNo,
                                           String bankNo, String bankName, String branchNo, String branchName, String cardProvinceCode,
                                           String cardProvinceName, String cardCityCode, String cardCityName, String cardAreaCode, String cardAreaName,
                                           String buslicPic, String frontPic, String backPic, String handPic) {
        BankCardInfo userInfo = new BankCardInfo();
        userInfo.setMchId(mchId);

        userInfo.setPayName(name);
        userInfo.setLegelName(name);
        userInfo.setLegelCertNo(certNo);
        userInfo.setCertNo(certNo);
        userInfo.setPayPhone(payPhone);

        userInfo.setEcardNo(ecardNo);
        userInfo.setCardNo(cardNo);

        userInfo.setBankNo(bankNo);
        userInfo.setBankName(bankName);
        userInfo.setBranchNo(branchNo);
        userInfo.setBranchName(branchName);

        userInfo.setCardProvince(cardProvinceCode);
        userInfo.setCardProvinceName(cardProvinceName);
        userInfo.setCardCity(cardCityCode);
        userInfo.setCardCityName(cardCityName);
        userInfo.setCardArea(cardAreaCode);
        userInfo.setCardAreaName(cardAreaName);

        userInfo.setBuslicPic(buslicPic);
        userInfo.setLegFrontPic(frontPic);
        userInfo.setLegBackPic(backPic);
        userInfo.setHandPic(handPic);

        return userInfo;
    }

    /**
     * 检查属性是否有为空的
     *
     * @throws Exception 异常
     */
    public void initCheckParams() throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(this.mchId), "代理商编号不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(this.legelName), "商户法人姓名不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.payName), "收款人姓名不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.payPhone), "收款卡对应的手机号不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(this.legelCertNo), "商户法人身份证号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.certNo), "商户身份证号不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(this.bankNo), "收款卡对应的银行代码不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.branchNo), "收款卡对应的银行支行代码不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(this.cardNo), "收款卡号不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.ecardNo), "电子收款卡号不能为空");

        Preconditions.checkArgument(!StringUtils.isEmpty(this.cardProvince), "收款卡所在省份不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.cardCity), "收款卡所在城市不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.cardArea), "收款卡所在区县不能为空");
    }

    /**
     * 检查证件照地址
     *
     * @throws Exception
     */
    public void checkCertPic() throws Exception {
        Preconditions.checkArgument(!StringUtils.isEmpty(this.buslicPic), "商户营业执照照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.legFrontPic), "商户身份证正面照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.legBackPic), "商户身份证反面照片不能为空");
        Preconditions.checkArgument(!StringUtils.isEmpty(this.handPic), "商户手持身份证照片不能为空");
    }

    /**
     * 还需要进件的数量
     * @return
     */
    public int needToRegisterNum() {
        return (this.getRegisterMaxCount() - this.getRegisterCount());
    }

    /**
     * 是否需要进件
     * @return
     */
    public boolean isNeedToRegister() {
        return (needToRegisterNum() > 0);
    }

    /**
     * 进件数加1
     */
    public void increaseRegisterCount() {
        this.registerCount = registerCount + 1;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        String[] hashContents = new String[]{this.payName, this.certNo, this.cardNo, this.ecardNo};
        for (String item : hashContents) {
            if (!StringUtils.isEmpty(item)) {
                hashCode += 31 * hashCode + item.hashCode();
            }
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BankCardInfo)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        BankCardInfo item = (BankCardInfo) obj;

        if (this.payName.equals(item.getPayName()) && this.certNo.equals(item.getCertNo()) && this.cardNo.equals(item.getCardNo())
                && this.ecardNo.equals(item.getEcardNo())) {
            return true;
        } else {
            return false;
        }
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getLegelName() {
        return legelName;
    }

    public void setLegelName(String legelName) {
        this.legelName = legelName == null ? null : legelName.trim();
    }

    public String getLegelCertNo() {
        return legelCertNo;
    }

    public void setLegelCertNo(String legelCertNo) {
        this.legelCertNo = legelCertNo == null ? null : legelCertNo.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo == null ? null : branchNo.trim();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? null : branchName.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getEcardNo() {
        return ecardNo;
    }

    public void setEcardNo(String ecardNo) {
        this.ecardNo = ecardNo == null ? null : ecardNo.trim();
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName == null ? null : payName.trim();
    }

    public String getCardProvince() {
        return cardProvince;
    }

    public void setCardProvince(String cardProvince) {
        this.cardProvince = cardProvince == null ? null : cardProvince.trim();
    }

    public String getCardProvinceName() {
        return cardProvinceName;
    }

    public void setCardProvinceName(String cardProvinceName) {
        this.cardProvinceName = cardProvinceName == null ? null : cardProvinceName.trim();
    }

    public String getCardCity() {
        return cardCity;
    }

    public void setCardCity(String cardCity) {
        this.cardCity = cardCity == null ? null : cardCity.trim();
    }

    public String getCardCityName() {
        return cardCityName;
    }

    public void setCardCityName(String cardCityName) {
        this.cardCityName = cardCityName == null ? null : cardCityName.trim();
    }

    public String getCardArea() {
        return cardArea;
    }

    public void setCardArea(String cardArea) {
        this.cardArea = cardArea == null ? null : cardArea.trim();
    }

    public String getCardAreaName() {
        return cardAreaName;
    }

    public void setCardAreaName(String cardAreaName) {
        this.cardAreaName = cardAreaName == null ? null : cardAreaName.trim();
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo == null ? null : certNo.trim();
    }

    public String getBuslicPic() {
        return buslicPic;
    }

    public void setBuslicPic(String buslicPic) {
        this.buslicPic = buslicPic == null ? null : buslicPic.trim();
    }

    public String getLegFrontPic() {
        return legFrontPic;
    }

    public void setLegFrontPic(String legFrontPic) {
        this.legFrontPic = legFrontPic == null ? null : legFrontPic.trim();
    }

    public String getHandPic() {
        return handPic;
    }

    public void setHandPic(String handPic) {
        this.handPic = handPic == null ? null : handPic.trim();
    }

    public String getDoorPic() {
        return doorPic;
    }

    public void setDoorPic(String doorPic) {
        this.doorPic = doorPic == null ? null : doorPic.trim();
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo == null ? null : channelNo.trim();
    }

    public Integer getRegisterCount() {
        return registerCount;
    }

    public void setRegisterCount(Integer registerCount) {
        this.registerCount = registerCount;
    }

    public Integer getRegisterMaxCount() {
        return registerMaxCount;
    }

    public void setRegisterMaxCount(Integer registerMaxCount) {
        this.registerMaxCount = registerMaxCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getPayPhone() {
        return payPhone;
    }

    public void setPayPhone(String payPhone) {
        this.payPhone = payPhone;
    }

    public String getLegBackPic() {
        return legBackPic;
    }

    public void setLegBackPic(String legBackPic) {
        this.legBackPic = legBackPic;
    }
}