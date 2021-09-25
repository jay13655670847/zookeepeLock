package com.jay.cn.zookeepelock.zkLock;

/**
 * @author lj
 * @version 1.0
 * @date 2021/9/16 9:20
 */

import com.jay.cn.zookeepelock.utils.ZkLockUtils;
import com.jay.cn.zookeepelock.utils.ZkUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zk锁
 */
public class ZookeeperLock {

    Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);

    ZooKeeper zooKeeper;

    @Before
    public void init(){
        zooKeeper = ZkUtils.conn();
    }

    @After
    public void after(){
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){

        for (int i = 0; i < 10; i++) {
            ZkLockUtils zkLockUtils = new ZkLockUtils();

            new Thread(()->{
                zkLockUtils.setZooKeeper(zooKeeper);
                zkLockUtils.setThreadName(Thread.currentThread().getName());
                //1.获取锁
                zkLockUtils.tryLock();
                //2.干活
                logger.info(Thread.currentThread().getName()+" working.......");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //3.释放锁
                zkLockUtils.unLock();


            }).start();

        }

        while (true){}
    }
}
