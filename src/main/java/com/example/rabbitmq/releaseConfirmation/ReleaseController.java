package com.example.rabbitmq.releaseConfirmation;

import com.example.rabbitmq.foundation.directConnection01.SimpleConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(SimpleConfig.SIMPLE_EXCHANGE, SimpleConfig.SIMPLE_DIRECT_ROUTING, "hello world1");
        }
    }
}
