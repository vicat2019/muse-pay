package com.nucc.entity.weixin;

import java.io.Serializable;

/**
 * @program: muse-pay
 * @description: 微信响应基类
 * @author: Vincent
 * @create: 2018-11-30 14:42
 **/
public class WXBaseResponse implements Serializable {

    // 成功
    public static final String RESULT_SUCCESS = "SUCCESS";
    // 失败
    public static final String RESULT_FAIL = "FAIL";

    // 返回状态码
    protected String return_code;
    // 返回信息
    protected String return_msg;

    // 结果状态参数
    private String result_code;
    private String err_code;
    private String err_code_des;


    protected void setSuccessValue() {
        this.return_code = RESULT_SUCCESS;
        this.return_msg = "操作成功";
        this.result_code = RESULT_SUCCESS;
    }

    protected void setSuccessValue(String msg) {
        this.return_code = RESULT_SUCCESS;
        this.return_msg = msg;
        this.result_code = RESULT_SUCCESS;
    }

    protected void setErrValue() {
        this.return_code = RESULT_FAIL;
        this.return_msg = "操作异常";
        this.result_code = RESULT_FAIL;
    }

    protected void setErrValue(String msg) {
        this.return_code = RESULT_FAIL;
        this.return_msg = msg;
        this.result_code = RESULT_FAIL;
    }

    public static WXBaseResponse getErrResult() {
        WXBaseResponse response = new WXBaseResponse();
        response.return_code = RESULT_FAIL;
        response.return_msg = "操作异常";
        response.result_code = RESULT_FAIL;

        return response;
    }

    public static WXBaseResponse getErrResult(String msg) {
        WXBaseResponse response = new WXBaseResponse();
        response.return_code = RESULT_FAIL;
        response.return_msg = msg;
        response.result_code = RESULT_FAIL;

        return response;
    }

    public static WXBaseResponse getSuccessResult() {
        WXBaseResponse response = new WXBaseResponse();
        response.return_code = RESULT_SUCCESS;
        response.return_msg = "操作成功";
        response.result_code = RESULT_SUCCESS;

        return response;
    }

    public static WXBaseResponse getSuccessResult(String msg) {
        WXBaseResponse response = new WXBaseResponse();
        response.return_code = RESULT_SUCCESS;
        response.return_msg = msg;
        response.result_code = RESULT_SUCCESS;

        return response;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }
}
