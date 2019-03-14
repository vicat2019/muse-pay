package com.merchant.util;

import java.util.Random;

/**
 * @program: merchant-register
 * @description: 手机号码生成类
 * @author: Vincent
 * @create: 2019-03-05 14:02
 **/
public class PhoneNumHelper {

    // 手机几号前缀
    private static final String[] PHONE_PREFIX = new String[]{"13", "15", "18"};

    /**
     * 生成手机号码
     *
     * @return String
     */
    public static String genPhoneNum() {
        Random random = new Random();
        double num = random.nextDouble();
        String numStr = String.valueOf(num);

        Random prefixRandom = new Random();
        String phonePrefix = PHONE_PREFIX[prefixRandom.nextInt(10) % 3];

        return phonePrefix + numStr.substring(2, 11);
    }

}
