package com.example.rabbitmq.delayQueue;

import cn.hutool.core.lang.UUID;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 延迟交换机-延迟队列
 * @author: @Dog_Elder
 * @create: 2023-03-14 11:12
 **/
@Slf4j
@RestController
@RequestMapping("delay-queue")
public class DelayQueueController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 生产者
     * delayTime 延迟的时间 (毫秒)
     **/
    @PostMapping("{delayTime}")
    public void delayQueue(@PathVariable("delayTime") Integer delayTime) {
        Map<String, String> map = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        map.put("uuid", uuid.toString());
        log.info("uuid:{} 发送时间:{}", uuid, LocalDateTime.now());

        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAY_EXCHANGE_NAME, DelayQueueConfig.DELAY_ROUTING_NAME, map, msg -> {
            //针对消息设置ttl
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }

    /**
     * 延迟消费者
     **/
    @RabbitHandler
    @RabbitListener(queues = {DelayQueueConfig.DELAY_QUEUE_NAME}
            , ackMode = "MANUAL"
    )
    public void deadConsume(Map<String, String> msg, Channel channel, Message message) throws IOException {
        try {
            log.info("延迟交换机-消费者 uuid:{} 接受时间:{}", msg.get("uuid"), LocalDateTime.now());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("延迟交换机-消费者处理失败：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
