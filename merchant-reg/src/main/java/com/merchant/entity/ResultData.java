package com.merchant.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/22 11 05
 **/
public class ResultData<T> implements Serializable {
    // 状态码
    private int code;
    // 消息
    private String message;
    // 数据
    private T data;
    // 时间戳
    private long timestamp;

    /**
     * 构造方法
     *
     * @param code
     * @param msg
     */
    public ResultData(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    /**
     * 构造方法
     *
     * @param code
     * @param msg
     * @param data
     */
    public ResultData(int code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    /**
     * 是否成功
     *
     * @return boolean
     */
    @Deprecated
    public boolean isOk() {
        return ResultStatusEnum.SUCCESS.getValue() == code;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean whetherOk() {
        return ResultStatusEnum.SUCCESS.getValue() == code;
    }

    /**
     * 结果为空
     *
     * @return boolean
     */
    public boolean resultIsEmpty() {
        // 是否为空
        if (this.data != null) {
            if (this.data instanceof Collection) {
                Collection temp = (Collection) this.data;
                return temp.isEmpty();
            } else if (this.data instanceof Map) {
                Map temp = (Map) this.data;
                return temp.isEmpty();
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 结果不为空
     *
     * @return boolean
     */
    public boolean resultIsNotEmpty() {
        return this.data != null;
    }

    /**
     * 返回成功结果，有数据
     *
     * @param data
     * @return
     */
    public static <T> ResultData<T> getSuccessResult(T data) {
        return new ResultData<T>(ResultStatusEnum.SUCCESS.getValue(), "操作成功", data);
    }

    /**
     * 返回成功结果，没有数据
     *
     * @param <T>
     * @return
     */
    public static <T> ResultData<T> getSuccessResult() {
        return new ResultData<T>(ResultStatusEnum.SUCCESS.getValue(), "操作成功", null);
    }

    /**
     * 返回成功结果，没有数据
     *
     * @param
     * @return
     */
    public static ResultData<String> getSuccessResult(String msg) {
        return new ResultData<String>(ResultStatusEnum.SUCCESS.getValue(), msg, null);
    }

    /**
     * 获取失败的结果对象，有信息
     *
     * @param msg
     * @return
     */
    public static <T> ResultData<T> getErrResult(String msg) {
        return new ResultData<T>(ResultStatusEnum.ERR.getValue(), msg);
    }

    /**
     * 获取失败的结果对象，没有信息
     *
     * @return
     */
    public static <T> ResultData<T> getErrResult() {
        return new ResultData<T>(ResultStatusEnum.ERR.getValue(), "操作失败");
    }

    /**
     * 获取结果对象
     *
     * @param code
     * @param msg
     * @return
     */
    public static <T> ResultData<T> getDefaultResult(int code, String msg) {
        return new ResultData<T>(code, msg);
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
