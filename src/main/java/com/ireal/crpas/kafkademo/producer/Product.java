package com.ireal.crpas.kafkademo.producer;


import com.alibaba.fastjson.JSON;
import com.ireal.crpas.kafkademo.test.MyCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
@Conditional(MyCondition.class)
@Component
public class Product {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public void send(String topic,String name){
        HashMap<String, String> map = new HashMap<>();
        map.put("devGbid", "32020001031981000003");
        String s = JSON.toJSONString(map);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, s);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("===Producing message success");
            }
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("===Producing message failed");
            }
        });
    }
}