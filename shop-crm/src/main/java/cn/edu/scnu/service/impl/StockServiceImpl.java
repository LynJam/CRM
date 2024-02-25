package cn.edu.scnu.service.impl;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.entity.OrderItemEntity;
import cn.edu.scnu.entity.StockEntity;
import cn.edu.scnu.entity.StockTaskDetailEntity;
import cn.edu.scnu.entity.StockTaskEntity;
import cn.edu.scnu.mapper.StockMapper;
import cn.edu.scnu.mapper.StockTaskDetailMapper;
import cn.edu.scnu.mapper.StockTaskMapper;
import cn.edu.scnu.service.OrderService;
import cn.edu.scnu.service.StockService;
import cn.edu.scnu.to.mq.StockDetailTo;
import cn.edu.scnu.to.mq.StockLockTo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, StockEntity> implements StockService {
    @Autowired
    private StockTaskMapper stockTaskMapper;
    @Autowired
    private StockTaskDetailMapper stockTaskDetailMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void updateStock(String productId, Integer stockNum) {
        LambdaQueryWrapper<StockEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StockEntity::getProductId, productId);
        StockEntity stockEntity = new StockEntity();
        stockEntity.setStockNum(stockNum);
        update(stockEntity, queryWrapper);
    }

    @Override
    public void save(String productId, Integer stockNum) {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setProductId(productId);
        stockEntity.setStockNum(stockNum);
        save(stockEntity);
    }

    @Override
    @Transactional
    public boolean lockStock(List<OrderItemEntity> orderItems) {
        if (orderItems == null || orderItems.size() == 0) {
            return false;
        }
        // 保存工作单信息
        StockTaskEntity stockTask = new StockTaskEntity();
        stockTask.setTaskId(new UUID(System.currentTimeMillis(), 0L).toString());
        stockTask.setOrderId(orderItems.get(0)
            .getOrderId());
        stockTask.setTaskStatus(1);
        stockTask.setCreateTime(new Date());
        stockTaskMapper.insert(stockTask);

        for (OrderItemEntity orderItem : orderItems) {
            Long count = stockMapper.lockSkuStock(orderItem.getProductId(), orderItem.getQuantity());
            if (count == 0) {
                //throw new BizException("库存不足");
                return false;
            }
            StockTaskDetailEntity stockTaskDetail = new StockTaskDetailEntity();
            stockTaskDetail.setLockStatus(1);
            stockTaskDetail.setTaskId(stockTask.getTaskId());
            stockTaskDetail.setProductId(orderItem.getProductId());
            stockTaskDetail.setProductNum(orderItem.getQuantity());
            stockTaskDetailMapper.insert(stockTaskDetail);

            StockLockTo lockTo = new StockLockTo();
            lockTo.setTaskId(stockTask.getTaskId());
            lockTo.setOrderId(stockTask.getOrderId());
            StockDetailTo stockDetailTo = new StockDetailTo();
            BeanUtils.copyProperties(stockTaskDetail, stockDetailTo);
            lockTo.setDetailTo(stockDetailTo);
            rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", lockTo);
        }
        return true;
    }

    @Override
    @Transactional
    public void unlockStock(StockLockTo stockLockTo) {
        String taskId = stockLockTo.getTaskId();
        StockDetailTo stockDetail = stockLockTo.getDetailTo();
        String orderId = stockLockTo.getOrderId();

        /**
         * 解锁
         * 1、查询数据库关于这个订单锁定库存信息
         *   有：证明库存锁定成功了
         *      解锁：订单状况
         *          1、没有这个订单，必须解锁库存
         *          2、有这个订单，不一定解锁库存
         *              订单状态：已取消：解锁库存
         *                      已支付：不能解锁库存
         */
        StockTaskDetailEntity stockTaskDetail = stockTaskDetailMapper.selectOne(new LambdaQueryWrapper<StockTaskDetailEntity>().eq(StockTaskDetailEntity::getTaskId, taskId)
            .eq(StockTaskDetailEntity::getProductId, stockDetail.getProductId()));
        if (stockTaskDetail == null || stockTaskDetail.getLockStatus() == 0) {
            return;
        }
        // TODO 查询订单状态
        OrderEntity order = orderService.getOne(new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getOrderId, orderId));
        if (order == null || order.getStatus() == 4) {
            unlockStock(taskId, stockDetail);
        }
    }

    private void unlockStock(String taskId, StockDetailTo stockDetail) {
        // 解锁库存
        stockMapper.unlockStock(stockDetail.getProductId(), stockDetail.getProductNum());
        // 修改锁定状态为 0
        StockTaskDetailEntity update = new StockTaskDetailEntity();
        update.setLockStatus(0);
        stockTaskDetailMapper.update(update, new LambdaQueryWrapper<StockTaskDetailEntity>().eq(StockTaskDetailEntity::getTaskId, taskId)
            .eq(StockTaskDetailEntity::getProductId, stockDetail.getProductId()));
    }

    @Override
    @Transactional
    public void unlockStock(String orderId) {
        StockTaskEntity stockTask = stockTaskMapper.selectOne(new LambdaQueryWrapper<StockTaskEntity>().eq(StockTaskEntity::getOrderId, orderId));
        if(stockTask == null) {
            return;
        }
        List<StockTaskDetailEntity> stockTaskDetails = stockTaskDetailMapper.selectList(new LambdaQueryWrapper<StockTaskDetailEntity>().eq(StockTaskDetailEntity::getTaskId, stockTask.getTaskId()));
        stockTaskDetails.forEach(stockTaskDetail -> {
            StockDetailTo stockDetailTo = new StockDetailTo();
            BeanUtils.copyProperties(stockTaskDetail, stockDetailTo);
            unlockStock(stockTask.getTaskId(), stockDetailTo);
        });
    }
}
