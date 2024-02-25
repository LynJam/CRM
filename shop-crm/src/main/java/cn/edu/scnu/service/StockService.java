package cn.edu.scnu.service;

import cn.edu.scnu.entity.OrderItemEntity;
import cn.edu.scnu.entity.StockEntity;
import cn.edu.scnu.to.mq.StockLockTo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface StockService extends IService<StockEntity> {
    void updateStock(String productId, Integer stockNum);

    void save(String productId, Integer stockNum);

    boolean lockStock(List<OrderItemEntity> orderItems);

    void unlockStock(StockLockTo stockLockTo);

    void unlockStock(String orderId);
}
