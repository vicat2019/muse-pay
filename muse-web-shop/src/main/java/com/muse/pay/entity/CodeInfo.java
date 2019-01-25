package com.muse.pay.entity;


import com.muse.common.entity.BaseEntityInfo;
import com.muse.pay.entity.common.VerifyCodeTypeEnum;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CodeInfo extends BaseEntityInfo {

    public static final int CODE_MAX_COUNT = 10;
    public static final String CODE_STATUS_UNUSED = "1";
    public static final String CODE_STATUS_USED = "2";

    private String code;

    private String email;

    private int count;

    private LocalDateTime expireTime;

    private String codeType;

    /**
     * 默认构造方法
     */
    public CodeInfo() {
    }

    /**
     * 构造方法
     *
     * @param code     验证码
     * @param email    邮箱地址
     * @param codeType 验证码类型
     */
    public CodeInfo(String code, String email, String codeType) {
        this.code = code;
        this.email = email;
        this.codeType = codeType;
    }

    /**
     * 构造方法
     *
     * @param id     ID标识
     * @param status 状态
     */
    public CodeInfo(int id, int status) {
        this.id = id;
        this.status = String.valueOf(status);
    }

    /**
     * 构造方法
     *
     * @param email    邮箱
     * @param codeType 验证码类型
     */
    public CodeInfo(String email, String codeType) {
        this.email = email;
        this.codeType = codeType;
    }

    /**
     * 验证码是否过期
     *
     * @return boolean
     */
    public boolean whetherExpire() {
        if (this.expireTime == null) {
            return false;
        }
        return this.expireTime.isBefore(LocalDateTime.now());
    }

    /**
     * 是否超过最大次数
     *
     * @return boolean
     */
    public boolean whetherOverCount() {
        return this.count >= CODE_MAX_COUNT;
    }

    /**
     * 是否已经使用过
     *
     * @return boolean
     */
    public boolean whetherUsed() {
        return this.status.equals(String.valueOf(CODE_STATUS_USED));
    }

    /**
     * 根据验证码类型返回验证码内容
     * @param codeType 验证码类型
     * @param code 验证码
     * @return Map
     */
    public static Map<String, String> getCodeContentByType(String codeType, String code) {
        Map<String, String> contentMap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());

        // 注册验证码
        if (VerifyCodeTypeEnum.REGISTER.name().equals(codeType)) {
            contentMap.put("title", "Pay注册验证码");
            contentMap.put("content", "您的注册验证码：" + code + "，有效期为30分钟，" + timestamp + "。");
        } else if (VerifyCodeTypeEnum.CHANGE_PASSWORD.name().equals(codeType)) {
            contentMap.put("title", "Pay修改密码验证码");
            contentMap.put("content", "您的修改密码的验证码：" + code + "，有效期为30分钟，" + timestamp + "。");
        } else {
            contentMap.put("title", "Pay验证码");
            contentMap.put("content", "您的验证码：" + code + "，有效期为30分钟，" + timestamp + "。");
        }

        return contentMap;
    }










    @Override
    public String toString() {
        return "CodeInfo{" +
                "code='" + code + '\'' +
                ", email='" + email + '\'' +
                ", count=" + count +
                ", expireTime=" + expireTime +
                ", status=" + status +
                ", codeType='" + codeType + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", version=" + version +
                ", modifyTime=" + modifyTime +
                ", createTime=" + createTime +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
}