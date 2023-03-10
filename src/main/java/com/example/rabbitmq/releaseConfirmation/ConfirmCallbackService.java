package com.example.rabbitmq.releaseConfirmation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback确认模式
 * 消息只要被 rabbitmq broker 接收到就会触发 confirmCallback 回调 。
 * 但消息被 broker 接收到只能表示已经到达 MQ服务器，并不能保证消息一定会被投递到目标 queue 里。所以接下来需要用到 returnCallback 。
 **/
@Slf4j
@Component
public class ConfirmCallbackService implements RabbitTemplate.ConfirmCallback {
    /**
     * @param correlationData: 对象内部只有一个 id 属性，用来表示当前消息的唯一性。
     * @param ack: 消息投递到broker 的状态，true表示成功。
     * @param cause:表示投递失败的原因。
     **/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        if (!ack) {
            log.error("消息发送异常!");
        } else {
            log.info("发送者爸爸已经收到确认，correlationData={} ,ack={}, cause={}", correlationData.getId(), ack, cause);
        }
    }
}
