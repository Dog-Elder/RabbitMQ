package com.example.rabbitmq.foundation.fanout02;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.example.rabbitmq.foundation.fanout02.FanoutRabbitConfig.*;

/**
 * @program: RabbitMQ
 * @description: 广播生产者
 * @author: @Dog_Elder
 * @create: 2023-03-05 22:16
 **/
@RestController
@RequestMapping("/fanout-producer")
public class FanoutProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseBody
    public void fanoutSend() {
        // 发送到交换机FanoutExchange
//        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(FANOUT_EXCHANGE,null, "广播模式-扇形交换机");
//        }
    }

    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_A, autoDelete = "false"),
                            exchange = @Exchange(name = FANOUT_EXCHANGE,type = "fanout")
                    )
            },
            ackMode = "MANUAL"
    )
    public void FanoutReceive1(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("消息处理成功 扇形接收 绑定队列QUEUE_A ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败 扇形接收 绑定队列QUEUE_A ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_B, autoDelete = "false"),
                            exchange = @Exchange(name = FANOUT_EXCHANGE,type = "fanout")
                    )
            },
            ackMode = "MANUAL"
    )
    public void FanoutReceive2(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("消息处理成功 扇形接收 绑定队列QUEUE_B ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败 扇形接收 绑定队列QUEUE_B ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_C, autoDelete = "false"),
                            exchange = @Exchange(name = FANOUT_EXCHANGE,type = "fanout")
                    )
            },
            ackMode = "MANUAL"
    )
    public void FanoutReceive3(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
//            int num = 1/0;
            System.out.println("消息处理成功 扇形接收 绑定队列QUEUE_C ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败 扇形接收 绑定队列QUEUE_C ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_D, autoDelete = "false"),
                            exchange = @Exchange(name = FANOUT_EXCHANGE,type = "fanout")
                    )
            },
            ackMode = "MANUAL"
    )
    public void FanoutReceive4(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
//            int num = 1/0;
            System.out.println("消息处理成功 扇形接收 绑定队列QUEUE_D ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败 扇形接收 绑定队列QUEUE_D ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
