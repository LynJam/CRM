package cn.edu.scnu.listener;

import cn.edu.scnu.entity.OrderEntity;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloListener {

    @RabbitListener(queues = {"hello-java-queue"})
    public void receiveMessage(Message message, OrderEntity content, Channel channel)  {
        System.out.println("HelloListener.receiveMessage");
        System.out.println("message = " + message);
        long deliveryTag = message.getMessageProperties()
            .getDeliveryTag();
        System.out.println("deliveryTag ==> " + deliveryTag);
        try {
            //手动签收消息
            if(deliveryTag % 2 == 0) {
                // 收货
                channel.basicAck(deliveryTag, false);
            } else {
                // 退货
                // requeue: true 发回服务器，重新入队，false 丢弃
                channel.basicNack(deliveryTag, false, false);

            }
        } catch (IOException e) {
            e.printStackTrace();
            //丢弃这条消息
        }
    }
}
