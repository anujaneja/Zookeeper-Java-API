package com.anujaneja.zookeeper.client;

import com.anujaneja.zookeeper.client.impl.ZKManagerImpl;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ZKClientTest {

    private ZKManagerImpl zkManager = new ZKManagerImpl("localhost");
    private byte[] data= "test1".getBytes();
    String path = "/node1";


    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateNode() throws KeeperException, InterruptedException {
//        data = (new Random().nextInt()+"").getBytes();
        zkManager.create(path,data, CreateMode.PERSISTENT);
        Stat stat = zkManager.getStats(path);
        assertNotNull(stat);
        zkManager.delete(path);
    }

    @Test
    public void testGetZNodeStats() throws KeeperException,
            InterruptedException {
        zkManager.create(path, data,CreateMode.PERSISTENT);
        Stat stat = zkManager.getStats(path);
        assertNotNull(stat);
        assertNotNull(stat.getVersion());
        zkManager.delete(path);
    }

    @Test
    public void testGetZNodeData() throws KeeperException, InterruptedException {
        data= (new Random().nextInt()+"").getBytes();

        zkManager.create(path, data,CreateMode.PERSISTENT);
        zkManager.update(path,data);
        String data = (String) zkManager.getData(path, true);
        assertNotNull(data);
//        zkManager.delete(path);
    }

    @Test
    public void testGetZNodeChildren() throws KeeperException, InterruptedException {
        zkManager.create(path, data,CreateMode.PERSISTENT);
        zkManager.create(path+"/test3", data,CreateMode.PERSISTENT);

        List<String> children= zkManager.getChildren(path);
        assertNotNull(children);
//        zkManager.delete(path);
    }


    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        zkManager.create(path, data,CreateMode.PERSISTENT);
        zkManager.delete(path);
        Stat stat = zkManager.getStats(path);
        assertNull(stat);
    }


}
