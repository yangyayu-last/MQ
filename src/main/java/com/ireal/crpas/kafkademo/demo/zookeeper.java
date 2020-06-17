package com.ireal.crpas.kafkademo.demo;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * https://www.cnblogs.com/gaoyawei/p/7723974.html
 * kafka java动态获取topic并动态创建消费者
 */
public class zookeeper {

    public static void main(String[] args) {
        String connectString = "192.168.25.100:2181";
        int sessionTimeout = 4000;
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent event) {
            }
        };
        try {
            ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
            List<String> list = zooKeeper.getChildren("/brokers/topics", false);
            int len = list.size();
            for(int i = 0;i < len;i++){
                System.out.println(list.get(i));//此处动态生成消费者
                if("__consumer_offsets".equals(list.get(i))){
                    continue;
                }
                JavaKafkaConsumerHighAPI example = new JavaKafkaConsumerHighAPI(list.get(i), 1,"","");
                new Thread(example).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
