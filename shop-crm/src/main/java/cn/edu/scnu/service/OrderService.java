package cn.edu.scnu.service;

import cn.edu.scnu.entity.OrderEntity;
import cn.edu.scnu.vo.OrderVo;
import cn.edu.scnu.vo.ProductCartVo;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface OrderService extends IService<OrderEntity> {
    OrderVo cartToTrade(List<ProductCartVo> productCartVos, String userId);

    void submitOrder(OrderVo orderVo, String userId);

    List<OrderVo> listByType(int type, String userId);

    void closeOrder(OrderEntity orderEntity);

    void payOrder(String orderId);

    void sendOrder(String orderId);

    void receivedOrder(String orderId);

    void canceledOrder(String orderId);
}
