//package com.example.rabbitmq.topic04;
//
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @program: RabbitMQ
// * @description: 路由模式
// * @author: @Dog_Elder
// * @create: 2023-03-07 20:09
// **/
//@Configuration
//public class TopicConfig {
//    /**
//     * 创建三个队列 ：RoutingQueue.SMS   RoutingQueue.EMS  RoutingQueue.WECHAT
//     * 将三个队列都绑定在交换机 DirectExchange 上
//     */
//
//    public static final String QUEUE_YES = "RoutingQueue.normal";
//    public static final String QUEUE_NO = "RoutingQueue.err";
//    /**
//     * 声明路由key
//     **/
//    public static final String DIRECT_ROUTING_INFO = "routing.info";
//    public static final String DIRECT_ROUTING_WARN = "routing.warn";
//    public static final String DIRECT_ROUTING_DEBUG = "routing.debug";
//    public static final String DIRECT_ROUTING_ERR = "routing.err";
//    /**
//     * 扇形交换机-路由模式
//     **/
//    public static final String ROUTING_EXCHANGE = "RoutingExchange";
//
//    /**
//     * 创建日志正常消息队列
//     **/
//    @Bean
//    public Queue queueYes() {
//        return new Queue(QUEUE_YES, true);
//    }
//    /**
//     * 创建日志异常消费队列
//     **/
//    @Bean
//    public Queue queueNo() {
//        return new Queue(QUEUE_NO, true);
//    }
//
//
//    /**
//     * 创建交换机A
//     **/
//    @Bean
//    public DirectExchange routingExchange() {
//        return new DirectExchange(ROUTING_EXCHANGE);
//    }
//
////    @Bean
////    public Binding bindingExchangeYES(){
////        return BindingBuilder.bind(queueYes())
////                .to(routingExchange()).with(DIRECT_ROUTING_INFO);
////    }
////    @Bean
////    public Binding bindingExchangeYES2(){
////        return BindingBuilder.bind(queueYes())
////                .to(routingExchange()).with(DIRECT_ROUTING_WARN);
////    }
////    @Bean
////    public Binding bindingExchangeYES3(){
////        return BindingBuilder.bind(queueYes())
////                .to(routingExchange()).with(DIRECT_ROUTING_DEBUG);
////    }
////    @Bean
////    public Binding bindingExchangeNo(){
////        return BindingBuilder.bind(queueNo()).to(routingExchange()).with(DIRECT_ROUTING_ERR);
////    }
//}
