package cn.edu.scnu;

import cn.edu.scnu.entity.OrderEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MqTest {
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        System.out.println("test");
    }

    @Test
    public void sendMessageTest() {

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
        }
        log.info("消息发送完成:{}", orderEntity);
    }

    @Test
    public void createExchange() {
        System.out.println("createExchange");

        amqpAdmin.declareExchange(new DirectExchange("hello-java-exchange", true, false));
    }

    @Test
    public void testCreateQueue() {
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功：", "hello-java-queue");
    }

    @Test
    public void createBinding() {

        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE, "hello-java-exchange", "hello.java", null);
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}]创建成功：", "hello-java-binding");
    }

    @Test
    public void create() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        arguments.put("x-message-ttl", 60000); // 消息过期时间 1分钟
        Queue queue = new Queue("order.delay.queue", true, false, false, arguments);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功：", "order.delay.queue");
    }
}
