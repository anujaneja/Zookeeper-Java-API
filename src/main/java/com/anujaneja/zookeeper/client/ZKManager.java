package com.anujaneja.zookeeper.client;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public  interface ZKManager {

    public void create(String path,byte[] data,CreateMode createMode) throws KeeperException,InterruptedException;

    public Stat getStats(String path) throws KeeperException,
            InterruptedException;

    public Object getData(String path,boolean watchFlag) throws KeeperException,
            InterruptedException;

    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException;

    public List<String> getChildren(String path) throws KeeperException,
            InterruptedException;

    public void delete(String path) throws KeeperException,
            InterruptedException;

}
