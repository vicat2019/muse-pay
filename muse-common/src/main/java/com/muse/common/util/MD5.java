package com.muse.common.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;
import java.security.SignatureException;
import java.util.*;

/**
 * 功能：MD5签名
 * 版本：3.3
 * 修改日期：2012-08-17
 */
public class MD5 {
    private static Logger log = LoggerFactory.getLogger("MD5");

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
    }

    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 生成签名
     *
     * @param map 参数
     * @param key KEY值
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String sign(Map map, String key) throws UnsupportedEncodingException {
        Set<Map.Entry<String, String>> entry1 = sortedmap(map);
        Iterator<Map.Entry<String, String>> it = entry1.iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            if ((null != v) && !"".equals(v) && !"sign".equals(k) && !"key".equals(k) && !"spbill_create_ip".equals(k)) {
                sf.append(k + "=" + v + "&");
            }
        }
        sf.append("key=" + key);
        String stringA = sf.toString();
        log.info("生成签名的字符串=" + stringA + ", key=" + key);
        String sign = getFileDigest(stringA.getBytes("UTF-8"));
        log.info("生成签名sign=" + sign);

        return sign;

    }

    /**
     * 参数名ASCII码从小到大排序
     *
     * @param map 参数MAP
     * @return
     */
    public static Set sortedmap(Map map) {
        SortedMap<String, String> sort = new TreeMap<String, String>(map);
        Set<Map.Entry<String, String>> entry1 = sort.entrySet();
        return entry1;
    }

    /**
     * 生成文件摘要
     *
     * @param input 内容
     * @return
     */
    public static String getFileDigest(byte[] input) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input);
            return byteToHex(md5.digest());
        } catch (Exception ex) {
            throw new RuntimeException("MD5 getFileDigest: " + ex.toString());
        }
    }

    /**
     * 二进制转16进制
     *
     * @param bytes 内容
     * @return String
     */
    private static String byteToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0x00ff);
            if (hexString.length() != 2) {
                sb.append('0').append(hexString);
            } else {
                sb.append(hexString);
            }
        }
        return new String(sb).toUpperCase();
    }


}