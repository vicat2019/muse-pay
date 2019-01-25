package com.rpctest.rpcserver;

import java.io.IOException;

/**
 * @program: muse-pay
 * @description: 服务中心接口
 * @author: Vincent
 * @create: 2018-11-13 11:52
 **/
public interface Server {
    void stop();

    void start() throws IOException;

    void register(Class serviceInterface, Class impl);

    boolean isRunning();

    int getPort();
}
