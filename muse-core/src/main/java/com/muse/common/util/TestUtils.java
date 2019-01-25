package com.muse.common.util;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @program: muse-pay
 * @description: 测试工具类
 * @author: Vincent
 * @create: 2018-10-06 16:14
 **/
@Service("testUtils")
public class TestUtils {

    public String getRandom() {
        Random random = new Random();
        return String.valueOf(random.nextDouble());
    }
}
