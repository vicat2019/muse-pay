package com.merchant.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.merchant.entity.ResultData;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @program: muse-pay
 * @description: 通用工具
 * @author: Vincent
 * @create: 2019-03-06 14:58
 **/
public class CommonUtils {

    // 生成随机字符串的基础
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // 邮箱后缀
    private static final String EMAIL_POSTFIX = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@163.com,@yeah.com";

    /**
     * 获取随机的字符串
     *
     * @param length 字符串长度
     * @return String
     */
    public static String genRandomStr(int length) {
        String targetStr = "";

        Random random = new Random();
        while (targetStr.length() < length) {
            targetStr += ALPHA.charAt(random.nextInt(ALPHA.length()));
        }
        return targetStr;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @return boolean
     */
    public static boolean collectionIsEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    /**
     * 生成指定范围内的谁技术
     *
     * @param min 最小值
     * @param max 最大值
     * @return int
     */
    public static int getRandomNum(int min, int max) {
        Random random = new Random();
        int second = random.nextInt(max - min) + min;
        return second;
    }

    /**
     * 当前时间是否在有效范围内
     *
     * @return boolean
     */
    public static boolean outOfTimeRange() {
        long currentTimestamp = System.currentTimeMillis();

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int dayOfMonth = now.getDayOfMonth();

        LocalDateTime startTime = LocalDateTime.of(year, month, dayOfMonth, 6, 30);
        long startTimestamp = startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        LocalDateTime endTime = LocalDateTime.of(year, month, dayOfMonth, 23, 10);
        long endTimestamp = endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        return (currentTimestamp < startTimestamp || currentTimestamp > endTimestamp);
    }

    /**
     * 根据名称生成邮箱地址
     * @param emailName 邮箱名称
     * @return String
     */
    public static String getRandomEmail(String emailName) {
        if (StringUtils.isEmpty(emailName)) {
            return "";
        }

        return emailName.trim() + genEmailPostfix();
    }

    /**
     * 随机获取邮箱后缀
     *
     * @return String
     */
    public static String genEmailPostfix() {
        List<String> emailPostFixList = Lists.newArrayList(Splitter.on(",").split(EMAIL_POSTFIX));

        Random random = new Random();
        int index = random.nextInt(50) % emailPostFixList.size();
        return emailPostFixList.get(index);
    }


    /**
     * 解析查询银行返回的内容
     *
     * @param resultData 解析返回结果
     * @return Map
     */
    public static Map<String, String> parseBinNongResult(ResultData resultData) {
        Map<String, String> resultMap = Maps.newHashMap();
        if (resultData.whetherOk()) {
            String content = (String) resultData.getData();
            JSONObject json = JSONObject.parseObject(content);
            JSONArray dataJson = json.getJSONArray("data");

            for (int i = 0; i < dataJson.size(); i++) {
                JSONObject tempJson = (JSONObject) dataJson.get(i);
                resultMap.put((String) tempJson.get("text"), (String) tempJson.get("value"));
            }
        }
        return resultMap;
    }


}
