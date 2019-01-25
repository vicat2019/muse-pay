package com.muse.pay.util;

import com.muse.common.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <b>功能说明:商户API工具类 </b>
 *
 * @author Peter <a href="http://www.roncoo.net">广州市领课网络科技有限公司(www.roncoo.net)</a>
 */
public class MerchantApiUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MerchantApiUtil.class);

    /**
     * 获取参数签名
     *
     * @param paramMap  签名参数
     * @param paySecret 签名密钥
     * @return
     */
    public static String getSign(Map<String, Object> paramMap, String paySecret) {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        if (smap.get("sign") != null) {
            smap.remove("sign");
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
        String argPreSign = stringBuffer.append("&paySecret=").append(paySecret).toString();
        LOG.info("待签名数据:" + argPreSign);
        return MD5Util.encode(argPreSign).toUpperCase();
    }

    /**
     * 获取参数拼接串
     *
     * @param paramMap
     * @return
     */
    public static String getParamStr(Map<String, Object> paramMap) {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

        return stringBuffer.toString();
    }

    /**
     * 获取参数拼接串
     *
     * @param paramMap
     * @return
     */
    public static String getParamStr2(Map<String, String> paramMap) {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                stringBuffer.append(m.getKey()).append("=").append(value).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

        return stringBuffer.toString();
    }

    /**
     * 获取参数拼接串
     *
     * @param paramMap
     * @return
     */
    public static String getParamStrUrlEncode(Map<String, Object> paramMap) {
        SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, Object> m : smap.entrySet()) {
            Object value = m.getValue();
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                String key = m.getKey();
                String keyValue = null;
                try {
                    keyValue = java.net.URLEncoder.encode(value.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LOG.error("UrlEncodeException:", e);
                }
                stringBuffer.append(key).append("=").append(keyValue).append("&");
            }
        }
        stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

        return stringBuffer.toString();
    }

    /**
     * 验证商户签名
     *
     * @param paramMap  签名参数
     * @param paySecret 签名私钥
     * @param signStr   原始签名密文
     * @return
     */
    public static boolean isRightSign(Map<String, Object> paramMap, String paySecret, String signStr) {

        if (StringUtils.isBlank(signStr)) {
            return false;
        }

        String sign = getSign(paramMap, paySecret);
        LOG.info("加密生成的sign[{}]", sign);
        if (signStr.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }


}
