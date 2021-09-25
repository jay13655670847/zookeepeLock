package com.jay.cn.zookeepelock.utils;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author lj
 * @version 1.0
 * @date 2021/9/16 20:39
 */
public class ZkLockUtils implements AsyncCallback.StringCallback , Watcher , AsyncCallback.Children2Callback {

    Logger logger = LoggerFactory.getLogger(ZkLockUtils.class);

    ZooKeeper zooKeeper;

    String threadName;

    String pathName;

    CountDownLatch count = new CountDownLatch(1);

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    /**
     * 获取锁
     */
    public void tryLock(){
        try {
            logger.info(threadName+"尝试获取锁");
            //创建临时节点
            //回调StringCallback
            zooKeeper.create("/lock",threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,this,"sdf");

            count.await();
            logger.info(threadName+"成功获取锁");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 释放锁
     */
    public void unLock(){
        try {
            //删除当前子节点，让剩余子节点进行争抢锁
            zooKeeper.delete(pathName,-1);
            logger.info(threadName+"释放锁");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * StringCallback
     * @param rc
     * @param path
     * @param ctx
     * @param name
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        logger.info(threadName+">>>>>>>>>>>>>name："+name);
        if (name != null){
            pathName = name;
            //此处的监听watcher设为false 否则children排序会对所有子节点进行监控，太浪费资源
            //Children2Callback
            zooKeeper.getChildren("/",false,this,"sdfg");
        }
    }


    /**
     * Children2Callback
     * @param i
     * @param s
     * @param o
     * @param list
     * @param stat
     */
    @Override
    public void processResult(int i, String s, Object o, List<String> list, Stat stat) {
        //对所有子节点进行排序
        Collections.sort(list);
        //获取当前节点在list当中的索引
        int index = list.indexOf(pathName.substring(1));

        if (index==0){
            //是第一个节点，释放锁
            count.countDown();
        }else{
            //不是，判断上一个节点是否还存在
            try {
                zooKeeper.exists("/"+list.get(index-1),this);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Watcher
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
            case Closed:
                break;
        }

        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                //当前节点删除，获取剩余子节点重新排序
                zooKeeper.getChildren("/",false,this,"sdf0");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            case DataWatchRemoved:
                break;
            case ChildWatchRemoved:
                break;
            case PersistentWatchRemoved:
                break;
        }
    }
}
