package com.muse.pay.entity.vo;

import com.muse.pay.entity.UserInfo;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/24 11 48
 **/
@Valid
public class UserInfoVO {

    private UserInfo userInfo;

    @NotEmpty(message = "确认密码不能为空")
    @Length(min = 6, max = 12, message = "密码长度不能少于6位，大于12位")
    private String confirmPassword;

    @NotEmpty(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码长度为6位")
    private String code;

    private String codeType;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}
