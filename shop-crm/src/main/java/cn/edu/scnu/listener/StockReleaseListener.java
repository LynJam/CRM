package cn.edu.scnu.listener;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.entity.OrderItemEntity;
import cn.edu.scnu.service.StockService;
import cn.edu.scnu.to.mq.StockLockTo;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@RabbitListener(queues = "stock.release.stock.queue")
@Service
public class StockReleaseListener {
    @Autowired
    StockService stockService;

    /**
     * 1、库存自动解锁 下订单成功，库存锁定成功，接下来的业务调用失败，导致订单回滚。之前锁定的库存就要自动解锁
     *
     * 2、订单失败 库存锁定失败
     *
     * 只要解锁库存的消息失败，一定要告诉服务解锁失败
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockTo stockLockTo, Message message, Channel channel) throws IOException {
        log.info("=== 收到解锁库存消息 ===");
        try {
            stockService.unlockStock(stockLockTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitHandler
    public void handleOrderCloseRelease(OrderEntity order, Message message, Channel channel) throws IOException {
        log.info("=== 收到关闭订单，解锁库存消息 ===");
        try {
            stockService.unlockStock(order.getOrderId());
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }

    }
}
