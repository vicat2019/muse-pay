package com.proxypool.util;

import com.proxypool.entry.RecruitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
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
    private static Logger log = LoggerFactory.getLogger("TextUtils");

    // 数字、字母混合字符串
    private static String content = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // 数字字符串
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

    /**
     * 从标题中获取图书名称
     *
     * @param title 标题
     * @return String
     */
    public static String getNameFromTitle(String title) {
        String name = "";
        if (!StringUtils.isEmpty(title)) {
            Pattern pattern = Pattern.compile("《(.+)》");
            Matcher matcher = pattern.matcher(title);
            if (matcher.find()) {
                name = matcher.group(1);
            }
        }
        log.info("从字符串中获取名称, title=" + title + ", name=" + name);
        return name;
    }

    public static String getAuthorFromTitle(String title) {
        String author = "";
        if (!StringUtils.isEmpty(title)) {
            // 去掉图书名称
            author = title.replaceAll("《.+》", "");
            // 去掉国家之类
            author = author.replaceAll("\\[.+\\]", "");
            // 去掉"（作者）"
            author = author.replaceAll("（作者）", "");
            // 去掉"作者："
            author = author.replaceAll("作者：", "");
            author = author.replaceAll("【.+】", "");
            // 去掉epub+mobi+azw3
            author = author.replaceAll("[(epub)|(mobi)|(azw3)]", "");
            // 去掉 +
            author = author.replaceAll("\\+|_", "");
            // 去掉空格
            author = author.replaceAll("\\s+", "");
        }
        log.info("从字符串中获取作者, title=" + title + ", author=" + author);
        return author;
    }

    /**
     * trim字符串
     *
     * @param content 字符串
     * @return String
     */
    public static String trim(String content) {
        if (StringUtils.isEmpty(content)) {
            return "";
        }

        return content.trim();
    }

    /**
     * 解析记录中的Salary值
     *
     * @param recruitInfo JL信息
     * @return BigDecimal[]
     */
    public static BigDecimal[] splitSalary(RecruitInfo recruitInfo) {
        // 检查参数是否为空
        if (recruitInfo == null || StringUtils.isEmpty(recruitInfo.getSalary())) {
            return null;
        }
        BigDecimal[] result = new BigDecimal[2];
        String salary = recruitInfo.getSalary().trim();
        salary = salary.replaceAll("以下", "");

        // 千/月 万/月 万/年
        String temp = salary.replaceAll("[\u4e00-\u9fa5]/[\u4e00-\u9fa5]", "");
        String[] numStr = temp.split("-");
        if (numStr.length == 1) {
            String tempStr = numStr[0];
            numStr = new String[2];
            numStr[0] = "0";
            numStr[1] = tempStr;
        }

        for (int i = 0; i < numStr.length; i++) {
            System.out.println(numStr[i]);
            result[i] = new BigDecimal(numStr[i]);
        }
        if (salary.contains("万/年")) {
            result[0] = result[0].divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
            result[1] = result[1].divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
        }
        if (salary.contains("千/月")) {
            result[0] = result[0].multiply(new BigDecimal("0.1"));
            result[1] = result[1].multiply(new BigDecimal("0.1"));
        }

        return result;
    }


    public static void main(String[] args) {
        RecruitInfo recruitInfo = new RecruitInfo();
        recruitInfo.setSalary("1.5-3万/月");

        BigDecimal[] result = splitSalary(recruitInfo);
        if (result != null) {
            for (BigDecimal bigDecimal : result) {
                System.out.println(bigDecimal.toString());
            }
        } else {
            System.out.println("null");
        }
    }


}
