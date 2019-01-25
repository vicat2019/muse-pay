package com.rpctest.zookeepertest;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * @program: muse-pay
 * @description: RPC TEST
 * @author: Vincent
 * @create: 2018-11-13 10:49
 **/
public class HelloZooKeeper {

    public static void main(String[] args) throws IOException {
        String hostPort = "192.168.0.90:2181";
        String zpath = "/";

        List<String> zooChildren;
        ZooKeeper zk = new ZooKeeper(hostPort, 2000, null);
        try {
            zooChildren = zk.getChildren(zpath, false);

            System.out.println("Znodes of '/': ");
            zooChildren.forEach(System.out::println);


        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
