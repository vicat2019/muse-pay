package com.proxypool.util;

import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: muse-pay
 * @description: 字符串工具类
 * @author: Vincent
 * @create: 2018-11-26 16:30
 **/
public class TextUtils {

    private static String content = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static String numContent = "0123456789";

    /**
     * 获取指定长度的随机字符串
     *
     * @param length 长度
     * @return String
     */
    public static String getRandomStr(int length) {
        return getRandomBy(content, length);
    }

    /**
     * 获取指定长度的随机数字字符串
     *
     * @param length 长度
     * @return String
     */
    public static String getRandomNum(int length) {
        return getRandomBy(numContent, length);
    }

    /**
     * 生成随机交易好
     *
     * @return
     */
    public static String makeTradeNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + getRandomNum(14);
    }

    private static String getRandomBy(String content, int length) {
        if (StringUtils.isEmpty(content) || length < 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (length > 0) {
            int index = random.nextInt(content.length() - 1);
            sb.append(content.charAt(index));

            length -= 1;
        }
        return sb.toString();
    }

    /**
     * 正则表达式匹配内容
     *
     * @param regex   正则表达式
     * @param content 内容
     * @return 是否匹配
     */
    public static boolean isMatch(String regex, String content) {
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return false;
        }

        return Pattern.matches(regex, content);
    }

    /**
     * 获取匹配的内容
     *
     * @param regex
     * @param content
     * @return
     */
    public static String getMatch(String regex, String content) {
        if (StringUtils.isEmpty(regex) || StringUtils.isEmpty(content)) {
            return "";
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }


}
