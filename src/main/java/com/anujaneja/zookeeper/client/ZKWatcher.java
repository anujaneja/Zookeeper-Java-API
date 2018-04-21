package com.anujaneja.zookeeper.client;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZKWatcher implements Watcher,AsyncCallback.StatCallback {

    private CountDownLatch latch;

    public ZKWatcher() {
        latch = new CountDownLatch(1);
    }

    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }

    public void process(WatchedEvent event) {
        System.out.println("Watcher fired on path: " + event.getPath() + " state: " +
                event.getState() + " type " + event.getType());
        //DO something in case of specific event type.
        latch.countDown();
    }

    public void await() throws InterruptedException {
        latch.await();
    }
}
