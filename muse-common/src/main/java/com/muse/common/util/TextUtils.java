package com.muse.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 字符串相关工具方法
 *
 * @Author: Administrator
 * @Date: 2018 2018/8/10 19 51
 **/
public class TextUtils {
    private static Logger log = LoggerFactory.getLogger("TextUtils");

    /**
     * 生成MUSE订单号
     * @return String
     */
    public static String makeMuseOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStr = sdf.format(new Date());

        return "MUSE" + timeStr + getNumRandomStr(6);
    }

    /**
     * 生成序列号
     * @return String
     */
    public static String makeSerialNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStr = sdf.format(new Date());

        return "SERIAL" + timeStr + getNumRandomStr(6);
    }

    /**
     * 获取随机字母数字组合
     *
     * @param length 字符串长度
     * @return String
     */
    public static String getRandomStr(Integer length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) {
                str += (char) (65 + random.nextInt(26));
            } else {
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }

    /**
     * 获取随机length个随机数字
     *
     * @param length 长度
     * @return String
     */
    public static String getNumRandomStr(int length) {
        String str = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str += random.nextInt(10);
        }
        return str;
    }


    /**
     * 将字符串格式的JSON转换成JSON
     * @param content JSON字符串
     * @return JSONObject
     */
    public static JSONObject fromStreamContext(String content) {
        try {
            String jsonStr = URLDecoder.decode(content, "UTF-8");
            if (jsonStr.length() > 0) {
                jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            }
            return (JSONObject) JSONObject.parse(jsonStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("将字符串内容转换成JSON对象，字符串内容=" + content + "，异常=" + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {

        System.out.println(getRandomStr(10));
        System.out.println(getRandomStr(10));


    }


}
