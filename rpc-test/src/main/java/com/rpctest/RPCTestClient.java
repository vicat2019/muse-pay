package com.rpctest;

import com.rpctest.rpcclient.RPCClient;
import com.rpctest.service.HelloService;

import java.net.InetSocketAddress;

/**
 * @program: muse-pay
 * @description: 测试客户端
 * @author: Vincent
 * @create: 2018-11-13 14:00
 **/
public class RPCTestClient {

    public static void main(String[] args) {
        // RPC客户端
        HelloService service = RPCClient.getRemoteProxyObj(HelloService.class,
                new InetSocketAddress("192.168.0.90", 8088));
        // 调用注册的服务
        System.out.println(service.sayHi("test"));

        for (int i=0; i<50; i++) {
            System.out.println(service.sayHi("NAME" + (i+1)));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
