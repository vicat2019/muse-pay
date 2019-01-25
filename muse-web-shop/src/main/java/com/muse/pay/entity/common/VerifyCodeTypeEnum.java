package com.muse.pay.entity.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum VerifyCodeTypeEnum {

    REGISTER("注册验证码"),
    CHANGE_PASSWORD("修改密码验证码"),
    FORGET_PASSWORD("忘记密码验证码");

    /**
     * 描述
     */
    private String desc;

    private VerifyCodeTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static VerifyCodeTypeEnum getEnum(String enumName) {
        VerifyCodeTypeEnum resultEnum = null;
        VerifyCodeTypeEnum[] enumAry = VerifyCodeTypeEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].name().equals(enumName)) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

    public static Map<String, Map<String, Object>> toMap() {
        VerifyCodeTypeEnum[] ary = VerifyCodeTypeEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = ary[num].name();
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList() {
        VerifyCodeTypeEnum[] ary = VerifyCodeTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            list.add(map);
        }
        return list;
    }
}
