package com.muse.common.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @Author: Administrator
 * @Date: 2018 2018/7/25 14 03
 **/
public class DateUtils {


    /**
     * 获取LocalDateTime
     *
     * @param date 时间
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(Date date) {
        if (date == null) {
            date = new Date();
        }
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 获取Date
     *
     * @param localDateTime 时间
     * @return Date
     */
    public static Date getDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();

        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        ZonedDateTime zdt2 = localDateTime.atZone(zoneId);
        return Date.from(zdt2.toInstant());
    }

    /**
     * 获取当前的时间，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


}
