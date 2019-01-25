package com.muse.pay.entity.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录状态枚举类
 */
public enum RecordStatusEnum {

    ACTIVE("激活"),
    FROZEN("激活"),
    DELLE("删除");

    /**
     * 描述
     */
    private String desc;

    RecordStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static RecordStatusEnum getEnum(String enumName) {
        RecordStatusEnum resultEnum = null;
        RecordStatusEnum[] enumAry = RecordStatusEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].name().equals(enumName)) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

    public static Map<String, Map<String, Object>> toMap() {
        RecordStatusEnum[] ary = RecordStatusEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<>();
            String key = ary[num].name();
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList() {
        RecordStatusEnum[] ary = RecordStatusEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("desc", ary[i].getDesc());
            list.add(map);
        }
        return list;
    }


}
