package com.ireal.crpas.kafkademo.consumer;

import com.ireal.crpas.kafkademo.test.MyCondition;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Conditional(MyCondition.class)
@Component
//@RefreshScope(proxyMode = DEFAULT)
public class Consumer {

  /*  @Value("${topicName}")
    private String topicName;*/

    //@KafkaListener(topics = { "test" })
    @KafkaListener(topics = {"${topicName}"})
    public void handle(ConsumerRecord<String, String> record,final Acknowledgment acknowledgment) {
       //System.out.println("[ 处理器开始处理消息 ]" + System.currentTimeMillis()+/topicName);

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(record);

        System.out.println("[ 处理器处理消息完成 ]" + System.currentTimeMillis());
        acknowledgment.acknowledge();//手动提交offset,不提交的话，每次启动消费着，都会重复消费，原来没提交的消息
    }

  @KafkaListener(topics = {"test"})
    public void a(ConsumerRecord<String, String> record,final Acknowledgment acknowledgment) {
      //  System.out.println("[ 处理器开始处理test消息 ]" + System.currentTimeMillis()+topicName);

     // System.out.println(topicName);
        acknowledgment.acknowledge();//手动提交offset,不提交的话，每次启动消费着，都会重复消费，原来没提交的消息
    }











    @KafkaListener(topics = {"device-repair-feed-back"},containerFactory = "kafkaListenerContainerFactory")
    public void consumer(ConsumerRecord consumerRecord,final Acknowledgment acknowledgment){
        System.out.println("consumerRecord = " + consumerRecord);
        /*Optional<Object> kafkaMassage = Optional.ofNullable(consumerRecord.value());
        if(kafkaMassage.isPresent()){
            Object o = kafkaMassage.get();
            System.out.println(o);
        }*/
        System.out.println("收到消息"+consumerRecord.value());

    Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
        Object message = kafkaMessage.get();
        System.out.println("----------------- record =" + consumerRecord);
        System.out.println("------------------ message =" + message);
    }
       acknowledgment.acknowledge();//手动提交offset,不提交的话，每次启动消费着，都会重复消费，原来没提交的消息
}

/*


    @KafkaListener(topics = "${kafka.topic.manual}", containerFactory = "manualKafkaListenerContainerFactory")
    public void receive(@Payload String message,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        Consumer consumer,
                        Acknowledgment ack) {
        System.out.println(String.format("From partition %d : %s", partition, message));
        // 同步提交
        consumer.commitSync();

        // ack这种方式提交也可以
        // ack.acknowledge();
    }

    */
/**
     * commitSync和commitAsync组合使用
     * <p>
     * 手工提交异步 consumer.commitAsync();
     * 手工同步提交 consumer.commitSync()
     * <p>
     * commitSync()方法提交最后一个偏移量。在成功提交或碰到无怯恢复的错误之前，
     * commitSync()会一直重试，但是commitAsync()不会。
     * <p>
     * 一般情况下，针对偶尔出现的提交失败，不进行重试不会有太大问题，因为如果提交失败是因为临时问题导致的，
     * 那么后续的提交总会有成功的。但如果这是发生在关闭消费者或再均衡前的最后一次提交，就要确保能够提交成功。
     * 因此，在消费者关闭前一般会组合使用commitAsync()和commitSync()。
     *//*

//    @KafkaListener(topics = "${kafka.topic.manual}", containerFactory = "manualKafkaListenerContainerFactory")
    public void manual(@Payload String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       Consumer consumer,
                       Acknowledgment ack) {
        try {
            System.out.println(String.format("From partition %d : %s", partition, message));
            // 同步提交
            consumer.commitSync();
        } catch (Exception e) {
            System.out.println("commit failed");
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }

    }


    */
/**
     * 手动提交，指定偏移量
     *
     * @param record
     * @param consumer
     *//*

//    @KafkaListener(topics = "${kafka.topic.manual}", containerFactory = "manualKafkaListenerContainerFactory")
    public void offset(ConsumerRecord record, Consumer consumer) {
        System.out.println(String.format("From partition %d : %s", record.partition(), record.value()));

        Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
        currentOffset.put(new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1));
        consumer.commitSync(currentOffset);
    }
*/

}