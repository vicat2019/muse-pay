package com.muse.common.entity;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 11 06
 **/
public enum ResultStatusEnum {
    ERR(1),
    SUCCESS(0);

    private int value;

    ResultStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


}
