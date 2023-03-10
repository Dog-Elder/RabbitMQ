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

    @Bean
    public Queue releaseQueue(){
        return new Queue("releaseQueue", true);
    }

    @Bean
    public DirectExchange releaseDirect(){
        return new DirectExchange("releaseDirect");
    }
    @Bean
    public Binding releaseBinding(){
        return  BindingBuilder.bind(releaseQueue()).to(releaseDirect()).with("releaseKey");
    }
}
