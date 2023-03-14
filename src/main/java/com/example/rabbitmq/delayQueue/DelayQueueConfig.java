package com.example.rabbitmq.delayQueue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 基于插件的延时队列
 * @author: @Dog_Elder
 * @create: 2023-03-13 23:40
 **/
@Configuration
public class DelayQueueConfig {
    //队列
    public static final String DELAY_QUEUE_NAME="delay.queue";
    //交换机
    public static final String DELAY_EXCHANGE_NAME="delay.exchange";
    //routingKey
    public static final String DELAY_ROUTING_NAME="delay.routingKey";

    //队列
    @Bean
    public Queue delayQueueName(){
        return new Queue(DELAY_QUEUE_NAME,true,false,false);
    }
    /**
     * 声明交换机
     **/
    @Bean
    public CustomExchange diyDelayExchange(){
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        /**
         * 交换机的名称
         * 交换机的类型
         * 是否需要持久化
         * 是否需要自动删除
         * 其他参数
         **/
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, map);
    }


    /**
     * 声明延迟交换机和队列关系
     **/
    @Bean
    public Binding delayQueueNameBingDiyDelayExchange() {
        return BindingBuilder.bind(delayQueueName()).to(diyDelayExchange()).with(DELAY_ROUTING_NAME).noargs();
    }
}
