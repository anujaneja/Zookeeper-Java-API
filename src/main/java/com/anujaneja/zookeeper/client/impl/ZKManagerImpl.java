package com.anujaneja.zookeeper.client.impl;

import com.anujaneja.zookeeper.client.ZKConnection;
import com.anujaneja.zookeeper.client.ZKManager;
import com.anujaneja.zookeeper.client.ZKWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ZKManagerImpl implements ZKManager {

    private ZKConnection zkConnection;
    private ZooKeeper zooKeeper;

    public ZKManagerImpl(String connectionStr) {
        initialize(connectionStr);
    }

    public void initialize(String connectionStr)  {
        try {
            zkConnection = new ZKConnection();
            zooKeeper = zkConnection.connect(connectionStr);
        }catch (Exception ex) {
            System.out.println("EXception in connecting to Zookeeper "+ex.getMessage());
        }

    }



    public void create(String path, byte[] data,CreateMode createMode) throws KeeperException, InterruptedException {
        if(zooKeeper.exists(path,false)==null) {
            zooKeeper.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE,createMode);
        } else {
            System.out.println("Zookeeper node already exists for path:" +path);
        }

    }

    /**
     * By default watch true
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public Stat getStats(String path) throws KeeperException, InterruptedException {
        Stat stat = zooKeeper.exists(path,true);
        if(stat!=null) {
            System.out.println("Node Exists with path: "+path+" and version is: "+stat.getVersion());
        } else {
            System.out.println("Node does not Exists with path: "+path);
        }
        return stat;
    }

    public Object getData(String path, boolean watchFlag) throws KeeperException, InterruptedException {

        byte[] b = null;
        Stat stat = getStats(path);

        if(stat!=null) {
            if(watchFlag) {
                //Create your own watch....
                ZKWatcher zkWatcher = new ZKWatcher();
                b= zooKeeper.getData(path,zkWatcher,stat);

                zkWatcher.await();

            } else {
                //No need to create a watch.....
                b = zooKeeper.getData(path,false,stat);
            }

            String data = null;
            try {
                data = new String(b, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(data);

            return data;

        } else {
            System.out.println("Node does not exists...."+path);
        }

        return null;
    }

    public void update(String path, byte[] data) throws KeeperException, InterruptedException {
        int version = getStats(path).getVersion();
        zooKeeper.setData(path,data,version);
    }

    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        Stat stat = getStats(path);
        List<String> children = null;

        if(stat!=null) {
            children = zooKeeper.getChildren(path,true);

            for(int i = 0;i<children.size();i++) {
                System.out.println(children.get(i));
            }
        } else {
            System.out.println("Node does not exists"+path);
        }

        return children;
    }

    public void delete(String path) throws KeeperException, InterruptedException {
        int version = zooKeeper.exists(path, true).getVersion();
        zooKeeper.delete(path, version);

    }
}
