package com.example.rabbitmq.releaseConfirmation;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;

/**
 * @program: RabbitMQ
 * @description: 发布确认生产者配置
 * @author: @Dog_Elder
 * @create: 2023-03-09 23:35
 **/
@Configuration
public class ReleaseOKConfig {
    //常规队列
    public static final String RELEASE_QUEUE = "releaseQueue";
    //常规交换机
    public static final String RELEASE_DIRECT = "releaseDirect";
    //常规路由key
    public static final String RELEASE_KEY = "releaseKey";

    //告警队列
    public static final String ALARM_QUEUE = "alarmQueue";
    //备份交换机 告警交换机(扇形)
    public static final String ALARM_EXCHANGE = "alarmExchange";

    //声明常规队列 常规交换机 常规 交换机队列绑定
    @Bean
    public Queue releaseQueue() {
        return new Queue(RELEASE_QUEUE, true);
    }

    /**
     * 声明交换机 绑定备份交换机-->告警交换机
     **/
    @Bean
    public DirectExchange releaseDirect() {
        //ExchangeBuilder.*多个交换机
        return ExchangeBuilder
                .directExchange(RELEASE_DIRECT)
                .durable(true)
                //绑定备份交换机
                .withArgument("alternate-exchange",ALARM_EXCHANGE).build();
    }

    @Bean
    public Binding releaseBinding() {
        return BindingBuilder.bind(releaseQueue()).to(releaseDirect()).with(RELEASE_KEY);
    }

    //声明告警队列 告警交换机 告警 交换机队列绑定
    @Bean
    public Queue alarmQueue() {
        return new Queue(ALARM_QUEUE, true);
    }

    @Bean
    public FanoutExchange alarmExchange() {
        return new FanoutExchange(ALARM_EXCHANGE);
    }

    @Bean
    public Binding alarmQueueBindingAlarmExchange() {
        return BindingBuilder.bind(alarmQueue()).to(alarmExchange());
    }
}
