package com.ireal.crpas.kafkademo.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanFactory implements BeanFactoryAware {

    public static DefaultListableBeanFactory listableBeanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        this.listableBeanFactory = listableBeanFactory;
    }

    public static void setBean(String beanName,Object object){
        //注意这里放入的bean在容器中是单例的
        listableBeanFactory.registerSingleton(beanName,object);
    }

    //根据beanName销毁(删除)单例的bean
    public static void removeSingletonBean(String beanName){
        listableBeanFactory.destroySingleton(beanName);
    }

    //手动把对象放入容器中,但是可以设置作用域
    public static void setBean(String beanName,Class<?> clazz){
        BeanDefinition beanDefinition =  new RootBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        //设置作用域
        beanDefinition.setScope("prototype");
        listableBeanFactory.registerBeanDefinition(beanName,beanDefinition);
    }

    //根据beanName删除使用BeanDefinition创建的bean , Spring默认就是使用BeanDefinition创建的bean对象
    public static void removeBean(String beanName){
        listableBeanFactory.removeBeanDefinition(beanName);
    }
}
