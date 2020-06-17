package com.ireal.crpas.kafkademo.demo;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.example.es.Es;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
public class KafkaConsumer implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private Map<String, Integer> topicCountMap;
    private Properties props;

    public KafkaConsumer(Map<String, Integer> topicCountMap, Properties props) {
        this.topicCountMap = topicCountMap;
        this.props = props;
    }

    @Override
    public void run() {
        ConsumerConnector consumer = null;
        ExecutorService executor = null;
        try {
            consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
            Map<String, List<KafkaStream<byte[], byte[]>>> msgStreams = consumer.createMessageStreams(topicCountMap);
            for (String topic : topicCountMap.keySet()) {
                List<KafkaStream<byte[], byte[]>> msgStreamList = msgStreams.get(topic);
                // 使用ExecutorService来调度线程
                executor = Executors.newFixedThreadPool(topicCountMap.get(topic));
                for (int i = 0; i < msgStreamList.size(); i++) {
                    KafkaStream<byte[], byte[]> kafkaStream = msgStreamList.get(i);
                    executor.submit(new HanldMessageThread(kafkaStream, i, topic));
                }
            }

        } catch (Exception e) {
            if (consumer != null) {
                consumer.shutdown();
            }
            if (executor != null) {
                executor.shutdown();
            }
            try {
                if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    logger.error("Timed out waiting for consumer threads to shutdown, exiting uncleanly");
                }
            } catch (InterruptedException e1) {
                logger.error("Interrupted during shutdown, exiting uncleanly");
            }
            logger.error(e.getMessage());
        }
    }

}

/**
 * 具体处理message的线程
 *
 * @author Administrator
 *
 */
class HanldMessageThread implements Runnable {

    private KafkaStream<byte[], byte[]> kafkaStream = null;
    private int num = 0;
    private String topic;

    public HanldMessageThread(KafkaStream<byte[], byte[]> kafkaStream, int num, String topic) {
        super();
        this.kafkaStream = kafkaStream;
        this.num = num;
        this.topic = topic;
    }

    public void run() {
        System.out.println(topic);
        ConsumerIterator<byte[], byte[]> iterator = kafkaStream.iterator();

        while (iterator.hasNext()) {
            String message = new String(iterator.next().message());
//          System.out.println(Thread.currentThread().getName());
//          System.out.println(this.num + "____" + topic + "____" + message);
//          System.out.println(Thread.currentThread().getId());
//          System.out.println("Thread no: " + num + ", message: " + message);
            if (topic.startsWith("xrs") || topic.startsWith("meitan") || topic.startsWith("qiyexinxi")) {
                //Es.setData(message, "xrs_db", topic);
                System.out.println(message);
            } else if (topic.startsWith("search")) {
               // Es.setData(message, "pholcus_news_v1", topic);
                System.out.println(message);
            } else {
                //Es.setData(message, "pholcus_db", topic);
                System.out.println(message);
            }
        }
    }

}
