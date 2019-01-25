package com.rpctest.rpcserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

/**
 * @program: muse-pay
 * @description: 服务任务
 * @author: Vincent
 * @create: 2018-11-13 11:58
 **/
public class ServiceTask implements Runnable {
    private Logger log = LoggerFactory.getLogger("ServiceTask");

    private HashMap<String, Class> serviceRegistry;

    private Socket clientSocket;

    /**
     * 构造方法
     *
     * @param client          客户端
     * @param serviceRegistry 注册的服务集合
     */
    public ServiceTask(Socket client, HashMap<String, Class> serviceRegistry) {
        this.clientSocket = client;
        this.serviceRegistry = serviceRegistry;
    }

    public void run() {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            // 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            // 服务名称
            String serviceName = inputStream.readUTF();
            // 方法名称
            String methodName = inputStream.readUTF();
            // 参数类型列表
            Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
            // 参数列表
            Object[] arguments = (Object[]) inputStream.readObject();

            // 根据服务名称从已注册的服务集合中获取服务对象
            Class serviceClass = serviceRegistry.get(serviceName);
            if (serviceClass == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }
            // 获取方法
            Method method = serviceClass.getMethod(methodName, parameterTypes);

            // 执行方法-即调用服务
            Object result = method.invoke(serviceClass.newInstance(), arguments);

            // 3.将执行结果反序列化，通过socket发送给客户端
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(result);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用服务异常=" + e.getMessage());

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("关闭输出流异常=" + e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("关闭输入流异常=" + e.getMessage());
                }
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("关闭Socket异常=" + e.getMessage());
                }
            }
        }
    }
}
