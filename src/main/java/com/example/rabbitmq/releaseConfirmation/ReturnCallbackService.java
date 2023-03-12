package com.example.rabbitmq.releaseConfirmation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ReturnCallback 退回模式
 * 如果消息未能投递到目标 queue 里将触发回调 returnCallback ，一旦向 queue 投递消息未成功，这里一般会记录下当前消息的详细投递数据，方便后续做重发或者补偿等操作。
 **/
@Slf4j
@Component
public class ReturnCallbackService implements RabbitTemplate.ReturnCallback {
    /**
     * @param message:消息体
     * @param replyCode:响应code
     * @param replyText:响应内容
     * @param exchange:交换机
     * @param routingKey:队列
     **/
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息 {} 经交换机 {} 通过routingKey={} 路由到队列失败，失败code为：{}， 失败原因为：{}",
                new String(message.getBody()), exchange, routingKey, replyCode, replyText);
    }
}
