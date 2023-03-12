package com.example.rabbitmq.releaseConfirmation;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @program: RabbitMQ
 * @description: 发布确认
 * @author: @Dog_Elder
 * @create: 2023-03-10 00:15
 **/
@RestController
@RequestMapping("/release")
public class ReleaseController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ConfirmCallbackService confirmCallbackService;

    @Autowired
    private ReturnCallbackService returnCallbackService;
    @PostMapping
    @ResponseBody
    public void release() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
//        for (int i = 0; i < 20; i++) {
            /**
             * 确保消息发送失败后可以重新返回到队列中
             * 注意：yml需要配置 publisher-returns: true
             */
            rabbitTemplate.setMandatory(true);

            /**
             * 消费者确认收到消息后，手动ack回执回调处理
             */
            rabbitTemplate.setConfirmCallback(confirmCallbackService);

            /**
             * 消息投递到队列失败回调处理
             */
            rabbitTemplate.setReturnCallback(returnCallbackService);

            /**
             * 发送消息
             */
            rabbitTemplate.convertAndSend("releaseDirect", "releaseKey1", "消息".getBytes(StandardCharsets.UTF_8),
                    message -> {
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        return message;
                    },
                    new CorrelationData(UUID.randomUUID().toString()));

//        }
    }
}
