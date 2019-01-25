package com.muse.pay.entity;

import com.muse.common.entity.BaseEntityInfo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 10 33
 **/
public class UserInfo extends BaseEntityInfo {

    public static final String REDIS_USER_KEY_PREFIX = "USER:KEY:";

    @NotEmpty(message = "昵称不能为空")
    @Length(min = 1, max = 10, message = "昵称长度不能超过10个字符")
    private String userName;

    @NotEmpty(message = "确认密码不能为空")
    @Length(min = 6, max = 12, message = "密码长度不能少于6位，大于12位")
    private String password;

    @Min(1)
    private int age;

    private String sex;

    private String address;

    @Email(message = "邮箱格式不正确")
    private String email;

    private BigDecimal amount;

    private BigDecimal frozenAmount;

    private String picUrl;

    public UserInfo() {}

    /**
     * 构造方法
     * @param email
     */
    public UserInfo(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                ", frozenAmount=" + frozenAmount +
                ", picUrl='" + picUrl + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
