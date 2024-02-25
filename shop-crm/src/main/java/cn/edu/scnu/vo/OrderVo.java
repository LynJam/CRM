package cn.edu.scnu.vo;

import cn.edu.scnu.entity.OrderItemEntity;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class OrderVo {
    private String token; // 接口幂等性
    private String orderId;
    private String sellerId;
    private String buyerId;
    private Double totalAmount;
    private Double payAmount;
    /**
     * 订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】
     */
    private Byte status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddr;
    private Date paymentTime;
    private Date deliveryTime;
    private Date receiveTime;
    private Byte confirmStatus;
    private Byte deleteStatus;
    List<OrderItemEntity> orderItems;
}
