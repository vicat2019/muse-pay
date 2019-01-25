package com.rpctest.zookeepertest;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * @program: muse-pay
 * @description: 定时更新数据
 * @author: Vincent
 * @create: 2018-11-13 11:30
 **/
public class DataUpdater implements Watcher {
    private Logger log = LoggerFactory.getLogger("DataUpdater");

    private static String hostPort = "192.168.0.90:2181";
    private static String zooDataPath = "/MyConfig";
    private ZooKeeper zk;

    /**
     * 构造方法
     *
     * @throws IOException 异常
     */
    public DataUpdater() throws IOException {
        try {
            zk = new ZooKeeper(hostPort, 2000, this);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("初始化定时更新数据类，异常=" + e.getMessage());
        }
    }

    /**
     * 每5秒钟更新数据
     *
     * @throws InterruptedException 线程中断异常
     * @throws KeeperException      ZooKeeper异常
     */
    public void run() throws InterruptedException, KeeperException {
        while (true) {
            String uuid = UUID.randomUUID().toString();
            byte zooData[] = uuid.getBytes();
            zk.setData(zooDataPath, zooData, -1);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            log.info("更新数据到ZooKeeper，更新内容=" + uuid + "， 更新路径=" + zooDataPath);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        log.info("接收到的监听事件=" + event.toString());
    }

    public static void main(String[] args) throws
            IOException, InterruptedException, KeeperException {
        DataUpdater dataUpdater = new DataUpdater();
        dataUpdater.run();
    }


}
