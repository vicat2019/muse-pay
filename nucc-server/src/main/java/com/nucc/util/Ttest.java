package com.nucc.util;

import com.alibaba.fastjson.JSON;
import com.nucc.entity.weixin.WXBaseResponse;

/**
 * @program: muse-pay
 * @description: 测试
 * @author: Vincent
 * @create: 2018-11-28 09:54
 **/
public class Ttest {

    public static void main(String[] args) {
        WXBaseResponse entity = WXBaseResponse.getErrResult("操作异常");
        try {
            System.out.println(JSON.toJSONString(entity));
            System.out.println(XMLUtils.objToXml(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
