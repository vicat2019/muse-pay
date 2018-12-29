package com.rpctest;

import com.rpctest.rpcserver.Server;
import com.rpctest.rpcserver.ServerCenter;
import com.rpctest.service.HelloService;
import com.rpctest.service.impl.HelloServiceImpl;

import java.io.IOException;

/**
 * @program: muse-pay
 * @description: RPC测试类
 * @author: Vincent
 * @create: 2018-11-13 12:05
 **/
public class RPCServer {

    public static void main(String[] args) throws IOException {
        // RPC服务端
        new Thread(() -> {
            try {
                // 生成RPC服务对象
                Server serviceServer = new ServerCenter(8088);
                // 注册服务
                serviceServer.register(HelloService.class, HelloServiceImpl.class);
                // 启动服务
                serviceServer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
