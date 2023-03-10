package com.example.rabbitmq.foundation.fanout02;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: RabbitMQ
 * @description: 广播
 * @author: @Dog_Elder
 * @create: 2023-03-05 22:22
 **/
@Configuration
public class FanoutRabbitConfig {
    /**
     * 创建三个队列 ：fanout.A   fanout.B  fanout.C
     * 将三个队列都绑定在交换机 fanoutExchange 上
     * 因为是扇型交换机, 路由键无需配置,配置也不起作用
     */

    public static final String QUEUE_A = "FanoutQueue.A";
    public static final String QUEUE_B = "FanoutQueue.B";
    public static final String QUEUE_C = "FanoutQueue.C";
    public static final String QUEUE_D = "FanoutQueue.D";
    /**
     * 扇形交换机A
     **/
    public static final String FANOUT_EXCHANGE = "FanoutExchange";

    /**
     * 创建队列
     **/
    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A, true);
    }

    /**
     * 创建队列
     **/
    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B, true);
    }

    /**
     * 创建队列
     **/
    @Bean
    public Queue queueC() {
        return new Queue(QUEUE_C, true);
    }

    /**
     * 创建交换机A
     **/
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    /**
     * 队列绑定到交换机
     **/
    @Bean
    Binding bindingExchangeA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    /**
     * 队列绑定到交换机
     **/
    @Bean
    Binding bindingExchangeB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    /**
     * 队列绑定到交换机
     **/
    @Bean
    Binding bindingExchangeC() {
        return BindingBuilder.bind(queueC()).to(fanoutExchange());
    }

}
