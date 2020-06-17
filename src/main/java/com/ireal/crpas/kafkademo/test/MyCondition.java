package com.ireal.crpas.kafkademo.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * kafka 动态启动
 * kafka代理服务器正常时启动kafka服务
 * kafka代理服务器不可用时，不启动kafka服务
 */
public class MyCondition implements Condition {

private static Boolean a=null;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if(true){
            return false;
        }

        if(a!=null&&a.booleanValue()){
            return true;
        }else if (a!=null&&!a.booleanValue()){
            return false;
        }
        //1、能获取到ioc使用的beanfactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //2、获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //3、获取当前环境信息
        Environment environment = context.getEnvironment();
        //4、获取到bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        String property = environment.getProperty("os.name");
        //此处也可以获取到配置文件的信息,比如获取配置文件中的端口
        String ipPort = environment.getProperty("spring.kafka.bootstrap-servers");

        System.out.println(ipPort);
        URI uri = URI.create("http://"+ipPort);

        String host = uri.getHost();

        int port1 = uri.getPort();




       /* Properties props = new Properties();
        props.put("bootstrap.servers", ipPort);
        props.put("acks", "1");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        boolean b=true;
        try {
            Producer<String, String> procuder = new KafkaProducer<String, String>(props);

        } catch (Exception e) {
            b=false;
            e.printStackTrace();
        }*/
        boolean b = this.isHostConnectable(host, port1);
        if(b){
            a=new Boolean(true);
        }else{
            a=new Boolean(false);
        }
        return b;
    }

    /**
     * 判断kafka服务器，能否正常连接
     * @param host
     * @param port
     * @return
     */
    public  boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port),2000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
