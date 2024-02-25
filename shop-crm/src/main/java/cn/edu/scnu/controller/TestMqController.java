package cn.edu.scnu.controller;

import cn.edu.scnu.entity.OrderEntity;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/mq")
@RestController
public class TestMqController {
    @Autowired
    private RabbitTemplate rabbitTemplate;



    @RequestMapping("/sendMsg")
    public String sendMessageTest() {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(UUID.randomUUID()
            .toString());
        orderEntity.setSellerId("1");
        orderEntity.setBuyerId("2");
        orderEntity.setTotalAmount(100.0);
        orderEntity.setPayAmount(100.0);
        orderEntity.setStatus((byte) 0);
        orderEntity.setReceiverName("张三");
        orderEntity.setReceiverPhone("12345678901");
        orderEntity.setReceiverAddr("广东省广州市");
        orderEntity.setPaymentTime(new Date());

        String msg = "Hello World";
        //1、发送消息,如果发送的消息是个对象，会使用序列化机制，将对象写出去，对象必须实现Serializable接口

        //2、发送的对象类型的消息，可以是一个json

        for (int i = 0; i < 3; i++) {

            rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", orderEntity, new CorrelationData(UUID.randomUUID()
                .toString()));
            log.info("消息发送完成:{}", orderEntity);
        }
        return "ok";
    }
}
