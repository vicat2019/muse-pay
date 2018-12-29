package com.rpctest.service.impl;

import com.rpctest.service.HelloService;

/**
 * @program: muse-pay
 * @description: 测试服务类
 * @author: Vincent
 * @create: 2018-11-13 11:50
 **/
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHi(String name) {
        return "Hi, " + name;
    }
}
