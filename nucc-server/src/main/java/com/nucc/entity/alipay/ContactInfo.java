package com.nucc.entity.alipay;

import com.muse.common.entity.BaseEntityInfo;
import com.muse.common.entity.ResultData;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;

/**
 * @program: muse-pay
 * @description: 联系信息类
 * @author: Vincent
 * @create: 2018-11-27 09:19
 **/
public class ContactInfo extends BaseEntityInfo {

    private String sub_merchant_id;

    // 联系人名字
    private String name;

    // 电话
    private String phone;

    // 手机号
    private String mobile;

    // 电子邮件
    private String email;

    // 商户联系人业务标识枚举
    private String[] tags;

    private String tag;

    // 联系人类型
    private String type;

    // 身份证号
    private String id_card_no;

    /**
     * 校验参数是否合法
     *
     * @return
     */
    public ResultData legalParam() {
        if (StringUtils.isEmpty(sub_merchant_id)) {
            return ResultData.getErrResult("商户编号不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            return ResultData.getErrResult("名称不能为空");
        }
        if (StringUtils.isEmpty(type)) {
            return ResultData.getErrResult("类型不能为空");
        }
        if (tag == null || tag.length() == 0) {
            return ResultData.getErrResult("商户联系人业务标识不能为空");
        }

        return ResultData.getSuccessResult();
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "sub_merchant_id='" + sub_merchant_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", tags=" + Arrays.toString(tags) +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", id_card_no='" + id_card_no + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }
}
