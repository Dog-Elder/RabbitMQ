package com.example.rabbitmq.foundation.directConnection01;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @program: RabbitMQ
 * @description: 直连模式配置
 * @author: @Dog_Elder
 * @create: 2023-02-28 21:25
 **/
@Configuration
public class SimpleConfig {

    /**
     * 直连队列名
     **/
    public static final String SIMPLE_QUEUE = "SimpleAutomaticQueue";
    /**
     * 直连交换机
     **/
    public static final String SIMPLE_EXCHANGE = "SimpleExchange";
    /**
     * 直连路由Key
     **/
    public static final String SIMPLE_DIRECT_ROUTING = "SimpleDirectRouting";

    /**
     * 队列 起名：SimpleAutomaticBean
     **/
    @Bean
    public Queue simpleQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("SimpleAutomaticBean",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(SIMPLE_QUEUE, true);
    }

    //Direct交换机 起名：TestDirectExchange
    @Bean
    DirectExchange SimpleExchange() {
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange(SIMPLE_EXCHANGE, true, false);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：SimpleDirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(simpleQueue()).to(SimpleExchange()).with(SIMPLE_DIRECT_ROUTING);
    }
//
//    @Bean
//    DirectExchange lonelyDirectExchange() {
//        return new DirectExchange("lonelyDirectExchange");
//    }

}
