package com.ireal.crpas.kafkademo.controller;

import com.ireal.crpas.kafkademo.producer.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/test")
//@RefreshScope
public class TestController {

    @Autowired(required = false)
    private Product product;

    @Value("${topicName}")
    private String topicName;

    @GetMapping("/1/{topic}")
    @ResponseBody
    public void update(@PathVariable String topic, String message) {

        product.send(topic,message);
    }
    @GetMapping("/2")
    @ResponseBody
    public void a() {

        System.out.println(topicName);
    }

    @GetMapping("/3")
    @ResponseBody
    public void b() {
        System.setProperty("topicName", "111");
        System.out.println(topicName);
    }
}
