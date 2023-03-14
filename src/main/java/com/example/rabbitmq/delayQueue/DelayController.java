package com.example.rabbitmq.delayQueue;

import cn.hutool.core.lang.UUID;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 延迟 死信队列
 * @author: @Dog_Elder
 * @create: 2023-03-13 19:39
 **/
@Slf4j
@RestController
@RequestMapping("delay-letter")
public class DelayController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 延时队列-队列绑定过期时间
     **/
    @PostMapping()
    @ResponseBody
    public void delayQueue() {
        Map<String, String> map = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        map.put("uuid", uuid.toString());
        log.info("uuid:{} 发送时间:{}", uuid, LocalDateTime.now());
        rabbitTemplate.convertAndSend(DelayConfig.ORDINARY_EXCHANGE, DelayConfig.ORDINARY_KEY);
    }

    /**
     * 死信消费者
     **/
    @RabbitHandler
    @RabbitListener(queues = {DelayConfig.DELAY_QUEUE}
            , ackMode = "MANUAL"
    )
    public void deadConsume(Map<String,String> msg, Channel channel, Message message) throws IOException {
        try {
            log.info("死信消费者 uuid:{} 接受时间:{}", msg.get("uuid"), LocalDateTime.now());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("死信消费者处理失败：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
