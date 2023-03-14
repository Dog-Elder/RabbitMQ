package com.example.rabbitmq.releaseConfirmation;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
        rabbitTemplate.convertAndSend(ReleaseOKConfig.RELEASE_DIRECT, ReleaseOKConfig.RELEASE_KEY, "正常消息".getBytes(StandardCharsets.UTF_8),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                },
                new CorrelationData(UUID.randomUUID().toString()));

        /**
         * 发送错误 消息 找不到交换机
         */
        rabbitTemplate.convertAndSend(ReleaseOKConfig.RELEASE_DIRECT+"1", ReleaseOKConfig.RELEASE_KEY, "找不到交换机 消息".getBytes(StandardCharsets.UTF_8),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                },
                new CorrelationData(UUID.randomUUID().toString()));
//
        /**
         * 发送错误 消息 无法路由
         */
        rabbitTemplate.convertAndSend(ReleaseOKConfig.RELEASE_DIRECT, ReleaseOKConfig.RELEASE_KEY+"2", "无法路由 消息".getBytes(StandardCharsets.UTF_8),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                },
                new CorrelationData(UUID.randomUUID().toString()));

    }

    @RabbitHandler
    /**
     * queuesToDeclare = @Queue(SimpleConfig.SIMPLE_QUEUE) 要监听的队列
     * ackMode = "MANUAL" 设置为消息手动提交ACK
     **/
    @RabbitListener(queuesToDeclare = @Queue(ReleaseOKConfig.RELEASE_QUEUE), ackMode = "MANUAL")
    public void receive(String msg, Channel channel, Message message) throws IOException {
        try {
            System.out.println("消息处理成功1：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败1：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    @RabbitHandler
    /**
     * 备份交换机 警告队列
     * queuesToDeclare = @Queue(SimpleConfig.SIMPLE_QUEUE) 要监听的队列
     * ackMode = "MANUAL" 设置为消息手动提交ACK
     **/
    @RabbitListener(queuesToDeclare = @Queue(ReleaseOKConfig.ALARM_QUEUE))
    public void warnConsumers(String msg, Channel channel, Message message) throws IOException {
        System.out.println("备份交换机 警告队列 接收到消息：" + msg);
    }
}
