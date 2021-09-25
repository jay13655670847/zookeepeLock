package com.jay.cn.zookeepelock.utils;

/**
 * @author lj
 * @version 1.0
 * @date 2021/9/16 9:06
 */

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zk连接工具类
 */
public class ZkUtils {

    public static final String ip="192.168.216.129:2181,192.168.216.130:2181,192.168.216.131:2181,192.168.216.132:2181/zkLock";

    static CountDownLatch count = new CountDownLatch(1);

    /**
     * zk连接
     * @return
     */
    public static ZooKeeper conn(){
        try {
            DefaultWatcher defaultWatcher = new DefaultWatcher();
            ZooKeeper zooKeeper = new ZooKeeper(ip, 5000, defaultWatcher);

            //异步连接，进行阻塞等待
            defaultWatcher.setCount(count);
            count.await();

            return zooKeeper;
        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
