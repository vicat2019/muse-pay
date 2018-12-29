package com.nucc.util;

import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: muse-pay
 * @description: 日期工具类
 * @author: Vincent
 * @create: 2018-12-06 16:14
 **/
public class DateUtils {

    /**
     * 按照给定格式返回当前日期
     *
     * @param format 格式
     * @return String
     */
    public static String getCurrent(String format) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }


}
