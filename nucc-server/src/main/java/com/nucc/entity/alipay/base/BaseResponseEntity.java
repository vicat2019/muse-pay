package com.nucc.entity.alipay.base;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 响应基础类
 * @author: Vincent
 * @create: 2018-11-26 14:25
 **/
public class BaseResponseEntity implements Serializable {
    private Logger log = LoggerFactory.getLogger("BaseResponseEntity");

    //String 是 网关返回码 40004
    protected String code;

    // String 是 网关返回码描述 Business Failed
    protected String msg;

    // String 否 网关明细返回码 isv.invalid-signature
    protected String sub_code;

    // String 否 网关明细返回码描述 交易已被支付sign
    protected String sub_msg;

    protected String sign;

    private static final String SUCCESS_CODE = "10000";

    private static final String ERROR_CODE = "99999";

    /**
     * 生成异常返回对象
     *
     * @return
     */
    public static BaseResponseEntity getFailInstance() {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setErrorValue();

        return responseEntity;
    }

    /**
     * 返回特定格式的结果
     *
     * @param method
     * @return
     */
    public Map<String, Object> genResultMap(String method) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(method.replaceAll("\\.", "_") + "_response", this);
        resultMap.put("sign", this.sign);

        log.info("返回内容=" + JSONObject.toJSONString(resultMap));

        return resultMap;
    }

    public void setOkValue() {
        this.code = SUCCESS_CODE;
        this.msg = "操作成功";
        this.sub_code = "isv.invalid-signature";
        this.sub_msg = "操作成功";
    }

    public void setErrorValue() {
        this.code = "1111";
        this.msg = "操作失败";
        this.sub_code = "isv.invalid-signature";
        this.sub_msg = "操作失败";
    }

    /**
     * 返回异常信息
     *
     * @param message
     * @return
     */
    public static BaseResponseEntity getErrResult(String message) {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setCode(ERROR_CODE);
        responseEntity.setMsg(message);
        return responseEntity;
    }

    /**
     * 返回异常信息
     *
     * @return
     */
    public static BaseResponseEntity getErrResult() {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setCode(ERROR_CODE);
        responseEntity.setMsg("操作异常");
        return responseEntity;
    }

    /**
     * 返回成功信息
     *
     * @param message
     * @return
     */
    public static BaseResponseEntity getSuccessResult(String message) {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setCode(SUCCESS_CODE);
        responseEntity.setMsg(message);
        return responseEntity;
    }

    /**
     * 返回信息
     *
     * @param code
     * @param message
     * @return
     */
    public static BaseResponseEntity getResult(String code, String message) {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setCode(code);
        responseEntity.setMsg(message);
        return responseEntity;
    }

    /**
     * 返回成功信息
     *
     * @return
     */
    public static BaseResponseEntity getSuccessResult() {
        BaseResponseEntity responseEntity = new BaseResponseEntity();
        responseEntity.setCode(SUCCESS_CODE);
        responseEntity.setMsg("操作成功");
        return responseEntity;
    }

    @Override
    public String toString() {
        return "BaseResponseEntity{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", sub_code='" + sub_code + '\'' +
                ", sub_msg='" + sub_msg + '\'' +
                '}';
    }

    /**
     * 是否正确
     *
     * @return
     */
    public boolean isOk() {
        return SUCCESS_CODE.equals(this.code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
