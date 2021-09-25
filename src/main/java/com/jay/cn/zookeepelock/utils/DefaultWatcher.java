package com.jay.cn.zookeepelock.utils;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author lj
 * @version 1.0
 * @date 2021/9/16 9:11
 */
public class DefaultWatcher implements Watcher {

    Logger logger = LoggerFactory.getLogger(DefaultWatcher.class);

    CountDownLatch count;

    public CountDownLatch getCount() {
        return count;
    }

    public void setCount(CountDownLatch count) {
        this.count = count;
    }

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
                logger.info(">>>>>>>>>>zookeeper conn success>>>>>>>>>>>>>>>>");
                count.countDown();
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
                logger.info(">>>>>>>>>>zookeeper closed success>>>>>>>>>>>>>>>>");
                break;
        }

        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
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
