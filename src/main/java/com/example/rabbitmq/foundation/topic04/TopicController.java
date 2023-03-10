package com.example.rabbitmq.foundation.topic04;

import cn.hutool.core.util.RandomUtil;
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
 * @description: 主题模式
 * @author: @Dog_Elder
 * @create: 2023-03-08 21:55
 **/
@RestController
@RequestMapping("/topic")
public class TopicController {

    /**
     * 队列用户信息
     **/
    public static final String QUEUE_TOPIC_INFO = "queue.topic_info";
    /**
     * 队列用户手机号
     **/
    public static final String QUEUE_TOPIC_PHONE = "queue.topic.phone";

    /**
     * 队列用户手机号和身份证号队列
     **/
    public static final String QUEUE_TOPIC_PHONE_ID = "queue.topic.phone.id";

    /**
     * 交换机
     **/
    public static final String TOPIC_EXCHANGE = "TopicExchange";


    /**
     * # 统配符
     * * (star) can substitute for exactly one word.    匹配不多不少恰好1个词
     * # (hash) can substitute for zero or more words.  匹配一个或多个词
     * # 如:
     * audit.#    匹配audit.irs.corporate或者 audit.irs 等
     * audit.*   只能匹配 audit.irs
     **/
    public static final String TOPIC_KEY_ALL = "user.#";
    public static final String TOPIC_KEY_PHONE = "user.phone";
    public static final String TOPIC_KEY_PHONE_EXTRA = "user.phone.*";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseBody
    public void testHello() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        String key = "";
//        for (int i = 0; i < 20; i++) {
            int c = RandomUtil.randomInt(1, 4);
            String msg = "";
            switch (c) {
                case 1:
                    //用户手机号身份证号地址key
                    msg = "用户 #";
                    key = "user.phone.id.address.ada.adada.adada.adadad.dadada.ada";
                    break;
                case 2:
                    //用户手机号
                    msg = "用户 手机号";
                    key = "user.phone";
                    break;
                case 3:
                    //msg = "用户手机号+ *";
                    msg = "用户 手机号+ *";
                    key = "user.phone.id";
                    break;
                default:
                    System.out.println("非法数据");
                    break;
            }
            rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, key, "消息:" + msg);
//        }
    }


    /**
     * 接受用户所有信息
     **/
    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_TOPIC_INFO, autoDelete = "false"),
                            exchange = @Exchange(name = TOPIC_EXCHANGE, type = "topic"),
                            //目前声明key 无效  貌似 消费者还是监听的是队列 生产者 生产发送的 路由 到此队列 DEBUG 该消费者还是能够监听到
                            key = {TOPIC_KEY_ALL}
                    )
            },
            ackMode = "MANUAL"
    )
    public void consumeInfo(String msg, Channel channel, Message message) throws IOException {
        try {
            System.out.println("成功 接受用户所有信息 #：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("失败 接受用户所有信息 #：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    /**
     * 接受用户手机号消息
     **/
    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_TOPIC_PHONE, autoDelete = "false"),
                            exchange = @Exchange(name = TOPIC_EXCHANGE, type = "topic"),
                            key = {TOPIC_KEY_PHONE}
                    )
            },
            ackMode = "MANUAL"
    )
    public void consumePhone(String msg, Channel channel, Message message) throws IOException {
        try {
            System.out.println("成功 接受用户手机号 ：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("失败 接受用户手机号 ：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    /**
     * 接受用户手机号和随机一个信息
     **/
    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = QUEUE_TOPIC_PHONE_ID, autoDelete = "false"),
                            exchange = @Exchange(name = TOPIC_EXCHANGE, type = "topic"),
                            key = {TOPIC_KEY_PHONE_EXTRA}
                    )
            },
            ackMode = "MANUAL"
    )
    public void consumeMobilePhoneNumberIDCard(String msg, Channel channel, Message message) throws IOException {
        try {
            System.out.println("成功 接受用户手机号 *：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("失败 接受用户手机号 * ：" + msg);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
