package com.merchant.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 路径类型枚举类
 * @author: Vincent
 * @create: 2019-03-06 14:03
 **/
public enum FilePathTypeEnum {

    EXCEL_FILE("Excel文件"),
    DOOR_PIC_FOLDER("门头照目录");

    /**
     * 描述
     */
    private String desc;

    FilePathTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static FilePathTypeEnum getEnum(String enumName) {
        FilePathTypeEnum resultEnum = null;
        FilePathTypeEnum[] enumAry = FilePathTypeEnum.values();
        for (int i = 0; i < enumAry.length; i++) {
            if (enumAry[i].name().equals(enumName)) {
                resultEnum = enumAry[i];
                break;
            }
        }
        return resultEnum;
    }

    public static Map<String, Map<String, Object>> toMap() {
        FilePathTypeEnum[] ary = FilePathTypeEnum.values();
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
        FilePathTypeEnum[] ary = FilePathTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("desc", ary[i].getDesc());
            list.add(map);
        }
        return list;
    }


}
