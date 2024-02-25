package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.entity.OrderItemEntity;
import cn.edu.scnu.entity.ProductEntity;
import cn.edu.scnu.enums.OrderStatusEnum;
import cn.edu.scnu.exception.BizException;
import cn.edu.scnu.mapper.OrderItemMapper;
import cn.edu.scnu.mapper.OrderMapper;
import cn.edu.scnu.mapper.ProductMapper;
import cn.edu.scnu.service.OrderService;
import cn.edu.scnu.service.ProductService;
import cn.edu.scnu.service.StockService;
import cn.edu.scnu.vo.OrderVo;
import cn.edu.scnu.vo.ProductCartVo;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ProductService productService;

    @Override
    public OrderVo cartToTrade(List<ProductCartVo> productCartVos, String userId) {
        OrderVo orderVo = new OrderVo();
        List<OrderItemEntity> orderItems = Lists.newArrayList();
        String orderId = new UUID(System.currentTimeMillis(), 0L).toString();
        orderVo.setOrderId(orderId);
        AtomicReference<Double> totalAmountAtom = new AtomicReference<>(0.0);
        productCartVos.forEach(productCartVo -> {
            String productId = productCartVo.getProductId();
            ProductEntity productEntity = getProductEntity(productId);
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProductId(productEntity.getProductId());
            orderItem.setProductImage(productEntity.getProductImage());
            orderItem.setProductPrice(productEntity.getProductPrice()
                .doubleValue());
            orderItem.setProductName(productEntity.getProductName());
            orderItem.setQuantity(productCartVo.getNum());
            orderItem.setOrderId(orderId);
            orderItem.setRealAmount(orderItem.getProductPrice() * orderItem.getQuantity());
            totalAmountAtom.set(totalAmountAtom.get() + orderItem.getRealAmount());
            orderItems.add(orderItem);

            orderVo.setSellerId(productEntity.getSellerId());
        });

        orderVo.setOrderItems(orderItems);
        orderVo.setBuyerId(userId);
        orderVo.setPayAmount(totalAmountAtom.get());
        return orderVo;
    }

    private ProductEntity getProductEntity(String productId) {
        LambdaQueryWrapper<ProductEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductEntity::getProductId, productId);
        return productMapper.selectOne(queryWrapper);
    }

    @Transactional
    @Override
    public void submitOrder(OrderVo orderVo, String userId) {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderVo, orderEntity);
        orderEntity.setStatus((byte) 0);
        save(orderEntity); // 保存订单
        List<OrderItemEntity> orderItems = orderVo.getOrderItems();
        orderItemMapper.batchInsert(orderItems);

        // 锁定库存，全部商品锁定成功，创建订单，否则回滚
        boolean success = stockService.lockStock(orderVo.getOrderItems());
        if(!success) {
            throw new BizException("库存不足");
        }

        //  发消息给 MQ 指定时间没有支付则自动关闭订单
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order", orderEntity);

        // 删除购物车中的商品
        productService.delProductCart(userId);

    }

    @Override
    public List<OrderVo> listByType(int type, String userId) {
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(type == 1) {
            queryWrapper.eq(OrderEntity::getSellerId, userId);
        } else if(type == 2) {
            queryWrapper.eq(OrderEntity::getBuyerId, userId);
        } else {
           queryWrapper.eq(OrderEntity::getBuyerId, userId).or().eq(OrderEntity::getSellerId, userId);
        }
        List<OrderEntity> orderEntities = list(queryWrapper);
        List<OrderVo> orderVoList = Lists.newArrayList();
        if(orderEntities.isEmpty()) {
            return orderVoList;
        }
        orderEntities.forEach(order -> {
            OrderVo orderVo = new OrderVo();
            LambdaQueryWrapper<OrderItemEntity> itemQueryWrapper = new LambdaQueryWrapper<>();
            itemQueryWrapper.eq(OrderItemEntity::getOrderId, order.getOrderId());
            List<OrderItemEntity> orderItems = orderItemMapper.selectList(itemQueryWrapper);
            orderVo.setOrderItems(orderItems);
            orderVoList.add(orderVo);
        });
        return orderVoList;
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {
//关闭订单之前先查询一下数据库，判断此订单状态是否已支付
        OrderEntity orderInfo = this.getOne(new LambdaQueryWrapper<>(orderEntity)
            .eq(OrderEntity::getOrderId, orderEntity.getOrderId()));

        if (orderInfo.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())) {
            //代付款状态进行关单
            OrderEntity orderUpdate = new OrderEntity();
            orderUpdate.setId(orderInfo.getId());
            orderUpdate.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(orderUpdate);

            // 发送消息给MQ
            try {
                //TODO 确保每个消息发送成功，给每个消息做好日志记录，(给数据库保存每一个详细信息)保存每个消息的详细信息
                rabbitTemplate.convertAndSend("order-event-exchange", "stock.release.stock.queue", orderInfo);
            } catch (Exception e) {
                //TODO 定期扫描数据库，重新发送失败的消息
            }
        }
    }
}
