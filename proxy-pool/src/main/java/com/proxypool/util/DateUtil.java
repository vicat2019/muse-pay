package com.proxypool.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: muse-pay
 * @description: 日期工具
 * @author: Vincent
 * @create: 2018-10-26 10:27
 **/
public class DateUtil {


    /**
     * 获取今天的日期
     * @return LocalDateTime
     */
    public static LocalDateTime getToday() {
        Date now = new Date();
        Instant instant = now.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime nowLocalDateTime = instant.atZone(zoneId).toLocalDateTime();

        return nowLocalDateTime;
    }

    /**
     * 匹配IP地址
     * @param ip IP地址
     * @return boolean
     */
    public static boolean isLegalIp(String ip) {
        Pattern pattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }



































}
