package com.example.rabbitmq.foundation.directConnection01;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 直连模式
 **/
@Controller
@RequestMapping("/simple-producer")
public class SimpleProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    @ResponseBody
    public void testHello() {
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        for (int i = 0; i < 20; i++) {
            rabbitTemplate.convertAndSend(SimpleConfig.SIMPLE_EXCHANGE, SimpleConfig.SIMPLE_DIRECT_ROUTING, "hello world1");
        }
    }

    @RabbitHandler
    /**
     * queuesToDeclare = @Queue(SimpleConfig.SIMPLE_QUEUE) 要监听的队列
     * ackMode = "MANUAL" 设置为消息手动提交ACK
     **/
    @RabbitListener(queuesToDeclare = @Queue(SimpleConfig.SIMPLE_QUEUE), ackMode = "MANUAL")
    public void receive1(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
//            int num = 1/0;
            Thread.sleep(1000);
            System.out.println("消息处理成功1：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败1：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    /*
     * 在@Queue和@Exchange注解中都有autoDelete属性，值是布尔类型的字符串。如：autoDelete=“false”。
     * @Queue：当所有消费客户端断开连接后，是否自动删除队列： true：删除，false:不删除。
     * @Exchange:当所有绑定队列都不在使用时，是否自动删除交换器： true：删除，false：不删除。
     * 当所有消费客户端断开连接时，而我们对RabbitMQ消息进行了持久化，那么这时未被消费的消息存于RabbitMQ服务器的内存中，如果RabbitMQ服务器都关闭了，那么未被消费的数据也都会丢失了。
     *
     * @RabbitListener bindings:绑定队列
     * @QueueBinding  value：绑定队列的名称
     *                  exchange：配置交换器
     * @Queue : value：配置队列名称  如果autoDelete为false 必须给队列声明一个名字 否则则是 '临时队列' 不会持久化  这点很重要
     *          autoDelete:是否是一个可删除的临时队列  当所有消费客户端断开连接后，是否自动删除队列： true：删除，false:不删除。
     * @Exchange value:为交换器起个名称
     *           type:指定具体的交换器类型  ExchangeTypes
     */
    @RabbitHandler
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = SimpleConfig.SIMPLE_QUEUE, autoDelete = "false"),
                            exchange = @Exchange(name = SimpleConfig.SIMPLE_EXCHANGE),
                            key = {SimpleConfig.SIMPLE_DIRECT_ROUTING}
                    )
            },
            ackMode = "MANUAL"
    )
    public void receive2(String msg, Channel channel, Message message) throws IOException {
        try {
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("消息处理成功2：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("消息处理失败2：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
