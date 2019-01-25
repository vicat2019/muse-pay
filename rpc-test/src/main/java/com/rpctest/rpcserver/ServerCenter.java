package com.rpctest.rpcserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: muse-pay
 * @description: 服务中心实现类
 * @author: Vincent
 * @create: 2018-11-13 11:53
 **/
public class ServerCenter implements Server {
    private Logger log = LoggerFactory.getLogger("ServerCenter");

    // 线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // 注册的服务列表
    private static final HashMap<String, Class> serviceRegistry = new HashMap<>();
    // 是否在运行
    private static boolean isRunning = false;
    // 端口
    private static int port;

    /**
     * 构造方法
     *
     * @param port 端口
     */
    public ServerCenter(int port) {
        this.port = port;
    }

    /**
     * 停止运行
     */
    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    /**
     * 开始运行
     *
     * @throws IOException 异常
     */
    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
        log.info("启动服务，开始运行");

        try {
            // 1、监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
            while (true) {
                executor.execute(new ServiceTask(server.accept(), serviceRegistry));
                log.info("监听TCP连接……");
            }
        } finally {
            server.close();
        }
    }

    /**
     * 注册服务
     *
     * @param serviceInterface 服务接口
     * @param impl             服务实现类
     */
    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }
}
