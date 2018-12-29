package com.muse.pay.util;

import com.muse.common.util.MD5;

import java.util.Map;

/**
 * @Author: Administrator
 * @Date: 2018 2018/8/8 17 00
 **/
public class SignCheckUtil {

    /**
     * 校验KEY
     *
     * @param params 参数
     * @param key    KEY值
     * @return boolean
     */
    public static boolean checkParam(Map params, String key) {
        boolean result = false;
        if (params.containsKey("sign")) {
            String sign = (String) params.get("sign");
            params.remove("sign");
            StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
            SignUtils.buildPayParams(buf, params, false);
            String preStr = buf.toString();
            String signRecieve = MD5.sign(preStr, "&key=" + key, "utf-8");
            result = sign.equalsIgnoreCase(signRecieve);
        }
        return result;
    }

    /**
     * 根据参数生成SIGN
     *
     * @param params 参数
     * @param key    加密KEY
     * @return String
     */
    public static String getSign(Map params, String key) {
        if (params.containsKey("sign")) {
            params.remove("sign");
        }

        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String signRecieve = MD5.sign(preStr, "&key=" + key, "utf-8");
        return signRecieve;
    }
}
