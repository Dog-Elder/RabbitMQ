package com.example.rabbitmq.delayQueue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 基于死信队列延时配置
 * @author: @Dog_Elder
 * @create: 2023-03-13 19:18
 **/
@Configuration
public class DelayConfig {
    //延时队列
    public static final String DELAY_QUEUE = "delay-queue";
    //普通队列 不用配置对应的消费者 主要是给队列设置TTL
    public static final String ORDINARY_QUEUE = "ordinary-queue";
    //延时交换机(其实就是死信交换机)
    public static final String DELAY_EXCHANGE = "delay-exchange-1";
    //普通交换机
    public static final String ORDINARY_EXCHANGE = "ordinary-exchange-1";
    //延时路由key
    public static final String DELAY_KEY = "delay-key-1";
    //普通路由key
    public static final String ORDINARY_KEY = "ordinary-key-1";

    //死信队列
    @Bean
    public Queue delayQueue(){
        return QueueBuilder.durable(DELAY_QUEUE).build();
    }

    //普通队列绑定死信队列 超时消息全部放入到死信队列 ttl直接绑定队列
    @Bean
    public Queue delayOrdinaryQueue(){
        Map<String, Object> map = new HashMap<>(3);
        map.put("x-dead-letter-exchange", DELAY_EXCHANGE);
        //声明该队列死信消息在交换机的 路由键
        map.put("x-dead-letter-routing-key", DELAY_KEY);
        //声明ttl超时时间 毫秒
//        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(ORDINARY_QUEUE).withArguments(map).build();
    }

    /**
     * 声明普通交换机
     **/
    @Bean
    public DirectExchange delayOrdinaryExchange() {
        return new DirectExchange(ORDINARY_EXCHANGE);
    }

    /**
     * 声明死信交换机
     **/
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }


    /**
     * 声明普通交换机和普通队列关系
     **/
    @Bean
    public Binding delayOrdinaryBinding() {
        return BindingBuilder.bind(delayOrdinaryQueue()).to(delayOrdinaryExchange()).with(ORDINARY_KEY);
    }

    /**
     * 声明死信交换机和死信队列关系
     **/
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_KEY);
    }
}
