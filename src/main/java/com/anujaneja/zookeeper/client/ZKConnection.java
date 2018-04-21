package com.anujaneja.zookeeper.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKConnection {

    private ZooKeeper zooKeeper;
    final CountDownLatch latch = new CountDownLatch(1);

    public ZKConnection() {

    }

    public ZooKeeper connect(String connectionStr) throws IOException,InterruptedException {

        zooKeeper = new ZooKeeper(connectionStr, 2000, new Watcher() {

            public void process(WatchedEvent we) {

                if (we.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                    System.out.println("Zookeeper connection successful to "+we.getPath());
                }
            }
        });

        latch.await();

        return zooKeeper;
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }




}
