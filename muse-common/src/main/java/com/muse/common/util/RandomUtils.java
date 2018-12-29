package com.muse.common.util;

import java.util.Random;

/**
 * @program: muse-pay
 * @description: 随机数字或者字符串工具类
 * @author: Vincent
 * @create: 2018-11-01 14:07
 **/
public class RandomUtils {


    public static int getRandomNum(int min, int max) {
        if (max == 0) max = 10;
        if (min > max) min = 0;
        int target = 0;
        int count = 0;

        Random random = new Random();
        while ((target == 0 || target < min) && count < 100) {
            target = random.nextInt(max);
            count += 1;
        }

        return target;
    }


    public static void main(String[] args) {
        for (int i=0; i<10; i++) System.out.println(getRandomNum(2, 0));

        for (int i=0; i<10; i++) System.out.println(getRandomNum(10, 15));
    }




}
