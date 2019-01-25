package com.rpctest.zookeepertest;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @program: muse-pay
 * @description: 数据监听
 * @author: Vincent
 * @create: 2018-11-13 11:21
 **/
public class DataWatcher implements Watcher, Runnable {
    private Logger log = LoggerFactory.getLogger("DataWatcher");

    private static String hostPort = "192.168.0.90:2181";
    private static String zooDataPath = "/MyConfig";
    private byte[] zooData = null;

    private ZooKeeper zk;

    /**
     * 构造方法
     */
    public DataWatcher() {
        try {
            zk = new ZooKeeper(hostPort, 2000, this);
            try {
                // 检查指定的路径是否存在，不存在则创建
                if (zk.exists(zooDataPath, this) == null) {
                    zk.create(zooDataPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
                log.error("初始化异常，创建ZK目录, 异常信息=" + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("初始化异常，创建ZK目录, 异常信息=" + e.getMessage());
        }
    }

    /**
     * 处理监听事件
     *
     * @param event 收到的监听事件
     */
    @Override
    public void process(WatchedEvent event) {
        System.out.printf("\n接收到的事件信息=%s", event.toString());

        // 处理监听到的事件
        if (event.getType() == Event.EventType.NodeDataChanged) {
            try {
                printData();
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
                log.error("处理监听事件异常=" + e.getMessage());
            }
        }
    }

    /**
     * 打印数据
     *
     * @throws InterruptedException 线程中断异常
     * @throws KeeperException      ZooKeeper异常
     */
    public void printData() throws InterruptedException, KeeperException {
        // 从ZooKeeper中获取数据，并重新设置监听器
        zooData = zk.getData(zooDataPath, this, null);

        // 输出当前节点的数据内容
        String zString = new String(zooData);
        System.out.printf("\n当前数据= @ZK Path %s: %s", zooDataPath, zString);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (true) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args)
            throws InterruptedException, KeeperException {
        DataWatcher dataWatcher = new DataWatcher();
        dataWatcher.printData();
        dataWatcher.run();
    }


}
