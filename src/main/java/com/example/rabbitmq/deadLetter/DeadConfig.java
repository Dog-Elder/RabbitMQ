package com.example.rabbitmq.deadLetter;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 死信配置
 * @author: @Dog_Elder
 * @create: 2023-03-13 12:53
 **/
@Configuration
public class DeadConfig {
    /**
     * 普通队列
     **/
    public static final String ORDINARY_QUEUE = "ordinaryQueue";
    /**
     * 死信队列
     **/
    public static final String DEAD_QUEUE = "deadQueue";

    /**
     * 普通交换机
     **/
    public static final String ORDINARY_EXCHANGE = "ordinaryExchange";
    /**
     * 死信交换机
     **/
    public static final String DEAD_EXCHANGE = "deadExchange";

    /**
     * 普通路由key
     **/
    public static final String ORDINARY_KEY = "ordinaryKey";
    /**
     * 死信路由key
     **/
    public static final String DEAD_KEY = "deadKey";


    /**
     * 声明普通队列并绑定死信交换机和路由key
     **/
    @Bean
    public Queue ordinaryQueue() {
        Map<String, Object> map = new HashMap<>();
        //声明该队列的死信消息发送到的 交换机 （队列添加了这个参数之后会自动与该交换机绑定，并设置路由键，不需要开发者手动设置)
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //声明该队列死信消息在交换机的 路由键
        map.put("x-dead-letter-routing-key", DEAD_KEY);
        //每更换一直情况都要删除原有的队列,否则启动报错
        //情况1:声明ttl超时时间 毫秒
//        map.put("x-message-ttl", 10000);
        //情况2:队列长度设置了 `x-max-length`  队列设置了`x-max-length`最大消息数量且当前队列中的消息已经达到了这个数量，再次投递，消息将被挤掉，被挤掉的是最靠近被消费那一端的消息。
        return QueueBuilder
                .durable(ORDINARY_QUEUE)
                .withArguments(map)
                .build();
    }

    /**
     * 声明死信队列
     **/
    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE, true);
    }

    /**
     * 声明普通交换机
     **/
    @Bean
    public DirectExchange ordinaryExchange() {
        return new DirectExchange(ORDINARY_EXCHANGE);
    }

    /**
     * 声明死信交换机
     **/
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    /**
     * 声明普通交换机和普通队列关系
     **/
    @Bean
    public Binding ordinaryBinding() {
        return BindingBuilder.bind(ordinaryQueue()).to(ordinaryExchange()).with(ORDINARY_KEY);
    }

    /**
     * 声明死信交换机和死信队列关系
     **/
    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_KEY);
    }

}
