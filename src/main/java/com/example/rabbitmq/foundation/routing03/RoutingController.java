package com.example.rabbitmq.foundation.routing03;

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

/**
 * @program: RabbitMQ
 * @description: 路由模式
 * @author: @Dog_Elder
 * @create: 2023-03-07 21:07
 **/
@RestController
@RequestMapping("/routing")
public class RoutingController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseBody
    public void testHello() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        String key = "";
        for (int i = 0; i < 20; i++) {
//            int c = RandomUtil.randomInt(1, 5);
            int c = 4;
            switch (c) {
                case 1:
                    key = RoutingConfig.DIRECT_ROUTING_INFO;
                    break;
                case 2:
                    key = RoutingConfig.DIRECT_ROUTING_DEBUG;
                    break;
                case 3:
                    key = RoutingConfig.DIRECT_ROUTING_WARN;
                    break;
                case 4:
                    key = RoutingConfig.DIRECT_ROUTING_ERR;
                    break;
                default:
                    System.out.println("非法数据");
                    break;
            }
            rabbitTemplate.convertAndSend(RoutingConfig.ROUTING_EXCHANGE, key, "消息:"+key);
        }
    }

    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = RoutingConfig.QUEUE_NO, autoDelete = "false"),
                            exchange = @Exchange(name = RoutingConfig.ROUTING_EXCHANGE),
                            key = {RoutingConfig.DIRECT_ROUTING_ERR}
                    )
            },
            ackMode = "MANUAL"
    )
    public void consumeNo(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("consumeNo ：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("consumeNo ：" + msg);
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
                            value = @Queue(value = RoutingConfig.QUEUE_YES, autoDelete = "false"),
                            exchange = @Exchange(name = RoutingConfig.ROUTING_EXCHANGE),
                            //目前声明key 无效  貌似 消费者还是监听的是队列 生产者 生产发送的 路由 到此队列 DEBUG 该消费者还是能够监听到
                            key = {RoutingConfig.DIRECT_ROUTING_INFO,RoutingConfig.DIRECT_ROUTING_DEBUG,RoutingConfig.DIRECT_ROUTING_WARN}
                    )
            },
            ackMode = "MANUAL"
    )
    public void consumeYes(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("consumeYes：" + msg);
//            Thread.sleep(1000);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("consumeYes：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
