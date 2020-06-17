package com.ireal.crpas.kafkademo.test;

import com.ireal.crpas.kafkademo.config.KafkaTemplateConfig;
import com.ireal.crpas.kafkademo.consumer.Consumer;
import com.ireal.crpas.kafkademo.producer.Product;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class A implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("============================项目启动完成");
        //KafkaListenerAnnotationBeanPostProcessor
        SpringBeanFactory.setBean("kafkaTemplateConfig", KafkaTemplateConfig.class);
        SpringBeanFactory.setBean("consumer", Consumer.class);
        SpringBeanFactory.setBean("product", Product.class);

    }


    //bean 的名称生成器.

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    /**

     * 提供公共的注册方法。

     * @param beanDefinitionRegistry

     * @param name

     * @param beanClass

     */

       /* public void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass){

        AnnotatedBeanDefinition annotatedBeanDefinition  = new AnnotatedGenericBeanDefinition(beanClass);

        //可以自动生成name

        String beanName = (name != null?name:this.beanNameGenerator.generateBeanName(annotatedBeanDefinition, registry));

        //bean注册的holer类.

        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(annotatedBeanDefinition, beanName);

        //使用bean注册工具类进行注册.

        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
        SpringApplication
    }
*/

}
