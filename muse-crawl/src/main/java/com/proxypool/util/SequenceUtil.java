package com.proxypool.util;

import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: muse-pay
 * @description: 序列号
 * @author: Vincent
 * @create: 2018-12-17 19:13
 **/
public class SequenceUtil {

    private static final Map<String, Integer> valueMap = new HashMap<>();
    private static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    static {
        for (int i = 0; i < b.length; i++) {
            valueMap.put(String.valueOf(b[i]), i);
        }
    }

    public static void main(String[] args) {
/*        int count = 0;
        for (int i = 1679616; i < 1679716; i++) {
            System.out.println(numToSequence(i));
            count++;
        }
        System.out.println(count);*/

        System.out.println(sequenceToNum("10000"));
    }


    public static String numToSequence(int num) {
        int wei = b.length;
        if (wei == 0) {
            return "";
        }
        String targetStr = "";
        while (num != 0) {
            targetStr = b[num % wei] + targetStr;
            num = num / wei;
        }
        String[] format = new String[]{"0", "0", "0", "0", "0"};
        int count = 0;
        for (int i = targetStr.length() - 1; i >= 0; i--) {
            int index = format.length - 1 - count;
            if (index < format.length) {
                format[index] = String.valueOf(targetStr.charAt(i));
                count++;
            }
        }
        return String.join("", format);
    }


    public static int sequenceToNum(String sequence) {
        if (StringUtils.isEmpty(sequence)) return 0;
        if (sequence.length() > 5) {
            sequence = sequence.substring(0, 5);
        }

        int step = 36;
        int count = 1;
        int value = 0;

        for (int i = sequence.length() - 1; i >= 0; i--) {
            String tag = String.valueOf(sequence.charAt(i));
            int temp = valueMap.get(tag);
            if (count == 1) {
                value = temp;
            } else {
                value += Math.pow(step, count - 1) * temp;
            }
            count++;
        }
        return value;
    }


}
