package com.example.rabbitmq.deadLetter;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.Channel;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: RabbitMQ
 * @description: 死信队列
 * @author: @Dog_Elder
 * @create: 2023-03-13 12:47
 **/
@RestController
@RequestMapping("dead-letter")
public class DeadLetterController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseBody
    public void testHello() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        for (int i = 0; i < 20; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "张三");
            map.put("caData", LocalDate.now().toString());
            rabbitTemplate.convertAndSend(DeadConfig.ORDINARY_EXCHANGE, DeadConfig.ORDINARY_KEY, map);
        }
    }

    /**
     * 普通消费者
     **/
    @RabbitHandler
    @RabbitListener(queues = {DeadConfig.ORDINARY_QUEUE}
            , ackMode = "MANUAL"
    )
    public void ordinaryConsume(Map msg, Channel channel, Message message) throws IOException {
        try {
            int c = RandomUtil.randomInt(1, 3);
            //模拟异常放入死信队列
            if (c==1) {
                //如果消息处理失败
                int num = 1/0;
            }
            System.out.println("普通消费者处理成功2：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("普通消费者处理失败2：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

//    /**
//     * 死信消费者
//     **/
//    @RabbitHandler
//    @RabbitListener(queues = {DeadConfig.DEAD_QUEUE}
//            , ackMode = "MANUAL"
//    )
//    public void deadConsume(Map msg, Channel channel, Message message) throws IOException {
//        try {
//            //如果消息处理失败
//            //int num = 1/0;
//            System.out.println("死信消费者处理成功：" + msg);
//            // deliveryTag:该消息的index
//            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
//            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (Exception e) {
//            System.out.println("死信消费者处理失败：" + msg);
//            // deliveryTag:该消息的index
//            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
//            // requeue：被拒绝的是否重新入队列
//            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }
//    }
}
